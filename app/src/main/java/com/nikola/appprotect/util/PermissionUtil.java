package com.nikola.appprotect.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nikola.appprotect.R;

public class PermissionUtil {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static boolean isUsageStatsPermissionGranted(Context context) {
        AppOpsManager appOps = (AppOpsManager) context
                .getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow("android:get_usage_stats",
                android.os.Process.myUid(), context.getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static Intent openUsageAccessPermission() {
        return new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
    }

    public static void showEnableUsageAccessDialog(Activity activity) {
        SettingsPermissionDialog.showEnableNotificationAccessDialog(activity,
                PermissionUtil.openUsageAccessPermission(),
                activity.getResources().getString(R.string.permission_access_dialog_message,
                        activity.getResources().getString(R.string.usage)),
                activity.getResources().getString(R.string.permit),
                activity.getResources().getString(android.R.string.cancel),
                activity.getResources().getString(R.string.permission_access_tutorial_line1,
                        activity.getResources().getString(R.string.app_name)),
                activity.getResources().getString(R.string.permission_access_tutorial_line2,
                        activity.getResources().getString(R.string.usage)),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                },
                new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                    }
                });
    }


    @SuppressWarnings("ConstantConditions")
    public static void showProtectAppDialogTutorial(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                new ContextThemeWrapper(context, R.style.AlertDialogStyle));
        LayoutInflater factory = LayoutInflater.from(context);
        @SuppressLint("InflateParams") final View view = factory.inflate(R.layout.popup_layout, null);
        builder.setView(view);
        builder.setCancelable(true);
        final AlertDialog alert = builder.create();
        TextView positiveButton = (TextView) view.findViewById(R.id.positiveButton);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.cancel();
            }
        });
        alert.show();
        alert.getWindow()
                .setLayout(context.getResources().getDimensionPixelSize(R.dimen.popup_width),
                        ViewGroup.LayoutParams.WRAP_CONTENT);
    }

}

