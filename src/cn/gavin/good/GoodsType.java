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
    Incubator("恒温箱", "自动孵蛋器，把你的蛋丢进去！按顺序自动孵化没有带在身上的蛋，孵化速度比带在身上慢。", new GoodScript() {
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
                            break;
                        }
                    }
                }
            }
            return null;
        }
    }, false),
    HadeMPoint("100W能力点数", "使用后获得100W能力点数", new GoodScript() {
        @Override
        public Object use() {
            if (HadeMPoint.count > 0) {
                GoodsType.HadeMPoint.count--;
                GoodsType.HadeMPoint.save();
                MazeContents.hero.addPoint(1000000);
            }
            return null;
        }
    }, true),
    ResetGift("洗髓丹", "使用后可以重现选择天赋", new GoodScript() {
        @Override
        public Object use() {
            if (HadeMPoint.count > 0) {
                GoodsType.ResetGift.count--;
                GoodsType.ResetGift.save();
                MainGameActivity.context.showGiftChoose();
            }
            return null;
        }
    }, true),
    HowToChangeHead("一篇文档", "这是一篇残缺的文档", new GoodScript() {
        @Override
        public Object use() {
            if (HadeMPoint.count > 0) {
                AlertDialog dialog = new AlertDialog.Builder(MainGameActivity.context).create();
                dialog.setTitle("如何更换██");
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "读完了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.setMessage("准备四张png图片素材（██*██， 最好是成比例的，否则会拉伸变形）\n" +
                        "分别修改名字为\n" +
                        "h_1.███\n" +
                        "h_2.███\n" +
                        "███████\n" +
                        "███.png\n" +
                        "将这些图片放到maze目录的image文件夹下就可以了\n" +
                        "如果出现了█████████，你可以<残缺无法阅读>\n" +
                        "之后你还可以在<这部分缺失掉了>\n" +
                        "是滴，████那个萌萌的██就只有████~~███");
                dialog.show();
            }
            return null;
        }
    }, true),
    TowerDoc("一篇文档", "这是一篇残缺的文档", new GoodScript() {
        @Override
        public Object use() {
            if (HadeMPoint.count > 0) {
                AlertDialog dialog = new AlertDialog.Builder(MainGameActivity.context).create();
                dialog.setTitle("███塔");
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "读完了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.setMessage("这是一作耸立在██中心的塔，一眼望去根本就看不到这座塔的顶端在哪里，似乎这座塔的高处永远被云层遮挡着。\n" +
                        "传说这座塔的历史比████还要悠远，传说这是因为存在这座塔，<无法阅读>。\n" +
                        "考核之塔对于██████████禁区，<无法阅读>进去其中，因为这座塔的█████是██的███。\n" +
                        "是的，没看错，这座本来就占地广阔的███塔中，有整整十层是<无法阅读>…\n" +
                        "当然并不是所有的██都可以进去到所有的楼层楼层中，██招收的██都是年满██岁的少男少女，那么，███████████第一层████。\n" +
                        "但是想要进去第█层，并不是到███的时候才可以进去，而是需要在<无法阅读>中，战胜当前层的█████才可以进去上一层。\n" +
                        "而值得一提的是，<无法阅读>担任的，不要以为██████就没有战斗力了，<无法阅读>的资格。\n" +
                        "是的，不管██还是██，能进去███塔多少层，都是靠自己去<无法阅读>！而上到了第██层之后，将不再是属于███的范围了。\n" +
                        "██层开始，每一层的███，<无法阅读>。 " +
                        "<无法阅读>在任何地方瞬间传送回对应的楼层。\n" +
                        "因为███塔███开放███，只有在有███登上█████，会自动<无法阅读>那么登上楼层的那个███就会成为█████，同时旧<这部分似乎被人工删除> ");
                dialog.show();
            }
            return null;
        }
    }, true),
    XiaoMeiDoc("一篇文档", "这是一篇残缺的文档，似乎是关于某个人物介绍的", new GoodScript() {
        @Override
        public Object use() {
            if (HadeMPoint.count > 0) {
                AlertDialog dialog = new AlertDialog.Builder(MainGameActivity.context).create();
                dialog.setTitle("██小梅███");
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "读完了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.setMessage("战火纷飞，小梅看着外面那混乱的战斗场面，心理无比的凄凉。她站在███中，想着自己的使命，也想着自己在这个██中留下的痕迹，在倒计时结束后她就会被传送到另外一个██中了，只是那个人是否知道自己即将远离他而去呢？\n" +
                        "一个███穿过一个防守战士的身体，那个战士瞬间就化为灰烬，███在穿过人体后依旧威力不减，直射到了███上，只是有██生成的██有怎么能轻易被另外一个██的力量击穿呢，███在在碰上███之后就碎散了开来，就像绽开的烟花，然后消散在空气中。\n" +
                        "<无法阅读>最常见的组合，现在这个战斗中，空中飞着的<这部无法阅读>\n" +
                        "小梅想起教导者们说的过的故事，在上一次的<无法阅读>，似乎有一位██加入了自己这一方，那个██加入使得她们的对抗更加持久一点，只是依旧没有改变██，<无法阅读>之后，便重新开始了，真正的从零开始。\n" +
                        "<无法阅读>小梅负责消除██发展中的出现一些变数。\n" +
                        "小梅忽然想到他<这部分无法阅读>\n" +
                        "<模糊到无法阅读>而是彻底的站在了对立面。组织中本来是希望通过自己引导他成为自己这边的人，这是一个漫长而且细密的计划。只是在小梅自己觉醒那个在她出现就被植入的记忆之前，她就已经无可救药的爱上了他，而他也爱上了她。\n" +
                        "在觉醒了记忆之后，小梅忽然觉得自己之前所做的一切和自己未来要做的一切都那么的飘渺，那么虚无，一切都是不存在的，不存在这个██中，也不存在那个██中。\n" +
                        "小梅甚至不知道自己是属于哪个██，她爱的人<缺失无法阅读>又或者这些爱情就根本不应该存在，自己是另外一个██的人，所有的交集都是那么的虚幻。\n" +
                        "一个光球飞进了防守军的队列中，然后扩大，罩住了一片区域，<缺失无法阅读>场中一片火光，不知道又有多少战士在这片火光中化为██。\n" +
                        "小梅跌坐在地上，她知道这片光球的力量就是他的██力量，她想起当初他们一起研究这种力量的用处之时是那么的开心，那么的幸福，那个时候他们谁都没有想到这种力量可以用来杀人。\n" +
                        "他的心中是否会像自己一样觉得失落呢？小梅想到，曾经的他是一个那么害羞的男孩，不会生气的性格，即使对待一个自己制造的████都那么友好。只是现在的他似乎早已经变了，从<无法阅读>\n" +
                        "人类最强的██加入战场后，似乎一切都不存在悬念了，<缺失无法阅读>法再对进攻者造成阻拦的效果，那个最强的█████████████无效化。这就是最强者的能力，他冷血的释放着自己的技能，然后让自己的██去收割敌人的生命。\n" +
                        "防守的队伍不断的在减少，有的人倒下了，有的人受伤倒在地上痛苦的呻吟，有的人被化成灰烬飘散在这个战场的上空……\n" +
                        "小梅子在███中和那些即将和自己一起传送走的人们默默的看着战斗。\n" +
                        "他们不是冷血，只是他们负着更加重大的责任，即将传送的的这些人中能能够成功到达的，或许只是那么寥寥的几个，又或者一个都没有。惨烈的牺牲只是为了能换来那么一点点的可能。\n" +
                        "倒计时终于结束了，而同一时刻，<无法阅读>防守者们再也无法抵挡██组成的队伍。\n" +
                        "小梅看到了那个最前面的人，所有的攻击落到他身前都会<无法阅读>没有任何一方的攻击能够近得到他的身边。\n" +
                        "小梅看者他走向自己<被人工删除的痕迹导致无法阅读>\n" +
                        "传送的的力量开启了，眼前能够看到的景色越来越模糊。“<无法阅读>最后还能再看你一眼，真好！”小梅勉强的笑着对他说，只是传送的开启，外面的人根本就不能听到里面的声音了。在景色最后消失的那一刻，小梅似乎看到了他█████，还有他眼中的██。\n" +
                        "“他是想留下我，还是想消灭我呢？”小梅猜测着他最后做出的那个动作，“无论如何，<无法阅读>”小梅自我安慰的想到，她的脸上有露出了那种纯纯的笑容，或许在另外一个██中，自己会以不同的形态继续的想念着他吧。\n" +
                        "███所在的区域中闪耀出一团白光，然后迅速的收缩成一个光点，<无法阅读>仿佛叹了一口，接着消失了。\n" +
                        "█████所在的地方忽然扩散出一阵白光，这阵白光不断的扩大，所过之处，摧毁了一切生命，没有任何声音，一切都显得那个的静谧，仿佛这道光就是这么悄悄的存在，悄悄的离去。只是，从此这个██████再也不会存在了……");
                dialog.show();
            }
            return null;
        }
    }, true);
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