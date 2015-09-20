package cn.gavin.monster;


import android.util.LongSparseArray;
import cn.gavin.Hero;
import cn.gavin.Maze;
import cn.gavin.utils.Random;
import cn.gavin.utils.StringUtils;

/**
 * gluo on 8/26/2015.
 */
public class Monster {
    private final static String[] firstNames = {"普通", "怪异", "飞翔", "稀有", "发狂", "神奇", "神经", "传奇"};
    private final static long[] firstAdditionHP = {15, 25, 400, 1000, 500, 2000, 10000, 50000};
    private final static long[] firstAdditionAtk = {3, 25, 350, 800, 4000, 3800, 5000, 20000};
    private final static String[] secondNames = {"小", "中", "大", "大大", "红色", "绿色", "人面"};
    private final static long[] secondAdditionHP = {15, 25, 100, 500, 1003, 2000, 3000};
    private final static long[] secondAdditionAtk = {5, 25, 100, 200, 400, 100, 2000};
    private final static String[] lastNames = {"蟑螂", "大蚯蚓", "异壳虫", "巨型飞蛾", "猪", "老鼠", "嗜血蚁", "变异蝎", "食人鸟", "丑蝙蝠", "蛇", "野牛", "龟", "三头蛇", "刺猬", "狼", "精灵", "僵尸", "骷髅", "凤凰", "龙", "作者"};
    private final static long[] baseHP = {13, 30, 55, 75, 95, 115, 400, 520, 280, 350, 380, 450, 530, 1000, 1500, 2300, 4000, 4500, 6000, 10000, 20000, 50000};
    private final static long[] baseAtk = {8, 15, 35, 55, 75, 105, 200, 250, 310, 350, 400, 900, 1000, 1430, 1500, 2000, 3000, 5000, 6000, 7000, 10000, 50000};
    private final static LongSparseArray<String> defender = new LongSparseArray<String>();

    static {
        defender.put(324l, "name:0x6c81739f_hp:138645_atk:37889");
        defender.put(101l, "name:0x590f65878fdb_hp:24990_atk:12911");
        defender.put(113l, "name:0x50bb74dc_hp:41331_atk:4542_mazeLev:113");
    }

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

    public static int getIndex(String name) {
        for (int i = 0; i < lastNames.length; i++) {
            if (name.matches(".*" + lastNames[i])) {
                return i;
            }
        }
        return -1;
    }

    public static Monster getBoss(Maze maze, Hero hero) {
        Random random = new Random();
        String desc = defender.get(maze.getLev());
        Monster monster;
        if (StringUtils.isNotEmpty(desc)) {
            String[] strings = desc.split("_");
            String name = strings[0].split(":")[1];
            long hp = Long.parseLong(strings[1].split(":")[1]);
            long atk = Long.parseLong(strings[2].split(":")[1]);
            monster = new Monster("", "【守护者】", name, hp, atk);
        } else {
            monster = buildDefaultDefender(maze, hero, random);
        }
        monster.material = random.nextLong(maze.getLev() + monster.atk + 1) / 20 + 5;
        return monster;
    }

    private static Monster buildDefaultDefender(Maze maze, Hero hero, Random random) {
        long hp = hero.getDefenseValue() * (random.nextLong(maze.getLev() + 1) + 5) + random.nextLong(hero.getUpperHp() + 1) + maze.getLev();
        long atk = hero.getDefenseValue() + maze.getLev() + random.nextLong(hero.getAttackValue() / 3 + maze.getLev() + 1);
        if (hp <= 0) hp = Integer.MAX_VALUE - 10;
        if (atk <= 0) hp = Integer.MAX_VALUE - 100;
        Monster monster = new Monster("第" + maze.getLev() + "层", "守护", "者",
                hp,
                atk);
        monster.formatName(hero);
        return monster;
    }

    private Monster(String firstName, String secondName, String lastName, long hp, long atk) {
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
        if (hero.getAttackValue() != 0)
            hp += maze.getLev() * random.nextLong(hero.getAttackValue() / 1540 + 1);
        if (hero.getPower() != 0)
            atk += random.nextLong(hero.getPower() / 10 + 1) * random.nextLong(maze.getLev() + 1);
        atk += random.nextLong(hero.getDefenseValue() / 1100 + 1) * random.nextLong(maze.getLev() + 1);
        if (hp <= 0) hp = Integer.MAX_VALUE - 10;
        if (atk <= 0) hp = Integer.MAX_VALUE - 100;
        long m1 = random.nextLong(hp + 1) / 180 + 5;
        long m2 = random.nextLong(atk + 1) / 509;
        material = random.nextLong((m1 + m2) / 20 + 1) + 3;
        maxHP = hp;
        formatName(hero);
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
        return firstName + "的" + secondName + lastName;
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
}
