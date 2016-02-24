package cn.gavin.save;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.EnumMap;

import cn.gavin.Achievement;
import cn.gavin.Hero;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.utils.MazeContents;
import cn.gavin.forge.effect.Effect;
import cn.gavin.maze.Maze;
import cn.gavin.pet.Pet;
import cn.gavin.pet.PetDB;
import cn.gavin.skill.SkillFactory;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 9/16/2015.
 */
public class SaveHelper {
    private MainGameActivity context;

    public SaveHelper(MainGameActivity activity) {
        context = activity;
    }

    public SaveHelper() {

    }

    public void savePreValue() {
        Hero hero = MazeContents.hero;
        SharedPreferences preferences = context.getSharedPreferences("preValueForHat2.0", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        boolean save = false;
        if (!hero.preValueForHat.isEmpty()) {
            for (EnumMap.Entry<Effect, Long> entry : hero.preValueForHat.entrySet()) {
                editor.putLong(entry.getKey().name(), entry.getValue());
            }
            save = true;
        }
        editor.putBoolean("exist", save);
        editor.apply();

        preferences = context.getSharedPreferences("preValueForNet2.0", Context.MODE_PRIVATE);
        editor = preferences.edit();
        save = false;
        if (!hero.preValueForNek.isEmpty()) {
            for (EnumMap.Entry<Effect, Long> entry : hero.preValueForNek.entrySet()) {
                editor.putLong(entry.getKey().name(), entry.getValue());
            }
            save = true;
        }
        editor.putBoolean("exist", save);
        editor.apply();

        preferences = context.getSharedPreferences("preValueForRing2.0", Context.MODE_PRIVATE);
        editor = preferences.edit();
        save = false;
        if (!hero.preValueForRing.isEmpty()) {
            for (EnumMap.Entry<Effect, Long> entry : hero.preValueForRing.entrySet()) {
                editor.putLong(entry.getKey().name(), entry.getValue());
            }
            save = true;
        }
        editor.putBoolean("exist", save);
        editor.apply();
    }

    public void saveHero() {
        Hero heroN = context.getHero();
        Maze maze = context.getMaze();
        SharedPreferences preferences = context.getSharedPreferences("hero2.0", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("name", heroN.getName());
        editor.putLong("hp", heroN.getRealHP());
        editor.putLong("upperHp", heroN.getRealUHP());
        editor.putLong("baseAttackValue", heroN.getBaseAttackValue());
        editor.putLong("baseDefense", heroN.getBaseDefense());
        editor.putLong("click", heroN.getClick());
        editor.putLong("point", heroN.getPoint());
        editor.putLong("material", heroN.getMaterial());
        editor.putLong("swordLev", heroN.getSwordLev());
        editor.putLong("armorLev", heroN.getArmorLev());
        editor.putString("swordName", heroN.getSword());
        editor.putString("armorName", heroN.getArmor());
        editor.putLong("maxMazeLev", heroN.getMaxMazeLev());
        editor.putLong("strength", heroN.getStrength());
        editor.putLong("power", heroN.getPower());
        editor.putLong("agility", heroN.getAgility());
        editor.putLong("clickAward", heroN.getClickAward());
        editor.putBoolean("mv", heroN.ismV());
        if (heroN.getRing() != null) {
            editor.putString("ring", heroN.getRing().getId());
        } else {
            editor.putString("ring", "");
        }
        if (heroN.getNecklace() != null) {
            editor.putString("necklace", heroN.getNecklace().getId());
        } else {
            editor.putString("necklace", "");
        }
        if (heroN.getHat() != null) {
            editor.putString("hat", heroN.getHat().getId());
        } else {
            editor.putString("hat", "");
        }
        StringBuilder sb = new StringBuilder();
        for (Achievement achievement : Achievement.values()) {
            if (achievement.isEnable()) {
                sb.append(1);
            } else {
                sb.append(0);
            }
        }
        editor.putString("achievement", sb.toString());
        editor.putLong("currentMazeLev", maze.getLev());
        long payTime = context.getAlipay().getPayTime();
        if(payTime < MazeContents.payTime){
            payTime = MazeContents.payTime;
        }
        editor.putLong("payTime", payTime);
        editor.putLong("death", heroN.getDeathCount());
        editor.putLong("lastUploadLev", context.getLastUploadLev());
        editor.putLong("skillPoint", heroN.getSkillPoint());
        editor.putLong("awardCount", heroN.getAwardCount());
        editor.putLong("lockBox", heroN.getLockBox());
        editor.putLong("keyCount", heroN.getKeyCount());
        editor.putLong("MAX_HP_RISE", heroN.MAX_HP_RISE);
        editor.putLong("ATR_RISE", heroN.ATR_RISE);
        editor.putLong("DEF_RISE", heroN.DEF_RISE);
        editor.putLong("reincaCount", heroN.getReincaCount());
        editor.putLong("hitRate", heroN.getHitRate());
        editor.putFloat("parry", heroN.getParry());
        editor.putFloat("dodgeRate", heroN.getDodgeRate());
        editor.putLong("clickPointAward", heroN.getClickPointAward());
        editor.putString("element", heroN.getTrueElement().name());
        editor.putInt("pet_size", heroN.getPetSize());
        editor.putFloat("pet_rate", heroN.getPetRate());
        editor.putFloat("egg_rate", heroN.getEggRate());
        editor.putLong("egg_step", heroN.getEggStep());
        StringBuilder petIds = new StringBuilder();
        for (Pet pet : heroN.getPets()) {
            petIds.append(pet.getId()).append("_");
        }
        editor.putString("pet_id", petIds.toString());
        String titleColor = heroN.getTitleColor();
        if(titleColor.startsWith("#ff") || titleColor.startsWith("#FF")){
            titleColor = titleColor.replaceFirst("#(ff|FF)","#8a");
        }
        editor.putString("title_color", titleColor);
        String leftUpColor = heroN.getLeftUpColor();
        if(leftUpColor.startsWith("#ff") || leftUpColor.startsWith("#FF")){
            leftUpColor = "#8bFFFFff";
        }
        editor.putString("left_up_color", leftUpColor);
        String leftDownColor = heroN.getLeftDownColor();
        if(leftDownColor.startsWith("#ff") || leftDownColor.startsWith("#FF")){
            leftDownColor = leftDownColor.replaceFirst("#(ff|FF)","#6b");
        }
        editor.putString("left_down_color", leftDownColor);
        String rightDownColor = heroN.getRightDownColor();
        if(rightDownColor.startsWith("#ff") || rightDownColor.startsWith("#FF")){
            rightDownColor = "#8bFFFFff";
        }
        editor.putString("right_down_color", rightDownColor);
        editor.putLong("reset_skill", heroN.getResetSkillCount());
        editor.putInt("csm", maze.getCsmgl());
        editor.putString("uuid", heroN.getUuid());
        editor.putFloat("pet_abe", heroN.getPetAbe());
        editor.putString("filter_pet_name",maze.getCatchPetNameContains());
        editor.putString("reject_element", heroN.getRejectElement().name());
        if(heroN.getGift()!=null){
            editor.putString("gift", heroN.getGift().name());
        }
        editor.apply();
    }

    public void saveSkill() {
        SkillFactory.save();
    }

    public void savePet() {
//        PetDB.save(context.getHero().getPets().toArray(new Pet[context.getHero().getPets().size()]));
        PetDB.save();
    }

    public void save() {
        saveHero();
        savePreValue();
        saveSkill();
        savePet();
    }

    public void backUp() {
        context.fileList();
    }
}
