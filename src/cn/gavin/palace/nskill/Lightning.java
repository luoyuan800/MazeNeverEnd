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
        base = 2000 * (count/1000) + 10000;
        Double pow = Math.pow(2, count / 1000);
        addition = 70000 + pow.longValue();
    }

    @Override
    public String getName() {
        return "闪电";
    }

    @Override
    public long getHarm(Base target) {
        return me.getAtk() + base + random.nextLong(base + addition) - target.getDef();
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
