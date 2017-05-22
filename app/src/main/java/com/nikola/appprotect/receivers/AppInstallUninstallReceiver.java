package com.nikola.appprotect.receivers;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.nikola.appprotect.database.AppsContentProvider;
import com.nikola.appprotect.database.AppsDb;

@SuppressWarnings("ConstantConditions")
public class AppInstallUninstallReceiver extends BroadcastReceiver {
    private String packageName;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!isAppBeingUpdated(intent)) {
            switch (intent.getAction()) {
                case Intent.ACTION_PACKAGE_ADDED:
                    appInstalled(context, intent);
                    break;
                case Intent.ACTION_PACKAGE_REMOVED:
                    appRemoved(context, intent);
                    break;
            }
        }
    }

    private void appRemoved(Context context, Intent intent) {
        packageName = getPackageName(intent);

        String mSelectionClause = AppsDb.COLUMN_PACKAGE_NAME + " LIKE ?";
        String[] mSelectionArgs = {"%" + packageName + "%"};

        context.getContentResolver().delete(AppsContentProvider.BASE_CONTENT_URI, mSelectionClause, mSelectionArgs);
    }

    private void appInstalled(Context context, Intent intent) {
        packageName = getPackageName(intent);
        PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        try {
            applicationInfo = packageManager.getApplicationInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        final String appLabel = applicationInfo.loadLabel(packageManager).toString();
        ContentValues appValues = new ContentValues();
        appValues.put(AppsDb.COLUMN_LABEL, appLabel);
        appValues.put(AppsDb.COLUMN_PACKAGE_NAME, packageName);
        appValues.put(AppsDb.COLUMN_LOCKED_STATUS, 0);

        context.getContentResolver().insert(AppsContentProvider.BASE_CONTENT_URI, appValues);
    }

    private boolean isAppBeingUpdated(Intent intent) {
        return intent.getExtras().getBoolean(Intent.EXTRA_REPLACING);
    }

    private String getPackageName(Intent intent) {
        return intent.getData().getSchemeSpecificPart();
    }
}
