package cn.gavin.save;

import android.content.Context;
import android.content.SharedPreferences;
import cn.gavin.Achievement;
import cn.gavin.Hero;
import cn.gavin.maze.Maze;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.pet.Pet;
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

    public void saveHero() {
        Hero heroN = context.getHero();
        Maze maze = context.getMaze();
        SharedPreferences preferences = context.getSharedPreferences("hero", Context.MODE_PRIVATE);
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
        if (heroN.getRing() != null) {
            editor.putString("ring", heroN.getRing().getId());
        }else{
            editor.putString("ring", "");
        }
        if (heroN.getNecklace() != null) {
            editor.putString("necklace", heroN.getNecklace().getId());
        }else{
            editor.putString("necklace","");
        }
        if (heroN.getHat() != null) {
            editor.putString("hat", heroN.getHat().getId());
        }else{
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
        editor.putLong("payTime", context.getAlipay().getPayTime());
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
        editor.putString("element", heroN.getElement().name());
        editor.putInt("pet_size", heroN.getPetSize());
        editor.putFloat("pet_rate", heroN.getPetRate());
        editor.putFloat("egg_rate", heroN.getEggRate());
        editor.putLong("egg_step", heroN.getEggStep());
        StringBuilder petIds = new StringBuilder();
        for(Pet pet : heroN.getPets()){
            petIds.append(pet.getId()).append("_");
        }
        editor.putString("pet_id", petIds.toString());
        editor.putString("title_color", heroN.getTitleColor());
        editor.apply();

    }

    public void saveSkill() {
        SkillFactory.save();
    }


    public void save() {
        saveHero();
        saveSkill();
    }
}
