package com.app.leon.moshtarak.Activities;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Debug;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.app.leon.moshtarak.BaseItems.BaseActivity;
import com.app.leon.moshtarak.Infrastructure.IAbfaService;
import com.app.leon.moshtarak.Infrastructure.ICallback;
import com.app.leon.moshtarak.Models.DbTables.LastBillInfo;
import com.app.leon.moshtarak.Models.DbTables.LastBillInfoV2;
import com.app.leon.moshtarak.Models.Enums.BundleEnum;
import com.app.leon.moshtarak.Models.Enums.DialogType;
import com.app.leon.moshtarak.Models.Enums.ProgressType;
import com.app.leon.moshtarak.Models.Enums.SharedReferenceKeys;
import com.app.leon.moshtarak.Models.InterCommunation.SimpleMessage;
import com.app.leon.moshtarak.MyApplication;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.Utils.Code128;
import com.app.leon.moshtarak.Utils.CustomDialog;
import com.app.leon.moshtarak.Utils.HttpClientWrapperNew;
import com.app.leon.moshtarak.Utils.NetworkHelper;
import com.app.leon.moshtarak.Utils.SharedPreference;
import com.app.leon.moshtarak.databinding.LastBillContentBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.top.lib.mpl.view.PaymentInitiator;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Retrofit;

public class LastBillActivity extends BaseActivity {
    public static final int REQUEST_CODE_PAYMENT_BILL = 199;
    static LastBillInfoV2 lastBillInfo;
    LastBillContentBinding binding;
    Context context;
    String billId, payId, apiKey;
    boolean isPayed = false;
    boolean isFromCardex = false;
    boolean isLastBill = false;

