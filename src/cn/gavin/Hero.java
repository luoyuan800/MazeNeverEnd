package cn.gavin;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import cn.gavin.monster.Monster;
import cn.gavin.skill.Skill;
import cn.gavin.skill.type.AttackSkill;
import cn.gavin.skill.type.DefendSkill;
import cn.gavin.skill.type.RestoreSkill;

public class Hero {
    private static final String TAG = "Hero";

    public static int MAX_GOODS_COUNT = 50;

    // 血上限成长(每点生命点数增加）
    public static final int MAX_HP_RISE = 5;
    // 攻击成长（每点力量点数增加）
    public static final int ATR_RISE = 2;
    // 防御成长 （每点敏捷点数增加）
    public static final int DEF_RISE = 1;
    private int click;
    private String name;
    private int hp;//当前
    private int upperHp;//上限值
    private int attackValue;
    private int defenseValue;
    public int level;
    private Queue<Skill> existSkill; // 已有的技能
    private Sword sword;
    private Armor armor;
    private int swordLev;
    private int armorLev;
    private int material;
    private int point;
    private int strength;//力量，影响攻击数值上限
    private int power;//体力，影响HP上限，生命恢复技能效果
    private int agility;//敏捷，影响技能施放概率，防御数值上限
    private int maxMazeLev = 0;
    private Random random;
    private int clickAward = 1;
    private int deathCount;
    private int skillPoint = 10;
    private Skill firstSkill;
    private Skill secondSkill;
    private Skill thirdSkill;

    public int getDeathCount() {
        return deathCount;
    }

    public void setDeathCount(int deathCount) {
        this.deathCount = deathCount;
    }

