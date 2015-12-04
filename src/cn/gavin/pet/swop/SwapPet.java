package cn.gavin.pet.swop;

import cn.bmob.v3.BmobObject;
import cn.gavin.Element;
import cn.gavin.monster.Monster;
import cn.gavin.palace.nskill.NSkill;
import cn.gavin.pet.Pet;
import cn.gavin.pet.skill.PetSkill;
import cn.gavin.utils.MazeContents;
import cn.gavin.utils.StringUtils;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 11/19/2015.
 */
public class SwapPet extends BmobObject {
    private String ownerId;
    private String ownerName;
    private String keeperId;
    private String keeperName;
    private String id;
    private String askSkill;
    private Long askAtk;
    private Long askDef;
    private Long askHp;
    private Integer askType;
    private String askName;
    private Integer askSex;
    //-----------------------------
    private Long deathCount;
    private String fName;
    private String mName;
    private Long atk = 0l;
    private Long def = 0l;
    private Long hp = 0l;
    private Long atk_rise = 0l;
    private Long hp_rise = 0l;
    private Long def_rise = 0l;
    private Integer sex = 0;
    private String skill;
    private Integer type = 0;//不能用int，改成string
    private String color;
    private String name;
    private SwapPet changedPet;
    private Boolean acknowledge = false;
    private String hello;
    private String element;
    private Long lev = 0l;

    public static SwapPet buildSwapPet(Pet pet) {
        SwapPet swapPet = new SwapPet();
        if (pet.getAllSkill() != null) {
            swapPet.setSkill(pet.getAllSkill().getName() + "_" + pet.getAllSkill().getCount());
        }
        swapPet.setAtk(pet.getMaxAtk());
        swapPet.setDef(pet.getMaxDef());
        swapPet.setHp(pet.getUHp());
        swapPet.setName(pet.getName());
        swapPet.setType(Monster.getIndex(pet.getType()));
        swapPet.setAtk_rise(pet.getAtk_rise());
        swapPet.setDef_rise(pet.getDef_rise());
        swapPet.setHp_rise(pet.getHp_rise());
        swapPet.setKeeperId(MazeContents.hero.getUuid());
        swapPet.setKeeperName(MazeContents.hero.getName());
        swapPet.setOwnerId(pet.getOwnerId());
        swapPet.setOwnerName(pet.getOwner());
        if(!StringUtils.isNotEmpty(swapPet.getOwnerId())){
            swapPet.setOwnerId(swapPet.getKeeperId());
            swapPet.setOwnerName(swapPet.getKeeperName());
        }
        swapPet.setId(pet.getId());
        swapPet.setmName(pet.getmName());
        swapPet.setfName(pet.getfName());
        swapPet.setSex(pet.getSex());
        swapPet.setDeathCount(pet.getDeathCount());
        swapPet.setElement(pet.getElement().name());
        swapPet.setColor(pet.getColor());
        swapPet.setLev(pet.getLev());
        return swapPet;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getKeeperId() {
        return keeperId;
    }

    public void setKeeperId(String keeperId) {
        this.keeperId = keeperId;
    }

    public String getKeeperName() {
        return keeperName;
    }

    public void setKeeperName(String keeperName) {
        this.keeperName = keeperName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAskSkill() {
        return askSkill;
    }

    public void setAskSkill(String askSkill) {
        this.askSkill = askSkill;
    }

    public Long getAskAtk() {
        return askAtk;
    }

    public void setAskAtk(Long askAtk) {
        this.askAtk = askAtk;
    }

    public Long getAskDef() {
        return askDef;
    }

    public void setAskDef(Long askDef) {
        this.askDef = askDef;
    }

    public Long getAskHp() {
        return askHp;
    }

    public void setAskHp(Long askHp) {
        this.askHp = askHp;
    }

    public Integer getAskType() {
        return askType;
    }

    public void setAskType(Integer askType) {
        this.askType = askType;
    }


    public Long getAtk() {
        return atk;
    }

    public void setAtk(Long atk) {
        this.atk = atk;
    }

    public Long getDef() {
        return def;
    }

    public void setDef(Long def) {
        this.def = def;
    }

    public Long getHp() {
        return hp;
    }

    public void setHp(Long hp) {
        this.hp = hp;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Long getAtk_rise() {
        return atk_rise;
    }

    public void setAtk_rise(Long atk_rise) {
        this.atk_rise = atk_rise;
    }

    public Long getHp_rise() {
        return hp_rise;
    }

    public void setHp_rise(Long hp_rise) {
        this.hp_rise = hp_rise;
    }

    public Long getDef_rise() {
        return def_rise;
    }

    public void setDef_rise(Long def_rise) {
        this.def_rise = def_rise;
    }

    public SwapPet getChangedPet() {
        return changedPet;
    }

    public void setChangedPet(SwapPet changedPet) {
        this.changedPet = changedPet;
    }

    public Boolean getAcknowledge() {
        return acknowledge;
    }

    public void setAcknowledge(Boolean acknowledge) {
        this.acknowledge = acknowledge;
    }

    public String getAskName() {
        return askName;
    }

    public void setAskName(String askName) {
        this.askName = askName;
    }

    public String getHello() {
        return hello;
    }

    public void setHello(String hello) {
        this.hello = hello;
    }

    public Pet buildPet() {
        Pet pet = new Pet();
        if (type >= Monster.lastNames.length) {
            pet.setType("蛋");
        } else {
            pet.setType(Monster.lastNames[type]);
        }
        pet.setName(name);
        pet.setHp_rise(hp_rise);
        pet.setAtk_rise(atk_rise);
        pet.setDef_rise(def_rise);
        pet.setHp(hp);
        pet.setAtk(atk);
        pet.setDef(def);
        pet.setColor(color);
        String[] skillNameAndCount = StringUtils.split(skill, "_");
        if(skillNameAndCount.length > 1) {
            NSkill pSkill = NSkill.createSkillByName(skillNameAndCount[0], pet, StringUtils.toLong(skillNameAndCount[1]), null);
            if (pSkill != null) {
                if (pSkill instanceof PetSkill) {
                    pet.setSkill(pSkill);
                } else {
                    pet.addSkill(pSkill);
                }
            }
        }
        pet.setSex(sex);
        pet.setDeathCount(deathCount);
        pet.setfName(fName);
        pet.setmName(mName);
        pet.setElement(Element.valueOf(element));
        pet.setOwner(ownerName);
        pet.setOwnerId(ownerId);
        pet.setLev(lev);
        return pet;
    }

    public Integer getAskSex() {
        return askSex;
    }

    public void setAskSex(Integer askSex) {
        this.askSex = askSex;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Long getDeathCount() {
        return deathCount;
    }

    public void setDeathCount(Long deathCount) {
        this.deathCount = deathCount;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getFormateName() {
        if (type == Integer.MAX_VALUE - 1) {
            return "蛋";
        } else {
            return "<font color=\"" + color + "\">" + getName() + (sex == 0 ? "♂" : "♀") + "</font>(" + getElement() + ")";
        }
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public void setLev(Long lev) {
        this.lev = lev;
    }

    public Long getLev(){
        return lev;
    }
}
