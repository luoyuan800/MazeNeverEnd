package cn.gavin.skill.system;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import cn.gavin.Achievement;
import cn.gavin.Hero;
import cn.gavin.R;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.maze.Maze;
import cn.gavin.monster.Monster;
import cn.gavin.skill.Skill;
import cn.gavin.skill.SkillFactory;
import cn.gavin.skill.expression.DescExpression;
import cn.gavin.skill.expression.EnableExpression;
import cn.gavin.skill.expression.UseExpression;
import cn.gavin.skill.type.AttackSkill;
import cn.gavin.skill.type.DefendSkill;
import cn.gavin.skill.type.PropertySkill;
import cn.gavin.utils.MathUtils;
import cn.gavin.utils.StringUtils;

/**
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED
 * Created by luoyuan on 10/9/15.
 */
public class SwindlerSkill extends SkillLayout {
    public SwindlerSkill(Context context) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.skill_layout_swindler, (ViewGroup) this.findViewById(R.id.skill_dialog));
        this.addView(view);
        this.view = view;
        this.context = context;
    }

    public void init() {
        Skill skill = SkillFactory.getSkill("点攻", hero);
        Button button = (Button) view.findViewById(R.id.skill_swindler_dg_button);
        skill.setSkillButton(button);
        button.setOnClickListener(buildOnClickListener(skill));
        skills.add(skill);


        skill = SkillFactory.getSkill("点防", hero);
        button = (Button) view.findViewById(R.id.skill_swindler_df_button);
        skill.setSkillButton(button);
        button.setOnClickListener(buildOnClickListener(skill));
        skills.add(skill);


        skill = SkillFactory.getSkill("欺诈游戏", hero);
        button = (Button) view.findViewById(R.id.skill_swindler_qg_button);
        skill.setSkillButton(button);
        button.setOnClickListener(buildOnClickListener(skill));
        skills.add(skill);


        skill = SkillFactory.getSkill("欺诈师", hero);
        button = (Button) view.findViewById(R.id.skill_swindler_qzs_button);
        skill.setSkillButton(button);
        button.setOnClickListener(buildOnClickListener(skill));
        skills.add(skill);


        skill = SkillFactory.getSkill("浮生百刃", hero);
        button = (Button) view.findViewById(R.id.skill_swindler_f_button);
        skill.setSkillButton(button);
        button.setOnClickListener(buildOnClickListener(skill));
        skills.add(skill);


        skill = SkillFactory.getSkill("虚无吞噬", hero);
        button = (Button) view.findViewById(R.id.skill_swindler_x_button);
        skill.setSkillButton(button);
        button.setOnClickListener(buildOnClickListener(skill));
        skills.add(skill);

    }

    public static Skill getSkill(String name, final Hero hero) {
        Skill skill = null;
        if ("点攻".equalsIgnoreCase(name)) {
            final AttackSkill iskll = new AttackSkill();
            skill = iskll;
            skill.setName("点攻");
            skill.setHero(hero);
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return SkillFactory.getSkill("欺诈游戏", hero).isActive() && Achievement.click50000.isEnable();
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("点击次数达到50000次才可以学习该技能。");
                    builder.append(skill.getProbability()).append("%的概率释放，根据点击数附加伤害。");
                    return builder.toString();
                }
            });
            skill.setRelease(new UseExpression() {
                @Override
                public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                    long harm = hero.getAttackValue() +
                            (hero.getClick() < (hero.getAttackValue()) ? (hero.getClick() + hero.getClickAward()) :
                                    hero.getRandom().nextLong(hero.getClick() + hero.getAttackValue()));
                    if (harm < 0) {
                        harm = hero.getRandom().nextLong(hero.getClick() + 1) + 1;
                    }
                    monster.addHp(-harm);
                    String msg = hero.getFormatName() + "使用了技能" + skill.getName() +
                            "，对" + monster.getFormatName() + "造成了" + StringUtils.formatNumber(harm) + "的伤害。";
                    skill.addMessage(msg);
                    monster.addBattleSkillDesc(msg);
                    return false;
                }

            });
            skill.setLevelUp(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    if (skill.getProbability() < 25) {
                        skill.setProbability(skill.getProbability() + 3);
                        return true;
                    }
                    return true;
                }
            });
            if (!skill.load()) {
                skill.setProbability(6f);
            }
        } else if ("点防".equalsIgnoreCase(name)) {
            final DefendSkill iskll = new DefendSkill();
            skill = iskll;
            skill.setName("点防");
            skill.setHero(hero);
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return SkillFactory.getSkill("欺诈游戏", hero).isActive() && Achievement.click50000.isEnable();
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("点击次数达到50000次才可以学习该技能。<br>防御技能<br>被攻击时");
                    builder.append(skill.getProbability()).append("%的概率释放，根据点击数增加防御。");
                    return builder.toString();
                }
            });
            skill.setRelease(new UseExpression() {
                @Override
                public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                    long harm = monster.getAtk() - hero.getDefenseValue();
                    if (harm <= 0) {
                        harm = hero.getRandom().nextLong(hero.getMaxMazeLev());
                    }
                    String amsg = monster.getFormatName() + "攻击了" + hero.getFormatName();
                    skill.addMessage(amsg);
                    monster.addBattleDesc(amsg);
                    long reduce = hero.getClick() < hero.getBaseAttackValue() ?
                            (hero.getClick() + hero.getClickAward()) :
                            (hero.getRandom().nextLong(hero.getClick() + hero.getBaseDefense()));
                    String smsg = hero.getFormatName() + "使用了技能" + skill.getName() + "提升了" + StringUtils.formatNumber(reduce) + "点防御力";
                    skill.addMessage(smsg);
                    monster.addBattleSkillDesc(smsg);
                    harm -= reduce;
                    if (harm < 0) harm = 0;
                    hero.addHp(-harm);
                    String msg = monster.getFormatName() + "攻击了" + hero.getFormatName() +
                            " 造成了" + StringUtils.formatNumber(harm) + "的伤害。";
                    skill.addMessage(msg);
                    monster.addBattleDesc(msg);
                    return false;
                }

            });
            skill.setLevelUp(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    if (skill.getProbability() < 25) {
                        skill.setProbability(skill.getProbability() + 2);
                        return true;
                    }
                    return true;
                }
            });
            if (!skill.load()) {
                skill.setProbability(6.5f);
            }
        } else if ("欺诈游戏".equalsIgnoreCase(name)) {
            final AttackSkill iskll = new AttackSkill();
            skill = iskll;
            skill.setName("欺诈游戏");
            skill.setHero(hero);
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return (skill.isActive() || hero.getSkillPoint() > 0);
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("玩的就是心跳。<br>攻击时抛一次硬币，如果为正面，对敌人造成N倍的攻击伤害。否则自己受到敌人攻击的N倍伤害。<br>其中N根据点击数计算（在殿堂中使用该技能时会修正为根据当前技能的使用+点击次数），取值范围[0,?]");
                    builder.append(skill.getProbability()).append("%的概率释放。");
                    return builder.toString();
                }
            });
            skill.setRelease(new UseExpression() {
                @Override
                public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                    long n = hero.getRandom().nextLong(hero.getClick() / 70000 + 15);
                    if (n > 10000) {
                        n = hero.getRandom().nextInt(10000);
                    }
                    boolean coinSide = hero.getRandom().nextBoolean();
                    String coin = hero.getFormatName() + "开启" + iskll.getName() + ",抛了一次硬币,结果为" + (coinSide ? "正" : "反");
                    skill.addMessage(coin);
                    monster.addBattleSkillDesc(coin);
                    if (!coinSide && SkillFactory.getSkill("欺诈师", hero).isActive()) {
                        coinSide = hero.getRandom().nextBoolean();
                        coin = "欺诈师" + hero.getFormatName() + "又抛了一次硬币,结果为" + (coinSide ? "正" : "反");
                        Achievement.swindler.enable(hero);
                        skill.addMessage(coin);
                        monster.addBattleSkillDesc(coin);
                    }

                    if (coinSide) {
                        long harm = n * hero.getAttackValue();
                        if (harm < 0) harm = hero.getRandom().nextLong(hero.getMaxMazeLev() + 1) + 1;
                        monster.addHp(-harm);
                        String msg = hero.getFormatName() + "攻击了" + monster.getFormatName() +
                                " 造成了" + StringUtils.formatNumber(harm) + "的伤害。";
                        skill.addMessage(msg);
                        monster.addBattleDesc(msg);
                    } else {
                        long harm = monster.getAtk() - hero.getDefenseValue();
                        if (harm < 0) {
                            harm = hero.getRandom().nextLong(hero.getMaxMazeLev() + 1) + 1;
                        }
                        if (hero.isParry()) {
                            String parrymsg = hero.getFormatName() + "成功格挡一次攻击，减少了当前受到的伤害！";
                            skill.addMessage(parrymsg);
                            monster.addBattleDesc(parrymsg);
                        }
                        harm *= n;
                        hero.addHp(-harm);
                        String msg = monster.getFormatName() + "攻击了" + hero.getFormatName() +
                                " 造成了" + StringUtils.formatNumber(harm) + "的伤害。";
                        skill.addMessage(msg);
                        monster.addBattleDesc(msg);
                    }
                    return false;
                }

            });
            skill.setLevelUp(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    if (skill.getProbability() < 45) {
                        skill.setProbability(skill.getProbability() + 5);
                        return true;
                    }
                    return true;
                }
            });
            if (!skill.load()) {
                skill.setProbability(10f);
            }
        } else if ("欺诈师".equalsIgnoreCase(name)) {
            final PropertySkill iskll = new PropertySkill();
            skill = iskll;
            skill.setName("欺诈师");
            skill.setHero(hero);
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return SkillFactory.getSkill("点攻", hero).isActive() || SkillFactory.getSkill("点防", hero).isActive();
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("机会还是有的，只要你脸皮厚。<br>当进行欺诈游戏时，如果抛出的硬币为反面的话，还可以不要脸的再抛一次。<br>");
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
                    return false;
                }
            });
            if (!skill.load()) {
                skill.setProbability(10f);
            }
        }
        if ("浮生百刃".equalsIgnoreCase(name)) {
            final AttackSkill iskll = new AttackSkill() {
                public void setActive(boolean active) {
                    if (active) {
                        long maxHp = MathUtils.getMaxValueByRiseAndLev(hero.MAX_HP_RISE, hero.getMaxMazeLev());
                        long maxDef = MathUtils.getMaxValueByRiseAndLev(hero.DEF_RISE, hero.getMaxMazeLev());
                        long maxAtk = MathUtils.getAvgValueByRiseAndLev(hero.ATR_RISE, hero.getMaxMazeLev());
                        long baseHarm = getHero().getRandom().nextLong((hero.getClick() +  (maxDef + maxAtk + maxHp)/3
                                + 1) + 100);
                        if (baseHarm < 0) {
                            baseHarm = Integer.MAX_VALUE / 100;
                        }
                        setBaseHarm(baseHarm);
                        long additionHarm = getHero().getRandom().nextLong((hero.getClick() + maxAtk + maxHp + maxDef
                                + 1) + 100);
                        if (additionHarm < 0) {
                            additionHarm = Integer.MAX_VALUE / 2;
                        }
                        setAdditionHarm(additionHarm);
                    }
                    super.setActive(active);
                }
            };
            skill = iskll;
            skill.setName("浮生百刃");
            skill.setHero(hero);
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return false;
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("无需技能点激活，每前进100层迷宫随机激活。<br>技能叠加的伤害数值在激活的时候随机生成。激活后每100层变换一次技能伤害值。<br>");
                    builder.append(skill.getProbability()).append("%的概率释放，造成").
                            append(!skill.isActive() ? "????" : (StringUtils.formatNumber((hero.getAttackValue() + iskll.getBaseHarm())) + " - " + StringUtils.formatNumber((hero.getAttackValue() + iskll.getBaseHarm() + iskll.getAdditionHarm())))).
                            append("的伤害。");
                    return builder.toString();
                }
            });
            skill.setRelease(new UseExpression() {
                @Override
                public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                    long harm = hero.getAttackValue() +
                            iskll.getBaseHarm() + hero.getRandom().nextLong(iskll.getAdditionHarm());
                    if (harm < 0) {
                        harm = Integer.MAX_VALUE;
                    }
                    monster.addHp(-harm);
                    String msg = hero.getFormatName() + "使用了技能" + skill.getName() +
                            "，对" + monster.getFormatName() + "造成了" + StringUtils.formatNumber(harm) + "的伤害。";
                    skill.addMessage(msg);
                    monster.addBattleSkillDesc(msg);
                    return false;
                }

            });
            skill.setLevelUp(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    if (skill.getProbability() < 25) {
                        skill.setProbability(skill.getProbability() + 3);
                        return true;
                    }
                    return true;
                }
            });
            if (!skill.load()) {
                skill.setProbability(6f);
            }
        } else if ("虚无吞噬".equalsIgnoreCase(name)) {
            final DefendSkill iskll = new DefendSkill();
            skill = iskll;
            skill.setName("虚无吞噬");
            skill.setHero(hero);
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return false;
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("无需技能点激活，每前进100层迷宫随机激活。<br>").
                            append("防御技能。敌方攻击的时候");
                    builder.append(skill.getProbability()).append("%的概率释放，抵消敌人的攻击。并且<br>抛一次硬币，正面就攻击敌人一次。欺诈师技能可以对这个技能生效。");
                    return builder.toString();
                }
            });
            skill.setRelease(new UseExpression() {
                @Override
                public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                    String msg = hero.getFormatName() + "使用了技能" + iskll.getName() + "抵消了" + monster.getFormatName() + "的攻击";
                    iskll.addMessage(msg);
                    monster.addBattleSkillDesc(msg);
                    boolean coinSide = hero.getRandom().nextBoolean();
                    String coin = hero.getFormatName() + "抛了一次硬币,结果为" + (coinSide ? "正" : "反");
                    skill.addMessage(coin);
                    monster.addBattleSkillDesc(coin);
                    if (!coinSide && SkillFactory.getSkill("欺诈师", hero).isActive()) {
                        coinSide = hero.getRandom().nextBoolean();
                        coin = "欺诈师" + hero.getFormatName() + "又抛了一次硬币,结果为" + (coinSide ? "正" : "反");
                        Achievement.swindler.enable(hero);
                        skill.addMessage(coin);
                        monster.addBattleSkillDesc(coin);
                    }
                    if (coinSide) {
                        long harm = hero.getAttackValue();
                        monster.addHp(-harm);
                        msg = hero.getFormatName() + "进行了一次攻击，对" + monster.getFormatName() + "造成了" + StringUtils.formatNumber(harm) + "的伤害。";
                        skill.addMessage(msg);
                        monster.addBattleDesc(msg);
                    }
                    return false;
                }

            });
            skill.setLevelUp(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    if (skill.getProbability() < 25) {
                        skill.setProbability(skill.getProbability() + 2);
                        return true;
                    }
                    return true;
                }
            });
            if (!skill.load()) {
                skill.setProbability(6.5f);
            }
        }
        return skill;
    }
}
