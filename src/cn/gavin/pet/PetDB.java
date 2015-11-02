package cn.gavin.pet;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cn.gavin.Element;
import cn.gavin.db.DBHelper;
import cn.gavin.log.LogHelper;
import cn.gavin.palace.nskill.NSkill;
import cn.gavin.utils.StringUtils;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 11/2/2015.
 */
public class PetDB {

    public static void createDB(SQLiteDatabase db){
        String sql = "CREATE TABLE pet(" +
                "id TEXT NOT NULL PRIMARY KEY," +
                "name TEXT NOT NULL," +
                "type TEXT NOT NULL," +
                "element TEXT NOT NULL," +
                "skill TEXT NOT NULL," +
                "skill_count TEXT," +
                "intimacy TEXT," +
                "death_c TEXT," +
                "atk TEXT," +
                "def TEXT," +
                "hp TEXT," +
                "u_hp TEXT," +
                "lev TEXT" +
                ")";

        db.execSQL(sql);
        db.execSQL("CREATE UNIQUE INDEX pet_index ON pet (id)");
    }

    public static void save(Pet...pets){
        String base = "REPLACE INTO pet (id, name, type, intimacy, element, skill, skill_count, death_c, atk, def, hp,u_hp, lev) values ('%s', '%s','%s','%s','%s','%s','%s','%s', '%s', '%s', '%s', '%s', '%s')";
        for(Pet pet : pets){
            if(!StringUtils.isNotEmpty(pet.getId())){
                pet.setId(UUID.randomUUID().toString());
            }
            NSkill skill = pet.getSkill();
            String sql = String.format(base, pet.getId(), pet.getName(), pet.getType(),
                    pet.getIntimacy(), pet.getElement().name(), skill!=null ? skill.getName():"",
                    skill!=null ? skill.getCount() : "0",pet.getDeathCount(), pet.getMaxAtk(),
                    pet.getMaxDef(), pet.getHp(), pet.getUHp(), pet.getLev());
            DBHelper.getDbHelper().excuseSQLWithoutResult(sql);
        }
    }

    public static void delete(Pet...pets){
        for(Pet pet : pets){
            DBHelper.getDbHelper().excuseSQLWithoutResult("DELETE FROM pet WHERE id ='" + pet.getId() + "'");
        }
    }

    public static List<Pet> loadPet(SQLiteDatabase db){
        Cursor cursor;
        List<Pet> pets = new ArrayList<Pet>();
        String sql = "SELECT * FROM pet";
        try {
            if (db != null) {
                cursor = db.rawQuery(sql, null);
                cursor.moveToFirst();
            } else {
                cursor = DBHelper.getDbHelper().excuseSOL(sql);
            }
            while(!cursor.isAfterLast()){
                Pet pet = new Pet();
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
                //TODO skill
                pets.add(pet);
                cursor.moveToNext();
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
            LogHelper.logException(e);
        }
        return pets;
    }
}
