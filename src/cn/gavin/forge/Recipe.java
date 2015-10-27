package cn.gavin.forge;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Message;
import android.util.Log;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.db.DBHelper;
import cn.gavin.upload.PalaceObject;
import cn.gavin.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED
 * Created by luoyuan on 10/6/15.
 */
public class Recipe extends BmobObject {
    private String name;
    private String items;
    private Boolean found;
    private Boolean user;
    private Integer type;
    private String color;
    private String base;
    private String addition;

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

    public Boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }

    public Boolean isUser() {
        return user;
    }

    public void setUser(boolean user) {
        this.user = user;
    }

    public Integer getType() {
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

    public void upload(final ProgressDialog dialog){
        if(user) {
            save(dialog.getContext(), new SaveListener() {
                @Override
                public void onSuccess() {
                    dialog.dismiss();
                }

                @Override
                public void onFailure(int i, String s) {
                    dialog.dismiss();
                }
            });
        }
    }

    public static void downland(final ProgressDialog dialog){
        BmobQuery<Recipe> query = new BmobQuery<Recipe>();
        query.setLimit(100);
        query.findObjects(dialog.getContext(), new FindListener<Recipe>() {
            @Override
            public void onSuccess(final List<Recipe> recipes) {
                DBHelper.getDbHelper().excuseSQLWithoutResult("DELETE FROM palace");
                for (Recipe object : recipes) {
                    object.save();
                }
                dialog.dismiss();
            }

            @Override
            public void onError(int i, String s) {
                dialog.dismiss();
            }
        });
    }

    public void save(){
        String sql = String.format("REPLACE INTO recipe (name, items, base, addition, found, user, type, color) values ('%s', '%s','%s','%s','%s','%s','%s','%s')",
                name,items,base,addition,found.toString(),user.toString(),type,color);
        DBHelper.getDbHelper().excuseSQLWithoutResult(sql);
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
}
