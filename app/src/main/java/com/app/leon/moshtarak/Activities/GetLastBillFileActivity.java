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
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.Utils.Code128;
import com.app.leon.moshtarak.Utils.CustomProgressBar;
import com.app.leon.moshtarak.databinding.GetLastBillFileActivityBinding;

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
    static String child = "bill";
    String imageUrl = "http://crm.abfaesfahan.ir/assets/imgs/bigger.png", imageName;
    GetLastBillFileActivityBinding binding;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 626;// int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 101;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION_FOR_SEND = 621;
    Context context;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = GetLastBillFileActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        context = this;
        initialize();
    }

    @SuppressLint("SimpleDateFormat")
    void initialize() {
        imageName = "bill_".concat((new SimpleDateFormat("yyyyMMdd_HHmmss")).
                format(new Date())).concat(".jpg");
        createImagePrintable();
        binding.buttonShare.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    sendImage();
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION_FOR_SEND);
                }
            } else {
                sendImage();
            }
        });
        binding.buttonSave.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    saveImage();
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
                }
            } else {
                saveImage();
            }
        });
    }

    void sendImage() {
        Bitmap bitmap = ((BitmapDrawable) binding.imageViewLastBill.getDrawable()).getBitmap();
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        share.putExtra(Intent.EXTRA_STREAM, getImageUri(bitmap, Bitmap.CompressFormat.JPEG, 100));
        startActivity(Intent.createChooser(share, getString(R.string.send_to)));
    }

    void saveImage() {
        Bitmap bitmap = ((BitmapDrawable) binding.imageViewLastBill.getDrawable()).getBitmap();
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File f = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES) + File.separator + imageName);
        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            Toast.makeText(context, R.string.saved, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Uri getImageUri(Bitmap src, Bitmap.CompressFormat format, int quality) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        src.compress(format, quality, os);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), src, "bill", null);
        return Uri.parse(path);
    }

    @SuppressLint("SdCardPath")
    void createImagePrintable() {
        Bitmap src = BitmapFactory.decodeResource(getResources(), R.drawable.bill_1); // the original file yourimage.jpg i added in resources
        Bitmap dest = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);
        String text = " تست تست";

        Canvas cs = new Canvas(dest);
        Paint tPaint = new Paint();
        tPaint.setTextSize(100);
        tPaint.setStyle(Paint.Style.FILL);
        cs.drawBitmap(src, 0f, 0f, null);

        tPaint.setColor(Color.BLUE);
        float xCoordinate = (float) src.getWidth() * 55 / 100;
        float yCoordinate = (float) src.getHeight() * 20 / 100;
        cs.drawText(text, xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 24 / 100;
        cs.drawText(text, xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 55 / 200;
        cs.drawText(text, xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 31 / 100;
        cs.drawText(text, xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 35 / 100;
        cs.drawText(text, xCoordinate, yCoordinate, tPaint);


        tPaint.setColor(getResources().getColor(R.color.green2));
        xCoordinate = (float) src.getWidth() * 1 / 11;
        yCoordinate = (float) src.getHeight() * 22 / 100;
        cs.drawText(text, xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 55 / 200;
        cs.drawText(text, xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 33 / 100;
        cs.drawText(text, xCoordinate, yCoordinate, tPaint);

        tPaint.setColor(getResources().getColor(R.color.orange1));
        xCoordinate = (float) src.getWidth() * 55 / 80;//27/40
        yCoordinate = (float) src.getHeight() * 53 / 100;
        cs.drawText(text, xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 113 / 200;
        cs.drawText(text, xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 121 / 200;
        cs.drawText(text, xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 64 / 100;
        cs.drawText(text, xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 67 / 100;
        cs.drawText(text, xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 71 / 100;
        cs.drawText(text, xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 75 / 100;
        cs.drawText(text, xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 79 / 100;
        cs.drawText(text, xCoordinate, yCoordinate, tPaint);

        tPaint.setColor(getResources().getColor(R.color.red4));
        xCoordinate = (float) src.getWidth() * 29 / 80;
        yCoordinate = (float) src.getHeight() * 53 / 100;
        cs.drawText(text, xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 113 / 200;
        cs.drawText(text, xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 121 / 200;
        cs.drawText(text, xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 64 / 100;
        cs.drawText(text, xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 67 / 100;
        cs.drawText(text, xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 71 / 100;
        cs.drawText(text, xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 75 / 100;
        cs.drawText(text, xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 79 / 100;
        cs.drawText(text, xCoordinate, yCoordinate, tPaint);

        tPaint.setColor(getResources().getColor(R.color.pink2));
        xCoordinate = (float) src.getWidth() * 3 / 80;
        yCoordinate = (float) src.getHeight() * 53 / 100;
        cs.drawText(text, xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 113 / 200;
        cs.drawText(text, xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 121 / 200;
        cs.drawText(text, xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 64 / 100;
        cs.drawText(text, xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 67 / 100;
        cs.drawText(text, xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 71 / 100;
        cs.drawText(text, xCoordinate, yCoordinate, tPaint);
        yCoordinate = (float) src.getHeight() * 75 / 100;
        cs.drawText(text, xCoordinate, yCoordinate, tPaint);

        Code128 code = new Code128(context);
        String barcode = "90182736451627384958273847";
        code.setData(barcode);
        xCoordinate = (float) src.getWidth() / 10;
        yCoordinate = (float) src.getHeight() * 90 / 100;
        cs.drawBitmap(code.getBitmap(src.getWidth() * 8 / 10, src.getHeight() / 15), xCoordinate, yCoordinate, tPaint);

        binding.imageViewLastBill.setImageBitmap(dest);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveImage();
            } else {
                Toast.makeText(context, R.string.no_access, Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION_FOR_SEND) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendImage();
            } else {
                Toast.makeText(context, R.string.no_access, Toast.LENGTH_LONG).show();
            }
        }
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