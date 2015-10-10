package cn.gavin.monster;

import android.database.Cursor;
import cn.gavin.db.DBHelper;
import cn.gavin.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED
 * Created by luoyuan on 10/7/15.
 */
public class MonsterItem {
    private String name;
    private String maxATKName;
    private String maxHPName;
    private String maxATKATK;
    private String maxATKHP;
    private boolean isMaxATKDefeat;
    private String maxATKDesc;
    private String maxHPATK;
    private String maxHPHP;
    private String maxHPDesc;
    private String maxHPLev;
    private String maxATKLev;
    private boolean isMaxHPDefeat;
    private long defeat;
    private long defeated;
    private boolean load;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMaxATKName() {
        return maxATKName;
    }

    public void setMaxATKName(String maxATKName) {
        this.maxATKName = maxATKName;
    }

    public String getMaxHPName() {
        return maxHPName;
    }

    public void setMaxHPName(String maxHPName) {
        this.maxHPName = maxHPName;
    }

    public String getMaxATKATK() {
        return maxATKATK;
    }

    public void setMaxATKATK(String maxATKATK) {
        this.maxATKATK = maxATKATK;
    }

    public String getMaxATKHP() {
        return maxATKHP;
    }

    public void setMaxATKHP(String maxATKHP) {
        this.maxATKHP = maxATKHP;
    }

    public boolean isMaxATKDefeat() {
        return isMaxATKDefeat;
    }

    public void setMaxATKDefeat(boolean maxATKDefeat) {
        isMaxATKDefeat = maxATKDefeat;
    }

    public String getMaxATKDesc() {
        return maxATKDesc;
    }

    public void setMaxATKDesc(String maxATKDesc) {
        this.maxATKDesc = maxATKDesc;
    }

    public String getMaxHPATK() {
        return maxHPATK;
    }

    public void setMaxHPATK(String maxHPATK) {
        this.maxHPATK = maxHPATK;
    }

    public String getMaxHPHP() {
        return maxHPHP;
    }

    public void setMaxHPHP(String maxHPHP) {
        this.maxHPHP = maxHPHP;
    }

    public String getMaxHPDesc() {
        return maxHPDesc;
    }

    public void setMaxHPDesc(String maxHPDesc) {
        this.maxHPDesc = maxHPDesc;
    }

    public String getMaxHPLev() {
        return maxHPLev;
    }

    public void setMaxHPLev(String maxHPLev) {
        this.maxHPLev = maxHPLev;
    }

    public String getMaxATKLev() {
        return maxATKLev;
    }

    public void setMaxATKLev(String maxATKLev) {
        this.maxATKLev = maxATKLev;
    }

    public boolean isMaxHPDefeat() {
        return isMaxHPDefeat;
    }

    public void setMaxHPDefeat(boolean maxHPDefeat) {
        isMaxHPDefeat = maxHPDefeat;
    }

    public long getDefeated() {
        return defeated;
    }

    public void setDefeated(long defeated) {
        this.defeated = defeated;
    }

    public long getDefeat() {
        return defeat;
    }

    public void setDefeat(long defeat) {
        this.defeat = defeat;
    }

    public boolean isLoad() {
        return load;
    }

    public void setLoad(boolean load) {
        this.load = load;
    }

