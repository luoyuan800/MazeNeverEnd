package cn.gavin.pet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.gavin.Hero;
import cn.gavin.R;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.activity.MazeContents;
import cn.gavin.utils.StringUtils;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 11/2/2015.
 */
public class PetAdapter extends BaseAdapter implements View.OnClickListener{

    static PetAdapter listener;

    @Override
    public void onClick(View v) {

    }

    public void refresh(){
        PetDB.save(adapterData.toArray(new Pet[adapterData.size()]));
        adapterData = PetDB.loadPet(null);
        notifyDataSetChanged();
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
        private final CheckBox onUsedCheck;
        private final TextView mName;
        private final TextView fName;
        private final TextView deathCount;
        View view;
        Pet pet;
        MainGameActivity context;

        public PetViewHolder(final MainGameActivity context) {
            this.context = context;
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.pet_detail, (ViewGroup) context.findViewById(R.id.pet_view_group));
            nameValue = (TextView) view.findViewById(R.id.name_value);
            hpValue = (TextView) view.findViewById(R.id.hp_value);
            atkValue = (TextView) view.findViewById(R.id.atk_value);
            defValue = (TextView) view.findViewById(R.id.def_value);
            skillName = (TextView) view.findViewById(R.id.pet_skill_name);
            releaseButton = (Button) view.findViewById(R.id.release_pet);
            petPic = (ImageView) view.findViewById(R.id.pet_detail_pic);
            petIni = (TextView) view.findViewById(R.id.pet_ini_value);
            addHpButton = (Button) view.findViewById(R.id.pet_add_hp);
            addAtkButton = (Button) view.findViewById(R.id.pet_add_atk);
            addDefButton = (Button) view.findViewById(R.id.pet_add_def);
            leveText = (TextView) view.findViewById(R.id.pet_level_label);
            onUsedCheck = (CheckBox) view.findViewById(R.id.pet_on_used_check);
            fName = (TextView) view.findViewById(R.id.pet_f_name_value);
            mName = (TextView) view.findViewById(R.id.pet_m_name_value);
            deathCount = (TextView) view.findViewById(R.id.pet_death_value);
        }

        private void refresh(){
            if (pet != null) {
                hpValue.setText(pet.getHp() + "/" + pet.getUHp());
                atkValue.setText(pet.getAtk() + "");
                defValue.setText(pet.getDef() + "");
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
                releaseButton.setEnabled(true);
            }else{
                releaseButton.setEnabled(false);
            }
        }

        public void updatePet(final Pet pet, final Set<String> onUsedPetsId) {
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
                final Hero hero = MazeContents.hero;
                if (hero.getPoint() < 1) {
                    addHpButton.setEnabled(false);
                    addAtkButton.setEnabled(false);
                    addDefButton.setEnabled(false);
                } else {
                    addHpButton.setEnabled(true);
                    addAtkButton.setEnabled(true);
                    addDefButton.setEnabled(true);
                }
                if(StringUtils.isNotEmpty(pet.getfName())){
                    fName.setText(pet.getfName());
                }else{
                    fName.setText("未知");
                }
                if(StringUtils.isNotEmpty(pet.getmName())){
                    mName.setText(pet.getmName());
                }else{
                    mName.setText("未知");
                }
                deathCount.setText(pet.getDeathCount() + "");
                onUsedCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked && !pet.isOnUsed()) {
                            int petSize = hero.getPetSize();
                            if (onUsedPetsId.size() >= petSize) {
                                AlertDialog dialog = new AlertDialog.Builder(context).create();
                                dialog.setTitle("队伍满");
                                TextView view = new TextView(context);
                                view.setText(Html.fromHtml("最多只能选择" + petSize+ "个宠物加入出战的队伍中！"));
                                onUsedCheck.setChecked(false);
                                dialog.setView(view);
                                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确认",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }

                                        });
                                dialog.show();
                            } else {
                                pet.setOnUsed(true);
                                onUsedPetsId.add(pet.getId());
                            }
                        }else if(!isChecked && pet.isOnUsed()){
                            pet.setOnUsed(false);
                            onUsedPetsId.remove(pet.getId());
                        }
                    }
                });
                releaseButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
                                        dialog.dismiss();
                                        onUsedCheck.setChecked(false);
                                        pet.releasePet(MazeContents.hero, context);
                                        listener.refresh();
                                        refresh();
                                    }

                                });
                        dialog.show();
                    }
                });
                addHpButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pet.addHp(MazeContents.hero);
                        refresh();
                    }
                });
                addAtkButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pet.addAtk(MazeContents.hero);
                        refresh();
                    }
                });
                addDefButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pet.addDef(MazeContents.hero);
                        refresh();
                    }
                });
                if(pet.isOnUsed()){
                    onUsedCheck.setChecked(true);
                }else{
                    onUsedCheck.setChecked(false);
                }

            }
        }
    }

    public PetAdapter(Hero hero) {
        adapterData = PetDB.loadPet(null);
        List<Pet> pets = hero.getPets();
        onUsedPetIds = new HashSet<String>(pets.size());
        for(Pet pet : pets){
            onUsedPetIds.add(pet.getId());
        }
        listener = this;
        this.hero = hero;
    }

    private  List<Pet> adapterData;
    private Set<String> onUsedPetIds;
    private Hero hero;
    public void setToHero(){
        List<Pet> pets = new ArrayList<Pet>(onUsedPetIds.size());
        for(String id : onUsedPetIds){
            Pet pet = new Pet();
            pet.setId(id);
            PetDB.load(pet);
            pets.add(pet);
        }
        hero.setPets(pets);
    }

    @Override
    public int getCount() {
        return adapterData != null ? adapterData.size() : 0;
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
        pet.setOnUsed(onUsedPetIds.contains(pet.getId()));
        holder.updatePet(pet, onUsedPetIds);
        return convertView;
    }
}
