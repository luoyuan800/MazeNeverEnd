package cn.gavin.skill;

import cn.gavin.Hero;
import cn.gavin.Maze;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.monster.Monster;
import cn.gavin.skill.type.AttackSkill;
import cn.gavin.skill.type.DefendSkill;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by luoyuan on 9/13/15.
 */
public class SkillFactory {
    private static Map<String, Skill> skillMap = new HashMap<String, Skill>();

    public static Skill getSkill(String name, Hero hero, final SkillDialog dialog) {
        Skill skill = skillMap.get(name);
        if (skill == null) {
            if (name.equals("勇者之击")) {
                AttackSkill attackSkill = new AttackSkill();
                skill = attackSkill;
                skill.setHero(hero);
                skill.setName("勇者之击");
                skill.setEnableExpression(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                        return hero.getSkillPoint() > 0 && !SkillFactory.getSkill("魔王天赋", hero, dialog).isActive();
                    }
                });
                skill.setDescription(new DescExpression() {
                    @Override
                    public String buildDescription(Skill skill) {
                        AttackSkill attackSkill = (AttackSkill) skill;
                        StringBuilder builder = new StringBuilder();
                        builder.append("<br>");
                        builder.append("勇者的基本技能，学会了才能踏上征途。<br>");
                        builder.append(attackSkill.getProbability()).append("%概率释放<br>");
                        builder.append("造成").append(attackSkill.getBaseHarm()).append("基本伤害。").append("0 - ").
                                append(attackSkill.getAdditionHarm()).append("随机伤害").append("<br>").
                                append("不可与魔王技能同时激活<br>");
                        return builder.toString();
                    }
                });
                skill.setPerform(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                        Random random = hero.getRandom();
                        return random.nextInt(100) > skill.getProbability();
                    }
                });
                skill.setRelease(new UseExpression() {
                    @Override
                    public boolean release(Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                        int harm = ((AttackSkill) skill).getBaseHarm() + hero.getRandom().nextInt((((AttackSkill) skill).getAdditionHarm()) + 1);
                        context.addMessage(hero.getName() + "使用了技能" + skill.getName() + "对" + monster.getFormatName() + "造成了" + harm + "点伤害");
                        monster.addHp(-harm);
                        return false;
                    }
                });
                if (!skill.load()) {
                    attackSkill.setBaseHarm(58);
                    attackSkill.setAdditionHarm(80);
                    attackSkill.setProbability(5);
                }
                skill.setLevelUp(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                        AttackSkill as = (AttackSkill) skill;
                        if (skill.getProbability() < 20) {
                            skill.setProbability(skill.getProbability() + 1);
                            as.setBaseHarm(as.getBaseHarm() + hero.getRandom().nextInt(hero.getDefenseValue() / 10 + 1));
                            as.setAdditionHarm(as.getAdditionHarm() * 3);
                            return true;
                        }
                        return false;
                    }
                });
            } else if (name.equals("魔王天赋")) {
                skill = new DefendSkill();
                skill.setName("魔王天赋");
                skill.setHero(hero);
                skill.setEnableExpression(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                        return hero.getSkillPoint() > 0 && !SkillFactory.getSkill("勇者之击", hero, dialog).isActive();
                    }
                });
                skill.setDescription(new DescExpression() {
                    @Override
                    public String buildDescription(Skill skill) {
                        DefendSkill defendSkill = (DefendSkill) skill;
                        StringBuilder builder = new StringBuilder();
                        builder.append("<br>").append("魔本天性，吾欲入魔，谁人可挡。<br>").
                                append("被攻击的时候有").append(defendSkill.getProbability()).
                                append("%概率将伤害转换为HP恢复。").
                                append("不可与勇者技能同时激活<br>");
                        return builder.toString();
                    }
                });
            } else if (name.equals("闪避")) {
                DefendSkill defendSkill = new DefendSkill();
                skill = defendSkill;
                skill.setHero(hero);
                skill.setName("闪避");
                skill.setEnableExpression(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                        return hero.getSkillPoint() > 0 && SkillFactory.getSkill("勇者一击", hero, dialog).isActive();
                    }
                });
                skill.setDescription(new DescExpression() {
                    @Override
                    public String buildDescription(Skill skill) {
                        DefendSkill ds = (DefendSkill) skill;
                        StringBuilder builder = new StringBuilder();
                        builder.append("<br>");
                        builder.append("防御技能<br>");
                        builder.append(ds.getProbability()).append("%概率闪避攻击<br>");
                        return builder.toString();
                    }
                });
                skill.setPerform(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                        Random random = hero.getRandom();
                        return random.nextInt(1000) > skill.getProbability() * 10;
                    }
                });
                skill.setRelease(new UseExpression() {
                    @Override
                    public boolean release(Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                        context.addMessage(monster.getFormatName() + "攻击" + hero.getName());
                        context.addMessage(hero.getName() + "使用技能" + skill.getName() + "躲过了攻击");
                        return false;
                    }
                });
                if (!skill.load()) {
                    skill.setProbability(0.1f);
                }
                skill.setLevelUp(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                        DefendSkill ds = (DefendSkill) skill;
                        if (skill.getProbability() < 2) {
                            skill.setProbability(skill.getProbability() + 0.1f);
                            return true;
                        }
                        return false;
                    }
                });
            } else if (name.equals("重拳")) {
                AttackSkill attackSkill = new AttackSkill();
                skill = attackSkill;
                skill.setHero(hero);
                skill.setName("重拳");
                skill.setEnableExpression(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                        return hero.getSkillPoint() > 0 && SkillFactory.getSkill("勇者一击", hero, dialog).isActive();
                    }
                });
                skill.setDescription(new DescExpression() {
                    @Override
                    public String buildDescription(Skill skill) {
                        AttackSkill as = (AttackSkill) skill;
                        StringBuilder builder = new StringBuilder();
                        builder.append("<br>");
                        builder.append(as.getProbability()).append("%的概率释放<br>");
                        builder.append("使用技能后，有").append(as.getProbability()/10f).append("%概率使得敌人眩晕<br>");
                        return builder.toString();
                    }
                });
                skill.setPerform(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                        Random random = hero.getRandom();
                        return random.nextInt(100) > skill.getProbability();
                    }
                });
                skill.setRelease(new UseExpression() {
                    @Override
                    public boolean release(Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                        int harm = hero.getAttackValue();
                        context.addMessage(hero.getName() + "使用技能" + skill.getName() + "攻击" + monster.getFormatName() + "造成了" + harm + "点伤害");
                        monster.addHp(-harm);
                        if(hero.getRandom().nextInt(1000) > skill.getProbability()){
                            context.addMessage(monster.getFormatName() + "被打晕了");
                            try {
                                Thread.sleep(context.getRefreshInfoSpeed());
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            harm = hero.getAttackValue();
                            context.addMessage(hero.getName() + "使用技能" + skill.getName() + "攻击" + monster.getFormatName() + "造成了" + harm + "点伤害");
                            monster.addHp(-harm);
                        }
                        return false;
                    }
                });
                if (!skill.load()) {
                    skill.setProbability(0.1f);
                }
                skill.setLevelUp(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                        DefendSkill ds = (DefendSkill) skill;
                        if (skill.getProbability() < 2) {
                            skill.setProbability(skill.getProbability() + 0.1f);
                            return true;
                        }
                        return false;
                    }
                });
            }
            if (skill != null) {
                skill.setSkillDialog(dialog);
            }
        }
        skillMap.put(name, skill);
        return skill;
    }

    public static void refreshSkillStatus() {
        for (Skill skill : skillMap.values()) {
            skill.refresh();
        }
    }

    public static void save() {
        for (Skill skill : skillMap.values()) {
            skill.save();
        }
    }
}
