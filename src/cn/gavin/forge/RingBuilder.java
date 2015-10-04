package cn.gavin.forge;

import android.database.Cursor;
import cn.gavin.db.DBHelper;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 9/25/2015.
 */
public class RingBuilder extends Builder {
    public static final int type = 1;
    public Cursor queryRecipe(){
        String sql = "SELECT * from recipe WHERE type = 1";
        return DBHelper.getDbHelper().excuseSOL(sql);
    }

    public int getType(){
        return type;
    }
}
