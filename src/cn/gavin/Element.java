package cn.gavin;

/**
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED
 * Created by luoyuan on 10/5/15.
 */
public enum Element {
    金, 水, 木, 火, 土, 无;

    public boolean isReinforce(Element e) {
        return (e == 金 && this == 土) || (e == 木 && this == 水) || (e == 水 && this == 金) || (e == 火 && this == 木) || (e == 土 && this == 火);
    }

    public boolean restriction(Element e) {
        return (e == 金 && this == 火) ||
                (e == 水 && this == 土) ||
                (e == 木 && this == 金) ||
                (e == 火 && this == 水) ||
                (e == 土 && this == 木) ||
                (e == 无 && this != 无);
    }

    public Element getReinforce() {
        switch (this){
            case 金:
                return 土;
            case 水:
                return 金;
            case 土:
                return 火;
            case 火:
                return 木;
            case 木:
                return 水;
            default:
                return 无;
        }
    }

    public Element getRestriction() {
        switch (this){
            case 金:
                return 火;
            case 水:
                return 土;
            case 土:
                return 木;
            case 火:
                return 水;
            case 木:
                return 金;
            default:
                return 火;
        }
    }
}