package cn.gavin.utils;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import cn.gavin.Achievement;
import cn.gavin.Element;
import cn.gavin.Hero;
import cn.gavin.R;
import cn.gavin.db.DBHelper;
import cn.gavin.forge.effect.Effect;
import cn.gavin.log.LogHelper;
import cn.gavin.maze.Maze;
import cn.gavin.monster.Monster;
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
    public static SkillDialog skillDialog;
    public static long lastUpload;
    public static String SD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/maze";

public static Bitmap loadImageFromSD(String name){
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
                            return (long) (hero.getUpperDef() * 5);
                        } else {
                            return value / 2;
                        }
                    }
                case ADD_ATK:
                    if (hero != null && value > hero.getUpperAtk() * 5) {
                        if (hero.getUpperAtk() > 0) {
                            return (long) (hero.getUpperAtk() * 5);
                        } else {
                            return value / 2;
                        }
                    }
                case ADD_UPPER_HP:
                    if (hero != null && value > hero.getRealUHP() * 5) {
                        if (hero.getRealUHP() > 0) {
                            return (long) (hero.getRealUHP() * 5);
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

    public static int getImageByName(String name, String type) {
        if ("蛋".equals(type)) {
            return R.drawable.egg;
        }
        int index = Monster.getIndex(name);
        int image = R.drawable.h_4_s;
        switch (index) {
            case 0:
                image = R.drawable.zl;
                break;
            case 1:
                if (name.contains("红")) {
                    image = R.drawable.qy_red;
                } else {
                    image = R.drawable.qy;
                }
                break;
            case 2:
                image = R.drawable.pc;
                break;
            case 3:
                image = R.drawable.feie;
                break;
            case 4:
                image = R.drawable.zz;
                break;
            case 5:
                image = R.drawable.laoshu;
                break;
            case 6:
                if (name.contains("红")) {
                    image = R.drawable.mayi_red;
                } else {
                    image = R.drawable.mayi;
                }
                break;
            case 7:
                if (name.contains("红")) {
                    image = R.drawable.laohu_red;
                } else {
                    image = R.drawable.laohu;
                }
                break;
            case 8:
                image = R.drawable.jiao;
                break;
            case 9:
                if (name.contains("红")) {
                    image = R.drawable.xiezi_red;
                } else {
                    image = R.drawable.xiezi;
                }
                break;
            case 10:
                image = R.drawable.srn;
                break;
            case 11:
                if (name.contains("红")) {
                    image = R.drawable.bianfu_red;
                } else {
                    image = R.drawable.bianfu;
                }
                break;
            case 12:
                image = R.drawable.se;
                break;
            case 13:
                image = R.drawable.niu;
                break;
            case 14:
                image = R.drawable.wugui;
                break;
            case 15:
                image = R.drawable.santoushe;
                break;
            case 16:
                image = R.drawable.ciwei;
                break;
            case 17:
                image = R.drawable.lan;
                break;
            case 18:
                image = R.drawable.jingling;
                break;
            case 19:
                if (name.contains("红")) {
                    image = R.drawable.jiangshi_red;
                } else {
                    image = R.drawable.jiangshi;
                }
                break;
            case 20:
                image = R.drawable.fengh;
                break;
            case 21:
                if (name.contains("红")) {
                    image = R.drawable.long_pet_red;
                } else {
                    image = R.drawable.long_pet;
                }
                break;
            case 22:
                image = R.drawable.kulou;
                break;
            case 24:
                image = R.drawable.xion;
                break;
            case 25:
                image = R.drawable.zhuyan;
                break;
            case 26:
                image = R.drawable.luwu;
                break;
            case 27:
                image = R.drawable.shankui;
                break;
            case 28:
                if (name.contains("红")) {
                    image = R.drawable.qiongqi_red;
                } else {
                    image = R.drawable.qiongqi;
                }
                break;
            case 29:
                if (name.contains("红")) {
                    image = R.drawable.jiuweihu_red;
                } else {
                    image = R.drawable.jiuweihu;
                }
                break;
            case 30:
                image = R.drawable.fudi;
                break;
            case 31:
                image = R.drawable.zheng;
                break;
            case 32:
                image = R.drawable.zhuru;
                break;
            case 33:
                image = R.drawable.taowu;
                break;
            default:
                image = R.drawable.h_4_s;
        }
        return image;
    }

    public static boolean checkPet(Pet myPet) {
        boolean check;
        String name = myPet.getName();
        int nameIndex = Monster.getIndex(name);
        check = nameIndex < Monster.lastNames.length || "蛋".equals(myPet.getType());
        return check;
    }

    public static int getIndex(String name) {
        Cursor cursor = DBHelper.getDbHelper().excuseSOL("SELECT id FROM palace WHERE type = '" + name + "'");
        if (!cursor.isAfterLast()) {
            return cursor.getInt(cursor.getColumnIndex("id"));
        }
        else return 0;
    }
}
