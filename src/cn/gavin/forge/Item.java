package cn.gavin.forge;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import cn.gavin.Hero;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.db.DBHelper;
import cn.gavin.forge.effect.Effect;
import cn.gavin.forge.list.ItemName;
import cn.gavin.log.LogHelper;
import cn.gavin.maze.Maze;
import cn.gavin.monster.Monster;
import cn.gavin.utils.MazeContents;
import cn.gavin.utils.Random;
import cn.gavin.utils.StringUtils;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 9/25/2015.
 */
public class Item implements Comparator<Item> {
    private ItemName name;
    private Effect effect;
    private Effect effect1;
    private Number effectValue;
    private Number effect1Value;
    private String id;

    public String toString() {
        StringBuilder builder = new StringBuilder(name.name());
        builder.append("<br>");
        if (effect != null)
            builder.append(effect.getName()).append(":").append(effectValue).append("<br>");
        if (effect1 != null) builder.append(effect1.getName()).append(":").append(effect1Value);
        return builder.toString();
    }

    public String buildProperties() {
        StringBuilder builder = new StringBuilder(name.name());
        builder.append("<br>");
        if (effect != null)
            builder.append(effect.name()).append(":").append(effectValue).append("<br>");
        if (effect1 != null) builder.append(effect1.name()).append(":").append(effect1Value);
        return builder.toString();
    }

    public ItemName getName() {
        return name;
    }

    public void setName(ItemName name) {
        this.name = name;
    }

    public Effect getEffect1() {
        return effect1;
    }

    public void setEffect1(Effect effect1) {
        this.effect1 = effect1;
    }

    public Effect getEffect() {
        return effect;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }

    public Number getEffect1Value() {
        return effect1Value;
    }

    public void setEffect1Value(Number effect1Value) {
        this.effect1Value = effect1Value;
    }

    public Number getEffectValue() {
        return effectValue;
    }

    public void setEffectValue(Number effectValue) {
        this.effectValue = effectValue;
    }

    public static Item buildItem(Hero hero, ItemName name) {
        Item item = new Item();
        item.setName(name);
        Random random = hero.getRandom();
        Effect e = Effect.randomEffect(random);
        if (e == Effect.ADD_CLICK_POINT_AWARD && random.nextInt(100000000) != 999) {
            e = Effect.values()[e.ordinal() - 1];
        }
        item.setEffect(e);
        item.setEffectValue(e.calculate(hero));
        if (random.nextBoolean()) {
            Effect e1 = Effect.randomEffect(random);
            if (e1 == Effect.ADD_CLICK_POINT_AWARD && random.nextInt(10000000) != 999) {
                e1 = Effect.values()[e1.ordinal() + 1];
            }
            if (e1 != e) {
                item.setEffect1(e1);
                item.setEffect1Value(e1.calculate(hero));
            }
        }
        return item;
    }

    public static Item buildItem(Hero hero, Maze maze, Monster monster) {
        if (1000 < getItemCount()) {
            return null;
        }
        ItemName name = null;
        for (ItemName in : monster.getItems()) {
            if (in.perform(hero, monster)) {
                name = in;
                break;
            }
        }
        if (name == null) return null;
        Item item = new Item();
        item.setName(name);
        Random random = hero.getRandom();
        Effect e = Effect.randomEffect(random);
        if (e == Effect.ADD_CLICK_POINT_AWARD && random.nextInt(100000000) != 999) {
            e = Effect.values()[e.ordinal() - 1];
        }
        item.setEffect(e);
        item.setEffectValue(e.calculate(hero, monster));
        if (maze.getLev() > 15 && random.nextBoolean()) {
            Effect e1 = Effect.randomEffect(random);
            if (e == Effect.ADD_CLICK_POINT_AWARD && random.nextInt(10000000) != 999) {
                e = Effect.values()[e.ordinal() + 1];
            }
            if (e1 != e) {
                item.setEffect1(e1);
                item.setEffect1Value(e1.calculate(hero, monster));
            }
        }
        return item;
    }

    public static int getItemCount() {
        try {
            return DBHelper.getDbHelper().excuseSOL("SELECT count(*) FROM item").getInt(0);
        } catch (Exception e) {
            LogHelper.logException(e);
            return 0;
        }
    }

    public boolean idEqual(Item item) {
        return StringUtils.isNotEmpty(id) && item != null && id.equals(item.id);
    }

