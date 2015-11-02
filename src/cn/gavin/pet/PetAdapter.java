package cn.gavin.pet;

import android.content.DialogInterface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.gavin.Hero;
import cn.gavin.R;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.activity.MazeContents;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 11/2/2015.
 */
public class PetAdapter extends BaseAdapter implements View.OnClickListener {
    static PetAdapter listener;
    @Override
    public void onClick(View v) {

    }

    static class PetViewHolder {
        private final TextView nameValue;
        private final TextView hpValue;
        private final TextView leveText;
        private final Button addDefButton;
        private final Button addAtkButton;
        private final Button addHpButton;
        private final TextView petIni;
        private final ImageView petPic;
        private final Button releaseButton;
        private final TextView skillName;
        private final TextView defValue;
        private final TextView atkValue;
        View view;
        Pet pet;
        public PetViewHolder(MainGameActivity context) {
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.pet_detail, (ViewGroup) context.findViewById(R.id.pet_view_group));
            nameValue = (TextView) view.findViewById(R.id.name_value);
            hpValue = (TextView) view.findViewById(R.id.hp_value);
            atkValue = (TextView) view.findViewById(R.id.atk_value);
            defValue = (TextView) view.findViewById(R.id.def_value);
            skillName = (TextView) view.findViewById(R.id.pet_skill_name);
            releaseButton = (Button) view.findViewById(R.id.release_pet);
            releaseButton.setOnClickListener(listener);
            petPic = (ImageView) view.findViewById(R.id.pet_detail_pic);
            petIni = (TextView) view.findViewById(R.id.pet_ini_value);
            addHpButton = (Button) view.findViewById(R.id.pet_add_hp);
            addHpButton.setOnClickListener(listener);
            addAtkButton = (Button) view.findViewById(R.id.pet_add_atk);
            addAtkButton.setOnClickListener(listener);
            addDefButton = (Button) view.findViewById(R.id.pet_add_def);
            addDefButton.setOnClickListener(listener);
            leveText = (TextView) view.findViewById(R.id.pet_level_label);
        }
        public void updatePet(Pet pet){
            this.pet = pet;
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

    public PetAdapter(Hero hero){
        adapterData = hero.getPets();
        listener = this;
    }

    private final List<Pet> adapterData;

    @Override
    public int getCount() {
        return adapterData!=null ? adapterData.size() : 0;
    }

    @Override
    public Pet getItem(int position) {
        if (position >= getCount()) position = 0;
        return adapterData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        PetViewHolder holder;
        if (convertView == null) {
            holder = new PetViewHolder(MainGameActivity.context);
            convertView = holder.view;
            convertView.setTag(holder);
        } else {
            holder = (PetViewHolder) convertView.getTag();
        }
        Pet pet = getItem(position);
            holder.updatePet(pet);
        return convertView;
    }
}
