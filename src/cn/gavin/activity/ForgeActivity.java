package cn.gavin.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import cn.gavin.R;
import cn.gavin.forge.Item;
import cn.gavin.forge.dialog.ItemDialog;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forge_view);
        systemName = (TextView) findViewById(R.id.forge_system_name);
        systemName.setText(systemNames[index]);
        Button pre = (Button) findViewById(R.id.prev_forge_system_button);
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                systemName.setText(getPrevSystemName());
            }
        });
        Button next = (Button) findViewById(R.id.next_forge_system_button);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                systemName.setText(getNextSystemName());
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
        itemDialog = new ItemDialog(this);
        resultText = (TextView) findViewById(R.id.forge_conform_result);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    int index = 0;
    String[] systemNames = {"帽子", "戒指", "项链"};

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

    public GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() - e2.getX() > 100) {
                systemName.setText(getNextSystemName());
                return true;
            } else if (e1.getX() - e2.getY() < -100) {
                systemName.setText(getPrevSystemName());
                return true;
            }
            return false;
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forge_item_1:
                resultText.setText(Html.fromHtml(item1.toString()));
                break;
            case R.id.forge_item_2:
                resultText.setText(Html.fromHtml(item2.toString()));
                break;
            case R.id.forge_item_3:
                resultText.setText(Html.fromHtml(item3.toString()));
                break;
            case R.id.forge_item_4:
                resultText.setText(Html.fromHtml(item4.toString()));
                break;
            case R.id.forge_item_5:
                resultText.setText(Html.fromHtml(item5.toString()));
                break;
        }
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
        itemDialog.show(view.getId());
        return false;
    }

    public boolean contains(Item item){
        return item1 == item || item2 == item || item3 == item || item4 == item || item5 == item;
    }
}
