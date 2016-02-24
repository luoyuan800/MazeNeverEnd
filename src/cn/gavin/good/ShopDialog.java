package cn.gavin.good;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import cn.gavin.Achievement;
import cn.gavin.log.LogHelper;
import cn.gavin.utils.MazeContents;
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
                    //透明
//                    Window window = shopDialog.getWindow();
//                    WindowManager.LayoutParams lp = window.getAttributes();
//                    lp.alpha = 0.4f;
//                    window.setAttributes(lp);
                    MazeContents.maze.setSailed(false);
                    Achievement.Shop.enable(MazeContents.hero);

                    break;
                case 1:
                    TextView textView = new TextView(activity);
                    textView.setText("爬楼的过程中会有商人随机进驻商店，目前还未有商人进驻你的商店，请继续爬楼。提高敏捷可以提升商人入驻的几率。");
                    shopDialog.setView(textView);
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
        if (MazeContents.maze.isSailed()) {
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
                        LogHelper.logException(e, false);
                    }
                }
            }).start();
        } else {
            handler.sendEmptyMessage(1);
        }
    }
}
