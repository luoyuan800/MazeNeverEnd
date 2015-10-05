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

    public Accessory build(List<Item> items) {
        if (this.items != items || !items.containsAll(this.items)) {
            detect(items);
        }
        int p = random.nextInt(100);
        if (a1 != null && p < a1.getPro()) {
            build(a1, false);
            return a1;
        } else if (a2 != null && p < a2.getPro()) {
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
        if(names.contains("黑")&&names.contains("白")){
            names.clear();
            names.add("无常");
        }else if(names.containsAll(Arrays.asList("蛇", "鼠"))){
            names.clear();
            names.add("风语");
        } else if(names.containsAll(Arrays.asList("鼠", "蚁"))){
            names.clear();
            names.add("缥缈");
        } else if(names.containsAll(Arrays.asList("黑","硝"))){
            names.clear();
            names.add("火神");
        }
        StringBuilder b = new StringBuilder();
        for (String s : names) {
            if(b.length() < 3){
                b.append(s);
            }else if (random.nextBoolean()) {
                int start = random.nextInt(2);
                b.replace(start, start +1, s);
            }
        }
        switch (getType()) {
            case RingBuilder.type:
                b.append("戒");
                break;
            case NecklaceBuilder.type:
                b.append("链");
                break;
            case HatBuilder.type:
                b.append("帽");
                break;
        }

        String name = b.toString();
        name = name.replaceAll("硝","火");
        name = name.replaceAll("食","宏");
        if(name.startsWith("龟")){
            name = name.replaceAll("龟","寿");
        }
        accessory.setName(name);
        accessory.setItems(items);
    }

    private void build(Accessory accessory, boolean detectSave) {
        Map<Effect, Number> effectNumberMap = accessory.getEffects();
        if (effectNumberMap == null) effectNumberMap = new EnumMap<Effect, Number>(Effect.class);
        for (Item item : items) {
            Effect effect = item.getEffect();
            if (effect != null) {
                Number number = effectNumberMap.get(effect);
                if (number != null) {
                    effectNumberMap.put(effect, number.longValue() + random.nextLong((number.longValue() + item.getEffectValue().longValue()) / 5 + 1));
                } else {
                    effectNumberMap.put(effect, item.getEffectValue().longValue());
                }
            }
            Effect effect1 = item.getEffect1();
            if (effect1 != null) {
                Number number = effectNumberMap.get(effect1);
                if (number != null) {
                    effectNumberMap.put(effect1, number.longValue() + random.nextLong((number.longValue() + item.getEffect1Value().longValue()) / 5 + 1));
                } else {
                    effectNumberMap.put(effect1, item.getEffect1Value().longValue());
                }
            }
        }
        accessory.setEffects(effectNumberMap);
        detectColor(accessory, detectSave);
        detectElement(accessory);
        accessory.setType(getType());
        for (Item item : items) {
            item.delete();
        }
        accessory.setItems(items);
        accessory.save();
    }

    private void detectColor(Accessory accessory, boolean detectSave) {
        String color = accessory.getColor();
        if (StringUtils.isNotEmpty(color)) {
            return;
        }
        color = "#000000";
        Map<Effect, Number> effectNumberMap = accessory.getEffects();
        for (Effect effect : effectNumberMap.keySet()) {
            switch (effect) {
                case ADD_ATK:
                case ADD_DEF:
                case ADD_UPPER_HP:
                    long l = effectNumberMap.get(effect).longValue();
                    if (l > 10000) {
                        color = "#0000FF";
                    }
                    if (l > 100000) {
                        color = "#9932CC";
                    }
                    if (l > 10000000) {
                        color = "#B8860B";

                    }
                    break;
                case ADD_AGI:
                    case ADD_STR:
                        long sml = effectNumberMap.get(effect).longValue();
                        if (sml > 5000) {
                            color = "#0000FF";
                        }
                        if (sml > 10000) {
                            color = "#9932CC";
                        }
                        if (sml > 100000) {
                            color = "#B8860B";

                        }
                default:
                    color = "#000000";
            }
        }
        accessory.setColor(color);
        if (color.equalsIgnoreCase("#0000FF") && detectSave) {
            accessory.setSave(true);
        }
    }

    private void detectElement(Accessory accessory) {
        EnumMap<Effect, Number> effectNumberEnumMap = new EnumMap<Effect, Number>(Effect.class);
        if (accessory.getEffects() != null) effectNumberEnumMap.putAll(accessory.getEffects());
        if (accessory.getAdditionEffects() != null)
            effectNumberEnumMap.putAll(accessory.getAdditionEffects());
        Effect lessEffect = null;
        for (Effect effect : effectNumberEnumMap.keySet()) {
            if (lessEffect == null) {
                lessEffect = effect;
            } else {
                if (effectNumberEnumMap.get(lessEffect).longValue() > effectNumberEnumMap.get(effect).longValue()) {
                    lessEffect = effect;
                }
            }
        }
        switch (lessEffect) {
            case ADD_ATK:
                accessory.setElement(Element.金);
                break;
            case ADD_DEF:
                accessory.setElement(Element.水);
                break;
            case ADD_UPPER_HP:
                accessory.setElement(Element.木);
                break;
            default:
                accessory.setElement(Element.无);
        }
    }

    public String detect(List<Item> items) {
        a1 = null;
        a2 = null;
        this.items = items;
        Cursor cursor = queryRecipe();
        while (!cursor.isAfterLast()) {
            float pro = 0.0f;
            Set<String> recipeItemSet = new HashSet<String>(5);
            for (String name : StringUtils.split(cursor.getString(cursor.getColumnIndex("items")), "-")) {
                recipeItemSet.add(name.trim());
            }
            int p = 85 / recipeItemSet.size();
            Set<String> itemNames = new HashSet<String>(items.size());
            for (Item item : items) {
                itemNames.add(item.getName().name());
            }
            for (String name : itemNames) {
                if (recipeItemSet.contains(name)) {
                    pro += p;
                }
            }
            Map<Effect, Number> baseEffectsMap = new EnumMap<Effect, Number>(Effect.class);
            for (String str : StringUtils.split(cursor.getString(cursor.getColumnIndex("base")), "-")) {
                String[] keyValue = StringUtils.split(str, ":");
                if (keyValue.length > 1) {
                    baseEffectsMap.put(Effect.valueOf(keyValue[0].trim()), Long.parseLong(keyValue[1]));
                }
            }
            Map<Effect, Number> additionEffectsMap = new EnumMap<Effect, Number>(Effect.class);
            for (String str : StringUtils.split(cursor.getString(cursor.getColumnIndex("addition")), "-")) {
                String[] keyValue = StringUtils.split(str, ":");
                if (keyValue.length > 1) {
                    additionEffectsMap.put(Effect.valueOf(keyValue[0].trim()), Long.parseLong(keyValue[1]));
                }
            }
            if (a1 == null || a1.getPro() < pro) {
                a1 = new Accessory();
                a1.setPro(pro);
                a1.setName(cursor.getString(cursor.getColumnIndex("name")));
                a1.setColor(cursor.getString(cursor.getColumnIndex("color")));
                a1.setAdditionEffects(additionEffectsMap);
                a1.setEffects(baseEffectsMap);
            } else if (a2 == null || a2.getPro() < pro) {
                a2 = new Accessory();
                a2.setPro(pro);
                a2.setName(cursor.getString(cursor.getColumnIndex("name")));
                a2.setColor(cursor.getString(cursor.getColumnIndex("color")));
                a2.setAdditionEffects(additionEffectsMap);
                a2.setEffects(baseEffectsMap);
            }
            cursor.moveToNext();
        }
        if (a1 != null && a2 != null) {
            if ((a1.getPro() + a2.getPro()) >= 100) {
                if (a1.getPro() >= a2.getPro()) {
                    a2.setPro((100 - a1.getPro()) / 2);
                } else {
                    a1.setPro((100 - a2.getPro()) / 2);
                }
            }
            if (a1.getPro() > 100) {
                a1.setPro(100);
                a2 = null;
            } else if (a2.getPro() > 100) {
                a2.setPro(100);
                a1 = null;
            }
        }
        StringBuilder builder = new StringBuilder();
        if (a1 != null && a1.getPro() > 0) {
            builder.append("<font color=\"").append(a1.getColor()).append("\">");
            builder.append(a1.getName()).append(" : ").append(a1.getPro()).append("%</font>");
        }
        if (a2 != null && a2.getPro() > 0) {
            builder.append("<br>").append("<font color=\"").append(a2.getColor()).append("\">");
            builder.append(a2.getName()).append(" : ").append(a2.getPro()).append("%</font>");
        }
        float normalP = 100.0f;
        if (a1 != null && a2 != null && (a1.getPro() + a2.getPro()) < 100) {
            normalP = 100 - a1.getPro() - a2.getPro();
        } else if (a2 == null && a1 != null) {
            normalP = 100 - a1.getPro();
        } else if (a1 == null && a2 != null) {
            normalP = 100 - a2.getPro();
        }
        if (normalP > 0) {
            builder.append("<br>").append("??? : ").append(normalP);
        }
        return builder.toString();
    }

    public abstract int getType();

    public abstract Cursor queryRecipe();
}
