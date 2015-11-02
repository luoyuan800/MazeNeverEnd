package cn.gavin.pet;

import android.content.Context;
import android.content.SharedPreferences;

import cn.gavin.Element;
import cn.gavin.Hero;
import cn.gavin.R;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.activity.MazeContents;
import cn.gavin.monster.Monster;
import cn.gavin.palace.Base;
import cn.gavin.palace.nskill.NSkill;
import cn.gavin.utils.StringUtils;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 10/29/2015.
 */
public class Pet extends Base {
    private NSkill skill;
    private long intimacy;
    private int image;
    private long deathCount;
    private String type;
    private String id;

    public void click() {
        intimacy++;
        if (intimacy > 50000000) {
            intimacy--;
        }
        if (getRandom().nextLong(intimacy / 10000) > 100 + getRandom().nextInt(50000)) {
            uHp *= 1.5;
            atk *= 1.1;
            def *= 1.2;
        }
    }

    public boolean dan() {
        return getRandom().nextInt(1000) < getRandom().nextLong(intimacy / 1000);
    }

    public boolean gon() {
        return getRandom().nextInt(100) < getRandom().nextLong(intimacy / 1000);
    }

    public void save() {
        PetDB.save(this);
    }

    public void load(Context context) {

    }

    public void addHp(long hp) {
        super.addHp(hp);
        if (hp <= 0) {
            intimacy *= 0.9;
            deathCount ++;
        }
    }

    public void setSkill(NSkill skill) {
        this.skill = skill;
    }

    public NSkill getSkill() {
        return skill;
    }

    public static Pet catchPet(Monster monster) {
        Pet pet = new Pet();
        pet.setElement(monster.getElement());
        pet.setName(monster.getName());
        long monsterHp = monster.getMaxHP();
        long hp = pet.getRandom().nextLong(monsterHp);
        if (hp == 0) {
            hp = monsterHp / 2;
        }
        pet.setDef(monsterHp - hp);
        pet.setAtk(monster.getAtk());
        pet.setHp(hp);
        int index = Monster.getIndex(pet.getName());
        if (index >= 0 && index < Monster.lastNames.length - 7) {
            setImage(pet, index);
        }else{
            return null;
        }
        pet.setLev(monster.getMazeLev());
        return pet;
    }

    private static void setImage(Pet pet, int index) {
        switch (index) {
            case 0:
                pet.image = R.drawable.zl;
                break;
            case 1:
                pet.image = R.drawable.qy;
                break;
            case 2:
                pet.image = R.drawable.pc;
                break;
            case 3:
                pet.image = R.drawable.feie;
                break;
            case 4:
                pet.image = R.drawable.zz;
                break;
            case 5:
                pet.image = R.drawable.laoshu;
                break;
            case 6:
                pet.image = R.drawable.mayi;
                break;
            case 7:
                pet.image = R.drawable.laohu;
                break;
            case 8:
                pet.image = R.drawable.jiao;
                break;
            case 9:
                pet.image = R.drawable.xiezi;
                break;
            case 10:
                pet.image = R.drawable.srn;
                break;
            case 11:
                pet.image = R.drawable.bianfu;
                break;
            case 12:
                pet.image = R.drawable.se;
                break;
            case 13:
                pet.image = R.drawable.niu;
                break;
            case 14:
                pet.image = R.drawable.wugui;
                break;
            case 15:
                pet.image = R.drawable.santoushe;
                break;
            case 16:
                pet.image = R.drawable.ciwei;
                break;
            case 17:
                pet.image = R.drawable.lan;
                break;
            case 18:
                pet.image = R.drawable.jingling;
                break;
            case 19:
                pet.image = R.drawable.jiangshi;
                break;
            case 20:
                pet.image = R.drawable.fengh;
                break;
            case 21:
                pet.image = R.drawable.long_pet;
                break;
            case 24:
                pet.image = R.drawable.xion;
            default:
                pet.image = R.drawable.h_4_s;
        }
    }

    public static long die(long num) {
        if(num == 0) return 0;
        if (num > 100000) {
            return 10;
        }
        if (num > 10000000) {
            return 20;
        }
        if (num > 100000000) {
            return 30;
        }
        if (num > 1000000000) {
            return 40;
        }
        if (num > 10000000000l) {
            return 50;
        }
        return 5;
    }

    public long getMaxAtk() {
        return atk;
    }

    public long getMaxDef() {
        return def;
    }

    public int getImage() {
        return image;
    }

    public long getIntimacy() {
        return intimacy;
    }

    public String getFormatName() {
        return "<font color=\"#556B2F\">" + getName() + "</font>(" + getElement() + ")";
    }

    public void restore() {
        this.hp = getUHp();
    }

    public void releasePet(Hero hero, MainGameActivity context) {
        context.addMessage(getFormatName() + "对" + hero.getFormatName() + "说：拜拜……");
        PetDB.delete(this);
    }

    public long getDeathCount() {
        return deathCount;
    }

    public void setDeathCount(long deathCount) {
        this.deathCount = deathCount;
    }

    public void addAtk(Hero hero){
        if(atk <= hero.getUpperDef()/10){
            atk += hero.ATR_RISE;
            hero.addPoint(-1);
        }
        click();
    }

    public void addDef(Hero hero){
        if(def <= hero.getUpperAtk()/10){
            def += hero.DEF_RISE;
            hero.addPoint(-1);
        }
        click();
    }

    public void addHp(Hero hero){
        if(getUHp() < hero.getHp()/10){
            setUHp(getUHp() + hero.MAX_HP_RISE);
            hp += hero.MAX_HP_RISE;
            hero.addPoint(-1);
        }
        click();
    }


    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setIntimacy(Long intimacy) {
        this.intimacy = intimacy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
