package com.app.leon.moshtarak.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.leon.moshtarak.BaseItems.BaseActivity;
import com.app.leon.moshtarak.Infrastructure.IAbfaService;
import com.app.leon.moshtarak.Infrastructure.ICallback;
import com.app.leon.moshtarak.Models.DbTables.LastBillInfo;
import com.app.leon.moshtarak.Models.Enums.BundleEnum;
import com.app.leon.moshtarak.Models.Enums.ProgressType;
import com.app.leon.moshtarak.Models.ViewModels.UiElementInActivity;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.Utils.Code128;
import com.app.leon.moshtarak.Utils.CustomTab;
import com.app.leon.moshtarak.Utils.HttpClientWrapper;
import com.app.leon.moshtarak.Utils.NetworkHelper;
import com.app.leon.moshtarak.Utils.SharedPreference;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Retrofit;


public class LastBillActivity extends BaseActivity {
    @BindView(R.id.textViewBillId)
    TextView textViewBillId;
    @BindView(R.id.textViewPayId)
    TextView textViewPayId;
    @BindView(R.id.textViewPreNumber)
    TextView textViewPreNumber;
    @BindView(R.id.textViewNewNumber)
    TextView textViewNewNumber;
    @BindView(R.id.textViewPreDate)
    TextView textViewPreDate;
    @BindView(R.id.textViewNewDate)
    TextView textViewNewDate;
    @BindView(R.id.textViewAbBaha)
    TextView textViewAbBaha;
    @BindView(R.id.textViewTax)
    TextView textViewTax;
    @BindView(R.id.textViewKarmozdeFazelab)
    TextView textViewKarmozdeFazelab;
    @BindView(R.id.textViewTakalifBoodje)
    TextView textViewTakalifBoodje;
    @BindView(R.id.textViewPreDebtOrOwe)
    TextView textViewPreDebtOrOwe;
    @BindView(R.id.textViewDate)
    TextView textViewDate;
    @BindView(R.id.textViewCost)
    TextView textViewCost;

    @BindView(R.id.textViewUse)
    TextView textViewUse;
    @BindView(R.id.textViewUseAverage)
    TextView textViewUseAverage;
    @BindView(R.id.textViewUseLength)
    TextView textViewUseLength;

    @BindView(R.id.imageViewBarcode)
    ImageView imageViewBarcode;
    Context context;
    String billId;
    String id;
    String zoneId;
    String address = "https://bill.bpm.bankmellat.ir/bpgwchannel/";
    boolean isPayed = false;

    @Override
    protected UiElementInActivity getUiElementsInActivity() {
        UiElementInActivity uiElementInActivity = new UiElementInActivity();
        uiElementInActivity.setContentViewId(R.layout.last_bill_activity);
        return uiElementInActivity;
    }

