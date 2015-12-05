package cn.gavin.save;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.Map;
import java.util.UUID;

import cn.gavin.Achievement;
import cn.gavin.Armor;
import cn.gavin.Element;
import cn.gavin.Hero;
import cn.gavin.Sword;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.activity.MainMenuActivity;
import cn.gavin.utils.MazeContents;
import cn.gavin.db.DBHelper;
import cn.gavin.forge.Accessory;
import cn.gavin.forge.effect.Effect;
import cn.gavin.log.LogHelper;
import cn.gavin.maze.Maze;
import cn.gavin.pet.Pet;
import cn.gavin.pet.PetDB;
import cn.gavin.skill.SkillDialog;
import cn.gavin.skill.SkillFactory;
import cn.gavin.utils.StringUtils;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 9/16/2015.
 */
public class LoadHelper {
    private Activity context;

    public LoadHelper(MainMenuActivity activity) {
        context = activity;
    }

    public LoadHelper(MainGameActivity activity) {
        context = activity;
    }

    public void loadValue(Hero hero) {
        SharedPreferences preferences = context.getSharedPreferences("preValueForHat", Context.MODE_PRIVATE);
        boolean save = preferences.getBoolean("exist", false);
        if (save) {
            for (Map.Entry<String, ?> entry : preferences.getAll().entrySet()) {
                if (!entry.getKey().equalsIgnoreCase("exist")) {
                    hero.preValueForHat.put(Effect.valueOf(entry.getKey()), preferences.getLong(entry.getKey(), 0l));
                }
            }
        }

        preferences = context.getSharedPreferences("preValueForNet", Context.MODE_PRIVATE);
        save = preferences.getBoolean("exist", false);
        if (save) {
            for (Map.Entry<String, ?> entry : preferences.getAll().entrySet()) {
                if (!entry.getKey().equalsIgnoreCase("exist")) {
                    hero.preValueForNek.put(Effect.valueOf(entry.getKey()), preferences.getLong(entry.getKey(), 0l));
                }
            }
        }

        preferences = context.getSharedPreferences("preValueForRing", Context.MODE_PRIVATE);
        save = preferences.getBoolean("exist", false);
        if (save) {
            for (Map.Entry<String, ?> entry : preferences.getAll().entrySet()) {
                if (!entry.getKey().equalsIgnoreCase("exist")) {
                    hero.preValueForRing.put(Effect.valueOf(entry.getKey()), preferences.getLong(entry.getKey(), 0l));
                }
            }
        }
    }

