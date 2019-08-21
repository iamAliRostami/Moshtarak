package com.app.leon.moshtarak.Activities;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.app.leon.moshtarak.Adapters.TrackCustomAdapter_1;
import com.app.leon.moshtarak.BaseItems.BaseActivity;
import com.app.leon.moshtarak.Infrastructure.IAbfaService;
import com.app.leon.moshtarak.Infrastructure.ICallback;
import com.app.leon.moshtarak.Models.DbTables.TrackingDto;
import com.app.leon.moshtarak.Models.Enums.ProgressType;
import com.app.leon.moshtarak.Models.ViewModels.UiElementInActivity;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.Utils.HttpClientWrapper;
import com.app.leon.moshtarak.Utils.NetworkHelper;

import java.util.ArrayList;

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

    @Override
    protected UiElementInActivity getUiElementsInActivity() {
        UiElementInActivity uiElementInActivity = new UiElementInActivity();
        uiElementInActivity.setContentViewId(R.layout.trackings_activity);
        return uiElementInActivity;
    }

    @Override
    protected void initialize() {
        ButterKnife.bind(this);
        context = this;
        setOnButtonSubmitClickListener();
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
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        linearLayout1.setVisibility(View.GONE);
        linearLayout2.setVisibility(View.VISIBLE);
        TrackCustomAdapter_1 trackCustomAdapter_1 = new TrackCustomAdapter_1(context, trackingDtos);
        listViewTrack.setAdapter(trackCustomAdapter_1);
    }
}
