package com.miymayster.olya.newsapp;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;

import java.util.List;

/**
 * Loads a list of articles by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class ArticleLoader extends AsyncTaskLoader<List<Article>> {
    /** Query URL */
    private String webUrl;

    /**
     * Constructs a new {@link ArticleLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public ArticleLoader(Context context, String url) {
        super(context);
        webUrl = url;
    }

    @Override
    public List<Article> loadInBackground() {
        if(TextUtils.isEmpty(webUrl)){
            return null;
        }
        // Perform the network request, parse the response, and extract a list of earthquakes.
        return QueryUtils.loadArticles(webUrl);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
