package cn.gavin.upload;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.GetListener;
import cn.gavin.Element;
import cn.gavin.Hero;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.activity.MazeContents;
import cn.gavin.forge.Accessory;
import cn.gavin.forge.Item;
import cn.gavin.forge.effect.Effect;
import cn.gavin.forge.list.ItemName;
import cn.gavin.log.LogHelper;
import cn.gavin.utils.StringUtils;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 10/26/2015.
 */
public class CdKey extends BmobObject {
    private Long lockBox;
    private Long key;
    private Long material;
    private Long skillPoint;
    private Long point;
    private String accName;
    private int accType;
    private String accEffect;
    private String accAdditionEffect;
    private String accElement;
    private String accColor;
    private Boolean isUsed;
    private Boolean isOnce;
    private String[] items;

    public Long getLockBox() {
        return lockBox;
    }

    public void setLockBox(Long lockBox) {
        this.lockBox = lockBox;
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public Long getMaterial() {
        return material;
    }

    public void setMaterial(Long material) {
        this.material = material;
    }

    public Long getSkillPoint() {
        return skillPoint;
    }

    public void setSkillPoint(Long skillPoint) {
        this.skillPoint = skillPoint;
    }

    public Long getPoint() {
        return point;
    }

    public void setPoint(Long point) {
        this.point = point;
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public int getAccType() {
        return accType;
    }

    public void setAccType(int accType) {
        this.accType = accType;
    }

    public String getAccEffect() {
        return accEffect;
    }

    public void setAccEffect(String accEffect) {
        this.accEffect = accEffect;
    }

    public String getAccAdditionEffect() {
        return accAdditionEffect;
    }

    public void setAccAdditionEffect(String accAdditionEffect) {
        this.accAdditionEffect = accAdditionEffect;
    }

    public String getAccElement() {
        return accElement;
    }

    public void setAccElement(String accElement) {
        this.accElement = accElement;
    }

    public String getAccColor() {
        return accColor;
    }

    public void setAccColor(String accColor) {
        this.accColor = accColor;
    }


    public boolean perform(Hero hero) {
        if (isUsed && isOnce) {
            return false;
        }
        if (StringUtils.isNotEmpty(accName)) {
            Accessory accessory = new Accessory();
            accessory.setName(accName);
            accessory.setElement(Element.valueOf(accElement));
            accessory.setColor(accColor);
            EnumMap<Effect, Number> effectNumberEnumMap = new EnumMap<Effect, Number>(Effect.class);
            for (String effect : StringUtils.split(accEffect, "-")) {
                String[] effectValue = StringUtils.split(effect, ":");
                if (effectValue.length > 1) {
                    effectNumberEnumMap.put(Effect.valueOf(effectValue[0]), StringUtils.toLong(effectValue[1]));
                }
            }
            accessory.setEffects(effectNumberEnumMap);
            EnumMap<Effect, Number> additionEffectNumberEnumMap = new EnumMap<Effect, Number>(Effect.class);
            for (String effect : StringUtils.split(accAdditionEffect, "-")) {
                String[] effectValue = StringUtils.split(effect, ":");
                if (effectValue.length > 1) {
                    additionEffectNumberEnumMap.put(Effect.valueOf(effectValue[0]), StringUtils.toLong(effectValue[1]));
                }
            }
            accessory.setAdditionEffects(additionEffectNumberEnumMap);
            try {
                if (items != null && items.length > 3) {
                    List<Item> is = new ArrayList<Item>(5);
                    for (String item : items) {
                        Item i = new Item();
                        i.setName(ItemName.valueOfName(item));
                        is.add(i);
                    }
                    accessory.setItems(is);
                    accessory.setSave(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogHelper.logException(e);
            }
            accessory.save();
        } else {
            hero.addMaterial(material);
            hero.setSkillPoint(hero.getSkillPoint() + skillPoint);
            hero.addPoint(point);
            hero.setLockBox(hero.getLockBox() + lockBox);
            hero.setKeyCount(hero.getKeyCount() + key);
        }
        if (isOnce) {
            this.setIsUsed(true);
            this.update(MainGameActivity.context);
        }
        return true;
    }

    public Boolean getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Boolean isUsed) {
        this.isUsed = isUsed;
    }

    public Boolean getIsOnce() {
        return isOnce;
    }

    public void setIsOnce(Boolean isOnce) {
        this.isOnce = isOnce;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("您获得了：<br>");
        if (StringUtils.isNotEmpty(accName)) {
            stringBuilder.append("<font color=\"").append(accColor).append("\">").append(accName).append("</font>");
        } else {
            if (material != 0) stringBuilder.append("锻造点数：").append(material).append("<br>");
            if (skillPoint != 0) stringBuilder.append("技能点数：").append(skillPoint).append("<br>");
            if (point != 0) stringBuilder.append("能力点数：").append(point).append("<br>");
        }
        return stringBuilder.toString();
    }

    public static void checkKey(String input, final Dialog dialog) {
        final String key = input.replaceFirst("~", "");
        BmobQuery<CdKey> query = new BmobQuery<CdKey>();
        final Context context = dialog.getContext();
        final AlertDialog succDialog = new AlertDialog.Builder(context).create();
        succDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "确定",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }

                });
        query.getObject(context, key, new GetListener<CdKey>() {

            @Override
            public void onSuccess(CdKey object) {
                dialog.dismiss();
                TextView view = new TextView(context);
                if (object.perform(MazeContents.hero)) {
                    view.setText(Html.fromHtml(object.toString()));
                    succDialog.setView(view);
                } else {
                    view.setText("这个兑换码已经使用过了！");
                    succDialog.setView(view);
                }
                succDialog.show();
            }

            @Override
            public void onFailure(int code, String arg0) {
                dialog.dismiss();
                TextView view = new TextView(context);
                view.setText("无此兑换码！");
                succDialog.setView(view);
                succDialog.show();
            }
        });
    }

    public String[] getItems() {
        return items;
    }

    public void setItems(String[] items) {
        this.items = items;
    }
}
