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
import cn.gavin.utils.StringUtils;

import java.util.List;

/**
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED
 * Created by luoyuan on 12/6/15.
 */
public class GoodsAdapter extends BaseAdapter {
    private int iteViewId;
    private List<GoodsInNet> goods;
    private Activity activity;

    public GoodsAdapter(Activity activity, List<GoodsInNet> goods) {
        this.goods = goods;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return goods.size();
    }

    @Override
    public GoodsInNet getItem(int i) {
        return goods.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(activity, R.layout.goods_shop_item, (ViewGroup) activity.findViewById(R.id.shop_root));
        }
        Button button = (Button) view.findViewById(R.id.good_buy_button);
        final GoodsInNet goodsInNet = getItem(i);
        final GoodsType type = GoodsType.loadByName(goodsInNet.getType());
        if (button != null) {
            button.setEnabled(MazeContents.hero.getMaterial() > goodsInNet.getCost());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MazeContents.hero.addMaterial(-goodsInNet.getCost());
                    type.setCount(type.getCount() + 1);
                    type.save();
                    notifyDataSetChanged();
                    Toast.makeText(activity, "成功购买" + type.getName(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        ((TextView) view.findViewById(R.id.good_buy_name)).setText(type.getName());
        ((TextView) view.findViewById(R.id.good_buy_desc)).setText(type.getDesc());
        ((TextView) view.findViewById(R.id.good_by_cost)).setText("价格：" + StringUtils.formatNumber(goodsInNet.getCost()));
        return view;
    }
}
