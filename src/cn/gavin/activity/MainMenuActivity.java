package cn.gavin.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.bmob.pay.tool.BmobPay;
import com.dgsdk.cp.QMCPConnect;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.gavin.R;
import cn.gavin.db.DBHelper;
import cn.gavin.log.LogHelper;
import cn.gavin.save.LoadHelper;
import cn.gavin.utils.MazeContents;

public class MainMenuActivity extends Activity implements OnClickListener {
    public MainMenuActivity context;

    private Button menuStart;


    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 3:
                    menuStart.setText("加载存档");
                    break;
                case 2:
                    menuStart.setText("加载技能");
                    break;
                case 1:
                    menuStart.setText("初始化迷宫");
                    break;
                case 0:
                    menuStart.setEnabled(true);
                    menuStart.setText("开 始 游 戏");
            }
            super.handleMessage(msg);
        }
    };
    private Shimmer shimmer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //这是为了应用程序安装完后直接打开，按home键退出后，再次打开程序出现的BUG
        /*if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            //结束你的activity
            this.finish();
            return;
        }*/
        context = this;
        setContentView(R.layout.activity_main_menu);
        ShimmerTextView titleText = (ShimmerTextView) findViewById(R.id.menu_title_tv);
        shimmer = new Shimmer();
        shimmer.start(titleText);
        Bmob.initialize(this, "4de7673ec85955af7568cfa1494c6498");
        BmobPay.init(MainMenuActivity.this, "4de7673ec85955af7568cfa1494c6498");
        QMCPConnect.ConnectQuMi(this, "4571c5e7528ba4d7", "458742eb0b203fac");
        QMCPConnect.getQumiConnectInstance(this).initPopAd(this);
        menuStart = (Button) findViewById(R.id.menu_start);
        menuStart.setOnClickListener(this);
        menuStart.setEnabled(false);
        menuStart.setText("加载数据");
        try {
            QMCPConnect.getQumiConnectInstance(this).showPopUpAd(this);
        }catch (Exception e){
            LogHelper.logException(e, false);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DBHelper.init(MainMenuActivity.this);
                    handler.sendEmptyMessage(3);
                    LoadHelper saveHelper = new LoadHelper(context);
                    saveHelper.loadHero();
                    handler.sendEmptyMessage(2);
                    saveHelper.loadSkill(MazeContents.hero);
                    handler.sendEmptyMessage(1);
                    handler.sendEmptyMessage(0);
                } catch (Exception exp) {
                    Log.e(MainGameActivity.TAG, "Init", exp);
                    LogHelper.logException(exp, false);
                    throw new RuntimeException(exp);
                }
            }
        }).start();
        // Test.dropTreasuresTest();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.menu_start:
                if(MazeContents.hero!=null && MazeContents.hero.getGift()==null){
                    BmobInstallation.getCurrentInstallation(this).save();
                }
                intent = new Intent(MainMenuActivity.this, MainGameActivity.class);
                startActivity(intent);
                shimmer.cancel();
                MainMenuActivity.this.finish();
                break;

            default:
                break;
        }
    }

}
