package cn.gavin.monster;

import android.database.Cursor;
import cn.gavin.Achievement;
import cn.gavin.Element;
import cn.gavin.Hero;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.db.DBHelper;
import cn.gavin.forge.list.ItemName;
import cn.gavin.gift.Gift;
import cn.gavin.log.LogHelper;
import cn.gavin.maze.Maze;
import cn.gavin.pet.Pet;
import cn.gavin.utils.MathUtils;
import cn.gavin.utils.MazeContents;
import cn.gavin.utils.Random;
import cn.gavin.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED
 * Created by luoyuan on 12/24/15.
 */
public class Monster {
    private FirstName firstName;
    private SecondName secondName;
    private String lastName;
    protected long atk;
    protected long hp;
    protected long material;
    private long mazeLev;
    private boolean defeat;
    private long maxHP;
    private String formatName;
    private String name;
    private float hitRate = 100.0f;
    private List<ItemName> items;
    private Element element;
    private boolean isHold = false;
    private long holdTurn = 0;
    private float silent;
    private long petRate = 0;
    private float petsub = 0f;
    private float eggRate;
    private int index;
    private long baseHp;
    private long baseAtk;
    private float baseEggRate;
    private float basePetRate;
    private String desc;
    private int imageId;
    private long meet_lev;
    private long catch_lev;
    private long beatCount;
    private long defeatCount;
    private long catchCount;
    private String type;

    public boolean isPetSub(Random random, Pet pet) {
        float v = petsub;
        if (!name.contains("守护者")) {
            int index = getIndex();
            v = petsub + index - MazeContents.getIndex(pet.getType());
            if ((pet.getMaxAtk() + pet.getMaxDef() + pet.getUHp()) / 3 < hp) {
                v /= 2;
            }
            if (v < 0) v = 0;
        }
        return (random.nextInt(100) + random.nextFloat()) < v;
    }

    public boolean isSilent(Random random) {
        return (random.nextInt(100) + random.nextFloat()) < silent;
    }

    public static Monster getBoss(Maze maze, Hero hero) {
        Random random = new Random();
        Monster monster;
        Cursor cursor = DBHelper.getDbHelper().excuseSOL("SELECT * FROM palace WHERE lev = '" + maze.getLev() + "'");
        if (!cursor.isAfterLast()) {
            long atk = StringUtils.toLong(cursor.getString(cursor.getColumnIndex("atk")));
            long hp = StringUtils.toLong(cursor.getString(cursor.getColumnIndex("hp"))) + StringUtils.toLong(cursor.getString(cursor.getColumnIndex("def")));
            monster = new Monster(FirstName.empty, SecondName.defender, cursor.getString(cursor.getColumnIndex("name")), atk, hp);
            monster.setElement(Element.valueOf(cursor.getString(cursor.getColumnIndex("element"))));
            monster.setSilent(65);
        } else {
            monster = buildDefaultDefender(maze, hero, random);
        }
        if (monster.material == 0) {
            monster.material = random.nextLong(maze.getLev() * monster.atk + 1) / 3115 + 55;
        }
        if (monster.material > 5000) {
            monster.material = 1000 + random.nextLong(5000);
        }
        monster.items = Arrays.asList(ItemName.原石, ItemName.铁矿石, ItemName.冷杉木,
                ItemName.萤石, ItemName.蚁须, ItemName.龙须);
        monster.builder = new StringBuilder("第");
        monster.builder.append(maze.getLev()).append("层").append("<br>-------");
        monster.element = Element.values()[random.nextInt(Element.values().length)];
        monster.mazeLev = maze.getLev();
        monster.formatName(hero);
        if (monster.silent < 1 && maze.getLev() > 500 && random.nextBoolean()) {
            monster.silent = 18.8f;
        }
        monster.setPetRate(0);
        return monster;
    }

