package cn.gavin.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import cn.gavin.R;
import android.widget.LinearLayout.LayoutParams;
/**
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED
 * Created by luoyuan on 11/23/15.
 */
public class SwapActivity extends Activity implements View.OnTouchListener, View.OnClickListener {
    private LinearLayout menu;
    private LinearLayout content;
    private LayoutParams menuParams;
    private LayoutParams contentParams;

    // menu完全显示时，留给content的宽度值。
    private static final int menuPadding = 80;

    // 分辨率
    private int disPlayWidth;

    private float xDown;
    private float xMove;

    private boolean mIsShow = false;
    private static final int speed = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.swap_pet_hua_dong);

        disPlayWidth = getWindowManager().getDefaultDisplay().getWidth();

        menu = (LinearLayout) findViewById(R.id.menu);
        menu.setOnClickListener(this);
        content = (LinearLayout) findViewById(R.id.content);
        content.setOnClickListener(this);
        menuParams = (LayoutParams) menu.getLayoutParams();
        contentParams = (LayoutParams) content.getLayoutParams();
        //findViewById(R.id.layout).setOnTouchListener(this);

        menuParams.width = disPlayWidth - menuPadding;
        contentParams.width = disPlayWidth;
        showMenu(mIsShow);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.menu:
                new showMenuAsyncTask().execute(-50);
                break;
            case R.id.content:
                new showMenuAsyncTask().execute(50);
                break;
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                showMenu(!mIsShow);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    private void showMenu(boolean isShow)
    {
        if (isShow)
        {
            mIsShow = true;
            menuParams.leftMargin = 0;
        } else
        {
            mIsShow = false;
            menuParams.leftMargin = 0 - menuParams.width;
        }
        menu.setLayoutParams(menuParams);
    }

    /**
     *
     *这是主要代码：模拟动画过程，也让我更熟悉了AsyncTask这玩意儿
     *
     */
    class showMenuAsyncTask extends AsyncTask<Integer, Integer, Integer>
    {

        @Override
        protected Integer doInBackground(Integer... params)
        {
            int leftMargin = menuParams.leftMargin;

            //这里也是值得学习的地方，如果在平常，自己肯定又是这样写:
            //   if(){
            //       while()
            //  }
            //   else if(){
            //       while()
            //  }
            while (true)
            {
                leftMargin += params[0];
                if (params[0] > 0 && leftMargin >= 0)
                {
                    break;
                } else if (params[0] < 0 && leftMargin <= -menuParams.width)
                {
                    break;
                }
                publishProgress(leftMargin);
                try
                {
                    Thread.sleep(30);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            return leftMargin;
        }

        @Override
        protected void onProgressUpdate(Integer... values)
        {
            super.onProgressUpdate(values);
            menuParams.leftMargin = values[0];
            menu.setLayoutParams(menuParams);
        }

        @Override
        protected void onPostExecute(Integer result)
        {
            super.onPostExecute(result);
            menuParams.leftMargin = result;
            menu.setLayoutParams(menuParams);
        }

    }

}
