package cn.gavin.pet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import cn.gavin.Hero;
import cn.gavin.R;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.activity.MazeContents;

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
                        alertDialog.hide();
                        adapter.saveToDB();
                        adapter.setToHero();
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
        if (alertDialog != null)
            alertDialog.dismiss();
    }

}
