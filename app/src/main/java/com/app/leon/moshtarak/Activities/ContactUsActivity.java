package com.app.leon.moshtarak.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.Utils.FontManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactUsActivity extends AppCompatActivity {
    @BindView(R.id.linearLayout1)
    LinearLayout linearLayout1;
    @BindView(R.id.linearLayout2)
    LinearLayout linearLayout2;
    @BindView(R.id.linearLayout3)
    LinearLayout linearLayout3;
    @BindView(R.id.linearLayout4)
    LinearLayout linearLayout4;
    @BindView(R.id.linearLayout5)
    LinearLayout linearLayout5;
    @BindView(R.id.relativeLayout)
    RelativeLayout relativeLayout;
    Context context;
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == linearLayout4.getId()) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"info@abfaisfahan.ir"});
                startActivity(Intent.createChooser(intent, "Choose an Email client :"));
                return;
            }
            String phone = "";
            if (id == linearLayout1.getId()) {
                phone = "122";
            } else if (id == linearLayout2.getId())
                phone = "1522";
            else if (id == linearLayout3.getId())
                phone = "03136680030";
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:".concat(phone)));
            if (ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                Log.e("call permission :", "Denied");
                intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
                return;
            }
            startActivity(intent);

        }
    };

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_us_activity);
        ButterKnife.bind(this);
        context = this;
        FontManager fontManager = new FontManager(context);
        fontManager.setFont(relativeLayout);
        linearLayout1.setOnClickListener(onClickListener);
        linearLayout2.setOnClickListener(onClickListener);
        linearLayout3.setOnClickListener(onClickListener);
        linearLayout4.setOnClickListener(onClickListener);
        linearLayout5.setOnClickListener(onClickListener);
    }
}
