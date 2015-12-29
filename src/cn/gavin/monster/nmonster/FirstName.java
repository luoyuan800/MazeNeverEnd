package cn.gavin.monster.nmonster;

/**
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED
 * Created by luoyuan on 12/29/15.
 */
public enum FirstName {
    normal("普通",1,1,0,0),
    weird("怪异",5,5,10 ,0),
    fly("飞翔",20,10,0 ,0),
    rate("稀有",15,30,0 ,-50),
    crazy("发狂",50,20,0.1f,0),
    magical("神奇",60,40,0 ,-5),
    neural("神经",50,75,1 ,-10),
    legendary("传奇",100,80,1 ,-30),
    unbeatable("无敌",150,110,5 ,-100)
    ;
    private float atkPercent;
    private float hpPercent;
    private float silent;
    private float eggRate;
    private  String name;
    private FirstName(String name, float atkPercent, float hpPercent, float silent, float eggRate){
        this.name = name;
        this.atkPercent = atkPercent;
        this.hpPercent = hpPercent;
        this.silent = silent;
        this.eggRate = eggRate;
    }
    public long getAtkAddition(long atk) {
        return (long) (atk * atkPercent);
    }

    public long getHPAddition(long hp) {
        return (long) (hp * hpPercent);
    }
    public float getSilent(){
        return silent;
    }
    public  float getEggRate(){
        return eggRate;
    }
    public String getName(){
        return name;
    }
}
