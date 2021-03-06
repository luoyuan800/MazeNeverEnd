package cn.gavin.maze;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import cn.gavin.Achievement;
import cn.gavin.Hero;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.db.DBHelper;
import cn.gavin.gift.Gift;
import cn.gavin.good.GoodsType;
import cn.gavin.log.LogHelper;
import cn.gavin.monster.Monster;
import cn.gavin.monster.MonsterDB;
import cn.gavin.pet.Pet;
import cn.gavin.pet.PetDB;
import cn.gavin.pet.skill.PetSkill;
import cn.gavin.skill.Skill;
import cn.gavin.skill.SkillFactory;
import cn.gavin.story.NPC;
import cn.gavin.story.StoryHelper;
import cn.gavin.utils.MazeContents;
import cn.gavin.utils.Random;
import cn.gavin.utils.StringUtils;

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
    private StoryHelper storyHelper;
    protected long lastSave;
    private long hunt = 150;
    private boolean sailed = false;
    private String catchPetNameContains = "";

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

    public void move(MainGameActivity context) {
        while (context.isGameThreadRunning()) {
            if (context.isPause()) {
                continue;
            }
            if (storyHelper != null && storyHelper.trigger()) {
                continue;
            }
            moving = true;
            step++;
            final String sque = "<br>-------------------------";
            if (random.nextLong(10000) > 9985 || step > random.nextLong(22) || random.nextLong(streaking + 1) > 50 + level) {
                if ((level - lastSave) > 50) {
                    lastSave = level;
                    context.save();
                }
                step = 0;
                level++;
                mazeLevelDetect();
                long point = 1 + level / 10 + random.nextLong(level + 1) / 300;
                if (hero.getMaxMazeLev() < 500) {
                    point *= 4;
                }
                if (point > 100) {
                    point = 60 + random.nextInt(70);
                }
                String msg = hero.getFormatName() + "进入了" + level + "层迷宫， 获得了<font color=\"#FF8C00\">" + point + "</font>点数奖励";
                for (Pet pet : new ArrayList<Pet>(hero.getPets())) {
                    pet.click();
                    if (pet.getType().equals("蛋")) {
                        pet.setDeathCount(pet.getDeathCount() - hero.getEggStep());
                        if (pet.getDeathCount() <= 0) {
                            addMessage(context, pet.getFormatName() + "出生了！");
                            try {
                                Cursor cursor = DBHelper.getDbHelper().excuseSOL("select catch meet_lev from monster where id = '" + pet.getIndex() + "'");
                                if (!cursor.isAfterLast()) {
                                    DBHelper.getDbHelper().excuseSQLWithoutResult("update monster set " +
                                            "catch = '" + (StringUtils.toLong(cursor.getString(cursor.getColumnIndex("catch"))) + 1) +
                                            " where id ='" + pet.getIndex() + "'");
                                    if (StringUtils.toLong(cursor.getString(cursor.getColumnIndex("meet_lev"))) == 0) {
                                        DBHelper.getDbHelper().excuseSQLWithoutResult("update monster set " +
                                                "meet_lev = '" + level +
                                                " where id ='" + pet.getIndex() + "'");
                                    }
                                }
                                cursor.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                                LogHelper.logException(e, false);
                            }
                            int index = pet.getIndex();
                            Cursor cursor = DBHelper.getDbHelper().excuseSOL("select type from monster where id = '" + index + "'");
                            if (cursor != null && !cursor.isAfterLast()) {
                                pet.setType(cursor.getString(cursor.getColumnIndex("type")));
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
                if(!GoodsType.Incubator.isLock()){
                    GoodsType.Incubator.use();
                }
                addMessage(context, msg);
                if (level > hero.getMaxMazeLev()) {
                    hero.setMaxMazeLev(level);
                }

                hero.addPoint(point);
                hero.addHp(random.nextLong(hero.getUpperHp() / 10 + 1) + random.nextLong(hero.getPower() / 500));

                addMessage(context, sque);
            } else if (random.nextLong(850 + hunt) > 993 && random.nextLong(hero.getAgility()) > random.nextLong(6971)) {
                long mate = random.nextLong(level * 30 + 1) + random.nextLong(hero.getAgility() / 10000 + 10) + 58;
                if (mate > 10000) {
                    mate = 9000 + random.nextLong(mate - 10000);
                }
                addMessage(context, hero.getFormatName() + "找到了一个宝箱， 获得了<font color=\"#FF8C00\">" + mate + "</font>锻造点数");
                hero.addMaterial(mate);
                addMessage(context, sque);
            } else if (hero.getHp() < hero.getUpperHp() && random.nextLong(1000) > 989) {
                long hel = random.nextLong(hero.getUpperHp() / 5 + 1) + random.nextLong(hero.getPower() / 300);
                if (hel > hero.getUpperHp() / 2) {
                    hel = random.nextLong(hero.getUpperHp() / 2 + 1) + 1;
                }
                addMessage(context, hero.getFormatName() + "休息了一会，恢复了<font color=\"#556B2F\">" + hel + "</font>点HP");
                hero.addHp(hel);
                addMessage(context, sque);
            } else if (random.nextLong(10000) > csmgl) {
                GoodsType closeP = GoodsType.ClosePortal;
                if (closeP.getCount() > 0 && !closeP.isLock()) {
                    closeP.use();
                } else {
                    step = 0;
                    long levJ = random.nextLong(hero.getMaxMazeLev() + 100 + hero.getReincaCount()) + 1;
                    addMessage(context, hero.getFormatName() + "踩到了传送门，被传送到了迷宫第" + levJ + "层");
                    level = levJ;
                    if (level > hero.getMaxMazeLev()) {
                        hero.setMaxMazeLev(level);
                    }
                    mazeLevelDetect();
                    addMessage(context, sque);
                }
            } else if (random.nextBoolean()) {
                int maxMonsterCount = 2;
                maxMonsterCount += 2 * (level / 300);
                if (maxMonsterCount > 15) {
                    maxMonsterCount = 15;
                }
                maxMonsterCount = 1 + random.nextInt(maxMonsterCount);
                for (int i = 0; i < maxMonsterCount; i++) {
                    boolean isBoss = false;
                    Skill skill1 = SkillFactory.getSkill("隐身", hero);
                    if (skill1.isActive() && skill1.perform()) {
                        continue;
                    }
                    Monster monster = null;
                    boolean cheat = false;
                    for (Pet pet : hero.getPets()) {
                        cheat = !MazeContents.checkPet(pet);
                        if (cheat) {
                            break;
                        }
                    }
                    if (!MazeContents.checkCheat(hero) || cheat) {
                        monster = Monster.CHEATBOSS();
                    }
                    if (level % 10000 == 0) {
                        monster = Monster.copy(hero);
                        isBoss = true;
                    }
                    if (monster == null && random.nextLong(1000) > 909) {
                        step += 21;
                        NPC boss = NPC.build(level);
                        if (boss != null) {
                            boss.setMaterial(random.nextLong(boss.getHp() / 10000) + level / 2);
                            if (!NPCBattleController.battle(hero, boss, random, this, context) && hero.getHp() <= 0) {
                                Monster asMonster = boss.formatAsMonster();
                                asMonster.setBattleMsg(NPCBattleController.getLastBattle());
                                beatJudge(context, asMonster, true);
                            } else {
                                addMessage(context, sque);
                            }
                            break;
                        } else {
                            monster = Monster.getBoss(this, hero);
                            isBoss = true;
                        }
                    }
                    if (monster == null) {
                        monster = new Monster(hero, this);
                        isBoss = false;
                    }
                    monster.setMazeLev(level);
                    if (monster.getMeet_lev() == 0) {
                        monster.setMeet_lev(level);
                    }
                    if (BattleController.battle(hero, monster, random, this, context)) {
                        streaking++;
                        if (streaking >= 100) {
                            Achievement.unbeaten.enable(hero);
                        }
                        monster.setDefeatCount(monster.getDefeatCount() + 1);
                        if (!StringUtils.isNotEmpty(catchPetNameContains) || monster.getName().contains(catchPetNameContains)) {
                            Pet pet = Pet.catchPet(monster);
                            if (pet != null) {
                                if (monster.getCatch_lev() <= 0) {
                                    monster.setCatch_lev(level);
                                }
                                addMessage(context, hero.getFormatName() + "收服了宠物 " + monster.getFormatName());
                                monster.addBattleDesc(hero.getFormatName() + "收服了宠物 " + monster.getFormatName());
                                if (hero.getPets().size() < hero.getPetSize()) {
                                    hero.getPets().add(pet);
                                }
                            }
                        }
                    } else {
                        isBoss = beatJudge(context, monster, isBoss);
                    }
                    MonsterDB.updateMonster(monster);
                    addMessage(context, sque);
                    if (isBoss) break;
                }
            } else {
                switch (random.nextInt(6)) {
                    case 0:
                        addMessage(context, hero.getFormatName() + "思考了一下人生...");
                        if (hero.getReincaCount() > 0) {
                            long point = hero.getReincaCount();
                            if (point > 10) {
                                point = 10 + hero.getRandom().nextInt(10);
                            }
                            addMessage(context, hero.getFormatName() + "获得了" + point + "点能力点");
                            hero.addPoint(point);
                        }
                        break;
                    case 1:
                        addMessage(context, hero.getFormatName() + "犹豫了一下不知道做什么好。");
                        if(hero.getGift() == Gift.ChildrenKing){
                            addMessage(context, hero.getFormatName() + "因为" +  Gift.ChildrenKing.getName() + "天赋，你虽然会因为无知犯错，但是大人们宽容你的错误。增加100点能力点");
                            hero.addPoint(100);
                        }
                        break;
                    case 2:
                        addMessage(context, hero.getFormatName() + "感觉到肚子饿了！");
                        if(hero.getGift() == Gift.ChildrenKing){
                            addMessage(context, hero.getFormatName() + "因为" +  Gift.ChildrenKing.getName() + "天赋，可以在肚子饿的时候哭闹一下，就会有人上前服侍。增加 20 点力量。");
                            hero.addStrength(20);
                        }
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
                        if(hero.getGift() == Gift.ChildrenKing){
                            addMessage(context, hero.getFormatName() + "因为" +  Gift.ChildrenKing.getName() + "天赋，发呆的时候咬咬手指头就可以恢复20%的生命值。");
                            hero.addHp((long)(hero.getUpperHp() * 0.3));
                        }
                        break;
                    case 5:
                        GoodsType mirrori = GoodsType.Mirror;
                        if (mirrori.getCount() > 0 && !mirrori.isLock()) {
                            addMessage(context, hero.getFormatName() + "拿出镜子照了一下，觉得自己很帅帅哒/亮亮哒！");
                            addMessage(context, hero.getFormatName() + "敏捷加  " + mirrori.getCount());
                            hero.addAgility(mirrori.getCount());
                            mirrori.use();
                        }
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

    private boolean beatJudge(MainGameActivity context, Monster monster, boolean isBoss) {
        if (level > 25) {
            context.save();
        }
        monster.setBeatCount(monster.getBeatCount() + 1);
        GoodsType medallion = GoodsType.Medallion;
        if (medallion.getCount() > 0 && !medallion.isLock()) {
            medallion.getScript().use();
            String notDie = hero.getFormatName() + "被" + monster.getFormatName() +
                    "打败了。<br>" + hero.getFormatName() + "掏出金晃晃的" + medallion.getName() + "晃瞎了大家的双眼。<br>" + hero.getFormatName() + "和他的宠物们原地复活了。";
            addMessage(context, notDie);
            monster.addBattleDesc(notDie);
            hero.restoreHalf();
        } else {
            GoodsType safetyRope = GoodsType.SafetyRope;
            if (safetyRope.getCount() > 0 && !safetyRope.isLock()) {
                safetyRope.getScript().use();
                long slev = level / 10 + 1;
                String notDie = hero.getFormatName() + "被" + monster.getFormatName() +
                        "打败了。<br>" + hero.getFormatName() + "因为" + safetyRope.getName() + "护体掉到了" + slev + "层";
                addMessage(context, notDie);
                monster.addBattleDesc(notDie);
                hero.restoreHalf();
                streaking = 0;
                step = 0;
                this.level = slev;
            } else {
                GoodsType halfSail = GoodsType.HalfSafe;
                if (halfSail.getCount() > 0 && !halfSail.isLock()) {
                    halfSail.getScript().use();
                    long slev = level / 2 + 1;
                    String notDie = hero.getFormatName() + "被" + monster.getFormatName() +
                            "打败了。<br>" + hero.getFormatName() + "因为" + halfSail.getName() + "护体掉到了" + slev + "层";
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
        return isBoss;
    }

    private void addMessage(MainGameActivity context, String msg) {
        BattleController.addMessage(context, msg);
    }

    private void mazeLevelDetect() {
        try {
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
                                if (!Achievement.maze50000.isEnable()) {
                                    GoodsType.RenameAcc.setCount(GoodsType.RenameAcc.getCount() + 1);
                                    GoodsType.RenameAcc.save();
                                    addMessage(MainGameActivity.context, "恭喜你进入了50000层，系统奖励了自定义一件装备名称的物品。");
                                }
                                Achievement.maze50000.enable(hero);

                            } else {
                                Achievement.cribber.enable(hero);
                            }
                            break;
                        case 100000:
                            GoodsType.RenamePet.setCount(GoodsType.RenamePet.getCount() + 1);
                            GoodsType.RenamePet.save();
                            addMessage(MainGameActivity.context, "恭喜你进入了100000层，系统奖励了自定义宠物名称的物品。");
                            GoodsType.RenameAcc.setCount(GoodsType.RenameAcc.getCount() + 1);
                            GoodsType.RenameAcc.save();
                            addMessage(MainGameActivity.context, "恭喜你进入了100000层，系统奖励了自定义一件装备名称的物品。");
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
                    List<Pet> pets = new ArrayList<Pet>(hero.getPets());
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
                            GoodsType barrier = GoodsType.Barrier;
                            if (barrier.getCount() > 0 && !barrier.isLock()) {
                                barrier.getScript().use();
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
                if (!isSailed() && (random.nextLong(hero.getAgility() / 5000) > random.nextLong(3000) || (hero.getMaxMazeLev() < 200 && hero.getMaterial() < 10000000))) {
                    setSailed(true);
                    addMessage(MainGameActivity.context, "有商人入驻商店了，你可以去选购物品。");
                }
            }
        } catch (Exception e) {
            LogHelper.logException(e, false);
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
        if (!sailed) {
            addMessage(MainGameActivity.context, "商人离开了商店，请耐心等待下一位商人的到达。");
        }
    }

    public String getCatchPetNameContains() {
        return catchPetNameContains;
    }

    public void setCatchPetNameContains(String catchPetNameContains) {
        this.catchPetNameContains = catchPetNameContains;
    }
}
