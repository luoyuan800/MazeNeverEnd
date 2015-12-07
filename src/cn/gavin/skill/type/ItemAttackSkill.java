package cn.gavin.skill.type;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 12/4/2015.
 */
public class ItemAttackSkill extends AttackSkill {
    public void setOnUsed(boolean onUsed){
        if (!this.onUsed && onUsed) {
            getHero().setItemSkill(this);
        } else if (this.onUsed && !onUsed) {
            getHero().setItemSkill(null);
        }
        this.onUsed = onUsed;
    }
}
