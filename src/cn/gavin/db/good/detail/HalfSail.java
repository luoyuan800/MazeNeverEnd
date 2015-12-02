package cn.gavin.db.good.detail;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 11/26/2015.
 */
public class HalfSail implements LocalGood{
    public static final int type = 4;

    public int getType(){
        return type;
    }
    private int count;
    @Override
    public String getName() {
        return "半价";
    }

    @Override
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String toString(){
        return getName() + "<br>拥有这个物品可以在被击败时只掉到当前层数的一半，你和你宠物半血复活。<br>拥有数量：" + count;
    }
    public static HalfSail load(){
        HalfSail halfSail = new HalfSail();
        return halfSail;
    }

}
