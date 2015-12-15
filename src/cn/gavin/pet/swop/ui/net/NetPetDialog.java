package cn.gavin.pet.swop.ui.net;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import cn.gavin.R;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.log.LogHelper;
import cn.gavin.monster.Monster;
import cn.gavin.pet.Pet;
import cn.gavin.pet.swop.SwapManager;
import cn.gavin.pet.swop.SwapPet;
import cn.gavin.pet.swop.ui.PetInfoDialogBuilder;
import cn.gavin.pet.swop.ui.PetSimpleViewAdapter;
import cn.gavin.utils.MazeContents;
import cn.gavin.utils.StringUtils;
import cn.gavin.utils.ui.LoadMoreListView;
import cn.gavin.utils.ui.SlidingMenu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 12/4/2015.
 */
public class NetPetDialog implements LoadMoreListView.OnRefreshLoadingMoreListener, View.OnClickListener {
    AlertDialog dialog;
    ProgressDialog progressDialog;
    final static Context context = MainGameActivity.context;
    SwapManager swapManager;
    SwapPet query;
    List<SwapPet> pets;
    NetPetSimpleViewAdapter adapter;
    LoadMoreListView listView;
    int page = 1;
    SlidingMenu slidingMenu;
    SwapPet netPet;
    List<DialogInterface> shouldCloseDialog = new ArrayList<DialogInterface>();
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case 0:
                        show();
                        break;
                    case 1:
                        Button button = (Button) dialog.findViewById(R.id.net_query_button);
                        button.setEnabled(false);
                        button.setText("查询中");
                        loadMore();
                        break;
                    case 2:
                        slidingMenu.closeMenu();
                        break;
                    case 3:
                        Button button3 = (Button) dialog.findViewById(R.id.net_query_button);
                        button3.setEnabled(true);
                        button3.setText("查询");
                        slidingMenu.closeMenu();
                        break;
                    case 4:
                        loadMoeResult();
                        break;
                    case 5:
                        Pet pet = (Pet) msg.obj;
                        AlertDialog newPetDialog = PetInfoDialogBuilder.build(pet, context, "照顾好:" + pet.getFormatName());
                        newPetDialog.show();
                        for(DialogInterface dialogInterface : shouldCloseDialog){
                            dialogInterface.dismiss();
                        }
                        adapter.clean();
                        page = 0;
                        handler.sendEmptyMessage(1);
                        break;
                    case 6:
                        AlertDialog err = new AlertDialog.Builder(context).create();
                        err.setMessage("连接失败，请稍候后重试！");
                        err.setButton(DialogInterface.BUTTON_NEGATIVE, "退出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        err.show();
                        break;
                }
                super.handleMessage(msg);
            } catch (Exception e) {
                LogHelper.logException(e);
                e.printStackTrace();
            }
        }
    };

    public NetPetDialog() {
        try {
            dialog = new AlertDialog.Builder(context).create();
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.net_pet_simple_view, null);
            dialog.setView(view);
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            swapManager = new SwapManager();
            query = new SwapPet();
            swapManager.searchPet(context, query, 1);
            progressDialog = new ProgressDialog(context);
            progressDialog.setIndeterminate(true);
            progressDialog.setTitle("正在连接宠物中心");
            progressDialog.setMessage("请稍候...");
            progressDialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                        while (!swapManager.isFinished()) ;
                        progressDialog.dismiss();
                        pets = swapManager.getResult();
                        if (pets != null && !pets.isEmpty()) {
                            handler.sendEmptyMessage(0);
                        } else {
                            handler.sendEmptyMessage(6);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogHelper.logException(e);
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
            LogHelper.logException(e);
        }
    }

    public void show() {
        adapter = new NetPetSimpleViewAdapter();
        adapter.setNetPetDialog(this);
        adapter.addPets(pets.toArray(new SwapPet[pets.size()]));
        dialog.show();
        listView = (LoadMoreListView) dialog.findViewById(R.id.net_pet_list);
        listView.setAdapter(adapter);
        listView.setOnLoadListener(this);
        ImageView open = (ImageView) dialog.findViewById(R.id.switch_menu);
        open.setOnClickListener(this);
        Button queryButton = (Button) dialog.findViewById(R.id.net_query_button);
        queryButton.setOnClickListener(this);
        slidingMenu = (SlidingMenu) dialog.findViewById(R.id.net_pet_root);
        slidingMenu.closeMenu();
        AlertDialog alertDialog = dialog;
        TextView title = (TextView) alertDialog.findViewById(R.id.pet_swap_title);
        title.setText(Html.fromHtml("设置查询条件"));
        Spinner fs = (Spinner) alertDialog.findViewById(R.id.first_name);
        Spinner ss = (Spinner) alertDialog.findViewById(R.id.second_name);
        Spinner ls = (Spinner) alertDialog.findViewById(R.id.last_name);
        List<String> firstNames = new ArrayList<String>(Monster.firstNames.length);
        firstNames.add("无");
        Collections.addAll(firstNames, Monster.firstNames);
        List<String> secondNames = new ArrayList<String>(Monster.secondNames.length);
        secondNames.add("无");
        Collections.addAll(secondNames, Monster.secondNames);
        List<String> lastNames = new ArrayList<String>(Monster.lastNames.length);
        lastNames.add("无");
        Collections.addAll(lastNames, Monster.lastNames);
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

    public void dismiss() {
        dialog.dismiss();
    }


    @Override
    public void onLoadMore() {
        loadMore();
    }

    private void loadMore() {
        try {

            page++;
            swapManager.searchPet(context, query, page);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (!swapManager.isFinished()) ;
                        handler.sendEmptyMessage(4);
                    } catch (Exception e) {
                        LogHelper.logException(e);
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
            LogHelper.logException(e);
        }
    }

    private void loadMoeResult() {
        List<SwapPet> morePets = swapManager.getResult();
        if (morePets != null && !morePets.isEmpty()) {
            adapter.addPets(morePets.toArray(new SwapPet[morePets.size()]));
            listView.onLoadMoreComplete(false);
        } else {
            listView.onLoadMoreComplete(true);
        }
        handler.sendEmptyMessage(3);
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.open_menu:
                case R.id.switch_menu:
                    if (slidingMenu != null) {
                        slidingMenu.toggle();
                    }
                    break;
                case R.id.net_query_button:
                    try {
                        SwapPet swapPet = query;
                        Spinner fs = (Spinner) dialog.findViewById(R.id.first_name);
                        Spinner ss = (Spinner) dialog.findViewById(R.id.second_name);
                        Spinner ls = (Spinner) dialog.findViewById(R.id.last_name);
                        String first = (String) fs.getSelectedItem();
                        String second = (String) ss.getSelectedItem();
                        String last = (String) ls.getSelectedItem();
                        String askName = "";
                        if (first != null && !"无".equals(first)) {
                            askName += (first + "的");
                        }
                        if (second != null && !"无".equals(second)) {
                            askName += second;
                        }
                        if (last != null && !"无".equals(last)) {
                            swapPet.setAskType(Monster.getIndex(last));
                        }
                        if (StringUtils.isNotEmpty(askName)) {
                            swapPet.setAskName(askName);
                        }
                        TextView askAtk = (TextView) dialog.findViewById(R.id.ask_atk);
                        swapPet.setAskAtk(StringUtils.toLong(askAtk.getText().toString()));
                        TextView askDef = (TextView) dialog.findViewById(R.id.ask_def);
                        swapPet.setAskDef(StringUtils.toLong(askDef.getText().toString()));
                        TextView askHp = (TextView) dialog.findViewById(R.id.ask_hp);
                        swapPet.setAskHp(StringUtils.toLong(askHp.getText().toString()));
                        Spinner sexs = (Spinner) dialog.findViewById(R.id.ask_sex);
                        if ("♂".equals(sexs.getSelectedItem())) {
                            swapPet.setAskSex(0);
                        } else if ("♀".equals(sexs.getSelectedItem())) {
                            swapPet.setAskSex(1);
                        }
                        adapter.clean();
                        page = 0;
                        handler.sendEmptyMessage(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogHelper.logException(e);
                    }
                    break;
                default:
                    if (v.getTag() != null && v.getTag() instanceof NetPetSimpleViewAdapter.PetViewHolder) {
                        NetPetSimpleViewAdapter.PetViewHolder petViewHolder = (NetPetSimpleViewAdapter.PetViewHolder) v.getTag();
                        netPet = petViewHolder.getPet();
                        List<Pet> myPets = swapManager.myAvailablePets(netPet);
                        AlertDialog myPetDialog = new AlertDialog.Builder(context).create();
                        if (!myPets.isEmpty()) {
                            PetSimpleViewAdapter petSimpleViewAdapter = new PetSimpleViewAdapter(this);
                            petSimpleViewAdapter.addPets(myPets.toArray(new Pet[myPets.size()]));
                            LoadMoreListView loadMoreListView = new LoadMoreListView(context);
                            loadMoreListView.setAdapter(petSimpleViewAdapter);
                            loadMoreListView.onLoadMoreComplete(true);
                            myPetDialog.setView(loadMoreListView);
                        } else {
                            StringBuilder condition = new StringBuilder("要求为:<br>");
                            if(netPet.getAskAtk()!=null) {
                                condition.append("攻击高于 <i>").append(netPet.getAskAtk()).append("</i><br>");
                            }
                            if(netPet.getAskDef()!=null) {
                                condition.append("防御高于 <i>").append(netPet.getAskDef()).append("</i><br>");
                            }
                            if(netPet.getAskHp()!=null) {
                                condition.append("hp高于 <i>").append(netPet.getAskHp()).append("</i><br>");
                            }
                            if(netPet.getAskType()!=null) {
                                if( netPet.getAskType() < Monster.lastNames.length) {
                                    condition.append("种类为 <i>").append(Monster.lastNames[netPet.getAskType()]).append("</i><br>");
                                }
                            }
                            if(StringUtils.isNotEmpty(netPet.getAskName())) {
                                condition.append("前后缀 <i>").append(netPet.getAskName()).append("</i><br>");
                            }
                            if(netPet.getAskSkill()!=null){
                                condition.append("技能限定 <i>").append(netPet.getAskSkill()).append("</i><br>");
                            }
                            if(netPet.getAskSex()!=null){
                                condition.append("性别限定 <i>").append(netPet.getAskSex()==0?"♂":"♀").append("</i><br>");
                            }
                            TextView textView = new TextView(context);
                            textView.setText(Html.fromHtml("<font color=\"red\">对不起！您没有符合对方要求的宠物。</font>" + condition));
                            myPetDialog.setView(textView);
                        }
                        myPetDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "退出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        myPetDialog.show();
                        shouldCloseDialog.add(myPetDialog);
                    } else if (v.getTag() != null && v.getTag() instanceof PetSimpleViewAdapter.PetViewHolder && netPet != null) {
                        Pet myPet = ((PetSimpleViewAdapter.PetViewHolder) v.getTag()).getPet();
                        if(!MazeContents.checkPet(myPet)){
                            Toast.makeText(context, "宠物数据异常，无法上传！", Toast.LENGTH_SHORT).show();
                            return ;
                        }
                        final SwapPet mySwapPet = SwapPet.buildSwapPet(myPet);
                        final Pet netSwapPet = netPet.buildPet();
                        mySwapPet.setKeeperId(netPet.getKeeperId());
                        mySwapPet.setKeeperName(netPet.getKeeperName());
                        netPet.setKeeperId(MazeContents.hero.getUuid());
                        netPet.setKeeperName(MazeContents.hero.getName());
                        mySwapPet.setChangedPet(netPet);
                        swapManager.uploadPet(context, mySwapPet, myPet);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                    while (!swapManager.isFinished()) ;
                                    netPet.setChangedPet(mySwapPet);
                                    SwapPet updatePet = new SwapPet();
                                    updatePet.setObjectId(netPet.getObjectId());
                                    updatePet.setKeeperId(netPet.getKeeperId());
                                    updatePet.setKeeperName(netPet.getKeeperName());
                                    SwapPet changedPet = new SwapPet();
                                    changedPet.setObjectId(netPet.getChangedPet().getObjectId());
                                    updatePet.setChangedPet(changedPet);
                                    swapManager.acknowledge(context, updatePet);
                                    netSwapPet.save();
                                    Message message = new Message();
                                    message.what = 5;
                                    message.obj = netSwapPet;
                                    handler.sendMessage(message);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    LogHelper.logException(e);
                                }
                            }
                        }).start();
                    }
            }
        } catch (Exception e) {
            LogHelper.logException(e);
            e.printStackTrace();
        }
    }
}
