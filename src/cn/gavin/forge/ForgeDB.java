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
        sql = String.format(base,"青铜戒", "铜矿石-精铁-黑石-钢石-牛皮", "ADD_DEF:2000-ADD_ATK:50000","ADD_UPPER_HP:50000", RingBuilder.type,"#0000FF");
        database.execSQL(sql);
        sql = String.format(base,"桑晶链", "蛇筋-龙须木-红檀木-玄石-钢石", "ADD_DEF:2000-ADD_ATK:50000","ADD_UPPER_HP:50000", NecklaceBuilder.type,"#0000FF");
        database.execSQL(sql);
        sql = String.format(base,"神心项链", "萤石-银矿石-白杏木-狼筋-精铁", "ADD_AGI:10000-ADD_ATK:500","ADD_UPPER_HP:500000", NecklaceBuilder.type,"#8A2BE2");
        database.execSQL(sql);
        sql = String.format(base,"天雷盔", "硝石-银矿石-冷杉木-龙皮-蛇骨", "ADD_STR:1000","ADD_UPPER_HP:10000", HatBuilder.type,"#8A2BE2");
        database.execSQL(sql);
    }
}
