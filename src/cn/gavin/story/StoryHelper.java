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
        if (MainGameActivity.context.getMaze().getLev()%11 == 0 && b && hero.getRandom().nextInt(1000) < 23) {
            if (!Achievement.story.isEnable()) {
                MainGameActivity.context.addMessage(String.format("%s找到了一扇上锁了的门，但是没有钥匙打开它。门后面是什么呢？", hero.getFormatName()));
                Achievement.story.enable(hero);
            } else if (hero.getRandom().nextBoolean() && hero.getRandom().nextLong(hero.getAgility() + 1) > 500
                    && 2 > (hero.getRandom().nextLong(hero.getLockBox() + 1) + 1) && hero.getRandom().nextLong(913 + MainGameActivity.context.getMaze().getHunt()) > 995) {
                MainGameActivity.context.addMessage(hero.getFormatName() + "找到一个带锁的宝箱");
                hero.setLockBox(hero.getLockBox() + 1);
            } else if (hero.getKeyCount() < 15 && hero.getRandom().nextLong(hero.getAgility() + 1) > 100 && hero.getRandom().nextInt(9801) > 897) {
                MainGameActivity.context.addMessage(hero.getFormatName() + "找到一把宝箱钥匙");
                hero.setKeyCount(hero.getKeyCount() + 1);
            } else if (hero.getKeyCount() > 0 && hero.getLockBox() == 0) {
                MainGameActivity.context.addMessage(String.format("%s找到了一扇上锁了的门，使用一把钥匙打开这扇门", hero.getFormatName()));
                hero.setKeyCount(hero.getKeyCount() - 1);
            }
        }
        return false;
    }
}