    @Override
    protected void initialize() {
        ButterKnife.bind(this);
        context = this;
        textViewCost.setOnClickListener(view -> {
            if (!isPayed)
                new CustomTab(address, LastBillActivity.this);
            else
                Toast.makeText(context, context.getString(R.string.payed), Toast.LENGTH_SHORT).show();
        });
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
            fillLastBillInfo();
        }
    }

    void fillLastBillInfo() {
        if (getIntent().getExtras() != null) {
            Bundle bundle1 = getIntent().getBundleExtra(BundleEnum.DATA.getValue());
            Bundle bundle2 = getIntent().getBundleExtra(BundleEnum.THIS_BILL.getValue());
            if (bundle1 != null) {
                textViewBillId.setText(bundle1.getString(BundleEnum.BILL_ID.getValue()));
                textViewPayId.setText(bundle1.getString(BundleEnum.PAY_ID.getValue()));
                textViewNewNumber.setText(bundle1.getString(BundleEnum.NEW.getValue()));
                textViewPreNumber.setText(bundle1.getString(BundleEnum.PRE.getValue()));

                textViewNewDate.setText(bundle1.getString(BundleEnum.CURRENT_READING_DATE.getValue()));
                textViewPreDate.setText(bundle1.getString(BundleEnum.PRE_READING_DATE.getValue()));

                textViewUseAverage.setText(bundle1.getString(BundleEnum.USE_AVERAGE.getValue()));
                textViewUseLength.setText(bundle1.getString(BundleEnum.USE_LENGTH.getValue()));
                textViewUse.setText(bundle1.getString(BundleEnum.USE.getValue()));


                textViewPreDebtOrOwe.setText(bundle1.getString(BundleEnum.PRE_DEBT_OR_OWE.getValue()));
                textViewTakalifBoodje.setText(bundle1.getString(BundleEnum.TAKALIF_BOODJE.getValue()));
                textViewKarmozdeFazelab.setText(bundle1.getString(BundleEnum.KARMOZDE_FAZELAB.getValue()));

                textViewAbBaha.setText(bundle1.getString(BundleEnum.AB_BAHA.getValue()));
                textViewTax.setText(bundle1.getString(BundleEnum.TAX.getValue()));
                textViewDate.setText(bundle1.getString(BundleEnum.DATE.getValue()));
                textViewCost.setText(bundle1.getString(BundleEnum.COST.getValue()));
                setImageBitmap(imageViewBarcode, bundle1.getString(BundleEnum.COST.getValue()));

                isPayed = bundle1.getBoolean(BundleEnum.IS_PAYED.getValue());

            } else if (bundle2 != null) {
                Retrofit retrofit = NetworkHelper.getInstance();
                final IAbfaService getLastBillInfo = retrofit.create(IAbfaService.class);
                Call<LastBillInfo> call = getLastBillInfo.getThisBillInfo(
                        bundle2.getString(BundleEnum.ID.getValue()),
                        bundle2.getString(BundleEnum.ZONE_ID.getValue()));
                ThisBill thisBill = new ThisBill();
                HttpClientWrapper.callHttpAsync(call, thisBill, context, ProgressType.SHOW.getValue());
            }

        } else {
            Retrofit retrofit = NetworkHelper.getInstance();
            final IAbfaService getThisBillInfo = retrofit.create(IAbfaService.class);
            Call<LastBillInfo> call = getThisBillInfo.getLastBillInfo(billId);
            ThisBill thisBill = new ThisBill();
            HttpClientWrapper.callHttpAsync(call, thisBill, context, ProgressType.SHOW.getValue());
        }
    }

    void setImageBitmap(ImageView imageView, String s) {
        Code128 code = new Code128(this);
        code.setData(s);
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        assert wm != null;
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;
        Bitmap bitmap = code.getBitmap(2 * width / 3, height / 8);
        imageView.setImageBitmap(bitmap);
    }

    class ThisBill implements ICallback<LastBillInfo> {
        @Override
        public void execute(LastBillInfo lastBillInfo) {
            textViewBillId.setText(lastBillInfo.getBillId().trim());
            textViewPayId.setText(lastBillInfo.getPayId().trim());
            textViewNewNumber.setText(lastBillInfo.getCurrentReadingNumber());
            textViewPreNumber.setText(lastBillInfo.getPreReadingNumber());

            textViewNewDate.setText(lastBillInfo.getCurrentReadingDate());
            textViewPreDate.setText(lastBillInfo.getPreReadingDate());

            textViewUseAverage.setText(lastBillInfo.getRate());
            textViewUseLength.setText(lastBillInfo.getDuration());
            textViewUse.setText(lastBillInfo.getUsageM3());

            textViewAbBaha.setText(lastBillInfo.getAbBaha());
            textViewTax.setText(lastBillInfo.getMaliat());
            textViewDate.setText(lastBillInfo.getDeadLine());
            textViewCost.setText(lastBillInfo.getPayable());

            textViewPreDebtOrOwe.setText(lastBillInfo.getPreDebtOrOwe());
            textViewTakalifBoodje.setText(lastBillInfo.getBoodje());
            textViewKarmozdeFazelab.setText(lastBillInfo.getKarmozdFazelab());

            setImageBitmap(imageViewBarcode, lastBillInfo.getPayable());

            isPayed = lastBillInfo.isPayed();
        }
    }
}
