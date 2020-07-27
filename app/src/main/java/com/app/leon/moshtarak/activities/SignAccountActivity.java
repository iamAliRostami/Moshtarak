package com.app.leon.moshtarak.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Debug;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.app.leon.moshtarak.BaseItems.BaseActivity;
import com.app.leon.moshtarak.Infrastructure.IAbfaService;
import com.app.leon.moshtarak.Infrastructure.ICallback;
import com.app.leon.moshtarak.Models.DbTables.Login;
import com.app.leon.moshtarak.Models.Enums.DialogType;
import com.app.leon.moshtarak.Models.Enums.SharedReferenceKeys;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.Utils.CustomDialog;
import com.app.leon.moshtarak.Utils.CustomErrorHandling;
import com.app.leon.moshtarak.Utils.CustomProgressBar;
import com.app.leon.moshtarak.Utils.NetworkHelper;
import com.app.leon.moshtarak.Utils.SharedPreference;
import com.app.leon.moshtarak.databinding.SignAccountContent1Binding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignAccountActivity extends BaseActivity
        implements ICallback<Login> {
    SignAccountContent1Binding binding;
    String billId, mobile;
    View viewFocus;
    Context context;
    boolean change = false;
    SharedPreference sharedPreference;
    ArrayList<String> items = new ArrayList<>();

    @SuppressLint("CutPasteId")
    @Override
    protected void initialize() {
        binding = SignAccountContent1Binding.inflate(getLayoutInflater());
        View childLayout = binding.getRoot();
        ConstraintLayout parentLayout = findViewById(R.id.base_Content);
        parentLayout.addView(childLayout);
        context = this;
        sharedPreference = new SharedPreference(context);
        if (sharedPreference.checkIsNotEmpty()) {
            binding.buttonSign.setText(getResources().getString(R.string.add_account));
            binding.linearLayoutAccounts.setVisibility(View.VISIBLE);
            Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.change_account));
            change = true;
        } else {
            binding.buttonSign.setText(getResources().getString(R.string.account));
            binding.linearLayoutAccounts.setVisibility(View.GONE);
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
            binding.spinnerAccounts.setAdapter(new ArrayAdapter<>(context,
                    android.R.layout.simple_spinner_dropdown_item, items));
            binding.spinnerAccounts.setSelection(sharedPreference.getIndex());
        }
    }

    void setTextViewOnClickListener() {
        binding.textViewInfo.setOnClickListener(v -> new CustomDialog(DialogType.Blue,
                SignAccountActivity.this, getString(R.string.why_we_need),
                getString(R.string.dear_user), getString(R.string.change_account),
                getString(R.string.accepted)));
    }

    void setButtonSignClickListener() {
        binding.buttonSign.setOnClickListener(view -> {
            View viewFocus;
            boolean cancel = false;
            billId = Objects.requireNonNull(binding.editTextBillId.getText()).toString();
            while (billId.startsWith(getString(R.string.number_zero)) && billId.length() >= 6) {
                billId = billId.substring(1);
            }
            if (billId.length() < 6) {
                cancel = true;
                binding.editTextBillId.setError(getString(R.string.error_empty));
                viewFocus = binding.editTextBillId;
                viewFocus.requestFocus();
            } else if (Objects.requireNonNull(binding.editTextMobile.getText()).length() < 9) {
                cancel = true;
                binding.editTextMobile.setError(getString(R.string.error_empty));
                view = binding.editTextMobile;
                view.requestFocus();
            }
            if (!cancel) {
                for (int i = 0; i < items.size(); i++) {
                    if (billId.equals(items.get(i))) {
                        sharedPreference.putIndex(i);
                        new CustomDialog(DialogType.YellowRedirect, SignAccountActivity.this,
                                getString(R.string.change_successful), getString(R.string.dear_user), getString(R.string.change_account),
                                getString(R.string.accepted));
                        cancel = true;
                    }
                }
                if (!cancel) {
                    mobile = getString(R.string._09).concat(binding.editTextMobile.getText().toString());
                    canMatch(billId, mobile);
                }
            }
        });
    }

    void setButtonLogOutClickListener() {
        binding.buttonLogOut.setOnClickListener(view -> {
            sharedPreference.removeItem(binding.spinnerAccounts.getSelectedItemPosition());
            new CustomDialog(DialogType.YellowRedirect, SignAccountActivity.this,
                    getString(R.string.logout_successful), getString(R.string.dear_user), getString(R.string.logout),
                    getString(R.string.accepted));
        });
    }

    void setButtonContinueClickListener() {
        binding.buttonContinue.setOnClickListener(view -> {
            new CustomDialog(DialogType.YellowRedirect, SignAccountActivity.this,
                    getString(R.string.change_successful), getString(R.string.dear_user), getString(R.string.change_account),
                    getString(R.string.accepted));
            sharedPreference.putIndex(binding.spinnerAccounts.getSelectedItemPosition());
        });
    }

    private void setEditTextChangedListener() {
        binding.editTextBillId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 13) {
                    viewFocus = binding.editTextMobile;
                    viewFocus.requestFocus();
                }
            }
        });
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
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    @SuppressLint("HardwareIds")
    void canMatch(String billId, String mobile) {
        Retrofit retrofit = NetworkHelper.getInstance();
        final IAbfaService canMatch = retrofit.create(IAbfaService.class);
        String serial = String.valueOf(Build.SERIAL);
        byte[] encodeValue = Base64.encode(billId.getBytes(), Base64.DEFAULT);
        String base64 = new String(encodeValue);
        Call<Login> call = canMatch.register(new Login(billId, base64, serial, getVersionInfo(),
                String.valueOf(Build.VERSION.RELEASE), mobile, getDeviceName()));
//        HttpClientWrapperNew.callHttpAsync(call, SignAccountActivity.this, context,
//                ProgressType.SHOW.getValue());

        CustomProgressBar progressBar = new CustomProgressBar();
        progressBar.show(context, context.getString(R.string.waiting), true);

        if (isOnline(context)) {
            call.enqueue(new Callback<Login>() {
                @Override
                public void onResponse(@NonNull Call<Login> call, @NonNull Response<Login> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            execute(response.body());
                        }
                    } else {
                        errorHandlingWithResponse(response);
                    }
                    progressBar.getDialog().dismiss();
                }

                @Override
                public void onFailure(@NonNull Call<Login> call, @NonNull Throwable t) {
                    String error;
                    if (t instanceof IOException) {
                        error = context.getString(R.string.error_connection);
                    } else error = new CustomErrorHandling(context).getErrorMessageTotal(t);
                    new CustomDialog(DialogType.Red, context, error, context.getString(R.string.dear_user),
                            context.getString(R.string.error), context.getString(R.string.accepted));
                    progressBar.getDialog().dismiss();
                }
            });
        } else {
            progressBar.getDialog().dismiss();
            Toast.makeText(context, R.string.turn_internet_on, Toast.LENGTH_SHORT).show();
        }
    }

    void errorHandlingWithResponse(Response<Login> response) {
        int code = response.code();
        if (code >= 500 && code < 600) {
            new CustomDialog(DialogType.Yellow, SignAccountActivity.this,
                    context.getString(R.string.error_internal),
                    context.getString(R.string.dear_user),
                    context.getString(R.string.login),
                    context.getString(R.string.accepted));
        } else if (code == 400) {
            new CustomDialog(DialogType.Yellow, SignAccountActivity.this,
                    context.getString(R.string.error_bill_id),
                    context.getString(R.string.dear_user),
                    context.getString(R.string.login),
                    context.getString(R.string.accepted));
        } else if (code == 404) {
            new CustomDialog(DialogType.Yellow, SignAccountActivity.this,
                    context.getString(R.string.error_change_server),
                    context.getString(R.string.dear_user),
                    context.getString(R.string.login),
                    context.getString(R.string.accepted));
        } else if (code >= 400 && code < 500) {
            new CustomDialog(DialogType.Yellow, SignAccountActivity.this,
                    context.getString(R.string.error_not_update),
                    context.getString(R.string.dear_user),
                    context.getString(R.string.login),
                    context.getString(R.string.accepted));
        } else {
            new CustomDialog(DialogType.Yellow, SignAccountActivity.this,
                    context.getString(R.string.error_other),
                    context.getString(R.string.dear_user),
                    context.getString(R.string.login),
                    context.getString(R.string.accepted));
        }
    }

    @Override
    public void execute(Login login) {
        if (login.getApiKey().isEmpty()) {
            new CustomDialog(DialogType.Red, SignAccountActivity.this, login.getMessage(),
                    getString(R.string.dear_user), getString(R.string.login),
                    getString(R.string.accepted));
        } else {
            sharedPreference.putDataArray(mobile, billId, login.getApiKey());
            new CustomDialog(DialogType.GreenRedirect, SignAccountActivity.this,
                    getString(R.string.you_are_signed), getString(R.string.dear_user), getString(R.string.login), getString(R.string.accepted));
        }
    }

    private boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return Objects.requireNonNull(cm).getActiveNetworkInfo() != null &&
                Objects.requireNonNull(cm.getActiveNetworkInfo()).isConnectedOrConnecting();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Runtime.getRuntime().totalMemory();
        Runtime.getRuntime().freeMemory();
        Runtime.getRuntime().maxMemory();
        Debug.getNativeHeapAllocatedSize();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().totalMemory();
        Runtime.getRuntime().freeMemory();
        Runtime.getRuntime().maxMemory();
        Debug.getNativeHeapAllocatedSize();
    }
}
