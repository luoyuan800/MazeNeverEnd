package cn.gavin.palace.nskill;

import cn.gavin.palace.Base;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 10/23/2015.
 */
public class OriginalPower extends DefSkill {
    public OriginalPower(Base me, long count) {
        super(me, count);
    }

    @Override
    public boolean release(Base target, long harm) {
        me.addMessage(me.getFormatName() + "恢复了全部生命值");
        me.setHp(me.getUHp());
        return false;
    }

    @Override
    public String getName() {
        return null;
    }
}
