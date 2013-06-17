package ru.ifmo.rain.tkachenko.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.*;
import ru.ifmo.rain.tkachenko.database.LinksDbAdapter;
import ru.ifmo.rain.tkachenko.rssreader.AlarmManagerBroadcastReceiver;
import ru.ifmo.rain.tkachenko.rssreader.Link;
import ru.ifmo.rain.tkachenko.rssreader.MyListAdapter;
import ru.ifmo.rain.tkachenko.rssreader.R;

import java.util.ArrayList;
import java.util.TreeSet;

public class LinksActivity extends Activity implements OnClickListener {
    final int MENU_DELETE = 2;
    final int MENU_EDIT_LINK = 3;
    final int MENU_EDIT_TITLE_ = 4;
    final int MENU_FAVORITE = 5;

    final int EDIT_DIALOG = 1;
    private int nowTab = 0;
    private ImageButton add;
    private Button editOk;
    private TabHost tabs;
    private ImageView curStar = null;
    private ListView list;
    private CheckBox isFavoriteCheckBox = null;
    private EditText editLink, editTitle, editDialog;
    private int pos;
    private ArrayList<Link> listItems = new ArrayList<Link>();
    private MyListAdapter adapter;
    private TreeSet<String> haveTitle = new TreeSet<String>();
    private LinksDbAdapter mDbHelper;
    private Dialog dialog;
    private Cursor linksCursor;
    private static final int DELETE_ALL_ID = Menu.FIRST;
    private AlarmManagerBroadcastReceiver alarm = new AlarmManagerBroadcastReceiver();
    private boolean setDefaultURL = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_links_tabs);
//        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitle);

        tabs = (TabHost) findViewById(R.id.TabHost01);

        tabs.setup();

        TabHost.TabSpec favorTab = tabs.newTabSpec("tag1");
        favorTab.setContent(R.id.favoritesLayout);
        favorTab.setIndicator("Favorites");
        tabs.addTab(favorTab);

        TabHost.TabSpec allTab = tabs.newTabSpec("tag2");
        allTab.setContent(R.id.allLayout);
        allTab.setIndicator("All");
        tabs.addTab(allTab);

        TabHost.TabSpec addTab = tabs.newTabSpec("tag3");
        addTab.setContent(R.id.addLayout);
        addTab.setIndicator("Add link");
        tabs.addTab(addTab);
        mDbHelper = new LinksDbAdapter(this);
