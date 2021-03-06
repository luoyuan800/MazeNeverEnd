package cn.gavin.pet;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import cn.gavin.Achievement;
import cn.gavin.Element;
import cn.gavin.db.DBHelper;
import cn.gavin.log.LogHelper;
import cn.gavin.palace.nskill.NSkill;
import cn.gavin.pet.skill.PetSkill;
import cn.gavin.utils.MazeContents;
import cn.gavin.utils.StringUtils;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 11/2/2015.
 */
public class PetDB {

    public static void createDB(SQLiteDatabase db) {
        String sql = "CREATE TABLE pet(" +
                "id TEXT NOT NULL PRIMARY KEY," +
                "name TEXT NOT NULL," +
                "type TEXT NOT NULL," +
                "element TEXT NOT NULL," +
                "skill TEXT NOT NULL," +
                "skill_count TEXT," +
                "intimacy TEXT," +
                "death_c TEXT," +
                "atk_rise TEXT," +
                "def_rise TEXT," +
                "hp_rise TEXT," +
                "atk TEXT," +
                "def TEXT," +
                "hp TEXT," +
                "u_hp TEXT," +
                "lev TEXT," +
                "sex INTEGER," +
                "owner TEXT," +
                "owner_id TEXT," +
                "color TEXT," +
                "monster_index INTEGER," +
                "egg_rate TEXT," +
                "image INTEGER," +
                "farther TEXT," +
                "mother TEXT" +
                ")";

        db.execSQL(sql);
        db.execSQL("CREATE UNIQUE INDEX pet_index ON pet (id)");
    }

    public static int getPetCount(SQLiteDatabase database) {
        String sql = "SELECT count(*) FROM pet";
        Cursor cursor;
        if (database != null) {
            cursor = database.rawQuery(sql, null);
            cursor.moveToFirst();
        } else {
            cursor = DBHelper.getDbHelper().excuseSOL(sql);
        }
        int i = cursor.getInt(0);
        cursor.close();
        return i;
    }

    public static void save(Pet... pets) {
        String base = "REPLACE INTO pet (id, name, type, intimacy, element, skill, skill_count, " +
                "death_c, atk, def, hp,u_hp, lev, farther, mother, sex, atk_rise, hp_rise, def_rise, owner, color, owner_id, monster_index, egg_rate, image) " +
                "values ('%s', '%s', '%s','%s','%s','%s','%s'," +
                "'%s','%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s','%s','%s', '%s','%s', '%s', '%s', '%s', '%s')";
        for (Pet pet : pets) {
            if (!StringUtils.isNotEmpty(pet.getId())) {
                pet.setId(UUID.randomUUID().toString());
            }
            NSkill skill = pet.getAllSkill();
            String sql = String.format(base, pet.getId(), pet.getName(), pet.getType(),
                    pet.getIntimacy(), pet.getElement().name(), skill != null ? skill.getName() : "",
                    skill != null ? skill.getCount() : "0", pet.getDeathCount(), pet.getMaxAtk(),
                    pet.getMaxDef(), pet.getHp(), pet.getUHp(), pet.getLev(), pet.getfName(),
                    pet.getmName(), pet.getSex(), pet.getAtk_rise(), pet.getHp_rise(), pet.getDef_rise(), pet.getOwner(), pet.getColor(), pet.getOwnerId(), pet.getIndex(), pet.getEggRate(), pet.getImage());
            DBHelper.getDbHelper().excuseSQLWithoutResult(sql);
            petCatch.put(pet.getId(),pet);
        }
    }

    public static void delete(Pet... pets) {
        for (Pet pet : pets) {
            DBHelper.getDbHelper().excuseSQLWithoutResult("DELETE FROM pet WHERE id ='" + pet.getId() + "'");
            petCatch.remove(pet.getId());
        }
    }

    public static void load(Pet... pets) {
        for (Pet pet : pets) {
            Cursor cursor = DBHelper.getDbHelper().excuseSOL("SELECT * FROM pet WHERE id ='" + pet.getId() + "'");
            if (!cursor.isAfterLast()) {
                buildPet(pet, cursor);
                petCatch.put(pet.getId(), pet);
            }
        }
    }

