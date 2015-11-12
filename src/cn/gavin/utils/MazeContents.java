package cn.gavin.utils;

import cn.gavin.Achievement;
import cn.gavin.Hero;
import cn.gavin.R;
import cn.gavin.forge.effect.Effect;
import cn.gavin.log.LogHelper;
import cn.gavin.maze.Maze;
import cn.gavin.monster.Monster;
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

    public static Maze getMaze() {
        return maze;
    }

    public static final String PALACE_TABLE_NAME = "uploader";


    public static boolean checkCheat(Hero hero) {
        long point = 3500 * hero.getMaxMazeLev();
        boolean normal = hero.getStrength() < point && hero.getAgility() < point && hero.getPower() < point;
        boolean normal2 = hero.getBaseAttackValue() < point * hero.ATR_RISE &&
                hero.getBaseDefense() < point * hero.DEF_RISE && hero.getRealUHP() < point * 10 * hero.MAX_HP_RISE;
        return normal && normal2 && ((hero.getUpperHp() + hero.getUpperDef() + hero.getUpperAtk()) < (hero.getMaxMazeLev() * 4900000 * (hero.getPay() + 2))) && !(hero.getMaxMazeLev() > 50000 && !Achievement.richer.isEnable());
    }

    public static Long reduceLegacyEffect(Effect effect, Long value) {
        try {
            switch (effect) {
                case ADD_DEF:
                    if (hero != null && value > hero.getUpperDef() * 1.5) {
                        return (long) (hero.getUpperAtk() * 1.5);
                    }
                case ADD_ATK:
                    if (hero != null && value > hero.getUpperAtk() * 1.5) {
                        return (long) (hero.getUpperAtk() * 1.5);
                    }
                case ADD_UPPER_HP:
                    if (hero != null && value > hero.getRealUHP() * 1.5) {
                        return (long) (hero.getUpperAtk() * 1.5);
                    }
                case ADD_POWER:
                    if (hero != null && value > hero.getPower()) {
                        return hero.getPower();
                    }
                case ADD_AGI:
                    if (hero != null && value > hero.getAgility()) {
                        return hero.getAgility();
                    }
                case ADD_STR:
                    if (hero != null && value > hero.getStrength()) {
                        return hero.getStrength();
                    }
            }
        }catch (Exception e){
            LogHelper.logException(e);
        }
        return value;
    }

    public static int getImageByName(String name){
        int index = Monster.getIndex(name);
        int image = R.drawable.h_4_s;
        switch (index) {
            case 0:
                image = R.drawable.zl;
                break;
            case 1:
                image = R.drawable.qy;
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
                image = R.drawable.mayi;
                break;
            case 7:
                image = R.drawable.laohu;
                break;
            case 8:
                image = R.drawable.jiao;
                break;
            case 9:
                image = R.drawable.xiezi;
                break;
            case 10:
                image = R.drawable.srn;
                break;
            case 11:
                image = R.drawable.bianfu;
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
                image = R.drawable.jiangshi;
                break;
            case 20:
                image = R.drawable.fengh;
                break;
            case 21:
                image = R.drawable.long_pet;
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
                image = R.drawable.qiongqi;
                break;
            default:
                image = R.drawable.h_4_s;
        }
        return image;
    }
}