    public void loadHero() {
        Hero heroN = new Hero("勇者");
        Maze maze = new Maze(heroN);
        SharedPreferences preferences = context.getSharedPreferences("hero", Context.MODE_PRIVATE);
        heroN.setName(preferences.getString("name", "勇者"));
        heroN.setHp(preferences.getLong("hp", 20));
        heroN.setUpperHp(preferences.getLong("upperHp", 20));
        heroN.setAttackValue(preferences.getLong("baseAttackValue", 10));
        heroN.setDefenseValue(preferences.getLong("baseDefense", 1));
        heroN.setClick(preferences.getLong("click", 0));
        heroN.setPoint(preferences.getLong("point", 0));
        if(!preferences.contains("n_material")){
            //第一次升级到1.8
            SharedPreferences.Editor editor = preferences.edit();
            editor.putLong("n_material", preferences.getLong("material", 0));
            editor.putLong("o_material", preferences.getLong("material",0));
            heroN.setMaterial(preferences.getLong("material", 0));
            editor.apply();
        }else {
            heroN.setMaterial(preferences.getLong("n_material", 0));
        }

        heroN.setSwordLev(preferences.getLong("swordLev", 0));
        heroN.setArmorLev(preferences.getLong("armorLev", 0));
        heroN.setMaxMazeLev(preferences.getLong("maxMazeLev", 1));
        heroN.setStrength(preferences.getLong("strength", heroN.getRandom().nextLong(5)));
        heroN.setPower(preferences.getLong("power", heroN.getRandom().nextLong(5)));
        heroN.setAgility(preferences.getLong("agility", heroN.getRandom().nextLong(5)));
        heroN.setClickAward(preferences.getLong("clickAward", 1));
        heroN.setSword(Sword.valueOf(preferences.getString("swordName", Sword.木剑.name())));
        heroN.setArmor(Armor.valueOf(preferences.getString("armorName", Armor.破布.name())));
        heroN.setDeathCount(preferences.getLong("death", 0));
        heroN.setSkillPoint(preferences.getLong("skillPoint", 1));
        MazeContents.lastUpload = preferences.getLong("lastUploadLev", 1);
        maze.setLevel(preferences.getLong("currentMazeLev", 1));
        String ach = preferences.getString("achievement", "0");
        for (int i = 0; i < ach.length() && i < Achievement.values().length; i++) {
            int enable = StringUtils.toInt(ach.charAt(i) + "");
            if (enable == 1) {
                Achievement.values()[i].enable();
            }
        }
        MazeContents.payTime = preferences.getLong("payTime", 0);
        heroN.setAwardCount(preferences.getLong("awardCount", 0));
        heroN.setLockBox(preferences.getLong("lockBox", 1));
        heroN.setKeyCount(preferences.getLong("keyCount", 1));
        String ringId = preferences.getString("ring", null);
        if (StringUtils.isNotEmpty(ringId)) {
            Accessory ring = new Accessory();
            ring.setId(ringId);
            if (ring.load())
                heroN.setAccessory(ring);
        }
        String necklaceId = preferences.getString("necklace", null);
        if (StringUtils.isNotEmpty(necklaceId)) {
            Accessory necklace = new Accessory();
            necklace.setId(necklaceId);
            if (necklace.load())
                heroN.setAccessory(necklace);
        }
        String hatId = preferences.getString("hat", null);
        if (StringUtils.isNotEmpty(hatId)) {
            Accessory hat = new Accessory();
            hat.setId(hatId);
            if (hat.load())
                heroN.setAccessory(hat);
        }
        Achievement.linger.enable(heroN);
        heroN.MAX_HP_RISE = preferences.getLong("MAX_HP_RISE", 5);
        heroN.ATR_RISE = preferences.getLong("ATR_RISE", 2);
        heroN.DEF_RISE = preferences.getLong("DEF_RISE", 1);
        heroN.setReincaCount(preferences.getLong("reincaCount", 0));
        heroN.setHitRate(preferences.getLong("hitRate", 0));
        heroN.setParry(preferences.getFloat("parry", 0));
        heroN.setDodgeRate(preferences.getFloat("dodgeRate", 0));
        heroN.setClickPointAward(preferences.getLong("clickPointAward", 0));
        heroN.setElement(Element.valueOf(preferences.getString("element", "无")));
        heroN.setPetSize(preferences.getInt("pet_size", 3));
        if(heroN.getPetSize() > 20) heroN.setPetSize(20);
        heroN.setPetRate(preferences.getFloat("pet_rate", 1.0f));
        heroN.setEggRate(preferences.getFloat("egg_rate", 200f));
        heroN.setEggStep(preferences.getLong("egg_step", 1));
        heroN.setResetSkillCount(preferences.getLong("reset_skill", 0));
        String petIds = preferences.getString("pet_id", "");
        String[] ids = StringUtils.split(petIds, "_");
        for (String id : ids) {
            if (StringUtils.isNotEmpty(id)) {
                Pet pet = PetDB.load(id);
                if (StringUtils.isNotEmpty(pet.getType())) {
                    heroN.getPets().add(pet);
                }
            }
        }
        heroN.setTitleColor(preferences.getString("title_color", "#ff00acff"));
        heroN.setUuid(preferences.getString("uuid", UUID.randomUUID().toString()));
        heroN.setOnSkill(false);
        heroN.setPetAbe(preferences.getFloat("pet_abe", 0));
        maze.setCsmgl(preferences.getInt("csm", 9987));
        loadValue(heroN);
        MazeContents.hero = heroN;
        MazeContents.maze = maze;

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
                    MazeContents.lastUpload = Integer.parseInt(atts[24]);
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
                MazeContents.payTime = Integer.parseInt(atts[19]);
                Achievement.linger.enable(heroN);
                MazeContents.hero = (heroN);
                MazeContents.maze = (maze);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(MainGameActivity.TAG, e.getMessage(), e);
            LogHelper.writeLog();
            return false;
        }
    }

    public void loadSkill(Hero hero, SkillDialog dialog) {
        String sql = "select name from skill";
        try {
            Cursor cursor = DBHelper.getDbHelper().excuseSOL(sql);
            while (!cursor.isAfterLast()) {
                SkillFactory.getSkill(cursor.getString(cursor.getColumnIndex("name")), hero);
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(MainGameActivity.TAG, "loadSkills", e);
        }
    }

}
