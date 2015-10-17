package cn.gavin;

import java.util.ArrayList;
import java.util.List;

import cn.gavin.activity.MainGameActivity;

/**
 * Created by gluo on 8/28/2015.
 */
public enum Achievement {
    reBird("重生", "珍惜生命，不要随便就转生啦~", 0, 0, 0, 0),
    click10000("点击中手", "手抽筋了吧？点击次数达到10000次", 0, 0, 0, 2),
    click50000("点击高手", "手抖都要点？点击次数达到50000次", 0, 0, 0, 4),
    click100000("点击达人", "你是还是单身吧？点击次数达到100000次", 0, 0, 0, 6),
    click100("点击新手", "点击次数达到100次", 0, 0, 0, 0),
    unbeaten("不败", "只能说你运气太好了，连胜次数达到100", 10, 0, 0, 0),
    maltreat("受虐狂", "被打败次数达到10000", 5, 0, 0, 0),
    maze50("菜鸟", "到达迷宫50层", 0, 0, 0, 0),
    maze100("新手", "到达迷宫100层", 0, 0, 20, 0),
    maze500("不是新人", "到达迷宫500层", 0, 20, 0, 0),
    maze1000("高手", "真难得，竟然到达迷宫1000层了", 20, 20, 20, 20),
    maze10000("无敌", "到达迷宫10000层了……", -20, -20, -20, -20),
    maze50000("无聊之人", "你真的好无聊，这么闷都玩到迷宫50000层了。", 1000, 1000, 1000, 1000),
    richer("有钱人", "你好帅好有钱哦，成功内购一次。", 0, 0, 0, 5),
    extreme("至尊", "偏科的典范，攻击力达到上限了", 0, 0, -1000, 0),
    master("至强", "偏科的典范，防御达到上限了", -1000, 0, 0, 0),
    lazy("懒惰", "持有点数5000点都不给分配，你是想怎么样玩这个游戏呢？", 0, 0, 0, 0),
    linger("流连忘返", "这么烂的游戏你还玩第二遍……成功读取一次存档.很遗憾的告诉你这个成就并没有任何奖励~", 0, 0, 0, 0),
    rich("富裕", "持有5000000锻造点数。小心！锻造点数太多会吸引那些强大又贪婪的怪物或者人的。", 0, 0, 0, 0),
    skilldness("技能达人", "敏捷决定了释放技能几率，不过我得告诉你敏捷再高也不会有100%释放的可能……。", 0, 0, 0, 0),
    speculator("投机者", "不早不晚，刚刚好。到达迷宫100层的时候武器和防具的等级均为0", 0, 100, 100, 0),
    fearDeath("怕死的人", "害怕失败，所以你把防御加了10000点以上", 0, 0, -20, 0),
    doctor("吸血鬼", "使用了10000次以上的生命吸收技能", 0, 0, 0, 0),
    hitter("重拳", "在普通攻击中使出了暴击", 0, 0, 0, 0),
    uploader("守护者", "分享你创建的角色到服务，下个版本你就可以和自己战斗了。", 0, 0, 10, 0),
    updater("更新", "更新了游戏版本，快去看看你上传的角色在不在某层迷宫的的某个角落呆着。", 0, 0, 10, 0),
    crapGame("烂游戏", "这么差劲的游戏你也愿意出钱玩，内购了50次。", 10, 10, 10, 0),
    goldColor("土豪金", "全身上下都是土豪金，防具升级为金。", 0, 0, 0, 10),
    artifact("神器", "防具或者武器升级到最高级！", 0, 0, -100, 0),
    hero("勇者", "学习并且使用了勇者技能", 10, 0, 0, 0),
    devils("魔王", "学习并且使用了了魔王技能", 0, 0, 0, 0),
    R_hero("真勇者", "学习并且使用了最后一个勇者职业的技能", 0, 0, 0, 0),
    satan("撒旦", "学习并且使用了了最后一个魔王职业技能", -10, 0, 0, 0),
    story("主线剧情", "恭喜你触发了主线剧情！什么？你以为这个游戏没有主线？那你就错了！", 0, 0, 0, 0),
    swindler("欺诈师", "欺骗，是你的生存法则吗？学习并且使用了欺诈师技能！", 0, 0, 100, 0),
    cribber("作弊者", "作弊可耻！修改游戏也是很无聊的！你的游戏数据被判定为异常！", -10000000, -10000000, -100000000,0),
    dragon("屠龙者", "在游戏中击败了1000条龙！这个成就纯粹看运气的了……", 0, 0, 0,0),
    EMPTY("", "", 0, 0, 0, 0);
    private int addStrength;
    private int addPower;
    private int addAgility;
    private String name;
    private String desc;
    private boolean enable;
    private int click;

    private Achievement(String name, String desc, int addStrength, int addPower, int addAgility, int click) {
        this.name = name;
        this.desc = desc;
        this.addStrength = addStrength;
        this.addAgility = addAgility;
        this.addPower = addPower;
        this.enable = false;
        this.click = click;
    }

    public void enable() {
        this.enable = true;
    }

    public void enable(Hero hero) {
        if (!this.enable) {
            this.enable = true;
            if(hero!=null) {
                hero.addStrength(addStrength);
                hero.addLife(addPower);
                hero.addAgility(addAgility);
                hero.addClickAward(click);
            }
            MainGameActivity context = MainGameActivity.context;
            if(context!=null) {
                context.addMessage("------------------------", "* 获得成就：<font color=\"#D2691E\">" + name + "</font> *", "------------------------");
            }
        }
    }

    public void disable(Hero hero) {
        this.enable = false;
        hero.addStrength(-addStrength);
        hero.addLife(-addPower);
        hero.addAgility(-addAgility);
        hero.addClickAward(-click);
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
}
