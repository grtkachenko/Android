package ru.tkachenko.exam.task_items;

import android.graphics.Color;

/**
 * Created with IntelliJ IDEA.
 * User: Grigory
 * Date: 1/16/13
 * Time: 11:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class MyTask {
    private String title, task;
    private int priority;  // 0, 1, 2
    public MyTask() {

    }
    public MyTask(String title, String task, int priority) {
        this.title = title;
        this.task = task;
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }
    public String getTask() {
        return task;
    }

    public String getStringProirity() {
        return Integer.toString(priority);
    }
    public int getColorPriority() {
        if (priority == 0) {
            return Color.RED;
        }
        if (priority == 1) {
            return Color.YELLOW;
        }
        return Color.GREEN;
    }


}
