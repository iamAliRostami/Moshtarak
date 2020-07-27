package com.app.leon.moshtarak.Utils;

import android.content.Context;

import com.app.leon.moshtarak.R;

import java.io.IOException;

import retrofit2.Response;

public class CustomErrorHandlingNew {
    Context context;

    public CustomErrorHandlingNew(Context context) {
        this.context = context;
    }

    public String getErrorMessageTotal(Throwable throwable) {
        String errorMessage;
        if (throwable instanceof IOException) {
            errorMessage = context.getString(R.string.error_connection);
            return errorMessage;
        } else {
            errorMessage = context.getString(R.string.error_other);
        }
        return errorMessage;
    }

    public <T> String getErrorMessageDefault(Response<T> response) {
        String errorMessage = "";
        int code = response.code();
        if (code >= 500 && code < 600) {
            errorMessage = context.getString(R.string.error_internal);
        } else if (code == 404) {
            errorMessage = context.getString(R.string.error_change_server);
        } else if (code >= 400 && code < 500) {
            errorMessage = context.getString(R.string.error_not_update);
        } else {
            errorMessage = context.getString(R.string.error_other);
        }
        return errorMessage;
    }
}
