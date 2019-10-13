package com.app.leon.moshtarak.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.app.leon.moshtarak.Models.Enums.BundleEnum;
import com.app.leon.moshtarak.R;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChartActivity extends AppCompatActivity {
    Context context;
    @BindView(R.id.horizontalChart)
    HorizontalBarChart horizontalChart;
    Typeface typeface;
    ArrayList<String> listText = new ArrayList<>();
    String[] lableText;
    ArrayList<Integer> listValue = new ArrayList<>();

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_activity_1);

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

        horizontalChart.setDrawBarShadow(false);
        horizontalChart.setDrawValueAboveBar(true);
        horizontalChart.getDescription().setEnabled(false);

        // scaling can now only be done on x- and y-axis separately
        horizontalChart.setPinchZoom(false);
        // draw shadows for each bar that show the maximum value
        // chart.setDrawBarShadow(true);
        horizontalChart.setDrawGridBackground(true);
        XAxis xl = horizontalChart.getXAxis();
        xl.setPosition(XAxisPosition.BOTTOM);
        xl.setTypeface(typeface);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(true);
        xl.setGranularity(1000f);
        xl.setGranularityEnabled(true);

        YAxis yl = horizontalChart.getAxisLeft();
        yl.setTypeface(typeface);
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(true);
        yl.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//        yl.setInverted(true);

        YAxis yr = horizontalChart.getAxisRight();

        yr.setTypeface(typeface);
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(false);
        yr.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//        yr.setInverted(true);

        horizontalChart.setFitBars(true);
        horizontalChart.animateY(2500);
        horizontalChart.animateX(2500);

        setData(listValue.size());
        horizontalChart.setFitBars(true);
        horizontalChart.invalidate();

        Legend l = horizontalChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setXEntrySpace(4f);
    }

    private void setData(int count) {
        float barWidth = 9f;
        float spaceForBar = 10f;
        ArrayList<BarEntry> values = new ArrayList<>();
        lableText = new String[listText.size()];
        for (int i = 0; i < count; i++) {
            float val = (float) listValue.get(i);
            values.add(new BarEntry(i * spaceForBar, val, lableText[i]));

//            lableText[i] = listText.get(i);
        }

        BarDataSet barDataSet;
        if (horizontalChart.getData() != null && horizontalChart.getData().getDataSetCount() > 0) {
            barDataSet = (BarDataSet) horizontalChart.getData().getDataSetByIndex(0);
            barDataSet.setValues(values);
            horizontalChart.getData().notifyDataChanged();
            horizontalChart.notifyDataSetChanged();
        } else {
            barDataSet = new BarDataSet(values, getString(R.string.use));
            barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(barDataSet);
            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setValueTypeface(typeface);
            data.setBarWidth(barWidth);

            data.setValueFormatter((value, entry, dataSetIndex, viewPortHandler) -> listText.get(dataSetIndex));
            horizontalChart.setData(data);
        }
    }
}
