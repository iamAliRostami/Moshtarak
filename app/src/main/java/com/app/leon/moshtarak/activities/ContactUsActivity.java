package com.app.leon.moshtarak.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Debug;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.app.leon.moshtarak.BaseItems.BaseActivity;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.databinding.ContactUsActivityBinding;

public class ContactUsActivity extends BaseActivity {
    Context context;
    ContactUsActivityBinding binding;
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == binding.linearLayout4.getId()) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"info@abfaisfahan.ir"});
                startActivity(Intent.createChooser(intent, "Choose an Email client :"));
                return;
            }
            String phone = "";
            if (id == binding.linearLayout1.getId()) {
                phone = "122";
            } else if (id == binding.linearLayout2.getId())
                phone = "1522";
            else if (id == binding.linearLayout3.getId())
                phone = "03136680030";
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:".concat(phone)));
            if (ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
                return;
            }
            startActivity(intent);
        }
    };

//    @SuppressLint("NewApi")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
//        super.onCreate(savedInstanceState);
//        binding = ContactUsActivityBinding.inflate(getLayoutInflater());
//        View view = binding.getRoot();
//        setContentView(view);
//        context = this;
//    }

    @Override
    protected void initialize() {
        binding = ContactUsActivityBinding.inflate(getLayoutInflater());
        View childLayout = binding.getRoot();
        ConstraintLayout parentLayout = findViewById(R.id.base_Content);
        parentLayout.addView(childLayout);
        context = this;
        binding.textViewVersion.setText(getString(R.string.version).concat(getVersionInfo()));
        binding.linearLayout1.setOnClickListener(onClickListener);
        binding.linearLayout2.setOnClickListener(onClickListener);
        binding.linearLayout3.setOnClickListener(onClickListener);
        binding.linearLayout4.setOnClickListener(onClickListener);
        binding.linearLayout5.setOnClickListener(onClickListener);
    }

    private String getVersionInfo() {
        String versionName = "";
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    @Override
    protected void onStop() {
        super.onStop();
        Runtime.getRuntime().totalMemory();
        Runtime.getRuntime().freeMemory();
        Runtime.getRuntime().maxMemory();
        Debug.getNativeHeapAllocatedSize();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().totalMemory();
        Runtime.getRuntime().freeMemory();
        Runtime.getRuntime().maxMemory();
        Debug.getNativeHeapAllocatedSize();
    }
}
