package cn.gavin.pet.swop.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import cn.gavin.R;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.log.LogHelper;
import cn.gavin.pet.Pet;
import cn.gavin.pet.swop.SwapManager;
import cn.gavin.pet.swop.SwapPet;
import cn.gavin.pet.swop.ui.net.NetPetDialog;
import cn.gavin.utils.MazeContents;
import cn.gavin.utils.StringUtils;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 12/4/2015.
 */
public class SwapPetMainDialog {
    AlertDialog mainDialog;
    ProgressDialog progressDialog;
    Context context = MainGameActivity.context;
    SwapManager swapManager;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case 0:
                        progressDialog.dismiss();
                        List<SwapPet> petList = swapManager.getResult();
                        if (!petList.isEmpty()) {
                            final SwapPet pet = petList.get(0);
                            final Pet myPet = pet.buildPet();
                            if (pet.getChangedPet() != null) {
                                SwapPet changePet = pet.getChangedPet();
                                String title;
                                if(StringUtils.isNotEmpty(changePet.getName())){
                                    title = "你用 " + changePet.getFormateName() + "换到了";
                                }else{
                                    title = "你获得了" + myPet.getFormatName();
                                }
                                mainDialog = PetInfoDialogBuilder.build(myPet, context, title);
                                mainDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        myPet.save();
                                        myPet.updateMonster();
                                        swapManager.acknowledge(context, pet);
                                        Toast.makeText(context, "--取回了" + pet.getName() + "，请好好照顾--", Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                });
                            } else {
                                mainDialog = PetInfoDialogBuilder.build(myPet, context, "你有一个宠物寄存在宠物中心");
                                mainDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "退出", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                mainDialog.setButton(DialogInterface.BUTTON_POSITIVE, "取回", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        myPet.save();
                                        swapManager.deleteFromNet(context, pet);
                                        Toast.makeText(context, "--成功取回" + pet.getName() + "--", Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                });
                            }
                        } else {
                            mainDialog = new AlertDialog.Builder(context).create();
                            LayoutInflater inflater = LayoutInflater.from(context);
                            View view = inflater.inflate(R.layout.swap_main_view, null);
                            view.findViewById(R.id.search_net_pet).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mainDialog.dismiss();
                                    NetPetDialog netPetDialog = new NetPetDialog();
                                }
                            });
                            View uploadView = view.findViewById(R.id.upload_my_pet);
                            if(MazeContents.hero.getMaterial() < 100000){
                                uploadView.setEnabled(false);
                            }else {
                                uploadView.setEnabled(true);
                                uploadView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        MazeContents.hero.addMaterial(-100000);
                                        mainDialog.dismiss();
                                        SwapDialog swapDialog = new SwapDialog(context);
                                        swapDialog.show();
                                    }
                                });
                            }
                            mainDialog.setView(view);
                            mainDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "退出", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                        }
                        if (mainDialog != null) {
                            mainDialog.show();
                        }
                        break;
                }
            }catch (Exception e){
                e.printStackTrace();
                LogHelper.logException(e, false);
            }
        }
    };

    public SwapPetMainDialog() {
        swapManager = new SwapManager();
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("正在连接宠物中心");
        progressDialog.setMessage("请稍候");
        progressDialog.show();
        swapManager.getAllMyOwnInNet(context);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    LogHelper.logException(e, false);
                    e.printStackTrace();
                }
                while (!swapManager.isFinished()) ;
                handler.sendEmptyMessage(0);
            }
        }).start();
    }
}
