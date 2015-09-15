package cn.gavin.skill.type;

import cn.gavin.monster.Monster;
import cn.gavin.skill.Skill;

/**
 * gluo on 9/15/2015.
 */
public class PropertySkill extends Skill {
    private int agi, str, life,clickAward, hp, def, atk;
    public boolean isOnUsed(){
        return isActive();
    }
    public String toString() {
        return String.format("<font color=\"red\">%s</font>使用/点击次数:%s<br>%s<br>属性技能%s", getName(), getCount(), description(),
                !isActive() ? "长按激活" : "已经激活");
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
    
    @Override
    protected void levelUp() {

    }

}
