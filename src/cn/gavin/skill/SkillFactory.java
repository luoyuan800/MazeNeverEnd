package cn.gavin.skill;

import android.util.Log;
import android.widget.Button;
import cn.gavin.Hero;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.db.DBHelper;
import cn.gavin.gift.Gift;
import cn.gavin.log.LogHelper;
import cn.gavin.maze.Maze;
import cn.gavin.skill.expression.EnableExpression;
import cn.gavin.skill.system.*;
import cn.gavin.utils.MazeContents;
import cn.gavin.utils.Random;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * by luoyuan
 * on 9/13/15.
 */
public class SkillFactory {
    private static Map<String, Skill> skillMap = new ConcurrentHashMap<String, Skill>();

    public static synchronized Skill getSkill(String name, Hero hero) {
        try {
            Skill skill = skillMap.get(name);
            if (skill == null) {
                skill = BaseSkill.getSkill(name, hero);
                if (skill == null) {
                    skill = EvilSkill.getSkill(name, hero);
                }
                if (skill == null) {
                    skill = SwindlerSkill.getSkill(name, hero);
                }
                if (skill == null) {
                    skill = LongSkill.getSkill(name, hero);
                }
                if (skill == null) {
                    skill = PetSkill.getSkill(name, hero);
                }
                if(skill == null){
                    skill = Elementalist.getSkill(name, hero);
                }
                if (skill != null && hero != null) {
                    skill.setPerform(new EnableExpression() {
                        @Override
                        public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                            Random random = hero.getRandom();
                            long r = skill.diejia(hero.getAgility()) + (hero.getGift() == Gift.SkillMaster ? 10 : 0);
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
            public String getName(){
                return "empty";
            }
        };
    }

    public static synchronized int reset(String name){
        int point = 0;
        Skill skill = getSkill(name, MazeContents.hero);
        if (skill.isActive()) {
            if (skill.isOnUsed()) {
                skill.setOnUsed(false, false);
            }
            skill.setActive(false);
            point++;
        }
        return point;
    }

    public static synchronized long reset() {
        int point = 0;
        for (Skill skill : skillMap.values()) {
            if (skill.isActive()) {
                if (skill.isOnUsed()) {
                    skill.setOnUsed(false, false);
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
