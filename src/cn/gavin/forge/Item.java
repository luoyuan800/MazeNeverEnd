package cn.gavin.forge;

import android.database.Cursor;
import cn.gavin.Hero;
import cn.gavin.Maze;
import cn.gavin.db.DBHelper;
import cn.gavin.forge.effect.Effect;
import cn.gavin.forge.list.ItemName;
import cn.gavin.monster.Monster;
import cn.gavin.utils.StringUtils;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 9/25/2015.
 */
public class Item {
    private ItemName name;
    private Effect effect;
    private Effect effect1;
    private Number effectValue;
    private Number effect1Value;
    private String id;

    public String toString() {
        StringBuilder builder = new StringBuilder(name.name());
        builder.append("<br>");
        if (effect != null) builder.append(effect.getName()).append(":").append(effectValue).append("<br>");
        if (effect1 != null) builder.append(effect1.getName()).append(":").append(effect1Value);
        return builder.toString();
    }

    public String buildProperties(){
        StringBuilder builder = new StringBuilder(name.name());
        builder.append("<br>");
        if (effect != null) builder.append(effect.name()).append(":").append(effectValue).append("<br>");
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

    public static Item buildItem(Hero hero, ItemName name){
        Item item = new Item();
        item.setName(name);
        Random random = hero.getRandom();
        Effect e = Effect.values()[random.nextInt(Effect.values().length)];
        item.setEffect(e);
        item.setEffectValue(1000);
        if (random.nextBoolean()) {
            Effect e1 = Effect.values()[random.nextInt(Effect.values().length)];
            if (e1 != e) {
                item.setEffect1(e1);
                item.setEffect1Value(1000);
            }
        }
        return item;
    }

    public static Item buildItem(Hero hero, Maze maze, Monster monster) {
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
        Effect e = Effect.values()[random.nextInt(Effect.values().length)];
        item.setEffect(e);
        item.setEffectValue(e.calculate(hero, monster));
        if (maze.getLev() > 15 && random.nextBoolean()) {
            Effect e1 = Effect.values()[random.nextInt(Effect.values().length)];
            if (e1 != e) {
                item.setEffect1(e1);
                item.setEffect1Value(e1.calculate(hero, monster));
            }
        }
        return item;
    }

    public static ArrayList<Item> loadItems() {
        DBHelper dbHelper = DBHelper.getDbHelper();
        String sql = "SELECT * FROM item";
        Cursor cursor = dbHelper.excuseSOL(sql);
        ArrayList<Item> items = new ArrayList<Item>(cursor.getCount());
        while (!cursor.isAfterLast()) {
            ItemName name = ItemName.valueOfName(cursor.getString(cursor.getColumnIndex("name")));
            Item item = new Item();
            item.setName(name);
            item.id = cursor.getString(cursor.getColumnIndex("id"));
            String properties = cursor.getString(cursor.getColumnIndex("properties"));
            if (StringUtils.isNotEmpty(properties)) {
                String[] props = properties.split("<br>");
                for (String pro : props) {
                    String[] proVal = pro.split(":");
                    if (proVal.length > 1) {
                        Effect e = Effect.valueOf(proVal[0]);
                        Long value = Long.parseLong(proVal[1]);
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
            cursor.moveToNext();
        }
        return items;
    }

    public void save() {
        DBHelper dbHelper = DBHelper.getDbHelper();
        String value = UUID.randomUUID().toString();
        String sql = String.format("INSERT INTO item (id,name,properties) values ('%s','%s', '%s')", value, name.name(), buildProperties());
        dbHelper.excuseSQLWithoutResult(sql);
        id = value;
    }

    public void delete() {
        DBHelper dbHelper = DBHelper.getDbHelper();
        String sql = String.format("DELETE FROM item WHERE id = '%s'", id);
        dbHelper.excuseSQLWithoutResult(sql);
    }


    public static Item emptyItem() {
        Item item = new Item();
        item.setName(ItemName.EMPTY);
        return item;
    }
}
