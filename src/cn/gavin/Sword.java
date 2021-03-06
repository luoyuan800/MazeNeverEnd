package cn.gavin;

/**
 * Created by luoyuan on 8/27/15.
 */
public enum Sword {

    木剑(100, 5), 铁剑(200, 90), 铜剑(500, 700), 银剑(1000,2000), 金剑(2000, 8000), 合金剑(4000,20000),
    鱼肠剑(8000,50000), 水波剑(16000, 80000), 烈焰剑(32000,120000), 水焰剑(64000, 580000),
    风烈剑(100000, 1500000), 火麟剑(200000, 2500000), 玉启剑(220000, 5500000), 灭焰剑(300000, 8500000), 罗渊剑(1000000, 19500000);
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
                return values()[index + 1];
            }else{
                Achievement.artifact.enable(null);
            }
        }
        return this;
    }
}
