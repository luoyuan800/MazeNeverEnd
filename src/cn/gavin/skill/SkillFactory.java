package cn.gavin.skill;

import android.util.Log;
import android.widget.Button;
import cn.gavin.Hero;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.db.DBHelper;
import cn.gavin.log.LogHelper;
import cn.gavin.maze.Maze;
import cn.gavin.skill.expression.EnableExpression;
import cn.gavin.skill.system.BaseSkill;
import cn.gavin.skill.system.EvilSkill;
import cn.gavin.skill.system.LongSkill;
import cn.gavin.skill.system.SwindlerSkill;
import cn.gavin.utils.Random;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * by luoyuan
 * on 9/13/15.
 */
public class SkillFactory {
    private static Map<String, Skill> skillMap = new ConcurrentHashMap<String, Skill>();

    public static synchronized Skill getSkill(String name, Hero hero, final SkillDialog dialog) {
        try {
            Skill skill = skillMap.get(name);
            if (skill == null) {
                skill = BaseSkill.getSkill(name, hero, dialog);
                if (skill == null){
                    skill = EvilSkill.getSkill(name, hero, dialog);
                }
                if(skill == null){
                    skill = SwindlerSkill.getSkill(name, hero, dialog);
                }
                if(skill == null){
                    skill = LongSkill.getSkill(name,hero,dialog);
                }
                if (skill != null) {
                    skill.setSkillDialog(dialog);
                    skill.setPerform(new EnableExpression() {
                        @Override
                        public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                            Random random = hero.getRandom();
                            long r = skill.diejia(hero.getAgility());
                            return random.nextLong(100) + random.nextFloat() < skill.getProbability() + random.nextLong(r + 1);
                        }
                    });
                    skillMap.put(name, skill);
                } else {
                    skill = emptySkill();
                }
            }
            return skill;
        } catch (Exception e) {
            Log.e(MainGameActivity.TAG, "BuildSkill", e);
            LogHelper.writeLog();
            throw new RuntimeException(e);
        }
    }

    public static void refreshSkillStatus() {
        for (Skill skill : skillMap.values()) {
            skill.refresh();
        }
    }

    public static void save() {
        DBHelper.getDbHelper().beginTransaction();
        for (Skill skill : skillMap.values()) {
            skill.save();
        }
        DBHelper.getDbHelper().endTransaction();
    }

    public static Skill emptySkill() {
        return new Skill() {
            public boolean isActive() {
                return false;
            }

            @Override
            public void save() {

            }

            public void setSkillButton(Button button) {

            }

            @Override
            public boolean load() {
                return false;
            }

            public String toString() {
                return "";
            }
        };
    }

    public static synchronized long reset() {
        int point = 0;
        for (Skill skill : skillMap.values()) {
            if (skill.isActive()) {
                if (skill.isOnUsed()) {
                    skill.setOnUsed(false);
                }
                skill.setActive(false);
                point++;
            }
        }
        return point;
    }

    public static void clean() {
        reset();
        skillMap.clear();
        DBHelper.getDbHelper().excuseSQLWithoutResult("DELETE FROM skill");

    }
}
