package com.app.leon.moshtarak.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Debug;
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
import com.app.leon.moshtarak.Utils.SharedPreference;
import com.app.leon.moshtarak.databinding.HelpContentBinding;

public class HelpActivity extends BaseActivity //implements OnPageChangeListener, OnLoadCompleteListener {
{
//    public static final String SAMPLE_FILE = "1.pdf";
//    private static final String TAG = HelpActivity.class.getSimpleName();
//    @BindView(R.id.pdfView)
//    PDFView pdfView;
//    Integer pageNumber = 0;
//    String pdfFileName;

    public static final String URL = "file:///android_asset/n_help.html";
    HelpContentBinding binding;
    ProgressDialog progressDialog;
    SharedPreference sharedPreference;
    Context context;

    @SuppressLint({"SetJavaScriptEnabled", "CutPasteId"})
    @Override
    protected void initialize() {
        binding = HelpContentBinding.inflate(getLayoutInflater());
        View childLayout = binding.getRoot();
        ConstraintLayout parentLayout = findViewById(R.id.base_Content);
        parentLayout.addView(childLayout);
        context = this;
        sharedPreference = new SharedPreference(context);

        progressDialog = new ProgressDialog(HelpActivity.this);
        progressDialog.setMessage(getString(R.string.waiting));
        progressDialog.setCancelable(false);
        progressDialog.show();
        binding.webViewHelp.setWebChromeClient(new HelpWebChromeClient());

        binding.webViewHelp.getSettings().setJavaScriptEnabled(true);
        binding.webViewHelp.getSettings().setBuiltInZoomControls(true);

        if (sharedPreference.getCache()) {
            Log.e("cache", "exist!");
            binding.webViewHelp.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ONLY);
        } else {
            Log.e("cache", "not exist!");
            binding.webViewHelp.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        }
        binding.webViewHelp.setWebViewClient(new HelpWebViewClient());
        binding.webViewHelp.loadUrl(getString(R.string.abfa_site));
//        binding.webViewHelp.loadUrl("www.google.com");
//        binding.webViewHelp.loadUrl(URL);

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e("back", "here");
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {// && binding.webViewHelp.canGoBack()) {
            finish();
//            binding.webViewHelp.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        Log.e("onBackPressed", "here");
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
            sharedPreference.putCache(true);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Toast.makeText(HelpActivity.this, getString(R.string.error).concat(" : ")
                    .concat(getString(R.string.error_IO)), Toast.LENGTH_SHORT).show();
        }
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

    @Override
    protected void onStop() {
        super.onStop();
        binding = null;
        context = null;

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
//    private void displayFromAsset() {
//        pdfFileName = HelpActivity.SAMPLE_FILE;
//        pdfView.fromAsset(SAMPLE_FILE)
//                .defaultPage(pageNumber)
//                .enableSwipe(true)
//                .swipeHorizontal(false)
//                .onPageChange(this)
//                .enableAnnotationRendering(true)
//                .onLoad(this)
//                .scrollHandle(new DefaultScrollHandle(this))
//                .load();
//    }
//
//
//    @Override
//    public void onPageChanged(int page, int pageCount) {
//        pageNumber = page;
//        setTitle(String.format("%s %s / %s", getString(R.string.help), page + 1, pageCount));
//
//    }
//
//
//    @Override
//    public void loadComplete(int nbPages) {
//        PdfDocument.Meta meta = pdfView.getDocumentMeta();
//        printBookmarksTree(pdfView.getTableOfContents(), "-");
//
//    }
//
//    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
//        for (PdfDocument.Bookmark b : tree) {
//
//            Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));
//
//            if (b.hasChildren()) {
//                printBookmarksTree(b.getChildren(), sep + "-");
//            }
//        }
//    }
}
