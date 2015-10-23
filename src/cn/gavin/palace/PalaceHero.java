package cn.gavin.palace;

import cn.gavin.Hero;
import cn.gavin.palace.nskill.NSkill;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 10/21/2015.
 */
public class PalaceHero extends Base {
    private int restoreCount = 0;
    private String hello;

    public PalaceHero(Hero hero) {
        setDodge(hero.getDodgeRate().intValue());
        setElement(hero.getElement());
        setParry(hero.getParry().intValue());
        setHit(hero.getHitRate().intValue());
        setHp(hero.getUpperHp());
        setAtk(hero.getUpperAtk());
        setDef(hero.getUpperDef());
        setName(hero.getName());
        setLev(hero.getMaxMazeLev());
        setRandom(hero.getRandom());
        setHello(hero.getHello());
        if (hero.getFirstSkill() != null) {
            addSkill(NSkill.createSkillBySkill(hero.getFirstSkill(), this));
        }
        if (hero.getSecondSkill() != null) {
            addSkill(NSkill.createSkillBySkill(hero.getSecondSkill(), this));
        }
        if (hero.getThirdSkill() != null) {
            addSkill(NSkill.createSkillBySkill(hero.getThirdSkill(), this));
        }
    }

    public int getRestoreCount() {
        return restoreCount;
    }

    public void setRestoreCount(int restoreCount) {
        this.restoreCount = restoreCount;
    }

    public void setHello(String hello) {
        this.hello = hello;
    }

    @Override
    public String getHello() {
        return hello;
    }
}
