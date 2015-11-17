package cn.gavin.forge.list;

import cn.gavin.forge.Item;
import cn.gavin.forge.effect.Effect;
import cn.gavin.log.LogHelper;
import cn.gavin.utils.MazeContents;
import cn.gavin.utils.Random;

import java.util.EnumMap;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 10/13/2015.
 */
public class FuseItems {
    public static Item fuse(Item... items) {
        try {
            EnumMap<Effect, Number> effectNumberEnumMap = new EnumMap<Effect, Number>(Effect.class);
            Random random = new Random();
            if(MazeContents.reduceLegacyEffect(items[0].getEffect(), items[0].getEffectValue().longValue()) != items[0].getEffectValue().longValue()){
                //如果超出上限了，直接返回第一个物品
                return items[0];
            }
            effectNumberEnumMap.put(items[0].getEffect(), items[0].getEffectValue());
            if (items[0].getEffect1() != null && items[0].getEffect1() != items[0].getEffect()) {
                if(MazeContents.reduceLegacyEffect(items[0].getEffect1(), items[0].getEffect1Value().longValue()) != items[0].getEffect1Value().longValue()){
                    return items[0];
                }
                effectNumberEnumMap.put(items[0].getEffect1(), items[0].getEffect1Value());
            }
            for (Item item : items) {
                Number number = effectNumberEnumMap.get(item.getEffect());
                if (number != null) {
                    effectNumberEnumMap.put(item.getEffect(), number.longValue() + random.nextLong(item.getEffectValue().longValue() * 2 + 1));
                }
                if (item.getEffect1() != null) {
                    number = effectNumberEnumMap.get(item.getEffect1());
                    if (number != null) {
                        effectNumberEnumMap.put(item.getEffect1(), number.longValue() + random.nextLong(item.getEffect1Value().longValue() * 2 + 1));
                    }
                }
                item.delete(null);
            }
            Item item = new Item();
            item.setName(items[0].getName());
            for (EnumMap.Entry<Effect, Number> entry : effectNumberEnumMap.entrySet()) {
                Number value = entry.getValue();
                Effect key = entry.getKey();
                value = MazeContents.reduceLegacyEffect(key, value.longValue());
                value=value.longValue()/3;
                if (item.getEffect() == null) {
                    item.setEffect(key);
                    item.setEffectValue(value);
                } else if (item.getEffect1() == null) {
                    item.setEffect1(key);
                    item.setEffect1Value(value);
                }
            }
            if (item.getEffectValue() != null && item.getEffect() == Effect.ADD_CLICK_POINT_AWARD && item.getEffectValue().longValue() > 4) {
                item.setEffectValue(random.nextLong(4) + 1);
            }
            if (item.getEffect1Value() != null && item.getEffect1() == Effect.ADD_CLICK_POINT_AWARD && item.getEffect1Value().longValue() > 4) {
                item.setEffect1Value(random.nextLong(4) + 1);
            }
            if (item.getEffectValue() != null &&
                    (item.getEffect() == Effect.ADD_HIT_RATE || item.getEffect() == Effect.ADD_DODGE_RATE || item.getEffect() == Effect.ADD_PARRY)
                    && item.getEffectValue().longValue() > 60) {
                item.setEffectValue(random.nextLong(60) + 15);
            }
            if (item.getEffect1Value() != null &&
                    (item.getEffect1() == Effect.ADD_HIT_RATE || item.getEffect1() == Effect.ADD_DODGE_RATE || item.getEffect1() == Effect.ADD_PARRY)
                    && item.getEffect1Value().longValue() > 70) {
                item.setEffect1Value(random.nextLong(70) + 15);
            }
            if (item.getEffect() != null) {
                switch (item.getEffect()) {
                    case ADD_ATK:
                        if (random.nextInt(100) < 20) {
                            item.setEffect(Effect.ADD_PER_ATK);
                            long longValue = item.getEffectValue().longValue();
                            long value = calculate(longValue);
                            item.setEffectValue(value);
                        }
                        break;
                    case ADD_UPPER_HP:
                        if (random.nextInt(100) < 20) {
                            item.setEffect(Effect.ADD_PER_UPPER_HP);
                            long longValue = item.getEffectValue().longValue();
                            long value = calculate(longValue);
                            item.setEffectValue(value);
                        }
                        break;
                    case ADD_DEF:
                        if (random.nextInt(100) < 20) {
                            item.setEffect(Effect.ADD_PER_DEF);
                            long longValue = item.getEffectValue().longValue();
                            long value = calculate(longValue);
                            item.setEffectValue(value);
                        }
                        break;
                    case ADD_PER_UPPER_HP:
                        if (item.getEffectValue().longValue() > 50) {
                            item.setEffectValue(50);
                        }
                        break;
                    case ADD_PER_ATK:
                        if (item.getEffectValue().longValue() > 50) {
                            item.setEffectValue(50);
                        }
                        break;
                    case ADD_PER_DEF:
                        if (item.getEffectValue().longValue() > 50) {
                            item.setEffectValue(50);
                        }
                        break;
                    case ADD_CLICK_AWARD:
                        if (item.getEffect1Value().longValue() > 1000) {
                            item.setEffect1Value(10001);
                        }
                        break;
                }
            }
            if (item.getEffect1() != null) {
                switch (item.getEffect1()) {
                    case ADD_ATK:
                        if (random.nextInt(100) < 20) {
                            item.setEffect(Effect.ADD_PER_ATK);
                            long longValue = item.getEffect1Value().longValue();
                            long value = calculate(longValue);
                            item.setEffect1Value(value);
                        }
                        break;
                    case ADD_UPPER_HP:
                        if (random.nextInt(100) < 20) {
                            item.setEffect1(Effect.ADD_PER_UPPER_HP);
                            long longValue = item.getEffect1Value().longValue();
                            long value = calculate(longValue);
                            item.setEffect1Value(value);
                        }
                        break;
                    case ADD_DEF:
                        if (random.nextInt(100) < 20) {
                            item.setEffect1(Effect.ADD_PER_DEF);
                            long longValue = item.getEffect1Value().longValue();
                            long value = calculate(longValue);
                            item.setEffect1Value(value);
                        }
                        break;
                    case ADD_PER_UPPER_HP:
                        if (item.getEffect1Value().longValue() > 60) {
                            item.setEffect1Value(60);
                        }
                        break;
                    case ADD_PER_ATK:
                        if (item.getEffect1Value().longValue() > 60) {
                            item.setEffect1Value(60);
                        }
                        break;
                    case ADD_PER_DEF:
                        if (item.getEffect1Value().longValue() > 90) {
                            item.setEffect1Value(60);
                        }
                        break;
                    case ADD_STR:
                        if (MazeContents.hero != null) {
                            if (item.getEffect1Value().longValue() > MazeContents.hero.getStrength() / 1000) {
                                item.setEffect1Value(MazeContents.hero.getStrength() / 10001);
                            }
                        }
                        break;
                    case ADD_AGI:
                        if (MazeContents.hero != null) {
                            if (item.getEffect1Value().longValue() > MazeContents.hero.getAgility() / 1000) {
                                item.setEffect1Value(MazeContents.hero.getAgility() / 10001);
                            }
                        }
                        break;
                    case ADD_POWER:
                        if (MazeContents.hero != null) {
                            if (item.getEffect1Value().longValue() > MazeContents.hero.getPower() / 500) {
                                item.setEffect1Value(MazeContents.hero.getPower() / 5001);
                            }
                        }
                        break;
                    case ADD_CLICK_AWARD:
                        if (item.getEffect1Value().longValue() > 1000) {
                            item.setEffect1Value(10001);
                        }
                        break;
                }
            }
            item.save(null);
            return item;
        } catch (Exception e) {
            LogHelper.logException(e);
            e.printStackTrace();
        }
        return null;
    }

    private static long calculate(long longValue) {
        long value = (longValue / 10) * 100 / longValue;
        if (value > 10) value = 10;
        if (value < 1) value = 1;
        return value;
    }
}
