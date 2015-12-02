package cn.gavin.db.good.detail;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 11/26/2015.
 */
public class Aphrodisiac extends UsableGood {
    public static final int type = 0;

    public int getType(){
        return type;
    }
    @Override
    public Object use() {
        return null;
    }

    @Override
    public String getName() {
        return "奴隶";
    }

    public String toString(){
        return getName() + "<br>使用后选择你队伍中的两只宠物生蛋。被选择的两只宠物亲密度会大幅度降低。<br>拥有数量：" + count;
    }
}
