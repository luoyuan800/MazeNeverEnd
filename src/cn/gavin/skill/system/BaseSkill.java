package cn.gavin.skill.system;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import cn.gavin.Achievement;
import cn.gavin.Hero;
import cn.gavin.R;
import cn.gavin.Sword;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.utils.MazeContents;
import cn.gavin.maze.Maze;
import cn.gavin.monster.Monster;
import cn.gavin.skill.Skill;
import cn.gavin.skill.SkillDialog;
import cn.gavin.skill.SkillFactory;
import cn.gavin.skill.expression.DescExpression;
import cn.gavin.skill.expression.EnableExpression;
import cn.gavin.skill.expression.UseExpression;
import cn.gavin.skill.type.AttackSkill;
import cn.gavin.skill.type.DefendSkill;
import cn.gavin.skill.type.PropertySkill;
import cn.gavin.utils.StringUtils;

/**
 * Created by luoyuan on 9/13/15.
 */
public class BaseSkill extends SkillLayout {
    public static String getSystemName() {
        return "勇者";
    }


    public BaseSkill(Context context) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.skill_layout_base, (ViewGroup) this.findViewById(R.id.base_skill_layout_root));
        this.addView(view);
        this.view = view;
        this.context = context;
    }

    public void init() {
        Skill skill = SkillFactory.getSkill("勇者之击", hero);
        Button button = (Button) view.findViewById(R.id.base_skill_r_button);
        button.setOnClickListener(buildOnClickListener(skill));
        skill.setSkillButton(button);
        skills.add(skill);

        skill = SkillFactory.getSkill("闪避", hero);
        button = (Button) view.findViewById(R.id.base_skill_s_button);
        button.setOnClickListener(buildOnClickListener(skill));
        skill.setSkillButton(button);
        skills.add(skill);

        skill = SkillFactory.getSkill("铁拳", hero);
        button = (Button) view.findViewById(R.id.skill_base_t_button);
        button.setOnClickListener(buildOnClickListener(skill));
        skill.setSkillButton(button);
        skills.add(skill);

        skill = SkillFactory.getSkill("反弹", hero);
        button = (Button) view.findViewById(R.id.skill_base_f_button);
        button.setOnClickListener(buildOnClickListener(skill));
        skill.setSkillButton(button);
        skills.add(skill);

        skill = SkillFactory.getSkill("巨大化", hero);
        button = (Button) view.findViewById(R.id.skill_base_j_button);
        button.setOnClickListener(buildOnClickListener(skill));
        skill.setSkillButton(button);
        skills.add(skill);

        skill = SkillFactory.getSkill("定身", hero);
        button = (Button) view.findViewById(R.id.skill_base_d_button);
        button.setOnClickListener(buildOnClickListener(skill));
        skill.setSkillButton(button);
        skills.add(skill);

        skill = SkillFactory.getSkill("超能量", hero);
        button = (Button) view.findViewById(R.id.skill_base_c_button);
        button.setOnClickListener(buildOnClickListener(skill));
        skill.setSkillButton(button);
        skills.add(skill);

        skill = SkillFactory.getSkill("瞬间移动", hero);
        button = (Button) view.findViewById(R.id.skill_base_sy_button);
        button.setOnClickListener(buildOnClickListener(skill));
        skill.setSkillButton(button);
        skills.add(skill);

        skill = SkillFactory.getSkill("斩击", hero);
        button = (Button) view.findViewById(R.id.skill_base_zj_button);
        button.setOnClickListener(buildOnClickListener(skill));
        skill.setSkillButton(button);
        skills.add(skill);

        skill = SkillFactory.getSkill("裂空剑", hero);
        button = (Button) view.findViewById(R.id.skill_base_lkj_button);
        button.setOnClickListener(buildOnClickListener(skill));
        skill.setSkillButton(button);
        skills.add(skill);

        skill = SkillFactory.getSkill("传送", hero);
        button = (Button) view.findViewById(R.id.skill_base_cs_button);
        button.setOnClickListener(buildOnClickListener(skill));
        skill.setSkillButton(button);
        skills.add(skill);

        skill = SkillFactory.getSkill("错位", hero);
        button = (Button) view.findViewById(R.id.skill_base_cw_button);
        button.setOnClickListener(buildOnClickListener(skill));
        skill.setSkillButton(button);
        skills.add(skill);

        skill = SkillFactory.getSkill("原能力", hero);
        button = (Button) view.findViewById(R.id.skill_base_ynl_button);
        button.setOnClickListener(buildOnClickListener(skill));
        skill.setSkillButton(button);
        skills.add(skill);

        skill = SkillFactory.getSkill("寻宝", hero);
        button = (Button) view.findViewById(R.id.skill_base_xb_button);
        button.setOnClickListener(buildOnClickListener(skill));
        skill.setSkillButton(button);
        skills.add(skill);

        skill = SkillFactory.getSkill("超防御", hero);
        button = (Button) view.findViewById(R.id.skill_base_cfy_button);
        button.setOnClickListener(buildOnClickListener(skill));
        skill.setSkillButton(button);
        skills.add(skill);

        skill = SkillFactory.getSkill("重击", hero);
        button = (Button) view.findViewById(R.id.skill_base_hit_button);
        button.setOnClickListener(buildOnClickListener(skill));
        skill.setSkillButton(button);
        skills.add(skill);
    }

    public static Skill getSkill(String name, Hero hero) {
        Skill skill = null;
        if (name.equals("勇者之击")) {
            AttackSkill attackSkill = new AttackSkill();
            skill = attackSkill;
            skill.setHero(hero);
            skill.setName("勇者之击");
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return (skill.isActive() || hero.getSkillPoint() > 0) && !SkillFactory.getSkill("魔王天赋", hero).isActive();
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    AttackSkill attackSkill = (AttackSkill) skill;
                    StringBuilder builder = new StringBuilder();
                    builder.append("勇者的基本技能，学会了才能踏上征途。<br>");
                    builder.append(attackSkill.getProbability()).append("%概率释放<br>");
                    builder.append("造成额外的").append(StringUtils.formatNumber(attackSkill.getBaseHarm())).append(" - ").
                            append(StringUtils.formatNumber(attackSkill.getBaseHarm() + attackSkill.getAdditionHarm())).append("伤害").append("<br>").
                            append("不可与魔王技能同时激活");
                    return builder.toString();
                }
            });

            skill.setRelease(new UseExpression() {
                @Override
                public boolean release(Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                    long addition = (((AttackSkill) skill).getAdditionHarm()) + ((AttackSkill) skill).getBaseHarm();
                    if (addition < 0) addition = ((AttackSkill) skill).getAdditionHarm();
                    long harm = hero.getAttackValue() + ((AttackSkill) skill).getBaseHarm() + hero.getRandom().nextLong(addition + 1);
                    if(harm <= 0){
                        ((AttackSkill) skill).setAdditionHarm(Integer.MAX_VALUE - 10000);
                        harm = hero.getRandom().nextLong(addition) + 1;
                    }
                    if(harm > hero.getBaseAttackValue() * 10){
                        harm/=10;
                    }
                    String skillmsg = skill.format(hero.getFormatName() + "使用了技能" + skill.getName() + "对" + monster.getFormatName() + "造成了" + StringUtils.formatNumber(harm) + "点伤害");
                    context.addMessage(skillmsg);
                    monster.addBattleSkillDesc(skillmsg);
                    monster.addHp(-harm);
                    Achievement.hero.enable(hero);
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
                    if (skill.getProbability() < 25) {
                        skill.setProbability(skill.getProbability() + 1.3f);
                    }
                    if (as.getBaseHarm() < hero.getBaseAttackValue() * 50 && as.getAdditionHarm() < Integer.MAX_VALUE) {
                        as.setBaseHarm(as.getBaseHarm() + hero.getRandom().nextLong(hero.getDefenseValue() / 50 + 1));
                        as.setAdditionHarm(as.getAdditionHarm() * 4);
                    }
                    if(as.getBaseHarm() > as.getAdditionHarm() -20){
                        as.setBaseHarm(as.getAdditionHarm() -20);
                    }
                    return false;
                }
            });
        } else if (name.equals("闪避")) {
            skill = new DefendSkill();
            skill.setName("闪避");
            skill.setHero(hero);
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return (skill.isActive() || hero.getSkillPoint() > 0) && SkillFactory.getSkill("勇者之击", hero).isActive();
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
                    String msg1 = skill.format(monster.getFormatName() + "攻击" + hero.getFormatName());
                    context.addMessage(msg1);
                    String msg2 = skill.format(hero.getFormatName() + "使用技能" + skill.getName() + "躲过了攻击");
                    context.addMessage(msg2);
                    monster.addBattleSkillDesc(msg1);
                    monster.addBattleSkillDesc(msg2);
                    return false;
                }
            });
            if (!skill.load()) {
                skill.setProbability(2.0f);
            }
            skill.setLevelUp(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    if (skill.getProbability() < 10) {
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
                    return (skill.isActive() || hero.getSkillPoint() > 0) && SkillFactory.getSkill("超能量", hero).isActive();
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
                    String msg = skill.format(hero.getFormatName() + "使用技能" + skill.getName() + "攻击" + monster.getFormatName() + "造成了" + StringUtils.formatNumber(harm) + "点伤害");
                    context.addMessage(msg);
                    monster.addBattleSkillDesc(msg);
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
                    if (skill.getProbability() < 25) {
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
                    return (skill.isActive() || hero.getSkillPoint() > 0) && SkillFactory.getSkill("勇者之击", hero).isActive();
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    AttackSkill as = (AttackSkill) skill;
                    StringBuilder builder = new StringBuilder();
                    builder.append("无视防御进行攻击。<br>");
                    builder.append(as.getProbability()).append("%的概率释放<br>");
                    builder.append("使用技能后，有").append(100 - as.getProbability()*2).append("%概率使得敌人眩晕");
                    return builder.toString();
                }
            });

            skill.setRelease(new UseExpression() {
                @Override
                public boolean release(Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                    long harm = hero.getUpperAtk() + hero.getRandom().nextLong(hero.getStrength() / 100 + 1);
                    String msg1 = skill.format(hero.getFormatName() + "使用技能" + skill.getName() + "攻击" + monster.getFormatName() + "造成了" + StringUtils.formatNumber(harm) + "点伤害");
                    context.addMessage(msg1);
                    monster.addBattleSkillDesc(msg1);
                    monster.addHp(-harm);
                    if (hero.getRandom().nextLong(100) > skill.getProbability() * 2) {
                        String msg2 = skill.format(monster.getFormatName() + "被打晕了");
                        context.addMessage(msg2);
                        monster.addBattleSkillDesc(msg2);
                        try {
                            Thread.sleep(context.getRefreshInfoSpeed());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        harm = hero.getAttackValue() + hero.getRandom().nextLong(hero.getStrength() / 100 + 1);
                        String msg3 = skill.format(hero.getFormatName() + "攻击眩晕中的" + monster.getFormatName() + "造成了" + StringUtils.formatNumber(harm) + "点伤害");
                        context.addMessage(msg3);
                        monster.addBattleSkillDesc(msg3);
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
                    if (skill.getProbability() < 20) {
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
                    return (skill.isActive() || hero.getSkillPoint() > 0) && SkillFactory.getSkill("闪避", hero).isActive();
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("防御技能<br>");
                    builder.append(skill.getProbability()).append("%概率反弹").append(50 + skill.getProbability() * 5).append("%的攻击伤害");
                    return builder.toString();
                }
            });

            skill.setRelease(new UseExpression() {
                @Override
                public boolean release(Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                    long harm = monster.getAtk() - hero.getDefenseValue();
                    if (harm <= 0) {
                        harm = hero.getRandom().nextLong(hero.getMaxMazeLev() + 1);
                    }
                    String msg1 = skill.format(monster.getFormatName() + "攻击" + hero.getFormatName());
                    context.addMessage(msg1);
                    monster.addBattleSkillDesc(msg1);
                    long rHarm = Math.round(harm * ((50f + skill.getProbability() * 5f) / 100f));
                    String msg2 = skill.format(hero.getFormatName() + "使用技能" + skill.getName() + "反弹了" + StringUtils.formatNumber(rHarm) + "的伤害");
                    context.addMessage(msg2);
                    monster.addBattleSkillDesc(msg2);
                    monster.addHp(-rHarm);
                    if (rHarm < harm) {
                        hero.addHp(-(harm - rHarm));
                    }
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
                        skill.setProbability(skill.getProbability() + 1.5f);
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
                    return (skill.isActive() || hero.getSkillPoint() > 0) && (SkillFactory.getSkill("闪避", hero).isActive() || SkillFactory.getSkill("铁拳", hero).isActive());
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("防御技能<br>");
                    builder.append(skill.getProbability()).append("%概率在受到伤害后持续一段时间增幅0 - ").append(skill.getProbability() * 5 + 25).append("%的攻击力");
                    return builder.toString();
                }
            });

            skill.setRelease(new UseExpression() {
                @Override
                public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                    long harm = monster.getAtk() - hero.getDefenseValue();
                    if (harm <= 0) {
                        harm = hero.getRandom().nextLong(hero.getMaxMazeLev() + 1);
                    }
                    hero.addHp(-harm);
                    String msg1 = skill.format(monster.getFormatName() + "攻击了" + hero.getFormatName() + "造成了" + StringUtils.formatNumber(harm) + "点伤害");
                    context.addMessage(msg1);
                    monster.addBattleSkillDesc(msg1);
                    if (!hero.isOnSkill()) {
                        final long atk = hero.getRandom().nextLong(Math.round(hero.getAttackValue() + hero.getAttackValue() * (25f + skill.getProbability() * 5f) / 100f) + 1);
                        hero.setSkillAdditionAtk(atk);
                        String msg2 = skill.format(hero.getFormatName() + "触发了" + skill.getName() + "攻击力增加了" + StringUtils.formatNumber(atk));
                        context.addMessage(msg2);
                        monster.addBattleSkillDesc(msg2);
                        hero.setOnSkill(true);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(MainGameActivity.context.getRefreshInfoSpeed() * (hero.getRandom().nextLong(5) + 1));
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                hero.setOnSkill(false);
                            }
                        }).start();
                    }
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
                    return (skill.isActive() || hero.getSkillPoint() > 0) && (SkillFactory.getSkill("铁拳", hero).isActive());
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("防御技能<br>");
                    builder.append(skill.getProbability()).append("%概率在受到攻击后持续一段时间使得对方不可动弹。");
                    return builder.toString();
                }
            });

            skill.setRelease(new UseExpression() {
                @Override
                public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                    long harm = monster.getAtk() - hero.getDefenseValue();
                    if (harm <= 0) {
                        harm = hero.getRandom().nextLong(hero.getMaxMazeLev() + 1);
                    }
                    hero.addHp(-harm);
                    String msg1 = skill.format(monster.getFormatName() + "攻击了" + hero.getFormatName() + "造成了" + harm + "点伤害");
                    context.addMessage(msg1);
                    monster.addBattleSkillDesc(msg1);
                    long turn = hero.getRandom().nextLong(Math.round(skill.getCount() / 2000f) + 1) + 1;
                    if(turn > 20) turn = 20;
                    monster.setHold(true);
                    monster.setHoldTurn(turn);
                    String msg2 = skill.format(hero.getFormatName() + "触发了" + skill.getName() + "定住对方" + turn + "个回合");
                    context.addMessage(msg2);
                    monster.addBattleSkillDesc(msg2);
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
                            && (SkillFactory.getSkill("反弹", hero).isActive() ||
                            SkillFactory.getSkill("巨大化", hero).isActive() ||
                            SkillFactory.getSkill("定身", hero).isActive());
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    DefendSkill ds = (DefendSkill) skill;
                    StringBuilder builder = new StringBuilder();
                    builder.append("防御技能<br>");
                    builder.append(ds.getProbability()).append("%概率在受到伤害后持续一段时间增幅0 - ").append(ds.getProbability() * 5 + 80).append("%的生命值上限");
                    return builder.toString();
                }
            });

            skill.setRelease(new UseExpression() {
                @Override
                public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                    long harm = monster.getAtk() - hero.getDefenseValue();
                    if (harm <= 0) {
                        harm = hero.getRandom().nextLong(hero.getMaxMazeLev() + 1);
                    }
                    hero.addHp(-harm);
                    String msg1 = skill.format(monster.getFormatName() + "攻击了" + hero.getFormatName() + "造成了" + StringUtils.formatNumber(harm) + "点伤害");
                    context.addMessage(msg1);
                    monster.addBattleSkillDesc(msg1);
                    if (hero.isOnSkill()) {
                        final long hp = hero.getRandom().nextLong(Math.round(hero.getAttackValue() + hero.getAttackValue() * (80f + skill.getProbability() * 5f) / 100f) + 1);
                        hero.setSkillAdditionHp(hp);
                        hero.setOnSkill(true);
                        String msg2 = skill.format(hero.getFormatName() + "触发了" + skill.getName() + "生命值上限增加了" + StringUtils.formatNumber(hp));
                        context.addMessage(msg2);
                        monster.addBattleSkillDesc(msg2);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(MainGameActivity.context.getRefreshInfoSpeed() * (hero.getRandom().nextLong(10) + 1));
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                hero.setOnSkill(false);
                            }
                        }).start();
                    }
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
                            && SkillFactory.getSkill("超能量", hero).isActive();
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("被动技能<br>");
                    builder.append(skill.getProbability()).append("%的概率在受到致命伤害前脱离战斗");
                    return builder.toString();
                }
            });

            skill.setRelease(new UseExpression() {
                @Override
                public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                    String msg = skill.format(hero.getFormatName() + "触发了" + skill.getName() + "，逃离了战斗\n-----------------------------");
                    context.addMessage(msg);
                    monster.addBattleSkillDesc(msg);
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
                    if (MainGameActivity.context != null) {
                        if (use && !onUsed)
                            MazeContents.getMaze().setCsmgl(MazeContents.getMaze().getCsmgl() - Math.round(getProbability()));
                        else if (!use && onUsed)
                            MazeContents.getMaze().setCsmgl(MazeContents.getMaze().getCsmgl() + Math.round(getProbability()));
                    }
                }
            };
            skill.setName("传送");
            skill.setHero(hero);
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return (skill.isActive() || hero.getSkillPoint() > 0)
                            && SkillFactory.getSkill("瞬间移动", hero).isActive();
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("我的目的地是星辰大海！<br>");
                    builder.append("增加踩到传送门的几率");
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
                skill.setProbability(60f);
            }
            skill.setLevelUp(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    if (skill.getProbability() < 400) {
                        skill.setProbability(skill.getProbability() + 30f);
                        MazeContents.getMaze().setCsmgl(MazeContents.getMaze().getCsmgl() - 50);
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
                            && SkillFactory.getSkill("超能量", hero).isActive() && (Sword.valueOf(hero.getSword()).ordinal() > Sword.金剑.ordinal());
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    AttackSkill ds = (AttackSkill) skill;
                    StringBuilder builder = new StringBuilder();
                    builder.append("高阶剑技能，需要装备武器阶位高于金剑才可以激活<br>");
                    builder.append(ds.getProbability()).append("%的概率释放，造成额外的").append(StringUtils.formatNumber(ds.getBaseHarm())).append(" - ").append(StringUtils.formatNumber(ds.getBaseHarm() + ds.getAdditionHarm())).append("伤害");
                    return builder.toString();
                }
            });

            skill.setRelease(new UseExpression() {
                @Override
                public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                    long harm = hero.getAttackValue() + ((AttackSkill) skill).getBaseHarm() + hero.getRandom().nextLong(((AttackSkill) skill).getAdditionHarm() + 1);
                    monster.addHp(-harm);
                    String msg = hero.getFormatName() + "使用了技能" + skill.getName() + "，对" + monster.getName() + "造成了" + StringUtils.formatNumber(harm) + "点伤害";
                    skill.addMessage(msg);
                    monster.addBattleSkillDesc(msg);
                    return false;
                }
            });
            if (!skill.load()) {
                skill.setProbability(5f);
                AttackSkill attk = (AttackSkill) skill;
                attk.setBaseHarm(10000);
                attk.setAdditionHarm(80000);
            }
            skill.setLevelUp(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    AttackSkill attackSkill = (AttackSkill) skill;
                    if (skill.getProbability() < 20) {
                        skill.setProbability(skill.getProbability() + 2f);
                        attackSkill.setBaseHarm(attackSkill.getBaseHarm() * 2);
                    }
                    if (attackSkill.getAdditionHarm() < hero.getBaseAttackValue() * 10 && attackSkill.getAdditionHarm() < Integer.MAX_VALUE) {
                        attackSkill.setAdditionHarm(attackSkill.getAdditionHarm() * 2);
                    }
                    return false;
                }
            });
        } else if (name.equals("原能力")) {
            DefendSkill defendSkill = new DefendSkill();
            skill = defendSkill;
            defendSkill.setName("原能力");
            defendSkill.setHero(hero);
            defendSkill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return (skill.isActive() || hero.getSkillPoint() > 0)
                            && SkillFactory.getSkill("裂空剑", hero).isActive();
                }
            });
            defendSkill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("防御技能<br>");
                    builder.append(skill.getProbability()).append("的概率释放，在受到伤害后恢复全部HP（在殿堂中这个技能会修正为在收到伤害前恢复HP）.");
                    return builder.toString();
                }
            });

            defendSkill.setRelease(new UseExpression() {
                @Override
                public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                    long harm = monster.getAtk() - hero.getDefenseValue();
                    if (harm < 0) {
                        harm = hero.getRandom().nextLong(hero.getMaxMazeLev() + 1);
                    }
                    hero.addHp(-harm);
                    if (hero.getHp() <= 0) {
                        return false;
                    }
                    String msg = monster.getFormatName() + "攻击了" + hero.getFormatName() + "，造成了" + StringUtils.formatNumber(harm) + "的伤害";
                    skill.addMessage(msg);
                    monster.addBattleSkillDesc(msg);
                    hero.restore();
                    msg = hero.getFormatName() + "使用了技能" + skill.getName() + "，HP全部恢复了";
                    skill.addMessage(msg);
                    monster.addBattleSkillDesc(msg);
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
            skill = iSkill;
            iSkill.setName("重击");
            iSkill.setHero(hero);
            iSkill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return (skill.isActive() || hero.getSkillPoint() > 0)
                            && SkillFactory.getSkill("原能力", hero).isActive();
                }
            });
            iSkill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    AttackSkill aSkill = (AttackSkill) skill;
                    StringBuilder builder = new StringBuilder();
                    builder.append("返璞归真<br>");
                    builder.append(skill.getProbability()).append("的概率释放，造成1-").append(aSkill.getBaseHarm()).append("倍伤害");
                    return builder.toString();
                }
            });
            iSkill.setRelease(new UseExpression() {
                @Override
                public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                    long n = hero.getRandom().nextLong(((AttackSkill) skill).getBaseHarm() + 1) + 1;
                    long harm = n * hero.getAttackValue();
                    if(harm <= 0){
                        ((AttackSkill) skill).setBaseHarm(n/2);
                        harm = hero.getAttackValue();
                    }
                    monster.addHp(-harm);
                    String msg = hero.getFormatName() + "使用了技能" + skill.getName() + "，对" + monster.getFormatName() + "造成了" + StringUtils.formatNumber(harm) + "伤害";
                    skill.addMessage(msg);
                    monster.addBattleSkillDesc(msg);
                    Achievement.R_hero.enable(hero);
                    return false;
                }
            });
            if (!iSkill.load()) {
                iSkill.setProbability(5f);
                iSkill.setBaseHarm(2);
            }
            iSkill.setLevelUp(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    if (skill.getProbability() < 20) {
                        skill.setProbability(skill.getProbability() + 1f);
                        ((AttackSkill) skill).setBaseHarm(((AttackSkill) skill).getBaseHarm() + 2);
                        return true;
                    }
                    return false;
                }
            });
        } else if (name.equals("寻宝")) {
            final PropertySkill iskll = new PropertySkill(0, 0, 0, 0, 0, 0, 0) {
                public void setOnUsed(boolean use) {
                    if (MainGameActivity.context != null && !onUsed && use) {
                        MainGameActivity.context.getMaze().setHunt(MainGameActivity.context.getMaze().getHunt() * 2);
                    } else if (MainGameActivity.context != null && onUsed && !use) {
                        MainGameActivity.context.getMaze().setHunt(MainGameActivity.context.getMaze().getHunt() / 2);
                    }
                    super.setOnUsed(use);
                }
            };
            skill = iskll;
            iskll.setName("寻宝");
            iskll.setHero(hero);
            iskll.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return (skill.isActive() || hero.getSkillPoint() > 0)
                            && SkillFactory.getSkill("传送", hero).isActive();
                }
            });
            iskll.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("看，那里有一个箱子，悄悄爬过去抱走！<br>");
                    builder.append("永久增加获得宝箱的概率");
                    return builder.toString();
                }
            });

            iskll.setRelease(new UseExpression() {
                @Override
                public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                    return false;
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
        } else if (name.equals("错位")) {
            final AttackSkill iskll = new AttackSkill();
            skill = iskll;
            iskll.setName("错位");
            iskll.setHero(hero);
            iskll.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return (skill.isActive() || hero.getSkillPoint() > 0)
                            && SkillFactory.getSkill("斩击", hero).isActive();
                }
            });
            iskll.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("不知道为什么就是想把别人的东西拿过来……当然我这是等价交换而已。<br>");
                    builder.append("当敌方生命值比自己HP高的时候，有").append(skill.getProbability()).append("%的概率交换双方的HP");
                    return builder.toString();
                }
            });

            iskll.setRelease(new UseExpression() {
                @Override
                public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                    if (monster.getHp() > hero.getHp()) {
                        long p = hero.getHp();
                        hero.setHp(monster.getHp());
                        monster.addHp(-monster.getHp() + p);
                        String msg = hero.getFormatName() + "使用了技能" + skill.getName() + "，和" + monster.getFormatName() + "交换了HP";
                        skill.addMessage(msg);
                        monster.addBattleSkillDesc(msg);
                    } else {
                        long harm = hero.getAttackValue();
                        monster.addHp(-harm);
                        String msg1 = hero.getFormatName() + "攻击了" + monster.getFormatName() + "造成了" + StringUtils.formatNumber(harm) + "点伤害";
                        MainGameActivity.context.addMessage(msg1);
                        monster.addBattleSkillDesc(msg1);
                    }
                    return false;
                }
            });
            if (!iskll.load()) {
                iskll.setProbability(1f);
            }
            iskll.setLevelUp(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    if (iskll.getProbability() < 10) {
                        iskll.setProbability(iskll.getProbability() + 1);
                    }
                    return true;
                }
            });
        } else if (name.equals("超防御")) {
            final PropertySkill iskll = new PropertySkill(0, 0, 0, 0, 0, 0, 0) {
                public void setOnUsed(boolean used) {
                    if(!onUsed && used){
                        setDef(getHero().getBaseDefense());
                    } if(onUsed && !used){
                        setDef(getHero().getBaseDefense()/2);
                    }
                    super.setOnUsed(used);
                }
            };
            skill = iskll;
            iskll.setName("超防御");
            iskll.setHero(hero);
            iskll.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return (skill.isActive() || hero.getSkillPoint() > 0)
                            && SkillFactory.getSkill("错位", hero).isActive();
                }
            });
            iskll.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("防御至上，这技能的bug已经突破天际了。<br>").append("当激活该技能时，当前的基础防御力翻倍。当取消激活该技能时，当前的基础防御力减半");
                    return builder.toString();
                }
            });

            iskll.setRelease(new UseExpression() {
                @Override
                public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {

                    return false;
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
        return skill;
    }
}