    private static Monster buildDefaultDefender(Maze maze, Hero hero, Random random) {
        long hp = MathUtils.getMaxValueByRiseAndLev(hero.MAX_HP_RISE, maze.getLev()) * 2;
        long atk = MathUtils.getAvgValueByRiseAndLev(hero.ATR_RISE, maze.getLev()) * 2;
        if (hp <= 0) hp = Integer.MAX_VALUE - 10;
        if (atk <= 0) hp = Integer.MAX_VALUE - 100;
        if (atk > hero.getUpperHp() + hero.getDefenseValue()) {
            atk = atk / 2;
        }
        if (maze.getLev() < 5000) {
            atk /= 2;
        } else if (maze.getLev() < 1000) {
            atk /= 3;
        }
        if (maze.getLev() < 100) {
            atk /= 4;
        }
        if (maze.getLev() < hero.getMaxMazeLev() / 2) {
            atk /= 2;
        }

        if (hero.getMaterial() > 10000000) {
            atk += random.nextLong(hero.getMaterial() / (MainGameActivity.context != null ? MainGameActivity.context.getAlipay().getPayTime() + 1 : 1) + 1);
        }

        if (hp > hero.getUpperAtk() * 40) {
            hp = hero.getAttackValue() * 40;
        }
        if (atk < hero.getDefenseValue()) {
            atk += random.nextLong(hero.getDefenseValue() * 2);
        }
        if (hp < 0) {
            hp = hero.getUpperHp();
        }
        if (maze.getLev() / 10000 > 5 && random.nextLong(hero.getReincaCount()) < 3) {
            if (random.nextBoolean()) {
                hp += hero.getUpperHp() / (hero.getReincaCount() + 1);
                atk += random.nextLong((hero.getUpperAtk() + hero.getRealUHP()) / 2);
                Monster monster = new Monster(FirstName.tire, SecondName.empty, "作者",
                        hp,
                        atk);
                monster.setSilent(55 / (int) (hero.getReincaCount() + 1));
                monster.petsub = 65;
                Achievement.Tire.enable(hero);
                return monster;
            }
        } else if (maze.getLev() / 10000 >= 4) {
            if (random.nextBoolean() && random.nextLong(hero.getReincaCount()) < 5) {
                atk += (random.nextLong((hero.getUpperAtk() + hero.getRealUHP()))) / (hero.getReincaCount() + 1);
                Monster monster = new Monster(FirstName.tire, SecondName.empty, "作者",
                        hp,
                        atk);
                monster.setSilent(20);
                monster.petsub = 50;
                Achievement.anger.enable(hero);
                return monster;
            }
        } else if (maze.getLev() % 50000 == 0) {
            if (random.nextBoolean() && random.nextLong(hero.getReincaCount()) < 2) {
                //atk += random.nextLong((hero.getUpperAtk() + hero.getRealUHP()));
                hp += (hero.getUpperHp() + hero.getUpperAtk() + hero.getUpperDef()) / (hero.getReincaCount() + 1);
                if (!Achievement.richer.isEnable()) {
                    atk *= 5;
                }
                Monster monster = new Monster(FirstName.frailty, SecondName.empty, "作者",
                        hp,
                        atk);
                monster.setSilent(99 / (int) (hero.getReincaCount() + 1));
                monster.petsub = 90;
                monster.material = -2222222;
                return monster;
            } else {
                atk = hero.getUpperDef() + 1000;
                hp += (hero.getUpperHp() + hero.getUpperAtk() + hero.getUpperDef()) / (hero.getReincaCount() + 1);
                if (!Achievement.richer.isEnable()) {
                    hp *= 2;
                }
                Monster monster = new Monster(FirstName.stupid, SecondName.empty, "作者",
                        hp,
                        atk);
                monster.setSilent(99 / (int) (hero.getReincaCount() + 1));
                monster.petsub = 96;
                monster.material = 2222222;
                return monster;
            }
        }
        return new Monster(FirstName.empty, SecondName.empty, "第" + maze.getLev() + "层守护者",
                hp,
                atk);
    }

    public Monster(FirstName firstName, SecondName secondName, String lastName, long hp, long atk) {
        this.atk = atk;
        this.hp = hp;
        maxHP = hp;
        this.firstName = firstName;
        this.secondName = secondName;
        this.lastName = lastName;
    }

