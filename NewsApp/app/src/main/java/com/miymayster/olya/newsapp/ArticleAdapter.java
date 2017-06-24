package com.miymayster.olya.newsapp;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class ArticleAdapter extends ArrayAdapter<Article> {
    public ArticleAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Article> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.article, parent, false);
        }
        Article article = getItem(position);
        if (article != null) {
            String dateString = "";
            String timeString = "";
            try {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                Date date = df.parse(article.getDate());
                SimpleDateFormat dateDateFormat = new SimpleDateFormat("yyyy.MM.dd");
                dateString = dateDateFormat.format(date);
                SimpleDateFormat timeDateFormat = new SimpleDateFormat("HH:mm");
                timeString = timeDateFormat.format(date);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
            ((TextView) convertView.findViewById(R.id.title)).setText(article.getTitle());
            ((TextView) convertView.findViewById(R.id.section)).setText(article.getSection());
            ((TextView) convertView.findViewById(R.id.date)).setText(dateString);
            ((TextView) convertView.findViewById(R.id.time)).setText(timeString);
        }
        return convertView;
    }
}
