package cn.gavin.skill;

import android.widget.Button;
import cn.gavin.Hero;
import cn.gavin.Maze;
import cn.gavin.R;

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
    private String description;
    private Expression expression;
    private Maze maze;

    public boolean isEnable() {
        return expression.isEnable(hero, maze, null);
    }
public void setEnableExpression(Expression exp){
    expression = exp;
}
    public boolean isOnUsed() {
        return onUsed;
    }

    public void setOnUsed(boolean onUsed) {
        this.onUsed = onUsed;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
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

    public abstract String description();

    public void setDescription(String description) {
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
}
