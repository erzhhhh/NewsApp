package com.example.erzhena.newsapp.Data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class NewsContract {

    public NewsContract() {}

    public static final class NewsEntry implements BaseColumns {
        public static final String CONTENT_AUTHORITY = "com.example.erzhena.newsapp";
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
        public static final String PATH_NEWS = "news";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_NEWS);

        public final static String TABLE_NAME = "news";

        public final static String COLUMN_NEWS_TITLE = "title";
        public final static String COLUMN_NEWS_DESC = "desc";
        public final static String COLUMN_NEWS_DATA = "data";
        public final static String COLUMN_NEWS_THUMB = "thumb";
        public final static String COLUMN_NEWS_URL = "url";


        //все новости
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NEWS;

        //опредленная
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NEWS;


        //CONTENT_URI = content://com.example.erzhena.newsapp/news
    }

}
