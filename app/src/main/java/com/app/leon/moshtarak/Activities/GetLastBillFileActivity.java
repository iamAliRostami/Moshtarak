package com.app.leon.moshtarak.Activities;

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
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.app.leon.moshtarak.Models.DbTables.LastBillInfoV2;
import com.app.leon.moshtarak.Models.Enums.BundleEnum;
import com.app.leon.moshtarak.MyApplication;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.Utils.Code128;
import com.app.leon.moshtarak.Utils.CustomProgressBar;
import com.app.leon.moshtarak.databinding.GetLastBillFileActivityBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class GetLastBillFileActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 626;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION_FOR_SEND = 621;
    GetLastBillFileActivityBinding binding;
    String imageName, billId, payId;
    Context context;
    Bitmap bitmapBill;
    Code128 code128;
    Paint tPaint;
    LastBillInfoV2 lastBillInfo;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = GetLastBillFileActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        context = this;

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        initialize();
    }

    @SuppressLint("SimpleDateFormat")
    void initialize() {

        Log.e("size", getFontSize(context));
        imageName = "bill_".concat((new SimpleDateFormat("yyyyMMdd_HHmmss")).
                format(new Date())).concat(".jpg");
        tPaint = new Paint();
        tPaint.setTextSize(33);

        tPaint.setTypeface(Typeface.createFromAsset(context.getAssets(), MyApplication.getFontName()));
        tPaint.setStyle(Paint.Style.FILL);

        new ThisBill(context).execute();

        binding.buttonShare.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    new SendImages(context, bitmapBill).execute();
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION_FOR_SEND);
                }
            } else {
                Toast.makeText(context, R.string.error_android_version, Toast.LENGTH_LONG).show();
            }
        });

        binding.buttonSave.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    new SaveImages(context, bitmapBill).execute();
                } else {
                    ActivityCompat.requestPermissions(this,
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
        });
    }

    String getFontSize(Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        if (density >= 4.0) {
            return "xxxhdpi";
        }
        if (density >= 3.0) {
            return "xxhdpi";
        }
        if (density >= 2.0) {
            return "xhdpi";
        }
        if (density >= 1.5) {
            return "hdpi";
        }
        if (density >= 1.0) {
            return "mdpi";
        }
        return "ldpi";
    }

    public Uri getImageUri(Bitmap src, Bitmap.CompressFormat format, int quality) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        src.compress(format, quality, os);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), src, "bill", null);
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

        tPaint.setColor(Color.BLUE);
        float xCoordinate = (float) src.getWidth() * 55 / 100;
        float yCoordinate = (float) src.getHeight() * 20 / 100;
        cs.drawText(lastBillInfo.getFullName(), xCoordinate, yCoordinate, tPaint);
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

        tPaint.setColor(getResources().getColor(R.color.pink3));
        xCoordinate = (float) src.getWidth() * 3 / 80;
        yCoordinate = (float) src.getHeight() * 53 / 100;
        cs.drawText(lastBillInfo.getKarbariTitle(), xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 113 / 200;
        floatNumber = Float.parseFloat(lastBillInfo.getAhadMaskooni());
        intNumber = (int) floatNumber;
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

    @SuppressLint("SdCardPath")
    void createImageToShow(LastBillInfoV2 lastBillInfo) {
        float floatNumber;
        int intNumber;

        Bitmap src = BitmapFactory.decodeResource(getResources(), R.drawable.bill_2);
        Bitmap dest = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas cs = new Canvas(dest);
        cs.drawBitmap(src, 0f, 0f, null);

        tPaint.setColor(Color.BLUE);
        float xCoordinate = (float) src.getWidth() / 10;
        float yCoordinate = (float) src.getHeight() * 18 / 400;
        cs.drawText(lastBillInfo.getFullName(), xCoordinate, yCoordinate, tPaint);
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
        cs.drawText(lastBillInfo.getKarbariTitle(), xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 327 / 400;
        floatNumber = Float.parseFloat(lastBillInfo.getAhadMaskooni());
        intNumber = (int) floatNumber;
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
        runOnUiThread(() -> binding.imageViewLastBill.setImageBitmap(dest));
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

    @SuppressLint("StaticFieldLeak")
    class SaveImages extends AsyncTask<Object, Object, Object> {
        CustomProgressBar customProgressBar;
        Bitmap bitmap;
        Context context;

        public SaveImages(Context context, Bitmap bitmap) {
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
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            File f = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES) + File.separator + imageName);
            try {
                if (f.createNewFile()) {
                    FileOutputStream fo = new FileOutputStream(f);
                    fo.write(bytes.toByteArray());
                    runOnUiThread(() -> Toast.makeText(context, R.string.saved, Toast.LENGTH_LONG).show());
                }
            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(context, R.string.error_preparing, Toast.LENGTH_LONG).show());
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    class SendImages extends AsyncTask<Object, Object, Object> {
        CustomProgressBar customProgressBar;
        Bitmap bitmap;
        Context context;

        public SendImages(Context context, Bitmap bitmap) {
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
//        share.setType("application/pdf");
            share.putExtra(Intent.EXTRA_STREAM, getImageUri(bitmap, Bitmap.CompressFormat.JPEG, 100));
            startActivity(Intent.createChooser(share, getString(R.string.send_to)));
        }
    }

    @SuppressLint("StaticFieldLeak")
    class ThisBill extends AsyncTask<Object, Object, Object> {
        CustomProgressBar customProgressBar;
        Context context;
        LastBillInfoV2 lastBillInfoV2;

        public ThisBill(Context context) {
            this.context = context;
            customProgressBar = new CustomProgressBar();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            customProgressBar.show(context, getString(R.string.waiting), false, dialog -> {
                Toast.makeText(MyApplication.getContext(),
                        MyApplication.getContext().getString(R.string.canceled),
                        Toast.LENGTH_LONG).show();
                customProgressBar.getDialog().dismiss();
                finish();
            });
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            customProgressBar.getDialog().dismiss();
        }

        @Override
        protected Object doInBackground(Object... objects) {
            if (getIntent().getExtras() != null) {
                Bundle bundle1 = getIntent().getBundleExtra(BundleEnum.DATA.getValue());
                if (bundle1 != null) {
                    String json = bundle1.getString(BundleEnum.LAST_BILL_TO_FILE.getValue());
                    Gson gson = new GsonBuilder().create();
                    lastBillInfoV2 = gson.fromJson(json, LastBillInfoV2.class);
                    billId = lastBillInfoV2.getBillId();
                    payId = lastBillInfoV2.getPayId();

                    code128 = setCode128();

                    lastBillInfo = lastBillInfoV2;
                    createImageToShow(lastBillInfo);
                    createImagePrintable(lastBillInfo);
                }
            }
            return null;
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        context = null;

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
    private class GetImages extends AsyncTask<Object, Object, Object> {
        private String requestUrl;
        private ImageView view;
        private Bitmap bitmap;
        private FileOutputStream fos;
        CustomProgressBar customProgressBar;

        private GetImages(String requestUrl, ImageView view, String imageName) {
            this.requestUrl = requestUrl;
            this.view = view;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            customProgressBar = new CustomProgressBar();
            customProgressBar.show(context, getString(R.string.waiting), true);
        }

        @Override
        protected Object doInBackground(Object... objects) {
            try {
                URL url = new URL(requestUrl);
                URLConnection conn = url.openConnection();
                bitmap = BitmapFactory.decodeStream(conn.getInputStream());
            } catch (Exception ex) {
                Log.e("Error", Objects.requireNonNull(ex.getMessage()));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            view.setImageBitmap(bitmap);
            customProgressBar.getDialog().dismiss();
        }

    }

    void saveAndSend() {
        Bitmap icon = ((BitmapDrawable) binding.imageViewLastBill.getDrawable()).getBitmap();
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//            File f = new File(Environment.getExternalStorageDirectory() +
//                    File.separator + imageName);
        File f = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES) + File.separator + imageName);
        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

        share.putExtra(Intent.EXTRA_STREAM, getImageUri(icon, Bitmap.CompressFormat.JPEG, 100));
        startActivity(Intent.createChooser(share, "Share Image"));
    }
}