package com.app.leon.moshtarak.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.app.leon.moshtarak.Models.DbTables.FollowUpDto;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.activities.ShowSMSActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class FollowUpCustomAdapterOld extends ArrayAdapter<FollowUpDto> {
    private ArrayList<FollowUpDto> followUpDtos;
    private Context context;

    public FollowUpCustomAdapterOld(Context context, ArrayList<FollowUpDto> followUpDtos) {
        super(context, 0);
        this.followUpDtos = followUpDtos;
        Collections.sort(this.followUpDtos, (o1, o2) -> o2.getDateJalali().compareToIgnoreCase(o1.getDateJalali()));
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            if (position % 2 == 0)
                view = LayoutInflater.from(context).inflate(R.layout.item_track, parent, false);
            else
                view = LayoutInflater.from(context).inflate(R.layout.item_track_, parent, false);
        }
        FollowUpDto followUpDto = getItem(position);

        TextView textViewStatus;
        TextView textViewDate;
        TextView textViewTime;

        textViewStatus = view.findViewById(R.id.textViewStatus);
        textViewDate = view.findViewById(R.id.textViewDate);
        textViewTime = view.findViewById(R.id.textViewTime);

        LinearLayout linearLayout = view.findViewById(R.id.linearLayout);

        linearLayout.setOnClickListener(view1 -> {
            Intent intent = new Intent(context, ShowSMSActivity.class);
            intent.putExtra("SMS", Objects.requireNonNull(followUpDto).getSmsList());
            intent.putExtra("SMS_LEVEL", " ".concat(followUpDto.getStatus()));
            context.startActivity(intent);
        });

        textViewStatus.setText(Objects.requireNonNull(followUpDto).getStatus());
        textViewDate.setText(followUpDto.getDateJalali());
        textViewTime.setText(followUpDto.getTime());

        return view;
    }

    @Override
    public int getCount() {
        return followUpDtos.size();
    }

    @Override
    public FollowUpDto getItem(int position) {
        return followUpDtos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}