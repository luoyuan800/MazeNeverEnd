package cn.gavin.skill;

/**
 * Created by luoyuan on 9/14/15.
 */

import cn.gavin.Hero;
import cn.gavin.Maze;
import cn.gavin.activity.MainGameActivity;

public interface Expression {
    public boolean isEnable(Hero hero, Maze maze, MainGameActivity context);
}
