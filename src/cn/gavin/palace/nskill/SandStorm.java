package cn.gavin.palace.nskill;

import cn.gavin.Element;
import cn.gavin.palace.Base;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 10/22/2015.
 */
public class SandStorm extends AtkSkill {
    public SandStorm(Base me, long count) {
        super(me, count);
    }

    @Override
    public String getName() {
        return "沙尘";
    }

    @Override
    public long getHarm(Base target) {
        long harm = me.getAtk();
        if(me.getElement().isReinforce(Element.土)){
            harm *= 2;
        }else if(me.getElement().restriction(Element.土)){
            harm *= 0.8;
        }
        return harm - target.getDef();
    }

    @Override
    public Element getElement(){
        return Element.土;
    }
}
