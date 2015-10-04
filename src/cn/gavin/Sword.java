package cn.gavin;

/**
 * Created by luoyuan on 8/27/15.
 */
public enum Sword {

    木剑(100, 5), 铁剑(200, 105), 铜剑(500, 1000), 银剑(1000,3000), 金剑(2000, 10000), 合金剑(4000,24000),
    鱼肠剑(8000,85000), 水波剑(16000, 180000), 烈焰剑(32000,800000), 水焰剑(64000, 1600000), 风烈剑(100000, 3400000);
    private long lev, base;

    private Sword(long lev, long base) {
        this.lev = lev;
        this.base = base;
    }

    public long getBase(){
        return base;
    }
    public Sword levelUp(long lev, Hero hero) {
        if (lev >= this.lev) {
            int index = ordinal();
            if (index < values().length - 1) {
                Sword sword = values()[index + 1];
                sword.base = hero.getRandom().nextLong(hero.getStrength()/1000) + sword.base;
                return sword;
            }else{
                Achievement.artifact.enable(null);
            }
        }
        return this;
    }
}
