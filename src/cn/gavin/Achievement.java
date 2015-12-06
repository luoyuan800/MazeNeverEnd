package cn.gavin;

import cn.gavin.activity.MainGameActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gluo on 8/28/2015.
 */
public enum Achievement {
    reBird("重生", "珍惜生命，不要随便就转生啦~", 0, 0, 0, 0, "转生", 0, 0f, 0),
    click10000("点击中手", "手抽筋了吧？点击次数达到10000次.增加2点击奖励", 0, 0, 0, 2, "点击次数达到10000次", 0, 0f, 0),
    click50000("点击高手", "手抖都要点？点击次数达到50000次。增加4点击奖励。", 0, 0, 0, 4, "点击次数达到50000次", 0, 0f, 0),
    click100000("点击达人", "你是还是单身吧？点击次数达到100000次.增加5点击奖励", 0, 0, 0, 6, "点击次数达到100000次", 0, 0f, 0),
    click100("点击新手", "点击次数达到100次，无任何奖励", 0, 0, 0, 0, "点击次数达到100次", 0, 0f, 0),
    unbeaten("不败", "只能说你运气太好了，连胜次数达到100。力量+10", 10, 0, 0, 0, "连胜次数达到100", 0, 0f, 0),
    maltreat("受虐狂", "被打败次数达到10000。力量+5", 5, 0, 0, 0, "被打败次数达到10000", 0, 0f, 0),
    maze50("菜鸟", "到达迷宫50层", 0, 0, 0, 0, "到达迷宫50层", 0, 0f, 0),
    maze100("新手", "到达迷宫100层。敏捷+20", 0, 0, 20, 0, "到达迷宫100层", 0, 0f, 0),
    maze500("不是新人", "到达迷宫500层，敏捷+20", 0, 20, 0, 0, "到达迷宫500层", 0, 0f, 0),
    maze1000("高手", "真难得，竟然到达迷宫1000层了。三围点击奖励各加20", 20, 20, 20, 20, "到达迷宫1000层了", 0, 0f, 0),
    maze10000("无敌", "到达迷宫10000层了……三围点击奖励各减20！", -20, -20, -20, -20, "到达迷宫10000层了", 0, 0f, 0),
    maze50000("无聊之人", "你真的好无聊，这么闷都玩到迷宫50000层了。三围点击奖励各加1000", 1000, 1000, 1000, 1000, "迷宫50000层", 0, 0f, 0),
    richer("有钱人", "你好帅好有钱哦，成功内购一次。增加5点击奖励", 0, 0, 0, 5, "踩/赞一次", 0, 0f, 0),
    extreme("至尊", "偏科的典范，攻击力达到上限了。敏捷减少1000", 0, 0, -1000, 0, "攻击力达到上限", 0, 0f, 0),
    master("至强", "偏科的典范，防御达到上限了。力量减少1000", -1000, 0, 0, 0, "防御达到上限", 0, 0f, 0),
    lazy("懒惰", "持有点数5000点都不给分配，你是想怎么样玩这个游戏呢？", 0, 0, 0, 0, "持有点数5000点", 0, 0f, 0),
    linger("流连忘返", "这么烂的游戏你还玩第二遍……成功读取一次存档.很遗憾的告诉你这个成就并没有任何奖励~", 0, 0, 0, 0, "读取一次存档", 0, 0f, 0),
    rich("富裕", "持有5000000锻造点数。小心！锻造点数太多会吸引那些强大又贪婪的怪物或者人。", 0, 0, 0, 0, "持有5000000锻造点数", 0, 0f, 0),
    skilldness("技能达人", "敏捷决定了释放技能几率，不过我得告诉你敏捷再高也不会有100%释放的可能……。", 0, 0, 0, 0, "敏捷10000以上", 0, 0f, 0),
    speculator("投机者", "不早不晚，刚刚好。到达迷宫500层的时候武器和防具的等级均为0。体力，敏捷各加500", 0, 500, 500, 0, "到达迷宫500层的时候武器和防具的等级均为0", 0, 0f, 0),
    fearDeath("怕死的人", "害怕失败，所以你把防御加了10000点以上，敏捷-20", 0, 0, -20, 0, "防御10000点以上", 0, 0f, 0),
    doctor("吸血鬼", "使用了10000次以上的生命吸收技能,很遗憾，这个木有任何附加效果。", 0, 0, 0, 0, "使用了10000次以上的生命吸收技能", 0, 0f, 0),
    hitter("重拳", "在普通攻击中使出了暴击", 0, 0, 0, 0, "在普通攻击中触发了暴击", 0, 0f, 0),
    uploader("守护者", "分享你创建的角色到服务，下个版本你就可以和自己战斗了。敏捷+10", 0, 0, 10, 0, "分享你创建的角色到服务", 0, 0f, 0),
    updater("更新", "更新了游戏版本，快去看看你上传的角色在不在某层迷宫的的某个角落呆着。敏捷+10", 0, 0, 10, 0, "更新了游戏版本", 0, 0f, 0),
    crapGame("烂游戏", "这么差劲的游戏你也愿意出钱玩，三围各加10。", 10, 10, 10, 0, "踩/赞了N次", 0, 0f, 0),
    goldColor("土豪金", "全身上下都是土豪金。增加10点击奖励", 0, 0, 0, 10, "防具升级为金甲", 0, 0f, 0),
    artifact("神器", "防具或者武器升级到最高级！减100点敏捷", 0, 0, -100, 0, "防具或者武器升级到最高级", 0, 0f, 0),
    hero("勇者", "学习并且使用了勇者技能,增加10点力量", 10, 0, 0, 0, "学习并且使用了勇者技能", 0, 0f, 0),
    devils("魔王", "学习并且使用了了魔王技能，并没有任何其他效果！", 0, 0, 0, 0, "学习魔王系的技能并且使用它", 0, 0f, 0),
    R_hero("真勇者", "学习并且使用了最后一个勇者职业的技能,无特殊效果!", 0, 0, 0, 0, "学习并且使用了最后一个勇者职业的技能", 0, 0f, 0),
    satan("撒旦", "学习并且使用了了最后一个魔王职业技能, 减少10点体力", -10, 0, 0, 0, "学习并且使用了了最后一个魔王职业技能", 0, 0f, 0),
    story("主线剧情", "恭喜你触发了主线剧情！什么？你以为这个游戏没有主线？那你就错了！", 0, 0, 0, 0, "隐藏剧情解锁", 0, 0f, 0),
    swindler("欺诈师", "欺骗，是你的生存法则吗？增加100点敏捷", 0, 0, 100, 0, "学习并且使用了欺诈师技能", 0, 0f, 0),
    cribber("作弊者", "作弊可耻！修改游戏也是很无聊的！你的游戏数据被判定为异常！强制减少10亿的三围。", -1000000000, -1000000000, -100000000, 0, "这是一个可耻的标记,修改游戏后有可能解锁", 0, 0f, 0),
    dragon("屠龙者", "在游戏中击败了1000条龙！这个成就纯粹看运气的了……解锁后可以学习龙裔系的技能", 0, 0, 0, 0, "在游戏中击败1000条龙", 0, 0f, 0),
    guider1("新手指导", "你遇见了NPC袁酥兄，他告诉了你这个游戏的基本玩法，" +
            "比如点击右上角的名字栏你可以更改名字或者输入隐藏代码获取奖励、可以长按战斗信息栏隐藏战斗信息、" +
            "可以点击人物图标获取奖励的锻造点数和能力点数、可以点击锁头图标开启宝箱、点击技能图标可以加速技能的升级。", 0, 0, 0, 0, "遇见剧情NPC解锁", 0, 0f, 0),
    guider2("NPC", "你再次遇见了NPC袁酥兄，但没想到他拿走了你四把钥匙并且悄悄的告诉你，" +
            "已经没有什么可以教给你的了，有问题就去问其他人吧……", 0, 0, 0, 0, "遇见剧情NPC解锁", 0, 0f, 0),
    guider3("打造攻略", "你再再次遇见了NPC袁酥兄，我知道你很想揍他，但他还是拿走了你一半的钥匙并且悄悄的告诉你，" +
            "融合材料的时候尽量使用属性相同的材料才会有更高几率生成高属性的材料哦、" +
            "拆解装备得出来的材料也有可能比直接掉落的单个材料好的哦、这个世界的橙装数量非常少并且打造成功率非常低，所以打造不出来也请不要诅咒作者买方便面没有调料包哦。", 0, 0, 0, 0, "遇见剧情NPC解锁", 0, 0f, 0),
    five("五行", "你遇见了NPC龙剑森，他赠送了你三把钥匙并且告诉了五行的秘密。在这个世界里，" +
            "人物和怪物的五行属性会影响攻击和防御的计算。而装备的五行属性会影响隐藏属性的激活。", 0, 0, 0, 0, "遇见隐藏NPC解锁", 0, 0f, 0),
    reinforce("相生", "你又遇见了NPC龙剑森，他又赠送了你三把钥匙，虽然你不是很想要，但是你没办法拒绝……" +
            "因为他是NPC。而且他还会告诉你错误的五行相生：金生水，木生水，火生金，土生金，水生木。请你不要相信他……", 0, 0, 0, 0, "遇见隐藏NPC解锁", 0, 0f, 0),
    restriction("相克", "你又遇见了那个烦人的NPC龙剑森，他再赠送了你三把钥匙，虽然你非常的不想要，但他还是把钥匙砸在了你的脸上……" +
            "因为他是NPC。而且他还会告诉你五行相克：金克木，木克土，土克水，水克火，火克金，无被所有克。那么问题来了，为什么这些NPC都要躲在门后面呢？", 0, 0, 0, 0, "遇见隐藏NPC解锁", 0, 0f, 0),
    palace("殿堂屠夫", "击败殿堂里面的所有人，增加10点击奖励", 0, 0, 0, 10, "通关一次殿堂。", 0, 0f, 0),
    pet("宠物", "成功抓获宠物，激活这个成就后可以学习驯兽师技能。<br>宠物是要放进队伍中才可以培养亲密度，亲密度达到一定程度才会自动攻击敌人或者帮你抵挡伤害。", 0, 0, 0, 0, "捕获一只宠物后解锁", 0, 0f, 0),
    egg("蛋", "获得了一个宠物蛋，激活这个成就后可以学习培育家技能。宠物蛋是要带在身上才会孵化的。", 0, 0, 0, 0, "获得一个宠物蛋后解锁", 0, 0f, 0),
    folk("霸王色", "使用你的王八色霸气吓走了敌人。三围各增加1000", 1000, 1000, 1000, 0, "使用咆哮技能60000次", 0, 0f, 0),
    Long("训龙者", "捕捉龙作为你宠物，你一定是强者！增加宠物捕获几率。", 0, 0, 0, 0, "成功捕捉一条龙", -0.03f, 0f, 0),
    XiaoQian("小强爱好者", "无法想象神马人会喜欢捉蟑螂作为宠物...增加宠物捕获几率。", 0, 0, 0, 0, "捕获蟑螂", -0.13f, 0f, 0),
    ShengShou("神兽", "神兽无法正常捕获，但是你依然拥有神兽作为宠物...增加宠物变异几率。", 0, 0, 0, 0, "拥有朱獳或者梼杌", 0f, 5f, 0),
    FShengShou("次神兽", "次神兽无法正常捕获，但是你依然拥有神兽作为宠物...增加宠物变异几率。", 0, 0, 0, 0, "拥有朱厌或者穷奇", 0f, 5f, 0),
    JiuWeiHU("九尾狐", "九尾狐不能正常捕获，只能通过交换或者奖励获取，但是你依然拥有九尾狐作为宠物...降低宠物捕获率。", 0, 0, 0, 0, "拥有九尾狐", 0.05f, 0f, 0),
    Changer("分享", "成功交换一次宠物，增加宠物生蛋率。", 0, 0, 0, 0, "交换一次宠物", 0f, 0f, 20),
    Searcher("寄存", "成功寄存一次宠物，降低宠物生蛋率。", 0, 0, 0, 0, "寄存一次宠物", 0f, 0f, -10),
    Ant("蚂蚁，蚂蚁！", "一群蚂蚁在那里！。", 0, 0, 0, 0, "捕获嗜血蚁", 0f, 0f, 0),
    Zombie("僵尸王", "僵尸你也敢养……。", 0, 0, 0, 0, "捕获僵尸", 0f, 0f, 0),
    WuSong("武松他弟", "我们不杀老虎，那是国家保护动物，所以我们要圈养他们！", 0, 0, 0, 0, "捕获老虎", 0f, 0f, 0),
    EMPTY("", "", 0, 0, 0, 0, "", 0, 0f, 0);
    private int addStrength;
    private int addPower;
    private int addAgility;
    private String name;
    private String desc;
    private boolean enable;
    private int click;
    private String unlockDesc;
    private float petRate;
    private float petAbe;
    private float eggRate;

