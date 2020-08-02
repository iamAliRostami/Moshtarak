package com.app.leon.moshtarak.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.app.leon.moshtarak.Models.Enums.BundleEnum;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.databinding.PayFragmentBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PayFragment extends DialogFragment {

    PayFragmentBinding binding;
    View view;
    private PayViewModel mViewModel;

    public static PayFragment newInstance(PayViewModel payViewModel) {
        PayFragment payFragment = new PayFragment();
        Bundle bundle = new Bundle();
        Gson gson = new Gson();
        String json = gson.toJson(payViewModel);
        bundle.putString(BundleEnum.PAY_VIEW_MODEL.getValue(), json);
        payFragment.setArguments(bundle);
        return payFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = PayFragmentBinding.inflate(inflater, container, false);
        binding.setPayViewModel(mViewModel);
        initialize();
        return binding.getRoot();
    }

    @SuppressLint("SetJavaScriptEnabled")
    void initialize() {
        binding.webViewPay.getSettings().setJavaScriptEnabled(true);
        binding.webViewPay.getSettings().setBuiltInZoomControls(true);
        binding.webViewPay.loadUrl(getString(R.string.mellat_site));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String json = getArguments().getString(BundleEnum.PAY_VIEW_MODEL.getValue());
            Gson gson = new GsonBuilder().create();
            mViewModel = gson.fromJson(json, PayViewModel.class);
        }
    }

    @Override
    public void onResume() {
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();

        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes(params);
        super.onResume();
    }
}
