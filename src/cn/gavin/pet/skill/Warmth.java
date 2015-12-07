package cn.gavin.pet.skill;

import cn.gavin.Hero;
import cn.gavin.pet.Pet;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 11/26/2015.
 */
public class Warmth extends PetSkill{
    @Override
    public void release(Hero hero) {
        for(Pet pet : hero.getPets()){
            if(pet.getType().equals("蛋")){
                pet.setDeathCount(pet.getDeathCount() - 1);
            }
        }
    }

    @Override
    public String getName() {
        return "温暖";
    }
}
