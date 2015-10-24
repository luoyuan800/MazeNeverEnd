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
        return "重击";
    }

    @Override
    public long getHarm(Base target) {
        long l1 = me.getRandom().nextLong((count / 1000) * 2 + 1) + 1;
        long l = me.getAtk() * l1 - target.getDef();
        if(l <= 0) l = me.getLev();
        return l;
    }


}
