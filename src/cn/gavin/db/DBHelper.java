package cn.gavin.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import cn.gavin.activity.MainGameActivity;

import java.io.File;

/**
 * Created by gluo on 9/14/2015.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static String DB_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/maze/data/";
    private static String DB_NAME = "mazeNeverEnd";
    private static int DB_VERSION = 1;

    private MainGameActivity context;
    private SQLiteDatabase database;

    public DBHelper(MainGameActivity context) {
        super(context, DB_PATH + DB_NAME, null, DB_VERSION);
        File path = new File(DB_PATH);
        if (!path.exists()) {
            path.mkdirs();
        }
        this.context = context;
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
        database = context.openOrCreateDatabase("monster_book", Context.MODE_PRIVATE, null);
        return database;
    }

    private SQLiteDatabase getDB() {
        SQLiteDatabase db = database;
        if (database == null) {
            try {
                db = getWritableDatabase();
            } catch (Exception e) {
                if (database == null || !database.isOpen()) {
                    db = openOrCreateInnerDB();
                    onCreate(db);
                } else {
                    return database;
                }
            }
        }else{
            if(!db.isOpen()){
                db = openOrCreateInnerDB();
            }
        }
        return db;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE monster_book(" +
                "name TEXT NOT NULL," +
                "format_name TEXT," +
                "isDefeat CHAR(50) NOT NULL," +
                "hp TEXT," +
                "atk TEXT," +
                "maze_lv INTEGER," +
                "hp1 TEXT," +
                "atk1 TEXT," +
                "maze_lv1 INTEGER" +
                ")";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
