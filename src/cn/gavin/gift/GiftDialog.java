package cn.gavin.gift;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import cn.gavin.Element;
import cn.gavin.R;
import cn.gavin.activity.MainGameActivity;
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
        mainDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                context.getHandler().sendEmptyMessage(123);
                dialog.dismiss();
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
