package cn.gavin.gift;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import cn.gavin.Achievement;
import cn.gavin.Element;
import cn.gavin.R;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.good.GoodsType;
import cn.gavin.skill.SkillFactory;
import cn.gavin.utils.MazeContents;

/**
 * Copyright 2016 luoyuan.
 * ALL RIGHTS RESERVED
 * Created by luoyuan on 1/1/16.
 */
public class GiftDialog {
    private AlertDialog mainDialog;
    private MainGameActivity context;

    public GiftDialog(final MainGameActivity context) {
        mainDialog = new AlertDialog.Builder(context).create();
        View giftView = View.inflate(context,R.layout.gift_list, (ViewGroup) context.findViewById(R.id.gift_root));
        mainDialog.setView(giftView);
        mainDialog.setTitle("选择一个天赋");
        mainDialog.setCancelable(false);
        mainDialog.setCanceledOnTouchOutside(false);
        mainDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                context.getHandler().sendEmptyMessage(123);
            }
        });
        this.context = context;
    }

    public void show() {
        mainDialog.show();
        Button giftButton = (Button) mainDialog.findViewById(R.id.hero_heart);
        if (giftButton != null) {
            giftButton.setOnClickListener(buildDetailAction(Gift.HeroHeart, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    MazeContents.hero.ATR_RISE *= 2;
                    dialogInterface.dismiss();
                    mainDialog.dismiss();
                    MazeContents.hero.setGift(Gift.HeroHeart);
                }
            }));
        }
        giftButton = (Button) mainDialog.findViewById(R.id.DarkHeard);
        if (giftButton != null) {
            giftButton.setOnClickListener(buildDetailAction(Gift.DarkHeard, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    MazeContents.hero.DEF_RISE *= 2;
                    dialogInterface.dismiss();
                    mainDialog.dismiss();
                    MazeContents.hero.setGift(Gift.DarkHeard);
                }
            }));
        }
        giftButton = (Button) mainDialog.findViewById(R.id.Warrior);
        if (giftButton != null) {
            giftButton.setOnClickListener(buildDetailAction(Gift.Warrior, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    MazeContents.hero.setFourthSkillEnable(false);
                    MazeContents.hero.setFifitSkillEnable(false);
                    MazeContents.hero.setSixthSkillEnable(false);
                    MazeContents.hero.ATR_RISE *= 2;
                    MazeContents.hero.DEF_RISE *= 2;
                    MazeContents.hero.MAX_HP_RISE *= 2;
                    dialogInterface.dismiss();
                    mainDialog.dismiss();
                    MazeContents.hero.setGift(Gift.Warrior);
                }
            }));
        }
        giftButton = (Button) mainDialog.findViewById(R.id.Searcher);
        if (giftButton != null) {
            giftButton.setOnClickListener(buildDetailAction(Gift.Searcher, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    mainDialog.dismiss();
                    MazeContents.hero.setGift(Gift.Searcher);
                }
            }));
        }
        giftButton = (Button) mainDialog.findViewById(R.id.Long);
        if (giftButton != null) {
            giftButton.setOnClickListener(buildDetailAction(Gift.Long, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    SkillFactory.getSkill("龙裔", MazeContents.hero).setActive(true);
                    dialogInterface.dismiss();
                    mainDialog.dismiss();
                    MazeContents.hero.setGift(Gift.Long);
                }
            }));
        }
        giftButton = (Button) mainDialog.findViewById(R.id.Element);
        if (giftButton != null) {
            giftButton.setOnClickListener(buildDetailAction(Gift.Element, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    mainDialog.dismiss();
                    MazeContents.hero.setGift(Gift.Element);
                }
            }));
        }
        giftButton = (Button) mainDialog.findViewById(R.id.Pokemon);
        if (giftButton != null) {
            giftButton.setOnClickListener(buildDetailAction(Gift.Pokemon, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    mainDialog.dismiss();
                    MazeContents.hero.setGift(Gift.Pokemon);
                    MazeContents.hero.setPetRate(MazeContents.hero.getPetRate() - 0.5f);
                }
            }));
        }
        giftButton = (Button) mainDialog.findViewById(R.id.FireBody);
        if (giftButton != null) {
            giftButton.setOnClickListener(buildDetailAction(Gift.FireBody, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    mainDialog.dismiss();
                    MazeContents.hero.setGift(Gift.FireBody);
                    MazeContents.hero.setEggRate(MazeContents.hero.getEggRate() + 120);
                }
            }));
        }
        giftButton = (Button) mainDialog.findViewById(R.id.SkillMaster);
        if (giftButton != null) {
            giftButton.setOnClickListener(buildDetailAction(Gift.SkillMaster, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    mainDialog.dismiss();
                    MazeContents.hero.setGift(Gift.SkillMaster);
                }
            }));
        }
        giftButton = (Button) mainDialog.findViewById(R.id.RandomMaster);
        if (giftButton != null) {
            giftButton.setOnClickListener(buildDetailAction(Gift.RandomMaster, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    mainDialog.dismiss();
                    MazeContents.hero.setGift(Gift.RandomMaster);
                }
            }));
        }
        giftButton = (Button) mainDialog.findViewById(R.id.ElementReject);
        if (giftButton != null) {
            giftButton.setOnClickListener(buildDetailAction(Gift.ElementReject, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    mainDialog.dismiss();
                    MazeContents.hero.setGift(Gift.ElementReject);
                    MazeContents.hero.setRejectElement(Element.values()[MazeContents.hero.getRandom().nextInt(Element.values().length)]);
                }
            }));
        }
        giftButton = (Button) mainDialog.findViewById(R.id.Maker);
        if (giftButton != null) {
            giftButton.setOnClickListener(buildDetailAction(Gift.Maker, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    mainDialog.dismiss();
                    MazeContents.hero.setGift(Gift.Maker);
                }
            }));
        }
        giftButton = (Button) mainDialog.findViewById(R.id.Epicure);
        if (giftButton != null) {
            giftButton.setOnClickListener(buildDetailAction(Gift.Epicure, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    mainDialog.dismiss();
                    MazeContents.hero.setGift(Gift.Epicure);
                }
            }));
        }
        giftButton = (Button) mainDialog.findViewById(R.id.Daddy);
        if (giftButton != null) {
            giftButton.setOnClickListener(buildDetailAction(Gift.Daddy, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    mainDialog.dismiss();
                    MazeContents.hero.setGift(Gift.Daddy);
                }
            }));
        }
        giftButton = (Button) mainDialog.findViewById(R.id.ChildrenKing);
        if (giftButton != null) {
            giftButton.setOnClickListener(buildDetailAction(Gift.ChildrenKing, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    mainDialog.dismiss();
                    MazeContents.hero.setGift(Gift.ChildrenKing);
                    boolean allGood = true;
                    for(GoodsType goods : GoodsType.values()){
                        if(goods.getCount() <= 0){
                            allGood = false;
                            break;
                        }
                    }
                    if(allGood){
                        Achievement.GameEnd.enable(MazeContents.hero);
                    }
                    AlertDialog dialog = new AlertDialog.Builder(MainGameActivity.context).create();
                    dialog.setTitle(Gift.ChildrenKing.getName());
                    dialog.setMessage("这个天赋令你可以：\n" +
                            "1. 当敌人是守护者的时候，秒杀他！\n" +
                            "2. 不断的自动增加力量！\n" +
                            "3. 不断的自动恢复生命！\n" +
                            "4. 不断的自动获取能力点数！\n" +
                            "5. 削弱所有的普通怪物！");
                }
            }));
        }
    }

    private View.OnClickListener buildDetailAction(final Gift gift, final DialogInterface.OnClickListener activeAction) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog detailDialog = new AlertDialog.Builder(context).create();
                detailDialog.setTitle(gift.getName());
                detailDialog.setMessage(gift.getDesc());
                detailDialog.setButton(DialogInterface.BUTTON_POSITIVE, "选择", activeAction);
                detailDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                detailDialog.show();
                Button activeButton = detailDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                if (activeButton != null) {
                    activeButton.setEnabled(gift.getRecount() <= MazeContents.hero.getReincaCount());
                }
            }
        };
    }
}
