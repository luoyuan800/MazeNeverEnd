package cn.gavin.monster.nmonster;

import android.database.sqlite.SQLiteDatabase;
import cn.gavin.R;

/**
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED
 * Created by luoyuan on 12/24/15.
 */
public class MonsterDB {
    public static void createMonsterTable(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("DELETE TABLE IF EXIST monster");
        String createTable = "CREATE TABLE if not exists monster(" +
                "id NUMBER NOT NULL PRIMARY KEY," +
                "type TEXT," +
                "atk TEXT," +
                "hp TEXT," +
                "pet_rate TEXT," +
                "drop_item TEXT," +
                "drop_good TEXT," +
                "beat TEXT," +
                "defeat TEXT," +
                "catch TEXT," +
                "meet_lev TEXT," +
                "catch_lev TEXT," +
                "egg_rate TEXT," +
                "pet_sub TEXT," +
                "des TEXT" +
                "img TEXT" +
                ")";
        sqLiteDatabase.execSQL(createTable);
        sqLiteDatabase.execSQL("CREATE UNIQUE INDEX monster_name_index ON goods (name)");
    }

    public static void addMonster(SQLiteDatabase database) {
        String sql = "replace into monster (" +
                "id, type, atk, hp, pet_rate, drop_item, drop_good, " +
                "beat, defeat, catch, meet_lev, catch_lev, egg_rate, pet_sub, des, img) values(" +
                "%s, '%s', '%s', '%s', '%s', '%s', '%s'," +
                "'0', '0', '0', '0', '0', '%s', '%s', '%s', '%s'";

        database.execSQL(String.format(sql, 1, "蟑螂", 1, 8, 300, "原石", "", 310,0,"",R.drawable.zl));
        database.execSQL(String.format(sql, 2, "大蚯蚓", 5, 20, 300, "硝石", "", 300, 0,"",R.drawable.qy));
        database.execSQL(String.format(sql, 31, "大蚯蚓", 5, 20, 300, "硝石", "", 300, 0,"",R.drawable.qy_red));
        database.execSQL(String.format(sql, 3, "异壳虫", 15, 55, 260, "硝石", "", 300, 0,"",R.drawable.pc));
        database.execSQL(String.format(sql, 4, "巨型飞蛾", 25, 75, 300, "硝石", "", 150, 0,"",R.drawable.feie));
        database.execSQL(String.format(sql, 5, "猪", 35, 95, 260, "硝石", "", 300, 0,"",R.drawable.zz));
        database.execSQL(String.format(sql, 6, "老鼠", 56, 115, 300, "硝石", "", 308, 0,"",R.drawable.laoshu));
        database.execSQL(String.format(sql, 7, "嗜血蚁", 100, 400, 250, "硝石", "", 150, 0,"",R.drawable.mayi));
        database.execSQL(String.format(sql, 33, "嗜血蚁", 100, 400, 250, "硝石", "", 150, 0,"",R.drawable.mayi_red));
        database.execSQL(String.format(sql, 8, "老虎", 120, 1000, 200, "硝石", "", 200, 0,"",R.drawable.laohu));
        database.execSQL(String.format(sql, 34, "老虎", 120, 1000, 200, "硝石", "", 200, 0,"",R.drawable.laohu_red));
        database.execSQL(String.format(sql, 9, "蛟", 305, 800, 150, "硝石", "", 300, 0,"",R.drawable.jiao));
        database.execSQL(String.format(sql, 10, "变异蝎", 40, 520, 300, "硝石", "", 200, 0,"",R.drawable.xiezi));
        database.execSQL(String.format(sql, 35, "变异蝎", 40, 520, 300, "硝石", "", 200, 0,"",R.drawable.xiezi_red));
        database.execSQL(String.format(sql, 11, "食人鸟", 60, 280, 200, "硝石", "", 250, 0,"",R.drawable.srn));
        database.execSQL(String.format(sql, 12, "丑蝙蝠", 350, 20, 253, "硝石", "", 222, 0,"",R.drawable.bianfu));
        database.execSQL(String.format(sql, 36, "丑蝙蝠", 350, 20, 253, "硝石", "", 222, 0,"",R.drawable.bianfu_red));
        database.execSQL(String.format(sql, 13, "蛇", 200, 380, 200, "硝石", "", 240, 0,"",R.drawable.se));
        database.execSQL(String.format(sql, 14, "猴", 210, 310, 300, "硝石", "", 280, 0,"",R.drawable.h_1));
        database.execSQL(String.format(sql, 15, "野牛", 450, 450, 290, "硝石", "", 270, 0,"",R.drawable.niu));
        database.execSQL(String.format(sql, 16, "龟", 500, 5300, 250, "硝石", "", 101, 0,"",R.drawable.wugui));
        database.execSQL(String.format(sql, 17, "三头蛇", 700, 1000, 240, "硝石", "", 260, 0,"",R.drawable.santoushe));
        database.execSQL(String.format(sql, 18, "刺猬", 1000, 1500, 230, "硝石", "", 255, 0,"",R.drawable.ciwei));
        database.execSQL(String.format(sql, 19, "狼", 1500, 2300, 225, "硝石", "", 250, 0,"",R.drawable.lan));
        database.execSQL(String.format(sql, 20, "精灵", 2500, 4000, 201, "硝石", "", 109, 0,"",R.drawable.jingling));
        database.execSQL(String.format(sql, 21, "僵尸", 300, 4500, 200, "硝石", "", 200, 0,"",R.drawable.jiangshi));
        database.execSQL(String.format(sql, 37, "僵尸", 300, 4500, 200, "硝石", "", 200, 0,"",R.drawable.jiangshi_red));
        database.execSQL(String.format(sql, 22, "凤凰", 3400, 6000, 190, "硝石", "", 231, 0,"",R.drawable.fengh));
        database.execSQL(String.format(sql, 23, "龙", 5000, 10000, 213, "硝石", "", 143, 0,"",R.drawable.long_pet));
        database.execSQL(String.format(sql, 40, "龙", 5000, 10000, 213, "硝石", "", 143, 0,"",R.drawable.long_pet_red));
        database.execSQL(String.format(sql, 24, "骷髅", 7000, 20000, 254, "硝石", "", 101, 0,"",R.drawable.kulou));
        database.execSQL(String.format(sql, 25, "作者（伪）", 70000, 70000, 0, "硝石", "", 0, 12,"",R.drawable.h_4_s));
        database.execSQL(String.format(sql, 26, "熊", 6000, 30000, 245, "硝石", "", 278, 1,"",R.drawable.xion));
        database.execSQL(String.format(sql, 27, "朱厌", 6500, 60000, 101, "硝石", "", 121, 2,"",R.drawable.zhuyan));
        database.execSQL(String.format(sql, 29, "陆吾", 10000, 80000, 100, "硝石", "", 109, 3,"",R.drawable.luwu));
        database.execSQL(String.format(sql, 30, "山魁", 20000, 50000, 105, "硝石", "", 120, 4,"",R.drawable.shankui));
        database.execSQL(String.format(sql, 39, "穷奇", 90000, 100000, 98, "硝石", "", 100, 5,"",R.drawable.qiongqi));
        database.execSQL(String.format(sql, 98, "穷奇", 90000, 100000, 98, "硝石", "", 100, 5,"",R.drawable.qiongqi_red));
        database.execSQL(String.format(sql, 38, "九尾狐", 100000, 300000, 59, "硝石", "", 99, 30,"",R.drawable.jiuweihu));
        database.execSQL(String.format(sql, 99, "九尾狐", 100000, 300000, 59, "硝石", "", 99, 30,"",R.drawable.jiuweihu_red));
        database.execSQL(String.format(sql, 100, "猼訑", 110000, 600000, 78, "硝石", "", 89, 7,"",R.drawable.fudi));
        database.execSQL(String.format(sql, 101, "狰", 140000, 650000, 68, "硝石", "", 77, 9,"",R.drawable.zheng));
        database.execSQL(String.format(sql, 102, "朱獳", 160000, 700000, 53, "硝石", "", 68, 12,"",R.drawable.zhuru));
        database.execSQL(String.format(sql, 103, "梼杌", 200000, 800000, 21, "硝石", "", 15, 40,"",R.drawable.taowu));
    }
}
