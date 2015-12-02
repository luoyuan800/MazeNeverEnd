package cn.gavin.db.good.detail;

import java.util.ArrayList;
import java.util.List;

import cn.gavin.Element;
import cn.gavin.monster.Monster;
import cn.gavin.palace.nskill.NSkill;
import cn.gavin.pet.Pet;
import cn.gavin.pet.PetDB;
import cn.gavin.pet.skill.PetSkillList;
import cn.gavin.utils.MazeContents;
import cn.gavin.utils.Random;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 12/2/2015.
 */
public class EggGift extends UsableGood {
    public static final int type = 2;

    public int getType(){
        return type;
    }
    private List<String> types;
    private int reduce;

    @Override
    public List<Pet> use() {
        List<Pet> eggs = new ArrayList<Pet>(types.size());
        Random random = new Random();
        int first = random.nextInt(Monster.firstNames.length);
        int second = random.nextInt(Monster.secondNames.length);
        for (String type : types) {
            Pet egg = new Pet();
            egg.setType("蛋");
            egg.setName(Monster.firstNames[first] + type + Monster.secondNames[second]);
            egg.setHp(random.nextLong(MazeContents.hero.getUpperHp()) + Monster.firstAdditionHP[first] + Monster.secondAdditionHP[second]);
            egg.setAtk(random.nextLong(MazeContents.hero.getBaseAttackValue()) + Monster.firstAdditionAtk[first] + Monster.secondAdditionAtk[second]);
            egg.setDef(random.nextLong(MazeContents.hero.getBaseDefense()) + 5);
            egg.setfName("未知");
            egg.setName("未知");
            egg.setDeathCount(255 - random.nextInt(250));
            NSkill skill = PetSkillList.getRandomSkill(random, egg, 0);
            egg.setSkill(skill);
            egg.setSex(random.nextInt(2));
            egg.setElement(Element.values()[random.nextInt(Element.values().length - 1)]);
            eggs.add(egg);
            egg.save();
        }

        return eggs;
    }

    @Override
    public String getName() {
        return "蛋礼包";
    }

    public String toString() {
        return getName() + "<br>使用可以获得：<br>" + types.size() + "个宠物蛋";
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public int getReduce() {
        return reduce;
    }

    public void setReduce(int reduce) {
        this.reduce = reduce;
    }
}
