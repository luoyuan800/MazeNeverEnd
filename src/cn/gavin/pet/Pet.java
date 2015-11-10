package cn.gavin.pet;

import android.content.Context;

import cn.gavin.Achievement;
import cn.gavin.Element;
import cn.gavin.Hero;
import cn.gavin.R;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.activity.MazeContents;
import cn.gavin.monster.Monster;
import cn.gavin.palace.Base;
import cn.gavin.palace.nskill.NSkill;
import cn.gavin.pet.skill.GoldenSearcher;
import cn.gavin.pet.skill.QuickGrow;
import cn.gavin.skill.SkillFactory;
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
    private int image;
    private long deathCount;
    private String type;
    private String id;
    private boolean onUsed;
    private String fName;
    private String mName;
    private int sex;
    private String color = "#556B2F";
    private long atk_rise = 1;
    private long def_rise = 1;
    private long hp_rise = 2;
    private String owner;


    public void click() {
        intimacy++;
        if (intimacy > 50000000) {
            intimacy--;
        }
        if (getRandom().nextLong(intimacy) > getRandom().nextLong(100000) + 1000) {
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
        if (hp <= 0) {
            if (SkillFactory.getSkill("爱心", MazeContents.hero, null).isActive()) {
                intimacy *= 0.9;
            } else {
                intimacy *= 0.6;
            }
            deathCount++;
        }
    }

    public void setSkill(NSkill skill) {
        this.skill = skill;
    }

    public NSkill getSkill() {
        return skill;
    }

    public static Pet catchPet(Monster monster) {
        Random random = new Random();
        double rate = (((monster.getMaxHP() * 3 - monster.getMaxHP() *
                MazeContents.hero.getPetRate() * 2) / (monster.getMaxHP() * 3)) * monster.getRate() *
                (2 - MazeContents.hero.getPetRate())) * 20 / (255 * MazeContents.hero.getPets().size());
        if (rate >= 100) {
            rate = 98;
        }
        double current = random.nextInt(100) + random.nextDouble();
        if (PetDB.getPetCount(null) < MazeContents.hero.getPetSize() + 5 && rate > current) {
            Pet pet = cPet(monster, random);
            if (pet == null) return null;
            Achievement.pet.enable(MazeContents.hero);
            return pet;
        } else {
            return null;
        }
    }

    public static Pet cPet(Monster monster, Random random) {
        if (PetDB.getPetCount(null) < MazeContents.hero.getPetSize() + 5) {
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
                pet.setType(Monster.lastNames[index]);
                setImage(pet, index);
            } else {
                return null;
            }
            pet.setLev(monster.getMazeLev());
            pet.setIntimacy(0l);
            pet.setSex(random.nextInt(2));
            pet.setOwner(MazeContents.hero.getName());
            if((SkillFactory.getSkill("神赋", MazeContents.hero, null).isActive() && random.nextInt(100) < 30) || random.nextInt(5000) < 5){
                if (random.nextBoolean()) {
                    pet.setSkill(new GoldenSearcher());
                } else {
                    pet.setSkill(new QuickGrow());
                }
            }
            if(SkillFactory.getSkill("霸气", MazeContents.hero, null).isActive()){
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
                //pet.image = R.drawable.xion;
            default:
                pet.image = R.drawable.h_4_s;
        }
    }

    public static long die(long num) {
        if (num == 0) return 0;
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
        if ("蛋".equals(getType())) {
            return "蛋";
        } else {
            return "<font color=\"" + color + "\">" + getName() + (sex == 0 ? "♂" : "♀") + "</font>(" + getElement() + ")";
        }
    }

    public void restore() {
        this.hp = getUHp();
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
        }
        click();
    }

    public void addDef(Hero hero) {
        if (def <= hero.getUpperAtk()) {
            def += def_rise;
            hero.addPoint(-1);
        }
        click();
    }

    public void addHp(Hero hero) {
        if (getUHp() < hero.getHp()) {
            setUHp(getUHp() + hp_rise);
            hp += hp_rise;
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
        this.fName = fName;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
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
                (2 - MazeContents.hero.getPetRate())) * 30 / 255;
        if (f.getType().equals(m.getType())) {
            rate *= 1.5;
        }
        if (hero.getElement() == Element.火) {
            rate *= 1.5;
        }
        if (rate >= 100) {
            rate = 98;
        }

        double current = random.nextInt(100) + random.nextDouble();
        if (!f.getId().equals(m.getId()) && rate > current) {
            Pet egg = new Pet();
            String type = m.getType();
            String firstName = StringUtils.split(f.getName(), "的")[0];
            String secondName = StringUtils.split(m.getName(), "的")[1];
            secondName = secondName.replace(type, "");
            egg.setName(firstName + "的" + secondName + type);
            egg.setIntimacy(1000l);
            egg.setType("蛋");
            egg.setDeathCount(255 - (f.getDeathCount() + m.getDeathCount()));
            egg.setfName(f.getName());
            egg.setmName(m.getName());
            egg.setHp(f.getUHp() / 2 + random.nextLong(m.getHp()));
            egg.setAtk(f.getMaxAtk() / 2 + random.nextLong(m.getMaxAtk()));
            egg.setDef(f.getMaxDef() / 2 + random.nextLong(m.getMaxDef()));
            egg.setAtk_rise((f.getAtk_rise() + m.getAtk_rise())/2);
            egg.setDef_rise((f.getDef_rise() + m.getDef_rise())/2);
            egg.setHp_rise((f.getHp_rise() + m.getHp_rise())/2);
            egg.setSex(random.nextInt(2));
            egg.setLev(lev);
            egg.setElement(Element.values()[random.nextInt(Element.values().length - 1)]);
            egg.setOwner(hero.getName());
            if (!f.getType().equals(m.getType())) {
                if (random.nextInt(10000) + random.nextFloat() < 2.015) {
                    String lastName = Monster.lastNames[random.nextInt(Monster.lastNames.length)];
                    egg.setType(lastName);
                    egg.setName("变异的" + lastName);
                    if (egg.getType().equals("作者")) {
                        egg.setType("蟑螂");
                        egg.setAtk(egg.getAtk() * 2);
                        egg.setName("变异的蟑螂");
                    }
                    egg.color = "#B8860B";
                    egg.atk_rise = MazeContents.hero.ATR_RISE;
                    egg.def_rise = MazeContents.hero.DEF_RISE;
                    egg.hp_rise = MazeContents.hero.MAX_HP_RISE;
                }
            }
            if((SkillFactory.getSkill("恩赐", MazeContents.hero, null).isActive() && random.nextInt(100) < 40) || random.nextInt(1000) < 5){
                if (random.nextBoolean()) {
                    egg.setSkill(new GoldenSearcher());
                } else {
                    egg.setSkill(new QuickGrow());
                }
            }
            PetDB.save(egg);
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
}
