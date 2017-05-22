package com.nikola.appprotect.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class GlideUtil {

    public static void setAppIcon(Context context, ImageView imageView, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        Glide.with(context)
                .load(ApplicationUtil.getAppIconUri(packageManager, packageName))
                .error(ApplicationUtil.getAppIcon(
                        packageName, packageManager))
                .into(imageView);
    }
}
