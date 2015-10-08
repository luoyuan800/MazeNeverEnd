package cn.gavin.forge.adapter;

import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.forge.Recipe;

import java.util.List;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 10/5/2015.
 */
public class RecipeAdapter extends BaseAdapter {

    static class RecipeViewHolder {
        TextView name1;
    }

    private final List<Recipe> adapterData = Recipe.loadRecipes();

    @Override
    public int getCount() {
        return adapterData.size();
    }

    @Override
    public Recipe getItem(int position) {
        if (position >= getCount()) position = 0;
        return adapterData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        RecipeViewHolder holder;
        if (convertView == null) {
            holder = new RecipeViewHolder();
            convertView = new TextView(MainGameActivity.context);
            holder.name1 = (TextView) convertView;
            convertView.setTag(holder);
        } else {
            holder = (RecipeViewHolder) convertView.getTag();
        }
        Recipe item = getItem(position);
        if (item != null) {
            holder.name1.setText(Html.fromHtml(item.toString()));
        } else {
            holder.name1.setText("");
        }
        return convertView;
    }
}
