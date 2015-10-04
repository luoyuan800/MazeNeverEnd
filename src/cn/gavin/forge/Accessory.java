package cn.gavin.forge;

import cn.gavin.forge.effect.Effect;

import java.util.List;
import java.util.Map;

/**
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED
 * Created by luoyuan on 10/4/15.
 */
public class Accessory extends Equipment {
    private float pro;
    private String name;
    private Map<Effect, Number> effects;
    private String color;
    private List<Item> items;
    private boolean save;
    private Element element;

    public float getPro() {
        return pro;
    }

    public void setPro(float pro) {
        this.pro = pro;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public boolean isSave() {
        return save;
    }

    public void setSave(boolean save) {
        this.save = save;
    }

    public Map<Effect, Number> getEffects() {
        return effects;
    }

    public void setEffects(Map<Effect, Number> effects) {
        this.effects = effects;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }
}
