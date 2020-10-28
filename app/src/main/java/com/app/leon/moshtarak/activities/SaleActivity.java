package com.app.leon.moshtarak.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Debug;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.app.leon.moshtarak.BaseItems.BaseActivity;
import com.app.leon.moshtarak.Infrastructure.IAbfaService;
import com.app.leon.moshtarak.Infrastructure.ICallback;
import com.app.leon.moshtarak.Infrastructure.ICallbackError;
import com.app.leon.moshtarak.Infrastructure.ICallbackIncomplete;
import com.app.leon.moshtarak.Models.DbTables.RegisterNewDto;
import com.app.leon.moshtarak.Models.Enums.DialogType;
import com.app.leon.moshtarak.Models.Enums.ProgressType;
import com.app.leon.moshtarak.Models.Enums.SharedReferenceKeys;
import com.app.leon.moshtarak.Models.InterCommunation.SimpleMessage;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.Utils.CustomDialog;
import com.app.leon.moshtarak.Utils.CustomErrorHandlingNew;
import com.app.leon.moshtarak.Utils.HttpClientWrapper;
import com.app.leon.moshtarak.Utils.NetworkHelper;
import com.app.leon.moshtarak.Utils.SharedPreference;
import com.app.leon.moshtarak.databinding.SaleContentBinding;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SaleActivity extends BaseActivity {
    SaleContentBinding binding;
    Context context;
    RegisterNewDto registerNewDto;
    SharedPreference sharedPreference;
    View viewFocus;


    @SuppressLint("CutPasteId")
    @Override
    protected void initialize() {
        binding = SaleContentBinding.inflate(getLayoutInflater());
        View childLayout = binding.getRoot();
        ConstraintLayout parentLayout = findViewById(R.id.base_Content);
        parentLayout.addView(childLayout);
        context = this;
        sharedPreference = new SharedPreference(context);

        if (sharedPreference.checkIsNotEmpty()) {
            binding.editTextMobile.setText(sharedPreference.getArrayList(
                    SharedReferenceKeys.MOBILE_NUMBER.getValue()).
                    get(sharedPreference.getIndex()).replaceFirst(
                    getString(R.string._09), ""));
        }
        View view = binding.radioGroupService;
        view.requestFocus();
        binding.radioButtonService1.setChecked(true);
        setButtonNavigationOnClickListener();
        SetEditTextChangedListener();
    }

    private void sendRequest() {
        Retrofit retrofit = NetworkHelper.getInstance();
        final IAbfaService SendRegisterRequest = retrofit.create(IAbfaService.class);
        registerNewDto = new RegisterNewDto(binding.editTextBillId.getText().toString(),
                binding.editTextName.getText().toString(),
                binding.editTextFamily.getText().toString(),
                binding.editTextFatherName.getText().toString(),
                binding.editTextNationNumber.getText().toString(),
                getString(R.string._09).concat(binding.editTextMobile.getText().toString()),
                binding.editTextPhoneNumber.getText().toString(),
                binding.editTextAddress.getText().toString(),
                binding.editTextPostalCode.getText().toString(), "4");
        if (binding.radioButtonService1.isChecked())
            registerNewDto.setSelectedServices(new String[]{"1"});
        else if (binding.radioButtonService2.isChecked())
            registerNewDto.setSelectedServices(new String[]{"1", "2"});
        Call<SimpleMessage> call = SendRegisterRequest.registerNew(registerNewDto);
        SaleRequest saleRequest = new SaleRequest();
        SaleRequestIncomplete incomplete = new SaleRequestIncomplete();
        GetError error = new GetError();
        HttpClientWrapper.callHttpAsync(call, ProgressType.SHOW.getValue(), context, saleRequest, incomplete, error);
    }

    private void setButtonNavigationOnClickListener() {
        binding.buttonNavigation.setOnClickListener(view1 -> {
            View view;
            boolean cancel = false;
            if (binding.editTextAddress.getText().length() < 1) {
                binding.editTextAddress.setError(getString(R.string.error_empty));
                view = binding.editTextAddress;
                view.requestFocus();
                cancel = true;
            }
            if (binding.editTextBillId.getText().length() < 6) {
                binding.editTextBillId.setError(getString(R.string.error_empty));
                view = binding.editTextBillId;
                view.requestFocus();
                cancel = true;
            }
            if (binding.editTextPostalCode.getText().length() < 10) {
                binding.editTextPostalCode.setError(getString(R.string.error_empty));
                view = binding.editTextPostalCode;
                view.requestFocus();
                cancel = true;
            }
            if (binding.editTextMobile.getText().length() < 9) {
                binding.editTextMobile.setError(getString(R.string.error_empty));
                view = binding.editTextMobile;
                view.requestFocus();
                cancel = true;
            }
            if (binding.editTextPhoneNumber.getText().length() < 8) {
                binding.editTextPhoneNumber.setError(getString(R.string.error_empty));
                view = binding.editTextPhoneNumber;
                view.requestFocus();
                cancel = true;
            }
            if (binding.editTextNationNumber.getText().length() < 10) {
                binding.editTextNationNumber.setError(getString(R.string.error_empty));
                view = binding.editTextNationNumber;
                view.requestFocus();
                cancel = true;
            }
            if (binding.editTextFatherName.getText().length() < 1) {
                binding.editTextFatherName.setError(getString(R.string.error_empty));
                view = binding.editTextFatherName;
                view.requestFocus();
                cancel = true;
            }
            if (binding.editTextFamily.getText().length() < 1) {
                binding.editTextFamily.setError(getString(R.string.error_empty));
                view = binding.editTextFamily;
                view.requestFocus();
                cancel = true;
            }
            if (binding.editTextName.getText().length() < 1) {
                binding.editTextName.setError(getString(R.string.error_empty));
                view = binding.editTextName;
                view.requestFocus();
                cancel = true;
            }
            if (!cancel) {
                sendRequest();
            }
        });
    }

    private void setEditTextNationNumberChangedListener() {
        binding.editTextNationNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 10) {
                    viewFocus = binding.editTextPhoneNumber;
                    viewFocus.requestFocus();
                }
            }
        });
    }

    private void setEditTextPhoneNumberChangedListener() {
        binding.editTextPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 8) {
                    viewFocus = binding.editTextMobile;
                    viewFocus.requestFocus();
                }
            }
        });
    }

    private void setEditTextMobileChangedListener() {
        binding.editTextMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 9) {
                    viewFocus = binding.editTextBillId;
                    viewFocus.requestFocus();
                }
            }
        });
    }

    private void setEditTextBillIdChangedListener() {
        binding.editTextPostalCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 10) {
                    viewFocus = binding.editTextAddress;
                    viewFocus.requestFocus();
                }
            }
        });
    }

    private void setEditTextAddressChangedListener() {
    }

    protected void SetEditTextChangedListener() {
        setEditTextPhoneNumberChangedListener();
        setEditTextNationNumberChangedListener();
        setEditTextMobileChangedListener();
        setEditTextBillIdChangedListener();
        setEditTextAddressChangedListener();
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

    class SaleRequest implements ICallback<SimpleMessage> {
        @Override
        public void execute(SimpleMessage simpleMessage) {
            new CustomDialog(DialogType.GreenRedirect, context, simpleMessage.getMessage(), context.getString(R.string.dear_user),
                    context.getString(R.string.buy), context.getString(R.string.accepted));
        }
    }

    class SaleRequestIncomplete implements ICallbackIncomplete<SimpleMessage> {
        @Override
        public void executeIncomplete(Response<SimpleMessage> response) {
            CustomErrorHandlingNew customErrorHandlingNew = new CustomErrorHandlingNew(context);
            String error = customErrorHandlingNew.getErrorMessageDefault(response);
            if (response.code() == 400)
                error = getString(R.string.error_billid_incorrect);
            new CustomDialog(DialogType.Yellow, SaleActivity.this, error,
                    SaleActivity.this.getString(R.string.dear_user),
                    SaleActivity.this.getString(R.string.login),
                    SaleActivity.this.getString(R.string.accepted));
        }
    }

    class GetError implements ICallbackError {
        @Override
        public void executeError(Throwable t) {
            CustomErrorHandlingNew customErrorHandlingNew = new CustomErrorHandlingNew(context);
            String error = customErrorHandlingNew.getErrorMessageTotal(t);
            new CustomDialog(DialogType.YellowRedirect, SaleActivity.this, error,
                    SaleActivity.this.getString(R.string.dear_user),
                    SaleActivity.this.getString(R.string.login),
                    SaleActivity.this.getString(R.string.accepted));
        }
    }
}
