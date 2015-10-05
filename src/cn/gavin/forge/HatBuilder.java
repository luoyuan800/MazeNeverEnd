package cn.gavin.forge;

import android.database.Cursor;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 9/25/2015.
 */
public class HatBuilder extends Builder {
    public static int type = 0;
    @Override
    public int getType() {
        return 0;
    }

    @Override
    public Cursor queryRecipe() {
        return null;
    }
}
