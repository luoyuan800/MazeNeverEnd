package cn.gavin.pet.swop.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.text.Html;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.gavin.R;
import cn.gavin.db.DBHelper;
import cn.gavin.log.LogHelper;
import cn.gavin.monster.FirstName;
import cn.gavin.monster.SecondName;
import cn.gavin.pet.Pet;
import cn.gavin.pet.PetDB;
import cn.gavin.pet.swop.SwapManager;
import cn.gavin.pet.swop.SwapPet;
import cn.gavin.utils.MazeContents;
import cn.gavin.utils.StringUtils;
import cn.gavin.utils.ui.LoadMoreListView;

/**
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED
 * Created by luoyuan on 11/23/15.
 */
public class SwapDialog implements LoadMoreListView.OnRefreshLoadingMoreListener, View.OnClickListener {
    private LoadMoreListView listView;
    private PetSimpleViewAdapter adapter;
    List<Pet> petList;
    int currentIndex = 0;
    private Context context;
    private AlertDialog dialog;

    public SwapDialog(Context context) {
        petList = new ArrayList<Pet>();
        List<Pet> allPets = PetDB.loadPet(null);
        for (Pet p : allPets) {
            if (!MazeContents.hero.petOnUsed(p)) {
                petList.add(p);
            }
        }
        this.context = context;
        listView = new LoadMoreListView(context);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(listView);
        builder.setTitle("选择您的宠物");
        builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        Pet[] pets = new Pet[5];
        for (; currentIndex < petList.size() && currentIndex < 5; currentIndex++) {
            if (!MazeContents.hero.getPets().contains(petList.get(currentIndex))) {
                pets[currentIndex] = petList.get(currentIndex);
            }
        }
        adapter = new PetSimpleViewAdapter(this);
        adapter.addPets(pets);
        listView.setAdapter(adapter);
        listView.setOnLoadListener(this);
        if (currentIndex < petList.size()) {
            listView.onLoadMoreComplete(false);
        } else {
            listView.onLoadMoreComplete(true);
        }
    }

