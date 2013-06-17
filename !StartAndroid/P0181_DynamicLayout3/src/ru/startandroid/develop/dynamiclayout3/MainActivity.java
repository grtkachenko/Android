package ru.startandroid.develop.dynamiclayout3;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity extends Activity implements OnSeekBarChangeListener {

	SeekBar sbWeight;
	Button btn1;
	Button btn2;

	LinearLayout.LayoutParams lParams1;
	LinearLayout.LayoutParams lParams2;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		sbWeight = (SeekBar) findViewById(R.id.sbWeight);
		sbWeight.setOnSeekBarChangeListener(this);

		btn1 = (Button) findViewById(R.id.btn1);
		btn2 = (Button) findViewById(R.id.btn2);

		lParams1 = (LinearLayout.LayoutParams) btn1.getLayoutParams();
		lParams2 = (LinearLayout.LayoutParams) btn2.getLayoutParams();
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		int leftValue = progress;
		int rightValue = seekBar.getMax() - progress;
		// настраиваем вес
		lParams1.weight = leftValue;
		lParams2.weight = rightValue;
		// в текст кнопок пишем значения переменных
		btn1.setText(String.valueOf(leftValue));
		btn2.setText(String.valueOf(rightValue));
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}
}