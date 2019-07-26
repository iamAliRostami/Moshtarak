package com.app.leon.moshtarak.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.view.Display;
import android.widget.ListView;

import com.app.leon.moshtarak.Adapters.KardexCustomAdapter_1;
import com.app.leon.moshtarak.BaseItems.BaseActivity;
import com.app.leon.moshtarak.Infrastructure.IAbfaService;
import com.app.leon.moshtarak.Infrastructure.ICallback;
import com.app.leon.moshtarak.Models.DbTables.Kardex;
import com.app.leon.moshtarak.Models.Enums.ProgressType;
import com.app.leon.moshtarak.Models.ViewModels.UiElementInActivity;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.Utils.HttpClientWrapper;
import com.app.leon.moshtarak.Utils.NetworkHelper;
import com.app.leon.moshtarak.Utils.SharedPreference;

import java.util.ArrayList;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Retrofit;

public class CardexActivity extends BaseActivity implements ICallback<ArrayList<Kardex>> {
    @BindView(R.id.listViewCardex)
    ListView recyclerViewCardex;
    KardexCustomAdapter_1 kardexCustomAdapter;
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
        kardexCustomAdapter = new KardexCustomAdapter_1(kardexes, context);
        recyclerViewCardex.setAdapter(kardexCustomAdapter);
//        recyclerViewCardex.setLayoutManager(new LinearLayoutManager(this) {
//            @Override
//            public boolean requestChildRectangleOnScreen(@NonNull RecyclerView parent,
//                                                         @NonNull View child, @NonNull Rect rect, boolean immediate) {
//                return false;
//            }
//        });
    }
}
