package cn.gavin.skill.expression;

import cn.gavin.Hero;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.monster.Monster;
import cn.gavin.skill.Skill;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 9/15/2015.
 */
public interface UseExpression {
    public boolean release(Hero hero, Monster monster, MainGameActivity context, Skill skill);
}
