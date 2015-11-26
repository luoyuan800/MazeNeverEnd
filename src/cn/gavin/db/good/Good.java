package cn.gavin.db.good;

import android.database.sqlite.SQLiteDatabase;

import cn.bmob.v3.BmobObject;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.db.good.detail.LocalGood;
import cn.gavin.utils.MazeContents;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 11/24/2015.
 */
public class Good extends BmobObject {
    private String name;
    private String desc;
    private Integer sailCount;
    private Long cost;
    private Boolean isLimited;
    public LocalGood getGood(){
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getSailCount() {
        return sailCount;
    }

    public void setSailCount(Integer sailCount) {
        this.sailCount = sailCount;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public void buy(int i) {
        if(isLimited){
            i = 1;
        }
        this.setSailCount(sailCount - i);
        this.update(MainGameActivity.context);
        //先查询出数据库中的个数，然后个数(count)加+i
    }

    public Boolean getIsLimited() {
        return isLimited;
    }

    public void setIsLimited(Boolean isLimited) {
        this.isLimited = isLimited;
    }
}
