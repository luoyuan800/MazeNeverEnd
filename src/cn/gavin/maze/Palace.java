package cn.gavin.maze;

import java.util.Stack;

import cn.gavin.Achievement;
import cn.gavin.Hero;
import cn.gavin.activity.PalaceActivity;
import cn.gavin.forge.Item;
import cn.gavin.forge.list.ItemName;
import cn.gavin.monster.Monster;
import cn.gavin.monster.PalaceMonster;
import cn.gavin.skill.Skill;
import cn.gavin.skill.SkillFactory;
import cn.gavin.utils.Random;
import cn.gavin.utils.StringUtils;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 10/9/2015.
 */
public class Palace extends Maze {
    private Stack<PalaceMonster> monsters;
    private PalaceActivity context;

    public void move(PalaceActivity context) {
        this.context = context;
        Hero hero = context.getHero();
        Random random = hero.getRandom();
        monsters = PalaceMonster.getPalaceMonsters(hero, this);
        context.addMessage(hero.getFormatName() + "进入了殿堂，并且向各层殿堂主发起了挑战！");
        while (context.isGameThreadRunning() && level <= hero.getMaxMazeLev()) {
            if (context.isPause()) {
                continue;
            }
            step++;
            if (monsters.isEmpty()) {
                context.addMessage("恭喜你擊敗關卡主！");
                if (random.nextBoolean()) {
                    hero.setLockBox(hero.getLockBox() + 1);
                    context.addMessage("获得了一个带锁的宝箱");
                }
                if ((level - lastSave) > 15) {
                    lastSave = level;
                    context.save();
                }
                step = 0;
                level++;
                long point = 2 + level / 10 + random.nextLong(hero.getMaxMazeLev() * 2 + 1) / 300;
                if (point > 100) {
                    point = 100;
                }
                context.addMessage(hero.getFormatName() + "进入了" + level + "层迷宫， 获得了<font color=\"#FF8C00\">" + point + "</font>点数奖励");
                hero.addPoint(point);
                hero.addHp(random.nextLong(hero.getUpperHp() / 100 + 1) + 10);
                context.addMessage("-------------------");
                monsters = PalaceMonster.getPalaceMonsters(hero, this);
            } else if (random.nextLong(1000) > 983 && random.nextLong(hero.getAgility()) > random.nextLong(6971)) {
                long mate = random.nextLong(level * 300 + 1) + random.nextLong(hero.getAgility() / 1000 + 100) + 1000;
                context.addMessage(hero.getFormatName() + "找到了一个宝箱， 获得了<font color=\"#FF8C00\">" + mate + "</font>材料");
                if (random.nextInt(1000) > 997) {
                    context.addMessage("获得了一个带锁的宝箱");
                    hero.setLockBox(hero.getLockBox() + 1);
                }
                hero.addMaterial(mate);
                context.addMessage("-------------------");
            } else if (hero.getHp() < hero.getUpperHp() && random.nextLong(1000) > 985) {
                long hel = random.nextLong(hero.getUpperHp() / 10 + 1) + random.nextLong(hero.getPower() / 500);
                if (hel > hero.getUpperHp() / 2) {
                    hel = random.nextLong(hero.getUpperHp() / 2 + 1) + 1;
                }
                context.addMessage(hero.getFormatName() + "休息了一会，恢复了<font color=\"#556B2F\">" + hel + "</font>点HP");
                hero.addHp(hel);
                context.addMessage("-------------------");
            } else {
                Monster monster = monsters.pop();
                monster.setMazeLev(level);
                String msg = hero.getFormatName() + "遇到了" + monster.getFormatName();
                context.addMessage(msg);
                monster.addBattleDesc(msg);
                if (monster instanceof PalaceMonster.PalaceBoss) {
                    context.addMessage(((PalaceMonster.PalaceBoss) monster).getHello());
                    monster.addBattleDesc(((PalaceMonster.PalaceBoss) monster).getHello());
                }
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
                                String atkmsg = hero.getFormatName() + "攻击了" + monster.getFormatName() + "，造成了<font color=\"red\">" + hero.getAttackValue() + "</font>点伤害。";
                                context.addMessage(atkmsg);
                                monster.addBattleDesc(atkmsg);
                            }
                        }
                    } else {
                        skill = hero.useDefendSkill(monster);
                        if (skill != null) {
                            isJump = skill.release(monster);
                        } else {
                            long harm = monster.getAtk() - hero.getDefenseValue();
                            if (harm <= 0) {
                                harm = random.nextLong(hero.getMaterial() + 1);
                            }
                            if (hero.getRandom().nextInt(100) > monster.getHitRate()) {
                                harm = random.nextLong(level + 1);
                                String s = monster.getFormatName() + "攻击打偏了";
                                context.addMessage(s);
                                monster.addBattleDesc(s);
                            }
                            if (harm >= hero.getHp()) {
                                Skill sy = SkillFactory.getSkill("瞬间移动", hero, context.getSkillDialog());
                                if (sy.isActive() && sy.perform()) {
                                    isJump = sy.release(monster);
                                    continue;
                                } else {
                                    sy = SkillFactory.getSkill("反杀", hero, context.getSkillDialog());
                                    if (sy.isActive() && sy.perform()) {
                                        isJump = sy.release(monster);
                                        continue;
                                    }
                                }
                            }
                            hero.addHp(-harm);
                            if (hero.isParry()) {
                                String parrymsg = hero.getFormatName() + "成功格挡一次攻击，减少了当前受到的伤害！";
                                context.addMessage(parrymsg);
                                monster.addBattleDesc(parrymsg);
                            }
                            String defmsg = monster.getFormatName() + "攻击了" + hero.getFormatName() + "，造成了<font color=\"red\">" + harm + "</font>点伤害。";
                            context.addMessage(defmsg);
                            monster.addBattleDesc(defmsg);
                        }
                    }
                    if (monster.getHp() <= 0) {
                        streaking++;
                        if (streaking >= 100) {
                            Achievement.unbeaten.enable(hero);
                        }
                        String defeatmsg = hero.getFormatName() + "击败了" + monster.getFormatName() + "， 获得了<font color=\"blue\">" + monster.getMaterial() + "</font>份锻造材料。";
                        context.addMessage(defeatmsg);
                        monster.addBattleDesc(defeatmsg);
                        hero.addMaterial(monster.getMaterial());
                        monster.setDefeat(true);
                        StringBuilder items = new StringBuilder();
                        if (monster.getItems() != null) {
                            for (ItemName item : monster.getItems()) {
                                Item i = Item.buildItem(hero, this, monster);
                                if (i != null) {
                                    i.save();
                                    items.append(item.name()).append(" ");
                                }
                            }
                            String str = items.toString();
                            if (StringUtils.isNotEmpty(str)) {
                                String itemmsg = hero.getFormatName() + "获得了:" + str;
                                context.addMessage(itemmsg);
                                monster.addBattleDesc(itemmsg);
                            }
                        }
                        context.getMonsterBook().addMonster(monster);
                        break;
                    } else if (hero.getHp() <= 0) {
                        Skill notDieSkill = SkillFactory.getSkill("不死之身", hero, context.getSkillDialog());
                        if (notDieSkill.isActive() && notDieSkill.perform()) {
                            notDieSkill.release(monster);
                        } else {
                            streaking = 0;
                            step = 0;
                            String defeatedmsg = hero.getFormatName() + "被" + monster.getFormatName() + "打败了，回到迷宫第一层。";
                            context.addMessage(defeatedmsg);
                            monster.addBattleDesc(defeatedmsg);
                            if (level > 25) {
                                context.save();
                            }
                            this.level = 1;
                            hero.restore();
                            monster.setDefeat(false);
                            lastSave = level;
                            context.getMonsterBook().addMonster(monster);
                            break;
                        }
                    }
                    if (isJump) {
                        continue;
                    }
                    atk = !atk;
                    try {
                        Thread.sleep(context.getRefreshInfoSpeed());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                context.addMessage("-----------------------------");
            }
            try {
                Thread.sleep(context.getRefreshInfoSpeed());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void addMessage(String message) {
        context.addMessage(message);
    }
}