    public Monster() {

    }

    public Monster(Hero hero, Maze maze, boolean mark){
        Random random = new Random();
        mazeLev = maze.getLev();
            firstName = FirstName.getRandom(mazeLev, random);
            secondName = SecondName.getRandom(mazeLev, random);
            lastName = "monster";
            hp = 55;
            atk = 15;
            if (maze.getLev() < 5000) {
                hp += firstName.getHPAddition(hp);
                hp += secondName.getHpAddition(hp);
                atk += firstName.getAtkAddition(atk);
                atk += secondName.getAtkAddition(atk);
                hp = MathUtils.getMonsterHP(hp, maze.getLev(), hero.MAX_HP_RISE, random);
                atk = MathUtils.getMonsterAtk(atk, maze.getLev(), hero.ATR_RISE, hero.DEF_RISE, random);
            } else {
                hp = MathUtils.getMonsterHP(hp, maze.getLev(), hero.MAX_HP_RISE, random);
                atk = MathUtils.getMonsterAtk(atk, maze.getLev(), hero.ATR_RISE, hero.DEF_RISE, random);
                hp += firstName.getHPAddition(hp);
                hp += secondName.getHpAddition(hp);
                atk += firstName.getAtkAddition(atk);
                atk += secondName.getAtkAddition(atk);
            }
    }

    public Monster(Hero hero, Maze maze) {
        Random random = hero.getRandom();
        mazeLev = maze.getLev();
        int id = (int) random.nextLong(maze.getLev() / 10 < MonsterDB.total - 5 ? maze.getLev() / 10 + 4 : MonsterDB.total) + 1;
        Cursor cursor = DBHelper.getDbHelper().excuseSOL("SELECT * FROM monster WHERE id = '" + id + "'");
        if (!cursor.isAfterLast()) {
            index = id;
            firstName = FirstName.getRandom(mazeLev, random);
            secondName = SecondName.getRandom(mazeLev, random);
            lastName = cursor.getString(cursor.getColumnIndex("type"));
            hp = StringUtils.toLong(cursor.getString(cursor.getColumnIndex("hp")));
            atk = StringUtils.toLong(cursor.getString(cursor.getColumnIndex("atk")));
            if (maze.getLev() < 5000) {
                hp += firstName.getHPAddition(hp);
                hp += secondName.getHpAddition(hp);
                atk += firstName.getAtkAddition(atk);
                atk += secondName.getAtkAddition(atk);
                hp = MathUtils.getMonsterHP(hp, maze.getLev(), hero.MAX_HP_RISE, random);
                atk = MathUtils.getMonsterAtk(atk, maze.getLev(), hero.ATR_RISE, hero.DEF_RISE, random);
            } else {
                hp = MathUtils.getMonsterHP(hp, maze.getLev(), hero.MAX_HP_RISE, random);
                atk = MathUtils.getMonsterAtk(atk, maze.getLev(), hero.ATR_RISE, hero.DEF_RISE, random);
                hp += firstName.getHPAddition(hp);
                hp += secondName.getHpAddition(hp);
                atk += firstName.getAtkAddition(atk);
                atk += secondName.getAtkAddition(atk);
            }
            petRate = StringUtils.toLong(cursor.getString(cursor.getColumnIndex("pet_rate")));
            petRate += secondName.getPetRate();
            silent += firstName.getSilent();
            silent += secondName.getSilent();
            petsub = StringUtils.toFloat(cursor.getString(cursor.getColumnIndex("pet_sub")));
            eggRate = StringUtils.toFloat(cursor.getString(cursor.getColumnIndex("egg_rate")));
            eggRate += firstName.getEggRate();
            defeatCount = StringUtils.toLong(cursor.getString(cursor.getColumnIndex("defeat")));
            beatCount = StringUtils.toLong(cursor.getString(cursor.getColumnIndex("beat")));
            catchCount = StringUtils.toLong(cursor.getString(cursor.getColumnIndex("catch")));
            meet_lev = StringUtils.toLong(cursor.getString(cursor.getColumnIndex("meet_lev")));
            catch_lev = StringUtils.toLong(cursor.getString(cursor.getColumnIndex("catch_lev")));
            imageId = cursor.getInt(cursor.getColumnIndex("img"));
            //petRate = 100 - (mazeLev / 100) - secondAdditionRate[second];
            if (petRate < 0) {
                petRate = 0;
            }
            if(eggRate < 0){
                eggRate = 0;
            }
            petRate /= 2;
            items = new ArrayList<ItemName>();
            try{
            for(String itemName : StringUtils.split(cursor.getString(cursor.getColumnIndex("drop_item")), "_")){
                items.add(ItemName.valueOf(itemName));
            }
            }catch (Exception e){
                LogHelper.logException(e);
            }
            cursor.close();
        } else {
            firstName = FirstName.image;
            secondName = SecondName.empty;
            lastName = hero.getName();
            hp = hero.getUpperHp() + hero.getUpperDef();
            atk = hero.getUpperAtk();
            this.silent = 80;
            this.petsub = 75;
            this.element = hero.getElement();
            this.mazeLev = hero.getMaxMazeLev();
            this.formatName(MazeContents.hero);
            if (this.getHp() < 0) {
                this.hp = Long.MAX_VALUE - 10000;
            }
            this.maxHP = this.hp;
        }
        if(hero.getMaterial() > 50000000 && hero.getGift()!= Gift.Searcher){
            atk += random.nextLong(hero.getMaterial() * 2) + 1000;
            hp += random.nextLong(hero.getMaterial() * 3) + 10000;
        }
        long m1 = random.nextLong(hp + 1) / 180 + 5;
        long m2 = random.nextLong(atk + 1) / 409 + 10;
        material = random.nextLong((m1 + m2) / 828 + 1) + 20 + random.nextLong(maze.getLev() / 5 + 1);
        if (hero.getMaxMazeLev() < 10000) {
            material += 30;
        }
        if (hero.getMaxMazeLev() > 5000 && maze.getLev() < hero.getMaxMazeLev()) {
            material /= 2;
        }
        if (material > 3000) {
            material = 300 + random.nextInt(2700);
        }
        maxHP = hp;

        element = Element.values()[random.nextInt(Element.values().length)];

        if (maze.getLev() > 30000) {
            int addi = 10;
            if (hero.getReincaCount() > 0) {
                addi /= (hero.getReincaCount() * 10);
            }
            if (addi < 1) {
                addi = 1;
            }
            hp *= addi;
            atk *= addi;
        }
        formatName(hero);
    }

