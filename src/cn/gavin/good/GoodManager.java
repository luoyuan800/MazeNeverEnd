package cn.gavin.good;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import java.util.Collections;
import java.util.List;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 12/2/2015.
 */
public class GoodManager {
    private boolean finished;
    private List<GoodsInNet> result;
    private Activity activity;

    public GoodManager(Activity context) {
        this.activity = context;
    }

    public void finished(List<GoodsInNet> goods) {
        setFinished(true);
        result = goods;
    }

    public void queryNetGoods() {
        setFinished(false);
        BmobQuery<GoodsInNet> query = new BmobQuery<GoodsInNet>();
        query.addWhereEqualTo("sail", true);
        query.findObjects(activity, new FindListener<GoodsInNet>() {
            @Override
            public void onSuccess(List<GoodsInNet> goodsInNets) {
                finished(goodsInNets);
            }

            @Override
            public void onError(int i, String s) {
                finished(Collections.<GoodsInNet>emptyList());
            }
        });
    }


    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public List<GoodsInNet> getResult() {
        return result;
    }

    public void setResult(List<GoodsInNet> result) {
        this.result = result;
    }

    public static void buildGoodsDB(SQLiteDatabase sqLiteDatabase){
        String createTable = "CREATE TABLE goods(" +
                "name TEXT NOT NULL PRIMARY KEY," +
                "count TEXT," +
                "class TEXT" +
                ")";
        sqLiteDatabase.execSQL(createTable);
        sqLiteDatabase.execSQL("CREATE UNIQUE INDEX goods_index ON goods (name)");
    }
}