package cn.gavin.monster.nmonster;

import android.database.Cursor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import cn.gavin.Achievement;
import cn.gavin.Element;
import cn.gavin.Hero;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.db.DBHelper;
import cn.gavin.forge.list.ItemName;
import cn.gavin.maze.Maze;
import cn.gavin.pet.Pet;
import cn.gavin.utils.MazeContents;
import cn.gavin.utils.Random;
import cn.gavin.utils.StringUtils;

/**
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED
 * Created by luoyuan on 12/24/15.
 */
public class Monster {
    private String firstName;
    private String secondName;
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
    private long petRate = 300;
    private float petsub = 0f;
    private int index;
    public boolean isPetSub(Random random, Pet pet) {
        float v = petsub;
        if(!name.contains("守护者")) {
            int index = getIndex();
            v = petsub + index - MazeContents.getIndex(pet.getType());
            if((pet.getMaxAtk() + pet.getMaxDef() + pet.getUHp())/3 < hp ){
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
            monster = new Monster("", "【守护者】", cursor.getString(cursor.getColumnIndex("name")), atk, hp);
            monster.setElement(Element.valueOf(cursor.getString(cursor.getColumnIndex("element"))));
            monster.setSilent(65);
        }else {
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
        long hp = (hero.getAttackValue() / 20) * (random.nextLong(maze.getLev() + 1)) + random.nextLong(hero.getUpperHp() + 1) + maze.getLev() * 1000;
        long atk = (hero.getDefenseValue() + hero.getHp()) / 5 + maze.getLev() * 32 + random.nextLong(hero.getAttackValue() / 3 + maze.getLev() + 1);
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
                Monster monster = new Monster("心好累", "", "作者",
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
                Monster monster = new Monster("愤怒", "", "作者",
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
                Monster monster = new Monster("心灵脆弱", "", "作者",
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
                Monster monster = new Monster("傻乎乎", "", "作者",
                        hp,
                        atk);
                monster.setSilent(99 / (int) (hero.getReincaCount() + 1));
                monster.petsub = 96;
                monster.material = 2222222;
                return monster;
            }
        }
        return new Monster("第" + maze.getLev() + "层", "守护", "者",
                hp,
                atk);
    }

    public Monster(String firstName, String secondName, String lastName, long hp, long atk) {
        this.atk = atk;
        this.hp = hp;
        maxHP = hp;
        this.firstName = firstName;
        this.secondName = secondName;
        this.lastName = lastName;
    }

    public Monster(Hero hero, Maze maze) {
        Random random =hero.getRandom();
        mazeLev = maze.getLev();
        int id = (int) random.nextLong(maze.getLev()/10 < MonsterDB.total ? maze.getLev()/10 + 4 : MonsterDB.total);
        Cursor cursor = DBHelper.getDbHelper().excuseSOL("SELECT * FROM monster WHERE id = '" + id + "'");
        if(!cursor.isAfterLast()){
            lastName = cursor.getString(cursor.getColumnIndex("type"));
        }else{
            //Build boss
        }
        long m1 = random.nextLong(hp + 1) / 180 + 5;
        long m2 = random.nextLong(atk + 1) / 409 + 10;
        material = random.nextLong((m1 + m2) / 828 + 1) + 20 + random.nextLong(maze.getLev() / 5 + 1);
        if(hero.getMaxMazeLev() < 10000){
            material += 30;
        }
        if(hero.getMaxMazeLev() > 5000 && maze.getLev() < hero.getMaxMazeLev()){
            material/=2;
        }
        if (material > 3000) {
            material = 300 + random.nextInt(2700);
        }
        maxHP = hp;

        element = Element.values()[random.nextInt(Element.values().length)];
        //petRate = 100 - (mazeLev / 100) - secondAdditionRate[second];
        if (petRate < 5) {
            petRate = 5;
        }
        petRate /= 2;

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
            if (!getName().startsWith("【守护者】") && silent == 0) {
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
            name = (StringUtils.isNotEmpty(firstName) ? (firstName + "的") : "") + secondName + lastName;
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
        Monster monster = new Monster("真正", "强壮", "作者", Long.MAX_VALUE - 10, Long.MAX_VALUE - 10);
        monster.silent = 100;
        monster.mazeLev = 0;
        monster.element = Element.水;
        monster.petsub = 100;
        monster.formatName(MazeContents.hero);
        monster.maxHP = monster.hp;
        return monster;
    }

    public static Monster copy(Hero hero) {
        Monster monster = new Monster("镜像", "", hero.getName(), hero.getUpperHp() + hero.getUpperDef(), hero.getUpperAtk());
        monster.silent = 80;
        monster.petsub = 75;
        monster.element = hero.getElement();
        monster.mazeLev = hero.getMaxMazeLev();
        monster.formatName(MazeContents.hero);
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
}
