package cn.gavin.forge.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.gavin.Hero;
import cn.gavin.forge.Item;
import cn.gavin.forge.adapter.ItemAdapter;
import cn.gavin.forge.effect.Effect;
import cn.gavin.log.LogHelper;
import cn.gavin.utils.MazeContents;
import cn.gavin.utils.ui.LoadMoreListView;

/**
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED
 * Created by luoyuan on 10/4/15.
 */
public class ItemDetailDialog {
    private AlertDialog itemDialog;
    private Activity activity;
    private ItemAdapter adapter;
    private EditText filterText;

    public ItemDetailDialog(Activity activity) {
        this.activity = activity;
    }

    private int what;

    public void show(int what) {
        if (itemDialog == null) init();
        itemDialog.show();
        this.what = what;
    }

    private void init() {
        itemDialog = new AlertDialog.Builder(activity).create();
        LoadMoreListView listView = new LoadMoreListView(activity);
        adapter = new ItemAdapter(activity, listView, this);
        listView.setAdapter(adapter);
        listView.setOnLoadListener(adapter);
        LinearLayout linearLayout = new LinearLayout(activity);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        filterText = new EditText(activity);
        filterText.addTextChangedListener(adapter);
        linearLayout.addView(filterText);
        linearLayout.addView(listView);

        itemDialog.setView(linearLayout);
        itemDialog.setTitle("材料列表");
        itemDialog.setButton(DialogInterface.BUTTON_POSITIVE, "随机添加", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                itemDialog.hide();
            }
        });
        itemDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "退出", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                itemDialog.hide();
            }
        });
        itemDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "清除材料", new DialogInterface.OnClickListener() {
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
                                filterText.setText("");
                                adapter.refresh();
                            }
                        } catch (Exception e) {
                            LogHelper.logException(e);
                        }
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

    public int getWhat() {
        return what;
    }

    public void setWhat(int what) {
        this.what = what;
    }

    public void refresh() {
        if (adapter != null) {
            adapter.refresh();
        }
    }
}
