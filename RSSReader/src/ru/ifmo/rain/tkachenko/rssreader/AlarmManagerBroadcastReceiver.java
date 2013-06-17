package ru.ifmo.rain.tkachenko.rssreader;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import ru.ifmo.rain.tkachenko.database.LinksDbAdapter;

/**
 * Created with IntelliJ IDEA.
 * User: Grigory
 * Date: 12/26/12
 * Time: 5:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {
    private static int updateTime = 1000 * 60 ;
    private Context context;
    private boolean isCreated = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context
                .getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();

        doMyUpdate(context);
        wl.release();
    }

    public void setAlarm(Context context) {
        if (isCreated) {
            return;
        }
        isCreated = true;
        this.context = context;
        AlarmManager am = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                updateTime, pi); // Millisec * Second * Minute
    }

    public void setAlarmTrue(Context context) {
        this.context = context;
        AlarmManager am = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                updateTime, pi); // Millisec * Second * Minute
    }

    public void cancelAlarm(Context context) {
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent sender = PendingIntent
                .getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    public void setUpdateTimeAndStart(int time) {
        AlarmManagerBroadcastReceiver.updateTime = time;
        cancelAlarm(context);
        setAlarmTrue(context);
    }

    public void setUpdateTime(int time) {
        AlarmManagerBroadcastReceiver.updateTime = time;
    }

    public int getUpdateTime() {
        return updateTime;
    }


    private void doMyUpdate(Context context) {
        LinksDbAdapter help = new LinksDbAdapter(context);
        help.updateAllFeeds();
    }
}
