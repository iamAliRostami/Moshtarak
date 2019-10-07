package com.app.leon.moshtarak.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.app.leon.moshtarak.Adapters.KardexCustomAdapter_1;
import com.app.leon.moshtarak.BaseItems.BaseActivity;
import com.app.leon.moshtarak.Infrastructure.IAbfaService;
import com.app.leon.moshtarak.Infrastructure.ICallback;
import com.app.leon.moshtarak.Models.DbTables.Kardex;
import com.app.leon.moshtarak.Models.Enums.BundleEnum;
import com.app.leon.moshtarak.Models.Enums.ProgressType;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.Utils.HttpClientWrapper;
import com.app.leon.moshtarak.Utils.NetworkHelper;
import com.app.leon.moshtarak.Utils.SharedPreference;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Retrofit;

public class CardexActivity extends BaseActivity implements ICallback<ArrayList<Kardex>> {
    @BindView(R.id.listViewCardex)
    ListView listViewCardex;
    KardexCustomAdapter_1 kardexCustomAdapter;
    int width;
    private Context context;
    private String billId;
    @BindView(R.id.linearLayoutChart)
    LinearLayout linearLayoutChart;
    ArrayList<Integer> yAxisData = new ArrayList<Integer>();

    @Override
    protected void initialize() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View childLayout = Objects.requireNonNull(inflater).inflate(R.layout.cardex_content, findViewById(R.id.cardex_activity));
        @SuppressLint("CutPasteId") ConstraintLayout parentLayout = findViewById(R.id.base_Content);
        parentLayout.addView(childLayout);
        ButterKnife.bind(this);
        context = this;
        accessData();
        setOnLinearLayoutChartClickListener();
    }

    void setOnLinearLayoutChartClickListener() {
        linearLayoutChart.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ChartActivity.class);
            intent.putIntegerArrayListExtra(BundleEnum.USE.getValue(), yAxisData);
            startActivity(intent);
        });
    }

    private void accessData() {
        SharedPreference appPrefs = new SharedPreference(context);
        if (!appPrefs.checkIsNotEmpty()) {
            Intent intent = new Intent(getApplicationContext(), SignAccountActivity.class);
            startActivity(intent);
            finish();
        } else {
            billId = appPrefs.getBillID();
            fillKardex();
        }
    }

    void fillKardex() {

        Retrofit retrofit = NetworkHelper.getInstance();
        final IAbfaService getKardex = retrofit.create(IAbfaService.class);
        Call<ArrayList<Kardex>> call = getKardex.getKardex(billId);
        HttpClientWrapper.callHttpAsync(call, CardexActivity.this, context, ProgressType.SHOW.getValue());
    }

    @Override
    public void execute(ArrayList<Kardex> kardexes) {
        kardexCustomAdapter = new KardexCustomAdapter_1(kardexes, context);
        listViewCardex.setAdapter(kardexCustomAdapter);
        for (int i = 0; i < kardexes.size(); i++) {
            float floatNumber = Float.valueOf(kardexes.get(i).getUsage());
            yAxisData.add((int) floatNumber);
        }
    }
}
