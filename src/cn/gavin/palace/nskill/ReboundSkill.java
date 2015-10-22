package cn.gavin.palace.nskill;

import cn.gavin.palace.Base;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 10/22/2015.
 */
public class ReboundSkill extends DefSkill {
    public ReboundSkill(Base me, long count) {
        super(me, count);
    }

    @Override
    public boolean release(Base target, long harm) {
        long re = (long)(harm * (20 + getRate() * 10)/100);
        target.addHp(-re);
        me.addMessage("反弹了" + re + "点伤害");
        return true;
    }

    @Override
    public String getName() {
        return "反弹";
    }
}
