package cn.gavin.db.good.detail;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 11/26/2015.
 */
public class SpecialPortal extends UsableGood {
    @Override
    public Object use() {
        return null;
    }

    @Override
    public String getName() {
        return "定向传送";
    }

    public String toString() {
        return getName() + "<br>使用后传送到指定的层数。只能传送到到达过的楼层。<br>拥有数量：" + count;
    }
}
