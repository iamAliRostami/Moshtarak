package com.app.leon.moshtarak.Activities;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.app.leon.moshtarak.Adapters.TrackCustomAdapter_1;
import com.app.leon.moshtarak.BaseItems.BaseActivity;
import com.app.leon.moshtarak.Infrastructure.IAbfaService;
import com.app.leon.moshtarak.Infrastructure.ICallback;
import com.app.leon.moshtarak.Models.DbTables.TrackingDto;
import com.app.leon.moshtarak.Models.Enums.ProgressType;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.Utils.HttpClientWrapper;
import com.app.leon.moshtarak.Utils.NetworkHelper;
import com.app.leon.moshtarak.databinding.TrackingsContentBinding;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Retrofit;

public class TrackingsActivity extends BaseActivity
        implements ICallback<ArrayList<TrackingDto>> {
    View viewFocus;
    Context context;
    TrackingsContentBinding binding;

    @SuppressLint("CutPasteId")
    @Override
    protected void initialize() {
        binding = TrackingsContentBinding.inflate(getLayoutInflater());
        View childLayout = binding.getRoot();
        ConstraintLayout parentLayout = findViewById(R.id.base_Content);
        parentLayout.addView(childLayout);
        context = this;
        setOnButtonSubmitClickListener();
        setEditTextTrackOnLongClickListener();
    }

    void setEditTextTrackOnLongClickListener() {
        binding.editTextTrack.setOnLongClickListener(v -> {
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
                binding.editTextTrack.setText(text);
            }
            return false;
        });
    }

    void setOnButtonSubmitClickListener() {
        binding.buttonSubmit.setOnClickListener(v -> {
            boolean cancel = false;
            if (Objects.requireNonNull(binding.editTextTrack.getText()).length() < 1) {
                cancel = true;
                binding.editTextTrack.setError(getString(R.string.error_empty));
                viewFocus = binding.editTextTrack;
                viewFocus.requestFocus();
            }
            if (!cancel) {
                Retrofit retrofit = NetworkHelper.getInstance();
                final IAbfaService tracking = retrofit.create(IAbfaService.class);
                Call<ArrayList<TrackingDto>> call = tracking.getTrackings(binding.editTextTrack.getText().toString());
                HttpClientWrapper.callHttpAsync(call, TrackingsActivity.this, context, ProgressType.SHOW.getValue());
            }
        });
    }

    @Override
    public void execute(ArrayList<TrackingDto> trackingDtos) {
        binding.linearLayout1.setVisibility(View.GONE);
        binding.linearLayout2.setVisibility(View.VISIBLE);
        TrackCustomAdapter_1 trackCustomAdapter_1 = new TrackCustomAdapter_1(context, trackingDtos);
        binding.listViewTrack.setAdapter(trackCustomAdapter_1);
    }
}
