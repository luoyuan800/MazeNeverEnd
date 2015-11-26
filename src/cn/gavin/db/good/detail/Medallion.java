package cn.gavin.db.good.detail;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 11/26/2015.
 */
public class Medallion implements LocalGood{
    public String getName(){
        return "免死金牌";
    }
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String toString(){
        return getName() + "<br>拥有这个物品可以在被击败时不会掉到第一层，你和你宠物原地半血复活。<br>拥有数量：" + count;
    }

    public static Medallion load(){
        Medallion medallion = new Medallion();
        return medallion;
    }
}
