package cn.gavin.pet.skill;

import cn.gavin.Hero;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 11/11/2015.
 */
public class HealthSkill extends PetSkill {
    @Override
    public void release(Hero hero) {
        hero.addHp(hero.getRealUHP()/10);
    }

    @Override
    public String getName() {
        return "治疗术";
    }
}
