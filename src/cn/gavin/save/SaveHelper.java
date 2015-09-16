package cn.gavin.save;

import android.app.Activity;
import android.database.Cursor;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
        try {
            FileOutputStream fos = context.openFileOutput("yzcmg.ave", Activity.MODE_PRIVATE);
            StringBuilder sb = new StringBuilder();
            sb.append(heroN.getName()).append("_").append(heroN.getHp()).append("_").append(heroN.getUpperHp()).
                    append("_").append(heroN.getBaseAttackValue()).append("_").append(heroN.getBaseDefense()).append("_").
                    append(heroN.getClick()).append("_").append(heroN.getPoint()).append("_").append(heroN.getMaterial())
                    .append("_").append(heroN.getSwordLev()).append("_").append(heroN.getArmorLev()).append("_").
                    append(heroN.getSword()).append("_").append(heroN.getArmor()).append("_").append(heroN.getMaxMazeLev())
                    .append("_").append(heroN.getStrength()).append("_").append(heroN.getPower()).append("_").
                    append(heroN.getAgility()).append("_").append(heroN.getClickAward());
            sb.append("_");
            for (Achievement achievement : Achievement.values()) {
                if (achievement.isEnable()) {
                    sb.append(1);
                } else {
                    sb.append(0);
                }
            }
            sb.append("_");
            sb.append(maze.getLev());
            sb.append("_").append(context.getAlipay().getPayTime());
            sb.append("_").append(heroN.getDeathCount());
            sb.append("_").append(111);
            sb.append("_").append(111);
            sb.append("_").append(111);
            sb.append("_").append(context.getLastUploadLev());
            fos.write(sb.toString().getBytes("UTF-8"));
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean loadHero() {
        Hero heroN;
        Maze maze;
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
                heroN = new Hero(atts[0]);
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
                maze = new Maze(heroN, context.getMonsterBook());
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
