package cn.gavin.palace.nskill;

import cn.gavin.Element;
import cn.gavin.palace.Base;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 10/22/2015.
 */
public class DragonBreath extends AtkSkill {
    public DragonBreath(Base me, long count) {
        super(me, count);
    }

    @Override
    public String getName() {
        return "吐息";
    }

    @Override
    public long getHarm(Base target) {
        long harm = me.getAtk();
        if(me.getElement().restriction(Element.火)){
            harm *= 0.8;
        }
        long l = harm - target.getDef();
        if(l <= 0) l = me.getLev();
        if(Element.火.restriction(target.getElement())){
            l += target.getUHp();
        }
        return l;
    }

    @Override
    public Element getElement(){
        return Element.火;
    }
}
