package cn.gavin.monster;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Message;

import java.util.List;
import java.util.Stack;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.gavin.BaseObject;
import cn.gavin.Element;
import cn.gavin.activity.BaseContext;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.db.DBHelper;
import cn.gavin.skill.Skill;
import cn.gavin.upload.PalaceObject;
import cn.gavin.utils.StringUtils;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 10/9/2015.
 */
public class PalaceMonster implements BaseObject {


    public static void updatePalace(final MainGameActivity context) {
        BmobQuery<PalaceObject> query = new BmobQuery<PalaceObject>();
        query.setLimit(context.getPalaceCount());
        query.findObjects(context, new FindListener<PalaceObject>() {
            @Override
            public void onSuccess(final List<PalaceObject> palaceObjects) {
                DBHelper.getDbHelper().excuseSQLWithoutResult("DELETE FROM palace");
                for (PalaceObject object : palaceObjects) {
                    object.save();
                }
                context.getHandler().sendEmptyMessage(106);
            }

            @Override
            public void onError(int i, String s) {
                Message message = new Message();
                message.what = 106;
                message.obj = s;
                context.getHandler().sendEmptyMessage(106);
            }
        });
    }

    public static void getPalaceCount(final BaseContext context) {
        BmobQuery<PalaceObject> query = new BmobQuery<PalaceObject>();
        query.count((Context) context, PalaceObject.class, new CountListener() {
            @Override
            public void onSuccess(int i) {
                Message message = new Message();
                message.what = 203;
                message.arg1 = i;
                context.getHandler().sendMessage(message);
            }

            @Override
            public void onFailure(int i, String s) {
                Message message = new Message();
                message.obj = s;
                message.what = 203;
                message.arg1 = DBHelper.getDbHelper().excuseSOL("SELECT count(*) FROM palace").getInt(0);
                context.getHandler().sendMessage(message);
            }
        });
    }

    public static void createDB(SQLiteDatabase db) {
        String createTable = "CREATE TABLE palace(" +
                "id TEXT NOT NULL PRIMARY KEY," +
                "name TEXT NOT NULL," +
                "lev INTEGER NOT NULL," +
                "hp TEXT," +
                "atk TEXT," +
                "def TEXT," +
                "element TEXT," +
                "skill TEXT," +
                "skill1 TEXT," +
                "skill2 TEXT," +
                "parry TEXT," +
                "hit_rate TEXT," +
                "pay TEXT," +
                "hello TEXT" +
                ")";
        db.execSQL(createTable);
        db.execSQL("CREATE UNIQUE INDEX maze_lev ON palace (name, lev)");
    }

    public static Monster getDefender(long lev) {
        Cursor cursor = DBHelper.getDbHelper().excuseSOL("SELECT * FROM palace WHERE lev = '" + lev + "'");
        if (!cursor.isAfterLast()) {
            long atk = Long.parseLong(cursor.getString(cursor.getColumnIndex("atk")));
            long hp = Long.parseLong(cursor.getString(cursor.getColumnIndex("hp"))) + Long.parseLong(cursor.getString(cursor.getColumnIndex("def")));
            Monster monster = new Monster("", "【守护者】", cursor.getString(cursor.getColumnIndex("name")), atk, hp);
            monster.setElement(Element.valueOf(cursor.getString(cursor.getColumnIndex("element"))));
            return monster;
        }
        return null;
    }

    public static Stack<String> getPalaceListString() {
        Stack<String> palaces = new Stack<String>();
        Cursor cursor = DBHelper.getDbHelper().excuseSOL("SELECT name, lev, hello FROM palace ORDER BY lev DESC");
        while (!cursor.isAfterLast()) {
            String name1 = cursor.getString(cursor.getColumnIndex("name"));
            if(name1.startsWith("0x")){
                name1 = StringUtils.toStringHex(name1);
            }
            palaces.push(cursor.getString(cursor.getColumnIndex("lev")) + " - <font color=\"red\">" + name1 + "</font> (" + cursor.getString(cursor.getColumnIndex("element")) + ")---" + cursor.getString(cursor.getColumnIndex("hello")));
            cursor.moveToNext();
        }
        return palaces;
    }

    public static Stack<PalaceMonster> getPalaceMonster() {
        Stack<PalaceMonster> stack = new Stack<PalaceMonster>();
        Cursor cursor = DBHelper.getDbHelper().excuseSOL("SELECT * FROM palace ORDER BY lev DESC");
        while (!cursor.isAfterLast()) {
            PalaceMonster monster = new PalaceMonster();
            monster.name = cursor.getString(cursor.getColumnIndex("name"));
            monster.hp = Long.parseLong(cursor.getString(cursor.getColumnIndex("hp")));
            monster.atk = Long.parseLong(cursor.getString(cursor.getColumnIndex("atk")));
            monster.def = Long.parseLong(cursor.getString(cursor.getColumnIndex("def")));
            cursor.moveToNext();
        }
        return stack;
    }

    private String name;
    private long hp, atk, def;
    private Skill skill1, skill2, skill3;

    public Long getAttackValue() {
        return atk;
    }

    public void addHp(long hp) {
        this.hp += hp;
    }

    public String getFormatName() {
        return "<font color=\"#800080\">" + name + "</font>";
    }
}
