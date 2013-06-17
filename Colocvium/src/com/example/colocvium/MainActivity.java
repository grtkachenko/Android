package com.example.colocvium;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

@SuppressLint("NewApi")
public class MainActivity extends Activity {
	/**
	 * Called when the activity is first created.
	 */
	private LinearLayout data;
	private Life life;
	private Button start;
	// 0 - start, 1 - pause
	private int condition = 0;

	@Override
	public void onResume() {
		super.onResume();
		life.resume();
	}

	@Override
	public void onPause() {
		super.onPause();
		life.pause();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		data = (LinearLayout)findViewById(R.id.data);
		life = (Life) findViewById(R.id.life);
		start = (Button) findViewById(R.id.start);
		start.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				condition++;
				condition %= 2;
				start.setText(condition == 0 ? "Pause" : "Start");
				if (condition == 1) {
					life.pause();
				} else {
					life.resume();
				}
				
			}
		});
	}

}
