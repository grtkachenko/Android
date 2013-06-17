package ru.tkachenko.exam.database;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: Grigory
 * Date: 1/15/13
 * Time: 9:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class DatabaseHelper {
    public static final String KEY_TITLE = "title";
    public static final String KEY_TASK = "task";
    public static final String KEY_PRIORITY = "priority";


    private Cursor colorCursor;
    private Context myContext;

    public DatabaseHelper(Context ctx) {
        this.myContext = ctx;

    }


    public void createTask(String title, String task, String priority) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_TASK, task);
        initialValues.put(KEY_PRIORITY, priority);


        myContext.getContentResolver().insert(
                MyContentProvider.CONTENT_COLOR_URI, initialValues);
    }

    public void deleteTask(long rowId) {
        Uri uri = ContentUris.withAppendedId(
                MyContentProvider.CONTENT_COLOR_URI, rowId);
        myContext.getContentResolver().delete(uri, null, null);
    }

    public void deleteTask(String title) {
        deleteTask(getIdByName(title));
    }

    public Cursor fetchAllTasks() {
        return myContext.getContentResolver().query(
                MyContentProvider.CONTENT_COLOR_URI, null, null, null, null);
    }


    public void updateTask(long rowId, String title, String task, String priority) {
        ContentValues args = new ContentValues();
        args.put(KEY_TITLE, title);
        args.put(KEY_TASK, task);
        args.put(KEY_PRIORITY, priority);


        Uri uri = Uri.withAppendedPath(MyContentProvider.CONTENT_COLOR_URI,
                Long.toString(rowId));
        myContext.getContentResolver().update(uri, args, null, null);
    }

    public void updateTask(String oldTitle, String title, String task, String priority) {
        updateTask(getIdByName(oldTitle), title, task, priority);
    }


    private long getIdByName(String name) {
        colorCursor = this.fetchAllTasks();
        long id = 0;
        if (colorCursor.moveToFirst()) {
            do {
                String cur = colorCursor.getString(colorCursor
                        .getColumnIndex("title"));

                if (cur.equals(name)) {
                    return colorCursor.getLong(colorCursor.getColumnIndex("_id"));
                }
            } while (colorCursor.moveToNext());
        }
        return 0;
    }
}
