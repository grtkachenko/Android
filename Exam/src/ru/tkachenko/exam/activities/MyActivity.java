package ru.tkachenko.exam.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import ru.tkachenko.exam.R;
import ru.tkachenko.exam.database.DatabaseHelper;
import ru.tkachenko.exam.task_items.MyListAdapter;
import ru.tkachenko.exam.task_items.MyTask;

import java.util.ArrayList;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    final int MENU_DELETE = 1;
    final int MENU_EDIT = 2;
    private DatabaseHelper mDbHelper;

    private ListView list;
    private Button addTaskButton;
    private int pos = 0;
    private Cursor itemsCursor;
    private MyListAdapter adapter;

    private ArrayList<MyTask> listItems = new ArrayList<MyTask>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        list = (ListView) findViewById(R.id.listView);
        addTaskButton = (Button) findViewById(R.id.addTaskButton);
        adapter = new MyListAdapter(this, listItems);
        list.setAdapter(adapter);

        mDbHelper = new DatabaseHelper(this);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyActivity.this, SetTask.class);
                i.putExtra("isEdit", "0");
                startActivityForResult(i, 1);
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long id) {
                pos = position;
                MyActivity.this.openContextMenu(list);
                return true;
            }
        });

        registerForContextMenu(list);
        fillData();
    }

    private void fillData() {
        listItems.clear();
        itemsCursor = mDbHelper.fetchAllTasks();
        if (itemsCursor.moveToFirst()) {
            do {
                String title = itemsCursor.getString(itemsCursor
                        .getColumnIndex("title"));
                String task = itemsCursor.getString(itemsCursor
                        .getColumnIndex("task"));

                int priority = Integer.parseInt(itemsCursor.getString(itemsCursor
                        .getColumnIndex("priority")));
                addTask(title, task, priority);
                adapter.notifyDataSetChanged();
            } while (itemsCursor.moveToNext());
        }
        itemsCursor.close();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(1, MENU_DELETE, 0, "Delete");
        menu.add(2, MENU_EDIT, 0, "Edit");

    }

    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == MENU_EDIT) {
            Intent i = new Intent(MyActivity.this, SetTask.class);
            i.putExtra("isEdit", "1");
            i.putExtra("title", listItems.get(pos).getTitle());
            i.putExtra("task", listItems.get(pos).getTask());
            i.putExtra("priority", listItems.get(pos).getStringProirity());

            startActivityForResult(i, 1);


        } else {
            mDbHelper.deleteTask(listItems.get(pos).getTitle());
            listItems.remove(pos);
            adapter.notifyDataSetChanged();
        }
        return super.onContextItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String title = data.getStringExtra("title");
                String task = data.getStringExtra("task");
                int priority = Integer.parseInt(data.getStringExtra("priority"));

                if (data.getStringExtra("isEdit").equals("true")) {
                    mDbHelper.updateTask(data.getStringExtra("oldTitle"), title, task, data.getStringExtra("priority"));
                    fillData();
                    adapter.notifyDataSetChanged();
                } else {
                    mDbHelper.createTask(title, task, data.getStringExtra("priority"));
                    addTask(title, task, priority);
                }
            }
            if (resultCode == RESULT_CANCELED) {

            }
        }//onAcrivityResult
    }

    private void addTask(String title, String task, int priority) {
        MyTask cur = new MyTask(title, task, priority);
        listItems.add(cur);
        adapter.notifyDataSetChanged();
    }

}
