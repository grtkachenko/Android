package grigory.develop.calctest;

import android.app.Activity;
import android.content.Intent;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class CalcTestActivity extends Activity implements OnClickListener {
	Button btnTrig, btnArif, btnOther;
	CheckBox isExtMenu;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		btnTrig = (Button) findViewById(R.id.btnTrig);
		btnArif = (Button) findViewById(R.id.btnArif);
		btnOther = (Button) findViewById(R.id.btnOther);
		isExtMenu = (CheckBox) findViewById(R.id.extMenu);

		btnTrig.setOnClickListener(this);
		btnArif.setOnClickListener(this);
		btnOther.setOnClickListener(this);
		isExtMenu.setOnClickListener(this);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		// добавляем пункты меню
		menu.add(0, 1, 0, "О программе");
		menu.add(1, 2, 1, "Выход");
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		// пункты меню с ID группы = 1 видны, если в CheckBox стоит галка
		menu.setGroupVisible(1, isExtMenu.isChecked());
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == 1) {
			Toast.makeText(this, "Just for fun programm :) ", Toast.LENGTH_LONG)
					.show();
		} else {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.btnArif) {
			Intent intent = new Intent(this, CalcArif.class);
			startActivity(intent);
			return;
		}
		if (v.getId() == R.id.btnTrig) {
			Intent intent = new Intent(this, CalcTrig.class);
			startActivity(intent);
			return;
		}
		if (v.getId() == R.id.btnOther) {
			Intent intent = new Intent(this, CalcOther.class);
			startActivity(intent);
			return;
		}
	}
}