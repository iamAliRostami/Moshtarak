package com.app.leon.moshtarak.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.Utils.FontManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowSMSActivity extends AppCompatActivity {
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> sms;
    @BindView(R.id.listViewSMS)
    ListView listViewSMS;
    Context context;
    @BindView(R.id.textViewSmsLevel)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_sms_activity);
        context = this;
        ButterKnife.bind(this);
        FontManager fontManager = new FontManager(getApplicationContext());
        fontManager.setFont(findViewById(R.id.relativeLayout));
        Intent intent = getIntent();
        sms = intent.getStringArrayListExtra("SMS");
        textView.setText(textView.getText().toString().concat(intent.getStringExtra("SMS_LEVEL")));
//        arrayAdapter = new ArrayAdapter<>(this, R.layout.sms_layout, sms);

        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.sms_layout, sms) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = view.findViewById(android.R.id.text1);
                Typeface typeface = Typeface.createFromAsset(context.getAssets(), "font/BYekan_3.ttf");
                text.setTypeface(typeface);
                return view;
            }
        };
        listViewSMS.setAdapter(arrayAdapter);
    }
}
