package com.app.leon.moshtarak.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.app.leon.moshtarak.Activities.HomeActivity;
import com.app.leon.moshtarak.Models.Enums.DialogType;
import com.app.leon.moshtarak.R;

public class CustomDialog {
    @SuppressLint("StaticFieldLeak")
    private static LovelyStandardDialog lovelyStandardDialog;

    public CustomDialog(DialogType choose, Context context, String message, String title, String top, String buttonText) {
        lovelyStandardDialog = new LovelyStandardDialog(context)
                .setTitle(title)
                .setMessage(message)
                .setTopTitle(top);
        if (choose == DialogType.Green)
            CustomGreenDialog(context, buttonText);
        else if (choose == DialogType.Yellow)
            CustomYellowDialog(context, buttonText);
        else if (choose == DialogType.Red)
            CustomRedDialog(context, buttonText);
        else if (choose == DialogType.GreenRedirect)
            CustomGreenDialogRedirect(context, buttonText);
        else if (choose == DialogType.YellowRedirect)
            CustomYellowDialogRedirect(context, buttonText);
        else if (choose == DialogType.RedRedirect)
            CustomRedDialogRedirect(context, buttonText);
        else if (choose == DialogType.Blue)
            CustomBlueDialog(context, buttonText);
    }

    private static void CustomBlueDialog(final Context context, String ButtonText) {
        lovelyStandardDialog
                .setTopColorRes(R.color.blue5)
                .setTopTitleColor(context.getResources().getColor(R.color.white))
                .setButtonsBackground(R.drawable.border_blue_4)
                .setPositiveButton(ButtonText, v -> lovelyStandardDialog.dismiss())
                .show();
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
                    ((Activity) context).finish();
                });
        lovelyStandardDialog.show();
    }

    private static void CustomYellowDialogRedirect(final Context context, String buttonText) {
        lovelyStandardDialog
                .setTopTitleColor(context.getResources().getColor(R.color.white))
                .setButtonsBackground(R.drawable.border_yellow_2)
                .setTopColorRes(R.color.yellow1)
                .setPositiveButton(buttonText, v -> {
                    Intent intent = new Intent(context, HomeActivity.class);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                })
                .show();
    }

    private static void CustomRedDialogRedirect(final Context context, String buttonText) {
        lovelyStandardDialog
                .setTopColorRes(R.color.red1)
                .setTopTitleColor(context.getResources().getColor(R.color.white))
                .setButtonsBackground(R.drawable.border_red_2)
                .setPositiveButton(buttonText, v -> {
                    Intent intent = new Intent(context, HomeActivity.class);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                })
                .show();
    }
}

