package cn.gavin.good;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.text.Html;
import android.widget.TextView;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.db.DBHelper;
import cn.gavin.forge.Item;
import cn.gavin.forge.effect.Effect;
import cn.gavin.forge.list.ItemName;
import cn.gavin.log.LogHelper;
import cn.gavin.pet.Pet;
import cn.gavin.utils.MazeContents;
import cn.gavin.utils.Random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
                            f.setIntimacy(f.getIntimacy() / 3);
                            m.setIntimacy(m.getIntimacy() / 4);
                            GoodsType.Aphrodisiac.count--;
                            GoodsType.Aphrodisiac.save();
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
            },
            true),
    HalfSafe("折扣", "拥有这个物品可以在被击败时只掉到当前层数的一半，你和你宠物半血复活", new GoodScript() {
        @Override
        public Object use() {
            GoodsType.HalfSafe.count--;
            GoodsType.HalfSafe.save();
            return null;
        }
    }, false),
    Medallion("免死金牌", "拥有这个物品可以在被击败时不会掉到第一层，你和你宠物原地半血复活", new GoodScript() {
        @Override
        public Object use() {
            GoodsType.Medallion.count--;
            GoodsType.Medallion.save();
            return null;
        }
    }, false),
    SafetyRope("安全绳", "拥有这个物品可以在被击败时掉回当前层数/10的那一层，你和你宠物原地半血复活", new GoodScript() {
        @Override
        public Object use() {
            GoodsType.SafetyRope.count--;
            GoodsType.SafetyRope.save();
            return null;
        }
    }, false),
    RandomGoods("开盖有奖", "使用后随机获得一个物品(有1/2的概率获物品）", new GoodScript() {
        @Override
        public Object use() {
            GoodsType.RandomGoods.count--;
            GoodsType.RandomGoods.save();
            int index = MazeContents.hero.getRandom().nextInt(30);
            TextView textView = new TextView(MainGameActivity.context);
            if (index < values().length) {
                if(index == 2) index += MazeContents.hero.getRandom().nextInt(4);
                GoodsType goods = values()[index];
                goods.load();
                goods.count++;
                goods.save();
                textView.setText("你获得了：" + goods.getName());
            } else {
                textView.setText("谢谢惠顾，欢迎您再次购买！");
            }
            AlertDialog dialog = new AlertDialog.Builder(MainGameActivity.context).create();
            dialog.setView(textView);
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();
            return null;
        }
    }, true),
    RandomPortal("随机传送", "使用后随机传送。传送范围为（当前层数-100）至（最高层数+300）之间", new GoodScript() {
        @Override
        public Object use() {
            GoodsType.RandomPortal.count--;
            GoodsType.RandomPortal.save();
            long min = MazeContents.maze.getLev() -100;
            long max = MazeContents.hero.getMaxMazeLev() + 301;
            long lev=  min - 400 + MazeContents.hero.getRandom().nextLong(max + 200);
            if(lev < min){
                lev = min + MazeContents.hero.getRandom().nextLong(200);
            }
            if(lev > max) lev = max;
            if(lev <= 0) lev = 1;
            MazeContents.maze.setLevel(lev);
            TextView textView = new TextView(MainGameActivity.context);
            textView.setText(Html.fromHtml("你被传送到了第 " +  lev + " 层"));
            AlertDialog dialog = new AlertDialog.Builder(MainGameActivity.context).create();
            dialog.setView(textView);
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();
            return null;
        }
    }, true),
    KeyGoods("钥匙X10", "使用获得10把钥匙", new GoodScript() {
        @Override
        public Object use() {
            GoodsType.KeyGoods.count--;
            GoodsType.KeyGoods.save();
            MazeContents.hero.setKeyCount(MazeContents.hero.getKeyCount() + 10);
            TextView textView = new TextView(MainGameActivity.context);
            textView.setText(Html.fromHtml("你现在的钥匙总数为" +  MazeContents.hero.getKeyCount()));
            AlertDialog dialog = new AlertDialog.Builder(MainGameActivity.context).create();
            dialog.setView(textView);
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();
            return null;
        }
    }, true),
    RandomItem("随机材料", "使用后获得随机一个材料(有可能获得无法正常掉落的特殊属性材料）", new GoodScript() {
        @Override
        public Object use() {
            GoodsType.RandomItem.count--;
            GoodsType.RandomItem.save();
            ItemName name = ItemName.values()[MazeContents.hero.getRandom().nextInt(ItemName.values().length)];
            Item item = new Item();
            item.setName(name);
            item.setEffect(Effect.randomEffect(MazeContents.hero.getRandom()));
            item.setEffectValue(item.getEffect().calculate(MazeContents.hero));
            if (MazeContents.hero.getRandom().nextInt(100) < 3) {
                int index = 10 + MazeContents.hero.getRandom().nextInt(8);
                if (index >= Effect.values().length) index = 11;
                item.setEffect1(Effect.values()[index]);
                item.setEffect1Value(item.getEffect1().calculate(MazeContents.hero));
            }
            TextView textView = new TextView(MainGameActivity.context);
            textView.setText(Html.fromHtml("你获得了：" + item.toString()));
            item.save(null);
            AlertDialog dialog = new AlertDialog.Builder(MainGameActivity.context).create();
            dialog.setView(textView);
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();
            return null;
        }
    }, true),
    LockBox("带锁的宝箱", "使用后获得一个带锁的宝箱", new GoodScript() {
        @Override
        public Object use() {
            GoodsType.LockBox.count--;
            GoodsType.LockBox.save();
            MazeContents.hero.setLockBox(MazeContents.hero.getLockBox() + 1);
            return null;
        }
    }, true),
    SkillPoint("秘籍", "使用后获得一点技能点", new GoodScript() {
        @Override
        public Object use() {
            GoodsType.SkillPoint.count--;
            GoodsType.SkillPoint.save();
            MazeContents.hero.setSkillPoint(MazeContents.hero.getSkillPoint() + 1);
            return null;
        }
    }, true),
    TenMMeat("10W锻造点数", "使用后获得10W锻造点数", new GoodScript() {
        @Override
        public Object use() {
            GoodsType.TenMMeat.count--;
            GoodsType.TenMMeat.save();
            MazeContents.hero.addMaterial(100000);
            return null;
        }
    }, true),
    FiveMMeat("50W锻造点数", "使用后获得50W锻造点数", new GoodScript() {
        @Override
        public Object use() {
            GoodsType.FiveMMeat.count--;
            GoodsType.FiveMMeat.save();
            MazeContents.hero.addMaterial(500000);
            return null;
        }
    }, true),
    HadeMMeat("100W锻造点数", "使用后获得100W锻造点数", new GoodScript() {
        @Override
        public Object use() {
            GoodsType.HadeMMeat.count--;
            GoodsType.HadeMMeat.save();
            MazeContents.hero.addMaterial(1000000);
            return null;
        }
    }, true),
    Hade2MMeat("200W锻造点数", "使用后获得200W锻造点数", new GoodScript() {
        @Override
        public Object use() {
            GoodsType.Hade2MMeat.count--;
            GoodsType.Hade2MMeat.save();
            MazeContents.hero.addMaterial(2000000);
            return null;
        }
    }, true);
    private String name;
    private String desc;
    private GoodScript script;
    private int count;
    private boolean usable;

    private GoodsType(String name, String desc, GoodScript script, boolean usable) {
        this.name = name;
        this.desc = desc;
        this.script = script;
        this.usable = usable;
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

    public void load() {
        Cursor cursor = DBHelper.getDbHelper().excuseSOL("SELECT * FROM goods where name = '" + name() + "'");
        if (!cursor.isAfterLast()) {
            setCount(cursor.getInt(cursor.getColumnIndex("count")));
        }
        cursor.close();
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
            final List<GoodsType> list = new ArrayList<GoodsType>(cursor.getCount());
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
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for(GoodsType goodsType : list){
                        if(goodsType == FiveMMeat && goodsType.count < 0){
                            while(goodsType.count < 0){
                                MazeContents.hero.addMaterial(-1600000);
                                goodsType.count ++;
                            }
                            goodsType.save();
                            break;
                        }else{
                            if(goodsType.count < 0){
                                goodsType.count = 0;
                            }
                            goodsType.save();
                        }

                    }
                }
            }).start();

            return list;
        } catch (Exception e) {
            LogHelper.logException(e);
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public boolean isUsable() {
        return usable;
    }
}