package cn.gavin.palace;

import java.util.Stack;

import cn.gavin.Hero;
import cn.gavin.activity.PalaceActivity;
import cn.gavin.maze.Maze;
import cn.gavin.utils.Random;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 10/9/2015.
 */
public class Palace extends Maze {
    private Base hero;
    protected long level;
    protected long step;
    protected long streaking;
    private float meetRate = 100f;
    protected long lastSave;
    private long hunt = 150;
    private Stack<PalaceMonster> palaceMonsters;
    private Hero heroN;

    public boolean isMoving() {
        return true;
    }

    private Random random = new Random();

    public Palace(Hero hero) {
        this.heroN = hero;
        this.hero = new PalaceHero(hero);
        palaceMonsters = PalaceMonster.getPalaceMonster();
    }

    public void move(PalaceActivity context) {
        hero.setContext(context);
        while (context != null && context.isGameThreadRunning()) {
            Base monster;
            if (palaceMonsters.isEmpty()) {
                monster = new PalaceHero(heroN);
                monster.setName(heroN.getName() + "-镜像");
            } else {
                monster = palaceMonsters.pop();
            }
            monster.setContext(context);
            long lev = monster.getLev();
            context.addMessage(hero.getFormatName() + "进入了第 <b>" + lev + "</b> 层殿堂");
            context.addMessage(hero.getFormatName() + "遇见了殿堂守护者 <b>" + monster.getFormatName() + "</b> ");
            context.addMessage(monster.getFormatName() + "对" + hero.getFormatName() + "说：<i>" + monster.getHello() + "</i> ");
            boolean atk = random.nextLong(hero.getHp() / 2 + 1) > random.nextLong(monster.getHp() / 2 + 1);
            while (monster.getHp() > 0 && hero.getHp() > 0) {
                if (context.isPause()) continue;
                if (atk) {
                    hero.atk(monster);
                } else {
                    monster.atk(hero);
                }
                atk = !atk;
                try {
                    Thread.sleep(context.getRefreshInfoSpeed());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (monster.getHp() <= 0) {
                context.addMessage(hero.getFormatName() + "战胜了" + monster.getFormatName());
                if (random.nextBoolean()) {
                    long count = random.nextLong(lev / 100 + 1) + 1;
                    context.addMessage(hero.getFormatName() + "获得了" + context + "个宝箱");
                    heroN.setLockBox(heroN.getLockBox() + count);
                }
                if(monster instanceof PalaceHero){
                    context.addMessage("恭喜你挑战成功！<br>你击败了所有的殿堂守护者。");
                    long count = random.nextLong(lev / 2000 + 1) + 5;
                    context.addMessage(hero.getFormatName() + "获得了" + count + "个宝箱");
                    heroN.setLockBox(heroN.getLockBox() + count);
                    context.setPause(true);
                    break;
                }
            } else if (hero.getHp() <= 0) {
                context.addMessage(hero.getFormatName() + "被" + monster.getFormatName() + "打败了！");
                context.addMessage(hero.getFormatName() + "的殿堂挑战失败！");
                context.setPause(true);
                break;
            }
        }
        context.addMessage("挑战结束，你现在可以退出殿堂了。");
    }

    public long getLev() {
        return level;
    }

    public void setLevel(long level) {
        this.level = level;
    }

    public float getMeetRate() {
        return meetRate;
    }

    public void setMeetRate(float meetRate) {
        this.meetRate = meetRate;
    }

    public long getHunt() {
        return hunt;
    }

    public void setHunt(long hunt) {
        this.hunt = hunt;
    }

    public Base getHero() {
        return hero;
    }
}

