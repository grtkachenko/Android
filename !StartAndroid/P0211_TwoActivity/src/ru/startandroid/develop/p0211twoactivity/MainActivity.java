package ru.startandroid.develop.p0211twoactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

	Button btnActTwo;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		btnActTwo = (Button) findViewById(R.id.btnActTwo);
		btnActTwo.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnActTwo:
			// TODO Call second activity
			Intent intent = new Intent(this, ActivityTwo.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
}