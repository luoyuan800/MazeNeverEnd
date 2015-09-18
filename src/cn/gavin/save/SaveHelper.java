package cn.gavin.save;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

import cn.gavin.Achievement;
import cn.gavin.Armor;
import cn.gavin.Hero;
import cn.gavin.Maze;
import cn.gavin.Sword;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.alipay.Alipay;
import cn.gavin.skill.SkillDialog;
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

    public void saveHero() {
        Hero heroN = context.getHero();
        Maze maze = context.getMaze();
        SharedPreferences preferences = context.getSharedPreferences("hero", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("name", heroN.getName());
        editor.putLong("hp", heroN.getHp());
        editor.putLong("upperHp", heroN.getUpperHp());
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
        editor.apply();
    }

    public void loadHero() {
        Hero heroN = new Hero("勇者");
        Maze maze = new Maze(heroN);
        if (loadOlderSaveFile(heroN, maze)) {
            context.deleteFile("yzcmg.ave");
            Achievement.linger.enable(heroN);
        } else {
            SharedPreferences preferences = context.getSharedPreferences("hero", Context.MODE_PRIVATE);
            heroN.setName(preferences.getString("name", "勇者"));
            heroN.setHp(preferences.getLong("hp", 20));
            heroN.setUpperHp(preferences.getLong("upperHp", 10));
            heroN.setAttackValue(preferences.getLong("attackValue", 10));
            heroN.setDefenseValue(preferences.getLong("defenseValue", 1));
            heroN.setClick(preferences.getLong("click", 0));
            heroN.setPoint(preferences.getLong("point", 0));
            heroN.setMaterial(preferences.getLong("material", 0));
            heroN.setSwordLev(preferences.getLong("swordLev", 0));
            heroN.setArmorLev(preferences.getLong("armorLev", 0));
            heroN.setMaxMazeLev(preferences.getLong("maxMazeLev", 1));
            heroN.setStrength(preferences.getLong("strength", heroN.getRandom().nextLong(5)));
            heroN.setPower(preferences.getLong("power", heroN.getRandom().nextLong(5)));
            heroN.setAgility(preferences.getLong("agility", heroN.getRandom().nextLong(5)));
            heroN.setClickAward(preferences.getLong("clickAward", 0));
            heroN.setSword(Sword.valueOf(preferences.getString("swordName", Sword.木剑.name())));
            heroN.setArmor(Armor.valueOf(preferences.getString("armorName", Armor.破布.name())));
            heroN.setDeathCount(preferences.getLong("death", 0));
            context.setLastUploadLev(preferences.getLong("lastUploadLev", 0));
            maze.setLevel(preferences.getLong("currentMazeLev", 1));
            String ach = preferences.getString("achievement", "0");
            for (int i = 0; i < ach.length() && i < Achievement.values().length; i++) {
                int enable = Integer.parseInt(ach.charAt(i) + "");
                if (enable == 1) {
                    Achievement.values()[i].enable();
                }
            }
            context.setAlipay(new Alipay(context, preferences.getLong("swordLev", 0)));
        }
        context.setHeroN(heroN);
        context.setMaze(maze);
    }

    private boolean loadOlderSaveFile(Hero heroN, Maze maze) {
        try {
            FileInputStream fis = context.openFileInput("yzcmg.ave");
            byte[] b = new byte[1];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while (fis.read(b) != -1) {
                baos.write(b, 0, b.length);
            }
            baos.close();
            fis.close();
            String save = baos.toString("UTF-8");
            String[] atts = save.split("_");
            if (atts.length >= 20) {
                heroN.setName(atts[0]);
                heroN.setHp(Integer.parseInt(atts[1]));
                heroN.setUpperHp(Integer.parseInt(atts[2]));
                heroN.setAttackValue(Integer.parseInt(atts[3]));
                heroN.setDefenseValue(Integer.parseInt(atts[4]));
                heroN.setClick(Integer.parseInt(atts[5]));
                heroN.setPoint(Integer.parseInt(atts[6]));
                heroN.setMaterial(Integer.parseInt(atts[7]));
                heroN.setSwordLev(Integer.parseInt(atts[8]));
                heroN.setArmorLev(Integer.parseInt(atts[9]));
                heroN.setMaxMazeLev(Integer.parseInt(atts[12]));
                heroN.setStrength(Integer.parseInt(atts[13]));
                heroN.setPower(Integer.parseInt(atts[14]));
                heroN.setAgility(Integer.parseInt(atts[15]));
                heroN.setClickAward(Integer.parseInt(atts[16]));
                heroN.setSword(Sword.valueOf(atts[10]));
                heroN.setArmor(Armor.valueOf(atts[11]));
                if (atts.length >= 24) {
                    heroN.setDeathCount(Integer.parseInt(atts[20]));
//                    heroN.getExistSkill().get(0).setCount(Integer.parseInt(atts[21]));
//                    heroN.getExistSkill().get(1).setCount(Integer.parseInt(atts[22]));
//                    heroN.getExistSkill().get(2).setCount(Integer.parseInt(atts[23]));
                }
                if (atts.length >= 25) {
                    context.setLastUploadLev(Integer.parseInt(atts[24]));
                }
                maze.setLevel(Integer.parseInt(atts[18]));
                if (maze.getLev() > heroN.getMaxMazeLev()) {
                    maze.setLevel(heroN.getMaxMazeLev());
                }
                for (int i = 0; i < atts[17].length() && i < Achievement.values().length; i++) {
                    int enable = Integer.parseInt(atts[17].charAt(i) + "");
                    if (enable == 1) {
                        Achievement.values()[i].enable();
                    }
                }
                context.setAlipay(new Alipay(context, Integer.parseInt(atts[19])));
                Achievement.linger.enable(heroN);
                context.setHeroN(heroN);
                context.setMaze(maze);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void loadSkill(Hero hero, SkillDialog dialog) {
        String sql = "select name from skill";
        Cursor cursor = context.getDbHelper().excuseSOL(sql);
        while (!cursor.isAfterLast()) {
            SkillFactory.getSkill(cursor.getString(cursor.getColumnIndex("name")), hero, dialog);
            cursor.moveToNext();
        }
    }

    public void saveSkill() {
        SkillFactory.save();
    }

    public void save() {
        saveHero();
        saveSkill();
    }
}
