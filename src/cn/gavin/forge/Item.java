package cn.gavin.forge;

import cn.gavin.skill.Skill;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 9/25/2015.
 */
public class Item {
    private long baseAgi;
    private long baseStre;
    private long baseLife;
    private long baseClickAward;
    private long baseAtk;
    private long baseDef;
    private float additionAtkRate;
    private float additionDefRate;
    private long additionAgi;
    private long additionStre;
    private long additionLife;
    private long additionClickAward;
    private long additionAtk;
    private long additionDef;
    private Skill skill;

    public long getBaseAgi() {
        return baseAgi;
    }

    public void setBaseAgi(long baseAgi) {
        this.baseAgi = baseAgi;
    }

    public long getBaseStre() {
        return baseStre;
    }

    public void setBaseStre(long baseStre) {
        this.baseStre = baseStre;
    }

    public long getBaseLife() {
        return baseLife;
    }

    public void setBaseLife(long baseLife) {
        this.baseLife = baseLife;
    }

    public long getBaseClickAward() {
        return baseClickAward;
    }

    public void setBaseClickAward(long baseClickAward) {
        this.baseClickAward = baseClickAward;
    }

    public long getBaseAtk() {
        return baseAtk;
    }

    public void setBaseAtk(long baseAtk) {
        this.baseAtk = baseAtk;
    }

    public long getBaseDef() {
        return baseDef;
    }

    public void setBaseDef(long baseDef) {
        this.baseDef = baseDef;
    }

    public float getAdditionAtkRate() {
        return additionAtkRate;
    }

    public void setAdditionAtkRate(float additionAtkRate) {
        this.additionAtkRate = additionAtkRate;
    }

    public float getAdditionDefRate() {
        return additionDefRate;
    }

    public void setAdditionDefRate(float additionDefRate) {
        this.additionDefRate = additionDefRate;
    }

    public long getAdditionAgi() {
        return additionAgi;
    }

    public void setAdditionAgi(long additionAgi) {
        this.additionAgi = additionAgi;
    }

    public long getAdditionStre() {
        return additionStre;
    }

    public void setAdditionStre(long additionStre) {
        this.additionStre = additionStre;
    }

    public long getAdditionLife() {
        return additionLife;
    }

    public void setAdditionLife(long additionLife) {
        this.additionLife = additionLife;
    }

    public long getAdditionClickAward() {
        return additionClickAward;
    }

    public void setAdditionClickAward(long additionClickAward) {
        this.additionClickAward = additionClickAward;
    }

    public long getAdditionAtk() {
        return additionAtk;
    }

    public void setAdditionAtk(long additionAtk) {
        this.additionAtk = additionAtk;
    }

    public long getAdditionDef() {
        return additionDef;
    }

    public void setAdditionDef(long additionDef) {
        this.additionDef = additionDef;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }
}
