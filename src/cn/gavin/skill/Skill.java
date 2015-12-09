package cn.gavin.skill;

import android.database.Cursor;
import android.util.Log;
import android.widget.Button;
import cn.gavin.Hero;
import cn.gavin.R;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.db.DBHelper;
import cn.gavin.maze.Maze;
import cn.gavin.monster.Monster;
import cn.gavin.skill.expression.DescExpression;
import cn.gavin.skill.expression.EnableExpression;
import cn.gavin.skill.expression.UseExpression;
import cn.gavin.utils.StringUtils;

/**
 * luoyuan on 9/12/15.
 */
public abstract class Skill {
    protected long count;
    private String name;
    protected boolean active;
    protected boolean onUsed;
    private Button skillButton;
    private Hero hero;
    private float probability;
    private DescExpression description;
    private EnableExpression enableExpression;
    private Maze maze;
    private EnableExpression perform;
    private UseExpression release;
    private EnableExpression levelUp;

    public Skill() {

    }

    public Skill(String name) {
        load();
    }

    public boolean isEnable() {
        return isActive() || (hero.getSkillPoint() > 0 && enableExpression.isEnable(hero, maze, MainGameActivity.context, this));
    }

    public void setEnableExpression(EnableExpression exp) {
        enableExpression = exp;
    }

    public boolean isOnUsed() {
        return onUsed;
    }

    public void setOnUsed(boolean onUsed) {
        if (!this.onUsed && onUsed) {
            hero.addSkill(this);
        } else if (this.onUsed && !onUsed) {
            hero.removeSkill(this);
        }
        save();
        this.onUsed = onUsed;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        if (!this.active && active) {
            hero.setSkillPoint(hero.getSkillPoint() - 1);
        }
        save();
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String description() {
        return description.buildDescription(this);
    }

    public void setDescription(DescExpression description) {
        this.description = description;
    }

    public String toString() {
        return String.format("<font color=\"red\">%s</font>(使用/点击次数:%s)<br>%s", name, count, description()
                );
    }

    public void setSkillButton(Button button) {
        skillButton = button;
        refresh();
    }

    public void refresh() {
        if (skillButton != null) {

            if (!isEnable()) {
                skillButton.setTextColor(skillButton.getResources().getColor(R.color.disable));
            } else {
                if (!active) {
                    skillButton.setTextColor(skillButton.getResources().getColor(R.color.un_active));
                } else if (!onUsed) {
                    skillButton.setTextColor(skillButton.getResources().getColor(R.color.active));
                } else {
                    skillButton.setTextColor(skillButton.getResources().getColor(R.color.onUse));
                }
            }
        }
    }

    public Button getSkillButton() {
        return skillButton;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public void setSkillDialog(SkillDialog skillDialog) {
    }

    public float getProbability() {
        return probability;
    }

    public void setProbability(float probability) {
        this.probability = probability;
    }

    public long diejia(long num) {
        String str = String.valueOf(num);
        long result = 0;
        for (int i = 0; i < str.length(); i++) {
            result += StringUtils.toInt(str.charAt(i) + "");
        }
        if (result > 30) {
            result = hero.getRandom().nextInt(30);
        }
        return result;
    }

    public boolean perform() {
        return perform.isEnable(hero, maze, MainGameActivity.context, this);
    }

    public void setPerform(EnableExpression perform) {
        this.perform = perform;
    }

    public String getDisplayName() {
        return name + "\n" + count;
    }

    private long latestClick = 0;

    public void addCount() {
        if (this.count < Long.MAX_VALUE - 1000) {
            this.count++;
            hero.click(false);
            if (count % 1005 == 0) {
                levelUp();
            }
            if (count % 7000 == 0 && hero.getRandom().nextLong(hero.getSkillPoint()) < 3) {
                hero.setSkillPoint(hero.getSkillPoint() + 1);
            }
        }
    }

    public void levelUp() {
        levelUp.isEnable(hero, maze, MainGameActivity.context, this);
    }

    /**
     * True, 跳过攻击判定
     * False： 继续攻击
     *
     * @param monster
     * @return
     */
    public boolean release(Monster monster) {
        addCount();
        return release.release(hero, monster, MainGameActivity.context, this);
    }

    public void setRelease(UseExpression release) {
        this.release = release;
    }

    public void save() {
        DBHelper helper = DBHelper.getDbHelper();
        String checkExistSql = String.format("select name from skill where name ='%s'", getName());
        Cursor cursor = helper.excuseSOL(checkExistSql);
        String sql;
        if (cursor.isAfterLast()) {
            sql = String.format("insert into skill (name, is_active,is_on_use,probability, count) values('%s', '%s', '%s', '%s','%s')",
                    getName(), isActive(), isOnUsed(), getProbability(), getCount());
        } else {
            sql = String.format("update skill set is_active = '%s', is_on_use = '%s', probability = '%s', count = '%s' where name = '%s'",
                    isActive(), isOnUsed(), getProbability(), getCount(), getName());
        }
        cursor.close();
        helper.excuseSQLWithoutResult(sql);
    }

    public boolean load() {
        try {
            DBHelper helper = DBHelper.getDbHelper();
            String sql = String.format("select * from skill where name='%s'",
                    getName());
            Cursor cursor = helper.excuseSOL(sql);
            if (!cursor.isAfterLast()) {
                setOnUsed(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("is_on_use"))));
                active = (Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("is_active"))));
                setProbability(StringUtils.toFloat(cursor.getString(cursor.getColumnIndex("probability"))));
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


    public EnableExpression getLevelUp() {
        return levelUp;
    }

    public void setLevelUp(EnableExpression levelUp) {
        this.levelUp = levelUp;
    }

    public String format(String desc) {
        return "<i>" + desc + "</i>";
    }

    public void addMessage(String msg) {
        MainGameActivity.context.addMessage(format(msg));
    }

    public boolean equal(Skill skill) {
        return skill != null && name.equalsIgnoreCase(skill.getName());
    }
}
