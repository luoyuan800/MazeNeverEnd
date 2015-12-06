package cn.gavin.good;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import cn.gavin.R;
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
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    type.getScript().use();
                    notifyDataSetChanged();
                }
            });
        }
        ((TextView) view.findViewById(R.id.good_buy_name)).setText(type.getName());
        ((TextView) view.findViewById(R.id.good_buy_desc)).setText(type.getDesc());
        ((TextView) view.findViewById(R.id.good_by_cost)).setText("个数：" + type.getCount());
        return view;
    }
}