    private static void buildPet(Pet pet, Cursor cursor) {
        try{
        pet.setIndex(cursor.getInt(cursor.getColumnIndex("monster_index")));
        pet.setEggRate(StringUtils.toFloat(cursor.getString(cursor.getColumnIndex("egg_rate"))));
        pet.setName(cursor.getString(cursor.getColumnIndex("name")));
        pet.setType(cursor.getString(cursor.getColumnIndex("type")));
        pet.setHp(StringUtils.toLong(cursor.getString(cursor.getColumnIndex("hp"))));
        pet.setAtk(StringUtils.toLong(cursor.getString(cursor.getColumnIndex("atk"))));
        pet.setDef(StringUtils.toLong(cursor.getString(cursor.getColumnIndex("def"))));
        pet.setUHp(StringUtils.toLong(cursor.getString(cursor.getColumnIndex("u_hp"))));
        pet.setIntimacy(StringUtils.toLong(cursor.getString(cursor.getColumnIndex("intimacy"))));
        pet.setDeathCount(StringUtils.toLong(cursor.getString(cursor.getColumnIndex("death_c"))));
        pet.setLev(StringUtils.toLong(cursor.getString(cursor.getColumnIndex("lev"))));
        pet.setElement(Element.valueOf(cursor.getString(cursor.getColumnIndex("element"))));
        pet.setfName(cursor.getString(cursor.getColumnIndex("farther")));
        pet.setmName(cursor.getString(cursor.getColumnIndex("mother")));
        pet.setSex(cursor.getInt(cursor.getColumnIndex("sex")));
        pet.setAtk_rise(StringUtils.toLong(cursor.getString(cursor.getColumnIndex("atk_rise"))));
        pet.setDef_rise(StringUtils.toLong(cursor.getString(cursor.getColumnIndex("def_rise"))));
        pet.setHp_rise(StringUtils.toLong(cursor.getString(cursor.getColumnIndex("hp_rise"))));
        /*int image = cursor.getInt(cursor.getColumnIndex("image"));
        if(image == 0){
            if(DBHelper.getDbHelper()!=null){
                Cursor cursor1 = DBHelper.getDbHelper().excuseSOL("select img from monster where id = '" + pet.getIndex() + "'");
                if(!cursor1.isAfterLast()){
                    image = cursor1.getInt(cursor.getColumnIndex("img"));
                }
                cursor1.close();
            }
        }*/
        //pet.setImage(image);
        pet.setOwner(cursor.getString(cursor.getColumnIndex("owner")));
        String ownerId = cursor.getString(cursor.getColumnIndex("owner_id"));
        if(StringUtils.isNotEmpty(ownerId)) {
            pet.setOwnerId(ownerId);
        }
        String color = cursor.getString(cursor.getColumnIndex("color"));
        if(StringUtils.isNotEmpty(color)) {
            pet.setColor(color);
        }
        if (pet.getName().startsWith("变异的") && StringUtils.isNotEmpty(pet.getfName()) && !pet.getfName().startsWith("变异的")) {
            pet.setColor("#B8860B");
        }
        String skill = cursor.getString(cursor.getColumnIndex("skill"));
        long skillCount = StringUtils.toLong(cursor.getString(cursor.getColumnIndex("skill_count")));
        NSkill nSkill = NSkill.createSkillByName(skill, pet, skillCount, null);
        if (nSkill instanceof PetSkill) {
            pet.setSkill(nSkill);
        } else {
            pet.addSkill(nSkill);
        }
        if("龙".equals(pet.getType())){
            Achievement.Long.enable(MazeContents.hero);
        }
        if("九尾狐".equals(pet.getType())){
            Achievement.JiuWeiHU.enable(MazeContents.hero);
        }
        if("朱獳".equals(pet.getType()) || "梼杌".equals(pet.getType()) ){
            Achievement.ShengShou.enable(MazeContents.hero);
        }
        if("朱厌".equals(pet.getType()) || "穷奇".equals(pet.getType()) ){
            Achievement.FShengShou.enable(MazeContents.hero);
        }
        if("老虎".equals(pet.getType()) ){
            Achievement.WuSong.enable(MazeContents.hero);
        }
        if("嗜血蚁".equals(pet.getType()) ){
            Achievement.Ant.enable(MazeContents.hero);
        }
        if("僵尸".equals(pet.getType()) ){
            Achievement.Zombie.enable(MazeContents.hero);
        }
        if("蟑螂".equals(pet.getType()) ){
            Achievement.XiaoQian.enable(MazeContents.hero);
        }
        }catch (Exception e){
            LogHelper.logException(e, false);
        }
    }

    public static List<Pet> loadPet(SQLiteDatabase db) {
        if (!petCatch.isEmpty() && getPetCount(db) <= petCatch.size()) {
            return new ArrayList<Pet>(petCatch.values());
        } else {
            if(!petCatch.isEmpty()){
                save();
            }
            Cursor cursor;
            List<Pet> pets = new ArrayList<Pet>();
            String sql = "SELECT * FROM pet ORDER BY intimacy";
            try {
                if (db != null) {
                    cursor = db.rawQuery(sql, null);
                    cursor.moveToFirst();
                } else {
                    cursor = DBHelper.getDbHelper().excuseSOL(sql);
                }
                while (!cursor.isAfterLast()) {
                    Pet pet = new Pet();
                    buildPet(pet, cursor);
                    pet.setId(cursor.getString(cursor.getColumnIndex("id")));
                    pets.add(pet);
                    petCatch.put(pet.getId(), pet);
                    cursor.moveToNext();
                }
                cursor.close();
            } catch (Exception e) {
                LogHelper.logException(e, false);
            }
            return pets;
        }
    }

    public static Pet load(String id){
        Pet p = petCatch.get(id);
        if(p !=null){
            return p;
        }else{
            Pet pet = new Pet();
            pet.setId(id);
            load(pet);
            return pet;
        }
    }

    public static ConcurrentHashMap<String, Pet> petCatch = new ConcurrentHashMap<String, Pet>();

    public synchronized static void save(){
        for(Pet pet : petCatch.values()){
            pet.save();
        }
    }
}
