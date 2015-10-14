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
import cn.gavin.activity.BaseContext;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.db.DBHelper;
import cn.gavin.upload.PalaceObject;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 10/9/2015.
 */
public class PalaceMonster {

    public static void updatePalace(final MainGameActivity context) {
        BmobQuery<PalaceObject> query = new BmobQuery<PalaceObject>();
        query.setLimit(context.getPalaceCount());
        query.findObjects(context, new FindListener<PalaceObject>() {
            @Override
            public void onSuccess(final List<PalaceObject> palaceObjects) {
                context.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        for (PalaceObject object : palaceObjects) {
                            object.save();
                        }
                    }
                });
                context.getHandler().sendEmptyMessage(105);
            }

            @Override
            public void onError(int i, String s) {

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
                message.what = 203;
                message.arg1 = 10;
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
        String addDefender = "INSERT INTO palace(id, name, lev, hp, atk, def,skill, skill1,skill2,parry,hit_rate,pay,hello) values('1234567890fs','我是神','10000','1931377600','1082065000','10000000','重击_5','多重攻击_5','铁拳_5','10','50','1000','你是不可能超越我的！')";
        db.execSQL(addDefender);
    }

    public static Monster getDefender(long lev) {
        Cursor cursor = DBHelper.getDbHelper().excuseSOL("SELECT * FROM palace WHERE lev = '" + lev + "'");
        if (!cursor.isAfterLast()) {
            long atk = Long.parseLong(cursor.getString(cursor.getColumnIndex("atk")));
            long hp = Long.parseLong(cursor.getString(cursor.getColumnIndex("hp"))) + Long.parseLong(cursor.getString(cursor.getColumnIndex("def")));
            return new Monster("", "【守护者】", cursor.getString(cursor.getColumnIndex("name")), atk, hp);
        }
        return null;
    }

    public static Stack<String> getPalaceListString() {
        Stack<String> palaces = new Stack<String>();
        Cursor cursor = DBHelper.getDbHelper().excuseSOL("SELECT name, lev, hello FROM palace ORDER BY lev DESC");
        while (!cursor.isAfterLast()) {
            palaces.push(cursor.getString(cursor.getColumnIndex("lev")) + " - <font color=\"red\">" + cursor.getString(cursor.getColumnIndex("name")) + "</font> " + "---" + cursor.getString(cursor.getColumnIndex("hello")));
            cursor.moveToNext();
        }
        return palaces;
    }
}
