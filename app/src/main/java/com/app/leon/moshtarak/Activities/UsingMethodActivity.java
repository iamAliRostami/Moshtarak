package com.app.leon.moshtarak.Activities;

import android.annotation.SuppressLint;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.app.leon.moshtarak.BaseItems.BaseActivity;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.databinding.UsingMethodContentBinding;

public class UsingMethodActivity extends BaseActivity {
    UsingMethodContentBinding binding;
//    @BindView(R.id.listViewLearningUsing)
//    ListView listViewLearningUsing;
//    @BindView(R.id.webViewLearningUsing)
//    WebView webView;
//    LearningCustomAdapter adapter;
//    List<LearningCustomAdapter.DrawerItem> dataList;

    @SuppressLint({"SetJavaScriptEnabled", "CutPasteId"})
    @Override
    protected void initialize() {
        binding = UsingMethodContentBinding.inflate(getLayoutInflater());
        View childLayout = binding.getRoot();
        ConstraintLayout parentLayout = findViewById(R.id.base_Content);
        parentLayout.addView(childLayout);
//        fillListViewLearningUsing();
        WebSettings webSetting = binding.webViewLearningUsing.getSettings();
        webSetting.setBuiltInZoomControls(true);
        webSetting.setJavaScriptEnabled(true);
        binding.webViewLearningUsing.setWebViewClient(new WebViewClient());
        binding.webViewLearningUsing.loadUrl("file:///android_asset/learning.html");
    }

//    void fillListViewLearningUsing() {
//        dataList = new ArrayList<>();
//        dataList.add(new LearningCustomAdapter.DrawerItem(getString(R.string.method_bath), R.drawable.btn_read));
//        dataList.add(new LearningCustomAdapter.DrawerItem(getString(R.string.method_washing), R.drawable.btn_read));
//        dataList.add(new LearningCustomAdapter.DrawerItem(getString(R.string.method_wc), R.drawable.btn_read));
//        dataList.add(new LearningCustomAdapter.DrawerItem(getString(R.string.method_watering), R.drawable.btn_read));
//        dataList.add(new LearningCustomAdapter.DrawerItem(getString(R.string.method_pool), R.drawable.btn_read));
//        dataList.add(new LearningCustomAdapter.DrawerItem(getString(R.string.method_planet), R.drawable.btn_read));
//        adapter = new LearningCustomAdapter(this, R.layout.item_learning, dataList);
//        listViewLearningUsing.setAdapter(adapter);
//    }
}
