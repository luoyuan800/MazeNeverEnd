package cn.gavin.skill;

import android.widget.Button;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.gavin.Hero;
import cn.gavin.Maze;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.monster.Monster;
import cn.gavin.skill.expression.DescExpression;
import cn.gavin.skill.expression.EnableExpression;
import cn.gavin.skill.expression.UseExpression;
import cn.gavin.skill.type.AttackSkill;
import cn.gavin.skill.type.DefendSkill;
import cn.gavin.utils.Random;

/**
 * Created by luoyuan on 9/13/15.
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
                        return random.nextLong(100) < skill.getProbability();
                    }
                });
                skill.setRelease(new UseExpression() {
                    @Override
                    public boolean release(Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                        long harm = ((AttackSkill) skill).getBaseHarm() + hero.getRandom().nextLong((((AttackSkill) skill).getAdditionHarm()) + 1);
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
                        return random.nextLong(1000) < skill.getProbability() * 10;
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
                    skill.setProbability(0.5f);
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
            } else if (name.equals("斩击")) {
                final AttackSkill attackSkill = new AttackSkill();
                skill = attackSkill;
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
                        builder.append("<br>");
                        builder.append(as.getProbability()).append("%的概率释放<br>");
                        builder.append("对对方造成不超过自身当前HP值的随机伤害<br>");
                        return builder.toString();
                    }
                });
                skill.setPerform(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                        Random random = hero.getRandom();
                        return random.nextLong(100) + random.nextFloat() < skill.getProbability();
                    }
                });
                skill.setRelease(new UseExpression() {
                    @Override
                    public boolean release(Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                        long harm = hero.getRandom().nextLong(hero.getHp() + 1);
                        context.addMessage(hero.getName() + "使用技能" + skill.getName() + "攻击" + monster.getFormatName() + "造成了" + harm + "点伤害");
                        monster.addHp(-harm);
                        return false;
                    }
                });
                if (!skill.load()) {
                    skill.setProbability(1.1f);
                }
                skill.setLevelUp(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                        DefendSkill ds = (DefendSkill) skill;
                        if (skill.getProbability() < 15) {
                            skill.setProbability(skill.getProbability() + 0.7f);
                            return true;
                        }
                        return false;
                    }
                });
            } else if (name.equals("铁拳")) {
                AttackSkill attackSkill = new AttackSkill();
                skill = attackSkill;
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
                        builder.append("<br>");
                        builder.append(as.getProbability()).append("%的概率释放<br>");
                        builder.append("使用技能后，有").append(as.getProbability() / 10f).append("%概率使得敌人眩晕<br>");
                        return builder.toString();
                    }
                });
                skill.setPerform(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                        Random random = hero.getRandom();
                        return random.nextLong(100) < skill.getProbability();
                    }
                });
                skill.setRelease(new UseExpression() {
                    @Override
                    public boolean release(Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                        long harm = hero.getAttackValue();
                        context.addMessage(hero.getName() + "使用技能" + skill.getName() + "攻击" + monster.getFormatName() + "造成了" + harm + "点伤害");
                        monster.addHp(-harm);
                        if (hero.getRandom().nextLong(1000) > skill.getProbability()) {
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
                        builder.append("<br>");
                        builder.append("防御技能<br>");
                        builder.append(ds.getProbability()).append("%概率反弹").append(20 + skill.getProbability() * 10).append("%的攻击伤害<br>");
                        return builder.toString();
                    }
                });
                skill.setPerform(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                        Random random = hero.getRandom();
                        return random.nextLong(100) + random.nextFloat() < skill.getProbability();
                    }
                });
                skill.setRelease(new UseExpression() {
                    @Override
                    public boolean release(Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                        long harm = monster.getAtk() - hero.getDefenseValue();
                        context.addMessage(monster.getFormatName() + "攻击" + hero.getName());
                        long rHarm = Math.round(harm * (20 + skill.getProbability() * 10));
                        context.addMessage(hero.getName() + "使用技能" + skill.getName() + "反弹了" + rHarm + "的伤害");
                        monster.addHp(-rHarm);
                        hero.addHp(-(harm - rHarm));
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
                            skill.setProbability(skill.getProbability() + 0.5f);
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
                        builder.append("<br>");
                        builder.append("防御技能<br>");
                        builder.append(ds.getProbability()).append("%概率在受到伤害后持续一段时间增幅0 - ").append(ds.getProbability() * 5 + 5).append("%的攻击力<br>");
                        return builder.toString();
                    }
                });
                skill.setPerform(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                        Random random = hero.getRandom();
                        return random.nextLong(100) + random.nextFloat() < skill.getProbability();
                    }
                });
                skill.setRelease(new UseExpression() {
                    @Override
                    public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                        long harm = monster.getAtk() - hero.getDefenseValue();
                        context.addMessage(monster.getFormatName() + "攻击了" + hero.getName() + "造成了" + harm + "点伤害");
                        hero.addHp(-harm);
                        final long atk = hero.getRandom().nextLong(Math.round(hero.getAttackValue() + hero.getAttackValue() * (5 + skill.getProbability() * 5)) + 1);
                        context.addMessage(hero.getName() + "触发了" + skill.getName() + "攻击力增加了" + atk);
                        hero.addAttackValue(atk);
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
                    skill.setProbability(0.8f);
                }
                skill.setLevelUp(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                        if (skill.getProbability() < 20) {
                            skill.setProbability(skill.getProbability() + 0.8f);
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
                        builder.append("<br>");
                        builder.append("防御技能<br>");
                        builder.append(ds.getProbability()).append("%概率在受到伤害后持续一段时间使得对方不可动弹").append("<br>");
                        return builder.toString();
                    }
                });
                skill.setPerform(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                        Random random = hero.getRandom();
                        return random.nextLong(1000) + random.nextFloat() < skill.getProbability() * 10;
                    }
                });
                skill.setRelease(new UseExpression() {
                    @Override
                    public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                        long harm = monster.getAtk() - hero.getDefenseValue();
                        context.addMessage(monster.getFormatName() + "攻击了" + hero.getName() + "造成了" + harm + "点伤害");
                        hero.addHp(-harm);
                        long turn = hero.getRandom().nextLong(Math.round(skill.getCount() / 1000f) + 1) + 1;
                        context.addMessage(hero.getName() + "触发了" + skill.getName() + "定住对方" + turn + "个回合");
                        while (turn-- > 0) {
                            monster.addHp(-hero.getAttackValue());
                            context.addMessage(hero.getName() + "攻击了" + monster.getFormatName() + "造成了" + harm + "点伤害");
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
                    skill.setProbability(0.8f);
                }
                skill.setLevelUp(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                        if (skill.getProbability() < 20) {
                            skill.setProbability(skill.getProbability() + 0.8f);
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
                        builder.append("<br>");
                        builder.append("防御技能<br>");
                        builder.append(ds.getProbability()).append("%概率在受到伤害后持续一段时间增幅0 - ").append(ds.getProbability() * 5 + 10).append("%的生命值上限<br>");
                        return builder.toString();
                    }
                });
                skill.setPerform(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                        Random random = hero.getRandom();
                        return random.nextLong(100) + random.nextFloat() < skill.getProbability();
                    }
                });
                skill.setRelease(new UseExpression() {
                    @Override
                    public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                        long harm = monster.getAtk() - hero.getDefenseValue();
                        context.addMessage(monster.getFormatName() + "攻击了" + hero.getName() + "造成了" + harm + "点伤害");
                        hero.addHp(-harm);
                        final long hp = hero.getRandom().nextLong(Math.round(hero.getAttackValue() + hero.getAttackValue() * (10 + skill.getProbability() * 5)) + 1);
                        context.addMessage(hero.getName() + "触发了" + skill.getName() + "生命值上限增加了" + hp);
                        hero.addUpperHp(hp);
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
                    skill.setProbability(1.1f);
                }
                skill.setLevelUp(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                        if (skill.getProbability() < 20) {
                            skill.setProbability(skill.getProbability() + 0.8f);
                            return true;
                        }
                        return false;
                    }
                });
            }
            if (skill != null) {
                skill.setSkillDialog(dialog);
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
