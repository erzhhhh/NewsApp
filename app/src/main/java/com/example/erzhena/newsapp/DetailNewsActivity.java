package com.example.erzhena.newsapp;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.erzhena.newsapp.Data.NewsContract;
import com.squareup.picasso.Picasso;

public class DetailNewsActivity extends AppCompatActivity {

    private TextView detail_title;
    String tTitle;
    private TextView detail_desc;
    String tDesc;
    private TextView detail_date;
    String tDate;
    private ImageView detail_image;
    String tThumb;
    String tURL;
    private Uri mCurrentNewsUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();

        mCurrentNewsUri = intent.getData();

        String dTitle = intent.getStringExtra("CURRENT_TITLE");
        String dDesc = intent.getStringExtra("CURRENT_DESC");
        String dDate = intent.getStringExtra("CURRENT_DATE");
        String dThumb = intent.getStringExtra("CURRENT_THUMB");
        String dURL = intent.getStringExtra("CURRENT_URL");


        tTitle = dTitle;
        tDesc = dDesc;
        tDate = dDate;
        tThumb = dThumb;
        tURL = dURL;

        detail_title = (TextView) findViewById(R.id.detail_title);
        detail_title.setText(dTitle);

        detail_desc = (TextView) findViewById(R.id.detail_text);
        detail_desc.setText(dDesc);

        detail_date = (TextView) findViewById(R.id.detail_date);
        detail_date.setText(dDate);

        detail_image = (ImageView) findViewById(R.id.detail_image);
        Picasso.get().load(dThumb).into(detail_image);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_detail_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                String dt = tTitle + "\n "+ tDesc + "\n" + tDate;
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

            case R.id.favorites:
                insertNews();
                return true;


            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void insertNews() {
        ContentValues values = new ContentValues();
        values.put(NewsContract.NewsEntry.COLUMN_NEWS_TITLE, tTitle.trim());
        values.put(NewsContract.NewsEntry.COLUMN_NEWS_DESC, tDesc.trim());
        values.put(NewsContract.NewsEntry.COLUMN_NEWS_DATA, tDate.trim());
        values.put(NewsContract.NewsEntry.COLUMN_NEWS_THUMB, tThumb.trim());
        values.put(NewsContract.NewsEntry.COLUMN_NEWS_URL, tURL.trim());

        Uri newUri = getContentResolver().insert(NewsContract.NewsEntry.CONTENT_URI, values);
        Log.i("newUri", String.valueOf(newUri));

        // Show a toast message depending on whether or not the insertion was successful.
        if (newUri == null) {
            // If the new content URI is null, then there was an error with insertion.
            Toast.makeText(this, getString(R.string.fail_added_news),
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast.
            Toast.makeText(this, getString(R.string.success_added_news),
                    Toast.LENGTH_SHORT).show();
        }

    }


    public void onBackPressed(){
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        finish();
    }
}
