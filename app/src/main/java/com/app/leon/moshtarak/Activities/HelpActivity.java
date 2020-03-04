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

public class HelpActivity extends BaseActivity //implements OnPageChangeListener, OnLoadCompleteListener {
{
    @BindView(R.id.webViewHelp)
    WebView webView;
    public static final String SAMPLE_FILE = "1.pdf";
    private static final String TAG = HelpActivity.class.getSimpleName();
    //    @BindView(R.id.pdfView)
//    PDFView pdfView;
    Integer pageNumber = 0;
    String pdfFileName;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initialize() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View childLayout = Objects.requireNonNull(inflater).inflate(R.layout.help_content, findViewById(R.id.help_activity));
        @SuppressLint("CutPasteId") ConstraintLayout parentLayout = findViewById(R.id.base_Content);
        parentLayout.addView(childLayout);
        ButterKnife.bind(this);
        WebSettings webSetting = webView.getSettings();
        webSetting.setBuiltInZoomControls(true);
        webSetting.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("file:///android_asset/help3.htm");
//        displayFromAsset();
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
