package cn.gavin.maze;

import cn.gavin.Achievement;
import cn.gavin.Hero;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.db.good.detail.Barrier;
import cn.gavin.db.good.detail.SafetyRope;
import cn.gavin.good.GoodsType;
import cn.gavin.monster.Monster;
import cn.gavin.monster.MonsterBook;
import cn.gavin.pet.Pet;
import cn.gavin.pet.PetDB;
import cn.gavin.pet.skill.PetSkill;
import cn.gavin.skill.Skill;
import cn.gavin.skill.SkillFactory;
import cn.gavin.story.StoryHelper;
import cn.gavin.utils.MazeContents;
import cn.gavin.utils.Random;

import java.util.List;

/**
 * Created by gluo on 8/26/2015.
 */
public class Maze {
    private int csmgl = 9987;
    private Hero hero;
    protected long level;
    private boolean moving;
    protected long step;
    protected long streaking;
    private float meetRate = 100f;
    private MonsterBook monsterBook;
    private StoryHelper storyHelper;
    protected long lastSave;
    private long hunt = 150;
    private int petRate = 1;
    private boolean sailed = false;

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

    public Maze() {

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
        while (context != null && context.isGameThreadRunning()) {
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
                if(hero.getMaxMazeLev() < 500){
                    point *= 3;
                }
                if (point > 70) {
                    point = 40 + random.nextInt(70);
                }
                String msg = hero.getFormatName() + "进入了" + level + "层迷宫， 获得了<font color=\"#FF8C00\">" + point + "</font>点数奖励";
                for (Pet pet : hero.getPets()) {
                    pet.click();
                    if (pet.getType().equals("蛋")) {
                        pet.setDeathCount(pet.getDeathCount() - hero.getEggStep());
                        if (pet.getDeathCount() <= 0) {
                            addMessage(context, pet.getFormatName() + "出生了！");
                            int index = Monster.getIndex(pet.getName());
                            if (index < Monster.lastNames.length) {
                                pet.setType(Monster.lastNames[index]);
                            } else {
                                pet.setType(pet.getName());
                            }
                            pet.setLev(level);
                            pet.setDeathCount(0);
                            PetDB.save(pet);
                        }
                    } else if (!"蛋".equals(pet.getType()) && pet.getHp() > 0) {
                        if (pet.getSkill() != null) {
                            if (pet.getSkill() instanceof PetSkill) {
                                ((PetSkill) pet.getSkill()).release(hero);
                            }
                        }
                    }
                }
                addMessage(context, msg);
                if (level > hero.getMaxMazeLev()) {
                    hero.setMaxMazeLev(level);
                }

                hero.addPoint(point);
                hero.addHp(random.nextLong(hero.getUpperHp() / 10 + 1) + random.nextLong(hero.getPower() / 500));

                addMessage(context, "-------------------");
            } else if (random.nextLong(850 + hunt) > 993 && random.nextLong(hero.getAgility()) > random.nextLong(6971)) {
                long mate = random.nextLong(level * 30 + 1) + random.nextLong(hero.getAgility() / 10000 + 10) + 58;
                if (mate > 10000) {
                    mate = 9000 + random.nextLong(mate - 10000);
                }
                addMessage(context, hero.getFormatName() + "找到了一个宝箱， 获得了<font color=\"#FF8C00\">" + mate + "</font>锻造点数");
                hero.addMaterial(mate);
                addMessage(context, "-------------------");
            } else if (hero.getHp() < hero.getUpperHp() && random.nextLong(1000) > 989) {
                long hel = random.nextLong(hero.getUpperHp() / 5 + 1) + random.nextLong(hero.getPower() / 300);
                if (hel > hero.getUpperHp() / 2) {
                    hel = random.nextLong(hero.getUpperHp() / 2 + 1) + 1;
                }
                addMessage(context, hero.getFormatName() + "休息了一会，恢复了<font color=\"#556B2F\">" + hel + "</font>点HP");
                hero.addHp(hel);
                addMessage(context, "-------------------");
            } else if (random.nextLong(9000) > csmgl) {
                step = 0;
                long levJ = random.nextLong(hero.getMaxMazeLev() + 15) + 1;
                addMessage(context, hero.getFormatName() + "踩到了传送门，被传送到了迷宫第" + levJ + "层");
                level = levJ;
                if (level > hero.getMaxMazeLev()) {
                    hero.setMaxMazeLev(level);
                }
                mazeLevelDetect();
                addMessage(context, "-------------------");
            } else if (random.nextBoolean()) {
                Skill skill1 = SkillFactory.getSkill("隐身", hero);
                if (skill1.isActive() && skill1.perform()) {
                    continue;
                }
                Monster monster = null;
                boolean cheat = false;
                for (Pet pet : hero.getPets()) {
                    cheat = !MazeContents.checkPet(pet);
                    if(cheat){
                        break;
                    }
                }
                if (!MazeContents.checkCheat(hero) || cheat) {
                    monster = Monster.CHEATBOSS();
                }
                if (level % 10000 == 0) {
                    monster = Monster.copy(hero);
                }
                if (monster == null && random.nextLong(1000) > 899) {
                    monster = Monster.getBoss(this, hero);
                    step += 21;
                }
                if (monster == null) {
                    monster = new Monster(hero, this);
                }
                monster.setMazeLev(level);
                if (BattleController.battle(hero, monster, random, this, context)) {
                    streaking++;
                    if (streaking >= 100) {
                        Achievement.unbeaten.enable(hero);
                    }
                    Pet pet = Pet.catchPet(monster);
                    if (pet != null) {
                        addMessage(context, hero.getFormatName() + "收服了宠物 " + monster.getFormatName());
                        monster.addBattleDesc(hero.getFormatName() + "收服了宠物 " + monster.getFormatName());
                        if (hero.getPets().size() < hero.getPetSize()) {
                            hero.getPets().add(pet);
                        }
                    }
                } else {
                    if (level > 25) {
                        context.save();
                    }
                    GoodsType medallion = GoodsType.Medallion;
                    medallion.load();
                    if (medallion.getCount() > 0) {
                        medallion.getScript().use();
                        String notDie = hero.getFormatName() + "被" + monster.getFormatName() +
                                "打败了。<br>" + hero.getFormatName() + "掏出金晃晃的" + medallion.getName() + "晃瞎了大家的双眼。<br>" + hero.getFormatName() + "和他的宠物们原地复活了。";
                        addMessage(context, notDie);
                        monster.addBattleDesc(notDie);
                        hero.restoreHalf();
                    } else {
                        GoodsType safetyRope = GoodsType.SafetyRope;
                        SafetyRope.load();
                        if (safetyRope.getCount() > 0) {
                            safetyRope.getScript().use();
                            long slev = level / 10 + 1;
                            String notDie = hero.getFormatName() + "被" + monster.getFormatName() +
                                    "打败了。<br>" + hero.getFormatName() + "因为" + safetyRope.getName() + "护体掉到了" + slev;
                            addMessage(context, notDie);
                            monster.addBattleDesc(notDie);
                            hero.restoreHalf();
                            streaking = 0;
                            step = 0;
                            this.level = slev;
                        } else {
                            GoodsType halfSail = GoodsType.HalfSafe;
                            halfSail.load();
                            if (halfSail.getCount() > 0) {
                                halfSail.getScript().use();
                                long slev = level / 2 + 1;
                                String notDie = hero.getFormatName() + "被" + monster.getFormatName() +
                                        "打败了。<br>" + hero.getFormatName() + "因为" + halfSail.getName() + "护体掉到了" + slev;
                                addMessage(context, notDie);
                                monster.addBattleDesc(notDie);
                                hero.restoreHalf();
                                streaking = 0;
                                step = 0;
                                this.level = slev;
                            } else {
                                streaking = 0;
                                step = 0;
                                String defeatedmsg = hero.getFormatName() + "被" + monster.getFormatName() + "打败了，掉回到迷宫第一层。";
                                addMessage(context, defeatedmsg);
                                monster.addBattleDesc(defeatedmsg);
                                this.level = 1;
                                hero.restore();
                            }
                        }
                    }
                    context.showFloatView(monster.getBattleMsg());
                    lastSave = level;
                }
                monsterBook.addMonster(monster);
                addMessage(context, "-----------------------------");
            } else {
                switch (random.nextInt(5)) {
                    case 0:
                        addMessage(context, hero.getFormatName() + "思考了一下人生...");
                        if(hero.getReincaCount() > 0){
                            long point = hero.getReincaCount();
                            if(point > 10){
                                point = 10 + hero.getRandom().nextInt(10);
                            }
                            addMessage(context, hero.getFormatName() + "获得了" + point + "点能力点");
                            hero.addPoint(point);
                        }
                        break;
                    case 1:
                        addMessage(context, hero.getFormatName() + "犹豫了一下不知道做什么好。");
                        break;
                    case 2:
                        addMessage(context, hero.getFormatName() + "感觉到肚子饿了！");
                        break;
                    case 3:
                        if (hero.getPets().size() > 0) {
                            addMessage(context, hero.getFormatName() + "和宠物们玩耍了一下会。");
                            for (Pet pet : hero.getPets()) {
                                pet.click();
                            }
                        } else {
                            addMessage(context, hero.getFormatName() + "想要养宠物");
                        }
                        break;
                    case 4:
                        addMessage(context, hero.getFormatName() + "正在发呆...");
                        break;
                }
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
        BattleController.addMessage(context, msg);
    }

    private void mazeLevelDetect() {
        if (hero != null) {
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
                            Achievement.speculator.enable(hero);
                        }
                        Achievement.maze500.enable(hero);
                        break;
                    case 1000:
                        Achievement.maze1000.enable(hero);
                        break;
                    case 10000:
                        if (Achievement.maze100.isEnable()) {
                            Achievement.maze10000.enable(hero);
                        } else {
                            Achievement.cribber.enable(hero);
                        }
                        break;
                    case 50000:
                        if (Achievement.maze10000.isEnable()) {
                            Achievement.maze50000.enable(hero);
                        } else {
                            Achievement.cribber.enable(hero);
                        }
                        break;

                }
            }
            if (level > 50000) {
                if (!Achievement.richer.isEnable()) {
                    addMessage(MainGameActivity.context, "您进入了奇怪的区域！");
//                    level--;
                }
            }
            if (PetDB.getPetCount(null) < hero.getPetSize() + 20) {
                Pet f = null;
                Pet m = null;
                List<Pet> pets = hero.getPets();
                for (Pet pet : pets) {
                    if (!pet.getType().equals("蛋")) {
                        for (Pet p : pets) {
                            if (!p.equals(pet) && !p.getType().equals("蛋") && pet.getSex() != p.getSex() && (pet.getElement().isReinforce(p.getElement()) || p.getElement().isReinforce(pet.getElement()))) {
                                if (p.getSex() == 0) {
                                    f = p;
                                    m = pet;
                                } else {
                                    f = pet;
                                    m = p;
                                }
                                break;
                            }
                        }
                    }
                    if (f != null && m != null) {
                        break;
                    }
                }
                if (f != null && m != null) {
                    Pet egg = Pet.egg(f, m, level, hero);
                    if (egg != null) {
                        Barrier barrier = Barrier.load();
                        if (barrier.getCount() > 0) {
                            barrier.setCount(barrier.getCount() - 1);
                            addMessage(MainGameActivity.context, f.getFormatName() + "和" + m.getFormatName() + "想生蛋。但是被" + hero.getFormatName() + "阻止了！");
                        } else {
                            PetDB.save(egg);
                            addMessage(MainGameActivity.context, f.getFormatName() + "和" + m.getFormatName() + "生了一个蛋。");
                            if (hero.getPets().size() < hero.getPetSize()) {
                                hero.getPets().add(egg);
                            }
                        }
                    }
                }

            }
            if (level != 0 && level % 100 == 0) {
                Skill fSkill = SkillFactory.getSkill("浮生百刃", hero);
                Skill xSkill = SkillFactory.getSkill("虚无吞噬", hero);
                boolean qzs = SkillFactory.getSkill("欺诈师", hero).isActive();
                if (qzs && (random.nextLong(hero.getMaxMazeLev() + 1000) > 1100)) {
                    fSkill.setActive(true);
                }
                if (qzs && (random.nextLong(hero.getMaxMazeLev() + 1000) > 1150)) {
                    xSkill.setActive(true);
                }
            }
            if(!isSailed() && random.nextLong(hero.getAgility()/5000) > random.nextLong(1000)){
                setSailed(true);
                addMessage(MainGameActivity.context,"有商人入驻商店了，你可以去选购物品。");
            }
        }
    }

    public long getLev() {
        return level;
    }

    public void setLevel(long level) {
        this.level = level;
        mazeLevelDetect();
    }

    public float getMeetRate() {
        return meetRate;
    }

    public void setMeetRate(float meetRate) {
        this.meetRate = meetRate;
    }

    public long getHunt() {
        return hunt;
    }

    public void setHunt(long hunt) {
        this.hunt = hunt;
    }

    public boolean isSailed() {
        return sailed;
    }

    public void setSailed(boolean sailed) {
        this.sailed = sailed;
        if(!sailed){
            addMessage(MainGameActivity.context, "商人离开了商店，请耐心等待下一位商人的到达。");
        }
    }
}