    @SuppressLint("CutPasteId")
    @Override
    protected void initialize() {
        binding = LastBillContentBinding.inflate(getLayoutInflater());
        View childLayout = binding.getRoot();
        ConstraintLayout parentLayout = findViewById(R.id.base_Content);
        parentLayout.addView(childLayout);
        context = this;
        accessData();
        binding.textViewCost.setOnClickListener(view -> {
            if (!isPayed) {
//                new CustomTab(address, LastBillActivity.this);
                getToken();
            } else
                Toast.makeText(MyApplication.getContext(),
                        MyApplication.getContext().getString(R.string.payed_2),
                        Toast.LENGTH_SHORT).show();
        });
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
            apiKey = sharedPreference.getArrayList(SharedReferenceKeys.API_KEY.getValue()).
                    get(sharedPreference.getIndex());
            Toast.makeText(MyApplication.getContext(), getString(R.string.active_user).
                    concat(billId), Toast.LENGTH_LONG).show();
            fillLastBillInfo();
        }
    }

    void fillLatBillFromCounter(LastBillInfo lastBillInfo) {
        float floatNumber;
        int intNumber;
        binding.textViewBillId.setText(billId);
        binding.textViewPayId.setText(payId);
        setImageBitmap(binding.imageViewBarcode);
        floatNumber = Float.parseFloat(Objects.requireNonNull(lastBillInfo.getPayable()));
        intNumber = (int) floatNumber;
        binding.textViewCost.setText(String.valueOf(intNumber));
        floatNumber = Float.parseFloat(Objects.requireNonNull(lastBillInfo.getCurrentReadingNumber()));
        intNumber = (int) floatNumber;
        binding.textViewNewNumber.setText(String.valueOf(intNumber));
        floatNumber = Float.parseFloat(Objects.requireNonNull(lastBillInfo.getPreReadingNumber()));
        intNumber = (int) floatNumber;
        binding.textViewPreNumber.setText(String.valueOf(intNumber));
        binding.textViewNewDate.setText(lastBillInfo.getCurrentReadingDate());
        binding.textViewPreDate.setText(lastBillInfo.getPreReadingDate());
        floatNumber = Float.parseFloat(Objects.requireNonNull(lastBillInfo.getRate()));
        intNumber = (int) floatNumber;
        binding.textViewUseAverage.setText(String.valueOf(intNumber));
        floatNumber = Float.parseFloat(Objects.requireNonNull(lastBillInfo.getDuration()));
        intNumber = (int) floatNumber;
        binding.textViewUseLength.setText(String.valueOf(intNumber));
        floatNumber = Float.parseFloat(Objects.requireNonNull(lastBillInfo.getUsageM3()));
        intNumber = (int) floatNumber;
        binding.textViewUseM3.setText(String.valueOf(intNumber));

        floatNumber = Float.parseFloat(Objects.requireNonNull(lastBillInfo.getUsageLiter()));
        intNumber = (int) floatNumber;
        binding.textViewUseLitr.setText(String.valueOf(intNumber));

        floatNumber = Float.parseFloat(Objects.requireNonNull(lastBillInfo.getPreDebtOrOwe()));
        intNumber = (int) floatNumber;
        binding.textViewPreDebtOrOwe.setText(String.valueOf(intNumber));
        floatNumber = Float.parseFloat(Objects.requireNonNull(lastBillInfo.getBoodje()));
        intNumber = (int) floatNumber;
        binding.textViewTakalifBoodje.setText(String.valueOf(intNumber));
        floatNumber = Float.parseFloat(Objects.requireNonNull(lastBillInfo.getKarmozdFazelab()));
        intNumber = (int) floatNumber;
        binding.textViewKarmozdeFazelab.setText(String.valueOf(intNumber));
        floatNumber = Float.parseFloat(Objects.requireNonNull(lastBillInfo.getAbBaha()));
        intNumber = (int) floatNumber;
        binding.textViewAbBaha.setText(String.valueOf(intNumber));
        floatNumber = Float.parseFloat(Objects.requireNonNull(lastBillInfo.getMaliat()));
        intNumber = (int) floatNumber;
        binding.textViewTax.setText(String.valueOf(intNumber));
        floatNumber = Float.parseFloat(Objects.requireNonNull(lastBillInfo.getMazadOlgoo()));
        intNumber = (int) floatNumber;
        binding.textViewMazadOlgoo.setText(String.valueOf(intNumber));
        floatNumber = Float.parseFloat(Objects.requireNonNull(lastBillInfo.getTabsare2()));
        intNumber = (int) floatNumber;
        binding.textViewTabsare2.setText(String.valueOf(intNumber));
        floatNumber = Float.parseFloat(Objects.requireNonNull(lastBillInfo.getTabsare3Ab()));
        intNumber = (int) floatNumber;
        binding.textViewTabsare3Ab.setText(String.valueOf(intNumber));
        floatNumber = Float.parseFloat(Objects.requireNonNull(lastBillInfo.getTabsare3Fazelab()));
        intNumber = (int) floatNumber;
        binding.textViewTabsare3Fazelab.setText(String.valueOf(intNumber));
        floatNumber = Float.parseFloat(Objects.requireNonNull(lastBillInfo.getAbonmanAb()));
        intNumber = (int) floatNumber;
        binding.textViewAbonmanAb.setText(String.valueOf(intNumber));
        floatNumber = Float.parseFloat(Objects.requireNonNull(lastBillInfo.getAbonmanFazelab()));
        intNumber = (int) floatNumber;
        binding.textViewAbonmanFazelab.setText(String.valueOf(intNumber));
        floatNumber = Float.parseFloat(Objects.requireNonNull(lastBillInfo.getFasleGarm()));
        intNumber = (int) floatNumber;
        binding.textViewFasleGarm.setText(String.valueOf(intNumber));
        floatNumber = Float.parseFloat(Objects.requireNonNull(lastBillInfo.getJam()));
        intNumber = (int) floatNumber;
        binding.textViewTotal.setText(String.valueOf(intNumber));
        binding.textViewDate.setText(lastBillInfo.getDeadLine());

        androidx.appcompat.widget.LinearLayoutCompat linearLayoutCompat =
                findViewById(R.id.linearLayoutCompatPayable1);
        linearLayoutCompat.setVisibility(View.GONE);
        if (isPayed) {
            linearLayoutCompat = findViewById(R.id.linearLayoutCompatPayable2);
            linearLayoutCompat.setVisibility(View.GONE);
        }
    }

    void fillLastBillInfo() {
        if (getIntent().getExtras() != null) {
            Bundle bundle1 = getIntent().getBundleExtra(BundleEnum.DATA.getValue());//setCounter
            Bundle bundle2 = getIntent().getBundleExtra(BundleEnum.THIS_BILL.getValue());//cardex_no_payed
            Bundle bundle3 = getIntent().getBundleExtra(BundleEnum.THIS_BILL_PAYED.getValue());//cardex_payed
            if (bundle1 != null) {
                String json = bundle1.getString(BundleEnum.LAST_BILL_FROM_COUNTER.getValue());
                Gson gson = new GsonBuilder().create();
                LastBillInfo lastBillInfo = gson.fromJson(json, LastBillInfo.class);

                billId = lastBillInfo.getBillId();
                payId = lastBillInfo.getPayId();
                isPayed = lastBillInfo.isPayed();

                fillLatBillFromCounter(lastBillInfo);
            } else {
                isFromCardex = true;
                Retrofit retrofit = NetworkHelper.getInstance();
                final IAbfaService getLastBillInfo = retrofit.create(IAbfaService.class);
                Call<LastBillInfoV2> call = null;
                if (bundle2 != null) {
                    Objects.requireNonNull(getSupportActionBar()).setTitle("مشاهده قبض");
                    isPayed = false;
                    call = getLastBillInfo.getThisBillInfo(
                            bundle2.getString(BundleEnum.ID.getValue()),
                            bundle2.getString(BundleEnum.ZONE_ID.getValue()));
                } else if (bundle3 != null) {
                    Objects.requireNonNull(getSupportActionBar()).setTitle("اطلاعات پرداخت");
                    isPayed = true;
                    call = getLastBillInfo.getThisPayInfo(
                            bundle3.getString(BundleEnum.ID.getValue()),
                            bundle3.getString(BundleEnum.ZONE_ID.getValue()));
                }
                ThisBill thisBill = new ThisBill();
                HttpClientWrapperNew.callHttpAsync(call, thisBill, context,
                        ProgressType.SHOW.getValue());
            }
        } else {
            isLastBill = true;
            Retrofit retrofit = NetworkHelper.getInstance();
            final IAbfaService getThisBillInfo = retrofit.create(IAbfaService.class);
            byte[] encodeValue = Base64.encode(billId.getBytes(), Base64.DEFAULT);
            String base64 = new String(encodeValue);
            Call<LastBillInfoV2> call = getThisBillInfo.getLastBillInfo(billId, base64.substring(0,
                    base64.length() - 1));
            ThisBill thisBill = new ThisBill();
            HttpClientWrapperNew.callHttpAsync(call, thisBill, context, ProgressType.SHOW.getValue());
        }
        if (isPayed)
            binding.textViewIsPayed.setText(context.getString(R.string.payed_2));
    }

    void setImageBitmap(ImageView imageView) {
        Code128 code = new Code128(this);
        String barcode = "";
        for (int count = 0; count < 13 - billId.length(); count++) {
            barcode = barcode.concat("0");
        }
        barcode = barcode.concat(billId);
        for (int count = 0; count < 13 - payId.length(); count++) {
            barcode = barcode.concat("0");
        }
        barcode = barcode.concat(payId);
        code.setData(barcode);
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        assert wm != null;
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;
        Bitmap bitmap = code.getBitmap(width, height / 8);
        imageView.setImageBitmap(bitmap);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.imageViewBarcode.setImageDrawable(null);
        binding = null;
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

    @Override
    protected void onPause() {
        super.onPause();
        Runtime.getRuntime().totalMemory();
        Runtime.getRuntime().freeMemory();
        Runtime.getRuntime().maxMemory();
        Debug.getNativeHeapAllocatedSize();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_bill) {
            Gson gson = new Gson();
            String json = gson.toJson(lastBillInfo);
            Bundle bundle = new Bundle();
            bundle.putString(BundleEnum.LAST_BILL_TO_FILE.getValue(), json);
            Intent intent = new Intent(getApplicationContext(), LastBillFileActivity.class);
            intent.putExtra(BundleEnum.DATA.getValue(), bundle);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isLastBill)
            getMenuInflater().inflate(R.menu.bill, menu);
        return true;
    }


    class ThisBill implements ICallback<LastBillInfoV2> {
        @SuppressLint("DefaultLocale")
        @Override
        public void execute(LastBillInfoV2 lastBillInfo) {

            LastBillActivity.lastBillInfo = lastBillInfo;
            androidx.appcompat.widget.LinearLayoutCompat linearLayoutCompat;
            if (isFromCardex && isPayed) {
                //اگر از کاردکس بود و پرداخت شده بود
                ScrollView scrollView = findViewById(R.id.scrollView1);
                scrollView.setVisibility(View.GONE);
                scrollView = findViewById(R.id.scrollView2);
                scrollView.setVisibility(View.VISIBLE);
                String amount = String.valueOf(lastBillInfo.getAmount());
                binding.textViewPayableN.setText(amount.substring(0, amount.indexOf(".")));
                binding.textViewPayableW.setText(lastBillInfo.getPayableReadable());
                binding.textViewPayDate.setText(lastBillInfo.getPayDay());
                binding.textViewPayTypeTitle.setText(lastBillInfo.getPayTypeTitle());
                binding.textViewBankTitle.setText(lastBillInfo.getBankTitle());
            } else if (isLastBill) {
                //اگر از آخرین قبض بود
                billId = lastBillInfo.getBillId().trim();
                payId = lastBillInfo.getPayId().trim();
                binding.textViewBillId.setText(billId);
                binding.textViewPayId.setText(payId);//TODO Sepehr
                setImageBitmap(binding.imageViewBarcode);
                //float floatNumber = Float.parseFloat(lastBillInfo.getCurrentReadingNumber());TODO
                float floatNumber = Float.parseFloat(lastBillInfo.getCurrentCounterNumber());
                int intNumber = (int) floatNumber;
                binding.textViewNewNumber.setText(String.valueOf(intNumber));

//                floatNumber = Float.parseFloat(lastBillInfo.getPreReadingNumber());TODO
                floatNumber = Float.parseFloat(lastBillInfo.getPreCounterNumber());
                intNumber = (int) floatNumber;
                binding.textViewPreNumber.setText(String.valueOf(intNumber));

                floatNumber = Float.parseFloat(lastBillInfo.getPayable());
                intNumber = (int) floatNumber;
                binding.textViewCost.setText(String.valueOf(intNumber));

//                binding.textViewNewDate.setText(lastBillInfo.getCurrentReadingDate());TODO
                binding.textViewNewDate.setText(lastBillInfo.getCurrentCounterReadingDate());
//                binding.textViewPreDate.setText(lastBillInfo.getPreReadingDate());TODO
                binding.textViewPreDate.setText(lastBillInfo.getPreCounterReadingDate());

//                floatNumber = Float.parseFloat(lastBillInfo.getRate());TODO
                floatNumber = Float.parseFloat(lastBillInfo.getMasrafAverage());
                intNumber = (int) floatNumber;
                binding.textViewUseAverage.setText(String.valueOf(intNumber));

//                binding.textViewUseLength.setText(lastBillInfo.getDuration());TODO
                binding.textViewUseLength.setText(lastBillInfo.getDays());

//                floatNumber = Float.parseFloat(lastBillInfo.getUsageM3());TODO
                floatNumber = Float.parseFloat(lastBillInfo.getMasraf());
                intNumber = (int) floatNumber;
                binding.textViewUseM3.setText(String.valueOf(intNumber));

                floatNumber = Float.parseFloat(lastBillInfo.getMasrafLiter());
                intNumber = (int) floatNumber;
                binding.textViewUseLitr.setText(String.valueOf(intNumber));

//                floatNumber = Float.parseFloat(lastBillInfo.getAbBahaDetail());//TODO
                floatNumber = Float.parseFloat(lastBillInfo.getAbBaha());
                intNumber = (int) floatNumber;
                binding.textViewAbBaha.setText(String.valueOf(intNumber));

                floatNumber = Float.parseFloat(lastBillInfo.getLavazemKahande());
                intNumber = (int) floatNumber;
                binding.textViewLavzemKahande.setText(String.valueOf(intNumber));

                floatNumber = Float.parseFloat(lastBillInfo.getTaxfif());
                intNumber = (int) floatNumber;
                binding.textViewTaxfif.setText(String.valueOf(intNumber));

                floatNumber = Float.parseFloat(lastBillInfo.getMaliat());
                intNumber = (int) floatNumber;
                binding.textViewTax.setText(String.valueOf(intNumber));

                binding.textViewDate.setText(lastBillInfo.getDeadLine());

//                floatNumber = Float.parseFloat(lastBillInfo.getPreDebtOrOwe());TODO
                floatNumber = Float.parseFloat(lastBillInfo.getPreBedOrBes());
                intNumber = (int) floatNumber;
                binding.textViewPreDebtOrOwe.setText(String.valueOf(intNumber));

//                floatNumber = Float.parseFloat(lastBillInfo.getBoodje());TODO
                floatNumber = Float.parseFloat(lastBillInfo.getBudget());
                intNumber = (int) floatNumber;
                binding.textViewTakalifBoodje.setText(String.valueOf(intNumber));

//                floatNumber = Float.parseFloat(lastBillInfo.getKarmozdFazelabDetails());TODO
                floatNumber = Float.parseFloat(lastBillInfo.getKarmozdFazelab());
                intNumber = (int) floatNumber;
                binding.textViewKarmozdeFazelab.setText(String.valueOf(intNumber));

                floatNumber = Float.parseFloat(lastBillInfo.getJam());
                intNumber = (int) floatNumber;
                binding.textViewTotal.setText(String.valueOf(intNumber));

                isPayed = lastBillInfo.isPayed();
                linearLayoutCompat = findViewById(R.id.linearLayoutCompatTabsare2);
                linearLayoutCompat.setVisibility(View.GONE);
                linearLayoutCompat = findViewById(R.id.linearLayoutCompatTabsare3Ab);
                linearLayoutCompat.setVisibility(View.GONE);
                linearLayoutCompat = findViewById(R.id.linearLayoutCompatTabsare3Fazelab);
                linearLayoutCompat.setVisibility(View.GONE);
                linearLayoutCompat = findViewById(R.id.linearLayoutCompatAbonmanAb);
                linearLayoutCompat.setVisibility(View.GONE);
                linearLayoutCompat = findViewById(R.id.linearLayoutCompatAbonmanFazelab);
                linearLayoutCompat.setVisibility(View.GONE);
                linearLayoutCompat = findViewById(R.id.linearLayoutCompatFasleGarm);
                linearLayoutCompat.setVisibility(View.GONE);
                linearLayoutCompat = findViewById(R.id.linearLayoutCompatMazadOlgoo);
                linearLayoutCompat.setVisibility(View.GONE);

            } else {
                //از آخرین قبض پرداخت نشده بود
                Log.e("status", "From Cardex, not payed Or New Counter");
                billId = lastBillInfo.getBillId().trim();
                payId = lastBillInfo.getPayId().trim();
                binding.textViewBillId.setText(billId);
                binding.textViewPayId.setText(payId);
                setImageBitmap(binding.imageViewBarcode);
                float floatNumber = Float.parseFloat(lastBillInfo.getCurrentReadingNumber());
                int intNumber = (int) floatNumber;
                binding.textViewNewNumber.setText(String.valueOf(intNumber));

                floatNumber = Float.parseFloat(lastBillInfo.getPreReadingNumber());
                intNumber = (int) floatNumber;
                binding.textViewPreNumber.setText(String.valueOf(intNumber));

                floatNumber = Float.parseFloat(lastBillInfo.getPayable());
                intNumber = (int) floatNumber;
                binding.textViewCost.setText(String.valueOf(intNumber));

                binding.textViewNewDate.setText(lastBillInfo.getCurrentReadingDate());
                binding.textViewPreDate.setText(lastBillInfo.getPreReadingDate());

                floatNumber = Float.parseFloat(lastBillInfo.getRate());
                intNumber = (int) floatNumber;
                binding.textViewUseAverage.setText(String.valueOf(intNumber));

                binding.textViewUseLength.setText(lastBillInfo.getDuration());

                floatNumber = Float.parseFloat(lastBillInfo.getUsageM3());
                intNumber = (int) floatNumber;
                binding.textViewUseM3.setText(String.valueOf(intNumber));

                floatNumber = Float.parseFloat(lastBillInfo.getUsageLiter());
                intNumber = (int) floatNumber;
                binding.textViewUseLitr.setText(String.valueOf(intNumber));

                floatNumber = Float.parseFloat(lastBillInfo.getAbBahaDetail());
                intNumber = (int) floatNumber;
                binding.textViewAbBaha.setText(String.valueOf(intNumber));

                floatNumber = Float.parseFloat(lastBillInfo.getMaliat());
                intNumber = (int) floatNumber;
                binding.textViewTax.setText(String.valueOf(intNumber));

                binding.textViewDate.setText(lastBillInfo.getDeadLine());

                floatNumber = Float.parseFloat(lastBillInfo.getPreDebtOrOwe());
                intNumber = (int) floatNumber;
                binding.textViewPreDebtOrOwe.setText(String.valueOf(intNumber));

                floatNumber = Float.parseFloat(lastBillInfo.getBoodje());
                intNumber = (int) floatNumber;
                binding.textViewTakalifBoodje.setText(String.valueOf(intNumber));

                floatNumber = Float.parseFloat(lastBillInfo.getKarmozdFazelabDetails());
                intNumber = (int) floatNumber;

                binding.textViewKarmozdeFazelab.setText(String.valueOf(intNumber));

                floatNumber = Float.parseFloat(lastBillInfo.getJam());
                intNumber = (int) floatNumber;
                binding.textViewTotal.setText(String.valueOf(intNumber));

                isPayed = lastBillInfo.isPayed();
                if (!isFromCardex) {
                    linearLayoutCompat = findViewById(R.id.linearLayoutCompatTabsare2);
                    linearLayoutCompat.setVisibility(View.GONE);
                    linearLayoutCompat = findViewById(R.id.linearLayoutCompatTabsare3Ab);
                    linearLayoutCompat.setVisibility(View.GONE);
                    linearLayoutCompat = findViewById(R.id.linearLayoutCompatTabsare3Fazelab);
                    linearLayoutCompat.setVisibility(View.GONE);
                    linearLayoutCompat = findViewById(R.id.linearLayoutCompatAbonmanAb);
                    linearLayoutCompat.setVisibility(View.GONE);
                    linearLayoutCompat = findViewById(R.id.linearLayoutCompatAbonmanFazelab);
                    linearLayoutCompat.setVisibility(View.GONE);
                    linearLayoutCompat = findViewById(R.id.linearLayoutCompatFasleGarm);
                    linearLayoutCompat.setVisibility(View.GONE);
                    linearLayoutCompat = findViewById(R.id.linearLayoutCompatMazadOlgoo);
                    linearLayoutCompat.setVisibility(View.GONE);
                }
                if (isFromCardex) {
                    floatNumber = Float.parseFloat(lastBillInfo.getTabsare2());
                    intNumber = (int) floatNumber;
                    binding.textViewTabsare2.setText(String.valueOf(intNumber));
                    floatNumber = Float.parseFloat(lastBillInfo.getTabsare3Ab());
                    intNumber = (int) floatNumber;
                    binding.textViewTabsare3Ab.setText(String.valueOf(intNumber));
                    floatNumber = Float.parseFloat(lastBillInfo.getTabsare3Fazelab());
                    intNumber = (int) floatNumber;
                    binding.textViewTabsare3Fazelab.setText(String.valueOf(intNumber));
                    floatNumber = Float.parseFloat(lastBillInfo.getAbonmanAb());
                    intNumber = (int) floatNumber;
                    binding.textViewAbonmanAb.setText(String.valueOf(intNumber));
                    floatNumber = Float.parseFloat(lastBillInfo.getAbonmanFazelab());
                    intNumber = (int) floatNumber;
                    binding.textViewAbonmanFazelab.setText(String.valueOf(intNumber));
                    floatNumber = Float.parseFloat(lastBillInfo.getFasleGarm());
                    intNumber = (int) floatNumber;
                    binding.textViewFasleGarm.setText(String.valueOf(intNumber));
                    floatNumber = Float.parseFloat(lastBillInfo.getMazadOlgoo());
                    intNumber = (int) floatNumber;
                    binding.textViewMazadOlgoo.setText(String.valueOf(intNumber));
                }
            }
            if (isFromCardex) {
                linearLayoutCompat = findViewById(R.id.linearLayoutCompatIDS);
                linearLayoutCompat.setVisibility(View.GONE);
                linearLayoutCompat = findViewById(R.id.linearLayoutCompatPayable1);
                linearLayoutCompat.setVisibility(View.GONE);
                linearLayoutCompat = findViewById(R.id.linearLayoutCompatPayable2);
                linearLayoutCompat.setVisibility(View.GONE);
                linearLayoutCompat = findViewById(R.id.linearLayoutCompatPay);
                linearLayoutCompat.setVisibility(View.GONE);
                linearLayoutCompat = findViewById(R.id.linearLayoutCompatTaxfif);
                linearLayoutCompat.setVisibility(View.GONE);
                linearLayoutCompat = findViewById(R.id.linearLayoutCompatKahande);
                linearLayoutCompat.setVisibility(View.GONE);
            }
            if (isPayed) {
                binding.textViewIsPayed.setText(context.getString(R.string.payed_2));
                linearLayoutCompat = findViewById(R.id.linearLayoutCompatIDS);
                linearLayoutCompat.setVisibility(View.GONE);
                linearLayoutCompat = findViewById(R.id.linearLayoutCompatPayable1);
                linearLayoutCompat.setVisibility(View.GONE);
                linearLayoutCompat = findViewById(R.id.linearLayoutCompatPayable2);
                linearLayoutCompat.setVisibility(View.GONE);
            }
        }
    }

    void getToken() {
        Retrofit retrofit = NetworkHelper.getInstance();
        final IAbfaService getToken = retrofit.create(IAbfaService.class);
        Call<SimpleMessage> call = getToken.getToken(apiKey);
        GetToken getToken1 = new GetToken();
        HttpClientWrapperNew.callHttpAsync(call, getToken1, context, ProgressType.SHOW.getValue());
    }

    void pay(String simpleMessage) {
        Intent intent = new Intent(LastBillActivity.this, PaymentInitiator.class);
        intent.putExtra("Type", "2");
        intent.putExtra("Token", simpleMessage);
        startActivityForResult(intent, REQUEST_CODE_PAYMENT_BILL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PAYMENT_BILL) {
            getPaymentResultCode(resultCode, data);
        }
    }

    private void getPaymentResultCode(int resultCode, Intent data) {
//
//        if (G.onMplResult != null) {
//            G.onMplResult.onResult(false);
//        }

        String enData = "", message = "", status = "0";
        int errorType = 0, orderId = 0;

        switch (resultCode) {
            case 1:// payment ok
            case 3://bill payment ok
                enData = data.getStringExtra("enData");
                message = data.getStringExtra("message");
                status = String.valueOf(data.getIntExtra("status", 0));
                break;
            case 2://payment error
            case 5://internal error payment
                errorType = data.getIntExtra("errorType", 0);
                orderId = data.getIntExtra("OrderID", 0);
                break;
            case 4://bill payment error
            case 6://internal error bill
            case 9:// internal error charge
                errorType = data.getIntExtra("errorType", 0);
                break;
        }
        if (errorType != 0) {
            showErrorTypeMpl(errorType);
        } else {
            new CustomDialog(DialogType.Yellow, context, message, context.getString(R.string.dear_user),
                    context.getString(R.string.pay), context.getString(R.string.accepted));
        }
    }

    private void showErrorTypeMpl(int errorType) {
        String message = "";
        switch (errorType) {
            case 2:
                message = getString(R.string.time_out_error);
                break;
            case 1000:
                message = getString(R.string.connection_error);
                break;
            case 1001:
                message = getString(R.string.server_error);
                break;
            case 1002:
                message = getString(R.string.network_error);
                break;
            case 201:
                message = getString(R.string.dialog_canceled);
                break;
            case 2334:
                message = getString(R.string.device_root);
                break;
        }

        if (message.length() > 0) {
            Toast.makeText(MyApplication.getContext(), message, Toast.LENGTH_LONG).show();
        }
    }

    class GetToken implements ICallback<SimpleMessage> {
        @Override
        public void execute(SimpleMessage simpleMessage) {
            pay(simpleMessage.getMessage());
        }
    }
}
