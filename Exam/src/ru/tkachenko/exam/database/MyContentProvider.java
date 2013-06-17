package ru.tkachenko.exam.database;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Created with IntelliJ IDEA.
 * User: Grigory
 * Date: 1/15/13
 * Time: 9:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class MyContentProvider extends ContentProvider {
    private static final String AUTHORITY = "ru.tkachenko.exam.database";
    public static Uri CONTENT_URI = Uri.parse(ContentResolver.SCHEME_CONTENT
            + "://" + AUTHORITY);

    private static final String DATABASE_NAME = "data";
    private static final int DATABASE_VERSION = 13;
    static final String COLOR_ID = "_id";
    private static String COLOR_TABLE = "colors";
    public static Uri CONTENT_COLOR_URI = Uri.withAppendedPath(CONTENT_URI,
            COLOR_TABLE);

    private static final int CODE_COLORS = 1;
    private static final int CODE_COLOR = 2;

    private static UriMatcher MATCHER = null;

    static {
        MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        MATCHER.addURI(AUTHORITY, COLOR_TABLE, CODE_COLORS);
        MATCHER.addURI(AUTHORITY, COLOR_TABLE + "/*", CODE_COLOR);
    }

    private SQLiteOpenHelper dbHelper;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        private static final String DATABASE_CREATE = "create table colors (_id integer primary key autoincrement, "
                + "title text not null, task text not null, priority text not null);";

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists colors");
            onCreate(db);
        }

        public void dropTable() {
            this.getWritableDatabase().execSQL("drop table colors if exist");
            this.getWritableDatabase().execSQL("create table colors (_id integer primary key autoincrement, "
                    + "title text not null, task text not null, priority text not null);");
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
            case CODE_COLORS:
                break;
            case CODE_COLOR:
                break;
            default:
                throw new IllegalArgumentException("Wrong uri: " + uri.toString());
        }
        Cursor c = db.query(COLOR_TABLE, projection, selection, selectionArgs,
                null, null, null);
        c.setNotificationUri(getContext().getContentResolver(),
                CONTENT_COLOR_URI);
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
            case CODE_COLORS:
                break;
            default:
                throw new IllegalArgumentException("Wrong uri: " + uri.toString());
        }
        long rowID = db.insert(COLOR_TABLE, null, cv);
        getContext().getContentResolver().notifyChange(CONTENT_COLOR_URI, null);
        return Uri.withAppendedPath(CONTENT_COLOR_URI, Long.toString(rowID));
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (MATCHER.match(uri)) {
            case CODE_COLOR:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = COLOR_ID + " = " + id;
                } else {
                    selection = selection + " AND " + COLOR_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong uri: " + uri.toString());
        }
        int res = db.delete(COLOR_TABLE, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(CONTENT_COLOR_URI, null);
        return res;
    }


    @Override
    public int update(Uri uri, ContentValues cv, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (MATCHER.match(uri)) {
            case CODE_COLOR:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = COLOR_ID + " = " + id;
                } else {
                    selection = selection + " AND " + COLOR_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong uri: " + uri.toString());
        }
        int res = db.update(COLOR_TABLE, cv, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(CONTENT_COLOR_URI, null);
        return res;
    }
}
