package cn.gavin.monster;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import cn.gavin.Element;
import cn.gavin.Hero;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.activity.MazeContents;
import cn.gavin.forge.Item;
import cn.gavin.forge.list.ItemName;
import cn.gavin.maze.Maze;
import cn.gavin.palace.PalaceMonster;
import cn.gavin.utils.Random;
import cn.gavin.utils.StringUtils;

/**
 * gluo on 8/26/2015.
 */
public class Monster {
    public final static String[] firstNames = {"普通", "怪异", "飞翔", "稀有", "发狂", "神奇", "神经", "传奇", "无敌"};
    private final static long[] firstAdditionHP = {15, 25, 400, 1000, 500, 2000, 10000, 70000, 70000};
    private final static long[] firstAdditionAtk = {3, 25, 100, 500, 2000, 2800, 4000, 10000, 20000};
    private final static String[] secondNames = {"小", "中", "大", "大大", "红色", "绿色", "人面","强壮","无力","红唇"};
    private final static long[] secondAdditionHP = {15, 25, 100, 500, 1003, 2000, 6000, 10000, 1000000, 100};
    private final static long[] secondAdditionAtk = {5, 10, 30, 100, 150, 500, 5500, 500, 1, 9000};
    public final static String[] lastNames = {"蟑螂", "大蚯蚓", "异壳虫", "巨型飞蛾", "猪", "老鼠", "嗜血蚁",
            "老虎", "蛟", "变异蝎", "食人鸟", "丑蝙蝠", "蛇", "野牛", "龟", "三头蛇", "刺猬", "狼", "精灵",
            "僵尸",  "凤凰", "龙", "骷髅","作者", "熊","朱厌","陆吾","山魁","穷奇"};
    private final static long[] baseHP = {8, 20, 55, 75, 95, 115, 400, 1000, 800, 520, 280, 350, 380,
            450, 530, 1000, 1500, 2300, 4000, 4500, 6000, 10000, 20000, 70000, 30000, 60000, 80000, 50000, 100000};
    private final static long[] baseAtk = {1, 5, 15, 25, 35, 56, 100, 120, 305, 40, 60, 125, 200,
            450, 500, 700, 720, 1000, 1500, 2500, 300, 3400, 5000, 70000, 6000, 6500, 10000, 20000, 90000};
    private final static List[] itemNames = {
            Arrays.asList(ItemName.原石),
            Arrays.asList(ItemName.硝石),
            Arrays.asList(ItemName.精铁, ItemName.萤石),
            Arrays.asList(ItemName.硝石),
            Arrays.asList(ItemName.精铁, ItemName.玄石),
            Arrays.asList(ItemName.鼠筋, ItemName.鼠骨, ItemName.鼠皮),
            Arrays.asList(ItemName.玄石, ItemName.蚁须),
            Arrays.asList(ItemName.虎皮, ItemName.虎筋, ItemName.虎骨),
            Arrays.asList(ItemName.蛟皮, ItemName.铜矿石),
            Arrays.asList(ItemName.钢石, ItemName.铁矿石),
            Arrays.asList(ItemName.食人鸟毛, ItemName.黑石),
            Arrays.asList(ItemName.鼠皮),
            Arrays.asList(ItemName.蛇皮, ItemName.蛇筋, ItemName.蛇骨),
            Arrays.asList(ItemName.牛皮, ItemName.牛筋, ItemName.牛骨),
            Arrays.asList(ItemName.龟壳),
            Arrays.asList(ItemName.蛇皮, ItemName.蛇骨),
            Arrays.asList(ItemName.红檀木),
            Arrays.asList(ItemName.狼皮, ItemName.狼筋),
            Arrays.asList(ItemName.萤石),
            Arrays.asList(ItemName.冷杉木),
            Arrays.asList(ItemName.原石),
            Arrays.asList(ItemName.凤凰木, ItemName.凤凰毛),
            Arrays.asList(ItemName.龙皮, ItemName.冷杉木, ItemName.原石, ItemName.银矿石, ItemName.龙骨, ItemName.龙筋, ItemName.龙须),
            Arrays.asList(ItemName.牛皮, ItemName.原石, ItemName.银矿石, ItemName.萤石, ItemName.白云石, ItemName.龙须木, ItemName.青檀木, ItemName.白杏木, ItemName.银矿石, ItemName.铁矿石)
    };

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
    private long rate = 300;

