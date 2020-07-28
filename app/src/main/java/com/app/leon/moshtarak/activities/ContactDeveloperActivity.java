package com.app.leon.moshtarak.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.app.leon.moshtarak.databinding.ContactDeveloperActivityBinding;

public class ContactDeveloperActivity extends AppCompatActivity {
    ContactDeveloperActivityBinding binding;
    View.OnClickListener onClickListener = view -> {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"ali.rostami33@gmail.com",
                "infoshamsaii@gmail.com"});//, "mantera.sh@gmail.com"
        startActivity(Intent.createChooser(intent, "Choose an Email client :"));
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ContactDeveloperActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.linearLayout1.setOnClickListener(onClickListener);
        binding.linearLayout2.setOnClickListener(onClickListener);
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
    protected void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().totalMemory();
        Runtime.getRuntime().freeMemory();
        Runtime.getRuntime().maxMemory();
        Debug.getNativeHeapAllocatedSize();
    }
}