    public static ArrayList<Item> loadItems(SQLiteDatabase db) {
        DBHelper dbHelper = DBHelper.getDbHelper();
        String sql = "SELECT * FROM item";
        ArrayList<Item> items = new ArrayList<Item>();
        try {
            Cursor cursor;
            if (db != null) {
                cursor = db.rawQuery(sql, null);
                cursor.moveToFirst();
            } else {
                cursor = dbHelper.excuseSOL(sql);
            }
            while (!cursor.isAfterLast()) {
                Item item = new Item();
                item.id = cursor.getString(cursor.getColumnIndex("id"));
                try {
                    ItemName name = ItemName.valueOfName(cursor.getString(cursor.getColumnIndex("name")));
                    item.setName(name);
                    String properties = cursor.getString(cursor.getColumnIndex("properties"));
                    if (StringUtils.isNotEmpty(properties)) {
                        String[] props = properties.split("<br>");
                        for (String pro : props) {
                            String[] proVal = pro.split(":");
                            if (proVal.length > 1) {
                                Effect e = Effect.valueOf(proVal[0]);
                                Long value = StringUtils.toLong(proVal[1]);
                                if (e == Effect.ADD_PER_ATK || e == Effect.ADD_PER_DEF || e == Effect.ADD_PER_UPPER_HP) {
                                    if (value > 20) value = 20l;
                                }
                                if (item.getEffect() == null) {
                                    item.setEffect(e);
                                    item.setEffectValue(value);
                                } else {
                                    item.setEffect1(e);
                                    item.setEffect1Value(value);
                                }
                            }
                        }
                    }
                    items.add(item);
                } catch (Exception e) {
                    item.delete(null);
                }
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(MainGameActivity.TAG, "loadItems", e);
            LogHelper.logException(e);
        }
        return items;
    }

    private static void rebuildItemTable() {
        try {
            MazeContents.hero.setNecklace(null);
            MazeContents.hero.setRing(null);
            MazeContents.hero.setHat(null);
            DBHelper.getDbHelper().excuseSQLWithoutResult("DROP TABLE item");
            DBHelper.getDbHelper().excuseSQLWithoutResult("DROP TABLE recipe");
            DBHelper.getDbHelper().excuseSQLWithoutResult("DROP TABLE accessory");
            new ForgeDB().createTable(DBHelper.getDbHelper().getDatabase());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(MainGameActivity.TAG, "RebuildTable", e);
            LogHelper.writeLog();
        }
    }

    public void save(SQLiteDatabase db) {
        String value = UUID.randomUUID().toString();
        String sql = String.format("INSERT INTO item (id,name,properties) values ('%s','%s', '%s')", value, name.name(), buildProperties());
        if (db == null) {
            DBHelper dbHelper = DBHelper.getDbHelper();
            dbHelper.excuseSQLWithoutResult(sql);
        } else {
            try {
                db.execSQL(sql);
            } catch (Exception e) {
                e.printStackTrace();
                LogHelper.logException(e);
            }
        }
        id = value;
    }

    public void delete(SQLiteDatabase db) {
        String sql = String.format("DELETE FROM item WHERE id = '%s'", id);
        if (db == null) {
            DBHelper dbHelper = DBHelper.getDbHelper();
            dbHelper.excuseSQLWithoutResult(sql);
        } else {
            try {
                db.execSQL(sql);
            } catch (Exception e) {
                LogHelper.logException(e);
            }
        }
    }

    @Override
    public int compare(Item lhs, Item rhs) {
        return lhs.getName().name().compareTo(rhs.getName().name());
    }

    public String getEffectString() {
        StringBuilder builder = new StringBuilder();
        if (effect != null)
            builder.append(effect.getName()).append(":").append(effectValue).append("<br>");
        if (effect1 != null) builder.append(effect1.getName()).append(":").append(effect1Value);
        return builder.toString();
    }

    public static List<Item> loadByLimit(int start, int size, String query){
        String sql = "select * from item " + query + " order by name limit " + size + " offset " +  start;
        Cursor cursor = DBHelper.getDbHelper().excuseSOL(sql);
        List<Item> items = new ArrayList<Item>(cursor.getCount());
        while (!cursor.isAfterLast()) {
            Item item = new Item();
            item.id = cursor.getString(cursor.getColumnIndex("id"));
            try {
                ItemName name = ItemName.valueOfName(cursor.getString(cursor.getColumnIndex("name")));
                item.setName(name);
                String properties = cursor.getString(cursor.getColumnIndex("properties"));
                if (StringUtils.isNotEmpty(properties)) {
                    String[] props = properties.split("<br>");
                    for (String pro : props) {
                        String[] proVal = pro.split(":");
                        if (proVal.length > 1) {
                            Effect e = Effect.valueOf(proVal[0]);
                            Long value = StringUtils.toLong(proVal[1]);
                            if (e == Effect.ADD_PER_ATK || e == Effect.ADD_PER_DEF || e == Effect.ADD_PER_UPPER_HP) {
                                if (value > 20) value = 20l;
                            }
                            if (item.getEffect() == null) {
                                item.setEffect(e);
                                item.setEffectValue(value);
                            } else {
                                item.setEffect1(e);
                                item.setEffect1Value(value);
                            }
                        }
                    }
                }
                items.add(item);
            } catch (Exception e) {
                item.delete(null);
            }
            cursor.moveToNext();
        }
        cursor.close();
        return items;
    }
}
