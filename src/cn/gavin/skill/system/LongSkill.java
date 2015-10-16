package cn.gavin.skill.system;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import cn.gavin.Achievement;
import cn.gavin.Hero;
import cn.gavin.R;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.maze.BattleController;
import cn.gavin.maze.Maze;
import cn.gavin.monster.Monster;
import cn.gavin.skill.Skill;
import cn.gavin.skill.SkillDialog;
import cn.gavin.skill.SkillFactory;
import cn.gavin.skill.expression.DescExpression;
import cn.gavin.skill.expression.EnableExpression;
import cn.gavin.skill.expression.UseExpression;
import cn.gavin.skill.type.AttackSkill;
import cn.gavin.skill.type.DefendSkill;
import cn.gavin.skill.type.PropertySkill;

/**
 * Created by luoyuan on 9/13/15.
 */
public class LongSkill extends  SkillLayout {
    public LongSkill(Context context) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.skill_layout_long, (ViewGroup) this.findViewById(R.id.skill_dialog));
        this.addView(view);
    }

    public void init(SkillDialog dialog){
        Skill skill = SkillFactory.getSkill("龙裔", hero, dialog);
        Button button = (Button) view.findViewById(R.id.long_skill_ly_button);
        skill.setSkillButton(button);
        skill = SkillFactory.getSkill("咆哮", hero, dialog);
        button = (Button) view.findViewById(R.id.long_skill_px_button);
        skill.setSkillButton(button);
    }

    public static Skill getSkill(String name, final Hero hero, final SkillDialog dialog) {
        Skill skill = null;
        if ("龙裔".equalsIgnoreCase(name)) {
            final PropertySkill iskll = new PropertySkill();
            skill = iskll;
            skill.setName("龙裔");
            skill.setHero(hero);
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return  Achievement.dragon.isEnable();
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("我们才是真正的龙的传人，干掉那些丑陋的西方爬虫！击败10000只龙怪后可以激活。<br>");
                    builder.append("被动技能，激活后免疫龙系怪物对你的伤害。");
                    return builder.toString();
                }
            });
            skill.setRelease(new UseExpression() {
                @Override
                public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {

                    return false;
                }

            });
            skill.setLevelUp(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {

                    return true;
                }
            });
            if (!skill.load()) {
                skill.setProbability(6f);
            }
        } else if ("咆哮".equalsIgnoreCase(name)) {
            final DefendSkill iskll = new DefendSkill();
            skill = iskll;
            skill.setName("咆哮");
            skill.setHero(hero);
            skill.setEnableExpression(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    return SkillFactory.getSkill("龙裔",hero, dialog).isActive();
                }
            });
            skill.setDescription(new DescExpression() {
                @Override
                public String buildDescription(Skill skill) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("在敌人攻击的时候用龙威压迫对方。<br>");
                    builder.append("防御技能，在敌人攻击的时候有").append(iskll.getProbability()).append("%的概率吓退敌人,结束战斗。");
                    return builder.toString();
                }
            });
            skill.setRelease(new UseExpression() {
                @Override
                public boolean release(final Hero hero, Monster monster, MainGameActivity context, Skill skill) {
                    String msg= hero.getFormatName() + "释放了" + iskll.getName() + "吓退了" + monster.getFormatName();
                    iskll.addMessage(msg);
                    monster.addBattleSkillDesc(msg);
                    return true;
                }

            });
            skill.setLevelUp(new EnableExpression() {
                @Override
                public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill) {
                    if(skill.getProbability() < 15){
                        skill.setProbability(skill.getProbability() + 1);
                    }
                    return true;
                }
            });
            if (!skill.load()) {
                skill.setProbability(1f);
            }
        }
        return skill;
    }
}
