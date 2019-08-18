package com.app.leon.moshtarak.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.leon.moshtarak.Activities.LastBillActivity;
import com.app.leon.moshtarak.Models.DbTables.Kardex;
import com.app.leon.moshtarak.Models.Enums.BundleEnum;
import com.app.leon.moshtarak.R;

import java.util.ArrayList;
import java.util.Collections;

public class KardexCustomAdapter_1 extends ArrayAdapter<Kardex> {
    private ArrayList<Kardex> kardexes;
    private Context context;

    public KardexCustomAdapter_1(ArrayList<Kardex> kardexes, Context context) {
        super(context, 0);
        this.kardexes = kardexes;
        for (int i = 0; i < this.kardexes.size(); i++) {
            Kardex kardex = this.kardexes.get(i);
            String owe = kardex.getOweDate();
            String creditor = kardex.getCreditorDate().trim();
            if (owe == null || owe.equals("null") || TextUtils.isEmpty(owe)) {
                kardex.setOweDate(creditor);
            }
        }
        Collections.sort(this.kardexes, (o1, o2) -> o2.getOweDate().compareTo(o1.getOweDate()));
        this.context = context;
    }

    @SuppressLint({"InflateParams", "DefaultLocale"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View viewHolder;
        if (position % 2 == 0)
            viewHolder = layoutInflater.inflate(R.layout.item_cardex_1, null);
        else
            viewHolder = layoutInflater.inflate(R.layout.item_cardex_2, null);

        Kardex kardex = kardexes.get(position);
        TextView textViewDate;
        TextView textViewUse;
        TextView textViewCost;
        TextView textViewNote;
        ImageView imageViewInfo;
        textViewDate = viewHolder.findViewById(R.id.textViewDate);
        textViewUse = viewHolder.findViewById(R.id.textViewUse);
        textViewCost = viewHolder.findViewById(R.id.textViewCost);
        textViewNote = viewHolder.findViewById(R.id.textViewNote);
        imageViewInfo = viewHolder.findViewById(R.id.imageViewInfo);
        float floatNumber = Float.valueOf(kardex.getAmount());
        int intNumber = (int) floatNumber;
        textViewCost.setText(String.valueOf(intNumber));
        textViewNote.setText(kardex.getDescription());
        floatNumber = Float.valueOf(kardex.getUsage());
        intNumber = (int) floatNumber;
        textViewUse.setText(String.valueOf(intNumber));

        textViewDate.setText(kardex.getOweDate());

//        String owe = kardex.getOweDate();
//        String creditor = kardex.getCreditorDate().trim();
//        if (TextUtils.isEmpty(creditor)) {
//            textViewDate.setText(owe);
//        } else if (owe == null || owe.equals("null") || TextUtils.isEmpty(owe)) {
//            textViewDate.setText(creditor);
//        }

        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/BYekan_3.ttf");
        textViewNote.setTypeface(typeface);
        textViewDate.setTypeface(typeface);
        textViewCost.setTypeface(typeface);
        textViewUse.setTypeface(typeface);
        if (kardex.isBill()) {
            textViewNote.setTextColor(context.getResources().getColor(R.color.colorAccentIndigo));
            textViewCost.setTextColor(context.getResources().getColor(R.color.pink2));
            imageViewInfo.setOnClickListener(view ->
            {
                Intent intent = new Intent(context, LastBillActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(BundleEnum.ID.getValue(), kardex.getId());
                bundle.putString(BundleEnum.ZONE_ID.getValue(), kardex.getZoneId());
                intent.putExtra(BundleEnum.THIS_BILL.getValue(), bundle);
                context.startActivity(intent);
            });
        } else if (kardex.isPay()) {
            imageViewInfo.setOnClickListener(view -> Toast.makeText(context, context.getString(R.string.payed), Toast.LENGTH_SHORT).show());
        }
        textViewCost.setGravity(1);
        textViewNote.setGravity(1);
        textViewDate.setGravity(1);
        textViewUse.setGravity(1);
        return viewHolder;
    }


    @Override
    public int getCount() {
        return kardexes.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}