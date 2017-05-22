package com.nikola.appprotect.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.RemoteViews;

import com.nikola.appprotect.R;
import com.nikola.appprotect.database.AppsContentProvider;
import com.nikola.appprotect.database.AppsDb;

public class AppWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(final Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int widgetId : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.app_widget_layout);
            MyTask task = new MyTask(context);
            task.execute();
            Intent intent = new Intent(context, AppWidget.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.imageView, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    private class MyTask extends AsyncTask<String, String, Integer> {
        private Context mContext;

        MyTask(Context context) {
            mContext = context;
        }

        @SuppressWarnings("ConstantConditions")
        @Override
        protected Integer doInBackground(String... params) {
            String appFilter = AppsDb.COLUMN_LOCKED_STATUS + "=" + "1";
            ContentValues updateValues = new ContentValues();
            updateValues.put(AppsDb.COLUMN_LOCKED_STATUS, "0");
            return mContext.getContentResolver().update(AppsContentProvider.BASE_CONTENT_URI, updateValues, appFilter, null);
        }
    }
}






