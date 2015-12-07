package cn.gavin.pet.skill;

import cn.gavin.Hero;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 11/26/2015.
 */
public class Tempt extends PetSkill {
    @Override
    public void release(Hero hero) {
        if(hero.getPetRate() > 0.4 && hero.getRandom().nextBoolean()){
            hero.setPetRate(hero.getPetRate() - 0.1f);
        }
    }

    @Override
    public String getName() {
        return "诱惑";
    }
}
