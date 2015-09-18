package cn.gavin.skill;

import android.database.Cursor;
import android.widget.Button;

import cn.gavin.Hero;
import cn.gavin.Maze;
import cn.gavin.R;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.db.DBHelper;
import cn.gavin.monster.Monster;
import cn.gavin.skill.expression.DescExpression;
import cn.gavin.skill.expression.EnableExpression;
import cn.gavin.skill.expression.UseExpression;

/**
 * luoyuan on 9/12/15.
 */
public abstract class Skill {
    protected long count;
    private String name;
    protected boolean active;
    private boolean onUsed;
    private Button skillButton;
    private Hero hero;
    private SkillDialog skillDialog;
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
        return enableExpression.isEnable(hero, maze, MainGameActivity.context, this);
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
        this.onUsed = onUsed;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        if (!this.active && active) {
            hero.setSkillPoint(hero.getSkillPoint() - 1);
        }
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
        return String.format("<font color=\"red\">%s</font>(使用/点击次数:%s)<br>%s<br>长按%s", name, count, description(),
                !isActive() ? "激活" : isOnUsed() ? "卸下" : "装备");
    }

    public void setSkillButton(Button button) {
        skillButton = button;
        refresh();
    }

    public void refresh() {
        if (skillButton != null) {
            if(!skillButton.hasOnClickListeners()) {
                skillButton.setOnClickListener(skillDialog.getClickListener(this));
                skillButton.setOnLongClickListener(skillDialog.getLongClickListener(this));
            }
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

    public SkillDialog getSkillDialog() {
        return skillDialog;
    }

    public void setSkillDialog(SkillDialog skillDialog) {
        this.skillDialog = skillDialog;
    }

    public float getProbability() {
        return probability;
    }

    public void setProbability(float probability) {
        this.probability = probability;
    }

    private int diejia(int num) {
        String str = String.valueOf(num);
        int result = 0;
        for (int i = 0; i < str.length(); i++) {
            result += Integer.parseInt(str.charAt(i) + "");
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

    public void addCount() {
        this.count++;
        if (count % 1000 == 0) {
            levelUp();
        }
        if (count % 3000 == 0) {
            hero.setSkillPoint(hero.getSkillPoint() + 1);
        }
    }

    protected void levelUp() {
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
        helper.excuseSQLWithoutResult(sql);
    }

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
            count = (Long.parseLong(cursor.getString(cursor.getColumnIndex("count"))));
            return true;
        }
        return false;
    }


    public EnableExpression getLevelUp() {
        return levelUp;
    }

    public void setLevelUp(EnableExpression levelUp) {
        this.levelUp = levelUp;
    }
}
