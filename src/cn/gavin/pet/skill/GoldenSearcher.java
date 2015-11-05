package cn.gavin.pet.skill;

import cn.gavin.Hero;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 11/5/2015.
 */
public class GoldenSearcher extends PetSkill {
    @Override
    public void release(Hero hero) {
        hero.addMaterial(hero.getRandom().nextLong(hero.getMaxMazeLev()/100) + 1);
    }

    @Override
    public String getName() {
        return "自动收集锻造点数";
    }
}
