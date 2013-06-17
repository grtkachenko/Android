package ru.ifmo.rain.tkachenko.picdecrease;

import android.os.Bundle;
import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

public class PicDecrease extends Activity {
	Life pic;

	@Override
	public void onResume() {
		super.onResume();
		pic.resume();
		pic.drawPicture();
	}

	@Override
	public void onPause() {
		super.onPause();
		pic.pause();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_pic_decrease);
		pic = (Life) findViewById(R.id.picture);

	}

}
