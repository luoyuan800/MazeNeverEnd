package cn.gavin.skill;

import cn.gavin.Hero;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.monster.Monster;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 9/15/2015.
 */
public interface UseExpression {
    public boolean release(Hero hero, Monster monster, MainGameActivity context);
}