    public void addClickAward(int num) {
        clickAward += num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHp() {
        return hp;
    }

    public void addHp(int hp) {
        if (hp < 0) {
            this.hp += hp;
        } else if (this.hp < (Integer.MAX_VALUE - hp - 100)) {
            this.hp += hp;
        }
        if (this.hp <= 0) {
            this.hp = 0;
            deathCount++;
            if (deathCount == 10000) {
                Achievement.maltreat.enable(this);
            }
        }
        if (this.hp > upperHp) this.hp = upperHp;
    }

    public int getBaseAttackValue() {
        return attackValue;
    }

    public int getUpperAtk() {
        return attackValue + sword.getBase() + swordLev;
    }

    public int getUpperDef() {
        int def = defenseValue + armorLev + armor.getBase();
        if (def >= 10000) {
            Achievement.fearDeath.enable(this);
        }
        return def;
    }

    public int getAttackValue() {
        return attackValue + random.nextInt(sword.getBase()) + random.nextInt(swordLev + 1);
    }

    public void addAttackValue(int attackValue) {
        this.attackValue += attackValue;
    }

    public int getBaseDefense() {
        return defenseValue;
    }

    public int getDefenseValue() {
        int defend = defenseValue + random.nextInt(armor.getBase()) + random.nextInt(armorLev * 2 + 1);
        if (random.nextInt(100) + random.nextInt(agility + 1) / 1000 > 96 + random.nextInt(strength + 1) / 1000) {
            defend *= 3;
        }

        return defend;
    }

    public void addDefenseValue(int defenseValue) {
        this.defenseValue += defenseValue;
    }

    public Queue<Skill> getExistSkill() {
        return existSkill;
    }

    public void addSkill(Skill skill) {
        if (firstSkill == null) firstSkill = skill;
        else if (secondSkill == null) secondSkill = skill;
        else if (thirdSkill == null) thirdSkill = skill;
        else {
            firstSkill = secondSkill;
            secondSkill = thirdSkill;
            thirdSkill = skill;
        }
    }

    private Hero(String name, int hp, int attackValue, int defenseValue, int level) {
        super();
        this.name = name;
        this.hp = hp;
        this.attackValue = attackValue;
        this.defenseValue = defenseValue;
        this.level = level;
        this.upperHp = hp;
        existSkill = new ConcurrentLinkedQueue<Skill>();
        sword = Sword.木剑;
        armor = Armor.破布;
    }

    public Hero(String name) {
        this(name, 20, 10, 10, 1);
        random = new Random();
        strength = random.nextInt(5);
        agility = random.nextInt(5);
        power = random.nextInt(5);
    }

    @Override
    public String toString() {
        return "Hero [name=" + name + ", hp=" + hp + ", attackValue=" + attackValue
                + ", defenseValue=" + defenseValue + ", level=" + level + "]";
    }

    public boolean upgradeSword() {
        if (swordLev + sword.getBase() + attackValue >= Integer.MAX_VALUE - 100) {
            return false;
        } else {
            if (material >= 100 + swordLev) {
                material -= (100 + armorLev);
                swordLev++;
                if (sword != sword.levelUp(swordLev)) {
                    sword = sword.levelUp(swordLev);
                    swordLev = 0;
                }
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean upgradeArmor() {
        if (armorLev + armor.getBase() + defenseValue >= Integer.MAX_VALUE - 100) {
            return false;
        } else {
            if (material >= 80 + armorLev) {
                material -= (80 + armorLev);
                armorLev++;
                if (armor != armor.levelUp(armorLev)) {
                    armor = armor.levelUp(armorLev);
                    armorLev = 0;
                    if (armor == Armor.金甲) {
                        Achievement.goldColor.enable(this);
                    }
                }
                return true;
            } else {
                return false;
            }
        }
    }

    public int getMaterial() {
        return material;
    }

    public void addMaterial(int material) {
        if (this.material < 0 || this.material < (Integer.MAX_VALUE - material - 1000))
            this.material += material;
        if (this.material < 0) this.material = 0;
        if (this.material >= 5000000) Achievement.rich.enable(this);
    }

    public int getPoint() {
        return point;
    }

    public void addPoint(int point) {
        if (point < 0 || this.point < (Integer.MAX_VALUE - point - 5000))
            this.point += point;
        if (this.point < 0) this.point = 0;
        if (this.point >= 5000) Achievement.lazy.enable(this);
    }

    public int getStrength() {
        return strength;
    }

    public void addStrength() {
        if (point != 0 && strength < (Integer.MAX_VALUE - 500)) {
            point--;
            strength++;
            if (attackValue < (Integer.MAX_VALUE - ATR_RISE - 500))
                attackValue += ATR_RISE;
            else {
                attackValue = Integer.MAX_VALUE;
                Achievement.extreme.enable(this);
            }
        }
    }

    public void addStrength(int str) {
        if (str < 0 || strength < (Integer.MAX_VALUE - strength - 100)) {
            strength += str;
            if (str < 0 || attackValue < (Integer.MAX_VALUE - ATR_RISE * str))
                attackValue += ATR_RISE * str;
            else {
                attackValue = Integer.MAX_VALUE;
                Achievement.extreme.enable(this);
            }
        }
        if (strength < 0) strength = 0;
        if (attackValue < 0) attackValue = 0;
    }

    public int getPower() {
        return power;
    }

    public void addLife() {
        if (point != 0 && power < (Integer.MAX_VALUE - 500)) {
            point--;
            power++;
            if (upperHp < (Integer.MAX_VALUE - MAX_HP_RISE)) {
                hp += MAX_HP_RISE;
                upperHp += MAX_HP_RISE;
            }
        }
    }

    public void addLife(int life) {
        if (life < 0 || power < (Integer.MAX_VALUE - life - 100)) {
            power += life;
            if (life < 0 || upperHp < (Integer.MAX_VALUE - MAX_HP_RISE * life)) {
                hp += MAX_HP_RISE * life;
                upperHp += MAX_HP_RISE * life;
            }
        }
        if (power < 0) power = 0;
        if (hp < 0) hp = 0;
        if (upperHp < 0) upperHp = 0;
    }

    public int getAgility() {
        return agility;
    }

    public void addAgility() {
        if (point != 0 && (agility < Integer.MAX_VALUE - 500)) {
            point--;
            agility++;
            if (defenseValue < (Integer.MAX_VALUE - DEF_RISE))
                defenseValue += DEF_RISE;
            else {
                defenseValue = Integer.MAX_VALUE;
                Achievement.master.enable(this);
            }
        }
        if (agility >= 10000) {
            Achievement.skilldness.enable(this);
        }
    }

    public void addAgility(int agi) {
        if (agi < 0 || agility < (Integer.MAX_VALUE - agi - 100)) {
            agility += agi;
            if (agi < 0 || defenseValue < (Integer.MAX_VALUE - DEF_RISE * agi))
                defenseValue += DEF_RISE * agi;
            else {
                defenseValue = Integer.MAX_VALUE;
                Achievement.master.enable(this);
            }
        }
        if (agility < 0) agility = 0;
        if (defenseValue < 0) defenseValue = 0;
        if (agility >= 10000) Achievement.skilldness.enable(this);
    }

    public void restore() {
        this.hp = upperHp;
    }

    public int getMaxMazeLev() {
        return maxMazeLev;
    }

    public void addMaxMazeLev() {
        if (this.maxMazeLev < Integer.MAX_VALUE - 10) {
            this.maxMazeLev++;
        }
    }

    public int getSwordLev() {
        return swordLev;
    }

    public int getArmorLev() {
        return armorLev;
    }

    public int getClick() {
        return click;
    }

    public void click(boolean award) {
        if (click < Integer.MAX_VALUE - 10) {
            if (this.click % 1000 == 0) {
                point += random.nextInt(15);
            }
            if (award) {
                this.material += clickAward;
            }
            this.click++;
            switch (click) {
                case 100:
                    Achievement.click100.enable(this);
                    break;
                case 10000:
                    Achievement.click10000.enable(this);
                    break;
                case 50000:
                    Achievement.click50000.enable(this);
                    break;
                case 100000:
                    Achievement.click100000.enable(this);
                    break;
            }
        }
    }

    public Skill useAttackSkill(Monster monster) {
        for (Skill skill : existSkill) {
            if (skill instanceof AttackSkill && skill.perform()) {
                return skill;
            }
        }
        return null;
    }

    public Skill useDefendSkill(Monster monster) {
        for (Skill skill : existSkill) {
            if (skill instanceof DefendSkill) {
                if (skill.perform()) {
                    return skill;
                }
            }
        }
        return null;
    }

    public Skill useRestoreSkill() {
        for (Skill skill : existSkill) {
            if (skill instanceof RestoreSkill) {
                if (skill.perform()) {
                    return skill;
                }
            }
        }
        return null;
    }

    public int getUpperHp() {
        return upperHp;
    }

    public String getSword() {
        return sword.name();
    }

    public String getArmor() {
        return armor.name();
    }

    public void setClick(int click) {
        this.click = click;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setUpperHp(int upperHp) {
        this.upperHp = upperHp;
    }

    public void setAttackValue(int attackValue) {
        this.attackValue = attackValue;
    }

    public void setDefenseValue(int defenseValue) {
        this.defenseValue = defenseValue;
    }

    public void setSword(Sword sword) {
        this.sword = sword;
    }

    public void setArmor(Armor armor) {
        this.armor = armor;
    }

    public void setSwordLev(int swordLev) {
        this.swordLev = swordLev;
    }

    public void setArmorLev(int armorLev) {
        this.armorLev = armorLev;
    }

    public void setMaterial(int material) {
        this.material = material;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    public void setMaxMazeLev(int maxMazeLev) {
        boolean notE = maxMazeLev != this.maxMazeLev;
        this.maxMazeLev = maxMazeLev;
        if (notE && this.maxMazeLev % 101 == 0) {
            this.skillPoint += 2;
        }
    }

    public void setClickAward(int clickAward) {
        this.clickAward = clickAward;
    }

    public int getClickAward() {
        return clickAward;
    }

    public int getSkillPoint() {
        return skillPoint;
    }

    public void setSkillPoint(int skillPoint) {
        this.skillPoint = skillPoint;
    }

    public Random getRandom() {
        return random;
    }

    public void removeSkill(Skill skill) {
        if(firstSkill == skill){
            firstSkill = secondSkill;
            secondSkill= thirdSkill;
            thirdSkill = null;
        }else if(secondSkill == skill){
            secondSkill = thirdSkill;
            thirdSkill = null;
        }else if(thirdSkill == skill){
            thirdSkill = null;
        }
    }

    public Skill getFirstSkill() {
        return firstSkill;
    }

    public void setFirstSkill(Skill firstSkill) {
        this.firstSkill = firstSkill;
    }

    public Skill getSecondSkill() {
        return secondSkill;
    }

    public void setSecondSkill(Skill secondSkill) {
        this.secondSkill = secondSkill;
    }

    public Skill getThirdSkill() {
        return thirdSkill;
    }

    public void setThirdSkill(Skill thirdSkill) {
        this.thirdSkill = thirdSkill;
    }
}
