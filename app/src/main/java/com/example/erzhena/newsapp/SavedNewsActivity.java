package com.example.erzhena.newsapp;


import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.erzhena.newsapp.Data.NewsContract;
import com.squareup.picasso.Picasso;

public class SavedNewsActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>  {

    private static final int NEWS_LOADER = 0;

    NewsCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);

        ListView newsListView = (ListView) findViewById(R.id.list);

        View emptyView = findViewById(R.id.empty_view);
        newsListView.setEmptyView(emptyView);

        mCursorAdapter = new NewsCursorAdapter(this, null);
        newsListView.setAdapter(mCursorAdapter);


        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(SavedNewsActivity.this, DetailNewsActivity.class);
                Uri currentPetUri = ContentUris.withAppendedId(NewsContract.NewsEntry.CONTENT_URI, id);
                intent.setData(currentPetUri);
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(NEWS_LOADER, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                NewsContract.NewsEntry._ID,
                NewsContract.NewsEntry.COLUMN_NEWS_TITLE,
                NewsContract.NewsEntry.COLUMN_NEWS_DATA,
                NewsContract.NewsEntry.COLUMN_NEWS_THUMB};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                NewsContract.NewsEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        View loadingIndicator = findViewById(R.id.loading_spinner);
        loadingIndicator.setVisibility(View.GONE);
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
