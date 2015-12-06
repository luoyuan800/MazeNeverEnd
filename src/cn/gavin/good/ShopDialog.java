package cn.gavin.good;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import cn.gavin.log.LogHelper;
import cn.gavin.utils.ui.LoadMoreListView;

/**
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED
 * Created by luoyuan on 12/6/15.
 */
public class ShopDialog {
    AlertDialog shopDialog;
    Activity activity;
    GoodManager goodManager;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    LoadMoreListView listView = new LoadMoreListView(activity);
                    GoodsAdapter adapter = new GoodsAdapter(activity, goodManager.getResult());
                    listView.setAdapter(adapter);
                    listView.onLoadMoreComplete(true);
                    shopDialog.setView(listView);
                    shopDialog.show();
                    break;
            }
        }
    };

    public ShopDialog(Activity context) {
        this.activity = context;
        shopDialog = new AlertDialog.Builder(context).create();
        shopDialog.setTitle("选购物品");
        shopDialog.setButton(DialogInterface.BUTTON_POSITIVE, "退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        goodManager = new GoodManager(activity);
    }

    private boolean onShow;

    public void show() {
        goodManager.queryNetGoods();
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("正在进入商店");
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                progressDialog.dismiss();
                onShow = false;
            }
        });
        progressDialog.show();
        onShow = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    while (!goodManager.isFinished() && onShow) ;
                    progressDialog.dismiss();
                    handler.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                    LogHelper.logException(e);
                }
            }
        }).start();
    }
}
