package cn.gavin.palace;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Message;

import java.util.List;
import java.util.Stack;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.gavin.Element;
import cn.gavin.activity.BaseContext;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.db.DBHelper;
import cn.gavin.monster.Monster;
import cn.gavin.palace.nskill.NSkill;
import cn.gavin.upload.PalaceObject;
import cn.gavin.utils.StringUtils;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 10/9/2015.
 */
public class PalaceMonster extends Base {
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
                "re_count TEXT," +
                "skill TEXT," +
                "skill1 TEXT," +
                "skill2 TEXT," +
                "parry TEXT," +
                "hit_rate TEXT," +
                "pay TEXT," +
                "hello TEXT" +
                ")";
        db.execSQL(createTable);
        db.execSQL("CREATE UNIQUE INDEX palace_lev ON palace (name, lev)");
    }

    public static Monster getDefender(long lev) {
        Cursor cursor = DBHelper.getDbHelper().excuseSOL("SELECT * FROM palace WHERE lev = '" + lev + "'");
        if (!cursor.isAfterLast()) {
            long atk = StringUtils.toLong(cursor.getString(cursor.getColumnIndex("atk")));
            long hp = StringUtils.toLong(cursor.getString(cursor.getColumnIndex("hp"))) + StringUtils.toLong(cursor.getString(cursor.getColumnIndex("def")));
            Monster monster = new Monster("", "【守护者】", cursor.getString(cursor.getColumnIndex("name")), atk, hp);
            monster.setElement(Element.valueOf(cursor.getString(cursor.getColumnIndex("element"))));
            return monster;
        }
        return null;
    }

    public static Stack<String> getPalaceListString() {
        Stack<String> palaces = new Stack<String>();
        Cursor cursor = DBHelper.getDbHelper().excuseSOL("SELECT name, lev, hello, element FROM palace ORDER BY lev DESC");
        while (!cursor.isAfterLast()) {
            String name1 = cursor.getString(cursor.getColumnIndex("name"));
            if (name1.startsWith("0x")) {
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
            monster.setElement(Element.valueOf(cursor.getString(cursor.getColumnIndex("element"))));
            monster.setName(cursor.getString(cursor.getColumnIndex("name")));
            monster.setHello(cursor.getString(cursor.getColumnIndex("hello")));
            monster.setLev(StringUtils.toLong(cursor.getString(cursor.getColumnIndex("lev"))));
            monster.setHp(StringUtils.toLong(cursor.getString(cursor.getColumnIndex("hp"))));
            monster.setDef(StringUtils.toLong(cursor.getString(cursor.getColumnIndex("def"))));
            monster.setAtk(StringUtils.toLong(cursor.getString(cursor.getColumnIndex("atk"))));
            monster.setHit(StringUtils.toLong(cursor.getString(cursor.getColumnIndex("hit_rate"))).intValue());
            monster.setParry(StringUtils.toLong(cursor.getString(cursor.getColumnIndex("parry"))).intValue());
            monster.setDodge(5);
            String skill1 = cursor.getString(cursor.getColumnIndex("skill"));
            String skill11 = cursor.getString(cursor.getColumnIndex("skill1"));
            String skill12 = cursor.getString(cursor.getColumnIndex("skill2"));
            String[] strings = StringUtils.split(skill1, "_");
            if (strings.length > 1) {
                monster.addSkill(NSkill.createSkillByName(strings[0],
                        monster, StringUtils.toLong(strings[1]), null));
            }
            strings = StringUtils.split(skill11, "_");
            if (strings.length > 1) {
                monster.addSkill(NSkill.createSkillByName(strings[0],
                        monster, StringUtils.toLong(strings[1]), null));
            }
            strings = StringUtils.split(skill12, "_");
            if (strings.length > 1) {
                monster.addSkill(NSkill.createSkillByName(strings[0],
                        monster, StringUtils.toLong(strings[1]), null));
            }
            stack.push(monster);
            cursor.moveToNext();
        }
        PalaceMonster dog = new PalaceMonster();
        dog.setName("看门人");
        dog.setLev(1);
        dog.setHello("我是殿堂看门人，如果你连我也打不过，就不要进去了！");
        dog.setElement(Element.无);
        dog.setHp(10000);
        dog.setDef(10000l);
        dog.setAtk(10000l);
        dog.setDodge(20);
        dog.setHit(15);
        dog.setParry(15);
        stack.push(dog);
        return stack;
    }

    private long lev;
    private String hello;

    public void setLev(long lev) {
        this.lev = lev;
    }

    public String getHello() {
        return hello;
    }

    public void setHello(String hello) {
        this.hello = hello;
    }

    public long getLev() {
        return lev;
    }

    public NSkill getAtkSkill() {
        NSkill skill = super.getAtkSkill();
        if (skill != null) {
            getContext().getHandler().sendEmptyMessage(11);
        }
        return skill;
    }

    public String getFormatName() {
        return "<font color=\"red\">" + getName() + "</font>(" + getElement() + ")";
    }

}
