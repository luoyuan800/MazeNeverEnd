package cn.gavin.good;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import cn.gavin.R;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.utils.MazeContents;

import java.util.List;

/**
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED
 * Created by luoyuan on 12/6/15.
 */
public class MyGoodsAdapter extends BaseAdapter {
    private int iteViewId;
    private List<GoodsType> goods;
    private Activity activity;

    public MyGoodsAdapter(Activity activity,List<GoodsType> goods) {
        this.goods = goods;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return goods.size();
    }

    @Override
    public GoodsType getItem(int i) {
        return goods.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(activity, R.layout.goods_item, (ViewGroup) activity.findViewById(R.id.shop_root));
        }
        Button button = (Button) view.findViewById(R.id.good_buy_button);
        final GoodsType type = getItem(i);
        if (button != null) {
            button.setEnabled(type.getCount() > 0);
            if(type.isUsable()){
                button.setText("使用");
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog dialog = new AlertDialog.Builder(MainGameActivity.context).create();
                        dialog.setTitle(type.getName());
                        dialog.setMessage(type.getDesc());
                        dialog.setButton(DialogInterface.BUTTON_POSITIVE,"确认", new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                type.getScript().use();
                                notifyDataSetChanged();
                            }
                        });
                        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                });
            }else{
                if(type.isLock()){
                    button.setText("解锁");
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            type.setLock(false);
                            notifyDataSetChanged();
                        }
                    });
                }else {
                    button.setText("锁定");
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            type.setLock(true);
                            notifyDataSetChanged();
                        }
                    });
                }
            }

        }
        ((TextView) view.findViewById(R.id.good_buy_name)).setText(type.getName());
        ((TextView) view.findViewById(R.id.good_buy_desc)).setText(type.getDesc() + "锁定后不会自动使用.");
        ((TextView) view.findViewById(R.id.good_by_cost)).setText("个数：" + type.getCount());
        return view;
    }
}
