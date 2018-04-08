package com.example.erzhena.newsapp;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.erzhena.newsapp.Data.NewsContract;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LoaderCallbacks<List<News>> {

    private static final int NEWS_LOADER_ID = 0;
    private NewsAdapter mAdapter;
    private TextView mEmptyStateTextView;
    private static final String SECTION_GENERAL = "general";
    private static final String SECTION_BUSINESS = "business";
    private static final String SECTION_ENTERTAINMENT = "entertainment";
    private static final String SECTION_HEALTH = "health";
    private static final String SECTION_SCIENCE = "science";
    private static final String SECTION_SPORT = "sport";
    private static final String SECTION_TECH = "technology";
    private static final String SECTION_EVERYTHING = "everything";
    private String navItem = SECTION_GENERAL;
    ListView newsListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("General news");

        newsListView = (ListView) findViewById(R.id.list);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        newsListView.setEmptyView(mEmptyStateTextView);

        mAdapter = new NewsAdapter(this, new ArrayList<News>());
        newsListView.setAdapter(mAdapter);


        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                News currentNews= mAdapter.getItem(position);
                String date = currentNews.getmDateValue();
                String author = currentNews.getmAuthor();
                String title = currentNews.getmTitle();
                String desc = currentNews.getmDescription();
                String url = currentNews.getmURL();
                String thumb = currentNews.getmThumbnail();

                Intent intent = new Intent(MainActivity.this, DetailNewsActivity.class);
                intent.putExtra("CURRENT_AUTHOR", author);
                intent.putExtra("CURRENT_TITLE", title);
                intent.putExtra("CURRENT_DESC", desc);
                intent.putExtra("CURRENT_DATE", date);
                intent.putExtra("CURRENT_URL", url);
                intent.putExtra("CURRENT_THUMB", thumb);
                intent.putExtra("ACTIVITY","main");

                Uri currentNewsUri = ContentUris.withAppendedId(NewsContract.NewsEntry.CONTENT_URI, l);
                intent.setData(currentNewsUri);

                startActivity(intent);
            }
        });


        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();


        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.loading_spinner);
            loadingIndicator.setVisibility(View.GONE);

            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        reloadNewsData();

    }


    private void reloadNewsData() {
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.restartLoader(NEWS_LOADER_ID, null, this);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_general) {
            navItem = SECTION_GENERAL;
            setTitle("General news");

        } else if (id == R.id.nav_business) {
            navItem = SECTION_BUSINESS;
            setTitle(getString(R.string.business_news));

        } else if (id == R.id.nav_entertainment) {
            navItem = SECTION_ENTERTAINMENT;
            setTitle("Entertainment news");

        } else if (id == R.id.nav_health) {
            navItem = SECTION_HEALTH;
            setTitle("Health news");

        } else if (id == R.id.nav_science) {
            navItem = SECTION_SCIENCE;
            setTitle("Science news");

        } else if (id == R.id.nav_sports) {
            navItem = SECTION_SPORT;
            setTitle("Sports news");

        } else if (id == R.id.nav_technology) {
            navItem = SECTION_TECH;
            setTitle("Technology news");

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        reloadNewsData();

        return true;
    }


    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {

//        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
//        String minMagnitude = sharedPrefs.getString(
//                getString(R.string.settings_min_magnitude_key),
//                getString(R.string.settings_min_magnitude_default));
//
//        String orderBy = sharedPrefs.getString(
//                getString(R.string.settings_order_by_key),
//                getString(R.string.settings_order_by_default)
//        );

        String USGS_REQUEST_URL = "https://newsapi.org/v2/top-headlines?";
        final String API_KEY = "014b2f51b7bd40ba8010be09d3b4f440";
        final String COUNTRY = "us";

        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("apiKey", API_KEY);
        uriBuilder.appendQueryParameter("country", COUNTRY);
        uriBuilder.appendQueryParameter("category", navItem);
        return new NewsLoader(this, uriBuilder.toString());
    }


    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        View loadingIndicator = findViewById(R.id.loading_spinner);
        loadingIndicator.setVisibility(View.GONE);

        mEmptyStateTextView.setText(R.string.no_news);

        mAdapter.clear();

        if (news != null && !news.isEmpty()) {
            mAdapter.addAll(news);
        }
    }


    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        mAdapter.clear();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;

            case R.id.already_saved:
                Intent intent = new Intent(MainActivity.this, SavedNewsActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

}