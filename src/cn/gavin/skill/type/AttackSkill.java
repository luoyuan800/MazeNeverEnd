package cn.gavin.skill.type;

import cn.gavin.monster.Monster;
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

    public int getBaseHarm() {
        return baseHarm;
    }

    public int getAdditionHarm() {
        return additionHarm;
    }
}
