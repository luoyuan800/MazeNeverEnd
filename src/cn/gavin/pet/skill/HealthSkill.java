package cn.gavin.pet.skill;

import cn.gavin.Hero;
import cn.gavin.activity.MainGameActivity;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 11/11/2015.
 */
public class HealthSkill extends PetSkill {
    @Override
    public void release(Hero hero) {
        if (hero != null) {
            hero.addHp(hero.getRealUHP() / 10);
            if (MainGameActivity.context != null && me != null) {
                MainGameActivity.context.addMessage(me.getFormatName() + "使用治疗术帮助" + hero.getFormatName() + "恢复了HP");
            }
        }
    }

    @Override
    public String getName() {
        return "治疗术";
    }
}
