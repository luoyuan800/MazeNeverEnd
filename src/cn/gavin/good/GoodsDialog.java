package cn.gavin.good;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import cn.gavin.utils.ui.LoadMoreListView;

/**
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED
 * Created by luoyuan on 12/6/15.
 */
public class GoodsDialog {
    AlertDialog goodDialog;
    Activity context;

    public GoodsDialog(Activity activity) {
        this.context = activity;
        goodDialog = new AlertDialog.Builder(context).create();
        goodDialog.setTitle("持有物品");
        goodDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
    }

    public void show() {
        LoadMoreListView loadMoreListView = new LoadMoreListView(context);
        loadMoreListView.onLoadMoreComplete(true);
        MyGoodsAdapter adapter = new MyGoodsAdapter(context, GoodsType.loadAll());
        loadMoreListView.setAdapter(adapter);
        goodDialog.setView(loadMoreListView);
        goodDialog.show();
    }
}
