package com.app.leon.moshtarak.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.app.leon.moshtarak.Models.DbTables.FollowUpDto;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.activities.ShowSMSActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class FollowUpCustomAdapter extends ArrayAdapter<FollowUpDto> {
    private final ArrayList<FollowUpDto> followUpDtos;
    private final Context context;

    public FollowUpCustomAdapter(Context context, ArrayList<FollowUpDto> followUpDtos) {
        super(context, 0);
        this.followUpDtos = followUpDtos;
        Collections.sort(this.followUpDtos, (o1, o2) -> o2.getDateJalali().compareToIgnoreCase(o1.getDateJalali()));
        this.context = context;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        FollowUpHolder followUpHolder;
        view = LayoutInflater.from(context).inflate(R.layout.item_track_new, parent, false);
//        if (position % 2 == 0) {
//            view = LayoutInflater.from(context).inflate(R.layout.item_track, parent, false);
//        } else {
//            view = LayoutInflater.from(context).inflate(R.layout.item_track_, parent, false);
//        }
        FollowUpDto followUpDto = getItem(position);
        followUpHolder = new FollowUpHolder(view, followUpDto);
        view.setTag(followUpHolder);
        followUpHolder.fillTextView();
        followUpHolder.fillImageView();
        followUpHolder.linearLayout.setOnClickListener(followUpHolder.onClickListener);
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

    @Override
    public int getViewTypeCount() {
        // TODO Auto-generated method stub
        if (getCount() != 0)
            return getCount();
        return 1;
    }

    class FollowUpHolder {
        TextView textViewStatus;
        TextView textViewDate;
        TextView textViewTime;
        ImageView imageViewIcon;
        LinearLayout linearLayout;
        View view;
        FollowUpDto followUpDto;
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShowSMSActivity.class);
                intent.putExtra("SMS", Objects.requireNonNull(followUpDto).getSmsList());
                intent.putExtra("SMS_LEVEL", " ".concat(followUpDto.getStatus()));
                context.startActivity(intent);
            }
        };

        public FollowUpHolder(View view, FollowUpDto followUpDto) {
            this.view = view;
            this.followUpDto = followUpDto;
            textViewStatus = view.findViewById(R.id.textViewStatus);
            textViewDate = view.findViewById(R.id.textViewDate);
            textViewTime = view.findViewById(R.id.textViewTime);
            imageViewIcon = view.findViewById(R.id.imageViewIcon);
            linearLayout = view.findViewById(R.id.linearLayout);
        }

        void fillTextView() {
            textViewStatus.setText(Objects.requireNonNull(followUpDto).getStatus());
            textViewDate.setText(followUpDto.getDateJalali());
            textViewTime.setText(followUpDto.getTime());
        }

        void fillImageView() {
            switch (followUpDto.getStateCode()) {
                case 0://ثبت درخواست
                    imageViewIcon.setImageDrawable(
                            context.getResources().getDrawable(R.drawable.sms_registered));
                    break;
                case 20://نتیجه ارزیابی
                case 110://تعیین نتیجه ارزیابی
                    imageViewIcon.setImageDrawable(
                            context.getResources().getDrawable(R.drawable.sms_confirmed));
                    break;
                case 10:// تعیین زمان ارزیابی
                case 15: //ارزیابی مجدد
                    imageViewIcon.setImageDrawable(
                            context.getResources().getDrawable(R.drawable.sms_guide));
                    break;
                case 17://بارگذاری مدارک بایگانی
                    imageViewIcon.setImageDrawable(
                            context.getResources().getDrawable(R.drawable.sms_upload));
                    break;
                case 50://محاسبات انجام شده
                    imageViewIcon.setImageDrawable(
                            context.getResources().getDrawable(R.drawable.sms_confirm));
                    break;
                case 60://محاسبات
                    imageViewIcon.setImageDrawable(
                            context.getResources().getDrawable(R.drawable.sms_clipboard));
                    break;
                case 65://محاسبات مجدد
                    imageViewIcon.setImageDrawable(
                            context.getResources().getDrawable(R.drawable.sms_again));
                    break;
                case 70://اختصاص ردیف
                    imageViewIcon.setImageDrawable(
                            context.getResources().getDrawable(R.drawable.sms_row));
                    break;
                case 75://تایید مبلغ
                    imageViewIcon.setImageDrawable(
                            context.getResources().getDrawable(R.drawable.sms_confirmed_pay));
                    break;
                case 80://پرداخت مشترک
                    imageViewIcon.setImageDrawable(
                            context.getResources().getDrawable(R.drawable.sms_confirmed2));
                    break;
                case 90://ثبت قطعی
                    imageViewIcon.setImageDrawable(
                            context.getResources().getDrawable(R.drawable.sms_registered3));
                    break;
                case 90000://حذف درخواست
                    imageViewIcon.setImageDrawable(
                            context.getResources().getDrawable(R.drawable.sms_delete));
                    break;
                case 90001://حذف کامل
                    imageViewIcon.setImageDrawable(
                            context.getResources().getDrawable(R.drawable.sms_delete_all));
                    break;
                case 90003://آرشیو شده
                    imageViewIcon.setImageDrawable(
                            context.getResources().getDrawable(R.drawable.sms_archived));
                    break;
            }
        }
    }
}