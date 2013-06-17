package ru.tkachenko.exam_helper.Helpers;

import android.graphics.Bitmap;

/**
 * Created with IntelliJ IDEA.
 * User: Grigory
 * Date: 2/16/13
 * Time: 8:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class TaskItem {
    private Bitmap bitmap;
    private String comment;

    public TaskItem(Bitmap bitmap) {
        this.bitmap = bitmap;
        this.comment = null;
    }

    public TaskItem(Bitmap bitmap, String comment) {
        this.bitmap = bitmap;
        this.comment = comment;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public String getDescription() {
        return comment;
    }
}
