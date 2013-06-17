package ru.ifmo.rain.tkachenko.rssreader;

import java.util.ArrayList;
import java.util.Collections;

public class RSSFeed {

	private String title = null;
	private String pubdate = null;
	private int itemcount = 0;
	private ArrayList<RSSItem> itemlist;

	public RSSFeed() {
		itemlist = new ArrayList<RSSItem>();
	}

	public int addItem(RSSItem item) {
		itemlist.add(item);
		itemcount++;
		
		return itemcount;
	}

	public RSSItem getItem(int location) {
		return itemlist.get(location);
	}

	public ArrayList<RSSItem> getAllItems() {
		return itemlist;
	}

	int getItemCount() {
		return itemcount;
	}

	void setTitle(String title) {
		this.title = title;
	}

	void setPubDate(String pubdate) {
		this.pubdate = pubdate;
	}

	public String getTitle() {
		return title;
	}

	public String getPubDate() {
		return pubdate;
	}

    public void sortByDate() {
        Collections.sort(itemlist);

    }
}
