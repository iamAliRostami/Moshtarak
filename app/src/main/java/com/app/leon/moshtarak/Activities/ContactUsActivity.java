package com.app.leon.moshtarak.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.app.leon.moshtarak.databinding.ContactUsActivityBinding;

public class ContactUsActivity extends AppCompatActivity {
    Context context;

    ContactUsActivityBinding binding;
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == binding.linearLayout4.getId()) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"info@abfaisfahan.ir"});
                startActivity(Intent.createChooser(intent, "Choose an Email client :"));
                return;
            }
            String phone = "";
            if (id == binding.linearLayout1.getId()) {
                phone = "122";
            } else if (id == binding.linearLayout2.getId())
                phone = "1522";
            else if (id == binding.linearLayout3.getId())
                phone = "03136680030";
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:".concat(phone)));
            if (ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                Log.e("call permission :", "Denied");
                intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
                return;
            }
            startActivity(intent);

        }
    };

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        super.onCreate(savedInstanceState);
        binding = ContactUsActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        context = this;

        binding.linearLayout1.setOnClickListener(onClickListener);
        binding.linearLayout2.setOnClickListener(onClickListener);
        binding.linearLayout3.setOnClickListener(onClickListener);
        binding.linearLayout4.setOnClickListener(onClickListener);
        binding.linearLayout5.setOnClickListener(onClickListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        binding = null;
        context = null;

        Runtime.getRuntime().totalMemory();
        Runtime.getRuntime().freeMemory();
        Runtime.getRuntime().maxMemory();
        Debug.getNativeHeapAllocatedSize();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
        context = null;

        Runtime.getRuntime().totalMemory();
        Runtime.getRuntime().freeMemory();
        Runtime.getRuntime().maxMemory();
        Debug.getNativeHeapAllocatedSize();
    }
}
