package com.app.leon.moshtarak.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.leon.moshtarak.Models.DbTables.Session;
import com.app.leon.moshtarak.R;

import java.util.List;

public class SessionCustomAdapter extends BaseAdapter {
    private List<Session> sessionList = null;
    private Context context;

    public SessionCustomAdapter(List<Session> sessions, Context context) {
        this.sessionList = sessions;
//        Collections.sort(this.sessionList, (o1, o2) -> o2.getRegisterDayJalali().compareTo(o1.getRegisterDayJalali()));
        this.context = context;
    }

    @Override
    public int getCount() {
        return sessionList.size();
    }

    @Override
    public Session getItem(int position) {
        return sessionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        view = LayoutInflater.from(context).inflate(R.layout.item_session, parent, false);
        Session session = getItem(position);

        TextView textViewModel = view.findViewById(R.id.textViewModel);
        TextView textViewOsVersion = view.findViewById(R.id.textViewOsVersion);
        TextView textViewLoginDate = view.findViewById(R.id.textViewLoginDate);

        textViewModel.setTextAppearance(context, R.style.text_default);
        textViewOsVersion.setTextAppearance(context, R.style.text_default);

        textViewModel.setText(session.getPhoneModel());
        textViewLoginDate.setText(session.getRegisterDayJalali());
        textViewOsVersion.setText(session.getOsVersion());

        return view;
    }
}
