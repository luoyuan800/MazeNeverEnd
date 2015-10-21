package cn.gavin.nskill;

import cn.gavin.BaseObject;
import cn.gavin.activity.BaseContext;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 10/21/2015.
 */
public interface NSkill {
    boolean perform();
    boolean release(final BaseObject target, BaseContext
            context);
}
