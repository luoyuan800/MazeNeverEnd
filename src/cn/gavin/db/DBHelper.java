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
import cn.gavin.log.LogHelper;
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
    private static int DB_VERSION = 16;

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
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(MainGameActivity.TAG, "CreateTable", e);
            LogHelper.logException(e);
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        upgrade8_9(db, oldVersion);
        upgrade10_11(db, oldVersion);
        upgrade11_12(db, oldVersion);
        upgrade12_13(db, oldVersion);
        upgrade14_15(db, oldVersion);
        upgrade15_16(db);
    }

    private void upgrade15_16(SQLiteDatabase db){
        try{
            PetDB.createDB(db);
            new ForgeDB().upgradeTp1_6(db);
        }catch (Exception e){
            e.printStackTrace();
            LogHelper.logException(e);
        }
    }

    private void upgrade14_15(SQLiteDatabase db, int oldVersion) {
        try {
            for (Accessory accessory : Accessory.loadAccessories(db)) {
                boolean change = false;
                EnumMap<Effect, Number> unTemp = new EnumMap<Effect, Number>(accessory.getEffects());
                for (EnumMap.Entry<Effect, Number> entry : unTemp.entrySet()) {
                    if (entry.getKey() == Effect.ADD_CLICK_POINT_AWARD) {
                        accessory.getEffects().remove(entry.getKey());
                        change = true;
                    }
                    if (entry.getKey() == Effect.ADD_AGI || entry.getKey() == Effect.ADD_POWER || entry.getKey() == Effect.ADD_STR) {
                        if (entry.getValue().longValue() > 500000) {
                            accessory.getEffects().put(entry.getKey(), entry.getValue().longValue() / 10);
                            change = true;
                        }
                    }
                }
                if(change) {
                    accessory.delete();
                    accessory.save();
                }
            }
            for (Item item : Item.loadItems(db)) {
                boolean change = false;
                if (item.getEffect() == Effect.ADD_CLICK_POINT_AWARD) {
                    item.setEffect(Effect.ADD_CLICK_AWARD);
                    change = true;
                }
                if (item.getEffect1() == Effect.ADD_CLICK_POINT_AWARD) {
                    item.setEffect1(Effect.ADD_CLICK_AWARD);
                    change = true;
                }
                if (item.getEffect() == Effect.ADD_AGI || item.getEffect() == Effect.ADD_POWER || item.getEffect() == Effect.ADD_STR) {
                    if (item.getEffectValue().longValue() > 500000) {
                        item.setEffectValue(item.getEffectValue().longValue() / 10);
                        change = true;
                    }
                }
                if (item.getEffect1() == Effect.ADD_AGI || item.getEffect1() == Effect.ADD_POWER || item.getEffect1() == Effect.ADD_STR) {
                    if (item.getEffect1Value().longValue() > 500000) {
                        item.setEffect1Value(item.getEffect1Value().longValue() / 10);
                        change = true;
                    }
                }
                if(change) {
                    item.delete(db);
                    item.save(db);
                }
            }
            new ForgeDB().upgradeTp1_5(db);
        } catch (Exception e) {
            e.printStackTrace();
            LogHelper.logException(e);
        }
    }

    private void upgrade12_13(SQLiteDatabase db, int oldVersion) {
        try {
            if (oldVersion == 12) {
                new ForgeDB().upgradeTo1_4_7(db);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MazeNeverEnd", e.getMessage());
            LogHelper.logException(e);
        }
    }

    private void upgrade11_12(SQLiteDatabase db, int oldVersion) {
        try {
            if (oldVersion == 11) {
                PalaceMonster.createDB(db);
                String createTable = "CREATE TABLE npc(" +
                        "name TEXT NOT NULL PRIMARY KEY," +
                        "atk TEXT," +
                        "hp TEXT," +
                        "lev TEXT" +
                        ")";
                db.execSQL(createTable);
                db.execSQL("CREATE UNIQUE INDEX npc_index ON npc (name,lev)");
                new ForgeDB().upgradeTo1_4(database);
                db.execSQL("DROP TABLE defender");
                upgrade12_13(db, 12);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MazeNeverEnd", e.getMessage());
            LogHelper.logException(e);
        }
    }

    private void upgrade10_11(SQLiteDatabase db, int oldVersion) {
        try {
            if (oldVersion == 10) {
                new ForgeDB().upgradeTo1_3_2(db);
                upgrade11_12(db, 11);
                upgrade12_13(db, 12);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MazeNeverEnd", e.getMessage());
            LogHelper.logException(e);
        }
    }

    private void upgrade8_9(SQLiteDatabase db, int oldVersion) {
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
                upgrade10_11(db, 10);
                upgrade11_12(db, 11);
                upgrade12_13(db, 12);
            }
        } catch (Exception e) {
            Log.e("MazeNeverEnd", e.getMessage());
            LogHelper.logException(e);
            e.printStackTrace();
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
