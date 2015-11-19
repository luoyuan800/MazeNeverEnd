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
public class HatBuilder extends Builder {
    public final static int type = 0;

    @Override
    public boolean isEnough() {
        boolean containOne = false;
        for (Item item : getItems()) {
            if(item.getName().getType() == ItemName.皮毛 || item.getName().getType() == ItemName.筋){
                if(!containOne){
                    containOne = true;
                }else{
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public Cursor queryRecipe() {
        String sql = "SELECT * from recipe WHERE type = 0";
        return DBHelper.getDbHelper().excuseSOL(sql);
    }

    @Override
    public String notEnough(){
        return "打造材料中缺少皮毛或者筋";
    }
}
