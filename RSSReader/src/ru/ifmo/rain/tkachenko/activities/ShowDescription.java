package ru.ifmo.rain.tkachenko.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import ru.ifmo.rain.tkachenko.rssreader.R;

public class ShowDescription extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_show_description);

        String descr = "", title = "";
        if (getIntent().getExtras() == null) {
            descr = "There is nothing here :(";
            title = "There is nothing here :(";
        } else {
            descr = getIntent().getExtras().getString("description");
            title = getIntent().getExtras().getString("title");
        }
        TextView tv = (TextView) findViewById(R.id.description);
        tv.setText((CharSequence) descr);
        TextView tvTitle = (TextView) findViewById(R.id.RSSTitle);
        tvTitle.setText((CharSequence) title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_show_description, menu);
        return true;
    }
}
