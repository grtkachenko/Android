package help;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;

public class CityContentProvider extends ContentProvider {
	private static final String DATABASE_NAME = "data";
	private static final String DATABASE_TABLE = "City";
	private static final int DATABASE_VERSION = 2;

	public static final String KEY_ROWID = "_id";
	public static final String KEY_CITY = "city";
	public static final String KEY_TEMP = "temp";
	public static final String KEY_TIME = "time";
	public static final String KEY_WEATHER = "weather";
	public static final String KEY_CONDITION = "condition";

	private static final String DB_CREATE = "create table City (_id integer primary key autoincrement, "
			+ "city text not null, temp text not null, time text not null, weather text not null, condition text not null);";

	static final String AUTHORITY = "com.example.fortest.providers.Cities";
	private Context mCtx;
	static final String CITIES_PATH = "cities";
	public static final Uri CITIES_CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + CITIES_PATH);

	static final String CITIES_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
			+ AUTHORITY + "." + CITIES_PATH;

	// одна строка
	static final String CITIES_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
			+ AUTHORITY + "." + CITIES_PATH;

	static final int URI_CITIES = 1;

	// Uri с указанным ID
	static final int URI_CITY_ID = 2;

	// описание и создание UriMatcher
	private static final UriMatcher uriMatcher;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, DATABASE_TABLE, URI_CITIES);
		uriMatcher.addURI(AUTHORITY, DATABASE_TABLE + "/#", URI_CITY_ID);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DB_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS City");
			onCreate(db);
		}
	}

	public CityContentProvider(Context ctx) {
		this.mCtx = ctx;
	}

	DatabaseHelper mDbHelper;
	SQLiteDatabase mDb;

	public void open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
	}

	public void close() {
		mDbHelper.close();
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		switch (uriMatcher.match(uri)) {
		case URI_CITIES: // общий Uri
			break;
		case URI_CITY_ID: // Uri с ID
			String id = uri.getLastPathSegment();
			// добавляем ID к условию выборки
			if (TextUtils.isEmpty(selection)) {
				selection = KEY_ROWID + " = " + id;
			} else {
				selection = selection + " AND " + KEY_ROWID + " = " + id;
			}
			break;
		default:
			throw new IllegalArgumentException("Wrong URI: " + uri);
		}
		mDb = mDbHelper.getWritableDatabase();
		Cursor cursor = mDb.query(DATABASE_TABLE, projection, selection,
				selectionArgs, null, null, sortOrder);
		// просим ContentResolver уведомлять этот курсор
		// об изменениях данных в CONTACT_CONTENT_URI
		cursor.setNotificationUri(getContext().getContentResolver(),
				CITIES_CONTENT_URI);
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
