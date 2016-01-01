package cn.gavin.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.EnumMap;

import cn.gavin.activity.MainGameActivity;
import cn.gavin.forge.Accessory;
import cn.gavin.forge.ForgeDB;
import cn.gavin.forge.Item;
import cn.gavin.forge.effect.Effect;
import cn.gavin.good.GoodManager;
import cn.gavin.log.LogHelper;
import cn.gavin.monster.MonsterDB;
import cn.gavin.palace.PalaceMonster;
import cn.gavin.pet.PetDB;

/**
 * Created by gluo on 9/14/2015.
 */
public class DBHelper {
    private static String DB_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/maze/data/demon";
    public final static String DB_NAME = "mazeNeverEnd";
    private static int DB_VERSION_1_2_1 = 9;
    private static int DB_VERSION_1_3_1 = 10;
    private static int DB_VERSION_1_3_2 = 11;
    private static int DB_VERSION_1_4_7 = 13;
    private static int DB_VERSION_1_4 = 12;
    private static int DB_VERSION_1_4_8 = 14;
    private static int DB_VERSION_1_5 = 15;
    private static int DB_VERSION_1_6 = 16;
    private static int DB_VERSION_1_7 = 17;
    private static int DB_VERSION_1_8 = 18;
    private static int DB_VERSION = 20;

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
            LogHelper.logException(e);
        }
    }

    private SQLiteDatabase openOrCreateInnerDB() {
        database = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        if(database.getVersion() < 20){
            context.deleteDatabase(DB_NAME);
            database = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        }
        if (database.getVersion() == 0) {
            onCreate(database);
        } else if (database.getVersion() < DB_VERSION) {
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

            String createTable = "CREATE TABLE skill(" +
                    "name TEXT NOT NULL PRIMARY KEY," +
                    "is_active CHAR(50)," +
                    "is_on_use CHAR(5)," +
                    "probability TEXT," +
                    "count TEXT," +
                    "base_harm TEXT," +
                    "addition_harm TEXT" +
                    ")";
            db.execSQL(createTable);
            createTable = "CREATE TABLE npc(" +
                    "name TEXT NOT NULL PRIMARY KEY," +
                    "atk TEXT," +
                    "hp TEXT," +
                    "lev TEXT" +
                    ")";
            db.execSQL(createTable);
            db.execSQL("CREATE UNIQUE INDEX npc_index ON npc (name,lev)");
            ForgeDB forgeDB = new ForgeDB();
            forgeDB.createTable(db);
            PalaceMonster.createDB(db);
            PetDB.createDB(db);
            GoodManager.buildGoodsDB(db);
            MonsterDB.createMonsterTable(db);
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(MainGameActivity.TAG, "CreateTable", e);
            LogHelper.logException(e);
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion < oldVersion){
            throw new RuntimeException("反向安装了低版本：" + oldVersion + "-->" + newVersion);
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
