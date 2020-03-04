package com.app.leon.moshtarak.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.app.leon.moshtarak.BaseItems.BaseActivity;
import com.app.leon.moshtarak.Infrastructure.IAbfaService;
import com.app.leon.moshtarak.Infrastructure.ICallback;
import com.app.leon.moshtarak.Models.DbTables.RegisterAsDto;
import com.app.leon.moshtarak.Models.DbTables.Service;
import com.app.leon.moshtarak.Models.Enums.DialogType;
import com.app.leon.moshtarak.Models.Enums.ProgressType;
import com.app.leon.moshtarak.Models.Enums.SharedReferenceKeys;
import com.app.leon.moshtarak.Models.InterCommunation.SimpleMessage;
import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.Utils.CustomDialog;
import com.app.leon.moshtarak.Utils.HttpClientWrapper;
import com.app.leon.moshtarak.Utils.NetworkHelper;
import com.app.leon.moshtarak.Utils.SharedPreference;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Retrofit;

public class AfterSaleServicesActivity extends BaseActivity {

    @BindView(R.id.listViewService)
    ListView listViewService;
    @BindView(R.id.buttonSubmit)
    Button buttonSubmit;
    @BindView(R.id.editTextMobile)
    EditText editTextMobile;
    Context context;
    SharedPreference sharedPreference;
    String billId;
    private ArrayList<String> servicesTitle = new ArrayList<>();
    private ArrayList<String> servicesId = new ArrayList<>();
    private ArrayList<String> requestServices = new ArrayList<>();

    @Override
    protected void initialize() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View childLayout = Objects.requireNonNull(inflater).inflate(R.layout.after_sale_service_content, findViewById(R.id.after_sale_services_activity));
        @SuppressLint("CutPasteId") ConstraintLayout parentLayout = findViewById(R.id.base_Content);
        parentLayout.addView(childLayout);
        ButterKnife.bind(this);
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
            editTextMobile.setText(sharedPreference.getArrayList(SharedReferenceKeys.MOBILE_NUMBER.getValue()).
                    get(sharedPreference.getIndex()).replaceFirst("09", ""));
            Toast.makeText(context, "اشتراک فعال:\n".concat(billId), Toast.LENGTH_LONG).show();
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
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                final CheckedTextView textView = view.findViewById(android.R.id.text1);
                Typeface typeface = Typeface.createFromAsset(getAssets(), "font/BYekan_3.ttf");
                textView.setTypeface(typeface);
                textView.setChecked(true);
                textView.setTextColor(getResources().getColor(R.color.black));
                return view;
            }
        };
        listViewService.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listViewService.setAdapter(arrayAdapter);
        setListViewServiceClickListener();
    }

    private void setListViewServiceClickListener() {
        listViewService.setOnItemClickListener((adapterView, view, i, l) -> {
            if (listViewService.isItemChecked(i)) {
                requestServices.add(servicesId.get(i));
            } else {
                requestServices.add(servicesId.remove(i));
            }
        });
    }

    void setOnButtonSubmitClickListener() {
        buttonSubmit.setOnClickListener(view -> {
            View viewFocus;
            if (editTextMobile.getText().length() < 9) {
                viewFocus = editTextMobile;
                viewFocus.requestFocus();
            } else if (servicesId.size() < 1) {
                new CustomDialog(DialogType.Green, context, context.getString(R.string.select), context.getString(R.string.dear_user),
                        context.getString(R.string.support), context.getString(R.string.accepted));
            } else {
                sendAfterSaleServiceRequest("09".concat(editTextMobile.getText().toString()));
            }
        });
    }

    void sendAfterSaleServiceRequest(String mobileNumber) {
        Retrofit retrofit = NetworkHelper.getInstance();
        final IAbfaService sendSupportRequest = retrofit.create(IAbfaService.class);
        RegisterAsDto registerAsDto = new RegisterAsDto(billId, requestServices, mobileNumber);
        Call<SimpleMessage> call = sendSupportRequest.registerAS(registerAsDto);
        SendRequest sendRequest = new SendRequest();
        HttpClientWrapper.callHttpAsync(call, sendRequest, context, ProgressType.SHOW.getValue());
    }

    void getServices() {
        Retrofit retrofit = NetworkHelper.getInstance();
        final IAbfaService getServices = retrofit.create(IAbfaService.class);
        Call<ArrayList<Service>> call = getServices.getDictionary();
        GetServices getServices1 = new GetServices();
        HttpClientWrapper.callHttpAsync(call, getServices1, context, ProgressType.SHOW.getValue());
    }

    class GetServices implements ICallback<ArrayList<Service>> {
        @Override
        public void execute(ArrayList<Service> services) {
            setupListViewReport(services);
        }
    }

    class SendRequest implements ICallback<SimpleMessage> {
        @Override
        public void execute(SimpleMessage simpleMessage) {
            new CustomDialog(DialogType.Green, context, simpleMessage.getMessage(), context.getString(R.string.dear_user),
                    context.getString(R.string.support), context.getString(R.string.accepted));
        }
    }
}
