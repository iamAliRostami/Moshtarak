package com.app.leon.moshtarak.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.app.leon.moshtarak.BaseItems.BaseActivity;
import com.app.leon.moshtarak.R;

import java.util.Objects;

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

    public static final int requestCodePaymentBill = 199;

    @SuppressLint({"HardwareIds", "MissingPermission", "CutPasteId"})
    @Override
    protected void initialize() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View childLayout = Objects.requireNonNull(inflater).inflate(R.layout.home_content, findViewById(R.id.home_activity));
        ConstraintLayout parentLayout = findViewById(R.id.base_Content);
        parentLayout.addView(childLayout);


        ButterKnife.bind(this);
        setOnClickListener();
        pay();
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
    };

    void pay() {

//        Intent intent = new Intent(HomeActivity.this, PaymentInitiator.class);
//        intent.putExtra("Type", "2");
//        intent.putExtra("Token", "token");
//        startActivityForResult(intent, requestCodePaymentBill);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == requestCodePaymentBill) {
            getPaymentResultCode(resultCode, data);
        }
    }

    private void getPaymentResultCode(int resultCode, Intent data) {
//
//        if (G.onMplResult != null) {
//            G.onMplResult.onResult(false);
//        }

        String enData = "", message = "", status = "0";
        int errorType = 0, orderId = 0;

        switch (resultCode) {
            case 1:// payment ok
                enData = data.getStringExtra("enData");
                message = data.getStringExtra("message");
                status = String.valueOf(data.getIntExtra("status", 0));
                break;
            case 2://payment error
                errorType = data.getIntExtra("errorType", 0);
                orderId = data.getIntExtra("OrderID", 0);
                break;
            case 3://bill payment ok
                enData = data.getStringExtra("enData");
                message = data.getStringExtra("message");
                status = String.valueOf(data.getIntExtra("status", 0));
                break;
            case 4://bill payment error
                errorType = data.getIntExtra("errorType", 0);
                break;
            case 5://internal error payment
                errorType = data.getIntExtra("errorType", 0);
                orderId = data.getIntExtra("OrderID", 0);
                break;
            case 6://internal error bill
                errorType = data.getIntExtra("errorType", 0);
                break;
            case 9:// internal error charge
                errorType = data.getIntExtra("errorType", 0);
                break;
        }

//        if (errorType != 0) {
//            showErrorTypeMpl(errorType);
//        }
    }
}
