package cn.gavin.pet.swop;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.gavin.monster.Monster;
import cn.gavin.pet.Pet;
import cn.gavin.pet.PetDB;
import cn.gavin.utils.MazeContents;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 11/23/2015.
 */
public class SwapManager {
    private boolean finished = false;
    List<SwapPet> pets = new ArrayList<SwapPet>();

    public List<SwapPet> getResult() {
        return pets;
    }

    public synchronized void finished(List<SwapPet> pets) {
        this.pets = pets;
        finished = true;
    }

    public void acknowledge(Context context, SwapPet pet) {
        pet.setAcknowledge(true);
        pet.update(context);
    }

    /**
     * 从网上拉去自己寄存的pet
     * 当查询完成后finished会被设置为true，并且pets会有值。
     * @param context
     */
    public void getAllMyOwnInNet(Context context) {
        pets.clear();
        finished = false;
        BmobQuery<SwapPet> query = new BmobQuery<SwapPet>();
        query.setLimit(10);
        query.addWhereEqualTo("ownerId", MazeContents.hero.getUuid());
        query.addWhereEqualTo("acknowledge", false);
        query.findObjects(context, new FindListener<SwapPet>() {
            @Override
            public void onSuccess(List<SwapPet> swapPets) {
                finished(swapPets);
            }

            @Override
            public void onError(int i, String s) {
                finished(Collections.<SwapPet>emptyList());
            }
        });
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public void uploadPet(Context context, SwapPet pet) {
        pet.save(context, new SaveListener() {
            @Override
            public void onSuccess() {
                finished(Collections.<SwapPet>emptyList());
            }

            @Override
            public void onFailure(int i, String s) {
                finished(null);
            }
        });
    }

    public void searchPet(Context context, SwapPet pet, int page){
        finished = false;
        pets = null;
        BmobQuery<SwapPet> query = new BmobQuery<SwapPet>();
        query.setLimit(9);
        query.setSkip(9 * (page-1));
        if(pet.getAskAtk()!=null) {
            query.addWhereGreaterThan("atk", pet.getAskAtk());
        }
        if(pet.getAskDef()!=null) {
            query.addWhereGreaterThan("def", pet.getAskDef());
        }
        if(pet.getAskHp()!=null) {
            query.addWhereGreaterThan("hp", pet.getAskHp());
        }
        if(pet.getAskType()!=null) {
            query.addWhereEqualTo("type", pet.getAskType());
        }
        if(pet.getAskName()!=null) {
            query.addWhereContains("name", pet.getAskName());
        }
        if(pet.getAskSkill()!=null){
            query.addWhereStartsWith("skill",pet.getAskSkill());
        }
        if(pet.getAskSex()!=null){
            query.addWhereEqualTo("sex", pet.getAskSex());
        }

        query.findObjects(context, new FindListener<SwapPet>() {
            @Override
            public void onSuccess(List<SwapPet> swapPets) {
                finished(swapPets);
            }

            @Override
            public void onError(int i, String s) {
                finished(Collections.<SwapPet>emptyList());
            }
        });
    }

    /**
     * 根据传入的Pet（包含限制条件的SwapPet查询自己的pet中是否有合适的
     * @param swapPet
     * @return
     */
    public List<Pet> myAvailablePets(SwapPet swapPet){
        List<Pet> aPets = new ArrayList<Pet>();
        for(Pet p : PetDB.loadPet(null)){
            boolean fix = true;
            if(swapPet.getAskSex()!=null){
                fix =  fix && p.getSex() == swapPet.getSex();
            }
            if(swapPet.getAskAtk()!=null){
                fix &= p.getMaxAtk() >= swapPet.getAskAtk();
            }
            if(swapPet.getAskHp()!=null){
                fix &= p.getUHp() >= swapPet.getAskHp();
            }
            if(swapPet.getAskDef() != null){
                fix &= p.getMaxDef() >= swapPet.getAskDef();
            }
            if(swapPet.getAskType() !=null){
                fix &= p.getType().equals(Monster.lastNames[swapPet.getAskType()]);
            }
            if(swapPet.getAskSkill() !=null){
                fix &= p.getAllSkill()!=null && p.getAllSkill().getName().startsWith(swapPet.getAskSkill());
            }
            if(swapPet.getAskName() !=null){
                fix &= p.getName().contains(swapPet.getAskName());
            }
            if(fix){
                aPets.add(p);
            }
        }
        return aPets;
    }
}
