package com.nikola.appprotect.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.util.List;

public class ApplicationUtil {
    private static final String SYSTEM_PACKAGE_NAME = "android";
    private static final String ANDROID_RESOURCE = "android.resource://";

    @SuppressLint("PackageManagerGetSignatures")
    private static boolean isSystemApp(String packageName, PackageManager packageManager) {
        try {
            PackageInfo targetPkgInfo = packageManager.getPackageInfo(
                    packageName, PackageManager.GET_SIGNATURES);
            PackageInfo sys = packageManager.getPackageInfo(
                    SYSTEM_PACKAGE_NAME, PackageManager.GET_SIGNATURES);
            return (targetPkgInfo != null && targetPkgInfo.signatures != null && sys.signatures[0]
                    .equals(targetPkgInfo.signatures[0]));
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static boolean isAppPreLoaded(String packageName, PackageManager packageManager) {
        if (packageName == null) {
            throw new IllegalArgumentException("Package name can not be null");
        }
        try {
            ApplicationInfo ai = packageManager.getApplicationInfo(
                    packageName, 0);
            if (ai != null
                    && (ai.flags & (ApplicationInfo.FLAG_SYSTEM | ApplicationInfo.FLAG_UPDATED_SYSTEM_APP))
                    != 0) {
                return isSystemApp(packageName, packageManager);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getApplicationLabel(ApplicationInfo applicationInfo,
                                             PackageManager packageManager) {
        CharSequence label = applicationInfo.loadLabel(packageManager);
        return label != null ? label.toString() : applicationInfo.packageName;
    }

    static Drawable getAppIcon(String packageName, PackageManager packageManager) {
        try {
            return packageManager.getApplicationIcon(packageName);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    static String getAppIconUri(PackageManager packageManager, String packageName) {
        try {
            int iconResId = packageManager.getApplicationInfo(packageName, 0).icon;
            return ANDROID_RESOURCE + packageName + "/" + iconResId;
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    public static List<ApplicationInfo> getApplications(Context context) {
        PackageManager packageManager = context.getPackageManager();
        return packageManager.getInstalledApplications(
                PackageManager.GET_META_DATA);
    }
}
