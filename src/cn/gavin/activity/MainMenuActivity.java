
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

import cn.gavin.Hero;
import cn.gavin.Maze;
import cn.gavin.R;
import cn.gavin.db.DBHelper;
import cn.gavin.log.LogHelper;
import cn.gavin.monster.MonsterBook;
import cn.gavin.save.LoadHelper;
import cn.gavin.skill.SkillDialog;

public class MainMenuActivity extends Activity implements OnClickListener {
    public Hero hero;
    public Maze maze;
    public long payTime;
    public long lastUpload;
    public static MainMenuActivity context;
    public SkillDialog skillDialog;

    private Button menuStart;


    private final Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        BmobPay.init(MainMenuActivity.this, "4de7673ec85955af7568cfa1494c6498");
        menuStart = (Button) findViewById(R.id.menu_start);
        menuStart.setOnClickListener(this);
        menuStart.setEnabled(false);
        menuStart.setText("加载存档");
        context = this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DBHelper.init(MainMenuActivity.this);
                    skillDialog = new SkillDialog();
                    LoadHelper saveHelper = new LoadHelper(context);
                    saveHelper.loadHero();
                    handler.sendEmptyMessage(2);
                    saveHelper.loadSkill(hero, skillDialog);
                    handler.sendEmptyMessage(1);
                    MonsterBook.init(context);
                    handler.sendEmptyMessage(0);
                }catch(Exception exp){
                    Log.e(MainGameActivity.TAG, "Init", exp);
                    LogHelper.writeLog();
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
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.menu_start:
                intent = new Intent(MainMenuActivity.this, MainGameActivity.class);
                startActivity(intent);
                MainMenuActivity.this.finish();
                break;

            default:
                break;
        }
    }

    public Maze getMaze() {
        return maze;
    }
}
