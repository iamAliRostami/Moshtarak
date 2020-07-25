package com.app.leon.moshtarak.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.app.leon.moshtarak.Models.DbTables.Cardex;
import com.app.leon.moshtarak.Models.Enums.BundleEnum;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.activities.LastBillActivity;

import java.util.ArrayList;
import java.util.Collections;

public class CardexCustomAdapter extends ArrayAdapter<Cardex> {
    private ArrayList<Cardex> cardexes;
    private Context context;

    public CardexCustomAdapter(ArrayList<Cardex> cardexes, Context context) {
        super(context, 0);
        this.cardexes = cardexes;
        for (int i = 0; i < this.cardexes.size(); i++) {
            Cardex cardex = this.cardexes.get(i);
            String owe = cardex.getOweDate();
            String creditor = cardex.getCreditorDate().trim();
            if (owe == null || owe.equals("null") || TextUtils.isEmpty(owe)) {
                cardex.setOweDate(creditor);
            }
        }
        Collections.sort(this.cardexes, (o1, o2) -> o2.getOweDate().compareTo(o1.getOweDate()));
        this.cardexes.add(0, new Cardex());
        this.context = context;
    }

    @NonNull
    @SuppressLint({"InflateParams", "DefaultLocale"})
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View viewHolder;
        if (position == 0) viewHolder = layoutInflater.inflate(R.layout.item_cardex_3, null);
        else {
            if (position % 2 == 0)
                viewHolder = layoutInflater.inflate(R.layout.item_cardex_1, null);
            else
                viewHolder = layoutInflater.inflate(R.layout.item_cardex_2, null);

            Cardex cardex = cardexes.get(position);
            TextView textViewDate = viewHolder.findViewById(R.id.textViewDate);
            TextView textViewUse = viewHolder.findViewById(R.id.textViewUseM3);
            TextView textViewCost = viewHolder.findViewById(R.id.textViewCost);
            TextView textViewNote = viewHolder.findViewById(R.id.textViewNote);
            ImageView imageViewInfo = viewHolder.findViewById(R.id.imageViewInfo);
            LinearLayout linearLayoutItem = viewHolder.findViewById(R.id.linearLayoutItem);

            float floatNumber = Float.parseFloat(cardex.getAmount());
            int intNumber = (int) floatNumber;
            textViewCost.setText(String.valueOf(intNumber));
            textViewNote.setText(cardex.getDescription());
            floatNumber = Float.parseFloat(cardex.getUsage());
            intNumber = (int) floatNumber;
            textViewUse.setText(String.valueOf(intNumber));

            textViewDate.setText(cardex.getOweDate());

            if (cardex.isBill()) {
                textViewNote.setTextColor(
                        context.getResources().getColor(R.color.colorAccentIndigo));
                textViewCost.setTextColor(
                        context.getResources().getColor(R.color.pink2));
                imageViewInfo.setImageDrawable(
                        context.getResources().getDrawable(R.drawable.img_last_bill1));
//                imageViewInfo.setImageDrawable(context.getResources().getDrawable(R.drawable.cash_payment));
//                src = "@drawable/cash_payment"

            }
            linearLayoutItem.setOnClickListener(view ->
            {
                Intent intent = new Intent(context, LastBillActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(BundleEnum.ID.getValue(), cardex.getId());
                bundle.putString(BundleEnum.ZONE_ID.getValue(), cardex.getZoneId());
                if (cardex.isPay())
                    intent.putExtra(BundleEnum.THIS_BILL_PAYED.getValue(), bundle);
                else if (cardex.isBill())
                    intent.putExtra(BundleEnum.THIS_BILL.getValue(), bundle);
                context.startActivity(intent);
            });
            textViewCost.setGravity(1);
            textViewNote.setGravity(1);
            textViewDate.setGravity(1);
            textViewUse.setGravity(1);
        }
        return viewHolder;
    }


    @Override
    public int getCount() {
        return cardexes.size();
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
