package com.app.leon.moshtarak.Activities;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.app.leon.moshtarak.BaseItems.BaseActivity;
import com.app.leon.moshtarak.Infrastructure.IAbfaService;
import com.app.leon.moshtarak.Infrastructure.ICallback;
import com.app.leon.moshtarak.Models.DbTables.Suggestion;
import com.app.leon.moshtarak.Models.Enums.DialogType;
import com.app.leon.moshtarak.Models.Enums.ProgressType;
import com.app.leon.moshtarak.Models.InterCommunation.SimpleMessage;
import com.app.leon.moshtarak.Models.ViewModels.UiElementInActivity;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.Utils.CustomDialog;
import com.app.leon.moshtarak.Utils.HttpClientWrapper;
import com.app.leon.moshtarak.Utils.NetworkHelper;

import java.util.ArrayList;

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
    EditText editText;
    @BindView(R.id.sendButton)
    Button sendButton;
    ArrayList<String> items;
    Context context;

    @Override
    protected UiElementInActivity getUiElementsInActivity() {
        UiElementInActivity uiElementInActivity = new UiElementInActivity();
        uiElementInActivity.setContentViewId(R.layout.suggest_activity);
        return uiElementInActivity;
    }

    @Override
    protected void initialize() {
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
        items.add(getString(R.string.mamoor));
        items.add(getString(R.string.train));
        items.add(getString(R.string.help));
        items.add(getString(R.string.support));
        items.add(getString(R.string.suggest));
        items.add(getString(R.string.other));
        spinner.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item,
                items));
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
            if (editText.getText().length() < 1) {
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
}
