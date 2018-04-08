package com.example.erzhena.newsapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class NewsCursorAdapter extends CursorAdapter {

    public NewsCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tTitle = (TextView) view.findViewById(R.id.news_title);
        TextView tDate = (TextView) view.findViewById(R.id.news_date);
        TextView tAuthor = (TextView) view.findViewById(R.id.news_authors);
        TextView tSource = (TextView) view.findViewById(R.id.news_source);
        ImageView tThumb = (ImageView) view.findViewById(R.id.news_thumbnail);


        String sTitle = cursor.getString(cursor.getColumnIndexOrThrow("title"));
        String sDate = cursor.getString(cursor.getColumnIndexOrThrow("data"));
        String sAuthor = cursor.getString(cursor.getColumnIndexOrThrow("author"));
        String sSource = cursor.getString(cursor.getColumnIndexOrThrow("source"));
        String sThumb = cursor.getString(cursor.getColumnIndexOrThrow("thumb"));

        if (TextUtils.isEmpty(sDate)) {
            sDate = context.getString(R.string.unknown_date);
        }

        // Populate fields with extracted properties
        tTitle.setText(sTitle);
        tDate.setText(sDate);
        tAuthor.setText(sAuthor);
        tSource.setText(sSource);
        Picasso.get().load(sThumb).into(tThumb);
    }
}
