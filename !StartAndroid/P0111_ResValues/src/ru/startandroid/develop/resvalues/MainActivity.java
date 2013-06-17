package ru.startandroid.develop.resvalues;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		LinearLayout llBottom = (LinearLayout) findViewById(R.id.llBottom);
		TextView tvBottom = (TextView) findViewById(R.id.tvBottom);
		Button btnBottom = (Button) findViewById(R.id.btnBottom);

		llBottom.setBackgroundResource(R.color.llBottomColor);
		tvBottom.setText(R.string.tvBottomText);
		btnBottom.setText(R.string.btnBottomText);
		// getResources().getString(R.string.tvBottomText);
		LinearLayout llTop = (LinearLayout) findViewById(R.id.llTop);
		TextView tvTop = (TextView) findViewById(R.id.tvTop);
		Button btnTop = (Button) findViewById(R.id.btnTop);

		llTop.setBackgroundResource(R.color.llTopColor);
		tvTop.setText(R.string.tvTopText);
		btnTop.setText(R.string.btnTopText);
	}
}