package cn.gavin.good;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.text.Html;
import android.text.InputFilter;
import android.widget.*;
import cn.gavin.Element;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.db.DBHelper;
import cn.gavin.forge.Item;
import cn.gavin.forge.adapter.AccessoryAdapter;
import cn.gavin.forge.effect.Effect;
import cn.gavin.forge.list.ItemName;
import cn.gavin.log.LogHelper;
import cn.gavin.pet.Pet;
import cn.gavin.pet.PetAdapter;
import cn.gavin.pet.PetDB;
import cn.gavin.utils.MazeContents;
import cn.gavin.utils.Random;
import cn.gavin.utils.StringUtils;

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
                    try {
                        if (Aphrodisiac.count > 0) {
                            AlertDialog alertDialog = new AlertDialog.Builder(MainGameActivity.context).create();
                            alertDialog.setTitle("你获得了一个蛋");
                            List<Pet> pets = new ArrayList<Pet>(MazeContents.hero.getPets());
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
                        }
                    } catch (Exception e) {
                        LogHelper.logException(e, false);
                    }
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
            if (RandomGoods.count > 0) {
                GoodsType.RandomGoods.count--;
                GoodsType.RandomGoods.save();
                int index = MazeContents.hero.getRandom().nextInt(30);
                TextView textView = new TextView(MainGameActivity.context);
                if (index == 2) index += MazeContents.hero.getRandom().nextInt(14);
                if (index == 14) index += 1 + MazeContents.hero.getRandom().nextInt(10);
                if (index < values().length) {
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
            }
            return null;
        }
    }, true),
    RandomPortal("随机传送", "使用后随机传送。传送范围为（当前层数-100）至（最高层数80%）之间", new GoodScript() {
        @Override
        public Object use() {
            if (RandomPortal.count > 0) {
                GoodsType.RandomPortal.count--;
                GoodsType.RandomPortal.save();
                long min = MazeContents.maze.getLev() - 100;
                long max = (long) (MazeContents.hero.getMaxMazeLev().doubleValue() * 0.8);
                long lev = min - 100 + MazeContents.hero.getRandom().nextLong(max + 100);
                if (lev < min) {
                    lev = min + MazeContents.hero.getRandom().nextLong(200);
                }
                if (lev > max) lev = max;
                if (lev <= 0) lev = 1;
                MazeContents.maze.setLevel(lev);
                TextView textView = new TextView(MainGameActivity.context);
                textView.setText(Html.fromHtml("你被传送到了第 " + lev + " 层"));
                AlertDialog dialog = new AlertDialog.Builder(MainGameActivity.context).create();
                dialog.setView(textView);
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
            return null;
        }
    }, true),
    KeyGoods("钥匙X10", "使用获得10把钥匙", new GoodScript() {
        @Override
        public Object use() {
            if (KeyGoods.count > 0) {
                GoodsType.KeyGoods.count--;
                GoodsType.KeyGoods.save();
                MazeContents.hero.setKeyCount(MazeContents.hero.getKeyCount() + 10);
                TextView textView = new TextView(MainGameActivity.context);
                textView.setText(Html.fromHtml("你现在的钥匙总数为" + MazeContents.hero.getKeyCount()));
                AlertDialog dialog = new AlertDialog.Builder(MainGameActivity.context).create();
                dialog.setView(textView);
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
            return null;
        }
    }, true),
    RandomItem("随机材料", "使用后获得随机一个材料(有可能获得无法正常掉落的特殊属性材料）", new GoodScript() {
        @Override
        public Object use() {
            if (RandomItem.count > 0) {
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
            }
            return null;
        }
    }, true),
    LockBox("带锁的宝箱", "使用后获得一个带锁的宝箱", new GoodScript() {
        @Override
        public Object use() {
            if (LockBox.count > 0) {
                GoodsType.LockBox.count--;
                GoodsType.LockBox.save();
                MazeContents.hero.setLockBox(MazeContents.hero.getLockBox() + 1);
            }
            return null;
        }
    }, true),
    SkillPoint("秘籍", "使用后获得一点技能点", new GoodScript() {
        @Override
        public Object use() {
            if (SkillPoint.count > 0) {
                GoodsType.SkillPoint.count--;
                GoodsType.SkillPoint.save();
                MazeContents.hero.setSkillPoint(MazeContents.hero.getSkillPoint() + 1);
            }
            return null;
        }
    }, true),
    TenMMeat("10W锻造点数", "使用后获得10W锻造点数", new GoodScript() {
        @Override
        public Object use() {
            if (TenMMeat.count > 0) {
                GoodsType.TenMMeat.count--;
                GoodsType.TenMMeat.save();
                MazeContents.hero.addMaterial(100000);
            }
            return null;
        }
    }, true),
    FiveMMeat("50W锻造点数", "使用后获得50W锻造点数", new GoodScript() {
        @Override
        public Object use() {
            if (FiveMMeat.count > 0) {
                GoodsType.FiveMMeat.count--;
                GoodsType.FiveMMeat.save();
                MazeContents.hero.addMaterial(500000);
            }
            return null;
        }
    }, true),
    HadeMMeat("100W锻造点数", "使用后获得100W锻造点数", new GoodScript() {
        @Override
        public Object use() {
            if (HadeMMeat.count > 0) {
                GoodsType.HadeMMeat.count--;
                GoodsType.HadeMMeat.save();
                MazeContents.hero.addMaterial(1000000);
            }
            return null;
        }
    }, true),
    Hade2MMeat("200W锻造点数", "使用后获得200W锻造点数", new GoodScript() {
        @Override
        public Object use() {
            if (Hade2MMeat.count > 0) {
                GoodsType.Hade2MMeat.count--;
                GoodsType.Hade2MMeat.save();
                MazeContents.hero.addMaterial(2000000);
            }
            return null;
        }
    }, true),
    RenameAcc("装备命名", "使用后选择一件装备自定义名字和描述(到达50000层免费赠送一个)。", new GoodScript() {
        @Override
        public Object use() {
            if (RenameAcc.count > 0) {
                GoodsType.RenameAcc.count--;
                GoodsType.RenameAcc.save();
                AlertDialog selectAccDialog = new AlertDialog.Builder(MainGameActivity.context).create();
                selectAccDialog.setTitle("选择一件装备");
                AccessoryAdapter accessoryAdapter = new AccessoryAdapter(2, selectAccDialog);
                ListView listView = new ListView(MainGameActivity.context);
                listView.setAdapter(accessoryAdapter);
                selectAccDialog.setView(listView);
                selectAccDialog.show();
            }
            return null;
        }
    }, true),
    RenamePet("宠物前缀", "使用后选择一只宠物自定义前缀名(到达100000层免费赠送一个)。", new GoodScript() {
        @Override
        public Object use() {
            if (RenamePet.count > 0) {
                GoodsType.RenamePet.count--;
                GoodsType.RenamePet.save();
                AlertDialog selectPetDialog = new AlertDialog.Builder(MainGameActivity.context).create();
                selectPetDialog.setTitle("选择宠物");
                PetAdapter petAdapter = new PetAdapter(selectPetDialog);
                ListView listView = new ListView(MainGameActivity.context);
                listView.setAdapter(petAdapter);
                selectPetDialog.setView(listView);
                selectPetDialog.show();
            }
            return null;
        }
    }, true),
    Barrier("单人房", "宠物每次想下蛋会被自动消耗来阻止。", new GoodScript() {
        @Override
        public Object use() {
            if (Barrier.count > 0) {
                GoodsType.Barrier.count--;
                GoodsType.Barrier.save();
            }
            return null;
        }
    }, false),
    Filter("过滤器", "使用后可以设计一个捕捉条件，那么只有符合条件的怪物被打败之后才可以捕捉。" +
            "比如你只想捕捉’龙‘，那么可以在弹出窗口中输入’龙‘之后确定；如果你只想捕捉前缀为’无敌‘的怪物，那么在设定过滤条件为’无敌的‘即可。" +
            "修改之后永久生效，如果要更改需要重新使用一个过滤器进行设置", new GoodScript() {
        @Override
        public Object use() {
            if (Filter.count > 0) {
                GoodsType.Filter.count--;
                GoodsType.Filter.save();
                AlertDialog filterDialog = new AlertDialog.Builder(MainGameActivity.context).create();
                filterDialog.setTitle("过滤条件");
                final EditText filterText = new EditText(MainGameActivity.context);
                filterText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
                filterText.setText("输入过滤条件");
                filterDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        MazeContents.maze.setCatchPetNameContains(filterText.getText().toString());
                    }
                });
                filterDialog.setView(filterText);
                filterDialog.show();
            }
            return null;
        }
    }, true),
    ResetSkill("易筋经", "使用后可以重置技能点。技能重置后返回激活的点数，升级消耗的技能点不会返还。"
            , new GoodScript() {
        @Override
        public Object use() {
            if (ResetSkill.count > 0) {
                GoodsType.ResetSkill.count--;
                GoodsType.ResetSkill.save();
                MainGameActivity.context.showResetSkillPointDialog();
            }
            return null;
        }
    }, true),
    Pretend("伪装", "使用后选择一个五行属性，可以在一段时间变化自己的五行属性为选择的那个。退出游戏会导致这个效果消失。"
            , new GoodScript() {
        @Override
        public Object use() {
            if (Pretend.count > 0) {
                GoodsType.Pretend.count--;
                GoodsType.Pretend.save();
                AlertDialog pretendDialog = new AlertDialog.Builder(MainGameActivity.context).create();
                pretendDialog.setTitle("选择五行属性");
                final Spinner spinner = new Spinner(MainGameActivity.context);
                List<String> elements = new ArrayList<String>(5);
                for (Element e : Element.values()) {
                    elements.add(e.name());
                }
                ArrayAdapter fa = new ArrayAdapter<String>(MainGameActivity.context, android.R.layout.simple_spinner_item, elements);
                fa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(fa);
                pretendDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MazeContents.hero.setPElement(Element.values()[spinner.getSelectedItemPosition()]);
                        dialog.dismiss();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000 * 60 * 5);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                MazeContents.hero.setPElement(null);
                                MainGameActivity.context.addMessage("伪装的效果消失了");
                            }
                        }).start();
                    }
                });
                pretendDialog.setView(spinner);
                pretendDialog.show();
            }
            return null;
        }
    }, true),
    HPM("小红药", "生命值低于50%的时候会自动使用立即恢复30%的生命值。", new GoodScript() {
        @Override
        public Object use() {
            if (HPM.count > 0 && !HPM.isLock()) {
                GoodsType.HPM.count--;
                GoodsType.HPM.save();
                MazeContents.hero.addHp((long) (MazeContents.hero.getUpperHp() * 0.3));
                MainGameActivity.context.addMessage("使用了小红药恢复了30%的生命值。");
            }
            return null;
        }
    }, false),
    HPML("大血瓶", "生命值低于10%的时候会自动使用立即恢复80%的生命值。", new GoodScript() {
        @Override
        public Object use() {
            if (HPML.count > 0 && !HPML.isLock()) {
                GoodsType.HPML.count--;
                GoodsType.HPML.save();
                MazeContents.hero.addHp((long) (MazeContents.hero.getUpperHp() * 0.8));
                MainGameActivity.context.addMessage("使用了大血瓶恢复了80%的生命值。");
            }
            return null;
        }
    }, false),
    ClosePortal("力场抗拒器", "踩到传送门的时候自动使用，不会被传送走。只会在爬楼过程中踩到随机传送门的时候生效。", new GoodScript() {
        @Override
        public Object use() {
            if (ClosePortal.count > 0) {
                GoodsType.ClosePortal.count--;
                GoodsType.ClosePortal.save();
                MainGameActivity.context.addMessage(MazeContents.hero.getFormatName() + "踩到了传送门，但是因为" + ClosePortal.getName() + "没有被传送走。");
            }
            return null;
        }
    }, false),
    Mirror("镜子", "爱美人士必备，爬楼再辛苦也要保持形象哦。", new GoodScript() {
        @Override
        public Object use() {
            return null;
        }
    }, false),
    Omelet("煎蛋", "传说中的煎蛋。吃下去之后发生随机事件。", new GoodScript() {
        @Override
        public Object use() {
            if (Omelet.count > 0) {
                GoodsType.Omelet.count--;
                GoodsType.Omelet.save();
                int index = MazeContents.hero.getRandom().nextInt(6);
                AlertDialog dialog = new AlertDialog.Builder(MainGameActivity.context).create();
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                String msg = "";
                switch (index){
                    case 0:
                        MazeContents.hero.addHp((long) (MazeContents.hero.getUpperHp() * 0.6));
                        msg = "使用煎蛋恢复了60%的生命值。";
                        MainGameActivity.context.addMessage(msg);
                        break;
                    case 1:
                        MazeContents.hero.restore();
                         msg = "使用煎蛋恢复了全部的生命值并且复活了所有宠物。";
                        MainGameActivity.context.addMessage(msg);
                        break;
                    case 2:
                        msg = "使用煎蛋恢复了复活了所有宠物。";
                        MainGameActivity.context.addMessage(msg);
                        for (Pet pet : new ArrayList<Pet>(MazeContents.hero.getPets())) {
                            pet.restore();
                        }
                        break;
                    case 3:
                        msg = "食用煎蛋肚子疼，导致生命值减少50%。";
                        MainGameActivity.context.addMessage(msg);
                        MazeContents.hero.addHp(-(long)(MazeContents.hero.getUpperHp() * 0.5));
                        break;
                    case 4:
                        msg = "食用了一个黑乎乎的煎蛋，导致生命值变为1。";
                        MainGameActivity.context.addMessage(msg);
                        MazeContents.hero.setHp(1);
                        break;
                    case 5:
                        msg = "使用煎蛋后掉进了一个洞";
                        MainGameActivity.context.addMessage(msg);
                        if(MazeContents.hero.getRandom().nextInt(100) > 3){
                            MainGameActivity.context.addMessage("不知道怎么跑到最高层了");
                            msg += "不知道怎么跑到最高层了";
                            MazeContents.maze.setLevel(MazeContents.hero.getMaxMazeLev());
                        }else{
                            long leve = MazeContents.hero.getRandom().nextLong(MazeContents.hero.getMaxMazeLev() - 1000);
                            MainGameActivity.context.addMessage("不知道怎么着就跑到" + leve + "层了");
                            msg += "不知道怎么着就跑到" + leve + "层了";
                            MazeContents.maze.setLevel(1000);
                        }
                }
                dialog.setMessage(msg);
                dialog.show();
            }
            return null;
        }
    }, true),
    Incubator("蛋蛋袋", "自动孵蛋器，把你的蛋丢进去！按顺序自动选择没有带在身上的蛋孵化，孵化一个蛋之后消失。", new GoodScript() {
        @Override
        public Object use() {
            if (Incubator.count > 0) {
                for(Pet pet : PetDB.loadPet(null)){
                    if("蛋".equals(pet.getType()) && !pet.isOnUsed()){
                        pet.setDeathCount(pet.getDeathCount() -1);
                        if(pet.getDeathCount() <= 0){
                            MainGameActivity.context.addMessage(pet.getFormatName() + "出生了！");
                            try {
                                Cursor cursor = DBHelper.getDbHelper().excuseSOL("select catch meet_lev from monster where id = '" + pet.getIndex() + "'");
                                if (!cursor.isAfterLast()) {
                                    DBHelper.getDbHelper().excuseSQLWithoutResult("update monster set " +
                                            "catch = '" + (StringUtils.toLong(cursor.getString(cursor.getColumnIndex("catch"))) + 1) +
                                            " where id ='" + pet.getIndex() + "'");
                                    if (StringUtils.toLong(cursor.getString(cursor.getColumnIndex("meet_lev"))) == 0) {
                                        DBHelper.getDbHelper().excuseSQLWithoutResult("update monster set " +
                                                "meet_lev = '" + MazeContents.maze.getLev() +
                                                " where id ='" + pet.getIndex() + "'");
                                    }
                                }
                                cursor.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                                LogHelper.logException(e, false);
                            }
                            int index = pet.getIndex();
                            Cursor cursor = DBHelper.getDbHelper().excuseSOL("select type from monster where id = '" + index + "'");
                            if (cursor != null && !cursor.isAfterLast()) {
                                pet.setType(cursor.getString(cursor.getColumnIndex("type")));
                            }
                            pet.setLev(MazeContents.maze.getLev());
                            pet.setDeathCount(0);
                            PetDB.save(pet);
                            Incubator.count--;
                            Incubator.save();
                            break;
                        }
                    }
                }
            }
            return null;
        }
    }, false);
    private String name;
    private String desc;
    private GoodScript script;
    private int count;
    private boolean usable;
    private boolean isLock;

    private GoodsType(String name, String desc, GoodScript script, boolean usable) {
        this.name = name;
        this.desc = desc;
        this.script = script;
        this.usable = usable;
        load();
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

    public Object use(){
        return script.use();
    }

    public void save() {
        DBHelper.getDbHelper().excuseSQLWithoutResult(
                String.format("REPLACE INTO goods (name,count, lock) values ('%s',%s, %s)", name(), count, isLock() ? 1 : 0));
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
            Integer lock = cursor.getInt(cursor.getColumnIndex("lock"));
            if(lock == 1){
                setLock(true);
            }else{
                setLock(false);
            }
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
                    LogHelper.logException(e, false);
                    e.printStackTrace();
                }
                cursor.moveToNext();
            }
            cursor.close();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (GoodsType goodsType : list) {
                        if (goodsType == FiveMMeat && goodsType.count < 0) {
                            while (goodsType.count < 0) {
                                if (MazeContents.hero.getMaterial() <= 0) {
                                    if (MazeContents.hero.getPoint() > 160000) {
                                        MazeContents.hero.addPoint(-160000);
                                    } else {
                                        MazeContents.hero.addLife(-16000);
                                        MazeContents.hero.addAgility(-16000);
                                        MazeContents.hero.addStrength(-16000);
                                    }
                                } else {
                                    MazeContents.hero.addMaterial(-1600000);
                                }
                                goodsType.count++;
                            }
                            goodsType.save();
                            break;
                        } else {
                            if (goodsType.count < 0) {
                                goodsType.count = 0;
                            }
                            goodsType.save();
                        }

                    }
                }
            }).start();

            return list;
        } catch (Exception e) {
            LogHelper.logException(e, false);
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public boolean isUsable() {
        return usable;
    }

    public boolean isLock() {
        return isLock;
    }

    public void setLock(boolean isLock) {
        this.isLock = isLock;
        save();
    }
}