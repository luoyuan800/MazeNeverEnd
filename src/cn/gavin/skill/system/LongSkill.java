package cn.gavin.skill.system;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import cn.gavin.Achievement;
import cn.gavin.Element;
import cn.gavin.Hero;
import cn.gavin.R;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.maze.BattleController;
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

/**
 * Created by luoyuan on 9/13/15.
 */
public class LongSkill extends SkillLayout {
    public LongSkill(Context context) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.skill_layout_long, (ViewGroup) this.findViewById(R.id.skill_dialog));
        this.addView(view);
        this.context = context;
    }

    public void init(SkillDialog dialog) {
        Skill skill = SkillFactory.getSkill("龙裔", hero, dialog);
        Button button = (Button) view.findViewById(R.id.long_skill_ly_button);
        skill.setSkillButton(button);
        skill = SkillFactory.getSkill("咆哮", hero, dialog);
        button = (Button) view.findViewById(R.id.long_skill_px_button);
        skill.setSkillButton(button);
        skill = SkillFactory.getSkill("龙铠", hero, dialog);
        button = (Button) view.findViewById(R.id.long_skill_lk_button);
        skill.setSkillButton(button);
        skill = SkillFactory.getSkill("觉醒", hero, dialog);
        button = (Button) view.findViewById(R.id.long_skill_jx_button);
        skill.setSkillButton(button);
        skill = SkillFactory.getSkill("龙爪", hero, dialog);
        button = (Button) view.findViewById(R.id.long_skill_lz_button);
        skill.setSkillButton(button);
        skill = SkillFactory.getSkill("吐息", hero, dialog);
        button = (Button) view.findViewById(R.id.long_skill_tx_button);
        skill.setSkillButton(button);
        skill = SkillFactory.getSkill("沙尘", hero, dialog);
        button = (Button) view.findViewById(R.id.long_skill_sc_button);
        skill.setSkillButton(button);
    }

    public static Skill getSkill(String name, final Hero hero, final SkillDialog dialog) {
        Skill skill = null;
        if ("龙裔".equalsIgnoreCase(name)) {
            final PropertySkill iskll = new PropertySkill();
            skill = iskll;
            skill.setName("龙裔");
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
                    StringBuilder builder = new StringBuilder();
                    builder.append("我们才是真正的龙的传人，干掉那些丑陋的西方爬虫！击败1000只龙怪后可以激活。<br>");
                    builder.append("被动技能，激活后免疫龙系怪物对你的伤害。");
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
                skill.setProbability(6f);
            }
        } else if ("咆哮".equalsIgnoreCase(name)) {
            final DefendSkill iskll = new DefendSkill();
            skill = iskll;
            skill.setName("咆哮");
            skill.setHero(hero);
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return SkillFactory.getSkill("龙裔", hero, dialog).isActive();
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("在敌人攻击的时候用龙威压迫对方。<br>");
                    builder.append("防御技能，在敌人攻击的时候有").append(iskll.getProbability()).append("%的概率吓退敌人,结束战斗。");
                    return builder.toString();
                }
            });
            skill.setRelease(new UseExpression() {
                @Override
                public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                    String msg = hero.getFormatName() + "释放了" + iskll.getName() + "吓退了" + monster.getFormatName();
                    iskll.addMessage(msg);
                    monster.addBattleSkillDesc(msg);
                    if (skill.getCount() > 60000) {
                        Achievement.folk.enable(hero);
                    }
                    return true;
                }

            });
            skill.setLevelUp(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    if (skill.getProbability() < 15) {
                        skill.setProbability(skill.getProbability() + 1);
                    }
                    return true;
                }
            });
            if (!skill.load()) {
                skill.setProbability(1f);
            }
        } else if ("龙铠".equalsIgnoreCase(name)) {
            final DefendSkill iskll = new DefendSkill();
            skill = iskll;
            skill.setName("龙铠");
            skill.setHero(hero);
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return SkillFactory.getSkill("咆哮", hero, dialog).isActive();
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("增强属性抗性。<br>");
                    builder.append("防御技能，在敌人攻击的时候有").append(iskll.getProbability()).append("%的概率免疫同属性敌人的攻击伤害（不包括技能伤害）。");
                    return builder.toString();
                }
            });
            skill.setRelease(new UseExpression() {
                @Override
                public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                    if (hero.getElement() == monster.getElement()) {
                        String msg = hero.getFormatName() + "免疫了" + monster.getFormatName() + "的攻击";
                        iskll.addMessage(msg);
                        monster.addBattleSkillDesc(msg);
                        return false;
                    } else {
                        return BattleController.heroDef(context, hero, monster, hero.getRandom());
                    }
                }

            });
            skill.setLevelUp(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    if (skill.getProbability() < 65) {
                        skill.setProbability(skill.getProbability() + 5);
                    }
                    return true;
                }
            });
            if (!skill.load()) {
                skill.setProbability(20f);
            }
        } else if ("觉醒".equalsIgnoreCase(name)) {
            final PropertySkill iskll = new PropertySkill() {
                public void setOnUsed(boolean use) {
                    if (!onUsed && use && MainGameActivity.context != null) {
                        long elementValue = (hero.getAgility() + hero.getStrength() + hero.getPower() + hero.getBaseDefense() + hero.getBaseAttackValue()) / 5;
                        if (hero.getRandom().nextLong(hero.getAttackValue() + 1) < hero.getRandom().nextLong(elementValue + 1)) {
                            hero.setElement(Element.金);
                        } else if (hero.getRandom().nextLong(hero.getDefenseValue() + 1) < hero.getRandom().nextLong(elementValue + 1)) {
                            hero.setElement(Element.土);
                        } else if (hero.getRandom().nextLong(hero.getUpperHp() + 1) < hero.getRandom().nextLong(elementValue + 1)) {
                            hero.setElement(Element.水);
                        } else if (hero.getRandom().nextLong(hero.getStrength() + 1) < hero.getRandom().nextLong(elementValue + 1)) {
                            hero.setElement(Element.火);
                        } else if (hero.getRandom().nextLong(hero.getAgility() + 1) < hero.getRandom().nextLong(elementValue + 1)) {
                            hero.setElement(Element.木);
                        }
                        hero.addPoint(-6000);
                    }
                    super.setOnUsed(use);
                }
            };
            skill = iskll;
            skill.setName("觉醒");
            skill.setHero(hero);
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return hero.getPoint() > 6001 && SkillFactory.getSkill("龙铠", hero, dialog).isActive();
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("五行属性觉醒。需要消耗6000点能力点数进行觉醒。<br>");
                    builder.append("激活的时候赋予你一个五行属性。五行属性根据当前的基本属性计算生成。").append("<br>").
                            append("当敌方的属性被你克制时，攻击可以增加伤害。防御可以减少伤害。");
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
                skill.setProbability(10f);
            }
        } else if ("龙爪".equalsIgnoreCase(name)) {
            final AttackSkill iskll = new AttackSkill();
            skill = iskll;
            skill.setName("龙爪");
            skill.setHero(hero);
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return SkillFactory.getSkill("龙裔", hero, dialog).isActive();
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("将当前的攻击转换为金属性。<br>");
                    builder.append("如果自身属性为相生属性则攻击翻倍。如果自身属性为相克属性则攻击减为80%。");
                    return builder.toString();
                }
            });
            skill.setRelease(new UseExpression() {
                @Override
                public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                    long harm = hero.getAttackValue();
                    if (hero.getElement() == Element.土) {
                        harm *= 2;
                    } else if (hero.getElement() == Element.火) {
                        harm *= 0.8;
                    }
                    if (Element.金.restriction(monster.getElement())) {
                        harm *= 1.5;
                    } else if (monster.getElement().restriction(Element.金)) {
                        harm *= 0.8;
                    }
                    if (hero.isHit()) {
                        String msg = hero.getFormatName() + "使出了暴击";
                        iskll.addMessage(msg);
                        monster.addBattleSkillDesc(msg);
                    }
                    monster.addHp(-harm);
                    String msg = hero.getFormatName() + "使用技能" + iskll.getName() + "对" + monster.getFormatName() + "造成了" + harm + "点金属性伤害。";
                    iskll.addMessage(msg);
                    monster.addBattleSkillDesc(msg);
                    return false;
                }

            });
            skill.setLevelUp(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    if (skill.getProbability() < 25) {
                        skill.setProbability(skill.getProbability() + 3);
                    }
                    return true;
                }
            });
            if (!skill.load()) {
                skill.setProbability(6f);
            }
        } else if ("吐息".equalsIgnoreCase(name)) {
            final AttackSkill iskll = new AttackSkill();
            skill = iskll;
            skill.setName("吐息");
            skill.setHero(hero);
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return SkillFactory.getSkill("龙爪", hero, dialog).isActive();
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("火焰吐息，将攻击转换为火属性。<br>");
                    builder.append("如果自身属性为相生属性则攻击翻倍。如果自身属性为相克属性则攻击减为80%。");
                    return builder.toString();
                }
            });
            skill.setRelease(new UseExpression() {
                @Override
                public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                    long harm = hero.getAttackValue();
                    if (hero.getElement() == Element.木) {
                        harm *= 2;
                    } else if (hero.getElement() == Element.水) {
                        harm *= 0.8;
                    }
                    if (Element.火.restriction(monster.getElement())) {
                        harm *= 1.5;
                    } else if (monster.getElement().restriction(Element.火)) {
                        harm *= 0.82;
                    }
                    if (hero.isHit()) {
                        String msg = hero.getFormatName() + "使出了暴击";
                        iskll.addMessage(msg);
                        monster.addBattleSkillDesc(msg);
                    }
                    monster.addHp(-harm);
                    String msg = hero.getFormatName() + "使用技能" + iskll.getName() + "对" + monster.getFormatName() + "造成了" + harm + "点火属性伤害。";
                    iskll.addMessage(msg);
                    monster.addBattleSkillDesc(msg);
                    return false;
                }

            });
            skill.setLevelUp(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    if (skill.getProbability() < 25) {
                        skill.setProbability(skill.getProbability() + 3);
                    }
                    return true;
                }
            });
            if (!skill.load()) {
                skill.setProbability(7f);
            }
        } else if ("沙尘".equalsIgnoreCase(name)) {
            final AttackSkill iskll = new AttackSkill();
            skill = iskll;
            skill.setName("沙尘");
            skill.setHero(hero);
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return SkillFactory.getSkill("吐息", hero, dialog).isActive();
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("飞沙走石，将攻击转换为土属性。<br>");
                    builder.append("如果自身属性为相生属性则攻击翻倍。如果自身属性为相克属性则攻击减为80%。");
                    return builder.toString();
                }
            });
            skill.setRelease(new UseExpression() {
                @Override
                public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                    long harm = hero.getAttackValue();
                    if (hero.getElement() == Element.火) {
                        harm *= 2;
                    } else if (hero.getElement() == Element.木) {
                        harm *= 0.8;
                    }
                    if (Element.土.restriction(monster.getElement())) {
                        harm *= 1.5;
                    } else if (monster.getElement().restriction(Element.土)) {
                        harm *= 0.8;
                    }
                    if (hero.isHit()) {
                        String msg = hero.getFormatName() + "使出了暴击";
                        iskll.addMessage(msg);
                        monster.addBattleSkillDesc(msg);
                    }
                    monster.addHp(-harm);
                    String msg = hero.getFormatName() + "使用技能" + iskll.getName() + "对" + monster.getFormatName() + "造成了" + harm + "点土属性伤害。";
                    iskll.addMessage(msg);
                    monster.addBattleSkillDesc(msg);
                    return false;
                }

            });
            skill.setLevelUp(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    if (skill.getProbability() < 25) {
                        skill.setProbability(skill.getProbability() + 3);
                    }
                    return true;
                }
            });
            if (!skill.load()) {
                skill.setProbability(7f);
            }
        }
        return skill;
    }
}
