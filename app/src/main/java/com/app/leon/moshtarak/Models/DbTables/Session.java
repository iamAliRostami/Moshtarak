package com.leon.nestools;

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
        binding = MainActivityBinding.inflate(getLayoutInf