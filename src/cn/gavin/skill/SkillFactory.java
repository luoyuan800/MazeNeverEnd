package cn.gavin.skill;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by luoyuan on 9/13/15.
 */
public class SkillFactory {
    private static Map<String, Skill> system = new HashMap<String, Skill>();
    public static String HIT = "重击",
            RESTORE = "恢复";

    public static Skill getSkill(String name) {
        Skill skill = system.get(name);
        if (skill == null) {
            if(HIT.equals(name)){
                skill = new AttackSkill();
            }else {
                skill = new RestoreSkill();
            }
                system.put(name, skill);
        }
        return skill;
    }
}
