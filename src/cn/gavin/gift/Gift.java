package cn.gavin.gift;

import cn.gavin.R;

/**
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED
 * Created by luoyuan on 12/25/15.
 */
public enum Gift {
    HeroHeart("勇敢的心", "一根筋的热血埋藏在你的心中，这个天赋会提升攻击成长", 0, R.id.hero_heart),
    DarkHeard("暗黑之心", "天生的黑暗心灵，连恒河水都洗不干净。这个天赋会提升防御成长", 0, R.id.DarkHeard),
    Warrior("战士", "勇敢向前绝对不会害怕，放弃技能注重身体属性成长！你最多只能使用三个技能，但是hp、攻击、防御成长翻倍（一转后才可以选择）。", 1, R.id.Warrior),
    Searcher("守财奴", "就算死，也要守护自己的财产！囤积锻造点数也不会招引强力的怪物（一转后才可以选择）。", 1, R.id.Searcher),
    Long("龙裔", "声称自己是龙的传人，但是却因为脸黑没有人相信你。默认激活龙裔技能，并且会变得脸黑。", 0, R.id.Long),
    Element("元素之心", "据说从一出生的时候就体内就被植入了奇怪的东西从而可以操纵元素能量。可以在不满足条件的情况下激活元素使职业。", 0, R.id.Element),
    Pokemon("神奇宝贝", "Pokemon Monster的忠实玩家，以至于产生了这个奇怪的天赋。宠物捕获率提高50（这个不是百分比）。", 0, R.id.Pokemon),
    FireBody("火焰身躯", "这不是一个攻击或者防御加成的技能，绝对不会伤害触摸到你身体的人，只能够增加120的宠物的生蛋率（这个不是百分比）。", 0, R.id.FireBody),
    SkillMaster("技能大师", "天生脸皮厚，所以你可以拥有比别人更高的技能释放概率，所有主动技能的释放概率提升10%（四转后才可选择）。", 4, R.id.SkillMaster),
    RandomMaster("冒险之心", "喜欢变幻莫测的未知事物，喜欢探索各个隐秘的角落。增加随机事件、随机人物出现的概率（二转后才可选择）。", 2, R.id.RandomMaster),
    ElementReject("元素抗性", "你讨厌五行的复杂，所以决定抛弃所有的元素变化，激活天赋后随机选定一个元素属性，遇见的敌人是这个元素的时候，" +
            "你有80%的几率完全抵消伤害，并且当自身属性为无的时候，有55%的几率秒杀敌人（三转后才可选择）。", 3, R.id.ElementReject),
    Maker("匠心", "你要用手中的铁锤敲出你想要的妹子或者帅哥。打造装备时带颜色装备的几率增加15%（二转后才可以选择）。", 2, R.id.Maker),
    ChildrenKing("孩子王", "永远保持童真，任何事物在你眼中都是新奇的，世界只是你手中的玩具（五十转后才可以选择）。", 50, R.id.ChildrenKing),
    Daddy("奶爸", "超级奶爸，不分昼夜的换尿布是你的天赋。每当因为受到伤害生命值变成负数的时候即刻增加10%的生命，如果可以抵消负数值，" +
            "你就可以避免挂掉。（二十五转后才可以选择）。", 25, R.id.Daddy),
    Epicure("美食家", "拥有这个天赋你就可以制作煎蛋了……（五转后才可以选择）。", 5, R.id.Epicure);

    private String name;
    private String desc;
    private int recount;
    private int view;

    private Gift(String name, String desc, int recount, int view) {
        this.name = name;
        this.desc = desc;
        this.recount = recount;
        this.view = view;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public int getRecount() {
        return recount;
    }

    public int getView() {
        return view;
    }
}
