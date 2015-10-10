package cn.gavin.maze;

import cn.gavin.Achievement;
import cn.gavin.Hero;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.forge.Item;
import cn.gavin.forge.list.ItemName;
import cn.gavin.monster.Monster;
import cn.gavin.monster.MonsterBook;
import cn.gavin.skill.Skill;
import cn.gavin.skill.SkillFactory;
import cn.gavin.story.StoryHelper;
import cn.gavin.utils.Random;
import cn.gavin.utils.StringUtils;

/**
 * Created by gluo on 8/26/2015.
 */
public class Maze {
    private int csmgl = 8977;
    private Hero hero;
    protected long level;
    private boolean moving;
    protected long step;
    protected long streaking;
    private float meetRate = 100f;
    private MonsterBook monsterBook;
    private StoryHelper storyHelper;
    protected long lastSave;

    public void setCsmgl(int csmgl) {
        this.csmgl = csmgl;
    }

    public int getCsmgl() {
        return csmgl;
    }

    public boolean isMoving() {
        return moving;
    }

    private Random random = new Random();

    public Maze(){

    }

    public Maze(Hero hero) {
        this.hero = hero;
        storyHelper = new StoryHelper();
    }

    public Maze(Hero hero, MonsterBook monsterBook) {
        this.hero = hero;
        this.monsterBook = monsterBook;
        storyHelper = new StoryHelper();
    }

