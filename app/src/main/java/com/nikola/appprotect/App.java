package com.nikola.appprotect;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.nikola.appprotect.database.AppsDbManager;

import timber.log.Timber;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        Stetho.initializeWithDefaults(this);
        FirebaseAnalytics.getInstance(this);
        MobileAds.initialize(getApplicationContext(), String.valueOf(R.string.banner_ad_unit_id));
        AppsDbManager.writeToDb(this);
    }
}
