package cn.gavin.forge;

import android.database.Cursor;
import cn.gavin.db.DBHelper;
import cn.gavin.forge.list.ItemName;

import java.util.List;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 9/25/2015.
 */
public class NecklaceBuilder extends Builder {
    public final static int type = 2;
    @Override
    public int getType() {
        return type;
    }

    @Override
    public Cursor queryRecipe() {
        String sql = "SELECT * from recipe WHERE type = 2";
        return DBHelper.getDbHelper().excuseSOL(sql);
    }

    @Override
    public boolean isEnough() {
        boolean containOne = false;
        for (Item item : getItems()) {
            if (item.getName().getType() == ItemName.骨头 || item.getName().getType() == ItemName.筋) {
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
        return "打造材料中缺少骨头或者筋";
    }
}
