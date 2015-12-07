package cn.gavin.forge.adapter;

import android.content.Context;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.gavin.R;
import cn.gavin.activity.ForgeActivity;
import cn.gavin.forge.Item;
import cn.gavin.forge.dialog.ItemDetailDialog;
import cn.gavin.utils.ui.LoadMoreListView;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 12/4/2015.
 */
public class ItemAdapter extends BaseAdapter implements LoadMoreListView.OnRefreshLoadingMoreListener, TextWatcher {
    List<Item> itemList;
    Context context;
    int what;
    LoadMoreListView listView;
    ItemDetailDialog detailDialog;

    public void setWhat(int what) {
        this.what = what;
    }

    public ItemAdapter(Context context, LoadMoreListView listView, ItemDetailDialog dialog) {
        this.detailDialog = dialog;
        this.context = context;
        this.listView = listView;
        itemList = new ArrayList<Item>();
        refresh();
    }

    private void addItem(List<Item> items) {
        for (Item item : items) {
            if (!((ForgeActivity) context).contains(item)) {
                itemList.add(item);
            }
        }
        notifyDataSetChanged();
    }

    private void searchResult(List<Item> items) {
        itemList.clear();
        for (Item item : items) {
            if (!((ForgeActivity) context).contains(item)) {
                itemList.add(item);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Item getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemViewHolder holder;
        if (convertView == null) {
            holder = new ItemViewHolder();
            convertView = holder.view;
            convertView.setTag(holder);
        } else {
            holder = (ItemViewHolder) convertView.getTag();
        }
        holder.updateItem(getItem(position));
        return convertView;
    }

    @Override
    public void onLoadMore() {
        List<Item> items = Item.loadByLimit(itemList.size() - 1, 10, "");
        if (!items.isEmpty()) {
            addItem(items);
            listView.onLoadMoreComplete(false);
        } else {
            listView.onLoadMoreComplete(true);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String query = "where name like '%" + s.toString() + "%'";
        List<Item> items = Item.loadByLimit(0, 10, query);
        searchResult(items);
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    public void refresh() {
        searchResult(Item.loadByLimit(0, 10, ""));
        listView.onLoadMoreComplete(false);
    }

    class ItemViewHolder {
        View view;
        TextView name;
        TextView desc;
        Item item;

        public ItemViewHolder() {
            view = View.inflate(context,
                    R.layout.item_simple_view, null);
            name = (TextView) view.findViewById(R.id.item_name);
            desc = (TextView) view.findViewById(R.id.item_desc);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (context instanceof ForgeActivity) {
                        Message message = new Message();
                        message.what = detailDialog.getWhat();
                        message.obj = getItem();
                        ((ForgeActivity) context).handler.sendMessage(message);
                        itemList.remove(getItem());
                        detailDialog.dismiss();
                    }
                }
            });
        }

        public void updateItem(Item item) {
            this.item = item;
            name.setText(item.getName().name());
            desc.setText(Html.fromHtml(item.getEffectString()));
        }

        public Item getItem() {
            return item;
        }
    }
}
