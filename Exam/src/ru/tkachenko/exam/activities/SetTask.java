package ru.tkachenko.exam.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import ru.tkachenko.exam.R;

/**
 * Created with IntelliJ IDEA.
 * User: Grigory
 * Date: 1/16/13
 * Time: 11:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class SetTask extends Activity implements View.OnClickListener {

    private Button confirm;
    private String oldTitle = "";
    private RadioButton checkLow, checkMiddle, checkHigh;
    private EditText editTitle, editTask;
    private int curPriority = 2;
    private boolean isEdit = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_task);
        confirm = (Button) findViewById(R.id.confirmButton);
        editTitle = (EditText) findViewById(R.id.editTitle);
        editTask = (EditText) findViewById(R.id.editTask);

        checkLow = (RadioButton) findViewById(R.id.lowCheckBox);
        checkMiddle = (RadioButton) findViewById(R.id.middleCheckBox);
        checkHigh = (RadioButton) findViewById(R.id.highCheckBox);


        if (getIntent().getStringExtra("isEdit").equals("1")) {
            isEdit = true;
            editTask.setText(getIntent().getStringExtra("task"));
            oldTitle = getIntent().getStringExtra("title");
            editTitle.setText(getIntent().getStringExtra("title"));
            setPriority(getIntent().getStringExtra("priority"));
        }

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTitle.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(),
                            "Type non-empty title", Toast.LENGTH_LONG).show();

                } else {
                    if (editTask.getText().toString().length() == 0) {
                        Toast.makeText(getApplicationContext(),
                                "Type non-empty text", Toast.LENGTH_LONG).show();
                    } else {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("title", editTitle.getText().toString());
                        returnIntent.putExtra("task", editTask.getText().toString());
                        returnIntent.putExtra("priority", Integer.toString(curPriority));
                        returnIntent.putExtra("isEdit", isEdit ? "true" : "false");
                        returnIntent.putExtra("oldTitle", oldTitle);



                        setResult(RESULT_OK, returnIntent);
                        finish();

                    }
                }

            }
        });
        checkLow.setClickable(false);
        checkMiddle.setClickable(false);
        checkHigh.setClickable(false);

        checkLow.setOnClickListener(this);
        checkMiddle.setOnClickListener(this);
        checkHigh.setOnClickListener(this);
    }

    private void setPriority(String p) {
        if (p.equals("0")) {
            onClick(checkLow);
        }
        if (p.equals("1")) {
            onClick(checkMiddle);
        }
        if (p.equals("2")) {
            onClick(checkHigh);
        }


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.lowCheckBox) {
            curPriority = 0;
            notClicledCheckBoxes(checkMiddle, checkHigh);
            checkLow.setChecked(true);
            return;
        }
        if (v.getId() == R.id.middleCheckBox) {
            curPriority = 1;
            notClicledCheckBoxes(checkLow, checkHigh);
            checkMiddle.setChecked(true);
            return;
        }
        if (v.getId() == R.id.highCheckBox) {
            curPriority = 2;
            notClicledCheckBoxes(checkLow,checkMiddle);
            checkHigh.setChecked(true);
            return;
        }

    }

    private void notClicledCheckBoxes(RadioButton b1, RadioButton b2) {
        b1.setChecked(false);
        b2.setChecked(false);
    }
}