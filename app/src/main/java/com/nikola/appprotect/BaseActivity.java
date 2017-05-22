package com.nikola.appprotect;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public abstract class BaseActivity extends AppCompatActivity {

    protected Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int contentViewId = getContentViewLayoutId();
        if (contentViewId != 0) {
            setContentView(contentViewId);
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    /**
     * Known views that the content will be searched for:
     * {@link Toolbar} {@link R.id#toolbar};
     */
    abstract protected int getContentViewLayoutId();
}
