package cn.gavin.pet.swop;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.gavin.pet.Pet;
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

    public void finished(List<SwapPet> pets) {
        this.pets = pets;
        finished = true;
    }

    public void acknowledge(Context context, SwapPet pet) {
        pet.setAcknowledge(true);
        pet.update(context);
    }

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

    public void uploadMyPet(Context context, Pet pet) {

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
}
