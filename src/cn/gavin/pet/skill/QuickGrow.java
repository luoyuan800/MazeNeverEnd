package cn.gavin.pet.skill;

import cn.gavin.Hero;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.palace.Base;
import cn.gavin.palace.nskill.NSkill;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 11/5/2015.
 */
public class QuickGrow extends PetSkill {
    @Override
    public String getName() {
        return "自动获取额外能力点";
    }

    @Override
    public void release(Hero hero) {
        hero.addPoint(hero.getRandom().nextLong(hero.getMaxMazeLev()/1000) + 1);
        if(MainGameActivity.context!=null){
            MainGameActivity.context.addMessage(me.getFormatName() + "不知道从什么地方收集了一些额外的能力点数");
        }
    }
}
