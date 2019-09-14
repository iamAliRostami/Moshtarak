package com.app.leon.moshtarak.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.leon.moshtarak.Activities.ShowSMSActivity;
import com.app.leon.moshtarak.Models.DbTables.TrackingDto;
import com.app.leon.moshtarak.R;

import java.util.ArrayList;
import java.util.Collections;

public class TrackCustomAdapter_1 extends ArrayAdapter<TrackingDto> {
    private ArrayList<TrackingDto> trackingDtos;
    private Context context;

    public TrackCustomAdapter_1(Context context, ArrayList<TrackingDto> trackingDtos) {
        super(context, 0);
        this.trackingDtos = trackingDtos;
        Collections.sort(this.trackingDtos, (o1, o2) -> o2.getDateJalali().compareToIgnoreCase(o1.getDateJalali()));
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            if (position % 2 == 0)
                view = LayoutInflater.from(context).inflate(R.layout.item_track, parent, false);
            else
                view = LayoutInflater.from(context).inflate(R.layout.item_track_, parent, false);
        }
        TrackingDto trackingDto = getItem(position);

        TextView textViewStatus;
        TextView textViewDate;
        TextView textViewTime;
        ImageView imageViewInfo;

        textViewStatus = view.findViewById(R.id.textViewStatus);
        textViewDate = view.findViewById(R.id.textViewDate);
        textViewTime = view.findViewById(R.id.textViewTime);
        imageViewInfo = view.findViewById(R.id.imageViewInfo);

        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/BYekan_3.ttf");
        textViewStatus.setTypeface(typeface);
        textViewDate.setTypeface(typeface);
        textViewTime.setTypeface(typeface);

        imageViewInfo.setOnClickListener(view1 -> {
            Intent intent = new Intent(context, ShowSMSActivity.class);
            intent.putExtra("SMS", trackingDto.getSmsList());
            intent.putExtra("SMS_LEVEL", " ".concat(trackingDto.getStatus()));
            context.startActivity(intent);
        });

        textViewStatus.setText(trackingDto.getStatus());
        textViewDate.setText(trackingDto.getDateJalali());
        textViewTime.setText(trackingDto.getTime());

        return view;
    }

    @Override
    public int getCount() {
        return trackingDtos.size();
    }

    @Override
    public TrackingDto getItem(int position) {
        return trackingDtos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}