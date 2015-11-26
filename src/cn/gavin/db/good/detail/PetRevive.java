package cn.gavin.db.good.detail;

import cn.gavin.Hero;
import cn.gavin.pet.Pet;
import cn.gavin.utils.MazeContents;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 11/26/2015.
 */
public class PetRevive extends UsableGood {
    @Override
    public Object use() {
        Hero hero = MazeContents.hero;
        if(hero!=null){
            count--;
            for(Pet pet: hero.getPets()){
                if(pet.getHp() <= 0){
                    pet.restoreHalf();
                }
            }
        }
        return null;
    }

    @Override
    public String getName() {
        return "复活宠物";
    }

    public String toString(){
        return getName() + "<br>使用后所有死亡的宠物半血复活。<br>拥有数量：" + count;
    }
}
