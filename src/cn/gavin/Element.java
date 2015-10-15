package cn.gavin;

/**
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED
 * Created by luoyuan on 10/5/15.
 */
public enum Element {
    金, 水, 木, 火, 土, 无;

    public boolean isReinforce(Element e) {
        return (e == 金 && this == 土) || (e == 木 && this == 水) || (e == 水 && this == 金) || (e == 火 && this == 木) || (e == 土&& this == 火);
    }

    public boolean restriction(Element e){
        return (e == 金 && this == 火) ||
                (e == 水 && this == 土)||
                (e == 木 && this == 金) ||
                (e == 火 && this == 水) ||
                (e == 土 && this == 木);
    }
}