package com.app.leon.moshtarak.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.app.leon.moshtarak.Adapters.SessionCustomAdapter;
import com.app.leon.moshtarak.Infrastructure.IAbfaService;
import com.app.leon.moshtarak.Infrastructure.ICallback;
import com.app.leon.moshtarak.Models.DbTables.Session;
import com.app.leon.moshtarak.Models.Enums.ProgressType;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.Utils.FontManager;
import com.app.leon.moshtarak.Utils.HttpClientWrapper;
import com.app.leon.moshtarak.Utils.NetworkHelper;
import com.app.leon.moshtarak.Utils.SharedPreference;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Retrofit;

public class SessionActivity extends AppCompatActivity implements ICallback<ArrayList<Session>> {
    Context context;
    String billId;
    SessionCustomAdapter sessionCustomAdapter;
    @BindView(R.id.listViewSession)
    ListView listViewSession;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_activity);
        ButterKnife.bind(this);
        context = this;
        ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);
        FontManager fontManager = new FontManager(context);
        fontManager.setFont(constraintLayout);
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
        final IAbfaService getSessions = retrofit.create(IAbfaService.class);
        Call<ArrayList<Session>> call = getSessions.getSessions(billId);
        HttpClientWrapper.callHttpAsync(call, SessionActivity.this, context, ProgressType.SHOW.getValue());
    }

    @Override
    public void execute(ArrayList<Session> sessions) {
        Log.e("size:", String.valueOf(sessions.size()));
        sessionCustomAdapter = new SessionCustomAdapter(sessions, context);
        listViewSession.setAdapter(sessionCustomAdapter);
        listViewSession.setTextFilterEnabled(true);
    }
}
