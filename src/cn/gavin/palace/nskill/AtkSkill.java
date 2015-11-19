package cn.gavin.palace.nskill;

import cn.gavin.palace.Base;
import cn.gavin.utils.Random;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 10/21/2015.
 */
public abstract class AtkSkill extends NSkill {

    public AtkSkill(Base me, long count){
        this.me = me;
        this.count = count;
        random = new Random();
        float rate = count/1000 + 2;
        if(rate > 25){
            rate = 25;
        }
        setRate(rate);
    }
    public boolean release(Base target, long harm) {
        return false;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public Random getRandom() {
        return random;
    }

    @Override
    public void setRandom(Random random) {
        this.random = random;
    }

    public Base getMe() {
        return me;
    }

    public void setMe(Base me) {
        this.me = me;
    }
}
