package cn.gavin.maze;

import cn.gavin.Achievement;
import cn.gavin.Hero;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.monster.Monster;
import cn.gavin.monster.MonsterBook;
import cn.gavin.skill.Skill;
import cn.gavin.skill.SkillFactory;
import cn.gavin.story.StoryHelper;
import cn.gavin.utils.Random;

/**
 * Created by gluo on 8/26/2015.
 */
public class Maze {
    private int csmgl = 8997;
    private Hero hero;
    protected long level;
    private boolean moving;
    protected long step;
    protected long streaking;
    private float meetRate = 100f;
    private MonsterBook monsterBook;
    private StoryHelper storyHelper;
    protected long lastSave;
    private long hunt = 150;

    public void setCsmgl(int csmgl) {
        this.csmgl = csmgl;
    }

    public int getCsmgl() {
        return csmgl;
    }

    public boolean isMoving() {
        return moving;
    }

    private Random random = new Random();

    public Maze() {

    }

    public Maze(Hero hero) {
        this.hero = hero;
        storyHelper = new StoryHelper();
    }

    public Maze(Hero hero, MonsterBook monsterBook) {
        this.hero = hero;
        this.monsterBook = monsterBook;
        storyHelper = new StoryHelper();
    }

    public void move(MainGameActivity context) {
        this.monsterBook = context.getMonsterBook();
        while (context != null && context.isGameThreadRunning()) {
            if (context.isPause()) {
                continue;
            }
            if (storyHelper != null && storyHelper.trigger()) {
                continue;
            }
            moving = true;
            step++;
            if (random.nextLong(10000) > 9985 || step > random.nextLong(22) || random.nextLong(streaking + 1) > 50 + level) {
                if ((level - lastSave) > 50) {
                    lastSave = level;
                    context.save();
                }
                step = 0;
                level++;
                mazeLevelDetect();
                long point = 1 + level / 10 + random.nextLong(level + 1) / 300;
                if (point > 100) {
                    point = 50 + random.nextInt(60);
                }
                String msg = hero.getFormatName() + "进入了" + level + "层迷宫， 获得了<font color=\"#FF8C00\">" + point + "</font>点数奖励";
                addMessage(context, msg);
                if (level > hero.getMaxMazeLev()) {
                    hero.addMaxMazeLev();
                }

                hero.addPoint(point);
                hero.addHp(random.nextLong(hero.getUpperHp() / 20 + 1) + random.nextLong(hero.getPower() / 100));
                addMessage(context, "-------------------");
            } else if (random.nextLong(850 + hunt) > 993 && random.nextLong(hero.getAgility()) > random.nextLong(6971)) {
                long mate = random.nextLong(level * 300 + 1) + random.nextLong(hero.getAgility() / 1000 + 100) + 100;
                addMessage(context, hero.getFormatName() + "找到了一个宝箱， 获得了<font color=\"#FF8C00\">" + mate + "</font>材料");
                hero.addMaterial(mate);
                addMessage(context, "-------------------");
            } else if (hero.getHp() < hero.getUpperHp() && random.nextLong(1000) > 989) {
                long hel = random.nextLong(hero.getUpperHp() / 10 + 1) + random.nextLong(hero.getPower() / 100);
                if (hel > hero.getUpperHp() / 2) {
                    hel = random.nextLong(hero.getUpperHp() / 2 + 1) + 1;
                }
                addMessage(context, hero.getFormatName() + "休息了一会，恢复了<font color=\"#556B2F\">" + hel + "</font>点HP");
                hero.addHp(hel);
                addMessage(context, "-------------------");
            } else if (random.nextLong(9000) > csmgl) {
                step = 0;
                long levJ = random.nextLong(hero.getMaxMazeLev() + 5) + 1;
                addMessage(context, hero.getFormatName() + "踩到了传送门，被传送到了迷宫第" + levJ + "层");
                level = levJ;
                if (level > hero.getMaxMazeLev()) {
                    hero.setMaxMazeLev(level);
                }
                mazeLevelDetect();
                addMessage(context, "-------------------");
            } else if (random.nextBoolean()) {
                Skill skill1 = SkillFactory.getSkill("隐身", hero, context.getSkillDialog());
                if (skill1.isActive() && skill1.perform()) {
                    continue;
                }
                Monster monster = null;
                if (random.nextLong(1000) > 899) {
                    monster = Monster.getBoss(this, hero);
                    step += 21;
                }
                if (monster == null) {
                    monster = new Monster(hero, this);
                }
                monster.setMazeLev(level);
                if (BattleController.battle(hero, monster, random, this, context)) {
                    streaking++;
                    if (streaking >= 100) {
                        Achievement.unbeaten.enable(hero);
                    }
                } else {
                    streaking = 0;
                    step = 0;
                    String defeatedmsg = hero.getFormatName() + "被" + monster.getFormatName() + "打败了，回到迷宫第一层。";
                    addMessage(context, defeatedmsg);
                    monster.addBattleDesc(defeatedmsg);
                    if (level > 25) {
                        context.save();
                    }
                    this.level = 1;
                    hero.restore();
                    lastSave = level;
                }
                monsterBook.addMonster(monster);
                addMessage(context, "-----------------------------");
            }
            try {
                Thread.sleep(context.getRefreshInfoSpeed());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        moving = false;
    }

    private void addMessage(MainGameActivity context, String msg) {
        BattleController.addMessage(context, msg);
    }

    private void mazeLevelDetect() {
        if (level > Integer.MAX_VALUE) {
            if (level > Long.MAX_VALUE - 100) {
                level--;
            }
        } else {
            switch ((int) level) {
                case 50:
                    Achievement.maze50.enable(hero);
                    break;
                case 100:
                    Achievement.maze100.enable(hero);
                    break;
                case 500:
                    if (hero.getArmorLev() == 0 && hero.getSwordLev() == 0) {
                        Achievement.speculator.enable(hero);
                    }
                    Achievement.maze500.enable(hero);
                    break;
                case 1000:
                    Achievement.maze1000.enable(hero);
                    break;
                case 10000:
                    if (Achievement.maze100.isEnable()) {
                        Achievement.maze10000.enable(hero);
                    } else {
                        Achievement.cribber.enable(hero);
                    }
                    break;
                case 50000:
                    if (Achievement.maze10000.isEnable()) {
                        Achievement.maze50000.enable(hero);
                    } else {
                        Achievement.cribber.enable(hero);
                    }
                    break;

            }
        }
        if(level > 500000){
            if(!Achievement.richer.isEnable()) {
                addMessage(MainGameActivity.context, "您不能再前进了，前面是付费玩家的地盘！");
                level --;
            }
        }
        if (level != 0 && level % 100 == 0) {
            Skill fSkill = SkillFactory.getSkill("浮生百刃", hero, MainGameActivity.context.getSkillDialog());
            Skill xSkill = SkillFactory.getSkill("虚无吞噬", hero, MainGameActivity.context.getSkillDialog());
            boolean qzs = SkillFactory.getSkill("欺诈师", hero, MainGameActivity.context.getSkillDialog()).isActive();
            if (qzs && (random.nextLong(hero.getMaxMazeLev() + 1000) > 1100)) {
                fSkill.setActive(true);
            }
            if (qzs && (random.nextLong(hero.getMaxMazeLev() + 1000) > 1150)) {
                xSkill.setActive(true);
            }
        }
    }

    public long getLev() {
        return level;
    }

    public void setLevel(long level) {
        mazeLevelDetect();
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
}
