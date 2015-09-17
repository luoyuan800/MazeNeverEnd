package cn.gavin;

/**
 * Created by luoyuan on 8/27/15.
 */
public enum Sword {

    木剑(100, 5), 铁剑(200, 105), 铜剑(500, 350), 银剑(1000,900), 金剑(2000, 1900), 合金剑(4000,3900),
    鱼肠剑(8000,8000), 水波剑(16000, 16000), 烈焰剑(32000,30000), 水焰剑(64000, 60000), 风烈剑(100000, 130000);
    private long lev, base;

    private Sword(long lev, long base) {
        this.lev = lev;
        this.base = base;
    }

    public long getBase(){
        return base;
    }
    public Sword levelUp(long lev) {
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
