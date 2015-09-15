package cn.gavin.skill;

import android.widget.Button;

import cn.gavin.Hero;
import cn.gavin.Maze;
import cn.gavin.R;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.monster.Monster;

/**
 * luoyuan on 9/12/15.
 */
public abstract class Skill {
    private long count;
    private String name;
    private boolean active;
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

    public boolean isEnable() {
        return enableExpression.isEnable(hero, maze, null, null);
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
        return String.format("<font color=\"red\">%s</font>使用/点击次数:%s<br>%s<br>长按%s", name, count, description(),
                !isActive() ? "激活" : isOnUsed() ? "卸下" : "装备");
    }

    public void setSkillButton(Button button) {
        skillButton = button;
        refresh();
    }

    public void refresh() {
        skillButton.setOnClickListener(skillDialog.getClickListener(description()));
        skillButton.setOnLongClickListener(skillDialog.getLongClickListener(this));
        skillButton.setText(count + "");
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
        return perform.isEnable(hero, maze, null, this);
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

    protected abstract void levelUp();

    /**
     * True, 跳过攻击判定
     * False： 继续攻击
     *
     * @param monster
     * @return
     */
    public boolean release(Monster monster) {
        return release.release(hero, monster, MainGameActivity.context);
    }

    public void setRelease(UseExpression release) {
        this.release = release;
    }
}
