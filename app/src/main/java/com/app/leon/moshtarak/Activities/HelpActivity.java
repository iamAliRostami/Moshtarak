package com.app.leon.moshtarak.Activities;

import android.annotation.SuppressLint;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.app.leon.moshtarak.BaseItems.BaseActivity;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.databinding.HelpContentBinding;

public class HelpActivity extends BaseActivity //implements OnPageChangeListener, OnLoadCompleteListener {
{
//    public static final String SAMPLE_FILE = "1.pdf";
//    private static final String TAG = HelpActivity.class.getSimpleName();
//    @BindView(R.id.pdfView)
//    PDFView pdfView;
//    Integer pageNumber = 0;
//    String pdfFileName;

    public static final String URL = "file:///android_asset/help1.htm";
    HelpContentBinding binding;

    @SuppressLint({"SetJavaScriptEnabled", "CutPasteId"})
    @Override
    protected void initialize() {
        binding = HelpContentBinding.inflate(getLayoutInflater());
        View childLayout = binding.getRoot();
        ConstraintLayout parentLayout = findViewById(R.id.base_Content);
        parentLayout.addView(childLayout);
        WebSettings webSetting = binding.webViewHelp.getSettings();
        webSetting.setBuiltInZoomControls(true);
        webSetting.setJavaScriptEnabled(true);

        binding.webViewHelp.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        binding.webViewHelp.setWebViewClient(new WebViewClient());
        binding.webViewHelp.loadUrl(URL);
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
