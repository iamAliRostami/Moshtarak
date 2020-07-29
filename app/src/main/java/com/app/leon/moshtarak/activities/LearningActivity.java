package com.app.leon.moshtarak.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Debug;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.app.leon.moshtarak.BaseItems.BaseActivity;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.databinding.LearningContentBinding;

public class LearningActivity extends BaseActivity {
    LearningContentBinding binding;

    @SuppressLint("CutPasteId")
    @Override
    protected void initialize() {
        binding = LearningContentBinding.inflate(getLayoutInflater());
        View childLayout = binding.getRoot();
        ConstraintLayout parentLayout = findViewById(R.id.base_Content);
        parentLayout.addView(childLayout);
        setImageButtonsClickListener();
    }

    private void setImageButtonsClickListener() {
        setImageViewWaterConsumptionListener();
    }

    private void setImageViewWaterConsumptionListener() {
        binding.imageViewWaterConsumption.setOnClickListener(view -> {
            Intent intent = new Intent(LearningActivity.this, UsingMethodActivity.class);
            startActivity(intent);
        });
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
