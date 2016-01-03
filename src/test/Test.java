package test;

import cn.gavin.Hero;
import cn.gavin.maze.Maze;
import cn.gavin.monster.Monster;

/**
 * Copyright 2016 luoyuan.
 * ALL RIGHTS RESERVED
 * Created by luoyuan on 1/2/16.
 */
public class Test {
    public static void main(String...args){
        Hero hero = new Hero("机器人一号（不加点）");
        hero.setAttackValue(10);
        hero.setDefenseValue(5);
        hero.setHp(20);
        Hero hero2 = new Hero("机器人一号（均衡加点）");
        hero2.setAttackValue(2000);
        hero2.setDefenseValue(1000);
        hero2.setHp(4000);
        Maze maze = new Maze();
        int too = 0;
        int min = 0;
        int avg = 0;
        int too2 = 0;
        int min2 = 0;
        int avg2 = 0;
        for(int i = 1; i<= 1000; i++){
            maze.setLevel(i);
            for(int j =0;j< 100;j++){
                Monster monster = new Monster(hero, maze,true);
                System.out.println("攻击:" + monster.getAtk());
                System.out.println("HP:" + monster.getHp());
                if(monster. getHp() > hero.getUpperHp() + hero.getUpperDef()){
                    System.out.println(hero.getName() + "被秒杀");
                    too++;
                }else if(monster.getHp() < hero.getUpperAtk() * 10){
                    System.out.println("可以对付");
                    avg ++;
                }else{
                    min ++;
                }
                if(monster. getHp() > hero2.getUpperHp() + hero2.getUpperDef()){
                    System.out.println(hero2.getName() + "秒杀");
                    too2++;
                }else if(monster.getHp() < hero.getUpperAtk() * 10){
                    System.out.println("可以对付");
                    avg2 ++;
                }else{
                    min2 ++;
                }
                System.out.println("----------------------");
            }
        }

       System.out.println("测试结果：");
        System.out.println(hero.getName() + "\n被秒杀：" + too +",可应付：" + avg+",一般：" + min);
        System.out.println(hero2.getName() + "\n被秒杀：" + too2 + ",可应付：" + avg2 + ",一般：" + min2);
    }
}
