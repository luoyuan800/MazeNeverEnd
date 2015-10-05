package cn.gavin.forge;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import cn.gavin.R;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.activity.MazeContents;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 10/5/2015.
 */
public class AccessoryAdapter extends BaseAdapter {

    public static class AccessoryList {
        Accessory a1;
        Accessory a2;
        Accessory a3;
        Accessory a4;

        public AccessoryList() {

        }

        public boolean addAccessory(Accessory item) {
            if (a1 == null) a1 = item;
            else if (a2 == null) a2 = item;
            else if (a3 == null) a3 = item;
            else if (a4 == null) a4 = item;
            else return false;
            return true;
        }
    }

    static class AccessoryViewHolder {
        Button name1;
        Button name2;
        Button name3;
        Button name4;
    }

    private List<AccessoryList> loadAccessoryLists() {
        List<AccessoryList> res = new ArrayList<AccessoryList>();
        List<Accessory> accessories = Accessory.load();
        AccessoryList accessoryList = new AccessoryList();
        res.add(accessoryList);
        for (Accessory accessory : accessories) {
            if (!accessoryList.addAccessory(accessory)) {
                accessoryList = new AccessoryList();
                res.add(accessoryList);
                accessoryList.addAccessory(accessory);
            }
        }
        return res;
    }

    private final List<AccessoryList> adapterData = loadAccessoryLists();
    public TextView accDesc;

    @Override
    public int getCount() {
        return adapterData.size();
    }

    @Override
    public AccessoryList getItem(int position) {
        if (position >= getCount()) position = 0;
        return adapterData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        AccessoryViewHolder holder;
        if (convertView == null) {
            holder = new AccessoryViewHolder();
            convertView = View.inflate(MainGameActivity.context,
                    R.layout.acc_item, null);
            holder.name1 = (Button) convertView.findViewById(R.id.acc_name_1);

            holder.name2 = (Button) convertView.findViewById(R.id.acc_name_2);

            holder.name3 = (Button) convertView.findViewById(R.id.acc_name_3);
            holder.name4 = (Button) convertView.findViewById(R.id.acc_name_4);

            convertView.setTag(holder);
        } else {
            holder = (AccessoryViewHolder) convertView.getTag();
        }
        AccessoryList item = getItem(position);
        if (item.a1 != null) {
            holder.name1.setText(Html.fromHtml(item.a1.getFormatName()));
            holder.name1.setEnabled(true);
            holder.name1.setOnClickListener(buildOnClick(item.a1));
        } else {
            holder.name1.setText("");
            holder.name1.setEnabled(false);
        }

        if (item.a2 != null) {
            holder.name2.setText(Html.fromHtml(item.a2.getFormatName()));
            holder.name2.setEnabled(true);
            holder.name2.setOnClickListener(buildOnClick(item.a2));
        } else {
            holder.name2.setText("");
            holder.name2.setEnabled(false);
        }

        if (item.a3 != null) {
            holder.name3.setText(Html.fromHtml(item.a3.getFormatName()));
            holder.name3.setEnabled(true);
            holder.name3.setOnClickListener(buildOnClick(item.a3));
        } else {
            holder.name3.setText("");
            holder.name3.setEnabled(false);
        }
        if (item.a4 != null) {
            holder.name4.setText(Html.fromHtml(item.a4.getFormatName()));
            holder.name4.setEnabled(true);
            holder.name4.setOnClickListener(buildOnClick(item.a4));
        } else {
            holder.name4.setText("");
            holder.name4.setEnabled(false);
        }
        return convertView;
    }

    private View.OnClickListener buildOnClick(final Accessory a) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainGameActivity.context).create();
                alertDialog.setTitle("装备信息");
                TextView tv = new TextView(alertDialog.getContext());
                tv.setText(Html.fromHtml(a.toString()));
                alertDialog.setView(tv);
                boolean isOnUsed = false;
                switch (a.getType()) {
                    case RingBuilder.type:
                        isOnUsed = MazeContents.hero.getRing() == a;
                        break;
                    case NecklaceBuilder.type:
                        isOnUsed = MazeContents.hero.getNecklace() == a;
                        break;
                    case HatBuilder.type:
                        isOnUsed = MazeContents.hero.getHat() == a;
                        break;
                }
                if (isOnUsed) {
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "卸下", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            switch (a.getType()) {
                                case RingBuilder.type:
                                    MazeContents.hero.setRing(null);
                                    break;
                                case NecklaceBuilder.type:
                                    MazeContents.hero.setNecklace(null);
                                    break;
                                case HatBuilder.type:
                                    MazeContents.hero.setHat(null);
                                    break;
                            }
                            dialogInterface.dismiss();
                            MainGameActivity.context.getHandler().sendEmptyMessage(0);
                        }
                    });
                } else {
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "装备", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            switch (a.getType()) {
                                case RingBuilder.type:
                                    MazeContents.hero.setRing(a);
                                    break;
                                case NecklaceBuilder.type:
                                    MazeContents.hero.setNecklace(a);
                                    break;
                                case HatBuilder.type:
                                    MazeContents.hero.setHat(a);
                                    break;
                            }
                            dialogInterface.dismiss();
                            MainGameActivity.context.getHandler().sendEmptyMessage(0);
                        }
                    });
                }
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alertDialog.show();
            }
        };
    }
}