    public void show() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }

    @Override
    public void onLoadMore() {
        if (currentIndex < petList.size()) {
            Pet[] pets = new Pet[5];
            int size = 0;
            for (; currentIndex < petList.size() && size < 5; currentIndex++, size++) {
                if (!MazeContents.hero.getPets().contains(petList.get(currentIndex))) {
                    pets[size] = petList.get(currentIndex);
                }
            }
            adapter.addPets(pets);
        }
        if (currentIndex < petList.size()) {
            listView.onLoadMoreComplete(false);
        } else {
            listView.onLoadMoreComplete(true);
        }
    }

    @Override
    public void onClick(View v) {
        try {
            if (v.getTag() != null && v.getTag() instanceof PetSimpleViewAdapter.PetViewHolder) {
                PetSimpleViewAdapter.PetViewHolder holder = (PetSimpleViewAdapter.PetViewHolder) v.getTag();
                final Pet pet = holder.getPet();
                if (pet != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    LayoutInflater inflater = LayoutInflater.from(context);
                    View view = inflater.inflate(R.layout.swap_tiao_jian, (ViewGroup) ((Activity) context).findViewById(R.id.swap_tiao_jian_root));
                    builder.setView(view);
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {
                            AlertDialog message = new AlertDialog.Builder(context).create();
                            final EditText editText = new EditText(context);
                            editText.setText("大家隨便換");
                            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
                            message.setTitle("輸入您的留言（簡介，要求...）");
                            message.setView(editText);
                            message.setButton(DialogInterface.BUTTON_POSITIVE, "確認", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInt, int which) {
                                    SwapPet swapPet = SwapPet.buildSwapPet(pet);
                                    swapPet.setHello(editText.getText().toString());
                                    AlertDialog alertDialog = (AlertDialog) dialog;
                                    Spinner fs = (Spinner) alertDialog.findViewById(R.id.first_name);
                                    Spinner ss = (Spinner) alertDialog.findViewById(R.id.second_name);
                                    Spinner ls = (Spinner) alertDialog.findViewById(R.id.last_name);
                                    String first = (String) fs.getSelectedItem();
                                    String second = (String) ss.getSelectedItem();
                                    String last = (String) ls.getSelectedItem();
                                    String askName = "";
                                    if (!"无".equals(first)) {
                                        askName += (first + "的");
                                    }
                                    if (!"无".equals(second)) {
                                        askName += second;
                                    }
                                    if (!"无".equals(last)) {
                                        swapPet.setAskType(last);
                                    }
                                    if (StringUtils.isNotEmpty(askName)) {
                                        swapPet.setAskName(askName);
                                    }
                                    TextView askAtk = (TextView) alertDialog.findViewById(R.id.ask_atk);
                                    swapPet.setAskAtk(StringUtils.toLong(askAtk.getText().toString()));
                                    TextView askDef = (TextView) alertDialog.findViewById(R.id.ask_def);
                                    swapPet.setAskDef(StringUtils.toLong(askDef.getText().toString()));
                                    TextView askHp = (TextView) alertDialog.findViewById(R.id.ask_hp);
                                    swapPet.setAskHp(StringUtils.toLong(askHp.getText().toString()));
                                    Spinner sexs = (Spinner) alertDialog.findViewById(R.id.ask_sex);
                                    if ("♂".equals(sexs.getSelectedItem())) {
                                        swapPet.setAskSex(0);
                                    } else if ("♀".equals(sexs.getSelectedItem())) {
                                        swapPet.setAskSex(1);
                                    }
                                    SwapManager swapManager = new SwapManager();
                                    swapManager.uploadPet(context, swapPet, pet);
                                    dialogInt.dismiss();
                                }
                            });
                            message.show();
                            dialog.dismiss();
                            dismiss();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    TextView title = (TextView) alertDialog.findViewById(R.id.pet_swap_title);
                    title.setText(Html.fromHtml("设置交换" + pet.getFormatName() + "的条件"));
                    Spinner fs = (Spinner) alertDialog.findViewById(R.id.first_name);
                    Spinner ss = (Spinner) alertDialog.findViewById(R.id.second_name);
                    Spinner ls = (Spinner) alertDialog.findViewById(R.id.last_name);
                    List<String> firstNames = new ArrayList<String>(FirstName.values().length);
                    firstNames.add("无");
                    for (FirstName firstName : FirstName.values()) {
                        if (firstName != FirstName.empty) {
                            firstNames.add(firstName.getName());
                        }
                    }
                    List<String> secondNames = new ArrayList<String>(SecondName.values().length);
                    secondNames.add("无");
                    for (SecondName secondName : SecondName.values()) {
                        if (secondName != SecondName.empty) {
                            secondNames.add(secondName.getName());
                        }
                    }
                    List<String> lastNames = new ArrayList<String>();
                    Cursor cursor = DBHelper.getDbHelper().excuseSOL("select type from monster");
                    while (!cursor.isAfterLast()) {
                        lastNames.add(cursor.getString(cursor.getColumnIndex("type")));
                    }
                    cursor.close();
                    lastNames.add("无");
                    ArrayAdapter fa = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, firstNames);
                    fa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    fs.setAdapter(fa);
                    ArrayAdapter sa = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, secondNames);
                    fa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ss.setAdapter(sa);
                    ArrayAdapter la = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, lastNames);
                    la.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ls.setAdapter(la);
                    Spinner sexs = (Spinner) alertDialog.findViewById(R.id.ask_sex);
                    ArrayAdapter sexa = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, new String[]{"无", "♂", "♀"});
                    sexs.setAdapter(sexa);
                }
            }
        } catch (Exception e) {
            LogHelper.logException(e);
        }
    }
}
