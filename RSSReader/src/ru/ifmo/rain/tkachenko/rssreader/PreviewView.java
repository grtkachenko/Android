package ru.ifmo.rain.tkachenko.rssreader;

import ru.ifmo.rain.tkachenko.activities.RSSActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PreviewView extends LinearLayout {
	private Context context;
	private LinearLayout dataLayout;
	public String date;
	private RSSItem rss;
	
	public PreviewView(Context context, RSSItem rss) {
		super(context);
		this.rss = rss;
		init(context);
		
		// TODO Auto-generated constructor stub
	}

	public PreviewView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
		// TODO Auto-generated constructor stub
	}

	public PreviewView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
		// TODO Auto-generated constructor stub
	}

	private void init(Context context) {
		this.context = context;
		this.setClickable(true);
		this.setOrientation(LinearLayout.VERTICAL);
		dataLayout = new LinearLayout(context);
		dataLayout.setOrientation(LinearLayout.VERTICAL);

		Line line = new Line(context);
		this.addView(dataLayout);
		this.addView(line);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.gravity = 0;
		lp.setMargins(0, 0, 0, 10);
		line.setLayoutParams(lp);
	}

	public class Line extends ImageView {

		public Line(Context context) {
			super(context);
			Bitmap bitmap = Bitmap.createBitmap(RSSActivity.screenWidth * 3 / 4, 3, Config.ARGB_8888);
			bitmap.eraseColor(Color.LTGRAY);
			this.setImageBitmap(bitmap);
		}

	}

	public void addTitle(String title) {
		TextView titleView = new TextView(context);
		titleView.setTextSize(24);
		titleView.setTextColor(Color.BLACK);
		titleView.setText((CharSequence) (title));
		dataLayout.addView(titleView);
	}

	public void addDate(String date) {
		this.date = date;
		TextView dateView = new TextView(context);
		dateView.setText((CharSequence) date);
		dateView.setTextColor(Color.BLACK);
		dataLayout.addView(dateView);
	}


    public String getDescription() {
        return rss.getDescription();
    }

    public String getTitle() {
        return rss.getTitle();
    }


}
