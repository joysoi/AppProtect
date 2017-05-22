package com.nikola.appprotect.services.ui;

import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.support.annotation.NonNull;
import android.view.WindowManager;

public class WindowManagerUtil {

    @NonNull
    public static WindowManager.LayoutParams setupLayoutParams(int gravity, int height,
                                                               int width) {
        WindowManager.LayoutParams paramsAppLabel =
                new WindowManager.LayoutParams(width,
                        height, WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
        paramsAppLabel.gravity = gravity;
        paramsAppLabel.x = 0;
        paramsAppLabel.y = 0;
        paramsAppLabel.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        return paramsAppLabel;
    }
}
