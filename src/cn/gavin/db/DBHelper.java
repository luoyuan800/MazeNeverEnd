package cn.gavin.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;

import cn.gavin.activity.MainGameActivity;

/**
 * Created by gluo on 9/14/2015.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static String DB_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/maze/data/";
    private static String DB_NAME = "mazeNeverEnd";
    private static int DB_VERSION = 8;

    private Context context;
    private SQLiteDatabase database;

    public DBHelper(Context context) {
        super(context, DB_PATH + DB_NAME, null, DB_VERSION);
        File path = new File(DB_PATH);
        if (!path.exists()) {
            path.mkdirs();
        }
        this.context = context;
        getDB();
    }

    private boolean checkDBExist() {
        SQLiteDatabase database = null;
        try {
            database = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        if (database != null) {
            database.close();
            return true;
        } else {
            return false;
        }
    }

    public SQLiteDatabase openDB() {
        SQLiteDatabase database = null;
        try {
            database = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        if (database == null) {
            File dir = new File(DB_PATH);
            if (!dir.exists()) dir.mkdirs();
            File dbf = new File(DB_PATH + DB_NAME);
            if (dbf.exists()) dbf.delete();
            database = SQLiteDatabase.openOrCreateDatabase(dbf, null);
        }
        return database;
    }

    public Cursor excuseSOL(String sql) {
        Cursor cursor = getDB().rawQuery(sql, null);
        cursor.moveToFirst();
        return cursor;
    }

    public void excuseSQLWithoutResult(String sql) {
        getDB().execSQL(sql);
    }

    private SQLiteDatabase openOrCreateInnerDB() {
        database = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        if (database.getVersion() == 0) {
            onCreate(database);
        }
        if (database.getVersion() < DB_VERSION) {
            onUpgrade(database, database.getVersion(), DB_VERSION);
        }
        database.setVersion(DB_VERSION);
        return database;
    }

    private SQLiteDatabase getDB() {
        SQLiteDatabase db = database;
        if (db == null || !db.isOpen()) {
            try {
                db = getWritableDatabase();
            } catch (Exception e) {
                db = openOrCreateInnerDB();
            }
        }
        return db;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            String createTable = "CREATE TABLE monster_book(" +
                    "name TEXT NOT NULL," +
                    "format_name TEXT," +
                    "isDefeat CHAR(50) NOT NULL," +
                    "hp TEXT," +
                    "atk TEXT," +
                    "maze_lv INTEGER," +
                    "hp1 TEXT," +
                    "atk1 TEXT," +
                    "maze_lv1 INTEGER," +
                    "count TEXT" +
                    ")";

            db.execSQL(createTable);
            createTable = "CREATE TABLE skill(" +
                    "name TEXT NOT NULL PRIMARY KEY," +
                    "is_active CHAR(50)," +
                    "is_on_use CHAR(5)," +
                    "probability TEXT," +
                    "count TEXT," +
                    "base_harm TEXT," +
                    "addition_harm TEXT" +
                    ")";
            db.execSQL(createTable);
            db.execSQL("CREATE UNIQUE INDEX monster_index ON monster_book (name)");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(MainGameActivity.TAG, "CreateTable", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private static DBHelper dbHelper;

    public static void init(Context context) {
        dbHelper = new DBHelper(context);
    }

    public static DBHelper getDbHelper() {
        return dbHelper;
    }

    public void beginTransaction() {
        getDB().beginTransaction();
    }

    public void markTransactionSuccess() {
        getDB().setTransactionSuccessful();
    }

    public void endTransaction(){
        markTransactionSuccess();
        getDB().endTransaction();
    }
}
