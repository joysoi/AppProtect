package com.nikola.appprotect.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


public class AppsContentProvider extends ContentProvider {
    private static final String AUTHORITY = "com.nikola.appprotect.database.contentprovider";
    private static final String BASE_PATH = "applications";
    public static final Uri BASE_CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    private static final int APP = 100;
    private static final int APP_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "applications", APP);
        uriMatcher.addURI(AUTHORITY, "applications/#", APP_ID);
        return uriMatcher;
    }

    private SQLiteDatabase sqLiteDatabase;

    @Override
    public boolean onCreate() {
        AppsDbHelper appsDbHelper = new AppsDbHelper(getContext());
        sqLiteDatabase = appsDbHelper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        if (sUriMatcher.match(uri) == APP_ID) {
            selection = AppsDb.COLUMN_ID + "/" + uri.getLastPathSegment();
        }
        return sqLiteDatabase.query(AppsDb.TABLE_NAME, AppsDb.ALL_COLUMNS, selection, selectionArgs, null, null, AppsDb.COLUMN_LOCKED_STATUS + " DESC");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long id = sqLiteDatabase.insert(AppsDb.TABLE_NAME, null, values);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return sqLiteDatabase.delete(AppsDb.TABLE_NAME, selection, selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return sqLiteDatabase.update(AppsDb.TABLE_NAME, values, selection, selectionArgs);
    }
}
