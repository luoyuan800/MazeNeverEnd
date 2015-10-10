package cn.gavin.monster;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Stack;

import cn.gavin.activity.MazeContents;
import cn.gavin.db.DBHelper;
import cn.gavin.utils.StringUtils;

/**
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED
 * Created by luoyuan on 10/7/15.
 */
public class Defender {

    private String desc;
    private String name;
    private String atk;
    private String hp;
    private String lev;
    private String hello;
    private String skill;
    private String skillLev;

    public void save(SQLiteDatabase db){
        String sql = String.format("INSERT INTO defender(name, lev, hp, atk, skill, skill_lev, hello) values('%s','%s','%s','%s','%s','%s','%s')", name, lev, hp, atk,skill,skillLev,hello);
        db.execSQL(sql);
    }

    public static Stack<Defender> loadAllDefender(SQLiteDatabase db) {
        Stack<Defender> defenders = new Stack<Defender>();
        Cursor cursor ;
        if(db!=null){
            cursor = db.rawQuery("SELECT * FROM defender ORDER BY lev DESC", null);
            cursor.moveToFirst();
        }else {
            cursor = DBHelper.getDbHelper().excuseSOL("SELECT * FROM defender ORDER BY lev DESC");
        }
        while (!cursor.isAfterLast()) {
            Defender d = new Defender();
            String name = cursor.getString(cursor.getColumnIndex("name"));
            if (name != null && name.startsWith("0x")) {
                name = StringUtils.toStringHex(name);
            }
            String lev = cursor.getString(cursor.getColumnIndex("lev"));
            if(MazeContents.hero !=null) {
                d.desc = String.format("<font color=\"#9932CC\">%s</font> - %s %s", name, lev, lev.equalsIgnoreCase(MazeContents.hero.getMaxMazeLev() + "") ? " <--- 你也在这里!" : "");
            }
            d.lev = lev;
            d.name = name;
            d.atk = cursor.getString(cursor.getColumnIndex("atk"));
            d.hp = cursor.getString(cursor.getColumnIndex("hp"));
            int index = cursor.getColumnIndex("hello");
            if(index!=-1) {
                d.hello = cursor.getString(index);
            }else{
                d.hello = "遇見了吾，你將止步於此！";
            }
            d.skill  = cursor.getString(cursor.getColumnIndex("skill"));
            d.skillLev  = cursor.getString(cursor.getColumnIndex("skill_lev"));
            defenders.push(d);
            cursor.moveToNext();
        }
        cursor.close();
        return defenders;
    }

    public String toString() {
        return desc;
    }

    public static void addDefender(String name, long hp, long atk, long lev, String skillName, long skillLev, String hello) {
        String addDefender = String.format("REPLACE INTO defender(name, lev, hp, atk, skill, skill_lev, hello) " +
                "values('%s','%s','%s','%s','%s','%s', '%s')", name, lev, hp, atk, skillName, skillLev, hello);
        DBHelper.getDbHelper().excuseSQLWithoutResult(addDefender);
    }

