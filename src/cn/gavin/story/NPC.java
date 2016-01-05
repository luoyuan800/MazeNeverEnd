package cn.gavin.story;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import cn.gavin.Element;
import cn.gavin.db.DBHelper;
import cn.gavin.skill.Skill;
import cn.gavin.skill.type.PropertySkill;

/**
 * Copyright 2016 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 1/5/2016.
 */
public class NPC {
    public static final int STORY_NPC = 0, PALACE_NPC = 1, IMAGE_NPC = 2;
    private String name;
    private Element element;
    private long lev;
    private long atk;
    private long def;
    private long hp;
    private long maxHp;
    private List<Skill> skillList;
    private PropertySkill propertySkill;
    private String desc;
    private boolean defeat;
    private String accName;
    private String achName;

    public  static List<NPC> loadNPCByType(int type){
        Cursor cursor;
        if(type == -1){
            cursor = DBHelper.getDbHelper().excuseSOL("select * from npc order by lev");
        }else {
            cursor = DBHelper.getDbHelper().excuseSOL("select * from npc where type = " + type);
        }
        List<NPC> npcs = new ArrayList<NPC>(cursor.getCount());
        while (!cursor.isAfterLast()){
            NPC npc = new NPC();

            cursor.moveToNext();
        }
        cursor.close();
        return npcs;
    }

    public String getSimpleHTMLDesc(){
        return lev + " - <font color=\"red\">" + name + "</font> (" + element + ")<br>&nbsp;&nbsp;" + desc;
    }
}
