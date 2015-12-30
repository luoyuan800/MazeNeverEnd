package cn.gavin.monster;

import cn.gavin.utils.Random;

/**
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED
 * Created by luoyuan on 12/29/15.
 */
public enum SecondName {
    small("小",1,1,20,0),
    middle("中",5,3,10,0),
    large("大",15,8,5,0),
    larger("大大",20,13,1,0),
    face("人面",20,13,1,0),
    Strong("强壮",20,13,1,0),
    weak("无力",20,13,1,0),
    redB("红唇",20,13,1,0),
    food("笨",20,13,1,0),
    reckless("鲁莽",20,13,1,0),
    violence("暴力",20,13,1,0),
    fat("胖",20,13,1,0),
    red("红色",20,13,1,0),
    green("绿色",20,13,1,0),
    defender("【守护者】",20,13,1,0),
    empty("",20,13,1,0)
    ;
    private SecondName(String name, float askPref, float hpPref, int petRate, int silent){
        this.name = name;
        this.additionAtk = askPref;
        this.additionHp = hpPref;
        this.petRate = petRate;
        this.silent = silent;
    }
    private String name;
    private  float additionHp;
    private float additionAtk;
    private int petRate;
    private float silent;

    public String getName() {
        return name;
    }

    public static SecondName getRandom(long mazeLev, Random random) {
        int length = values().length - 2;
        int second = (int) random.nextLong(mazeLev < length ? mazeLev + 1 : length);
        if(second >= length){
            second = random.nextInt(length);
        }
        return values()[second];
    }

    public long getAtkAddition(long atk) {
        return (long)(atk * (double)additionAtk);
    }

    public long getHpAddition(long hp) {
        return (long)(hp * (double)additionHp);
    }

    public int getPetRate() {
        return petRate;
    }

    public float getSilent() {
        return silent;
    }
}
