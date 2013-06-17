package ru.startandroid.develop.onclickbuttons;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	TextView tvOut;
	Button btnOk;
	Button btnCancel;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// найдем View-элементы
		tvOut = (TextView) findViewById(R.id.tvOut);
		btnOk = (Button) findViewById(R.id.btnOk);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		OnClickListener oclBtnOk = new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tvOut.setText("Нажата кнопка ОК");
			}
		};
		OnClickListener oclBtnCancel = new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tvOut.setText("Нажата кнопка Cancel");
			}
		};
		
		btnOk.setOnClickListener(oclBtnOk);
		btnCancel.setOnClickListener(oclBtnCancel);
	}
}