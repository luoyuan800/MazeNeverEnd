package cn.gavin.forge;

import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.gavin.Element;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.gift.Gift;
import cn.gavin.utils.MazeContents;
import cn.gavin.forge.effect.Effect;
import cn.gavin.utils.Random;
import cn.gavin.utils.StringUtils;

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

    public abstract boolean isEnough();

    public Accessory build(List<Item> items, boolean isCheck) {
        if (this.items != items || !items.containsAll(this.items)) {
            detect(items);
        }
        if (!isCheck || isEnough()) {
            int p = random.nextInt(100);
            if(MazeContents.hero!=null){
                p -= random.nextLong(MazeContents.hero.getStrength()/1000000 + 2);
            }
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
        } else {
            return null;
        }
    }

    private void buildName(Accessory accessory) {
        Set<String> names = new HashSet<String>(5);
        for (Item item : items) {
            names.add(item.getName().name().charAt(0) + "");
        }
        if (names.contains("黑") && names.contains("白")) {
            names.clear();
            names.add("无常");
            Map<Effect, Number> addition = accessory.getAdditionEffects();
            if (addition == null) {
                addition = new EnumMap<Effect, Number>(Effect.class);
                accessory.setAdditionEffects(addition);
            }
            addition.put(Effect.ADD_UPPER_HP, 51119);
        } else if (names.containsAll(Arrays.asList("蛇", "鼠"))) {
            names.clear();
            names.add("风语");
        } else if (names.containsAll(Arrays.asList("鼠", "蚁"))) {
            names.clear();
            names.add("缥缈");
        } else if (names.containsAll(Arrays.asList("黑", "硝"))) {
            names.clear();
            names.add("火神");
            Map<Effect, Number> addition = accessory.getAdditionEffects();
            if (addition == null) {
                addition = new EnumMap<Effect, Number>(Effect.class);
                accessory.setAdditionEffects(addition);
            }
            addition.put(Effect.ADD_ATK, 20009);
        } else if (names.containsAll(Arrays.asList("龙", "凤", "虎", "牛"))) {
            names.clear();
            names.add("小田螺の");
            accessory.setColor("#C71585");
            Map<Effect, Number> addition = accessory.getAdditionEffects();
            if (addition == null) {
                addition = new EnumMap<Effect, Number>(Effect.class);
                accessory.setAdditionEffects(addition);
            }
            addition.put(Effect.ADD_DODGE_RATE, 39);
            addition.put(Effect.ADD_PET_ABE, 0.03);
        }
        StringBuilder b = new StringBuilder();
        for (String s : names) {
            if (b.length() < 3) {
                b.append(s);
            } else if (random.nextBoolean()) {
                int start = random.nextInt(2);
                b.replace(start, start + 1, s);
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
        name = name.replaceAll("硝", "火");
        name = name.replaceAll("食", "宏");
        name = name.replaceAll("精", "魔");
        name = name.replaceAll("原", "神");
        name = name.replace("牛蛇", "鬼");
        name = name.replace("蛇牛", "魂");
        name = name.replace("鼠牛", "天煞");
        name = name.replace("牛鼠", "地煞");
        if (name.startsWith("龟")) {
            name = name.replaceAll("龟", "寿");
        }
        accessory.setName(name);
        accessory.setItems(items);

    }

    private void build(Accessory accessory, boolean detectSave) {
        Map<Effect, Number> effectNumberMap = accessory.getEffects();
        if (effectNumberMap == null) {
            effectNumberMap = new EnumMap<Effect, Number>(Effect.class);
        }
        Map<Effect, Number> constantEffectMap = new EnumMap<Effect, Number>(effectNumberMap);
        for (Item item : items) {
            Effect effect = item.getEffect();
            if (effect != null) {
                Number number = effectNumberMap.get(effect);
                if (number != null) {
                    effectNumberMap.put(effect, number.longValue() + random.nextLong((number.longValue() + item.getEffectValue().longValue()) / 2 + 1));
                } else {
                    effectNumberMap.put(effect, item.getEffectValue().longValue());
                }
            }
            Effect effect1 = item.getEffect1();
            if (effect1 != null) {
                Number number = effectNumberMap.get(effect1);
                if (number != null && number.longValue() != 0) {
                    effectNumberMap.put(effect1, number.longValue() + random.nextLong((number.longValue() + item.getEffect1Value().longValue()) / 2 + 1));
                } else {
                    effectNumberMap.put(effect1, item.getEffect1Value().longValue());
                }
            }
        }
        EnumMap<Effect, Number> affectEffects = new EnumMap<Effect, Number>(Effect.class);

        for (EnumMap.Entry<Effect, Number> entry : effectNumberMap.entrySet()) {
            if (affectEffects.size() > 0 && !constantEffectMap.containsKey(entry.getKey())) {
                if (random.nextBoolean()) affectEffects.put(entry.getKey(), entry.getValue());
            } else {
                affectEffects.put(entry.getKey(), entry.getValue());
            }
        }
        accessory.setEffects(affectEffects);
        detectColor(accessory, detectSave);
        detectElement(accessory);
        accessory.setType(getType());
        for (Item item : items) {
            item.delete(null);
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
        boolean save = false;
        for (Effect effect : effectNumberMap.keySet()) {
            switch (effect) {
                case ADD_ATK:
                case ADD_DEF:
                case ADD_UPPER_HP:
                    long l = effectNumberMap.get(effect).longValue();
                    if (l > 10000) {
                        color = "#556B2F";
                    }
                    if (l > 100000) {
                        color = "#9932CC";
                    }
                    if (l > 50000000) {
                        color = "#B8860B";

                    }
                    if (l > MazeContents.hero.getBaseDefense()) {
                        color = "#9932CC";
                    }

                    break;
                case ADD_AGI:
                case ADD_STR:
                case ADD_POWER:
                    long sml = effectNumberMap.get(effect).longValue();
                    if (sml > 1000) {
                        color = "#556B2F";
                    }
                    if (sml > 5000) {
                        color = "#0000FF";
                    }
                    if (sml > 8000) {
                        color = "#9932CC";
                    }

                    break;
                case ADD_CLICK_AWARD:
                    long cw = effectNumberMap.get(effect).longValue();
                    if (cw > 100) {
                        color = "#556B2F";
                    }
                    if (cw > 1000) {
                        color = "#0000FF";
                        save = true;
                    }
                    if (cw > 5000) {
                        color = "#9932CC";
                        save = true;
                    }

                    break;
                default:
                    color = "#000000";
            }
        }
        accessory.setColor(color);
        if (detectSave && save) {
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
            case ADD_POWER:
                accessory.setElement(Element.金);
                break;
            case ADD_DEF:
            case ADD_STR:
                accessory.setElement(Element.水);
                break;
            case ADD_UPPER_HP:
            case ADD_AGI:
                accessory.setElement(Element.火);
                break;
            case ADD_CLICK_AWARD:
                accessory.setElement(Element.土);
                break;
            default:
                accessory.setElement(Element.values()[random.nextInt(Element.values().length)]);
        }
    }

    public String detect(List<Item> items) {
        a1 = null;
        a2 = null;
        this.items = items;
        StringBuilder builder = new StringBuilder();
        try {
            Cursor cursor = queryRecipe();
            while (!cursor.isAfterLast()) {
                float pro = 0.0f;
                ArrayList<String> recipeItemList = new ArrayList<String>(5);
                for (String name : StringUtils.split(cursor.getString(cursor.getColumnIndex("items")), "-")) {
                    recipeItemList.add(name.trim());
                }
                float p = 100 / (recipeItemList.size() * 3);
                ArrayList<String> itemNames = new ArrayList<String>(items.size());
                for (Item item : items) {
                    itemNames.add(item.getName().name());
                }
                boolean isUser = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("user")));
                boolean isFound = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("found")));
                String color = cursor.getString(cursor.getColumnIndex("color"));
                if (itemNames.size() == recipeItemList.size()) {
                    for (int i = 0; i < itemNames.size(); i++) {
                        if (recipeItemList.get(i).equalsIgnoreCase(itemNames.get(i))) {
                            pro += p * 2;
                        } else {
                            pro -= p;
                        }
                    }
                }
                for (String name : itemNames) {
                    if (recipeItemList.contains(name)) {
                        pro += p;
                        recipeItemList.remove(recipeItemList.indexOf(name));
                    }
                }
                Map<Effect, Number> baseEffectsMap = new EnumMap<Effect, Number>(Effect.class);
                for (String str : StringUtils.split(cursor.getString(cursor.getColumnIndex("base")), "-")) {
                    String[] keyValue = StringUtils.split(str, ":");
                    if (keyValue.length > 1) {
                        baseEffectsMap.put(Effect.valueOf(keyValue[0].trim()), StringUtils.toLong(keyValue[1]));
                    }
                }
                Map<Effect, Number> additionEffectsMap = new EnumMap<Effect, Number>(Effect.class);
                for (String str : StringUtils.split(cursor.getString(cursor.getColumnIndex("addition")), "-")) {
                    String[] keyValue = StringUtils.split(str, ":");
                    if (keyValue.length > 1) {
                        additionEffectsMap.put(Effect.valueOf(keyValue[0].trim()), StringUtils.toLong(keyValue[1]));
                    }
                }
                if (pro < 0) pro = 0;
                if (a1 == null || a1.getPro() < pro) {
                    a1 = new Accessory();
                    if (isUser) {
                        pro /= 2;
                    }
                    if(!isFound){
                        pro /= 2;
                    }
                    a1.setPro(pro);
                    a1.setName(cursor.getString(cursor.getColumnIndex("name")));
                    a1.setColor(color);
                    a1.setAdditionEffects(additionEffectsMap);
                    a1.setEffects(baseEffectsMap);
                } else if (a2 == null || a2.getPro() < pro) {
                    a2 = new Accessory();
                    if (isUser) {
                        pro /= 2;
                    }
                    if(!isFound){
                        pro /= 2;
                    }
                    a2.setPro(pro);
                    a2.setName(cursor.getString(cursor.getColumnIndex("name")));
                    a2.setColor(color);
                    a2.setAdditionEffects(additionEffectsMap);
                    a2.setEffects(baseEffectsMap);
                }
                cursor.moveToNext();
            }
            if (a1 != null && a2 != null) {
                if ("#FF8C00".equalsIgnoreCase(a1.getColor())) {
                    a1.setPro(a1.getPro() / 2);
                    if(MazeContents.hero.getGift() == Gift.Maker) {
                        a1.setPro(a1.getPro() + 15);
                    }
                }
                if ("#FF8C00".equalsIgnoreCase(a2.getColor())) {
                    a2.setPro(a2.getPro() / 2);
                    if(MazeContents.hero.getGift() == Gift.Maker) {
                        a2.setPro(a2.getPro() + 15);
                    }
                }
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
        } catch (
                Exception e
                )

        {
            e.printStackTrace();
            Log.e(MainGameActivity.TAG, "build_detect", e);
        }

        return builder.toString();
    }

    public abstract int getType();

    public abstract Cursor queryRecipe();


    public List<Item> getItems() {
        return items;
    }

    public abstract String notEnough();
}
