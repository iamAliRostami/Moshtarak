package com.app.leon.moshtarak.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.leon.moshtarak.Models.Enums.BundleEnum;
import com.app.leon.moshtarak.R;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChartActivity extends AppCompatActivity {
    Context context;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.horizontalChart)
    HorizontalBarChart horizontalChart;
    Typeface typeface;
    ArrayList<String> listText = new ArrayList<>();
    String[] labelText;
    ArrayList<Integer> listValue = new ArrayList<>();

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_activity);

        if (getIntent().getExtras() != null) {
            Bundle bundle1 = getIntent().getBundleExtra(BundleEnum.DATA.getValue());
            if (bundle1 != null) {
                listText = bundle1.getStringArrayList(BundleEnum.DATE.getValue());
                listValue = bundle1.getIntegerArrayList(BundleEnum.USE.getValue());
            }
        }
        context = this;
        ButterKnife.bind(this);
        String fontName = "font/BYekan_3.ttf";
        typeface = Typeface.createFromAsset(context.getAssets(), fontName);
        customizeChartView();
        setData();
    }


    void customizeChartView() {
        horizontalChart.setDrawBarShadow(false);
        horizontalChart.setDrawValueAboveBar(true);
        horizontalChart.getDescription().setEnabled(false);
        horizontalChart.setPinchZoom(false);

        horizontalChart.setDrawGridBackground(true);
        XAxis xl = horizontalChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setTypeface(typeface);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(true);
        xl.setGranularity(21f);
        xl.setGranularityEnabled(true);

        YAxis yl = horizontalChart.getAxisLeft();
        yl.setTypeface(typeface);
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(true);
        yl.setAxisMinimum(-1f); // this replaces setStartAtZero(true)
//        yl.setInverted(true);

        YAxis yr = horizontalChart.getAxisRight();
        yr.setTypeface(typeface);
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(true);
        yr.setAxisMinimum(-1f); // this replaces setStartAtZero(true)
//        yr.setInverted(true);

        horizontalChart.setFitBars(true);
        horizontalChart.animateY(2500);
        horizontalChart.animateX(2500);
        horizontalChart.getLegend().setEnabled(false);
        horizontalChart.setFitBars(true);
        horizontalChart.invalidate();
    }

    private void setData() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        float x = 1f;
        int y = 0;
        int margin = getResources().getDimensionPixelSize(R.dimen.activity_mid_margin);
        linearLayout.setWeightSum(listValue.size());
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
//            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setLayoutParams(params);
            textView.setGravity(Gravity.CENTER);
            linearLayout.addView(textView);
            linearLayout.invalidate();
        }
        BarDataSet bardataset = new BarDataSet(barEntries, getString(R.string.use));
        bardataset.setValueTypeface(typeface);
        bardataset.setValueTextSize(getResources().getDimensionPixelSize(R.dimen.textSizeSmall));
        BarData data = new BarData(bardataset);
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        horizontalChart.setData(data);
        horizontalChart.animateY(2500);
        horizontalChart.animateX(2500);
    }
}

