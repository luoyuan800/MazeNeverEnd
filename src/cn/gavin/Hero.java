package cn.gavin;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import cn.gavin.activity.MainGameActivity;
import cn.gavin.db.DBHelper;
import cn.gavin.forge.Accessory;
import cn.gavin.forge.HatBuilder;
import cn.gavin.forge.NecklaceBuilder;
import cn.gavin.forge.RingBuilder;
import cn.gavin.forge.effect.Effect;
import cn.gavin.monster.Monster;
import cn.gavin.pet.Pet;
import cn.gavin.skill.Skill;
import cn.gavin.skill.SkillFactory;
import cn.gavin.skill.type.AttackSkill;
import cn.gavin.skill.type.DefendSkill;
import cn.gavin.skill.type.RestoreSkill;
import cn.gavin.utils.Random;

public class Hero implements BaseObject {
    private static final String TAG = "Hero";

    public static Long MAX_GOODS_COUNT = 50l;

    // 血上限成长(每点生命点数增加）
    public Long MAX_HP_RISE = 5l;
    // 攻击成长（每点力量点数增加）
    public Long ATR_RISE = 2l;
    // 防御成长 （每点敏捷点数增加）
    public Long DEF_RISE = 1l;
    private String formatName;
    private Long click = 0l;
    private String name;
    private Long hp = 0l;//当前
    private Long upperHp = 10l;//上限值
    private Long attackValue = 0l;
    private Long defenseValue = 0l;
    public Long level = 1l;
    private Queue<Skill> existSkill; // 已有的技能
    private Sword sword;
    private Armor armor;
    private Long swordLev = 0l;
    private Long armorLev = 0l;
    private Long material = 0l;
    private Long point = 0l;
    private Long strength = 0l;//力量，影响攻击数值上限
    private Long power = 0l;//体力，影响HP上限，生命恢复技能效果
    private Long agility = 0l;//敏捷，影响技能施放概率，防御数值上限
    private Long maxMazeLev = 1l;
    private Random random;
    private Long clickAward = 1l;
    private Long deathCount = 0l;
    private Long skillPoint = 1l;
    private Skill firstSkill;
    private Skill secondSkill;
    private Skill thirdSkill;
    private Long awardCount = 1l;
    private Long lockBox = 0l;
    private Long keyCount = 0l;
    private Accessory ring;
    private Accessory necklace;
    private Accessory hat;
    private Float parry = 0.0f;
    private boolean onChange = false;
    private Long changAtk = 0l;
    private Long changeHp = 0l;
    private Long changeUhp = 0l;
    private String changeName;
    private Boolean onSkill = false;
    private Long skillAdditionAtk = 0l;
    private Long skillAdditionDef = 0l;
    private Long skillAdditionHp = 0l;
    private Long reincaCount = 0l;
    private Long hitRate = 0l;
    private Long pay = 0l;
    private String hello = "你是不可能超越我的！";
    private Float dodgeRate = 0f;
    private Long clickPointAward = 0l;
    private Element element = Element.无;
    private List<Pet> pets;
    private int petSize = 3;
    private float petRate = 0.7f;
    private float eggRate = 300;
    private long eggStep = 1;
    private String titleColor;
    private long resetSkillCount = 0;
    private String bakColor;

    public Float getParry() {
        return parry;
    }

    public void setParry(float parry) {
        this.parry = parry;
    }

    private EnumMap<Effect, Long> getAccessoryEffectMap(Accessory accessory) {
        EnumMap<Effect, Long> effectLongEnumMap = new EnumMap<Effect, Long>(Effect.class);
        if (accessory != null) {
            calculateEffect(effectLongEnumMap, accessory);
            calculateAdditionEffectE(effectLongEnumMap, accessory);
        }
        return effectLongEnumMap;
    }

    private void calculateEffect(EnumMap<Effect, Long> effectLongEnumMap, Accessory accessory) {
        if (accessory.getEffects() != null) {
            for (EnumMap.Entry<Effect, Number> effect : accessory.getEffects().entrySet()) {
                Long value = effectLongEnumMap.get(effect.getKey());
                if (value != null) {
                    effectLongEnumMap.put(effect.getKey(), value + effect.getValue().longValue());
                } else {
                    effectLongEnumMap.put(effect.getKey(), effect.getValue().longValue());
                }
            }
        }
    }

