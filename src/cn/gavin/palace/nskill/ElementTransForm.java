package cn.gavin.palace.nskill;

import cn.gavin.Element;
import cn.gavin.palace.Base;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 10/29/2015.
 */
public class ElementTransForm extends AtkSkill {
    private Element element;
    public ElementTransForm(Base me, long count) {
        super(me, count);
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public long getHarm(Base target) {
        this.element = target.getElement().getRestriction();
        long harm = me.getAtk() - target.getDef();
        if(harm < 0) harm = random.nextLong(target.getLev() + 1);
        return harm;
    }

    public Element getElement(){
        return element;
    }
}
