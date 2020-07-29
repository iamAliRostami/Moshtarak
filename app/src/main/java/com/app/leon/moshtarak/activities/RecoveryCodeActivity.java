package com.app.leon.moshtarak.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.app.leon.moshtarak.Infrastructure.IAbfaService;
import com.app.leon.moshtarak.Infrastructure.ICallback;
import com.app.leon.moshtarak.Infrastructure.ICallbackError;
import com.app.leon.moshtarak.Infrastructure.ICallbackIncomplete;
import com.app.leon.moshtarak.Models.DbTables.Request;
import com.app.leon.moshtarak.Models.Enums.DialogType;
import com.app.leon.moshtarak.Models.Enums.ProgressType;
import com.app.leon.moshtarak.Models.Enums.SharedReferenceKeys;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.Utils.CustomDialog;
import com.app.leon.moshtarak.Utils.CustomErrorHandlingNew;
import com.app.leon.moshtarak.Utils.HttpClientWrapper;
import com.app.leon.moshtarak.Utils.NetworkHelper;
import com.app.leon.moshtarak.Utils.SharedPreference;
import com.app.leon.moshtarak.adapters.RecoverCodeCustomAdapter;
import com.app.leon.moshtarak.databinding.RecoveryCodeActivityBinding;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RecoveryCodeActivity extends AppCompatActivity {
    Context context;
    String billId;
    RecoverCodeCustomAdapter recoverCodeCustomAdapter;
    RecoveryCodeActivityBinding binding;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        super.onCreate(savedInstanceState);
        binding = RecoveryCodeActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        context = this;
        SharedPreference sharedPreference = new SharedPreference(context);
        if (!sharedPreference.checkIsNotEmpty()) {
            Intent intent = new Intent(getApplicationContext(), SignAccountActivity.class);
            startActivity(intent);
            finish();
        } else {
            billId = sharedPreference.getArrayList(SharedReferenceKeys.BILL_ID.getValue()).
                    get(sharedPreference.getIndex());
            getAllSession();
        }
        setOnEditTextSearchChangedListener();
    }

    void setOnEditTextSearchChangedListener() {
        binding.editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String Search = binding.editTextSearch.getText().toString().toLowerCase(Locale.getDefault());
                recoverCodeCustomAdapter.filter(Search);
            }
        });
    }

    void getAllSession() {
        Retrofit retrofit = NetworkHelper.getInstance();
        final IAbfaService getRequests = retrofit.create(IAbfaService.class);
        Call<List<Request>> call = getRequests.getAllRequests(billId);
        GetSession session = new GetSession();
        GetSessionIncomplete incomplete = new GetSessionIncomplete();
        GetError error = new GetError();
        HttpClientWrapper.callHttpAsync(call, ProgressType.SHOW.getValue(), context, session, incomplete, error);
    }

    class GetSession implements ICallback<List<Request>> {
        @Override
        public void execute(List<Request> requests) {
            recoverCodeCustomAdapter = new RecoverCodeCustomAdapter(requests, context);
            binding.listViewRequest.setAdapter(recoverCodeCustomAdapter);
            binding.listViewRequest.setTextFilterEnabled(true);
        }
    }

    class GetSessionIncomplete implements ICallbackIncomplete<List<Request>> {
        @Override
        public void executeIncomplete(Response<List<Request>> response) {
            CustomErrorHandlingNew customErrorHandlingNew = new CustomErrorHandlingNew(context);
            String error = customErrorHandlingNew.getErrorMessageDefault(response);
            new CustomDialog(DialogType.Yellow, RecoveryCodeActivity.this, error,
                    RecoveryCodeActivity.this.getString(R.string.dear_user),
                    RecoveryCodeActivity.this.getString(R.string.login),
                    RecoveryCodeActivity.this.getString(R.string.accepted));
        }
    }

    class GetError implements ICallbackError {
        @Override
        public void executeError(Throwable t) {
            CustomErrorHandlingNew customErrorHandlingNew = new CustomErrorHandlingNew(context);
            String error = customErrorHandlingNew.getErrorMessageTotal(t);
            new CustomDialog(DialogType.YellowRedirect, RecoveryCodeActivity.this, error,
                    RecoveryCodeActivity.this.getString(R.string.dear_user),
                    RecoveryCodeActivity.this.getString(R.string.login),
                    RecoveryCodeActivity.this.getString(R.string.accepted));
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
