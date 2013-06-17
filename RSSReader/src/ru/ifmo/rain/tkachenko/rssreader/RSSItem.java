package ru.ifmo.rain.tkachenko.rssreader;

public class RSSItem implements Comparable<RSSItem> {
    private String title = null;
    private String description = null;
    private String link = null;
    private String category = null;
    private String pubdate = null;

    public RSSItem() {
    }

    public RSSItem(String title, String description, String pubdate) {
        this.title = title;
        this.description = description;
        this.pubdate = pubdate;
    }


    void setTitle(String title) {
        this.title = title;
    }

    void setDescription(String description) {
        this.description = description;
    }

    void setLink(String link) {
        this.link = link;
    }

    void setCategory(String category) {
        this.category = category;
    }

    void setPubDate(String pubdate) {
        this.pubdate = pubdate;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }


    public String getPubDate() {
        return pubdate;
    }

    public String toString() {
        // limit how much text you display
        if (title.length() > 42) {
            return title.substring(0, 42) + "...";
        }
        return title;
    }

    @Override
    public int compareTo(RSSItem another) {
        String stThis = this.pubdate.substring(4);
        String stAnother = another.pubdate.substring(4);
        return -stThis.compareTo(stAnother);
    }
}
