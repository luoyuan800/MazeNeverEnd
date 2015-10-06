package cn.gavin.forge.list;

import cn.gavin.Hero;
import cn.gavin.monster.Monster;
import cn.gavin.utils.Random;
import cn.gavin.utils.StringUtils;

/**
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED
 * Created by luoyuan on 10/4/15.
 */
public enum ItemName {
    青檀木(ItemName.木材),
    红檀木(ItemName.木材),
    紫熏木(ItemName.木材),
    龙须木(ItemName.木材),
    冷杉木(ItemName.木材),
    凤凰木(ItemName.木材),
    白杏木(ItemName.木材),
    虎皮(ItemName.皮毛),
    龙皮(ItemName.皮毛),
    蛟皮(ItemName.皮毛),
    蛇皮(ItemName.皮毛),
    鼠皮(ItemName.皮毛),
    牛皮(ItemName.皮毛),
    狼皮(ItemName.皮毛),
    龟壳(ItemName.皮毛),
    食人鸟毛(ItemName.皮毛),
    凤凰毛(ItemName.皮毛),
    精铁(ItemName.石头),
    原石(ItemName.石头),
    钢石(ItemName.石头),
    黑石(ItemName.石头),
    铜矿石(ItemName.石头),
    玄石(ItemName.石头),
    萤石(ItemName.石头),
    硝石(ItemName.石头),
    银矿石(ItemName.石头),
    铁矿石(ItemName.石头),
    白云石(ItemName.石头),
    龙筋(ItemName.筋),
    蛇筋(ItemName.筋),
    虎筋(ItemName.筋),
    牛筋(ItemName.筋),
    鼠筋(ItemName.筋),
    蚁须(ItemName.筋),
    龙须(ItemName.筋),
    狼筋(ItemName.筋),
    蛇骨(ItemName.骨头),
    龙骨(ItemName.骨头),
    虎骨(ItemName.骨头),
    鼠骨(ItemName.骨头),
    牛骨(ItemName.骨头),
    EMPTY(ItemName.筋);
    private int type;

    private ItemName(int type) {
        this.type = type;
    }

    public boolean perform(Hero hero, Monster monster) {
        Random random = hero.getRandom();
        if (random.nextInt(1000) < 3) {
            if (monster.getMaterial() < 1000) {
                return random.nextLong(hero.getAgility() + monster.getAtk() + 1) - 1000 > random.nextLong(monster.getMaxHP() + hero.getAttackValue() + 1) + 2000;
            } else {
                return random.nextLong(Math.abs(monster.getAtk() - hero.getAttackValue()) + 1) - 100000 > random.nextLong(Math.abs(hero.getHp() - monster.getMaxHP()) + 1) + 2000;
            }
        } else {
            return false;
        }
    }

    public static final int 木材 = 0, 皮毛 = 1, 石头 = 2, 骨头 = 3, 筋 = 4;

    private int getType() {
        return type;
    }

    public static ItemName valueOfName(String name) {
        if (StringUtils.isNotEmpty(name)) {
            for (ItemName itemName : values()) {
                if (itemName.name().equalsIgnoreCase(name.trim())) {
                    return itemName;
                }
            }
        }
        return null;
    }
}
