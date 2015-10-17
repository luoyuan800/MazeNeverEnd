package cn.gavin.forge;

import android.database.Cursor;
import android.util.Log;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.db.DBHelper;
import cn.gavin.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED
 * Created by luoyuan on 10/6/15.
 */
public class Recipe {
    private String name;
    private String items;
    private boolean found;
    private boolean user;
    private int type;
    private String color;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItems() {
        if (!found && user) {
            return items.substring(0, items.lastIndexOf("+")) + "+ ?";
        }
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }

    public boolean isUser() {
        return user;
    }

    public void setUser(boolean user) {
        this.user = user;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("<font color=\"");
        builder.append(color).append("\">");
        builder.append(StringUtils.split(name,"<br>")[0]);
        builder.append("=").append(getItems());
        builder.append("</font>");
        return builder.toString();
    }

    public static List<Recipe> loadRecipes() {
        List<Recipe> recipeList = new ArrayList<Recipe>();
        try {
            Cursor cursor = DBHelper.getDbHelper().excuseSOL("SELECT name,items,found,user,type,color FROM recipe WHERE found = 'true' OR user = 'true'");
            while (!cursor.isAfterLast()) {
                Recipe recipe = new Recipe();
                recipe.name = cursor.getString(cursor.getColumnIndex("name"));
                recipe.items = cursor.getString(cursor.getColumnIndex("items"));
                if (recipe.items != null) {
                    recipe.items = recipe.items.replaceAll("-", "+");
                }
                recipe.found = Boolean.getBoolean(cursor.getString(cursor.getColumnIndex("found")));
                recipe.user = Boolean.getBoolean(cursor.getString(cursor.getColumnIndex("user")));
                recipe.type = cursor.getInt(cursor.getColumnIndex("type"));
                recipe.color = cursor.getString(cursor.getColumnIndex("color"));
                recipeList.add(recipe);
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(MainGameActivity.TAG, "loadRecipe", e);
        }
        return recipeList;
    }
}
