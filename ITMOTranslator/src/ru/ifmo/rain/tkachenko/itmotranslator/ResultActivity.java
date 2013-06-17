package ru.ifmo.rain.tkachenko.itmotranslator;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.Display;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ResultActivity extends Activity implements Runnable {
	LinearLayout myGallery;
	TextView translate;
	String searchString, oldString;
	Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_picture);
		oldString = getIntent().getExtras().getString("WORD");
		searchString = new WordTranslate(getIntent().getExtras().getString(
				"WORD")).getAnswer();

		translate = (TextView) findViewById(R.id.translate);
		translate.setText(searchString.toString());

		context = getApplicationContext();
		Thread thread = new Thread(this);
		thread.start();
		ProgressBar pr = (ProgressBar) findViewById(R.id.progressBar1);
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		pr.getLayoutParams().width = size.x;
		iv = new ImageView(this);

		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this,
				R.anim.fade);
		translate.startAnimation(hyperspaceJumpAnimation);
	}

	Bitmap[] pics;

	public void run() {
		pics = new PicturesFinder(oldString).get();
		myGallery = (LinearLayout) findViewById(R.id.myGallery);
		handler.sendEmptyMessage(0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_picture, menu);
		return true;
	}

	ImageView iv;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			myGallery.removeAllViews();
			for (Bitmap cur : pics) {
				iv = new ImageView(context);
				iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
				iv.setLayoutParams(new ImageSwitcher.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				iv.setImageBitmap(cur);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						myGallery.getLayoutParams());
				if (cur != pics[pics.length - 1]) {
					lp.setMargins(0, 0, 20, 0);
				}
				myGallery.addView(iv, lp);
			}

		};
	};

}
