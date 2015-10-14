package cn.gavin.activity;

import cn.gavin.Hero;
import cn.gavin.maze.Maze;
import cn.gavin.skill.SkillDialog;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 10/5/2015.
 */
public class MazeContents {
    public static long payTime;
    public static Hero hero;
    public static Maze maze;
    public static SkillDialog skillDialog;
    public static long lastUpload;
    public static Maze getMaze(){
        return maze;
    }

    public static final String PALACE_TABLE_NAME = "uploader";


}
