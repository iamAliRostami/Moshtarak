package com.app.leon.moshtarak.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Debug;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.app.leon.moshtarak.BaseItems.BaseActivity;
import com.app.leon.moshtarak.Infrastructure.IAbfaService;
import com.app.leon.moshtarak.Infrastructure.ICallback;
import com.app.leon.moshtarak.Models.DbTables.RegisterAsDto;
import com.app.leon.moshtarak.Models.DbTables.Service;
import com.app.leon.moshtarak.Models.Enums.DialogType;
import com.app.leon.moshtarak.Models.Enums.SharedReferenceKeys;
import com.app.leon.moshtarak.Models.InterCommunation.SimpleMessage;
import com.app.leon.moshtarak.MyApplication;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.Utils.CustomDialog;
import com.app.leon.moshtarak.Utils.CustomErrorHandling;
import com.app.leon.moshtarak.Utils.CustomProgressBar;
import com.app.leon.moshtarak.Utils.NetworkHelper;
import com.app.leon.moshtarak.Utils.SharedPreference;
import com.app.leon.moshtarak.databinding.AfterSaleServiceContentBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AfterSaleServicesActivity extends BaseActivity {
    AfterSaleServiceContentBinding binding;
    Context context;
    SharedPreference sharedPreference;
    String billId;
    private ArrayList<String> servicesTitle = new ArrayList<>();
    private ArrayList<String> servicesId = new ArrayList<>();
    private ArrayList<String> requestServices = new ArrayList<>();

    @SuppressLint("CutPasteId")
    @Override
    protected void initialize() {
        binding = AfterSaleServiceContentBinding.inflate(getLayoutInflater());
        View childLayout = binding.getRoot();
        ConstraintLayout parentLayout = findViewById(R.id.base_Content);
        parentLayout.addView(childLayout);
        context = this;
        accessData();
    }

    private void accessData() {
        sharedPreference = new SharedPreference(context);
        if (!sharedPreference.checkIsNotEmpty()) {
            Intent intent = new Intent(getApplicationContext(), SignAccountActivity.class);
            startActivity(intent);
            finish();
        } else {
            billId = sharedPreference.getArrayList(SharedReferenceKeys.BILL_ID.getValue()).
                    get(sharedPreference.getIndex());
            binding.editTextMobile.setText(sharedPreference.getArrayList(
                    SharedReferenceKeys.MOBILE_NUMBER.getValue()).
                    get(sharedPreference.getIndex()).replaceFirst(getString(R.string._09), ""));
            Toast.makeText(MyApplication.getContext(), getString(R.string.active_user).concat(billId),
                    Toast.LENGTH_LONG).show();
            getServices();
            setOnButtonSubmitClickListener();
        }
    }

    void setupListViewReport(ArrayList<Service> services) {
        if (services.size() > 0) {
            for (Service service : services) {
                servicesTitle.add(service.getTitle());
                servicesId.add(service.getId());
            }
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                R.layout.item_spinner, servicesTitle) {
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                final CheckedTextView textView = view.findViewById(android.R.id.text1);
                textView.setChecked(true);
                textView.setTextColor(getResources().getColor(R.color.black));
                return view;
            }
        };
        binding.listViewService.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        binding.listViewService.setAdapter(arrayAdapter);
        setListViewServiceClickListener();
    }

    private void setListViewServiceClickListener() {
        binding.listViewService.setOnItemClickListener((adapterView, view, i, l) -> {
            if (binding.listViewService.isItemChecked(i)) {
                requestServices.add(servicesId.get(i));
            } else {
                requestServices.remove(servicesId.get(i));
            }
        });
    }

    void setOnButtonSubmitClickListener() {
        binding.buttonSubmit.setOnClickListener(view -> {
            View viewFocus;
            if (binding.editTextMobile.getText().length() < 9) {
                viewFocus = binding.editTextMobile;
                viewFocus.requestFocus();
            } else if (requestServices.size() < 1) {
                new CustomDialog(DialogType.Yellow, context, context.getString(R.string.select),
                        context.getString(R.string.dear_user), context.getString(R.string.support), context.getString(R.string.accepted));
            } else {
                sendAfterSaleServiceRequest(getString(R.string._09).concat(
                        binding.editTextMobile.getText().toString()));
            }
        });
    }

    void sendAfterSaleServiceRequest(String mobileNumber) {
        Retrofit retrofit = NetworkHelper.getInstance();
        final IAbfaService sendSupportRequest = retrofit.create(IAbfaService.class);
        RegisterAsDto registerAsDto = new RegisterAsDto(billId, requestServices, mobileNumber);
        Call<SimpleMessage> call = sendSupportRequest.registerAS(registerAsDto);
        SendRequest sendRequest = new SendRequest();
//        HttpClientWrapperNew.callHttpAsync(call, sendRequest, context, ProgressType.SHOW.getValue());
        CustomProgressBar progressBar = new CustomProgressBar();
        progressBar.show(context, context.getString(R.string.waiting), true);

        if (isOnline(context)) {
            call.enqueue(new Callback<SimpleMessage>() {
                @Override
                public void onResponse(@NonNull Call<SimpleMessage> call, @NonNull Response<SimpleMessage> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            sendRequest.execute(response.body());
                        }
                    } else {
                        errorHandlingWithResponse(response);
                    }
                    progressBar.getDialog().dismiss();
                }

                @Override
                public void onFailure(@NonNull Call<SimpleMessage> call, @NonNull Throwable t) {
                    String error;
                    if (t instanceof IOException) {
                        error = context.getString(R.string.error_connection);
                    } else error = new CustomErrorHandling(context).getErrorMessageTotal(t);
                    new CustomDialog(DialogType.Red, context, error, context.getString(R.string.dear_user),
                            context.getString(R.string.error), context.getString(R.string.accepted));
                    progressBar.getDialog().dismiss();
                }
            });
        } else {
            progressBar.getDialog().dismiss();
            Toast.makeText(context, R.string.turn_internet_on, Toast.LENGTH_SHORT).show();
        }
    }

    void getServices() {
        Retrofit retrofit = NetworkHelper.getInstance();
        final IAbfaService abfaService = retrofit.create(IAbfaService.class);
        Call<ArrayList<Service>> call = abfaService.getDictionary();
        GetServices getServices1 = new GetServices();
//        HttpClientWrapperNew.callHttpAsync(call, getServices1, context, ProgressType.SHOW.getValue());
        CustomProgressBar progressBar = new CustomProgressBar();
        progressBar.show(context, context.getString(R.string.waiting), true);
        if (isOnline(context)) {
            call.enqueue(new Callback<ArrayList<Service>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<Service>> call, @NonNull Response<ArrayList<Service>> response) {
                    if (response.isSuccessful()) {
                        getServices1.execute(response.body());
                    } else {
                        errorHandlingWithResponse(response);
                    }
                    progressBar.getDialog().dismiss();
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<Service>> call, @NonNull Throwable t) {
                    String error;
                    if (t instanceof IOException) {
                        error = context.getString(R.string.error_connection);
                    } else error = new CustomErrorHandling(context).getErrorMessageTotal(t);
                    new CustomDialog(DialogType.Red, context, error, context.getString(R.string.dear_user),
                            context.getString(R.string.error), context.getString(R.string.accepted));
                    progressBar.getDialog().dismiss();
                }
            });
        } else {
            progressBar.getDialog().dismiss();
            Toast.makeText(context, R.string.turn_internet_on, Toast.LENGTH_SHORT).show();
        }
    }

    <T> void errorHandlingWithResponse(Response<T> response) {
        int code = response.code();
        if (code >= 500 && code < 600) {
            new CustomDialog(DialogType.Yellow, AfterSaleServicesActivity.this,
                    context.getString(R.string.error_internal),
                    context.getString(R.string.dear_user),
                    context.getString(R.string.login),
                    context.getString(R.string.accepted));
        } else if (code == 404) {
            new CustomDialog(DialogType.Yellow, AfterSaleServicesActivity.this,
                    context.getString(R.string.error_change_server),
                    context.getString(R.string.dear_user),
                    context.getString(R.string.login),
                    context.getString(R.string.accepted));
        } else if (code >= 400 && code < 500) {
            new CustomDialog(DialogType.Yellow, AfterSaleServicesActivity.this,
                    context.getString(R.string.error_not_update),
                    context.getString(R.string.dear_user),
                    context.getString(R.string.login),
                    context.getString(R.string.accepted));
        } else {
            new CustomDialog(DialogType.Yellow, AfterSaleServicesActivity.this,
                    context.getString(R.string.error_other),
                    context.getString(R.string.dear_user),
                    context.getString(R.string.login),
                    context.getString(R.string.accepted));
        }
    }

    class GetServices implements ICallback<ArrayList<Service>> {
        @Override
        public void execute(ArrayList<Service> services) {
            setupListViewReport(services);
        }
    }

    private boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return Objects.requireNonNull(cm).getActiveNetworkInfo() != null &&
                Objects.requireNonNull(cm.getActiveNetworkInfo()).isConnectedOrConnecting();
    }

    class SendRequest implements ICallback<SimpleMessage> {
        @Override
        public void execute(SimpleMessage simpleMessage) {
            new CustomDialog(DialogType.Green, context, simpleMessage.getMessage(),
                    context.getString(R.string.dear_user),
                    context.getString(R.string.support), context.getString(R.string.accepted));
        }
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
        servicesId = null;
        servicesTitle = null;
        requestServices = null;
        Runtime.getRuntime().totalMemory();
        Runtime.getRuntime().freeMemory();
        Runtime.getRuntime().maxMemory();
        Debug.getNativeHeapAllocatedSize();
    }
}
