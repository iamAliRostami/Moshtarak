package com.app.leon.moshtarak.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.app.leon.moshtarak.BaseItems.BaseActivity;
import com.app.leon.moshtarak.Infrastructure.IAbfaService;
import com.app.leon.moshtarak.Infrastructure.ICallback;
import com.app.leon.moshtarak.Infrastructure.ICallbackError;
import com.app.leon.moshtarak.Infrastructure.ICallbackIncomplete;
import com.app.leon.moshtarak.Models.DbTables.Cardex;
import com.app.leon.moshtarak.Models.Enums.BundleEnum;
import com.app.leon.moshtarak.Models.Enums.DialogType;
import com.app.leon.moshtarak.Models.Enums.ProgressType;
import com.app.leon.moshtarak.Models.Enums.SharedReferenceKeys;
import com.app.leon.moshtarak.MyApplication;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.Utils.CustomDialog;
import com.app.leon.moshtarak.Utils.CustomErrorHandlingNew;
import com.app.leon.moshtarak.Utils.HttpClientWrapper;
import com.app.leon.moshtarak.Utils.NetworkHelper;
import com.app.leon.moshtarak.Utils.SharedPreference;
import com.app.leon.moshtarak.adapters.CardexCustomAdapter;
import com.app.leon.moshtarak.databinding.CardexContentBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CardexActivity extends BaseActivity {
    CardexContentBinding binding;
    CardexCustomAdapter cardexCustomAdapter;
    ArrayList<Integer> yAxisData = new ArrayList<>();
    ArrayList<String> axisValues = new ArrayList<>();
    private Context context;
    private String billId;
    SharedPreference sharedPreference;

    @SuppressLint("CutPasteId")
    @Override
    protected void initialize() {
        binding = CardexContentBinding.inflate(getLayoutInflater());
        View childLayout = binding.getRoot();
        ConstraintLayout parentLayout = findViewById(R.id.base_Content);
        parentLayout.addView(childLayout);
        context = this;
        accessData();
        setOnLinearLayoutChartClickListener();
    }

    void setOnLinearLayoutChartClickListener() {
        binding.linearLayoutChart.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ChartActivity.class);
            Bundle bundle = new Bundle();
            bundle.putIntegerArrayList(BundleEnum.USE.getValue(), yAxisData);
            bundle.putStringArrayList(BundleEnum.DATE.getValue(), axisValues);
            intent.putExtra(BundleEnum.DATA.getValue(), bundle);
            startActivity(intent);
        });
    }

    private void accessData() {
        sharedPreference = new SharedPreference(context);
        if (!sharedPreference.checkIsNotEmpty()) {
            Intent intent = new Intent(getApplicationContext(), SignAccountActivity.class);
            startActivity(intent);
            finish();
        } else {
            billId = sharedPreference.getArrayList(SharedReferenceKeys.BILL_ID.getValue()).
                    get(sharedPreference.getIndex());
            String name = sharedPreference.getArrayList(SharedReferenceKeys.NAME.getValue()).
                    get(sharedPreference.getIndex());
            Toast.makeText(MyApplication.getContext(), getString(R.string.active_user).concat(name),
                    Toast.LENGTH_LONG).show();
            fillCardex();
        }
    }

    void fillCardex() {
        Retrofit retrofit = NetworkHelper.getInstance();
        final IAbfaService getKardex = retrofit.create(IAbfaService.class);
        byte[] encodeValue = Base64.encode(billId.getBytes(), Base64.DEFAULT);
        String base64 = new String(encodeValue);
        Call<ArrayList<Cardex>> call = getKardex.getKardex(billId, base64.substring(0,
                base64.length() - 1));
        GetCardex getCardex = new GetCardex();
        GetError getError = new GetError();
        GetCardexIncomplete incomplete = new GetCardexIncomplete();
        HttpClientWrapper.callHttpAsync(call, ProgressType.SHOW.getValue(), context, getCardex,
                incomplete, getError);
    }

    class GetCardex implements ICallback<ArrayList<Cardex>> {
        @Override
        public void execute(ArrayList<Cardex> cardexes) {
            for (int i = 0; i < cardexes.size(); i++) {
                if (cardexes.get(i).getOweDate() != null) {
                    float floatNumber = Float.parseFloat(cardexes.get(i).getUsage());
                    yAxisData.add(0, (int) floatNumber);
                    axisValues.add(cardexes.get(i).getOweDate());
                }
            }
            cardexCustomAdapter = new CardexCustomAdapter(cardexes, context);
            LayoutInflater inflater = getLayoutInflater();
            ViewGroup header = (ViewGroup) inflater.inflate(R.layout.cardex_header,
                    binding.listViewCardex, false);
            binding.listViewCardex.addHeaderView(header, null, false);
            binding.listViewCardex.setAdapter(cardexCustomAdapter);
            cardexCustomAdapter.notifyDataSetChanged();
        }
    }

    class GetCardexIncomplete implements ICallbackIncomplete<ArrayList<Cardex>> {
        @Override
        public void executeIncomplete(Response<ArrayList<Cardex>> response) {
            CustomErrorHandlingNew customErrorHandlingNew = new CustomErrorHandlingNew(context);
            String error = customErrorHandlingNew.getErrorMessageDefault(response);
            if (response.code() == 400) {
                error = CardexActivity.this.getString(R.string.error_register_again);
                sharedPreference.removeItem(sharedPreference.getIndex());
            }
            new CustomDialog(DialogType.YellowRedirect, CardexActivity.this, error,
                    CardexActivity.this.getString(R.string.dear_user),
                    CardexActivity.this.getString(R.string.login),
                    CardexActivity.this.getString(R.string.accepted));
        }
    }

    class GetError implements ICallbackError {
        @Override
        public void executeError(Throwable t) {
            CustomErrorHandlingNew customErrorHandlingNew = new CustomErrorHandlingNew(context);
            String error = customErrorHandlingNew.getErrorMessageTotal(t);
            new CustomDialog(DialogType.YellowRedirect, CardexActivity.this, error,
                    CardexActivity.this.getString(R.string.dear_user),
                    CardexActivity.this.getString(R.string.login),
                    CardexActivity.this.getString(R.string.accepted));
        }
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
