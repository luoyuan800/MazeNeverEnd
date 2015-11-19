package cn.gavin.palace.nskill;

import cn.gavin.palace.Base;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 10/22/2015.
 */
public class Lightning extends AtkSkill {
    long base;
    long addition;
    public Lightning(Base me, long count) {
        super(me, count);
        long l = count / 1000;
        if(l > 15) l = 15;
        base = 2000 * l + 10000;
        Double pow = Math.pow(2, l);
        addition = 70000 + pow.longValue();
    }

    @Override
    public String getName() {
        return "闪电";
    }

    @Override
    public long getHarm(Base target) {
        long l = me.getAtk() + base + random.nextLong(base + addition) - target.getDef();
        if(l <= 0) l = me.getLev();
        return l;
    }

    public void setBase(long base) {
        this.base = base;
    }

    public long getBase() {
        return base;
    }

    public void setAddition(long addition) {
        this.addition = addition;
    }

    public long getAddition() {
        return addition;
    }
}
