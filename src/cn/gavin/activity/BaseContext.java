package cn.gavin.activity;

import android.os.Handler;

import cn.gavin.monster.MonsterBook;
import cn.gavin.skill.SkillDialog;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 10/14/2015.
 */
public interface BaseContext {

    MonsterBook getMonsterBook();
    Handler getHandler();
    void addMessage(String... msg);
    boolean isPause();

    long getRefreshInfoSpeed();

    boolean isHideBattle();
}
