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
import cn.gavin.activity.MainMenuActivity;
import cn.gavin.alipay.Alipay;
import cn.gavin.db.DBHelper;
import cn.gavin.monster.MonsterBook;
import cn.gavin.skill.SkillDialog;
import cn.gavin.skill.SkillFactory;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 9/16/2015.
 */
public class LoadHelper {
    private MainMenuActivity context;

    public LoadHelper(MainMenuActivity activity) {
        context = activity;
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
            heroN.setAttackValue(preferences.getLong("baseAttackValue", 10));
            heroN.setDefenseValue(preferences.getLong("baseDefense", 1));
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
            heroN.setSkillPoint(preferences.getLong("skillPoint",1));
            context.lastUpload = preferences.getLong("lastUploadLev", 0);
            maze.setLevel(preferences.getLong("currentMazeLev", 1));
            String ach = preferences.getString("achievement", "0");
            for (int i = 0; i < ach.length() && i < Achievement.values().length; i++) {
                int enable = Integer.parseInt(ach.charAt(i) + "");
                if (enable == 1) {
                    Achievement.values()[i].enable();
                }
            }
            context.payTime = preferences.getLong("swordLev", 0);
            heroN.setAwardCount(preferences.getLong("awardCount", 0));
        }
        context.hero = heroN;
        context.maze = maze;
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
                    context.lastUpload = Integer.parseInt(atts[24]);
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
                context.payTime = Integer.parseInt(atts[19]);
                Achievement.linger.enable(heroN);
                context.hero = (heroN);
                context.maze = (maze);
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
        Cursor cursor = DBHelper.getDbHelper().excuseSOL(sql);
        while (!cursor.isAfterLast()) {
            SkillFactory.getSkill(cursor.getString(cursor.getColumnIndex("name")), hero, dialog);
            cursor.moveToNext();
        }
    }

}
