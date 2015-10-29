package cn.gavin.pet;

import android.content.Context;
import android.content.SharedPreferences;

import cn.gavin.Element;
import cn.gavin.Hero;
import cn.gavin.R;
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
    private long maxAtk;
    private long maxDef;
    private long maxHp;
    private int image;

    public void click() {
        intimacy++;
        if (intimacy > 50000000) {
            intimacy--;
        }
        if (getRandom().nextLong(intimacy / 10000) > 100 + getRandom().nextInt(50000)) {
            maxHp *= 2;
            maxAtk *= 1.2;
            maxDef *= 1.3;
        }
    }

    public boolean dan() {
        return getRandom().nextInt(1000) < getRandom().nextLong(intimacy / 1000);
    }

    public boolean gon() {
        return getRandom().nextInt(100) < getRandom().nextLong(intimacy / 1000);
    }

    public void save(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("pet", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("name", getName());
        editor.putLong("atk", atk);
        editor.putLong("def", def);
        editor.putLong("hp", hp);
        editor.putLong("u_hp", getUHp());
        if (skill != null) {
            editor.putLong("skill_count", skill.getCount());
            editor.putString("skill_name", skill.getName());
        }
        editor.putString("element", getElement().name());
        editor.putLong("lev", getLev());
        editor.putInt("hit", getHit());
        editor.putInt("parry", getParry());
        editor.putInt("doge", getDodge());
        editor.putLong("intimacy", intimacy);
        editor.putLong("max_atk", maxAtk);
        editor.putLong("max_def", maxDef);
        editor.putLong("max_hp", hp);
        editor.apply();
    }

    public void load(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("pet", Context.MODE_PRIVATE);
        String name = preferences.getString("name", "");
        if (StringUtils.isNotEmpty(name)) {
            setName(name);
            setAtk(preferences.getLong("atk", 0));
            setDef(preferences.getLong("def", 0));
            setHp(preferences.getLong("hp", 0));
            setUHp(preferences.getLong("u_hp", 0));
            long count = preferences.getLong("skill_count", 0);
            String skillName = preferences.getString("skill_name", "");
            NSkill skill = NSkill.createSkillByName(skillName, this, count, null);
            if (skill != null) {
                this.setSkill(skill);
            }
            setElement(Element.valueOf(preferences.getString("element", "无")));
            setLev(preferences.getLong("lev", 1));
            setHit(preferences.getInt("hit", 0));
            setParry(preferences.getInt("parry", 0));
            setDodge(preferences.getInt("doge", 0));
            this.intimacy = preferences.getLong("intimacy", 0);
            setMaxAtk(preferences.getLong("max_atk", 1));
            setMaxDef(preferences.getLong("max_def", 0));
            setMaxHp(preferences.getLong("max_hp", 0));
            int index = Monster.getIndex(this.getName());
            if (index >= 0 && index < Monster.lastNames.length - 7) {
                setImage(this, index);
            }
        }
    }

    public void addHp(long hp) {
        super.addHp(hp);
        if (hp <= 0) {
            intimacy *= 0.9;
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
        pet.setMaxDef(monsterHp - hp);
        pet.setMaxAtk(monster.getAtk());
        pet.setMaxHp(hp);
        pet.setHp(die(hp));
        pet.setAtk(die(pet.getMaxAtk()));
        pet.setDef(die(pet.getMaxDef()));
        int index = Monster.getIndex(pet.getName());
        if (index >= 0 && index < Monster.lastNames.length - 7) {
            setImage(pet, index);
        }else{
            return null;
        }
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
        return maxAtk;
    }

    public void setMaxAtk(long maxAtk) {
        this.maxAtk = maxAtk;
    }

    public long getMaxDef() {
        return maxDef;
    }

    public void setMaxDef(long maxDef) {
        this.maxDef = maxDef;
    }

    public long getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(long maxHp) {
        this.maxHp = maxHp;
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
        this.hp = maxHp;
    }

    public void releasePet(Hero hero, Context context) {
        hero.setPet(null);
        SharedPreferences preferences = context.getSharedPreferences("pet", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("name", "");
        editor.apply();
    }
}