    public boolean isSilent(Random random){
        return (random.nextInt(100) + random.nextFloat()) < silent;
    }

    public static int getIndex(String name) {
        for (int i = 0; i < lastNames.length; i++) {
            if (name.matches(".*" + lastNames[i])) {
                return i;
            }
        }
        return Integer.MAX_VALUE - 1;
    }

    public static Monster getBoss(Maze maze, Hero hero) {
        Random random = new Random();
        Monster monster = PalaceMonster.getDefender(maze.getLev());
        if (monster == null) {
            if (random.nextInt(100) < 25) {
                return null;
            }
            monster = buildDefaultDefender(maze, hero, random);
        }
        monster.material = random.nextLong(maze.getLev() * monster.atk + 1) / 115 + 25;
        if (monster.material > 10000) {
            monster.material = 3000 + random.nextLong(10000);
        }
        monster.items = Arrays.asList(ItemName.原石, ItemName.铁矿石, ItemName.冷杉木,
                ItemName.萤石, ItemName.蚁须, ItemName.龙须);
        monster.builder = new StringBuilder("第");
        monster.builder.append(maze.getLev()).append("层").append("<br>-------");
        monster.element = Element.values()[random.nextInt(Element.values().length)];
        monster.formatName(hero);
        if(maze.getLev() > 500 && random.nextBoolean()){
            monster.silent = 18.8f;
        }
        monster.setRate(1);
        return monster;
    }

