package cn.gavin.monster;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import cn.gavin.R;

/**
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED
 * Created by luoyuan on 10/7/15.
 */
public class MonsterDetailDialog implements View.OnClickListener {
    private final TextView maxAtkNameText;
    private final TextView killedCountText;
    private final TextView killCountText;
    private final TextView maxAtkDescText;
    private final TextView maxHpDescText;
    private final TextView maxHpAtkText;
    private final TextView maxHpHpText;
    private final TextView maxHpNameText;
    private final TextView maxHpLvText;
    private final TextView maxAtkLvText;
    private final TextView maxAtkHpText;
    private final TextView maxAtkAtkText;
    private Context context;
    private AlertDialog alertDialog;
    private MonsterItem monster;
    public MonsterDetailDialog(Context context) {
        this.context = context;
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.hide();
                    }
                });
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.monster_detail, (ViewGroup) ((Activity) context).findViewById(R.id.monster_detail));
        alertDialog.setView(view);
        maxAtkNameText = (TextView) view.findViewById(R.id.max_atk_name);
        maxAtkAtkText = (TextView) view.findViewById(R.id.max_atk_atk);
        maxAtkHpText = (TextView) view.findViewById(R.id.max_atk_hp);
        maxAtkLvText = (TextView) view.findViewById(R.id.max_atk_lev);
        maxHpLvText = (TextView) view.findViewById(R.id.max_hp_lev);
        maxHpNameText = (TextView) view.findViewById(R.id.max_hp_name);
        maxHpHpText = (TextView) view.findViewById(R.id.max_hp_hp);
        maxHpAtkText = (TextView) view.findViewById(R.id.max_hp_atk);
        maxHpDescText = (TextView) view.findViewById(R.id.max_hp_desc);
        maxHpDescText.setOnClickListener(this);
        maxAtkDescText = (TextView) view.findViewById(R.id.max_atk_desc);
        maxAtkDescText.setOnClickListener(this);
        killCountText = (TextView) view.findViewById(R.id.kill_count);
        killedCountText = (TextView) view.findViewById(R.id.killed_count);
    }

    public void show(MonsterItem monster){
        this.monster = monster;
        alertDialog.setTitle(monster.getName() + "详细信息");
        maxAtkNameText.setText(Html.fromHtml(monster.getMaxATKName()));
        maxAtkHpText.setText("HP：" + monster.getMaxATKHP());
        maxAtkAtkText.setText("ATK：" + monster.getMaxATKATK());
        maxAtkLvText.setText("层数：" + (monster.getMaxATKName().contains("殿堂") ? "殿堂_":"") +monster.getMaxATKLev());
        maxAtkDescText.setText("结果：" +(monster.isMaxATKDefeat()?"轻松击败！\n(点击查看）" : "被虐得满地找牙！\n(点击查看)"));
        maxHpNameText.setText(Html.fromHtml(monster.getMaxHPName()));
        maxHpHpText.setText("HP：" + monster.getMaxHPHP());
        maxHpAtkText.setText("ATK：" +monster.getMaxHPATK());
        maxHpLvText.setText("层数：" +(monster.getMaxHPName().contains("殿堂") ? "殿堂_":"") + monster.getMaxHPLev());
        maxHpDescText.setText("结果：" + (monster.isMaxHPDefeat()?"轻松击败！\n(点击查看）" : "被虐得满地找牙！\n(点击查看)"));
        killCountText.setText("击败次数：" + monster.getDefeat());
        killedCountText.setText("被狙杀次数：" + monster.getDefeated());
        alertDialog.show();
    }

    public void dismiss(){
        alertDialog.dismiss();
    }

    @Override
    public void onClick(View view) {
        AlertDialog descDialog = new AlertDialog.Builder(context).create();
        descDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        descDialog.setTitle("战斗信息");
        ScrollView scrollView = new ScrollView(context);
        TextView textView = new TextView(context);
        switch (view.getId()){
            case R.id.max_atk_desc:
                textView.setText(Html.fromHtml(monster.getMaxATKDesc()));
                break;
            case R.id.max_hp_desc:
                textView.setText(Html.fromHtml(monster.getMaxHPDesc()));
                break;
        }
        scrollView.addView(textView);
        descDialog.setView(scrollView);
        descDialog.show();
    }
}
