package com.app.leon.moshtarak.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.app.leon.moshtarak.BaseItems.BaseActivity;
import com.app.leon.moshtarak.Infrastructure.IAbfaService;
import com.app.leon.moshtarak.Infrastructure.ICallback;
import com.app.leon.moshtarak.Models.DbTables.Login;
import com.app.leon.moshtarak.Models.Enums.DialogType;
import com.app.leon.moshtarak.Models.Enums.ProgressType;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.Utils.CustomDialog;
import com.app.leon.moshtarak.Utils.HttpClientWrapper;
import com.app.leon.moshtarak.Utils.NetworkHelper;
import com.app.leon.moshtarak.Utils.SharedPreference;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Retrofit;

public class SignAccountActivity extends BaseActivity
        implements ICallback<Login> {
    @BindView(R.id.editTextBillId)
    EditText editTextBillId;
    @BindView(R.id.editTextAccount)
    EditText editTextAccount;
    @BindView(R.id.editTextNationNumber)
    EditText editTextNationNumber;
    @BindView(R.id.editTextMobile)
    EditText editTextMobile;
    @BindView(R.id.buttonSign)
    Button buttonSign;
    @BindView(R.id.buttonLogOut)
    Button buttonLogOut;
    String billId, account, mobile, nationNumber;
    View viewFocus;
    Context context;
    boolean change = false;

    @Override
    protected void initialize() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View childLayout = Objects.requireNonNull(inflater).inflate(R.layout.sign_account_content, findViewById(R.id.sign_account_activity));
        @SuppressLint("CutPasteId") ConstraintLayout parentLayout = findViewById(R.id.base_Content);
        parentLayout.addView(childLayout);
        ButterKnife.bind(this);
        context = this;

        SharedPreference sharedPreference = new SharedPreference(context);
        if (sharedPreference.checkIsNotEmpty()) {
            buttonSign.setText(getResources().getString(R.string.change_account));
            buttonLogOut.setVisibility(View.VISIBLE);
//            Objects.requireNonNull(getActionBar()).setTitle(getString(R.string.change_account));
            Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.change_account));

            change = true;
        } else {
            buttonSign.setText(getResources().getString(R.string.account));
            buttonLogOut.setVisibility(View.GONE);
//            Objects.requireNonNull(getActionBar()).setTitle(getString(R.string.account));
            Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.account));
        }
        setButtonLogOutClickListener();
        setButtonSignClickListener();
        SetEditTextChangedListener();
    }

    void setButtonSignClickListener() {
        buttonSign.setOnClickListener(view -> {
            View viewFocus;
            boolean cancel = false;
            if (editTextBillId.getText().length() < 6) {
                cancel = true;
                editTextBillId.setError(getString(R.string.error_empty));
                viewFocus = editTextBillId;
                viewFocus.requestFocus();
            }
            if (!cancel && editTextAccount.getText().length() < 7) {
                cancel = true;
                editTextAccount.setError(getString(R.string.error_empty));
                viewFocus = editTextAccount;
                viewFocus.requestFocus();
            }
            if (editTextNationNumber.getText().length() < 10) {
                view = editTextNationNumber;
                view.requestFocus();
                editTextNationNumber.setError(getString(R.string.nation_number_error));
                cancel = true;
            }
            if (editTextMobile.getText().length() < 9) {
                view = editTextMobile;
                view.requestFocus();
                cancel = true;
            }
            if (!cancel) {
                account = editTextAccount.getText().toString();
                billId = editTextBillId.getText().toString();
                mobile = "09".concat(editTextMobile.getText().toString());
                nationNumber = editTextNationNumber.getText().toString();
                canMatch(billId, account, mobile, nationNumber);
            }
        });
    }

    void setButtonLogOutClickListener() {
        buttonLogOut.setOnClickListener(view -> {
            SharedPreference sharedPreference = new SharedPreference(SignAccountActivity.this);
            sharedPreference.putData("", "", "", "");
            new CustomDialog(DialogType.YellowRedirect, SignAccountActivity.this,
                    getString(R.string.logout_successful), getString(R.string.dear_user), getString(R.string.logout),
                    getString(R.string.accepted));
            buttonSign.setText(getResources().getString(R.string.account));
            buttonLogOut.setVisibility(View.GONE);
            change = false;
        });
    }

    private void SetEditTextChangedListener() {
        editTextBillId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 13) {
                    viewFocus = editTextAccount;
                    viewFocus.requestFocus();
                }
            }
        });
        editTextAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 14) {
                    viewFocus = editTextNationNumber;
                    viewFocus.requestFocus();
                }
            }
        });

        editTextNationNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 10) {
                    viewFocus = editTextMobile;
                    viewFocus.requestFocus();
                }
            }
        });
    }

    void canMatch(String billId, String account, String mobile, String nationNumber) {
        Retrofit retrofit = NetworkHelper.getInstance();
        final IAbfaService canMatch = retrofit.create(IAbfaService.class);
        @SuppressLint("HardwareIds") String serial = String.valueOf(Build.SERIAL);
        Call<Login> call = canMatch.register(new Login(billId, account, nationNumber, serial,
                getVersionInfo(), String.valueOf(Build.VERSION.RELEASE), mobile, getDeviceName()));
        HttpClientWrapper.callHttpAsync(call, SignAccountActivity.this, context, ProgressType.SHOW.getValue());
    }

    @Override
    public void execute(Login login) {
        if (login.getApiKey().isEmpty()) {
            new CustomDialog(DialogType.Red, SignAccountActivity.this, login.getMessage(),
                    //getString(R.string.error_is_not_match),
                    getString(R.string.dear_user), getString(R.string.login),
                    getString(R.string.accepted));

        } else {
            SharedPreference sharedPreference = new SharedPreference(SignAccountActivity.this);
            sharedPreference.putData(account, billId, mobile, login.getApiKey());
            new CustomDialog(DialogType.GreenRedirect, SignAccountActivity.this, getString(R.string.you_are_signed),
                    getString(R.string.dear_user), getString(R.string.login),
                    getString(R.string.accepted));
            if (!change) {
                buttonSign.setText(getResources().getString(R.string.change_account));
                buttonLogOut.setVisibility(View.VISIBLE);
                change = true;
            }
            editTextAccount.setText("");
            editTextBillId.setText("");
            editTextMobile.setText("");
            editTextNationNumber.setText("");
        }
    }

    private String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    private String getVersionInfo() {
        String versionName = "";
//        int versionCode = -1;
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
//            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }
}
