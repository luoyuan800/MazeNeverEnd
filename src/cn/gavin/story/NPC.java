package cn.gavin.story;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cn.gavin.Achievement;
import cn.gavin.Element;
import cn.gavin.Hero;
import cn.gavin.db.DBHelper;
import cn.gavin.skill.Skill;
import cn.gavin.skill.SkillFactory;
import cn.gavin.skill.type.AttackSkill;
import cn.gavin.skill.type.DefendSkill;
import cn.gavin.utils.MazeContents;
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

    public void insertNPC(){
        String sql = "replace into npc values('%s', '%s', %s, %s, %s, '%s', '%s', '%s', '%s', 0, '%s', '%s', %s)";
    }

    private String name;
    private Element element;
    private long lev;
    private List<Skill> skillList;
    private List<Skill> propertySkillList;
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
        npc.setAttackValue(cursor.getLong(cursor.getColumnIndex("atk")));
        npc.setDefenseValue(cursor.getLong(cursor.getColumnIndex("def")));
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

    public void setHp(long hp) {
        super.setHp(hp);
        super.setUpperHp(hp);
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
                s = s.copy();
                s.setHero(this);
                skillList.add(s);
            }
        }
    }

    public void setSkillList(List<Skill> skillList) {
        this.skillList = skillList;
    }

    public Skill getPropertySkill(String name) {
        for(Skill skill : propertySkillList){
            if(skill.getName().equals(name)){
                return skill;
            }
        }
        return null;
    }

    public void setPropertySkill(String propertySkill) {
        for(String name : StringUtils.split(propertySkill, "_")) {
            Skill skill = SkillFactory.getSkill(name, this);
            if(skill!=null){
                skill = skill.copy();
                skill.setHero(this);
                propertySkillList.add(skill);
            }
        }
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

    public void defeat(){
        if(StringUtils.isNotEmpty(accName)){
            DBHelper.getDbHelper().excuseSOL("UPDATE recipe set found = '" + Boolean.TRUE + "' WHERE name = '" + accName + "'");
        }
        if(StringUtils.isNotEmpty(achName)){
            Achievement.valueOf(achName).enable(MazeContents.hero);
        }
    }

    public void setAchName(String achName) {
        this.achName = achName;
    }

    public Skill useAtkSkill(){
        for(Skill skill : skillList){
            if(skill instanceof AttackSkill && skill.perform()){
                return skill;
            }
        }
        return null;
    }

    public Skill useDefSkill() {
        for(Skill skill : skillList){
            if(skill instanceof DefendSkill && skill.perform()){
                return skill;
            }
        }
        return null;
    }

    public static NPC build(long level) {
        NPC npc = null;
        Cursor cursor = DBHelper.getDbHelper().excuseSOL("select * from npc where lev = " + level + " and found = 0");
        if(!cursor.isAfterLast()){
            npc= buildNpc(cursor);
        }
        cursor.close();
        return npc;
    }
}
