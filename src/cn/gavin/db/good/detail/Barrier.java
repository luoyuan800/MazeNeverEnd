package cn.gavin.db.good.detail;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 11/26/2015.
 */
public class Barrier implements LocalGood {
    public static final int type = 1;

    public int getType(){
        return type;
    }
    private int count;

    @Override
    public String getName() {
        return "隔阂";
    }

    @Override
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public void setClass(String clazz) {

    }

    public String toString() {
        return getName() + "<br>每当你的宠物想要生蛋的时候，自动使用这个物品阻止他们生蛋。<br>拥有数量：" + count;
    }

    public static Barrier load() {
        Barrier barrier = new Barrier();
        return barrier;

    }
}
