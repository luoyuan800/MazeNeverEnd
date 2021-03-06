package cn.gavin.palace.nskill;

import cn.gavin.palace.Base;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 10/22/2015.
 */
public class HeroHit extends AtkSkill {
    private long base = 158;
    private long addition;
    public HeroHit(Base me, long count) {
        super(me, count);
        long l = count / 1000;
        if(l > 15)l = 15;
        Double a  = Math.pow(2, l) * 85;
        addition = a.longValue();
    }

    @Override
    public String getName() {
        return "勇者之击";
    }

    @Override
    public long getHarm(Base target) {
        long l = random.nextLong(base + addition);
        if(l<=0) l = random.nextLong(addition);
        if(l > me.getAtk() * 10){
            l /= 10;
        }
        long l1 = me.getAtk() + base + l - target.getDef();
        if(l1 <= 0) l1 = me.getLev();
        return l1;
    }

    public long getBase() {
        return base;
    }

    public void setBase(long base) {
        this.base = base;
    }

    public long getAddition() {
        return addition;
    }

    public void setAddition(long addition) {
        this.addition = addition;
    }
}
