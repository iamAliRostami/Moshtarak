package com.app.leon.moshtarak.Activities;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.app.leon.moshtarak.Adapters.FollowUpCustomAdapter;
import com.app.leon.moshtarak.BaseItems.BaseActivity;
import com.app.leon.moshtarak.Infrastructure.IAbfaService;
import com.app.leon.moshtarak.Infrastructure.ICallback;
import com.app.leon.moshtarak.Models.DbTables.FollowUpDto;
import com.app.leon.moshtarak.Models.Enums.ProgressType;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.Utils.HttpClientWrapperNew;
import com.app.leon.moshtarak.Utils.NetworkHelper;
import com.app.leon.moshtarak.databinding.FollowUpContentBinding;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Retrofit;

public class FollowUpActivity extends BaseActivity implements ICallback<ArrayList<FollowUpDto>> {
    View viewFocus;
    Context context;
    FollowUpContentBinding binding;

    @SuppressLint("CutPasteId")
    @Override
    protected void initialize() {
        binding = FollowUpContentBinding.inflate(getLayoutInflater());
        View childLayout = binding.getRoot();
        ConstraintLayout parentLayout = findViewById(R.id.base_Content);
        parentLayout.addView(childLayout);
        context = this;
        setOnButtonSubmitClickListener();
        setEditTextFollowUpOnLongClickListener();
    }

    void setEditTextFollowUpOnLongClickListener() {
        binding.editTextFollowUp.setOnLongClickListener(v -> {
            Object clipboardService = getSystemService(CLIPBOARD_SERVICE);
            final ClipboardManager clipboardManager = (ClipboardManager) clipboardService;
            ClipData clipData = null;
            if (clipboardManager != null) {
                clipData = clipboardManager.getPrimaryClip();
            }
            int itemCount = 0;
            if (clipData != null) {
                itemCount = clipData.getItemCount();
            }
            if (itemCount > 0) {
                ClipData.Item item = clipData.getItemAt(0);
                String text = item.getText().toString();
                binding.editTextFollowUp.setText(text);
            }
            return false;
        });
    }

    void setOnButtonSubmitClickListener() {
        binding.buttonSubmit.setOnClickListener(v -> {
            boolean cancel = false;
            if (Objects.requireNonNull(binding.editTextFollowUp.getText()).length() < 1) {
                cancel = true;
                binding.editTextFollowUp.setError(getString(R.string.error_empty));
                viewFocus = binding.editTextFollowUp;
                viewFocus.requestFocus();
            }
            if (!cancel) {
                Retrofit retrofit = NetworkHelper.getInstance();
                final IAbfaService tracking = retrofit.create(IAbfaService.class);
                Call<ArrayList<FollowUpDto>> call = tracking.getTrackings(binding.editTextFollowUp.getText().toString());
                HttpClientWrapperNew.callHttpAsync(call, FollowUpActivity.this, context, ProgressType.SHOW.getValue());
            }
        });
    }

    @Override
    public void execute(ArrayList<FollowUpDto> followUpDtos) {
        binding.linearLayout1.setVisibility(View.GONE);
        binding.linearLayout2.setVisibility(View.VISIBLE);
        FollowUpCustomAdapter followUpCustomAdapter = new FollowUpCustomAdapter(context, followUpDtos);
        binding.listViewFollowUp.setAdapter(followUpCustomAdapter);
    }
}
