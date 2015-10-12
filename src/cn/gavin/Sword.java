package cn.gavin;

/**
 * Created by luoyuan on 8/27/15.
 */
public enum Sword {

    木剑(100, 5), 铁剑(200, 90), 铜剑(500, 700), 银剑(1000,2000), 金剑(2000, 8000), 合金剑(4000,20000),
    鱼肠剑(8000,50000), 水波剑(16000, 80000), 烈焰剑(32000,120000), 水焰剑(64000, 580000), 风烈剑(100000, 1500000);
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
                sword.base = hero.getRandom().nextLong(hero.getStrength()/1000) + sword.base + hero.getSwordLev() * hero.ATR_RISE;
                return sword;
            }else{
                Achievement.artifact.enable(null);
            }
        }
        return this;
    }
}
