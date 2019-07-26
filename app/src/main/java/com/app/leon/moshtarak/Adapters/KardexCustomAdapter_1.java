package com.app.leon.moshtarak.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.leon.moshtarak.Activities.LastBillActivity;
import com.app.leon.moshtarak.Models.DbTables.Kardex;
import com.app.leon.moshtarak.R;

import java.util.ArrayList;

public class KardexCustomAdapter_1 extends ArrayAdapter<Kardex> {
    private ArrayList<Kardex> kardexes;
    private Context context;

    public KardexCustomAdapter_1(ArrayList<Kardex> kardexes, Context context) {
        super(context, 0);
        this.kardexes = kardexes;
        this.context = context;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View viewHolder;
        if (position % 2 == 0)
            viewHolder = layoutInflater.inflate(R.layout.item_cardex, null);
        else
            viewHolder = layoutInflater.inflate(R.layout.item_cardex_, null);

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
        textViewCost.setText(kardex.getAmount());
        textViewNote.setText(kardex.getDescription());
        textViewUse.setText(kardex.getUsage());

        String owe = kardex.getOweDate();
        String creditor = kardex.getCreditorDate().trim();

        if (TextUtils.isEmpty(creditor)) {
            textViewDate.setText(owe);
        } else if (owe == null || owe.equals("null") || TextUtils.isEmpty(owe)) {
            textViewDate.setText(creditor);
        }
        imageViewInfo.setOnClickListener(view ->

        {
            Intent intent = new Intent(context, LastBillActivity.class);
            context.startActivity(intent);
        });
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/BYekan_3.ttf");
        textViewNote.setTypeface(typeface);
        textViewDate.setTypeface(typeface);
        textViewCost.setTypeface(typeface);
        textViewUse.setTypeface(typeface);
        if (kardex.getDescription().

                contains("صدور قبض"))
            textViewNote.setTextColor(context.getResources().

                    getColor(R.color.colorAccentIndigo));

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

//    class ViewHolder extends RecyclerView.ViewHolder {
//        TextView textViewDate;
//        TextView textViewUse;
//        TextView textViewCost;
//        TextView textViewNote;
//        ImageView imageViewInfo;
//
//        ViewHolder(View itemView) {
//            super(itemView);
//            textViewDate = itemView.findViewById(R.id.textViewDate);
//            textViewUse = itemView.findViewById(R.id.textViewUse);
//            textViewCost = itemView.findViewById(R.id.textViewCost);
//            textViewNote = itemView.findViewById(R.id.textViewNote);
//            imageViewInfo = itemView.findViewById(R.id.imageViewInfo);
//        }
//    }
}
