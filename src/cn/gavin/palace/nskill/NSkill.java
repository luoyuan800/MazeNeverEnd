package cn.gavin.palace.nskill;

import cn.gavin.BaseObject;
import cn.gavin.activity.BaseContext;
import cn.gavin.palace.Base;
import cn.gavin.utils.Random;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 10/21/2015.
 */
public abstract class NSkill {
    private float rate = 25;
    private Random random = new Random();
    public boolean perform(){
        return random.nextInt(100) + random.nextFloat() < rate;
    }
    public abstract boolean release(final Base target, long harm);

    public abstract String getName();

    public abstract long getHarm(Base target);

    public static NSkill createSkillByName(String name, Base me, long count){
        if(name.equals("重击")){
            return new HitSkill(me,count);
        }
        return null;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public void setRandom(Random random) {
        this.random = random;
    }
}
