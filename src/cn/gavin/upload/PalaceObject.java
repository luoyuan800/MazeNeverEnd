package cn.gavin.upload;

import cn.bmob.v3.BmobObject;
import cn.gavin.db.DBHelper;
import cn.gavin.utils.StringUtils;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 10/14/2015.
 */
public class PalaceObject extends BmobObject {
    private String name;
    private String atk;
    private String hp;
    private String def;
    private String parry;
    private String hitRate;
    private String skill;
    private String skill1;
    private String skill2;
    private String pay;
    private Long lev;
    private String hello;
    private String element;
    private Long reCount;

    public PalaceObject(){
        setTableName("uploader");
    }

    public String getName() {
        if(name.startsWith("0x")){
            name = StringUtils.toStringHex(name);
        }
        return name;
    }

    public void setName(String name) {
        if(name.startsWith("0x")){
            name = StringUtils.toStringHex(name);
        }
        this.name = name;
    }

    public String getAtk() {
        return atk;
    }

    public void setAtk(String atk) {
        this.atk = atk;
    }

    public String getHp() {
        return hp;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

    public String getDef() {
        return def;
    }

    public void setDef(String def) {
        this.def = def;
    }

    public String getParry() {
        return parry;
    }

    public void setParry(String parry) {
        this.parry = parry;
    }

    public String getHitRate() {
        return hitRate;
    }

    public void setHitRate(String hitRate) {
        this.hitRate = hitRate;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getSkill1() {
        return skill1;
    }

    public void setSkill1(String skill1) {
        this.skill1 = skill1;
    }

    public String getSkill2() {
        return skill2;
    }

    public void setSkill2(String skill2) {
        this.skill2 = skill2;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public Long getLev() {
        return lev;
    }

    public void setLev(Long lev) {
        this.lev = lev;
    }

    public void save(){
        DBHelper.getDbHelper().excuseSQLWithoutResult(String.format(
                "REPLACE INTO palace ( id,name, atk, hp, lev, def, parry, hit_rate,skill, skill1, skill2, hello, pay, element, re_count) " +
                        "values ('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')",getObjectId(),getName(), atk,hp,lev,def,parry,hitRate,skill,skill1,skill2, hello, pay, element, reCount));
    }

    public void setHello(String hello) {
        this.hello = hello;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public Long getReCount() {
        return reCount;
    }

    public void setReCount(Long reCount) {
        this.reCount = reCount;
    }
}