    private void formatName(Hero hero) {
        if (getAtk() > (hero.getUpperHp() + hero.getDefenseValue()) / 2) {
            setFormatName("<B><font color=\"red\">" + getName() + "</font></B>" + "(" + element + ")");
            if (!getName().startsWith("【守护者】") && silent == 0 && mazeLev < 10000) {
                atk = hero.getUpperDef() + hero.getUpperAtk() / 4;
                if (mazeLev > 2000) {
                    silent = hero.getRandom().nextLong(20 + mazeLev / 1000) + hero.getRandom().nextFloat();
                }
            }
        } else {
            setFormatName(getName() + "(" + element + ")");
            if (silent == 0 && mazeLev > 4000) {
                silent = hero.getRandom().nextLong(4 + mazeLev / 5000) + hero.getRandom().nextFloat();
            }
        }
    }

    public long getAtk() {
        return atk;
    }

    public long getHp() {
        return hp;
    }

    public void addHp(long hp) {
        this.hp += hp;
    }

    public long getMaterial() {
        return material;
    }

    public String getName() {
        if (name == null) {
            name = (StringUtils.isNotEmpty(firstName.getName()) ? (firstName.getName() + "的") : "") + secondName.getName() + lastName;
        }
        return name;
    }

    public boolean isDefeat() {
        return defeat;
    }

    public void setDefeat(boolean defeat) {
        this.defeat = defeat;
    }

    public long getMazeLev() {
        return mazeLev;
    }

    public void setMazeLev(long mazeLev) {
        this.mazeLev = mazeLev;
    }

