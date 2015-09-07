package cn.gavin;

import java.util.Random;

import cn.gavin.activity.MainGameActivity;

/**
 * Created by gluo on 8/26/2015.
 */
public class Maze {
    private Hero hero;
    private int level;
    private boolean moving;
    private int step;
    private int streaking;

    public boolean isMoving() {
        return moving;
    }

    private Random random = new Random();

    public Maze(Hero hero) {
        this.hero = hero;
    }

    public void move(MainGameActivity context) {
        while (context.isGameThreadRunning()) {
            if (context.isPause()) {
                continue;
            }
            moving = true;
            step++;
            if (random.nextInt(10000) > 9985 || step > random.nextInt(22) || random.nextInt(streaking + level + 1) > 20 + level) {
                step = 0;
                level++;
                mazeLevelDetect();
                int point = 2 + random.nextInt(level + 1) / 2;
                context.addMessage(hero.getName() + "进入了" + level + "层迷宫， 获得了" + point + "点数奖励");
                if (level > hero.getMaxMazeLev()) {
                    hero.addMaxMazeLev();
                }
                hero.addPoint(point);
                hero.addHp(hero.getUpperHp() / (random.nextInt(level + 1) + 1) * (random.nextInt(level + 1) + 1));
                context.addMessage("-------------------");
            } else if (random.nextInt(100) > 95) {
                int mate = random.nextInt(level * 2 + 1) + random.nextInt(hero.getAgility() + 1) + 2;
                context.addMessage(hero.getName() + "找到了一个宝箱， 获得了" + mate + "材料");
                hero.addMaterial(mate);
                context.addMessage("-------------------");
            } else if (hero.getHp() < hero.getUpperHp() && random.nextInt(100) > 85) {
                int hel = random.nextInt(hero.getUpperHp() + 1);
                context.addMessage(hero.getName() + "休息了一会，恢复了" + hel + "点HP");
                hero.addHp(hel);
                context.addMessage("-------------------");
            } else if (random.nextInt(9000) > 8977) {
                step = 0;
                int levJ = random.nextInt(hero.getMaxMazeLev() + 5) + 1;
                context.addMessage(hero.getName() + "踩到了传送门，被传送到了迷宫第" + levJ + "层");
                level = levJ;
                if (level > hero.getMaxMazeLev()) {
                    hero.setMaxMazeLev(level);
                }
                mazeLevelDetect();
                context.addMessage("-------------------");
            } else if (random.nextBoolean()) {
                Monster monster;
                if (random.nextInt(1000) > 899) {
                    monster = Monster.getBoss(this, hero);
                    step += 21;
                } else {
                    monster = new Monster(hero, this);
                }
                context.addMessage(hero.getName() + "遇到了" + monster.getName());
                boolean atk = hero.getAgility() > monster.getHp() / 2 || random.nextBoolean();
                while (monster.getHp() > 0 && hero.getHp() > 0) {
                    if (context.isPause()) {
                        continue;
                    }
                    if (atk) {
                        Skill skill = hero.useSkill();
                        if (skill != null) {
                            context.addMessages(skill.release(hero, monster));
                        } else {
                            monster.addHp(-(hero.getAttackValue()));
                            context.addMessage(hero.getName() + "攻击了" + monster.getName() + "，造成了" + hero.getAttackValue() + "点伤害。");
                        }
                    } else {
                        int harm = monster.getAtk() - hero.getDefenseValue();
                        if (harm <= 0) {
                            harm = random.nextInt(level + 1);
                        }
                        hero.addHp(-harm);
                        context.addMessage(monster.getName() + "攻击了" + hero.getName() + "，造成了" + harm + "点伤害。");
                    }
                    atk = !atk;
                    try {
                        Thread.sleep(context.getRefreshInfoSpeed());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (monster.getHp() <= 0) {
                    streaking++;
                    if (streaking >= 100) {
                        Achievement.unbeaten.enable(hero);
                    }
                    context.addMessage(hero.getName() + "击败了" + monster.getName() + "， 获得了" + monster.getMaterial() + "份锻造材料。");
                    hero.addMaterial(monster.getMaterial());
                } else {
                    streaking = 0;
                    step = 0;
                    context.addMessage(hero.getName() + "被" + monster.getName() + "打败了，回到迷宫第一层。");
                    this.level = 1;
                    hero.restore();
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
        switch (level) {
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

    public int getLev() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
