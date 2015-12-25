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

        database.execSQL(String.format(sql, 1, "蟑螂", 1, 8, 300, "原石", "只要有人的地方就有蟑螂，不管你爬得多高，你都可以遇见它们。它们是这个世界上最古老的怪物之一，并且也是繁殖力最高（没有之一）的。", 310,0,"",R.drawable.zl));
        database.execSQL(String.format(sql, 2, "大蚯蚓", 5, 20, 300, "硝石", "", 300, 0,"这并不是普通的蚯蚓，据说它们是基因改造产生的，当然你还是可以抓住它们然后拿去钓鱼，不过你得先找到可以钓鱼的地方。",R.drawable.qy));
        database.execSQL(String.format(sql, 3, "异壳虫", 15, 55, 260, "硝石", "", 300, 0,"不知道它们怎么产生的，但是它们在迷宫底层绝对是你的噩梦。",R.drawable.pc));
        database.execSQL(String.format(sql, 4, "巨型飞蛾", 25, 75, 300, "硝石", "", 150, 0,"身躯巨大的飞蛾，喜欢往光亮的地方聚集。喜欢在你的头顶飞来飞去。",R.drawable.feie));
        database.execSQL(String.format(sql, 5, "猪", 35, 95, 260, "硝石", "", 300, 0,"这种可以吃又可以玩的怪物，骚年不抓一只养养看吗？",R.drawable.zz));
        database.execSQL(String.format(sql, 6, "老鼠", 56, 115, 300, "硝石", "", 308, 0,"老鼠是一种啮齿动物，体形有大有小。当然这个世界的老鼠大小可是和普通人一样大，所以被老鼠打败了也不要觉得丢人。",R.drawable.laoshu));
        database.execSQL(String.format(sql, 7, "嗜血蚁", 100, 400, 250, "硝石", "嗜血蚁是一种昆虫。属节肢动物门，昆虫纲，膜翅目，蚁科。它们随时都处于嗜血的状态，只要被看到就会疯狂的进行攻击。", 150, 0,"",R.drawable.mayi));
        database.execSQL(String.format(sql, 33, "嗜血蚁", 100, 400, 250, "硝石", "", 150, 0,"嗜血蚁是一种昆虫。属节肢动物门，昆虫纲，膜翅目，蚁科。它们随时都处于嗜血的状态，只要被看到就会疯狂的攻击。",R.drawable.mayi_red));
        database.execSQL(String.format(sql, 8, "老虎", 120, 1000, 200, "硝石", "", 200, 0,"大型猫科动物，肚子饿的时候会假装亲近人类，肚子不饿的时候不具备攻击力。据说会捕获人类去当它的铲屎官。",R.drawable.laohu));
        database.execSQL(String.format(sql, 34, "老虎", 120, 1000, 200, "硝石", "", 200, 0,"大型猫科动物，肚子饿的时候会假装亲近人类，肚子不饿的时候不具备攻击力。据说会捕获人类去当它的铲屎官。",R.drawable.laohu_red));
        database.execSQL(String.format(sql, 9, "蛟", 305, 800, 150, "硝石", "", 300, 0,"龙的亚种，身躯脆肉，但是攻击力不俗。",R.drawable.jiao));
        database.execSQL(String.format(sql, 10, "变异蝎", 40, 520, 300, "硝石", "", 200, 0,"变异的蝎子，似乎是身躯变大，并且毒性会导致敌人产生幻觉。经常被一些无良的商人抓来制作毒品卖给爬楼的勇者。",R.drawable.xiezi));
        database.execSQL(String.format(sql, 35, "变异蝎", 40, 520, 300, "硝石", "", 200, 0,"变异的蝎子，似乎是身躯变大，并且毒性会导致敌人产生幻觉。经常被一些无良的商人抓来制作毒品卖给爬楼的勇者。",R.drawable.xiezi_red));
        database.execSQL(String.format(sql, 11, "食人鸟", 60, 280, 200, "硝石", "", 250, 0,"一种可爱又小巧的鸟类，红色的嘴巴表示它们具有强烈的主动攻击性。",R.drawable.srn));
        database.execSQL(String.format(sql, 12, "丑蝙蝠", 350, 20, 253, "硝石", "", 222, 0,"黑暗中生活的动物，蝙蝠本来就很丑了，更何况丑蝙蝠。",R.drawable.bianfu));
        database.execSQL(String.format(sql, 36, "丑蝙蝠", 350, 20, 253, "硝石", "", 222, 0,"黑暗中生活的动物，蝙蝠本来就很丑了，更何况丑蝙蝠。",R.drawable.bianfu_red));
        database.execSQL(String.format(sql, 13, "蛇", 200, 380, 200, "硝石", "", 240, 0,"滑溜溜的。",R.drawable.se));
        database.execSQL(String.format(sql, 14, "猴", 210, 310, 300, "硝石", "", 280, 0,"喜欢躲在阴暗处丢石头。似乎有一点点的智慧，据说是从外面世界逃进迷宫的人类？",R.drawable.h_1));
        database.execSQL(String.format(sql, 15, "野牛", 450, 450, 290, "硝石", "", 270, 0,"据说勤劳老实木讷的人会变成一头牛。",R.drawable.niu));
        database.execSQL(String.format(sql, 16, "龟", 500, 5300, 250, "硝石", "", 101, 0,"因为长寿，它们可能是唯一知道迷宫存在秘密的种族，可惜她们不会交流。",R.drawable.wugui));
        database.execSQL(String.format(sql, 17, "三头蛇", 700, 1000, 240, "硝石", "", 260, 0,"据说这些怪物是人为制造出来的，当然并不是简单的把三条蛇的脑袋缝在一起。",R.drawable.santoushe));
        database.execSQL(String.format(sql, 18, "刺猬", 1000, 1500, 230, "硝石", "", 255, 0,"这个浑身都是刺的家伙，出乎意料的温柔。",R.drawable.ciwei));
        database.execSQL(String.format(sql, 19, "狼", 1500, 2300, 225, "硝石", "", 250, 0,"据说迷宫外面世界的狼都是群居的，但是为什么迷宫里面的狼都是独立特行的呢？",R.drawable.lan));
        database.execSQL(String.format(sql, 20, "精灵", 2500, 4000, 201, "硝石", "", 109, 0,"美貌与身材兼具的生物，但是它们可是很凶残的。",R.drawable.jingling));
        database.execSQL(String.format(sql, 21, "僵尸", 300, 4500, 200, "硝石", "", 200, 0,"并不是所有的僵尸都是腐烂的尸体，这些僵尸具有可爱的外表，并且攻击力也很低。似乎是一种纯粹卖萌的怪物。",R.drawable.jiangshi));
        database.execSQL(String.format(sql, 37, "僵尸", 300, 4500, 200, "硝石", "", 200, 0,"并不是所有的僵尸都是腐烂的尸体，这些僵尸具有可爱的外表，并且攻击力也很低。似乎是一种纯粹卖萌的怪物。",R.drawable.jiangshi_red));
        database.execSQL(String.format(sql, 22, "凤凰", 3400, 6000, 190, "硝石", "", 231, 0,"传说中的生物，据说食人鸟捕食了人类后变异而成。",R.drawable.fengh));
        database.execSQL(String.format(sql, 23, "龙", 5000, 10000, 213, "硝石", "", 143, 0,"西方神话传说中的一种生物，不知道为什么会出现在迷宫中。",R.drawable.long_pet));
        database.execSQL(String.format(sql, 40, "龙", 5000, 10000, 213, "硝石", "", 143, 0,"西方神话传说中的一种生物，不知道为什么会出现在迷宫中。",R.drawable.long_pet_red));
        database.execSQL(String.format(sql, 24, "骷髅", 7000, 20000, 254, "硝石", "", 101, 0,"迷宫中的死神，据说被迷宫制造者赋予了管理迷宫生物生死的权利，但是在迷宫制造者消失之后，它们就变成了暴虐的怪物。",R.drawable.kulou));
        database.execSQL(String.format(sql, 25, "作者（伪）", 70000, 70000, 0, "硝石", "", 0, 12,"游荡在这个迷宫的一种意志的化身。没有实体，所以无法捕捉。据说是因为作者在构思这个游戏的时候因为睡眠不足产生的一种怨念。",R.drawable.h_4_s));
        database.execSQL(String.format(sql, 26, "熊", 6000, 30000, 245, "硝石", "", 278, 1,"喜欢睡觉的一种怪物，皮非常非常的厚。",R.drawable.xion));
        database.execSQL(String.format(sql, 27, "朱厌", 6500, 60000, 101, "硝石", "", 121, 2,"朱厌是古代汉族神话传说中的凶兽,身形像猿猴,白头红脚。",R.drawable.zhuyan));
        database.execSQL(String.format(sql, 29, "陆吾", 10000, 80000, 100, "硝石", "", 109, 3,"陆吾即肩吾，古代神话传说中的昆仑山神名，人面虎身虎爪而九尾。喜欢养各种奇怪的怪兽，是个老实可爱的培育师。",R.drawable.luwu));
        database.execSQL(String.format(sql, 30, "山魁", 20000, 50000, 105, "硝石", "", 120, 4,"山魈其实就是凶狠的大猴子，丑得让人看见就想哭。",R.drawable.shankui));
        database.execSQL(String.format(sql, 39, "穷奇", 90000, 100000, 98, "硝石", "穷奇，汉族神话传说中的古代四凶之一，外貌像老虎又像牛，长有一双翅膀和刺猬的毛发，代表至邪之物。", 100, 5,"",R.drawable.qiongqi));
        database.execSQL(String.format(sql, 98, "穷奇", 90000, 100000, 98, "硝石", "穷奇，汉族神话传说中的古代四凶之一，外貌像老虎又像牛，长有一双翅膀和刺猬的毛发，代表至邪之物。", 100, 5,"",R.drawable.qiongqi_red));
        database.execSQL(String.format(sql, 38, "九尾狐", 100000, 300000, 59, "硝石", "", 99, 30,"九尾狐，中华古代汉族神话传说中的生物--青丘之山，有兽焉，其状如狐而九尾。",R.drawable.jiuweihu));
        database.execSQL(String.format(sql, 99, "九尾狐", 100000, 300000, 59, "硝石", "", 99, 30,"九尾狐，中华古代汉族神话传说中的生物--青丘之山，有兽焉，其状如狐而九尾。",R.drawable.jiuweihu_red));
        database.execSQL(String.format(sql, 100, "猼訑", 110000, 600000, 78, "硝石", "", 89, 7,"猼訑（bódàn）是古代汉族神话传说中一种样子像羊的怪兽。它有九条尾和四只耳朵，眼睛长在背上。有人说取它的皮披在身上，就不会知道什么是畏惧。",R.drawable.fudi));
        database.execSQL(String.format(sql, 101, "狰", 140000, 650000, 68, "硝石", "", 77, 9,"狰是古代汉族传说中的奇兽，章峨之山有兽焉，其状如赤豹，五尾一角，其音如击石，其名曰狰。",R.drawable.zheng));
        database.execSQL(String.format(sql, 102, "朱獳", 160000, 700000, 53, "硝石", "", 68, 12,"朱獳（zhūrú ）是汉族神话中的兽类，样子很像狐狸，背部长有鱼的鳍。",R.drawable.zhuru));
        database.execSQL(String.format(sql, 103, "梼杌", 200000, 800000, 21, "硝石", "", 15, 40,"梼杌(táowù)是上古时期华夏神话中的四凶之一。长得很像老虎，毛长，人面、虎足、猪口牙，尾长。",R.drawable.taowu));
    }
}
