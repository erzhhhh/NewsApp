package com.example.erzhena.newsapp;

import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.erzhena.newsapp.Data.NewsContract;
import com.squareup.picasso.Picasso;

public class DetailNewsFromSavedActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_PET_LOADER = 0;

    private TextView mTitleDetailText;
    String tTitle;
    private TextView mDescDetailText;
    String tDesc;
    private TextView mDateDetailText;
    private TextView mAuthorDetailText;
    private TextView mURLDetailText;
    String tDate;
    private ImageView mImageDetail;
    String tThumb;
    String tURL;
    private Uri mCurrentNewsUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        mCurrentNewsUri = intent.getData();


        mTitleDetailText = (TextView) findViewById(R.id.detail_title);
        mDescDetailText = (TextView) findViewById(R.id.detail_text);
        mAuthorDetailText = (TextView) findViewById(R.id.detail_author);
        mDateDetailText = (TextView) findViewById(R.id.detail_date);
        //without source textview
        mImageDetail = (ImageView) findViewById(R.id.detail_image);
        mURLDetailText = (TextView) findViewById(R.id.detail_link);


        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportLoaderManager().initLoader(EXISTING_PET_LOADER, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_detail_saved_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                String dt = tTitle + "\n "+ tDesc + "\n" + tDate + "\n" + tURL;
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, dt);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                return true;

            case android.R.id.home:
                Intent mainIntent = new Intent(this, MainActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }


    public void onBackPressed(){
            Intent mainIntent = new Intent(this, SavedNewsActivity.class);
            startActivity(mainIntent);
            finish();
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


        Log.i("onCreateLoader","onCreateLoader");

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                NewsContract.NewsEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);              // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of pet attributes that we're interested in
            int titleColumnIndex = cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_NEWS_TITLE);
            int descColumnIndex = cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_NEWS_DESC);
            int dataColumnIndex = cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_NEWS_DATA);
            int sourceColumnIndex = cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_NEWS_SOURCE);
            int authorColumnIndex = cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_NEWS_AUTHOR);
            int thumbColumnIndex = cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_NEWS_THUMB);
            int urlColumnIndex = cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_NEWS_URL);

            // Extract out the value from the Cursor for the given column index
            String title = cursor.getString(titleColumnIndex);
            String desc = cursor.getString(descColumnIndex);
            String data = cursor.getString(dataColumnIndex);
            //source is not using in detail view
            String source = cursor.getString(sourceColumnIndex);
            String author = cursor.getString(authorColumnIndex);
            String thumb = cursor.getString(thumbColumnIndex);
            String url = cursor.getString(urlColumnIndex);


            // Update the views on the screen with the values from the database
            mTitleDetailText.setText(title);
            mDescDetailText.setText(desc);
            mDateDetailText.setText(data);
            //without source
            mAuthorDetailText.setText(author);
            Picasso.get().load(thumb).into(mImageDetail);
            mURLDetailText.setText(url);

            tTitle = title;
            tDesc = desc;
            tDate = data;
            tURL = url;

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
//        mTitleDetailText.setText("");
//        mDescDetailText.setText("");
//        mDateDetailText.setText("");// Select "Unknown" gender
//        mAuthorDetailText.setText("");
//        Picasso.get().load("").into(mImageDetail);
//        mURLDetailText.setText("");

        finish();
    }
}
