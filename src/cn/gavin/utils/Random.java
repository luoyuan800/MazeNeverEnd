package cn.gavin.utils;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 9/17/2015.
 */
public class Random extends java.util.Random {
    public long nextLong(long num){
        if(num < Integer.MAX_VALUE){
            return nextInt((int)num);
        }else{
            String s = String.valueOf(num);
            int i = s.length() / 2;
            int j = Integer.parseInt(s.substring(0, i));
            int k = Integer.parseInt(s.substring(i, s.length()-1));
            return Long.parseLong(String.format("%s%s",nextInt(j),nextInt(k)));
        }
    }
}
