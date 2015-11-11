package cn.gavin.palace.nskill;

import cn.gavin.Element;
import cn.gavin.palace.Base;
import cn.gavin.pet.skill.GoldenSearcher;
import cn.gavin.pet.skill.HealthSkill;
import cn.gavin.pet.skill.QuickGrow;
import cn.gavin.skill.Skill;
import cn.gavin.skill.type.AttackSkill;
import cn.gavin.utils.Random;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 10/21/2015.
 */
public abstract class NSkill {
    long count;
    Base me;
    private float rate = 25;
    Random random = new Random();
    public boolean perform(){
        return random.nextInt(100) + random.nextFloat() < rate;
    }
    public abstract boolean release(final Base target, long harm);

    public abstract String getName();

    public abstract long getHarm(Base target);

    public static NSkill createSkillByName(String name, Base me, long count, Skill oSkill){
        NSkill skill = null;
        if(name.equals("重击")){
            skill =  new HitSkill(me,count);
        }else if(name.equals("多重攻击")){
            skill = new MultiSkill(me, count);
        }else if(name.equals("闪避")){
            skill = new DodgeSkill(me, count);
        }else if(name.equals("勇者之击")){
            skill = new HeroHit(me, count);
            if(oSkill!=null){
                ((HeroHit)skill).setBase(((AttackSkill)oSkill).getBaseHarm());
                ((HeroHit)skill).setAddition(((AttackSkill) oSkill).getAdditionHarm());
            }
        }else if(name.equals("魔王天赋")){
            skill = new EvilTalent(me, count);
        }else if(name.equals("龙爪")){
            skill = new DragonClaw(me, count);
        }else if(name.equals("吐息")){
            skill = new DragonBreath(me, count);
        }else if(name.equals("沙尘")){
            skill = new SandStorm(me, count);
        }else if(name.equals("欺诈游戏")){
            skill = new SwindleGame(me, count);
        }else if(name.equals("闪电")){
            skill = new Lightning(me, count);
            if(oSkill!=null){
                ((Lightning)skill).setBase(((AttackSkill)oSkill).getBaseHarm());
                ((Lightning)skill).setAddition(((AttackSkill)oSkill).getAdditionHarm());
            }
        }else if(name.equals("反弹")){
            skill = new ReboundSkill(me, count);
        }else if(name.equals("水波")){
            skill = new WaterWave(me, count);
        }else if(name.equals("虚无吞噬")){
            skill = new SwallowSkill(me, count);
        }else if(name.equals("原能力")){
            skill = new OriginalPower(me, count);
        } else if(name.equals("治疗术")){
            skill = new HealthSkill();
        } else if(name.equals("自动获取额外能力点")){
            skill = new QuickGrow();
        } else if(name.equals("自动收集锻造点数")){
            skill = new GoldenSearcher();
        }
        return skill;
    }

    public static NSkill createSkillBySkill(Skill skill, Base hero){
        NSkill nSkill = createSkillByName(skill.getName(),hero, skill.getCount(), null);
        if(nSkill!=null){
            nSkill.setRate(skill.getProbability());
        }
        return nSkill;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    public Element getElement() {
        return me.getElement();
    }

    public long getCount() {
        return count;
    }
}
