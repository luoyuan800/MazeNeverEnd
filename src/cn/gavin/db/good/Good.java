package cn.gavin.db.good;

import android.database.sqlite.SQLiteDatabase;

import cn.bmob.v3.BmobObject;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.utils.MazeContents;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 11/24/2015.
 */
public class Good extends BmobObject {
    private String name;
    private String desc;
    private String effect;
    private Integer sailCount;
    private Long cost;
    private Long count;//For local use
    public GoodEffect getGoodEffect(){
        return GoodEffect.valueOf(effect);
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

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public void useGood(){
        getGoodEffect().useGood();
        count --;
        saveToDB(null);
    }

    public void deleteFromDB(SQLiteDatabase database){

    }

    public void saveToDB(SQLiteDatabase database){


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
        this.setSailCount(sailCount + 1);
        this.update(MainGameActivity.context);
        //先查询出数据库中的个数，然后个数(count)加+i
        count += i;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
