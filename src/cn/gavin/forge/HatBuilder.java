package cn.gavin.forge;

import android.database.Cursor;
import cn.gavin.db.DBHelper;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 9/25/2015.
 */
public class HatBuilder extends Builder {
    public final static int type = 0;
    @Override
    public int getType() {
        return 0;
    }

    @Override
    public Cursor queryRecipe() {
        String sql = "SELECT * from recipe WHERE type = 0";
        return DBHelper.getDbHelper().excuseSOL(sql);
    }
}
