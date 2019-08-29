package com.app.leon.moshtarak.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;

import com.app.leon.moshtarak.BaseItems.BaseActivity;
import com.app.leon.moshtarak.Models.ViewModels.UiElementInActivity;
import com.app.leon.moshtarak.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity {
    @BindView(R.id.imageButtonSale)
    ImageButton imageButtonSale;
    @BindView(R.id.imageButtonLastBill)
    ImageButton imageButtonLastBill;
    @BindView(R.id.imageButtonKardex)
    ImageButton imageButtonKardex;
    @BindView(R.id.imageButtonTrain)
    ImageButton imageButtonTrain;
    @BindView(R.id.imageButtonMamoor)
    ImageButton imageButtonMamoor;
    @BindView(R.id.imageButtonSuggest)
    ImageButton imageButtonSuggets;
    @BindView(R.id.imageButtonTracking)
    ImageButton imageButtonTracking;
    @BindView(R.id.imageButtonHelp)
    ImageButton imageButtonHelp;
    @BindView(R.id.imageButtonSupport)
    ImageButton imageButtonSupport;
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
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
                    intent = new Intent(getApplicationContext(), LastBillActivity.class);
                    startActivity(intent);
                    break;
                case R.id.imageButtonKardex:
                    intent = new Intent(getApplicationContext(), CardexActivity.class);
                    startActivity(intent);
                    break;
                case R.id.imageButtonTracking:
                    intent = new Intent(getApplicationContext(), TrackingsActivity.class);
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
        }
    };

    @Override
    protected UiElementInActivity getUiElementsInActivity() {
        UiElementInActivity uiElementInActivity = new UiElementInActivity();
        uiElementInActivity.setContentViewId(R.layout.home_activity);
        return uiElementInActivity;
    }

    @SuppressLint({"HardwareIds", "MissingPermission"})
    @Override
    protected void initialize() {
        ButterKnife.bind(this);
        setOnClickListener();
    }

    void setOnClickListener() {
        imageButtonSale.setOnClickListener(onClickListener);
        imageButtonLastBill.setOnClickListener(onClickListener);
        imageButtonKardex.setOnClickListener(onClickListener);
        imageButtonTrain.setOnClickListener(onClickListener);
        imageButtonMamoor.setOnClickListener(onClickListener);
        imageButtonSuggets.setOnClickListener(onClickListener);
        imageButtonTracking.setOnClickListener(onClickListener);
        imageButtonHelp.setOnClickListener(onClickListener);
        imageButtonSupport.setOnClickListener(onClickListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imageButtonSale.setImageDrawable(null);
        imageButtonLastBill.setImageDrawable(null);
        imageButtonKardex.setImageDrawable(null);
        imageButtonTrain.setImageDrawable(null);
        imageButtonMamoor.setImageDrawable(null);
        imageButtonSuggets.setImageDrawable(null);
        imageButtonTracking.setImageDrawable(null);
        imageButtonSupport.setImageDrawable(null);
        imageButtonHelp.setImageDrawable(null);
    }
}
