package com.app.leon.moshtarak.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Debug;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.app.leon.moshtarak.BaseItems.BaseActivity;
import com.app.leon.moshtarak.Infrastructure.IAbfaService;
import com.app.leon.moshtarak.Infrastructure.ICallback;
import com.app.leon.moshtarak.Models.DbTables.MemberInfo;
import com.app.leon.moshtarak.Models.Enums.DialogType;
import com.app.leon.moshtarak.Models.Enums.SharedReferenceKeys;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.Utils.CustomDialog;
import com.app.leon.moshtarak.Utils.CustomErrorHandling;
import com.app.leon.moshtarak.Utils.CustomProgressBar;
import com.app.leon.moshtarak.Utils.NetworkHelper;
import com.app.leon.moshtarak.Utils.SharedPreference;
import com.app.leon.moshtarak.databinding.BaseInfoContentBinding;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BaseInfoActivity extends BaseActivity implements ICallback<MemberInfo> {
    Context context;
    String billId;
    BaseInfoContentBinding binding;

    @SuppressLint("CutPasteId")
    @Override
    protected void initialize() {
        binding = BaseInfoContentBinding.inflate(getLayoutInflater());
        View childLayout = binding.getRoot();
        ConstraintLayout parentLayout = findViewById(R.id.base_Content);
        parentLayout.addView(childLayout);
        context = this;
        accessData();
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
            fillInfo();
        }
    }

    private void fillInfo() {
        Retrofit retrofit = NetworkHelper.getInstance();
        final IAbfaService getInfo = retrofit.create(IAbfaService.class);
        Call<MemberInfo> call = getInfo.getInfo(billId);
//        HttpClientWrapperNew.callHttpAsync(call, BaseInfoActivity.this, context,
//                ProgressType.SHOW.getValue());
        CustomProgressBar progressBar = new CustomProgressBar();
        progressBar.show(context, context.getString(R.string.waiting), true);
        if (isOnline(context)) {
            call.enqueue(new Callback<MemberInfo>() {
                @Override
                public void onResponse(@NonNull Call<MemberInfo> call, @NonNull Response<MemberInfo> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            execute(response.body());
                        }
                    } else {
                        errorHandlingWithResponse(response);
                    }
                    progressBar.getDialog().dismiss();
                }

                @Override
                public void onFailure(@NonNull Call<MemberInfo> call, @NonNull Throwable t) {
                    String error;
                    if (t instanceof IOException) {
                        error = context.getString(R.string.error_connection);
                    } else error = new CustomErrorHandling(context).getErrorMessageTotal(t);
                    new CustomDialog(DialogType.Red, context, error, context.getString(R.string.dear_user),
                            context.getString(R.string.error), context.getString(R.string.accepted));
                    progressBar.getDialog().dismiss();
                }
            });
        } else {
            progressBar.getDialog().dismiss();
            Toast.makeText(context, R.string.turn_internet_on, Toast.LENGTH_SHORT).show();
        }
    }

    <T> void errorHandlingWithResponse(Response<T> response) {
        int code = response.code();
        if (code >= 500 && code < 600) {
            new CustomDialog(DialogType.Yellow, BaseInfoActivity.this,
                    context.getString(R.string.error_internal),
                    context.getString(R.string.dear_user),
                    context.getString(R.string.login),
                    context.getString(R.string.accepted));
        } else if (code == 404) {
            new CustomDialog(DialogType.Yellow, BaseInfoActivity.this,
                    context.getString(R.string.error_change_server),
                    context.getString(R.string.dear_user),
                    context.getString(R.string.login),
                    context.getString(R.string.accepted));
        } else if (code >= 400 && code < 500) {
            new CustomDialog(DialogType.Yellow, BaseInfoActivity.this,
                    context.getString(R.string.error_not_update),
                    context.getString(R.string.dear_user),
                    context.getString(R.string.login),
                    context.getString(R.string.accepted));
        } else {
            new CustomDialog(DialogType.Yellow, BaseInfoActivity.this,
                    context.getString(R.string.error_other),
                    context.getString(R.string.dear_user),
                    context.getString(R.string.login),
                    context.getString(R.string.accepted));
        }
    }

    @Override
    public void execute(MemberInfo memberInfo) {
        binding.textViewId.setText(memberInfo.getBillId());
        binding.textViewFile.setText(memberInfo.getRadif().substring(0,
                memberInfo.getRadif().indexOf(".")));
        binding.textViewAccount.setText(memberInfo.getEshterak());
        int ahad = Integer.parseInt(memberInfo.getDomesticUnit()) +
                Integer.parseInt(memberInfo.getNonDomesticUnit());
        binding.textViewAhad.setText(String.valueOf(ahad));
        binding.textViewBranchRadius.setText(memberInfo.getQotr());
        binding.textViewCapacity.setText(memberInfo.getCapacity());
        binding.textViewDebt.setText(memberInfo.getMande());
        binding.textViewSiphonRadius.setText(memberInfo.getSiphon());
        binding.textViewUser.setText(memberInfo.getKarbari());
        binding.textViewName.setText(memberInfo.getFirstName().trim().concat(" ").
                concat(memberInfo.getSureName().trim()));
    }

    private boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return Objects.requireNonNull(cm).getActiveNetworkInfo() != null &&
                Objects.requireNonNull(cm.getActiveNetworkInfo()).isConnectedOrConnecting();
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
