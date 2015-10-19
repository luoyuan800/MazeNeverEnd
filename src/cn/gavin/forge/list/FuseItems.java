package cn.gavin.forge.list;

import java.util.EnumMap;

import cn.gavin.forge.Item;
import cn.gavin.forge.effect.Effect;
import cn.gavin.utils.Random;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 10/13/2015.
 */
public class FuseItems {
    public static Item fuse(Item...items){
        EnumMap<Effect,Number> effectNumberEnumMap = new EnumMap<Effect, Number>(Effect.class);
        Random random = new Random();
        int index = random.nextInt(items.length);
        effectNumberEnumMap.put(items[index].getEffect(), items[index].getEffectValue());
        if(items[index].getEffect1()!=null && items[index].getEffect1()!=items[index].getEffect()){
            effectNumberEnumMap.put(items[index].getEffect1(), items[index].getEffect1Value());
        }
        for(Item item : items){
            Number number = effectNumberEnumMap.get(item.getEffect());
            if(number!=null){
                effectNumberEnumMap.put(item.getEffect(), number.longValue() + random.nextLong(item.getEffectValue().longValue() + 1000));
            }
            if(item.getEffect1()!=null){
                number = effectNumberEnumMap.get(item.getEffect1());
                if(number!=null){
                    effectNumberEnumMap.put(item.getEffect1(), number.longValue() + random.nextLong(item.getEffect1Value().longValue()));
                }
            }
            item.delete();
        }
        Item item = new Item();
        item.setName(items[index].getName());
        for(EnumMap.Entry<Effect, Number> entry : effectNumberEnumMap.entrySet()){
            if(item.getEffect() == null){
                item.setEffect(entry.getKey());
                item.setEffectValue(entry.getValue());
            }else if(item.getEffect1() == null){
                item.setEffect1(entry.getKey());
                item.setEffect1Value(entry.getValue());
            }
        }
        item.save();
        return item;
    }
}
