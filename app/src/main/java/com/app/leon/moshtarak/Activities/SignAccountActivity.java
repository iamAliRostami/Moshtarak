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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.app.leon.moshtarak.BaseItems.BaseActivity;
import com.app.leon.moshtarak.Infrastructure.IAbfaService;
import com.app.leon.moshtarak.Infrastructure.ICallback;
import com.app.leon.moshtarak.Models.DbTables.Login;
import com.app.leon.moshtarak.Models.Enums.DialogType;
import com.app.leon.moshtarak.Models.Enums.ProgressType;
import com.app.leon.moshtarak.Models.Enums.SharedReferenceKeys;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.Utils.CustomDialog;
import com.app.leon.moshtarak.Utils.HttpClientWrapper;
import com.app.leon.moshtarak.Utils.NetworkHelper;
import com.app.leon.moshtarak.Utils.SharedPreference;

import java.util.ArrayList;
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

    @BindView(R.id.buttonContinue)
    Button buttonContinue;
    @BindView(R.id.textViewInfo)
    TextView textViewInfo;
    @BindView(R.id.linearLayoutAccounts)
    LinearLayout linearLayoutAccounts;
    @BindView(R.id.spinnerAccounts)
    Spinner spinnerAccounts;
    String billId, mobile;//, nationNumber, account;
    View viewFocus;
    Context context;
    boolean change = false;
    SharedPreference sharedPreference;
    ArrayList<String> items = new ArrayList<>();

    @Override
    protected void initialize() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View childLayout = Objects.requireNonNull(inflater).inflate(R.layout.sign_account_content, findViewById(R.id.sign_account_activity));
        @SuppressLint("CutPasteId") ConstraintLayout parentLayout = findViewById(R.id.base_Content);
        parentLayout.addView(childLayout);
        ButterKnife.bind(this);
        context = this;
        sharedPreference = new SharedPreference(context);
        if (sharedPreference.checkIsNotEmpty()) {
            buttonSign.setText(getResources().getString(R.string.add_account));
            linearLayoutAccounts.setVisibility(View.VISIBLE);
            Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.change_account));
            change = true;
        } else {
            buttonSign.setText(getResources().getString(R.string.account));
            linearLayoutAccounts.setVisibility(View.GONE);
            Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.account));
        }
        setButtonLogOutClickListener();
        setButtonContinueClickListener();
        setButtonSignClickListener();
        setEditTextChangedListener();
        setTextViewOnClickListener();
        fillSpinner();
    }

    void fillSpinner() {
        if (sharedPreference.getLength() > 0) {
            items = sharedPreference.getArrayList(SharedReferenceKeys.BILL_ID.getValue());
        }
        if (items.size() > 0) {
            spinnerAccounts.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item,
                    items));
            spinnerAccounts.setSelection(sharedPreference.getIndex());
        }
    }

    void setTextViewOnClickListener() {
        textViewInfo.setOnClickListener(v -> new CustomDialog(DialogType.Blue, SignAccountActivity.this,
                "اطلاعات کاربری جهت تطبیق با اطلاعات ثبت شده در سامانه و جلوگیری از هر گونه سوء استفاده احتمالی از اطلاعات شماست.\n" +
                        "این اطلاعات به صورت محرمانه نزد ما خواهد ماند.\n", getString(R.string.dear_user), getString(R.string.change_account),
                getString(R.string.accepted)));
//        textViewInfo.setOnClickListener(v -> new LovelyInfoDialog(context)
//                .setTopColorRes(R.color.blue5)
//                .setNotShowAgainOptionEnabled(0)
//                .setNotShowAgainOptionChecked(true)
//                .setTitle(R.string.dear_user)
//                .setMessage("اطلاعات کاربری جهت تطبیق با اطلاعات ثبت شده در سامانه و جلوگیری از هر گونه سوء استفاده احتمالی از اطلاعات شماست.\n" +
//                        "این اطلاعات به صورت محرمانه نزد ما خواهد ماند.\n")
//                .show());
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
            sharedPreference.removeItem(spinnerAccounts.getSelectedItemPosition());
            new CustomDialog(DialogType.YellowRedirect, SignAccountActivity.this,
                    getString(R.string.logout_successful), getString(R.string.dear_user), getString(R.string.logout),
                    getString(R.string.accepted));
        });
    }

    void setButtonContinueClickListener() {
        buttonContinue.setOnClickListener(view -> {
            new CustomDialog(DialogType.YellowRedirect, SignAccountActivity.this,
                    getString(R.string.change_successful), getString(R.string.dear_user), getString(R.string.change_account),
                    getString(R.string.accepted));
            sharedPreference.putIndex(spinnerAccounts.getSelectedItemPosition());
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
                    getString(R.string.dear_user), getString(R.string.login),
                    getString(R.string.accepted));

        } else {
            sharedPreference.putDataArray(mobile, billId, login.getApiKey());
            new CustomDialog(DialogType.GreenRedirect, SignAccountActivity.this, getString(R.string.you_are_signed),
                    getString(R.string.dear_user), getString(R.string.login),
                    getString(R.string.accepted));
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
