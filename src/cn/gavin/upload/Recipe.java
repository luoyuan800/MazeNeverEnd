package cn.gavin.upload;

import android.database.Cursor;

import cn.bmob.v3.BmobObject;
import cn.gavin.db.DBHelper;
import cn.gavin.log.LogHelper;
import cn.gavin.utils.StringUtils;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 10/27/2015.
 */
public class Recipe extends BmobObject {
    private String name;
    private String items;
    private String base;
    private String addition;
    private Boolean found = false;
    private Boolean user = false;
    private Integer type;
    private String color;

    public void save(){
        String sql = String.format("REPLACE INTO recipe (name, items, base, addition, found, user, type, color) values ('%s', '%s','%s','%s','%s','%s','%s','%s')",
                name,items,base,addition,found.toString(),user.toString(),type,color);
        DBHelper.getDbHelper().excuseSQLWithoutResult(sql);
    }

    public void upload(){

    }

    public void load(){
        try {
            if (StringUtils.isNotEmpty(name)) {
                Cursor cursor = DBHelper.getDbHelper().excuseSOL("SELECT * FROM recipe WHERE name = '" + name + "'");
                if (!cursor.isAfterLast()) {
                    items = cursor.getString(cursor.getColumnIndex("items"));
                    base = cursor.getString(cursor.getColumnIndex("base"));
                    addition = cursor.getString(cursor.getColumnIndex("addition"));
                    found = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("found")));
                    user = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("user")));
                    type = StringUtils.toInt((cursor.getString(cursor.getColumnIndex("type"))));
                    color = cursor.getString(cursor.getColumnIndex("color"));
                }
            }
        }catch (Exception e){
            LogHelper.logException(e, false);
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getAddition() {
        return addition;
    }

    public void setAddition(String addition) {
        this.addition = addition;
    }

    public Boolean getFound() {
        return found;
    }

    public void setFound(Boolean found) {
        this.found = found;
    }

    public Boolean getUser() {
        return user;
    }

    public void setUser(Boolean user) {
        this.user = user;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
