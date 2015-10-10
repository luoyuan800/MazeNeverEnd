package cn.gavin.monster;

import android.database.Cursor;

import java.util.Stack;

import cn.gavin.Hero;
import cn.gavin.db.DBHelper;
import cn.gavin.maze.Palace;
import cn.gavin.utils.Random;
import cn.gavin.utils.StringUtils;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 10/9/2015.
 */
public class PalaceMonster extends Monster {
    Random random;
    String hello;
    String skill;
    long skillLev = 1;
    Palace palace;

    public long getAtk() {
        if (random.nextInt(100) < 20) {
            String desc = getFormatName() + "使用了技能《重击》";
            addBattleDesc(desc);
            palace.addMessage(desc);
            return atk * skillLev;
        }
        return atk;
    }

    public PalaceMonster(Hero hero, Palace maze) {
        super("", "【殿堂】", "", 123, 123);
        this.palace = maze;
        random = hero.getRandom();
        long hp = (hero.getAttackValue() / 20) * (random.nextLong(maze.getLev() + 1)) + random.nextLong(hero.getUpperHp() + 1) + maze.getLev() * 100;
        long atk = (hero.getDefenseValue() + hero.getHp()) / 5 + maze.getLev() * 32 + random.nextLong(hero.getAttackValue() / 3 + maze.getLev() + 1);
        if (hp <= 0) hp = Integer.MAX_VALUE - 10;
        if (atk <= 0) hp = Integer.MAX_VALUE - 100;
        if (atk > hero.getUpperHp() + hero.getDefenseValue()) {
            atk = atk / 2;
        }
        if (hp > hero.getAttackValue() * 30) {
            hp = hero.getAttackValue() * 30;
        }
        if (atk < hero.getDefenseValue()) {
            atk += random.nextLong(hero.getDefenseValue() * 3);
        }
        this.atk += atk;
        this.hp += hp;
        this.material = 1000 + random.nextLong(maze.getLev() * atk + 1) / 100;
    }

    public String getHello() {
        return hello;
    }

    public void setHello(String hello) {
        if(StringUtils.isNotEmpty(hello) && !"null".equals(hello)) {
            this.hello = hello;
        }else{
            this.hello = "我没有什么话好说的，战斗吧！";
        }
    }


    public static class PalaceBoss extends PalaceMonster {
        Palace maze;

        public PalaceBoss(Hero hero, Palace maze) {
            super(hero, maze);
            this.maze = maze;
        }

        public void load() {
            try {
                Cursor cursor = DBHelper.getDbHelper().excuseSOL("SELECT * FROM defender WHERE lev = '" + maze.getLev() + "'");
                if (cursor.isAfterLast()) {
                    String atkStr = cursor.getString(cursor.getColumnIndex("atk"));
                    if ("null".equalsIgnoreCase(atkStr)) {
                        atk = Long.parseLong(atkStr) + random.nextLong(atk + 1);
                    }
                    String hpStr = cursor.getString(cursor.getColumnIndex("hp"));
                    if (!"null".equalsIgnoreCase(hpStr)) {
                        hp = Long.parseLong(hpStr) + random.nextLong(hp);
                    }
                    skill = cursor.getString(cursor.getColumnIndex("skill"));
                    String skillLev = cursor.getString(cursor.getColumnIndex("skill_lev"));
                    if ("null".equals(skillLev)) {
                        this.skillLev = 3;
                    } else {
                        this.skillLev = Long.parseLong(skillLev) * 3;
                    }
                    this.material = random.nextLong(maze.getLev() * atk + 1) / 100 + random.nextLong(material + 1);
                    setHello(cursor.getString(cursor.getColumnIndex("hello")));
                }
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Stack<PalaceMonster> getPalaceMonsters(Hero hero, Palace palace) {
        Stack<PalaceMonster> stack = new Stack<PalaceMonster>();
        PalaceBoss boss = new PalaceBoss(hero, palace);
        boss.load();
        stack.push(boss);
        for (int i = 0; i < 8; i++) {
            if (hero.getRandom().nextBoolean()) {
                stack.add(new PalaceMonster(hero, palace));
            }
        }
        return stack;
    }
}
