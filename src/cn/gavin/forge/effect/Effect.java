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
            long atk = random.nextLong((hero.getStrength() + monster.getMaxHP()) / 500 + 1);
            if (atk <= 1000) atk = random.nextLong(hero.getMaxMazeLev() + 1) + 1000;
            return atk;
        }
    }, "增加攻击上限"),
    ADD_UPPER_HP(new Calculate() {
        @Override
        public Number calculate(Hero hero, Monster monster) {
            Random random = hero.getRandom();
            long hp = random.nextLong((hero.getAgility() + monster.getAtk()) / 500 + 1);
            if (hp <= 1000) hp = random.nextLong(hero.getMaxMazeLev() + 1) + 1000;
            return hp;
        }
    }, "增加HP上限"),
    ADD_DEF(new Calculate() {
        @Override
        public Number calculate(Hero hero, Monster monster) {
            Random random = hero.getRandom();
            long def = random.nextLong((hero.getPower() + monster.getMaterial()) / 1500 + 1);
            if (def <= 1000) def = random.nextLong(hero.getMaxMazeLev() + 1) + 1000;
            return def;
        }
    }, "增加防御上限"),
    ADD_AGI(new Calculate() {
        @Override
        public Number calculate(Hero hero, Monster monster) {
            Random random = hero.getRandom();
            long agi = random.nextLong((hero.getBaseDefense() + monster.getMaxHP()) / 200000 + 1);
            if (agi <= 500) agi = random.nextLong(hero.getMaxMazeLev() + 1) + 499;
            return agi;
        }
    }, "增加敏捷"),
    ADD_STR(new Calculate() {
        @Override
        public Number calculate(Hero hero, Monster monster) {
            Random random = hero.getRandom();
            long str = random.nextLong((hero.getAttackValue() + monster.getAtk()) / 200000 + 1);
            if (str <= 500) str = random.nextLong(hero.getMaxMazeLev() + 1) + 399;
            return str;
        }
    }, "增加力量"),
    ADD_POWER(new Calculate() {
        @Override
        public Number calculate(Hero hero, Monster monster) {
            Random random = hero.getRandom();
            long power = random.nextLong((hero.getHp() + monster.getHp()) / 200000 + 1) + 1;
            if (power <= 500) power = random.nextLong(hero.getMaxMazeLev() + 1) + 699;
            return power;
        }
    }, "增加体力"),
    ADD_CLICK_AWARD(new Calculate() {
        @Override
        public Number calculate(Hero hero, Monster monster) {
            Random random = hero.getRandom();
            long award = random.nextLong((hero.getMaxMazeLev() + monster.getMaterial()) / 500 + 1) + 1;
            if (award <= 10) award = random.nextLong(hero.getMaxMazeLev() + 1) + 10;
            return award;
        }
    }, "增加点击奖励"),
    ADD_PARRY(new Calculate() {
        @Override
        public Number calculate(Hero hero, Monster monster) {
            Random random = hero.getRandom();
            long award = random.nextLong((hero.getMaxMazeLev() + monster.getMaterial()) / 10000 + 2) + 1;
            if (award >= 20) award = random.nextLong(20) + 1;
            return award;
        }
    }, "增加格挡概率");
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
        if(hero.getRandom().nextInt(100) > 95){
            monster.setMazeLev(maze.getLev()*2);
        }
        return calculate(hero, monster).longValue();
    }

}
