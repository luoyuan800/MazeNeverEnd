package cn.gavin.forge;

import android.database.sqlite.SQLiteDatabase;

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
        dbHelper.execSQL("CREATE UNIQUE INDEX recipe_index ON recipe (name)");
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
        sql = String.format(base,"青铜戒", "铜矿石-精铁-黑石-钢石-牛皮", "ADD_DEF:2000-ADD","ADD_UPPER_HP:50000", RingBuilder.type,"#0000FF");
        database.execSQL(sql);
        sql = String.format(base,"火神戒", "凤凰毛-硝石-银矿石-牛筋", "ADD_STR:2000-ADD_ATK:60000","ADD_AGI:5000", RingBuilder.type,"#9932CC");
        database.execSQL(sql);
        sql = String.format(base,"神心戒", "虎筋-龙皮-银矿石-钢石-萤石", "ADD_AGI:2000-ADD_POWER:3000","ADD_AGI:5000", RingBuilder.type,"#008000");
        database.execSQL(sql);
        sql = String.format(base,"桑晶链", "蛇筋-龙须木-红檀木-玄石-钢石", "ADD_POWER:2000-ADD_ATK:50000","ADD_UPPER_HP:50000", NecklaceBuilder.type,"#0000FF");
        database.execSQL(sql);
        sql = String.format(base,"神心项链", "萤石-银矿石-白杏木-狼筋-精铁", "ADD_AGI:5000-ADD_ATK:5000","ADD_UPPER_HP:500000", NecklaceBuilder.type,"#8A2BE2");
        database.execSQL(sql);
        sql = String.format(base,"默心链", "白云石-银矿石-玄石-龟壳", "ADD_AGI:1000-ADD_DEF:50000","ADD_POWER:5000", NecklaceBuilder.type,"#8A2BE2");
        database.execSQL(sql);
        sql = String.format(base,"黄金链", "钢石-铁矿石-硝石-原石-龙筋", "ADD_CLICK_AWARD:50-ADD_DEF:10000","ADD_CLICK_AWARD:200", NecklaceBuilder.type,"#8A2BE2");
        database.execSQL(sql);
        sql = String.format(base,"天雷盔", "硝石-银矿石-冷杉木-龙皮-蛇骨", "ADD_STR:1000","ADD_UPPER_HP:10000", HatBuilder.type,"#8A2BE2");
        database.execSQL(sql);
        sql = String.format(base,"兽首盔", "虎皮-虎骨-牛皮-牛骨-精铁", "ADD_STR:1000","ADD_ATK:60000", HatBuilder.type,"#8A2BE2");
        database.execSQL(sql);
        sql = String.format(base,"青云冠", "青檀木-蛇皮-白杏木-玄石", "ADD_AGI:1000-ADD_DEF:10000","ADD_POWER:3000", HatBuilder.type,"#8A2BE2");
        database.execSQL(sql);
        sql = String.format(base,"星尘冠", "冷杉木-龙皮-白杏木-玄石", "ADD_ATK:30000","ADD_POWER:3000", HatBuilder.type,"#FF8C00");
        database.execSQL(sql);
        upgradeTo1_3_2(database);
        upgradeTo1_4(database);
    }

    public void upgradeTo1_3_2(SQLiteDatabase database){
        String base = "INSERT INTO recipe (name, items, base, addition, found, user, type, color) values ('%s', '%s','%s','%s','false','false','%s','%s')";
        String sql = String.format(base,"天使眼淚", "龙骨-凤凰毛-蛇筋-龙须木-红檀木",
                "ADD_UPPER_HP:30000-ADD_DEF:10000","ADD_POWER:3000-ADD_CLICK_AWARD:200",
                NecklaceBuilder.type,"#FF8C00");
        database.execSQL(sql);
        sql = String.format(base,"天神戒", "蛇皮-牛骨-白杏木-银矿石-紫熏木",
                "ADD_ATK:30000-ADD_DEF:10000","ADD_ATK:50000-ADD_CLICK_AWARD:500", RingBuilder.type,"#FF8C00");
        database.execSQL(sql);
    }
    public void upgradeTo1_4(SQLiteDatabase database){
        database.execSQL("CREATE UNIQUE INDEX recipe_index ON recipe (name)");
        String base = "INSERT INTO recipe (name, items, base, addition, found, user, type, color) values ('%s', '%s','%s','%s','false','true','%s','%s')";
        String sql = String.format(base,"沁玟之永恒", "龙筋-冷杉木-冷杉木-冷杉木-冷杉木",
                "ADD_POWER:1000000-ADD_AGI:1000000-ADD_STR:1000000","ADD_CLICK_AWARD:2000", NecklaceBuilder.type,"#FF8C00");
        database.execSQL(sql);
        sql = String.format(base,"沁玟之思念", "龙筋-龙筋-玄石-龙筋-龙筋",
                "ADD_DODGE_RATE:50","ADD_CLICK_POINT_AWARD:100", HatBuilder.type,"#FF8C00");
        database.execSQL(sql);
        sql = String.format(base,"沁玟之护守", "玄石-玄石-玄石-玄石-冷杉木", "ADD_CLICK_AWARD:3000-ADD_CLICK_POINT_AWARD:50","ADD_PARRY:12", RingBuilder.type,"#FF8C00");
        database.execSQL(sql);
        sql = String.format(base,"要你命3000<br><font color=\"#FF4500\">要你命3000，要么生，要么死</font>", "硝石-黑石-虎骨-萤石-蚁须", "ADD_ATK:50000-ADD_DEF:-50000-ADD_UPPER_HP:23333","ADD_AGI:5000", NecklaceBuilder.type,"#800080");
        database.execSQL(sql);
        base = "REPLACE INTO recipe (name, items, base, addition, found, user, type, color) values ('%s', '%s','%s','%s','false','false','%s','%s')";
        sql = String.format(base,"天使眼淚", "龙骨-凤凰毛-蛇筋-龙须木-红檀木",
                "ADD_UPPER_HP:30000-ADD_DEF:10000","ADD_POWER:3000-ADD_CLICK_AWARD:200",
                NecklaceBuilder.type,"#FF8C00");
        database.execSQL(sql);
        sql = String.format(base,"天神戒", "蛇皮-牛骨-白杏木-银矿石-紫熏木",
                "ADD_ATK:30000-ADD_DEF:10000","ADD_ATK:50000-ADD_CLICK_AWARD:500", RingBuilder.type,"#FF8C00");
        database.execSQL(sql);
        sql = String.format(base,"旭日盔<br><font color=\"#FF4500\">日出东方，唯我不败</font>", "食人鸟毛-鼠骨-鼠筋-龟壳-青檀木", "ADD_ATK:10000-ADD_UPPER_HP:10000","ADD_AGI:7000", HatBuilder.type,"#FF8C00");
        database.execSQL(sql);
    }
}
