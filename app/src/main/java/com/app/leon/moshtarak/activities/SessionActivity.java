package com.app.leon.moshtarak.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Debug;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.app.leon.moshtarak.BaseItems.BaseActivity;
import com.app.leon.moshtarak.Infrastructure.IAbfaService;
import com.app.leon.moshtarak.Infrastructure.ICallback;
import com.app.leon.moshtarak.Infrastructure.ICallbackError;
import com.app.leon.moshtarak.Infrastructure.ICallbackIncomplete;
import com.app.leon.moshtarak.Models.DbTables.Session;
import com.app.leon.moshtarak.Models.Enums.DialogType;
import com.app.leon.moshtarak.Models.Enums.ProgressType;
import com.app.leon.moshtarak.Models.Enums.SharedReferenceKeys;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.Utils.CustomDialog;
import com.app.leon.moshtarak.Utils.CustomErrorHandlingNew;
import com.app.leon.moshtarak.Utils.HttpClientWrapper;
import com.app.leon.moshtarak.Utils.NetworkHelper;
import com.app.leon.moshtarak.Utils.SharedPreference;
import com.app.leon.moshtarak.adapters.SessionCustomAdapter;
import com.app.leon.moshtarak.databinding.SessionActivityBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SessionActivity extends BaseActivity {
    Context context;
    String billId;
    SessionCustomAdapter sessionCustomAdapter;
    SessionActivityBinding binding;

    @Override
    protected void initialize() {
        binding = SessionActivityBinding.inflate(getLayoutInflater());
        View childLayout = binding.getRoot();
        ConstraintLayout parentLayout = findViewById(R.id.base_Content);
        parentLayout.addView(childLayout);
        context = this;
        SharedPreference sharedPreference = new SharedPreference(context);
        if (!sharedPreference.checkIsNotEmpty()) {
            Intent intent = new Intent(getApplicationContext(), RegisterAccountActivity.class);
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
        final IAbfaService service = retrofit.create(IAbfaService.class);
        Call<ArrayList<Session>> call = service.getSessions(billId);
        GetSession session = new GetSession();
        GetError error = new GetError();
        GetSessionIncomplete incomplete = new GetSessionIncomplete();
        HttpClientWrapper.callHttpAsync(call, ProgressType.SHOW.getValue(), context, session, incomplete, error);
    }

    class GetSession implements ICallback<ArrayList<Session>> {
        @Override
        public void execute(ArrayList<Session> sessions) {
            if (sessions.size() > 0) {
                sessionCustomAdapter = new SessionCustomAdapter(sessions, context);
                binding.listViewSession.setAdapter(sessionCustomAdapter);
                binding.listViewSession.setTextFilterEnabled(true);
            } else {
                binding.listViewSession.setVisibility(View.GONE);
                binding.textViewNotFound.setVisibility(View.VISIBLE);
            }
        }
    }

    class GetSessionIncomplete implements ICallbackIncomplete<ArrayList<Session>> {
        @Override
        public void executeIncomplete(Response<ArrayList<Session>> response) {
            CustomErrorHandlingNew customErrorHandlingNew = new CustomErrorHandlingNew(context);
            String error = customErrorHandlingNew.getErrorMessageDefault(response);
            new CustomDialog(DialogType.YellowRedirect, SessionActivity.this, error,
                    SessionActivity.this.getString(R.string.dear_user),
                    SessionActivity.this.getString(R.string.login),
                    SessionActivity.this.getString(R.string.accepted));
        }
    }

    class GetError implements ICallbackError {
        @Override
        public void executeError(Throwable t) {
            CustomErrorHandlingNew customErrorHandlingNew = new CustomErrorHandlingNew(context);
            String error = customErrorHandlingNew.getErrorMessageTotal(t);
            new CustomDialog(DialogType.YellowRedirect, SessionActivity.this, error,
                    SessionActivity.this.getString(R.string.dear_user),
                    SessionActivity.this.getString(R.string.login),
                    SessionActivity.this.getString(R.string.accepted));
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
