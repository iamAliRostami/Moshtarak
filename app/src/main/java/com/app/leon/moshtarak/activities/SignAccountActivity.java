package com.app.leon.moshtarak.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Debug;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.app.leon.moshtarak.BaseItems.BaseActivity;
import com.app.leon.moshtarak.Infrastructure.IAbfaService;
import com.app.leon.moshtarak.Infrastructure.ICallback;
import com.app.leon.moshtarak.Infrastructure.ICallbackError;
import com.app.leon.moshtarak.Infrastructure.ICallbackIncomplete;
import com.app.leon.moshtarak.Models.DbTables.Login;
import com.app.leon.moshtarak.Models.Enums.DialogType;
import com.app.leon.moshtarak.Models.Enums.ProgressType;
import com.app.leon.moshtarak.Models.Enums.SharedReferenceKeys;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.Utils.CustomDialog;
import com.app.leon.moshtarak.Utils.CustomErrorHandlingNew;
import com.app.leon.moshtarak.Utils.HttpClientWrapper;
import com.app.leon.moshtarak.Utils.NetworkHelper;
import com.app.leon.moshtarak.Utils.SharedPreference;
import com.app.leon.moshtarak.databinding.SignAccountContentBinding;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignAccountActivity extends BaseActivity {
    SignAccountContentBinding binding;
    String billId, mobile;
    View viewFocus;
    Context context;
    boolean change = false;
    SharedPreference sharedPreference;
    ArrayList<String> items = new ArrayList<>();
    ArrayList<String> itemsBillId = new ArrayList<>();

    @SuppressLint("CutPasteId")
    @Override
    protected void initialize() {
        binding = SignAccountContentBinding.inflate(getLayoutInflater());
        View childLayout = binding.getRoot();
        ConstraintLayout parentLayout = findViewById(R.id.base_Content);
        parentLayout.addView(childLayout);
        context = this;
        sharedPreference = new SharedPreference(context);

        if (sharedPreference.checkIsNotEmpty()) {
            Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.change_account));
            binding.buttonSign.setText(getResources().getString(R.string.add_account));
            binding.linearLayoutChange.setVisibility(View.GONE);
//            binding.linearLayoutAccounts.setVisibility(View.VISIBLE);
            setLinearLayoutAddOnClickListener();
        } else {
            Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.account));
            binding.buttonSign.setText(getResources().getString(R.string.login));
            binding.linearLayoutAccounts.setVisibility(View.GONE);
            binding.imageViewAdd.setVisibility(View.GONE);
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
            items = sharedPreference.getArrayList(SharedReferenceKeys.NAME.getValue());
            itemsBillId = sharedPreference.getArrayList(SharedReferenceKeys.BILL_ID.getValue());
        }
        if (items.size() > 0) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context,
                    R.layout.register_item, android.R.id.text1, items);
            arrayAdapter.setDropDownViewResource(R.layout.register_item_popup);
            binding.spinnerAccounts.setAdapter(arrayAdapter);
            binding.spinnerAccounts.setSelection(sharedPreference.getIndex());
        }
    }

    void setLinearLayoutAddOnClickListener() {
        binding.linearLayoutAdd.setOnClickListener(view -> {
            Transition transition = new Slide(Gravity.BOTTOM);
            transition.setDuration(600);
            transition.addTarget(binding.linearLayoutChange);
            TransitionManager.beginDelayedTransition(binding.linearLayoutTotal, transition);
            binding.linearLayoutChange.setVisibility(change ? View.GONE : View.VISIBLE);
            binding.imageViewAdd.animate().alpha(0.0f);
            binding.imageViewAdd.setImageDrawable(change ?
                    context.getResources().getDrawable(R.drawable.img_plus_1) :
                    context.getResources().getDrawable(R.drawable.img_minus_1));
            binding.imageViewAdd.animate().alpha(1.0f);
            change = !change;
        });
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
                for (int i = 0; i < itemsBillId.size(); i++) {
                    if (billId.equals(itemsBillId.get(i))) {
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
        Register register = new Register();
        RegisterIncomplete incomplete = new RegisterIncomplete();
        GetError error = new GetError();
        HttpClientWrapper.callHttpAsync(call, ProgressType.SHOW.getValue(), context, register,
                incomplete, error);
    }

    class Register implements ICallback<Login> {
        @Override
        public void execute(Login login) {
            if (login.getApiKey().isEmpty()) {
                new CustomDialog(DialogType.Red, SignAccountActivity.this, login.getMessage(),
                        getString(R.string.dear_user), getString(R.string.login),
                        getString(R.string.accepted));
            } else {
                sharedPreference.putDataArray(mobile, billId, login.getApiKey(), login.getNameAndFamily());
                new CustomDialog(DialogType.GreenRedirect, SignAccountActivity.this,
                        getString(R.string.you_are_signed), getString(R.string.dear_user), getString(R.string.login), getString(R.string.accepted));
            }
        }
    }

    class RegisterIncomplete implements ICallbackIncomplete<Login> {
        @Override
        public void executeIncomplete(Response<Login> response) {
            CustomErrorHandlingNew customErrorHandlingNew = new CustomErrorHandlingNew(context);
            String error = customErrorHandlingNew.getErrorMessageDefault(response);
            if (response.code() == 400) {
                error = getString(R.string.error_bill_id);
            }
            new CustomDialog(DialogType.Yellow, SignAccountActivity.this, error,
                    SignAccountActivity.this.getString(R.string.dear_user),
                    SignAccountActivity.this.getString(R.string.login),
                    SignAccountActivity.this.getString(R.string.accepted));
        }
    }

    class GetError implements ICallbackError {
        @Override
        public void executeError(Throwable t) {
            CustomErrorHandlingNew customErrorHandlingNew = new CustomErrorHandlingNew(context);
            String error = customErrorHandlingNew.getErrorMessageTotal(t);
            new CustomDialog(DialogType.Yellow, SignAccountActivity.this, error,
                    SignAccountActivity.this.getString(R.string.dear_user),
                    SignAccountActivity.this.getString(R.string.login),
                    SignAccountActivity.this.getString(R.string.accepted));
        }

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
