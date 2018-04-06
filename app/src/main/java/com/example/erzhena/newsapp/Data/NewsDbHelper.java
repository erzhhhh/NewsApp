package com.example.erzhena.newsapp.Data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NewsDbHelper extends SQLiteOpenHelper {

    public final static String DB_NAME = "savedNews.db";
    public final static int DB_VERSION = 1;

    public NewsDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase dbSavedNews) {

        String SQL_CREATE_NEWS_TABLE =  "CREATE TABLE " + NewsContract.NewsEntry.TABLE_NAME + " ("
                + NewsContract.NewsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NewsContract.NewsEntry.COLUMN_NEWS_TITLE + " TEXT NOT NULL, "
                + NewsContract.NewsEntry.COLUMN_NEWS_DESC + " TEXT, "
                + NewsContract.NewsEntry.COLUMN_NEWS_DATA + " TEXT NOT NULL, "
                + NewsContract.NewsEntry.COLUMN_NEWS_THUMB + " TEXT, "
                + NewsContract.NewsEntry.COLUMN_NEWS_URL + " TEXT NOT NULL);";

        dbSavedNews.execSQL(SQL_CREATE_NEWS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
