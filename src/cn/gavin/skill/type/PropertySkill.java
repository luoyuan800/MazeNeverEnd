package cn.gavin.skill.type;

import android.view.View;
import android.widget.Toast;

import cn.gavin.activity.MainGameActivity;
import cn.gavin.skill.Skill;

/**
 * gluo on 9/15/2015.
 */
public class PropertySkill extends Skill {
    private int agi;
    private int str;
    private int life;
    private int clickAward;
    private int hp;
    private int def;
    private int atk;

    public PropertySkill(){

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
        return String.format("<font color=\"red\">%s</font>(使用/点击次数:%s)<br>%s<br>属性技能%s", getName(), getCount(), description(),
                !isActive() ? "长按激活" : "已经激活（被动技能激活后无需装备就可以生效）");
    }

    @Override
    public void setActive(boolean active) {
        if (!isActive() && active) {
            super.setActive(true);
            setOnUsed(true);
        }else if(isActive() && !active){
            setOnUsed(false);
        }
    }

    @Override
    public void setOnUsed(boolean used) {
        if (MainGameActivity.context != null && used) {
            getHero().addAgility(agi);
            getHero().addStrength(str);
            getHero().addLife(life);
            getHero().setUpperHp(getHero().getUpperHp() + hp);
            getHero().addAttackValue(atk);
            getHero().addDefenseValue(def);
            getHero().addClickAward(clickAward);
            Toast.makeText(MainGameActivity.context, "激活" + getName(), Toast.LENGTH_SHORT).show();
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

    public int getAgi() {
        return agi;
    }

    public void setAgi(int agi) {
        this.agi = agi;
    }

    public int getStr() {
        return str;
    }

    public void setStr(int str) {
        this.str = str;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getClickAward() {
        return clickAward;
    }

    public void setClickAward(int clickAward) {
        this.clickAward = clickAward;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getDef() {
        return def;
    }

    public void setDef(int def) {
        this.def = def;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }
}
