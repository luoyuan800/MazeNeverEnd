package cn.gavin.story;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cn.gavin.Achievement;
import cn.gavin.Element;
import cn.gavin.Hero;
import cn.gavin.db.DBHelper;
import cn.gavin.skill.Skill;
import cn.gavin.skill.SkillFactory;
import cn.gavin.skill.type.AttackSkill;
import cn.gavin.skill.type.DefendSkill;
import cn.gavin.utils.MazeContents;
import cn.gavin.utils.StringUtils;

/**
 * Copyright 2016 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 1/5/2016.
 */
public class NPC extends Hero {
    public static final int STORY_NPC = 0;
    public static final int PALACE_NPC = 1;
    public static final int IMAGE_NPC = 2;
    public static final int ALL_TYPE = -1;

    public static void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE npc(" +
                "uuid TEXT NOT NULL PRIMARY KEY," +
                "name TEXT ," +
                "atk NUMBER," +
                "hp NUMBER," +
                "def NUMBER," +
                "hit_rate NUMBER," +
                "parry NUMBER," +
                "desc TEXT," +
                "element TEXT," +
                "skill TEXT," +
                "p_skill TEXT," +
                "defeat INTEGER," +
                "found INTEGER," +
                "type INTEGER," +
                "acc TEXT," +
                "ach TEXT," +
                "lev NUMBER" +
                ")");
        db.execSQL("CREATE UNIQUE INDEX npc_index ON npc (uuid)");
//        insertNPC(db);
    }

    public static void insertNPC(SQLiteDatabase db) {
        insertNPC("0c69c615-7d28-4cde-8bf1-9456458c6cb1","锄禾日当午(1)",65241l, 14575l,51694l,0,1.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","无","欺诈游戏-多重攻击-腐蚀","","","",134l, db);
        insertNPC("4c266705-4adb-4030-b8f0-402c14828636","不是太吊(1)",262064l, 5914005l,3823752l,0,6.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","金","捕捉术-多重攻击-闪电","","","",10400l, db);
        insertNPC("9c707cd0-0bf5-4862-a4cc-52843a7b92e6","破军",218037l, 73597660l,1263207l,12,1.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","金","浮生百刃-多重攻击-腐蚀","","","",14026l, db);
        insertNPC("8a52c8dd-f771-4899-ace3-18afba874c7e","黄乾",5844l, 88140l,14588l,0,0.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","无","铁拳-斩击-群殴","","","",214l, db);
        insertNPC("08bb3d5b-d059-4575-8e09-0dfa816e53c6","东西",334731l, 447370l,415430l,0,0.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","无","超能量-巨大化-闪避","","","",285l, db);
        insertNPC("9951910e-6a49-4b88-8f12-fc987a8a51ca","光暗魔导师",18117l, 328155l,74576l,8,0.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","无","多重攻击-魔王天赋-浮生百刃","","","",290l, db);
        insertNPC("6a74840f-c7f4-4342-ad8d-53a4e4548c83","小言大战悍妇(1)",10413l, 1810l,13827l,0,0.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","无","--","","","",277l, db);
        insertNPC("8c229a28-2885-43cc-bc33-6626997ef41d","绯红",9748l, 20023751l,809461l,2,90.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","金","浮生百刃-原能力-勇者之击","","","",7398l, db);
        insertNPC("eb02adf2-36e2-4a24-ba31-999c9f3b6d25","苏",135597l, 16605l,22577l,0,0.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","无","魔王天赋-腐蚀-迷雾","","","",193l, db);
        insertNPC("46b3b3a9-65ac-4485-8bd0-76622bdf0ab3","(｡•́︿•̀｡)(306)",98915699238l, 128363737189l,63377838663l,90,90.0f,"上一个版本的殿堂守护者<br>&nbsp;终于上了50000了…定制我来了","火","魔王天赋-多重攻击-生命吸收","","","",49994l, db);
        insertNPC("ae274b1f-388c-48b7-b062-283ea0c63791","疾风(1)",218309l, 8739361l,194832l,0,0.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","无","强化-捕捉术-闪电","","","",1666l, db);
        insertNPC("db08833d-8771-4503-b4fa-dd707b47fd31","c",736l, 960l,4l,0,0.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","无","--","","","",1001l, db);
        insertNPC("52fd2680-dc42-4c08-a26b-293559c8f40a","铭神君(3)",1207572l, 26319347l,100992l,9,1.0f,"上一个版本的殿堂守护者<br>&nbsp;下一步是10000","火","多重攻击-浮生百刃-魔王天赋","","","",5064l, db);
        insertNPC("60b2b985-4652-4528-8d15-79d80d3ab949","请叫我🌹公主殿下🌹",14569l, 8879942l,68610l,0,2.0f,"上一个版本的殿堂守护者<br>&nbsp;💝我是初音💝我是公主💝请叫我🌹公主殿下🌹","无","勇者之击-群殴-","","","",4435l, db);
        insertNPC("d522e7cc-4bea-4b65-8007-cace46139774","forge1.2d",34991l, 139788l,12167l,0,0.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","无","勇者之击-巨大化-超能量","","","",322l, db);
        insertNPC("381d6f37-0708-49f7-9250-3475aa2e70f1","勇者无双(1)",34669l, 3678l,2692l,0,0.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","无","捕捉术-群殴-","","","",430l, db);
        insertNPC("025e1f74-839b-41a8-b2d2-30c0f4aa5267","💏💏",227865l, 8418209l,438389l,0,3.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","无","反弹-勇者之击-闪避","","","",6196l, db);
        insertNPC("c56005b2-62ff-4424-971c-53baffcfe529","小贺",51120l, 250125l,47098l,0,0.0f,"上一个版本的殿堂守护者<br>&nbsp;我的大屌早已饥渴难耐了1","无","魔王天赋-强化-闪电","","","",449l, db);
        insertNPC("5dc94ed7-1d8a-404e-b42c-148615faf2db","深蓝深蓝",6271236l, 8765995l,4263293l,58,2.0f,"上一个版本的殿堂守护者<br>&nbsp;真无限爬层","火","咆哮-虚无吞噬-重击","","","",31368l, db);
        insertNPC("c917dd9a-8544-4a3a-a6d3-0a862d007a0c","老白大爷",6372l, 360850l,7384l,0,0.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","无","超能量-铁拳-欺诈游戏","","","",191l, db);
        insertNPC("64d86cb5-4430-43e4-9067-e295da067697","工具箱(3)",26780818l, 1067739637l,49021912l,13,11.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","木","水波-浮生百刃-魔王天赋","","","",39036l, db);
        insertNPC("016b7a8e-0848-4b9a-b52f-26a881a8eb94","boy(7)",505013503l, 12470880488l,2157926774l,90,90.0f,"上一个版本的殿堂守护者<br>&nbsp;阳光总在风雨后~","金","浮生百刃-虚无吞噬-多重攻击","","","",41714l, db);
        insertNPC("45b0d9bb-003b-4698-bd43-ab441e2d187c","大哥大(7)",2030562990l, 4007576502l,330935726l,19,0.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","火","生命吸收-虚无吞噬-多重攻击","","","",10535l, db);
        insertNPC("db0ba8da-a8b2-43ec-a0ed-0bc5fda165a1","孤鱼游清池",5270851l, 10411778l,1193544l,0,0.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","土","勇者之击-浮生百刃-重击","","","",7651l, db);
        insertNPC("7698b0fe-6085-4fc8-a26e-b1e6243707f8","是不是名字长了就可以果断牛逼(1)",3346070l, 115956110l,609608l,0,1.0f,"上一个版本的殿堂守护者<br>&nbsp;哈哈哈哈哈哈","土","勇者之击-浮生百刃-虚无吞噬","","","",6825l, db);
        insertNPC("abced403-0a87-4d8f-a9c2-e966744f4971","勇者国家(1)",10734l, 57574l,1627170l,0,0.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","无","--","","","",4600l, db);
        insertNPC("6b5ecd6d-b626-4dad-98b6-9a5ed86d0ecb","黄昏之狼",1811672l, 3289914l,727776l,2,2.0f,"上一个版本的殿堂守护者<br>&nbsp;你將止步於此！","无","原能力-浮生百刃-虚无吞噬","","","",3634l, db);
        insertNPC("31041cb0-e2b6-4c71-96c1-a2721918be82","茶水",1716424l, 1867914l,1615112l,66,42.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","木","勇者之击-浮生百刃-原能力","","","",2817l, db);
        insertNPC("c5c440bd-7aeb-47a3-9c18-eff7b5070bad","👆朝花夕拾💍杯中酒🍺",380l, 5207600l,85122l,0,0.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","无","勇者之击-反弹-斩击","","","",5022l, db);
        insertNPC("97f9f913-2030-4c7a-9d59-a3beb0984847","浅笑",37495l, 30815l,4492l,0,0.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","无","勇者之击--","","","",136l, db);
        insertNPC("fc840c23-6bd0-4a69-bfcd-eb574f5947e5","人生的意义",7000720l, 60864915l,205275l,51,1.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","金","浮生百刃-勇者之击-斩击","","","",12489l, db);
        insertNPC("bd55641e-bed2-44e5-9d02-53595c9c4d71","乐枫",4275251l, 13777287l,436459l,58,30.0f,"上一个版本的殿堂守护者<br>&nbsp;吃饱了，睡觉(∩＿∩)","水","原能力-浮生百刃-错位","","","",16675l, db);
        insertNPC("2b361b81-4c13-4939-81d2-0fe79ca7d1d0","文子",2638083l, 37213779l,644313l,11,8.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","金","浮生百刃-勇者之击-斩击","","","",5467l, db);
        insertNPC("2ca3fde5-1b3b-4268-8d0d-4da4bac0a102","M9(1)",11863937l, 50032687l,91380l,2,2.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","土","浮生百刃-原能力-勇者之击","","","",19686l, db);
        insertNPC("29a38201-69f4-4197-9353-9e076f4f5093","一飞(2)",642139l, 159877427l,414825l,64,0.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","火","勇者之击-咆哮-浮生百刃","","","",7071l, db);
        insertNPC("4fd677bf-2966-4c45-9822-5e1e5addf80e","来无影(6)",17833960l, 51867907l,20398538l,2,0.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","火","原能力-浮生百刃-虚无吞噬","","","",9970l, db);
        insertNPC("49430eae-24ad-422c-9400-2eae65dbdf6a","👾(1)",2163523l, 25716994l,4015508l,74,91.0f,"上一个版本的殿堂守护者<br>&nbsp;😡都让开😡我要定制！","无","浮生百刃-虚无吞噬-魔王天赋","","","",35450l, db);
        insertNPC("8476c9a4-cb16-4bf0-813f-2d152a52405f","夜良",168243l, 46642632l,576149l,3,0.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","无","浮生百刃-生命吸收-强化","","","",857l, db);
        insertNPC("c827d222-5423-4772-a4d9-0b0bec753bde","屌爆了(3)",346801l, 22742557l,520882l,83,1.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","火","勇者之击-斩击-闪避","","","",5781l, db);
        insertNPC("a9dd2cf1-8ca7-4b27-8eae-5041e328592b","剑河民中马特(2)",41086l, 4139892l,103003l,0,0.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","无","魔王天赋-多重攻击-强化","","","",310l, db);
        insertNPC("e6cffdbd-e6a8-48c6-a281-ca39cbcc002a","神魔",143882l, 13898670l,145397l,16,2.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","土","重击-咆哮-","","","",5220l, db);
        insertNPC("23dda6f9-58f3-4e0f-b153-e32e7cf0c7ff","行者无疆(1)",3272l, 4719696l,4813470l,0,0.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","无","捕捉术-群殴-浮生百刃","","","",45885l, db);
        insertNPC("21ff6471-d654-4d00-ab54-0a1605e2b036","发呆",134034l, 4419651l,3139464l,0,0.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","金","勇者之击-浮生百刃-虚无吞噬","","","",29625l, db);
        insertNPC("51b9e208-7295-4ba6-bded-2ea0b260f2b4","零冰(1)",45497l, 3587618l,18204l,0,0.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","无","浮生百刃-原能力-勇者之击","","","",1687l, db);
        insertNPC("56000ac7-69dc-4d98-ae9d-b7f96a1a3a3f","forge1.2d ",432261l, 6071563l,1357066l,84,95.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","无","原能力-勇者之击-浮生百刃","","","",22599l, db);
        insertNPC("18115839-5592-40fa-9605-5fcb347e6387","浅巷墨璃",57866l, 803770l,39359l,0,0.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","无","反弹-超能量-捕捉术","","","",832l, db);
        insertNPC("9ae50cf4-b91a-46f1-8b20-c7ab887acb51","Hero`💀方航(9)",22494855l, 1407072464l,209289974l,0,4.0f,"上一个版本的殿堂守护者<br>&nbsp;💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀","火","浮生百刃-原能力-定身","","","",6148l, db);
        insertNPC("1c57acd9-bad6-4c21-b8a4-ad1d5ed35340","Kiraco",778850l, 100054625l,22673789l,90,90.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","金","浮生百刃-反弹-勇者之击","","","",36919l, db);
        insertNPC("738028fd-ea7a-4bf1-9b77-bcb3c8143889","#神?#🌻致命一击🌻",81426l, 392519115l,2614264l,23,90.0f,"上一个版本的殿堂守护者<br>&nbsp;🚀🚄🚉🚃🚗🚕🚓🚒🚑🚙🚲","金","浮生百刃-原能力-勇者之击","","","",238934l, db);
        insertNPC("20918df1-233d-4589-b600-831fa59d49cf","雪鐘卿(2)",145438l, 4138895l,227947l,1,1.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","无","迷雾-魔王天赋-腐蚀","","","",514l, db);
        insertNPC("3bcaad25-163d-4df9-8e54-0fdc411827a3","龙傲天(1)",3247091l, 1537767l,729738l,0,0.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","无","魔王天赋-欺诈游戏-多重攻击","","","",1016l, db);
        insertNPC("f2eaf31b-fa0f-4b2b-879c-9527344425bb","❌👸⭕👙(1)",121984588l, 844184503l,55535308l,90,90.0f,"上一个版本的殿堂守护者<br>&nbsp;想不到我也能上十万!","火","浮生百刃-魔王天赋-捕捉术","","","",117200l, db);
        insertNPC("263abdbf-814b-455e-a527-4480c4fc4683","#神?#谁说非要有五行属性",13240l, 760468l,42010l,0,0.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","无","捕捉术-勇者之击-浮生百刃","","","",40002l, db);
        insertNPC("78913ed8-20b5-4ec7-adc9-23de6799c155","试作型面包(1)",31459240l, 2738852191l,349456l,12,18.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","金","浮生百刃-原能力-虚无吞噬","","","",5392l, db);
        insertNPC("b5f98ab0-5825-479a-ae05-58fa676229b8","谜离阿",346l, 258145l,109391l,0,0.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見","无","勇者之击-欺诈游戏-超能量","","","",330l, db);
        insertNPC("54aef667-95a6-4c5e-a320-1c359f6f3917","居然可以改名😨(3)",28711404l, 2434930003l,656032764l,90,30.0f,"上一个版本的殿堂守护者<br>&nbsp;😱😱😱","无","虚无吞噬-勇者之击-浮生百刃","","","",16688l, db);
        insertNPC("b4ecfee6-ad74-490b-9891-4d04112733e9","大♥少",137171l, 222920l,338937l,0,0.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","无","魔王天赋-多重攻击-腐蚀","","","",602l, db);
        insertNPC("d54a370e-41ba-4853-9d8b-dd5d834490a2","大白兔奶烟",1144417l, 1675613l,471145l,51,100.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","火","勇者之击-捕捉术-群殴","","","",18000l, db);
        insertNPC("0f8c4dd8-88c2-4f15-82fc-37fa9b4cf989","颜值爆表",1047376l, 17990618l,5783329l,0,1.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","金","勇者之击-浮生百刃-原能力","","","",47069l, db);
        insertNPC("8939e5ce-2b40-438e-b5a3-5a941db569b9","勇者临死前(1)",1624528l, 2455180l,628375l,60,2.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","无","多重攻击-反击-闪电","","","",3180l, db);
        insertNPC("fb4f53b7-cf73-496e-9f5e-37a949abc4c8","离线哥哥",173033l, 2000000l,140342l,0,0.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","无","捕捉术-勇者之击-重击","","","",2804l, db);
        insertNPC("fb17a104-4302-4932-b437-539ee765f18f","勇敢的懦夫",5033519l, 22310847l,5160686l,90,90.0f,"上一个版本的殿堂守护者<br>&nbsp;苦不苦，想想红军两万五！","木","勇者之击-浮生百刃-虚无吞噬","","","",27477l, db);
        insertNPC("fe279fa3-cf11-4b25-a8ea-7c5fbe3029f9","宁",214065l, 100205l,205962l,0,0.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","无","勇者之击-反弹-闪避","","","",926l, db);
        insertNPC("5ef56f29-e22c-4362-a9ae-34b9cdfac00e","昏",34766l, 24620l,11123l,0,0.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","无","--","","","",163l, db);
        insertNPC("bd7aa350-cdca-4b26-99d2-846c6332cda6","去年今日此门中",876817l, 31115546l,2774853l,90,90.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","金","勇者之击-斩击-浮生百刃","","","",32566l, db);
        insertNPC("e57cab1e-bcf5-47b3-a367-86c9d371ea1b","伯符鸟(2)",11353938l, 54456713l,10974657l,51,92.0f,"上一个版本的殿堂守护者<br>&nbsp;德玛西亚！！！","金","浮生百刃-虚无吞噬-勇者之击","","","",23993l, db);
        insertNPC("7845d922-27eb-41fc-ba4d-f5610e157406","我可不是随便的主儿",507202l, 1825750l,59587l,3,0.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","无","反击--","","","",401l, db);
        insertNPC("6ddfd9ec-360b-4aad-849f-f2f5ee494185","帝",2524370l, 2162780l,377799l,0,0.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","无","裂空剑-重击-铁拳","","","",2524l, db);
        insertNPC("08cbc46f-06ca-4d5b-bf20-9645bb7cb5bb","没事干(1)",9721349l, 805209405l,34777098l,0,1.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","无","反击-浮生百刃-虚无吞噬","","","",5830l, db);
        insertNPC("417a5a55-bb14-4b99-bd30-dbbf24b18841","疯狂的大裤衩(1)",400126l, 4763341l,624760l,3,2.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","无","捕捉术-多重攻击-生命吸收","","","",1528l, db);
        insertNPC("7e61cfa4-e2bc-4731-a0a8-8c32c6ecf5cb","劍🔯聖(1)",255949l, 11867162l,467676l,51,5.0f,"上一个版本的殿堂守护者<br>&nbsp;哇咔咔   我叫大魔王","金","浮生百刃-虚无吞噬-勇者之击","","","",8944l, db);
        insertNPC("18cf5834-ea7e-4936-bd11-82ae51366bb4","🐯🐯🐯🐯🐯",720395l, 11938878l,321045l,0,1.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","无","强化-浮生百刃-虚无吞噬","","","",1174l, db);
        insertNPC("429023b2-afe6-4294-9915-4aed91ec993c","疯姐姐",197120l, 1555086l,4115l,1,0.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","无","勇者之击-原能力-反弹","","","",810l, db);
        insertNPC("25076fe2-facb-4632-87a6-6072b2e3c719","反对者(4)",394991014l, 11486019294l,7029433560l,90,17.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見逼大的了，你上！","金","腐蚀-多重攻击-闪电","","","",40910l, db);
        insertNPC("3ba775b3-140c-40ea-b765-84d865fade8b","🍝",127732l, 293700l,61731l,0,0.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","无","魔王天赋-腐蚀-闪电","","","",573l, db);
        insertNPC("eae03a1a-689d-4cbf-807b-ea481704d660","小堕落龙",7380811l, 125182995l,9239529l,24,1.0f,"上一个版本的殿堂守护者<br>&nbsp;没了😔继续上吧😄不到5万心不死啊😊。。。。。。。。","土","原能力-咆哮-浮生百刃","","","",32500l, db);
        insertNPC("f9547bf6-fd89-4304-a06b-b682326d38f3","刘方杰",418379l, 654900l,1002069l,0,0.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","无","魔王天赋-多重攻击-强化","","","",854l, db);
        insertNPC("5091485d-13d6-4c54-9f5e-5109608639d4","诚恳的战士",464966l, 2265255l,143657l,0,0.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","无","魔王天赋-强化-闪电","","","",458l, db);
        insertNPC("7acff89d-e34c-4b10-9e78-dcabd4e85611","『爱吃猫的鱼』",380l, 1501085l,9776l,0,0.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","无","勇者之击-反弹-闪避","","","",1847l, db);
        insertNPC("4ce0d920-336b-45b7-a148-798d23aabf10","蔚蓝(3)",65106651l, 345853625l,19042614l,51,0.0f,"上一个版本的殿堂守护者<br>&nbsp;帅气！","金","群殴-浮生百刃-重击","","","",30999l, db);
        insertNPC("d5725167-eff2-4066-81c1-eba9ad47d5d3","无敌的仙人",822l, 2591780l,422677l,10,1.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","无","勇者之击-浮生百刃-虚无吞噬","","","",5037l, db);
        insertNPC("d5f4bf99-eda3-495c-8a5b-eccaf08d591e","#神?#啪嗒砰",103800l, 86732355l,1450382l,90,86.0f,"上一个版本的殿堂守护者<br>&nbsp;爬啊爬啊爬","金","勇者之击-原能力-浮生百刃","","","",39500l, db);
        insertNPC("5d1bef8e-a5ab-4e92-9369-d8c5d4f5b6a3","兔兔(2)",13525l, 341830l,113l,0,0.0f,"上一个版本的殿堂守护者<br>&nbsp;好好搞","无","--","","","",138l, db);
        insertNPC("c439e36e-0301-41c0-b3b1-f0a2666bf94c","葬言",3305738l, 3545746l,199875l,0,2.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","土","魔王天赋-多重攻击-群殴","","","",2462l, db);
        insertNPC("5326dcfc-4b3d-4245-82ee-77f880cff1ab","啪嗒砰二世",3119011l, 68953768l,1834163l,59,94.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","金","浮生百刃-生命吸收-魔王天赋","","","",9975l, db);
        insertNPC("6433422e-df8c-47eb-8133-cf59bd45958d","传奇的",705558l, 1133075l,1328041l,81,90.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","无","裂空剑-重击-定身","","","",2151l, db);
        insertNPC("d626d493-a831-4f3f-a412-a4b7632cf107","点句式(2)",18225688l, 24245912l,17309605l,90,0.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","土","浮生百刃-虚无吞噬-魔王天赋","","","",37567l, db);
        insertNPC("d93377f7-4bb2-402c-a827-f6e44c281d33","绿色史莱姆(5)",6155695405l, 48482640519l,5243362024l,87,90.0f,"上一个版本的殿堂守护者<br>&nbsp;啦啦啦","金","魔王天赋-多重攻击-生命吸收","","","",24858l, db);
        insertNPC("fdead405-03b2-438f-8708-2b87f3897b98","(๑• . •๑)",9403176l, 10148270l,40103548l,90,31.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","火","浮生百刃-原能力-咆哮","","","",44003l, db);
        insertNPC("d0d665a0-2719-457a-b366-e388b6544322","开始终点",224328l, 201760l,59389l,0,1.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","无","反弹-闪避-","","","",250l, db);
        insertNPC("6d94dcac-9793-4632-b1db-0d341df43a2c","巴西",96024l, 1853872l,1001705l,0,6.0f,"上一个版本的殿堂守护者<br>&nbsp;叫我爸爸","无","闪电-魔王天赋-欺诈游戏","","","",1389l, db);
        insertNPC("cacdc836-c968-4637-b9d5-f1f441c43bf6","10086+1(1)",20643833l, 111543666l,10479890l,0,0.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，","土","--","","","",9620l, db);
        insertNPC("33f81b68-306b-492d-9748-5e8ce289439f","想自杀啊(2)",55973l, 1710673l,2017093l,11,0.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","无","错位-浮生百刃-虚无吞噬","","","",1605l, db);
        insertNPC("e1578f08-c66e-4167-b169-b3639724bd9c","👆👆👆👆👆👆👆",2519524l, 12308435l,2507461l,9,2.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","木","浮生百刃-魔王天赋-水波","","","",15811l, db);
        insertNPC("7007ede3-a065-4329-bad0-d2139af4d308","👽哎吆👽(154)",5285987348l, 8107081318l,6068943622l,10,28.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","无","超能量-重击-吐息","","","",41934l, db);
        insertNPC("5b5384fa-321e-4f77-a963-1fa516faa947","(๑•̀ㅂ•́)و✧离线哥哥",1470088l, 27334459l,307519l,82,0.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","金","勇者之击-群殴-浮生百刃","","","",6191l, db);
        insertNPC("628d00d2-c461-44b2-a057-3eec8b5c181f","#神?#逗比大作战(3)",133877l, 139200141l,2093120l,51,90.0f,"上一个版本的殿堂守护者<br>&nbsp;万四","金","浮生百刃-错位-咆哮","","","",39015l, db);
        insertNPC("869c5f10-03ff-45e4-b46c-58a1abca93c4","谜离的你迷离的眼(2)",6595615l, 976204262l,120660420l,90,0.0f,"上一个版本的殿堂守护者<br>&nbsp;💀","金","勇者之击-原能力-浮生百刃","","","",24909l, db);
        insertNPC("c62a803b-db32-4228-8ed6-1511127584a3","saberb",78020l, 17545879l,2034767l,86,1.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","土","浮生百刃-点攻-多重攻击","","","",10854l, db);
        insertNPC("cc1eb375-a72d-40f9-9917-8a7ead8caf0e","大魔王",468221l, 557350l,50808l,0,0.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","无","魔王天赋-捕捉术-多重攻击","","","",806l, db);
        insertNPC("b413a932-9c06-469c-97e0-2a5af4e0ea96","幽幽(2)",8999687l, 2787315l,17065618l,90,-10.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","金","勇者之击-浮生百刃-错位","","","",1032l, db);
        insertNPC("2544f469-1425-4e07-b820-346236071387","阿尔托莉雅(4)",8717523l, 174649100l,150018007l,90,33.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","无","欺诈游戏-多重攻击-浮生百刃","","","",2623l, db);
        insertNPC("d6ac0f9f-da4a-4110-a6fb-bd37e73531ed","初音公主殿下",454125l, 7132305l,3204306l,0,0.0f,"上一个版本的殿堂守护者<br>&nbsp;我就是我，无人可替，我就是初音未来","金","勇者之击-浮生百刃-咆哮","","","",10445l, db);
        insertNPC("be89832e-42b3-4c5c-86f8-b5817c656bb0","肏(1)",1035878l, 8688122723l,5557034115l,0,78.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","金","反弹-浮生百刃-斩击","","","",50000l, db);
        insertNPC("a2a22f9b-d24c-4de6-9b51-d7c8504c7a1d","#神?#传说中的🐮魔王(7)",200230356366l, 5319205839691l,271622489141l,50,60.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","火","生命吸收-魔王天赋-多重攻击","","","",201153l, db);
        insertNPC("7d50743a-ffbd-44d4-9a45-eb7c56b342b6","💞💕(1)",11822l, 5676054l,23357l,90,91.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","无","反弹-勇者之击-斩击","","","",14323l, db);
        insertNPC("bfed85a8-d030-42b6-b78c-0c052e3480e6","云动",7454834l, 78542073l,9137628l,90,90.0f,"上一个版本的殿堂守护者<br>&nbsp;逗比大作战","金","--","","","",18678l, db);
        insertNPC("815ce416-d569-41b3-9305-3a9ecf0fdd96","HERO(1)",7507689l, 48988996l,75951l,17,0.0f,"上一个版本的殿堂守护者<br>&nbsp;德玛西亚！","火","群殴-浮生百刃-勇者之击","","","",6629l, db);
        insertNPC("5542bfd4-c7c0-4e67-8163-0866987c3294","笨笨(7)",158670790l, 141777552l,114275327l,53,94.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","火","多重攻击-生命吸收-闪电","","","",9099l, db);
        insertNPC("2ea4de7d-589b-45be-b447-9e3670885a43","爱吃肉的土豆",18125461l, 795403416l,3543562l,0,1.0f,"上一个版本的殿堂守护者<br>&nbsp;下次应该是3000层了！","火","强化-水波-生命吸收","","","",2365l, db);
        insertNPC("f93f67c7-5857-4991-8b64-eb27dcac5d56","🙌🙏🙅🙆",198775l, 5789935l,39463l,4,0.0f,"上一个版本的殿堂守护者<br>&nbsp;😘","无","错位-裂空剑-重击","","","",1090l, db);
        insertNPC("7f03bf9a-023a-4ed6-972a-c6e0006851cb","系统提示",37529l, 5760445l,51648l,2,0.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","无","多重攻击-浮生百刃-魔王天赋","","","",3676l, db);
        insertNPC("afb287ff-4a73-4d51-b182-d5ef0706d609","别讲(3)",65900475l, 100979286l,84928623l,19,0.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","火","群殴-原能力-重击","","","",8447l, db);
        insertNPC("a6408316-5b6e-47f0-8b20-1ee00abba337","飘逸风尘(2)",119846197l, 23106467398l,118912211l,90,90.0f,"上一个版本的殿堂守护者<br>&nbsp;😨上面都是20万以上怎么玩","火","浮生百刃-原能力-勇者之击","","","",69704l, db);
        insertNPC("cd363cd9-6a29-4044-b21f-a9760ee03071","赤いの魔王さま(3)",41436094448l, 256104936956l,57482088891l,86,92.0f,"上一个版本的殿堂守护者<br>&nbsp;吃屎啦","火","多重攻击-水波-生命吸收","","","",117345l, db);
    }

    public static void insertNPC(String id, String name, long atk, long hp, long def, long hit,
                                 float parry, String desc, String element, String skill,
                                 String pskill, String acc, String ach, long lev, SQLiteDatabase database) {
        insertNPC(id,  name, atk,  hp, def, hit,
         parry,  desc,  element,  skill,
                 pskill,  acc,  ach,  lev,  STORY_NPC,database);
    }
    public static void insertNPC(String id, String name, long atk, long hp, long def, long hit,
                                 float parry, String desc, String element, String skill,
                                 String pskill, String acc, String ach, long lev, int type,SQLiteDatabase database) {
        String sql = String.format("replace into npc values('%s', '%s', %s, %s, %s, %s, %s, '%s', '%s', '%s', '%s', 0, 0,%s,'%s', '%s', %s)",
                id, name,atk,hp,def, hit,parry,desc,element,skill,pskill,type,acc, ach, lev);
        if(database==null){
            DBHelper.getDbHelper().excuseSQLWithoutResult(sql);
        }else{
            database.execSQL(sql);
        }
    }

    private String name;
    private Element element = Element.无;
    private long lev;
    private List<Skill> skillList;
    private List<Skill> propertySkillList;
    private String desc;
    private boolean defeat;
    private String accName;
    private String achName;
    private boolean found;


    public NPC() {
        super("");
    }

    public static List<NPC> loadNPCByType(int type) {
        Cursor cursor;
        if (type == -1) {
            cursor = DBHelper.getDbHelper().excuseSOL("select * from npc order by lev");
        } else {
            cursor = DBHelper.getDbHelper().excuseSOL("select * from npc where type = " + type);
        }
        List<NPC> npcs = new ArrayList<NPC>(cursor.getCount());
        while (!cursor.isAfterLast()) {
            NPC npc = buildNpc(cursor);
            npcs.add(npc);
            cursor.moveToNext();
        }
        cursor.close();
        return npcs;
    }

    private static NPC buildNpc(Cursor cursor) {
        NPC npc = new NPC();
        npc.setName(cursor.getString(cursor.getColumnIndex("name")));
        npc.setHp(cursor.getLong(cursor.getColumnIndex("hp")));
        npc.setAttackValue(cursor.getLong(cursor.getColumnIndex("atk")));
        npc.setDefenseValue(cursor.getLong(cursor.getColumnIndex("def")));
        npc.setSkill(cursor.getString(cursor.getColumnIndex("skill")));
        npc.setAccName(cursor.getString(cursor.getColumnIndex("acc")));
        npc.setAchName(cursor.getString(cursor.getColumnIndex("ach")));
        npc.setDesc(cursor.getString(cursor.getColumnIndex("desc")));
        npc.setLev(cursor.getLong(cursor.getColumnIndex("lev")));
        npc.setPropertySkill(cursor.getString(cursor.getColumnIndex("p_skill")));
        npc.setUuid(cursor.getString(cursor.getColumnIndex("uuid")));
        npc.setDefeat(1 == cursor.getInt(cursor.getColumnIndex("defeat")));
        npc.setFound(1 == cursor.getInt(cursor.getColumnIndex("found")));
        npc.setHitRate(cursor.getLong(cursor.getColumnIndex("hit_rate")));
        npc.setParry(cursor.getFloat(cursor.getColumnIndex("parry")));
        npc.setElement(cursor.getString(cursor.getColumnIndex("element")));
        npc.setUuid(cursor.getString(cursor.getColumnIndex("uuid")));
        return npc;
    }

    public String getSimpleHTMLDesc() {
        return lev + " - <font color=\"red\">" + name + "</font> (" + element + ")<br>&nbsp;&nbsp;" + desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(String element) {
        try {
            this.element = Element.valueOf(element);
        } catch (Exception e) {
            this.element = Element.无;
        }
        super.setElement(this.element);
    }

    public long getLev() {
        return lev;
    }

    public void setLev(long lev) {
        this.lev = lev;
    }

    public void setHp(long hp) {
        super.setHp(hp);
        super.setUpperHp(hp);
    }

    public List<Skill> getSkillList() {
        return skillList;
    }

    public void setSkill(String skill) {
        String[] skillNames = StringUtils.split(skill, "-");
        skillList = new ArrayList<Skill>(skillNames.length);
        for (String name : skillNames) {
            Skill s = SkillFactory.getSkill(name, this);
            if (s != null && !s.getName().equals("empty")) {
                s = s.copy();
                if(s!=null){
                s.setHero(this);
                skillList.add(s);
                }
            }
        }
    }

    public void setSkillList(List<Skill> skillList) {
        this.skillList = skillList;
    }

    public Skill getPropertySkill(String name) {
        for (Skill skill : propertySkillList) {
            if (skill.getName().equals(name)) {
                return skill;
            }
        }
        return null;
    }

    public void setPropertySkill(String propertySkill) {
        propertySkillList = new ArrayList<Skill>(1);
        for (String name : StringUtils.split(propertySkill, "-")) {
            Skill skill = SkillFactory.getSkill(name, this);
            if (skill != null && !skill.getName().equals("empty")) {
                skill = skill.copy();
                if(skill!=null){
                skill.setHero(this);
                propertySkillList.add(skill);
                }
            }
        }
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isDefeat() {
        return defeat;
    }

    public void setDefeat(boolean defeat) {
        this.defeat = defeat;
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public String getAchName() {
        return achName;
    }

    public void defeat() {
        if (StringUtils.isNotEmpty(accName)) {
            DBHelper.getDbHelper().excuseSQLWithoutResult("UPDATE recipe set found = '" + Boolean.TRUE + "' WHERE name = '" + accName + "'");
        }
        if (StringUtils.isNotEmpty(achName)) {
            Achievement.valueOf(achName).enable(MazeContents.hero);
        }
        DBHelper.getDbHelper().excuseSQLWithoutResult("UPDATE npc set found = 1 , defeat = " + (defeat ? 1 : 0) + " WHERE uuid = '" + getUuid() + "'");
    }

    public void setAchName(String achName) {
        this.achName = achName;
    }

    public Skill useAtkSkill() {
        for (Skill skill : skillList) {
            if (skill instanceof AttackSkill && skill.perform()) {
                return skill;
            }
        }
        return null;
    }

    public Skill useDefSkill() {
        for (Skill skill : skillList) {
            if (skill instanceof DefendSkill && skill.perform()) {
                return skill;
            }
        }
        return null;
    }

    public static NPC build(long level) {
        NPC npc = null;
        Cursor cursor = DBHelper.getDbHelper().excuseSOL("select * from npc where lev = " + level + " and found = 0");
        if (!cursor.isAfterLast()) {
            npc = buildNpc(cursor);
        }
        cursor.close();
        return npc;
    }

    public String getDetailDesc() {
        StringBuilder builder = new StringBuilder("<b>");
        builder.append(name).append("</b> ").append(element).append("<br>");
        builder.append("生命:<font color=\"blue\">").append(StringUtils.formatNumber(getHp())).append("</font>&nbsp;");
        builder.append("攻击:<font color=\"blue\">").append(StringUtils.formatNumber(getBaseAttackValue())).append("</font>&nbsp;");
        builder.append("防御:<font color=\"blue\">").append(StringUtils.formatNumber(getBaseDefense())).append("</font>&nbsp;");
        builder.append("<br>");
        if (defeat) {
            builder.append("在第").append(lev).append("层被你打败。");
        } else {
            builder.append("在第").append(lev).append("层打败了你。");
        }
        builder.append("<br>");
        builder.append("&nbsp;");
        builder.append(desc);
        return builder.toString();
    }

    public Boolean getFound() {
        return found;
    }

    public void setFound(Boolean found) {
        this.found = found;
    }
}
