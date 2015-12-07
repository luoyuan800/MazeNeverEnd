package cn.gavin.skill.system;

import cn.gavin.Achievement;
import cn.gavin.Hero;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.maze.Maze;
import cn.gavin.monster.Monster;
import cn.gavin.palace.nskill.AtkSkill;
import cn.gavin.skill.Skill;
import cn.gavin.skill.SkillDialog;
import cn.gavin.skill.expression.DescExpression;
import cn.gavin.skill.expression.EnableExpression;
import cn.gavin.skill.expression.UseExpression;
import cn.gavin.skill.type.AttackSkill;
import cn.gavin.skill.type.ItemAttackSkill;
import cn.gavin.skill.type.PropertySkill;
import cn.gavin.utils.StringUtils;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 12/4/2015.
 */
public class ItemSkill {
    public static Skill getSkill(String name, final Hero hero, final SkillDialog dialog) {
        Skill skill = null;
        if ("星爆".equalsIgnoreCase(name)) {
            final ItemAttackSkill iskll = new ItemAttackSkill();
            skill = iskll;
            skill.setName("星爆");
            skill.setHero(hero);
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return Achievement.dragon.isEnable();
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    return "有3%的几率释放：<br>亮瞎敌人的双眼：敌人生命值变为一个随机数（不大于当前生命值）。";
                }
            });
            skill.setRelease(new UseExpression() {
                @Override
                public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                    Long hp = hero.getRandom().nextLong(monster.getHp());
                    String skillMsg = hero.getFormatName() + "使用了技能"  + skill.getName() + "," + monster.getFormatName() + "的HP变成了" + StringUtils.formatNumber(hp);
                    skill.addMessage(skillMsg);
                    monster.addBattleSkillDesc(skillMsg);
                    monster.addHp(-monster.getHp() + hp);
                    return false;
                }

            });
            skill.setLevelUp(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {

                    return true;
                }
            });
            if (!skill.load()) {
                skill.setProbability(3f);
            }
        }else if ("冰爆".equalsIgnoreCase(name)) {
            final ItemAttackSkill iskll = new ItemAttackSkill();
            skill = iskll;
            skill.setName("冰爆");
            skill.setHero(hero);
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return Achievement.dragon.isEnable();
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    return "有3%的几率释放：<br>冻住敌人随机回合。";
                }
            });
            skill.setRelease(new UseExpression() {
                @Override
                public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                    Long n = hero.getRandom().nextLong(hero.getDeathCount() + 5);
                    String skillMsg = hero.getFormatName() + "使用了技能"  + skill.getName() + "," + monster.getFormatName() + "被冰封住了" +  n + "个回合";
                    skill.addMessage(skillMsg);
                    monster.addBattleSkillDesc(skillMsg);
                    monster.setHoldTurn(n);
                    monster.setHold(true);
                    return false;
                }

            });
            skill.setLevelUp(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {

                    return true;
                }
            });
            if (!skill.load()) {
                skill.setProbability(3f);
            }
        }else if ("令风".equalsIgnoreCase(name)) {
            final ItemAttackSkill iskll = new ItemAttackSkill();
            skill = iskll;
            skill.setName("令风");
            skill.setHero(hero);
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return Achievement.dragon.isEnable();
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    return "有3%的几率释放：<br>秒杀土属性的敌人。";
                }
            });
            skill.setRelease(new UseExpression() {
                @Override
                public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                    String skillMsg = hero.getFormatName() + "使用了技能"  + skill.getName() + ",秒杀了" + monster.getFormatName();
                    skill.addMessage(skillMsg);
                    monster.addBattleSkillDesc(skillMsg);
                    monster.addHp(-monster.getHp());
                    return false;
                }

            });
            skill.setLevelUp(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {

                    return true;
                }
            });
            if (!skill.load()) {
                skill.setProbability(3f);
            }
        }
        return skill;
    }
}
