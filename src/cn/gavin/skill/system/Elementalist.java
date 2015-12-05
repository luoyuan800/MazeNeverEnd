package cn.gavin.skill.system;

import android.content.Context;

import cn.gavin.Element;
import cn.gavin.Hero;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.maze.Maze;
import cn.gavin.monster.Monster;
import cn.gavin.skill.Skill;
import cn.gavin.skill.SkillDialog;
import cn.gavin.skill.SkillFactory;
import cn.gavin.skill.expression.DescExpression;
import cn.gavin.skill.expression.EnableExpression;
import cn.gavin.skill.expression.UseExpression;
import cn.gavin.skill.type.AttackSkill;
import cn.gavin.skill.type.PropertySkill;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 10/29/2015.
 */
public class Elementalist extends SkillLayout {
    public Elementalist(Context context) {
        super(context);
    }

    @Override
    public void init() {

    }

    public static Skill getSkill(String name, final Hero hero, final SkillDialog dialog) {
        if(name.equals("元素变换")){
            AttackSkill skill = new AttackSkill();
            skill.setHero(hero);
            skill.setName("元素变换");
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return hero.getElement() != Element.无;
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    AttackSkill attackSkill = (AttackSkill) skill;
                    StringBuilder builder = new StringBuilder();
                    builder.append("<br>");
                    builder.append(attackSkill.getProbability()).append("%概率释放<br>");
                    return builder.toString();
                }
            });

            skill.setRelease(new UseExpression() {
                @Override
                public boolean release(Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                    long harm = hero.getAttackValue();
                    if(!hero.getElement().restriction(monster.getElement())){
                        skill.addMessage(hero.getFormatName() + "" + monster.getElement().getRestriction());
                    }
                    harm *= 1.5;
                    monster.addHp(-harm);
                    skill.addMessage(hero.getFormatName() + "" + monster.getFormatName() + "" + harm + "");
                    return false;
                }
            });
            if (!skill.load()) {
                skill.setProbability(2);
            }
            skill.setLevelUp(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    if (skill.getProbability() < 19) {
                        skill.setProbability(skill.getProbability() + 1.3f);
                    }

                    return false;
                }
            });
            return skill;
        }else if(name.equals("元素控制")){
            final AttackSkill iskill = new AttackSkill();
            iskill.setHero(hero);
            iskill.setName("元素控制");
            iskill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return hero.getElement() != Element.无 && SkillFactory.getSkill("元素变换", hero).isActive();
                }
            });
            iskill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    AttackSkill attackSkill = (AttackSkill) skill;
                    StringBuilder builder = new StringBuilder();
                    builder.append("<br>");
                    builder.append(attackSkill.getProbability()).append("%概率释放<br>");
                    return builder.toString();
                }
            });

            iskill.setRelease(new UseExpression() {
                @Override
                public boolean release(Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                    Element element = Element.values()[hero.getRandom().nextInt(Element.values().length)];
                    skill.addMessage(hero.getFormatName() + "" + element);
                    hero.setElement(element);
                    return false;
                }
            });
            if (!iskill.load()) {
                iskill.setProbability(3);
            }
            iskill.setLevelUp(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    if (skill.getProbability() < 15) {
                        skill.setProbability(skill.getProbability() + 1f);
                    }
                    return false;
                }
            });
            return iskill;
        }else if(name.equals("元素吸收")){
            final PropertySkill iskill = new PropertySkill();
            iskill.setHero(hero);
            iskill.setName("元素吸收");
            iskill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return hero.getElement() != Element.无 && SkillFactory.getSkill("元素变换", hero).isActive();
                }
            });
            iskill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("每击败一个属性和你相同的敌人，你的生命会得到恢复<br>");
                    return builder.toString();
                }
            });

            iskill.setRelease(new UseExpression() {
                @Override
                public boolean release(Hero hero, Monster monster, MainGameActivity context, Skill skill) {

                    return false;
                }
            });
            iskill.load();
            iskill.setLevelUp(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {

                    return false;
                }
            });
            return iskill;
        }else if(name.equals("")){
            final AttackSkill iskill = new AttackSkill();
            iskill.setHero(hero);
            iskill.setName("");
            iskill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return hero.getElement() != Element.无 && SkillFactory.getSkill("", hero).isActive();
                }
            });
            iskill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    AttackSkill attackSkill = (AttackSkill) skill;
                    StringBuilder builder = new StringBuilder();
                    builder.append("<br>");
                    builder.append(attackSkill.getProbability()).append("%概率释放<br>");
                    return builder.toString();
                }
            });

            iskill.setRelease(new UseExpression() {
                @Override
                public boolean release(Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                    long harm = hero.getAttackValue();
                    if(hero.getElement().restriction(monster.getElement())){
                        iskill.addMessage(hero.getFormatName() + "");
                        harm += monster.getAtk();
                    }
                    context.addMessage(hero.getFormatName() + "" + monster.getFormatName() + "" + harm + "");
                    monster.addHp(-harm);
                    return false;
                }
            });
            if (!iskill.load()) {
                iskill.setProbability(1);
            }
            iskill.setLevelUp(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    if (skill.getProbability() < 15) {
                        skill.setProbability(skill.getProbability() + 1f);
                    }
                    return false;
                }
            });
            return iskill;
        }
        return null;
    }
}
