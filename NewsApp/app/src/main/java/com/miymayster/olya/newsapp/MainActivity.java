package com.miymayster.olya.newsapp;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {
    /**
     * Constant value for request url
     */
    private final static String REQUEST_URL = "https://content.guardianapis.com/lifeandstyle/gardens?api-key=test";
    /**
     * Constant value for the articles loader ID.
     */
    private static final int ARTICLE_LOADER = 1;
    /**
     * list of articles loaded from the server
     */
    private ArrayList<Article> mArticles;
    /**
     * ListView to show articles
     */
    ListView mArticleList;
    /**
     * mAdapter to show articles in the ListView
     */
    ArticleAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mArticles = new ArrayList<>();
        mAdapter = new ArticleAdapter(this, R.layout.article, mArticles);
        mArticleList = (ListView) findViewById(R.id.list);
        mArticleList.setEmptyView(findViewById(R.id.no_news_text_view));
        mArticleList.setAdapter(mAdapter);
        mArticleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(mArticles.get(position).getUrl()));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        // Get a reference to the LoaderManager, in order to interact with loaders.
        LoaderManager loaderManager = getSupportLoaderManager();
        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
        // because this activity implements the LoaderCallbacks interface).
        loaderManager.initLoader(ARTICLE_LOADER, null, this);
    }

    /**
     * check if user got internet
     * @return true - if user got internet connection / false - if there is none
     */
    private boolean checkForInternet() {
        // Get a reference to the ConnectivityManager, in order to check for connection
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        // get info about active connection
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        // check if there is info and that there is connection
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case ARTICLE_LOADER:
                // Get a reference to progress bar
                View progressBar = findViewById(R.id.progress);
                // show progress bar
                progressBar.setVisibility(View.VISIBLE);
                // Create a new loader for the given URL
                return new ArticleLoader(this, REQUEST_URL);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {
        // Loader finish, so we can show newly loaded data
        refreshUI((ArrayList<Article>) articles);
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        // Loader reset, so we can clear out our existing data.
        refreshUI(null);
    }

    public void refreshUI(ArrayList<Article> articles) {
        mArticles = articles;
        TextView noNewsTextView = ((TextView) findViewById(R.id.no_news_text_view));
        // Get a reference to progress bar
        View progressBar = findViewById(R.id.progress);
        // hide progress bar
        progressBar.setVisibility(View.GONE);
        noNewsTextView.setText("");
        // Clear the mAdapter of previous articles data
        mAdapter.clear();
        // If there is a valid list of {@link Article}s, then add them to the mAdapter's
        // data set. This will trigger the ListView to update.
        if(articles != null && !articles.isEmpty()) {
            mAdapter.addAll(articles);
        } else {
            if (checkForInternet()) {
                noNewsTextView.setText(getString(R.string.no_news));
            } else {
                noNewsTextView.setText(getString(R.string.no_connection));
            }
        }
        mAdapter.notifyDataSetChanged();
    }
}
