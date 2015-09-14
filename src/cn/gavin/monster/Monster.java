package cn.gavin.monster;

import java.util.Random;

import cn.gavin.Hero;
import cn.gavin.Maze;

/**
 * gluo on 8/26/2015.
 */
public class Monster {
    private final static String[] firstNames = {"普通", "怪异", "飞翔", "稀有", "发狂", "神奇", "神经"};
    private final static int[] firstAdditionHP = {15, 25, 400, 1000, 500, 2000, 10000};
    private final static int[] firstAdditionAtk = {3, 25, 350, 800, 4000, 3800, 5000};
    private final static String[] secondNames = {"小", "中", "大", "大大", "红色", "绿色", "人面"};
    private final static int[] secondAdditionHP = {15, 25, 100, 500, 1003, 2000, 3000};
    private final static int[] secondAdditionAtk = {5, 25, 100, 200, 400, 100, 2000};
    private final static String[] lastNames = {"蟑螂", "猪", "老鼠", "蛇", "野牛", "龟", "刺猬", "狼", "精灵", "僵尸", "骷髅", "凤凰", "龙", "作者"};
    private final static int[] baseHP = {3, 25, 75, 95, 115, 520, 280, 350, 380, 450, 530, 4000, 1000, 50000};
    private final static int[] baseAtk = {2, 15, 55, 75, 105, 200, 250, 310, 350, 400, 1430, 3000, 5000, 50000};
    private String firstName;
    private String secondName;
    private String lastName;
    private int atk;
    private int hp;
    private int material;
    private int mazeLev;
    private boolean defeat;
    private int maxHP;
    private String formatName;

    public static Monster getBoss(Maze maze, Hero hero) {
        Random random = new Random();
        int hp = hero.getDefenseValue() * (random.nextInt(maze.getLev() + 1) + 5) + random.nextInt(hero.getUpperHp() + 1) + maze.getLev();
        int atk = hero.getDefenseValue() + maze.getLev() + random.nextInt(hero.getAttackValue() / 3 + maze.getLev() + 1);
        if (hp <= 0) hp = Integer.MAX_VALUE - 10;
        if (atk <= 0) hp = Integer.MAX_VALUE - 100;
        Monster monster = new Monster("第" + maze.getLev() + "层", "守护", "者",
                hp,
                atk);
        monster.material = random.nextInt(maze.getLev() + monster.atk + 1) / 3 + 5;
        monster.formatName(hero);
        return monster;
    }

    private Monster(String firstName, String secondName, String lastName, int hp, int atk) {
        this.atk = atk;
        this.hp = hp;
        maxHP = hp;
        this.firstName = firstName;
        this.secondName = secondName;
        this.lastName = lastName;
    }

    public Monster(Hero hero, Maze maze) {
        Random random = new Random();
        int first = random.nextInt(maze.getLev() < firstNames.length ? maze.getLev() + 1 : firstNames.length);
        int second = random.nextInt(maze.getLev() < secondNames.length ? maze.getLev() + 1 : secondNames.length);
        int last = random.nextInt(maze.getLev() < lastNames.length ? maze.getLev() + 1 : lastNames.length);
        hp = baseHP[last] + firstAdditionHP[first] + secondAdditionHP[second];
        atk = baseAtk[last] + firstAdditionAtk[first] + secondAdditionAtk[second];
        firstName = firstNames[first];
        secondName = secondNames[second];
        lastName = lastNames[last];
        if (hero.getAttackValue() != 0)
            hp += maze.getLev() * random.nextInt(hero.getAttackValue() / 1540 + 1);
        if (hero.getPower() != 0)
            atk += random.nextInt(hero.getPower() / 10 + 1) * random.nextInt(maze.getLev() + 1);
        atk += random.nextInt(hero.getDefenseValue() / 1100 + 1) * random.nextInt(maze.getLev() + 1);
        if (hp <= 0) hp = Integer.MAX_VALUE - 10;
        if (atk <= 0) hp = Integer.MAX_VALUE - 100;
        material = random.nextInt(hp + 1) / 20 + 5;
        maxHP = hp;
        formatName(hero);
    }

    private void formatName(Hero hero){
        if(getAtk() > (hero.getUpperHp() + hero.getDefenseValue())/2){
            setFormatName("<B><font color=\"red\">" + getName() + "</font></B>");
        }else{
            setFormatName(getName());
        }
    }
    public int getAtk() {
        return atk;
    }

    public int getHp() {
        return hp;
    }

    public void addHp(int hp) {
        this.hp += hp;
    }

    public int getMaterial() {
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

    public int getMazeLev() {
        return mazeLev;
    }

    public void setMazeLev(int mazeLev) {
        this.mazeLev = mazeLev;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public String getFormatName() {
        return formatName;
    }

    public void setFormatName(String formatName) {
        this.formatName = formatName;
    }
}
