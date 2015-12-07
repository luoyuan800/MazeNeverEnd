package cn.gavin.db.good.detail;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 11/26/2015.
 */
public abstract class UsableGood implements LocalGood {
    protected int count;
    protected String clazz;
    public void setClass(String clazz){
        this.clazz  = clazz;
    }
    public String getClazz(){
        return clazz;
    }
    public abstract Object use();

    @Override
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