    public void move(MainGameActivity context) {
        this.monsterBook = context.getMonsterBook();
        while (context!=null && context.isGameThreadRunning()) {
            if (context.isPause()) {
                continue;
            }
            if (storyHelper != null && storyHelper.trigger()) {
                continue;
            }
            moving = true;
            step++;
            if (random.nextLong(10000) > 9985 || step > random.nextLong(22) || random.nextLong(streaking + 1) > 50 + level) {
                if ((level - lastSave) > 50) {
                    lastSave = level;
                    context.save();
                }
                step = 0;
                level++;
                mazeLevelDetect();
                long point = 1 + level / 10 + random.nextLong(level + 1) / 300;
                if(point > 100){
                    point = 100;
                }
                String msg = hero.getFormatName() + "进入了" + level + "层迷宫， 获得了<font color=\"#FF8C00\">" + point + "</font>点数奖励";
                addMessage(context, msg);
                if (level > hero.getMaxMazeLev()) {
                    hero.addMaxMazeLev();
                }

                hero.addPoint(point);
                hero.addHp(random.nextLong(hero.getUpperHp() / 10 + 1) +10);
                addMessage(context, "-------------------");
            } else if (random.nextLong(1000) > 983 && random.nextLong(hero.getAgility()) > random.nextLong(6971)) {
                long mate = random.nextLong(level * 300 + 1) + random.nextLong(hero.getAgility() / 1000 + 100) + 100;
                addMessage(context, hero.getFormatName() + "找到了一个宝箱， 获得了<font color=\"#FF8C00\">" + mate + "</font>材料");
                hero.addMaterial(mate);
                addMessage(context, "-------------------");
            } else if (hero.getHp() < hero.getUpperHp() && random.nextLong(1000) > 985) {
                long hel = random.nextLong(hero.getUpperHp() / 70 + 1) + random.nextLong(hero.getPower() / 500);
                if(hel > hero.getUpperHp()/2){
                    hel = random.nextLong(hero.getUpperHp()/2 + 1) +1;
                }
                addMessage(context, hero.getFormatName() + "休息了一会，恢复了<font color=\"#556B2F\">" + hel + "</font>点HP");
                hero.addHp(hel);
                addMessage(context, "-------------------");
            } else if (random.nextLong(9000) > csmgl) {
                step = 0;
                long levJ = random.nextLong(hero.getMaxMazeLev() + 5) + 1;
                addMessage(context, hero.getFormatName() + "踩到了传送门，被传送到了迷宫第" + levJ + "层");
                level = levJ;
                if (level > hero.getMaxMazeLev()) {
                    hero.setMaxMazeLev(level);
                }
                mazeLevelDetect();
                addMessage(context, "-------------------");
            } else if (random.nextBoolean()) {
                Skill skill1 = SkillFactory.getSkill("隐身", hero, context.getSkillDialog());
                if (skill1.isActive() && skill1.perform()) {
                    continue;
                }
                Monster monster = null;
                if (random.nextLong(1000) > 899) {
                    monster = Monster.getBoss(this, hero);
                    step += 21;
                }
                if (monster == null) {
                    monster = new Monster(hero, this);
                }
                monster.setMazeLev(level);
                String msg = hero.getFormatName() + "遇到了" + monster.getFormatName();
                addMessage(context, msg);
                monster.addBattleDesc(msg);
                boolean atk = hero.getAgility() > monster.getHp() / 2 || random.nextBoolean();
                Skill skill;
                boolean isJump = false;
                while (!isJump && monster.getHp() > 0 && hero.getHp() > 0) {
                    if (context.isPause()) {
                        continue;
                    }
                    if (atk) {
                        skill = hero.useAttackSkill(monster);
                        isJump = false;
                        if (skill != null) {
                            isJump = skill.release(monster);
                        } else {
                            if (hero.getHp() < hero.getUpperHp()) {
                                skill = hero.useRestoreSkill();
                            }
                            if (skill != null) {
                                isJump = skill.release(monster);
                            } else {
                                monster.addHp(-(hero.getAttackValue()));
                                String atkmsg = hero.getFormatName() + "攻击了" + monster.getFormatName() + "，造成了<font color=\"red\">" + hero.getAttackValue() + "</font>点伤害。";
                                addMessage(context, atkmsg);
                            monster.addBattleDesc(atkmsg);
                            }
                        }
                    } else {
                        skill = hero.useDefendSkill(monster);
                        if (skill != null) {
                            isJump = skill.release(monster);
                        } else {
                            long harm = monster.getAtk() - hero.getDefenseValue();
                            if (harm <= 0 || hero.getRandom().nextInt(100) > monster.getHitRate()) {
                                harm = hero.getMaxMazeLev();
                            }
                            if (hero.getRandom().nextInt(100) > monster.getHitRate()) {
                                harm = random.nextLong(level + 1);
                                String s = monster.getFormatName() + "攻击打偏了";
                                addMessage(context, s);
                                monster.addBattleDesc(s);
                            }
                            if (harm >= hero.getHp()) {
                                Skill sy = SkillFactory.getSkill("瞬间移动", hero, context.getSkillDialog());
                                if (sy.isActive() && sy.perform()) {
                                    isJump = sy.release(monster);
                                    continue;
                                } else {
                                    sy = SkillFactory.getSkill("反杀", hero, context.getSkillDialog());
                                    if (sy.isActive() && sy.perform()) {
                                        isJump = sy.release(monster);
                                        continue;
                                    }
                                }
                            }
                            hero.addHp(-harm);
                            if(hero.isParry()){
                                String parrymsg = hero.getFormatName() + "成功格挡一次攻击，减少了当前受到的伤害！";
                                addMessage(context, parrymsg);
                                monster.addBattleDesc(parrymsg);
                            }
                            String defmsg = monster.getFormatName() + "攻击了" + hero.getFormatName() + "，造成了<font color=\"red\">" + harm + "</font>点伤害。";
                            addMessage(context, defmsg);
                            monster.addBattleDesc(defmsg);
                        }
                    }
                    atk = !atk;
                    try {
                        Thread.sleep(context.getRefreshInfoSpeed());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (isJump){
                    continue;
                }
                if (monster.getHp() <= 0) {
                    streaking++;
                    if (streaking >= 100) {
                        Achievement.unbeaten.enable(hero);
                    }
                    String defeatmsg = hero.getFormatName() + "击败了" + monster.getFormatName() + "， 获得了<font color=\"blue\">" + monster.getMaterial() + "</font>份锻造材料。";
                    addMessage(context, defeatmsg);
                    monster.addBattleDesc(defeatmsg);
                    hero.addMaterial(monster.getMaterial());
                    monster.setDefeat(true);
                    //monsterBook.addMonster(monster);
                    StringBuilder items = new StringBuilder();
                    if(monster.getItems()!=null) {
                        for (ItemName item : monster.getItems()) {
                            Item i = Item.buildItem(hero, this, monster);
                            if (i != null) {
                                i.save();
                                items.append(item.name()).append(" ");
                            }
                        }
                        String str = items.toString();
                        if (StringUtils.isNotEmpty(str)) {
                            String itemmsg = hero.getFormatName() + "获得了:" + str;
                            addMessage(context, itemmsg);
                            monster.addBattleDesc(itemmsg);
                        }
                    }
                    monsterBook.addMonster(monster);
                } else {
                    Skill notDieSkill = SkillFactory.getSkill("不死之身", hero, context.getSkillDialog());
                    if (notDieSkill.isActive() && notDieSkill.perform()) {
                        isJump = notDieSkill.release(monster);
                    } else {
                        streaking = 0;
                        step = 0;
                        String defeatedmsg = hero.getFormatName() + "被" + monster.getFormatName() + "打败了，回到迷宫第一层。";
                        addMessage(context, defeatedmsg);
                        monster.addBattleDesc(defeatedmsg);
                        if(level > 25){
                            context.save();
                        }
                        this.level = 1;
                        hero.restore();
                        monster.setDefeat(false);
                        lastSave = level;
                        monsterBook.addMonster(monster);
                    }
                }
                addMessage(context, "-----------------------------");
            }
            try {
                Thread.sleep(context.getRefreshInfoSpeed());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        moving = false;
    }

    private void addMessage(MainGameActivity context, String msg) {
        if(context!=null) {
            context.addMessage(msg);
        }
    }

    private void mazeLevelDetect() {
        if (level > Integer.MAX_VALUE) {
            if (level > Long.MAX_VALUE - 100) {
                level--;
            }
        } else {
            switch ((int) level) {
                case 50:
                    Achievement.maze50.enable(hero);
                    break;
                case 100:
                    Achievement.maze100.enable(hero);
                    break;
                case 500:
                    if (hero.getArmorLev() == 0 && hero.getSwordLev() == 0) {

                    }
                    Achievement.maze500.enable(hero);
                    break;
                case 1000:
                    Achievement.maze1000.enable(hero);
                    break;
                case 10000:
                    Achievement.maze10000.enable(hero);
                    break;
                case 50000:
                    Achievement.maze50000.enable(hero);
                    break;

            }
        }
    }

    public long getLev() {
        return level;
    }

    public void setLevel(long level) {
        mazeLevelDetect();
        this.level = level;
    }

    public float getMeetRate() {
        return meetRate;
    }

    public void setMeetRate(float meetRate) {
        this.meetRate = meetRate;
    }
}
