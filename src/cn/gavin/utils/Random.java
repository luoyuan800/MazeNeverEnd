package cn.gavin.utils;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 9/17/2015.
 */
public class Random extends java.util.Random {
    public long nextLong(long num) {
        if (num <= 0) {
            return 0;
        }
        if (num < Integer.MAX_VALUE) {
            return nextInt((int) num);
        } else {
            int j;
            long k;
            j = Integer.MAX_VALUE - 1;
            k = (num - Integer.MAX_VALUE);
            return (long) nextInt(j) + nextLong(k);
        }
    }
}
