package cn.gavin.pet.swop.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import cn.gavin.R;
import cn.gavin.pet.Pet;
import cn.gavin.utils.StringUtils;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 12/4/2015.
 */
public class PetInfoDialogBuilder {

    public static AlertDialog build(Pet pet, Context context, String title){
        AlertDialog newPetDialog = new AlertDialog.Builder(context).create();
        newPetDialog.setTitle(Html.fromHtml(title));
        newPetDialog.setButton(DialogInterface.BUTTON_POSITIVE, "好的", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        if ("蛋".equals(pet.getType())) {
            TextView textView = new TextView(context);
            textView.setText("一个蛋");
            newPetDialog.setView(textView);
        } else {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.pet_info, null);
            newPetDialog.setView(view);
            TextView nameValue = (TextView) view.findViewById(R.id.name_value);
            nameValue.setText(Html.fromHtml(pet.getFormatName()));
            TextView hpValue = (TextView) view.findViewById(R.id.hp_value);
            hpValue.setText(StringUtils.formatNumber(pet.getUHp()));
            TextView atkValue = (TextView) view.findViewById(R.id.atk_value);
            atkValue.setText(StringUtils.formatNumber(pet.getMaxAtk()));
            TextView defValue = (TextView) view.findViewById(R.id.def_value);
            defValue.setText(StringUtils.formatNumber(pet.getMaxDef()));
            TextView skillName = (TextView) view.findViewById(R.id.pet_skill_name);
            if (pet.getAllSkill() != null) {
                skillName.setText(pet.getAllSkill().getName());
            }
            TextView petOwner = (TextView) view.findViewById(R.id.pet_owner_value);
            petOwner.setText(pet.getOwner());
            TextView leveText = (TextView) view.findViewById(R.id.pet_level_label);
            leveText.setText("相遇在第" + pet.getLev() + "层");
            TextView fName = (TextView) view.findViewById(R.id.pet_f_name_value);
            fName.setText(StringUtils.isNotEmpty(pet.getfName()) ? pet.getfName() : "未知");
            TextView mName = (TextView) view.findViewById(R.id.pet_m_name_value);
            mName.setText(StringUtils.isNotEmpty(pet.getmName()) ? pet.getmName() : "未知");
            TextView deathCount = (TextView) view.findViewById(R.id.pet_death_value);
            deathCount.setText(pet.getDeathCount() + "");
        }
        return newPetDialog;
    }
}
