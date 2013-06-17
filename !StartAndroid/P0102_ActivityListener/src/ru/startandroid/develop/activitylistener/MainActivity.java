package ru.startandroid.develop.activitylistener;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
	TextView tvOut;
	Button btnOk;
	Button btnCancel;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		tvOut = (TextView) findViewById(R.id.tvOut);
		btnOk = (Button) findViewById(R.id.btnOk);
		btnCancel = (Button) findViewById(R.id.btnCancel);

		btnOk.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.btnOk) {
			tvOut.setText("Нажата кнопка ОК");
		} else {
			tvOut.setText("Нажата кнопка Cancel");
		}
	}
}