package cn.gavin.palace.nskill;

import cn.gavin.palace.Base;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 10/22/2015.
 */
public class SwindleGame extends AtkSkill {
    public SwindleGame(Base me, long count) {
        super(me, count);
    }

    @Override
    public String getName() {
        return "欺诈游戏";
    }

    @Override
    public long getHarm(Base target) {
        long n = random.nextLong(count/1000 + 2);
        if(random.nextBoolean()){
            me.addMessage(me.getFormatName() + "抛了一次硬币，结果为正面");
            me.addMessage(me.getFormatName() + "攻击" + target.getFormatName());
            long l = me.getAtk() - target.getDef();
            if(l <= 0) l = me.getLev();
            long l1 = n * l;
            if(l1 <= 0) l1 = 1l;
            return l1;
        }else{
            me.addMessage(me.getFormatName() + "抛了一次硬币，结果为反面");
            me.addMessage(target.getFormatName() + "攻击" + me.getFormatName());
            long l = target.getAtk() - me.getDef();
            if(l <= 0) l = target.getLev();
            long l1 = n * l;
            if(l1 <= 0) l1 = 1l;
            return -l1;
        }
    }
}
