package cn.gavin.utils;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import cn.gavin.Achievement;
import cn.gavin.Hero;
import cn.gavin.db.DBHelper;
import cn.gavin.forge.effect.Effect;
import cn.gavin.log.LogHelper;
import cn.gavin.maze.Maze;
import cn.gavin.pet.Pet;
import cn.gavin.skill.SkillDialog;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 10/5/2015.
 */
public class MazeContents {
    public static long payTime;
    public static Hero hero;
    public static Maze maze;
    public static long lastUpload;
    public static String SD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/maze";

    public static Bitmap loadImageFromSD(String name) {
        return BitmapFactory.decodeFile(SD_PATH + "/image/" + name);
    }

    public static Maze getMaze() {
        return maze;
    }

    public static final String PALACE_TABLE_NAME = "uploader";


    public static boolean checkCheat(Hero hero) {
        long point = 4500 * hero.getMaxMazeLev() * (hero.getReincaCount() + 1);
        boolean normal = hero.getStrength() < point && hero.getAgility() < point && hero.getPower() < point;
        boolean normal2 = hero.getBaseAttackValue() < point * hero.ATR_RISE &&
                hero.getBaseDefense() < point * hero.DEF_RISE && hero.getRealUHP() < point * 10 * hero.MAX_HP_RISE;
        return normal && normal2 && ((hero.getUpperHp() + hero.getUpperDef() + hero.getUpperAtk()) < (hero.getMaxMazeLev() * 4900000 * (hero.getReincaCount() + 1) * (hero.getPay() + 2))) && !(hero.getMaxMazeLev() > 50000 && !Achievement.richer.isEnable());
    }

    public static Long reduceLegacyEffect(Effect effect, Long value) {
        try {
            switch (effect) {
                case ADD_DEF:
                    if (hero != null && value > hero.getUpperDef() * 5) {
                        if (hero.getUpperDef() > 0) {
                            return hero.getUpperDef() * 5;
                        } else {
                            return value / 2;
                        }
                    }
                case ADD_ATK:
                    if (hero != null && value > hero.getUpperAtk() * 5) {
                        if (hero.getUpperAtk() > 0) {
                            return hero.getUpperAtk() * 5;
                        } else {
                            return value / 2;
                        }
                    }
                case ADD_UPPER_HP:
                    if (hero != null && value > hero.getRealUHP() * 5) {
                        if (hero.getRealUHP() > 0) {
                            return hero.getRealUHP() * 5;
                        } else {
                            return value / 2;
                        }
                    }
                case ADD_POWER:
                    if (hero != null && value > hero.getPower()) {
                        if (hero.getPower() > 0) {
                            return hero.getPower();
                        } else {
                            return value / 2;
                        }
                    }
                case ADD_AGI:
                    if (hero != null && value > hero.getAgility()) {
                        if (hero.getAgility() > 0) {
                            return hero.getAgility();
                        } else {
                            return value / 2;
                        }
                    }
                case ADD_STR:
                    if (hero != null && value > hero.getStrength()) {
                        if (hero.getStrength() > 0) {
                            return hero.getStrength();
                        } else {
                            return value / 2;
                        }
                    }
                case ADD_PER_ATK:
                case ADD_PER_DEF:
                case ADD_PER_UPPER_HP:
                    if (value > 80) {
                        return 80l;
                    }
            }
        } catch (Exception e) {
            LogHelper.logException(e);
        }
        return value;
    }

    public static boolean checkPet(Pet myPet) {
        boolean check = true;
        Cursor cursor = DBHelper.getDbHelper().excuseSOL("select type from monster where id = '" + myPet.getIndex() + "'");
        if (!cursor.isAfterLast()) {
            check = myPet.getType().equals("蛋") || myPet.getType().equals(cursor.getString(cursor.getColumnIndex("type")));
        }
        cursor.close();
        return check;
    }

    public static int getIndex(String name) {
        Cursor cursor = DBHelper.getDbHelper().excuseSOL("SELECT id FROM palace WHERE type = '" + name + "'");
        try {
            if (!cursor.isAfterLast()) {
                return cursor.getInt(cursor.getColumnIndex("id"));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return 0;
    }
}
