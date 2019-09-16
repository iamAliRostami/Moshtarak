package com.app.leon.moshtarak.BaseItems;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.app.leon.moshtarak.Activities.SignAccountActivity;
import com.app.leon.moshtarak.Infrastructure.IAbfaService;
import com.app.leon.moshtarak.Infrastructure.ICallback;
import com.app.leon.moshtarak.Models.DbTables.Request;
import com.app.leon.moshtarak.Models.Enums.ProgressType;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.Utils.HttpClientWrapper;
import com.app.leon.moshtarak.Utils.NetworkHelper;
import com.app.leon.moshtarak.Utils.SharedPreference;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;

public class RecoveryCodeActivity extends AppCompatActivity implements ICallback<ArrayList<Request>> {
    Context context;
    String billId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recovery_code_activity);
        context = this;
        SharedPreference appPrefs = new SharedPreference(context);
        if (!appPrefs.checkIsNotEmpty()) {
            Intent intent = new Intent(getApplicationContext(), SignAccountActivity.class);
            startActivity(intent);
            finish();
        } else {
            billId = appPrefs.getBillID();
            getAllSession();
        }

    }

    void getAllSession() {
        Retrofit retrofit = NetworkHelper.getInstance();
        final IAbfaService getRequests = retrofit.create(IAbfaService.class);
        Call<ArrayList<Request>> call = getRequests.getAllRequests(billId);
        HttpClientWrapper.callHttpAsync(call, RecoveryCodeActivity.this, context, ProgressType.SHOW.getValue());
    }

    @Override
    public void execute(ArrayList<Request> requests) {
        Log.e("size:", String.valueOf(requests.size()));
    }
}
