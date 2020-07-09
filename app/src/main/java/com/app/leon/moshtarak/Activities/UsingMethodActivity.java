package com.app.leon.moshtarak.Activities;

import android.annotation.SuppressLint;
import android.os.Debug;
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
        binding.webViewLearningUsing.getSettings().setBuiltInZoomControls(true);
        binding.webViewLearningUsing.getSettings().setJavaScriptEnabled(true);


        binding.webViewLearningUsing.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        binding.webViewLearningUsing.setWebViewClient(new WebViewClient());
        binding.webViewLearningUsing.loadUrl("file:///android_asset/learning.html");
    }

    @Override
    protected void onStop() {
        super.onStop();
        binding = null;

        Runtime.getRuntime().totalMemory();
        Runtime.getRuntime().freeMemory();
        Runtime.getRuntime().maxMemory();
        Debug.getNativeHeapAllocatedSize();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;

        Runtime.getRuntime().totalMemory();
        Runtime.getRuntime().freeMemory();
        Runtime.getRuntime().maxMemory();
        Debug.getNativeHeapAllocatedSize();
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
