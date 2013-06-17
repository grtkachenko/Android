//package com.example.weathertabssettings;
//
//import help.AlarmManagerBroadcastReceiver;
//import help.Cities;
//import help.CityDbAdapter;
//
//import java.util.ArrayList;
//import java.util.TreeSet;
//
//import android.content.Context;
//import android.content.Intent;
//import android.database.Cursor;
//import android.database.DataSetObserver;
//import android.graphics.Color;
//import android.graphics.Typeface;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.AbsListView;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.AdapterView.OnItemLongClickListener;
//import android.widget.ArrayAdapter;
//import android.widget.AutoCompleteTextView;
//import android.widget.BaseExpandableListAdapter;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.LinearLayout.LayoutParams;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.TimePicker;
//import android.widget.Toast;
//
//public class ExpAdapter extends BaseExpandableListAdapter {
//	static String arrGroupelements[] = Settings.arrGroupelements;
//	private CityDbAdapter mDbHelper;
//	private Button addCity, updSetOK;
//	private LinearLayout citySettingsLayout;
//	private TimePicker timePicker;
//	private AutoCompleteTextView editText, addCityEditText;
//	private TreeSet<String> haveCity = new TreeSet<String>();
//	private Cursor cityCursor;
//	private ArrayList<String> listItems = new ArrayList<String>();
//	private ListView list;
//	private ArrayAdapter<String> adapterList;
//	private int pos = 0;
//	private Settings settings = null;
//
//	/**
//	 * strings for child elements
//	 */
//	private void tuneCitySettings(View tmp) {
//		addCity = (Button) tmp.findViewById(R.id.addCity);
//		citySettingsLayout = (LinearLayout) tmp
//				.findViewById(R.id.citySettingsLayout);
//
//		addCityEditText = (AutoCompleteTextView) tmp
//				.findViewById(R.id.addCityEditText);
//		addCity.setOnClickListener(new OnClickListener() {
//
//			public void onClick(View v) {
//				// onKeyDown(KeyEvent.KEYCODE_BACK, new KeyEvent());
//				if (haveCity.size() == 0) {
//					Toast.makeText(myContext,
//							"Please choose at least one city",
//							Toast.LENGTH_LONG).show();
//				} else {
//					Intent returnIntent = new Intent();
//					String[] data = new String[haveCity.size()];
//					int num = 0;
//					cityCursor = mDbHelper.fetchAllCity();
//					if (cityCursor.moveToFirst()) {
//						do {
//							data[num++] = cityCursor.getString(cityCursor
//									.getColumnIndex("city"));
//						} while (cityCursor.moveToNext());
//					}
//					cityCursor.close();
//					returnIntent.putExtra("data", data);
//					// setResult(1, returnIntent);
//					// finish();
//				}
//			}
//		});
//		ArrayAdapter<String> adapter = new ArrayAdapter<String>(myContext,
//				android.R.layout.simple_dropdown_item_1line, Cities.CITIES);
//		addCityEditText.setAdapter(adapter);
//		addCityEditText.setOnItemClickListener(new OnItemClickListener() {
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				if (!haveCity.contains(arg0.getItemAtPosition(arg2).toString())) {
//					addCityEditText.setText(arg0.getItemAtPosition(arg2)
//							.toString());
//					String cur = arg0.getItemAtPosition(arg2).toString();
//
//					clickAdd(addCity, true);
//					haveCity.add(cur);
//					addCityEditText.setText("");
//
//				} else {
//					addCityEditText.setText("");
//				}
//
//			}
//
//		});
//
//		list = (ListView) tmp.findViewById(R.id.listView);
//		adapterList = new ArrayAdapter<String>(myContext,
//				android.R.layout.simple_list_item_1, listItems);
//
//		list.setAdapter(adapterList);
//		list.setOnItemLongClickListener(new OnItemLongClickListener() {
//
//			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
//					int position, long id) {
//				pos = position;
//				settings.openContextMenu(list);
//				return true;
//			}
//		});
//
//		settings.registerForContextMenu(list);
//		mDbHelper = new CityDbAdapter(myContext);
//		mDbHelper.open();
//		fillData();
//	}
//
//
//	private void fillData() {
//		haveCity.clear();
//
//		adapterList.clear();
//		cityCursor = mDbHelper.fetchAllCity();
//		if (cityCursor.moveToFirst()) {
//			do {
//				String data = cityCursor.getString(cityCursor
//						.getColumnIndex("city"));
//				if (haveCity.contains(data)) {
//					continue;
//				}
//				addCityEditText.setText(data);
//				clickAdd(addCity, false);
//				haveCity.add(data);
//
//			} while (cityCursor.moveToNext());
//		}
//		// for (int i = 0; i < adapterList.getCount() + 1; i++) {
//		// citySettingsLayout.addView(new TextView(myContext), 2);
//		// }
//		adapterList.notifyDataSetChanged();
//		cityCursor.close();
//	}
//
//	public void clickAdd(View v, boolean addToDb) {
//		switch (v.getId()) {
//		case R.id.addCity:
//
//			TextView tv = new TextView(myContext);
//			String cur = addCityEditText.getText().toString();
//			if (haveCity.contains(cur)) {
//				break;
//			}
//			//
//			tv.setText(cur);
//			// Typeface tf = Typeface.createFromAsset(getAssets(),
//			// "fonts/oblique.ttf");
//			// tv.setTypeface(tf);
//			tv.setTextSize(30);
//			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//			lp.setMargins(0, 15, 0, 0); // llp.setMargins(left,
//			tv.setLayoutParams(lp);
//			tv.setTextColor(Color.BLACK);
//			addCityEditText.setText("");
//			if (addToDb) {
//				mDbHelper.createCity(tv.getText().toString(), "",
//				// "7 $ 1 2 3 4 5 1 2 3 4 5 1 2 3 4 5 1 2 3 4 5 1 2 3 4 5 1 2 3 4 5 1 2 3 4 5 1 2 3 4 5 1 2 3 4 5 1 2 3 4 5 1 2 3 4 5 1 2 3 4 5 1 2 3 4 5 1 2 3 4 5 1 2 3 4 5 1 2 3 4 5 1 2 3 4 5 1 2 3 4 5 1 2 3 4 5 1 2 3 4 5 $ 1/5",
//						"", "", "");
//
//			}
//			adapterList.add(tv.getText().toString());
//
//			settings.registerForContextMenu(tv);
//			notifyDataSetChanged();
//			adapterList.notifyDataSetChanged();
//			break;
//
//		default:
//			break;
//		}
//	}
//
//	private void tuneTimeSettings(View tmp) {
//		timePicker = (TimePicker) tmp.findViewById(R.id.timePicker);
//		timePicker.setIs24HourView(true);
//		timePicker
//				.setCurrentHour(AlarmManagerBroadcastReceiver.updateTime / 1000 / 60 / 60);
//
//		timePicker
//				.setCurrentMinute((AlarmManagerBroadcastReceiver.updateTime - AlarmManagerBroadcastReceiver.updateTime
//						/ 1000 / 60 / 60 * (1000 * 60 * 60)) / 1000 / 60);
//
//		updSetOK = (Button) tmp.findViewById(R.id.updSetOk);
//
//		updSetOK.setOnClickListener(new OnClickListener() {
//
//			public void onClick(View v) {
//				AlarmManagerBroadcastReceiver.updateTime = (timePicker
//						.getCurrentHour() * 60 + timePicker.getCurrentMinute()) * 60 * 1000 + 500;
//			}
//		});
//	}
//
//	private Context myContext;
//
//	public ExpAdapter(Context context, Settings settings) {
//		myContext = context;
//		this.settings = settings;
//	}
//
//	public Object getChild(int groupPosition, int childPosition) {
//		return null;
//	}
//
//	public long getChildId(int groupPosition, int childPosition) {
//		return 0;
//	}
//
//	public View getChildView(int groupPosition, int childPosition,
//			boolean isLastChild, View convertView, ViewGroup parent) {
//		View tmp = convertView;
//		LayoutInflater inflater = (LayoutInflater) myContext
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		if (groupPosition == 0) {
//			tmp = inflater.inflate(R.layout.first_part_of_list, null);
//			tuneCitySettings(tmp);
//		} else {
//			tmp = inflater.inflate(R.layout.second_part_of_list, null);
//			TimePicker timePicker = (TimePicker) tmp
//					.findViewById(R.id.timePicker);
//			timePicker.setIs24HourView(true);
//			tuneTimeSettings(tmp);
//		}
//
//		convertView = tmp;
//		return convertView;
//	}
//
//	public int getChildrenCount(int groupPosition) {
//		return 1;
//	}
//
//	public Object getGroup(int groupPosition) {
//		return null;
//	}
//
//	public int getGroupCount() {
//		return arrGroupelements.length;
//	}
//
//	public long getGroupId(int groupPosition) {
//		return 0;
//	}
//
//	public View getGroupView(int groupPosition, boolean isExpanded,
//			View convertView, ViewGroup parent) {
//
//		if (convertView == null) {
//			LayoutInflater inflater = (LayoutInflater) myContext
//					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			convertView = inflater.inflate(R.layout.group_row, null);
//		}
//
//		TextView tvGroupName = (TextView) convertView
//				.findViewById(R.id.tvGroupName);
//		tvGroupName.setTextSize(20);
//		tvGroupName.setTypeface(null, Typeface.ITALIC);
//		tvGroupName.setText(arrGroupelements[groupPosition]);
//
//		return convertView;
//	}
//
//	public boolean hasStableIds() {
//		return false;
//	}
//
//	public boolean isChildSelectable(int groupPosition, int childPosition) {
//		return true;
//	}
//
//}