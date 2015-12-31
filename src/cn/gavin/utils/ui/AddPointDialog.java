package cn.gavin.utils.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import cn.gavin.Hero;
import cn.gavin.R;
import cn.gavin.utils.MazeContents;

/**
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED
 * Created by luoyuan on 12/6/15.
 */
public class AddPointDialog {
    AlertDialog alertDialog;
    Context context;
    View.OnClickListener addListener;
    View.OnClickListener addNListener;
    public AddPointDialog(Context context){
        this.context = context;
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "退出",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

    }

    public void show(View.OnClickListener addListener, View.OnClickListener addNListener, int image){
        this.addListener = addListener;
        this.addNListener = addNListener;
        View view = View.inflate(context, R.layout.add_point_view,
                (ViewGroup) ((Activity)context).findViewById(R.id.add_point_root));
        alertDialog.setView(view);
        alertDialog.show();
        Button add = (Button) alertDialog.findViewById(R.id.add_point_1);
        Button addN = (Button) alertDialog.findViewById(R.id.add_point_n);
        ((ImageView)alertDialog.findViewById(R.id.add_point_image)).setImageResource(image);
        add.setOnClickListener(addListener);
        addN.setOnClickListener(addNListener);
        refresh();
    }
Handler handler = new Handler(){
    public void handleMessage(Message msg){
        switch (msg.what){
            case 0:
                Button add = (Button) alertDialog.findViewById(R.id.add_point_1);
                Button addN = (Button) alertDialog.findViewById(R.id.add_point_n);
                Hero hero = MazeContents.hero;
                if(hero.getPoint() < 1){
                    add.setEnabled(false);
                    addN.setEnabled(false);
                }
                break;
        }
    }
};
    public void refresh() {
        handler.sendEmptyMessage(0);
    }

    public void setTitle(String title) {
        alertDialog.setTitle(title);
    }
}
