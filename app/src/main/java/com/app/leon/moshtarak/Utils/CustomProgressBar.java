package com.app.leon.moshtarak.Utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.leon.moshtarak.Activities.HomeActivity;
import com.app.leon.moshtarak.R;

import java.util.Objects;

public final class CustomProgressBar {

    private Dialog dialog;

    public Dialog show(Context context) {
        return show(context, null);
    }

    public Dialog show(Context context, CharSequence title) {
        return show(context, title, false);
    }

    public Dialog show(Context context, CharSequence title, boolean cancelable) {
        return show(context, title, cancelable, dialog -> {
            Toast.makeText(context, "عملیات متوقف شد.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(context, HomeActivity.class);
            context.startActivity(intent);
        });
    }

    @SuppressLint("InflateParams")
    public Dialog show(Context context, CharSequence title, boolean cancelable,
                       DialogInterface.OnCancelListener cancelListener) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = Objects.requireNonNull(inflater).inflate(R.layout.progress_bar, null);
        RelativeLayout relativeLayout = view.findViewById(R.id.relativeLayout);
        FontManager fontManager = new FontManager(context);
        fontManager.setFont(relativeLayout);
        relativeLayout.setOnClickListener(v -> {
            dialog.dismiss();
            dialog.cancel();
        });
        if (title != null) {
            final TextView tv = view.findViewById(R.id.textView_title);
            tv.setText(title);
        }

        dialog = new Dialog(context, R.style.NewDialog);
        dialog.setContentView(view);
        Objects.requireNonNull(dialog.getWindow()).setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(cancelable);

        dialog.setOnCancelListener(cancelListener);
        dialog.show();

        return dialog;
    }

    public Dialog getDialog() {
        return dialog;
    }

}