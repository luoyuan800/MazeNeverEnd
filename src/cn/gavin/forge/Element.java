package cn.gavin.forge;

/**
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED
 * Created by luoyuan on 10/5/15.
 */
public enum Element {
        金,水,木,火,土,无;
public boolean isReinforce(Element e){
    return e.ordinal() > ordinal() || (e.ordinal() == 5 && ordinal() == 0);
}
}