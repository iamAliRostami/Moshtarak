package com.app.leon.moshtarak.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.app.leon.moshtarak.Adapters.SessionCustomAdapter;
import com.app.leon.moshtarak.Infrastructure.IAbfaService;
import com.app.leon.moshtarak.Infrastructure.ICallback;
import com.app.leon.moshtarak.Models.DbTables.Session;
import com.app.leon.moshtarak.Models.Enums.ProgressType;
import com.app.leon.moshtarak.Models.Enums.SharedReferenceKeys;
import com.app.leon.moshtarak.Utils.HttpClientWrapperNew;
import com.app.leon.moshtarak.Utils.NetworkHelper;
import com.app.leon.moshtarak.Utils.SharedPreference;
import com.app.leon.moshtarak.databinding.SessionActivityBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;

public class SessionActivity extends AppCompatActivity implements ICallback<ArrayList<Session>> {
    Context context;
    String billId;
    SessionCustomAdapter sessionCustomAdapter;
    SessionActivityBinding binding;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        super.onCreate(savedInstanceState);
        binding = SessionActivityBinding.inflate(getLayoutInflater());
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

    }

    void getAllSession() {
        Retrofit retrofit = NetworkHelper.getInstance();
        final IAbfaService getSessions = retrofit.create(IAbfaService.class);
        Call<ArrayList<Session>> call = getSessions.getSessions(billId);
        HttpClientWrapperNew.callHttpAsync(call, SessionActivity.this, context, ProgressType.SHOW.getValue());
    }

    @Override
    public void execute(ArrayList<Session> sessions) {
        sessionCustomAdapter = new SessionCustomAdapter(sessions, context);
        binding.listViewSession.setAdapter(sessionCustomAdapter);
        binding.listViewSession.setTextFilterEnabled(true);
    }


    @Override
    protected void onStop() {
        super.onStop();
        binding = null;
        context = null;
        sessionCustomAdapter = null;
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
        sessionCustomAdapter = null;
        Runtime.getRuntime().totalMemory();
        Runtime.getRuntime().freeMemory();
        Runtime.getRuntime().maxMemory();
        Debug.getNativeHeapAllocatedSize();
    }
}
