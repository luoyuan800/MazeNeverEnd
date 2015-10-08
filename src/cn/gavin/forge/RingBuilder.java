package cn.gavin.forge;

import android.database.Cursor;
import cn.gavin.db.DBHelper;
import cn.gavin.forge.list.ItemName;

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

    @Override
    public boolean isEnough() {
        boolean containOne = false;
        for (Item item : getItems()) {
            if (item.getName().getType() == ItemName.木材 || item.getName().getType() == ItemName.石头) {
                if (!containOne) {
                    containOne = true;
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String notEnough(){
        return "打造材料中缺少木材或者石头";
    }
}
