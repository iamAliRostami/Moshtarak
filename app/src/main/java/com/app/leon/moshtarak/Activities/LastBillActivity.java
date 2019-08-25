package com.app.leon.moshtarak.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
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
    @BindView(R.id.textViewAbBahaDetail)
    TextView textViewabBahaDetail;
    @BindView(R.id.textViewTabsare2)
    TextView textViewTabsare2;
    @BindView(R.id.textViewTabsare3Ab)
    TextView textViewTabsare3Ab;
    @BindView(R.id.textViewTabsare3Fazelab)
    TextView textViewTabsare3Fazelab;
    @BindView(R.id.textViewAbonmanAb)
    TextView textViewAbonmanAb;
    @BindView(R.id.textViewAbonmanFazelab)
    TextView textViewAbonmanFazelab;
    @BindView(R.id.textViewFasleGarm)
    TextView textViewFasleGarm;
    @BindView(R.id.textViewMazadOlgoo)
    TextView textViewMazadOlgoo;
    @BindView(R.id.textViewKarmozdFazelabDetails)
    TextView textViewKarmozdFazelabDetails;

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
    boolean isFromCardex = false;

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
        accessData();
        textViewCost.setOnClickListener(view -> {
            if (!isPayed)
                new CustomTab(address, LastBillActivity.this);
            else
                Toast.makeText(context, context.getString(R.string.payed), Toast.LENGTH_SHORT).show();
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
            fillLastBillInfo();
        }
    }

    void fillLastBillInfo() {
        if (getIntent().getExtras() != null) {
            Bundle bundle1 = getIntent().getBundleExtra(BundleEnum.DATA.getValue());
            Bundle bundle2 = getIntent().getBundleExtra(BundleEnum.THIS_BILL.getValue());
            if (bundle1 != null) {
                float floatNumber;
                int intNumber;
                textViewBillId.setText(bundle1.getString(BundleEnum.BILL_ID.getValue()));
                textViewPayId.setText(bundle1.getString(BundleEnum.PAY_ID.getValue()));
                floatNumber = Float.valueOf(bundle1.getString(BundleEnum.NEW.getValue()));
                intNumber = (int) floatNumber;
                textViewNewNumber.setText(String.valueOf(intNumber));
                floatNumber = Float.valueOf(bundle1.getString(BundleEnum.PRE.getValue()));
                intNumber = (int) floatNumber;
                textViewPreNumber.setText(String.valueOf(intNumber));

                textViewNewDate.setText(bundle1.getString(BundleEnum.CURRENT_READING_DATE.getValue()));
                textViewPreDate.setText(bundle1.getString(BundleEnum.PRE_READING_DATE.getValue()));

                floatNumber = Float.valueOf(bundle1.getString(BundleEnum.USE_AVERAGE.getValue()));
                intNumber = (int) floatNumber;
                textViewUseAverage.setText(String.valueOf(intNumber));
                floatNumber = Float.valueOf(bundle1.getString(BundleEnum.USE_LENGTH.getValue()));
                intNumber = (int) floatNumber;
                textViewUseLength.setText(String.valueOf(intNumber));
                floatNumber = Float.valueOf(bundle1.getString(BundleEnum.USE.getValue()));
                intNumber = (int) floatNumber;
                textViewUse.setText(String.valueOf(intNumber));

                floatNumber = Float.valueOf(bundle1.getString(BundleEnum.PRE_DEBT_OR_OWE.getValue()));
                intNumber = (int) floatNumber;
                textViewPreDebtOrOwe.setText(String.valueOf(intNumber));

                floatNumber = Float.valueOf(bundle1.getString(BundleEnum.TAKALIF_BOODJE.getValue()));
                intNumber = (int) floatNumber;
                textViewTakalifBoodje.setText(String.valueOf(intNumber));

                floatNumber = Float.valueOf(bundle1.getString(BundleEnum.KARMOZDE_FAZELAB.getValue()));
                intNumber = (int) floatNumber;
                textViewKarmozdeFazelab.setText(String.valueOf(intNumber));

                floatNumber = Float.valueOf(bundle1.getString(BundleEnum.AB_BAHA.getValue()));
                intNumber = (int) floatNumber;
                textViewAbBaha.setText(String.valueOf(intNumber));
                floatNumber = Float.valueOf(bundle1.getString(BundleEnum.TAX.getValue()));
                intNumber = (int) floatNumber;
                textViewTax.setText(String.valueOf(intNumber));

                textViewDate.setText(bundle1.getString(BundleEnum.DATE.getValue()));

                floatNumber = Float.valueOf(bundle1.getString(BundleEnum.COST.getValue()));
                intNumber = (int) floatNumber;
                textViewCost.setText(String.valueOf(intNumber));
                setImageBitmap(imageViewBarcode, String.valueOf(intNumber));

                isPayed = bundle1.getBoolean(BundleEnum.IS_PAYED.getValue());

            } else if (bundle2 != null) {
                isFromCardex = true;
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
        @SuppressLint("DefaultLocale")
        @Override
        public void execute(LastBillInfo lastBillInfo) {
            textViewBillId.setText(lastBillInfo.getBillId().trim());
            textViewPayId.setText(lastBillInfo.getPayId().trim());
            float floatNumber = Float.valueOf(lastBillInfo.getCurrentReadingNumber());
            int intNumber = (int) floatNumber;

            textViewNewNumber.setText(String.valueOf(intNumber));

            floatNumber = Float.valueOf(lastBillInfo.getPreReadingNumber());
            intNumber = (int) floatNumber;
            textViewPreNumber.setText(String.valueOf(intNumber));

            textViewNewDate.setText(lastBillInfo.getCurrentReadingDate());
            textViewPreDate.setText(lastBillInfo.getPreReadingDate());

            floatNumber = Float.valueOf(lastBillInfo.getRate());
            intNumber = (int) floatNumber;
            textViewUseAverage.setText(String.valueOf(intNumber));

            textViewUseLength.setText(lastBillInfo.getDuration());

            floatNumber = Float.valueOf(lastBillInfo.getUsageM3());
            intNumber = (int) floatNumber;
            textViewUse.setText(String.valueOf(intNumber));

            floatNumber = Float.valueOf(lastBillInfo.getAbBaha());
            intNumber = (int) floatNumber;
            textViewAbBaha.setText(String.valueOf(intNumber));

            floatNumber = Float.valueOf(lastBillInfo.getMaliat());
            intNumber = (int) floatNumber;
            textViewTax.setText(String.valueOf(intNumber));

            textViewDate.setText(lastBillInfo.getDeadLine());

            floatNumber = Float.valueOf(lastBillInfo.getPayable());
            intNumber = (int) floatNumber;
            textViewCost.setText(String.valueOf(intNumber));
            setImageBitmap(imageViewBarcode, String.valueOf(intNumber));

            floatNumber = Float.valueOf(lastBillInfo.getPreDebtOrOwe());
            intNumber = (int) floatNumber;
            textViewPreDebtOrOwe.setText(String.valueOf(intNumber));

            floatNumber = Float.valueOf(lastBillInfo.getBoodje());
            intNumber = (int) floatNumber;
            textViewTakalifBoodje.setText(String.valueOf(intNumber));

            floatNumber = Float.valueOf(lastBillInfo.getKarmozdFazelab());
            intNumber = (int) floatNumber;

            floatNumber = Float.valueOf(lastBillInfo.getKarmozdFazelabDetails());
            intNumber = (int) floatNumber;

            textViewKarmozdeFazelab.setText(String.valueOf(intNumber));

            isPayed = lastBillInfo.isPayed();
            if (isFromCardex) {
                androidx.appcompat.widget.LinearLayoutCompat linearLayoutCompat;
                linearLayoutCompat = findViewById(R.id.abBahaDetail);
                linearLayoutCompat.setVisibility(View.VISIBLE);
                linearLayoutCompat = findViewById(R.id.tabsare2);
                linearLayoutCompat.setVisibility(View.VISIBLE);
                linearLayoutCompat = findViewById(R.id.tabsare3Ab);
                linearLayoutCompat.setVisibility(View.VISIBLE);
                linearLayoutCompat = findViewById(R.id.tabsare3Fazelab);
                linearLayoutCompat.setVisibility(View.VISIBLE);
                linearLayoutCompat = findViewById(R.id.abonmanAb);
                linearLayoutCompat.setVisibility(View.VISIBLE);
                linearLayoutCompat = findViewById(R.id.abonmanFazelab);
                linearLayoutCompat.setVisibility(View.VISIBLE);
                linearLayoutCompat = findViewById(R.id.fasleGarm);
                linearLayoutCompat.setVisibility(View.VISIBLE);
                linearLayoutCompat = findViewById(R.id.mazadOlgoo);
                linearLayoutCompat.setVisibility(View.VISIBLE);
//                linearLayoutCompat = findViewById(R.id.karmozdFazelabDetails);
//                linearLayoutCompat.setVisibility(View.VISIBLE);

                floatNumber = Float.valueOf(lastBillInfo.getAbBahaDetail());
                intNumber = (int) floatNumber;
                textViewabBahaDetail.setText(String.valueOf(intNumber));
                floatNumber = Float.valueOf(lastBillInfo.getTabsare2());
                intNumber = (int) floatNumber;
                textViewTabsare2.setText(String.valueOf(intNumber));
                floatNumber = Float.valueOf(lastBillInfo.getTabsare3Ab());
                intNumber = (int) floatNumber;
                textViewTabsare3Ab.setText(String.valueOf(intNumber));
                floatNumber = Float.valueOf(lastBillInfo.getTabsare3Fazelab());
                intNumber = (int) floatNumber;
                textViewTabsare3Fazelab.setText(String.valueOf(intNumber));
                floatNumber = Float.valueOf(lastBillInfo.getAbonmanAb());
                intNumber = (int) floatNumber;
                textViewAbonmanAb.setText(String.valueOf(intNumber));
                floatNumber = Float.valueOf(lastBillInfo.getAbonmanFazelab());
                intNumber = (int) floatNumber;
                textViewAbonmanFazelab.setText(String.valueOf(intNumber));
                floatNumber = Float.valueOf(lastBillInfo.getFasleGarm());
                intNumber = (int) floatNumber;
                textViewFasleGarm.setText(String.valueOf(intNumber));
                floatNumber = Float.valueOf(lastBillInfo.getMazadOlgoo());
                intNumber = (int) floatNumber;
                textViewMazadOlgoo.setText(String.valueOf(intNumber));
//                floatNumber = Float.valueOf(lastBillInfo.getKarmozdFazelabDetails());
//                intNumber = (int) floatNumber;
//                textViewKarmozdFazelabDetails.setText(String.valueOf(intNumber));
            }
        }
    }
}
