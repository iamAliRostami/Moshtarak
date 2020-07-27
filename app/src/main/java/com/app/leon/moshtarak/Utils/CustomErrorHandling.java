package com.app.leon.moshtarak.Utils;

import android.annotation.SuppressLint;
import android.content.Context;

import com.app.leon.moshtarak.Models.Enums.ErrorHandlerType;
import com.app.leon.moshtarak.R;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

public class CustomErrorHandling extends Exception {
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private int ErrorCode;

    public CustomErrorHandling(Context context) {
        CustomErrorHandling.context = context;
    }

    public static APIError parseError(Response<?> response) {
        try {
            Converter<ResponseBody, APIError> converter =
                    NetworkHelper.getInstance().responseBodyConverter(APIError.class, new Annotation[0]);
            APIError error;
            error = converter.convert(Objects.requireNonNull(response.errorBody()));
            return error;
        } catch (IOException e) {
            return new APIError();
        } catch (JsonSyntaxException e) {
            return new APIError();
        } catch (Exception e) {
            return new APIError();
        }
    }

    public String getErrorMessage(int httpResponseCode, ErrorHandlerType errorHandlerType) {
        if (errorHandlerType == ErrorHandlerType.login) {
            return getErrorMessageForLogin(httpResponseCode);
        } else if (errorHandlerType == ErrorHandlerType.ordinary) {
            return getErrorMessageDefault(httpResponseCode);
        }
        return "";
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

    private String getErrorMessageDefault(int httpResponseCode) {
        String errorMessage = "";
        if (httpResponseCode == 500) {
            errorMessage = context.getString(R.string.error_internal);
        } else if (httpResponseCode == 400) {
            errorMessage = context.getString(R.string.error_input);
        } else if (httpResponseCode == 404) {
            errorMessage = context.getString(R.string.error_change_server);
        } else if (httpResponseCode == 401) {
            errorMessage = context.getString(R.string.error_not_auth);
        } else if (httpResponseCode == 405) {
            errorMessage = context.getString(R.string.error_not_update);
        } else if (httpResponseCode == 406) {
            errorMessage = context.getString(R.string.error_not_update2);
        }
        return errorMessage;
    }

    private String getErrorMessageForLogin(int httpResponseCode) {
        String errorMessage = "";
        if (httpResponseCode == 500) {
            errorMessage = context.getString(R.string.error_internal);
        } else if (httpResponseCode == 400) {
            errorMessage = context.getString(R.string.error_input);
        } else if (httpResponseCode == 404) {
            errorMessage = context.getString(R.string.error_change_server);
        } else if (httpResponseCode == 401) {
            errorMessage = context.getString(R.string.error_user_password);
        } else if (httpResponseCode == 405) {
            errorMessage = context.getString(R.string.error_not_update);
        } else if (httpResponseCode == 406) {
            errorMessage = context.getString(R.string.error_not_update2);
        }
        return errorMessage;
    }

    public static class APIError {

        private int Status;
        private String Message;

        APIError() {
        }

        public int status() {
            return Status;
        }

        public String message() {
            return Message;
        }
    }
}
