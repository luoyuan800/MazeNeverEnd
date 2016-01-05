package cn.gavin.story;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cn.gavin.Element;
import cn.gavin.Hero;
import cn.gavin.db.DBHelper;
import cn.gavin.skill.Skill;
import cn.gavin.skill.SkillFactory;
import cn.gavin.skill.type.PropertySkill;
import cn.gavin.utils.StringUtils;

/**
 * Copyright 2016 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 1/5/2016.
 */
public class NPC extends Hero {
    public static final int STORY_NPC = 0;
    public static final int PALACE_NPC = 1;
    public static final int IMAGE_NPC = 2;

    public static void createTable(SQLiteDatabase db){
        db.execSQL("CREATE TABLE npc(" +
                "uuid TEXT NOT NULL PRIMARY KEY," +
                "name TEXT ," +
                "atk NUMBER," +
                "hp NUMBER," +
                "def NUMBER," +
                "desc TEXT," +
                "element TEXT," +
                "skill TEXT," +
                "p_skill TEXT," +
                "defeat INTEGER," +
                "acc TEXT," +
                "ach TEXT," +
                "lev NUMBER" +
                ")");
        db.execSQL("CREATE UNIQUE INDEX npc_index ON npc (uuid)");
    }

    private String name;
    private Element element;
    private long lev;
    private long atk;
    private long def;
    private long hp;
    private long maxHp;
    private List<Skill> skillList;
    private Skill propertySkill;
    private String desc;
    private boolean defeat;
    private String accName;
    private String achName;


    public NPC() {
        super("");
    }

    public static List<NPC> loadNPCByType(int type) {
        Cursor cursor;
        if (type == -1) {
            cursor = DBHelper.getDbHelper().excuseSOL("select * from npc order by lev");
        } else {
            cursor = DBHelper.getDbHelper().excuseSOL("select * from npc where type = " + type);
        }
        List<NPC> npcs = new ArrayList<NPC>(cursor.getCount());
        while (!cursor.isAfterLast()) {
            NPC npc = buildNpc(cursor);
            npcs.add(npc);
            cursor.moveToNext();
        }
        cursor.close();
        return npcs;
    }

    private static NPC buildNpc(Cursor cursor) {
        NPC npc = new NPC();
        npc.setHp(cursor.getLong(cursor.getColumnIndex("hp")));
        npc.setAtk(cursor.getLong(cursor.getColumnIndex("atk")));
        npc.setDef(cursor.getLong(cursor.getColumnIndex("def")));
        npc.setSkill(cursor.getString(cursor.getColumnIndex("skill")));
        npc.setAccName(cursor.getString(cursor.getColumnIndex("acc")));
        npc.setAchName(cursor.getString(cursor.getColumnIndex("ach")));
        npc.setDesc(cursor.getString(cursor.getColumnIndex("desc")));
        npc.setLev(cursor.getLong(cursor.getColumnIndex("lev")));
        npc.setPropertySkill(cursor.getString(cursor.getColumnIndex("p_skill")));
        npc.setUuid(cursor.getString(cursor.getColumnIndex("uuid")));
        npc.setDefeat(1 == cursor.getInt(cursor.getColumnIndex("defeat")));
        return npc;
    }

    public String getSimpleHTMLDesc() {
        return lev + " - <font color=\"red\">" + name + "</font> (" + element + ")<br>&nbsp;&nbsp;" + desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(String element) {
        try {
            this.element = Element.valueOf(element);
        } catch (Exception e) {
            this.element = Element.无;
        }
    }

    public long getLev() {
        return lev;
    }

    public void setLev(long lev) {
        this.lev = lev;
    }

    public long getAtk() {
        return atk;
    }

    public void setAtk(long atk) {
        this.atk = atk;
    }

    public long getDef() {
        return def;
    }

    public void setDef(long def) {
        this.def = def;
    }

    public Long getHp() {
        return hp;
    }

    public void setHp(long hp) {
        this.hp = hp;
        this.maxHp = hp;
    }

    public void addHp(long hp){
        this.hp += hp;
    }

    public long getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(long maxHp) {
        this.maxHp = maxHp;
    }

    public List<Skill> getSkillList() {
        return skillList;
    }

    public void setSkill(String skill) {
        String[] skillNames = StringUtils.split(skill, "_");
        skillList = new ArrayList<Skill>(skillNames.length);
        for (String name : skillNames) {
            Skill s = SkillFactory.getSkill(name, this);
            if (s != null) {
                skillList.add(s);
            }
        }
    }

    public void setSkillList(List<Skill> skillList) {
        this.skillList = skillList;
    }

    public Skill getPropertySkill() {
        return propertySkill;
    }

    public void setPropertySkill(String propertySkill) {
        this.propertySkill = SkillFactory.getSkill(propertySkill, this);
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isDefeat() {
        return defeat;
    }

    public void setDefeat(boolean defeat) {
        this.defeat = defeat;
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public String getAchName() {
        return achName;
    }

    public void setAchName(String achName) {
        this.achName = achName;
    }
}
