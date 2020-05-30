package com.app.leon.moshtarak.Adapters;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.leon.moshtarak.Models.DbTables.Request;
import com.app.leon.moshtarak.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.Context.CLIPBOARD_SERVICE;

public class RecoverCodeCustomAdapter extends BaseAdapter {
    private ArrayList<Request> requests;
    private List<Request> tempRequests = null;
    private Context context;

    public RecoverCodeCustomAdapter(List<Request> requests, Context context) {
        this.tempRequests = requests;
//        Collections.sort(this.tempRequests, (o1, o2) -> o2.getRequestDateAsk().compareTo(o1.getRequestDateAsk()));
        this.requests = new ArrayList<>();
        this.requests.addAll(requests);
//        Collections.sort(this.requests, (o1, o2) -> o2.getRequestDateAsk().compareTo(o1.getRequestDateAsk()));
        this.context = context;
    }

    @Override
    public int getCount() {
        return tempRequests.size();
    }

    @Override
    public Request getItem(int position) {
        return tempRequests.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        view = LayoutInflater.from(context).inflate(R.layout.item_recovery_code, parent, false);
        Request request = getItem(position);

        TextView textViewTitle = view.findViewById(R.id.textViewTitle);
        TextView textViewRequestDateAsk = view.findViewById(R.id.textViewRequestDateAsk);
        TextView textViewRequestRegister = view.findViewById(R.id.textViewRequestDateRegister);
        TextView textViewParNumber = view.findViewById(R.id.textViewParNumber);

        LinearLayout linearLayout = view.findViewById(R.id.linearLayout);
        linearLayout.setOnLongClickListener(v -> {
            final ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("Source Text", textViewParNumber.getText().toString());
            if (clipboardManager != null) {
                clipboardManager.setPrimaryClip(clipData);
            }
            Toast.makeText(v.getContext(), context.getString(R.string.copy_in_clipboard), Toast.LENGTH_SHORT).show();
            return false;
        });

        textViewTitle.setText(request.getRequestTitles());
        textViewRequestDateAsk.setText(request.getRequestDateAsk());
        textViewRequestRegister.setText(request.getRequestDateRegister());
        textViewParNumber.setText(request.getRequestParNumber());

        return view;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        tempRequests.clear();
        if (charText.length() == 0) {
            tempRequests.addAll(requests);
        } else {
            for (Request r : requests) {
                if (r.getRequestTitles().toLowerCase(Locale.getDefault()).contains(charText)) {
                    tempRequests.add(r);
                }
            }
        }
        notifyDataSetChanged();
    }
}
