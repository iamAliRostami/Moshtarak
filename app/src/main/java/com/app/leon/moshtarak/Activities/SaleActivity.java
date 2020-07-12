package com.app.leon.moshtarak.Activities;

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
import com.app.leon.moshtarak.Models.DbTables.RegisterNewDto;
import com.app.leon.moshtarak.Models.Enums.DialogType;
import com.app.leon.moshtarak.Models.Enums.ProgressType;
import com.app.leon.moshtarak.Models.Enums.SharedReferenceKeys;
import com.app.leon.moshtarak.Models.InterCommunation.SimpleMessage;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.Utils.CustomDialog;
import com.app.leon.moshtarak.Utils.HttpClientWrapperNew;
import com.app.leon.moshtarak.Utils.NetworkHelper;
import com.app.leon.moshtarak.Utils.SharedPreference;
import com.app.leon.moshtarak.databinding.SaleContentBinding;

import retrofit2.Call;
import retrofit2.Retrofit;

public class SaleActivity extends BaseActivity
        implements ICallback<SimpleMessage> {
    SaleContentBinding binding;
    Context context;
    RegisterNewDto registerNewDto;
    SharedPreference sharedPreference;
    View viewFocus;


    @Override
    protected void initialize() {
        binding = SaleContentBinding.inflate(getLayoutInflater());
        View childLayout = binding.getRoot();
        @SuppressLint("CutPasteId") ConstraintLayout parentLayout = findViewById(R.id.base_Content);
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
        setEditTextOnFocusChangeListener();
        setButtonNavigationOnClickListener();
        SetEditTextChangedListener();
    }

    @Override
    public void execute(SimpleMessage simpleMessage) {
        new CustomDialog(DialogType.GreenRedirect, context, simpleMessage.getMessage(), context.getString(R.string.dear_user),
                context.getString(R.string.buy), context.getString(R.string.accepted));
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
        HttpClientWrapperNew.callHttpAsync(call, SaleActivity.this, context,
                ProgressType.SHOW.getValue());
    }

    private void setButtonNavigationOnClickListener() {
        binding.buttonNavigation.setOnClickListener(view1 -> {
            View view;
            boolean cancel = false;
            if (binding.editTextAddress.getText().length() < 1) {
                view = binding.editTextAddress;
                view.requestFocus();
                cancel = true;
            }
            if (binding.editTextBillId.getText().length() < 6) {
                view = binding.editTextBillId;
                view.requestFocus();
                cancel = true;
            }
            if (binding.editTextPostalCode.getText().length() < 10) {
                view = binding.editTextPostalCode;
                view.requestFocus();
                cancel = true;
            }
            if (binding.editTextMobile.getText().length() < 9) {
                view = binding.editTextMobile;
                view.requestFocus();
                cancel = true;
            }
            if (binding.editTextPhoneNumber.getText().length() < 8) {
                view = binding.editTextPhoneNumber;
                view.requestFocus();
                cancel = true;
            }
            if (binding.editTextNationNumber.getText().length() < 10) {
                view = binding.editTextNationNumber;
                view.requestFocus();
                cancel = true;
            }
            if (binding.editTextFatherName.getText().length() < 1) {
                view = binding.editTextFatherName;
                view.requestFocus();
                cancel = true;
            }
            if (binding.editTextFamily.getText().length() < 1) {
                view = binding.editTextFamily;
                view.requestFocus();
                cancel = true;
            }
            if (binding.editTextName.getText().length() < 1) {
                view = binding.editTextName;
                view.requestFocus();
                cancel = true;
            }
            if (!cancel) {
                sendRequest();
            }
        });
    }

    private void setEditTextOnFocusChangeListener() {
        setEditTextNameOnFocusChangeListener();
        setEditTextFamilyOnFocusChangeListener();
        setEditTextFatherNameOnFocusChangeListener();
        setEditTextNationNumberOnFocusChangeListener();
        setEditTextPhoneNumberOnFocusChangeListener();
        setEditTextMobileOnFocusChangeListener();
        setEditTextPostalCodeOnFocusChangeListener();
        setEditTextBillIdOnFocusChangeListener();
        setEditTextAddressOnFocusChangeListener();
    }

    private void setEditTextNameOnFocusChangeListener() {
        binding.editTextName.setOnFocusChangeListener((view1, b) -> {
            if (b) {
                binding.editTextName.setBackground(getResources().getDrawable(R.drawable.border_orange_));
                binding.linearLayoutName.setBackground(getResources().getDrawable(R.drawable.border_orange_));
                binding.textViewName.setTextColor(getResources().getColor(R.color.orange2));
            } else {
                binding.editTextName.setBackground(getResources().getDrawable(R.drawable.border_gray_2));
                binding.linearLayoutName.setBackground(getResources().getDrawable(R.drawable.border_gray_2));
                binding.textViewName.setTextColor(getResources().getColor(R.color.black1));
            }
        });
    }

    private void setEditTextFamilyOnFocusChangeListener() {
        binding.editTextFamily.setOnFocusChangeListener((view1, b) -> {
            if (b) {
                binding.editTextFamily.setBackground(getResources().getDrawable(R.drawable.border_orange_));
                binding.linearLayoutFamily.setBackground(getResources().getDrawable(R.drawable.border_orange_));
                binding.textViewFamily.setTextColor(getResources().getColor(R.color.orange2));
            } else {
                binding.editTextFamily.setBackground(getResources().getDrawable(R.drawable.border_gray_2));
                binding.linearLayoutFamily.setBackground(getResources().getDrawable(R.drawable.border_gray_2));
                binding.textViewFamily.setTextColor(getResources().getColor(R.color.black1));
            }
        });
    }

    private void setEditTextFatherNameOnFocusChangeListener() {
        binding.editTextFatherName.setOnFocusChangeListener((view1, b) -> {
            if (b) {
                binding.editTextFatherName.setBackground(getResources().getDrawable(R.drawable.border_orange_));
                binding.linearLayoutFatherName.setBackground(getResources().getDrawable(R.drawable.border_orange_));
                binding.textViewFatherName.setTextColor(getResources().getColor(R.color.orange2));
            } else {
                binding.editTextFatherName.setBackground(getResources().getDrawable(R.drawable.border_gray_2));
                binding.linearLayoutFatherName.setBackground(getResources().getDrawable(R.drawable.border_gray_2));
                binding.textViewFatherName.setTextColor(getResources().getColor(R.color.black1));
            }
        });
    }

    private void setEditTextNationNumberOnFocusChangeListener() {
        binding.editTextNationNumber.setOnFocusChangeListener((view1, b) -> {
            if (b) {
                binding.editTextNationNumber.setBackground(getResources().getDrawable(R.drawable.border_orange_));
                binding.linearLayoutNationNumber.setBackground(getResources().getDrawable(R.drawable.border_orange_));
                binding.textViewNationNumber.setTextColor(getResources().getColor(R.color.orange2));
            } else {
                binding.editTextNationNumber.setBackground(getResources().getDrawable(R.drawable.border_gray_2));
                binding.linearLayoutNationNumber.setBackground(getResources().getDrawable(R.drawable.border_gray_2));
                binding.textViewNationNumber.setTextColor(getResources().getColor(R.color.black1));
            }
        });
    }

    private void setEditTextPhoneNumberOnFocusChangeListener() {
        binding.editTextPhoneNumber.setOnFocusChangeListener((view1, b) -> {
            if (b) {
                binding.editTextPhoneNumber.setBackground(getResources().getDrawable(R.drawable.border_orange_));
                binding.linearLayoutPhoneNumber.setBackground(getResources().getDrawable(R.drawable.border_orange_));
                binding.textViewPhoneNumber.setTextColor(getResources().getColor(R.color.orange2));
            } else {
                binding.editTextPhoneNumber.setBackground(getResources().getDrawable(R.drawable.border_gray_2));
                binding.linearLayoutPhoneNumber.setBackground(getResources().getDrawable(R.drawable.border_gray_2));
                binding.textViewPhoneNumber.setTextColor(getResources().getColor(R.color.black1));
            }
        });
    }

    private void setEditTextMobileOnFocusChangeListener() {
        binding.editTextMobile.setOnFocusChangeListener((view1, b) -> {
            if (b) {
                binding.editTextMobile.setBackground(getResources().getDrawable(R.drawable.border_orange_));
                binding.linearLayoutMobile.setBackground(getResources().getDrawable(R.drawable.border_orange_));
                binding.textViewMobile.setTextColor(getResources().getColor(R.color.orange2));
            } else {
                binding.editTextMobile.setBackground(getResources().getDrawable(R.drawable.border_gray_2));
                binding.linearLayoutMobile.setBackground(getResources().getDrawable(R.drawable.border_gray_2));
                binding.textViewMobile.setTextColor(getResources().getColor(R.color.black1));
            }
        });
    }

    private void setEditTextPostalCodeOnFocusChangeListener() {
        binding.editTextPostalCode.setOnFocusChangeListener((view1, b) -> {
            if (b) {
                binding.editTextPostalCode.setBackground(getResources().getDrawable(R.drawable.border_orange_));
                binding.linearLayoutPostalCode.setBackground(getResources().getDrawable(R.drawable.border_orange_));
                binding.textViewPostalCode.setTextColor(getResources().getColor(R.color.orange2));
            } else {
                binding.editTextPostalCode.setBackground(getResources().getDrawable(R.drawable.border_gray_2));
                binding.linearLayoutPostalCode.setBackground(getResources().getDrawable(R.drawable.border_gray_2));
                binding.textViewPostalCode.setTextColor(getResources().getColor(R.color.black1));
            }
        });
    }

    private void setEditTextBillIdOnFocusChangeListener() {
        binding.editTextBillId.setOnFocusChangeListener((view1, b) -> {
            if (b) {
                binding.editTextBillId.setBackground(getResources().getDrawable(R.drawable.border_orange_));
                binding.linearLayoutBillId.setBackground(getResources().getDrawable(R.drawable.border_orange_));
                binding.textViewBillId.setTextColor(getResources().getColor(R.color.orange2));
            } else {
                binding.editTextBillId.setBackground(getResources().getDrawable(R.drawable.border_gray_2));
                binding.linearLayoutBillId.setBackground(getResources().getDrawable(R.drawable.border_gray_2));
                binding.textViewBillId.setTextColor(getResources().getColor(R.color.black1));
            }
        });
    }

    private void setEditTextAddressOnFocusChangeListener() {
        binding.editTextAddress.setOnFocusChangeListener((view, b) -> {
            if (b) {
                binding.editTextAddress.setBackground(getResources().getDrawable(R.drawable.border_orange_));
                binding.linearLayoutAddress.setBackground(getResources().getDrawable(R.drawable.border_orange_));
                binding.textViewAddress.setTextColor(getResources().getColor(R.color.orange2));
            } else {
                binding.editTextAddress.setBackground(getResources().getDrawable(R.drawable.border_gray_2));
                binding.linearLayoutAddress.setBackground(getResources().getDrawable(R.drawable.border_gray_2));
                binding.textViewAddress.setTextColor(getResources().getColor(R.color.black1));
            }
        });
    }

    private void setEditTextNameChangedListener() {

    }

    private void setEditTextFamilyChangedListener() {
    }

    private void setEditTextFatherNameChangedListener() {
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

    private void setEditTextPostalCodeChangedListener() {
        binding.editTextPostalCode.setOnFocusChangeListener((view1, b) -> {
            if (b) {
                binding.editTextPostalCode.setBackground(getResources().getDrawable(R.drawable.border_orange_));
                binding.linearLayoutPostalCode.setBackground(getResources().getDrawable(R.drawable.border_orange_));
                binding.textViewPostalCode.setTextColor(getResources().getColor(R.color.orange2));
            } else {
                binding.editTextPostalCode.setBackground(getResources().getDrawable(R.drawable.border_gray_2));
                binding.linearLayoutPostalCode.setBackground(getResources().getDrawable(R.drawable.border_gray_2));
                binding.textViewPostalCode.setTextColor(getResources().getColor(R.color.black1));
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
                    viewFocus = binding.radioGroupService;
                    viewFocus.requestFocus();
                }
            }
        });
    }

    private void setEditTextAddressChangedListener() {
    }

    protected void SetEditTextChangedListener() {
        setEditTextNameChangedListener();
        setEditTextFamilyChangedListener();
        setEditTextFatherNameChangedListener();
        setEditTextPhoneNumberChangedListener();
        setEditTextNationNumberChangedListener();
        setEditTextMobileChangedListener();
        setEditTextBillIdChangedListener();
        setEditTextPostalCodeChangedListener();
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
        binding = null;
        context = null;

        Runtime.getRuntime().totalMemory();
        Runtime.getRuntime().freeMemory();
        Runtime.getRuntime().maxMemory();
        Debug.getNativeHeapAllocatedSize();
    }
}
