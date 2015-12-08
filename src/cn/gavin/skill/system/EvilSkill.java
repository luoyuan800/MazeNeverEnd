package cn.gavin.skill.system;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import cn.gavin.Achievement;
import cn.gavin.Armor;
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
import cn.gavin.skill.type.EvilHide;
import cn.gavin.skill.type.EvilReKill;
import cn.gavin.skill.type.PropertySkill;
import cn.gavin.utils.StringUtils;

/**
 * Created by luoyuan on 9/28/15.
 */
public class EvilSkill extends SkillLayout {

    public EvilSkill(Context context) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.skill_layout_devils, (ViewGroup) this.findViewById(R.id.skill_dialog));
        this.addView(view);
        this.view = view;
        this.context = context;
    }

    public void init() {
        Skill skill = SkillFactory.getSkill("魔王天赋", hero);
        Button button = (Button) view.findViewById(R.id.skill_evil_m_button);
        skill.setSkillButton(button);
        button.setOnClickListener(buildOnClickListener(skill));
        skills.add(skill);

        skill = SkillFactory.getSkill("腐蚀", hero);
        button = (Button) view.findViewById(R.id.skill_evil_f_button);
        skill.setSkillButton(button);
        button.setOnClickListener(buildOnClickListener(skill));
        skills.add(skill);

        skill = SkillFactory.getSkill("强化", hero);
        button = (Button) view.findViewById(R.id.skill_evil_q_button);
        skill.setSkillButton(button);
        button.setOnClickListener(buildOnClickListener(skill));
        skills.add(skill);

        skill = SkillFactory.getSkill("迷雾", hero);
        button = (Button) view.findViewById(R.id.skill_evil_mw_button);
        skill.setSkillButton(button);
        button.setOnClickListener(buildOnClickListener(skill));
        skills.add(skill);

        skill = SkillFactory.getSkill("闪电", hero);
        button = (Button) view.findViewById(R.id.skill_evil_s_button);
        skill.setSkillButton(button);
        button.setOnClickListener(buildOnClickListener(skill));
        skills.add(skill);

        skill = SkillFactory.getSkill("隐身", hero);
        button = (Button) view.findViewById(R.id.skill_evil_y_button);
        skill.setSkillButton(button);
        button.setOnClickListener(buildOnClickListener(skill));
        skills.add(skill);

        skill = SkillFactory.getSkill("水波", hero);
        button = (Button) view.findViewById(R.id.skill_evil_sb_button);
        skill.setSkillButton(button);
        button.setOnClickListener(buildOnClickListener(skill));
        skills.add(skill);

        skill = SkillFactory.getSkill("精神力", hero);
        button = (Button) view.findViewById(R.id.skill_evil_j_button);
        skill.setSkillButton(button);
        button.setOnClickListener(buildOnClickListener(skill));
        skills.add(skill);

        skill = SkillFactory.getSkill("反杀", hero);
        button = (Button) view.findViewById(R.id.skill_evil_fs_button);
        skill.setSkillButton(button);
        button.setOnClickListener(buildOnClickListener(skill));
        skills.add(skill);

        skill = SkillFactory.getSkill("不死之身", hero);
        button = (Button) view.findViewById(R.id.skill_evil_b_button);
        skill.setSkillButton(button);
        button.setOnClickListener(buildOnClickListener(skill));
        skills.add(skill);

        skill = SkillFactory.getSkill("变身", hero);
        button = (Button) view.findViewById(R.id.skill_evil_bs_button);
        skill.setSkillButton(button);
        button.setOnClickListener(buildOnClickListener(skill));
        skills.add(skill);

        skill = SkillFactory.getSkill("生命吸收", hero);
        button = (Button) view.findViewById(R.id.skill_evil_sm_button);
        skill.setSkillButton(button);
        button.setOnClickListener(buildOnClickListener(skill));
        skills.add(skill);

        skill = SkillFactory.getSkill("多重攻击", hero);
        button = (Button) view.findViewById(R.id.skill_evil_mul_button);
        skill.setSkillButton(button);
        button.setOnClickListener(buildOnClickListener(skill));
        skills.add(skill);
    }

    public static Skill getSkill(String name, Hero hero) {
        Skill skill = null;
        if (name.equals("腐蚀")) {
            skill = new AttackSkill();
            skill.setName("腐蚀");
            skill.setHero(hero);
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return hero.getSkillPoint() > 0 && SkillFactory.getSkill("魔王天赋", hero).isActive();
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append(skill.getProbability()).append("%的概率释放，敌方的生命值减半。");
                    return builder.toString();
                }
            });
            skill.setRelease(new UseExpression() {
                @Override
                public boolean release(Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                    monster.addHp(-(monster.getHp() / 2));
                    String msg = hero.getFormatName() + "使用了技能" + skill.getName() + "，使得" + monster.getFormatName() + "的生命值减半了。";
                    skill.addMessage(msg);
                    monster.addBattleSkillDesc(msg);
                    Achievement.devils.enable(hero);
                    return false;
                }

            });
            skill.setLevelUp(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    if (skill.getProbability() > 25) {
                        return false;
                    } else {
                        skill.setProbability(skill.getProbability() + 1.1f);
                        return true;
                    }
                }
            });
            if (!skill.load()) {
                skill.setProbability(2.2f);
            }
        } else if (name.equals("魔王天赋")) {
            skill = new DefendSkill();
            skill.setName("魔王天赋");
            skill.setHero(hero);
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return hero.getSkillPoint() > 0 && !SkillFactory.getSkill("勇者之击", hero).isActive();
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
            skill.setRelease(new UseExpression() {
                @Override
                public boolean release(Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                    long harm = monster.getAtk() - hero.getDefenseValue();
                    if (harm <= 0) {
                        harm = hero.getRandom().nextLong(hero.getMaxMazeLev() + 1) + 1;
                    }
                    hero.addHp(harm);
                    String msg1 = monster.getFormatName() + "攻击了" + hero.getFormatName();
                    skill.addMessage(msg1);
                    monster.addBattleSkillDesc(msg1);
                    String msg2 = hero.getFormatName() + "使用了技能" + skill.getName() + "将" + StringUtils.formatNumber(harm) + "点伤害转化为生命";
                    skill.addMessage(msg2);
                    monster.addBattleSkillDesc(msg2);
                    Achievement.devils.enable(hero);
                    return false;
                }

            });
            skill.setLevelUp(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    if (skill.getProbability() > 25) {
                        return false;
                    }
                    skill.setProbability(skill.getProbability() + 3.1f);
                    return true;
                }
            });
            if (!skill.load()) {
                skill.setProbability(3.0f);
            }
        } else if (name.equals("强化")) {
            final AttackSkill iskll = new AttackSkill();
            skill = iskll;
            skill.setName("强化");
            skill.setHero(hero);
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return hero.getSkillPoint() > 0 && SkillFactory.getSkill("魔王天赋", hero).isActive();
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append(skill.getProbability()).append("%的概率释放，自身生命值上限增加").append(iskll.getBaseHarm()).append("倍").append("。强化效果结束时，HP会重置。");
                    return builder.toString();
                }
            });
            skill.setRelease(new UseExpression() {
                @Override
                public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                    if (hero.isOnSkill()) {
                        long harm = monster.getAtk() - hero.getDefenseValue();
                        if (harm <= 0) {
                            harm = hero.getRandom().nextLong(hero.getMaxMazeLev() + 1) + 1;
                        }
                        hero.addHp(harm);
                        String msg1 = monster.getFormatName() + "攻击了" + hero.getFormatName() + "，造成了<font color=\"red\">" + StringUtils.formatNumber(harm) + "</font>点伤害。";
                        skill.addMessage(msg1);
                        monster.addBattleSkillDesc(msg1);
                    } else {
                        final long hp = hero.getHp() * iskll.getBaseHarm();
                        hero.setSkillAdditionHp(hp);
                        hero.setOnSkill(true);
                        final long n = hero.getRandom().nextLong(hero.getMaxMazeLev() / 100l + 10l);
                        String msg = hero.getFormatName() + "使用了技能" + skill.getName() + "，使得自身的生命值上限增加了" + iskll.getBaseHarm() + "倍，持续" + n + "回合。";
                        skill.addMessage(msg);
                        monster.addBattleSkillDesc(msg);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                int i = 0;
                                while (i < n) {
                                    try {
                                        Thread.sleep(MainGameActivity.context.getRefreshInfoSpeed());
                                    } catch (InterruptedException e) {
                                        Thread.currentThread().interrupt();
                                        e.printStackTrace();
                                    }
                                    i++;
                                }
                                iskll.addMessage("强化技能效果消失了");
                                hero.setOnSkill(false);
                                hero.setHp(hero.getUpperHp());

                            }
                        }).start();
                    }
                    return false;
                }

            });
            skill.setLevelUp(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    if (skill.getProbability() > 25) {
                        return false;
                    }
                    skill.setProbability(skill.getProbability() + 1.1f);
                    iskll.setBaseHarm(iskll.getBaseHarm() + 1);
                    return true;
                }
            });
            if (!skill.load()) {
                skill.setProbability(2.2f);
                iskll.setBaseHarm(2l);
            }
        } else if (name.equals("迷雾")) {
            final AttackSkill iskll = new AttackSkill();
            skill = iskll;
            skill.setName("迷雾");
            skill.setHero(hero);
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return hero.getSkillPoint() > 0 && SkillFactory.getSkill("腐蚀", hero).isActive();
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append(skill.getProbability()).append("%的概率释放，降低敌方的命中率直到战斗结束。");
                    return builder.toString();
                }
            });
            skill.setRelease(new UseExpression() {
                @Override
                public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                    monster.setHitRate(monster.getHitRate() / 2);
                    String msg = hero.getFormatName() + "使用了技能" + skill.getName() + "，降低了" + monster.getFormatName() + "的命中率。";
                    skill.addMessage(msg);
                    monster.addBattleSkillDesc(msg);
                    return false;
                }

            });
            skill.setLevelUp(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    if (skill.getProbability() > 25) {
                        return false;
                    }
                    skill.setProbability(skill.getProbability() + 1.1f);
                    return true;
                }
            });
            if (!skill.load()) {
                skill.setProbability(2.2f);
                iskll.setBaseHarm(2l);
            }
        } else if (name.equals("隐身")) {
            skill = new EvilHide();
            skill.setName("隐身");
            skill.setHero(hero);
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return hero.getSkillPoint() > 0 && SkillFactory.getSkill("迷雾", hero).isActive();
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("你看我不到，你看我不到，你看我不到……<br>");
                    builder.append("降低");
                    builder.append(skill.getProbability()).append("%的遇怪概率。");
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
                    if (skill.getProbability() > 20) {
                        return false;
                    }
                    skill.setProbability(skill.getProbability() + 0.8f);
                    return true;
                }
            });
            if (!skill.load()) {
                skill.setProbability(12.2f);
            }
        } else if (name.equals("精神力")) {
            skill = new PropertySkill() {
                public void setOnUsed(boolean used) {

                    if (MainGameActivity.context != null) {
                        if (!onUsed && used) {
                            setAgi(getHero().getAgility());
                            setStr(getHero().getStrength());
                            setLife(getHero().getPower());
                        } else if (onUsed && !used) {
                            setAgi(getHero().getAgility() / 2);
                            setStr(getHero().getStrength() / 2);
                            setLife(getHero().getPower() / 2);
                        }
                        super.setOnUsed(used);
                    }
                }
            };
            skill.setName("精神力");
            skill.setHero(hero);
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return hero.getSkillPoint() > 0 && SkillFactory.getSkill("隐身", hero).isActive();
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("唯一一个影响基本属性的技能<br>");
                    builder.append("激活时提升一倍力量、体力、敏捷。<br>反激活（重置技能）时力量、体力、敏捷减半。");
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
                    if (skill.getProbability() > 25) {
                        return false;
                    }
                    skill.setProbability(skill.getProbability() + 1.1f);
                    return true;
                }
            });
            if (!skill.load()) {
                skill.setProbability(12.2f);
            }
        } else if (name.equals("不死之身")) {
            final PropertySkill iskll = new PropertySkill();
            skill = iskll;
            skill.setName("不死之身");
            skill.setHero(hero);
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return hero.getSkillPoint() > 0 && SkillFactory.getSkill("精神力", hero).isActive();
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("激活后可以在HP变为零的时候有").append(skill.getProbability()).append("的概率原地满血复活。");
                    return builder.toString();
                }
            });
            skill.setRelease(new UseExpression() {
                @Override
                public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                    hero.restore();
                    String msg = hero.getFormatName() + "使用了技能" + iskll.getName() + "，原地满血复活，并且逃离了战斗。";
                    skill.addMessage(msg);
                    monster.addBattleSkillDesc(msg);
                    return true;
                }

            });
            skill.setLevelUp(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    if (skill.getProbability() > 15) {
                        return false;
                    }
                    skill.setProbability(skill.getProbability() + 1.1f);
                    return true;
                }
            });
            if (!skill.load()) {
                skill.setProbability(5.0f);
            }
        } else if (name.equals("闪电")) {
            final AttackSkill iskll = new AttackSkill();
            skill = iskll;
            skill.setName("闪电");
            skill.setHero(hero);
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return hero.getSkillPoint() > 0 && SkillFactory.getSkill("强化", hero).isActive();
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("从虚空中拉出一条闪电攻击敌人。<br>").append(skill.getProbability()).
                            append("的概率释放。造成额外的").append(StringUtils.formatNumber(iskll.getBaseHarm())).append("-").
                            append(StringUtils.formatNumber(iskll.getBaseHarm() + iskll.getAdditionHarm())).append("闪电伤害");
                    return builder.toString();
                }
            });
            skill.setRelease(new UseExpression() {
                @Override
                public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                    long harm = hero.getAttackValue() + iskll.getBaseHarm() + hero.getRandom().nextLong(iskll.getAdditionHarm() + 1);
                    if (harm < 0) {
                        iskll.setAdditionHarm(Integer.MAX_VALUE);
                        iskll.setBaseHarm(Integer.MAX_VALUE / 8);
                        harm = Integer.MAX_VALUE - 100000;
                    }
                    monster.addHp(-harm);
                    String msg = hero.getFormatName() + "使用了技能" + iskll.getName() + "，对" +
                            monster.getFormatName() + "造成了" + StringUtils.formatNumber(harm) + "的闪电伤害";
                    skill.addMessage(msg);
                    monster.addBattleSkillDesc(msg);
                    return false;
                }

            });
            skill.setLevelUp(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    if (skill.getProbability() < 25) {
                        skill.setProbability(skill.getProbability() + 1.1f);
                    }
                    if (iskll.getAdditionHarm() < hero.getBaseAttackValue() * 100 && iskll.getAdditionHarm() < Integer.MAX_VALUE) {
                        iskll.setBaseHarm(iskll.getBaseHarm() * 2 + 2000);
                        iskll.setAdditionHarm(iskll.getAdditionHarm() * 2 + 4000);
                    }
                    return false;
                }
            });
            if (!skill.load()) {
                skill.setProbability(5.0f);
                iskll.setBaseHarm(10000l);
                iskll.setAdditionHarm(70000l);
            }
        } else if (name.equals("水波")) {
            final DefendSkill iskll = new DefendSkill();
            skill = iskll;
            skill.setName("水波");
            skill.setHero(hero);
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return hero.getArmor().equalsIgnoreCase(Armor.水波甲.name()) && hero.getSkillPoint() > 0 && SkillFactory.getSkill("闪电", hero).isActive();
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("柔则克刚。需要防具升级为水波甲<b>的时候</b>才可以激活。<br>").append(skill.getProbability()).
                            append("的概率释放。受到攻击的时候抵消").append(iskll.getProbability() * 2 + 50).append("%的伤害");
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
                    String msg = monster.getFormatName() + "攻击了" + hero.getFormatName() + "造成了" + StringUtils.formatNumber(harm) + "的伤害。";
                    skill.addMessage(msg);
                    monster.addBattleSkillDesc(msg);
                    double v = harm * (iskll.getProbability() * 2 + 50d) / 100d;
                    if (v > harm) v = harm - 1;
                    harm = (long) (harm - v);
                    String msg1 = hero.getFormatName() + "使用了技能" + iskll.getName() + "，抵消了" +
                            (long) v + "的伤害";
                    skill.addMessage(msg1);
                    monster.addBattleSkillDesc(msg1);
                    hero.addHp(-harm);
                    return false;
                }

            });
            skill.setLevelUp(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    if (skill.getProbability() < 25) {
                        skill.setProbability(skill.getProbability() + 6.5f);
                        return true;
                    }
                    return false;
                }
            });
            if (!skill.load()) {
                skill.setProbability(8.0f);
            }
        } else if (name.equals("反杀")) {
            skill = new EvilReKill();
            skill.setName("反杀");
            skill.setHero(hero);
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return (skill.isActive() || hero.getSkillPoint() > 0)
                            && SkillFactory.getSkill("水波", hero).isActive();
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("被动技能<br>");
                    builder.append(skill.getProbability()).append("%的概率在受到致命伤害前反杀敌人。");
                    return builder.toString();
                }
            });

            skill.setRelease(new UseExpression() {
                @Override
                public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                    String msg = skill.format(hero.getFormatName() + "触发了" + skill.getName() +
                            "，反杀了" + monster.getFormatName() + "\n-----------------------------");
                    context.addMessage(msg);
                    monster.addBattleSkillDesc(msg);
                    monster.addHp(-monster.getHp());
                    return false;
                }
            });
            if (!skill.load()) {
                skill.setProbability(1f);
            }
            skill.setLevelUp(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    if (skill.getProbability() < 5) {
                        skill.setProbability(skill.getProbability() + 0.5f);
                        return true;
                    }
                    return false;
                }
            });
        } else if (name.equals("变身")) {
            final AttackSkill iskll = new AttackSkill();
            skill = iskll;
            skill.setName("变身");
            skill.setHero(hero);
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return hero.getSkillPoint() > 0 && (SkillFactory.getSkill("精神力", hero).isActive()
                            || SkillFactory.getSkill("反杀", hero).isActive());
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("我为万物。<br>").append(skill.getProbability()).
                            append("的概率释放。当对方攻击力比自己高的时候，变身成为对方。持续").append(iskll.getBaseHarm()).append("个回合").
                            append("(变身只会改变名字、基本攻击和最高HP)。变身效果结束时HP会重置。");
                    return builder.toString();
                }
            });
            skill.setRelease(new UseExpression() {
                @Override
                public boolean release(final Hero hero, Monster monster, MainGameActivity context, final Skill skill) {
                    if (hero.isOnChange()) {
                        long harm = hero.getAttackValue();
                        monster.addHp(-harm);
                        String msg = hero.getFormatName() + "攻击了" +
                                monster.getFormatName() + "造成了" + StringUtils.formatNumber(harm) + "的伤害";
                        skill.addMessage(msg);
                        monster.addBattleSkillDesc(msg);
                    } else {
                        hero.setChangeName(monster.getName());
                        hero.setChangAtk(monster.getAtk());
                        hero.setChangeUhp(monster.getMaxHP());
                        hero.setChangeHp(hero.getHp());
                        hero.setOnChange(true);
                        String msg = hero.getName() + "使用了技能" + iskll.getName() + "变身成为了" +
                                monster.getFormatName();
                        skill.addMessage(msg);
                        monster.addBattleSkillDesc(msg);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                int i = 0;
                                while (i < iskll.getBaseHarm()) {
                                    try {
                                        Thread.sleep(MainGameActivity.context.getRefreshInfoSpeed());
                                    } catch (InterruptedException e) {
                                        Thread.currentThread().interrupt();
                                        e.printStackTrace();
                                    }
                                    i++;
                                }
                                iskll.addMessage(hero.getName() + "的变身效果消失了");
                                hero.setOnChange(false);
                                hero.setHp(hero.getUpperHp());
                            }
                        }).start();
                    }
                    return false;
                }

            });
            skill.setLevelUp(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    if (skill.getProbability() < 35) {
                        skill.setProbability(skill.getProbability() + 3.1f);
                        if (iskll.getBaseHarm() < 20) {
                            iskll.setBaseHarm(iskll.getBaseHarm() + 3);
                        }
                        return true;
                    }
                    return false;
                }
            });

            if (!skill.load()) {
                skill.setProbability(1.0f);
                iskll.setBaseHarm(10l);
            }
        } else if (name.equals("生命吸收")) {
            final AttackSkill iskll = new AttackSkill();
            skill = iskll;
            skill.setName("生命吸收");
            skill.setHero(hero);
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return hero.getSkillPoint() > 0 && (SkillFactory.getSkill("反杀", hero).isActive()
                    );
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("你的是我的，我的还是我的。<br>").append(skill.getProbability()).
                            append("的概率释放。吸取对方").append(iskll.getBaseHarm()).append("%的生命值。");
                    return builder.toString();
                }
            });
            skill.setRelease(new UseExpression() {
                @Override
                public boolean release(final Hero hero, Monster monster, MainGameActivity context, final Skill skill) {
                    double harm = monster.getHp() * (iskll.getBaseHarm() / 100d);
                    if (harm < 0) harm = 0;
                    monster.addHp(-(long) harm);
                    hero.addHp((long) harm);
                    String msg = hero.getFormatName() + "使用了技能" + skill.getName() + "，吸收了" + monster.getFormatName() + "的" + StringUtils.formatNumber((long) harm) + "点生命值。";
                    skill.addMessage(msg);
                    monster.addBattleSkillDesc(msg);
                    if (skill.getCount() == 10000) {
                        Achievement.doctor.enable(hero);
                    }
                    return false;
                }

            });
            skill.setLevelUp(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    if (skill.getProbability() < 35) {
                        skill.setProbability(skill.getProbability() + 3.1f);
                    }
                    if (iskll.getBaseHarm() < 80)
                        iskll.setBaseHarm(iskll.getBaseHarm() + 3);
                    return false;
                }
            });
            if (!skill.load()) {
                skill.setProbability(3.0f);
                iskll.setBaseHarm(30l);
            }
        } else if (name.equals("多重攻击")) {
            final AttackSkill iskll = new AttackSkill();
            skill = iskll;
            skill.setName("多重攻击");
            skill.setHero(hero);
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return hero.getSkillPoint() > 0 && (SkillFactory.getSkill("不死之身", hero).isActive()
                            || SkillFactory.getSkill("变身", hero).isActive() || SkillFactory.getSkill("生命吸收", hero).isActive());
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("速度也是一种极致。你们这些杂碎完全看不清我的动作了吧，啊哈哈哈哈哈哈哈哈…（回声）<br>").append(skill.getProbability()).
                            append("的概率释放。使用").append(iskll.getBaseHarm()).append("%的攻击力进行多次攻击。");
                    return builder.toString();
                }
            });
            skill.setRelease(new UseExpression() {
                @Override
                public boolean release(final Hero hero, Monster monster, MainGameActivity context, final Skill skill) {
                    Double harm = hero.getAttackValue() * (iskll.getBaseHarm() / 100d);
                    long i = hero.getRandom().nextLong(iskll.getAdditionHarm() + 1) + 1;
                    long l = harm.longValue() * i;
                    if (l <= 0) {
                        l = hero.getBaseAttackValue();
                        iskll.setAdditionHarm(i / 2);
                    }
                    monster.addHp(-l);
                    String msg = hero.getFormatName() + "使用了技能" + skill.getName() + "，对" +
                            monster.getFormatName() + "进行了" + i + "次攻击，总共造成了" + StringUtils.formatNumber(l) + "伤害。";
                    skill.addMessage(msg);
                    monster.addBattleSkillDesc(msg);
                    Achievement.satan.enable(hero);
                    return false;
                }

            });
            skill.setLevelUp(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    if (skill.getProbability() < 35) {
                        skill.setProbability(skill.getProbability() + 3.1f);
                        if (iskll.getBaseHarm() < 100) {
                            iskll.setBaseHarm(iskll.getBaseHarm() + 2);
                        }
                        iskll.setAdditionHarm(iskll.getAdditionHarm() + 2);
                        return true;
                    }
                    return false;
                }
            });
            if (!skill.load()) {
                skill.setProbability(3.0f);
                iskll.setBaseHarm(60l);
                iskll.setAdditionHarm(5l);
            }
        }
        return skill;
    }
}
