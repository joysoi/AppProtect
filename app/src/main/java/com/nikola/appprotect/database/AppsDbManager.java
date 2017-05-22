package com.nikola.appprotect.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import com.nikola.appprotect.util.ApplicationUtil;
import java.util.List;
import java.util.Vector;

public class AppsDbManager {
    public static void writeToDb(Context context) {

        boolean isDbFull = false;
        String[] projection = {
                AppsDb.COLUMN_ID,
                AppsDb.COLUMN_LABEL,
                AppsDb.COLUMN_PACKAGE_NAME,
                AppsDb.COLUMN_LOCKED_STATUS};

        try (Cursor cursor = context.getContentResolver().query(AppsContentProvider.BASE_CONTENT_URI, projection, null, null, null)) {
            if (cursor != null) {
                if (cursor.getCount() != 0) {
                    isDbFull = true;
                }
                cursor.close();
            }
        }
        if (!isDbFull) {
            Vector<ContentValues> cVVector = new Vector<>();
            final String PACKAGE_NAME = context.getPackageName();
            List<ApplicationInfo> list = ApplicationUtil.getApplications(context);
            for (ApplicationInfo app : list) {
                if (context.getPackageManager().getLaunchIntentForPackage(app.packageName) != null
                        && !ApplicationUtil.isAppPreLoaded(app.packageName,
                        context.getPackageManager())
                        && app.packageName.compareTo(PACKAGE_NAME) != 0) {

                    ContentValues appValues = new ContentValues();
                    appValues.put(AppsDb.COLUMN_LABEL, ApplicationUtil.getApplicationLabel(app, context.getPackageManager()));
                    appValues.put(AppsDb.COLUMN_PACKAGE_NAME, app.packageName);
                    appValues.put(AppsDb.COLUMN_LOCKED_STATUS, 0);
                    cVVector.add(appValues);
                }
            }
            if (cVVector.size() != 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                context.getContentResolver().bulkInsert(AppsContentProvider.BASE_CONTENT_URI, cvArray);
            }
        }
    }
}
