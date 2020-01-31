package com.app.leon.moshtarak.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.app.leon.moshtarak.BaseItems.BaseActivity;
import com.app.leon.moshtarak.Infrastructure.IAbfaService;
import com.app.leon.moshtarak.Infrastructure.ICallback;
import com.app.leon.moshtarak.Models.DbTables.MemberInfo;
import com.app.leon.moshtarak.Models.Enums.ProgressType;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.Utils.HttpClientWrapper;
import com.app.leon.moshtarak.Utils.NetworkHelper;
import com.app.leon.moshtarak.Utils.SharedPreference;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Retrofit;

public class BaseInfoActivity extends BaseActivity implements ICallback<MemberInfo> {
    @BindView(R.id.textViewDebt)
    TextView textViewDebt;
    @BindView(R.id.textViewSiphonRadius)
    TextView textViewSiphonRadius;
    @BindView(R.id.textViewBranchRadius)
    TextView textViewBranchRadius;
    @BindView(R.id.textViewAhad)
    TextView textViewAhad;
    @BindView(R.id.textViewCapacity)
    TextView textViewCapacity;
    @BindView(R.id.textViewUser)
    TextView textViewUser;
    @BindView(R.id.textViewId)
    TextView textViewId;
    @BindView(R.id.textViewAccount)
    TextView textViewAccount;
    @BindView(R.id.textViewFile)
    TextView textViewFile;
    @BindView(R.id.textViewName)
    TextView textViewName;
    Context context;
    String billId;

    @Override
    protected void initialize() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View childLayout = Objects.requireNonNull(inflater).inflate(R.layout.base_info_content, findViewById(R.id.base_info_activity));
        @SuppressLint("CutPasteId") ConstraintLayout parentLayout = findViewById(R.id.base_Content);
        parentLayout.addView(childLayout);
        ButterKnife.bind(this);
        context = this;
        accessData();

    }

    private void accessData() {
        SharedPreference appPrefs = new SharedPreference(context);
        if (!appPrefs.checkIsNotEmpty()) {
            Intent intent = new Intent(getApplicationContext(), SignAccountActivity.class);
            startActivity(intent);
            finish();
        } else {
            billId = appPrefs.getBillID();
            fillInfo();
        }
    }

    private void fillInfo() {
        Retrofit retrofit = NetworkHelper.getInstance();
        final IAbfaService getInfo = retrofit.create(IAbfaService.class);
        Call<MemberInfo> call = getInfo.getInfo(billId);

        HttpClientWrapper.callHttpAsync(call, BaseInfoActivity.this, context, ProgressType.SHOW.getValue());
    }

    @Override
    public void execute(MemberInfo memberInfo) {
//        String id = memberInfo.getBillId().substring(1,memberInfo.getBillId().indexOf("."));
        textViewId.setText(memberInfo.getBillId());
        textViewFile.setText(memberInfo.getRadif().substring(0, memberInfo.getRadif().indexOf(".")));
        textViewAccount.setText(memberInfo.getEshterak());
        textViewAhad.setText(memberInfo.getDomesticUnit().concat(memberInfo.getNonDomesticUnit()));
        textViewBranchRadius.setText(memberInfo.getQotr());
        textViewCapacity.setText(memberInfo.getCapacity());
        textViewDebt.setText(memberInfo.getMande());
        textViewSiphonRadius.setText(memberInfo.getSiphon());
        textViewUser.setText(memberInfo.getKarbari());
        textViewName.setText(memberInfo.getFirstName().trim().concat(" ").concat(memberInfo.getSureName().trim()));
    }
}
