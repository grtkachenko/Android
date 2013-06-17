package com.ifmo.colloc;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

public class MyActivity extends Activity {
	/**
	 * Called when the activity is first created.
	 */
	Mandelbrot mandelbrot;

	@Override
	public void onResume() {
		super.onResume();
		mandelbrot.resume();
	}

	@Override
	public void onPause() {
		super.onPause();
		mandelbrot.pause();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		mandelbrot = new Mandelbrot(this, size);
		setContentView(mandelbrot);
	}

}
