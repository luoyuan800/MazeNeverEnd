package cn.gavin;

/**
 * Created by luoyuan on 8/27/15.
 */
public enum Armor {
    破布(100,3), 肚兜(200, 103), 布衣(500, 503), 布甲(1000, 2000), 铁甲(2000, 4000), 铜甲(4000, 10000), 银甲(8000, 30000), 金甲(10000, 80000),
    烈焰甲(16000, 180000), 水波甲(32000,400000), 天使甲(64000, 10000000), 能量甲(100000, 2000000);
    private long lev, base;
public long getBase(){
    return base;
}
    private Armor(long lev, long base) {
        this.lev = lev;
        this.base = base;
    }

    public Armor levelUp(long lev) {
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
