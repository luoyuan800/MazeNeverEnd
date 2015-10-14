package cn.gavin.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.forge.ForgeDB;
import cn.gavin.log.LogHelper;
import cn.gavin.monster.Defender;
import cn.gavin.monster.PalaceMonster;

/**
 * Created by gluo on 9/14/2015.
 */
public class DBHelper {
    private static String DB_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/maze/data/demon";
    public final static String DB_NAME = "mazeNeverEnd";
    private static int DB_VERSION_1_2_1 = 9;
    private static int DB_VERSION_1_3_1 = 10;
    private static int DB_VERSION = 11;

    private Context context;
    private SQLiteDatabase database;

    public DBHelper(Context context) {
        this.context = context;
        getDB();
    }

    public Cursor excuseSOL(String sql) {
        Cursor cursor = getDB().rawQuery(sql, null);
        cursor.moveToFirst();
        return cursor;
    }

    public void excuseSQLWithoutResult(String sql) {
        try {
            getDB().execSQL(sql);
        } catch (Exception e) {
            Log.e("DB", "DB ERROR", e);
            e.printStackTrace();
        }
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

    private synchronized SQLiteDatabase getDB() {
        SQLiteDatabase db = database;
        if (db == null || !db.isOpen()) {
            db = openOrCreateInnerDB();
        }
        return db;
    }

    public void onCreate(SQLiteDatabase db) {
        try {
            db.beginTransaction();
            String createTable = "CREATE TABLE monster(" +
                    "name TEXT NOT NULL PRIMARY KEY," +
                    "max_hp_name TEXT," +
                    "max_atk_name TEXT," +
                    "max_hp_defeat CHAR(5)," +
                    "max_atk_defeat CHAR(5)," +
                    "max_atk_atk TEXT," +
                    "max_hp_atk TEXT," +
                    "max_hp_hp TEXT," +
                    "max_atk_hp TEXT," +
                    "max_atk_lev TEXT," +
                    "max_atk_battle TEXT," +
                    "max_hp_battle TEXT," +
                    "max_hp_lev TEXT," +
                    "defeat TEXT," +
                    "defeated TEXT" +
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
            db.execSQL("CREATE UNIQUE INDEX monster_index ON monster (name)");
            db.execSQL("CREATE TABLE hero(" +
                    "name TEXT NOT NULL PRIMARY KEY," +
                    "lev TEXT NOT NULL, " +
                    "hp TEXT NOT NULL, " +
                    "skill TEXT NOT NULL, " +
                    "skill_lev TEXT NOT NULL, " +
                    "atk TEXT NOT NULL");
            db.execSQL("CREATE UNIQUE INDEX maze_lev ON hero (name, lev)");
            ForgeDB forgeDB = new ForgeDB();
            forgeDB.createTable(db);
            PalaceMonster.createDB(db);
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(MainGameActivity.TAG, "CreateTable", e);
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            if (oldVersion == 8) {
                String createTable = "CREATE TABLE monster(" +
                        "name TEXT NOT NULL PRIMARY KEY," +
                        "max_hp_name TEXT," +
                        "max_atk_name TEXT," +
                        "max_hp_defeat CHAR(5)," +
                        "max_atk_defeat CHAR(5)," +
                        "max_atk_atk TEXT," +
                        "max_hp_atk TEXT," +
                        "max_hp_hp TEXT," +
                        "max_atk_hp TEXT," +
                        "max_atk_lev TEXT," +
                        "max_atk_battle TEXT," +
                        "max_hp_battle TEXT," +
                        "max_hp_lev TEXT," +
                        "defeat TEXT," +
                        "defeated TEXT" +
                        ")";
                db.execSQL(createTable);
            }
        }catch(Exception e){
            Log.e("MazeNeverEnd",e.getMessage());
            LogHelper.writeLog();
            e.printStackTrace();
        }

        try {
            if (oldVersion == 10) {
                new ForgeDB().upgradeTo1_3_2(db);
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.e("MazeNeverEnd",e.getMessage());
            LogHelper.writeLog();
        }
        try{
            if(oldVersion==11){
                db.execSQL("CREATE TABLE hero(" +
                        "name TEXT NOT NULL PRIMARY KEY," +
                        "lev TEXT NOT NULL, " +
                        "hp TEXT NOT NULL, " +
                        "skill TEXT NOT NULL, " +
                        "skill_lev TEXT NOT NULL, " +
                        "atk TEXT NOT NULL");
                db.execSQL("CREATE UNIQUE INDEX maze_lev ON hero (name, lev)");
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.e("MazeNeverEnd",e.getMessage());
            LogHelper.writeLog();
        }
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

    public void endTransaction() {
        markTransactionSuccess();
        getDB().endTransaction();
    }

    public void close() {
        if (database != null && database.isOpen())
            database.close();
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }
}
