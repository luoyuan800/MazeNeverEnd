package cn.gavin.db.good.detail;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 11/26/2015.
 */
public class SafetyRope implements LocalGood {
    private int count;
    @Override
    public String getName() {
        return "安全绳";
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return getName() + "<br>拥有这个物品可以在被击败时掉回当前层数/10的那一层，你和你宠物恢复一般的生命值。<br>拥有数量：" + count;
    }

    public static SafetyRope load(){
        SafetyRope safetyRope = new SafetyRope();
        return safetyRope;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
