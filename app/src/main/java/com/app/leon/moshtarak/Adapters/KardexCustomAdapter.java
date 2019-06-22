package com.app.leon.moshtarak.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.leon.moshtarak.Activities.LastBillActivity;
import com.app.leon.moshtarak.Models.DbTables.Kardex;
import com.app.leon.moshtarak.R;

import java.util.ArrayList;
import java.util.List;

public class KardexCustomAdapter extends RecyclerView.Adapter<KardexCustomAdapter.ViewHolder> {
    private ArrayList<Kardex> kardexes;
    private Context context;
    private int size = 0;
    private int width;

    public KardexCustomAdapter(ArrayList<Kardex> kardexes, int width) {
        this.kardexes = kardexes;
        this.width = width;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View viewCardex;
        if (size % 2 == 0)
            viewCardex = layoutInflater.inflate(R.layout.item_cardex, null);
        else
            viewCardex = layoutInflater.inflate(R.layout.item_cardex_, null);
        return new ViewHolder(viewCardex);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Kardex kardex = kardexes.get(i);
        viewHolder.textViewCost.setText(kardex.getAmount());
        viewHolder.textViewNote.setText(kardex.getDescription());
        viewHolder.textViewUse.setText(kardex.getUsage());
        String owe = kardex.getOweDate();
        String creditor = kardex.getCreditorDate();

        if (creditor.contains("        ") || creditor.equals("          ") || TextUtils.isEmpty(creditor)) {
            viewHolder.textViewDate.setText(owe);
        } else if (owe == null || owe.equals("null") || TextUtils.isEmpty(owe)) {
            viewHolder.textViewDate.setText(creditor);
        }
        viewHolder.imageViewInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LastBillActivity.class);
                context.startActivity(intent);
            }
        });
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/BYekan_3.ttf");
        viewHolder.textViewNote.setTypeface(typeface);
        viewHolder.textViewDate.setTypeface(typeface);
        viewHolder.textViewCost.setTypeface(typeface);
        viewHolder.textViewUse.setTypeface(typeface);

        viewHolder.textViewNote.setWidth((width - viewHolder.imageViewInfo.getWidth()) / 4);
        viewHolder.textViewCost.setWidth((width - viewHolder.imageViewInfo.getWidth()) / 4);
        viewHolder.textViewDate.setWidth((width - viewHolder.imageViewInfo.getWidth()) / 3);
        viewHolder.textViewUse.setWidth((width - viewHolder.imageViewInfo.getWidth()) / 6);

        viewHolder.textViewCost.setGravity(1);
        viewHolder.textViewNote.setGravity(1);
        viewHolder.textViewDate.setGravity(1);
        viewHolder.textViewUse.setGravity(1);
//        viewHolder.textViewUse.setWidth(width - (viewHolder.imageViewInfo.getWidth())
//                + viewHolder.textViewNote.getWidth() + viewHolder.textViewCost.getWidth()
//                + viewHolder.textViewDate.getWidth());
        size++;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }


    @Override
    public int getItemCount() {
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

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDate;
        TextView textViewUse;
        TextView textViewCost;
        TextView textViewNote;
        ImageView imageViewInfo;

        ViewHolder(View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewUse = itemView.findViewById(R.id.textViewUse);
            textViewCost = itemView.findViewById(R.id.textViewCost);
            textViewNote = itemView.findViewById(R.id.textViewNote);
            imageViewInfo = itemView.findViewById(R.id.imageViewInfo);
        }
    }
}
