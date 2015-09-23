package cn.gavin.story;

import cn.gavin.Achievement;
import cn.gavin.activity.MainGameActivity;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 9/23/2015.
 */
public class StoryHelper {
    public boolean trigger(){
        if(MainGameActivity.context.getMaze().getLev() == 1 && MainGameActivity.context.getHero().getAgility() > MainGameActivity.context.getHero().getRandom().nextInt()){
            MainGameActivity.context.addMessage(String.format("%s找到了一扇上锁了的门，但是没有钥匙打开它。门后面是什么呢？", MainGameActivity.context.getHero().getFormatName()));
            Achievement.story.enable(MainGameActivity.context.getHero());
        }
        return false;
    }
}
