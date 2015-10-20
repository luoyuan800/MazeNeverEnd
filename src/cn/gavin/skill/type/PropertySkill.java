package cn.gavin.skill.type;

import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import cn.gavin.activity.MainGameActivity;
import cn.gavin.db.DBHelper;
import cn.gavin.skill.Skill;
import cn.gavin.utils.StringUtils;

/**
 * gluo on 9/15/2015.
 */
public class PropertySkill extends Skill {
    private long agi;
    private long str;
    private long life;
    private long clickAward;
    private long hp;
    private long def;
    private long atk;

    public PropertySkill() {

    }

    public PropertySkill(int agi, int str, int life, int clickAward, int hp, int def, int atk) {
        super();
        this.agi = agi;
        this.str = str;
        this.life = life;
        this.clickAward = clickAward;
        this.hp = hp;
        this.def = def;
        this.atk = atk;
    }

    public boolean isOnUsed() {
        return isActive();
    }

    public String toString() {
        return String.format("<font color=\"red\">%s</font><br>%s<br>被动技能%s", getName(), description(),
                !isActive() ? "长按激活" : "已经激活（被动技能激活后无需装备就可以生效）");
    }

    @Override
    public void setActive(boolean active) {
        if (!isActive() && active) {
            super.setActive(true);
            setOnUsed(true);
        } else if (isActive() && !active) {
            setOnUsed(false);
        }
        this.active = active;
        super.refresh();
    }

    @Override
    public void setOnUsed(boolean used) {
        if (MainGameActivity.context != null) {
            if (!onUsed && used) {
                getHero().addAgility(agi);
                getHero().addStrength(str);
                getHero().addLife(life);
                getHero().setUpperHp(getHero().getUpperHp() + hp);
                getHero().addAttackValue(atk);
                getHero().addDefenseValue(def);
                getHero().addClickAward(clickAward);
                Toast.makeText(MainGameActivity.context, "激活" + getName(), Toast.LENGTH_SHORT).show();
            } else if(!used && onUsed){
                getHero().addAgility(-agi);
                getHero().addStrength(-str);
                getHero().addLife(-life);
                getHero().setUpperHp(getHero().getUpperHp() - hp);
                getHero().addAttackValue(-atk);
                getHero().addDefenseValue(-def);
                getHero().addClickAward(-clickAward);
            }
        }
        this.onUsed = used;
    }

    public void refresh() {
        super.refresh();
        getSkillButton().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (isEnable()) {
                    setActive(true);
                }
                return false;
            }
        });
    }

    public long getAgi() {
        return agi;
    }

    public void setAgi(long agi) {
        this.agi = agi;
    }

    public long getStr() {
        return str;
    }

    public void setStr(long str) {
        this.str = str;
    }

    public long getLife() {
        return life;
    }

    public void setLife(long life) {
        this.life = life;
    }

    public long getClickAward() {
        return clickAward;
    }

    public void setClickAward(long clickAward) {
        this.clickAward = clickAward;
    }

    public long getHp() {
        return hp;
    }

    public void setHp(long hp) {
        this.hp = hp;
    }

    public long getDef() {
        return def;
    }

    public void setDef(long def) {
        this.def = def;
    }

    public long getAtk() {
        return atk;
    }

    public void setAtk(long atk) {
        this.atk = atk;
    }

    public boolean load() {
        try {
            DBHelper helper = DBHelper.getDbHelper();
            String sql = String.format("select * from skill where name='%s'",
                    getName());
            Cursor cursor = helper.excuseSOL(sql);
            if (!cursor.isAfterLast()) {
                onUsed = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("is_on_use")));
                active = (Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("is_active"))));
                setProbability(Float.parseFloat(cursor.getString(cursor.getColumnIndex("probability"))));
                count = (StringUtils.toLong(cursor.getString(cursor.getColumnIndex("count"))));
                return true;
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(MainGameActivity.TAG, "loadSkill", e);
        }
        return false;
    }
}
