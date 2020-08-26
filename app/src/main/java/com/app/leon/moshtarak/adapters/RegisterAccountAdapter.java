package com.app.leon.moshtarak.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.leon.moshtarak.Models.Enums.DialogType;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.Utils.CustomDialog;
import com.app.leon.moshtarak.Utils.SharedPreference;

import java.util.ArrayList;

public class RegisterAccountAdapter extends BaseAdapter {

    public ArrayList<String> title;
    public ArrayList<String> alias;
    public ArrayList<String> billIds;
    SharedPreference sharedPreference;
    int index;
    LayoutInflater inflater;
    Context context;

    public RegisterAccountAdapter(ArrayList<String> title, ArrayList<String> alias,
                                  ArrayList<String> billIds, int index, Context context) {
        this.title = title;
        this.alias = alias;
        this.index = index;
        this.billIds = billIds;
        title.remove(this.index);
        alias.remove(this.index);
        billIds.remove(this.index);
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        sharedPreference = new SharedPreference(context);
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View convertView = view;
        if (convertView == null)
            convertView = inflater.inflate(R.layout.register_account_item, null);
        TextView textViewName = convertView.findViewById(R.id.textViewName);
        TextView textViewAlias = convertView.findViewById(R.id.textViewAlias);
        TextView textViewBillId = convertView.findViewById(R.id.textViewBillId);
        LinearLayout linearLayout = convertView.findViewById(R.id.linearLayoutChange);
        ImageView imageView = convertView.findViewById(R.id.imageViewDelete);
        textViewName.setText(title.get(i));
        textViewAlias.setText(alias.get(i).length() > 0 ? alias.get(i) : title.get(i));
//        if (alias.get(i).length() > 0)
        textViewBillId.setText(billIds.get(i));
        linearLayout.setOnClickListener(view1 -> {
            new CustomDialog(DialogType.YellowRedirect, context,
                    context.getString(R.string.change_successful),
                    context.getString(R.string.dear_user),
                    context.getString(R.string.change_account),
                    context.getString(R.string.back_home));
            sharedPreference.putIndex(i < index ? i : i + 1);
        });
        imageView.setOnClickListener(view12 -> {
            sharedPreference.removeItem(i < index ? i : i + 1);
            new CustomDialog(DialogType.YellowRedirect, context,
//                    context.getString(R.string.logout_successful),
                    context.getString(R.string.delete_successful),
                    context.getString(R.string.dear_user),
                    context.getString(R.string.logout),
                    context.getString(R.string.back_home));
        });
//        if (index == i) {
//            convertView.setVisibility(View.GONE);
//            convertView.setLayoutParams(new AbsListView.LayoutParams(-1, -2));
//        }
        return convertView;
    }

    @Override
    public int getCount() {
        return title.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

}
