
package cn.gavin.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.bmob.pay.tool.BmobPay;

import cn.gavin.R;
import cn.gavin.db.DBHelper;
import cn.gavin.monster.MonsterBook;

public class MainMenuActivity extends Activity implements OnClickListener {

    private Button menuStart;


    private final Handler handler = new Handler(){
        public void handleMessage(Message msg){
            menuStart.setEnabled(true);
            menuStart.setText("开 始 游 戏");
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
        menuStart.setText("正在初始化");
        new Thread(new Runnable() {
            @Override
            public void run() {
                DBHelper.init(MainMenuActivity.this);
                MonsterBook.init(MainMenuActivity.this);
                handler.sendEmptyMessage(0);
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

}
