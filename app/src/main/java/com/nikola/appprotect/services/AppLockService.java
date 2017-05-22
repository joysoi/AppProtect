package com.nikola.appprotect.services;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;

import com.jaredrummler.android.processes.AndroidProcesses;
import com.jaredrummler.android.processes.models.AndroidAppProcess;
import com.nikola.appprotect.R;
import com.nikola.appprotect.database.AppsContentProvider;
import com.nikola.appprotect.database.AppsDb;
import com.nikola.appprotect.services.ui.FakeLockRelativeLayout;
import com.nikola.appprotect.services.ui.WindowManagerUtil;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import timber.log.Timber;


@SuppressWarnings("ConstantConditions")
public class AppLockService extends Service {
    private static final String USAGE_STATS = "usagestats";
    private Handler handler = new Handler();
    private Context context = this;
    private boolean isServiceStarted = false;
    private WindowManager windowManager;
    private ImageView fakeLockBackground;
    private FakeLockRelativeLayout fakeLockRelativeLayout;
    private static final int REMOVING_PASSWORD_SCREEN_DELAY = 400;
    private static final int FOREGROUND_PACKAGES_INTERVAL = 1100;
    private static final int HANDLER_REPEATING_TIME = 50;
    private UsageStatsManager usageStatsManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isServiceStarted) {
            handler.postDelayed(runnable, HANDLER_REPEATING_TIME);
            isServiceStarted = true;
        }
        return START_STICKY;
    }

    public Runnable runnable = new Runnable() {
        @Override
        public void run() {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                usageStatsManager = (UsageStatsManager) getSystemService(USAGE_STATS);
                getTopPackageNameOnLollipopAndUp();
            } else {
                ActivityManager activityManager = (ActivityManager) context
                        .getSystemService(Activity.ACTIVITY_SERVICE);
                String packageName = activityManager.getRunningTasks(1).get(0).topActivity
                        .getPackageName();
                lockAppController(packageName);
            }
            handler.postDelayed(this, HANDLER_REPEATING_TIME);
        }
    };

    @TargetApi(Build.VERSION_CODES.LOLLIPOP) private void getTopPackageNameOnLollipopAndUp() {
        final String packageName;
        long time = System.currentTimeMillis();
        List<UsageStats>
                stats =
                usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST,
                        time - FOREGROUND_PACKAGES_INTERVAL, time);

        if (stats != null) {
            SortedMap<Long, UsageStats> usageStatsSortedMap = new TreeMap<>();
            for (UsageStats usageStats : stats) {
                usageStatsSortedMap.put(usageStats.getLastTimeUsed(), usageStats);
            }

            if (!usageStatsSortedMap.isEmpty()) {
                packageName = usageStatsSortedMap.get(usageStatsSortedMap.lastKey()).getPackageName();
                boolean isOnForeGround = true;
                for (AndroidAppProcess process : AndroidProcesses.getRunningAppProcesses()) {
                    if (process.getPackageName().equals(packageName)) {
                        isOnForeGround = process.foreground;
                    }
                }
                if (isOnForeGround) {
                    lockAppController(packageName);
                }
            }
        }
    }

    private boolean isThePackageLocked(String packageName) {
        String[] mProjection =
                {AppsDb.COLUMN_ID};
        String mSelectionClause = AppsDb.COLUMN_PACKAGE_NAME + " = ?" + " AND " + AppsDb.COLUMN_LOCKED_STATUS + " = ? ";
        String[] mSelectionArgs = {packageName, "1"};
        try {
            return getContentResolver().query(AppsContentProvider.BASE_CONTENT_URI, mProjection, mSelectionClause, mSelectionArgs, null, null).getCount() != 0;
        } catch (NullPointerException e) {
            return false;
        }
    }

    private void lockAppController(String packageName) {
        final ActiveApplicationLockSyncObject.ActiveApplication activeApplication =
                ActiveApplicationLockSyncObject.getInstance().getCheckAndSameName();
        final boolean isPackageLocked = isThePackageLocked(packageName);
        final boolean isPasswordScreenNeeded = activeApplication.check;
        final String samePackageNameCondition = activeApplication.samePackageName;
        if (isPasswordScreenNeeded & isPackageLocked) {
            showFakeLockScreen();
            ActiveApplicationLockSyncObject.getInstance().setCheck(false);
            ActiveApplicationLockSyncObject.getInstance().setPackageName(packageName);
        }
        if (!isPasswordScreenNeeded & !compareTo(packageName, samePackageNameCondition)) {
            ActiveApplicationLockSyncObject.getInstance().setCheck(true);
            ActiveApplicationLockSyncObject.getInstance().setPackageName(samePackageNameCondition);
            removePasswordScreen();
        }
    }

    private boolean compareTo(String packageName, String appName) {
        if (appName == null) {
            return true;
        }
        int result = packageName.compareTo(appName);
        return result == 0;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isServiceStarted = false;
    }


    private void showFakeLockScreen() {
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        fakeLockBackground = new ImageView(this);
        fakeLockRelativeLayout = new FakeLockRelativeLayout(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            fakeLockBackground.setBackgroundColor(ContextCompat.getColor(context,
                    R.color.black));
        } else {
            fakeLockBackground.setBackgroundColor(getResources().getColor(R.color.black));
        }
        fakeLockRelativeLayout.OnClickListener(new FakeLockRelativeLayout.OnClickListener() {
            @Override
            public void onIconClick() {
                removePasswordScreen();
            }

            @Override
            public void onButtonClick() {
                removePasswordScreen();
            }
        });
        windowManager.addView(fakeLockBackground,
                WindowManagerUtil.setupLayoutParams(Gravity.CENTER, WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.MATCH_PARENT));
        windowManager.addView(fakeLockRelativeLayout,
                WindowManagerUtil.setupLayoutParams(Gravity.CENTER_HORIZONTAL,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT));
    }

    private void removePasswordScreen() {
        final Handler handler = new Handler();
        final Runnable callback = new Runnable() {
            @Override
            public void run() {
                if (fakeLockRelativeLayout != null & fakeLockBackground != null) {
                    windowManager.removeView(fakeLockBackground);
                    windowManager.removeView(fakeLockRelativeLayout);
                    fakeLockRelativeLayout = null;
                    fakeLockBackground = null;
                }
            }
        };
        handler.postDelayed(callback, REMOVING_PASSWORD_SCREEN_DELAY);
    }
}
