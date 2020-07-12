package com.app.leon.moshtarak.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.app.leon.moshtarak.Adapters.RecoverCodeCustomAdapter;
import com.app.leon.moshtarak.Infrastructure.IAbfaService;
import com.app.leon.moshtarak.Infrastructure.ICallback;
import com.app.leon.moshtarak.Models.DbTables.Request;
import com.app.leon.moshtarak.Models.Enums.ProgressType;
import com.app.leon.moshtarak.Models.Enums.SharedReferenceKeys;
import com.app.leon.moshtarak.Utils.HttpClientWrapperNew;
import com.app.leon.moshtarak.Utils.NetworkHelper;
import com.app.leon.moshtarak.Utils.SharedPreference;
import com.app.leon.moshtarak.databinding.RecoveryCodeActivityBinding;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Retrofit;

public class RecoveryCodeActivity extends AppCompatActivity implements ICallback<List<Request>> {
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
        HttpClientWrapperNew.callHttpAsync(call, RecoveryCodeActivity.this, context, ProgressType.SHOW.getValue());
    }

    @Override
    public void execute(List<Request> requests) {
        recoverCodeCustomAdapter = new RecoverCodeCustomAdapter(requests, context);
        binding.listViewRequest.setAdapter(recoverCodeCustomAdapter);
        binding.listViewRequest.setTextFilterEnabled(true);
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
        recoverCodeCustomAdapter = null;
        Runtime.getRuntime().totalMemory();
        Runtime.getRuntime().freeMemory();
        Runtime.getRuntime().maxMemory();
        Debug.getNativeHeapAllocatedSize();
    }
}
