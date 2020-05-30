package com.app.leon.moshtarak.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.app.leon.moshtarak.BaseItems.BaseActivity;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.databinding.LearningContentBinding;

public class LearningActivity extends BaseActivity {
    LearningContentBinding binding;

    @Override
    protected void initialize() {
        binding = LearningContentBinding.inflate(getLayoutInflater());
        View childLayout = binding.getRoot();
        @SuppressLint("CutPasteId") ConstraintLayout parentLayout = findViewById(R.id.base_Content);
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
}
