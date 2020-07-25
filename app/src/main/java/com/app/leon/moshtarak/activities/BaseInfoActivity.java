package com.app.leon.moshtarak.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Debug;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.app.leon.moshtarak.BaseItems.BaseActivity;
import com.app.leon.moshtarak.Infrastructure.IAbfaService;
import com.app.leon.moshtarak.Infrastructure.ICallback;
import com.app.leon.moshtarak.Models.DbTables.MemberInfo;
import com.app.leon.moshtarak.Models.Enums.ProgressType;
import com.app.leon.moshtarak.Models.Enums.SharedReferenceKeys;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.Utils.HttpClientWrapperNew;
import com.app.leon.moshtarak.Utils.NetworkHelper;
import com.app.leon.moshtarak.Utils.SharedPreference;
import com.app.leon.moshtarak.databinding.BaseInfoContentBinding;

import retrofit2.Call;
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
        HttpClientWrapperNew.callHttpAsync(call, BaseInfoActivity.this, context,
                ProgressType.SHOW.getValue());
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
        context = null;
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
