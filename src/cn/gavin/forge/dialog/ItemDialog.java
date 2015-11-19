package cn.gavin.forge.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.gavin.Hero;
import cn.gavin.R;
import cn.gavin.activity.ForgeActivity;
import cn.gavin.log.LogHelper;
import cn.gavin.utils.MazeContents;
import cn.gavin.forge.Item;
import cn.gavin.forge.effect.Effect;
import cn.gavin.utils.Random;

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
    private EditText filterText;

    public ItemDialog(Activity activity) {
        this.activity = activity;
        adapter = new ItemAdapter();
    }

    private int what;

    public void show(int what) {
        if (itemDialog == null) init();
        selected = null;
        itemDesc.setText("");
        itemDialog.show();
        adapter.refresh(filterText.getText().toString());
        this.what = what;
    }

    private void init() {
        itemDialog = new AlertDialog.Builder(activity).create();
        LinearLayout linearLayout = new LinearLayout(activity);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        filterText = new EditText(activity);
        filterText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        linearLayout.addView(filterText);
        itemDesc = new TextView(activity);
        linearLayout.addView(itemDesc);
        ListView listView = new ListView(activity);
        listView.setAdapter(adapter);
        linearLayout.addView(listView);
        Button add = new Button(activity);
        add.setText("一键随机添加材料（慎用！）");
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                int i = 0;
                for (ItemList list : adapter.adapterData) {
                    if ( i < 5 && random.nextBoolean()) {
                        if (activity instanceof ForgeActivity) {
                            if (i == 0 && list.i1 != null) {
                                Message message = new Message();
                                message.what = R.id.forge_item_1;
                                message.obj = list.i1;
                                ((ForgeActivity) activity).handler.sendMessage(message);
                                i++;
                            }
                            if (i == 1 && list.i2 != null) {
                                Message message = new Message();
                                message.what = R.id.forge_item_2;
                                message.obj = list.i2;
                                ((ForgeActivity) activity).handler.sendMessage(message);
                                i++;
                            }
                            if (i == 2 && list.i3 != null) {
                                Message message = new Message();
                                message.what = R.id.forge_item_3;
                                message.obj = list.i3;
                                ((ForgeActivity) activity).handler.sendMessage(message);
                                i++;
                            }
                            if (i == 3 && list.i4 != null) {
                                Message message = new Message();
                                message.what = R.id.forge_item_4;
                                message.obj = list.i4;
                                ((ForgeActivity) activity).handler.sendMessage(message);
                                i++;
                            }
                        }
                    }
                }
                itemDialog.hide();
            }
        });
