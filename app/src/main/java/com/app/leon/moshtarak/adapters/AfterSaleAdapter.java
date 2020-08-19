package com.app.leon.moshtarak.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import com.app.leon.moshtarak.R;

import java.util.ArrayList;

public class AfterSaleAdapter extends BaseAdapter {
    public ArrayList<String> title;
    public ArrayList<Boolean> selected;
    LayoutInflater inflater;

    public AfterSaleAdapter(Context c, ArrayList<String> title) {
        this.title = title;
        selected = new ArrayList<>();
        for (int i = 0; i < title.size(); i++)
            selected.add(false);
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return title.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    public View getView(int position, View convertView, ViewGroup parent) {
        CheckBoxViewHolder holder;
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.item_after_sale, null);
        }
        holder = new CheckBoxViewHolder(view);
        holder.checkBox.setText(title.get(position));
        holder.checkBox.setOnClickListener(view1 -> {
            holder.checkBox.setChecked(!holder.checkBox.isChecked());
            selected.set(position, holder.checkBox.isChecked());
        });
        holder.checkBox.setChecked(selected.get(position));
        return view;
    }

    public static class CheckBoxViewHolder {
        public CheckedTextView checkBox;

        public CheckBoxViewHolder(View view) {
            this.checkBox = view.findViewById(android.R.id.text1);
        }
    }
}
