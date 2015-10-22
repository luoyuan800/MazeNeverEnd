package cn.gavin.palace.nskill;

import cn.gavin.BaseObject;
import cn.gavin.activity.BaseContext;
import cn.gavin.palace.Base;
import cn.gavin.utils.Random;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 10/15/2015.
 */
public class HitSkill extends AtkSkill {

    public HitSkill(Base me, long count) {
        super(me, count);
        setRate(20f);
    }

    public boolean perform() {
        return random.nextInt(100) < 20;
    }

    @Override
    public boolean release(Base target, long harm) {
        return false;
    }

    @Override
    public String getName() {
        return "<重击>";
    }

    @Override
    public long getHarm(Base target) {
        return me.getAtk() * me.getRandom().nextLong((count / 1000) * 2 + 1) - target.getDef();
    }


}
