package cn.gavin.pet.skill;

import cn.gavin.palace.nskill.NSkill;
import cn.gavin.pet.Pet;
import cn.gavin.skill.SkillFactory;
import cn.gavin.utils.MazeContents;
import cn.gavin.utils.Random;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 11/12/2015.
 */
public enum PetSkillList {
    Warmth("温暖"), Tempt("诱惑"), Shaman("祭祀"), QuickGrow("自动获取额外能力点"), GoldenSearcher("自动收集锻造点数"), HealthSkill("治疗术"), HeroHit("勇者之击"), DragonBreath("吐息"), DragonClaw("龙爪"), Lightning("闪电"), MultiSkill("多重攻击"), SandStorm("沙尘"), SwindleGame("欺诈游戏");
    private final String name;

    private PetSkillList(String name) {
        this.name = name;
    }

    public NSkill getSkill(Pet pet) {
        switch (this) {
            case GoldenSearcher:
            case QuickGrow:
            case Shaman:
            case Warmth:
            case Tempt:
            case HealthSkill:
                return NSkill.createSkillByName(name, pet, 1, null);
            default:
                return NSkill.createSkillBySkill(SkillFactory.getSkill(name, MazeContents.hero), pet);
        }
    }

    public static NSkill getRandomSkill(Random random, Pet pet, int reduce){
        int sindex = random.nextInt(PetSkillList.values().length - reduce);
        return PetSkillList.values()[sindex].getSkill(pet);
    }

    public static NSkill getRandomSkill(Random random, Pet pet, int start, int reduce){
        if(reduce >= values().length){
            reduce = 0;
        }
        int sindex = start + random.nextInt(PetSkillList.values().length - reduce);
        if(sindex >= values().length){
            sindex = values().length -1;
        }
        return PetSkillList.values()[sindex].getSkill(pet);
    }
}
