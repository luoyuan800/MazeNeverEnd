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
public class PetDialog implements View.OnClickListener {
    private final TextView hpValue;
    private final TextView atkValue;
    private final TextView defValue;
    private final TextView skillName;
    private final TextView nameValue;
    private final Button releaseButton;
    private final ImageView petPic;
    private final TextView petIni;
    private final Button addHpButton;
    private final Button addAtkButton;
    private final Button addDefButton;
    private final TextView leveText;
    private Pet pet;
    private MainGameActivity context;
    private AlertDialog alertDialog;

    public PetDialog(MainGameActivity context) {
        this.context = context;
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("技能选择");
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "退出",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.hide();
                    }

                });

        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.pet_detail, (ViewGroup) context.findViewById(R.id.pet_view_group));
        alertDialog.setView(view);
        nameValue = (TextView) view.findViewById(R.id.name_value);
        hpValue = (TextView) view.findViewById(R.id.hp_value);
        atkValue = (TextView) view.findViewById(R.id.atk_value);
        defValue = (TextView) view.findViewById(R.id.def_value);
        skillName = (TextView) view.findViewById(R.id.pet_skill_name);
        releaseButton = (Button) view.findViewById(R.id.release_pet);
        releaseButton.setOnClickListener(this);
        petPic = (ImageView) view.findViewById(R.id.pet_detail_pic);
        petIni = (TextView) view.findViewById(R.id.pet_ini_value);
        addHpButton = (Button) view.findViewById(R.id.pet_add_hp);
        addHpButton.setOnClickListener(this);
        addAtkButton = (Button) view.findViewById(R.id.pet_add_atk);
        addAtkButton.setOnClickListener(this);
        addDefButton = (Button) view.findViewById(R.id.pet_add_def);
        addDefButton.setOnClickListener(this);
        leveText = (TextView) view.findViewById(R.id.pet_level_label);
    }

    public void show(Pet pet) {
        this.pet = pet;
        handler.sendEmptyMessage(0);
        alertDialog.show();
    }

    public void dismiss() {
        if (alertDialog != null)
            alertDialog.dismiss();
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (pet != null) {
                        nameValue.setText(Html.fromHtml(pet.getFormatName()));
                        leveText.setText(Html.fromHtml("相遇在第<b>" + pet.getLev() + "</b>层"));
                        hpValue.setText(pet.getHp() + "/" + pet.getUHp());
                        atkValue.setText(pet.getAtk() + "");
                        defValue.setText(pet.getDef() + "");
                        if (pet.getSkill() != null) {
                            skillName.setText(pet.getSkill().getName());
                        } else {
                            skillName.setText("无");
                        }
                        petPic.setImageResource(pet.getImage());
                        petIni.setText(pet.getIntimacy() + "");
                        Hero hero = MazeContents.hero;
                        if (hero.getPoint() < 1) {
                            addHpButton.setEnabled(false);
                            addAtkButton.setEnabled(false);
                            addDefButton.setEnabled(false);
                        } else {
                            addHpButton.setEnabled(true);
                            addAtkButton.setEnabled(true);
                            addDefButton.setEnabled(true);
                        }
                    }
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pet_add_def:
                pet.addDef(MazeContents.hero);
                break;
            case R.id.pet_add_atk:
                pet.addAtk(MazeContents.hero);
                break;
            case R.id.pet_add_hp:
                pet.addHp(MazeContents.hero);
                break;
            case R.id.release_pet:
                AlertDialog dialog = new AlertDialog.Builder(context).create();
                dialog.setTitle("确认释放宠物？？");
                TextView view = new TextView(context);
                view.setText(Html.fromHtml("你确认释放宠物：" + pet.getFormatName() + "吗？<br>释放后该宠物即为消失，已经分配的点数不会返回！"));
                dialog.setView(view);
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "退出",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }

                        });
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确认",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialog.hide();
                                dialog.dismiss();
                                pet.releasePet(MazeContents.hero, context);
                            }

                        });
                dialog.show();
                break;
        }
        handler.sendEmptyMessage(0);
    }
}
