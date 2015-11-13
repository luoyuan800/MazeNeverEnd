package cn.gavin.pet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.gavin.Hero;
import cn.gavin.R;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.utils.MazeContents;
import cn.gavin.utils.StringUtils;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 11/2/2015.
 */
public class PetSimpleAdapter extends BaseAdapter {

    static PetSimpleAdapter listener;
    final PetDetailDialog detailDialog;

    public void refresh() {
        saveToDB();
        adapterData = PetDB.loadPet(null);
        notifyDataSetChanged();
    }

    public void saveToDB() {
        PetDB.save(adapterData.toArray(new Pet[adapterData.size()]));
    }

public void close(){
        detailDialog.dismiss();
}

    public PetSimpleAdapter(Hero hero) {
        adapterData = PetDB.loadPet(null);
        List<Pet> pets = hero.getPets();
        onUsedPetIds = new HashSet<String>(pets.size());
        for (Pet pet : pets) {
            onUsedPetIds.add(pet.getId());
        }
        listener = this;
        this.hero = hero;
        detailDialog = new PetDetailDialog(MainGameActivity.context);
    }

    private List<Pet> adapterData;
    private final Set<String> onUsedPetIds;
    private Hero hero;

    public void setToHero() {
        List<Pet> pets = new ArrayList<Pet>(onUsedPetIds.size());
        for (String id : onUsedPetIds) {
            Pet pet = PetDB.load(id);
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
            holder = new PetViewHolder();
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

    static class PetViewHolder {
        private TextView view = new TextView(MainGameActivity.context);
        private Pet pet;

        public void updatePet(final Pet pet, final Set<String> onUsedPetIds) {
            this.pet = pet;
            if (pet != null) {
                if (view == null) {
                    view = new TextView(MainGameActivity.context);
                }
                view.setTextSize(20);
                view.setText(Html.fromHtml(pet.getFormatName() + (pet.isOnUsed() ? "  √ " : "")));
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ("蛋".equals(pet.getType())) {
                            new EggDetail(pet, MainGameActivity.context, onUsedPetIds);
                        } else {
                            listener.detailDialog.show();
                            listener.detailDialog.updatePet(pet, onUsedPetIds);
                        }
                    }
                });
            }
        }
    }

    static class EggDetail {
        Pet pet;
        MainGameActivity context;
        final AlertDialog detail;

        public EggDetail(final Pet pet, final MainGameActivity context, final Set<String> onUsedPetsId) {
            this.context = context;
            this.pet = pet;
            final Hero hero = MazeContents.hero;
            final CheckBox onUsedCheck = new CheckBox(context);
            onUsedCheck.setText("带身上");
            Button releaseButton = new Button(context);
            releaseButton.setText("丢弃");
            detail = new AlertDialog.Builder(context).create();
            detail.setButton(DialogInterface.BUTTON_NEGATIVE, "退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.addView(onUsedCheck);
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(R.drawable.egg);
            layout.addView(imageView);
            linearLayout.addView(layout);
            detail.setView(linearLayout);
            TextView name = new TextView(context);
            name.setText(pet.getType());
            linearLayout.addView(name);
            TextView pa = new TextView(context);
            pa.setText("父亲： " + pet.getfName() + "\n母亲：" + pet.getmName());
            linearLayout.addView(pa);
            TextView bi = new TextView(context);
            if (pet.getDeathCount() < 10) {
                bi.setText("好像要破壳而出了！");
            } else if (pet.getDeathCount() < 80) {
                bi.setText("似乎有点动静...");
            } else {
                bi.setText("不知道里面是什么...");
            }
            linearLayout.addView(bi);
            linearLayout.addView(releaseButton);
            onUsedCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked && !pet.isOnUsed()) {
                        int petSize = hero.getPetSize();
                        if (onUsedPetsId.size() >= petSize) {
                            AlertDialog dialog = new AlertDialog.Builder(context).create();
                            dialog.setTitle("队伍满");
                            TextView view = new TextView(context);
                            view.setText(Html.fromHtml("最多只能选择" + petSize + "个宠物加入出战的队伍中！"));
                            buttonView.setChecked(false);
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
                            listener.setToHero();
                            onUsedPetsId.add(pet.getId());
                            listener.refresh();
                        }
                    } else if (!isChecked && pet.isOnUsed()) {
                        pet.setOnUsed(false);
                        listener.setToHero();
                        onUsedPetsId.remove(pet.getId());
                        listener.refresh();
                    }
                }
            });
            releaseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog dialog = new AlertDialog.Builder(context).create();
                    dialog.setTitle("确认丢弃宠物蛋？？");
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
                                    detail.dismiss();
                                    onUsedCheck.setChecked(false);
                                    listener.adapterData.remove(pet);
                                    pet.releasePet(MazeContents.hero, context);
                                    listener.setToHero();
                                    listener.refresh();
                                }

                            });
                    dialog.show();
                }
            });

            if (pet.isOnUsed()) {
                onUsedCheck.setChecked(true);
            } else {
                onUsedCheck.setChecked(false);
            }
            detail.show();
        }
    }

    static class PetDetailDialog {
        private final TextView nameValue;
        private final TextView hpValue;
        private final TextView leveText;
        private final Button addDefButton;
        private final Button addAtkButton;
        private final Button addHpButton;
        private final TextView petOwner;
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
        final AlertDialog detail;

        public void show() {
            detail.show();
        }

        public void dismiss() {
            detail.dismiss();
        }

        public PetDetailDialog(final MainGameActivity context) {
            this.context = context;
            detail = new AlertDialog.Builder(context).create();
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.pet_detail, (ViewGroup) context.findViewById(R.id.pet_view_group));
            detail.setView(view);
            detail.setButton(DialogInterface.BUTTON_NEGATIVE, "退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    detail.hide();
                }
            });
            nameValue = (TextView) view.findViewById(R.id.name_value);
            hpValue = (TextView) view.findViewById(R.id.hp_value);
            atkValue = (TextView) view.findViewById(R.id.atk_value);
            defValue = (TextView) view.findViewById(R.id.def_value);
            skillName = (TextView) view.findViewById(R.id.pet_skill_name);
            releaseButton = (Button) view.findViewById(R.id.release_pet);
            petPic = (ImageView) view.findViewById(R.id.pet_detail_pic);
            petOwner = (TextView) view.findViewById(R.id.pet_owner_value);
            addHpButton = (Button) view.findViewById(R.id.pet_add_hp);
            addAtkButton = (Button) view.findViewById(R.id.pet_add_atk);
            addDefButton = (Button) view.findViewById(R.id.pet_add_def);
            leveText = (TextView) view.findViewById(R.id.pet_level_label);
            onUsedCheck = (CheckBox) view.findViewById(R.id.pet_on_used_check);
            fName = (TextView) view.findViewById(R.id.pet_f_name_value);
            mName = (TextView) view.findViewById(R.id.pet_m_name_value);
            deathCount = (TextView) view.findViewById(R.id.pet_death_value);
        }

        private void refresh() {
            if (pet != null) {
                String source = getIntimacyString();
                leveText.setText(Html.fromHtml(source));
                hpValue.setText(pet.getHp() + "/" + pet.getUHp());
                atkValue.setText(pet.getMaxAtk() + "");
                defValue.setText(pet.getMaxDef() + "");
                petOwner.setText(pet.getOwner());
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
            } else {
                releaseButton.setEnabled(false);
            }
        }

        private String getIntimacyString() {
            String source = "相遇在第<b>" + pet.getLev() + "</b>层。<font color=\"#6A5ACD\">";
            long intimacy = pet.getIntimacy();
            if(intimacy < 500){
                source += "它似乎很讨厌你。";
            }else if(intimacy < 1000){
                source += "它好像不愿搭理你。";
            }else if(intimacy < 2000){
                source += "它在偷偷看你。";
            }else if(intimacy < 3000){
                source += "它有一点傲娇。";
            }else if(intimacy < 5000){
                source += "它开始喜欢你了。";
            }else if(intimacy < 8000){
                source += "它在亲近你。";
            }else if(intimacy < 10000){
                source += "它不愿意离开你。";
            }else if(intimacy > 20000){
                source += "它黏在你身上甩不掉。";
            }
            source += "</font>";
            return source;
        }

        public void updatePet(final Pet pet, final Set<String> onUsedPetsId) {
            this.pet = pet;
            if (pet != null) {
                nameValue.setText(Html.fromHtml(pet.getFormatName()));
                leveText.setText(Html.fromHtml(getIntimacyString()));
                hpValue.setText(pet.getHp() + "/" + pet.getUHp());
                atkValue.setText(pet.getMaxAtk() + "");
                defValue.setText(pet.getMaxDef() + "");
                if (pet.getSkill() != null) {
                    skillName.setText(pet.getSkill().getName());
                } else {
                    skillName.setText("无");
                }
                petPic.setImageResource(MazeContents.getImageByName(pet.getName()));
                petOwner.setText(pet.getOwner());
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
                if (StringUtils.isNotEmpty(pet.getfName())) {
                    fName.setText(pet.getfName());
                } else {
                    fName.setText("未知");
                }
                if (StringUtils.isNotEmpty(pet.getmName())) {
                    mName.setText(pet.getmName());
                } else {
                    mName.setText("未知");
                }
                deathCount.setText(pet.getDeathCount() + "");
                onUsedCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked && !pet.isOnUsed()) {
                            int petSize = hero.getPetSize();
                            if (onUsedPetsId.size() >= petSize) {
                                AlertDialog dialog = new AlertDialog.Builder(context).create();
                                dialog.setTitle("队伍满");
                                TextView view = new TextView(context);
                                view.setText(Html.fromHtml("最多只能选择" + petSize + "个宠物加入出战的队伍中！"));
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
                                listener.refresh();
                            }
                        } else if (!isChecked && pet.isOnUsed()) {
                            pet.setOnUsed(false);
                            onUsedPetsId.remove(pet.getId());
                            listener.refresh();
                        }
                    }
                });
                releaseButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog dialog = new AlertDialog.Builder(context).create();
                        dialog.setTitle("确认释放宠物？？");
                        TextView view = new TextView(context);
                        view.setText(Html.fromHtml("你确认释放宠物：" + pet.getFormatName() + "吗？<br>释放后该宠物即为消失，已经分配的点数不会返还！"));
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
                                        detail.dismiss();
                                        onUsedPetsId.remove(pet.getId());
                                        listener.adapterData.remove(pet);
                                        if(pet.isOnUsed()){
                                            pet.setOnUsed(false);
                                            onUsedCheck.setChecked(false);
                                            listener.setToHero();
                                        }
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
                if (pet.isOnUsed()) {
                    onUsedCheck.setChecked(true);
                } else {
                    onUsedCheck.setChecked(false);
                }

            }
        }
    }
}
