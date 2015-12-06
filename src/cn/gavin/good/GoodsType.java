package cn.gavin.good;

import android.database.Cursor;
import cn.gavin.db.DBHelper;
import cn.gavin.log.LogHelper;
import cn.gavin.pet.Pet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED
 * Created by luoyuan on 12/6/15.
 */
public enum GoodsType {
    Aphrodisiac("奴隶", "使用后选择你队伍中的两只宠物生蛋。被选择的两只宠物亲密度会大幅度降低。",
            new GoodScript() {
                public Pet use() {

                    return null;
                }
            }
    );
    private String name;
    private String desc;
    private GoodScript script;
    private int count;

    private GoodsType(String name, String desc, GoodScript script) {
        this.name = name;
        this.desc = desc;
        this.script = script;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    private void setDesc(String desc) {
        this.desc = desc;
    }

    public GoodScript getScript() {
        return script;
    }

    public void save() {
        DBHelper.getDbHelper().excuseSQLWithoutResult(
                String.format("REPLACE INTO goods (name,count) values ('%s',%s)", name(), count));
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public static GoodsType loadByName(String name) {
        GoodsType goods = valueOf(name);
        Cursor cursor = DBHelper.getDbHelper().excuseSOL("SELECT * FROM goods where name = '" + name + "'");
        if (!cursor.isAfterLast()) {
            goods.setCount(cursor.getInt(cursor.getColumnIndex("count")));
        }
        cursor.close();
        return goods;
    }

    public static List<GoodsType> loadAll() {
        try {
            Cursor cursor = DBHelper.getDbHelper().excuseSOL("SELECT * FROM goods WHERE count > 0");
            List<GoodsType> list = new ArrayList<GoodsType>(cursor.getCount());
            while(!cursor.isAfterLast()){
                try{
                GoodsType goodsType = valueOf(cursor.getString(cursor.getColumnIndex("name")));
                goodsType.setCount(cursor.getInt(cursor.getColumnIndex("count")));
                list.add(goodsType);
                }catch (Exception e){
                    LogHelper.logException(e);
                    e.printStackTrace();
                }
                cursor.moveToNext();
            }
            cursor.close();
            return list;
        } catch (Exception e) {
            LogHelper.logException(e);
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}