    public static Monster buildDefender(long lev) {
        try {
            Cursor cursor = DBHelper.getDbHelper().excuseSOL("SELECT * FROM defender WHERE lev = '" + lev + "' AND skill_lev IS NOT '0'");
            if (!cursor.isAfterLast()) {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                if (name != null && name.startsWith("0x")) {
                    name = StringUtils.toStringHex(name);
                }
                String hp = cursor.getString(cursor.getColumnIndex("hp"));
                String atk = cursor.getString(cursor.getColumnIndex("atk"));
                if (StringUtils.isNotEmpty(hp) && StringUtils.isNotEmpty(atk) && !"null".equalsIgnoreCase(hp) && !"null".equalsIgnoreCase(atk)) {
                    return new Monster("", "【守护者】", name, Long.parseLong(hp), Long.parseLong(atk));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void createDB(SQLiteDatabase db) {
        String createTable = "CREATE TABLE defender(" +
                "name TEXT NOT NULL," +
                "lev INTEGER NOT NULL PRIMARY KEY," +
                "hp TEXT," +
                "atk TEXT," +
                "skill TEXT," +
                "skill_lev TEXT," +
                "hello TEXT" +
                ")";
        db.execSQL(createTable);
        db.execSQL("CREATE UNIQUE INDEX maze_lev ON defender (lev)");
        String addDefender = "INSERT INTO defender(name, lev, hp, atk, skill, skill_lev, hello) values('沁玟','324','238645','47889','重击','1','遇見了吾，你將止步於此！')";
        db.execSQL(addDefender);
        addDefender = "INSERT INTO defender(name, lev, hp, atk, skill, skill_lev,hello) values('夏文进','101','44990','22911','重击','1','遇見了吾，你將止步於此！')";
        db.execSQL(addDefender);
        addDefender = "INSERT INTO defender(name, lev, hp, atk, skill, skill_lev,hello) values('傻瓜','112','81331','5542','重击','1','遇見了吾，你將止步於此！')";
        db.execSQL(addDefender);
        addDefender = "INSERT INTO defender(name, lev, hp, atk, skill, skill_lev,hello) values('自己的爸爸','200','80046','10329','重击','1','遇見了吾，你將止步於此！')";
        db.execSQL(addDefender);
        addDefender = "INSERT INTO defender(name, lev, hp, atk, skill, skill_lev,hello) values('SunKnight','527','85677','519','重击','1','遇見了吾，你將止步於此！')";
        db.execSQL(addDefender);
        addDefender = "INSERT INTO defender(name, lev, hp, atk, skill, skill_lev,hello) values('沁玟','1033','1470816','81','重击','1','遇見了吾，你將止步於此！')";
        db.execSQL(addDefender);
        addDefender = "INSERT INTO defender(name, lev, hp, atk, skill, skill_lev,hello) values('沁玟','817','515279','34','重击','1','遇見了吾，你將止步於此！')";
        db.execSQL(addDefender);
        addDefender = "INSERT INTO defender(name, lev, hp, atk, skill, skill_lev,hello) values('沁玟','235','29218','34','重击','1','遇見了吾，你將止步於此！')";
        db.execSQL(addDefender);
        addDefender = "INSERT INTO defender(name, lev, hp, atk, skill, skill_lev,hello) values('沁玟','3138','19313776','2082065','重击','1','遇見了吾，你將止步於此！')";
        db.execSQL(addDefender);
        addDefender = "INSERT INTO defender(name, lev, hp, atk, skill, skill_lev,hello) values('0x52c78005','682','889225','40977','重击','1','遇見了吾，你將止步於此！')";
        db.execSQL(addDefender);
        addDefender = "INSERT INTO defender(name, lev, hp, atk, skill, skill_lev,hello) values('0x52c78005','100','356966','607682','重击','1','遇見了吾，你將止步於此！')";
        db.execSQL(addDefender);
        addDefender = "INSERT INTO defender(name, lev, hp, atk, skill, skill_lev,hello) values('0x590f65878fdb','274','913592','522590','重击','1','遇見了吾，你將止步於此！')";
        db.execSQL(addDefender);
        addDefender = "INSERT INTO defender(name, lev, hp, atk, skill, skill_lev,hello) values('0x590f65878fdb','163','30692','30265','重击','1','遇見了吾，你將止步於此！')";
        db.execSQL(addDefender);
        addDefender = "INSERT INTO defender(name, lev, hp, atk, skill, skill_lev,hello) values('我是神，有本事上来！','10000','1931377600','208206500','重击','2','你是不可能超越我的！')";
        db.execSQL(addDefender);
    }

    public static void upgradeDB9To10(SQLiteDatabase db) {
        Stack<Defender> defenders = Defender.loadAllDefender(db);
        db.execSQL("DROP TABLE defender");
        String createTable = "CREATE TABLE defender(" +
                "name TEXT NOT NULL," +
                "lev INTEGER NOT NULL PRIMARY KEY," +
                "hp TEXT," +
                "atk TEXT," +
                "skill TEXT," +
                "skill_lev TEXT," +
                "hello TEXT" +
                ")";
        db.execSQL(createTable);
        db.execSQL("CREATE UNIQUE INDEX maze_lev ON defender (lev)");
        for(Defender d : defenders){
            d.save(db);
        }
        String addDefender = "REPLACE INTO defender(name, lev, hp, atk, skill, skill_lev,hello) values('我是神，有本事上来！','10000','1931377600','208206500','重击','2','你是不可能超越我的！')";
        db.execSQL(addDefender);
        addDefender = "INSERT INTO defender(name, lev, hp, atk, skill, skill_lev,hello) values('某鸟','212','981331','95542','重击','1','我有台词，你咬我呀！')";
        db.execSQL(addDefender);
        addDefender = "INSERT INTO defender(name, lev, hp, atk, skill, skill_lev,hello) values('笨牛','121','81331','65542','重击','1','我很帅很帅，哈哈哈哈！')";
        db.execSQL(addDefender);
    }

    public String getDesc() {
        return desc;
    }
}
