package cn.gavin.monster;

import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Stack;

import cn.gavin.activity.MainGameActivity;
import cn.gavin.forge.Recipe;
import cn.gavin.maze.Palace;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 10/9/2015.
 */
public class PalaceAdapt extends BaseAdapter{

    static class PalaceViewHolder {
        TextView name1;
    }

    private final Stack<Defender> adapterData = Defender.loadAllDefender(null);

    @Override
    public int getCount() {
        return adapterData.size();
    }

    @Override
    public Defender getItem(int position) {
        if (position >= getCount()) position = 0;
        return adapterData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        PalaceViewHolder holder;
        if (convertView == null) {
            holder = new PalaceViewHolder();
            convertView = new TextView(MainGameActivity.context);
            holder.name1 = (TextView) convertView;
            convertView.setTag(holder);
        } else {
            holder = (PalaceViewHolder) convertView.getTag();
        }
        Defender item = getItem(position);
        if (item != null) {
            holder.name1.setText(Html.fromHtml(item.toString()));
        } else {
            holder.name1.setText("");
        }
        return convertView;
    }
}