    public long getMaxHP() {
        return maxHP;
    }

    public String getFormatName() {
        return formatName;
    }

    public void setFormatName(String formatName) {
        this.formatName = formatName;
    }

    public List<ItemName> getItems() {
        return items;
    }

    public void setItems(List<ItemName> items) {
        this.items = items;
    }

    public float getHitRate() {
        return hitRate;
    }

    public void setHitRate(float hitRate) {
        this.hitRate = hitRate;
    }

    private StringBuilder builder;

    public void addBattleDesc(String desc) {
        if (builder == null) builder = new StringBuilder();
        builder.append("<br>").append(desc);
    }

    public void addBattleSkillDesc(String desc) {
        if (builder == null) builder = new StringBuilder();
        builder.append("<br><font color=\"#8B0000\">").append(desc).append("</font>");
    }

    public String getBattleMsg() {
        return builder.toString();
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public boolean isHold() {
        if (holdTurn-- <= 0) {
            isHold = false;
        }
        return isHold;
    }

    public void setHold(boolean isHold) {
        this.isHold = isHold;
    }

    public long getHoldTurn() {
        return holdTurn;
    }

    public void setHoldTurn(long holdTurn) {
        this.holdTurn = holdTurn;
    }

    public long getPetRate() {
        return petRate;
    }

    public void setPetRate(long petRate) {
        this.petRate = petRate;
    }

    public static Monster CHEATBOSS() {
        Monster monster = new Monster(FirstName.really, SecondName.Strong, "作者", Long.MAX_VALUE - 10, Long.MAX_VALUE - 10);
        monster.silent = 100;
        monster.mazeLev = 0;
        monster.element = Element.水;
        monster.petsub = 100;
        monster.formatName(MazeContents.hero);
        monster.maxHP = monster.hp;
        return monster;
    }

    public static Monster copy(Hero hero) {
        Monster monster = new Monster(FirstName.image, SecondName.empty, hero.getName(), hero.getUpperHp() + hero.getUpperDef(), hero.getUpperAtk());
        monster.silent = 80;
        monster.petsub = 75;
        monster.element = hero.getElement();
        monster.mazeLev = hero.getMaxMazeLev();
        monster.formatName(MazeContents.hero);
        monster.atk = monster.getAtk() * 2;
        if (monster.getHp() < 0) {
            monster.hp = Long.MAX_VALUE - 10000;
        }
        monster.maxHP = monster.hp;
        return monster;
    }

    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
    }

    public void setSilent(int silent) {
        this.silent = silent;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getLastName() {
        return lastName;
    }

    public float getEggRate() {
        return eggRate;
    }

    public long getBaseHp() {
        return baseHp;
    }

    public void setBaseHp(long baseHp) {
        this.baseHp = baseHp;
    }

    public long getBaseAtk() {
        return baseAtk;
    }

    public void setBaseAtk(long baseAtk) {
        this.baseAtk = baseAtk;
    }

    public float getBaseEggRate() {
        return baseEggRate;
    }

    public void setBaseEggRate(float baseEggRate) {
        this.baseEggRate = baseEggRate;
    }

    public float getBasePetRate() {
        return basePetRate;
    }

    public void setBasePetRate(float basePetRate) {
        this.basePetRate = basePetRate;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public long getMeet_lev() {
        return meet_lev;
    }

    public void setMeet_lev(long meet_lev) {
        this.meet_lev = meet_lev;
    }

    public long getCatch_lev() {
        return catch_lev;
    }

    public void setCatch_lev(long catch_lev) {
        this.catch_lev = catch_lev;
    }

    public long getBeatCount() {
        return beatCount;
    }

    public void setBeatCount(long beatCount) {
        this.beatCount = beatCount;
    }

    public long getDefeatCount() {
        return defeatCount;
    }

    public void setDefeatCount(long defeatCount) {
        this.defeatCount = defeatCount;
    }

    public long getCatchCount() {
        return catchCount;
    }

    public void setCatchCount(long catchCount) {
        this.catchCount = catchCount;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}