//        mDbHelper.dropTabe();;
        View tmp = tabs.getTabContentView();
        tuneFavorTab(tmp);
        tabs.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            public void onTabChanged(String tabId) {
                int i = tabs.getCurrentTab();
                nowTab = i;
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(addCityEditText.getWindowToken(), 0);
                if (i == 0) {
                    // city tab
                    tuneFavorTab(tabs.getTabContentView());
                } else {
                    if (i == 1) {
                        tuneAllTab(tabs.getTabContentView());
                    } else {
                        tuneAddTab(tabs.getTabContentView());
                    }
                    // time upd tab

                }

            }
        });
        alarm.setAlarm(this);

    }

    private void tuneFavorTab(View view) {
        list = (ListView) view.findViewById(R.id.listView);
        initList();
        fillData(false);
    }

    private void tuneAllTab(View view) {
        list = (ListView) view.findViewById(R.id.listViewAll);
        initList();
        fillData(true);
    }

    private void initList() {
        adapter = new MyListAdapter(this, listItems);
        list.setAdapter(adapter);

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long id) {
                pos = position;
                LinksActivity.this.openContextMenu(list);
                return true;
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent i = new Intent(LinksActivity.this, RSSActivity.class);
                String link = ((TextView) arg1.findViewById(R.id.site)).getText().toString();
                String title = ((TextView) arg1.findViewById(R.id.title)).getText().toString();

                i.putExtra("query", link);
                i.putExtra("title", title);

                startActivity(i);
            }
        });
        registerForContextMenu(list);
    }

    private void tuneAddTab(View view) {
        add = (ImageButton) view.findViewById(R.id.add);
        add.setOnClickListener(this);
        editLink = (EditText) findViewById(R.id.site);
        editTitle = (EditText) findViewById(R.id.title);
        if (setDefaultURL) {
            editLink.setText(RSSActivity.FEED_LINK);
        }
        isFavoriteCheckBox = (CheckBox) findViewById(R.id.isFavorite);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        menu.add(1, MENU_EDIT_TITLE_, 0, "Edit title");
        menu.add(2, MENU_EDIT_LINK, 0, "Edit link");

        menu.add(3, MENU_DELETE, 0, "Delete");
        View cur = ((ListView) v).getChildAt(pos);
        curStar = (ImageView) cur.findViewById(R.id.favoriteStar);


        if (curStar.getVisibility() == View.INVISIBLE) {
            menu.add(4, MENU_FAVORITE, 0, "Add to favorites");
        } else {
            menu.add(4, MENU_FAVORITE, 0, "Delete from favorites");
        }

    }

    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == MENU_EDIT_LINK || item.getItemId() == MENU_EDIT_TITLE_) {

            dialog = new Dialog(LinksActivity.this);
            dialog.setTitle("Edit link");
            dialog.setContentView(R.layout.edit_dialog);
            dialog.setCancelable(true);
            editDialog = (EditText) dialog.findViewById(R.id.editText);
            editOk = (Button) dialog.findViewById(R.id.editOk);
            if (item.getItemId() == MENU_EDIT_TITLE_) {
                dialog.setTitle("Edit title");
                editOk.setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                        if (editDialog.getText().toString().length() != 0) {
                            mDbHelper.updateTitle(listItems.get(pos).getTitle(), editDialog.getText().toString());
                            listItems.get(pos).setTitle(editDialog.getText().toString());
                            adapter.notifyDataSetChanged();
                            editDialog.setText("");
                            dialog.dismiss();
                        }
                    }
                });
            } else {
                editOk.setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                        if (editDialog.getText().toString().length() != 0) {
                            mDbHelper.updateLink(listItems.get(pos).getSite(), editDialog.getText().toString());
                            listItems.get(pos).setSite(editDialog.getText().toString());
                            adapter.notifyDataSetChanged();
                            editDialog.setText("");
                            dialog.dismiss();
                        }
                    }
                });
            }
            dialog.show();
        } else {
            if ((item.getItemId() == MENU_DELETE)) {
                mDbHelper.deleteLink(listItems.get(pos).getSite());
                listItems.remove(pos);
            } else {
                if (curStar.getVisibility() == View.INVISIBLE) {
                    mDbHelper.updateFav(listItems.get(pos).getTitle(), "true");
                    curStar.setVisibility(View.VISIBLE);
                } else {
                    mDbHelper.updateFav(listItems.get(pos).getTitle(), "false");
                    curStar.setVisibility(View.INVISIBLE);
                }
            }
            fillData(nowTab == 1);
            adapter.notifyDataSetChanged();
        }
        return super.onContextItemSelected(item);
    }

    private void fillData(boolean showFavorite) {
        haveTitle.clear();
        listItems.clear();
        linksCursor = mDbHelper.fetchAllLinks();
        if (linksCursor.moveToFirst()) {
            do {
                String data = linksCursor.getString(linksCursor
                        .getColumnIndex("url"));
                Link link = new Link(data, linksCursor.getString(linksCursor
                        .getColumnIndex("title")));
                link.setFavorite(linksCursor.getString(linksCursor
                        .getColumnIndex("fav")).equals("true") ? true : false);
                if (linksCursor.getString(linksCursor
                        .getColumnIndex("fav")).equals("false") && !showFavorite) {
                    continue;
                }
                listItems.add(link);
                haveTitle.add(link.getTitle());
                adapter.notifyDataSetChanged();
            } while (linksCursor.moveToNext());
        }
        linksCursor.close();
    }

    @SuppressWarnings("deprecation")
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.add:
                if (editTitle.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(),
                            "Type a title", Toast.LENGTH_LONG).show();
                    break;
                }
                if (editLink.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(),
                            "Type a link", Toast.LENGTH_LONG).show();
                    break;
                }

                if (haveTitle.contains(editTitle.getText().toString())) {
                    Toast.makeText(getApplicationContext(),
                            "Already have a link with this title", Toast.LENGTH_LONG).show();
                    break;
                }
                String title = editTitle.getText().toString(), s = null;
                mDbHelper.createLink(editLink.getText().toString(), editTitle.getText().toString(), isFavoriteCheckBox.isChecked() ? "true" : "false", "");
                haveTitle.add(editTitle.getText().toString());
                Link link = new Link(editLink.getText().toString(), editTitle.getText().toString());
                listItems.add(link);
                editLink.setText("");
                editTitle.setText("");
                adapter.notifyDataSetChanged();
                s = mDbHelper.getFeed(title);
                Log.i("url", s);
                break;
            default:
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    Intent intent = new Intent(this, RSSActivity.class);
                    intent.putExtra("query", ((TextView) v.findViewById(R.id.site)).getText().toString());
                    startActivity(intent);
                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(this)
                            .create();
                    alertDialog.setTitle("Oops...");
                    alertDialog.setMessage("Please, check your connection");
                    alertDialog.setButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                }
                            });
                    alertDialog.show();
                }

                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, DELETE_ALL_ID, 0, R.string.menu_delete_all);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case DELETE_ALL_ID:
                linksCursor = mDbHelper.fetchAllLinks();
                ArrayList<Long> deleteList = new ArrayList<Long>();
                if (linksCursor.moveToFirst()) {
                    do {
                        deleteList.add(linksCursor.getLong(linksCursor
                                .getColumnIndex("_id")));
                    } while (linksCursor.moveToNext());
                }
                linksCursor.close();
                for (Long i : deleteList) {
                    mDbHelper.deleteLink(i);
                }
                listItems.clear();
                fillData(nowTab == 0);
                adapter.notifyDataSetChanged();
                return true;
        }

        return super.onMenuItemSelected(featureId, item);
    }
}
