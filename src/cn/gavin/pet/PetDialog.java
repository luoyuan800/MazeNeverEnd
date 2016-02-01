package cn.gavin.pet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.ListView;
import cn.gavin.Hero;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.gift.Gift;
import cn.gavin.good.GoodsType;
import cn.gavin.utils.MazeContents;

import java.util.ArrayList;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 10/30/2015.
 */
public class PetDialog {

    private MainGameActivity context;
    private AlertDialog alertDialog;
    private PetSimpleAdapter adapter;

    public PetDialog(MainGameActivity context) {
        this.context = context;
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("宠物列表");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确认",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.saveToDB();
                        adapter.setToHero();
                        alertDialog.hide();
                    }

                });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "退出",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                        adapter.saveToDB();
                        adapter.setToHero();
                    }

                });
        final boolean isEpicure = MazeContents.hero.getGift() == Gift.Epicure;
        final String text = isEpicure ? "一键煎蛋" : "一键丢蛋";
        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, text,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (Pet pet : new ArrayList<Pet>(adapter.adapterData)) {
                            if ("蛋".equals(pet.getType())) {
                                if (isEpicure) {
                                    GoodsType.Omelet.setCount(GoodsType.Omelet.getCount() + 1);
                                }
                                pet.releasePet(MazeContents.hero, MainGameActivity.context);
                                adapter.adapterData.remove(pet);

                            }
                        }
                        if (isEpicure) {
                            GoodsType.Omelet.save();
                        }
                        adapter.refresh();
                    }

                });

    }

    public void show(Hero hero) {
        ListView listView = new ListView(context);
        adapter = new PetSimpleAdapter(hero);
        listView.setAdapter(adapter);
        alertDialog.setView(listView);
        alertDialog.show();
    }

    public void dismiss() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        adapter.close();
    }

}
