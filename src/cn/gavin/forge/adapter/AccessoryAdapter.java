package cn.gavin.forge.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.gavin.R;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.utils.MazeContents;
import cn.gavin.forge.Accessory;
import cn.gavin.forge.HatBuilder;
import cn.gavin.forge.Item;
import cn.gavin.forge.NecklaceBuilder;
import cn.gavin.forge.RingBuilder;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 10/5/2015.
 */
public class AccessoryAdapter extends BaseAdapter {
    private int mark;
    private AlertDialog fartherDialog;

    public AccessoryAdapter(int mark, AlertDialog context) {
        this.mark = mark;
        this.fartherDialog = context;
    }

    public AccessoryAdapter() {
        mark = 0;
    }

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
        List<Accessory> accessories = Accessory.loadAccessories(null);
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

    private List<AccessoryList> adapterData = loadAccessoryLists();
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
            holder.name1.setText(Html.fromHtml(item.a1.getFormatName() + (checkOnUsed(item.a1) ? "<font color=\"#006400\">u</font>" : "")));
            holder.name1.setEnabled(true);
            holder.name1.setOnClickListener(buildOnClickListener(item.a1));
        } else {
            holder.name1.setText("");
            holder.name1.setEnabled(false);
        }

        if (item.a2 != null) {
            holder.name2.setText(Html.fromHtml(item.a2.getFormatName() + (checkOnUsed(item.a2) ? "<font color=\"#006400\">u</font>" : "")));
            holder.name2.setEnabled(true);
            holder.name2.setOnClickListener(buildOnClickListener(item.a2));
        } else {
            holder.name2.setText("");
            holder.name2.setEnabled(false);
        }

        if (item.a3 != null) {
            holder.name3.setText(Html.fromHtml(item.a3.getFormatName() + (checkOnUsed(item.a3) ? "<font color=\"#006400\">u</font>" : "")));
            holder.name3.setEnabled(true);
            holder.name3.setOnClickListener(buildOnClickListener(item.a3));
        } else {
            holder.name3.setText("");
            holder.name3.setEnabled(false);
        }
        if (item.a4 != null) {
            holder.name4.setText(Html.fromHtml(item.a4.getFormatName() + (checkOnUsed(item.a4) ? "<font color=\"#006400\">u</font>" : "")));
            holder.name4.setEnabled(true);
            holder.name4.setOnClickListener(buildOnClickListener(item.a4));
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
                boolean isOnUsed = checkOnUsed(a);
                final AlertDialog alertDialog = new AlertDialog.Builder(MainGameActivity.context).create();
                alertDialog.setTitle("装备信息");
                LinearLayout linearLayout = new LinearLayout(alertDialog.getContext());
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                TextView tv = new TextView(alertDialog.getContext());
                linearLayout.addView(tv);
                ViewGroup.LayoutParams params = tv.getLayoutParams();
                params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                tv.setLayoutParams(params);
                tv.setText(Html.fromHtml(a.toString()));
                alertDialog.setView(linearLayout);
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
                            notifyDataSetChanged();
                        }
                    });
                } else {
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "装备", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            for(Number num : a.getEffects().values()){
                                if(num.longValue() > MazeContents.hero.getUpperAtk() + MazeContents.hero.getUpperDef() + MazeContents.hero.getRealUHP()){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainGameActivity.context);
                                    builder.setMessage(Html.fromHtml(a.getFormatName()+ "属性太高，现在无法装备！请提高" +  MazeContents.hero.getFormatName() + "的属性！"));
                                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                    return;
                                }
                            }
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
                            notifyDataSetChanged();
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

    private View.OnClickListener buildOnClickListener(final Accessory a){
        switch (mark){
            case 0:
                return buildOnClick(a);
            case 1:
                return buildDOnClick(a);
            case 2:
                return buildROnClick(a);
        }
        return null;
    }

    private View.OnClickListener buildROnClick(final Accessory a){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isOnUsed = checkOnUsed(a);
                final AlertDialog alertDialog = new AlertDialog.Builder(fartherDialog.getContext()).create();
                alertDialog.setTitle("输入名字和描述");
                LinearLayout linearLayout = new LinearLayout(fartherDialog.getContext());
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout nameLayout = new LinearLayout(fartherDialog.getContext());
                nameLayout.setOrientation(LinearLayout.HORIZONTAL);
                final TextView textView = new TextView(fartherDialog.getContext());
                textView.setText("修改名字：");
                nameLayout.addView(textView);
                final EditText nameEdit = new EditText(fartherDialog.getContext());
                nameEdit.setText(a.getName());
                nameLayout.addView(nameEdit);
                linearLayout.addView(nameLayout);
                LinearLayout descrLayout = new LinearLayout(fartherDialog.getContext());
                descrLayout.setOrientation(LinearLayout.HORIZONTAL);
                TextView descText = new TextView(fartherDialog.getContext());
                descText.setText("修改描述：");
                descrLayout.addView(descText);
                final EditText descEdit = new EditText(fartherDialog.getContext());
                descEdit.setText("自定义装备描述");
                descrLayout.addView(descEdit);
                linearLayout.addView(descrLayout);
                alertDialog.setView(linearLayout);
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        a.setName(nameEdit.getText().toString());
                        a.setTag(descEdit.getText().toString());
                        a.save();
                        AlertDialog ad2 = new AlertDialog.Builder(alertDialog.getContext()).create();
                        ad2.setTitle("改名成功");
                        TextView textView1 = new TextView(alertDialog.getContext());
                        textView1.setText(Html.fromHtml(a.toString()));
                        ad2.setView(textView1);
                        ad2.setButton(DialogInterface.BUTTON_NEGATIVE, "知道了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        ad2.show();
                        alertDialog.dismiss();
                        fartherDialog.dismiss();
                        refresh();
                    }
                });
                alertDialog.show();
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(!isOnUsed);
            }
        };
    }

    private View.OnClickListener buildDOnClick(final Accessory a) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isOnUsed = checkOnUsed(a);
                final AlertDialog alertDialog = new AlertDialog.Builder(fartherDialog.getContext()).create();
                alertDialog.setTitle("拆解装备");
                final TextView textView = new TextView(fartherDialog.getContext());
                textView.setText(Html.fromHtml("拆解装备后你可以随机获得材料!<br>"+a.toString()));
                alertDialog.setView(textView);
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        List<Item> items = a.dismantle();
                        AlertDialog ad2 = new AlertDialog.Builder(alertDialog.getContext()).create();
                        ad2.setTitle("拆解结果");
                        TextView textView1 = new TextView(alertDialog.getContext());
                        StringBuilder builder = new StringBuilder("您从" + a.getFormatName() + "拆解出了：");
                        for (Item item : items) {
                            builder.append("<br>").append(item.toString());
                        }
                        textView1.setText(Html.fromHtml(builder.toString()));
                        ad2.setView(textView1);
                        ad2.setButton(DialogInterface.BUTTON_NEGATIVE, "退出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        ad2.show();
                        alertDialog.dismiss();
                        fartherDialog.dismiss();
                        refresh();
                    }
                });
                alertDialog.show();
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(!isOnUsed);
            }
        };
    }

    public void refresh(){
        adapterData = loadAccessoryLists();
        notifyDataSetChanged();

    }

    private boolean checkOnUsed(Accessory a) {
        boolean onUsed = false;
        switch (a.getType()) {
            case RingBuilder.type:
                Accessory ring = MazeContents.hero.getRing();
                onUsed = ring != null && ring.getId().equalsIgnoreCase(a.getId());
                break;
            case NecklaceBuilder.type:
                Accessory necklace = MazeContents.hero.getNecklace();
                onUsed = necklace != null && necklace.getId().equalsIgnoreCase(a.getId());
                break;
            case HatBuilder.type:
                Accessory hat = MazeContents.hero.getHat();
                onUsed = hat != null && hat.getId().equalsIgnoreCase(a.getId());
                break;
        }
        return onUsed;
    }
}
