package com.leon.nestools;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.leon.nestools.databinding.MainActivityBinding;

public class MainActivity extends AppCompatActivity {
    MainActivityBinding binding;
    ProgressDialog progressDialog;
    Context context;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MainActivityBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(R.layout.main_activity);
        initialize();
    }

    @SuppressLint("SetJavaScriptEnabled")
    void initialize() {
        context = this;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(getString(R.string.waiting));
        progressDialog.setCancelable(false);
        progressDialog.show();
        binding.webView.setWebChromeClient(new WebChromeClient());
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.getSettings().setBuiltInZoomControls(true);
//        binding.webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//        WebSettings settings = binding.webView.getSettings()