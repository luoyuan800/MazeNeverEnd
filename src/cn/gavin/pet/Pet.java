package cn.gavin.pet;

import android.content.Context;
import android.database.Cursor;

import cn.gavin.Achievement;
import cn.gavin.Element;
import cn.gavin.Hero;
import cn.gavin.R;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.db.DBHelper;
import cn.gavin.log.LogHelper;
import cn.gavin.monster.Monster;
import cn.gavin.monster.MonsterDB;
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
    private float eggRate;
    private int index;
    private int image;


    public void click() {
        if (!MazeContents.hero.getUuid().equals(getOwnerId()) && MazeContents.hero.getUpperAtk() < getMaxAtk() / 2) {
            return;
        }
        intimacy++;
        if (intimacy > 50000000 || MazeContents.hero.getUpperAtk() < getMaxAtk() / 10) {
            intimacy--;
        }
        if (getRandom().nextLong(intimacy) > 500 + getRandom().nextInt(500)) {
            uHp += 50;
            atk += 10;
            def += 70;
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
                MazeContents.hero.getPetRate() * 2) / (monster.getMaxHP() * 3 + 1))
                * monster.getPetRate() *
                (2 - MazeContents.hero.getPetRate())) * 10 / (255 * MazeContents.hero.getPets().size() + 1);
        if (rate >= 100) {
            rate = 90;
        }
        double current = random.nextInt(100) + random.nextDouble() + random.nextInt(PetDB.getPetCount(null) + 1) / 10;
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
        if ((MazeContents.hero.ismV() && monster.getIndex() % 2 == 0) || monster.getIndex() == 25 || monster.getPetRate() < 0) {
            return null;
        }
        if (PetDB.getPetCount(null) < MazeContents.hero.getPetSize() + 15) {
            Pet pet = new Pet();
            pet.setElement(monster.getElement());
            pet.setName(monster.getName());
            long monsterHp = monster.getMaxHP();
            long hp = pet.getRandom().nextLong(monsterHp);
            if (hp == 0) {
                hp = monsterHp / 2 + 10;
            }
            pet.setDef((monsterHp - hp) / 300 + 10);
            if (pet.getMaxDef() > MazeContents.hero.getBaseDefense()) {
                pet.setDef(pet.getMaxDef() / 200 + 10);
            }
            pet.setAtk(monster.getAtk() / 500 + 10);
            if (pet.getMaxAtk() > MazeContents.hero.getBaseAttackValue()) {
                pet.setAtk(pet.getMaxAtk() / 400 + 10);
            }
            pet.setHp(hp / 500 + 20);
            if (pet.getUHp() > MazeContents.hero.getRealUHP()) {
                pet.setHp(pet.getUHp() / 200 + 20);
            }
            pet.setType(monster.getLastName());
            pet.setLev(monster.getMazeLev());
            pet.setIntimacy(0l);
            pet.setEggRate(monster.getEggRate());
            pet.setSex(random.nextInt(2));
            pet.setOwner(MazeContents.hero.getName());
            pet.setOwnerId(MazeContents.hero.getUuid());
            pet.setIndex(monster.getIndex());
            pet.setImage(monster.getImageId());
            if ((SkillFactory.getSkill("神赋", MazeContents.hero).isActive() && random.nextInt(100) < 30) || random.nextInt(5000) < 5) {
                int sindex = random.nextInt(PetSkillList.values().length);
                if (sindex < 5) {
                    sindex += random.nextInt(6);
                }
                if (sindex == 2) {
                    sindex = 3;
                }
                if (sindex >= PetSkillList.values().length) {
                    sindex = PetSkillList.values().length;
                }
                NSkill petS = PetSkillList.values()[sindex].getSkill(pet);
                if (petS instanceof PetSkill) {
                    pet.setSkill(petS);
                } else {
                    pet.addSkill(petS);
                }
            } else if (random.nextInt(1000) == 1) {
                final HealthSkill healthSkill = new HealthSkill();
                healthSkill.setMe(pet);
                pet.setSkill(healthSkill);
            }
            if (SkillFactory.getSkill("霸气", MazeContents.hero).isActive()) {
                pet.setAtk_rise(pet.getAtk_rise() * 3);
                pet.setDef_rise(pet.getDef_rise() * 3);
                pet.setHp_rise(pet.getHp_rise() * 3);
            }
            PetDB.save(pet);
            monster.setCatchCount(monster.getCatchCount() + 1);
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
            if (getHp() > 0) {
                return "<font color=\"" + color + "\">" + getName() + (sex == 0 ? "♂" : "♀") + "</font>(" + getElement() + ")";
            } else {
                return "<font color=\"#808080\">" + getName() + (sex == 0 ? "♂" : "♀") + "</font>(" + getElement() + ")";
            }
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
        float eggRate = f.getEggRate() / 3 + m.getEggRate() / 2;
        double rate = (((hero.getUpperAtk() * 3 - hero.getUpperAtk() *
                MazeContents.hero.getPetRate() * 2) / (hero.getUpperHp() * 3)) * (hero.getEggRate() + eggRate) *
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

        double current = random.nextInt(100) - 1 + random.nextDouble() + random.nextInt(PetDB.getPetCount(null) + 1) / 10;
        if (!f.getId().equals(m.getId()) && rate > current) {
            return getEgg(f, m, lev, hero, random);
        } else {
            return null;
        }
    }

    public static Pet getEgg(Pet f, Pet m, long lev, Hero hero, Random random) {
        Pet egg = new Pet();
        egg.setIndex(m.getIndex());
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
        Cursor fc = DBHelper.getDbHelper().excuseSOL("select atk,hp from monster where id = '" + f.getIndex() + "'");
        Cursor mc = DBHelper.getDbHelper().excuseSOL("select atk,hp from monster where id = '" + m.getIndex() + "'");
        if (!fc.isAfterLast()) {
            long batk = (StringUtils.toLong(fc.getString(fc.getColumnIndex("atk"))) + StringUtils.toLong(mc.getString(mc.getColumnIndex("atk")))) / 2;
            long bhp = (StringUtils.toInt(fc.getString(fc.getColumnIndex("hp"))) + StringUtils.toInt(mc.getString(mc.getColumnIndex("hp")))) / 2;
            egg.setHp(bhp);
            egg.setAtk(batk);
            fc.close();
            mc.close();
        } else {
            egg.setHp(f.getUHp() / 20 + random.nextLong(m.getHp()) + 1);
            egg.setAtk(f.getMaxAtk() / 20 + random.nextLong(m.getMaxAtk()) + 1);
        }
        egg.setDef(f.getMaxDef() / 2 + random.nextLong(m.getMaxDef()));
        egg.setAtk_rise((f.getAtk_rise() + m.getAtk_rise()) / 2);
        egg.setDef_rise((f.getDef_rise() + m.getDef_rise()) / 2);
        egg.setHp_rise((f.getHp_rise() + m.getHp_rise()) / 2);
        egg.setSex(random.nextInt(2));
        egg.setLev(lev);
        egg.setElement(Element.values()[random.nextInt(Element.values().length - 1)]);
        egg.setOwner(hero.getName());
        egg.setOwnerId(hero.getUuid());
        egg.setImage(m.getImage());
        if (!f.getType().equals(m.getType())) {
            if (random.nextInt(10000) + random.nextFloat() < (5.115 + hero.getPetAbe())) {
                int index = random.nextInt(MonsterDB.total);
                Cursor cursor = DBHelper.getDbHelper().excuseSOL("select * from monster where id = '" + index + "'");
                if (!cursor.isAfterLast()) {
                    egg.setName("变异的" + cursor.getString(cursor.getColumnIndex("type")));
                    if(index == 25) {
                        egg.setElement(Element.无);
                    }
                    egg.color = "#B8860B";
                    egg.atk_rise = MazeContents.hero.ATR_RISE * 2;
                    egg.def_rise = MazeContents.hero.DEF_RISE * 2;
                    egg.hp_rise = MazeContents.hero.MAX_HP_RISE * 2;
                    egg.setImage(cursor.getInt(cursor.getColumnIndex("img")));
                    egg.setAtk(egg.getMaxAtk() + StringUtils.toLong(cursor.getString(cursor.getColumnIndex("atk"))) / 2);
                }
                cursor.close();
            }
        }
        if ((SkillFactory.getSkill("恩赐", MazeContents.hero).isActive() &&
                random.nextInt(100) < 45) || random.nextInt(1000) < 5 || egg.getName().startsWith("变异的")) {
            int index = random.nextInt(PetSkillList.values().length);
            if (index == 2) index = 1;
            NSkill petS = PetSkillList.values()[index].getSkill(egg);
            if (petS instanceof PetSkill) {
                egg.setSkill(petS);
            } else {
                egg.addSkill(petS);
            }
        }else {
            if (random.nextInt(1000) < 10) {
                final HealthSkill healthSkill = new HealthSkill();
                healthSkill.setMe(egg);
                egg.setSkill(healthSkill);
            }
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

    public String getFirstName() {
        String[] names = StringUtils.split(getName(), "的");
        if (names.length > 1) {
            return names[0];
        } else {
            return "";
        }
    }

    public String getLastName() {
        String[] names = StringUtils.split(getName(), "的");
        if (names.length > 1) {
            return names[1];
        } else {
            return "";
        }
    }

    public float getEggRate() {
        return eggRate;
    }

    public void setEggRate(float eggRate) {
        this.eggRate = eggRate;
    }

    public int getIndex() {
        try{
        if(index <=0){
          String[] names = StringUtils.split(getName(), "的");
            if(names.length > 1){
                Cursor cursor = DBHelper.getDbHelper().excuseSOL("select id, type from monster");
                while(!cursor.isAfterLast()){
                    if(names[1].contains(cursor.getString(cursor.getColumnIndex("type")))){
                        index = cursor.getInt(cursor.getColumnIndex("id"));
                        break;
                    }
                    cursor.moveToNext();
                }
                cursor.close();
            }
        }
        }catch (Exception e){
            LogHelper.logException(e, false);
        }
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getImage() {
        if(!"蛋".equals(type)){
            if(image <= 0) {
                Cursor cursor = DBHelper.getDbHelper().excuseSOL("select img from monster where id = '" + getIndex() + "'");
                image = cursor.getInt(cursor.getColumnIndex("img"));
                cursor.close();
            }
        }else{
            return R.drawable.egg;
        }
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void updateMonster() {
        int index = getIndex();
        try {
            Cursor cursor = DBHelper.getDbHelper().excuseSOL("select catch_lev, catch from monster where id = '" + index + "'");
            if (!cursor.isAfterLast()) {
                long catch_lev = StringUtils.toLong(cursor.getString(cursor.getColumnIndex("catch_lev")));
                long catch_count = StringUtils.toLong(cursor.getString(cursor.getColumnIndex("catch")));
                if (catch_lev <= 0) {
                    catch_lev = MazeContents.maze.getLev();
                }
                catch_count++;
                DBHelper.getDbHelper().excuseSQLWithoutResult("update monster set catch_lev = '" +
                        catch_lev + "', " +
                        "catch = '" + catch_count + "' where id = '" + index + "'");
            }
            cursor.close();
        }catch (Exception e){
            LogHelper.logException(e, false);
        }
        }
}
