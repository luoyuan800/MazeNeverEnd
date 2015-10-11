package cn.gavin.maze;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;

import cn.gavin.activity.MainGameActivity;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 10/10/2015.
 */
public class MazeService extends Service {
    public static String ACTION = "cn.gavin.maze.MazeService";
    private PowerManager.WakeLock wakeLock;

    @Override
    public void onCreate(){
        PowerManager pm=(PowerManager)getSystemService(Context.POWER_SERVICE);
        wakeLock=pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MazeNeverEnd");
        wakeLock.acquire();
    }

    @Override
    public void onDestroy(){
        if(wakeLock!=null){
            wakeLock.release();
        }
        MainGameActivity.context.stopMaze();
    }

    @Override
    public IBinder onBind(Intent intent) {
            MainGameActivity.context.startMaze();
        return null;
    }
}
