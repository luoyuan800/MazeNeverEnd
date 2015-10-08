package cn.gavin.monster;


import java.util.Arrays;
import java.util.List;

import cn.gavin.Hero;
import cn.gavin.Maze;
import cn.gavin.forge.list.ItemName;
import cn.gavin.utils.Random;
import cn.gavin.utils.StringUtils;

/**
 * gluo on 8/26/2015.
 */
public class Monster {
    private final static String[] firstNames = {"普通", "怪异", "飞翔", "稀有", "发狂", "神奇", "神经", "传奇"};
    private final static long[] firstAdditionHP = {15, 25, 400, 1000, 500, 2000, 10000, 50000};
    private final static long[] firstAdditionAtk = {3, 25, 350, 800, 4000, 3800, 5000, 30000};
    private final static String[] secondNames = {"小", "中", "大", "大大", "红色", "绿色", "人面"};
    private final static long[] secondAdditionHP = {15, 25, 100, 500, 1003, 2000, 6000};
    private final static long[] secondAdditionAtk = {5, 25, 100, 200, 400, 100, 1000};
    public final static String[] lastNames = {"蟑螂", "大蚯蚓", "异壳虫", "巨型飞蛾", "猪", "老鼠", "嗜血蚁",
            "老虎", "蛟", "变异蝎", "食人鸟", "丑蝙蝠", "蛇", "野牛", "龟", "三头蛇", "刺猬", "狼", "精灵",
            "僵尸", "骷髅", "凤凰", "龙", "作者"};
    private final static long[] baseHP = {8, 20, 55, 75, 95, 115, 400, 1000, 800, 520, 280, 350, 380, 450, 530, 1000, 1500, 2300, 4000, 4500, 6000, 10000, 20000, 50000};
    private final static long[] baseAtk = {1, 10, 35, 55, 75, 105, 200, 250, 1500, 2000, 310, 350, 400, 900, 1000, 1430, 1500, 2000, 3000, 5000, 6000, 7000, 10000, 50000};
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
    private long atk;
    private long hp;
    private long material;
    private long mazeLev;
    private boolean defeat;
    private long maxHP;
    private String formatName;
    private String name;
    private float hitRate = 100.0f;
    private List<ItemName> items;

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
        Monster monster = Defender.buildDefender(maze.getLev());
        if (monster == null) {
            if (random.nextInt(100) < 25) {
                return null;
            }
            monster = buildDefaultDefender(maze, hero, random);
        }
        monster.material = random.nextLong(maze.getLev() * monster.atk + 1) / 100 + 25;
        if (monster.material > 40000) {
            monster.material = random.nextLong(50000);
        }
        monster.items = Arrays.asList(ItemName.原石, ItemName.铁矿石, ItemName.冷杉木,
                ItemName.萤石, ItemName.白云石);
        monster.formatName(hero);
        monster.builder = new StringBuilder("第");
        monster.builder.append(maze.getLev()).append("层").append("<br>-------");
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
        if (hp > hero.getAttackValue() * 30) {
            hp = hero.getAttackValue() * 30;
        }
        if (atk < hero.getDefenseValue()) {
            atk += random.nextLong(hero.getDefenseValue() * 3);
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
        int first = (int) random.nextLong(maze.getLev() / 20 < firstNames.length ? maze.getLev() / 20 + 1 : firstNames.length);
        int second = (int) random.nextLong(maze.getLev() < secondNames.length ? maze.getLev() + 1 : secondNames.length);
        int last = (int) random.nextLong(maze.getLev() < lastNames.length ? maze.getLev() + 1 : lastNames.length);
        hp = baseHP[last] + firstAdditionHP[first] + secondAdditionHP[second];
        atk = baseAtk[last] + firstAdditionAtk[first] + secondAdditionAtk[second];
        firstName = firstNames[first];
        secondName = secondNames[second];
        lastName = lastNames[last];
        this.items = itemNames[last];
        if (hero.getStrength() > 100 && hero.getAgility()>1000 && hero.getPower() > 100 && random.nextBoolean()) {
            hp += maze.getLev() * random.nextLong((hero.getStrength()+ hero.getAgility() + hero.getPower())/ 100 + 1);
            atk += maze.getLev() * random.nextLong((hero.getStrength()+ hero.getAgility() + hero.getPower())/ 800 + 1);
        }
        atk += baseAtk[last] * random.nextLong(maze.getLev() + 1);
        hp += baseHP[last] * random.nextLong(maze.getLev() + 1);
        if (hp < hero.getAttackValue()) {
            hp += random.nextLong(hero.getAttackValue() * 2);
        }

        if (hp > hero.getAttackValue() * 19) {
            hp = hero.getAttackValue() * 20;
        }
        if (atk < hero.getDefenseValue()) {
            atk += random.nextLong(hero.getDefenseValue() * 2);
        }
        if (hp <= 0) hp = Integer.MAX_VALUE - 10;
        if (atk <= 0) hp = Integer.MAX_VALUE - 100;
        long m1 = random.nextLong(hp + 1) / 180 + 5;
        long m2 = random.nextLong(atk + 1) / 409 + 10;
        material = random.nextLong((m1 + m2) / 20 + 1) + 10 + random.nextLong(maze.getLev() * 10 + 1);
        maxHP = hp;
        formatName(hero);
        builder = new StringBuilder("第");
        builder.append(maze.getLev()).append("层").append("<br>------------");
    }

    private void formatName(Hero hero) {
        if (getAtk() > (hero.getUpperHp() + hero.getDefenseValue()) / 2) {
            setFormatName("<B><font color=\"red\">" + getName() + "</font></B>");
        } else {
            setFormatName(getName());
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
}
