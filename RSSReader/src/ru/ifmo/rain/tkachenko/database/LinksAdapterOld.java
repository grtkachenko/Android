package ru.ifmo.rain.tkachenko.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import ru.ifmo.rain.tkachenko.rssreader.RSSFeed;
import ru.ifmo.rain.tkachenko.rssreader.RSSHandler;
import ru.ifmo.rain.tkachenko.rssreader.RSSItem;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.net.URL;
import java.util.ArrayList;

public class LinksAdapterOld {
    public static final String KEY_URL = "url";
    public static final String KEY_TITLE = "title";
    public static final String KEY_FAV = "fav";
    public static final String KEY_FEED = "feed";


    public static final String KEY_ROWID = "_id";

    private static final String TAG = "LinksDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_CREATE = "create table links (_id integer primary key autoincrement, "
            + "url text not null, title text not null, fav text not null, feed text not null);";

    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE = "links";
    private static final int DATABASE_VERSION = 2;

    private Cursor linkCursor;
    private Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, "Create");
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS links");
            onCreate(db);
        }
    }

    public void dropTabe() {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        mDb.execSQL("DROP TABLE IF EXISTS links");
        mDb.execSQL(DATABASE_CREATE);
    }

    public LinksAdapterOld(Context ctx) {
        this.mCtx = ctx;
    }

    public LinksAdapterOld open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        //mDbHelper.onCreate(mDb); ������ ����� � �� ���� �� ������� ���������� ���� �� ������ Create
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public long createLink(String url, String title, String fav) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_URL, url);
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_FAV, fav);
        initialValues.put(KEY_FEED, "");

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    public boolean deleteLink(long rowId) {
        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean deleteLink(String link) {
        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + getIdByLink(link), null) > 0;
    }

    public Cursor fetchAllLinks() {
        return mDb.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_URL, KEY_TITLE, KEY_FAV, KEY_FEED},
                null, null, null, null, null);
    }

    public Cursor fetchLink(long rowId) throws SQLException {

        Cursor mCursor = mDb.query(true, DATABASE_TABLE, new String[]{
                KEY_ROWID, KEY_URL, KEY_TITLE, KEY_FAV, KEY_FEED}, KEY_ROWID + "=" + rowId, null, null,
                null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public boolean updateLink(long rowId, String url) {
        ContentValues args = new ContentValues();
        args.put(KEY_URL, url);

        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean updateLink(String oldUrl, String url) {
        ContentValues args = new ContentValues();
        args.put(KEY_URL, url);

        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + getIdByLink(oldUrl), null) > 0;
    }

    public boolean updateTitle(String oldTitle, String title) {
        ContentValues args = new ContentValues();
        args.put(KEY_TITLE, title);

        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + getIdByTitle(oldTitle), null) > 0;
    }

    public boolean updateFav(String title, String fav) {
        ContentValues args = new ContentValues();
        args.put(KEY_FAV, fav);
        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + getIdByTitle(title), null) > 0;
    }

    public boolean isFavorite(String title) {
        Cursor link = fetchLink(getIdByTitle(title));
        return link.getString(link
                .getColumnIndex("fav")).equals("true");
    }


    public boolean updateFeed(String title, String feed) {
        ContentValues args = new ContentValues();
        args.put(KEY_FEED, feed);
        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + getIdByTitle(title), null) > 0;
    }

    public void updateAllFeeds() {
        curAdapter = this;
        curAdapter.open();
        MyTask mt = new MyTask();
        mt.execute();
    }

    public volatile boolean f = true, isRefresh = false, isCalced = true;
    public LinksAdapterOld curAdapter = null;

    class MyTask extends AsyncTask<Void, Void, Void> {
        private RSSFeed feed = null;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            linkCursor = curAdapter.fetchAllLinks();
            if (linkCursor.moveToFirst()) {
                do {
                    String cur = "http://" + linkCursor.getString(linkCursor
                            .getColumnIndex("url"));

                    try {
                        URL url = new URL(cur);

                        SAXParserFactory factory = SAXParserFactory.newInstance();
                        SAXParser parser = factory.newSAXParser();

                        XMLReader xmlreader = parser.getXMLReader();
                        RSSHandler theRssHandler = new RSSHandler();
                        xmlreader.setContentHandler(theRssHandler);
                        InputSource is = new InputSource(url.openStream());
                        xmlreader.parse(is);

                        feed = theRssHandler.getFeed();
                        ArrayList<RSSItem> data = feed.getAllItems();
                        String rssFeedString = "";
                        for (int i = 0; i < data.size(); i++) {
                            rssFeedString += data.get(i).getTitle() + " # " + data.get(i).getDescription() + " # " + data.get(i).getPubDate() + " ## ";
                        }
                        rssFeedString += "###";
                        curAdapter.updateFeed(linkCursor.getString(linkCursor
                                .getColumnIndex("title")), rssFeedString);
                        Log.i("url", "OK");

                    } catch (Exception ee) {
                    }
                } while (linkCursor.moveToNext());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }


    }


    private long getIdByLink(String link) {
        linkCursor = this.fetchAllLinks();
        long id = 0;
        if (linkCursor.moveToFirst()) {
            do {
                String cur = linkCursor.getString(linkCursor
                        .getColumnIndex("url"));

                if (cur.equals(link)) {
                    return linkCursor.getLong(linkCursor.getColumnIndex("_id"));
                }
            } while (linkCursor.moveToNext());
        }
        return 0;
    }

    private long getIdByTitle(String title) {
        linkCursor = this.fetchAllLinks();
        long id = 0;
        if (linkCursor.moveToFirst()) {
            do {
                String cur = linkCursor.getString(linkCursor
                        .getColumnIndex("title"));

                if (cur.equals(title)) {
                    return linkCursor.getLong(linkCursor.getColumnIndex("_id"));
                }
            } while (linkCursor.moveToNext());
        }
        return 0;
    }

    public String getFeed(String title) {
        Cursor cursor = fetchLink(getIdByTitle(title));
        String feed = cursor.getString(cursor
                .getColumnIndex("feed"));
        return feed;
    }
}
