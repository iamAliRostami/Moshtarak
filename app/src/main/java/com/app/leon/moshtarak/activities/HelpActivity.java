package com.app.leon.moshtarak.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Debug;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.app.leon.moshtarak.BaseItems.BaseActivity;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.databinding.HelpContentBinding;

import java.io.File;
import java.util.Date;
import java.util.Objects;

public class HelpActivity extends BaseActivity {
    HelpContentBinding binding;
    ProgressDialog progressDialog;
    Context context;

    @SuppressLint({"SetJavaScriptEnabled", "CutPasteId"})
    @Override
    protected void initialize() {
        binding = HelpContentBinding.inflate(getLayoutInflater());
        View childLayout = binding.getRoot();
        ConstraintLayout parentLayout = findViewById(R.id.base_Content);
        parentLayout.addView(childLayout);
        context = this;
        clearCacheFolder(context.getCacheDir(), 1);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(getString(R.string.waiting));
        progressDialog.setCancelable(false);
        progressDialog.show();
        binding.webViewHelp.setWebChromeClient(new HelpWebChromeClient());

        binding.webViewHelp.getSettings().setJavaScriptEnabled(true);
        binding.webViewHelp.getSettings().setBuiltInZoomControls(true);
        binding.webViewHelp.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        binding.webViewHelp.setWebViewClient(new HelpWebViewClient());
        binding.webViewHelp.loadUrl(getString(R.string.help_url));
    }

    static int clearCacheFolder(final File dir, final int numDays) {
        int deletedFiles = 0;
        if (dir != null && dir.isDirectory()) {
            try {
                for (File child : Objects.requireNonNull(dir.listFiles())) {
                    if (child.isDirectory()) {
                        deletedFiles += clearCacheFolder(child, numDays);
                    }
                    if (child.lastModified() < new Date().getTime() -
                            numDays * DateUtils.DAY_IN_MILLIS) {
                        if (child.delete()) {
                            deletedFiles++;
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("TAG", String.format("Failed to clean the cache, error %s", e.getMessage()));
            }
        }
        return deletedFiles;
    }

    private class HelpWebChromeClient extends WebChromeClient {
        public void onProgressChanged(WebView view, int progress) {
            setTitle(getString(R.string.waiting));
            setProgress(progress * 100);
            Log.e("progress ", String.valueOf(progress));
            if (progress >= 70) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
            if (progress == 100)
                setTitle(R.string.help);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private class HelpWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description,
                                    String failingUrl) {
            Log.e("error", description);
            Toast.makeText(HelpActivity.this, getString(R.string.error).concat(" : ")
                    .concat(getString(R.string.error_connection)), Toast.LENGTH_SHORT).show();
        }
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
        binding = null;
        context = null;

        Runtime.getRuntime().totalMemory();
        Runtime.getRuntime().freeMemory();
        Runtime.getRuntime().maxMemory();
        Debug.getNativeHeapAllocatedSize();
    }
}
