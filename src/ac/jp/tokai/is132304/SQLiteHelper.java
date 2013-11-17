package ac.jp.tokai.is132304;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper {

	static final String DATABASE_NAME = "ModelData.db";
	static final int DATABASE_VERSION = 1;

	public static final String TABLE_NAME = "ModelData";
	public static final String COL_ID = "M_ID"; // ID
	public static final String COL_W = "Width"; // W
	public static final String COL_H = "Height"; // H
	public static final String COL_D = "Depth"; // D
	public static final String COL_NOTE = "note"; // ƒƒ‚
	public static final String COL_LASTUPDATE = "lastupdate";

	protected final Context context;
	protected DatabaseHelper dbHelper;
	protected SQLiteDatabase db;

	public SQLiteHelper(Context context) {
		this.context = context;
		dbHelper = new DatabaseHelper(this.context);
	}

	//
	// SQLiteOpenHelper
	//

	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE IF EXISTS " + TABLE_NAME + " (" + COL_ID
					+ " INTEGER PRIMARY ," + COL_W + " REAL NOT NULL," + COL_H
					+ " REAL NOT NULL," + COL_D + " REAL NOT NULL," + COL_NOTE
					+ " TEXT NOT NULL," + COL_LASTUPDATE + " TEXT NOT NULL);");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			onCreate(db);
		}

	}

	//
	// Adapter Methods
	//

	public SQLiteHelper open() {
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}

	//
	// App Methods
	//

	public boolean deleteAllNotes() {
		return db.delete(TABLE_NAME, null, null) > 0;
	}

	public boolean deleteNote(int id) {
		return db.delete(TABLE_NAME, COL_ID + "=" + id, null) > 0;
	}

	public Cursor getData(int id) {
		return db.rawQuery("select * from " + TABLE_NAME + " where " + COL_ID
				+ "=" + id + ";", null);
	}

	public void saveData(int id, double W, double H, double D, String Memo) {
		ContentValues values = new ContentValues();
		values.put(COL_ID, id);
		values.put(COL_W, W);
		values.put(COL_H, H);
		values.put(COL_D, D);
		values.put(COL_NOTE, Memo);
		try {
			db.replaceOrThrow(TABLE_NAME, null, values);
		} catch (SQLException e) {
			try {
				db.insertOrThrow(TABLE_NAME, null, values);
			} catch (SQLException sqe) {
			}
		}
	}
}
