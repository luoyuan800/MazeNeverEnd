package cn.gavin.pet.swop;

import cn.bmob.v3.BmobObject;
import cn.gavin.maze.Maze;
import cn.gavin.monster.Monster;
import cn.gavin.pet.Pet;
import cn.gavin.utils.MazeContents;

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
    private Long atk;
    private Long def;
    private Long hp;
    private Long atk_rise;
    private Long hp_rise;
    private Long def_rise;
    private String skill;
    private Integer type;
    private String color;
    private String name;
    private SwapPet changedPet;
    private Boolean acknowledge;

    public SwapPet buildSwapPet(Pet pet) {
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
        swapPet.setId(pet.getId());
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
}
