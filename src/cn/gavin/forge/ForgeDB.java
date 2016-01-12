package cn.gavin.forge;

import android.database.sqlite.SQLiteDatabase;

/**
 * luoyuan on 10/4/15.
 */
public class ForgeDB {
    public void createTable(SQLiteDatabase dbHelper) {
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

    public void insertRecipe(SQLiteDatabase database) {
        String base = "INSERT INTO recipe (name, items, base, addition, found, user, type, color) values ('%s', '%s','%s','%s','false','false','%s','%s')";
        String sql = String.format(base, "乌金戒", "黑石-玄石-龟壳-冷杉木", "ADD_ATK:12000-ADD_UPPER_HP:10000", "ADD_DEF:100000", RingBuilder.type, "#9932CC");
        database.execSQL(sql);
        sql = String.format(base, "水纹戒", "凤凰木-龟壳-蚁须-青檀木", "ADD_DEF:4200", "ADD_ATK:10000", RingBuilder.type, "#556B2F");
        database.execSQL(sql);
        sql = String.format(base, "青铜戒", "铜矿石-精铁-黑石-钢石-牛皮", "ADD_DEF:2000", "ADD_UPPER_HP:50000", RingBuilder.type, "#556B2F");
        database.execSQL(sql);
        sql = String.format(base, "火神戒", "凤凰毛-硝石-银矿石-牛筋", "ADD_STR:1000-ADD_ATK:6000", "ADD_AGI:5000", RingBuilder.type, "#0000FF");
        database.execSQL(sql);
        sql = String.format(base, "神心戒", "虎筋-龙皮-银矿石-钢石-萤石", "ADD_AGI:1000-ADD_POWER:1000", "ADD_AGI:2000", RingBuilder.type, "#0000FF");
        database.execSQL(sql);
        sql = String.format(base, "桑晶链", "蛇筋-龙须木-红檀木-玄石-钢石", "ADD_POWER:2000-ADD_ATK:6000", "ADD_UPPER_HP:50000", NecklaceBuilder.type, "#0000FF");
        database.execSQL(sql);
        sql = String.format(base, "神心项链", "萤石-银矿石-白杏木-狼筋-精铁", "ADD_AGI:900-ADD_ATK:5000", "ADD_UPPER_HP:50000", NecklaceBuilder.type, "#9932CC");
        database.execSQL(sql);
        sql = String.format(base, "默心链", "白云石-银矿石-玄石-龟壳", "ADD_AGI:1000-ADD_DEF:50000", "ADD_POWER:10000", NecklaceBuilder.type, "#9932CC");
        database.execSQL(sql);
        sql = String.format(base, "黄金链", "钢石-铁矿石-硝石-原石-龙筋", "ADD_CLICK_AWARD:50-ADD_DEF:10000", "ADD_CLICK_AWARD:200", NecklaceBuilder.type, "#0000FF");
        database.execSQL(sql);
        sql = String.format(base, "天雷盔", "硝石-银矿石-冷杉木-龙皮-蛇骨", "ADD_STR:1000", "ADD_UPPER_HP:10000", HatBuilder.type, "#556B2F");
        database.execSQL(sql);
        sql = String.format(base, "兽首盔", "虎皮-虎骨-牛皮-牛骨-精铁", "ADD_STR:1000", "ADD_ATK:90000", HatBuilder.type, "#9932CC");
        database.execSQL(sql);
        sql = String.format(base, "青云冠", "青檀木-蛇皮-白杏木-玄石", "ADD_AGI:1000-ADD_DEF:10000", "ADD_POWER:3000", HatBuilder.type, "#0000FF");
        database.execSQL(sql);
        sql = String.format(base, "星尘冠", "冷杉木-龙皮-白杏木-玄石", "ADD_ATK:30000", "ADD_POWER:3000", HatBuilder.type, "#FF8C00");
        database.execSQL(sql);
        upgradeTo1_3_2(database);
        upgradeTo1_4(database);
        upgradeTo1_4_7(database);
        upgradeTp1_5(database);
        upgradeTp1_6(database);
        upgradeTp1_7(database);
        upgradeTo1_8(database);
    }

    public void upgradeTo1_3_2(SQLiteDatabase database) {

    }

    public void upgradeTo1_4(SQLiteDatabase database) {
        database.execSQL("CREATE UNIQUE INDEX recipe_index ON recipe (name)");
        String base = "INSERT INTO recipe (name, items, base, addition, found, user, type, color) values ('%s', '%s','%s','%s','false','false','%s','%s')";
        String sql = String.format(base, "要你命3000<br><font color=\"#FF4500\">要你命3000，要么生，要么死</font>", "硝石-黑石-虎骨-萤石-蚁须", "ADD_ATK:50000-ADD_DEF:~50000-ADD_UPPER_HP:23333", "ADD_AGI:5000", NecklaceBuilder.type, "#800080");
        database.execSQL(sql);
        base = "REPLACE INTO recipe (name, items, base, addition, found, user, type, color) values ('%s', '%s','%s','%s','false','false','%s','%s')";
        sql = String.format(base, "天使眼淚", "龙骨-凤凰毛-蛇筋-龙须木-红檀木",
                "ADD_UPPER_HP:30000-ADD_DEF:10000", "ADD_POWER:3000-ADD_CLICK_AWARD:200",
                NecklaceBuilder.type, "#FF8C00");
        database.execSQL(sql);
        sql = String.format(base, "天神戒", "蛇皮-牛骨-白杏木-银矿石-紫熏木",
                "ADD_ATK:30000-ADD_DEF:10000", "ADD_ATK:50000-ADD_CLICK_AWARD:500", RingBuilder.type, "#FF8C00");
        database.execSQL(sql);
        sql = String.format(base, "旭日盔<br><font color=\"#FF4500\">日出东方，唯我不败</font>", "食人鸟毛-鼠骨-鼠筋-龟壳-青檀木", "ADD_ATK:10000-ADD_UPPER_HP:10000", "ADD_AGI:7000", HatBuilder.type, "#FF8C00");
        database.execSQL(sql);

    }

    public void upgradeTo1_4_7(SQLiteDatabase database) {

    }

    public void upgradeTp1_5(SQLiteDatabase database) {
        String base = "REPLACE INTO recipe (name, items, base, addition, found, user, type, color) values ('%s', '%s','%s','%s','false','false','%s','%s')";
        String sql = String.format(base, "您捡到一只六娃", "鼠皮-鼠筋-白云石-白杏木-牛骨", "ADD_STR:1000-ADD_POWER:2000-ADD_CLICK_AWARD:300", "ADD_AGI:30000-ADD_UPPER_HP:~30000", HatBuilder.type, "#FF8C00");
        database.execSQL(sql);
        sql = String.format(base, "沁玟之思念", "龙筋-龙筋-玄石-龙筋-龙筋",
                "ADD_DODGE_RATE:~20", "ADD_CLICK_POINT_AWARD:1", HatBuilder.type, "#FF8C00");
        database.execSQL(sql);
        sql = String.format(base, "沁玟之护守", "玄石-玄石-玄石-玄石-冷杉木", "ADD_CLICK_AWARD:~300", "ADD_PARRY:20", RingBuilder.type, "#FF8C00");
        database.execSQL(sql);
        sql = String.format(base, "沁玟之永恒", "龙筋-冷杉木-冷杉木-冷杉木-冷杉木",
                "ADD_POWER:~3000-ADD_AGI:~2000-ADD_STR:~1000", "ADD_CLICK_AWARD:300", NecklaceBuilder.type, "#FF8C00");
        database.execSQL(sql);
        sql = String.format(base, "余辉", "龙筋-白云石-龙皮-冷杉木",
                "ADD_UPPER_HP:400000-ADD_ATK:250000-ADD_DEF:~3000000", "ADD_POWER:1000", NecklaceBuilder.type, "#800080");
        database.execSQL(sql);
    }

    public void upgradeTp1_6(SQLiteDatabase database) {
        String base = "REPLACE INTO recipe (name, items, base, addition, found, user, type, color) values ('%s', '%s','%s','%s','false','false','%s','%s')";
        String sql = String.format(base, "烈焰红唇戒", "银矿石-铁矿石-铜矿石-黑石-萤石", "ADD_HIT_RATE:80-ADD_ATK:~30000", "ADD_AGI:500", RingBuilder.type, "#FF8C00");
        database.execSQL(sql);
        sql = String.format(base, "黯然销魂链", "白杏木-冷杉木-凤凰木-紫熏木-红檀木",
                "ADD_DODGE_RATE:80-ADD_ATK:~30000", "ADD_POWER:1000", NecklaceBuilder.type, "#FF8C00");
        database.execSQL(sql);
        sql = String.format(base, "凤姐的丝袜", "牛皮-虎皮-蛇皮-龙皮-鼠皮", "ADD_PARRY:80-ADD_ATK:~30000", "ADD_STR:200", HatBuilder.type, "#FF8C00");
        database.execSQL(sql);
        sql = String.format(base, "Soi●残缺(皇冠)", "食人鸟毛-鼠筋-鼠筋-鼠筋-鼠筋", "ADD_PER_ATK:3", "ADD_STR:2000-ADD_PARRY:~2", HatBuilder.type, "#FF8C00");
        database.execSQL(sql);
        sql = String.format(base, "Soi●残缺(帝镯)", "鼠骨-鼠筋-鼠骨-鼠骨-鼠骨", "ADD_PER_DEF:3", "ADD_POWER:2000-ADD_PARRY:~5", NecklaceBuilder.type, "#FF8C00");
        database.execSQL(sql);
        sql = String.format(base, "Soi●残缺(霸戒)", "硝石-紫熏木-紫熏木-紫熏木-紫熏木", "ADD_PER_UPPER_HP:3", "ADD_AGI:3000-ADD_DODGE_RATE:~15", RingBuilder.type, "#FF8C00");
        database.execSQL(sql);
        sql = String.format(base, "乐枫", "龙须木-虎皮-蛟筋-钢石-萤石", "ADD_POWER:3000-ADD_AGI:~3000", "ADD_PER_DEF:10", HatBuilder.type, "#FF8C00");
        database.execSQL(sql);
        sql = String.format(base, "(乐枫)蔚蓝项链", "龙筋-龙皮-青檀木-白云石-食人鸟毛", "ADD_AGI:3000-ADD_POWER:~5000", "ADD_PER_UPPER_HP:15", NecklaceBuilder.type, "#FF8C00");
        database.execSQL(sql);
        sql = String.format(base, "(乐枫)云戒", "龙骨-硝石-银矿石-龙须木-原石", "ADD_AGI:3000-ADD_POWER:~5000", "ADD_PER_UPPER_HP:15", RingBuilder.type, "#FF8C00");
        database.execSQL(sql);
    }

    public void upgradeTp1_7(SQLiteDatabase database) {
        String base = "REPLACE INTO recipe (name, items, base, addition, found, user, type, color) values ('%s', '%s','%s','%s','false','false','%s','%s')";

        String sql = String.format(base, "左眼的漩涡", "龙皮-龙骨-龙须-龙须木-龙筋",
                "ADD_HIT_RATE:57-ADD_DODGE_RATE:20-ADD_PARRY:37-ADD_ATK:700000", "ADD_PER_ATK:~10-ADD_PER_DEF：~10", HatBuilder.type, "#FF8C00");
        database.execSQL(sql);
        sql = String.format(base, "叶良辰的内裤", "龙皮-龙须-龙须-龙须-龙须", "ADD_HIT_RATE:51-ADD_DODGE_RATE:10", "ADD_PER_ATK:61-ADD_PARRY:90", HatBuilder.type, "#FF8C00");
        database.execSQL(sql);
        sql = String.format(base, "Soi●残缺(暴击)", "食人鸟毛-鼠筋-鼠筋-鼠筋-鼠筋", "ADD_ATK:~20000-ADD_DEF:~20000-ADD_UPPER_HP:~20000", "ADD_PER_UPPER_HP:~15-ADD_HIT_RATE:30-ADD_PARRY:30-ADD_DODGE_RATE:10-ADD_CLICK_AWARD:10-ADD_CLICK_POINT_AWARD:1", HatBuilder.type, "#FF8C00");
        database.execSQL(sql);
        sql = String.format(base, "Soi●残缺(格挡)", "鼠骨-鼠筋-鼠骨-鼠骨-鼠骨", "ADD_ATK:~20000-ADD_DEF:~20000-ADD_UPPER_HP:~20000", "ADD_PER_UPPER_HP:~25-ADD_HIT_RATE:20-ADD_PARRY:30-ADD_DODGE_RATE:10-ADD_CLICK_AWARD:10-ADD_CLICK_POINT_AWARD:1", NecklaceBuilder.type, "#FF8C00");
        database.execSQL(sql);
        sql = String.format(base, "Soi●残缺(闪避)", "硝石-紫熏木-紫熏木-紫熏木-紫熏木", "ADD_ATK:~20000-ADD_DEF:~20000-ADD_UPPER_HP:~20000", "ADD_PER_UPPER_HP:~35-ADD_HIT_RATE:20-ADD_PARRY:30-ADD_DODGE_RATE:20-ADD_CLICK_AWARD:10-ADD_CLICK_POINT_AWARD:1", RingBuilder.type, "#FF8C00");
        database.execSQL(sql);

    }

    public void upgradeTo1_8(SQLiteDatabase database){
        String base = "REPLACE INTO recipe (name, items, base, addition, found, user, type, color) values ('%s', '%s','%s','%s','false','false','%s','%s')";
        String sql = String.format(base, "初音的守护<br><font color=\"#FF4500\">天长地久，不离不弃</font>", "龙皮-龙须-紫熏木-紫熏木-紫熏木", "ADD_CLICK_AWARD:60000-ADD_ATK:~700000-ADD_DEF:~100000", "ADD_DODGE_RATE:35-ADD_PER_UPPER_HP:~22", RingBuilder.type, "#FF8C00");
        database.execSQL(sql);
        sql = String.format(base, "蝶祁的眷恋<br><font color=\"#FF4500\">我会在你身后一直默默的陪着你</font>", "龙皮-龙须-紫熏木-龙骨-紫熏木", "ADD_PER_DEF:14-ADD_PER_ATK:10-ADD_CLICK_POINT_AWARD:3", "ADD_HIT_RATE:~10-ADD_PER_UPPER_HP:62", NecklaceBuilder.type, "#FF8C00");
        database.execSQL(sql);
        sql = String.format(base, "李宗瑞的安全套", "龙皮-龙须-精铁-龙骨-紫熏木", "ADD_ATK:10000", "ADD_HIT_RATE:90-ADD_PARRY:35-ADD_DODGE_RATE:15-ADD_CLICK_AWARD:50", RingBuilder.type, "#FF8C00");
        database.execSQL(sql);
        sql = String.format(base, "赵日天的裤腰带", "龙皮-龙须-龙须-龙骨-紫熏木", "ADD_PER_ATK:24", "ADD_HIT_RATE:30-ADD_PARRY:30-ADD_DODGE_RATE:10-ADD_CLICK_AWARD:5-ADD_CLICK_POINT_AWARD:1", RingBuilder.type, "#FF8C00");
        database.execSQL(sql);
        sql = String.format(base, "仙剑メ天道链", "蚁须-龙须-龙须-龙骨-紫熏木", "ADD_AGI:24000", "ADD_HIT_RATE:60-ADD_PARRY:~10-ADD_CLICK_POINT_AWARD:1", NecklaceBuilder.type, "#FF8C00");
        database.execSQL(sql);
        sql = String.format(base, "仙剑メ天道冠", "蚁须-龙须-龙须-龙骨-紫熏木", "ADD_PER_DEF:24", "ADD_HIT_RATE:~10-ADD_PARRY:60-ADD_CLICK_AWARD:65", HatBuilder.type, "#FF8C00");
        database.execSQL(sql);
        sql = String.format(base, "仙剑メ天道戒", "蚁须-鼠骨-龙须-龙骨-紫熏木", "ADD_ATK:64000", "ADD_HIT_RATE:30-ADD_PARRY:30-ADD_DODGE_RATE:10", RingBuilder.type, "#FF8C00");
        database.execSQL(sql);
        sql = String.format(base, "雪儿的启示<br><font color=\"#FF4500\">(｡•́︿•̀｡)</font>", "龙筋-龙筋-龙须-龙骨-紫熏木", "ADD_PARRY:55-ADD_POWER:10000", "ADD_STR:100000-ADD_PARRY:~25-ADD_PER_DEF:30-ADD_CLICK_AWARD:40", NecklaceBuilder.type, "#FF8C00");
        database.execSQL(sql);
        sql = String.format(base, "传说中的🐮魔王<br><font color=\"\"#FF4500\"\">唯一</font>", "龙筋-龙筋-玄石-龙骨-紫熏木", "ADD_PER_ATK:65-ADD_PER_UPPER_HP:65", "ADD_CLICK_AWARD:~1000000-ADD_DODGE_RATE:50-ADD_PARRY:50-ADD_HIT_RATE:50-ADD_PER_DEF:65", HatBuilder.type, "#FF8C00");
        database.execSQL(sql);
    }
}
