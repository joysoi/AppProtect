package com.nikola.appprotect.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nikola.appprotect.R;

import butterknife.ButterKnife;


final class SettingsPermissionDialog {

    static void showEnableNotificationAccessDialog(
            final Activity activity,
            final Intent settingsActivityIntent,
            String notificationMessage,
            String positiveButtonText,
            String negativeButtonText,
            final String toastMessageLineOne,
            final String toastMessageLineTwo,
            final DialogInterface.OnClickListener onPositiveButtonClickListener,
            DialogInterface.OnClickListener onNegativeButtonClickListener,
            DialogInterface.OnDismissListener onDismissListener) {
        AlertDialog alertDialog =
                new AlertDialog.Builder(activity, R.style.AlertDialogStyle)
                        .setTitle(activity.getResources().getString(R.string.allow_access))
                        .setMessage(notificationMessage)
                        .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onPositiveButtonClickListener.onClick(dialog, which);
                                activity.startActivity(settingsActivityIntent);
                                showSettingsTutorialToast(activity, toastMessageLineOne, toastMessageLineTwo);
                                ((AlertDialog) dialog).setOnDismissListener(null);
                            }
                        })
                        .setNegativeButton(negativeButtonText, onNegativeButtonClickListener)
                        .setOnDismissListener(onDismissListener)
                        .create();
        alertDialog.show();
    }

    private static void showSettingsTutorialToast(Activity activity, String line1Message,
                                                  String line2Message) {

        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_access_tutorial, null);
        TextView findApp = ButterKnife.findById(layout, R.id.find_app);
        findApp.setText(line1Message);
        TextView line2 = ButterKnife.findById(layout, R.id.text_line_2);
        line2.setText(line2Message);

        ViewGroup backgroundContainer = ButterKnife.findById(layout, R.id.container);
        backgroundContainer.getLayoutParams().height = height;
        backgroundContainer.getLayoutParams().width = width;
        backgroundContainer.requestLayout();

        Toast toast = new Toast(activity.getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}

