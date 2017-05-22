package com.nikola.appprotect.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AppsDb {
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LABEL = "LABEL";
    public static final String COLUMN_PACKAGE_NAME = "PACKAGE_NAME";
    public static final String COLUMN_LOCKED_STATUS = "LOCKED";

    static final String[] ALL_COLUMNS = {
            AppsDb.COLUMN_ID,
            AppsDb.COLUMN_LABEL,
            AppsDb.COLUMN_PACKAGE_NAME,
            AppsDb.COLUMN_LOCKED_STATUS};

    static final String TABLE_NAME = "applications";
    private static final String LOG_TAG = "ApplicationsDb";

    private static final String DATABASE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_LABEL + " TEXT," +
                    COLUMN_PACKAGE_NAME + " TEXT," +
                    COLUMN_LOCKED_STATUS + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    static void onCreate(SQLiteDatabase db) {
        Log.w(LOG_TAG, DATABASE_CREATE);
        db.execSQL(DATABASE_CREATE);
    }

    static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
