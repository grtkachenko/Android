package ru.startandroid.develop.p0261intentfilter;

import java.sql.Date;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ActivityTime extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.time);

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String time = sdf.format(new Date(System.currentTimeMillis()));

		TextView tvTime = (TextView) findViewById(R.id.tvTime);
		tvTime.setText(time);
	}
}