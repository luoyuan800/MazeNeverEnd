package cn.gavin.maze;

import cn.gavin.Element;
import cn.gavin.Hero;
import cn.gavin.activity.BaseContext;
import cn.gavin.gift.Gift;
import cn.gavin.monster.Monster;
import cn.gavin.palace.Base;
import cn.gavin.palace.nskill.NSkill;
import cn.gavin.pet.Pet;
import cn.gavin.skill.Skill;
import cn.gavin.skill.SkillFactory;
import cn.gavin.story.NPC;
import cn.gavin.utils.Random;
import cn.gavin.utils.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 10/14/2015.
 */
public class NPCBattleController {
    public static boolean heroDef(BaseContext context, Hero hero, Hero monster) {
        Random random = hero instanceof NPC ? monster.getRandom() : hero.getRandom();
        if (hero.getGift() == Gift.ElementReject && hero.getElement() != null && monster.getElement() == hero.getRejectElement() && random.nextInt(100) < 80) {
            String rejectmsg = hero.getFormatName() + "的" + hero.getGift().getName() + "天赋免疫了" + monster.getFormatName() + "的攻击";
            addMessage(context, rejectmsg);
            addBattleMsg(rejectmsg);
            return false;
        } else {
            Skill skill = hero.useDefSkill();
            boolean isJump = false;
            if (!monster.isHold()) {
                if (!(hero instanceof NPC)) {
                    List<Pet> pets = new ArrayList<Pet>(hero.getPets());
                    for (Pet pet : pets) {
                        if (pet != null && pet.getHp() > 0 && !pet.getType().equals("蛋") && pet.dan()) {
                            pet.setContext(context);
                            long harm = monster.getAttackValue() - pet.getDef();
                            if (harm < 0) {
                                harm = random.nextLong(hero.getMaxMazeLev());
                            }
                            String petMsg = pet.getFormatName() + "舍身救主，挡下了一次攻击伤害（<font color=\"red\">" + StringUtils.formatNumber(harm) + "</font>点)!";
                            addMessage(context, petMsg);
                            addBattleMsg(petMsg);
                            if (harm < 0) harm = 0;
                            pet.addHp(-harm);
                            if (pet.getHp() <= 0) {
                                String petDie = pet.getFormatName() + "牺牲了！";
                                addMessage(context, petDie);
                                addBattleMsg(petDie);
                            }
                            return false;
                        }
                    }
                }
                boolean skillJump = false;
                if (skill != null && monster.isSilent(random)) {
                    addMessage(context, hero.getFormatName() + "想要使用技能" + skill.getName());
                    addMessage(context, monster.getFormatName() + "打断了" + hero.getFormatName() + "的技能");
                    skillJump = true;
                }
                if (skill != null && !skillJump) {
                    Monster monster1 = monster.formatAsMonster();
                    isJump = skill.release(monster1);
                    monster.setHp(monster1.getHp());
                    monster.setHold((int) monster1.getHoldTurn());
                } else if (!(hero instanceof NPC) && monster.getName().contains("龙") && SkillFactory.getSkill("龙裔", hero).isActive()) {
                    addMessage(context, hero.getFormatName() + "激发龙裔效果，免疫龙系怪物的伤害！");
                } else {
                    long harm = monster.getAttackValue() - hero.getDefenseValue();
                    //相克，伤害1.5倍
                    if (monster.getElement().restriction(hero.getElement())) {
                        harm *= 1.5;
                    } else if (hero.getElement().restriction(monster.getElement())) {
                        //被克，伤害减低一半
                        harm /= 2;
                    }
                    boolean b = hero.getRandom().nextInt(100) < monster.getHitRate();
                    if (harm <= 0 || b) {
                        harm = random.nextLong(hero.getMaxMazeLev());
                    }
                    if (b) {
                        harm = random.nextLong(hero.getMaxMazeLev() + 1);
                        String s = monster.getFormatName() + "攻击打偏了";
                        addMessage(context, s);
                        addBattleMsg(s);
                    }

                    if (!(hero instanceof NPC) && harm >= hero.getHp()) {
                        Skill sy = SkillFactory.getSkill("瞬间移动", hero);
                        if (sy.isActive() && sy.perform()) {
                            return sy.release(new Monster());
                        } else {
                            sy = hero.getPropertySkill("反杀");
                            if (sy != null && sy.isActive() && sy.perform()) {
                                monster.setHp(0);
                                return sy.release(new Monster());

                            }
                        }
                    }

                    if (random.nextLong(100) + hero.getDodgeRate() + random.nextLong(hero.getAgility() + 1) / 5000 > 97 + random.nextInt(100) + random.nextLong(hero.getPower() + 1) / 5000) {
                        String dodgeMsg = hero.getFormatName() + "身手敏捷的闪开了" + monster.getFormatName() + "的攻击";
                        addMessage(context, dodgeMsg);
                        addBattleMsg(dodgeMsg);
                    } else {
                        if (hero.isParry()) {
                            String parrymsg = hero.getFormatName() + "成功进行了一次格挡，减少了受到的伤害！";
                            addMessage(context, parrymsg);
                            addBattleMsg(parrymsg);
                        }
                        hero.addHp(-harm);
                        String defmsg = monster.getFormatName() + "攻击了" + hero.getFormatName() +
                                "，造成了<font color=\"red\">" + StringUtils.formatNumber(harm) + "</font>点伤害。";
                        addMessage(context, defmsg);
                        addBattleMsg(defmsg);
                    }
                }
            }
            return isJump;
        }
    }

