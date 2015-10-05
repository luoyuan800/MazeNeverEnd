package cn.gavin.forge;

import android.database.Cursor;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 9/25/2015.
 */
public class NecklaceBuilder extends Builder {
    public final static int type = 2;
    @Override
    public int getType() {
        return type;
    }

    @Override
    public Cursor queryRecipe() {
        return null;
    }
}