    public void load() {
        DBHelper dbHelper = DBHelper.getDbHelper();
        Cursor cursor = dbHelper.excuseSOL("SELECT * FROM monster WHERE name = '" + name + "'");
        if (!cursor.isAfterLast()) {
            String maxHpName = cursor.getString(cursor.getColumnIndex("max_hp_name"));
            setMaxHPName(StringUtils.isNotEmpty(maxHpName) && !"null".equalsIgnoreCase(maxHpName) ? maxHpName : "");
            String maxHpAtk = cursor.getString(cursor.getColumnIndex("max_hp_atk"));
            setMaxHPATK(StringUtils.isNotEmpty(maxHpAtk) && !"null".equalsIgnoreCase(maxHpAtk) ? maxHpAtk : "");
            String maxHpHp = cursor.getString(cursor.getColumnIndex("max_hp_hp"));
            setMaxHPHP(StringUtils.isNotEmpty(maxHpHp) && !"null".equalsIgnoreCase(maxHpHp) ? maxHpHp : "");
            String maxHpDefeat = cursor.getString(cursor.getColumnIndex("max_hp_defeat"));
            setMaxHPDefeat(StringUtils.isNotEmpty(maxHpDefeat) && !"null".equalsIgnoreCase(maxHpDefeat) ? Boolean.valueOf(maxHpDefeat) : false);
            String maxHpLev = cursor.getString(cursor.getColumnIndex("max_hp_lev"));
            setMaxHPLev(StringUtils.isNotEmpty(maxHpLev) && !"null".equalsIgnoreCase(maxHpLev) ? maxHpLev : "");
            String maxAtkName = cursor.getString(cursor.getColumnIndex("max_atk_name"));
            setMaxATKName(StringUtils.isNotEmpty(maxAtkName) && !"null".equalsIgnoreCase(maxAtkName) ? maxAtkName : "");
            String maxAtkAtk = cursor.getString(cursor.getColumnIndex("max_atk_atk"));
            setMaxATKATK(StringUtils.isNotEmpty(maxAtkAtk) && !"null".equalsIgnoreCase(maxAtkAtk) ? maxAtkAtk : "");
            String maxAtkHp = cursor.getString(cursor.getColumnIndex("max_atk_hp"));
            setMaxATKHP(StringUtils.isNotEmpty(maxAtkHp) && !"null".equalsIgnoreCase(maxAtkHp) ? maxAtkHp : "");
            String maxAtkDefeat = cursor.getString(cursor.getColumnIndex("max_atk_defeat"));
            setMaxATKDefeat(StringUtils.isNotEmpty(maxAtkDefeat) && !"null".equalsIgnoreCase(maxAtkDefeat) ? Boolean.valueOf(maxAtkDefeat) : false);
            String maxAtkLev = cursor.getString(cursor.getColumnIndex("max_atk_lev"));
            setMaxATKLev(StringUtils.isNotEmpty(maxAtkLev) && !"null".equalsIgnoreCase(maxAtkLev) ? maxAtkLev : "");
            String maxHPDesc = cursor.getString(cursor.getColumnIndex("max_hp_battle"));
            setMaxHPDesc(StringUtils.isNotEmpty(maxHPDesc) && !"null".equalsIgnoreCase(maxHPDesc) ? maxHPDesc : "");
            String maxAtkDesc = cursor.getString(cursor.getColumnIndex("max_atk_battle"));
            setMaxATKDesc(StringUtils.isNotEmpty(maxAtkDesc) && !"null".equalsIgnoreCase(maxAtkDesc) ? maxAtkDesc : "");
            long defeat1 = cursor.getLong(cursor.getColumnIndex("defeat"));
            setDefeat(defeat1);
            long defeated1 = cursor.getLong(cursor.getColumnIndex("defeated"));
            setDefeated(defeated1);
            setLoad(true);
        } else {
            setLoad(false);
        }
        cursor.close();
    }

