package cn.gavin.skill.type;

import android.database.Cursor;
import android.util.Log;

import cn.gavin.activity.MainGameActivity;
import cn.gavin.db.DBHelper;
import cn.gavin.skill.Skill;
import cn.gavin.utils.StringUtils;

/**
 * Created by luoyuan on 9/13/15.
 */
public class AttackSkill extends Skill implements LevelAble {

    private long baseHarm;
    private long additionHarm;

    @Override
    public void save() {
        DBHelper helper = DBHelper.getDbHelper();
        String checkExistSql = String.format("select name from skill where name ='%s'", getName());
        Cursor cursor = helper.excuseSOL(checkExistSql);
        String sql;
        if (cursor.isAfterLast()) {
            sql = String.format("insert into skill (name, is_active,is_on_use,probability, count, base_harm, addition_harm) values('%s', '%s', '%s', '%s','%s','%s','%s')",
                    getName(), isActive(), isOnUsed(), getProbability(), getCount(), baseHarm, additionHarm);
        } else {
            sql = String.format("update skill set is_active = '%s', is_on_use = '%s', probability = '%s', count = '%s', base_harm = '%s', addition_harm = '%s' where name = '%s'",
                    isActive(), isOnUsed(), getProbability(), getCount(), baseHarm, additionHarm, getName());
        }
        cursor.close();
        helper.excuseSQLWithoutResult(sql);
    }

    @Override
    public boolean load() {
        DBHelper helper = DBHelper.getDbHelper();
        String sql = String.format("select * from skill where name='%s'",
                getName());
        try {
            boolean load = false;
            Cursor cursor = helper.excuseSOL(sql);
            while (!cursor.isAfterLast()) {
                Long base_harm = StringUtils.toLong(cursor.getString(cursor.getColumnIndex("base_harm")));
                if (baseHarm == 0 || base_harm > baseHarm) {
                    load = true;
                    baseHarm = base_harm;
                    setOnUsed(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("is_on_use"))), true);
                    active = (Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("is_active"))));
                    setProbability(StringUtils.toFloat(cursor.getString(cursor.getColumnIndex("probability"))));
                    count = (StringUtils.toLong(cursor.getString(cursor.getColumnIndex("count"))));
                    additionHarm = StringUtils.toLong(cursor.getString(cursor.getColumnIndex("addition_harm")));
                }
                cursor.moveToNext();
            }
            cursor.close();
            if(load) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(MainGameActivity.TAG, "LoadAttackSkill", e);
        }
        return false;
    }

    public long getBaseHarm() {
        return baseHarm;
    }

    public long getAdditionHarm() {
        return additionHarm;
    }

    public void setBaseHarm(long baseHarm) {
        if (baseHarm < Long.MAX_VALUE - 10000)
            this.baseHarm = baseHarm;
    }

    public void setAdditionHarm(long additionHarm) {
        if (additionHarm < Long.MAX_VALUE - 100000)
            this.additionHarm = additionHarm;
    }
}
