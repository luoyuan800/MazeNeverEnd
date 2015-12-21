package cn.gavin.pet.swop;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.gavin.Achievement;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.log.LogHelper;
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

    public void acknowledge(final Context context, SwapPet pet) {
        pet.setAcknowledge(true);
        pet.update(context, pet.getObjectId(), new UpdateListener() {
            @Override
            public void onSuccess() {
                Achievement.Changer.enable(MazeContents.hero);
                Toast.makeText(context, "--交换完成--", Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onFailure(int i, String s) {

                Toast.makeText(context, "--交换失败" + s + "--", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    /**
     * 从网上拉去自己寄存的pet
     * 当查询完成后finished会被设置为true，并且pets会有值。
     *
     * @param context
     */
    public void getAllMyOwnInNet(Context context) {
        pets.clear();
        finished = false;
        BmobQuery<SwapPet> query = new BmobQuery<SwapPet>();
        query.setLimit(10);
        query.addWhereEqualTo("keeperId", MazeContents.hero.getUuid());
        query.addWhereEqualTo("acknowledge", false);
        query.include("changedPet");
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

    public void uploadPet(Context context, SwapPet pet, final Pet petO) {
        try {
            if (!MazeContents.checkPet(petO)) {
                Toast.makeText(context, "宠物数据异常，无法上传！", Toast.LENGTH_SHORT).show();
                return;
            }
            finished = false;
            final ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("上传中(不能取消)...");
            progressDialog.show();
            pet.save(context, new SaveListener() {
                @Override
                public void onSuccess() {
                    petO.releasePet(MazeContents.hero, MainGameActivity.context);
                    progressDialog.dismiss();
                    finished(Collections.<SwapPet>emptyList());
                    Toast.makeText(MainGameActivity.context, (petO.getType().equals("蛋") ? "蛋" : petO.getName())
                            + "已经上传", Toast.LENGTH_SHORT).show();
                    Achievement.Searcher.enable(MazeContents.hero);
                }

                @Override
                public void onFailure(int i, String s) {
                    progressDialog.dismiss();
                    Toast.makeText(MainGameActivity.context, "上传宠物失败，请检查网络后重试" + s, Toast.LENGTH_SHORT).show();
                    finished(null);
                }
            });
        } catch (Exception e) {
            LogHelper.logException(e);
            e.printStackTrace();
        }
    }

    public void searchPet(Context context, SwapPet pet, int page) {
        finished = false;
        pets = null;
        BmobQuery<SwapPet> query = new BmobQuery<SwapPet>();
        query.setLimit(9);
        query.setSkip(9 * (page - 1));
        if (pet.getAskAtk() != null) {
            query.addWhereGreaterThan("atk", pet.getAskAtk());
        }
        if (pet.getAskDef() != null) {
            query.addWhereGreaterThan("def", pet.getAskDef());
        }
        if (pet.getAskHp() != null) {
            query.addWhereGreaterThan("hp", pet.getAskHp());
        }
        if (pet.getAskType() != null) {
            query.addWhereEqualTo("type", pet.getAskType());
        }
        if (pet.getAskName() != null) {
            query.addWhereContains("name", pet.getAskName());
        }
        if (pet.getAskSkill() != null) {
            query.addWhereStartsWith("skill", pet.getAskSkill());
        }
        if (pet.getAskSex() != null) {
            query.addWhereEqualTo("sex", pet.getAskSex());
        }
        query.addWhereDoesNotExists("changedPet");
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
     *
     * @param swapPet
     * @return
     */
    public List<Pet> myAvailablePets(SwapPet swapPet) {
        List<Pet> aPets = new ArrayList<Pet>();
        for (Pet p : PetDB.loadPet(null)) {
            boolean fix = !MazeContents.hero.petOnUsed(p);
            boolean egg = "蛋".equals(p.getType());
            if (swapPet.getAskSex() != null) {
                fix = fix && p.getSex() == swapPet.getSex();
            }
            if (swapPet.getAskAtk() != null) {
                if(egg){
                    fix &= 10>=swapPet.getAskAtk();
                }else {
                    fix &= p.getMaxAtk() >= swapPet.getAskAtk();
                }
            }
            if (swapPet.getAskHp() != null) {
                if (egg) {
                    fix &= 10 >= swapPet.getAskHp();
                }else {
                    fix &= p.getUHp() >= swapPet.getAskHp();
                }
            }
            if (swapPet.getAskDef() != null) {
                if(egg){
                    fix &= 10 >= swapPet.getAskDef();
                }else {
                    fix &= p.getMaxDef() >= swapPet.getAskDef();
                }
            }
            if (swapPet.getAskType() != null && swapPet.getAskType() < Monster.lastNames.length) {
                fix &= egg && p.getType().equals(Monster.lastNames[swapPet.getAskType()]);
            }
            if (swapPet.getAskSkill() != null) {
                fix &= p.getAllSkill() != null && p.getAllSkill().getName().startsWith(swapPet.getAskSkill());
            }
            if (swapPet.getAskName() != null) {
                fix &= !egg && p.getName().contains(swapPet.getAskName());
            }
            if (fix) {
                aPets.add(p);
            }
        }
        return aPets;
    }

    public void deleteFromNet(Context context, SwapPet pet) {
        pet.delete(context);
    }

}
