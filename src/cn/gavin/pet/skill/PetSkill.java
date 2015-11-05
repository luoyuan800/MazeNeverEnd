package cn.gavin.pet.skill;

import cn.gavin.Hero;
import cn.gavin.palace.Base;
import cn.gavin.palace.nskill.NSkill;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 11/5/2015.
 */
public abstract class PetSkill extends NSkill {
    @Override
    public boolean release(Base target, long harm) {
        return false;
    }

    @Override
    public long getHarm(Base target) {
        return 0;
    }

    public abstract void release(Hero hero);
}
