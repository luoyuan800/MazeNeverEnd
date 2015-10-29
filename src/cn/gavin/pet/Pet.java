package cn.gavin.pet;

import android.content.Context;
import android.content.SharedPreferences;
import cn.gavin.Element;
import cn.gavin.Hero;
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
        pet.setMaxHp(monster.getMaxHP());
        long monsterHp = monster.getHp();
        long hp = pet.getRandom().nextLong(monsterHp);
        if (hp == 0) {
            hp = monsterHp / 2;
        }
        pet.setMaxDef(monsterHp - hp);
        pet.setMaxHp(hp);
        pet.setHp(die(hp));
        pet.setAtk(die(pet.getMaxAtk()));
        pet.setDef(die(pet.getMaxAtk()));
        return pet;
    }

    public static long die(long num) {
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
