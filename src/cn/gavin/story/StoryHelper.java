package cn.gavin.story;

import cn.gavin.Achievement;
import cn.gavin.Hero;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.utils.Random;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 9/23/2015.
 */
public class StoryHelper {
    public boolean trigger() {
        final MainGameActivity context = MainGameActivity.context;
        Hero hero = context.getHero();
        if(!Achievement.guider1.isEnable()) {
            context.setPause(true);
            context.addMessage("门后面有一个人，你以为你在这个迷宫里面找到了一个正常的人，于是你上前和他搭话。<br><font color=\"#FF0000\">某某人</font>：我是个NPC，所以我不会和你战斗<br>" +
                    "<font color=\"#FF0000\">某某人</font>：我的名字叫<font color=\"#FF0000\">袁酥兄</font>，请不要想歪了,我是个很正经的人！<br><font color=\"#FF0000\">袁酥兄</font>：难得我们在这里相遇了，就让我们交个朋友吧。<br>" +
                    "<font color=\"#FF0000\">袁酥兄</font>：这样我们就是朋友了,那我就告诉你这个游戏的基本玩法吧<br>" +
                    "<font color=\"#FF0000\">袁酥兄</font>：首先看屏幕的左上角，那里是不是有一个很丑很搓的人像在那里呢？没错，不要怀疑，那个人就是你了！<br>" +
                    "<font color=\"#FF0000\">袁酥兄</font>：抚摸，哦不是，点击一下你自己的图像，你会发现有锻造点数奖励的哦！<br>" +
                    "<font color=\"#FF0000\">袁酥兄</font>：然后你看到了图标右边的锁头标记和钥匙标记了吗？点击锁头标记你可以打开一个宝箱，钥匙标记表示你现在还有多少把钥匙。<br>" +
                    "<font color=\"#FF0000\">袁酥兄</font>：接着我们来看看右上角，这里会显示你的名字和当前的迷宫层数与记录。<br>" +
                    "<font color=\"#FF0000\">袁酥兄</font>：点击名字，可以弹出修改名字的对话框。<br>" +
                    "<font color=\"#FF0000\">袁酥兄</font>：再来看名字的下面，这三个框框就是技能栏了，请不要再问别人这个是神马东东了……<br>" +
                    "<font color=\"#FF0000\">袁酥兄</font>：好了，你现在可以点击右边的继续按钮继续游戏了。哦，对了，长按战斗信息栏是可以隐藏战斗信息的。");
            Achievement.guider1.enable(hero);
            hero.setKeyCount(hero.getKeyCount() + 1);
            return true;
        }
        final Random random = hero.getRandom();
        boolean b = hero.getAgility() / 2000 > random.nextLong();
        boolean a = Achievement.guider1.isEnable() ? random.nextInt(1000) > 97 : random.nextInt(100) < 53;
        if (Achievement.story.isEnable() && hero.getKeyCount() > 0 && !Achievement.restriction.isEnable() && a) {
            context.addMessage(String.format("%s找到了一扇上锁了的门，使用一把钥匙打开这扇门", hero.getFormatName()));
            hero.setKeyCount(hero.getKeyCount() - 1);
            if (Achievement.guider1.isEnable() && !Achievement.guider2.isEnable() && b && hero.getKeyCount() >= 4 && random.nextInt(100) > 95 && context.getMaze().getLev()%21 == 0) {
                context.addMessage(hero.getFormatName() + "在门后面遇见了<font color=\"#FF0000\">袁酥兄</font><br>" +
                        "<font color=\"#FF0000\">袁酥兄</font>：孩子我们又见面了~<br>" +
                        "<font color=\"#FF0000\">袁酥兄</font>：这次就我就告诉你一些高等级的秘密<br>" +
                        "<font color=\"#FF0000\">袁酥兄</font>：吧啦吧啦吧啦吧啦……<br>" +
                        "<font color=\"#FF0000\">袁酥兄</font>：好的，我说完了。如果没听清楚的话就直接去成就面板查看备忘吧。<br>" +
                        "<font color=\"#FF0000\">袁酥兄</font>从" + hero.getFormatName() + "身上掏走了四把钥匙");
                Achievement.guider2.enable(hero);
                hero.setKeyCount(hero.getKeyCount() - 4);
                return true;
            } else if (context.getMaze().getLev()%23 == 0 && Achievement.guider1.isEnable() && Achievement.guider2.isEnable() && b && random.nextInt(100) > 97 && !Achievement.guider3.isEnable() && hero.getKeyCount() >= 5) {
                context.addMessage(hero.getFormatName() + "在门后面遇见了<font color=\"#FF0000\">袁酥兄</font><br>" +
                        "<font color=\"#FF0000\">袁酥兄</font>：老朋友，你好~<br>" +
                        "<font color=\"#FF0000\">袁酥兄</font>：这次见面我没有什么话好说，我只是想从你身上拿走钥匙而已。<br>" +
                        hero.getFormatName() + "身上的钥匙全都被抢走了！<br>" +
                        hero.getFormatName() + "捡到了一个带锁的宝箱。");
                Achievement.guider3.enable(hero);
                hero.setKeyCount(0);
                hero.setLockBox(hero.getLockBox() + 1);
                return true;
            } else if (context.getMaze().getLev()%95 == 0 && !Achievement.five.isEnable() && hero.getKeyCount() >= random.nextInt(5000)) {
                context.addMessage(hero.getFormatName() + "在门里面遇见了一个奇怪的男人♂<br>" +
                        "<font color=\"#FF0000\">某某人</font>：这位迷失的朋友，你好！我是<font color=\"#FF0000\">龙剑森</font>~<br>" +
                        "<font color=\"#FF0000\">龙剑森</font>：我是不小心进入到了这个迷宫中，从此和外面的时候隔离开来了。<br>" +
                        "<font color=\"#FF0000\">龙剑森</font>：这不知是好是坏，外面的世界或许早已……<br>" +
                        "<font color=\"#FF0000\">龙剑森</font>：算了，不提了。来，这是我给你的见面礼。" +
                        hero.getFormatName() + "获得了三把钥匙！<br>" +
                        "<font color=\"#FF0000\">龙剑森</font>：我还要告诉你五行的秘密。<br>" +
                        "<font color=\"#FF0000\">龙剑森</font>：在攻击的时候，如果对方的属性被你克制，那么你的攻击伤害会变成1.5倍。<br>" +
                        "<font color=\"#FF0000\">龙剑森</font>：但是如果对方的属性克制了你，那么你你对敌方的攻击伤害就会减半。<br>" +
                        "<font color=\"#FF0000\">龙剑森</font>：敌人攻击你的时候也是遵循这个规则的。<br>" +
                        "<font color=\"#FF0000\">龙剑森</font>：那么再见吧，朋友。期待我们再次见面。");
                Achievement.five.enable(hero);
                hero.setKeyCount(hero.getKeyCount() + 3);
                return true;
            } else if (context.getMaze().getLev()%121 == 0&&Achievement.five.isEnable() && !Achievement.reinforce.isEnable() && hero.getKeyCount() >= random.nextInt(1000)) {
                context.addMessage(hero.getFormatName() + "门里面遇见了<font color=\"#FF0000\">龙剑森</font><br>" +
                        "<font color=\"#FF0000\">龙剑森</font>：老朋友我们又见面了~<br>" +
                        "<font color=\"#FF0000\">龙剑森</font>：我在这迷宫中晃荡多年，对各种怪异早已经见怪不怪了。" +
                        "<font color=\"#FF0000\">龙剑森</font>：来，好久不见，可有想我呢？。" +
                        hero.getFormatName() + "获得了三把钥匙！<br>" +
                        "<font color=\"#FF0000\">龙剑森</font>：相信你已经遇见了很多带属性的怪物了，那么我告诉你五行相生的秘密<br>" +
                        "<font color=\"#FF0000\">龙剑森</font>：吧啦吧啦吧啦吧啦。<br>" +
                        "<font color=\"#FF0000\">龙剑森</font>：吧啦吧啦吧啦吧啦吧啦。<br>" +
                        "<font color=\"#FF0000\">龙剑森</font>：吧啦吧啦吧啦再多几个吧啦吧啦……<br>" +
                        "<font color=\"#FF0000\">龙剑森</font>：那么再见吧，朋友。期待我们再次见面。");
                Achievement.reinforce.enable(hero);
                hero.setKeyCount(hero.getKeyCount() + 3);
                return true;
            } else if (context.getMaze().getLev()%221 == 0&&Achievement.five.isEnable() && Achievement.reinforce.isEnable() && !Achievement.restriction.isEnable() && hero.getKeyCount() >= random.nextInt(5000)) {
                context.addMessage(hero.getFormatName() + "门里面遇见了<font color=\"#FF0000\">龙剑森</font><br>" +
                        "<font color=\"#FF0000\">龙剑森</font>：老朋友我们又见面了~<br>" +
                        "<font color=\"#FF0000\">龙剑森</font>：来，好久不见，可有想我呢？。" +
                        hero.getFormatName() + "获得了三把钥匙！<br>" +
                        "<font color=\"#FF0000\">龙剑森</font>：或许你这个仿佛没有尽头的迷宫产生了绝望，但是我一代英雄人物沦为只能和你说话的NPC，比你更加绝望。<br>" +
                        "<font color=\"#FF0000\">龙剑森</font>：我早已忘记自己的过去了……" +
                        "<font color=\"#FF0000\">龙剑森</font>：你要相信在这个世界里，你并不是孤独一人的。<br>" +
                        "<font color=\"#FF0000\">龙剑森</font>：朋友。继续往上爬吧，总有一天你会找到这个世界的真谛。<br>" +
                        "<font color=\"#FF0000\">龙剑森</font>：那么再见吧，期待我们再次见面。");
                Achievement.restriction.enable(hero);
                hero.setKeyCount(hero.getKeyCount() + 3);
                return true;
            }else if(b && random.nextLong(hero.getKeyCount() + 1500) < random.nextInt(5)){
                context.addMessage(hero.getFormatName() + "遇见了<font color=\"#FF0000\">小梅</font>，她红着脸递出了一把钥匙。");
                hero.setKeyCount(hero.getKeyCount() + 1);
                return false;
            }else if(b && random.nextLong(hero.getKeyCount() + 1000) < random.nextInt(10)){
                context.addMessage(hero.getFormatName() + "遇见了<font color=\"#FF0000\">雷二蛋</font>，他红着脸掏走了一把钥匙。");
                hero.setKeyCount(hero.getKeyCount() - 1);
                return false;
            }else {
                context.addMessage("但是门后面什么都没有！");
            }
        } else {
            if (b && !Achievement.story.isEnable()) {
                context.addMessage(String.format("%s找到了一扇上锁了的门，但是没有钥匙打开它。门后面是什么呢？", hero.getFormatName()));
                Achievement.story.enable(hero);
                return false;
            }
        }
        if (context.getMaze().getLev() % 6 == 0 && b && random.nextInt(1000) < 23) {
            final long properties = random.nextLong(hero.getAgility() + hero.getStrength() + hero.getPower() + 1);
            if (random.nextBoolean() && properties > 2030
                    && 2 > (random.nextLong(hero.getLockBox() + 1) + 1) && random.nextLong(910 + context.getMaze().getHunt()) > 995) {
                context.addMessage(hero.getFormatName() + "找到一个带锁的宝箱");
                hero.setLockBox(hero.getLockBox() + 1);
            } else if (hero.getKeyCount() < 15 && properties > 1030 && random.nextInt(1001) > 887) {
                context.addMessage(hero.getFormatName() + "找到一把宝箱钥匙");
                hero.setKeyCount(hero.getKeyCount() + 1);
            }
        }
        return false;
    }
}