    private static StringBuilder battleMsg;

    public synchronized static String getLastBattle() {
        String result = "";
        if (battleMsg != null) {
            result = battleMsg.toString();
        }
        battleMsg = null;
        return result;
    }

    private static void addBattleMsg(String msg) {
        if (battleMsg == null) {
            battleMsg = new StringBuilder();
        }
        battleMsg.append("<br>").append(msg);
    }

    public static boolean heroAtk(BaseContext context, Hero hero, Hero target) {
        Skill skill;
        boolean isJump;
        skill = hero.useAtkSkill();
        isJump = false;
        List<Pet> pets = hero instanceof NPC ? Collections.<Pet>emptyList() : new ArrayList<Pet>(hero.getPets());
        for (Pet pet : pets) {
            if (pet != null && pet.getHp() > 0 && !"蛋".equals(pet.getType())) {
                if (pet.gon()) {
                    if (!target.isPetSub()) {
                        NSkill petSkill = pet.getAtkSkill();
                        long petHarm = 0;
                        if (petSkill != null) {
                            String petSkillMsg = pet.getFormatName() + "使用了技能" + petSkill.getName();
                            addMessage(context, petSkillMsg);
                            addBattleMsg(petSkillMsg);
                            Base base = new Base() {
                            };
                            base.setDef(0l);
                            base.setAtk(target.getAttackValue());
                            base.setHp(target.getHp());
                            base.setElement(target.getElement());
                            petHarm = petSkill.getHarm(base);
                        } else {
                            petHarm = pet.getAtk();
                        }
                        String petAtk = pet.getFormatName() + "攻击了" + target.getFormatName() + ",造成了"
                                + StringUtils.formatNumber(petHarm) + "点伤害";
                        target.addHp(-petHarm);
                        addMessage(context, petAtk);
                        addBattleMsg(petAtk);
                    } else {
                        String petsub = target.getFormatName() + "吓得" + pet.getFormatName() + "不敢出手。";
                        addMessage(context, petsub);
                        addBattleMsg(petsub);
                    }
                    break;
                }
            }
        }
        if (skill != null) {
            if (!target.isSilent(hero.getRandom())) {
                Monster monster = target.formatAsMonster();
                isJump = skill.release(monster);
                target.setHp(monster.getHp());
            } else {
                addMessage(context, hero.getFormatName() + "想要使用技能" + skill.getName());
                addMessage(context, target.getFormatName() + "打断了" + hero.getFormatName() + "的技能");
            }
        } else {
            isJump = heroDef(context, target, hero);
        }
        return isJump;
    }

    public static void addMessage(BaseContext context, String msg) {
        if (context != null) {
            context.addMessage(msg);
        }
    }


