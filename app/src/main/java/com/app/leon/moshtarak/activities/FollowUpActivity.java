package com.app.leon.moshtarak.activities;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Debug;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.app.leon.moshtarak.BaseItems.BaseActivity;
import com.app.leon.moshtarak.Infrastructure.IAbfaService;
import com.app.leon.moshtarak.Infrastructure.ICallback;
import com.app.leon.moshtarak.Infrastructure.ICallbackError;
import com.app.leon.moshtarak.Infrastructure.ICallbackIncomplete;
import com.app.leon.moshtarak.Models.DbTables.FollowUpDto;
import com.app.leon.moshtarak.Models.Enums.DialogType;
import com.app.leon.moshtarak.Models.Enums.ProgressType;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.Utils.CustomDialog;
import com.app.leon.moshtarak.Utils.CustomErrorHandlingNew;
import com.app.leon.moshtarak.Utils.HttpClientWrapper;
import com.app.leon.moshtarak.Utils.NetworkHelper;
import com.app.leon.moshtarak.adapters.FollowUpCustomAdapter;
import com.app.leon.moshtarak.databinding.FollowUpContentBinding;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FollowUpActivity extends BaseActivity {
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
                Call<ArrayList<FollowUpDto>> call = tracking.followingUp(
                        binding.editTextFollowUp.getText().toString());
                Follow follow = new Follow();
                GetError getError = new GetError();
                FollowIncomplete followIncomplete = new FollowIncomplete();
                HttpClientWrapper.callHttpAsync(call, ProgressType.SHOW.getValue(), context,
                        follow, followIncomplete, getError);
            }
        });
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
        Runtime.getRuntime().totalMemory();
        Runtime.getRuntime().freeMemory();
        Runtime.getRuntime().maxMemory();
        Debug.getNativeHeapAllocatedSize();
    }

    class Follow implements ICallback<ArrayList<FollowUpDto>> {
        @Override
        public void execute(ArrayList<FollowUpDto> followUpDtos) {
            binding.linearLayout1.setVisibility(View.GONE);
            binding.linearLayout2.setVisibility(View.VISIBLE);
            FollowUpCustomAdapter followUpCustomAdapter =
                    new FollowUpCustomAdapter(context, followUpDtos);
            binding.listViewFollowUp.setAdapter(followUpCustomAdapter);
        }
    }

    class FollowIncomplete implements ICallbackIncomplete<ArrayList<FollowUpDto>> {
        @Override
        public void executeIncomplete(Response<ArrayList<FollowUpDto>> response) {
            CustomErrorHandlingNew customErrorHandlingNew = new CustomErrorHandlingNew(context);
            String error = customErrorHandlingNew.getErrorMessageDefault(response);
            if (response.code() == 404) {
                CustomErrorHandlingNew.APIError apiError = customErrorHandlingNew.parseError(response);
                error = apiError.message();
            }
            new CustomDialog(DialogType.Yellow, FollowUpActivity.this, error,
                    FollowUpActivity.this.getString(R.string.dear_user),
                    FollowUpActivity.this.getString(R.string.login),
                    FollowUpActivity.this.getString(R.string.accepted));
        }
    }

    class GetError implements ICallbackError {
        @Override
        public void executeError(Throwable t) {
            CustomErrorHandlingNew customErrorHandlingNew = new CustomErrorHandlingNew(context);
            String error = customErrorHandlingNew.getErrorMessageTotal(t);
            new CustomDialog(DialogType.YellowRedirect, FollowUpActivity.this, error,
                    FollowUpActivity.this.getString(R.string.dear_user),
                    FollowUpActivity.this.getString(R.string.login),
                    FollowUpActivity.this.getString(R.string.accepted));
        }
    }
}
