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
        Hero hero2 = new Hero("机器人二号（均衡加点）");
        hero2.setAttackValue(20000);
        hero2.setDefenseValue(9000);
        hero2.setHp(40000);
        Hero hero3 = new Hero("机器人三号（只加攻击）");
        hero3.setAttackValue(60000);
        hero3.setDefenseValue(10);
        hero3.setHp(400);
        Hero hero4 = new Hero("机器人四号（只加HP）");
        hero3.setAttackValue(200);
        hero3.setDefenseValue(10);
        hero3.setHp(40000);
        Maze maze = new Maze();
        int too = 0;
        int min = 0;
        int avg = 0;
        int too2 = 0;
        int min2 = 0;
        int avg2 = 0;
        int too3 = 0;
        int min3 = 0;
        int avg3 = 0;
        int too4 = 0;
        int min4 = 0;
        int avg4 = 0;
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
                }else if(monster.getHp() < hero2.getUpperAtk() * 10){
                    System.out.println("可以对付");
                    avg2 ++;
                }else{
                    min2 ++;
                }
                if(monster. getHp() > hero3.getUpperHp() + hero3.getUpperDef()){
                    too3++;
                }else if(monster.getHp() < hero3.getUpperAtk() * 10){
                    avg3 ++;
                }else{
                    min3 ++;
                }
                if(monster. getHp() > hero4.getUpperHp() + hero4.getUpperDef()){
                    too4++;
                }else if(monster.getHp() < hero4.getUpperAtk() * 10){
                    avg4 ++;
                }else{
                    min4 ++;
                }
                System.out.println("----------------------");
            }
        }

       System.out.println("1000层以内随机遇怪100次循环测试结果：");
        System.out.println(hero.getName() + "\n被秒杀：" + too +",可应付：" + avg+",秒杀：" + min);
        System.out.println(hero2.getName() + "\n被秒杀：" + too2 + ",可应付：" + avg2 + ",秒杀：" + min2);
        System.out.println(hero3.getName() + "\n被秒杀：" + too3 + ",可应付：" + avg3 + ",秒杀：" + min3);
        System.out.println(hero4.getName() + "\n被秒杀：" + too4 + ",可应付：" + avg4 + ",秒杀：" + min4);
    }
}
