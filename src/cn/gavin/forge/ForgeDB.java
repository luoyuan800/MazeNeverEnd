package cn.gavin.forge;

import android.database.sqlite.SQLiteDatabase;
import cn.gavin.db.DBHelper;

/**
 * luoyuan on 10/4/15.
 */
public class ForgeDB {
    public void createTable(SQLiteDatabase dbHelper){
        String recipeTable = "CREATE TABLE recipe(" +
                "name TEXT NOT NULL PRIMARY KEY," +
                "items TEXT NOT NULL," +
                "base TEXT," +
                "addition TEXT," +
                "found CHAR(5)," +
                "user CHAR(5)," +
                "type INTEGER," +
                "color TEXT" +
                ")";
        String accessoryTable = "CREATE TABLE accessory(" +
                "id TEXT NOT NULL PRIMARY KEY," +
                "name TEXT NOT NULL," +
                "base TEXT," +
                "addition TEXT," +
                "element CHAR(2)," +
                "type INTEGER," +
                "color TEXT" +
                ")";
        String itemTable = "CREATE TABLE item(" +
                "id TEXT NOT NULL PRIMARY KEY," +
                "name TEXT NOT NULL," +
                "properties TEXT" +
                ")";
        dbHelper.execSQL(recipeTable);
        dbHelper.execSQL(accessoryTable);
        dbHelper.execSQL(itemTable);
        insertRecipe(dbHelper);
    }

    public void insertRecipe(SQLiteDatabase database){
        String base = "INSERT INTO recipe (name, items, base, addition, found, user, type, color) values ('%s', '%s','%s','%s','false','false','%s','%s')";
        String sql = String.format(base,"乌金戒", "黑石-玄石-龟壳-冷杉木", "ADD_ATK:12000-ADD_UPPER_HP:10000","ADD_DEF:100000", RingBuilder.type,"#008000");
        database.execSQL(sql);
        sql = String.format(base,"水纹戒", "凤凰木-龟壳-蚁须-青檀木", "ADD_DEF:12000","ADD_ATK:100000", RingBuilder.type,"#008000");
        database.execSQL(sql);
    }
}
