package cn.gavin.pet;

import android.content.Context;
import cn.gavin.Achievement;
import cn.gavin.Element;
import cn.gavin.Hero;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.monster.Monster;
import cn.gavin.palace.Base;
import cn.gavin.palace.nskill.NSkill;
import cn.gavin.pet.skill.HealthSkill;
import cn.gavin.pet.skill.PetSkill;
import cn.gavin.pet.skill.PetSkillList;
import cn.gavin.skill.SkillFactory;
import cn.gavin.utils.MazeContents;
import cn.gavin.utils.Random;
import cn.gavin.utils.StringUtils;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 10/29/2015.
 */
public class Pet extends Base {
    private NSkill skill;
    private long intimacy;
    private long deathCount;
    private String type;
    private String id;
    private boolean onUsed;
    private String fName;
    private String mName;
    private int sex;
    private String color = "#556B2F";
    private long atk_rise = 5;
    private long def_rise = 5;
    private long hp_rise = 20;
    private String owner;
    private String ownerId;


    public void click() {
        if(!MazeContents.hero.getUuid().equals(getOwnerId()) && MazeContents.hero.getUpperAtk() < getMaxAtk()/2){
            return;
        }
        intimacy++;
        if (intimacy > 50000000 || MazeContents.hero.getUpperAtk() < getMaxAtk()/4) {
            intimacy--;
        }
        if (getRandom().nextLong(intimacy) > 3000) {
            uHp += 100;
            atk += 50;
            def += 150;
        }
    }

    public boolean dan() {
        return hp > 0 && getRandom().nextInt(500) < getRandom().nextLong(intimacy / 500);
    }

    public boolean gon() {
        return hp > 0 && getRandom().nextInt(100) < getRandom().nextLong(intimacy / 200);
    }

    public void save() {
        PetDB.save(this);
    }

    public void load(Context context) {
        PetDB.load(this);
    }

    public void addHp(long hp) {
        super.addHp(hp);
        if (this.hp <= 0) {
            if (SkillFactory.getSkill("爱心", MazeContents.hero).isActive()) {
                intimacy *= 0.9;
            } else {
                intimacy *= 0.6;
            }
            deathCount++;
        }
    }

    public void setSkill(NSkill skill) {
        if (skill != null) {
            if (skill instanceof PetSkill) {
                this.skill = skill;
            } else {
                addSkill(skill);
            }
        }
        this.skill = skill;
    }

    public NSkill getSkill() {
        return skill;
    }

    public NSkill getAllSkill() {
        if (skill != null) {
            return skill;
        } else if (!getSkills().isEmpty()) {
            return getSkills().iterator().next();
        }
        return null;
    }

    public static Pet catchPet(Monster monster) {
        Random random = new Random();
        double rate = (((monster.getMaxHP() * 3 - monster.getMaxHP() *
                MazeContents.hero.getPetRate() * 2) / (monster.getMaxHP() * 3)) * monster.getRate() *
                (2 - MazeContents.hero.getPetRate())) * 10 / (255 * MazeContents.hero.getPets().size());
        if (rate >= 100) {
            rate = 98;
        }
        double current = random.nextInt(100) + random.nextDouble();
        if (PetDB.getPetCount(null) < MazeContents.hero.getPetSize() + 17 && rate > current) {
            Pet pet = cPet(monster, random);
            if (pet == null) return null;
            Achievement.pet.enable(MazeContents.hero);
            return pet;
        } else {
            return null;
        }
    }

