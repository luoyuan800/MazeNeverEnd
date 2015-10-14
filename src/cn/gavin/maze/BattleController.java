package cn.gavin.maze;

import cn.gavin.Hero;
import cn.gavin.activity.BaseContext;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.forge.Item;
import cn.gavin.forge.list.ItemName;
import cn.gavin.monster.Monster;
import cn.gavin.monster.MonsterBook;
import cn.gavin.skill.Skill;
import cn.gavin.skill.SkillFactory;
import cn.gavin.utils.Random;
import cn.gavin.utils.StringUtils;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 10/14/2015.
 */
public class BattleController {
    public static boolean heroDef(BaseContext context, Hero hero, Monster monster, Random random) {
        Skill skill = hero.useDefendSkill(monster);
        boolean isJump = false;
        if (skill != null) {
            isJump = skill.release(monster);
        } else {
            long harm = monster.getAtk() - hero.getDefenseValue();
            if (harm <= 0 || hero.getRandom().nextInt(100) > monster.getHitRate()) {
                harm = random.nextLong(hero.getMaxMazeLev());
            }
            if (hero.getRandom().nextInt(100) > monster.getHitRate()) {
                harm = random.nextLong(hero.getMaxMazeLev() + 1);
                String s = monster.getFormatName() + "攻击打偏了";
                addMessage(context, s);
                monster.addBattleDesc(s);
            }
            if (harm >= hero.getHp()) {
                Skill sy = SkillFactory.getSkill("瞬间移动", hero, context.getSkillDialog());
                if (sy.isActive() && sy.perform()) {
                    return sy.release(monster);
                } else {
                    sy = SkillFactory.getSkill("反杀", hero, context.getSkillDialog());
                    if (sy.isActive() && sy.perform()) {
                        return sy.release(monster);
                    }
                }
            }
                hero.addHp(-harm);
                if (hero.isParry()) {
                    String parrymsg = hero.getFormatName() + "成功格挡一次攻击，减少了当前受到的伤害！";
                    addMessage(context, parrymsg);
                    monster.addBattleDesc(parrymsg);
                }
                String defmsg = monster.getFormatName() + "攻击了" + hero.getFormatName() + "，造成了<font color=\"red\">" + harm + "</font>点伤害。";
                addMessage(context, defmsg);
                monster.addBattleDesc(defmsg);
        }
        return isJump;
    }

    public static boolean heroAtk(BaseContext context, Hero hero, Monster monster) {
        Skill skill;
        boolean isJump;
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
                if (hero.isHit()) {
                    String hitMsg = hero.getFormatName() + "使出了暴击，攻击伤害提高了。";
                    addMessage(context, hitMsg);
                    monster.addBattleDesc(hitMsg);
                }
                monster.addHp(-(hero.getAttackValue()));
                String atkmsg = hero.getFormatName() + "攻击了" + monster.getFormatName() + "，造成了<font color=\"red\">" + hero.getAttackValue() + "</font>点伤害。";
                addMessage(context, atkmsg);
                monster.addBattleDesc(atkmsg);
            }
        }
        return isJump;
    }

    public static void addMessage(BaseContext context, String msg) {
        if (context != null && !context.isHideBattle()) {
            context.addMessage(msg);
        }
    }


    public static boolean battle(Hero hero, Monster monster, Random random, Maze maze, BaseContext context) {
        MonsterBook monsterBook = context.getMonsterBook();
        String msg = hero.getFormatName() + "遇到了" + monster.getFormatName();
        addMessage(context, msg);
        monster.addBattleDesc(msg);
        boolean atk = random.nextLong(hero.getAgility() + 100) > monster.getHp() / 2 || random.nextBoolean();
        boolean isJump = false;
        while (!isJump && monster.getHp() > 0 && hero.getHp() > 0) {
            if (context.isPause()) {
                continue;
            }
            if (atk) {
                isJump = BattleController.heroAtk(context, hero, monster);
            } else {
                isJump = BattleController.heroDef(context, hero, monster, random);
            }
            atk = !atk;
            try {
                Thread.sleep(context.getRefreshInfoSpeed());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (isJump) {
            return true;
        }
        if (monster.getHp() <= 0) {
            String defeatmsg = hero.getFormatName() + "击败了" + monster.getFormatName() + "， 获得了<font color=\"blue\">" + monster.getMaterial() + "</font>份锻造材料。";
            addMessage(context, defeatmsg);
            monster.addBattleDesc(defeatmsg);
            hero.addMaterial(monster.getMaterial());
            monster.setDefeat(true);
            StringBuilder items = new StringBuilder();
            if (monster.getItems() != null) {
                for (ItemName item : monster.getItems()) {
                    Item i = Item.buildItem(hero, maze, monster);
                    if (i != null) {
                        i.save();
                        items.append(item.name()).append(" ");
                    }
                }
                String str = items.toString();
                if (StringUtils.isNotEmpty(str)) {
                    String itemmsg = hero.getFormatName() + "获得了:" + str;
                    addMessage(context, itemmsg);
                    monster.addBattleDesc(itemmsg);
                }
            }
            isJump = true;
        } else {
            Skill notDieSkill = SkillFactory.getSkill("不死之身", hero, context.getSkillDialog());
            if (notDieSkill.isActive() && notDieSkill.perform()) {
                isJump = notDieSkill.release(monster);
            } else {
                monster.setDefeat(false);
                isJump = false;
            }
        }
        monsterBook.addMonster(monster);
        return isJump;
    }
}
