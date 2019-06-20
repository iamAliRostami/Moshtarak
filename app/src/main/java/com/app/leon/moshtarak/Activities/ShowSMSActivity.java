package com.app.leon.moshtarak.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.app.leon.moshtarak.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowSMSActivity extends AppCompatActivity {
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> sms;
    @BindView(R.id.listViewSMS)
    ListView listViewSMS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_sms_activity);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        sms = intent.getStringArrayListExtra("SMS");

        arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, sms);
        listViewSMS.setAdapter(arrayAdapter);
    }
}
