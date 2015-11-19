package cn.gavin.palace.nskill;

import cn.gavin.palace.Base;
import cn.gavin.utils.Random;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 10/22/2015.
 */
public class MultiSkill extends AtkSkill {
    private long i, j;
    public MultiSkill(Base me, long count) {
        super(me, count);
        i = count/1000 + 2;
        j = count/1000 + 2 + 60;
        setRate(30f);
    }

    @Override
    public String getName() {
        return "多重攻击";
    }

    @Override
    public long getHarm(Base target) {
        long c = random.nextLong(i) +1;
        long l = me.getAtk() - target.getDef();
        if(l <= 0) l = me.getLev();
        Double l1 = l * (j / 100d) * c;
        long harm = l1.longValue();
        me.addMessage(me.getFormatName() + "攻击了" + c + "次");
        return harm;
    }
}
