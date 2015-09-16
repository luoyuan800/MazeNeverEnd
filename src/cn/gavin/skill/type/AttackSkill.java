package cn.gavin.skill.type;

import android.database.Cursor;

import cn.gavin.activity.MainGameActivity;
import cn.gavin.db.DBHelper;
import cn.gavin.skill.Skill;

/**
 * Created by luoyuan on 9/13/15.
 */
public class AttackSkill extends Skill {

    private int baseHarm;
    private int additionHarm;

    @Override
    protected void levelUp() {

    }

    @Override
    public void save() {
        MainGameActivity context = MainGameActivity.context;
        DBHelper helper = context.getDbHelper();
        String checkExistSql =  String.format("select name from skill where name ='%s'", getName());
        Cursor cursor = helper.excuseSOL(checkExistSql);
        String sql;
        if(cursor.isAfterLast()) {
            sql = String.format("insert into skill (name, is_active,is_on_use,probability, count, base_harm, addition_harm) values('%s', '%s', '%s', '%s','%s','%s','%s')",
                    getName(), isActive(), isOnUsed(), getProbability(), getCount(), baseHarm, additionHarm);
        }else{
            sql = String.format("update skill set is_active = '%s', is_on_use = '%s', probability = '%s', count = '%s', base_harm = '%s', addition_harm = '%s' where name = '%s'",
                    isActive(), isOnUsed(), getProbability(), getCount(), baseHarm, additionHarm, getName());
        }
        helper.excuseSQLWithoutResult(sql);
    }

    @Override
    public boolean load() {
        MainGameActivity context = MainGameActivity.context;
        DBHelper helper = context.getDbHelper();
        String sql = String.format("select * from skill where name='%s'",
                getName());
        Cursor cursor = helper.excuseSOL(sql);
        if (!cursor.isAfterLast()) {
            setOnUsed(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("is_on_use"))));
            active = (Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("is_active"))));
            setProbability(Float.parseFloat(cursor.getString(cursor.getColumnIndex("probability"))));
            count= (Long.parseLong(cursor.getString(cursor.getColumnIndex("count")))) ;
            baseHarm = Integer.parseInt(cursor.getString(cursor.getColumnIndex("base_harm")));
            additionHarm = Integer.parseInt(cursor.getString(cursor.getColumnIndex("addition_harm")));
            return  true;
        }
        return false;
    }

    public int getBaseHarm() {
        return baseHarm;
    }

    public int getAdditionHarm() {
        return additionHarm;
    }

}
