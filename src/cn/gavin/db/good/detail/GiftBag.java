package cn.gavin.db.good.detail;

import java.util.List;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 11/26/2015.
 */
public class GiftBag extends UsableGood {
    public static final int type = 3;

    public int getType(){
        return type;
    }
    private List<String> items;
    @Override
    public Object use() {
        return null;
    }

    @Override
    public String getName() {
        return "礼包";
    }

    public String toString(){
        return getName() + "<br>使用可以获得：<br>" + items.toString();
    }

    public void setItems(List<String> items) {
        this.items = items;
    }
}
