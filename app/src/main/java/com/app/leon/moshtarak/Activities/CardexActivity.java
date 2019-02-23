package com.app.leon.moshtarak.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;

import com.app.leon.moshtarak.Adapters.KardexCustomAdapter;
import com.app.leon.moshtarak.BaseItems.BaseActivity;
import com.app.leon.moshtarak.Infrastructure.IAbfaService;
import com.app.leon.moshtarak.Models.DbTables.Kardex;
import com.app.leon.moshtarak.Models.Enums.ProgressType;
import com.app.leon.moshtarak.Models.ViewModels.UiElementInActivity;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.Utils.HttpClientWrapper;
import com.app.leon.moshtarak.Utils.ICallback;
import com.app.leon.moshtarak.Utils.NetworkHelper;
import com.app.leon.moshtarak.Utils.SharedPreference;

import java.util.ArrayList;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Retrofit;

public class CardexActivity extends BaseActivity implements ICallback<ArrayList<Kardex>> {
    @BindView(R.id.recyclerViewCardex)
    RecyclerView recyclerViewCardex;
    KardexCustomAdapter kardexCustomAdapter;
    int width;
    private Context context;
    private String billId;

    @Override
    protected UiElementInActivity getUiElementsInActivity() {
        UiElementInActivity uiElementInActivity = new UiElementInActivity();
        uiElementInActivity.setContentViewId(R.layout.cardex_activity);
        return uiElementInActivity;
    }

    @Override
    protected void initialize() {
        context = this;
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
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
        kardexCustomAdapter = new KardexCustomAdapter(kardexes, width);
        recyclerViewCardex.setAdapter(kardexCustomAdapter);
        recyclerViewCardex.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean requestChildRectangleOnScreen(RecyclerView parent, View child, Rect rect, boolean immediate) {
                return false;
            }
        });
    }
}
