package cn.gavin.forge.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import cn.gavin.R;
import cn.gavin.activity.ForgeActivity;
import cn.gavin.forge.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED
 * Created by luoyuan on 10/4/15.
 */
public class ItemDialog {
    private AlertDialog itemDialog;
    private Activity activity;
    private Item selected;
    private TextView itemDesc;
    private ItemAdapter adapter;

    public ItemDialog(Activity activity) {
        this.activity = activity;
        adapter = new ItemAdapter();
    }

    private int what;

    public void show(int what) {
        if (itemDialog == null) init();
        itemDialog.show();
        this.what = what;
    }

    private void init() {
        itemDialog = new AlertDialog.Builder(activity).create();
        LinearLayout linearLayout = new LinearLayout(activity);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        itemDesc = new TextView(activity);
        linearLayout.addView(itemDesc);
        ListView listView = new ListView(activity);
        listView.setAdapter(adapter);
        linearLayout.addView(listView);

        itemDialog.setView(linearLayout);
        itemDialog.setTitle("材料列表");
        itemDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (activity instanceof ForgeActivity) {
                    Message message = new Message();
                    message.what = what;
                    message.obj = selected;
                    ((ForgeActivity) activity).handler.sendMessage(message);
                }
                itemDialog.hide();
            }
        });
        itemDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "退出", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                itemDialog.hide();
            }
        });
    }

    public void dismiss() {
        if (itemDialog != null) itemDialog.dismiss();
    }

    public Item getSelected() {
        return selected;
    }

    public static class ItemList {
        Item i1;
        Item i2;
        Item i3;
        Item i4;

        public ItemList() {

        }

        public boolean addItem(Item item) {
            if (i1 == null) i1 = item;
            else if (i2 == null) i2 = item;
            else if (i3 == null) i3 = item;
            else if (i4 == null) i4 = item;
            else return false;
            return true;
        }
    }

    static class ItemViewHolder {
        Button name1;
        Button name2;
        Button name3;
        Button name4;
    }

    class ItemAdapter extends BaseAdapter {
        private final List<ItemList> adapterData = getItemListAdp();

        @Override
        public int getCount() {
            return adapterData.size();
        }

        @Override
        public ItemList getItem(int position) {
            if (position >= getCount()) position = 0;
            return adapterData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ItemViewHolder holder;
            ForgeActivity forgeActivity = (ForgeActivity) activity;
            if (convertView == null) {
                holder = new ItemViewHolder();
                convertView = View.inflate(activity,
                        R.layout.items_item, null);
                holder.name1 = (Button) convertView.findViewById(R.id.item_name_1);
                holder.name1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ItemList itemList = getItem(position);
                        if (itemList.i1 != null) {
                            selected = itemList.i1;
                            itemDesc.setText(Html.fromHtml(itemList.i1.toString()));
                        }
                    }
                });
                holder.name2 = (Button) convertView.findViewById(R.id.item_name_2);
                holder.name2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ItemList itemList = getItem(position);
                        if (itemList.i2 != null) {
                            selected = itemList.i2;
                            itemDesc.setText(Html.fromHtml(itemList.i2.toString()));
                        }
                    }
                });
                holder.name4 = (Button) convertView.findViewById(R.id.item_name_4);
                holder.name4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ItemList itemList = getItem(position);
                        if (itemList.i4 != null) {
                            selected = itemList.i4;
                            itemDesc.setText(Html.fromHtml(itemList.i4.toString()));
                        }
                    }
                });
                holder.name3 = (Button) convertView.findViewById(R.id.item_name_3);
                holder.name3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ItemList itemList = getItem(position);
                        if (itemList.i3 != null) {
                            selected = itemList.i3;
                            itemDesc.setText(Html.fromHtml(itemList.i3.toString()));
                        }
                    }
                });
                convertView.setTag(holder);
            } else {
                holder = (ItemViewHolder) convertView.getTag();
            }
            ItemList item = getItem(position);
            if (item.i1 != null || forgeActivity.contains(item.i1)) {
                holder.name1.setText(item.i1.getName().name());
                holder.name1.setEnabled(true);
            } else {
                holder.name1.setText("");
                holder.name1.setEnabled(false);
            }
            if (item.i2 != null|| forgeActivity.contains(item.i2)) {
                holder.name2.setText(item.i2.getName().name());
                holder.name2.setEnabled(true);
            } else {
                holder.name2.setText("");
                holder.name2.setEnabled(false);
            }
            if (item.i3 != null|| forgeActivity.contains(item.i3)) {
                holder.name3.setText(item.i3.getName().name());
                holder.name3.setEnabled(true);
            } else {
                holder.name3.setText("");
                holder.name3.setEnabled(false);
            }
            if (item.i4 != null|| forgeActivity.contains(item.i4)) {
                holder.name4.setText(item.i4.getName().name());
                holder.name4.setEnabled(true);
            } else {
                holder.name4.setText("");
                holder.name4.setEnabled(false);
            }
            return convertView;
        }

    }

    private List<ItemList> getItemListAdp() {
        ArrayList<Item> list = Item.loadItems();
        ItemList itemList = new ItemList();
        ArrayList<ItemList> rs = new ArrayList<ItemList>(list.size() / 3);
        rs.add(itemList);
        for (Item item : list) {
            if (!itemList.addItem(item)) {
                itemList = new ItemList();
                itemList.addItem(item);
                rs.add(itemList);
            }
        }
        return rs;
    }
}
