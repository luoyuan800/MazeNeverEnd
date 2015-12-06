package cn.gavin.db.good;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.gavin.utils.MazeContents;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 11/24/2015.
 */
public class GoodManager {
    public static void buildGoodsDB(SQLiteDatabase sqLiteDatabase){
        String createTable = "CREATE TABLE goods(" +
                "name TEXT NOT NULL PRIMARY KEY," +
                "count TEXT," +
                "class TEXT" +
                ")";
        sqLiteDatabase.execSQL(createTable);
        sqLiteDatabase.execSQL("CREATE UNIQUE INDEX goods_index ON goods (name)");
    }
}
