package cn.gavin.monster;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Stack;

import cn.gavin.db.DBHelper;
import cn.gavin.utils.StringUtils;

/**
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED
 * Created by luoyuan on 10/7/15.
 */
public class Defender {

    private String desc;

    public static Stack<Defender> loadAllDefender(){
        Stack<Defender> defenders = new Stack<Defender>();
        Cursor cursor = DBHelper.getDbHelper().excuseSOL("SELECT * FROM defender ORDER BY lev ASC");
        while(cursor.isAfterLast()){
            Defender d = new Defender();
            d.desc = String.format("<font color=\"#9932CC\">%s</font> - %s" , cursor.getString(cursor.getColumnIndex("name")), cursor.getString(cursor.getColumnIndex("lev")));
            defenders.push(d);
        }
        return defenders;
    }

    public String toString(){
        return desc;
    }

    public static void addDefender(String name, long hp, long atk, long lev, String skillName, long skillLev){
        String addDefender = String.format("REPLACE INTO defender(name, lev, hp, atk, skill, skill_lev) " +
                "values('%s','%s','%s','%s','%s','%s')", name, lev, hp, atk, skillName, skillLev);
        DBHelper.getDbHelper().excuseSQLWithoutResult(addDefender);
    }

    public static Monster buildDefender(long lev) {
        try {
            Cursor cursor = DBHelper.getDbHelper().excuseSOL("SELECT * FROM defender WHERE lev = '" + lev + "' AND skill_lev IS NOT '0'");
            if (!cursor.isAfterLast()) {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                if(name!=null && name.startsWith("0x")){
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
                "lev TEXT NOT NULL PRIMARY KEY," +
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
    }

    public static void upgradeDB(SQLiteDatabase db){
        db.execSQL("DROP TABLE defender");
        createDB(db);
    }

    public String getDesc() {
        return desc;
    }
}
