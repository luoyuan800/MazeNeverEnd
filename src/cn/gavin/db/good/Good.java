package cn.gavin.db.good;

import android.database.sqlite.SQLiteDatabase;

import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.db.good.detail.Aphrodisiac;
import cn.gavin.db.good.detail.Barrier;
import cn.gavin.db.good.detail.EggGift;
import cn.gavin.db.good.detail.GiftBag;
import cn.gavin.db.good.detail.HalfSail;
import cn.gavin.db.good.detail.LocalGood;
import cn.gavin.db.good.detail.Medallion;
import cn.gavin.db.good.detail.PetRevive;
import cn.gavin.db.good.detail.RandomPortal;
import cn.gavin.db.good.detail.SafetyRope;
import cn.gavin.db.good.detail.SpecialPortal;
import cn.gavin.log.LogHelper;
import cn.gavin.utils.MazeContents;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 11/24/2015.
 */
public class Good extends BmobObject {
    private String name;
    private String desc;
    private Integer sailCount;
    private Long cost;
    private Boolean isLimited;
    private String type;
    private String[] eggTypes;
    private Integer reduce;
    public LocalGood getGood(){
        try {
            Class<LocalGood> clazz = (Class<LocalGood>) this.getClass().getClassLoader().loadClass(type);
            LocalGood good =  clazz.newInstance();
            switch (good.getType()){
                case Aphrodisiac.type:
                    Aphrodisiac aphrodisiac = (Aphrodisiac) good;
                    break;
                case Barrier.type:
                    break;
                case EggGift.type:
                    EggGift eggGift = (EggGift)good;
                    eggGift.setTypes(Arrays.asList(eggTypes));
                    eggGift.setReduce(reduce);
                    break;
                case GiftBag.type:
                    break;
                case HalfSail.type:
                    break;
                case Medallion.type:
                    break;
                case PetRevive.type:
                    break;
                case RandomPortal.type:
                    break;
                case SafetyRope.type:
                    break;
                case SpecialPortal.type:
                    break;
            }
            return good;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            LogHelper.logException(e);
        } catch (InstantiationException e) {
            e.printStackTrace();
            LogHelper.logException(e);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            LogHelper.logException(e);
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getSailCount() {
        return sailCount;
    }

    public void setSailCount(Integer sailCount) {
        this.sailCount = sailCount;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public void buy(int i) {
        if(isLimited){
            i = 1;
        }
        this.setSailCount(sailCount - i);
        this.update(MainGameActivity.context);
        //先查询出数据库中的个数，然后个数(count)加+i
    }

    public Boolean getIsLimited() {
        return isLimited;
    }

    public void setIsLimited(Boolean isLimited) {
        this.isLimited = isLimited;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getType(){
        return type;
    }

    public String[] getEggTypes() {
        return eggTypes;
    }

    public void setEggTypes(String[] eggTypes) {
        this.eggTypes = eggTypes;
    }

    public Integer getReduce() {
        return reduce;
    }

    public void setReduce(Integer reduce) {
        this.reduce = reduce;
    }
}
