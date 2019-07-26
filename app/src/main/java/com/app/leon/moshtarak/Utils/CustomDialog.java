package com.app.leon.moshtarak.Utils;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.app.leon.moshtarak.Activities.HomeActivity;
import com.app.leon.moshtarak.Models.Enums.DialogType;
import com.app.leon.moshtarak.R;

public class CustomDialog {
    @SuppressLint("StaticFieldLeak")
    private static LovelyStandardDialog lovelyStandardDialog;
    private Context context;
    private String Top, Title, Message, ButtonText;


    public CustomDialog(DialogType choose, Context context, String message, String title, String top, String buttonText) {
        this.context = context;
        Message = message;
        Title = title;
        Top = top;
        ButtonText = buttonText;
        lovelyStandardDialog = new LovelyStandardDialog(context)
                .setTitle(Title)
                .setMessage(Message)
                .setTopTitle(Top);
        if (choose == DialogType.Green)
            CustomGreenDialog(this.context, ButtonText);
        else if (choose == DialogType.Yellow)
            CustomYellowDialog(this.context, ButtonText);
        else if (choose == DialogType.Red)
            CustomRedDialog(this.context, ButtonText);
        else if (choose == DialogType.GreenRedirect)
            CustomGreenDialogRedirect(this.context, ButtonText);
        else if (choose == DialogType.YellowRedirect)
            CustomYellowDialogRedirect(this.context, ButtonText);
        else if (choose == DialogType.RedRedirect)
            CustomRedDialogRedirect(this.context, ButtonText);
    }


    private static void CustomGreenDialog(final Context context, String ButtonText) {
        lovelyStandardDialog
                .setTopColorRes(R.color.green2)
                .setTopTitleColor(context.getResources().getColor(R.color.white))
                .setButtonsBackground(R.drawable.border_green_2)
                .setPositiveButton(ButtonText, v -> lovelyStandardDialog.dismiss())
                .show();
    }

    private static void CustomYellowDialog(final Context context, String buttonText) {
        lovelyStandardDialog
                .setTopTitleColor(context.getResources().getColor(R.color.white))
                .setTopColorRes(R.color.yellow1)
                .setButtonsBackground(R.drawable.border_yellow_2)
                .setPositiveButton(buttonText, v -> {
                })
                .show();
    }

    private static void CustomRedDialog(final Context context, String buttonText) {
        lovelyStandardDialog
                .setTopColorRes(R.color.red1)
                .setTopTitleColor(context.getResources().getColor(R.color.white))
                .setButtonsBackground(R.drawable.border_red_2)
                .setPositiveButton(buttonText, v -> lovelyStandardDialog.dismiss())
                .show();
    }

    private static void CustomGreenDialogRedirect(final Context context, String ButtonText) {
        lovelyStandardDialog
                .setTopColorRes(R.color.green2)
                .setTopTitleColor(context.getResources().getColor(R.color.white))
                .setButtonsBackground(R.drawable.border_green_2)
                .setPositiveButton(ButtonText, v -> {
                    Intent intent = new Intent(context, HomeActivity.class);
                    context.startActivity(intent);
                });
        lovelyStandardDialog.show();
    }

    private static void CustomYellowDialogRedirect(final Context context, String buttonText) {
        lovelyStandardDialog
                .setTopTitleColor(context.getResources().getColor(R.color.white))
                .setButtonsBackground(R.drawable.border_yellow_2)
                .setTopColorRes(R.color.yellow1)
                .setPositiveButton(buttonText, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                })
                .show();
    }

    private static void CustomRedDialogRedirect(final Context context, String buttonText) {
        lovelyStandardDialog
                .setTopColorRes(R.color.red1)
                .setTopTitleColor(context.getResources().getColor(R.color.white))
                .setButtonsBackground(R.drawable.border_red_2)
                .setPositiveButton(buttonText, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lovelyStandardDialog.dismiss();
                    }
                })
                .show();
    }
}

