package cn.gavin;

/**
 * Created by luoyuan on 8/27/15.
 */
public enum Armor {
    破布(100,3), 肚兜(200, 50), 布衣(500, 200), 布甲(1000, 600), 铁甲(2000, 2000), 铜甲(4000, 8000), 银甲(7000, 20000), 金甲(9990, 50000),
    水波甲(16000, 100000), 烈焰甲(32000,300000), 天使甲(64000, 700000), 能量甲(100000, 1100000);
    private long lev, base;
public long getBase(){
    return base;
}
    private Armor(long lev, long base) {
        this.lev = lev;
        this.base = base;
    }

    public Armor levelUp(long lev, Hero hero) {
        if (lev >= this.lev) {
            int index = ordinal();
            if (index < values().length - 1) {
                Armor armor = values()[index + 1];
                armor.base = armor.base + hero.getRandom().nextLong(hero.getPower()/3000) + hero.getArmorLev() * hero.DEF_RISE;
                return armor;
            }else{
                Achievement.artifact.enable(null);
            }
        }
        return this;
    }
}