    public static List<MonsterItem> loadMonsterItems() {
        DBHelper dbHelper = DBHelper.getDbHelper();
        Cursor cursor = dbHelper.excuseSOL("SELECT * FROM monster");
        List<MonsterItem> list = new ArrayList<MonsterItem>(cursor.getCount());
        while (!cursor.isAfterLast()) {
            MonsterItem item = new MonsterItem();
            item.setName(cursor.getString(cursor.getColumnIndex("name")));
            String maxHpName = cursor.getString(cursor.getColumnIndex("max_hp_name"));
            item.setMaxHPName(StringUtils.isNotEmpty(maxHpName) && !"null".equalsIgnoreCase(maxHpName) ? maxHpName : "");
            String maxHpAtk = cursor.getString(cursor.getColumnIndex("max_hp_atk"));
            item.setMaxHPATK(StringUtils.isNotEmpty(maxHpAtk) && !"null".equalsIgnoreCase(maxHpAtk) ? maxHpAtk : "");
            String maxHpHp = cursor.getString(cursor.getColumnIndex("max_hp_hp"));
            item.setMaxHPHP(StringUtils.isNotEmpty(maxHpHp) && !"null".equalsIgnoreCase(maxHpHp) ? maxHpHp : "");
            String maxHpDefeat = cursor.getString(cursor.getColumnIndex("max_hp_defeat"));
            item.setMaxHPDefeat(StringUtils.isNotEmpty(maxHpDefeat) && !"null".equalsIgnoreCase(maxHpDefeat) ? Boolean.valueOf(maxHpDefeat) : false);
            String maxHpLev = cursor.getString(cursor.getColumnIndex("max_hp_lev"));
            item.setMaxHPLev(StringUtils.isNotEmpty(maxHpLev) && !"null".equalsIgnoreCase(maxHpLev) ? maxHpLev : "");
            String maxAtkName = cursor.getString(cursor.getColumnIndex("max_atk_name"));
            item.setMaxATKName(StringUtils.isNotEmpty(maxAtkName) && !"null".equalsIgnoreCase(maxAtkName) ? maxAtkName : "");
            String maxAtkAtk = cursor.getString(cursor.getColumnIndex("max_atk_atk"));
            item.setMaxATKATK(StringUtils.isNotEmpty(maxAtkAtk) && !"null".equalsIgnoreCase(maxAtkAtk) ? maxAtkAtk : "");
            String maxAtkHp = cursor.getString(cursor.getColumnIndex("max_atk_hp"));
            item.setMaxATKHP(StringUtils.isNotEmpty(maxAtkHp) && !"null".equalsIgnoreCase(maxAtkHp) ? maxAtkHp : "");
            String maxAtkDefeat = cursor.getString(cursor.getColumnIndex("max_atk_defeat"));
            item.setMaxATKDefeat(StringUtils.isNotEmpty(maxAtkDefeat) && !"null".equalsIgnoreCase(maxAtkDefeat) ? Boolean.valueOf(maxAtkDefeat) : false);
            String maxAtkLev = cursor.getString(cursor.getColumnIndex("max_atk_lev"));
            item.setMaxATKLev(StringUtils.isNotEmpty(maxAtkLev) && !"null".equalsIgnoreCase(maxAtkLev) ? maxAtkLev : "");
            String maxHPDesc = cursor.getString(cursor.getColumnIndex("max_hp_battle"));
            item.setMaxHPDesc(StringUtils.isNotEmpty(maxHPDesc) && !"null".equalsIgnoreCase(maxHPDesc) ? maxHPDesc : "");
            String maxAtkDesc = cursor.getString(cursor.getColumnIndex("max_atk_battle"));
            item.setMaxATKDesc(StringUtils.isNotEmpty(maxAtkDesc) && !"null".equalsIgnoreCase(maxAtkDesc) ? maxAtkDesc : "");
            long defeat1 = cursor.getLong(cursor.getColumnIndex("defeat"));
            item.setDefeat(defeat1);
            long defeated1 = cursor.getLong(cursor.getColumnIndex("defeated"));
            item.setDefeated(defeated1);
            item.setLoad(true);
            list.add(item);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public void save() {
        String baseSql = String.format("replace into monster (name, max_hp_name, max_atk_name, max_hp_defeat, max_atk_defeat,max_atk_atk,max_hp_atk" +
                ",max_hp_hp,max_atk_hp, max_atk_lev,max_hp_lev,max_atk_battle,max_hp_battle,defeat,defeated) values" +
                "('%s', '%s', '%s','%s', '%s', '%s', '%s','%s','%s','%s','%s','%s','%s','%s','%s')",
                name, maxHPName, maxATKName, isMaxHPDefeat, isMaxATKDefeat, maxATKATK, maxHPATK, maxHPHP,
                maxATKHP, maxATKLev, maxHPLev, maxATKDesc, maxHPDesc, defeat, defeated);
        DBHelper.getDbHelper().excuseSQLWithoutResult(baseSql);
    }
}
