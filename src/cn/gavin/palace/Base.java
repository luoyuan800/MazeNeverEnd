package cn.gavin.palace;

import java.util.LinkedHashSet;
import java.util.Set;

import cn.gavin.Element;
import cn.gavin.activity.BaseContext;
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
    protected long uHp;
    protected long hp;
    protected long def;
    private Random random = new Random();
    protected long atk;
    private int hit;
    private int parry;
    private String name;
    private int dodge = 15;
    private Element element;
    private Set<NSkill> skillSet = new LinkedHashSet<NSkill>(3);
    private BaseContext context;
    private long lev;

    void addSkill(NSkill skill) {
        if (skill != null)
            skillSet.add(skill);
    }

    void atk(Base target) {
        NSkill skill = getAtkSkill();
        Element element = this.element;
        long harm = 0;
        if (skill != null) {
            addMessage(getFormatName() + "使用技能" + skill.getName());
            harm = skill.getHarm(target);
            element = skill.getElement();
            if(harm < 0){
                target.normalAtk(this, -harm, element);
                return;
            }
        } else {
            addMessage(getFormatName() + "攻击了" + target.getFormatName());
            harm = getAtk() - target.getDef();
            if(harm < 0){
                if(random.nextLong(100) < 5){
                    addMessage(getFormatName() + "击穿了" + target.getFormatName() + "的防御");
                    harm = atk;
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
            return atk + random.nextLong(lev);
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
            return def + random.nextLong(lev);
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
                addMessage(target.getFormatName() + "使用了技能" + skill.getName());
                element = skill.getElement();
                skip = skill.release(this, harm);
            }
            if(!skip){
                harm = judgeElement(target, harm, element);
                if(harm <= 0) harm = random.nextLong(target.getLev()) + 1;
                addMessage(target.getFormatName() + "受到了<font color=\"red\">" + harm + "</font>点伤害");
                target.addHp(-harm);
            }
        }
    }
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
        if(hp > uHp) hp = uHp;
    }

    public NSkill getAtkSkill() {
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
        this.uHp = hp;
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
        if(atk < Long.MAX_VALUE -100000) {
            this.atk = atk;
        }else{
            this.atk = Long.MAX_VALUE - 100000;
        }
    }

    public Random getRandom() {
        return random;
    }

    public long getLev() {
        return lev;
    }

    public String getHello() {
        return "……";
    }

    public BaseContext getContext() {
        return context;
    }

    public void setContext(BaseContext context) {
        this.context = context;
    }

    public Set<NSkill> getSkills() {
        return skillSet;
    }

    public void setLev(long lev) {
        this.lev = lev;
    }

    public String getName(){
        return name;
    }

    public long getUHp() {
        return uHp;
    }

    public int getHit() {
        return hit;
    }

    public int getParry() {
        return parry;
    }

    public int getDodge() {
        return dodge;
    }

    public void setUHp(long hp){
        this.uHp = hp;
    }
}
