package cn.gavin.pet.skill;

import cn.gavin.Hero;
import cn.gavin.pet.Pet;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 11/19/2015.
 */
public class Shaman extends PetSkill {
    @Override
    public void release(Hero hero) {
        if(hero.getRandom().nextLong(((Pet)me).getIntimacy()) > 998){
            for(Pet pet : hero.getPets()){
                if(pet.getHp() <= 0){
                    pet.restore();
                    pet.addHp(-pet.getUHp()/2);
                    return;
                }
            }
        }
    }

    @Override
    public String getName() {
        return "祭师";
    }
}
