package com.app.leon.moshtarak.Activities;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.app.leon.moshtarak.BaseItems.BaseActivity;
import com.app.leon.moshtarak.R;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UsingMethodActivity extends BaseActivity {
    //    @BindView(R.id.listViewLearningUsing)
//    ListView listViewLearningUsing;
    @BindView(R.id.webViewLearningUsing)
    WebView webView;
//    LearningCustomAdapter adapter;
//    List<LearningCustomAdapter.DrawerItem> dataList;

    @SuppressLint({"SetJavaScriptEnabled", "CutPasteId"})
    @Override
    protected void initialize() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View childLayout = Objects.requireNonNull(inflater).inflate(R.layout.using_method_content, findViewById(R.id.using_method_activity));
        ConstraintLayout parentLayout = findViewById(R.id.base_Content);
        parentLayout.addView(childLayout);
        ButterKnife.bind(this);
//        fillListViewLearningUsing();
        WebSettings webSetting = webView.getSettings();
        webSetting.setBuiltInZoomControls(true);
        webSetting.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("file:///android_asset/learning.html");
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
