package com.example.erzhena.newsapp;

import android.app.LoaderManager;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.erzhena.newsapp.Data.NewsContract;
import com.squareup.picasso.Picasso;

public class DetailNewsFromSavedActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_NEWS_LOADER = 0;

    private TextView mTitleDetailText;
    private TextView mDescDetailText;
    private TextView mDateDetailText;
    private TextView mAuthorDetailText;
    private TextView mURLDetailText;
    private ImageView mImageDetail;
    private Uri mCurrentNewsUri;

    String tTitle;
    String tDesc;
    String tDate;
    String tURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setTitle("Saved news");

        Intent intent = getIntent();
        mCurrentNewsUri = intent.getData();

        getLoaderManager().initLoader(EXISTING_NEWS_LOADER, null, this);

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

            case R.id.delete_from_saved:
                showDeleteConfirmationDialog();
                return true;

            case android.R.id.home:
                Intent intent = new Intent(this, SavedNewsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
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

        return new CursorLoader(this,
                mCurrentNewsUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToNext()) {
            int titleColumnIndex = cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_NEWS_TITLE);
            int descColumnIndex = cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_NEWS_DESC);
            int dataColumnIndex = cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_NEWS_DATA);
            int authorColumnIndex = cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_NEWS_AUTHOR);
            int thumbColumnIndex = cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_NEWS_THUMB);
            int urlColumnIndex = cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_NEWS_URL);

            String title = cursor.getString(titleColumnIndex);
            String desc = cursor.getString(descColumnIndex);
            String data = cursor.getString(dataColumnIndex);
            String author = cursor.getString(authorColumnIndex);
            String thumb = cursor.getString(thumbColumnIndex);
            String url = cursor.getString(urlColumnIndex);


            mTitleDetailText.setText(title);
            mDescDetailText.setText(desc);
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
        finish();
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_all_news_dialog_msg_saved);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteNews();
                Intent mainIntent = new Intent(DetailNewsFromSavedActivity.this, SavedNewsActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent);
                finish();
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

    private void deleteNews() {
        if (mCurrentNewsUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentNewsUri, null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.editor_delete_news_success),
                        Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, getString(R.string.editor_delete_news_failed),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

}
