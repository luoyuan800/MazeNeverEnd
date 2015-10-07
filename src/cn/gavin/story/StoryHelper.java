package cn.gavin.story;

import cn.gavin.Achievement;
import cn.gavin.Hero;
import cn.gavin.activity.MainGameActivity;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 9/23/2015.
 */
public class StoryHelper {
    public boolean trigger() {
        Hero hero = MainGameActivity.context.getHero();
        boolean b = hero.getAgility() / 2000 > hero.getRandom().nextLong();
        if (MainGameActivity.context.getMaze().getLev() == 1 && b && hero.getRandom().nextInt(1000) < 20) {
            MainGameActivity.context.addMessage(String.format("%s找到了一扇上锁了的门，但是没有钥匙打开它。门后面是什么呢？", hero.getFormatName()));
            Achievement.story.enable(hero);
        } else if (Achievement.story.isEnable() && b && hero.getRandom().nextBoolean() && hero.getRandom().nextLong(hero.getAgility() + 1) > 500 && 3 > (hero.getRandom().nextLong(hero.getLockBox() + 1) + 1)&& hero.getRandom().nextInt(1003) > 987) {
            MainGameActivity.context.addMessage(hero.getFormatName() + "找到一个带锁的宝箱");
            hero.setLockBox(hero.getLockBox() + 1);
        } else if (Achievement.story.isEnable() && hero.getKeyCount() < 15 && hero.getRandom().nextLong(hero.getAgility() + 1) > 600 && hero.getRandom().nextInt(1000) > 797) {
            MainGameActivity.context.addMessage(hero.getFormatName() + "找到一把宝箱钥匙");
            hero.setKeyCount(hero.getKeyCount() + 1);
        }
        return false;
    }
}
