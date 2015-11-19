package cn.gavin.palace.nskill;

import cn.gavin.palace.Base;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 10/22/2015.
 */
public class SwallowSkill extends DefSkill {
    public SwallowSkill(Base me, long count) {
        super(me, count);
    }

    @Override
    public boolean release(Base target, long harm) {
        me.addMessage("抵消了攻击伤害");
        if(random.nextBoolean()){
            me.addMessage(me.getFormatName() + "抛了一次硬币，结果为正面");
            me.addMessage(me.getFormatName() + "攻击" + target.getFormatName());
            harm = me.getAtk() - target.getDef();
            me.normalAtk(target, harm, me.getElement());
        }else{
            me.addMessage(me.getFormatName() + "抛了一次硬币，结果为反面");
        }
        return true;
    }

    @Override
    public String getName() {
        return "虚无吞噬";
    }
}
