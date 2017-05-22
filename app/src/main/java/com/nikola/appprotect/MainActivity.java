package com.nikola.appprotect;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.nikola.appprotect.database.AppsContentProvider;
import com.nikola.appprotect.database.AppsDb;

public class MainActivity extends BaseActivity {
    private static final String BROADCAST = "WakefulBroadcastReceiver.StartManually";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        AdRequest adRequest = new AdRequest.Builder().build();
        AdView adView = (AdView) findViewById(R.id.ad_banner);
        if (adView != null) {
            adView.loadAd(adRequest);
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        MainFragment fragment = new MainFragment();
        transaction.replace(R.id.container, fragment, "fragment_tag");
        transaction.commit();
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_main;
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            ((DataPass) getSupportFragmentManager().findFragmentByTag("fragment_tag")).input(query);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    @SuppressWarnings("ConstantConditions")
    public boolean isThereAnyLockedApps() {
        String[] mProjection =
                {AppsDb.COLUMN_ID};
        String mSelectionClause = AppsDb.COLUMN_LOCKED_STATUS + " = ?";
        String[] mSelectionArgs = {"1"};
        try {
            return getContentResolver().query(AppsContentProvider.BASE_CONTENT_URI, mProjection, mSelectionClause, mSelectionArgs, null, null).getCount() != 0;
        } catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isThereAnyLockedApps()) {
            Intent intent = new Intent(BROADCAST);
            sendBroadcast(intent);
        }
    }
}
