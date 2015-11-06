package cn.gavin.skill.system;

import android.content.Context;
import cn.gavin.Hero;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.maze.Maze;
import cn.gavin.monster.Monster;
import cn.gavin.pet.Pet;
import cn.gavin.skill.Skill;
import cn.gavin.skill.SkillDialog;
import cn.gavin.skill.SkillFactory;
import cn.gavin.skill.expression.DescExpression;
import cn.gavin.skill.expression.EnableExpression;
import cn.gavin.skill.expression.UseExpression;
import cn.gavin.skill.type.AttackSkill;
import cn.gavin.skill.type.PropertySkill;

/**
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED
 * Created by luoyuan on 11/6/15.
 */
public class PetSkill extends SkillLayout {
    public PetSkill(Context context) {
        super(context);
    }

    public static Skill getSkill(String name, final Hero hero, final SkillDialog dialog) {
        Skill skill = null;
        if ("驯兽师".equalsIgnoreCase(name)) {
            final PropertySkill iskll = new PropertySkill();
            skill = iskll;
            skill.setName("驯兽师");
            skill.setHero(hero);
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return !SkillFactory.getSkill("培育家",hero,dialog).isActive();
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("激活后增加捕获宠物的几率。");
                    return builder.toString();
                }
            });
            skill.setRelease(new UseExpression() {
                @Override
                public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {

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
                iskll.setPetRate(-0.5f);
            }
        }else if ("培育家".equalsIgnoreCase(name)) {
            final PropertySkill iskll = new PropertySkill();
            skill = iskll;
            skill.setName("培育家");
            skill.setHero(hero);
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return !SkillFactory.getSkill("驯兽师",hero,dialog).isActive();
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("激活后增加获得宠物蛋的几率。");
                    return builder.toString();
                }
            });
            skill.setRelease(new UseExpression() {
                @Override
                public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {

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
                iskll.setPetRate(200f);
            }
        }else if ("捕捉术".equalsIgnoreCase(name)) {
            final AttackSkill iskll = new AttackSkill();
            skill = iskll;
            skill.setName("捕捉术");
            skill.setHero(hero);
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return SkillFactory.getSkill("驯兽师",hero,dialog).isActive();
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("使用这个技能击败敌人的时候100%捕捉成功。<br>").append(iskll.getProbability()).append("%几率释放。");
                    return builder.toString();
                }
            });
            skill.setRelease(new UseExpression() {
                @Override
                public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                    long harm = hero.getAttackValue();
                    monster.addHp(harm);
                    if(monster.getHp() <= 0){
                        Pet pet = Pet.cPet(monster, hero.getRandom());
                        if(pet!=null){
                            iskll.addMessage(hero.getFormatName() + "捕捉到了" + pet.getFormatName());
                        }
                    }
                    return false;
                }

            });
            skill.setLevelUp(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    if(skill.getProbability() < 15){
                        skill.setProbability(skill.getProbability() + 0.6f);
                    }
                    return true;
                }
            });
            if (!skill.load()) {
                skill.setProbability(1.0f);
            }
        }else if ("催化剂".equalsIgnoreCase(name)) {
            final PropertySkill iskll = new PropertySkill();
            skill = iskll;
            skill.setName("催化剂");
            skill.setHero(hero);
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return SkillFactory.getSkill("培育家",hero,dialog).isActive();
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("激活后增加孵蛋的速度。");
                    return builder.toString();
                }
            });
            skill.setRelease(new UseExpression() {
                @Override
                public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {

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
                iskll.setEggStep(2f);
            }
        }else if ("霸气".equalsIgnoreCase(name)) {
            final PropertySkill iskll = new PropertySkill();
            skill = iskll;
            skill.setName("霸气");
            skill.setHero(hero);
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return SkillFactory.getSkill("捕获术",hero,dialog).isActive();
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("激活后捕获的宠物成长值翻倍。");
                    return builder.toString();
                }
            });
            skill.setRelease(new UseExpression() {
                @Override
                public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {

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

            }
        }else if ("爱心".equalsIgnoreCase(name)) {
            final PropertySkill iskll = new PropertySkill();
            skill = iskll;
            skill.setName("爱心");
            skill.setHero(hero);
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return SkillFactory.getSkill("催化剂",hero,dialog).isActive();
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("激活后减少宠物死亡亲密度损耗。");
                    return builder.toString();
                }
            });
            skill.setRelease(new UseExpression() {
                @Override
                public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {

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

            }
        }else if ("群殴".equalsIgnoreCase(name)) {
            final PropertySkill iskll = new PropertySkill();
            skill = iskll;
            skill.setName("群殴");
            skill.setHero(hero);
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return SkillFactory.getSkill("霸气",hero,dialog).isActive();
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("命令队伍中的所有宠物攻击敌人。");
                    return builder.toString();
                }
            });
            skill.setRelease(new UseExpression() {
                @Override
                public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                    //TODO
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

            }
        }
        return skill;
    }
}
