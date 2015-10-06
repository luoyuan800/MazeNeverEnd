package cn.gavin.monster;


import android.util.LongSparseArray;
import cn.gavin.Hero;
import cn.gavin.Maze;
import cn.gavin.forge.list.ItemName;
import cn.gavin.utils.Random;
import cn.gavin.utils.StringUtils;

import java.util.Arrays;
import java.util.List;

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
    private final static String[] lastNames = {"蟑螂", "大蚯蚓", "异壳虫", "巨型飞蛾", "猪", "老鼠", "嗜血蚁",
            "老虎", "蛟", "变异蝎", "食人鸟", "丑蝙蝠", "蛇", "野牛", "龟", "三头蛇", "刺猬", "狼", "精灵",
            "僵尸", "骷髅", "凤凰", "龙", "作者"};
    private final static long[] baseHP = {13, 30, 55, 75, 95, 115, 400, 1000, 800, 520, 280, 350, 380, 450, 530, 1000, 1500, 2300, 4000, 4500, 6000, 10000, 20000, 50000};
    private final static long[] baseAtk = {8, 15, 35, 55, 75, 105, 200, 250, 1500, 2000, 310, 350, 400, 900, 1000, 1430, 1500, 2000, 3000, 5000, 6000, 7000, 10000, 50000};
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
            Arrays.asList(ItemName.牛皮,ItemName.原石, ItemName.银矿石, ItemName.萤石, ItemName.白云石, ItemName.龙须木, ItemName.青檀木, ItemName.白杏木, ItemName.银矿石, ItemName.铁矿石)
    };
    private final static LongSparseArray<String> defender = new LongSparseArray<String>();

    static {
        defender.put(324l, "name:沁玟_hp:138645_atk:37889");
        defender.put(101l, "name:夏文进_hp:24990_atk:12911");
        defender.put(113l, "name:傻瓜_hp:41331_atk:4542");
        defender.put(200l, "name:自己的爸爸_hp:40046_atk:9329");
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
        String desc = defender.get(maze.getLev());
        Monster monster;
        if (StringUtils.isNotEmpty(desc)) {
            String[] strings = desc.split("_");
            String name = strings[0].split(":")[1];
            long hp = Long.parseLong(strings[1].split(":")[1]);
            long atk = Long.parseLong(strings[2].split(":")[1]);
            monster = new Monster("", "【守护者】", name, hp, atk);
            monster.setFormatName("<b>" + monster.getName() + "</b>");
        } else {
            if (random.nextInt(100) < 25) {
                return null;
            }
            monster = buildDefaultDefender(maze, hero, random);
        }
        monster.material = random.nextLong(maze.getLev() + monster.atk + 1) / 100 + 5;
        monster.items = Arrays.asList(ItemName.原石, ItemName.铁矿石,ItemName.冷杉木,
                ItemName.萤石, ItemName.白云石);
        monster.formatName(hero);
        return monster;
    }

    private static Monster buildDefaultDefender(Maze maze, Hero hero, Random random) {
        long hp = (hero.getAttackValue() / 20) * (random.nextLong(maze.getLev() + 1)) + random.nextLong(hero.getUpperHp() + 1) + maze.getLev() * 100;
        long atk = (hero.getDefenseValue() + hero.getHp()) / 5 + maze.getLev() * 32 + random.nextLong(hero.getAttackValue() / 3 + maze.getLev() + 1);
        if (hp <= 0) hp = Integer.MAX_VALUE - 10;
        if (atk <= 0) hp = Integer.MAX_VALUE - 100;
        if (atk > hero.getUpperHp() + hero.getDefenseValue()) {
            atk = atk / 3;
        }
        if (hp > hero.getAttackValue() * 25) {
            hp = hero.getAttackValue() * 26;
        }
        return new Monster("第" + maze.getLev() + "层", "守护", "者",
                hp,
                atk);
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
        this.items = itemNames[last];
        if (hero.getAttackValue() != 0)
            hp += maze.getLev() * random.nextLong(hero.getStrength() / 600 + 1);
        if (hero.getAgility() != 0)
            atk += random.nextLong(hero.getAgility() / 1000 + 1) * random.nextLong(maze.getLev() + 1);
        atk += random.nextLong(hero.getDefenseValue() / 1500 + 1) * random.nextLong(maze.getLev() + 1);
        if (hp < hero.getAttackValue()) {
            hp += random.nextLong(hero.getAttackValue() * 2);
        }
        if (atk > hero.getUpperHp() + hero.getDefenseValue()) {
            atk = atk / 3;
        }
        if (hp > hero.getAttackValue() * 19) {
            hp = hero.getAttackValue() * 20;
        }
        if (hp <= 0) hp = Integer.MAX_VALUE - 10;
        if (atk <= 0) hp = Integer.MAX_VALUE - 100;
        long m1 = random.nextLong(hp + 1) / 180 + 5;
        long m2 = random.nextLong(atk + 1) / 409;
        material = random.nextLong((m1 + m2) / 20 + 1) + 3 + random.nextLong(maze.getLev() * 10 + 1);
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
}
