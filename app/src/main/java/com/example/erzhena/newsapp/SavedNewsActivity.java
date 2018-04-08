package com.example.erzhena.newsapp;


import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.erzhena.newsapp.Data.NewsContract;

public class SavedNewsActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>  {

    private static final int NEWS_LOADER = 1;

    NewsCursorAdapter mCursorAdapter;

    int d;

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
                Intent newsDetailIntent = new Intent(SavedNewsActivity.this,  DetailNewsFromSavedActivity.class);
                Uri currentNewsUri = ContentUris.withAppendedId(NewsContract.NewsEntry.CONTENT_URI, id);
                newsDetailIntent.setData(currentNewsUri);
                startActivity(newsDetailIntent);
                finish();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }});

        getLoaderManager().initLoader(NEWS_LOADER, null, this);
    }

    private void deleteAllNews() {
        int rowsDeleted = getContentResolver().delete(NewsContract.NewsEntry.CONTENT_URI, null, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(d != 0) {
            getMenuInflater().inflate(R.menu.menu_saved, menu);
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_all_news:
                showDeleteConfirmationDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                NewsContract.NewsEntry._ID,
                NewsContract.NewsEntry.COLUMN_NEWS_TITLE,
                NewsContract.NewsEntry.COLUMN_NEWS_DESC,
                NewsContract.NewsEntry.COLUMN_NEWS_DATA,
                NewsContract.NewsEntry.COLUMN_NEWS_SOURCE,
                NewsContract.NewsEntry.COLUMN_NEWS_AUTHOR,
                NewsContract.NewsEntry.COLUMN_NEWS_THUMB,
                NewsContract.NewsEntry.COLUMN_NEWS_URL};

        return new CursorLoader(this,   // Parent activity context
                NewsContract.NewsEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        int p = cursor.getCount();
        d = p;

        View loadingIndicator = findViewById(R.id.loading_spinner);
        loadingIndicator.setVisibility(View.GONE);
        mCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_all_news_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteAllNews();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