    private static Monster buildDefaultDefender(Maze maze, Hero hero, Random random) {
        long hp = (hero.getAttackValue() / 20) * (random.nextLong(maze.getLev() + 1)) + random.nextLong(hero.getUpperHp() + 1) + maze.getLev() * 100;
        long atk = (hero.getDefenseValue() + hero.getHp()) / 5 + maze.getLev() * 32 + random.nextLong(hero.getAttackValue() / 3 + maze.getLev() + 1);
        if (hp <= 0) hp = Integer.MAX_VALUE - 10;
        if (atk <= 0) hp = Integer.MAX_VALUE - 100;
        if (atk > hero.getUpperHp() + hero.getDefenseValue()) {
            atk = atk / 2;
        }

        if (hero.getMaterial() > 10000000) {
            atk += random.nextLong(hero.getMaterial() / (MainGameActivity.context != null ? MainGameActivity.context.getAlipay().getPayTime() + 1 : 1) + 1);

        }
        if (maze.getLev() < 200) {
            atk /= 2;
        }
        if (maze.getLev() < 100) {
            atk /= 3;
        }
        if (hp > hero.getAttackValue() * 30) {
            hp = hero.getAttackValue() * 30;
        }
        if (atk < hero.getDefenseValue()) {
            atk += random.nextLong(hero.getDefenseValue() * 2);
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
        Random random = new Random();
        int first = (int) random.nextLong(maze.getLev() / 50 < firstNames.length ? maze.getLev() / 50 + 1 : firstNames.length);
        int second = (int) random.nextLong(maze.getLev() < secondNames.length ? maze.getLev() + 1 : secondNames.length);
        int last = (int) random.nextLong(maze.getLev() < lastNames.length ? maze.getLev() + 1 : lastNames.length);
        hp = baseHP[last] + firstAdditionHP[first] + secondAdditionHP[second];
        atk = baseAtk[last] + firstAdditionAtk[first] + secondAdditionAtk[second];
        firstName = firstNames[first];
        secondName = secondNames[second];
        lastName = lastNames[last];
        if(last < itemNames.length) {
            this.items = itemNames[last];
        }else{
            this.items = Collections.emptyList();
        }
        if (hero.getStrength() > 100 && hero.getAgility() > 1000 && hero.getPower() > 100 && random.nextLong(maze.getLev() / 100 + 1) > 100 && random.nextInt(100) < 11) {
            hp += random.nextLong((maze.getLev() + hero.getMaxMazeLev()) / 200) * random.nextLong((hero.getStrength() + hero.getAgility() + hero.getStrength()) / 100 + 1);
            atk += maze.getLev() * random.nextLong((hero.getStrength() + hero.getAgility() + hero.getPower()) / 800 + 1);
            atk += baseAtk[last] * random.nextLong(maze.getLev() + hero.getMaxMazeLev() / 100 + 1);
            atk += secondAdditionAtk[second] * random.nextLong(maze.getLev()/500);
            hp += firstAdditionHP[first]*random.nextLong(maze.getLev()/600);
            if (atk < hero.getDefenseValue()) atk = random.nextLong(hero.getAttackValue() + atk);
            atk += random.nextLong((hero.getUpperAtk() + hero.getUpperHp() + hero.getUpperDef()) / (6 * hero.getMaxMazeLev()) + hero.getMaxMazeLev());
        }
        hp += baseHP[last] * random.nextLong(maze.getLev() + 1);
        atk += baseAtk[last] * hero.getReincaCount();
        if (hp < hero.getAttackValue()) {
            hp += random.nextLong(hero.getAttackValue() * 2);
        }
        if (hp > hero.getAttackValue() * 19) {
            hp = hero.getAttackValue() * 20;
        }
        if (hero.getMaterial() > 90000000) {
            atk += random.nextLong(hero.getAttackValue() / (hero.getMaxMazeLev() + 1));
            hp += random.nextLong(hero.getMaterial() / (MainGameActivity.context != null ? MainGameActivity.context.getAlipay().getPayTime() + 1 : 1) + 1);
        }
        if (hp <= 0) hp = Integer.MAX_VALUE - 10;
        if (atk <= 0) hp = Integer.MAX_VALUE - 100;
        if (maze.getLev() < 150) {
            atk /= 4;
        }
        long m1 = random.nextLong(hp + 1) / 180 + 5;
        long m2 = random.nextLong(atk + 1) / 409 + 10;
        material = random.nextLong((m1 + m2) / 29 + 2) + 10 + random.nextLong(maze.getLev() * 9 + 1);
        maxHP = hp;
        builder = new StringBuilder("第");
        builder.append(maze.getLev()).append("层").append("<br>------------");
        element = Element.values()[random.nextInt(Element.values().length)];
        rate = 100 - (mazeLev/100) - last * 4;
        if(rate < 5){
            rate = 5;
        }
        rate /= 2;
        formatName(hero);
    }

    private void formatName(Hero hero) {
        if (getAtk() > (hero.getUpperHp() + hero.getDefenseValue()) / 2) {
            setFormatName("<B><font color=\"red\">" + getName() + "</font></B>" + "(" + element + ")");
        } else {
            setFormatName(getName() + "(" + element + ")");
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

    public long getRate() {
        return rate;
    }

    public void setRate(long rate) {
        this.rate = rate;
    }

    public static Monster CHEATBOSS() {
        Monster monster = new Monster("真正", "强壮" ,"作者", Long.MAX_VALUE -10, Long.MAX_VALUE - 10);
        monster.silent = 100;
        monster.mazeLev = 0;
        monster.element = Element.水;
        monster.formatName(MazeContents.hero);
        return monster;
    }

    public static Monster copy(Hero hero) {
        Monster monster = new Monster("镜像", "", hero.getName(), hero.getUpperHp() + hero.getUpperDef(), hero.getUpperAtk());
        monster.silent = 80;
        monster.element = hero.getElement();
        monster.mazeLev = hero.getMaxMazeLev();
        monster.formatName(MazeContents.hero);
        if(monster.getHp() < 0){
            monster.hp = Long.MAX_VALUE -1000;
            monster.maxHP = monster.hp;
        }
        return monster;
    }
}
