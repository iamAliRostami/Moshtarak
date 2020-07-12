package com.app.leon.moshtarak.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.databinding.ShowSmsActivityBinding;

import java.util.ArrayList;
import java.util.Objects;

public class ShowSMSActivity extends AppCompatActivity {
    Context context;
    ShowSmsActivityBinding binding;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> sms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ShowSmsActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        context = this;
        Intent intent = getIntent();
        sms = intent.getStringArrayListExtra("SMS");
        binding.textViewSmsLevel.setText(binding.textViewSmsLevel.getText().toString().concat(Objects.requireNonNull(intent.getStringExtra("SMS_LEVEL"))));
        arrayAdapter = new ArrayAdapter<>(this, R.layout.sms_layout, sms);
        if (sms.size() < 1)
            binding.textViewNoSMS.setVisibility(View.VISIBLE);
//        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),
//                R.layout.sms_layout, sms) {
//            @NonNull
//            @Override
//            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
//                View view = super.getView(position, convertView, parent);
//                TextView text = view.findViewById(android.R.id.text1);
////                Typeface typeface = Typeface.createFromAsset(context.getAssets(), "font/my_font.ttf");
////                text.setTypeface(typeface);
//                return view;
//            }
//        };
        binding.listViewSMS.setAdapter(arrayAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Runtime.getRuntime().totalMemory();
        Runtime.getRuntime().freeMemory();
        Runtime.getRuntime().maxMemory();
        Debug.getNativeHeapAllocatedSize();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
        context = null;
        arrayAdapter = null;
        sms = null;
        Runtime.getRuntime().totalMemory();
        Runtime.getRuntime().freeMemory();
        Runtime.getRuntime().maxMemory();
        Debug.getNativeHeapAllocatedSize();
    }
}
