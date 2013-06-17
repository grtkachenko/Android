package ru.startandroid.develop.contextmenu;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
	final int MENU_COLOR_RED = 1;
	final int MENU_COLOR_GREEN = 2;
	final int MENU_COLOR_BLUE = 3;

	final int MENU_SIZE_22 = 4;
	final int MENU_SIZE_26 = 5;
	final int MENU_SIZE_30 = 6;
	TextView tvColor, tvSize;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		tvColor = (TextView) findViewById(R.id.tvColor);
		tvSize = (TextView) findViewById(R.id.tvSize);

		// ��� tvColor � tvSize ���������� ��������� ����������� ����
		registerForContextMenu(tvColor);
		registerForContextMenu(tvSize);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tvColor:
			menu.add(0, MENU_COLOR_RED, 0, "Red");
			menu.add(0, MENU_COLOR_GREEN, 0, "Green");
			menu.add(0, MENU_COLOR_BLUE, 0, "Blue");
			break;
		case R.id.tvSize:
			menu.add(0, MENU_SIZE_22, 0, "22");
			menu.add(0, MENU_SIZE_26, 0, "26");
			menu.add(0, MENU_SIZE_30, 0, "30");
			break;
		}
	}

	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		// ������ ���� ��� tvColor
		case MENU_COLOR_RED:
			tvColor.setTextColor(Color.RED);
			tvColor.setText("Text color = red");
			break;
		case MENU_COLOR_GREEN:
			tvColor.setTextColor(Color.GREEN);
			tvColor.setText("Text color = green");
			break;
		case MENU_COLOR_BLUE:
			tvColor.setTextColor(Color.BLUE);
			tvColor.setText("Text color = blue");
			break;
		// ������ ���� ��� tvSize
		case MENU_SIZE_22:
			tvSize.setTextSize(22);
			tvSize.setText("Text size = 22");
			break;
		case MENU_SIZE_26:
			tvSize.setTextSize(26);
			tvSize.setText("Text size = 26");
			break;
		case MENU_SIZE_30:
			tvSize.setTextSize(30);
			tvSize.setText("Text size = 30");
			break;
		}
		return super.onContextItemSelected(item);
	}
}