    private void calculateAdditionEffectE(EnumMap<Effect, Long> effectLongEnumMap, Accessory accessory) {
        if (accessory.getAdditionEffects() != null &&
                isReinforce(accessory)) {
            for (EnumMap.Entry<Effect, Number> effect : accessory.getAdditionEffects().entrySet()) {
                Long value = effectLongEnumMap.get(effect.getKey());
                if (value != null) {
                    effectLongEnumMap.put(effect.getKey(), value + effect.getValue().longValue());
                } else {
                    effectLongEnumMap.put(effect.getKey(), effect.getValue().longValue());
                }
            }
        }
    }

    public Long getDeathCount() {
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
        this.formatName = "";
    }

    public Long getRealHP() {
        return hp;
    }

    public Long getHp() {
        long rs = (isOnChange() ? changeHp : hp) + getSkillAdditionHp();
        if (rs == 0) {
            onChange = false;
        }
        return rs;
    }

    public void addHp(long hp) {
        if (onChange) {
            this.changeHp += hp;
        } else {
            if (hp < 0) {
                this.hp += hp;
            } else if (this.hp < (Integer.MAX_VALUE - hp - 100)) {
                this.hp += hp;
            }
            if (this.hp <= 0 && getSkillAdditionHp() <= 0) {
                this.hp = 0l;
                deathCount++;
                if (deathCount == 10000) {
                    Achievement.maltreat.enable(this);
                }
            }
            if (this.hp > upperHp) this.hp = upperHp;
        }
    }

    public Long getBaseAttackValue() {
        return attackValue;
    }

    public Long getUpperAtk() {
        return attackValue + sword.getBase() + swordLev * ATR_RISE + getSkillAdditionAtk();
    }

    public Long getUpperDef() {
        long def = defenseValue + armorLev * DEF_RISE + armor.getBase() + getSkillAdditionDef();
        if (def >= 10000) {
            Achievement.fearDeath.enable(this);
        }
        return def;
    }

    private Boolean isHit = false;

    public Long getAttackValue() {
        long atk = isOnChange() ? changAtk : (attackValue + random.nextLong(sword.getBase()) + random.nextLong(swordLev + 1) * DEF_RISE + getSkillAdditionAtk());
        if (random.nextLong(100) + hitRate + random.nextLong(agility + 1) / 5000 > 97 + random.nextInt(100) + random.nextLong(power + 1) / 5000) {
            atk += atk / 3;
            isHit = true;
        } else {
            isHit = false;
        }
        return atk;
    }

    public void addAttackValue(long attackValue) {
        if (attackValue < 0 || this.attackValue <= Long.MAX_VALUE - attackValue) {
            this.attackValue += attackValue;
        }

    }

    public Long getBaseDefense() {
        return defenseValue;
    }

    private Boolean isParry = false;

    public Long getDefenseValue() {
        long defend = defenseValue + random.nextLong(armor.getBase()) + random.nextLong(armorLev + 1) * ATR_RISE + getSkillAdditionDef();
        if (parry > 100) parry = 90f;
        if (random.nextLong(100) + parry + random.nextLong(agility + 1) / 5000 > 97 + random.nextInt(50) + random.nextLong(strength + 1) / 5000) {
            defend *= 3;
            isParry = true;
        } else {
            isParry = false;
        }
        return defend;
    }

    public void addDefenseValue(long defenseValue) {
        if (defenseValue < 0 || this.defenseValue <= Long.MAX_VALUE - defenseValue) {
            this.defenseValue += defenseValue;
        }
    }

    public Queue<Skill> getExistSkill() {
        return existSkill;
    }

