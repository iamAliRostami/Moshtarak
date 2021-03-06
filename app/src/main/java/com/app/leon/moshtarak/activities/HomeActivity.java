package com.app.leon.moshtarak.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Debug;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.app.leon.moshtarak.BaseItems.BaseActivity;
import com.app.leon.moshtarak.MyApplication;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.databinding.HomeContentBinding;

public class HomeActivity extends BaseActivity {
    HomeContentBinding binding;
    View.OnClickListener onClickListener = view -> {
        Intent intent;
        switch (view.getId()) {
            case R.id.imageButtonSuggest:
            case R.id.textViewSuggest:
                intent = new Intent(getApplicationContext(), SuggestActivity.class);
                startActivity(intent);
                break;
            case R.id.imageButtonTrain:
            case R.id.textViewTrain:
                intent = new Intent(getApplicationContext(), LearningActivity.class);
                startActivity(intent);
                break;
            case R.id.imageButtonLastBill:
            case R.id.textViewLastBill:
//                intent = new Intent(getApplicationContext(), LastBillActivity.class);
                intent = new Intent(getApplicationContext(), LastBillFileActivity.class);
                startActivity(intent);
                break;
            case R.id.imageButtonKardex:
            case R.id.textViewKardex:
                intent = new Intent(getApplicationContext(), CardexActivity.class);
                startActivity(intent);
                break;
            case R.id.imageButtonTracking:
            case R.id.textViewTracking:
                intent = new Intent(getApplicationContext(), FollowUpActivity.class);
                startActivity(intent);
                break;
            case R.id.imageButtonMamoor:
            case R.id.textViewMamoor:
                intent = new Intent(getApplicationContext(), SetCounterActivity.class);
                startActivity(intent);
                break;
            case R.id.imageButtonSale:
            case R.id.textViewSale:
                intent = new Intent(getApplicationContext(), SaleActivity.class);
                startActivity(intent);
                break;
            case R.id.imageButtonHelp:
            case R.id.textViewHelp:
                intent = new Intent(getApplicationContext(), HelpActivity.class);
                startActivity(intent);
                break;
            case R.id.imageButtonSupport:
            case R.id.textViewSupport:
                intent = new Intent(getApplicationContext(), AfterSaleServicesActivity.class);
                startActivity(intent);
                break;
        }
    };

    @SuppressLint({"HardwareIds", "MissingPermission", "CutPasteId"})
    @Override
    protected void initialize() {
        binding = HomeContentBinding.inflate(getLayoutInflater());
        View childLayout = binding.getRoot();
        ConstraintLayout parentLayout = findViewById(R.id.base_Content);
        parentLayout.addView(childLayout);
        MyApplication.isHome = true;
        setOnClickListener();
    }

    void setOnClickListener() {
        binding.imageButtonSale.setOnClickListener(onClickListener);
        binding.imageButtonLastBill.setOnClickListener(onClickListener);
        binding.imageButtonKardex.setOnClickListener(onClickListener);
        binding.imageButtonTrain.setOnClickListener(onClickListener);
        binding.imageButtonMamoor.setOnClickListener(onClickListener);
        binding.imageButtonSuggest.setOnClickListener(onClickListener);
        binding.imageButtonTracking.setOnClickListener(onClickListener);
        binding.imageButtonHelp.setOnClickListener(onClickListener);
        binding.imageButtonSupport.setOnClickListener(onClickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.isHome = true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        MyApplication.isHome = true;
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
}
