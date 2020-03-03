package com.app.leon.moshtarak.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import com.app.leon.moshtarak.Utils.LovelyInfoDialog;
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
    @BindView(R.id.editTextMobile)
    EditText editTextMobile;
    @BindView(R.id.buttonSign)
    Button buttonSign;
    @BindView(R.id.buttonLogOut)
    Button buttonLogOut;
    @BindView(R.id.textViewInfo)
    TextView textViewInfo;
    String billId, mobile;//, nationNumber, account;
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
            Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.change_account));
            change = true;
        } else {
            buttonSign.setText(getResources().getString(R.string.account));
            buttonLogOut.setVisibility(View.GONE);
            Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.account));
        }
        setButtonLogOutClickListener();
        setButtonSignClickListener();
        setEditTextChangedListener();
        setTextViewOnClickListener();
    }

    void setTextViewOnClickListener() {
        textViewInfo.setOnClickListener(v -> new LovelyInfoDialog(context)
                .setTopColorRes(R.color.grayDark)
                .setNotShowAgainOptionEnabled(0)
                .setNotShowAgainOptionChecked(true)
                .setTitle(R.string.dear_user)
                .setMessage("اطلاعات کاربری جهت تطبیق با اطلاعات ثبت شده در سامانه و جلوگیری از هر گونه سوء استفاده احتمالی از اطلاعات شماست.\n" +
                        "این اطلاعات به صورت محرمانه نزد ما خواهد ماند.\n")
                .show());

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
            if (editTextMobile.getText().length() < 9) {
                view = editTextMobile;
                view.requestFocus();
                cancel = true;
            }
            if (!cancel) {
                billId = editTextBillId.getText().toString();
                mobile = "09".concat(editTextMobile.getText().toString());
                canMatch(billId, mobile);
            }
        });
    }

    void setButtonLogOutClickListener() {
        buttonLogOut.setOnClickListener(view -> {
            SharedPreference sharedPreference = new SharedPreference(SignAccountActivity.this);
            sharedPreference.putData("", "", "");
            new CustomDialog(DialogType.YellowRedirect, SignAccountActivity.this,
                    getString(R.string.logout_successful), getString(R.string.dear_user), getString(R.string.logout),
                    getString(R.string.accepted));
            buttonSign.setText(getResources().getString(R.string.account));
            buttonLogOut.setVisibility(View.GONE);
            change = false;
        });
    }

    private void setEditTextChangedListener() {
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
                    viewFocus = editTextMobile;
                    viewFocus.requestFocus();
                }
            }
        });
    }

    void canMatch(String billId, String mobile) {
        Retrofit retrofit = NetworkHelper.getInstance();
        final IAbfaService canMatch = retrofit.create(IAbfaService.class);
        @SuppressLint("HardwareIds") String serial = String.valueOf(Build.SERIAL);

        byte[] encodeValue = Base64.encode(billId.getBytes(), Base64.DEFAULT);
        String base64 = new String(encodeValue);

        Call<Login> call = canMatch.register(new Login(billId, base64, serial, getVersionInfo(),
                String.valueOf(Build.VERSION.RELEASE), mobile, getDeviceName()));
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
            sharedPreference.putData(billId, mobile, login.getApiKey());
            new CustomDialog(DialogType.GreenRedirect, SignAccountActivity.this, getString(R.string.you_are_signed),
                    getString(R.string.dear_user), getString(R.string.login),
                    getString(R.string.accepted));
            if (!change) {
                buttonSign.setText(getResources().getString(R.string.change_account));
                buttonLogOut.setVisibility(View.VISIBLE);
                change = true;
            }
            editTextBillId.setText("");
            editTextMobile.setText("");
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
