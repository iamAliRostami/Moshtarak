package com.app.leon.moshtarak.Utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.app.leon.moshtarak.Infrastructure.ICallback;
import com.app.leon.moshtarak.Models.Enums.DialogType;
import com.app.leon.moshtarak.Models.Enums.ErrorHandlerType;
import com.app.leon.moshtarak.Models.Enums.ProgressType;
import com.app.leon.moshtarak.R;

import org.json.JSONObject;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HttpClientWrapperNew {
    private HttpClientWrapperNew() {
    }

    public static <T> void callHttpAsync(Call<T> call, final ICallback<T> callback, final Context context, int dialogType) {
        callHttpAsync(call, callback, context, dialogType, ErrorHandlerType.ordinary);
    }

    public static <T> void callHttpAsync(Call<T> call, final ICallback<T> callback, final Context context, int dialogType, final ErrorHandlerType errorHandlerType) {
        CustomProgressBar progressBar = new CustomProgressBar();
        if (dialogType == ProgressType.SHOW.getValue() || dialogType == ProgressType.SHOW_CANCELABLE.getValue()) {
            progressBar.show(context, context.getString(R.string.waiting), true);
        }
        if (isOnline(context)) {
            final String[] error = new String[1];
            call.enqueue(new Callback<T>() {
                @Override
                public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
                    try {
                        if (response.isSuccessful()) {
                            callback.execute(response.body());
                        } else {
                            try {
                                if (checkRespond(response)) {
                                    JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.errorBody()).string());
                                    error[0] = jsonObject.getString(context.getString(R.string.message));
                                } else {
                                    error[0] = new CustomErrorHandling(context).getErrorMessage(response.code(), errorHandlerType);
                                }
                            } catch (Exception e) {
                                error[0] = new CustomErrorHandling(context).getErrorMessage(response.code(), errorHandlerType);
                            }
                            new CustomDialog(DialogType.Yellow, context, error[0], context.getString(R.string.dear_user),
                                    context.getString(R.string.error), context.getString(R.string.accepted));
                        }
                    } catch (Exception e) {
                        try {
                            if (checkRespond(response)) {
                                JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.errorBody()).string());
                                error[0] = jsonObject.getString(context.getString(R.string.message));
                            } else {
                                error[0] = new CustomErrorHandling(context).getErrorMessage(response.code(), errorHandlerType);
                            }
                        } catch (Exception e1) {
                            error[0] = new CustomErrorHandling(context).getErrorMessage(response.code(), errorHandlerType);
                        }
//                        new CustomDialog(DialogType.Yellow, context, error[0], context.getString(R.string.dear_user),
//                                context.getString(R.string.error), context.getString(R.string.accepted));
                        ((Activity) context).runOnUiThread(() -> new CustomDialog(DialogType.Yellow, context, error[0], context.getString(R.string.dear_user),
                                context.getString(R.string.error), context.getString(R.string.accepted)));
                    }
                    progressBar.getDialog().dismiss();
                }

                @Override
                public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
                    Activity activity = (Activity) context;
                    if (!activity.isFinishing()) {
                        error[0] = new CustomErrorHandling(context).getErrorMessageTotal(t);
                        new CustomDialog(DialogType.Red, context, error[0], context.getString(R.string.dear_user),
                                context.getString(R.string.error), context.getString(R.string.accepted));
                    }
                    progressBar.getDialog().dismiss();
                }
            });
        } else {
            progressBar.getDialog().dismiss();
            Toast.makeText(context, R.string.turn_internet_on, Toast.LENGTH_SHORT).show();
        }
    }

    public static <T> void callHttpAsync(Call<T> call, final ICallback<T> callback, final Context context) {
        CustomProgressBar progressBar = new CustomProgressBar();
        progressBar.show(context, context.getString(R.string.waiting), true);
        if (isOnline(context)) {
            final String[] error = new String[1];
            call.enqueue(new Callback<T>() {
                @Override
                public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
                    try {
                        if (response.isSuccessful()) {
                            callback.execute(response.body());
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.errorBody()).string());
                                error[0] = jsonObject.getString(context.getString(R.string.message));
                            } catch (Exception e) {
                                error[0] = new CustomErrorHandling(context).getErrorMessage(response.code(), ErrorHandlerType.ordinary);
                            }
                            new CustomDialog(DialogType.Yellow, context, error[0], context.getString(R.string.dear_user),
                                    context.getString(R.string.error), context.getString(R.string.accepted));
                        }
                    } catch (Exception e) {
                        try {
                            JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.errorBody()).string());
                            error[0] = jsonObject.getString(context.getString(R.string.message));

                        } catch (Exception e1) {
                            error[0] = new CustomErrorHandling(context).getErrorMessage(response.code(), ErrorHandlerType.ordinary);
                        }
                        new CustomDialog(DialogType.Yellow, context, error[0], context.getString(R.string.dear_user),
                                context.getString(R.string.error), context.getString(R.string.accepted));
                    }
                    progressBar.getDialog().dismiss();
                }

                @Override
                public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
                    Activity activity = (Activity) context;
                    if (!activity.isFinishing()) {
                        error[0] = new CustomErrorHandling(context).getErrorMessageTotal(t);
                        new CustomDialog(DialogType.Red, context, error[0], context.getString(R.string.dear_user),
                                context.getString(R.string.error), context.getString(R.string.accepted));
                    }
                    progressBar.getDialog().dismiss();
                }
            });
        } else {
            progressBar.getDialog().dismiss();
            Toast.makeText(context, R.string.turn_internet_on, Toast.LENGTH_SHORT).show();
        }
    }


    private static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return Objects.requireNonNull(cm).getActiveNetworkInfo() != null &&
                Objects.requireNonNull(cm.getActiveNetworkInfo()).isConnectedOrConnecting();
    }

    private static boolean checkRespond(Response<?> response) {
        return response.code() != 404 && response.code() != 401 &&
                response.code() != 405 && response.code() != 406;
//        response.code() != 400 && response.code() != 500;
    }
}
