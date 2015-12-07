package cn.gavin.skill.system;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import cn.gavin.Achievement;
import cn.gavin.Hero;
import cn.gavin.R;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.maze.Maze;
import cn.gavin.monster.Monster;
import cn.gavin.palace.Base;
import cn.gavin.palace.nskill.NSkill;
import cn.gavin.pet.Pet;
import cn.gavin.skill.Skill;
import cn.gavin.skill.SkillFactory;
import cn.gavin.skill.expression.DescExpression;
import cn.gavin.skill.expression.EnableExpression;
import cn.gavin.skill.expression.UseExpression;
import cn.gavin.skill.type.AttackSkill;
import cn.gavin.skill.type.DefendSkill;
import cn.gavin.skill.type.PropertySkill;
import cn.gavin.utils.StringUtils;

/**
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED
 * Created by luoyuan on 11/6/15.
 */
public class PetSkill extends SkillLayout {
    public PetSkill(Context context) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.skill_layout_pet, (ViewGroup) this.findViewById(R.id.skill_dialog));
        this.addView(view);
        this.context = context;
    }

    public void init() {
        Skill skill = SkillFactory.getSkill("驯兽师", hero);
        Button button = (Button) view.findViewById(R.id.pet_skill_xss_button);
        skill.setSkillButton(button);
        button.setOnClickListener(buildOnClickListener(skill));
        skills.add(skill);

        skill = SkillFactory.getSkill("培育家", hero);
        button = (Button) view.findViewById(R.id.pet_skill_pyj_button);
        skill.setSkillButton(button);
        button.setOnClickListener(buildOnClickListener(skill));
        skills.add(skill);

        skill = SkillFactory.getSkill("捕捉术", hero);
        button = (Button) view.findViewById(R.id.pet_skill_bzs_button);
        skill.setSkillButton(button);
        button.setOnClickListener(buildOnClickListener(skill));
        skills.add(skill);

        skill = SkillFactory.getSkill("催化剂", hero);
        button = (Button) view.findViewById(R.id.pet_skill_chj_button);
        skill.setSkillButton(button);
        button.setOnClickListener(buildOnClickListener(skill));
        skills.add(skill);

        skill = SkillFactory.getSkill("霸气", hero);
        button = (Button) view.findViewById(R.id.pet_skill_bq_button);
        skill.setSkillButton(button);
        button.setOnClickListener(buildOnClickListener(skill));
        skills.add(skill);

        skill = SkillFactory.getSkill("爱心", hero);
        button = (Button) view.findViewById(R.id.pet_skill_ax_button);
        skill.setSkillButton(button);
        button.setOnClickListener(buildOnClickListener(skill));
        skills.add(skill);

        skill = SkillFactory.getSkill("群殴", hero);
        button = (Button) view.findViewById(R.id.pet_skill_qo_button);
        skill.setSkillButton(button);
        button.setOnClickListener(buildOnClickListener(skill));
        skills.add(skill);

        skill = SkillFactory.getSkill("反击", hero);
        button = (Button) view.findViewById(R.id.pet_skill_fj_button);
        skill.setSkillButton(button);
        button.setOnClickListener(buildOnClickListener(skill));
        skills.add(skill);

        skill = SkillFactory.getSkill("神赋", hero);
        button = (Button) view.findViewById(R.id.pet_skill_sf_button);
        skill.setSkillButton(button);
        button.setOnClickListener(buildOnClickListener(skill));
        skills.add(skill);

        skill = SkillFactory.getSkill("恩赐", hero);
        button = (Button) view.findViewById(R.id.pet_skill_ec_button);
        skill.setSkillButton(button);
        button.setOnClickListener(buildOnClickListener(skill));
        skills.add(skill);

        skill = SkillFactory.getSkill("宠物大师", hero);
        button = (Button) view.findViewById(R.id.pet_skill_cwds_button);
        skill.setSkillButton(button);
        button.setOnClickListener(buildOnClickListener(skill));
        skills.add(skill);

    }

    public static Skill getSkill(String name, final Hero hero) {
        Skill skill = null;
        if ("驯兽师".equalsIgnoreCase(name)) {
            final PropertySkill iskll = new PropertySkill();
            skill = iskll;
            skill.setName("驯兽师");
            skill.setHero(hero);
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return !SkillFactory.getSkill("培育家", hero).isActive() && Achievement.pet.isEnable();
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("激活后增加捕获宠物的几率。不可和培育家一起激活!");
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
        } else if ("培育家".equalsIgnoreCase(name)) {
            final PropertySkill iskll = new PropertySkill();
            skill = iskll;
            skill.setName("培育家");
            skill.setHero(hero);
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return !SkillFactory.getSkill("驯兽师", hero).isActive() && Achievement.egg.isEnable();
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("激活后增加获得宠物蛋的几率。不可与驯兽师一起激活！");
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
            iskll.setEggRate(200f);
        } else if ("捕捉术".equalsIgnoreCase(name)) {
            final AttackSkill iskll = new AttackSkill();
            skill = iskll;
            skill.setName("捕捉术");
            skill.setHero(hero);
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return SkillFactory.getSkill("驯兽师", hero).isActive();
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("使用这个技能<b>击败</b>敌人的时候100%捕捉成功。<br>").append(iskll.getProbability()).append("%几率释放。");
                    return builder.toString();
                }
            });
            skill.setRelease(new UseExpression() {
                @Override
                public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                    long harm = hero.getAttackValue();
                    monster.addHp(-harm);
                    String msg = hero.getFormatName() + "攻击了" + monster.getFormatName() + "造成" + StringUtils.formatNumber(harm) + "点伤害";
                    iskll.addMessage(msg);
                    monster.addBattleDesc(msg);
                    if (monster.getHp() <= 0) {
                        Pet pet = Pet.cPet(monster, hero.getRandom());
                        if (pet != null) {
                            msg = hero.getFormatName() + "捕捉到了" + pet.getFormatName();
                            iskll.addMessage(msg);
                            monster.addBattleSkillDesc(msg);
                        }
                    }
                    return false;
                }

            });
            skill.setLevelUp(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    if (skill.getProbability() < 25) {
                        skill.setProbability(skill.getProbability() + 3.6f);
                    }
                    return true;
                }
            });
            if (!skill.load()) {
                skill.setProbability(1.0f);
            }
        } else if ("催化剂".equalsIgnoreCase(name)) {
            final PropertySkill iskll = new PropertySkill();
            skill = iskll;
            skill.setName("催化剂");
            skill.setHero(hero);
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return SkillFactory.getSkill("培育家", hero).isActive();
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
        } else if ("霸气".equalsIgnoreCase(name)) {
            final PropertySkill iskll = new PropertySkill();
            skill = iskll;
            skill.setName("霸气");
            skill.setHero(hero);
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return SkillFactory.getSkill("捕捉术", hero).isActive();
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("激活后捕获的宠物成长值翻三倍。");
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
        } else if ("爱心".equalsIgnoreCase(name)) {
            final PropertySkill iskll = new PropertySkill();
            skill = iskll;
            skill.setName("爱心");
            skill.setHero(hero);
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return SkillFactory.getSkill("催化剂", hero).isActive();
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
        } else if ("群殴".equalsIgnoreCase(name)) {
            final AttackSkill iskll = new AttackSkill();
            skill = iskll;
            skill.setName("群殴");
            skill.setHero(hero);
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return SkillFactory.getSkill("霸气", hero).isActive();
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("命令队伍中的所有宠物攻击敌人。");
                    builder.append(skill.getProbability()).append("%的概率释放。");
                    return builder.toString();
                }
            });
            skill.setRelease(new UseExpression() {
                @Override
                public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                    String skillMsg = hero.getFormatName() + "使用了技能" + iskll.getName();
                    skill.addMessage(skillMsg);
                    monster.addBattleSkillDesc(skillMsg);
                    if (hero.getPets().size() > 0) {
                        for (Pet pet : hero.getPets()) {
                            if (pet != null && pet.getHp() > 0 && !"蛋".equals(pet.getType())) {
                                NSkill petSkill = pet.getAtkSkill();
                                long petHarm = 0;
                                if (petSkill != null) {
                                    String petSkillMsg = pet.getFormatName() + "使用了技能" + petSkill.getName();
                                    iskll.addMessage(petSkillMsg);
                                    monster.addBattleSkillDesc(petSkillMsg);
                                    monster.addBattleDesc(petSkillMsg);
                                    Base target = new Base() {
                                    };
                                    target.setDef(0l);
                                    target.setAtk(monster.getAtk());
                                    target.setHp(monster.getHp());
                                    target.setElement(monster.getElement());
                                    petHarm = petSkill.getHarm(target);
                                } else {
                                    petHarm = pet.getAtk();
                                }
                                String petAtk = pet.getFormatName() + "攻击了" + monster.getFormatName() + ",造成了" + StringUtils.formatNumber(petHarm) + "点伤害";
                                monster.addHp(-petHarm);
                                iskll.addMessage(petAtk);
                                monster.addBattleSkillDesc(petAtk);
                                monster.addBattleDesc(petAtk);
                            }
                        }
                    }
                    return false;
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
                skill.setProbability(1);
            }
        } else if ("反击".equalsIgnoreCase(name)) {
            final DefendSkill iskll = new DefendSkill();
            skill = iskll;
            skill.setName("反击");
            skill.setHero(hero);
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return SkillFactory.getSkill("爱心", hero).isActive();
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("受到攻击伤害之后所有宠物攻击敌人。");
                    builder.append(skill.getProbability()).append("%的概率释放。");
                    return builder.toString();
                }
            });
            skill.setRelease(new UseExpression() {
                @Override
                public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                    long harm = monster.getAtk() - hero.getDefenseValue();
                    if (harm <= 0) {
                        harm = hero.getRandom().nextLong(hero.getMaxMazeLev() + 1) + 1;
                    }
                    hero.addHp(-harm);
                    String harmMsg = monster.getFormatName() + "攻击了" + hero.getFormatName() + "，造成了" + StringUtils.formatNumber(harm) + "点伤害。";
                    String skillMsg = hero.getFormatName() + "使用了技能" + iskll.getName();
                    skill.addMessage(harmMsg);
                    monster.addBattleSkillDesc(harmMsg);
                    skill.addMessage(skillMsg);
                    monster.addBattleSkillDesc(skillMsg);
                    if (hero.getPets().size() > 0) {
                        for (Pet pet : hero.getPets()) {
                            if (pet != null && pet.getHp() > 0 && !"蛋".equals(pet.getType())) {
                                NSkill petSkill = pet.getAtkSkill();
                                long petHarm = 0;
                                if (petSkill != null) {
                                    String petSkillMsg = pet.getFormatName() + "使用了技能" + petSkill.getName();
                                    iskll.addMessage(petSkillMsg);
                                    monster.addBattleSkillDesc(petSkillMsg);
                                    monster.addBattleDesc(petSkillMsg);
                                    Base target = new Base() {
                                    };
                                    target.setDef(0l);
                                    target.setAtk(monster.getAtk());
                                    target.setHp(monster.getHp());
                                    target.setElement(monster.getElement());
                                    petHarm = petSkill.getHarm(target);
                                } else {
                                    petHarm = pet.getAtk();
                                }
                                String petAtk = pet.getFormatName() + "攻击了" + monster.getFormatName() + ",造成了" + StringUtils.formatNumber(petHarm) + "点伤害";
                                monster.addHp(-petHarm);
                                iskll.addMessage(petAtk);
                                monster.addBattleSkillDesc(petAtk);
                                monster.addBattleDesc(petAtk);
                            }
                        }
                    }
                    return false;
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
                skill.setProbability(1);
            }
        } else if ("神赋".equalsIgnoreCase(name)) {
            final PropertySkill iskll = new PropertySkill();
            skill = iskll;
            skill.setName("神赋");
            skill.setHero(hero);
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return SkillFactory.getSkill("群殴", hero).isActive();
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("激活后捕获的宠物有30%的几率带有技能。");
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
        } else if ("恩赐".equalsIgnoreCase(name)) {
            final PropertySkill iskll = new PropertySkill();
            skill = iskll;
            skill.setName("恩赐");
            skill.setHero(hero);
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return SkillFactory.getSkill("反击", hero).isActive();
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("激活后从蛋里孵出来的宠物有40%的几率携带技能。");
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
        } else if ("宠物大师".equalsIgnoreCase(name)) {
            final PropertySkill iskll = new PropertySkill();
            skill = iskll;
            skill.setName("宠物大师");
            skill.setHero(hero);
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return SkillFactory.getSkill("神赋", hero).isActive() || SkillFactory.getSkill("恩赐", hero).isActive();
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("激活后提升宠物队伍上限。");
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
                iskll.setPetSize(5);
            }
            if (iskll.getPetSize() < 5) {
                iskll.setPetSize(5);
            }
        }
        return skill;
    }
}
