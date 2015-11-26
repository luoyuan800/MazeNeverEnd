package cn.gavin.db.good.detail;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 11/26/2015.
 */
public class RandomPortal extends UsableGood {
    @Override
    public Object use() {
        return null;
    }

    @Override
    public String getName() {
        return "随机传送";
    }

    public String toString(){
        return getName() + "<br>使用后随机传送。传送范围为（当前层数-100）至（当前层数+300）之间。<br>拥有数量：" + count;
    }
}