    public void addSkill(Skill skill) {
        if (skill.equal(firstSkill) || skill.equal(secondSkill) || skill.equal(thirdSkill)) {
            return;
        }
        if (firstSkill != null && thirdSkill != null && secondSkill != null) {
            firstSkill.setOnUsed(false);
        }
        if (firstSkill == null) firstSkill = skill;
        else if (secondSkill == null) secondSkill = skill;
        else if (thirdSkill == null) thirdSkill = skill;
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

    public Boolean upgradeSword(long count) {
        for (int i = 0; i < count; i++) {
            if (swordLev * ATR_RISE + sword.getBase() + attackValue >= Long.MAX_VALUE - 100) {
                return false;
            } else {
                if (material > 100 + swordLev) {
                    material -= (100 + swordLev);
                    swordLev++;
                    if (sword != sword.levelUp(swordLev, this)) {
                        addAttackValue(getRandom().nextLong(getStrength() / 1000) + getSwordLev() * ATR_RISE);
                        sword = sword.levelUp(swordLev, this);
                        swordLev = 0l;
                    }
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    public Boolean upgradeArmor(long count) {
        for (int i = 0; i < count; i++) {
            if (armorLev * DEF_RISE + armor.getBase() + defenseValue >= Long.MAX_VALUE - 1000) {
                return false;
            } else {
                if (material > 80 + armorLev) {
                    material -= (80 + armorLev);
                    armorLev++;
                    if (armor != armor.levelUp(armorLev, this)) {
                        addDefenseValue(random.nextLong(getPower() / 3000) + getArmorLev() * DEF_RISE);
                        armor = armor.levelUp(armorLev, this);
                        armorLev = 0l;
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

    public Long getMaterial() {
        return material;
    }

    public void addMaterial(long material) {
        if (material <= 0) {
            this.material += material;
        } else if (this.material < (Long.MAX_VALUE - material - 1000))
            this.material += material;
        if (this.material < 0) this.material = 0l;
        if (this.material >= 5000000) Achievement.rich.enable(this);
    }

    public Long getPoint() {
        return point;
    }

    public void addPoint(long point) {
        if (point < 0 || this.point < (Long.MAX_VALUE - point - 5000))
            this.point += point;
        if (this.point < 0) this.point = 0l;
        if (this.point >= 5000) Achievement.lazy.enable(this);
    }

    public Long getStrength() {
        return strength;
    }

    public void addStrength() {
        if (point != 0 && strength < (Integer.MAX_VALUE)) {
            point--;
            strength++;
            if (attackValue < (Long.MAX_VALUE - ATR_RISE - 500))
                attackValue += ATR_RISE;
            else {
                attackValue = Long.MAX_VALUE;
                Achievement.extreme.enable(this);
            }
        }
    }

    public void addStrength(long str) {
        if (str < 0 || strength < (Integer.MAX_VALUE)) {
            strength += str;
            if (str < 0 || attackValue < (Long.MAX_VALUE - ATR_RISE * str)) {
                attackValue += ATR_RISE * str;
            } else {
                attackValue = Long.MAX_VALUE;
                Achievement.extreme.enable(this);
            }
        }
    }

    public Long getPower() {
        return power;
    }

    public void addLife() {
        if (point != 0 && power < (Integer.MAX_VALUE)) {
            point--;
            power++;
            if (upperHp < (Long.MAX_VALUE - MAX_HP_RISE)) {
                hp += MAX_HP_RISE;
                upperHp += MAX_HP_RISE;
            }
        }
    }

    public void addLife(long life) {
        if (life < 0 || power < (Integer.MAX_VALUE)) {
            power += life;
            if (life < 0 || upperHp < (Long.MAX_VALUE - MAX_HP_RISE * life)) {
                hp += MAX_HP_RISE * life;
                upperHp += MAX_HP_RISE * life;
            }
        }
        if (upperHp < 0) upperHp = 20l;
    }

    public Long getAgility() {
        return agility;
    }

    public void addAgility() {
        if (point != 0 && (agility < Integer.MAX_VALUE - 500)) {
            point--;
            agility++;
            if (defenseValue < (Long.MAX_VALUE - DEF_RISE))
                defenseValue += DEF_RISE;
            else {
                defenseValue = Long.MAX_VALUE;
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
            if (agi < 0 || defenseValue < (Long.MAX_VALUE - DEF_RISE * agi))
                defenseValue += DEF_RISE * agi;
            else {
                defenseValue = Long.MAX_VALUE;
                Achievement.master.enable(this);
            }
        }
        if (agility >= 10000) Achievement.skilldness.enable(this);
    }

    public void restore() {
        onChange = false;
        onSkill = false;
        this.hp = upperHp;
        for(Pet pet : getPets()){
            pet.restore();
        }
    }

    public Long getMaxMazeLev() {
        return maxMazeLev;
    }

    public void addMaxMazeLev() {
        if (this.maxMazeLev < Long.MAX_VALUE - 10) {
            this.maxMazeLev++;
        }
    }

    public Long getSwordLev() {
        return swordLev;
    }

    public Long getArmorLev() {
        return armorLev;
    }

    public Long getClick() {
        return click;
    }

    private Long latestClick = 0l;

    public void click(boolean award) {
        long interval = 95;
        if (this.getClickAward() > 300) interval = 200;
        if (this.getClickPointAward() > 5) interval += 100;
        if (System.currentTimeMillis() - latestClick > interval) {
            if (click < Long.MAX_VALUE - 1000) {

                if (award) {
                    if (this.click % 1000 == 0) {
                        point += random.nextLong(15);
                    }
                    if (this.click % 3000 == 0) {
                        if (random.nextBoolean()) {
                            keyCount += 1;
                        }
                    }
                    if(clickAward > 500) {
                        addMaterial(random.nextLong(clickAward) + 100);
                    }else{
                        addMaterial(clickAward);
                    }
                    if(clickPointAward > 5) {
                        addPoint(random.nextLong(clickPointAward));
                    }else{
                        addMaterial(clickPointAward);
                    }
                }
                this.click++;
                switch (click.intValue()) {
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
        latestClick = System.currentTimeMillis();
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

    public Long getRealUHP() {
        return upperHp;
    }

    public Long getUpperHp() {
        return (isOnChange() ? changeUhp : upperHp) + getSkillAdditionHp();
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
        if (this.upperHp + hp < Long.MAX_VALUE - 1000) {
            if ((this.upperHp + hp) > 9) {
                this.upperHp += hp;
            } else {
                upperHp = 10l;
            }
            //addHp(hp);
        }
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
        if (clickAward == 0) {
            clickAward = 1;
        }
        this.clickAward = clickAward;
    }

    public Long getClickAward() {
        return clickAward;
    }

    public Long getSkillPoint() {
        return skillPoint;
    }

    public void setSkillPoint(long skillPoint) {
        this.skillPoint = skillPoint;
    }

    public Random getRandom() {
        return random;
    }

    public void removeSkill(Skill skill) {
        if (skill.equal(firstSkill)) {
            firstSkill = secondSkill;
            secondSkill = thirdSkill;
            thirdSkill = null;
        } else if (skill.equal(secondSkill)) {
            secondSkill = thirdSkill;
            thirdSkill = null;
        } else if (skill.equal(thirdSkill)) {
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
        if (onChange) {
            return "<font color=\"#800080\">" + changeName + "</font>";
        } else {
            return "<font color=\"#800080\">" + getName() + "</font>(" + element + ")";

        }
    }

    public Long getAwardCount() {
        return awardCount;
    }

    public void setAwardCount(long awardCount) {
        this.awardCount = awardCount;
    }

    public Long getKeyCount() {
        return keyCount;
    }

    public void setKeyCount(long keyCount) {
        this.keyCount = keyCount;
    }

    public Long getLockBox() {
        return lockBox;
    }

    public void setLockBox(long lockBox) {
        this.lockBox = lockBox;
    }

    public Accessory getRing() {
        return ring;
    }

    public synchronized void setRing(Accessory ring) {
        if (ring != null && this.ring != null && this.ring.getId().equals(ring.getId())) {
            return;
        }
        cleanEffect();
        if (ring != null) {
            if (this.ring == null || !(ring.getId().equalsIgnoreCase(this.ring.getId()))) {
                this.ring = ring;
            }
        } else {
            this.ring = null;
        }
        appendEffect();
    }

    private void appendEffect(Accessory accessory) {
        EnumMap<Effect, Long> effectLongEnumMap = getAccessoryEffectMap(accessory);
        for (EnumMap.Entry<Effect, Long> effect : effectLongEnumMap.entrySet()) {
            switch (effect.getKey()) {
                case ADD_DODGE_RATE:
                    setDodgeRate(dodgeRate + effect.getValue());
                    break;
                case ADD_CLICK_POINT_AWARD:
                    setClickPointAward(clickPointAward + effect.getValue());
                    break;
                case ADD_HIT_RATE:
                    setHitRate(hitRate + effect.getValue());
                    break;
                case ADD_STR:
                    addStrength(effect.getValue());
                    break;
                case ADD_UPPER_HP:
                    addUpperHp(effect.getValue());
                    break;
                case ADD_DEF:
                    addDefenseValue(effect.getValue());
                    break;
                case ADD_ATK:
                    addAttackValue(effect.getValue());
                    break;
                case ADD_AGI:
                    addAgility(effect.getValue());
                    break;
                case ADD_POWER:
                    addLife(effect.getValue());
                    break;
                case ADD_CLICK_AWARD:
                    addClickAward(effect.getValue());
                    break;
                case ADD_PARRY:
                    parry += effect.getValue();
                    break;
                case ADD_PER_ATK:
                    Double atk = getUpperAtk() * effect.getValue()/100d;
                    addAttackValue(atk.longValue());
                    break;
                case ADD_PER_DEF:
                    Double def = getUpperDef() * effect.getValue()/100d;
                    addDefenseValue(def.longValue());
                    break;
                case ADD_PER_UPPER_HP:
                    Double uHp = getUpperHp() * effect.getValue()/100d;
                    addUpperHp(uHp.longValue());
                    break;
            }
        }
    }

    private void cleanEffect(Accessory accessory) {
        EnumMap<Effect, Long> effectLongEnumMap = getAccessoryEffectMap(accessory);
        for (EnumMap.Entry<Effect, Long> effect : effectLongEnumMap.entrySet()) {
            switch (effect.getKey()) {
                case ADD_DODGE_RATE:
                    setDodgeRate(dodgeRate - effect.getValue());
                    break;
                case ADD_CLICK_POINT_AWARD:
                    setClickPointAward(clickPointAward - effect.getValue());
                    break;
                case ADD_HIT_RATE:
                    setHitRate(hitRate - effect.getValue());
                    break;
                case ADD_STR:
                    addStrength(-effect.getValue());
                    break;
                case ADD_UPPER_HP:
                    addUpperHp(-effect.getValue());
                    break;
                case ADD_DEF:
                    addDefenseValue(-effect.getValue());
                    break;
                case ADD_ATK:
                    addAttackValue(-effect.getValue());
                    break;
                case ADD_AGI:
                    addAgility(-effect.getValue());
                    break;
                case ADD_POWER:
                    addLife(-effect.getValue());
                    break;
                case ADD_CLICK_AWARD:
                    addClickAward(-effect.getValue());
                    break;
                case ADD_PARRY:
                    parry -= effect.getValue();
                    if (parry < 0) parry = 0f;
                    break;
                case ADD_PER_ATK:
                    Double atk = attackValue * effect.getValue()/100d;
                    addAttackValue(-atk.longValue());
                    break;
                case ADD_PER_DEF:
                    Double def = defenseValue * effect.getValue()/100d;
                    addDefenseValue(-def.longValue());
                    break;
                case ADD_PER_UPPER_HP:
                    Double uHp = getRealUHP() * effect.getValue()/100d;
                    addUpperHp(-uHp.longValue());
                    break;
            }
        }
    }

    public Accessory getNecklace() {
        return necklace;
    }

    public synchronized void setNecklace(Accessory necklace) {
        if (necklace != null && this.necklace != null && this.necklace.getId().equals(necklace.getId())) {
            return;
        }
        cleanEffect();
        if (necklace != null) {
            if (this.necklace == null || !(necklace.getId().equalsIgnoreCase(this.necklace.getId()))) {
                this.necklace = necklace;
            }
        } else {
            this.necklace = null;
        }
        appendEffect();
    }

    private void appendEffect() {
        appendEffect(ring);
        appendEffect(hat);
        appendEffect(necklace);
    }

    private void cleanEffect() {
        cleanEffect(ring);
        cleanEffect(hat);
        cleanEffect(necklace);
    }

    public Accessory getHat() {
        return hat;
    }

    public Boolean isParry() {
        return isParry;
    }

    public synchronized void setHat(Accessory hat) {
        if (hat != null && this.hat != null && this.hat.getId().equals(hat.getId())) {
            return;
        }
        cleanEffect();
        if (hat != null) {
            if (this.hat == null || !(hat.getId().equalsIgnoreCase(this.hat.getId()))) {
                this.hat = hat;
            }
        } else {
            this.hat = null;
        }
        appendEffect();
    }

    public void reincarnation() {
        long mate = (reincaCount + 2) * 600101;
        if (material < mate) {
            Toast.makeText(MainGameActivity.context, "锻造点数不足" + mate + "！" + getName(), Toast.LENGTH_SHORT).show();
        } else {

            addReincarnation(name + "(" + reincaCount + ")", getUpperHp() + getUpperDef(), getUpperAtk(), getMaxMazeLev());
            long hpRise = random.nextLong(power / 5103 + 4) + MAX_HP_RISE + reincaCount;
            long defRISE = random.nextLong(agility / 6309 + 2) + DEF_RISE + reincaCount;
            long atkRISE = random.nextLong(strength / 7501 + 3) + ATR_RISE + reincaCount;
            if(hpRise > 10 * MAX_HP_RISE){
                MAX_HP_RISE  += random.nextLong(hpRise) + reincaCount + 1;
            }else{
                MAX_HP_RISE = hpRise;
            }
            if(defRISE > 10 * DEF_RISE){
                DEF_RISE  += random.nextLong(defRISE) + reincaCount + 1;
            }else{
                DEF_RISE = defRISE;
            }
            if(atkRISE > 10 * ATR_RISE){
                ATR_RISE  += random.nextLong(atkRISE) + reincaCount + 1;
            }else{
                ATR_RISE = atkRISE;
            }
            material -= mate;
            long attackValue = random.nextLong(20) + 10;
            long defenseValue = random.nextLong(20) + 10;
            long upperHp = random.nextLong(20) + 25;
            SkillFactory.clean();
            setUpperHp(upperHp);
            hp = upperHp;
            setAttackValue(attackValue);
            setDefenseValue(defenseValue);
            point = 1l;
            parry = 0f;
            armor = Armor.破布;
            sword = Sword.木剑;
            armorLev = 1l;
            swordLev = 1l;
            power = 5l;
            strength = 5l;
            agility = 5l;
            hitRate = 0l;
            clickPointAward = 0l;
            clickAward = reincaCount + 1;
            DBHelper dbHelper = DBHelper.getDbHelper();
            dbHelper.beginTransaction();
            dbHelper.excuseSQLWithoutResult("DELETE FROM item");
            dbHelper.excuseSQLWithoutResult("DELETE FROM accessory");
            Accessory ring = getRing();
            if (ring != null) {
                ring.save();
            }
            Accessory necklace = getNecklace();
            if (necklace != null) {
                necklace.save();
            }
            Accessory hat = getHat();
            if (hat != null) {
                hat.save();
            }
            appendEffect();
            Achievement.reBird.enable(this);
            dbHelper.endTransaction();
            MainGameActivity.context.addMessage(getFormatName() + "成功转生！");
            MainGameActivity.context.save();
            reincaCount++;
        }
    }

    private void addReincarnation(String name, long hp, long atk, long lev) {
        String sql = String.format("REPLACE INTO npc(name, lev, hp, atk) " +
                        "values('%s','%s','%s','%s')",
                name, lev, hp, atk);
        DBHelper.getDbHelper().excuseSQLWithoutResult(sql);
    }

    public void setAccessory(Accessory accessory) {
        switch (accessory.getType()) {
            case RingBuilder.type:
                this.ring = accessory;
                break;
            case NecklaceBuilder.type:
                this.necklace = accessory;
                break;
            case HatBuilder.type:
                this.hat = accessory;
                break;
        }
    }

    public Boolean isReinforce(Accessory accessory) {
        switch (accessory.getType()) {
            case RingBuilder.type:
                return ring != null && ring.getId().equals(accessory.getId()) &&
                        ((necklace != null && necklace.getElement().isReinforce(accessory.getElement())) || (hat != null && hat.getElement().isReinforce(accessory.getElement())));
            case NecklaceBuilder.type:
                return necklace != null && necklace.getId().equals(accessory.getId()) &&
                        ((ring != null && ring.getElement().isReinforce(accessory.getElement())) || (hat != null && hat.getElement().isReinforce(accessory.getElement())));
            case HatBuilder.type:
                return hat != null && hat.getId().equals(accessory.getId()) &&
                        ((necklace != null && necklace.getElement().isReinforce(accessory.getElement())) || (ring != null && ring.getElement().isReinforce(accessory.getElement())));
            default:
                return false;
        }
    }

    public Boolean isOnChange() {
        return onChange;
    }

    public void setOnChange(boolean onChange) {
        this.onChange = onChange;
    }

    public Long getChangeHp() {
        return changeHp;
    }

    public void setChangeHp(Long changeHp) {
        this.changeHp = changeHp;
    }

    public Long getChangAtk() {
        return changAtk;
    }

    public void setChangAtk(Long changAtk) {
        this.changAtk = changAtk;
    }

    public void setChangeUhp(Long changeUhp) {
        this.changeUhp = changeUhp;
    }

    public void setChangeName(String changeName) {
        this.changeName = changeName;
    }

    public Boolean isOnSkill() {
        return onSkill;
    }

    public void setOnSkill(boolean onSkill) {
        this.onSkill = onSkill;
    }

    public Long getSkillAdditionAtk() {
        return onSkill ? skillAdditionAtk : 0;
    }

    public void setSkillAdditionAtk(Long skillAdditionAtk) {
        this.skillAdditionAtk = skillAdditionAtk;
    }

    public Long getSkillAdditionDef() {
        return onSkill ? skillAdditionDef : 0;
    }

    public void setSkillAdditionDef(long skillAdditionDef) {
        this.skillAdditionDef = skillAdditionDef;
    }

    public Long getSkillAdditionHp() {
        return onSkill ? skillAdditionHp : 0;
    }

    public void setSkillAdditionHp(long skillAdditionHp) {
        this.skillAdditionHp = skillAdditionHp;
    }

    public Long getReincaCount() {
        return reincaCount;
    }

    public void setReincaCount(long reincaCount) {
        this.reincaCount = reincaCount;
    }

    public Long getHitRate() {
        return hitRate;
    }

    public void setHitRate(long hitRate) {
        if (hitRate > 90) {
            hitRate = 90;
        }
        if (hitRate < 0) hitRate = 0;
        this.hitRate = hitRate;
    }

    public Boolean isHit() {
        if (isHit) {
            Achievement.hitter.enable(this);
        }
        return isHit;
    }

    public void setPay(long pay) {
        this.pay = pay;
    }

    public Long getPay() {
        return pay;
    }

    public String getHello() {
        return hello;
    }

    public void setHello(String hello) {
        this.hello = hello;
    }

    public Float getDodgeRate() {
        return dodgeRate;
    }

    public void setDodgeRate(Float dodgeRate) {
        if (dodgeRate > 90) {
            dodgeRate = 90f;
        }
        if (dodgeRate < 0) {
            dodgeRate = 0f;
        }
        this.dodgeRate = dodgeRate;
    }

    public Long getClickPointAward() {
        return clickPointAward;
    }

    public void setClickPointAward(Long clickPointAward) {
        this.clickPointAward = clickPointAward;
        if (this.clickPointAward > 2) {
            this.clickPointAward = 2l;
        }
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public List<Pet> getPets() {
        if(pets == null){
            pets = new ArrayList<Pet>();
        }
        return pets;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }

    public int getPetSize() {
        if(petSize <= 0){
            petSize = 3;
        }
        return petSize;
    }

    public void setPetSize(int petSize) {
        this.petSize = petSize;
    }

    public float getPetRate() {
        return petRate;
    }

    public void setPetRate(float petRate) {
        this.petRate = petRate;
    }

    public float getEggRate() {
        return eggRate;
    }

    public void setEggRate(float eggRate) {
        this.eggRate = eggRate;
    }

    public long getEggStep() {
        return eggStep;
    }

    public void setEggStep(long eggStep) {
        this.eggStep = eggStep;
    }

    public void removePet(Pet pet) {
        List<Pet> petList = getPets();
        for(int i = 0; i< petList.size(); i++){
            if(petList.get(i).getId().equals(pet.getId())){
                petList.remove(i);
                return;
            }
        }
    }

    public void setTitleColor(String titleColor) {
        this.titleColor = titleColor;
    }

    public String getTitleColor() {
        return titleColor;
    }

    public long getResetSkillCount() {
        return resetSkillCount;
    }

    public void setResetSkillCount(long resetSkillCount) {
        this.resetSkillCount = resetSkillCount;
    }

    public String getBakColor() {
        return bakColor;
    }

    public void setBakColor(String bakColor) {
        this.bakColor = bakColor;
    }
}
