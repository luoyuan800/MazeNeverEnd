package cn.gavin.nskill;

import cn.gavin.BaseObject;
import cn.gavin.activity.BaseContext;
import cn.gavin.utils.Random;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 10/15/2015.
 */
public class HitSkill extends AtkSkill {
    private BaseObject me;
    private int count;
    private Random random;

    public HitSkill(BaseObject me, int count) {
        this.me = me;
        this.count = count;
        random = new Random();
    }

    @Override
    public boolean perform() {
        return random.nextInt(100) < 25;
    }

    public boolean release(final BaseObject target, BaseContext
            context) {
        long n = random.nextInt(count) + 1;
        long harm = n * me.getAttackValue();
        target.addHp(-harm);
        String msg = me.getFormatName() + "使用了技能《重击》" + "，对" + target.getFormatName() + "造成了" + harm + "伤害";
        context.addMessage(msg);
        return false;
    }

}
