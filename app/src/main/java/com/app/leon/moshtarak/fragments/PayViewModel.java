package com.app.leon.moshtarak.fragments;

import androidx.lifecycle.ViewModel;

public class PayViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    String billId;
    String payId;

    public PayViewModel(String billId, String payId) {
        this.billId = billId;
        this.payId = payId;
    }

    public String getBillId() {
        return billId;
    }

    public String getPayId() {
        return payId;
    }
}