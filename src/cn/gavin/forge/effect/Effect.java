package cn.gavin.forge.effect;

import cn.gavin.Hero;
import cn.gavin.maze.Maze;
import cn.gavin.monster.Monster;
import cn.gavin.utils.Random;

/**
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED
 * Created by luoyuan on 10/4/15.
 */
public enum Effect {
    ADD_ATK(new Calculate() {
        @Override
        public Number calculate(Hero hero, Monster monster) {
            Random random = hero.getRandom();
            long atk = random.nextLong((hero.getStrength() + monster.getMaxHP()) / 3705 + 1);
            if (atk <= 1800) atk = random.nextLong(hero.getMaxMazeLev() + 1) + 1800;
            return atk;
        }
    }, "增加攻击上限"),
    ADD_UPPER_HP(new Calculate() {
        @Override
        public Number calculate(Hero hero, Monster monster) {
            Random random = hero.getRandom();
            long hp = random.nextLong((hero.getAgility() + monster.getAtk())/2750) +1900;
            return hp;
        }
    }, "增加HP上限"),
    ADD_DEF(new Calculate() {
        @Override
        public Number calculate(Hero hero, Monster monster) {
            Random random = hero.getRandom();
            long def = random.nextLong((hero.getPower() + monster.getMaterial()) / 3500 + 1);
            if (def <= 1200) def = random.nextLong(hero.getMaxMazeLev() + 1) + 1200;
            return def;
        }
    }, "增加防御上限"),
    ADD_AGI(new Calculate() {
        @Override
        public Number calculate(Hero hero, Monster monster) {
            Random random = hero.getRandom();
            long agi = random.nextLong((hero.getBaseDefense() + monster.getMaxHP()) /100000 + 1);
            if (agi <= 80) agi = random.nextLong(hero.getMaxMazeLev() + 1) + 80;
            return agi;
        }
    }, "增加敏捷"),
    ADD_STR(new Calculate() {
        @Override
        public Number calculate(Hero hero, Monster monster) {
            Random random = hero.getRandom();
            long str = random.nextLong((hero.getAttackValue() + monster.getAtk()) / 100000 + 1);
            if (str <=100) str = random.nextLong(hero.getMaxMazeLev() + 1) + 100;
            return str;
        }
    }, "增加力量"),
    ADD_POWER(new Calculate() {
        @Override
        public Number calculate(Hero hero, Monster monster) {
            Random random = hero.getRandom();
            long power = random.nextLong((hero.getHp() + monster.getHp()) / 60000 + 1) + 1;
            if (power <= 150) power = random.nextLong(hero.getMaxMazeLev() + 1) + 150;
            return power;
        }
    }, "增加体力"),
    ADD_CLICK_AWARD(new Calculate() {
        @Override
        public Number calculate(Hero hero, Monster monster) {
            Random random = hero.getRandom();
            long award = random.nextLong((hero.getMaxMazeLev() + monster.getMaterial()) / 1000 + 1) + 6;
            if (award > 300) award = random.nextLong(300) + 100;
            return award;
        }
    }, "增加点击锻造点数奖励"),
    ADD_CLICK_POINT_AWARD(new Calculate() {
        @Override
        public Number calculate(Hero hero, Monster monster) {
            Random random = hero.getRandom();
            long award = random.nextLong((hero.getMaxMazeLev() + monster.getMaterial()) / 5000 + 1) + 1;
            if (award <= 2) award = 1;
            if (award >= 4) award = random.nextLong(4) + 1;
            return award;
        }
    }, "增加点击能力点数奖励"),
    ADD_HIT_RATE(new Calculate() {
        @Override
        public Number calculate(Hero hero, Monster monster) {
            Random random = hero.getRandom();
            long award = random.nextLong((hero.getMaxMazeLev() + monster.getMaterial()) / 500 + 5);
            if (award >= 20) award = random.nextLong(20) + 1;
            if(award == 0) award = 1;
            return award;
        }
    }, "增加暴击率"),
    ADD_DODGE_RATE(new Calculate() {
        @Override
        public Number calculate(Hero hero, Monster monster) {
            Random random = hero.getRandom();
            long award = random.nextLong((hero.getMaxMazeLev() + monster.getMaterial()) / 500 + 5);
            if (award >= 20) award = random.nextLong(20) + 1;
            if(award <= 0) award = 1;
            return award;
        }
    }, "增加闪避率"),
    ADD_PARRY(new Calculate() {
        @Override
        public Number calculate(Hero hero, Monster monster) {
            Random random = hero.getRandom();
            long award = random.nextLong((hero.getMaxMazeLev() + monster.getMaterial()) / 2000 + 2);
            if (award >= 20) award = random.nextLong(20) + 1;
            if(award <= 0) award =1;
            return award;
        }
    }, "增加格挡概率"),
    ADD_PER_ATK(new Calculate() {
        @Override
        public Number calculate(Hero hero, Monster monster) {
            Random random = hero.getRandom();
            long atk = random.nextLong((hero.getStrength() + monster.getMaxHP()) / 3705 + 1) * 100 /hero.getUpperAtk();
            if(atk < 1) atk = 1;
            if (atk > 10) atk = 10;
            return atk;
        }
    }, "增加攻击上限%"),
    ADD_PER_UPPER_HP(new Calculate() {
        @Override
        public Number calculate(Hero hero, Monster monster) {
            Random random = hero.getRandom();
            long hp = (random.nextLong((hero.getAgility() + monster.getAtk())/2750) +1900) * 100 / hero.getRealUHP();
            if(hp < 1) hp = 1;
            if (hp > 10) hp = 10;
            return hp;
        }
    }, "增加HP上限%"),
    ADD_PER_DEF(new Calculate() {
        @Override
        public Number calculate(Hero hero, Monster monster) {
            Random random = hero.getRandom();
            long def = random.nextLong((hero.getPower() + monster.getMaterial()) / 3500 + 1) * 100/hero.getUpperDef();
            if(def < 1) def = 1;
            if(def > 10) def = 10;
            return def;
        }
    }, "增加防御上限%");
    private Calculate calculate;
    private String name;

    private Effect(Calculate calculate, String name) {
        this.calculate = calculate;
        this.name = name;
    }

    public Number calculate(Hero hero, Monster monster) {
        return calculate.calculate(hero, monster);
    }

    public String getName() {
        return name;
    }

    public long calculate(Hero hero) {
        Maze maze = new Maze();
        maze.setLevel(hero.getMaxMazeLev());
        Monster monster = new Monster(hero, maze);
        if (hero.getRandom().nextInt(100) > 95) {
            monster.setMazeLev(maze.getLev() * 2);
        }
        return calculate(hero, monster).longValue();
    }

}
