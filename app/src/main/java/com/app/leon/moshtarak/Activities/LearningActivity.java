package com.app.leon.moshtarak.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.app.leon.moshtarak.BaseItems.BaseActivity;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.Utils.FontManager;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LearningActivity extends BaseActivity {
    @BindView(R.id.imageViewWaterConsumption)
    ImageView imageViewWaterConsumption;

    @Override
    protected void initialize() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View childLayout = Objects.requireNonNull(inflater).inflate(R.layout.learning_content, findViewById(R.id.learning_activity));
        @SuppressLint("CutPasteId") ConstraintLayout parentLayout = findViewById(R.id.base_Content);
        parentLayout.addView(childLayout);
        ButterKnife.bind(this);
        FontManager fontManager = new FontManager(getApplicationContext());
        fontManager.setFont(findViewById(R.id.learning_activity));
        setImageButtonsClickListener();
    }

    private void setImageButtonsClickListener() {
        setImageViewWaterConsumptionListener();
    }

    private void setImageViewWaterConsumptionListener() {
        imageViewWaterConsumption.setOnClickListener(view -> {
            Intent intent = new Intent(LearningActivity.this, UsingMethodActivity.class);
            startActivity(intent);
        });
    }
}
