package com.app.leon.moshtarak.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.app.leon.moshtarak.Infrastructure.IAbfaService;
import com.app.leon.moshtarak.Infrastructure.ICallback;
import com.app.leon.moshtarak.Infrastructure.ICallbackError;
import com.app.leon.moshtarak.Infrastructure.ICallbackIncomplete;
import com.app.leon.moshtarak.Models.DbTables.LastBillInfoV2;
import com.app.leon.moshtarak.Models.DbTables.PayData;
import com.app.leon.moshtarak.Models.Enums.BundleEnum;
import com.app.leon.moshtarak.Models.Enums.DialogType;
import com.app.leon.moshtarak.Models.Enums.ProgressType;
import com.app.leon.moshtarak.Models.Enums.SharedReferenceKeys;
import com.app.leon.moshtarak.MyApplication;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.Utils.Code128;
import com.app.leon.moshtarak.Utils.CustomDialog;
import com.app.leon.moshtarak.Utils.CustomErrorHandlingNew;
import com.app.leon.moshtarak.Utils.CustomProgressBar;
import com.app.leon.moshtarak.Utils.CustomTab;
import com.app.leon.moshtarak.Utils.HttpClientWrapper;
import com.app.leon.moshtarak.Utils.NetworkHelper;
import com.app.leon.moshtarak.Utils.SharedPreference;
import com.app.leon.moshtarak.databinding.LastBillFileActivityBinding;
import com.top.lib.mpl.view.PaymentInitiator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LastBillFileActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 626;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION_FOR_SEND = 621;
    private static final int REQUEST_CODE_PAYMENT_BILL = 199;
    static LastBillInfoV2 lastBillInfo;
    SharedPreference sharedPreference;
    LastBillFileActivityBinding binding;
    String imageName, billId, payId, apiKey;
    Context context;
    Bitmap bitmapBill;
    Code128 code128;
    Paint tPaint;
    boolean isBill = true;
    //    int small = 40, medium = 50, large = 70, huge = 100;
    int small = 40, medium = 50, large = 23, huge = 33;
    View.OnClickListener onClickListenerSave = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    new SaveImages(context, bitmapBill).execute();
                } else {
                    ActivityCompat.requestPermissions(LastBillFileActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
                }
            } else {
                try {
                    new SaveImages(context, bitmapBill).execute();
                } catch (Exception e) {
                    Toast.makeText(context, R.string.error_preparing, Toast.LENGTH_LONG).show();
                }
            }
        }
    };
    View.OnClickListener onClickListenerShare = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    new SendImages(context, bitmapBill).execute();
                } else {
                    ActivityCompat.requestPermissions(LastBillFileActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION_FOR_SEND);
                }
            } else {
                Toast.makeText(context, R.string.error_android_version, Toast.LENGTH_LONG).show();
            }
        }
    };
    View.OnClickListener onClickListenerPay = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //TODO
            if (!lastBillInfo.isPayed()) {
                getToken();
                //TODO
//                new CustomTab(getString(R.string.mellat_site), MyApplication.getContext());
            } else
                Toast.makeText(MyApplication.getContext(),
                        MyApplication.getContext().getString(R.string.payed_2),
                        Toast.LENGTH_SHORT).show();
        }
    };

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LastBillFileActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        context = this;
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        initialize();
    }

    @SuppressLint("SimpleDateFormat")
    void initialize() {
        imageName = "bill_".concat((new SimpleDateFormat("yyyyMMdd_HHmmss")).
                format(new Date())).concat(".jpg");
        tPaint = new Paint();
        tPaint.setTypeface(Typeface.createFromAsset(context.getAssets(), MyApplication.getFontName()));
        tPaint.setStyle(Paint.Style.FILL);
        accessData();
        onClickListener();
    }

    void onClickListener() {
        binding.buttonShare.setOnClickListener(onClickListenerShare);
        binding.buttonSave.setOnClickListener(onClickListenerSave);
        binding.buttonPay.setOnClickListener(onClickListenerPay);

        binding.imageViewShare.setOnClickListener(onClickListenerShare);
        binding.imageViewSave.setOnClickListener(onClickListenerSave);
        binding.imageViewPay.setOnClickListener(onClickListenerPay);

        binding.linearLayoutShare.setOnClickListener(onClickListenerShare);
        binding.linearLayoutSave.setOnClickListener(onClickListenerSave);
        binding.linearLayoutPay.setOnClickListener(onClickListenerPay);
    }

    public Uri getImageUri(Bitmap src, Bitmap.CompressFormat format, int quality) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        src.compress(format, quality, os);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), src, "bill",
                null);
        return Uri.parse(path);
    }

    @SuppressLint("SdCardPath")
    void createImagePrintable(LastBillInfoV2 lastBillInfo) {
        float floatNumber;
        int intNumber;
        Bitmap src = BitmapFactory.decodeResource(getResources(), R.drawable.bill_1); // the original file yourimage.jpg i added in resources
        Bitmap dest = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas cs = new Canvas(dest);
        cs.drawBitmap(src, 0f, 0f, null);

        tPaint.setTextSize(large);
        tPaint.setColor(Color.BLUE);
        float xCoordinate = (float) src.getWidth() * 55 / 100;
        float yCoordinate = (float) src.getHeight() * 20 / 100;
        cs.drawText(lastBillInfo.getFullName(), xCoordinate, yCoordinate, tPaint);

        tPaint.setTextSize(huge);
        yCoordinate = (float) src.getHeight() * 24 / 100;
        cs.drawText(lastBillInfo.getBillId(), xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 55 / 200;
        cs.drawText(lastBillInfo.getPayId(), xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 31 / 100;
        cs.drawText(lastBillInfo.getPayable(), xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 35 / 100;
        cs.drawText(lastBillInfo.getDeadLine(), xCoordinate, yCoordinate, tPaint);

        tPaint.setColor(getResources().getColor(R.color.green2));
        xCoordinate = (float) src.getWidth() / 10;
        yCoordinate = (float) src.getHeight() * 22 / 100;
        floatNumber = Float.parseFloat(lastBillInfo.getMasraf());
        intNumber = (int) floatNumber;
        cs.drawText(String.valueOf(intNumber), xCoordinate, yCoordinate, tPaint);

        yCoordinate = (float) src.getHeight() * 55 / 200;
        floatNumber = Float.parseFloat(lastBillInfo.getMasrafLiter());
        intNumber = (int) floatNumber;
        cs.drawText(String.valueOf(intNumber), xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 33 / 100;
        floatNumber = Float.parseFloat(lastBillInfo.getMasrafAverage());
        intNumber = (int) floatNumber;
        cs.drawText(String.valueOf(intNumber), xCoordinate, yCoordinate, tPaint);

        tPaint.setColor(getResources().getColor(R.color.orange1));
        xCoordinate = (float) src.getWidth() * 55 / 80;//27/40
        yCoordinate = (float) src.getHeight() * 53 / 100;
        floatNumber = Float.parseFloat(lastBillInfo.getAbBaha());
        intNumber = (int) floatNumber;
        cs.drawText(String.valueOf(intNumber), xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 113 / 200;
        floatNumber = Float.parseFloat(lastBillInfo.getKarmozdFazelab());
        intNumber = (int) floatNumber;
        cs.drawText(String.valueOf(intNumber), xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 121 / 200;
        floatNumber = Float.parseFloat(lastBillInfo.getMaliat());
        intNumber = (int) floatNumber;
        cs.drawText(String.valueOf(intNumber), xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 64 / 100;
        floatNumber = Float.parseFloat(lastBillInfo.getBudget());
        intNumber = (int) floatNumber;
        cs.drawText(String.valueOf(intNumber), xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 67 / 100;
        floatNumber = Float.parseFloat(lastBillInfo.getLavazemKahande());
        intNumber = (int) floatNumber;
        cs.drawText(String.valueOf(intNumber), xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 71 / 100;
        floatNumber = Float.parseFloat(lastBillInfo.getJam());
        intNumber = (int) floatNumber;
        cs.drawText(String.valueOf(intNumber), xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 75 / 100;
        floatNumber = Float.parseFloat(lastBillInfo.getTaxfif());
        intNumber = (int) floatNumber;
        cs.drawText(String.valueOf(intNumber), xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 79 / 100;
        floatNumber = Float.parseFloat(lastBillInfo.getPreBedOrBes());
        intNumber = (int) floatNumber;
        cs.drawText(String.valueOf(intNumber), xCoordinate, yCoordinate, tPaint);

        tPaint.setColor(getResources().getColor(R.color.brown));
        xCoordinate = (float) src.getWidth() * 29 / 80;
        yCoordinate = (float) src.getHeight() * 53 / 100;
        floatNumber = Float.parseFloat(lastBillInfo.getRadif());
        intNumber = (int) floatNumber;
        cs.drawText(String.valueOf(intNumber), xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 113 / 200;
        floatNumber = Float.parseFloat(lastBillInfo.getBarge());
        intNumber = (int) floatNumber;
        cs.drawText(String.valueOf(intNumber), xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 121 / 200;
        cs.drawText(lastBillInfo.getEshterak(), xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 64 / 100;
        cs.drawText(lastBillInfo.getPreCounterReadingDate(), xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 67 / 100;
        cs.drawText(lastBillInfo.getCurrentCounterReadingDate(), xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 71 / 100;
        cs.drawText(lastBillInfo.getDays(), xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 75 / 100;
        floatNumber = Float.parseFloat(lastBillInfo.getPreCounterNumber());
        intNumber = (int) floatNumber;
        cs.drawText(String.valueOf(intNumber), xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 79 / 100;
        floatNumber = Float.parseFloat(lastBillInfo.getCurrentCounterNumber());
        intNumber = (int) floatNumber;
        cs.drawText(String.valueOf(intNumber), xCoordinate, yCoordinate, tPaint);

        tPaint.setTextSize(large);
        tPaint.setColor(getResources().getColor(R.color.pink3));
        xCoordinate = (float) src.getWidth() * 3 / 80;
        yCoordinate = (float) src.getHeight() * 53 / 100;
        cs.drawText(lastBillInfo.getKarbariTitle(), xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 113 / 200;
        floatNumber = Float.parseFloat(lastBillInfo.getAhadMaskooni());
        intNumber = (int) floatNumber;
        tPaint.setTextSize(huge);
        cs.drawText(String.valueOf(intNumber), xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 121 / 200;
        floatNumber = Float.parseFloat(lastBillInfo.getAhadNonMaskooni());
        intNumber = (int) floatNumber;
        cs.drawText(String.valueOf(intNumber), xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 64 / 100;
        floatNumber = Float.parseFloat(lastBillInfo.getZarfiatQarardadi());
        intNumber = (int) floatNumber;
        cs.drawText(String.valueOf(intNumber), xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 67 / 100;
        cs.drawText(lastBillInfo.getQotr(), xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 71 / 100;
        cs.drawText(lastBillInfo.getQotrSifoon(), xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 75 / 100;
        cs.drawText(lastBillInfo.getCounterStateId(), xCoordinate, yCoordinate, tPaint);

        xCoordinate = (float) src.getWidth() / 10;
        yCoordinate = (float) src.getHeight() * 85 / 100;
        float finalXCoordinate = xCoordinate;
        float finalYCoordinate = yCoordinate;
        cs.drawBitmap(code128.getBitmap(src.getWidth() * 8 / 10,
                src.getHeight() / 10), finalXCoordinate, finalYCoordinate, tPaint);
        bitmapBill = dest;
    }

    void fillTextView(LastBillInfoV2 lastBillInfo) {
        binding.textView1.setText(lastBillInfo.getFullName());
        binding.textView2.setText(lastBillInfo.getBillId());
        binding.textView3.setText(lastBillInfo.getPayId());
        binding.textView4.setText(getNumberSeparator(lastBillInfo.getPayable()));
        binding.textView5.setText(lastBillInfo.getDeadLine());

        binding.textView6.setText(lastBillInfo.getMasraf());
        binding.textView7.setText(lastBillInfo.getMasrafLiter());
        binding.textView8.setText(lastBillInfo.getMasrafAverage());

        binding.textView9.setText(getNumberSeparator(lastBillInfo.getAbBaha()));
        binding.textView10.setText(getNumberSeparator(lastBillInfo.getKarmozdFazelab()));
        binding.textView11.setText(getNumberSeparator(lastBillInfo.getMaliat()));
        binding.textView12.setText(getNumberSeparator(lastBillInfo.getBudget()));
        binding.textView13.setText(getNumberSeparator(lastBillInfo.getLavazemKahande()));
        binding.textView14.setText(getNumberSeparator(lastBillInfo.getJam()));
        binding.textView15.setText(getNumberSeparator(lastBillInfo.getTaxfif()));
        binding.textView16.setText(getNumberSeparator(lastBillInfo.getPreBedOrBes()));

        binding.textView17.setText(lastBillInfo.getRadif());
        binding.textView18.setText(lastBillInfo.getBarge());
        binding.textView19.setText(lastBillInfo.getEshterak());
        binding.textView20.setText(lastBillInfo.getPreCounterReadingDate());
        binding.textView21.setText(lastBillInfo.getCurrentCounterReadingDate());
        binding.textView22.setText(lastBillInfo.getDays());
        binding.textView23.setText(lastBillInfo.getPreCounterNumber());
        binding.textView24.setText(lastBillInfo.getCurrentCounterNumber());

        binding.textView25.setText(lastBillInfo.getKarbariTitle());
        binding.textView26.setText(lastBillInfo.getAhadMaskooni());
        binding.textView27.setText(lastBillInfo.getAhadNonMaskooni());
        binding.textView28.setText(lastBillInfo.getZarfiatQarardadi());
        binding.textView29.setText(lastBillInfo.getQotr());
        binding.textView30.setText(lastBillInfo.getQotrSifoon());
        binding.textView31.setText(lastBillInfo.getCounterStateId());
    }

    @SuppressLint("DefaultLocale")
    public String getNumberSeparator(String number) {
        String s;
        try {
            s = String.format("%,d", Long.parseLong(number));
        } catch (NumberFormatException e) {
            s = number;
        }
        return s;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new SaveImages(context, bitmapBill).execute();
            } else {
                Toast.makeText(context, R.string.no_access, Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION_FOR_SEND) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new SendImages(context, bitmapBill).execute();
            } else {
                Toast.makeText(context, R.string.no_access, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void accessData() {
        sharedPreference = new SharedPreference(context);
        if (!sharedPreference.checkIsNotEmpty()) {
            Intent intent = new Intent(getApplicationContext(), RegisterAccountActivity.class);
            startActivity(intent);
            finish();
        } else {
            billId = sharedPreference.getArrayList(SharedReferenceKeys.BILL_ID.getValue()).
                    get(sharedPreference.getIndex());
            apiKey = sharedPreference.getArrayList(SharedReferenceKeys.API_KEY.getValue()).
                    get(sharedPreference.getIndex());
            String name = sharedPreference.getArrayList(SharedReferenceKeys.NAME.getValue()).
                    get(sharedPreference.getIndex());

            String alias = sharedPreference.getArrayList(SharedReferenceKeys.ALIAS.getValue()).
                    get(sharedPreference.getIndex());
            Toast.makeText(MyApplication.getContext(), getString(R.string.active_user_3).concat(
                    alias.length() > 0 ? alias : name),
                    Toast.LENGTH_LONG).show();
            fillLastBillInfo();
        }
    }

    void fillLastBillInfo() {
        Retrofit retrofit = NetworkHelper.getInstance();
        final IAbfaService getThisBillInfo = retrofit.create(IAbfaService.class);
        byte[] encodeValue = Base64.encode(billId.getBytes(), Base64.DEFAULT);
        String base64 = new String(encodeValue);
        Call<LastBillInfoV2> call = getThisBillInfo.getLastBillInfo(billId, base64.substring(0,
                base64.length() - 1));
        HttpClientWrapper.callHttpAsync(call, ProgressType.SHOW.getValue(), context, new GetBill(),
                new GetBillIncomplete(), new GetError());
    }

    Code128 setCode128() {
        Code128 code = new Code128(context);
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
        return code;
    }

    void getToken() {
        Retrofit retrofit = NetworkHelper.getInstance();
        final IAbfaService service = retrofit.create(IAbfaService.class);
        Call<PayData> call = service.getToken(apiKey);
        GetToken token = new GetToken();
        GetTokenIncomplete incomplete = new GetTokenIncomplete();
        GetError error = new GetError();
        HttpClientWrapper.callHttpAsync(call, ProgressType.SHOW.getValue(), context, token,
                incomplete, error);
    }

    void pay(String simpleMessage) {
        Intent intent = new Intent(LastBillFileActivity.this, PaymentInitiator.class);
        intent.putExtra(BundleEnum.TYPE.getValue(), "2");
        intent.putExtra(BundleEnum.TOKEN.getValue(), simpleMessage);
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
        String enData = "", message = "";
        int status = 0;
        int errorType = 0, orderId = 0;

        switch (resultCode) {
            case 1://payment ok
            case 3://bill payment ok
                enData = data.getStringExtra("enData");
                message = data.getStringExtra("message");
                status = data.getIntExtra("status", 0);
                break;
            case 2://payment error
            case 5://internal error payment
            case 4://bill payment error
            case 6://internal error bill
            case 9:// internal error charge
                errorType = data.getIntExtra("errorType", 0);
                break;
        }
        Log.e("resultCode", String.valueOf(resultCode));
        Log.e("status", String.valueOf(status));
        Log.e("errorType", String.valueOf(errorType));
        if (message != null) {
            Log.e("message", message);
        }

        if (errorType != 0) {
            showErrorTypeMpl(errorType);
        } else if ((resultCode == 1 || resultCode == 3) && status == 0) {
            new CustomDialog(DialogType.Green, LastBillFileActivity.this, message,
                    LastBillFileActivity.this.getString(R.string.dear_user),
                    LastBillFileActivity.this.getString(R.string.pay),
                    LastBillFileActivity.this.getString(R.string.accepted));

        } else {
            if (status < -32755 || status == -130 || status == 6 || (status >= 18 && status <= 32) ||
                    status == 66 || (status >= 87 && status <= 89) || (status >= 95)) {
                message = LastBillFileActivity.this.getString(R.string.error_unknown)
                        .concat(LastBillFileActivity.this.getString(R.string.error_code)
                                .concat(String.valueOf(status)));
            }
            new CustomDialog(DialogType.Yellow, LastBillFileActivity.this, message,
                    LastBillFileActivity.this.getString(R.string.dear_user),
                    LastBillFileActivity.this.getString(R.string.pay),
                    LastBillFileActivity.this.getString(R.string.accepted));
        }
    }

    private void showErrorTypeMpl(int errorType) {
        String message;
        switch (errorType) {
            case 2:
                message = getString(R.string.time_out_error);
                break;
            case 1000:
            case 1002:
                message = getString(R.string.error_other);
                break;
            case 1001:
                message = getString(R.string.server_error);
                break;
            case 201:
                message = getString(R.string.dialog_canceled);
                break;
            case 2334:
                message = getString(R.string.device_root);
                break;
            default:
                message = getString(R.string.error_unknown);
        }
        if (message.length() > 0) {
            Toast.makeText(MyApplication.getContext(), message, Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("SdCardPath")
    void createImageToShow(LastBillInfoV2 lastBillInfo) {
        float floatNumber;
        int intNumber;

        Bitmap src = BitmapFactory.decodeResource(getResources(), R.drawable.bill_1);
        Bitmap dest = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas cs = new Canvas(dest);
        cs.drawBitmap(src, 0f, 0f, null);

        tPaint.setColor(Color.BLUE);
        tPaint.setTextSize(small);
        float xCoordinate = (float) src.getWidth() / 10;
        float yCoordinate = (float) src.getHeight() * 18 / 400;
        cs.drawText(lastBillInfo.getFullName(), xCoordinate, yCoordinate, tPaint);
        tPaint.setTextSize(medium);
        yCoordinate = (float) src.getHeight() * 26 / 400;
        cs.drawText(lastBillInfo.getBillId(), xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 33 / 400;
        cs.drawText(lastBillInfo.getPayId(), xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 40 / 400;
        cs.drawText(lastBillInfo.getPayable(), xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 47 / 400;
        cs.drawText(lastBillInfo.getDeadLine(), xCoordinate, yCoordinate, tPaint);

        tPaint.setColor(getResources().getColor(R.color.green2));
        yCoordinate = (float) src.getHeight() * 71 / 400;
        floatNumber = Float.parseFloat(lastBillInfo.getMasraf());
        intNumber = (int) floatNumber;
        cs.drawText(String.valueOf(intNumber), xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 82 / 400;
        floatNumber = Float.parseFloat(lastBillInfo.getMasrafLiter());
        intNumber = (int) floatNumber;
        cs.drawText(String.valueOf(intNumber), xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 94 / 400;
        floatNumber = Float.parseFloat(lastBillInfo.getMasrafAverage());
        intNumber = (int) floatNumber;
        cs.drawText(String.valueOf(intNumber), xCoordinate, yCoordinate, tPaint);

        tPaint.setColor(getResources().getColor(R.color.orange1));
        yCoordinate = (float) src.getHeight() * 124 / 400;
        floatNumber = Float.parseFloat(lastBillInfo.getAbBaha());
        intNumber = (int) floatNumber;
        cs.drawText(String.valueOf(intNumber), xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 134 / 400;
        floatNumber = Float.parseFloat(lastBillInfo.getKarmozdFazelab());
        intNumber = (int) floatNumber;
        cs.drawText(String.valueOf(intNumber), xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 144 / 400;
        floatNumber = Float.parseFloat(lastBillInfo.getMaliat());
        intNumber = (int) floatNumber;
        cs.drawText(String.valueOf(intNumber), xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 154 / 400;
        floatNumber = Float.parseFloat(lastBillInfo.getBudget());
        intNumber = (int) floatNumber;
        cs.drawText(String.valueOf(intNumber), xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 163 / 400;
        floatNumber = Float.parseFloat(lastBillInfo.getLavazemKahande());
        intNumber = (int) floatNumber;
        cs.drawText(String.valueOf(intNumber), xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 173 / 400;
        floatNumber = Float.parseFloat(lastBillInfo.getJam());
        intNumber = (int) floatNumber;
        cs.drawText(String.valueOf(intNumber), xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 183 / 400;
        floatNumber = Float.parseFloat(lastBillInfo.getTaxfif());
        intNumber = (int) floatNumber;
        cs.drawText(String.valueOf(intNumber), xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 193 / 400;
        floatNumber = Float.parseFloat(lastBillInfo.getPreBedOrBes());
        intNumber = (int) floatNumber;
        cs.drawText(String.valueOf(intNumber), xCoordinate, yCoordinate, tPaint);

        tPaint.setColor(getResources().getColor(R.color.brown));
        yCoordinate = (float) src.getHeight() * 222 / 400;
        floatNumber = Float.parseFloat(lastBillInfo.getRadif());
        intNumber = (int) floatNumber;
        cs.drawText(String.valueOf(intNumber), xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 232 / 400;
        floatNumber = Float.parseFloat(lastBillInfo.getBarge());
        intNumber = (int) floatNumber;
        cs.drawText(String.valueOf(intNumber), xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 242 / 400;
        cs.drawText(lastBillInfo.getEshterak(), xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 252 / 400;
        cs.drawText(lastBillInfo.getPreCounterReadingDate(), xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 262 / 400;
        cs.drawText(lastBillInfo.getCurrentCounterReadingDate(), xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 272 / 400;//
        cs.drawText(lastBillInfo.getDays(), xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 282 / 400;
        floatNumber = Float.parseFloat(lastBillInfo.getPreCounterNumber());
        intNumber = (int) floatNumber;
        cs.drawText(String.valueOf(intNumber), xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 292 / 400;
        floatNumber = Float.parseFloat(lastBillInfo.getCurrentCounterNumber());
        intNumber = (int) floatNumber;
        cs.drawText(String.valueOf(intNumber), xCoordinate, yCoordinate, tPaint);

        tPaint.setColor(getResources().getColor(R.color.pink3));
        yCoordinate = (float) src.getHeight() * 318 / 400;

        tPaint.setTextSize(small);
        cs.drawText(lastBillInfo.getKarbariTitle(), xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 327 / 400;
        floatNumber = Float.parseFloat(lastBillInfo.getAhadMaskooni());
        intNumber = (int) floatNumber;
        tPaint.setTextSize(medium);
        cs.drawText(String.valueOf(intNumber), xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 335 / 400;
        floatNumber = Float.parseFloat(lastBillInfo.getAhadNonMaskooni());
        intNumber = (int) floatNumber;
        cs.drawText(String.valueOf(intNumber), xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 344 / 400;
        floatNumber = Float.parseFloat(lastBillInfo.getZarfiatQarardadi());
        intNumber = (int) floatNumber;
        cs.drawText(String.valueOf(intNumber), xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 352 / 400;
        cs.drawText(lastBillInfo.getQotr(), xCoordinate, yCoordinate, tPaint);

        yCoordinate = (float) src.getHeight() * 361 / 400;
        cs.drawText(lastBillInfo.getQotrSifoon(), xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 369 / 400;
        cs.drawText(lastBillInfo.getCounterStateId(), xCoordinate, yCoordinate, tPaint);

        xCoordinate = (float) src.getWidth() / 10;
        yCoordinate = (float) src.getHeight() * 191 / 200;
        float finalYCoordinate = yCoordinate;
        float finalXCoordinate = xCoordinate;
        cs.drawBitmap(code128.getBitmap(src.getWidth() * 8 / 10,
                src.getHeight() / 30), finalXCoordinate, finalYCoordinate, tPaint);
//        runOnUiThread(() -> binding.imageViewLastBill.setImageBitmap(dest));
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

    @SuppressLint("StaticFieldLeak")
    class SendImages extends AsyncTask<Object, Object, Object> {
        CustomProgressBar customProgressBar;
        Bitmap bitmap;
        Context context;

        public SendImages(Context context, Bitmap bitmap) {
            super();
            this.bitmap = bitmap;
            this.context = context;
            this.customProgressBar = new CustomProgressBar();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            customProgressBar.show(context, getString(R.string.waiting), false, dialog -> {
                Toast.makeText(MyApplication.getContext(),
                        MyApplication.getContext().getString(R.string.canceled),
                        Toast.LENGTH_LONG).show();
                customProgressBar.getDialog().dismiss();
            });
        }

        @Override
        protected Object doInBackground(Object... objects) {
            sendImage();
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            customProgressBar.getDialog().dismiss();
        }

        void sendImage() {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/jpeg");
            share.putExtra(Intent.EXTRA_STREAM, getImageUri(bitmap, Bitmap.CompressFormat.JPEG, 100));
            startActivity(Intent.createChooser(share, getString(R.string.send_to)));
        }
    }

    @SuppressLint("StaticFieldLeak")
    class SaveImages extends AsyncTask<Object, Object, Object> {
        CustomProgressBar customProgressBar;
        Bitmap bitmap;
        Context context;

        public SaveImages(Context context, Bitmap bitmap) {
            super();
            this.bitmap = bitmap;
            this.context = context;
            this.customProgressBar = new CustomProgressBar();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            customProgressBar.show(context, getString(R.string.waiting), false, dialog -> {
                Toast.makeText(MyApplication.getContext(),
                        MyApplication.getContext().getString(R.string.canceled),
                        Toast.LENGTH_LONG).show();
                customProgressBar.getDialog().dismiss();
            });
        }

        @Override
        protected Object doInBackground(Object... objects) {
            saveImage();
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            customProgressBar.getDialog().dismiss();
        }

        void saveImage() {
            String root = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES).toString() + File.separator + getString(R.string.app_name);
            File myDir = new File(root);
            if (!myDir.mkdirs()) return;

            File file = new File(myDir, imageName);
            System.out.println(file.getAbsolutePath());
            if (file.exists()) file.delete();
            Log.i("LOAD", root + imageName);
            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
                out.flush();
                out.close();
                runOnUiThread(() -> Toast.makeText(context, R.string.saved, Toast.LENGTH_LONG).show());
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(context, R.string.error_preparing, Toast.LENGTH_LONG).show());
            }
            MediaScannerConnection.scanFile(context, new String[]{file.getPath()}, new String[]{"image/jpeg"}, null);
        }
    }

    class GetToken implements ICallback<PayData> {
        @Override
        public void execute(PayData payData) {
            if (payData.isMpl)
                pay(payData.message);
            else
                new CustomTab(getString(R.string.mellat_site), MyApplication.getContext());
        }
    }

    class GetBill implements ICallback<LastBillInfoV2> {
        @SuppressLint("DefaultLocale")
        @Override
        public void execute(LastBillInfoV2 lastBillInfo) {
            payId = lastBillInfo.getPayId();
            code128 = setCode128();
            LastBillFileActivity.lastBillInfo = lastBillInfo;
            fillTextView(lastBillInfo);
            if (Build.VERSION.SDK_INT >= 23)
                createImagePrintable(lastBillInfo);
        }
    }

    class GetBillIncomplete implements ICallbackIncomplete<LastBillInfoV2> {
        @Override
        public void executeIncomplete(Response<LastBillInfoV2> response) {
            binding.textViewNotFound.setVisibility(View.VISIBLE);
            binding.linearLayoutBill.setVisibility(View.GONE);
            CustomErrorHandlingNew customErrorHandlingNew = new CustomErrorHandlingNew(context);
            String error = customErrorHandlingNew.getErrorMessageDefault(response);
            if (response.code() == 404) {
                error = LastBillFileActivity.this.getString(R.string.error_register_again);
                sharedPreference.removeItem(sharedPreference.getIndex());
            } else if (response.code() == 204) {
                error = LastBillFileActivity.this.getString(R.string.error_not_found);
            }
            new CustomDialog(DialogType.YellowRedirect, LastBillFileActivity.this, error,
                    LastBillFileActivity.this.getString(R.string.dear_user),
                    LastBillFileActivity.this.getString(R.string.last_bill_2),
                    LastBillFileActivity.this.getString(R.string.accepted));
        }
    }

    class GetTokenIncomplete implements ICallbackIncomplete<PayData> {
        @Override
        public void executeIncomplete(Response<PayData> response) {
            CustomErrorHandlingNew customErrorHandlingNew = new CustomErrorHandlingNew(context);
            String error = customErrorHandlingNew.getErrorMessageDefault(response);
            new CustomDialog(DialogType.Yellow, LastBillFileActivity.this, error,
                    LastBillFileActivity.this.getString(R.string.dear_user),
                    LastBillFileActivity.this.getString(R.string.pay),
                    LastBillFileActivity.this.getString(R.string.accepted));
        }
    }

    class GetError implements ICallbackError {
        @Override
        public void executeError(Throwable t) {
            if (isBill) {
                binding.textViewNotFound.setVisibility(View.VISIBLE);
                binding.linearLayoutBill.setVisibility(View.GONE);
                isBill = false;
            }
            CustomErrorHandlingNew customErrorHandlingNew = new CustomErrorHandlingNew(context);
            String error = customErrorHandlingNew.getErrorMessageTotal(t);
            new CustomDialog(DialogType.YellowRedirect, LastBillFileActivity.this, error,
                    LastBillFileActivity.this.getString(R.string.dear_user),
                    LastBillFileActivity.this.getString(R.string.pay),
                    LastBillFileActivity.this.getString(R.string.accepted));
        }
    }
}