package cn.gavin.gift;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import cn.gavin.R;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.utils.MazeContents;

/**
 * Copyright 2016 luoyuan.
 * ALL RIGHTS RESERVED
 * Created by luoyuan on 1/1/16.
 */
public class GiftDialog {
    private AlertDialog mainDialog;
    private MainGameActivity context;

    public GiftDialog(MainGameActivity context) {
        mainDialog = new AlertDialog.Builder(context).create();

        this.context = context;
    }

    public void show() {
        mainDialog.show();
        mainDialog.setContentView(R.layout.gift_list);
        mainDialog.setTitle("选择一个天赋");
        mainDialog.setCancelable(false);
        mainDialog.setCanceledOnTouchOutside(false);
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
