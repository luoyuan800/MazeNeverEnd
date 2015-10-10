package cn.gavin.skill.type;

import android.database.Cursor;
import android.util.Log;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.db.DBHelper;
import cn.gavin.skill.Skill;

/**
 * Created by luoyuan on 9/13/15.
 */
public class AttackSkill extends Skill {

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
        helper.excuseSQLWithoutResult(sql);
    }

    @Override
    public boolean load() {
        DBHelper helper = DBHelper.getDbHelper();
        String sql = String.format("select * from skill where name='%s'",
                getName());
        try {
            Cursor cursor = helper.excuseSOL(sql);
            if (!cursor.isAfterLast()) {
                setOnUsed(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("is_on_use"))));
                active = (Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("is_active"))));
                setProbability(Float.parseFloat(cursor.getString(cursor.getColumnIndex("probability"))));
                count = (Long.parseLong(cursor.getString(cursor.getColumnIndex("count"))));
                baseHarm = Long.parseLong(cursor.getString(cursor.getColumnIndex("base_harm")));
                additionHarm = Long.parseLong(cursor.getString(cursor.getColumnIndex("addition_harm")));
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
