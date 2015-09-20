package cn.gavin.skill;

import android.widget.Button;
import cn.gavin.Hero;
import cn.gavin.Maze;
import cn.gavin.Sword;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.monster.Monster;
import cn.gavin.skill.expression.DescExpression;
import cn.gavin.skill.expression.EnableExpression;
import cn.gavin.skill.expression.UseExpression;
import cn.gavin.skill.type.AttackSkill;
import cn.gavin.skill.type.DefendSkill;
import cn.gavin.skill.type.PropertySkill;
import cn.gavin.utils.Random;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * by luoyuan
 * on 9/13/15.
 */
public class SkillFactory {
    private static Map<String, Skill> skillMap = new ConcurrentHashMap<String, Skill>();

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
                        return (skill.isActive() || hero.getSkillPoint() > 0) && !SkillFactory.getSkill("魔王天赋", hero, dialog).isActive();
                    }
                });
                skill.setDescription(new DescExpression() {
                    @Override
                    public String buildDescription(Skill skill) {
                        AttackSkill attackSkill = (AttackSkill) skill;
                        StringBuilder builder = new StringBuilder();
                        builder.append("勇者的基本技能，学会了才能踏上征途。<br>");
                        builder.append(attackSkill.getProbability()).append("%概率释放<br>");
                        builder.append("造成额外的").append(attackSkill.getBaseHarm()).append(" - ").
                                append(attackSkill.getBaseHarm() + attackSkill.getAdditionHarm()).append("伤害").append("<br>").
                                append("不可与魔王技能同时激活");
                        return builder.toString();
                    }
                });

                skill.setRelease(new UseExpression() {
                    @Override
                    public boolean release(Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                        long harm = hero.getAttackValue() + ((AttackSkill) skill).getBaseHarm() + hero.getRandom().nextLong((((AttackSkill) skill).getAdditionHarm()) + 1);
                        context.addMessage(skill.format(hero.getFormatName() + "使用了技能" + skill.getName() + "对" + monster.getFormatName() + "造成了" + harm + "点伤害"));
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
                        if (skill.getProbability() < 50) {
                            skill.setProbability(skill.getProbability() + 3);
                            as.setBaseHarm(as.getBaseHarm() + hero.getRandom().nextLong(hero.getDefenseValue() / 10 + 1));
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
                        builder.append("魔本天性，吾欲入魔，谁人可挡。<br>").
                                append("被攻击的时候有").append(defendSkill.getProbability()).
                                append("%概率将伤害转换为HP恢复。").
                                append("不可与勇者技能同时激活");
                        return builder.toString();
                    }
                });
            } else if (name.equals("闪避")) {
                skill = new DefendSkill();
                skill.setName("闪避");
                skill.setHero(hero);
                skill.setEnableExpression(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                        return (skill.isActive() || hero.getSkillPoint() > 0) && SkillFactory.getSkill("勇者之击", hero, dialog).isActive();
                    }
                });
                skill.setDescription(new DescExpression() {
                    @Override
                    public String buildDescription(Skill skill) {
                        DefendSkill ds = (DefendSkill) skill;
                        StringBuilder builder = new StringBuilder();
                        builder.append("防御技能<br>");
                        builder.append(ds.getProbability()).append("%概率闪避攻击");
                        return builder.toString();
                    }
                });

                skill.setRelease(new UseExpression() {
                    @Override
                    public boolean release(Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                        context.addMessage(skill.format(monster.getFormatName() + "攻击" + hero.getFormatName()));
                        context.addMessage(skill.format(hero.getFormatName() + "使用技能" + skill.getName() + "躲过了攻击"));
                        return false;
                    }
                });
                if (!skill.load()) {
                    skill.setProbability(2.0f);
                }
                skill.setLevelUp(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                        if (skill.getProbability() < 2) {
                            skill.setProbability(skill.getProbability() + 1.1f);
                            return true;
                        }
                        return false;
                    }
                });
            } else if (name.equals("斩击")) {
                skill = new AttackSkill();
                skill.setName("斩击");
                skill.setHero(hero);
                skill.setEnableExpression(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                        return (skill.isActive() || hero.getSkillPoint() > 0) && SkillFactory.getSkill("超能量", hero, dialog).isActive();
                    }
                });
                skill.setDescription(new DescExpression() {
                    @Override
                    public String buildDescription(Skill skill) {
                        AttackSkill as = (AttackSkill) skill;
                        StringBuilder builder = new StringBuilder();
                        builder.append(as.getProbability()).append("%的概率释放<br>");
                        builder.append("对对方造成不超过自身当前HP值的随机伤害");
                        return builder.toString();
                    }
                });

                skill.setRelease(new UseExpression() {
                    @Override
                    public boolean release(Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                        long harm = hero.getRandom().nextLong(hero.getHp() + 1);
                        context.addMessage(skill.format(hero.getFormatName() + "使用技能" + skill.getName() + "攻击" + monster.getFormatName() + "造成了" + harm + "点伤害"));
                        monster.addHp(-harm);
                        return false;
                    }
                });
                if (!skill.load()) {
                    skill.setProbability(7.5f);
                }
                skill.setLevelUp(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                        DefendSkill ds = (DefendSkill) skill;
                        if (skill.getProbability() < 15) {
                            skill.setProbability(skill.getProbability() + 1.1f);
                            return true;
                        }
                        return false;
                    }
                });
            } else if (name.equals("铁拳")) {
                skill = new AttackSkill();
                skill.setName("铁拳");
                skill.setHero(hero);
                skill.setEnableExpression(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                        return (skill.isActive() || hero.getSkillPoint() > 0) && SkillFactory.getSkill("勇者之击", hero, dialog).isActive();
                    }
                });
                skill.setDescription(new DescExpression() {
                    @Override
                    public String buildDescription(Skill skill) {
                        AttackSkill as = (AttackSkill) skill;
                        StringBuilder builder = new StringBuilder();
                        builder.append(as.getProbability()).append("%的概率释放<br>");
                        builder.append("使用技能后，有").append(as.getProbability() / 10f).append("%概率使得敌人眩晕");
                        return builder.toString();
                    }
                });

                skill.setRelease(new UseExpression() {
                    @Override
                    public boolean release(Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                        long harm = hero.getAttackValue() + hero.getRandom().nextLong(hero.getStrength() / 100 + 1);
                        context.addMessage(skill.format(hero.getFormatName() + "使用技能" + skill.getName() + "攻击" + monster.getFormatName() + "造成了" + harm + "点伤害"));
                        monster.addHp(-harm);
                        if (hero.getRandom().nextLong(1000) > skill.getProbability()) {
                            context.addMessage(skill.format(monster.getFormatName() + "被打晕了"));
                            try {
                                Thread.sleep(context.getRefreshInfoSpeed());
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            harm = hero.getAttackValue() + hero.getRandom().nextLong(hero.getStrength() / 100 + 1);
                            context.addMessage(skill.format(hero.getFormatName() + "使用技能" + skill.getName() + "攻击" + monster.getFormatName() + "造成了" + harm + "点伤害"));
                            monster.addHp(-harm);
                        }
                        return false;
                    }
                });
                if (!skill.load()) {
                    skill.setProbability(2.5f);
                }
                skill.setLevelUp(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                        if (skill.getProbability() < 2) {
                            skill.setProbability(skill.getProbability() + 1.1f);
                            return true;
                        }
                        return false;
                    }
                });
            } else if (name.equals("反弹")) {
                skill = new DefendSkill();
                skill.setName("反弹");
                skill.setHero(hero);
                skill.setEnableExpression(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                        return (skill.isActive() || hero.getSkillPoint() > 0) && SkillFactory.getSkill("闪避", hero, dialog).isActive();
                    }
                });
                skill.setDescription(new DescExpression() {
                    @Override
                    public String buildDescription(Skill skill) {
                        DefendSkill ds = (DefendSkill) skill;
                        StringBuilder builder = new StringBuilder();
                        builder.append("防御技能<br>");
                        builder.append(ds.getProbability()).append("%概率反弹").append(20 + skill.getProbability() * 10).append("%的攻击伤害");
                        return builder.toString();
                    }
                });

                skill.setRelease(new UseExpression() {
                    @Override
                    public boolean release(Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                        long harm = monster.getAtk() - hero.getDefenseValue();
                        context.addMessage(skill.format(monster.getFormatName() + "攻击" + hero.getFormatName()));
                        long rHarm = Math.round(harm * (20 + skill.getProbability() * 10));
                        context.addMessage(skill.format(hero.getFormatName() + "使用技能" + skill.getName() + "反弹了" + rHarm + "的伤害"));
                        monster.addHp(-rHarm);
                        hero.addHp(-(harm - rHarm));
                        return false;
                    }
                });
                if (!skill.load()) {
                    skill.setProbability(2.0f);
                }
                skill.setLevelUp(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                        if (skill.getProbability() < 20) {
                            skill.setProbability(skill.getProbability() + 2.5f);
                            return true;
                        }
                        return false;
                    }
                });
            } else if (name.equals("巨大化")) {
                skill = new DefendSkill();
                skill.setName("巨大化");
                skill.setHero(hero);
                skill.setEnableExpression(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                        return (skill.isActive() || hero.getSkillPoint() > 0) && (SkillFactory.getSkill("闪避", hero, dialog).isActive() || SkillFactory.getSkill("铁拳", hero, dialog).isActive());
                    }
                });
                skill.setDescription(new DescExpression() {
                    @Override
                    public String buildDescription(Skill skill) {
                        DefendSkill ds = (DefendSkill) skill;
                        StringBuilder builder = new StringBuilder();
                        builder.append("防御技能<br>");
                        builder.append(ds.getProbability()).append("%概率在受到伤害后持续一段时间增幅0 - ").append(ds.getProbability() * 5 + 5).append("%的攻击力");
                        return builder.toString();
                    }
                });

                skill.setRelease(new UseExpression() {
                    @Override
                    public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                        long harm = monster.getAtk() - hero.getDefenseValue();
                        context.addMessage(skill.format(monster.getFormatName() + "攻击了" + hero.getFormatName() + "造成了" + harm + "点伤害"));
                        hero.addHp(-harm);
                        final long atk = hero.getRandom().nextLong(Math.round(hero.getAttackValue() + hero.getAttackValue() * (5 + skill.getProbability() * 5)) + 1);
                        hero.addAttackValue(atk);
                        context.addMessage(skill.format(hero.getFormatName() + "触发了" + skill.getName() + "攻击力增加了" + atk));
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(MainGameActivity.context.getRefreshInfoSpeed() * (hero.getRandom().nextLong(5) + 1));
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                hero.addAttackValue(-atk);
                            }
                        }).start();
                        return false;
                    }
                });
                if (!skill.load()) {
                    skill.setProbability(4.8f);
                }
                skill.setLevelUp(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                        if (skill.getProbability() < 20) {
                            skill.setProbability(skill.getProbability() + 1.2f);
                            return true;
                        }
                        return false;
                    }
                });
            } else if (name.equals("定身")) {
                skill = new DefendSkill();
                skill.setName("定身");
                skill.setHero(hero);
                skill.setEnableExpression(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                        return (skill.isActive() || hero.getSkillPoint() > 0) && (SkillFactory.getSkill("铁拳", hero, dialog).isActive());
                    }
                });
                skill.setDescription(new DescExpression() {
                    @Override
                    public String buildDescription(Skill skill) {
                        DefendSkill ds = (DefendSkill) skill;
                        StringBuilder builder = new StringBuilder();
                        builder.append("防御技能<br>");
                        builder.append(ds.getProbability()).append("%概率在受到伤害后持续一段时间使得对方不可动弹");
                        return builder.toString();
                    }
                });

                skill.setRelease(new UseExpression() {
                    @Override
                    public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                        long harm = monster.getAtk() - hero.getDefenseValue();
                        context.addMessage(skill.format(monster.getFormatName() + "攻击了" + hero.getFormatName() + "造成了" + harm + "点伤害"));
                        hero.addHp(-harm);
                        long turn = hero.getRandom().nextLong(Math.round(skill.getCount() / 1000f) + 1) + 1;
                        context.addMessage(skill.format(hero.getFormatName() + "触发了" + skill.getName() + "定住对方" + turn + "个回合"));
                        while (turn-- > 0) {
                            monster.addHp(-hero.getAttackValue());
                            context.addMessage(skill.format(hero.getFormatName() + "攻击了" + monster.getFormatName() + "造成了" + harm + "点伤害"));
                            try {
                                Thread.sleep(MainGameActivity.context.getRefreshInfoSpeed());
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        return false;
                    }
                });
                if (!skill.load()) {
                    skill.setProbability(1.0f);
                }
                skill.setLevelUp(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                        if (skill.getProbability() < 20) {
                            skill.setProbability(skill.getProbability() + 1.1f);
                            return true;
                        }
                        return false;
                    }
                });
            } else if (name.equals("超能量")) {
                skill = new DefendSkill();
                skill.setName("超能量");
                skill.setHero(hero);
                skill.setEnableExpression(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                        return (skill.isActive() || hero.getSkillPoint() > 0)
                                && (SkillFactory.getSkill("反弹", hero, dialog).isActive() ||
                                SkillFactory.getSkill("巨大化", hero, dialog).isActive() ||
                                SkillFactory.getSkill("定身", hero, dialog).isActive());
                    }
                });
                skill.setDescription(new DescExpression() {
                    @Override
                    public String buildDescription(Skill skill) {
                        DefendSkill ds = (DefendSkill) skill;
                        StringBuilder builder = new StringBuilder();
                        builder.append("防御技能<br>");
                        builder.append(ds.getProbability()).append("%概率在受到伤害后持续一段时间增幅0 - ").append(ds.getProbability() * 5 + 10).append("%的生命值上限");
                        return builder.toString();
                    }
                });

                skill.setRelease(new UseExpression() {
                    @Override
                    public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                        long harm = monster.getAtk() - hero.getDefenseValue();
                        hero.addHp(-harm);
                        context.addMessage(skill.format(monster.getFormatName() + "攻击了" + hero.getFormatName() + "造成了" + harm + "点伤害"));
                        final long hp = hero.getRandom().nextLong(Math.round(hero.getAttackValue() + hero.getAttackValue() * (10 + skill.getProbability() * 5)) + 1);
                        hero.addUpperHp(hp);
                        context.addMessage(skill.format(hero.getFormatName() + "触发了" + skill.getName() + "生命值上限增加了" + hp));
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(MainGameActivity.context.getRefreshInfoSpeed() * (hero.getRandom().nextLong(10) + 1));
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                hero.addUpperHp(-hp);
                            }
                        }).start();
                        return false;
                    }
                });
                if (!skill.load()) {
                    skill.setProbability(3.5f);
                }
                skill.setLevelUp(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                        if (skill.getProbability() < 20) {
                            skill.setProbability(skill.getProbability() + 1.8f);
                            return true;
                        }
                        return false;
                    }
                });
            } else if (name.equals("瞬间移动")) {
                skill = new PropertySkill(0, 0, 0, 0, 0, 0, 0);
                skill.setName("瞬间移动");
                skill.setHero(hero);
                skill.setEnableExpression(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                        return (skill.isActive() || hero.getSkillPoint() > 0)
                                && SkillFactory.getSkill("超能量", hero, dialog).isActive();
                    }
                });
                skill.setDescription(new DescExpression() {
                    @Override
                    public String buildDescription(Skill skill) {
                        DefendSkill ds = (DefendSkill) skill;
                        StringBuilder builder = new StringBuilder();
                        builder.append("被动技能<br>");
                        builder.append(ds.getProbability()).append("%的概率在受到致命伤害前脱离战斗");
                        return builder.toString();
                    }
                });

                skill.setRelease(new UseExpression() {
                    @Override
                    public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                        context.addMessage(skill.format(hero.getFormatName() + "触发了" + skill.getName() + "，逃离了战斗"));
                        return true;
                    }
                });
                if (!skill.load()) {
                    skill.setProbability(1f);
                }
                skill.setLevelUp(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                        if (skill.getProbability() < 10) {
                            skill.setProbability(skill.getProbability() + 0.9f);
                            return true;
                        }
                        return false;
                    }
                });
            } else if (name.equals("传送")) {
                skill = new PropertySkill(0, 0, 0, 0, 0, 0, 0) {
                    public void setOnUsed(boolean use) {
                        if (use)
                            MainGameActivity.context.getMaze().setCsmgl(MainGameActivity.context.getMaze().getCsmgl() - Math.round(getProbability()));
                        else
                            MainGameActivity.context.getMaze().setCsmgl(MainGameActivity.context.getMaze().getCsmgl() + Math.round(getProbability()));
                    }
                };
                skill.setName("传送");
                skill.setHero(hero);
                skill.setEnableExpression(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                        return (skill.isActive() || hero.getSkillPoint() > 0)
                                && SkillFactory.getSkill("瞬间移动", hero, dialog).isActive();
                    }
                });
                skill.setDescription(new DescExpression() {
                    @Override
                    public String buildDescription(Skill skill) {
                        DefendSkill ds = (DefendSkill) skill;
                        StringBuilder builder = new StringBuilder();
                        builder.append("被动技能<br>");
                        builder.append(ds.getProbability()).append("增加踩到传送门的几率");
                        return builder.toString();
                    }
                });

                skill.setRelease(new UseExpression() {
                    @Override
                    public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {
//                        context.addMessage(hero.getFormatName() + "触发了" + skill.getName() + "，逃离了战斗");
                        return true;
                    }
                });
                if (!skill.load()) {
                    skill.setProbability(50f);
                }
                skill.setLevelUp(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                        if (skill.getProbability() < 400) {
                            skill.setProbability(skill.getProbability() + 30f);
                            MainGameActivity.context.getMaze().setCsmgl(MainGameActivity.context.getMaze().getCsmgl() - 30);
                            return true;
                        }
                        return false;
                    }
                });
            } else if (name.equals("裂空剑")) {
                skill = new AttackSkill();
                skill.setName("裂空剑");
                skill.setHero(hero);
                skill.setEnableExpression(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                        return (skill.isActive() || hero.getSkillPoint() > 0)
                                && SkillFactory.getSkill("超能量", hero, dialog).isActive() && (Sword.valueOf(hero.getSword()).ordinal() > Sword.金剑.ordinal());
                    }
                });
                skill.setDescription(new DescExpression() {
                    @Override
                    public String buildDescription(Skill skill) {
                        AttackSkill ds = (AttackSkill) skill;
                        StringBuilder builder = new StringBuilder();
                        builder.append("高阶剑技能，需要装备武器阶位高于金剑才可以激活<br>");
                        builder.append(ds.getProbability()).append("的概率释放，造成额外的").append(ds.getBaseHarm()).append(" - ").append(ds.getBaseHarm() + ds.getAdditionHarm()).append("伤害");
                        return builder.toString();
                    }
                });

                skill.setRelease(new UseExpression() {
                    @Override
                    public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                        long harm = hero.getAttackValue() + ((AttackSkill) skill).getBaseHarm() + hero.getRandom().nextLong(((AttackSkill) skill).getAdditionHarm() + 1);
                        monster.addHp(-harm);
                        skill.addMessage(hero.getFormatName() + "使用了技能" + skill.getName() + "，对" + monster.getName() + "造成了" + harm + "点伤害");
                        return false;
                    }
                });
                if (!skill.load()) {
                    skill.setProbability(5f);
                    AttackSkill attk = (AttackSkill) skill;
                    attk.setBaseHarm(10000);
                    attk.setAdditionHarm(50000);
                }
                skill.setLevelUp(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                        if (skill.getProbability() < 20) {
                            skill.setProbability(skill.getProbability() + 2f);
                            return true;
                        }
                        return false;
                    }
                });
            } else if (name.equals("原能力")) {
                DefendSkill defendSkill = new DefendSkill();
                defendSkill.setName("原能力");
                defendSkill.setHero(hero);
                defendSkill.setEnableExpression(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                        return (skill.isActive() || hero.getSkillPoint() > 0)
                                && SkillFactory.getSkill("裂空剑", hero, dialog).isActive();
                    }
                });
                defendSkill.setDescription(new DescExpression() {
                    @Override
                    public String buildDescription(Skill skill) {
                        AttackSkill ds = (AttackSkill) skill;
                        StringBuilder builder = new StringBuilder();
                        builder.append("防御技能<br>");
                        builder.append(ds.getProbability()).append("的概率释放，在受到伤害后恢复全部HP");
                        return builder.toString();
                    }
                });

                defendSkill.setRelease(new UseExpression() {
                    @Override
                    public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                        long harm = monster.getAtk() - hero.getDefenseValue();
                        if (harm >= hero.getHp()) {
                            hero.addHp(-harm);
                            skill.addMessage(monster.getFormatName() + "攻击了" + hero.getFormatName() + "，造成了" + harm + "的伤害");
                            return false;
                        }
                        hero.restore();
                        skill.addMessage(hero.getFormatName() + "使用了技能" + skill.getName() + "，HP全部恢复了");
                        return false;
                    }
                });
                if (!defendSkill.load()) {
                    defendSkill.setProbability(1f);
                }
                defendSkill.setLevelUp(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                        if (skill.getProbability() < 10) {
                            skill.setProbability(skill.getProbability() + 1f);
                            return true;
                        }
                        return false;
                    }
                });
            } else if (name.equals("重击")) {
                AttackSkill iSkill = new AttackSkill();
                iSkill.setName("重击");
                iSkill.setHero(hero);
                iSkill.setEnableExpression(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                        return (skill.isActive() || hero.getSkillPoint() > 0)
                                && SkillFactory.getSkill("原能力", hero, dialog).isActive();
                    }
                });
                iSkill.setDescription(new DescExpression() {
                    @Override
                    public String buildDescription(Skill skill) {
                        AttackSkill aSkill = (AttackSkill) skill;
                        StringBuilder builder = new StringBuilder();
                        builder.append("返璞归真<br>");
                        builder.append(skill.getProbability()).append("的概率释放，造成").append(aSkill.getBaseHarm()).append("倍伤害");
                        return builder.toString();
                    }
                });
                iSkill.setRelease(new UseExpression() {
                    @Override
                    public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                        long n = hero.getRandom().nextLong(((AttackSkill) skill).getBaseHarm() + 1) + 1;
                        long harm = n * hero.getAttackValue();
                        monster.addHp(-harm);
                        skill.addMessage(hero.getFormatName() + "使用了技能" + skill.getName() + "，对" + monster.getFormatName() + "造成了" + harm + "伤害");
                        return false;
                    }
                });
                if (!iSkill.load()) {
                    iSkill.setProbability(5f);
                    iSkill.setBaseHarm(3);
                }
                iSkill.setLevelUp(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                        if (skill.getProbability() < 20) {
                            skill.setProbability(skill.getProbability() + 1f);
                            ((AttackSkill) skill).setBaseHarm(((AttackSkill) skill).getBaseHarm() + 5);
                            return true;
                        }
                        return false;
                    }
                });
            } else if (name.equals("寻宝")) {
                final PropertySkill iskll = new PropertySkill(2900, 0, 0, 0, 0, 0, 0);
                iskll.setName("寻宝");
                iskll.setHero(hero);
                iskll.setEnableExpression(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                        return (skill.isActive() || hero.getSkillPoint() > 0)
                                && SkillFactory.getSkill("传送", hero, dialog).isActive();
                    }
                });
                iskll.setDescription(new DescExpression() {
                    @Override
                    public String buildDescription(Skill skill) {
                        DefendSkill ds = (DefendSkill) skill;
                        StringBuilder builder = new StringBuilder();
                        builder.append("被动技能<br>");
                        builder.append(ds.getProbability()).append("永久增加获得宝箱的概率防御上限");
                        return builder.toString();
                    }
                });

                iskll.setRelease(new UseExpression() {
                    @Override
                    public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                        return true;
                    }
                });
                if (!iskll.load()) {
                    iskll.setProbability(1f);
                }
                iskll.setLevelUp(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                        return true;
                    }
                });
            }else if (name.equals("错位")) {
                final AttackSkill iskll = new AttackSkill();
                iskll.setName("错位");
                iskll.setHero(hero);
                iskll.setEnableExpression(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                        return (skill.isActive() || hero.getSkillPoint() > 0)
                                && SkillFactory.getSkill("斩击", hero, dialog).isActive();
                    }
                });
                iskll.setDescription(new DescExpression() {
                    @Override
                    public String buildDescription(Skill skill) {
                        StringBuilder builder = new StringBuilder();
                        builder.append("当敌方生命值比自己HP高的时候，有").append(skill.getProbability()).append("%的概率交换双方的HP");
                        return builder.toString();
                    }
                });

                iskll.setRelease(new UseExpression() {
                    @Override
                    public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                        if(monster.getHp() > hero.getHp()){
                            long p = hero.getHp();
                            hero.setHp(monster.getHp());
                            monster.addHp(-monster.getHp() + p);
                            skill.addMessage(hero.getFormatName() + "使用了技能" +skill.getName() + "，和" + monster.getFormatName() + "交换了HP");
                        }else{
                            long harm = hero.getAttackValue();
                            monster.addHp(-harm);
                            MainGameActivity.context.addMessage(hero.getFormatName() + "攻击了" + monster.getFormatName() + "造成了" + harm + "点伤害");
                        }
                        return true;
                    }
                });
                if (!iskll.load()) {
                    iskll.setProbability(1f);
                }
                iskll.setLevelUp(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                        return true;
                    }
                });
            }else if (name.equals("超防御")) {
                final PropertySkill iskll = new PropertySkill(0,0,0,0,0,0,0){
                    public void setActive(boolean active){
                        if(active){
                            getHero().setDefenseValue(getHero().getBaseDefense() * 2);
                        }else{
                            getHero().setDefenseValue(getHero().getBaseDefense() / 2);
                        }
                        super.setActive(active);
                    }
                };
                iskll.setName("超防御");
                iskll.setHero(hero);
                iskll.setEnableExpression(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                        return (skill.isActive() || hero.getSkillPoint() > 0)
                                && SkillFactory.getSkill("错位", hero, dialog).isActive();
                    }
                });
                iskll.setDescription(new DescExpression() {
                    @Override
                    public String buildDescription(Skill skill) {
                        StringBuilder builder = new StringBuilder();
                        builder.append("防御至上<br>").append("被动技能，激活即生效<br>").append("当激活该技能时，当前的基础防御力翻倍。当取消激活该技能时，当前的基础防御力减半");
                        return builder.toString();
                    }
                });

                iskll.setRelease(new UseExpression() {
                    @Override
                    public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {

                        return true;
                    }
                });
                if (!iskll.load()) {
                    iskll.setProbability(1f);
                }
                iskll.setLevelUp(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                        return true;
                    }
                });
            }
            if (skill != null) {
                skill.setSkillDialog(dialog);
                skill.setPerform(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                        Random random = hero.getRandom();
                        long r = skill.diejia(hero.getAgility());
                        return random.nextLong(100) + random.nextFloat() < skill.getProbability() + random.nextLong(r + 1);
                    }
                });
                skillMap.put(name, skill);
            } else {
                skill = emptySkill();
            }
        }
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

    public static Skill emptySkill() {
        return new Skill() {
            public boolean isActive() {
                return false;
            }

            @Override
            public void save() {

            }

            public void setSkillButton(Button button) {

            }

            @Override
            public boolean load() {
                return false;
            }
        };
    }

    public static long reset() {
        int point = 0;
        for (Skill skill : skillMap.values()) {
            if (skill.isActive()) {
                if (skill.isOnUsed()) {
                    skill.setOnUsed(false);
                }
                skill.setActive(false);
                point++;
            }
        }
        return point;
    }
}
