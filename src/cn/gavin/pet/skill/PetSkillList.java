package cn.gavin.pet.skill;

import cn.gavin.palace.nskill.NSkill;
import cn.gavin.pet.Pet;
import cn.gavin.skill.SkillFactory;
import cn.gavin.utils.MazeContents;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 11/12/2015.
 */
public enum PetSkillList {
    GoldenSearcher("自动收集锻造点数"), QuickGrow("自动获取额外能力点"), HeroHit("勇者之击"), DragonBreath("吐息"), DragonClaw("龙爪"), Lightning("闪电"), MultiSkill("多重攻击"), SandStorm("沙尘"), SwindleGame("欺诈游戏");
    private final String name;

    private PetSkillList(String name) {
        this.name = name;
    }

    public NSkill getSkill(Pet pet) {
        switch (this) {
            case GoldenSearcher:
            case QuickGrow:
                return NSkill.createSkillByName(name, pet, 1, null);
            default:
                return NSkill.createSkillBySkill(SkillFactory.getSkill(name, MazeContents.hero,null), pet);
        }
    }
}
