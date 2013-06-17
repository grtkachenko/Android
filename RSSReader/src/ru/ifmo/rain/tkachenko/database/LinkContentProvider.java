package ru.ifmo.rain.tkachenko.database;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Created with IntelliJ IDEA.
 * User: Grigory
 * Date: 12/26/12
 * Time: 6:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class LinkContentProvider extends ContentProvider {
    private static final String AUTHORITY = "ru.ifmo.rain.tkachenko.database";
    public static Uri CONTENT_URI = Uri.parse(ContentResolver.SCHEME_CONTENT
            + "://" + AUTHORITY);

    private static final String DATABASE_NAME = "data";
    private static final int DATABASE_VERSION = 8;
    static final String LINK_ID = "_id";
    private static String LINKS_TABLE = "links";
    public static Uri CONTENT_LINK_URI = Uri.withAppendedPath(CONTENT_URI,
            LINKS_TABLE);

    private static final int CODE_LINKS = 1;
    private static final int CODE_LINK = 2;

    private static UriMatcher MATCHER = null;

    static {
        MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        MATCHER.addURI(AUTHORITY, LINKS_TABLE, CODE_LINKS);
        MATCHER.addURI(AUTHORITY, LINKS_TABLE + "/*", CODE_LINK);
    }

    private SQLiteOpenHelper dbHelper;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        private static final String DATABASE_CREATE = "create table links (_id integer primary key autoincrement, "
                + "url text not null, title text not null, fav text not null, feed text not null);";

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists links");
            onCreate(db);
        }

        public void dropTable() {
            this.getWritableDatabase().execSQL("drop table links if exist");
            this.getWritableDatabase().execSQL("create table links (_id integer primary key autoincrement, "
                    + "url text not null, title text not null, fav text not null, feed text not null);");
        }
    }


    @Override
    public boolean onCreate() {
        dbHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (MATCHER.match(uri)) {
            case CODE_LINKS:
                break;
            case CODE_LINK:
                break;
            default:
                throw new IllegalArgumentException("Wrong uri: " + uri.toString());
        }
        Cursor c = db.query(LINKS_TABLE, projection, selection, selectionArgs,
                null, null, null);
        c.setNotificationUri(getContext().getContentResolver(),
                CONTENT_LINK_URI);
        return c;
    }

    @Override
    public String getType(Uri uri) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Uri insert(Uri uri, ContentValues cv) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (MATCHER.match(uri)) {
            case CODE_LINKS:
                break;
            default:
                throw new IllegalArgumentException("Wrong uri: " + uri.toString());
        }
        long rowID = db.insert(LINKS_TABLE, null, cv);
        getContext().getContentResolver().notifyChange(CONTENT_LINK_URI, null);
        return Uri.withAppendedPath(CONTENT_LINK_URI, Long.toString(rowID));
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (MATCHER.match(uri)) {
            case CODE_LINK:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = LINK_ID + " = " + id;
                } else {
                    selection = selection + " AND " + LINK_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong uri: " + uri.toString());
        }
        int res = db.delete(LINKS_TABLE, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(CONTENT_LINK_URI, null);
        return res;
    }


    @Override
    public int update(Uri uri, ContentValues cv, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (MATCHER.match(uri)) {
            case CODE_LINK:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = LINK_ID + " = " + id;
                } else {
                    selection = selection + " AND " + LINK_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong uri: " + uri.toString());
        }
        int res = db.update(LINKS_TABLE, cv, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(CONTENT_LINK_URI, null);
        return res;
    }

}
