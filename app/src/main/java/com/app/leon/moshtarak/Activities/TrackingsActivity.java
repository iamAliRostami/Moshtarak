package com.app.leon.moshtarak.Activities;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.app.leon.moshtarak.Adapters.TrackCustomAdapter_1;
import com.app.leon.moshtarak.BaseItems.BaseActivity;
import com.app.leon.moshtarak.Infrastructure.IAbfaService;
import com.app.leon.moshtarak.Infrastructure.ICallback;
import com.app.leon.moshtarak.Models.DbTables.TrackingDto;
import com.app.leon.moshtarak.Models.Enums.ProgressType;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.Utils.FontManager;
import com.app.leon.moshtarak.Utils.HttpClientWrapper;
import com.app.leon.moshtarak.Utils.NetworkHelper;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Retrofit;

public class TrackingsActivity extends BaseActivity
        implements ICallback<ArrayList<TrackingDto>> {
    @BindView(R.id.editTextTrack)
    EditText editTextTrack;
    @BindView(R.id.buttonSubmit)
    Button buttonSubmit;
    @BindView(R.id.linearLayout1)
    LinearLayout linearLayout1;
    @BindView(R.id.linearLayout2)
    LinearLayout linearLayout2;
    @BindView(R.id.listViewTrack)
    ListView listViewTrack;
    View viewFocus;
    Context context;

    @SuppressLint("CutPasteId")
    @Override
    protected void initialize() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View childLayout = Objects.requireNonNull(inflater).inflate(R.layout.trackings_content, findViewById(R.id.tracking_activity));
        ConstraintLayout parentLayout = findViewById(R.id.base_Content);
        parentLayout.addView(childLayout);
        ButterKnife.bind(this);
        FontManager fontManager = new FontManager(getApplicationContext());
        fontManager.setFont(findViewById(R.id.tracking_activity));
        context = this;
        setOnButtonSubmitClickListener();
        setEditTextTrackOnLongClickListener();
    }

    void setEditTextTrackOnLongClickListener() {
        editTextTrack.setOnLongClickListener(v -> {
            Object clipboardService = getSystemService(CLIPBOARD_SERVICE);
            final ClipboardManager clipboardManager = (ClipboardManager) clipboardService;
            ClipData clipData = clipboardManager.getPrimaryClip();
            int itemCount = clipData.getItemCount();
            if (itemCount > 0) {
                ClipData.Item item = clipData.getItemAt(0);
                String text = item.getText().toString();
                editTextTrack.setText(text);
            }
            return false;
        });
    }
    void setOnButtonSubmitClickListener() {
        buttonSubmit.setOnClickListener(v -> {
            boolean cancel = false;
            if (editTextTrack.getText().length() < 1) {
                cancel = true;
                viewFocus = editTextTrack;
                viewFocus.requestFocus();
            }
            if (!cancel) {
                Retrofit retrofit = NetworkHelper.getInstance();
                final IAbfaService tracking = retrofit.create(IAbfaService.class);
                Call<ArrayList<TrackingDto>> call = tracking.getTrackings(editTextTrack.getText().toString());
                HttpClientWrapper.callHttpAsync(call, TrackingsActivity.this, context, ProgressType.SHOW.getValue());
            }
        });
    }

    @Override
    public void execute(ArrayList<TrackingDto> trackingDtos) {
        linearLayout1.setVisibility(View.GONE);
        linearLayout2.setVisibility(View.VISIBLE);
        TrackCustomAdapter_1 trackCustomAdapter_1 = new TrackCustomAdapter_1(context, trackingDtos);
        listViewTrack.setAdapter(trackCustomAdapter_1);
    }
}
