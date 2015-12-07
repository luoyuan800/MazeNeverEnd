package cn.gavin.good;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.text.Html;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.gavin.activity.MainGameActivity;
import cn.gavin.db.DBHelper;
import cn.gavin.log.LogHelper;
import cn.gavin.pet.Pet;
import cn.gavin.utils.MazeContents;
import cn.gavin.utils.Random;

/**
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED
 * Created by luoyuan on 12/6/15.
 */
public enum GoodsType {
    Aphrodisiac("奴隶", "使用后随机选择队伍中的两个宠物进行生蛋，宠物亲密度会大幅度降低。",
            new GoodScript() {
                public Pet use() {
                    AlertDialog alertDialog = new AlertDialog.Builder(MainGameActivity.context).create();
                    alertDialog.setTitle("你获得了一个蛋");
                    List<Pet> pets = MazeContents.hero.getPets();
                    Random random = MazeContents.hero.getRandom();
                    Pet f = null;
                    Pet m = null;
                    for (Pet pet : pets) {
                        if (!"蛋".equals(pet.getType()) && random.nextBoolean() && pet.getSex() == 0) {
                            f = pet;
                            break;
                        }
                        if (!"蛋".equals(pet.getType()) && pet.getSex() == 1 && random.nextBoolean()) {
                            m = pet;
                            break;
                        }
                    }
                    if (f == null) {
                        for (Pet pet : pets) {
                            if (!"蛋".equals(pet.getType()) && pet.getSex() == 0) {
                                f = pet;
                                break;
                            }
                        }
                    }

                    if (m == null) {
                        for (Pet pet : pets) {
                            if (!"蛋".equals(pet.getType()) && pet.getSex() == 1) {
                                m = pet;
                                break;
                            }
                        }
                    }
                    TextView textView = new TextView(MainGameActivity.context);
                    if (f == null || m == null) {
                        textView.setText("你队伍中的宠物性别不符！无法使用这个物品!");
                    } else {
                        Pet egg = Pet.getEgg(f, m, MazeContents.maze.getLev(), MazeContents.hero, MazeContents.hero.getRandom());
                        if (egg == null) {
                            textView.setText("使用物品失败！");
                        } else {
                            textView.setText(Html.fromHtml(f.getFormatName() + "和" + m.getFormatName() + "生了一个蛋"));
                            f.setIntimacy(f.getIntimacy()/3);
                            m.setIntimacy(m.getIntimacy()/4);
                            egg.save();
                        }
                    }
                    alertDialog.setView(textView);
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.show();
                    return null;
                }
            }
    );
    private String name;
    private String desc;
    private GoodScript script;
    private int count;

    private GoodsType(String name, String desc, GoodScript script) {
        this.name = name;
        this.desc = desc;
        this.script = script;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    private void setDesc(String desc) {
        this.desc = desc;
    }

    public GoodScript getScript() {
        return script;
    }

    public void save() {
        DBHelper.getDbHelper().excuseSQLWithoutResult(
                String.format("REPLACE INTO goods (name,count) values ('%s',%s)", name(), count));
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public static GoodsType loadByName(String name) {
        GoodsType goods = valueOf(name);
        Cursor cursor = DBHelper.getDbHelper().excuseSOL("SELECT * FROM goods where name = '" + name + "'");
        if (!cursor.isAfterLast()) {
            goods.setCount(cursor.getInt(cursor.getColumnIndex("count")));
        }
        cursor.close();
        return goods;
    }

    public static List<GoodsType> loadAll() {
        try {
            Cursor cursor = DBHelper.getDbHelper().excuseSOL("SELECT * FROM goods WHERE count > 0");
            List<GoodsType> list = new ArrayList<GoodsType>(cursor.getCount());
            while (!cursor.isAfterLast()) {
                try {
                    GoodsType goodsType = valueOf(cursor.getString(cursor.getColumnIndex("name")));
                    goodsType.setCount(cursor.getInt(cursor.getColumnIndex("count")));
                    list.add(goodsType);
                } catch (Exception e) {
                    LogHelper.logException(e);
                    e.printStackTrace();
                }
                cursor.moveToNext();
            }
            cursor.close();
            return list;
        } catch (Exception e) {
            LogHelper.logException(e);
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}