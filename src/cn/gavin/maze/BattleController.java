package cn.gavin.maze;

import java.util.List;

import cn.gavin.Hero;
import cn.gavin.activity.BaseContext;
import cn.gavin.forge.Item;
import cn.gavin.forge.list.ItemName;
import cn.gavin.monster.Monster;
import cn.gavin.palace.Base;
import cn.gavin.palace.nskill.NSkill;
import cn.gavin.pet.Pet;
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
        if (!monster.isHold()) {
            List<Pet> pets = hero.getPets();
            for (Pet pet : pets) {
                if (pet != null && pet.getHp() > 0 && !pet.getType().equals("蛋") && pet.dan()) {
                    pet.setContext(context);
                    long harm = monster.getAtk() - pet.getDef();
                    String petMsg = pet.getFormatName() + "舍身救主，挡下了这次攻击伤害（<font color=\"red\">" + harm + "</font>点)!";
                    addMessage(context, petMsg);
                    monster.addBattleDesc(petMsg);
                    if (harm < 0) harm = 0;
                    pet.addHp(-harm);
                    if (pet.getHp() <= 0) {
                        String petDie = pet.getFormatName() + "牺牲了！";
                        addMessage(context, petDie);
                        monster.addBattleDesc(petDie);
                    }
                    return false;
                }
            }

            if (skill != null && !monster.isSilent(random)) {
                isJump = skill.release(monster);
            } else if (monster.getName().contains("龙") && SkillFactory.getSkill("龙裔", hero, context.getSkillDialog()).isActive()) {
                addMessage(context, hero.getFormatName() + "激发龙裔效果，免疫龙系怪物的伤害！");
            } else {
                long harm = monster.getAtk() - hero.getDefenseValue();
                //相克，伤害1.5倍
                if (monster.getElement().restriction(hero.getElement())) {
                    harm *= 1.5;
                } else if (hero.getElement().restriction(monster.getElement())) {
                    //被克，伤害减低一半
                    harm /= 2;
                }
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

                if (random.nextLong(100) + hero.getDodgeRate() + random.nextLong(hero.getAgility() + 1) / 5000 > 97 + random.nextInt(100) + random.nextLong(hero.getPower() + 1) / 5000) {
                    String dodgeMsg = hero.getFormatName() + "身手敏捷的闪开了" + monster.getFormatName() + "的攻击";
                    addMessage(context, dodgeMsg);
                    monster.addBattleDesc(dodgeMsg);
                } else {
                    if (hero.isParry()) {
                        String parrymsg = hero.getFormatName() + "成功进行了一次格挡，减少了受到的伤害！";
                        addMessage(context, parrymsg);
                        monster.addBattleDesc(parrymsg);
                    }
                    hero.addHp(-harm);
                    String defmsg = monster.getFormatName() + "攻击了" + hero.getFormatName() +
                            "，造成了<font color=\"red\">" + StringUtils.formatNumber(harm) + "</font>点伤害。";
                    addMessage(context, defmsg);
                    monster.addBattleDesc(defmsg);
                }
            }
        }
        return isJump;
    }

    public static boolean heroAtk(BaseContext context, Hero hero, Monster monster) {
        Skill skill;
        boolean isJump;
        skill = hero.useAttackSkill(monster);
        isJump = false;
        List<Pet> pets = hero.getPets();
        for (Pet pet : pets) {
            if (pet != null && pet.getHp() > 0 && !"蛋".equals(pet.getType())) {
                if (pet.gon()) {
                    NSkill petSkill = pet.getAtkSkill();
                    long petHarm = 0;
                    if (petSkill != null) {
                        String petSkillMsg = pet.getFormatName() + "使用了技能" + petSkill.getName();
                        addMessage(context, petSkillMsg);
                        monster.addBattleDesc(petSkillMsg);
                        Base target = new Base() {
                        };
                        target.setDef(0l);
                        target.setAtk(monster.getAtk());
                        target.setHp(monster.getHp());
                        target.setElement(monster.getElement());
                        petHarm = petSkill.getHarm(target);
                    } else {
                        petHarm = pet.getAtk();
                    }
                    String petAtk = pet.getFormatName() + "攻击了" + monster.getFormatName() + ",造成了"
                            + StringUtils.formatNumber(petHarm) + "点伤害";
                    monster.addHp(petHarm);
                    addMessage(context, petAtk);
                    monster.addBattleDesc(petAtk);
                    break;
                }
            }
        }
        if (skill != null) {
            if (!monster.isSilent(hero.getRandom())) {
                isJump = skill.release(monster);
            } else {
                addMessage(context, hero.getFormatName() + "想要使用技能" + skill.getName());
                addMessage(context, monster.getFormatName() + "打断了" + hero.getFormatName() + "的技能");
            }
        } else {
            if (hero.getHp() < hero.getUpperHp()) {
                skill = hero.useRestoreSkill();
            }
            if (skill != null) {
                isJump = skill.release(monster);
            } else {
                Long attackValue = hero.getAttackValue();
                //被克，伤害1.5倍
                if (hero.getElement().restriction(monster.getElement())) {
                    attackValue += attackValue / 2;
                } else if (monster.getElement().restriction(hero.getElement())) {
                    //相克，伤害减低一半
                    attackValue -= attackValue / 2;
                }
                monster.addHp(-attackValue);
                if (hero.isHit()) {
                    String hitMsg = hero.getFormatName() + "使出了暴击，攻击伤害提高了。";
                    addMessage(context, hitMsg);
                    monster.addBattleDesc(hitMsg);
                }
                String atkmsg = hero.getFormatName() + "攻击了" + monster.getFormatName() + "，造成了<font color=\"#000080\">" + StringUtils.formatNumber(attackValue) + "</font>点伤害。";
                addMessage(context, atkmsg);
                monster.addBattleDesc(atkmsg);
            }
        }
        return isJump;
    }

    public static void addMessage(BaseContext context, String msg) {
        if (context != null) {
            context.addMessage(msg);
        }
    }


    public static boolean battle(Hero hero, Monster monster, Random random, Maze maze, BaseContext context) {
        int count = 0;
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
                if (count == 20) {
                    String s = "阿西巴，这怪怎么打不死的？" + hero.getFormatName() + "小声的嘟哝着。";
                    addMessage(context, s);
                    monster.addBattleDesc(s);
                }
                isJump = BattleController.heroAtk(context, hero, monster);
            } else {
                if (count == 20) {
                    String s = "阿西巴，这家伙怎打不死的？" + monster.getFormatName() + "小声的嘟哝着。";
                    addMessage(context, s);
                    monster.addBattleDesc(s);
                }
                isJump = BattleController.heroDef(context, hero, monster, random);
            }
            atk = !atk;
            count++;
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
            String defeatmsg = hero.getFormatName() + "击败了" + monster.getFormatName() + "， 获得了<font color=\"blue\">" + monster.getMaterial() + "</font>份锻造点数。";
            addMessage(context, defeatmsg);
            monster.addBattleDesc(defeatmsg);
            hero.addMaterial(monster.getMaterial());
            monster.setDefeat(true);
            StringBuilder items = new StringBuilder();
            if (monster.getItems() != null) {
                for (ItemName item : monster.getItems()) {
                    Item i = Item.buildItem(hero, maze, monster);
                    if (i != null) {
                        i.save(null);
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
        } else if (hero.getHp() <= 0) {
            Skill notDieSkill = SkillFactory.getSkill("不死之身", hero, context.getSkillDialog());
            if (notDieSkill.isActive() && notDieSkill.perform()) {
                isJump = notDieSkill.release(monster);
            } else {
                monster.setDefeat(false);
                isJump = false;
            }
        } else {
            isJump = true;
        }
        return isJump;
    }
}
