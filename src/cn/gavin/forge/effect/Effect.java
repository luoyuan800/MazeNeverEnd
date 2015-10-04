package cn.gavin.forge.effect;

import cn.gavin.Hero;
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
            long atk = random.nextLong((hero.getStrength() + monster.getHp())/3000 + 1);
            if(atk <= 1000) atk = random.nextLong(hero.getMaxMazeLev()) + 1000;
            return atk;
        }
    }),
    ADD_UPPER_HP(new Calculate() {
        @Override
        public Number calculate(Hero hero, Monster monster) {
            Random random = hero.getRandom();
            long hp = random.nextLong((hero.getAgility() + monster.getAtk())/100 + 1);
            if(hp <= 1000) hp = random.nextLong(hero.getMaxMazeLev()) + 1000;
            return hp;
        }
    }),
    ADD_DEF(new Calculate() {
        @Override
        public Number calculate(Hero hero, Monster monster) {
            Random random = hero.getRandom();
            long def = random.nextLong((hero.getPower() + monster.getMaterial())/2000 + 1);
            if(def <= 1000) def = random.nextLong(hero.getMaxMazeLev()) + 1000;
            return def;
        }
    });
private Calculate calculate;
    private Effect(Calculate calculate){
        this.calculate = calculate;
    }
    public Number calculate(Hero hero, Monster monster){
        return calculate.calculate(hero, monster);
    }
}
