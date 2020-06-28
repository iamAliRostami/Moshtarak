package com.app.leon.moshtarak.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.app.leon.moshtarak.BaseItems.BaseActivity;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.databinding.HomeContentBinding;

public class HomeActivity extends BaseActivity {
    HomeContentBinding binding;
    View.OnClickListener onClickListener = view -> {
        Intent intent;
        switch (view.getId()) {
            case R.id.imageButtonSuggest:
                intent = new Intent(getApplicationContext(), SuggestActivity.class);
                startActivity(intent);
                break;
            case R.id.imageButtonTrain:
                intent = new Intent(getApplicationContext(), LearningActivity.class);
                startActivity(intent);
                break;
            case R.id.imageButtonLastBill:
//                intent = new Intent(getApplicationContext(), LastBillActivity.class);
                intent = new Intent(getApplicationContext(), GetLastBillFileActivity.class);
                startActivity(intent);
                break;
            case R.id.imageButtonKardex:
                intent = new Intent(getApplicationContext(), CardexActivity.class);
                startActivity(intent);
                break;
            case R.id.imageButtonTracking:
                intent = new Intent(getApplicationContext(), FollowUpActivity.class);
                startActivity(intent);
                break;
            case R.id.imageButtonMamoor:
                intent = new Intent(getApplicationContext(), SetCounterActivity.class);
                startActivity(intent);
                break;
            case R.id.imageButtonSale:
                intent = new Intent(getApplicationContext(), SaleActivity.class);
                startActivity(intent);
                break;
            case R.id.imageButtonHelp:
                intent = new Intent(getApplicationContext(), HelpActivity.class);
                startActivity(intent);
                break;
            case R.id.imageButtonSupport:
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
    protected void onDestroy() {
        super.onDestroy();
        binding.imageButtonSale.setImageDrawable(null);
        binding.imageButtonLastBill.setImageDrawable(null);
        binding.imageButtonKardex.setImageDrawable(null);
        binding.imageButtonTrain.setImageDrawable(null);
        binding.imageButtonMamoor.setImageDrawable(null);
        binding.imageButtonSuggest.setImageDrawable(null);
        binding.imageButtonTracking.setImageDrawable(null);
        binding.imageButtonSupport.setImageDrawable(null);
        binding.imageButtonHelp.setImageDrawable(null);
    }
}
