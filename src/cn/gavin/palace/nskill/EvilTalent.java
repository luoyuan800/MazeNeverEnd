package cn.gavin.palace.nskill;

import cn.gavin.palace.Base;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 10/22/2015.
 */
public class EvilTalent extends DefSkill {
    public EvilTalent(Base me, long count) {
        super(me, count);
    }

    @Override
    public boolean release(Base target, long harm) {
        me.addHp(harm);
        me.addMessage(me.getFormatName() + "将" + harm + "点伤害转换成了HP恢复.");
        return true;
    }

    @Override
    public String getName() {
        return "魔王天赋";
    }
}
