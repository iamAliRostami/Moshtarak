package com.app.leon.moshtarak.Activities;

import android.content.Intent;
import android.widget.ImageView;

import com.app.leon.moshtarak.BaseItems.BaseActivity;
import com.app.leon.moshtarak.Models.ViewModels.UiElementInActivity;
import com.app.leon.moshtarak.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LearningActivity extends BaseActivity {
    @BindView(R.id.imageViewWaterConsumption)
    ImageView imageViewWaterConsumption;

    @Override
    protected UiElementInActivity getUiElementsInActivity() {
        UiElementInActivity uiElementInActivity = new UiElementInActivity();
        uiElementInActivity.setContentViewId(R.layout.learning_activity);
        return uiElementInActivity;
    }

    @Override
    protected void initialize() {
        ButterKnife.bind(this);
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
