package cn.gavin.forge;

import android.database.Cursor;
import cn.gavin.db.DBHelper;
import cn.gavin.forge.effect.Effect;
import cn.gavin.utils.StringUtils;

import java.util.*;

/**
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED
 * Created by luoyuan on 10/4/15.
 */
public class Accessory extends Equipment {
    private String id;
    private float pro;
    private String name;
    private Map<Effect, Number> effects;
    private String color;
    private List<Item> items;
    private boolean save;
    private Element element;
    private Map<Effect, Number> additionEffects;
    private int type;

    public int getType() {
        return type;
    }

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

    public Map<Effect, Number> getAdditionEffects() {
        return additionEffects;
    }

    public void setAdditionEffects(Map<Effect, Number> additionEffects) {
        this.additionEffects = additionEffects;
    }

    public void save() {
        DBHelper dbHelper = DBHelper.getDbHelper();
        StringBuilder base = new StringBuilder();
        if (effects != null) {
            for (Effect effect : effects.keySet()) {
                base.append(effect.name()).append(":").append(effects.get(effect)).append("-");
            }
        }
        StringBuilder addition = new StringBuilder();
        if (additionEffects != null) {
            for (Effect effect : additionEffects.keySet()) {
                addition.append(effect.name()).append(":").append(additionEffects.get(effect));
            }
        }
        id = UUID.randomUUID().toString();
        String sql = String.format("INSERT INTO accessory (id, name,base,addition,element,type,color) " +
                "values ('%s', '%s', '%s','%s','%s','%s','%s')", id,
                name, base.toString(), addition.toString(), element.name(), type, color);
        dbHelper.excuseSQLWithoutResult(sql);
        sql = String.format("SELECT name from recipe where name = '%s'", name);
        Cursor cursor = dbHelper.excuseSOL(sql);
        StringBuilder itemBuilder = new StringBuilder();
        for (Item item : items) {
            itemBuilder.append(item.getName().name()).append("-");
        }
        if (save && cursor.isAfterLast()) {
            sql = String.format("INSERT INTO recipe (name,items,base,addition,found,user,type,color) " +
                    "values('%s','%s','%s','%s','%s','%s','%s','%s')",
                    name, itemBuilder.toString().replaceFirst("\\+$", ""), base.toString(), addition.toString(), Boolean.FALSE, Boolean.TRUE, type, color);
        } else if (!cursor.isAfterLast()) {
            sql = String.format("UPDATE recipe set found = '%s' WHERE name = '%s'", Boolean.TRUE, name);
        }
        dbHelper.excuseSQLWithoutResult(sql);
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isActive() {
        return false;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("<font color=\"").append(color).append("\">");
        builder.append(name).append("</font><br>");
        builder.append("属性: ").append(element.name()).append("<br>");
        for (Effect effect : effects.keySet()) {
            builder.append("<br>").append(effect.getName()).append(":").append(effects.get(effect));
        }
        if (additionEffects != null) {
            builder.append("<font color=\"").append(isActive() ? "#B8860B" : "#D3D3D3").append("\">");
            for (Effect effect : additionEffects.keySet()) {
                builder.append("<br>").append(effect.getName()).append(":").append(additionEffects.get(effect));
            }
            builder.append("</font>");
        }
        return builder.toString();
    }

    public static List<Accessory> loadAccessories() {
        Cursor cursor = DBHelper.getDbHelper().excuseSOL("SELECT * FROM accessory");
        List<Accessory> res = new ArrayList<Accessory>(cursor.getCount());
        while (!cursor.isAfterLast()) {
            Accessory accessory = new Accessory();
            accessory.setName(cursor.getString(cursor.getColumnIndex("name")));
            accessory.setElement(Element.valueOf(cursor.getString(cursor.getColumnIndex("element"))));
            accessory.setColor(cursor.getString(cursor.getColumnIndex("color")));
            EnumMap<Effect, Number> base = new EnumMap<Effect, Number>(Effect.class);
            for (String str : StringUtils.split(cursor.getString(cursor.getColumnIndex("base")), "-")) {
                String[] keyValue = StringUtils.split(str, ":");
                if (keyValue.length > 1) {
                    base.put(Effect.valueOf(keyValue[0].trim()), Long.parseLong(keyValue[1]));
                }
            }
            EnumMap<Effect, Number> addition = new EnumMap<Effect, Number>(Effect.class);
            for (String str : StringUtils.split(cursor.getString(cursor.getColumnIndex("addition")), "-")) {
                String[] keyValue = StringUtils.split(str, ":");
                if (keyValue.length > 1) {
                    addition.put(Effect.valueOf(keyValue[0].trim()), Long.parseLong(keyValue[1]));
                }
            }
            accessory.setEffects(base);
            accessory.setAdditionEffects(addition);
            accessory.setType(cursor.getInt(cursor.getColumnIndex("type")));
            accessory.setId(cursor.getString(cursor.getColumnIndex("id")));
            res.add(accessory);
            cursor.moveToNext();
        }
        return res;
    }

    public String getFormatName() {
        return "<font color=\"" + color + "\">" + name + "</font>";
    }

    public boolean load() {
        try {
            if (id != null) {
                Cursor cursor = DBHelper.getDbHelper().excuseSOL("SELECT * FROM accessory WHERE id = '" + id + "'");
                if (!cursor.isAfterLast()) {
                    setName(cursor.getString(cursor.getColumnIndex("name")));
                    setElement(Element.valueOf(cursor.getString(cursor.getColumnIndex("element"))));
                    setColor(cursor.getString(cursor.getColumnIndex("color")));
                    EnumMap<Effect, Number> base = new EnumMap<Effect, Number>(Effect.class);
                    for (String str : StringUtils.split(cursor.getString(cursor.getColumnIndex("base")), "-")) {
                        String[] keyValue = StringUtils.split(str, ":");
                        if (keyValue.length > 1) {
                            base.put(Effect.valueOf(keyValue[0].trim()), Long.parseLong(keyValue[1]));
                        }
                    }
                    EnumMap<Effect, Number> addition = new EnumMap<Effect, Number>(Effect.class);
                    for (String str : StringUtils.split(cursor.getString(cursor.getColumnIndex("addition")), "-")) {
                        String[] keyValue = StringUtils.split(str, ":");
                        if (keyValue.length > 1) {
                            addition.put(Effect.valueOf(keyValue[0].trim()), Long.parseLong(keyValue[1]));
                        }
                    }
                    setEffects(base);
                    setAdditionEffects(addition);
                    setType(cursor.getInt(cursor.getColumnIndex("type")));
                    setId(cursor.getString(cursor.getColumnIndex("id")));
                }
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
