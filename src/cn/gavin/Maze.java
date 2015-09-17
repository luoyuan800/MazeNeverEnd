package cn.gavin;

import cn.gavin.activity.MainGameActivity;
import cn.gavin.monster.Monster;
import cn.gavin.monster.MonsterBook;
import cn.gavin.skill.Skill;
import cn.gavin.utils.Random;

/**
 * Created by gluo on 8/26/2015.
 */
public class Maze {
    private Hero hero;
    private long level;
    private boolean moving;
    private long step;
    private long streaking;
    private MonsterBook monsterBook;

    public boolean isMoving() {
        return moving;
    }

    private Random random = new Random();

    public Maze(Hero hero) {
        this.hero = hero;
        monsterBook = MonsterBook.getMonsterBook();
    }

    public Maze(Hero hero, MonsterBook monsterBook) {
        this.hero = hero;
        this.monsterBook = monsterBook;
    }

    public void move(MainGameActivity context) {
        String heroName = "<font color=\"#800080\">" + hero.getName() + "</font>";
        while (context.isGameThreadRunning()) {
            if (context.isPause()) {
                continue;
            }
            moving = true;
            step++;
            if (random.nextLong(10000) > 9985 || step > random.nextLong(22) || random.nextLong(streaking + level + 1) > 20 + level) {
                step = 0;
                level++;
                mazeLevelDetect();
                long point = 1 + random.nextLong(level + 1) / 19;
                context.addMessage(heroName + "进入了" + level + "层迷宫， 获得了<font color=\"#FF8C00\">" + point + "</font>点数奖励");
                if (level > hero.getMaxMazeLev()) {
                    hero.addMaxMazeLev();
                }
                hero.addPoint(point);
                hero.addHp(hero.getUpperHp() / (random.nextLong(level + 1) + 1) * (random.nextLong(level + 1) + 1));
                context.addMessage("-------------------");
            } else if (random.nextLong(100) > 95) {
                long mate = random.nextLong(level * 2 + 1) + random.nextLong(hero.getAgility() / 100 + 1) + 2;
                context.addMessage(heroName + "找到了一个宝箱， 获得了<font color=\"#FF8C00\">" + mate + "</font>材料");
                hero.addMaterial(mate);
                context.addMessage("-------------------");
            } else if (hero.getHp() < hero.getUpperHp() && random.nextLong(100) > 85) {
                long hel = random.nextLong(hero.getUpperHp() + 1);
                context.addMessage(heroName + "休息了一会，恢复了<font color=\"green\">" + hel + "</font>点HP");
                hero.addHp(hel);
                context.addMessage("-------------------");
            } else if (random.nextLong(9000) > 8977) {
                step = 0;
                long levJ = random.nextLong(hero.getMaxMazeLev() + 5) + 1;
                context.addMessage(heroName + "踩到了传送门，被传送到了迷宫第" + levJ + "层");
                level = levJ;
                if (level > hero.getMaxMazeLev()) {
                    hero.setMaxMazeLev(level);
                }
                mazeLevelDetect();
                context.addMessage("-------------------");
            } else if (random.nextBoolean()) {
                Monster monster;
                if (random.nextLong(1000) > 899) {
                    monster = Monster.getBoss(this, hero);
                    step += 21;
                } else {
                    monster = new Monster(hero, this);
                }

                context.addMessage(heroName + "遇到了" + monster.getFormatName());
                boolean atk = hero.getAgility() > monster.getHp() / 2 || random.nextBoolean();
                Skill skill;
                boolean isJump = false;
                while (!isJump && monster.getHp() > 0 && hero.getHp() > 0) {
                    if (context.isPause()) {
                        continue;
                    }
                    if (atk) {
                        skill = hero.useAttackSkill(monster);
                        isJump = false;
                        if (skill != null) {
                            isJump = skill.release(monster);
                        } else {
                            if (hero.getHp() < hero.getUpperHp()) {
                                skill = hero.useRestoreSkill();
                            }
                            if (skill != null) {
                                isJump = skill.release(monster);
                            } else {
                                monster.addHp(-(hero.getAttackValue()));
                                context.addMessage(heroName + "攻击了" + monster.getName() + "，造成了<font color=\"red\">" + hero.getAttackValue() + "</font>点伤害。");
                            }
                        }
                    } else {
                        skill = hero.useDefendSkill(monster);
                        if (skill != null) {
                            isJump = skill.release(monster);
                        } else {
                            long harm = monster.getAtk() - hero.getDefenseValue();
                            if (harm <= 0) {
                                harm = random.nextLong(level + 1);
                            }
                            hero.addHp(-harm);
                            context.addMessage(monster.getName() + "攻击了" + heroName + "，造成了<font color=\"red\">" + harm + "</font>点伤害。");
                        }
                    }
                    atk = !atk;
                    try {
                        Thread.sleep(context.getRefreshInfoSpeed());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (isJump) continue;
                if (monster.getHp() <= 0) {
                    streaking++;
                    if (streaking >= 100) {
                        Achievement.unbeaten.enable(hero);
                    }
                    context.addMessage(heroName + "击败了" + monster.getName() + "， 获得了<font color=\"blue\">" + monster.getMaterial() + "</font>份锻造材料。");
                    hero.addMaterial(monster.getMaterial());
                    monster.setDefeat(true);
                    monster.setMazeLev(level);
                    monsterBook.addMonster(monster);
                } else {
                    streaking = 0;
                    step = 0;
                    context.addMessage(heroName + "被" + monster.getName() + "打败了，回到迷宫第一层。");
                    this.level = 1;
                    hero.restore();
                    monster.setDefeat(false);
                    monster.setMazeLev(level);
                    monsterBook.addMonster(monster);
                }
                context.addMessage("-----------------------------");
            }
            try {
                Thread.sleep(context.getRefreshInfoSpeed());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        moving = false;
    }

    private void mazeLevelDetect() {
        switch ((int) level) {
            case 50:
                Achievement.maze50.enable(hero);
                break;
            case 100:
                Achievement.maze100.enable(hero);
                break;
            case 500:
                if (hero.getArmorLev() == 0 && hero.getSwordLev() == 0) {

                }
                Achievement.maze500.enable(hero);
                break;
            case 1000:
                Achievement.maze1000.enable(hero);
                break;
            case 10000:
                Achievement.maze10000.enable(hero);
                break;
            case 50000:
                Achievement.maze50000.enable(hero);
                break;
        }
    }

    public long getLev() {
        return level;
    }

    public void setLevel(long level) {
        this.level = level;
    }
}
