package cn.gavin.skill.expression;

/**
 * Created by luoyuan on 9/14/15.
 */

import cn.gavin.Hero;
import cn.gavin.maze.Maze;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.skill.Skill;

public interface EnableExpression {
    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context, Skill skill);
}
