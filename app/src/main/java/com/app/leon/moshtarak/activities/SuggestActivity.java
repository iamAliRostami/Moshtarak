package com.app.leon.moshtarak.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Debug;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.app.leon.moshtarak.BaseItems.BaseActivity;
import com.app.leon.moshtarak.Infrastructure.IAbfaService;
import com.app.leon.moshtarak.Infrastructure.ICallback;
import com.app.leon.moshtarak.Infrastructure.ICallbackError;
import com.app.leon.moshtarak.Infrastructure.ICallbackIncomplete;
import com.app.leon.moshtarak.Models.DbTables.Suggestion;
import com.app.leon.moshtarak.Models.Enums.DialogType;
import com.app.leon.moshtarak.Models.Enums.ProgressType;
import com.app.leon.moshtarak.Models.InterCommunation.SimpleMessage;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.Utils.CustomDialog;
import com.app.leon.moshtarak.Utils.CustomErrorHandlingNew;
import com.app.leon.moshtarak.Utils.HttpClientWrapper;
import com.app.leon.moshtarak.Utils.NetworkHelper;
import com.app.leon.moshtarak.databinding.SuggestContentBinding;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SuggestActivity extends BaseActivity {
    SuggestContentBinding binding;
    ArrayList<String> items;
    Context context;

    @SuppressLint("CutPasteId")
    @Override
    protected void initialize() {
        binding = SuggestContentBinding.inflate(getLayoutInflater());
        View childLayout = binding.getRoot();
        ConstraintLayout parentLayout = findViewById(R.id.base_Content);
        parentLayout.addView(childLayout);

        context = this;
        setRadioGroupOnCheckedChanged();
        setSpinnerArrayAdapter();
        setOnSendButtonClickListener();
    }

    void setSpinnerArrayAdapter() {
        items = new ArrayList<>();
        items.add(getString(R.string.sale));
        items.add(getString(R.string.last_bill_2));
        items.add(getString(R.string.cardex));
        items.add(getString(R.string.cardex_ab));
        items.add(getString(R.string.tracking));
        items.add(getString(R.string.mamoor_1));
        items.add(getString(R.string.train));
        items.add(getString(R.string.help));
        items.add(getString(R.string.support));
        items.add(getString(R.string.suggest));
        items.add(getString(R.string.other_));
        binding.suggestSpinner.setAdapter(new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_dropdown_item, items));
    }

    void setRadioGroupOnCheckedChanged() {
        binding.radioButtonSuggest1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.suggestSpinner.setVisibility(View.VISIBLE);
            }
        });
        binding.radioButtonSuggest2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.suggestSpinner.setVisibility(View.GONE);
            }
        });
    }

    void setOnSendButtonClickListener() {
        binding.sendButton.setOnClickListener(v -> {
            View viewFocus;
            if (Objects.requireNonNull(binding.suggestEditText.getText()).length() < 1) {
                binding.suggestEditText.setError(getString(R.string.error_empty));
                viewFocus = binding.suggestEditText;
                viewFocus.requestFocus();
            } else {
                int select;
                if (binding.radioButtonSuggest2.isChecked()) {
                    select = 1;
                } else {
                    select = binding.suggestSpinner.getSelectedItemPosition() + 2;
                }
                Retrofit retrofit = NetworkHelper.getInstance();
                final IAbfaService suggestion = retrofit.create(IAbfaService.class);
                Call<SimpleMessage> call = suggestion.sendSuggestion(new Suggestion(
                        String.valueOf(select), binding.suggestEditText.getText().toString(),
                        String.valueOf(Build.VERSION.RELEASE), getDeviceName()));
                Suggest suggest = new Suggest();
                SuggestIncomplete incomplete = new SuggestIncomplete();
                GetError error = new GetError();
                HttpClientWrapper.callHttpAsync(call, ProgressType.SHOW.getValue(), context,
                        suggest, incomplete, error);
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

    class Suggest implements ICallback<SimpleMessage> {
        @Override
        public void execute(SimpleMessage simpleMessage) {
            new CustomDialog(DialogType.Yellow, context, simpleMessage.getMessage(), context.getString(R.string.dear_user),
                    context.getString(R.string.suggest), context.getString(R.string.accepted));
        }
    }

    class SuggestIncomplete implements ICallbackIncomplete<SimpleMessage> {
        @Override
        public void executeIncomplete(Response<SimpleMessage> response) {
            CustomErrorHandlingNew customErrorHandlingNew = new CustomErrorHandlingNew(context);
            String error = customErrorHandlingNew.getErrorMessageDefault(response);
            new CustomDialog(DialogType.Yellow, context, error,
                    context.getString(R.string.dear_user),
                    context.getString(R.string.login),
                    context.getString(R.string.accepted));
        }
    }

    class GetError implements ICallbackError {
        @Override
        public void executeError(Throwable t) {
            CustomErrorHandlingNew customErrorHandlingNew = new CustomErrorHandlingNew(context);
            String error = customErrorHandlingNew.getErrorMessageTotal(t);
            new CustomDialog(DialogType.Yellow, context, error,
                    context.getString(R.string.dear_user),
                    context.getString(R.string.login),
                    context.getString(R.string.accepted));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_contact) {
            Intent intent = new Intent(getApplicationContext(), ContactDeveloperActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
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
        items = null;
        Runtime.getRuntime().totalMemory();
        Runtime.getRuntime().freeMemory();
        Runtime.getRuntime().maxMemory();
        Debug.getNativeHeapAllocatedSize();
    }
}