    public static Pet cPet(Monster monster, Random random) {
        if (PetDB.getPetCount(null) < MazeContents.hero.getPetSize() + 17) {
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
            if(MazeContents.hero.ismV() && index%2==0){
                return null;
            }
            if (index >= 0 && index < Monster.lastNames.length - 11) {
                pet.setType(Monster.lastNames[index]);
            } else {
                return null;
            }
            pet.setLev(monster.getMazeLev());
            pet.setIntimacy(0l);
            pet.setSex(random.nextInt(2));
            pet.setOwner(MazeContents.hero.getName());
            pet.setOwnerId(MazeContents.hero.getUuid());
            if ((SkillFactory.getSkill("神赋", MazeContents.hero).isActive() && random.nextInt(100) < 30) || random.nextInt(5000) < 5) {
                int sindex = random.nextInt(PetSkillList.values().length);
                if (sindex == 0 || sindex == 1) {
                    sindex += random.nextInt(4);
                }
                NSkill petS = PetSkillList.values()[sindex].getSkill(pet);
                if (petS instanceof PetSkill) {
                    pet.setSkill(petS);
                } else {
                    pet.addSkill(petS);
                }
            } else if (random.nextInt(1000) == 1) {
                pet.setSkill(new HealthSkill());
            }
            if (SkillFactory.getSkill("霸气", MazeContents.hero).isActive()) {
                pet.setAtk_rise(pet.getAtk_rise() * 3);
                pet.setDef_rise(pet.getDef_rise() * 3);
                pet.setHp_rise(pet.getHp_rise() * 3);
            }
            PetDB.save(pet);
            return pet;
        } else {
            return null;
        }
    }

    public long getMaxAtk() {
        return atk;
    }

    public long getMaxDef() {
        return def;
    }

    public long getIntimacy() {
        return intimacy;
    }

    public String getFormatName() {
        if ("蛋".equals(getType())) {
            return "蛋";
        } else {
            return "<font color=\"" + color + "\">" + getName() + (sex == 0 ? "♂" : "♀") + "</font>(" + getElement() + ")";
        }
    }

    public void restore() {
        this.hp = getUHp();
    }

    public void restoreHalf() {
        this.hp = getUHp() / 2;
    }

    public void releasePet(Hero hero, MainGameActivity context) {
        hero.removePet(this);
        context.addMessage(getFormatName() + "对" + hero.getFormatName() + "说：拜拜……");
        PetDB.delete(this);
    }

    public long getDeathCount() {
        return deathCount;
    }

    public void setDeathCount(long deathCount) {
        this.deathCount = deathCount;
    }

    public void addAtk(Hero hero) {
        if (atk <= hero.getUpperDef() * 2) {
            atk += atk_rise;
            hero.addPoint(-1);
            click();
        }
    }

    public void addDef(Hero hero) {
        if (def <= hero.getUpperAtk()) {
            def += def_rise;
            hero.addPoint(-1);
            click();
        }
    }

