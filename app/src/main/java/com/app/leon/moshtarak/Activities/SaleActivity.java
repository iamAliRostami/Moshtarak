package com.app.leon.moshtarak.Activities;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.app.leon.moshtarak.BaseItems.BaseActivity;
import com.app.leon.moshtarak.Infrastructure.IAbfaService;
import com.app.leon.moshtarak.Infrastructure.ICallback;
import com.app.leon.moshtarak.Models.DbTables.RegisterNewDto;
import com.app.leon.moshtarak.Models.Enums.DialogType;
import com.app.leon.moshtarak.Models.Enums.ProgressType;
import com.app.leon.moshtarak.Models.InterCommunation.SimpleMessage;
import com.app.leon.moshtarak.Models.ViewModels.UiElementInActivity;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.Utils.CustomDialog;
import com.app.leon.moshtarak.Utils.HttpClientWrapper;
import com.app.leon.moshtarak.Utils.NetworkHelper;
import com.app.leon.moshtarak.Utils.SharedPreference;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Retrofit;

public class SaleActivity extends BaseActivity
        implements ICallback<SimpleMessage> {
    @BindView(R.id.radioGroupService)
    RadioGroup radioGroupService;
    @BindView(R.id.radioButtonService1)
    RadioButton radioButtonService1;
    @BindView(R.id.radioButtonService2)
    RadioButton radioButtonService2;
    @BindView(R.id.buttonNavigation)
    Button buttonNavigation;
    @BindView(R.id.textViewFamily)
    TextView textViewFamily;
    @BindView(R.id.textViewName)
    TextView textViewName;
    @BindView(R.id.textViewFatherName)
    TextView textViewFatherName;
    @BindView(R.id.textViewNationNumber)
    TextView textViewNationNumber;
    @BindView(R.id.textViewPhoneNumber)
    TextView textViewPhoneNumber;
    @BindView(R.id.textViewMobile)
    TextView textViewMobile;
    @BindView(R.id.textViewService)
    TextView textViewService;
    @BindView(R.id.textViewPostalCode)
    TextView textViewPostalCode;
    @BindView(R.id.textViewAddress)
    TextView textViewAddress;
    @BindView(R.id.textViewBillId)
    TextView textViewBillId;
    @BindView(R.id.editTextFamily)
    EditText editTextFamily;
    @BindView(R.id.editTextName)
    EditText editTextName;
    @BindView(R.id.editTextFatherName)
    EditText editTextFatherName;
    @BindView(R.id.editTextNationNumber)
    EditText editTextNationNumber;
    @BindView(R.id.editTextPhoneNumber)
    EditText editTextPhoneNumber;
    @BindView(R.id.editTextMobile)
    EditText editTextMobile;
    @BindView(R.id.editTextPostalCode)
    EditText editTextPostalCode;
    @BindView(R.id.editTextAddress)
    EditText editTextAddress;
    @BindView(R.id.editTextBillId)
    EditText editTextBillId;
    @BindView(R.id.linearLayoutName)
    LinearLayout linearLayoutName;
    @BindView(R.id.linearLayoutFamily)
    LinearLayout linearLayoutFamily;
    @BindView(R.id.linearLayoutFatherName)
    LinearLayout linearLayoutFatherName;
    @BindView(R.id.linearLayoutNationNumber)
    LinearLayout linearLayoutNationNumber;
    @BindView(R.id.linearLayoutPhoneNumber)
    LinearLayout linearLayoutPhoneNumber;
    @BindView(R.id.linearLayoutMobile)
    LinearLayout linearLayoutMobile;
    @BindView(R.id.linearLayoutPostalCode)
    LinearLayout linearLayoutPostalCode;
    @BindView(R.id.linearLayoutAddress)
    LinearLayout linearLayoutAddress;
    @BindView(R.id.linearLayoutBillId)
    LinearLayout linearLayoutBillId;
    Context context;
    RegisterNewDto registerNewDto;
    SharedPreference sharedPreference;
    View viewFocus;

    @Override
    protected UiElementInActivity getUiElementsInActivity() {
        UiElementInActivity uiElementInActivity = new UiElementInActivity();
        uiElementInActivity.setContentViewId(R.layout.sale_activity);
        return uiElementInActivity;
    }

    @Override
    protected void initialize() {
        ButterKnife.bind(this);
        context = this;
        sharedPreference = new SharedPreference(context);
        editTextMobile.setText(sharedPreference.getMobileNumber().replaceFirst("09", ""));
        View view = buttonNavigation;
        view.requestFocus();
        radioButtonService1.setChecked(true);
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
        registerNewDto = new RegisterNewDto(editTextBillId.getText().toString(), editTextName.getText().toString()
                , editTextFamily.getText().toString(), editTextFatherName.getText().toString(),
                editTextNationNumber.getText().toString(), "09".concat(editTextMobile.getText().toString()),
                editTextPhoneNumber.getText().toString(), editTextAddress.getText().toString(),
                editTextPostalCode.getText().toString(), "4");
        if (radioButtonService1.isChecked())
            registerNewDto.setSelectedServices(new String[]{"1"});
        else if (radioButtonService2.isChecked())
            registerNewDto.setSelectedServices(new String[]{"1", "2"});
        Call<SimpleMessage> call = SendRegisterRequest.registerNew(registerNewDto);
        HttpClientWrapper.callHttpAsync(call, SaleActivity.this, context, ProgressType.SHOW.getValue());
    }

    private void setButtonNavigationOnClickListener() {
        buttonNavigation.setOnClickListener(view1 -> {
            View view;
            boolean cancel = false;
            if (editTextAddress.getText().length() < 1) {
                view = editTextAddress;
                view.requestFocus();
                cancel = true;
            }
            if (editTextBillId.getText().length() < 6) {
                view = editTextBillId;
                view.requestFocus();
                cancel = true;
            }
            if (editTextPostalCode.getText().length() < 10) {
                view = editTextPostalCode;
                view.requestFocus();
                cancel = true;
            }
            if (editTextMobile.getText().length() < 9) {
                view = editTextMobile;
                view.requestFocus();
                cancel = true;
            }
            if (editTextPhoneNumber.getText().length() < 8) {
                view = editTextPhoneNumber;
                view.requestFocus();
                cancel = true;
            }
            if (editTextNationNumber.getText().length() < 10) {
                view = editTextNationNumber;
                view.requestFocus();
                cancel = true;
            }
            if (editTextFatherName.getText().length() < 1) {
                view = editTextFatherName;
                view.requestFocus();
                cancel = true;
            }
            if (editTextFamily.getText().length() < 1) {
                view = editTextFamily;
                view.requestFocus();
                cancel = true;
            }
            if (editTextName.getText().length() < 1) {
                view = editTextName;
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
        editTextName.setOnFocusChangeListener((view1, b) -> {
            if (b) {
                editTextName.setBackground(getResources().getDrawable(R.drawable.border_orange_));
                linearLayoutName.setBackground(getResources().getDrawable(R.drawable.border_orange_));
                textViewName.setTextColor(getResources().getColor(R.color.orange2));
            } else {
                editTextName.setBackground(getResources().getDrawable(R.drawable.border_gray_2));
                linearLayoutName.setBackground(getResources().getDrawable(R.drawable.border_gray_2));
                textViewName.setTextColor(getResources().getColor(R.color.black1));
            }
        });
    }

    private void setEditTextFamilyOnFocusChangeListener() {
        editTextFamily.setOnFocusChangeListener((view1, b) -> {
            if (b) {
                editTextFamily.setBackground(getResources().getDrawable(R.drawable.border_orange_));
                linearLayoutFamily.setBackground(getResources().getDrawable(R.drawable.border_orange_));
                textViewFamily.setTextColor(getResources().getColor(R.color.orange2));
            } else {
                editTextFamily.setBackground(getResources().getDrawable(R.drawable.border_gray_2));
                linearLayoutFamily.setBackground(getResources().getDrawable(R.drawable.border_gray_2));
                textViewFamily.setTextColor(getResources().getColor(R.color.black1));
            }
        });
    }

    private void setEditTextFatherNameOnFocusChangeListener() {
        editTextFatherName.setOnFocusChangeListener((view1, b) -> {
            if (b) {
                editTextFatherName.setBackground(getResources().getDrawable(R.drawable.border_orange_));
                linearLayoutFatherName.setBackground(getResources().getDrawable(R.drawable.border_orange_));
                textViewFatherName.setTextColor(getResources().getColor(R.color.orange2));
            } else {
                editTextFatherName.setBackground(getResources().getDrawable(R.drawable.border_gray_2));
                linearLayoutFatherName.setBackground(getResources().getDrawable(R.drawable.border_gray_2));
                textViewFatherName.setTextColor(getResources().getColor(R.color.black1));
            }
        });
    }

    private void setEditTextNationNumberOnFocusChangeListener() {
        editTextNationNumber.setOnFocusChangeListener((view1, b) -> {
            if (b) {
                editTextNationNumber.setBackground(getResources().getDrawable(R.drawable.border_orange_));
                linearLayoutNationNumber.setBackground(getResources().getDrawable(R.drawable.border_orange_));
                textViewNationNumber.setTextColor(getResources().getColor(R.color.orange2));
            } else {
                editTextNationNumber.setBackground(getResources().getDrawable(R.drawable.border_gray_2));
                linearLayoutNationNumber.setBackground(getResources().getDrawable(R.drawable.border_gray_2));
                textViewNationNumber.setTextColor(getResources().getColor(R.color.black1));
            }
        });
    }

    private void setEditTextPhoneNumberOnFocusChangeListener() {
        editTextPhoneNumber.setOnFocusChangeListener((view1, b) -> {
            if (b) {
                editTextPhoneNumber.setBackground(getResources().getDrawable(R.drawable.border_orange_));
                linearLayoutPhoneNumber.setBackground(getResources().getDrawable(R.drawable.border_orange_));
                textViewPhoneNumber.setTextColor(getResources().getColor(R.color.orange2));
            } else {
                editTextPhoneNumber.setBackground(getResources().getDrawable(R.drawable.border_gray_2));
                linearLayoutPhoneNumber.setBackground(getResources().getDrawable(R.drawable.border_gray_2));
                textViewPhoneNumber.setTextColor(getResources().getColor(R.color.black1));
            }
        });
    }

    private void setEditTextMobileOnFocusChangeListener() {
        editTextMobile.setOnFocusChangeListener((view1, b) -> {
            if (b) {
                editTextMobile.setBackground(getResources().getDrawable(R.drawable.border_orange_));
                linearLayoutMobile.setBackground(getResources().getDrawable(R.drawable.border_orange_));
                textViewMobile.setTextColor(getResources().getColor(R.color.orange2));
            } else {
                editTextMobile.setBackground(getResources().getDrawable(R.drawable.border_gray_2));
                linearLayoutMobile.setBackground(getResources().getDrawable(R.drawable.border_gray_2));
                textViewMobile.setTextColor(getResources().getColor(R.color.black1));
            }
        });
    }

    private void setEditTextPostalCodeOnFocusChangeListener() {
        editTextPostalCode.setOnFocusChangeListener((view1, b) -> {
            if (b) {
                editTextPostalCode.setBackground(getResources().getDrawable(R.drawable.border_orange_));
                linearLayoutPostalCode.setBackground(getResources().getDrawable(R.drawable.border_orange_));
                textViewPostalCode.setTextColor(getResources().getColor(R.color.orange2));
            } else {
                editTextPostalCode.setBackground(getResources().getDrawable(R.drawable.border_gray_2));
                linearLayoutPostalCode.setBackground(getResources().getDrawable(R.drawable.border_gray_2));
                textViewPostalCode.setTextColor(getResources().getColor(R.color.black1));
            }
        });
    }

    private void setEditTextBillIdOnFocusChangeListener() {
        editTextBillId.setOnFocusChangeListener((view1, b) -> {
            if (b) {
                editTextBillId.setBackground(getResources().getDrawable(R.drawable.border_orange_));
                linearLayoutBillId.setBackground(getResources().getDrawable(R.drawable.border_orange_));
                textViewBillId.setTextColor(getResources().getColor(R.color.orange2));
            } else {
                editTextBillId.setBackground(getResources().getDrawable(R.drawable.border_gray_2));
                linearLayoutBillId.setBackground(getResources().getDrawable(R.drawable.border_gray_2));
                textViewBillId.setTextColor(getResources().getColor(R.color.black1));
            }
        });
    }

    private void setEditTextAddressOnFocusChangeListener() {
        editTextAddress.setOnFocusChangeListener((view, b) -> {
            if (b) {
                editTextAddress.setBackground(getResources().getDrawable(R.drawable.border_orange_));
                linearLayoutAddress.setBackground(getResources().getDrawable(R.drawable.border_orange_));
                textViewAddress.setTextColor(getResources().getColor(R.color.orange2));
            } else {
                editTextAddress.setBackground(getResources().getDrawable(R.drawable.border_gray_2));
                linearLayoutAddress.setBackground(getResources().getDrawable(R.drawable.border_gray_2));
                textViewAddress.setTextColor(getResources().getColor(R.color.black1));
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
                    viewFocus = editTextPhoneNumber;
                    viewFocus.requestFocus();
                }
            }
        });
    }

    private void setEditTextPhoneNumberChangedListener() {
        editTextPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 8) {
                    viewFocus = editTextMobile;
                    viewFocus.requestFocus();
                }
            }
        });
    }

    private void setEditTextMobileChangedListener() {
        editTextMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 9) {
                    viewFocus = editTextBillId;
                    viewFocus.requestFocus();
                }
            }
        });
    }

    private void setEditTextPostalCodeChangedListener() {
        editTextPostalCode.setOnFocusChangeListener((view1, b) -> {
            if (b) {
                editTextPostalCode.setBackground(getResources().getDrawable(R.drawable.border_orange_));
                linearLayoutPostalCode.setBackground(getResources().getDrawable(R.drawable.border_orange_));
                textViewPostalCode.setTextColor(getResources().getColor(R.color.orange2));
            } else {
                editTextPostalCode.setBackground(getResources().getDrawable(R.drawable.border_gray_2));
                linearLayoutPostalCode.setBackground(getResources().getDrawable(R.drawable.border_gray_2));
                textViewPostalCode.setTextColor(getResources().getColor(R.color.black1));
            }
        });
    }

    private void setEditTextBillIdChangedListener() {
        editTextPostalCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 10) {
                    viewFocus = radioGroupService;
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
}
