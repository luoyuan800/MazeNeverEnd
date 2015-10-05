package cn.gavin.forge;

import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.activity.R;

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

        public AccessoryList() {

        }

        public boolean addAccessory(Accessory item) {
            if (a1 == null) a1 = item;
            else if (a2 == null) a2 = item;
            else if (a3 == null) a3 = item;
            else return false;
            return true;
        }
    }

    static class AccessoryViewHolder {
        Button name1;
        Button name2;
        Button name3;
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
            holder.name1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    accDesc.setText(Html.fromHtml(getItem(position).a1.toString()));
                }
            });
            holder.name2 = (Button) convertView.findViewById(R.id.acc_name_2);
            holder.name2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    accDesc.setText(Html.fromHtml(getItem(position).a2.toString()));
                }
            });
            holder.name3 = (Button) convertView.findViewById(R.id.acc_name_3);
            holder.name3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    accDesc.setText(Html.fromHtml(getItem(position).a3.toString()));
                }
            });
            convertView.setTag(holder);
        } else {
            holder = (AccessoryViewHolder) convertView.getTag();
        }
        AccessoryList item = getItem(position);
        if (item.a1 != null) {
            holder.name1.setText(Html.fromHtml(item.a1.getFormatName()));
            holder.name1.setEnabled(true);
        } else {
            holder.name1.setText("");
            holder.name1.setEnabled(false);
        }

        if (item.a2 != null) {
            holder.name2.setText(Html.fromHtml(item.a2.getFormatName()));
            holder.name2.setEnabled(true);
        } else {
            holder.name2.setText("");
            holder.name2.setEnabled(false);
        }

        if (item.a3 != null) {
            holder.name3.setText(Html.fromHtml(item.a3.getFormatName()));
            holder.name3.setEnabled(true);
        } else {
            holder.name3.setText("");
            holder.name3.setEnabled(false);
        }

        return convertView;
    }
}
