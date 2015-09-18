package cn.gavin.skill.type;

import android.view.View;
import cn.gavin.skill.Skill;

/**
 * gluo on 9/15/2015.
 */
public class PropertySkill extends Skill {
    private int agi, str, life,clickAward, hp, def, atk;

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

    public boolean isOnUsed(){
        return isActive();
    }
    public String toString() {
        return String.format("<font color=\"red\">%s</font>使用/点击次数:%s<br>%s<br>属性技能%s", getName(), getCount(), description(),
                !isActive() ? "长按激活" : "已经激活（被动技能激活后无需装备就可以生效）");
    }

    @Override
    public void setActive(boolean active){
        if(!isActive() && active){
            super.setActive(true);
            setOnUsed(true);
        }
    }
    @Override
    public void setOnUsed(boolean used){
        getHero().addAgility(agi);
        getHero().addStrength(str);
        getHero().addLife(life);
        getHero().setUpperHp(getHero().getUpperHp() + hp);
        getHero().addAttackValue(atk);
        getHero().addDefenseValue(def);
        getHero().addClickAward(clickAward);
    }

    public void refresh(){
        super.refresh();
        getSkillButton().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                setActive(true);
                return false;
            }
        });
    }

}
