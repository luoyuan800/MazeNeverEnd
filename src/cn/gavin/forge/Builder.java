package cn.gavin.forge;

import android.database.Cursor;
import cn.gavin.forge.effect.Effect;
import cn.gavin.utils.Random;
import cn.gavin.utils.StringUtils;

import java.util.*;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 9/25/2015.
 */
public abstract class Builder {
    private Random random = new Random();
    public Accessory a1;
    public Accessory a2;
    public List<Item> items;

    public Accessory builder(List<Item> items) {
        if (this.items != items || !items.containsAll(this.items)) {
            detect(items);
        }
        int p = random.nextInt(100);
        if (p > a1.getPro()) {
            build(a1, false);
            return a1;
        } else if (p > a2.getPro()) {
            build(a2, false);
            return a2;
        } else {
            Accessory accessory = new Accessory();
            buildName(accessory);
            build(accessory, true);
            return accessory;
        }
    }

    private void buildName(Accessory accessory) {
        Set<String> names = new HashSet<String>(5);
        for (Item item : items) {
            names.add(item.getName().name().charAt(0) + "");
        }
        StringBuilder b = new StringBuilder();
        for (String s : names) {
            b.append(s);
        }
        accessory.setName(b.toString());
        accessory.setItems(items);
    }

    private void build(Accessory accessory, boolean detectSave) {
        Map<Effect, Number> effectNumberMap = new EnumMap<Effect, Number>(Effect.class);
        for (Item item : items) {
            Number number = effectNumberMap.get(item.getEffect());
            if (number != null) {
                effectNumberMap.put(item.getEffect(), number.longValue() + random.nextLong((number.longValue() + item.getEffectValue().longValue()) / 5 + 1));
            } else {
                effectNumberMap.put(item.getEffect(), item.getEffectValue().longValue());
            }
            number = effectNumberMap.get(item.getEffect1());
            if (number != null) {
                effectNumberMap.put(item.getEffect1(), number.longValue() + random.nextLong((number.longValue() + item.getEffect1Value().longValue()) / 5 + 1));
            } else {
                effectNumberMap.put(item.getEffect1(), item.getEffect1Value().longValue());
            }
        }
        String color = "";
        for (Effect effect : effectNumberMap.keySet()) {
            switch (effect) {
                case ADD_ATK:
                case ADD_DEF:
                case ADD_UPPER_HP:
                    long l = effectNumberMap.get(effect).longValue();
                    if (l > 10000) {
                        color = "blue";
                    }
                    if(l > 1000000){
                        color = "golden";

                    }
                    break;
            }
        }
        if(color.equalsIgnoreCase("golden") && detectSave){
            accessory.setSave(true);
        }
        accessory.setEffects(effectNumberMap);
    }

    public String detect(List<Item> items) {
        this.items = items;
        Cursor cursor = queryRecipe();
        while (!cursor.isAfterLast()) {
            float pro = 0.0f;
            Set<String> recipeItemSet = new HashSet<String>(5);
            for (String name : StringUtils.split(cursor.getString(cursor.getColumnIndex("items")), "_")) {
                recipeItemSet.add(name.trim());
            }
            int p = 85 / recipeItemSet.size();
            for (Item item : items) {
                if (recipeItemSet.contains(item.getName().name())) {
                    pro += p;
                }
            }
            if (a1 == null || a1.getPro() < pro) {
                a1 = new Accessory();
                a1.setPro(pro);
                a1.setName(cursor.getString(cursor.getColumnIndex("name")));
                a1.setColor(cursor.getString(cursor.getColumnIndex("color")));
            } else if (a2 == null || a2.getPro() < pro) {
                a2 = new Accessory();
                a2.setPro(pro);
                a1.setName(cursor.getString(cursor.getColumnIndex("name")));
                a1.setColor(cursor.getString(cursor.getColumnIndex("color")));
            }
            cursor.moveToNext();
        }
        if ((a1.getPro() + a2.getPro()) >= 100) {
            if (a1.getPro() >= a2.getPro()) {
                a2.setPro((100 - a1.getPro()) / 2);
            } else {
                a1.setPro((100 - a2.getPro()) / 2);
            }
        }
        StringBuilder builder = new StringBuilder();
        builder.append("<font color=\"").append(a1.getColor()).append("\">");
        builder.append(a1.getName()).append(" : ").append(a1.getPro()).append("%</font>");
        builder.append("<br>").append("<font color=\"").append(a2.getColor()).append("\">");
        builder.append(a2.getName()).append(" : ").append(a2.getPro()).append("%</font>");
        builder.append("<br>").append("??? : ").append(100 - a1.getPro() - a2.getPro());
        return builder.toString();
    }

    public abstract int getType();

    public abstract Cursor queryRecipe();
}