//        linearLayout.addView(add);
        Button clean = new Button(activity);
        clean.setText("一键清除低属性材料（慎用！）");
        clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog clean = new AlertDialog.Builder(activity).create();
                clean.setTitle("确认清除材料？");
                TextView textView = new TextView(activity);
                textView.setText("属性为增加基本攻击、防御、HP并且数值低于迷宫层数*30的材料会被清除！");
                clean.setView(textView);
                clean.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Hero hero = MazeContents.hero;
                        if (hero != null) {
                            for (Item item : Item.loadItems(null)) {
                                boolean delete = false;
                                Effect effect = item.getEffect();
                                if (effect == Effect.ADD_DEF || effect == Effect.ADD_ATK || effect == Effect.ADD_UPPER_HP) {
                                    if (item.getEffectValue().longValue() < hero.getMaxMazeLev() * 30) {
                                        delete = true;
                                    }
                                }
                                if (item.getEffect1() != null) {
                                    effect = item.getEffect1();
                                    delete = (effect == Effect.ADD_DEF || effect == Effect.ADD_ATK
                                            || effect == Effect.ADD_UPPER_HP) && item.getEffect1Value().longValue() < hero.getMaxMazeLev() * 30;
                                }
                                if (delete) {
                                    item.delete(null);
                                }
                            }
                            adapter.refresh("");
                        }
                        dialog.dismiss();
                    }
                });
                clean.setButton(DialogInterface.BUTTON_NEGATIVE, "退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                clean.show();
            }
        });
        //linearLayout.addView(clean);
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
        itemDialog.setButton(DialogInterface.BUTTON_NEUTRAL,"清除材料", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog onpush = new AlertDialog.Builder(activity).create();
                onpush.setTitle("确认清除材料（慎）？");
                TextView textView = new TextView(activity);
                textView.setText("属性为增加基本攻击、防御、HP并且数值低于迷宫层数*40的材料会被清除！\n属性为增加敏捷、力量、体力，数值不是负数并且小于迷宫层数的将会被清除！\n多个属性的材料，只要有一个属性符合条件就会被清除！\n清除过程会有点卡，请耐心等待！");
                onpush.setView(textView);
                onpush.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            Hero hero = MazeContents.hero;
                            if (hero != null) {
                                for (Item item : Item.loadItems(null)) {
                                    boolean delete = false;
                                    Effect effect = item.getEffect();
                                    if (effect == Effect.ADD_DEF || effect == Effect.ADD_ATK || effect == Effect.ADD_UPPER_HP) {
                                        if (item.getEffectValue().longValue() > 0 && item.getEffectValue().longValue() < hero.getMaxMazeLev() * 40) {
                                            delete = true;
                                        }
                                    } else if (effect == Effect.ADD_AGI || effect == Effect.ADD_POWER || effect == Effect.ADD_STR) {
                                        if (item.getEffectValue().longValue() > 0 && item.getEffectValue().longValue() < hero.getMaxMazeLev()) {
                                            delete = true;
                                        }
                                    }
                                    if (item.getEffect1() != null) {
                                        effect = item.getEffect1();
                                        if ((effect == Effect.ADD_DEF || effect == Effect.ADD_ATK
                                                || effect == Effect.ADD_UPPER_HP) && item.getEffect1Value().longValue() > 0 && item.getEffect1Value().longValue() < hero.getMaxMazeLev() * 40) {
                                            delete = true;
                                        } else if ((effect == Effect.ADD_AGI || effect == Effect.ADD_POWER || effect == Effect.ADD_STR) && (item.getEffect1Value().longValue() > 0 && item.getEffect1Value().longValue() < hero.getMaxMazeLev())) {
                                            delete = true;
                                        }
                                    }
                                    if (delete) {
                                        item.delete(null);
                                    }
                                }
                                adapter.refresh("");
                            }
                        }catch (Exception e){
                            LogHelper.logException(e);
                        }
                        dialog.dismiss();
                    }
                });
                onpush.setButton(DialogInterface.BUTTON_NEGATIVE, "退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                onpush.show();
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

    class ItemAdapter extends BaseAdapter implements Filterable {
        ArrayList<Item> list = Item.loadItems(null);

        private List<ItemList> adapterData = getItemListAdp(list);

        public void refresh(String s) {
            list = Item.loadItems(null);
            getFilter().filter(s);
        }

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

                holder.name2 = (Button) convertView.findViewById(R.id.item_name_2);

                holder.name4 = (Button) convertView.findViewById(R.id.item_name_4);

                holder.name3 = (Button) convertView.findViewById(R.id.item_name_3);

                convertView.setTag(holder);
            } else {
                holder = (ItemViewHolder) convertView.getTag();
            }
            ItemList item = getItem(position);
            if (item.i1 != null && !forgeActivity.contains(item.i1)) {
                holder.name1.setText(item.i1.getName().name());
                holder.name1.setEnabled(true);
                holder.name1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getItem(position).i1 != null) {
                            selected = getItem(position).i1;
                            itemDesc.setText(Html.fromHtml(getItem(position).i1.toString()));
                        }
                    }
                });
            } else {
                holder.name1.setText("");
                holder.name1.setEnabled(false);
            }
            if (item.i2 != null && !forgeActivity.contains(item.i2)) {
                holder.name2.setText(item.i2.getName().name());
                holder.name2.setEnabled(true);
                holder.name2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getItem(position).i2 != null) {
                            selected = getItem(position).i2;
                            itemDesc.setText(Html.fromHtml(getItem(position).i2.toString()));
                        }
                    }
                });

            } else {
                holder.name2.setText("");
                holder.name2.setEnabled(false);
            }
            if (item.i3 != null && !forgeActivity.contains(item.i3)) {
                holder.name3.setText(item.i3.getName().name());
                holder.name3.setEnabled(true);
                holder.name3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getItem(position).i3 != null) {
                            selected = getItem(position).i3;
                            itemDesc.setText(Html.fromHtml(getItem(position).i3.toString()));
                        }
                    }
                });
            } else {
                holder.name3.setText("");
                holder.name3.setEnabled(false);
            }
            if (item.i4 != null && !forgeActivity.contains(item.i4)) {
                holder.name4.setText(item.i4.getName().name());
                holder.name4.setEnabled(true);
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
            } else {
                holder.name4.setText("");
                holder.name4.setEnabled(false);
            }
            return convertView;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();
                    List<Item> newList = new ArrayList<Item>(list.size());
                    for (Item item : list) {
                        String prefix = constraint.toString();
                        if (item.getName().name().startsWith(prefix)) {
                            newList.add(item);
                        }
                    }
                    Collections.sort(newList, new Comparator<Item>() {

                        @Override
                        public int compare(Item lhs, Item rhs) {
                            return lhs.getName().name().compareTo(rhs.getName().name());
                        }
                    });
                    List<ItemList> listAdp = getItemListAdp(newList);
                    results.values = listAdp;
                    results.count = listAdp.size();
                    return results;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    adapterData = (List<ItemList>) results.values;
                    if (results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
        }
    }

    private List<ItemList> getItemListAdp(List<Item> list) {
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
