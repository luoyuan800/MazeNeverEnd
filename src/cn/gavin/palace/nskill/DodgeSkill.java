package cn.gavin.palace.nskill;

import cn.gavin.palace.Base;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 10/22/2015.
 */
public class DodgeSkill extends DefSkill {
    public DodgeSkill(Base me, long count) {
        super(me, count);
        setRate(10f);
    }

    @Override
    public boolean release(Base target, long harm) {
        me.addMessage(me.getFormatName() + "闪避了" + target.getFormatName() + "的攻击");
        return true;
    }

    @Override
    public String getName() {
        return "闪避";
    }
}