    public synchronized static boolean battle(Hero hero, NPC monster, Random random, Maze maze, BaseContext context) {
        if (battleMsg != null) {
            battleMsg = new StringBuilder();
        }
        int count = 0;
        String msg = hero.getFormatName() + "在第" + maze.getLev() + "层遇到了" + monster.getFormatName();
        addMessage(context, msg);
        addBattleMsg(msg);

        boolean atk = random.nextLong(hero.getAgility() + 100) > monster.getHp() / 2 || random.nextBoolean();
        boolean isJump = false;
        if (hero.getGift() == Gift.ElementReject && hero.getElement() == Element.无 && hero.getRejectElement() == monster.getElement() && random.nextInt(100) < 55) {
            String rejectString = hero.getFormatName() + "因为元素抗拒天赋秒杀了" + monster.getFormatName();
            addMessage(context, rejectString);
            addBattleMsg(rejectString);
            monster.addHp(-monster.getHp());
        }
        while (!isJump && monster.getHp() > 0 && hero.getHp() > 0) {
            if (context.isPause()) {
                continue;
            }

            if (atk) {
                if (count == 20) {
                    String s = "阿西巴，这怪怎么打不死的？" + hero.getFormatName() + "小声的嘟哝着。";
                    addMessage(context, s);
                    addBattleMsg(s);
                } else if (count != 0 && count % 50 == 0) {
                    String s = "由于战斗时间过长，" + hero.getFormatName() + "和" + monster.getFormatName() + "决定玩一局筛子游戏，谁的筛子数大，谁的当前生命值减半。";
                    addMessage(context, s);
                    addBattleMsg(s);
                    int i = random.nextInt(6) + 1;
                    int j = random.nextInt(6) + 1;
                    s = hero.getFormatName() + "抛出筛子，点数为：" + i;
                    addMessage(context, s);
                    addBattleMsg(s);
                    s = monster.getFormatName() + "抛出筛子，点数为：" + j;
                    addMessage(context, s);
                    addBattleMsg(s);
                    if (i > j) {
                        hero.addHp(-hero.getHp() / 2);
                        s = hero.getFormatName() + "HP减半了";
                        addMessage(context, s);
                        addBattleMsg(s);
                    } else {
                        monster.addHp(-monster.getHp() / 2);
                        s = monster.getFormatName() + "HP减半了";
                        addMessage(context, s);
                        addBattleMsg(s);
                    }
                }
                isJump = heroAtk(context, hero, monster);
            } else {
                if (count == 20) {
                    String s = "阿西巴，这家伙怎打不死的？" + monster.getFormatName() + "小声的嘟哝着。";
                    addMessage(context, s);
                    addBattleMsg(s);
                }
                isJump = NPCBattleController.heroAtk(context, monster, hero);
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
            if (monster.getElement() == hero.getElement()) {
                Skill ysxs = hero.getPropertySkill("元素吸收");
                if (ysxs != null && (hero instanceof NPC || ysxs.isActive())) {
                    String skillMsg = hero.getFormatName() + "因为技能<元素吸收>恢复了生命。";
                    context.addMessage(skillMsg);
                    addBattleMsg(skillMsg);
                    hero.addHp(hero.getUpperAtk() / 10);
                }
            }
            String defeatmsg = hero.getFormatName() + "击败了" + monster.getFormatName() +
                    "， 获得了<font color=\"blue\">" + monster.getMaterial() + "</font>份锻造点数。";
            addMessage(context, defeatmsg);
            addBattleMsg(defeatmsg);
            hero.addMaterial(monster.getLev() * 3);
            monster.setDefeat(true);
            monster.defeat();
            return true;
        } else if (hero.getHp() <= 0) {
            if (!atk && !isJump) {//如果是在自己的攻击回合挂掉的（莫名其妙挂掉的补充)
                String unknownDie = hero.getFormatName() + "在攻击的时候用力过头，摔到楼下去了……";
                addMessage(context, unknownDie);
                addBattleMsg(unknownDie);
            }
            Skill notDieSkill = SkillFactory.getSkill("不死之身", hero);
            if (notDieSkill.isActive() && notDieSkill.perform()) {
                return notDieSkill.release(new Monster());
            } else {
                monster.setDefeat(false);
                monster.defeat();
                return false;
            }
        } else if (hero.getHp() > 0 && monster.getHp() > 0) {
            String nmsg = hero.getFormatName() + "用魅力征服了" + monster.getFormatName() + "。两人握手言和，不进行你死我活的战斗了。";
            addBattleMsg(nmsg);
            addMessage(context, nmsg);
        }
        return true;
    }
}
