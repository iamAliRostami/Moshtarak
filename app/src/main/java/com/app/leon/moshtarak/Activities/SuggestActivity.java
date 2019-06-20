package com.app.leon.moshtarak.Activities;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.app.leon.moshtarak.BaseItems.BaseActivity;
import com.app.leon.moshtarak.Models.ViewModels.UiElementInActivity;
import com.app.leon.moshtarak.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SuggestActivity extends BaseActivity {
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
    Button button;
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
    }

    void setSpinnerArrayAdapter() {
        items = new ArrayList<>();
        items.add(getString(R.string.sale));
        items.add(getString(R.string.last_bill));
        items.add(getString(R.string.kardex));
        items.add(getString(R.string.cardex));
        items.add(getString(R.string.tracking));
        items.add(getString(R.string.mamoor));
        items.add(getString(R.string.train));
        items.add(getString(R.string.help));
        items.add(getString(R.string.support));
        items.add(getString(R.string.suggest));
        items.add("دیگر");
        spinner.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item,
                items));

    }

    void setRadioGroupOnCheckedChanged() {
        radioButtonSuggest1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    spinner.setVisibility(View.VISIBLE);
                }
            }
        });
        radioButtonSuggest2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    spinner.setVisibility(View.GONE);
                }
            }
        });
    }
}
