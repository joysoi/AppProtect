package com.nikola.appprotect.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


class AppsDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Applications";
    private static final int DATABASE_VERSION = 1;

    AppsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        AppsDb.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        AppsDb.onUpgrade(db, oldVersion, newVersion);
    }
}
