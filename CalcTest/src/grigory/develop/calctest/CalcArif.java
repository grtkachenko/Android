package grigory.develop.calctest;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CalcArif extends Activity implements OnClickListener {
	EditText num1, num2;
	Button plus, minus, mult;
	TextView result;
	Animation anim;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.arif);

		num1 = (EditText) findViewById(R.id.numArif1);
		num2 = (EditText) findViewById(R.id.numArif2);

		plus = (Button) findViewById(R.id.plus);
		minus = (Button) findViewById(R.id.minus);
		mult = (Button) findViewById(R.id.multiply);
		result = (TextView) findViewById(R.id.ansArif);

		plus.setOnClickListener(this);
		minus.setOnClickListener(this);
		mult.setOnClickListener(this);
		
		anim = AnimationUtils.loadAnimation(this, R.anim.myalpha);
		result.startAnimation(anim);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		double dnum1 = 0, dnum2 = 0, dres = 0;
		if (TextUtils.isEmpty(num1.getText().toString())
				|| TextUtils.isEmpty(num2.getText().toString())) {
			return;
		}

		dnum1 = Float.parseFloat(num1.getText().toString());
		dnum2 = Float.parseFloat(num2.getText().toString());
		if (v.getId() == R.id.plus) {
			dres = dnum1 + dnum2;
			result.setText("" + dres);
		}
		if (v.getId() == R.id.minus) {
			dres = dnum1 - dnum2;
			result.setText("" + dres);
		}
		if (v.getId() == R.id.multiply) {
			dres = dnum1 * dnum2;
			result.setText("" + dres);
		}
		
	}
}
