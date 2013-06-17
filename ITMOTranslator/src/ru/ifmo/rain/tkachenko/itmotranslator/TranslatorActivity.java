package ru.ifmo.rain.tkachenko.itmotranslator;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TranslatorActivity extends Activity implements OnClickListener {
	Button button;
	EditText field;

	Intent intent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_translator);
		button = (Button) findViewById(R.id.translateButton);
		field = (EditText) findViewById(R.id.wordField);
		button.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_translator, menu);
		return true;
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null;
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.translateButton && isNetworkAvailable()) {
			intent = new Intent(this, ResultActivity.class);
			intent.putExtra("WORD", field.getText().toString());
			startActivity(intent);
		} else {
			Toast.makeText(this, "Check your connection", Toast.LENGTH_SHORT)
			.show();
		}
	}
}
