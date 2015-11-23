package cn.gavin.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.gavin.R;
import cn.gavin.forge.*;
import cn.gavin.forge.adapter.AccessoryAdapter;
import cn.gavin.forge.dialog.ItemDialog;
import cn.gavin.forge.list.FuseItems;
import cn.gavin.utils.MazeContents;

import java.util.ArrayList;
import java.util.List;


/**
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED
 * Created by luoyuan on 10/4/15.
 */
public class ForgeActivity extends Activity implements View.OnClickListener, View.OnLongClickListener {
    private GestureDetector detector; //手势检测
    private TextView systemName;
    private Button item1Button, item2Button, item3Button, item4Button, item5Button;
    ItemDialog itemDialog;
    private Item item1, item2, item3, item4, item5;
    private TextView resultText;
    private Builder ringBuilder = new RingBuilder();
    private Builder hatBuilder = new HatBuilder();
    private Builder necklaceBuilder = new NecklaceBuilder();
    List<Item> items = new ArrayList<Item>();
    private Button forgeButton;
    private Button conformButton;
    private Button cleanButton;
    private Button fuseButton;
    private final int mate = 100250;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainGameActivity.context.setPause(true);
        setContentView(R.layout.forge_view);
        systemName = (TextView) findViewById(R.id.forge_system_name);
        systemName.setText(systemNames[index]);
        Button pre = (Button) findViewById(R.id.prev_forge_system_button);
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSystem(false);
            }
        });
        Button next = (Button) findViewById(R.id.next_forge_system_button);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSystem(true);
            }
        });
        detector = new GestureDetector(this, gestureListener);
        item1Button = (Button) findViewById(R.id.forge_item_1);
        item2Button = (Button) findViewById(R.id.forge_item_2);
        item3Button = (Button) findViewById(R.id.forge_item_3);
        item4Button = (Button) findViewById(R.id.forge_item_4);
        item5Button = (Button) findViewById(R.id.forge_item_5);
        item1Button.setOnClickListener(this);
        item2Button.setOnClickListener(this);
        item3Button.setOnClickListener(this);
        item4Button.setOnClickListener(this);
        item5Button.setOnClickListener(this);
        item1Button.setOnLongClickListener(this);
        item2Button.setOnLongClickListener(this);
        item3Button.setOnLongClickListener(this);
        item4Button.setOnLongClickListener(this);
        item5Button.setOnLongClickListener(this);
        itemDialog = new ItemDialog(this);
        resultText = (TextView) findViewById(R.id.forge_conform_result);
        conformButton = (Button) findViewById(R.id.forge_conform_button);
        conformButton.setOnClickListener(this);
        forgeButton = (Button) findViewById(R.id.forge_do_button);
        forgeButton.setOnClickListener(this);
        cleanButton = (Button) findViewById(R.id.forge_clean_button);
        cleanButton.setOnClickListener(this);
        fuseButton = (Button) findViewById(R.id.forge_dismantle_button);
        fuseButton.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MainGameActivity.context.setPause(false);
    }

    int index = 0;
    String[] systemNames = {"打造帽子", "打造戒指", "打造项链", "融合材料"};

    private String getNextSystemName() {
        if (index >= systemNames.length - 1) {
            index = 0;
        } else {
            index++;
        }
        return systemNames[index];
    }

    private String getPrevSystemName() {
        if (index <= 0) {
            index = systemNames.length - 1;
        } else {
            index--;
        }
        return systemNames[index];
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    private void setSystem(boolean next) {
        if (next) {
            systemName.setText(getNextSystemName());

        } else {
            systemName.setText(getPrevSystemName());
        }
        if (index == 3) {
            conformButton.setEnabled(false);
            forgeButton.setText("融合");
        } else {
            conformButton.setEnabled(true);
            forgeButton.setText("打造");
        }
    }

    public GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() - e2.getX() > 100) {
                setSystem(true);
                return true;
            } else if (e1.getX() - e2.getY() < -100) {
                setSystem(false);
                return true;
            }
            return false;
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forge_item_1:
            case R.id.forge_item_2:
            case R.id.forge_item_3:
            case R.id.forge_item_4:
            case R.id.forge_item_5:
                if (itemDialog == null) {
                    itemDialog = new ItemDialog(this);
                }
                itemDialog.show(view.getId());
                break;
            case R.id.forge_dismantle_button:
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                final AccessoryAdapter adapter = new AccessoryAdapter(true, alertDialog);
                alertDialog.setTitle("点击装备进行拆解");
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "一键拆解黑装/10w", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (Accessory accessory : Accessory.loadAccessories(null)) {
                            if ("#000000".equals(accessory.getColor()) && MazeContents.hero!=null && !MazeContents.hero.isOn(accessory)) {
                                accessory.dismantle();
                            }
                        }
                        if (MazeContents.hero != null) {
                            MazeContents.hero.addMaterial(-100000);
                        }
                        adapter.refresh();
                    }
                });
                ListView listView = new ListView(this);
                listView.setAdapter(adapter);
                alertDialog.setView(listView);
                alertDialog.show();
                Button button = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                if (MazeContents.hero != null) {
                    if (MazeContents.hero.getMaterial() < 100000) {
                        button.setEnabled(false);
                    } else {
                        button.setEnabled(true);
                    }
                }
                break;
            case R.id.forge_clean_button:
                clean();
                break;

            case R.id.forge_conform_button:
                items.clear();
                if (item1 != null) items.add(item1);
                if (item2 != null) items.add(item2);
                if (item3 != null) items.add(item3);
                if (item4 != null) items.add(item4);
                if (item5 != null) items.add(item5);
                if (items.size() >= 3) {
                    String result = "";
                    switch (index) {
                        case RingBuilder.type:
                            result = ringBuilder.detect(items);
                            break;
                        case NecklaceBuilder.type:
                            result = necklaceBuilder.detect(items);
                            break;
                        case HatBuilder.type:
                            result = hatBuilder.detect(items);
                            break;
                    }
                    resultText.setText(Html.fromHtml(result));
                }
                break;
            case R.id.forge_do_button:
                if (MazeContents.hero.getMaterial() >= mate) {
                    if (index == 3) {
                        if (item1 != null && item2 != null && item3 != null && item4 != null && item5 != null) {
                            Item item = FuseItems.fuse(item1, item2, item3, item4, item5);
                            if (item != null) {
                                resultText.setText(Html.fromHtml("获得了<br>" + item.toString()));
                            }
                        } else {
                            resultText.setText("需要五个材料才可以进行融合");
                        }
                    } else {
                        items.clear();
                        if (item1 != null) items.add(item1);
                        if (item2 != null) items.add(item2);
                        if (item3 != null) items.add(item3);
                        if (item4 != null) items.add(item4);
                        if (item5 != null) items.add(item5);
                        Builder builder = ringBuilder;
                        if (items.size() > 2) {
                            switch (index) {
                                case RingBuilder.type:
                                    builder = ringBuilder;
                                    break;
                                case NecklaceBuilder.type:
                                    builder = necklaceBuilder;
                                    break;
                                case HatBuilder.type:
                                    builder = hatBuilder;
                                    break;
                            }
                            Accessory accessory = builder.build(items);
                            if (accessory != null) {
                                showResult(accessory);
                            } else {
                                resultText.setText(Html.fromHtml(builder.notEnough()));
                            }
                        } else {
                            resultText.setText("选择的打造材料不足，请继续添加!");
                        }
                    }
                    MazeContents.hero.addMaterial(-mate);
                    clean();
                } else {
                    Toast.makeText(this, "--锻造点数不足" + mate + "--", Toast.LENGTH_SHORT)
                            .show();
                    resultText.setText("--锻造点数不足" + mate + "!--");
                }
        }

    }

    private void clean() {
        item1 = null;
        item2 = null;
        item3 = null;
        item4 = null;
        item5 = null;
        if (items != null && !items.isEmpty()) items.clear();
        item1Button.setText("");
        item2Button.setText("");
        item3Button.setText("");
        item4Button.setText("");
        item5Button.setText("");
        if (itemDialog != null) itemDialog.dismiss();
        itemDialog = null;
    }

    public void showResult(Accessory accessory) {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle("打造成功");
        final TextView tv = new TextView(this);
        tv.setText(Html.fromHtml(accessory.toString()));
        dialog.setView(tv);
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "确定",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }

                });
        dialog.show();
    }

    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Item item = (Item) msg.obj;
            if (item != null) {
                switch (msg.what) {
                    case R.id.forge_item_1:
                        item1Button.setText(item.getName().name());
                        item1 = item;
                        break;
                    case R.id.forge_item_2:
                        item2Button.setText(item.getName().name());
                        item2 = item;
                        break;
                    case R.id.forge_item_3:
                        item3Button.setText(item.getName().name());
                        item3 = item;
                        break;
                    case R.id.forge_item_4:
                        item4Button.setText(item.getName().name());
                        item4 = item;
                        break;
                    case R.id.forge_item_5:
                        item5Button.setText(item.getName().name());
                        item5 = item;
                        break;
                }
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.forge_item_1:
                if (item1 != null) {
                    resultText.setText(Html.fromHtml(item1.toString()));
                }
                break;
            case R.id.forge_item_2:
                if (item2 != null) {
                    resultText.setText(Html.fromHtml(item2.toString()));
                }
                break;
            case R.id.forge_item_3:
                if (item3 != null) {
                    resultText.setText(Html.fromHtml(item3.toString()));
                }
                break;

            case R.id.forge_item_4:
                if (item4 != null) {
                    resultText.setText(Html.fromHtml(item4.toString()));
                }
                break;
            case R.id.forge_item_5:
                if (item5 != null) {
                    resultText.setText(Html.fromHtml(item5.toString()));
                }
                break;
        }
        return false;
    }

    public boolean contains(Item item) {
        return item.idEqual(item1) || item.idEqual(item2) || item.idEqual(item3) || item.idEqual(item4) || item.idEqual(item5);
    }

}