    private Achievement(String name, String desc, int addStrength, int addPower, int addAgility, int click, String lockTip, float petRate, float petAbe, float eggRate) {
        this.name = name;
        this.desc = desc;
        this.addStrength = addStrength;
        this.addAgility = addAgility;
        this.addPower = addPower;
        this.enable = false;
        this.click = click;
        this.unlockDesc = lockTip;
        this.petRate = petRate;
        this.petAbe = petAbe;
        this.eggRate = eggRate;
    }

    public void enable() {
        this.enable = true;
    }

    public void enable(Hero hero) {
        if (!this.enable) {
            this.enable = true;
            if (hero != null) {
                hero.addStrength(addStrength);
                hero.addLife(addPower);
                hero.addAgility(addAgility);
                hero.addClickAward(click);
                hero.setPetRate(hero.getPetRate() + petRate);
                hero.setPetAbe(hero.getPetAbe() + petAbe);
                hero.setEggRate(hero.getEggRate() + eggRate);
            }
            MainGameActivity context = MainGameActivity.context;
            if (context != null) {
                context.addMessage("------------------------", "* 获得成就：<font color=\"#D2691E\">" + name + "</font> *", "------------------------");
            }
        }
    }

    public void disable(Hero hero) {
        this.enable = false;
        if (hero != null) {
            hero.addStrength(-addStrength);
            hero.addLife(-addPower);
            hero.addAgility(-addAgility);
            hero.addClickAward(-click);
            hero.setPetRate(hero.getPetRate() - petRate);
            hero.setPetAbe(hero.getPetAbe() - petAbe);
            hero.setEggRate(hero.getEggRate() - eggRate);
        }
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public boolean isEnable() {
        return enable;
    }

    public static List<MainGameActivity.AchievementList> getAchievementListAdp() {
        List<MainGameActivity.AchievementList> rs = new ArrayList<MainGameActivity.AchievementList>(values().length / 3);
        for (int i = 0; i < values().length; i += 4) {
            Achievement a0 = values()[i];
            Achievement a1 = i + 1 < values().length ? values()[i + 1] : EMPTY;
            Achievement a2 = i + 2 < values().length ? values()[i + 2] : EMPTY;
            Achievement a3 = i + 3 < values().length ? values()[i + 3] : EMPTY;
            MainGameActivity.AchievementList list = new MainGameActivity.AchievementList(a0, a1, a2, a3);
            rs.add(list);
        }
        return rs;
    }

    public String getUnlockDesc() {
        return unlockDesc;
    }

    public void setUnlockDesc(String unlockDesc) {
        this.unlockDesc = unlockDesc;
    }
}
