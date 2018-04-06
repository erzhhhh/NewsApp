package com.example.erzhena.newsapp.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.erzhena.newsapp.News;

public class NewsProvider extends ContentProvider {

    private NewsDbHelper mNewsHelper;
    public static final String LOG_TAG = NewsProvider.class.getSimpleName();

    @Override
    public boolean onCreate() {
        mNewsHelper = new NewsDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase database = mNewsHelper.getReadableDatabase();
        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case NEWS:
                cursor = database.query(NewsContract.NewsEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case NEWS_ID:
                selection = NewsContract.NewsEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = database.query(NewsContract.NewsEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), NewsContract.NewsEntry.CONTENT_URI);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case NEWS:
                return insertNews(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }


    private Uri insertNews(Uri uri, ContentValues values) {
        SQLiteDatabase database = mNewsHelper.getWritableDatabase();

        // Insert the new pet with the given values
        long id = database.insert(NewsContract.NewsEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }



    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mNewsHelper.getWritableDatabase();
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case NEWS:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(NewsContract.NewsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case NEWS_ID:
                // Delete a single row given by the ID in the URI
                selection = NewsContract.NewsEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(NewsContract.NewsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case NEWS:
                return NewsContract.NewsEntry.CONTENT_LIST_TYPE;
            case NEWS_ID:
                return NewsContract.NewsEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    private static final int NEWS = 100;
    private static final int NEWS_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        sUriMatcher.addURI(NewsContract.NewsEntry.CONTENT_AUTHORITY, NewsContract.NewsEntry.PATH_NEWS, NEWS);
        sUriMatcher.addURI(NewsContract.NewsEntry.CONTENT_AUTHORITY, NewsContract.NewsEntry.PATH_NEWS + "/#", NEWS_ID);
    }
}