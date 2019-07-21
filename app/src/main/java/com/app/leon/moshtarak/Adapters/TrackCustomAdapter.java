package com.app.leon.moshtarak.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.app.leon.moshtarak.Activities.ShowSMSActivity;
import com.app.leon.moshtarak.Models.DbTables.TrackingDto;
import com.app.leon.moshtarak.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrackCustomAdapter extends RecyclerView.Adapter<TrackCustomAdapter.ViewHolder> {
    private ArrayList<TrackingDto> trackingDtos;
    private Context context;
    private int size = 0;
    private int width;

    public TrackCustomAdapter(ArrayList<TrackingDto> trackingDtos, int width) {
        this.trackingDtos = trackingDtos;
        Collections.sort(this.trackingDtos, (o1, o2) -> o1.getDateJalali().compareToIgnoreCase(o2.getDateJalali()));
        this.width = width;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View viewCardex;
        if (size % 2 == 0)
            viewCardex = layoutInflater.inflate(R.layout.item_track, null);
        else
            viewCardex = layoutInflater.inflate(R.layout.item_track_, null);
        viewCardex.setForegroundGravity(1);

        return new ViewHolder(viewCardex);
    }

    @SuppressLint("RtlHardcoded")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final TrackingDto trackingDto = trackingDtos.get(i);
        viewHolder.textViewStatus.setText(trackingDto.getStatus());
        viewHolder.textViewDate.setText(trackingDto.getDateJalali());
        viewHolder.textViewTime.setText(trackingDto.getTime());
        viewHolder.imageViewInfo.setOnClickListener(view -> {
            Intent intent = new Intent(context, ShowSMSActivity.class);
            intent.putExtra("SMS", trackingDto.getSmsList());
            context.startActivity(intent);
        });
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/BYekan_3.ttf");
        viewHolder.textViewStatus.setTypeface(typeface);
        viewHolder.textViewDate.setTypeface(typeface);
        viewHolder.textViewTime.setTypeface(typeface);

        viewHolder.textViewStatus.setWidth((width - viewHolder.imageViewInfo.getWidth()) / 2);
        viewHolder.textViewDate.setWidth((width - viewHolder.imageViewInfo.getWidth()) / 3);
        viewHolder.textViewTime.setWidth((width - viewHolder.imageViewInfo.getWidth()) / 3);

        viewHolder.textViewStatus.setGravity(Gravity.RIGHT);
        viewHolder.textViewDate.setGravity(1);
        viewHolder.textViewTime.setGravity(1);
        size++;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemCount() {
        return trackingDtos.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewStatus;
        TextView textViewDate;
        TextView textViewTime;
        ImageView imageViewInfo;

        ViewHolder(View itemView) {
            super(itemView);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewTime = itemView.findViewById(R.id.textViewTime);
            imageViewInfo = itemView.findViewById(R.id.imageViewInfo);
        }
    }
}