    public void addHp(Hero hero) {
        if (getUHp() < hero.getHp()) {
            setUHp(getUHp() + hp_rise);
            hp += hp_rise;
            hero.addPoint(-1);
            click();
        }
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

    public boolean isOnUsed() {
        return onUsed;
    }

    public void setOnUsed(boolean onUsed) {
        this.onUsed = onUsed;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        if (!StringUtils.isNotEmpty(fName)) {
            fName = "未知";
        }
        this.fName = fName;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        if (!StringUtils.isNotEmpty(mName)) {
            mName = "未知";
        }
        this.mName = mName;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public static Pet egg(Pet f, Pet m, long lev, Hero hero) {
        Random random = new Random();
        double rate = (((hero.getUpperAtk() * 3 - hero.getUpperAtk() *
                MazeContents.hero.getPetRate() * 2) / (hero.getUpperHp() * 3)) * hero.getEggRate() *
                (2 - MazeContents.hero.getPetRate())) * 20 / 255;
        if (f.getType().equals(m.getType())) {
            rate *= 1.5;
        }
        if (hero.getElement() == Element.火) {
            rate *= 1.5;
        }
        if (rate >= 100) {
            rate = 98;
        }

        if (rate <= 0) {
            rate = 0.01;
        }

        double current = random.nextInt(100) + random.nextDouble();
        if (!f.getId().equals(m.getId()) && rate > current) {
            Pet egg = new Pet();
            String type = m.getType();
            if ("蛋".equals(type) || f.getName().endsWith("蛋") || m.getName().endsWith("蛋")) {
                return null;
            }
            String firstName = StringUtils.split(f.getName(), "的")[0];
            String secondName = StringUtils.split(m.getName(), "的")[1];
            secondName = secondName.replace(type, "");
            egg.setName(firstName + "的" + secondName + type);
            egg.setIntimacy(1000l);
            egg.setType("蛋");
            egg.setDeathCount(255 - (f.getDeathCount() + m.getDeathCount()));
            if (egg.getDeathCount() <= 0) {
                egg.setDeathCount(5);
            }
            egg.setfName(f.getName());
            egg.setmName(m.getName());
            egg.setHp(f.getUHp() / 2 + random.nextLong(m.getHp()));
            egg.setAtk(f.getMaxAtk() / 2 + random.nextLong(m.getMaxAtk()));
            egg.setDef(f.getMaxDef() / 2 + random.nextLong(m.getMaxDef()));
            egg.setAtk_rise((f.getAtk_rise() + m.getAtk_rise()) / 2);
            egg.setDef_rise((f.getDef_rise() + m.getDef_rise()) / 2);
            egg.setHp_rise((f.getHp_rise() + m.getHp_rise()) / 2);
            egg.setSex(random.nextInt(2));
            egg.setLev(lev);
            egg.setElement(Element.values()[random.nextInt(Element.values().length - 1)]);
            egg.setOwner(hero.getName());
            egg.setOwnerId(hero.getUuid());
            if (!f.getType().equals(m.getType())) {
                if (random.nextInt(10000) + random.nextFloat() < (31.115 + hero.getPetAbe())) {
                    String lastName = Monster.lastNames[random.nextInt(Monster.lastNames.length)];
                    egg.setName("变异的" + lastName);
                    if (lastName.equals("作者")) {
                        egg.setAtk(egg.getAtk() * 2);
                        egg.setName("变异的蟑螂");
                    }
                    egg.color = "#B8860B";
                    egg.atk_rise = MazeContents.hero.ATR_RISE;
                    egg.def_rise = MazeContents.hero.DEF_RISE;
                    egg.hp_rise = MazeContents.hero.MAX_HP_RISE;
                }
            }
            if ((SkillFactory.getSkill("恩赐", MazeContents.hero).isActive() && random.nextInt(100) < 45) || random.nextInt(1000) < 5) {
                NSkill petS = PetSkillList.values()[random.nextInt(PetSkillList.values().length)].getSkill(egg);
                if (petS instanceof PetSkill) {
                    egg.setSkill(petS);
                } else {
                    egg.addSkill(petS);
                }
            }
            if (random.nextInt(1000) < 10) {
                egg.setSkill(new HealthSkill());
            }
            NSkill pSkill = egg.getAllSkill();
            if (pSkill == null) {
                if (f.getAllSkill() != null && m.getAllSkill() != null) {
                    if (random.nextBoolean()) {
                        pSkill = f.getAllSkill();
                    } else {
                        pSkill = m.getAllSkill();
                    }
                } else if (f.getAllSkill() != null) {
                    pSkill = f.getAllSkill();
                } else if (m.getAllSkill() != null) {
                    pSkill = m.getAllSkill();
                }
                if (pSkill != null) {
                    if (pSkill instanceof PetSkill) {
                        egg.setSkill(pSkill);
                    } else {
                        egg.addSkill(pSkill);
                    }
                }
            }

            Achievement.egg.enable(hero);
            return egg;
        } else {
            return null;
        }
    }

    public long getAtk_rise() {
        return atk_rise;
    }

    public long getDef_rise() {
        return def_rise;
    }

    public long getHp_rise() {
        return hp_rise;
    }

    public void setAtk_rise(Long atk_rise) {
        this.atk_rise = atk_rise;
    }

    public void setHp_rise(Long hp_rise) {
        this.hp_rise = hp_rise;
    }

    public void setDef_rise(Long def_rise) {
        this.def_rise = def_rise;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
