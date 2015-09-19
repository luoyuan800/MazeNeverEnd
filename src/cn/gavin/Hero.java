package cn.gavin;

import cn.gavin.monster.Monster;
import cn.gavin.skill.Skill;
import cn.gavin.skill.type.AttackSkill;
import cn.gavin.skill.type.DefendSkill;
import cn.gavin.skill.type.RestoreSkill;
import cn.gavin.utils.Random;

import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Hero {
    private static final String TAG = "Hero";

    public static long MAX_GOODS_COUNT = 50;

    // 血上限成长(每点生命点数增加）
    public static final long MAX_HP_RISE = 5;
    // 攻击成长（每点力量点数增加）
    public static final long ATR_RISE = 2;
    // 防御成长 （每点敏捷点数增加）
    public static final long DEF_RISE = 1;
    private long click;
    private String name;
    private long hp;//当前
    private long upperHp;//上限值
    private long attackValue;
    private long defenseValue;
    public long level;
    private Queue<Skill> existSkill; // 已有的技能
    private Sword sword;
    private Armor armor;
    private long swordLev;
    private long armorLev;
    private long material;
    private long point;
    private long strength;//力量，影响攻击数值上限
    private long power;//体力，影响HP上限，生命恢复技能效果
    private long agility;//敏捷，影响技能施放概率，防御数值上限
    private long maxMazeLev = 0;
    private Random random;
    private long clickAward = 1;
    private long deathCount;
    private long skillPoint;
    private Skill firstSkill;
    private Skill secondSkill;
    private Skill thirdSkill;

    public long getDeathCount() {
        return deathCount;
    }

    public void setDeathCount(long deathCount) {
        this.deathCount = deathCount;
    }

    public void addClickAward(long num) {
        clickAward += num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getHp() {
        return hp;
    }

    public void addHp(long hp) {
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

    public long getBaseAttackValue() {
        return attackValue;
    }

    public long getUpperAtk() {
        return attackValue + sword.getBase() + swordLev;
    }

    public long getUpperDef() {
        long def = defenseValue + armorLev + armor.getBase();
        if (def >= 10000) {
            Achievement.fearDeath.enable(this);
        }
        return def;
    }

    public long getAttackValue() {
        return attackValue + random.nextLong(sword.getBase()) + random.nextLong(swordLev + 1);
    }

    public void addAttackValue(long attackValue) {
        this.attackValue += attackValue;
    }

    public long getBaseDefense() {
        return defenseValue;
    }

    public long getDefenseValue() {
        long defend = defenseValue + random.nextLong(armor.getBase()) + random.nextLong(armorLev * 2 + 1);
        if (random.nextLong(100) + random.nextLong(agility + 1) / 1000 > 96 + random.nextLong(strength + 1) / 1000) {
            defend *= 3;
        }

        return defend;
    }

    public void addDefenseValue(long defenseValue) {
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

    private Hero(String name, long hp, long attackValue, long defenseValue, long level) {
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
    }

    @Override
    public String toString() {
        return "Hero [name=" + name + ", hp=" + hp + ", attackValue=" + attackValue
                + ", defenseValue=" + defenseValue + ", level=" + level + "]";
    }

    public boolean upgradeSword(long count) {
        for (int i = 0; i < count; i++) {
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
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean upgradeArmor(long count) {
        for (int i = 0; i < count; i++) {
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
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    public long getMaterial() {
        return material;
    }

    public void addMaterial(long material) {
        if (this.material < 0 || this.material < (Integer.MAX_VALUE - material - 1000))
            this.material += material;
        if (this.material < 0) this.material = 0;
        if (this.material >= 5000000) Achievement.rich.enable(this);
    }

    public long getPoint() {
        return point;
    }

    public void addPoint(long point) {
        if (point < 0 || this.point < (Integer.MAX_VALUE - point - 5000))
            this.point += point;
        if (this.point < 0) this.point = 0;
        if (this.point >= 5000) Achievement.lazy.enable(this);
    }

    public long getStrength() {
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

    public void addStrength(long str) {
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

    public long getPower() {
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

    public void addLife(long life) {
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

    public long getAgility() {
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

    public void addAgility(long agi) {
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

    public long getMaxMazeLev() {
        return maxMazeLev;
    }

    public void addMaxMazeLev() {
        if (this.maxMazeLev < Integer.MAX_VALUE - 10) {
            this.maxMazeLev++;
        }
    }

    public long getSwordLev() {
        return swordLev;
    }

    public long getArmorLev() {
        return armorLev;
    }

    public long getClick() {
        return click;
    }

    public void click(boolean award) {
        if (click < Integer.MAX_VALUE - 10) {
            if (this.click % 1000 == 0) {
                point += random.nextLong(15);
            }
            if (award) {
                this.material += clickAward;
            }
            this.click++;
            switch ((int) click) {
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
        List<Skill> existSkill = Arrays.asList(firstSkill, secondSkill, thirdSkill);
        for (Skill skill : existSkill) {
            if (skill != null && skill instanceof AttackSkill && skill.perform()) {
                return skill;
            }
        }
        return null;
    }

    public Skill useDefendSkill(Monster monster) {
        List<Skill> existSkill = Arrays.asList(firstSkill, secondSkill, thirdSkill);
        for (Skill skill : existSkill) {
            if (skill != null && skill instanceof DefendSkill) {
                if (skill.perform()) {
                    return skill;
                }
            }
        }
        return null;
    }

    public Skill useRestoreSkill() {
        List<Skill> existSkill = Arrays.asList(firstSkill, secondSkill, thirdSkill);
        for (Skill skill : existSkill) {
            if (skill != null && skill instanceof RestoreSkill) {
                if (skill.perform()) {
                    return skill;
                }
            }
        }
        return null;
    }

    public long getUpperHp() {
        return upperHp;
    }

    public String getSword() {
        return sword.name();
    }

    public String getArmor() {
        return armor.name();
    }

    public void setClick(long click) {
        this.click = click;
    }

    public void setHp(long hp) {
        this.hp = hp;
    }

    public void setUpperHp(long upperHp) {
        this.upperHp = upperHp;
    }

    public void addUpperHp(long hp) {
        this.upperHp += hp;
        addHp(hp);
    }

    public void setAttackValue(long attackValue) {
        this.attackValue = attackValue;
    }

    public void setDefenseValue(long defenseValue) {
        this.defenseValue = defenseValue;
    }

    public void setSword(Sword sword) {
        this.sword = sword;
    }

    public void setArmor(Armor armor) {
        this.armor = armor;
    }

    public void setSwordLev(long swordLev) {
        this.swordLev = swordLev;
    }

    public void setArmorLev(long armorLev) {
        this.armorLev = armorLev;
    }

    public void setMaterial(long material) {
        this.material = material;
    }

    public void setPoint(long point) {
        this.point = point;
    }

    public void setStrength(long strength) {
        this.strength = strength;
    }

    public void setPower(long power) {
        this.power = power;
    }

    public void setAgility(long agility) {
        this.agility = agility;
    }

    public void setMaxMazeLev(long maxMazeLev) {
        boolean notE = maxMazeLev != this.maxMazeLev;
        this.maxMazeLev = maxMazeLev;
        if (notE && this.maxMazeLev % 101 == 0) {
            this.skillPoint += 2;
        }
    }

    public void setClickAward(long clickAward) {
        this.clickAward = clickAward;
    }

    public long getClickAward() {
        return clickAward;
    }

    public long getSkillPoint() {
        return skillPoint;
    }

    public void setSkillPoint(long skillPoint) {
        this.skillPoint = skillPoint;
    }

    public Random getRandom() {
        return random;
    }

    public void removeSkill(Skill skill) {
        if (firstSkill == skill) {
            firstSkill = secondSkill;
            secondSkill = thirdSkill;
            thirdSkill = null;
        } else if (secondSkill == skill) {
            secondSkill = thirdSkill;
            thirdSkill = null;
        } else if (thirdSkill == skill) {
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

    public String getFormatName() {
        return "<font color=\"#800080\">" + hero.getName() + "</font>";
    }
}
