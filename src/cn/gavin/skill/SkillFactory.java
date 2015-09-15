package cn.gavin.skill;

import java.util.HashMap;
import java.util.Map;

import cn.gavin.Hero;
import cn.gavin.Maze;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.skill.type.AttackSkill;
import cn.gavin.skill.type.DefendSkill;

/**
 * Created by luoyuan on 9/13/15.
 */
public class SkillFactory {
    private static Map<String, Skill> skillMap = new HashMap<String, Skill>();

    public static Skill getSkill(String name, Hero hero) {
        Skill skill = skillMap.get(name);
        if (skill == null) {
            if(name.equals("勇者之击")){
                skill = new AttackSkill();
                skill.setName("勇者之击");
                skill.setHero(hero);
                skill.setEnableExpression(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context) {
                        return hero.getSkillPoint() > 0 && !SkillFactory.getSkill("魔王天赋", hero).isActive();
                    }
                });
                skill.setDescription(new DescExpression() {
                    @Override
                    public String buildDescription(Skill skill) {
                        return null;
                    }
                });
            }else if(name.equals("魔王天赋")){
                skill = new DefendSkill();
                skill.setName("魔王天赋");
                skill.setHero(hero);
                skill.setEnableExpression(new EnableExpression() {
                    @Override
                    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context) {
                        return hero.getSkillPoint() > 0 && !SkillFactory.getSkill("勇者之击", hero).isActive();
                    }
                });
            }
        }
        return skill;
    }

    public static void refreshSkillStatus(){
        for(Skill skill : skillMap.values()){
            skill.refresh();
        }
    }
}
