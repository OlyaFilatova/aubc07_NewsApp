package com.miymayster.olya.newsapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    public static ArrayList<Article> loadArticles(String requestUrl) {
        URL url = createURL(requestUrl);
        ArrayList<Article> articles = null;
        try {
            String jsonString = makeHttpRequest(url);
            articles = extractArticles(jsonString);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "JSONException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(LOG_TAG, "IOException: " + e.getMessage());
        }
        return articles;
    }

    private static URL createURL(String requestUrl) {
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonString = null;
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setReadTimeout(10000);
        httpURLConnection.setConnectTimeout(10000);
        httpURLConnection.connect();
        if (httpURLConnection.getResponseCode() == 200) {
            jsonString = readFromInputStream(httpURLConnection.getInputStream());
        }
        return jsonString;
    }

    private static String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        InputStreamReader reader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String line = bufferedReader.readLine();
        while (line != null) {
            stringBuilder.append(line);
            line = bufferedReader.readLine();
        }
        return stringBuilder.toString();
    }

    private static ArrayList<Article> extractArticles(String jsonString) throws JSONException {
        ArrayList<Article> articles = new ArrayList<>();
        JSONObject rootObject = new JSONObject(jsonString);
        JSONObject responseObject = rootObject.getJSONObject("response");
        JSONArray resultsArray = responseObject.getJSONArray("results");
        int length = resultsArray.length();
        for (int i = 0; i < length; i++) {
            JSONObject articleObject = resultsArray.getJSONObject(i);
            String webTitle = articleObject.getString("webTitle");
            String sectionName = articleObject.getString("sectionName");
            String webPublicationDate = articleObject.getString("webPublicationDate");
            String webUrl = articleObject.getString("webUrl");
            Article article = new Article(webTitle, sectionName, webPublicationDate, webUrl);
            articles.add(article);
        }
        return articles;
    }
}
