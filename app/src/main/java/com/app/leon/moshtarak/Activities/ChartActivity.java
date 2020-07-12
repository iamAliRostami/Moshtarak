package com.app.leon.moshtarak.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Debug;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.app.leon.moshtarak.Models.Enums.BundleEnum;
import com.app.leon.moshtarak.MyApplication;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.databinding.ChartActivityBinding;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Objects;


public class ChartActivity extends AppCompatActivity {
    ChartActivityBinding binding;
    Context context;
    Typeface typeface;
    ArrayList<String> listText = new ArrayList<>();
    ArrayList<Integer> listValue = new ArrayList<>();


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        super.onCreate(savedInstanceState);
        binding = ChartActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.chart2));
        if (getIntent().getExtras() != null) {
            Bundle bundle1 = getIntent().getBundleExtra(BundleEnum.DATA.getValue());
            if (bundle1 != null) {
                listText = bundle1.getStringArrayList(BundleEnum.DATE.getValue());
                listValue = bundle1.getIntegerArrayList(BundleEnum.USE.getValue());
            }
        }
        context = this;
        typeface = Typeface.createFromAsset(context.getAssets(), MyApplication.getFontName());
        customizeChartView();
        setData();
    }

    @SuppressLint("NewApi")
    void customizeChartView() {
        binding.horizontalChart.setDrawBarShadow(false);
        binding.horizontalChart.setDrawValueAboveBar(true);
        binding.horizontalChart.getDescription().setEnabled(false);
        binding.horizontalChart.setPinchZoom(false);
//        binding.horizontalChart.setBackgroundColor(getColor(R.color.gray3));
//        binding.horizontalChart.setGridBackgroundColor(getColor(R.color.gray4));

        binding.horizontalChart.setBackgroundColor(ContextCompat.getColor(MyApplication.getContext()
                , R.color.gray3));
        binding.horizontalChart.setGridBackgroundColor(ContextCompat.getColor(MyApplication.getContext()
                , R.color.gray4));

        binding.horizontalChart.setDrawGridBackground(true);
        XAxis xl = binding.horizontalChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setTypeface(typeface);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(true);
        xl.setGranularity(21f);
        xl.setGranularityEnabled(true);

        YAxis yl = binding.horizontalChart.getAxisLeft();
        yl.setTypeface(typeface);
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(true);
        yl.setAxisMinimum(-1f);

        YAxis yr = binding.horizontalChart.getAxisRight();
        yr.setTypeface(typeface);
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(true);
        yr.setAxisMinimum(-1f);

        binding.horizontalChart.setFitBars(true);
        binding.horizontalChart.setScaleEnabled(false);
        binding.horizontalChart.animateY(2500);
        binding.horizontalChart.animateX(2500);
        binding.horizontalChart.getLegend().setEnabled(false);
        binding.horizontalChart.setFitBars(true);
        binding.horizontalChart.invalidate();
    }

    private void setData() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        float x = 1f;
        int margin = getResources().getDimensionPixelSize(R.dimen.activity_mid_margin);
        binding.linearLayout.setWeightSum(listValue.size());
        for (int i = 0; i < listValue.size(); i++) {
            barEntries.add(new BarEntry(x, listValue.get(i)));
            x = x + 1f;
            TextView textView = new TextView(this);
            textView.setText(listText.get(i));
            textView.setId(5 + i);
            textView.setTypeface(typeface);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, 0);
            params.weight = 1.0f;
            if (i == 0) {
                params.setMargins(margin, margin, margin, 0);
                textView.setPadding(0, margin, 0, 0);
            } else if (i == listValue.size() - 1) {
                params.setMargins(margin, 0, margin, margin);
                textView.setPadding(0, 0, 0, margin);
            } else
                params.setMargins(margin, 0, margin, 0);
            textView.setLayoutParams(params);
            textView.setGravity(Gravity.CENTER);
            binding.linearLayout.addView(textView);
            binding.linearLayout.invalidate();
        }
        BarDataSet bardataset = new BarDataSet(barEntries, getString(R.string.use));
        bardataset.setValueTypeface(typeface);
        BarData data = new BarData(bardataset);
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        binding.horizontalChart.setData(data);
        binding.horizontalChart.animateY(2500);
        binding.horizontalChart.animateX(2500);
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
        listText = null;
        listValue = null;
        Runtime.getRuntime().totalMemory();
        Runtime.getRuntime().freeMemory();
        Runtime.getRuntime().maxMemory();
        Debug.getNativeHeapAllocatedSize();
    }
}

