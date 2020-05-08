package com.app.leon.moshtarak.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.app.leon.moshtarak.BaseItems.BaseActivity;
import com.app.leon.moshtarak.Infrastructure.IAbfaService;
import com.app.leon.moshtarak.Infrastructure.ICallback;
import com.app.leon.moshtarak.Models.DbTables.Suggestion;
import com.app.leon.moshtarak.Models.Enums.DialogType;
import com.app.leon.moshtarak.Models.Enums.ProgressType;
import com.app.leon.moshtarak.Models.InterCommunation.SimpleMessage;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.Utils.CustomDialog;
import com.app.leon.moshtarak.Utils.HttpClientWrapper;
import com.app.leon.moshtarak.Utils.NetworkHelper;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Retrofit;

public class SuggestActivity extends BaseActivity implements ICallback<SimpleMessage> {
    @BindView(R.id.suggestSpinner)
    Spinner spinner;
    @BindView(R.id.radioGroupSuggest)
    RadioGroup radioGroup;
    @BindView(R.id.radioButtonSuggest1)
    RadioButton radioButtonSuggest1;
    @BindView(R.id.radioButtonSuggest2)
    RadioButton radioButtonSuggest2;
    @BindView(R.id.suggestEditText)
    TextInputEditText editText;
    @BindView(R.id.sendButton)
    Button sendButton;
    ArrayList<String> items;
    Context context;


    @Override
    protected void initialize() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View childLayout = Objects.requireNonNull(inflater).inflate(R.layout.suggest_content1, findViewById(R.id.suggest_activity));
        @SuppressLint("CutPasteId") ConstraintLayout parentLayout = findViewById(R.id.base_Content);
        parentLayout.addView(childLayout);
        ButterKnife.bind(this);
        context = this;
        setRadioGroupOnCheckedChanged();
        setSpinnerArrayAdapter();
        setOnSendButtonClickListener();
    }

    void setSpinnerArrayAdapter() {
        items = new ArrayList<>();
        items.add(getString(R.string.sale));
        items.add(getString(R.string.last_bill_2));
        items.add(getString(R.string.kardex));
        items.add(getString(R.string.cardex));
        items.add(getString(R.string.tracking));
        items.add(getString(R.string.mamoor_1));
        items.add(getString(R.string.train));
        items.add(getString(R.string.help));
        items.add(getString(R.string.support));
        items.add(getString(R.string.suggest));
        items.add(getString(R.string.other_));
        spinner.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item,
                items) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                CheckedTextView text = view.findViewById(android.R.id.text1);
                Typeface typeface = Typeface.createFromAsset(context.getAssets(), "font/BYekan_3.ttf");
                text.setTypeface(typeface);
                return view;
            }
        });
    }

    void setRadioGroupOnCheckedChanged() {
        radioButtonSuggest1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                spinner.setVisibility(View.VISIBLE);
            }
        });
        radioButtonSuggest2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                spinner.setVisibility(View.GONE);
            }
        });
    }

    void setOnSendButtonClickListener() {
        sendButton.setOnClickListener(v -> {
            View viewFocus;
            if (Objects.requireNonNull(editText.getText()).length() < 1) {
                editText.setError(getString(R.string.error_empty));
                viewFocus = editText;
                viewFocus.requestFocus();
            } else {
                int select;
                if (radioButtonSuggest2.isChecked()) {
                    select = 1;
                } else {
                    select = spinner.getSelectedItemPosition() + 2;
                }
                Retrofit retrofit = NetworkHelper.getInstance();
                final IAbfaService suggestion = retrofit.create(IAbfaService.class);
                Call<SimpleMessage> call = suggestion.sendSuggestion(new Suggestion(
                        String.valueOf(select), editText.getText().toString(),
                        String.valueOf(Build.VERSION.RELEASE), getDeviceName()));
                HttpClientWrapper.callHttpAsync(call, SuggestActivity.this, context,
                        ProgressType.SHOW.getValue());
            }
        });
    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    @Override
    public void execute(SimpleMessage simpleMessage) {
        new CustomDialog(DialogType.Yellow, context, simpleMessage.getMessage(), context.getString(R.string.dear_user),
                context.getString(R.string.suggest), context.getString(R.string.accepted));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_contact) {
            Intent intent = new Intent(getApplicationContext(), ContactUsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
