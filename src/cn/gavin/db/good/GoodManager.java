package cn.gavin.db.good;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.gavin.utils.MazeContents;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 11/24/2015.
 */
public class GoodManager {
    private Context context;
    private boolean finished = false;
    private List<Good> goods;
    public GoodManager(Context context){
        this.context = context;
    }
    public List<Good> getMyGoods(){
        List<Good> goods = new ArrayList<Good>();
        return goods;
    }
    public synchronized void finished(List<Good> rs){
        goods = rs;
        finished = true;
    }
    public void getShopGoods(){
        goods = null;
        finished = false;
        BmobQuery<Good> query = new BmobQuery<Good>();
        query.setLimit(20);
        query.findObjects(context, new FindListener<Good>() {
            @Override
            public void onSuccess(List<Good> goods) {
                finished(goods);
            }

            @Override
            public void onError(int i, String s) {
                finished(Collections.<Good>emptyList());
            }
        });
    }
    public boolean buyGood(Good good){
        if(MazeContents.hero!=null){
            if(MazeContents.hero.getMaterial() >= good.getCost()){
                MazeContents.hero.addMaterial(-good.getCost());
                good.buy(1);
                good.saveToDB(null);
                return true;
            }
        }
        return false;
    }
    public boolean buyGood(Good good, int count){
        if(MazeContents.hero!=null){
            if(count > good.getSailCount()){
                count = good.getSailCount();
            }
            if(MazeContents.hero.getMaterial() >= good.getCost() * count){
                MazeContents.hero.addMaterial(-good.getCost() * count);
                good.buy(count);
                good.saveToDB(null);
                return true;
            }
        }
        return false;
    }
    public Good getMySpecialEffectGoods(){
        return null;
    }
}
