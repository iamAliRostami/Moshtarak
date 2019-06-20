package com.app.leon.moshtarak.Activities;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.app.leon.moshtarak.BaseItems.BaseActivity;
import com.app.leon.moshtarak.Infrastructure.IAbfaService;
import com.app.leon.moshtarak.Infrastructure.ICallback;
import com.app.leon.moshtarak.Models.DbTables.MemberInfo;
import com.app.leon.moshtarak.Models.Enums.ProgressType;
import com.app.leon.moshtarak.Models.ViewModels.UiElementInActivity;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.Utils.HttpClientWrapper;
import com.app.leon.moshtarak.Utils.NetworkHelper;
import com.app.leon.moshtarak.Utils.SharedPreference;

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
    protected UiElementInActivity getUiElementsInActivity() {
        UiElementInActivity uiElementInActivity = new UiElementInActivity();
        uiElementInActivity.setContentViewId(R.layout.base_info_activity);
        return uiElementInActivity;
    }

    @Override
    protected void initialize() {
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
        textViewId.setText(memberInfo.getBillId());
        textViewAccount.setText(memberInfo.getEshterak());
        textViewAhad.setText(memberInfo.getDomesticUnit().concat(memberInfo.getNonDomesticUnit()));
        textViewBranchRadius.setText(memberInfo.getQotr());
        textViewCapacity.setText(memberInfo.getCapacity());
        textViewDebt.setText(memberInfo.getMande());
        textViewFile.setText(memberInfo.getRadif());
        textViewSiphonRadius.setText(memberInfo.getSiphon());
        textViewUser.setText(memberInfo.getKarbari());
        textViewName.setText(memberInfo.getFirstName().concat(" ").concat(memberInfo.getSureName()));
    }
}
