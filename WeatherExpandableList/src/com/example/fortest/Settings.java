package com.example.fortest;

import android.app.ExpandableListActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

public class Settings extends ExpandableListActivity {
	/**
	 * strings for group elements
	 */
	static String arrGroupelements[] = { "City settings", "Update interval settings" };

	/**
	 * strings for child elements
	 */

	DisplayMetrics metrics;
	int width;
	ExpandableListView expList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		expList = getExpandableListView();
		metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		width = metrics.widthPixels;
		// this code for adjusting the group indicator into right side of the
		// view
		expList.setIndicatorBounds(width - GetDipsFromPixel(50), width
				- GetDipsFromPixel(10));
		expList.setAdapter(new ExpAdapter(this, this));

		expList.setOnGroupExpandListener(new OnGroupExpandListener() {
			public void onGroupExpand(int groupPosition) {
				Log.e("onGroupExpand", "OK");
			}
		});
		expList.setOnGroupCollapseListener(new OnGroupCollapseListener() {
			public void onGroupCollapse(int groupPosition) {
				Log.e("onGroupCollapse", "OK");
			}
		});

		expList.setOnChildClickListener(new OnChildClickListener() {
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				Log.e("OnChildClickListener", "OK");
				return false;
			}
		});
	}

	public int GetDipsFromPixel(float pixels) {
		// Get the screen's density scale
		final float scale = getResources().getDisplayMetrics().density;
		// Convert the dps to pixels, based on density scale
		return (int) (pixels * scale + 0.5f);
	}
}