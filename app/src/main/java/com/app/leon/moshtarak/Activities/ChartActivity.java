package com.app.leon.moshtarak.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.app.leon.moshtarak.Models.Enums.BundleEnum;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.Utils.FontManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

public class ChartActivity extends AppCompatActivity {
    Context context;
    LineChartView lineChartView;
    ArrayList<Integer> list;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_activity);
        context = this;
        ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);
        FontManager fontManager = new FontManager(context);
        fontManager.setFont(constraintLayout);

        if (getIntent().getExtras() != null)
            list = getIntent().getIntegerArrayListExtra(BundleEnum.USE.getValue());

        String[] axisData = new String[Objects.requireNonNull(list).size()];
        int[] yAxisData = new int[list.size()];

        for (int i = 0; i < list.size(); i++) {
            axisData[i] = "";
            Log.e("usage", String.valueOf(list.get(i)));
            float floatNumber = Float.valueOf(list.get(i));
            yAxisData[i] = (int) floatNumber;
        }
        List yAxisValues = new ArrayList();
        List axisValues = new ArrayList();

        Line line = new Line(yAxisValues).setColor(getColor(R.color.blue2));

        for (int i = 0; i < axisData.length; i++) {
            axisValues.add(i, new AxisValue(i).setLabel(axisData[i]));
        }

        for (int i = 0; i < yAxisData.length; i++) {
            yAxisValues.add(new PointValue(i, yAxisData[i]));
        }
        List lines = new ArrayList();
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        Axis axis = new Axis();
        axis.setValues(axisValues);
        data.setAxisXBottom(axis);

        Axis yAxis = new Axis();
        yAxis.setTextColor(getColor(R.color.blue2));
        yAxis.setTextSize(16);
//        yAxis.setName(getString(R.string.use));
        data.setAxisYLeft(yAxis);
        lineChartView = findViewById(R.id.chart);
        lineChartView.setLineChartData(data);
    }
}
