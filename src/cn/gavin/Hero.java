package cn.gavin;

import android.widget.Toast;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.db.DBHelper;
import cn.gavin.forge.Accessory;
import cn.gavin.forge.HatBuilder;
import cn.gavin.forge.NecklaceBuilder;
import cn.gavin.forge.RingBuilder;
import cn.gavin.forge.effect.Effect;
import cn.gavin.monster.Defender;
import cn.gavin.monster.Monster;
import cn.gavin.skill.Skill;
import cn.gavin.skill.SkillFactory;
import cn.gavin.skill.type.AttackSkill;
import cn.gavin.skill.type.DefendSkill;
import cn.gavin.skill.type.RestoreSkill;
import cn.gavin.utils.Random;
import cn.gavin.utils.StringUtils;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Hero {
    private static final String TAG = "Hero";

    public static long MAX_GOODS_COUNT = 50;

    // 血上限成长(每点生命点数增加）
    public long MAX_HP_RISE = 5;
    // 攻击成长（每点力量点数增加）
    public long ATR_RISE = 2;
    // 防御成长 （每点敏捷点数增加）
    public long DEF_RISE = 1;
    private String formatName;
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
    private long skillPoint = 1;
    private Skill firstSkill;
    private Skill secondSkill;
    private Skill thirdSkill;
    private long awardCount;
    private long lockBox;
    private long keyCount;
    private Accessory ring;
    private Accessory necklace;
    private Accessory hat;
    private float parry;

    private EnumMap<Effect, Long> getAccessoryEffectMap() {
        EnumMap<Effect, Long> effectLongEnumMap = new EnumMap<Effect, Long>(Effect.class);
        if (ring != null) {
            calculateEffect(effectLongEnumMap, ring);
            calculateAdditionEffectE(effectLongEnumMap, ring);
        }
        if (necklace != null) {
            calculateEffect(effectLongEnumMap, necklace);
            calculateAdditionEffectE(effectLongEnumMap, necklace);
        }
        if (hat != null) {
            calculateEffect(effectLongEnumMap, hat);
            calculateAdditionEffectE(effectLongEnumMap, hat);
        }
        return effectLongEnumMap;
    }

    private void calculateEffect(EnumMap<Effect, Long> effectLongEnumMap, Accessory accessory) {
        if(accessory.getEffects()!=null) {
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
        this.formatName = "";
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
        return attackValue + sword.getBase() + swordLev * ATR_RISE;
    }

    public long getUpperDef() {
        long def = defenseValue + armorLev * DEF_RISE + armor.getBase();
        if (def >= 10000) {
            Achievement.fearDeath.enable(this);
        }
        return def;
    }

    public long getAttackValue() {
        return attackValue + random.nextLong(sword.getBase()) + random.nextLong(swordLev + 1) * DEF_RISE;
    }

    public void addAttackValue(long attackValue) {
        if((this.attackValue + attackValue) > 10) {
            this.attackValue += attackValue;
        }else{
            this.attackValue = 10;
        }
    }

    public long getBaseDefense() {
        return defenseValue;
    }

    private boolean isParry = false;

    public long getDefenseValue() {
        long defend = defenseValue + random.nextLong(armor.getBase()) + random.nextLong(armorLev + 1) * ATR_RISE;
        if (random.nextLong(100) + parry + random.nextLong(agility + 1) / 5000 > 97 + random.nextInt(20) + random.nextLong(strength + 1) / 5000) {
            defend *= 3;
            isParry = true;
        } else {
            isParry = false;
        }
        return defend;
    }

    public void addDefenseValue(long defenseValue) {
        if((this.defenseValue + defenseValue)> 10) {
            this.defenseValue += defenseValue;
        }else{
            this.defenseValue = 10;
        }
    }

    public Queue<Skill> getExistSkill() {
        return existSkill;
    }

    public void addSkill(Skill skill) {
        if (firstSkill == null) firstSkill = skill;
        else if (secondSkill == null) secondSkill = skill;
        else if (thirdSkill == null) thirdSkill = skill;
        else {
            firstSkill.setOnUsed(false);
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
            if (swordLev + sword.getBase() + attackValue >= Long.MAX_VALUE - 100) {
                return false;
            } else {
                if (material >= 100 + swordLev) {
                    material -= (100 + armorLev);
                    swordLev++;
                    if (sword != sword.levelUp(swordLev, this)) {
                        sword = sword.levelUp(swordLev, this);
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
            if (armorLev + armor.getBase() + defenseValue >= Long.MAX_VALUE - 1000) {
                return false;
            } else {
                if (material >= 80 + armorLev) {
                    material -= (80 + armorLev);
                    armorLev++;
                    if (armor != armor.levelUp(armorLev, this)) {
                        armor = armor.levelUp(armorLev, this);
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
        if (material <= 0) {
            this.material += material;
        } else if (this.material < (Long.MAX_VALUE - material - 1000))
            this.material += material;
        if (this.material < 0) this.material = 0;
        if (this.material >= 5000000) Achievement.rich.enable(this);
    }

    public long getPoint() {
        return point;
    }

    public void addPoint(long point) {
        if (point < 0 || this.point < (Long.MAX_VALUE - point - 5000))
            this.point += point;
        if (this.point < 0) this.point = 0;
        if (this.point >= 5000) Achievement.lazy.enable(this);
    }

    public long getStrength() {
        return strength;
    }

    public void addStrength() {
        if (point != 0 && strength < (Long.MAX_VALUE - 500)) {
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
        if (str < 0 || strength < (Long.MAX_VALUE - strength - 100)) {
            strength += str;
            if (str < 0 || attackValue < (Long.MAX_VALUE - ATR_RISE * str)) {
                attackValue += ATR_RISE * str;
            } else {
                attackValue = Long.MAX_VALUE;
                Achievement.extreme.enable(this);
            }
        }
        if (strength < 0) strength = 0;
        if (attackValue < 0) attackValue = 5;
    }

    public long getPower() {
        return power;
    }

    public void addLife() {
        if (point != 0 && power < (Integer.MAX_VALUE - 500)) {
            point--;
            power++;
            if (upperHp < (Long.MAX_VALUE - MAX_HP_RISE)) {
                hp += MAX_HP_RISE;
                upperHp += MAX_HP_RISE;
            }
        }
    }

    public void addLife(long life) {
        if (life < 0 || power < (Integer.MAX_VALUE - life - 100)) {
            power += life;
            if (life < 0 || upperHp < (Long.MAX_VALUE - MAX_HP_RISE * life)) {
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
        if (this.maxMazeLev < Long.MAX_VALUE - 10) {
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
        if((this.upperHp + hp) > 9) {
            this.upperHp += hp;
        }else{
            upperHp = 10;
        }
        //addHp(hp);
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
        if (!StringUtils.isNotEmpty(formatName)) {
            formatName = "<font color=\"#800080\">" + getName() + "</font>";
        }
        return formatName;
    }

    public long getAwardCount() {
        return awardCount;
    }

    public void setAwardCount(long awardCount) {
        this.awardCount = awardCount;
    }

    public long getKeyCount() {
        return keyCount;
    }

    public void setKeyCount(long keyCount) {
        this.keyCount = keyCount;
    }

    public long getLockBox() {
        return lockBox;
    }

    public void setLockBox(long lockBox) {
        this.lockBox = lockBox;
    }

    public Accessory getRing() {
        return ring;
    }

    public void setRing(Accessory ring) {
        cleanEffect();
        if(ring!=null) {
            this.ring = ring;
            appendEffect();
        }else{
            this.ring = null;
        }
    }

    private void appendEffect() {
        EnumMap<Effect, Long> effectLongEnumMap = getAccessoryEffectMap();
        for (EnumMap.Entry<Effect, Long> effect : effectLongEnumMap.entrySet()) {
            switch (effect.getKey()) {
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
            }
        }
    }

    private void cleanEffect() {
        EnumMap<Effect, Long> effectLongEnumMap = getAccessoryEffectMap();
        for (EnumMap.Entry<Effect, Long> effect : effectLongEnumMap.entrySet()) {
            switch (effect.getKey()) {
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
                    break;
            }
        }
    }

    public Accessory getNecklace() {
        return necklace;
    }

    public void setNecklace(Accessory necklace) {
        cleanEffect();
        if(necklace!=null) {
            this.necklace = necklace;
            appendEffect();
        }else{
            this.necklace = null;
        }
    }

    public Accessory getHat() {
        return hat;
    }

    public boolean isParry() {
        return isParry;
    }

    public void setHat(Accessory hat) {
        cleanEffect();
        if(hat!=null) {
            this.hat = hat;
            appendEffect();
        }else{
            this.hat = null;
        }
    }

    public void reincarnation() {
        if (material < 200101) {
            Toast.makeText(MainGameActivity.context, "锻造点数不足200101！" + getName(), Toast.LENGTH_SHORT).show();
        } else {
            Skill skill;
            if (getFirstSkill() != null) {
                skill = getFirstSkill();
            } else {
                skill = getThirdSkill();
            }
            Defender.addDefender(StringUtils.toHexString(name), getUpperHp() + getUpperDef(), getUpperAtk(), getMaxMazeLev(), skill == null ? "重击" : skill.getName(), 10, "遇見了吾，你將止步於此！");
            MAX_HP_RISE = random.nextLong(power / 5000 + 4) + 5;
            DEF_RISE = random.nextLong(agility / 5000 + 2) + 1;
            ATR_RISE = random.nextLong(strength / 5000 + 3) + 2;
            maxMazeLev = 1;
            material -= 200101;
            attackValue = random.nextLong(20) + 10;
            defenseValue = random.nextLong(20) + 10;
            setUpperHp(random.nextLong(20) + 25);
            point = 1;
            parry = 0;
            armor = Armor.破布;
            sword = Sword.木剑;
            armorLev = 1;
            swordLev = 1;
            power = 5;
            strength = 5;
            agility = 5;

            DBHelper dbHelper = DBHelper.getDbHelper();
            dbHelper.beginTransaction();
            dbHelper.excuseSQLWithoutResult("DELETE FROM item");
            dbHelper.excuseSQLWithoutResult("DELETE FROM accessory");
            SkillFactory.clean();
            Accessory ring = getRing();
            if (ring != null) {
                this.ring = null;
                setRing(ring);
                ring.save();
            }
            Accessory necklace = getNecklace();
            if (necklace != null) {
                this.necklace = null;
                setNecklace(necklace);
                necklace.save();
            }
            Accessory hat = getHat();
            if (hat != null) {
                this.hat = null;
                setHat(hat);
                hat.save();
            }
            Achievement.reBird.enable(this);
            dbHelper.endTransaction();
            MainGameActivity.context.addMessage(getFormatName() + "成功转生！");
        }
    }

    public void setAccessory(Accessory accessory){
        switch (accessory.getType()){
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

    public boolean isReinforce(Accessory accessory){
        switch (accessory.getType()){
            case RingBuilder.type:
                return ring!=null && ring.getId().equals(accessory.getId()) &&
                        ((necklace!=null && necklace.getElement().isReinforce(accessory.getElement())) || (hat!=null && hat.getElement().isReinforce(accessory.getElement())));
            case NecklaceBuilder.type:
                return necklace!=null && necklace.getId().equals(accessory.getId()) &&
                        ((ring!=null && ring.getElement().isReinforce(accessory.getElement())) || (hat!=null && hat.getElement().isReinforce(accessory.getElement())));
            case HatBuilder.type:
                return hat!=null && hat.getId().equals(accessory.getId()) &&
                        ((necklace!=null && necklace.getElement().isReinforce(accessory.getElement())) || (ring!=null && ring.getElement().isReinforce(accessory.getElement())));
            default:
                return false;
        }
    }
}
