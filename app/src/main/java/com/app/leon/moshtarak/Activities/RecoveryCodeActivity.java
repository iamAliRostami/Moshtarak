package com.app.leon.moshtarak.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.leon.moshtarak.Adapters.RecoverCodeCustomAdapter;
import com.app.leon.moshtarak.Infrastructure.IAbfaService;
import com.app.leon.moshtarak.Infrastructure.ICallback;
import com.app.leon.moshtarak.Models.DbTables.Request;
import com.app.leon.moshtarak.Models.Enums.ProgressType;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.Utils.HttpClientWrapper;
import com.app.leon.moshtarak.Utils.NetworkHelper;
import com.app.leon.moshtarak.Utils.SharedPreference;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Retrofit;

public class RecoveryCodeActivity extends AppCompatActivity implements ICallback<List<Request>> {
    Context context;
    String billId;
    @BindView(R.id.editTextSearch)
    EditText editTextSearch;
    RecoverCodeCustomAdapter recoverCodeCustomAdapter;
    @BindView(R.id.listViewRequest)
    ListView listViewRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recovery_code_activity);
        ButterKnife.bind(this);
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
        setOnEditTextSearchChangedListener();
    }

    void setOnEditTextSearchChangedListener() {
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String Search = editTextSearch.getText().toString().toLowerCase(Locale.getDefault());
                recoverCodeCustomAdapter.filter(Search);
            }
        });
    }
    void getAllSession() {
        Retrofit retrofit = NetworkHelper.getInstance();
        final IAbfaService getRequests = retrofit.create(IAbfaService.class);
        Call<List<Request>> call = getRequests.getAllRequests(billId);
        HttpClientWrapper.callHttpAsync(call, RecoveryCodeActivity.this, context, ProgressType.SHOW.getValue());
    }

    @Override
    public void execute(List<Request> requests) {
        recoverCodeCustomAdapter = new RecoverCodeCustomAdapter(requests, context);
        listViewRequest.setAdapter(recoverCodeCustomAdapter);
        listViewRequest.setTextFilterEnabled(true);
    }
}
