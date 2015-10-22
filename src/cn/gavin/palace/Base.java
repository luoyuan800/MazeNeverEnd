package cn.gavin.palace;

import java.util.LinkedHashSet;
import java.util.Set;

import cn.gavin.Element;
import cn.gavin.activity.PalaceActivity;
import cn.gavin.palace.nskill.AtkSkill;
import cn.gavin.palace.nskill.DefSkill;
import cn.gavin.palace.nskill.NSkill;
import cn.gavin.utils.Random;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 10/21/2015.
 */
public abstract class Base {
    private long hp;
    private long def;
    private Random random;
    private long atk;
    private int hit;
    private int parry;
    private String name;
    private int dodge = 15;
    private Element element;
    private Set<NSkill> skillSet = new LinkedHashSet<NSkill>(3);
    private PalaceActivity context;

    void addSkill(NSkill skill) {
        if (skill != null)
            skillSet.add(skill);
    }

    void atk(Base target) {
        addMessage(getFormatName() + "攻击了" + target.getFormatName());
        NSkill skill = getAtkSkill();
        Element element = this.element;
        long harm = 0;
        if (skill != null) {
            addMessage(getFormatName() + "使用技能" + skill.getName() + "攻击" + target.getFormatName());
            harm = skill.getHarm(target);
            element = skill.getElement();
            if(harm < 0){
                target.normalAtk(this, -harm, element);
                return;
            }
        } else {
            harm = getAtk() - target.getDef();
            if(harm < 0){
                if(random.nextLong(100) < 5){
                    addMessage(getFormatName() + "击穿了" + target.getFormatName() + "的防御");
                    harm = harm + target.def;
                }else{
                    harm = 0;
                }
            }
        }
        normalAtk(target, harm, element);
    }

    public long getAtk() {
        if (isHit()) {
            return atk *= 1.5;
        } else {
            return atk;
        }
    }

    private boolean isHit() {
        boolean hit = random.nextInt(1000) < this.hit;
        if (hit) {
            addMessage(getFormatName() + "使出了暴击");
        }
        return hit;
    }

    public long getDef() {
        if (isParry()) {
            return def *= 1.5;
        } else {
            return def;
        }
    }

    private boolean isParry() {
        boolean b = random.nextInt(1000) < parry;
        if (b) {
            addMessage(getFormatName() + "使出了格挡");
        }
        return b;
    }

    public void normalAtk(Base target, long harm, Element element) {
        if (target.isDodge()) {
            addMessage(target.getFormatName() + "闪开了" + getFormatName() + "的攻击。");
        } else {
            NSkill skill = target.getDefSkill();
            boolean skip = false;
            if (skill != null) {
                element = skill.getElement();
                skip = skill.release(this, harm);
            }
            if(!skip){
                harm = judgeElement(target, harm, element);
                if(harm < 0) harm = 0;
                addMessage(target.getFormatName() + "受到了<font color=\"red\">" + harm + "</font>点伤害");
                target.addHp(-harm);
            }
        }
    }
l
    private long judgeElement(Base target, long harm, Element meElement) {
        if (meElement.restriction(target.getElement())) {
            harm *= 1.5;
        }
        if (target.getElement().restriction(meElement)) {
            harm *= 0.8;
        }
        return harm;
    }

    private NSkill getDefSkill() {
        for(NSkill skill : skillSet){
            if(skill instanceof DefSkill && skill.perform()){
                return skill;
            }
        }
        return null;
    }

    public void addHp(long l) {
        hp += l;
    }

    NSkill getAtkSkill() {
        for(NSkill skill : skillSet){
            if(skill instanceof AtkSkill && skill.perform()){
                return skill;
            }
        }
        return null;
    }

    public void addMessage(String str) {
        if (context != null) {
            context.addMessage(str);
        }
    }

    public String getFormatName() {
        return "<font color=\"#800080\">" + name + "</font>(" + element + ")";
    }

    public boolean isDodge() {
        return random.nextInt(1000) < dodge;
    }

    public Element getElement() {
        return element;
    }

    public long getHp() {
        return hp;
    }

    public void setHp(long hp) {
        this.hp = hp;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    public void setHit(int hit) {
        this.hit = hit;
    }

    public void setParry(int parry) {
        this.parry = parry;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDodge(int dodge) {
        this.dodge = dodge;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public void setDef(Long def) {
        this.def = def;
    }

    public void setAtk(Long atk) {
        this.atk = atk;
    }

    public Random getRandom() {
        return random;
    }

    public long getLev() {
        return 0;
    }

    public String getHello() {
        return "……";
    }

    public PalaceActivity getContext() {
        return context;
    }

    public void setContext(PalaceActivity context) {
        this.context = context;
    }

    public Set<NSkill> getSkills() {
        return skillSet;
    }
}
