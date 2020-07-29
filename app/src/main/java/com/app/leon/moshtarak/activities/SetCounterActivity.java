package com.app.leon.moshtarak.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.app.leon.moshtarak.BaseItems.BaseActivity;
import com.app.leon.moshtarak.Infrastructure.IAbfaService;
import com.app.leon.moshtarak.Infrastructure.ICallback;
import com.app.leon.moshtarak.Infrastructure.ICallbackError;
import com.app.leon.moshtarak.Infrastructure.ICallbackIncomplete;
import com.app.leon.moshtarak.Models.DbTables.LastBillInfo;
import com.app.leon.moshtarak.Models.Enums.BundleEnum;
import com.app.leon.moshtarak.Models.Enums.DialogType;
import com.app.leon.moshtarak.Models.Enums.ProgressType;
import com.app.leon.moshtarak.Models.Enums.SharedReferenceKeys;
import com.app.leon.moshtarak.MyApplication;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.Utils.CustomDialog;
import com.app.leon.moshtarak.Utils.CustomErrorHandlingNew;
import com.app.leon.moshtarak.Utils.HttpClientWrapper;
import com.app.leon.moshtarak.Utils.LovelyTextInputDialog;
import com.app.leon.moshtarak.Utils.NetworkHelper;
import com.app.leon.moshtarak.Utils.SharedPreference;
import com.app.leon.moshtarak.databinding.SetCounterContentBinding;
import com.google.gson.Gson;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SetCounterActivity extends BaseActivity {
    LovelyTextInputDialog lovelyTextInputDialog;
    Context context;
    String billId, number, phoneNumber;
    SetCounterContentBinding binding;

    @Override
    protected void initialize() {
        binding = SetCounterContentBinding.inflate(getLayoutInflater());
        View childLayout = binding.getRoot();
        @SuppressLint("CutPasteId") ConstraintLayout parentLayout = findViewById(R.id.base_Content);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        parentLayout.addView(childLayout);
        context = this;
        setComponentPosition();
        accessData();
//        sharedPreference = new SharedPreference(context);
        setTextChangedListener();
        setOnButtonSignClickListener();
    }

    void showDialog() {
        lovelyTextInputDialog = new LovelyTextInputDialog(this, R.style.EditTextTintTheme)
                .setTopColorRes(R.color.orange1)
                .setTitle(R.string.dear_user)
                .setMessage(getString(R.string.enter_counter_number).concat(
                        getString(R.string.enter_counter_number_method)))
                .setCancelable(false)
                .setInputFilter(R.string.all_field_should_filled, text -> {
                    EditText editTextNumber = LovelyTextInputDialog.getEditTextNumber(1);
                    if (editTextNumber.getText().length() < 1)
                        return false;
                    editTextNumber = LovelyTextInputDialog.getEditTextNumber(2);
                    if (editTextNumber.getText().length() < 1)
                        return false;
                    editTextNumber = LovelyTextInputDialog.getEditTextNumber(3);
                    if (editTextNumber.getText().length() < 1)
                        return false;
                    editTextNumber = LovelyTextInputDialog.getEditTextNumber(4);
                    if (editTextNumber.getText().length() < 1)
                        return false;
                    editTextNumber = LovelyTextInputDialog.getEditTextNumber(5);
                    return editTextNumber.getText().length() >= 1;
                })
                .setConfirmButton(R.string.confirm, text -> {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    Objects.requireNonNull(imm).toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    String s1, s2, s3, s4, s5;
                    EditText editTextNumber = LovelyTextInputDialog.getEditTextNumber(1);
                    s1 = editTextNumber.getText().toString();
                    editTextNumber = LovelyTextInputDialog.getEditTextNumber(2);
                    s2 = editTextNumber.getText().toString();
                    editTextNumber = LovelyTextInputDialog.getEditTextNumber(3);
                    s3 = editTextNumber.getText().toString();
                    editTextNumber = LovelyTextInputDialog.getEditTextNumber(4);
                    s4 = editTextNumber.getText().toString();
                    editTextNumber = LovelyTextInputDialog.getEditTextNumber(5);
                    s5 = editTextNumber.getText().toString();
                    binding.editText1.setText(s1);
                    binding.editText2.setText(s2);
                    binding.editText3.setText(s3);
                    binding.editText4.setText(s4);
                    binding.editText5.setText(s5);
                })
                .setNegativeButton(R.string.cancel, v -> {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    Objects.requireNonNull(imm).toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
//                    Toast.makeText(context, R.string.canceled, Toast.LENGTH_LONG).show();
                    Toast.makeText(MyApplication.getContext(),
                            MyApplication.getContext().getString(R.string.canceled),
                            Toast.LENGTH_LONG).show();

                });
        lovelyTextInputDialog.show();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setTextChangedListener() {
        binding.editText1.setOnClickListener(v -> showDialog());
        binding.editText2.setOnClickListener(v -> showDialog());
        binding.editText3.setOnClickListener(v -> showDialog());
        binding.editText4.setOnClickListener(v -> showDialog());
        binding.editText5.setOnClickListener(v -> showDialog());
    }

    private void setOnButtonSignClickListener() {
        binding.buttonSign.setOnClickListener(view -> {
            boolean cancel = false;
            View viewFocus;
            if (binding.editText5.getText().length() < 1) {
                cancel = true;
                viewFocus = binding.editText5;
                viewFocus.requestFocus();
            }
            if (binding.editText4.getText().length() < 1) {
                cancel = true;
                viewFocus = binding.editText4;
                viewFocus.requestFocus();
            }
            if (binding.editText3.getText().length() < 1) {
                cancel = true;
                viewFocus = binding.editText3;
                viewFocus.requestFocus();
            }

            if (binding.editText2.getText().length() < 1) {
                cancel = true;
                viewFocus = binding.editText2;
                viewFocus.requestFocus();
            }
            if (binding.editText1.getText().length() < 1) {
                cancel = true;
                viewFocus = binding.editText1;
                viewFocus.requestFocus();
            }
            if (!cancel) {
                number = binding.editText1.getText().toString();
                number = number.concat(binding.editText2.getText().toString()).concat(binding.editText3.getText().toString())
                        .concat(binding.editText4.getText().toString()).concat(binding.editText5.getText().toString());
                sendNumber();
            }
        });
    }

    private void accessData() {
        SharedPreference sharedPreference = new SharedPreference(context);
        if (!sharedPreference.checkIsNotEmpty()) {
            Intent intent = new Intent(getApplicationContext(), SignAccountActivity.class);
            startActivity(intent);
            finish();
        } else {
            billId = sharedPreference.getArrayList(SharedReferenceKeys.BILL_ID.getValue()).
                    get(sharedPreference.getIndex());
            phoneNumber = sharedPreference.getArrayList(SharedReferenceKeys.MOBILE_NUMBER.getValue()).
                    get(sharedPreference.getIndex()).replaceFirst(getString(R.string._09), "");
            Toast.makeText(MyApplication.getContext(), "اشتراک فعال:\n".concat(billId), Toast.LENGTH_LONG).show();
        }
    }

    private void sendNumber() {
        Retrofit retrofit = NetworkHelper.getInstance();
        final IAbfaService sendNumber = retrofit.create(IAbfaService.class);
        Call<LastBillInfo> call = sendNumber.sendNumber(billId, number, getString(R.string._09).
                concat(phoneNumber), 4);
        SetCounter setCounter = new SetCounter();
        SetCounterIncomplete incomplete = new SetCounterIncomplete();
        GetError error = new GetError();
        HttpClientWrapper.callHttpAsync(call, ProgressType.SHOW.getValue(), context, setCounter, incomplete, error);
    }

    class SetCounter implements ICallback<LastBillInfo> {
        @Override
        public void execute(LastBillInfo lastBillInfo) {
            Intent intent = new Intent(getApplicationContext(), LastBillActivity.class);
            Bundle bundle = new Bundle();

            Gson gson = new Gson();
            String json = gson.toJson(lastBillInfo);
            bundle.putString(BundleEnum.LAST_BILL_FROM_COUNTER.getValue(), json);
            intent.putExtra(BundleEnum.DATA.getValue(), bundle);
            startActivity(intent);
        }
    }

    class SetCounterIncomplete implements ICallbackIncomplete<LastBillInfo> {
        @Override
        public void executeIncomplete(Response<LastBillInfo> response) {
            CustomErrorHandlingNew customErrorHandlingNew = new CustomErrorHandlingNew(context);
            String error = customErrorHandlingNew.getErrorMessageDefault(response);

            if (response.code() == 400) {
                CustomErrorHandlingNew.APIError apiError = customErrorHandlingNew.parseError(response);
                error = apiError.message();
            }
            new CustomDialog(DialogType.Yellow, SetCounterActivity.this, error,
                    SetCounterActivity.this.getString(R.string.dear_user),
                    SetCounterActivity.this.getString(R.string.login),
                    SetCounterActivity.this.getString(R.string.accepted));
        }
    }

    class GetError implements ICallbackError {
        @Override
        public void executeError(Throwable t) {
            CustomErrorHandlingNew customErrorHandlingNew = new CustomErrorHandlingNew(context);
            String error = customErrorHandlingNew.getErrorMessageTotal(t);
            new CustomDialog(DialogType.YellowRedirect, SetCounterActivity.this, error,
                    SetCounterActivity.this.getString(R.string.dear_user),
                    SetCounterActivity.this.getString(R.string.login),
                    SetCounterActivity.this.getString(R.string.accepted));
        }
    }

    void setComponentPosition() {
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = Objects.requireNonNull(wm).getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int height = metrics.heightPixels;

        binding.linearLayout1.setY((float) (height - (double) 16 * height / 27));
        binding.linearLayout2.setY((float) (height - (double) 2 * height / 5));

//        Bitmap src = BitmapFactory.decodeResource(getResources(), R.drawable.img_water_meter);
//        binding.linearLayout1.setY((float) (44 * src.getHeight() / 27));
//        binding.linearLayout2.setY((float) (58 * src.getHeight() / 27));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().totalMemory();
        Runtime.getRuntime().freeMemory();
        Runtime.getRuntime().maxMemory();
        Debug.getNativeHeapAllocatedSize();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Runtime.getRuntime().totalMemory();
        Runtime.getRuntime().freeMemory();
        Runtime.getRuntime().maxMemory();
        Debug.getNativeHeapAllocatedSize();
    }
}
