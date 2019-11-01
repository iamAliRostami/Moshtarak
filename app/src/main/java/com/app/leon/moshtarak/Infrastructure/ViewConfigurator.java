package com.app.leon.moshtarak.Infrastructure;


import android.view.View;

public interface ViewConfigurator<T extends View> {
    void configureView(T v);
}
