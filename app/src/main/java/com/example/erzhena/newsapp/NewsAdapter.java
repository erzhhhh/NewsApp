package com.example.erzhena.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Context context, List<News> lNews) {
        super(context, 0, lNews);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        News currentNews = getItem(position);

        TextView titleTextView = (TextView) listItemView.findViewById(R.id.news_title);
        titleTextView.setText(currentNews.getmTitle());

        TextView dateTextView = (TextView) listItemView.findViewById(R.id.news_date);
        dateTextView.setText(currentNews.getmDateValue());

        ImageView picture = (ImageView) listItemView.findViewById(R.id.news_thumbnail);
        String thumbnail = currentNews.getmThumbnail();
        Picasso.get().load(thumbnail).into(picture);

        return listItemView;
    }
}