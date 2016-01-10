package cn.gavin.story;

import android.database.Cursor;
import android.os.Message;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.db.DBHelper;
import cn.gavin.utils.StringUtils;

import java.util.List;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 10/14/2015.
 */
public class NPCPlaceObject extends BmobObject {
    private String name;
    private String atk;
    private String hp;
    private String def;
    private String parry;
    private String hitRate;
    private String skill;
    private String pay;
    private Long lev;
    private String hello;
    private String element;
    private Long reCount;
    private Integer version;
    private String uuid;
    private Integer sort;
    private String award;

    public NPCPlaceObject() {
        setTableName("uploader");
    }

    public static void updatePalace(final MainGameActivity context) {
        BmobQuery<NPCPlaceObject> query = new BmobQuery<NPCPlaceObject>();
        query.setLimit(150);
        query.findObjects(context, new FindListener<NPCPlaceObject>() {
            @Override
            public void onSuccess(final List<NPCPlaceObject> palaceObjects) {
                for (NPCPlaceObject object : palaceObjects) {
                    object.save();
                }
                context.getHandler().sendEmptyMessage(106);
            }

            @Override
            public void onError(int i, String s) {
                Message message = new Message();
                message.what = 106;
                message.obj = s;
                context.getHandler().sendEmptyMessage(106);
            }
        });
    }

    public String getName() {
        if (name.startsWith("0x")) {
            name = StringUtils.toStringHex(name);
        }
        return name;
    }

    public void setName(String name) {
        if (name.startsWith("0x")) {
            name = StringUtils.toStringHex(name);
        }
        this.name = name;
    }

    public String getAtk() {
        return atk;
    }

    public void setAtk(String atk) {
        this.atk = atk;
    }

    public String getHp() {
        return hp;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

    public String getDef() {
        return def;
    }

    public void setDef(String def) {
        this.def = def;
    }

    public String getParry() {
        return parry;
    }

    public void setParry(String parry) {
        this.parry = parry;
    }

    public String getHitRate() {
        return hitRate;
    }

    public void setHitRate(String hitRate) {
        this.hitRate = hitRate;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public Long getLev() {
        return lev;
    }

    public void setLev(Long lev) {
        this.lev = lev;
    }

    public void save() {
        DBHelper dbHelper = DBHelper.getDbHelper();
        Cursor cursor = dbHelper.excuseSOL("select uuid from npc where type = " + NPC.PALACE_NPC + " and lev = " + lev);
        if (!cursor.isAfterLast()) {
            dbHelper.excuseSQLWithoutResult("delete from table where uuid = '" + cursor.getString(0) + "'");
        }
        cursor.close();
        NPC.insertNPC(uuid, name, StringUtils.toLong(atk), StringUtils.toLong(hp), StringUtils.toLong(def),
                StringUtils.toLong(hitRate),
                StringUtils.toFloat(parry), hello, element, skill,
                "", "", "", lev, NPC.PALACE_NPC, null);
    }

    public void setHello(String hello) {
        this.hello = hello;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public Long getReCount() {
        return reCount;
    }

    public void setReCount(Long reCount) {
        this.reCount = reCount;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getAward() {
        return award;
    }

    public void setAward(String award) {
        this.award = award;
    }
}
