package ru.ifmo.rain.tkachenko.rssreader;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.*;
import android.util.Log;

public class RSSHandler extends DefaultHandler {

	RSSFeed feed;
	RSSItem item;
	String lastElementName = "";
	boolean bFoundChannel = false;
	final int RSSTITLE = 1;
	final int RSSLINK = 2;
	final int RSSDESCRIPTION = 3;
	final int RSSCATEGORY = 4;
	final int RSSPUBDATE = 5;

	int depth = 0;
	int currentState = 0;

	

	/*
	 * getFeed - this returns our feed when all of the parsing is complete
	 */
	public RSSFeed getFeed() {
		return feed;
	}

	public void startDocument() throws SAXException {
		// initialize our RSSFeed object - this will hold our parsed contents
		feed = new RSSFeed();
		// initialize the RSSItem object - you will use this as a crutch to grab
		// the info from the channel
		// because the channel and items have very similar entries..
		item = new RSSItem();

	}

	public void endDocument() throws SAXException {
	}

	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		depth++;
		if (localName.equals("channel")) {
			currentState = 0;
			return;
		}
		if (localName.equals("image")) {
			// record our feed data - you temporarily stored it in the item :)
			feed.setTitle(item.getTitle());
			feed.setPubDate(item.getPubDate());
		}
		if (localName.equals("item")) {
			// create a new item
			item = new RSSItem();
			return;
		}
		if (localName.equals("title")) {
			currentState = RSSTITLE;
			return;
		}
		if (localName.equals("description")) {
			currentState = RSSDESCRIPTION;
			return;
		}
		if (localName.equals("link")) {
			currentState = RSSLINK;
			return;
		}
		if (localName.equals("category")) {
			currentState = RSSCATEGORY;
			return;
		}
		if (localName.equals("pubDate")) {
			currentState = RSSPUBDATE;
			return;
		}
		// if you don't explicitly handle the element, make sure you don't wind
		// up erroneously storing a newline or other bogus data into one of our
		// existing elements
		currentState = 0;
	}

	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		depth--;
		if (localName.equals("item")) {
			// add our item to the list!
			feed.addItem(item);
			return;
		}
	}

	public void characters(char ch[], int start, int length) {
		String theString = new String(ch, start, length);
		Log.i("RSSReader", "characters[" + theString + "]");

		switch (currentState) {
		case RSSTITLE:
			item.setTitle(theString);
			currentState = 0;
			break;
		case RSSLINK:
			item.setLink(theString);
			currentState = 0;
			break;
		case RSSDESCRIPTION:
			item.setDescription(theString);
			currentState = 0;
			break;
		case RSSCATEGORY:
			item.setCategory(theString);
			currentState = 0;
			break;
		case RSSPUBDATE:
			item.setPubDate(theString);
			currentState = 0;
			break;
		default:
			return;
		}

	}
}

