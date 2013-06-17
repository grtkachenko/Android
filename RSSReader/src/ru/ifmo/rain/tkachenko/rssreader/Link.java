package ru.ifmo.rain.tkachenko.rssreader;

import android.annotation.SuppressLint;

@SuppressLint("ViewConstructor")
public class Link {
    private String site, title;
    private boolean isFavorite = false;
    public long rowId = 0;

    public Link(String site, String title) {
        this.site = site;
        this.title = title;
    }

    public String getSite() {
        return site;
    }

    public String getTitle() {
        return title;
    }

    public void setSite(String data) {
        this.site = data;
    }

    public void setTitle(String data) {
        this.title = data;
    }

    public void setFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }
    public boolean isFavorite() {
        return this.isFavorite;
    }




}
