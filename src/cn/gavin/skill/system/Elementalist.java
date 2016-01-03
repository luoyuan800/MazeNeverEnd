package cn.gavin.skill.system;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import cn.gavin.Element;
import cn.gavin.Hero;
import cn.gavin.R;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.gift.Gift;
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
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 10/29/2015.
 */
public class Elementalist extends SkillLayout {
    public Elementalist(Context context) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.skill_layout_element, (ViewGroup) this.findViewById(R.id.element_skill_root));
        this.addView(view);
        this.view = view;
        this.context = context;
    }

    @Override
    public void init() {
        Skill skill = SkillFactory.getSkill("元素变换", hero);
        Button button = (Button) view.findViewById(R.id.element_skill_ysbh);
        button.setOnClickListener(buildOnClickListener(skill));
        skill.setSkillButton(button);
        skills.add(skill);

        skill = SkillFactory.getSkill("元素控制", hero);
        button = (Button) view.findViewById(R.id.element_skill_yskz);
        button.setOnClickListener(buildOnClickListener(skill));
        skill.setSkillButton(button);
        skills.add(skill);

        skill = SkillFactory.getSkill("元素星爆", hero);
        button = (Button) view.findViewById(R.id.element_skill_ysxb);
        button.setOnClickListener(buildOnClickListener(skill));
        skill.setSkillButton(button);
        skills.add(skill);

        skill = SkillFactory.getSkill("元素压制", hero);
        button = (Button) view.findViewById(R.id.element_skill_ysyz);
        button.setOnClickListener(buildOnClickListener(skill));
        skill.setSkillButton(button);
        skills.add(skill);

        skill = SkillFactory.getSkill("元素冰封", hero);
        button = (Button) view.findViewById(R.id.element_skill_ysbf);
        button.setOnClickListener(buildOnClickListener(skill));
        skill.setSkillButton(button);
        skills.add(skill);

        skill = SkillFactory.getSkill("元素防御", hero);
        button = (Button) view.findViewById(R.id.element_skill_ysfy);
        button.setOnClickListener(buildOnClickListener(skill));
        skill.setSkillButton(button);
        skills.add(skill);

        skill = SkillFactory.getSkill("元素虚无", hero);
        button = (Button) view.findViewById(R.id.element_skill_ysxvwu);
        button.setOnClickListener(buildOnClickListener(skill));
        skill.setSkillButton(button);
        skills.add(skill);

        skill = SkillFactory.getSkill("风刃", hero);
        button = (Button) view.findViewById(R.id.element_skill_fenren);
        button.setOnClickListener(buildOnClickListener(skill));
        skill.setSkillButton(button);
        skills.add(skill);

        skill = SkillFactory.getSkill("土遁", hero);
        button = (Button) view.findViewById(R.id.element_skill_td);
        button.setOnClickListener(buildOnClickListener(skill));
        skill.setSkillButton(button);
        skills.add(skill);

        skill = SkillFactory.getSkill("元素吸收", hero);
        button = (Button) view.findViewById(R.id.element_skill_ysxs);
        button.setOnClickListener(buildOnClickListener(skill));
        skill.setSkillButton(button);
        skills.add(skill);

        skill = SkillFactory.getSkill("元素使", hero);
        button = (Button) view.findViewById(R.id.element_skill_yss);
        button.setOnClickListener(buildOnClickListener(skill));
        skill.setSkillButton(button);
        skills.add(skill);

        skill = SkillFactory.getSkill("元素风暴", hero);
        button = (Button) view.findViewById(R.id.element_skill_ysfb);
        button.setOnClickListener(buildOnClickListener(skill));
        skill.setSkillButton(button);
        skills.add(skill);
    }

    public static Skill getSkill(String name, final Hero hero) {
        if (name.equals("元素变换")) {
            AttackSkill skill = new AttackSkill();
            skill.setHero(hero);
            skill.setName("元素变换");
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return (hero.getElement() != Element.无 && hero.getReincaCount() > 0) || hero.getGift() == Gift.Element;
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    AttackSkill attackSkill = (AttackSkill) skill;
                    StringBuilder builder = new StringBuilder();
                    builder.append("随心所欲，想换就换。转换自己的五行属性。所有元素使职业的技能都需要具有五行属性并且是一转之后才可以激活。<br>");
                    builder.append(attackSkill.getProbability()).append("%概率释放");
                    return builder.toString();
                }
            });

            skill.setRelease(new UseExpression() {
                @Override
                public boolean release(Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                    Element element = Element.values()[hero.getRandom().nextInt(Element.values().length)];
                    hero.setElement(element);
                    String msg = hero.getFormatName() + "使用技能" + skill.getName() + "将自己的属性转换成了" + element.name();
                    skill.addMessage(msg);
                    monster.addBattleSkillDesc(msg);
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
        } else if (name.equals("元素控制")) {
            final AttackSkill iskill = new AttackSkill();
            iskill.setHero(hero);
            iskill.setName("元素控制");
            iskill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return ((hero.getElement() != Element.无 && hero.getReincaCount() > 0) || hero.getGift() == Gift.Element) && SkillFactory.getSkill("元素变换", hero).isActive();
                }
            });
            iskill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    AttackSkill attackSkill = (AttackSkill) skill;
                    StringBuilder builder = new StringBuilder();
                    builder.append("在使用这个技能的时候，强制转换攻击属性为克制敌人的属性。<br>");
                    builder.append(attackSkill.getProbability()).append("%概率释放");
                    return builder.toString();
                }
            });

            iskill.setRelease(new UseExpression() {
                @Override
                public boolean release(Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                    long harm = hero.getAttackValue();
                    if (!hero.getElement().restriction(monster.getElement())) {
                        String msg = hero.getFormatName() + "将自己的攻击属性转换成了" + monster.getElement().getRestriction();
                        skill.addMessage(msg);
                        monster.addBattleSkillDesc(msg);
                    }
                    harm *= 1.5;
                    monster.addHp(-harm);
                    String msg = hero.getFormatName() + "攻击了" + monster.getFormatName() + "造成了" + harm + "点伤害";
                    skill.addMessage(msg);
                    monster.addBattleSkillDesc(msg);
                    return false;
                }
            });
            if (!iskill.load()) {
                iskill.setProbability(3);
            }
            iskill.setLevelUp(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    if (skill.getProbability() < 25) {
                        skill.setProbability(skill.getProbability() + 1f);
                    }
                    return false;
                }
            });
            return iskill;
        } else if (name.equals("元素吸收")) {
            final PropertySkill iskill = new PropertySkill();
            iskill.setHero(hero);
            iskill.setName("元素吸收");
            iskill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return ((hero.getElement() != Element.无 && hero.getReincaCount() > 0) || hero.getGift() == Gift.Element) && SkillFactory.getSkill("元素变换", hero).isActive();
                }
            });
            iskill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("每击败一个属性和你相同的敌人，你的生命会得到恢复");
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
        } else if (name.equals("元素压制")) {
            final AttackSkill iskill = new AttackSkill();
            iskill.setHero(hero);
            iskill.setName("元素压制");
            iskill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return ((hero.getElement() != Element.无 && hero.getReincaCount() > 0) || hero.getGift() == Gift.Element)
                            && SkillFactory.getSkill("元素防御", hero).isActive()
                            && SkillFactory.getSkill("元素控制", hero).isActive();
                }
            });
            iskill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    AttackSkill attackSkill = (AttackSkill) skill;
                    StringBuilder builder = new StringBuilder();
                    builder.append("当敌人的属性被你克制时，敌人受到的伤害为技能的基本伤害+敌人的攻击力。不可以和元素防御同时激活<br>");
                    builder.append(attackSkill.getProbability()).append("%概率释放<br>");
                    return builder.toString();
                }
            });

            iskill.setRelease(new UseExpression() {
                @Override
                public boolean release(Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                    long harm = hero.getAttackValue();
                    if (hero.getElement().restriction(monster.getElement())) {
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
                    if (skill.getProbability() < 20) {
                        skill.setProbability(skill.getProbability() + 1f);
                    }
                    return false;
                }
            });
            return iskill;
        } else if (name.equals("元素星爆")) {
            final AttackSkill iskill = new AttackSkill();
            iskill.setHero(hero);
            iskill.setName("元素星爆");
            iskill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return ((hero.getElement() != Element.无 && hero.getReincaCount() > 0) || hero.getGift() == Gift.Element) && SkillFactory.getSkill("元素压制", hero).isActive();
                }
            });
            iskill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    AttackSkill attackSkill = (AttackSkill) skill;
                    StringBuilder builder = new StringBuilder();
                    builder.append("元素大爆炸，亮瞎敌人的双眼之后秒杀他们。当敌人的属性被你克制时，秒杀敌人！<br>");
                    builder.append(attackSkill.getProbability()).append("%概率释放<br>");
                    return builder.toString();
                }
            });

            iskill.setRelease(new UseExpression() {
                @Override
                public boolean release(Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                    if (hero.getElement().restriction(monster.getElement())) {
                        String msg = hero.getFormatName() + "使用了技能" + skill.getName() + "秒杀了" + monster.getFormatName();
                        iskill.addMessage(msg);
                        monster.addBattleSkillDesc(msg);
                        monster.addHp(-monster.getHp());
                    } else {
                        long harm = hero.getAttackValue();
                        String s = hero.getFormatName() + "攻击了" + monster.getFormatName() + "造成了" + harm + "点伤害";
                        context.addMessage(s);
                        monster.addBattleDesc(s);
                        monster.addHp(-harm);
                    }
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
        } else if (name.equals("元素冰封")) {
            final AttackSkill iskill = new AttackSkill();
            iskill.setHero(hero);
            iskill.setName("元素冰封");
            iskill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return ((hero.getElement() != Element.无 && hero.getReincaCount() > 0) || hero.getGift() == Gift.Element) && SkillFactory.getSkill("元素星爆", hero).isActive();
                }
            });
            iskill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    AttackSkill attackSkill = (AttackSkill) skill;
                    StringBuilder builder = new StringBuilder();
                    builder.append("冰封敌人。只有当你的属性为水的时候，才会发动，冻住敌人随机回合数！具体是多少？你猜，反正比定身技能多……<br>");
                    builder.append(attackSkill.getProbability()).append("%概率释放<br>");
                    return builder.toString();
                }
            });

            iskill.setRelease(new UseExpression() {
                @Override
                public boolean release(Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                    if (hero.getElement() == Element.水) {
                        long turn = hero.getRandom().nextLong(Math.round(skill.getCount() / 1000f) + 1) + 1;
                        if (turn > 30) turn = 30;
                        monster.setHold(true);
                        monster.setHoldTurn(turn);
                        String msg2 = skill.format(hero.getFormatName() + "使用了技能" + skill.getName() + "冻住对方" + turn + "个回合");
                        context.addMessage(msg2);
                        monster.addBattleSkillDesc(msg2);
                    }
                    return false;
                }
            });
            if (!iskill.load()) {
                iskill.setProbability(8);
            }
            iskill.setLevelUp(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    if (skill.getProbability() < 25) {
                        skill.setProbability(skill.getProbability() + 1f);
                    }
                    return false;
                }
            });
            return iskill;
        } else if (name.equals("元素防御")) {
            final DefendSkill iskill = new DefendSkill();
            iskill.setHero(hero);
            iskill.setName("元素防御");
            iskill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return ((hero.getElement() != Element.无 && hero.getReincaCount() > 0) || hero.getGift() == Gift.Element)
                            && SkillFactory.getSkill("元素压制", hero).isActive()
                            && SkillFactory.getSkill("元素变换", hero).isActive();
                }
            });
            iskill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("当攻击的你的敌人的属性被你的五行属性克制的时候，免疫那个敌人的攻击。否则抵消50%的伤害。不可以和元素压制同时激活");
                    builder.append(skill.getProbability()).append("%概率释放<br>");
                    return builder.toString();
                }
            });

            iskill.setRelease(new UseExpression() {
                @Override
                public boolean release(Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                    if (hero.getElement().restriction(monster.getElement())) {
                        String msg2 = hero.getFormatName() + "使用了技能" + skill.getName() + "免疫了" + monster.getFormatName() + "的攻击";
                        context.addMessage(msg2);
                        monster.addBattleSkillDesc(msg2);
                    } else {
                        long harm = monster.getAtk() - hero.getDefenseValue();
                        if (harm <= 0) harm = hero.getRandom().nextLong(hero.getMaxMazeLev());
                        harm /= 2;
                        String msg = monster.getFormatName() + "攻击了" + hero.getFormatName() + "\n"
                                + hero.getFormatName() + "使用技能" + skill.getName() + "抵消了一半的伤害后受到了" + harm + "点伤害";
                        iskill.addMessage(msg);
                        monster.addBattleSkillDesc(msg);
                    }
                    return false;
                }
            });
            if (!iskill.load()) {
                iskill.setProbability(8);
            }
            iskill.setLevelUp(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    if (skill.getProbability() < 25) {
                        skill.setProbability(skill.getProbability() + 1f);
                    }
                    return false;
                }
            });
            return iskill;
        } else if (name.equals("元素虚无")) {
            final DefendSkill iskill = new DefendSkill();
            iskill.setHero(hero);
            iskill.setName("元素虚无");
            iskill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return ((hero.getElement() != Element.无 && hero.getReincaCount() > 0) || hero.getGift() == Gift.Element) && SkillFactory.getSkill("元素防御", hero).isActive();
                }
            });
            iskill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("触发这个技能的时候，当你的五行属性不是无的时候，即使敌人的攻击是致命攻击，你也不会挂掉。");
                    builder.append(skill.getProbability()).append("%概率释放<br>");
                    return builder.toString();
                }
            });

            iskill.setRelease(new UseExpression() {
                @Override
                public boolean release(Hero hero, Monster monster, MainGameActivity context, Skill skill) {

                    long harm = monster.getAtk() - hero.getDefenseValue();
                    if (harm <= 0) harm = hero.getRandom().nextLong(hero.getMaxMazeLev());
                    String msg = monster.getFormatName() + "攻击了" + hero.getFormatName() + "造成了" + harm + "点伤害";
                    iskill.addMessage(msg);
                    monster.addBattleSkillDesc(msg);
                    hero.addHp(-harm);
                    if (hero.getHp() <= harm && hero.getElement() != Element.无) {
                        String msg2 = hero.getFormatName() + "触发" + skill.getName() + "保留了1点HP";
                        iskill.addMessage(msg2);
                        monster.addBattleSkillDesc(msg2);
                        hero.setHp(1);
                    }
                    return false;
                }
            });
            if (!iskill.load()) {
                iskill.setProbability(10);
            }
            iskill.setLevelUp(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    if (skill.getProbability() < 30) {
                        skill.setProbability(skill.getProbability() + 2f);
                    }
                    return false;
                }
            });
            return iskill;
        } else if (name.equals("风刃")) {
            final DefendSkill iskill = new DefendSkill();
            iskill.setHero(hero);
            iskill.setName("风刃");
            iskill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return ((hero.getElement() != Element.无 && hero.getReincaCount() > 0) || hero.getGift() == Gift.Element) && SkillFactory.getSkill("元素虚无", hero).isActive();
                }
            });
            iskill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("当你的五行属性不是无的时候，敌人对你造成伤害的同时也会受到").
                            append(skill.getProbability() * 2 + 1).append("%的伤害");
                    builder.append(skill.getProbability()).append("%概率释放<br>");
                    return builder.toString();
                }
            });

            iskill.setRelease(new UseExpression() {
                @Override
                public boolean release(Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                    long harm = monster.getAtk() - hero.getDefenseValue();
                    if (harm <= 0) harm = hero.getRandom().nextLong(hero.getMaxMazeLev());
                    String msg = monster.getFormatName() + "攻击了" + hero.getFormatName() + "造成了" + harm + "点伤害";
                    iskill.addMessage(msg);
                    monster.addBattleSkillDesc(msg);
                    hero.addHp(-harm);
                    if (hero.getElement() != Element.无) {
                        String msg2 = hero.getFormatName() + "触发" + skill.getName() + "反伤了" + monster.getFormatName()
                                + harm + "点伤害";
                        iskill.addMessage(msg2);
                        monster.addBattleSkillDesc(msg2);
                        hero.setHp(1);
                    }
                    return false;
                }
            });
            if (!iskill.load()) {
                iskill.setProbability(13);
            }
            iskill.setLevelUp(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    if (skill.getProbability() < 40) {
                        skill.setProbability(skill.getProbability() + 2f);
                    }
                    return false;
                }
            });
            return iskill;
        } else if (name.equals("土遁")) {
            final DefendSkill iskill = new DefendSkill();
            iskill.setHero(hero);
            iskill.setName("土遁");
            iskill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return ((hero.getElement() != Element.无 && hero.getReincaCount() > 0) || hero.getGift() == Gift.Element) && SkillFactory.getSkill("风刃", hero).isActive();
                }
            });
            iskill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("当你的五行属性是土的时候，在受到敌人攻击的时候你可以随时钻地逃跑。");
                    builder.append(skill.getProbability()).append("%概率释放<br>");
                    return builder.toString();
                }
            });

            iskill.setRelease(new UseExpression() {
                @Override
                public boolean release(Hero hero, Monster monster, MainGameActivity context, Skill skill) {

                    if (hero.getElement() == Element.土) {
                        String msg2 = hero.getFormatName() + "触发" + skill.getName() + "逃跑了";
                        iskill.addMessage(msg2);
                        monster.addBattleSkillDesc(msg2);
                        return true;
                    } else {
                        long harm = monster.getAtk() - hero.getDefenseValue();
                        if (harm <= 0) harm = hero.getRandom().nextLong(hero.getMaxMazeLev());
                        String msg = monster.getFormatName() + "攻击了" + hero.getFormatName() + "造成了" + harm + "点伤害";
                        iskill.addMessage(msg);
                        monster.addBattleSkillDesc(msg);
                        hero.addHp(-harm);
                        return false;
                    }
                }
            });
            if (!iskill.load()) {
                iskill.setProbability(3);
            }
            iskill.setLevelUp(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    if (skill.getProbability() < 15) {
                        skill.setProbability(skill.getProbability() + 2f);
                    }
                    return false;
                }
            });
            return iskill;
        } else if (name.equals("元素使")) {
            final PropertySkill iskill = new PropertySkill();
            iskill.setHero(hero);
            iskill.setName("元素使");
            iskill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return ((hero.getElement() != Element.无 && hero.getReincaCount() > 0) || hero.getGift() == Gift.Element) && SkillFactory.getSkill("元素吸收", hero).isActive();
                }
            });
            iskill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("是时候告诉别人你的职业了！激活这个技能之后，你的五行相生相克效果翻倍！");
                    return builder.toString();
                }
            });

            iskill.setRelease(new UseExpression() {
                @Override
                public boolean release(Hero hero, Monster monster, MainGameActivity context, Skill skill) {

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
                        skill.setProbability(skill.getProbability() + 2f);
                    }
                    return false;
                }
            });
            return iskill;
        } else if (name.equals("元素风暴")) {
            final AttackSkill iskill = new AttackSkill();
            iskill.setName("元素风暴");
            iskill.setHero(hero);
            iskill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return ((hero.getElement() != Element.无 && hero.getReincaCount() > 0) || hero.getGift() == Gift.Element) && SkillFactory.getSkill("元素使", hero).isActive();
                }
            });
            iskill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    AttackSkill ds = (AttackSkill) skill;
                    StringBuilder builder = new StringBuilder();
                    builder.append("元素爆炸，只要敌人的属性不是无，就有几率触发这个高伤害技能。<br>");
                    builder.append(ds.getProbability()).append("%的概率释放，造成额外的").append(StringUtils.formatNumber(ds.getBaseHarm())).append(" - ").append(StringUtils.formatNumber(ds.getBaseHarm() + ds.getAdditionHarm())).append("伤害");
                    return builder.toString();
                }
            });

            iskill.setRelease(new UseExpression() {
                @Override
                public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                    if (monster.getElement() != Element.无) {
                        long harm = hero.getAttackValue() + ((AttackSkill) skill).getBaseHarm() + hero.getRandom().nextLong(((AttackSkill) skill).getAdditionHarm() + 1);
                        monster.addHp(-harm);
                        String msg = hero.getFormatName() + "使用了技能" + skill.getName() + "，对" + monster.getName() + "造成了" + StringUtils.formatNumber(harm) + "点伤害";
                        skill.addMessage(msg);
                        monster.addBattleSkillDesc(msg);
                    }
                    return false;
                }
            });
            if (!iskill.load()) {
                iskill.setProbability(5f);
                iskill.setBaseHarm(20000);
                iskill.setAdditionHarm(100000);
            }
            iskill.setLevelUp(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    AttackSkill attackSkill = (AttackSkill) skill;
                    if (skill.getProbability() < 20) {
                        skill.setProbability(skill.getProbability() + 2f);
                    }
                    long maxHp = MathUtils.getMaxValueByRiseAndLev(hero.MAX_HP_RISE, hero.getMaxMazeLev());
                    long maxDef = MathUtils.getMaxValueByRiseAndLev(hero.DEF_RISE, hero.getMaxMazeLev());
                    if (attackSkill.getBaseHarm() < maxDef * 13 && attackSkill.getAdditionHarm() < maxHp + maxDef * 5) {
                        attackSkill.setBaseHarm(attackSkill.getBaseHarm() * 4);
                        attackSkill.setAdditionHarm(attackSkill.getAdditionHarm() * 4);
                    }
                    return false;
                }
            });
        }
        return null;
    }
}
