package cn.gavin.story;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cn.gavin.Achievement;
import cn.gavin.Element;
import cn.gavin.Hero;
import cn.gavin.db.DBHelper;
import cn.gavin.log.LogHelper;
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
        insertNPC(db);
    }

    public static void insertNPC(SQLiteDatabase db) {
        insertNPC("46b3b3a9-65ac-4485-8bd0-76622bdf0ab3","(｡•́︿•̀｡)(306)",98915699238l, 128363737189l,63377838663l,90,90.0f,"上一个版本的殿堂守护者<br>&nbsp;终于上了50000了…定制我来了","火","魔王天赋-多重攻击-生命吸收","","","",49994l,STORY_NPC, db);
        insertNPC("52fd2680-dc42-4c08-a26b-293559c8f40a","铭神君(3)",1207572l, 26319347l,100992l,9,1.0f,"上一个版本的殿堂守护者<br>&nbsp;下一步是10000","火","多重攻击-浮生百刃-魔王天赋","","","",5064l,STORY_NPC, db);
        insertNPC("60b2b985-4652-4528-8d15-79d80d3ab949","勇者闯迷宫萌萌哒人工智能接待机器人①号",14569l, 8879942l,68610l,0,2.0f,"我是初音 你们可以叫我公主殿下。<br>谁也不能代替我的存在！因为我是独一无二的。","无","勇者之击-群殴-","","","",4435l,STORY_NPC, db);
        insertNPC("c56005b2-62ff-4424-971c-53baffcfe529","小贺",51120l, 250125l,47098l,0,0.0f,"上一个版本的殿堂守护者<br>&nbsp;我的大屌早已饥渴难耐了1","无","魔王天赋-强化-闪电","","","",449l,STORY_NPC, db);
        insertNPC("5dc94ed7-1d8a-404e-b42c-148615faf2db","深蓝深蓝",6271236l, 8765995l,4263293l,58,2.0f,"上一个版本的殿堂守护者<br>&nbsp;真无限爬层","火","咆哮-虚无吞噬-重击","","","",31368l,STORY_NPC, db);
        insertNPC("016b7a8e-0848-4b9a-b52f-26a881a8eb94","boy(7)",505013503l, 12470880488l,2157926774l,90,90.0f,"上一个版本的殿堂守护者<br>&nbsp;阳光总在风雨后~","金","浮生百刃-虚无吞噬-多重攻击","","","",41714l,STORY_NPC, db);
        insertNPC("7698b0fe-6085-4fc8-a26e-b1e6243707f8","是不是名字长了就可以果断牛逼(1)",3346070l, 115956110l,609608l,0,1.0f,"上一个版本的殿堂守护者<br>&nbsp;哈哈哈哈哈哈","土","勇者之击-浮生百刃-虚无吞噬","","","",6825l,STORY_NPC, db);
        insertNPC("6b5ecd6d-b626-4dad-98b6-9a5ed86d0ecb","黄昏之狼",1811672l, 3289914l,727776l,2,2.0f,"上一个版本的殿堂守护者<br>&nbsp;你將止步於此！","无","原能力-浮生百刃-虚无吞噬","","","",3634l,STORY_NPC, db);
        insertNPC("bd55641e-bed2-44e5-9d02-53595c9c4d71","乐枫",4275251l, 13777287l,436459l,58,30.0f,"上一个版本的殿堂守护者<br>&nbsp;吃饱了，睡觉(∩＿∩)","水","原能力-浮生百刃-错位","","","",16675l,STORY_NPC, db);
        insertNPC("49430eae-24ad-422c-9400-2eae65dbdf6a","👾(1)",2163523l, 25716994l,4015508l,74,91.0f,"上一个版本的殿堂守护者<br>&nbsp;😡都让开😡我要定制！","无","浮生百刃-虚无吞噬-魔王天赋","","","",35450l,STORY_NPC, db);
        insertNPC("9ae50cf4-b91a-46f1-8b20-c7ab887acb51","Hero`💀方航(9)",22494855l, 1407072464l,209289974l,0,4.0f,"上一个版本的殿堂守护者<br>&nbsp;💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀 💀","火","浮生百刃-原能力-定身","","","",6148l,STORY_NPC, db);
        insertNPC("738028fd-ea7a-4bf1-9b77-bcb3c8143889","#神?#🌻致命一击🌻",81426l, 392519115l,2614264l,23,90.0f,"上一个版本的殿堂守护者<br>&nbsp;🚀🚄🚉🚃🚗🚕🚓🚒🚑🚙🚲","金","浮生百刃-原能力-勇者之击","","","",238934l,STORY_NPC, db);
        insertNPC("263abdbf-814b-455e-a527-4480c4fc4683","#神?#谁说非要有五行属性",13240l, 760468l,42010l,0,0.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，你將止步於此！","无","捕捉术-勇者之击-浮生百刃","","","",40002l,STORY_NPC, db);
        insertNPC("b5f98ab0-5825-479a-ae05-58fa676229b8","谜离阿",346l, 258145l,109391l,0,0.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見","无","勇者之击-欺诈游戏-超能量","","","",330l,STORY_NPC, db);
        insertNPC("54aef667-95a6-4c5e-a320-1c359f6f3917","居然可以改名😨(3)",28711404l, 2434930003l,656032764l,90,30.0f,"上一个版本的殿堂守护者<br>&nbsp;😱😱😱","无","虚无吞噬-勇者之击-浮生百刃","","","",16688l,STORY_NPC, db);
        insertNPC("fb17a104-4302-4932-b437-539ee765f18f","勇敢的懦夫",5033519l, 22310847l,5160686l,90,90.0f,"上一个版本的殿堂守护者<br>&nbsp;苦不苦，想想红军两万五！","木","勇者之击-浮生百刃-虚无吞噬","","","",27477l,STORY_NPC, db);
        insertNPC("e57cab1e-bcf5-47b3-a367-86c9d371ea1b","伯符鸟(2)",11353938l, 54456713l,10974657l,51,92.0f,"上一个版本的殿堂守护者<br>&nbsp;德玛西亚！！！","金","浮生百刃-虚无吞噬-勇者之击","","","",23993l,STORY_NPC, db);
        insertNPC("7e61cfa4-e2bc-4731-a0a8-8c32c6ecf5cb","劍🔯聖(1)",255949l, 11867162l,467676l,51,5.0f,"上一个版本的殿堂守护者<br>&nbsp;哇咔咔   我叫大魔王","金","浮生百刃-虚无吞噬-勇者之击","","","",8944l,STORY_NPC, db);
        insertNPC("25076fe2-facb-4632-87a6-6072b2e3c719","反对者(4)",394991014l, 11486019294l,7029433560l,90,17.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見逼大的了，你上！","金","腐蚀-多重攻击-闪电","","","",40910l,STORY_NPC, db);
        insertNPC("eae03a1a-689d-4cbf-807b-ea481704d660","小堕落龙",7380811l, 125182995l,9239529l,24,1.0f,"上一个版本的殿堂守护者<br>&nbsp;没了😔继续上吧😄不到5万心不死啊😊。。。。。。。。","土","原能力-咆哮-浮生百刃","","","",32500l,STORY_NPC, db);
        insertNPC("4ce0d920-336b-45b7-a148-798d23aabf10","蔚蓝(3)",65106651l, 345853625l,19042614l,51,0.0f,"上一个版本的殿堂守护者<br>&nbsp;帅气！","金","群殴-浮生百刃-重击","","","",30999l,STORY_NPC, db);
        insertNPC("d5f4bf99-eda3-495c-8a5b-eccaf08d591e","#神?#啪嗒砰",103800l, 86732355l,1450382l,90,86.0f,"啪嗒砰：要是你能追到我，我就让你嘿嘿嘿。<br>金刚：要是你被我追上，我就把你嘿嘿嘿！","金","勇者之击-原能力-浮生百刃","","","",39500l,STORY_NPC, db);
        insertNPC("5d1bef8e-a5ab-4e92-9369-d8c5d4f5b6a3","兔兔(2)",13525l, 341830l,113l,0,0.0f,"上一个版本的殿堂守护者<br>&nbsp;好好搞","无","--","","","",138l,STORY_NPC, db);
        insertNPC("d93377f7-4bb2-402c-a827-f6e44c281d33","绿色史莱姆(5)",6155695405l, 48482640519l,5243362024l,87,90.0f,"上一个版本的殿堂守护者<br>&nbsp;啦啦啦","金","魔王天赋-多重攻击-生命吸收","","","",24858l,STORY_NPC, db);
        insertNPC("6d94dcac-9793-4632-b1db-0d341df43a2c","巴西",96024l, 1853872l,1001705l,0,6.0f,"上一个版本的殿堂守护者<br>&nbsp;叫我爸爸","无","闪电-魔王天赋-欺诈游戏","","","",1389l,STORY_NPC, db);
        insertNPC("cacdc836-c968-4637-b9d5-f1f441c43bf6","10086+1(1)",20643833l, 111543666l,10479890l,0,0.0f,"上一个版本的殿堂守护者<br>&nbsp;遇見了吾，","土","--","","","",9620l,STORY_NPC, db);
        insertNPC("628d00d2-c461-44b2-a057-3eec8b5c181f","#神?#逗比大作战(3)",133877l, 139200141l,2093120l,51,90.0f,"上一个版本的殿堂守护者<br>&nbsp;万四","金","浮生百刃-错位-咆哮","","","",39015l,STORY_NPC, db);
        insertNPC("869c5f10-03ff-45e4-b46c-58a1abca93c4","谜离的你迷离的眼(2)",6595615l, 976204262l,120660420l,90,0.0f,"上一个版本的殿堂守护者<br>&nbsp;💀","金","勇者之击-原能力-浮生百刃","","","",24909l,STORY_NPC, db);
        insertNPC("d6ac0f9f-da4a-4110-a6fb-bd37e73531ed","初音公主殿下",454125l, 7132305l,3204306l,0,0.0f,"上一个版本的殿堂守护者<br>&nbsp;我就是我，无人可替，我就是初音未来","金","勇者之击-浮生百刃-咆哮","","","",10445l,STORY_NPC, db);
        insertNPC("a2a22f9b-d24c-4de6-9b51-d7c8504c7a1d","#神?#传说中的🐮魔王(7)",200230356366l, 5319205839691l,271622489141l,50,60.0f,"人生一场虚空大梦，韶华白首，不过转瞬。惟有天道恒在，往复循环，不曾更改！","火","生命吸收-魔王天赋-多重攻击","","传说中的🐮魔王<br><font color=\"\"#FF4500\"\">唯一</font>","",201153l,STORY_NPC, db);
        insertNPC("bfed85a8-d030-42b6-b78c-0c052e3480e6","云动",7454834l, 78542073l,9137628l,90,90.0f,"上一个版本的殿堂守护者<br>&nbsp;逗比大作战","金","--","","","",18678l,STORY_NPC, db);
        insertNPC("815ce416-d569-41b3-9305-3a9ecf0fdd96","HERO(1)",7507689l, 48988996l,75951l,17,0.0f,"上一个版本的殿堂守护者<br>&nbsp;德玛西亚！","火","群殴-浮生百刃-勇者之击","","","",6629l,STORY_NPC, db);
        insertNPC("2ea4de7d-589b-45be-b447-9e3670885a43","爱吃肉的土豆",18125461l, 795403416l,3543562l,0,1.0f,"上一个版本的殿堂守护者<br>&nbsp;下次应该是3000层了！","火","强化-水波-生命吸收","","","",2365l,STORY_NPC, db);
        insertNPC("f93f67c7-5857-4991-8b64-eb27dcac5d56","🙌🙏🙅🙆",198775l, 5789935l,39463l,4,0.0f,"上一个版本的殿堂守护者<br>&nbsp;😘","无","错位-裂空剑-重击","","","",1090l,STORY_NPC, db);
        insertNPC("a6408316-5b6e-47f0-8b20-1ee00abba337","飘逸风尘(2)",119846197l, 23106467398l,118912211l,90,90.0f,"上一个版本的殿堂守护者<br>&nbsp;😨上面都是20万以上怎么玩","火","浮生百刃-原能力-勇者之击","","","",69704l,STORY_NPC, db);
        insertNPC("cd363cd9-6a29-4044-b21f-a9760ee03071","赤いの魔王さま(3)",41436094448l, 256104936956l,57482088891l,86,92.0f,"上一个版本的殿堂守护者<br>&nbsp;吃屎啦","火","多重攻击-水波-生命吸收","","","",117345l,STORY_NPC, db);
        insertNPC("cd363cd9-6a29-4044-b21f-a9760ee03071","沁玟",141436094448l, 22256104936956l,57482088l,96,92.0f,"&nbsp;传说中的群主，无法捕获，外号包租婆，绝招是发红包！击败他之后你就可以调戏他了。","火","多重攻击-水波-生命吸收","","","Master",517345l,STORY_NPC, db);
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

    public static int foundPrce(){
        Cursor cursor = DBHelper.getDbHelper().excuseSOL("select count(*) from npc where type = 0");
        int prec = 0;
        if(!cursor.isAfterLast()){
            int total = cursor.getInt(0);
            cursor.close();
            cursor = DBHelper.getDbHelper().excuseSOL("select count(*) from npc where type = 0 and found = 1");
            if(!cursor.isAfterLast()){
                int found = cursor.getInt(0);
                prec = found * 100 / total;
            }
        }
        if(!cursor.isClosed()){
            cursor.close();
        }
        return prec;
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

    public static void deleteNPC(int type){
        DBHelper.getDbHelper().excuseSQLWithoutResult("delete from npc where type = " + type);
    }

    public static List<NPC> loadNPCByType(int type) {
        Cursor cursor;
        if (type == -1) {
            cursor = DBHelper.getDbHelper().excuseSOL("select * from npc order by lev");
        } else {
            cursor = DBHelper.getDbHelper().excuseSOL("select * from npc where type = " + type + " order by lev DESC");
        }
        List<NPC> npcs = new ArrayList<NPC>(cursor.getCount());
        while (!cursor.isAfterLast()) {
            NPC npc = buildNpc(cursor);
            if(npc!=null){
            npcs.add(npc);
            }
            cursor.moveToNext();
        }
        cursor.close();
        return npcs;
    }

    private static NPC buildNpc(Cursor cursor) {
        try{
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
        }catch (Exception e){
            LogHelper.logException(e);
        }
        return null;
    }

    public String getSimpleHTMLDesc() {
        return lev + " - <font color=\"red\"><b>" + name + "</b></font> (" + element + ")<br><font color=\"#87CEEB\">&nbsp;&nbsp;" + desc + "</font><br>";
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
        if(cursor.isAfterLast()){
            cursor.close();
            cursor = DBHelper.getDbHelper().excuseSOL("select * from npc where lev = " + level);
        }
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
        builder.append("<font color=\"#87CEFA\">").append(desc);
        if(StringUtils.isNotEmpty(accName)) {
            builder.append("<br>").append("可以获得装备‘").append(accName).append("’的配方");
        }
        if(StringUtils.isNotEmpty(achName)){
            builder.append("<br>").append("可以获得成就").append(achName);
        }
        builder.append("</font>");
        return builder.toString();
    }

    public Boolean getFound() {
        return found;
    }

    public void setFound(Boolean found) {
        this.found = found;
    }
}
