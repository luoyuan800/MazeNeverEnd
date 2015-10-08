package cn.gavin.forge.effect;

import cn.gavin.Hero;
import cn.gavin.monster.Monster;
import cn.gavin.utils.Random;

/**
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED
 * Created by luoyuan on 10/4/15.
 */
public interface Calculate {
    public Number calculate(Hero hero, Monster monster);
}
