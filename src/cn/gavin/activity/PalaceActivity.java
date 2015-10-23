package cn.gavin.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.List;

import cn.gavin.Hero;
import cn.gavin.R;
import cn.gavin.log.LogHelper;
import cn.gavin.maze.Maze;
import cn.gavin.palace.Palace;
import cn.gavin.monster.MonsterBook;
import cn.gavin.palace.PalaceHero;
import cn.gavin.palace.nskill.NSkill;
import cn.gavin.skill.SkillDialog;

public class PalaceActivity extends Activity implements OnClickListener, BaseContext {
    //Constants
    public static final String TAG = "MazeNeverEnd";
    public static final String APK_PATH = Environment.getExternalStorageDirectory() + "/maze";

    // 战斗刷新速度
    private long refreshInfoSpeed =600;

    // 战斗信息
    private ScrollView mainInfoSv;
    private LinearLayout mainInfoPlatform;
    private int fightInfoTotalCount = 50;

    // 英雄
    private Hero heroN;
    private Palace maze;

    // 人物信息栏
    private TextView mainContriHp;
    private TextView mainContriAtt;
    private TextView mainContriDef;
    private TextView swordLev;
    private TextView armorLev;
    private TextView clickCount;
    //按钮
    private Button heroPic;
    private Button pauseButton;
    private Button firstSkillButton;
    private Button secondSkillButton;
    private Button thirdSkillButton;

    private boolean pause;
    private boolean gameThreadRunning;
    public static PalaceActivity context;
    private GameThread gameThread;
    //Data
    private TextView ringTextView;
    private TextView necklaceTextView;
    private TextView hatTextView;
    private MonsterBook monsterBook;
    private SkillDialog skillDialog;
    private boolean isHidBattle;
    private Button exitButton;
    private Button restoreButton;

    @Override
    public boolean isHideBattle() {
        return isHidBattle;
    }
    //Get Function
    public long getRefreshInfoSpeed() {
        return refreshInfoSpeed;
    }

    public boolean isGameThreadRunning() {
        return gameThreadRunning;
    }

    public boolean isPause() {
        return pause;
    }

    public Handler getHandler() {
        return handler;
    }

    //Set Function
    public void addMessage(String... msg) {
        Message message = Message.obtain();
        message.what = 10;
        Bundle bundle = message.getData();
        bundle.putStringArray("msg", msg);
        handler.sendMessage(message);
    }

    public void addMessages(List<String> msgs) {
        Message message = Message.obtain();
        message.what = 10;
        Bundle bundle = message.getData();
        bundle.putStringArray("msg", msgs.toArray(new String[msgs.size()]));
        handler.sendMessage(message);
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case 13:
                        PalaceHero mazeHero = (PalaceHero) maze.getHero();
                        int material = 100000 + mazeHero.getRestoreCount() * 500000;
                        if(heroN.getMaterial() > material) {
                            heroN.addMaterial(material);
                            mazeHero.setRestoreCount(mazeHero.getRestoreCount() + 1);
                            mazeHero.addHp(mazeHero.getUHp() / 2 + 1);
                            long value = (100000 + mazeHero.getRestoreCount() * 500000) / 10000;
                            restoreButton.setText(Html.fromHtml("<font color=\"red\">" + value + "</font>W锻造点<br>恢复一半HP"));
                        }
                        break;
                    case 12:
                        Boolean b = (Boolean)msg.obj;
                        if(b){
                            findViewById(R.id.palace_bak).setBackgroundResource(R.drawable.long_1);
                            exitButton.setText("骄傲的离开殿堂");
                        }else{
                            findViewById(R.id.palace_bak).setBackgroundResource(R.drawable.long_bak);
                            exitButton.setText("灰溜溜的爬出殿堂");
                        }
                        pauseButton.setEnabled(false);
                        restoreButton.setEnabled(false);
                        break;
                    case 11:
                        Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake);
                        findViewById(R.id.hero_pic).startAnimation(shake);
                        break;
                    case 1:
                        if (pause) {
                            pauseButton.setText("点击继续挑战");
                        } else {
                            pauseButton.setText("暂停");
                        }
                    case 4:
                        clickCount.setText("点击\n" + heroN.getClick());
                        heroPic.setBackgroundResource(R.drawable.h_1);
                        break;
                    case 5:
                        clickCount.setText("点击\n" + heroN.getClick());
                        heroPic.setBackgroundResource(R.drawable.h_2);
                        break;
                    case 10:
                        Bundle bundle = msg.peekData();
                        if (bundle != null && !bundle.isEmpty()) {
                            String[] messages = bundle.getStringArray("msg");
                            for (String str : messages) {

                                TextView oneKickInfo = new TextView(PalaceActivity.this);
                                // 将一次信息数据显示到页面中
                                oneKickInfo.setText(Html.fromHtml(str));
                                mainInfoPlatform.addView(oneKickInfo);
                                scrollToBottom(mainInfoSv, mainInfoPlatform);
                            }
                        }
                        if (mainInfoPlatform.getChildCount() > fightInfoTotalCount) {
                            mainInfoPlatform.removeViewAt(0);
                        }
                        // mainInfoSv.fullScroll(ScrollView.FOCUS_DOWN);
                        break;
                }
                refresh();
                super.handleMessage(msg);
            } catch (Exception exp) {
                Log.e(TAG, "MainGameActivity.Handler", exp);
                LogHelper.logException(exp);
            }
        }

    };

    /**
     * SrcollView战斗信息栏滚到最底部
     */
    public void scrollToBottom(final View scroll, final View inner) {

        Handler mHandler = new Handler();

        mHandler.post(new Runnable() {
            public void run() {
                if (scroll == null || inner == null) {
                    return;
                }

                int offset = inner.getMeasuredHeight() - scroll.getHeight();
                if (offset < 0) {
                    offset = 0;
                }

                scroll.scrollTo(0, offset);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainGameActivity.context.setPause(true);
        setContentView(R.layout.palace_game_view);
        context = this;
        Log.i(TAG, "start palace~");
        initGameData();
        pause = true;
        start();
    }

    public void start(){
        gameThreadRunning = true;
        gameThread = new GameThread();
        gameThread.start();
    }

    @Override
    protected void onDestroy() {
        gameThreadRunning = false;
        MainGameActivity.context.setPause(false);
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                showExitDialog();
                return true;

            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 弹出退出程序提示框
     */
    private void showExitDialog() {
        AlertDialog dialog = new Builder(this).create();
        dialog.setTitle("是否中止殿堂挑战？");
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PalaceActivity.this.finish();
                    }

                });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }

                });
        dialog.show();
    }


    private void initGameData() {
        // 英雄
        heroN = MazeContents.hero;
        maze = new Palace(heroN);
        monsterBook = MainGameActivity.context.getMonsterBook();
        skillDialog = MainGameActivity.context.getSkillDialog();
        // 左侧战斗信息
        mainInfoSv = (ScrollView) findViewById(R.id.main_info_sv);
        mainInfoPlatform = (LinearLayout) findViewById(R.id.main_info_ll);
        // ---- ---- 属性
        mainContriHp = (TextView) findViewById(R.id.main_contri_hp);
        mainContriAtt = (TextView) findViewById(R.id.main_contri_att);
        mainContriDef = (TextView) findViewById(R.id.main_contri_def);
        swordLev = (TextView) findViewById(R.id.main_contri_level);
        armorLev = (TextView) findViewById(R.id.main_armor_level);
        heroPic = (Button) findViewById(R.id.hero_pic);
        heroPic.setOnClickListener(this);
        heroPic.setBackgroundResource(R.drawable.h_1);
        heroPic.setTextColor(getResources().getColor(R.color.red));
        heroPic.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        pauseButton = (Button) findViewById(R.id.palace_pause_button);
        pauseButton.setOnClickListener(this);
        clickCount = (TextView) findViewById(R.id.hero_pic_click_count);
        firstSkillButton = (Button) findViewById(R.id.first_skill);
        firstSkillButton.setOnClickListener(this);
        secondSkillButton = (Button) findViewById(R.id.secondary_skill);
        secondSkillButton.setOnClickListener(this);
        thirdSkillButton = (Button) findViewById(R.id.third_skill);
        thirdSkillButton.setOnClickListener(this);
        ringTextView = (TextView) findViewById(R.id.ring_view);
        ringTextView.setOnClickListener(this);
        necklaceTextView = (TextView) findViewById(R.id.necklace_view);
        necklaceTextView.setOnClickListener(this);
        hatTextView = (TextView) findViewById(R.id.hat_view);
        hatTextView.setOnClickListener(this);
        exitButton = (Button) findViewById(R.id.exist_palace_button);
        exitButton.setOnClickListener(this);
        pauseButton.setEnabled(true);
        restoreButton = (Button)findViewById(R.id.restore_palace_button);
        restoreButton.setOnClickListener(this);
        refresh();
    }

    private synchronized void refresh() {
        clickCount.setText("点击\n" + heroN.getClick());
        mainContriHp.setText(maze.getHero().getHp() + "/" + heroN.getUpperHp());
        mainContriAtt.setText(heroN.getUpperAtk() + "");
        mainContriDef.setText(heroN.getUpperDef() + "");
        swordLev.setText(heroN.getSword() + "+" + heroN.getSwordLev());
        armorLev.setText(heroN.getArmor() + "+" + heroN.getArmorLev());
        int i =0;
        for(NSkill skill : maze.getHero().getSkills()){
            switch (i){
                case 0:
                    firstSkillButton.setText(skill.getName());
                    break;
                case 1:
                    secondSkillButton.setText(skill.getName());
                    break;
                case 2:
                    thirdSkillButton.setText(skill.getName());
                    break;
            }
            i++;
        }

        if (heroN.getRing() != null) {
            ringTextView.setText(Html.fromHtml(heroN.getRing().getFormatName()));
        } else {
            ringTextView.setText("未装备");
        }
        if (heroN.getNecklace() != null) {
            necklaceTextView.setText(Html.fromHtml(heroN.getNecklace().getFormatName()));
        } else {
            necklaceTextView.setText("未装备");
        }
        if (heroN.getHat() != null) {
            hatTextView.setText(Html.fromHtml(heroN.getHat().getFormatName()));
        } else {
            hatTextView.setText("未装备");
        }
        long value = ((PalaceHero)maze.getHero()).getRestoreCount() * 500000 + 100000;
        if(!pause && heroN.getMaterial() > value){
            restoreButton.setEnabled(true);
        }else{
            restoreButton.setEnabled(false);
        }
    }

    public Hero getHero() {
        return heroN;
    }

    public Maze getMaze() {
        return maze;
    }

    public long getLastUploadLev() {
        return MazeContents.lastUpload;
    }

    public void setHeroN(Hero heroN) {
        this.heroN = heroN;
    }

    public void setMaze(Palace maze) {
        this.maze = maze;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
        handler.sendEmptyMessage(1);
    }

    public MonsterBook getMonsterBook() {
        return monsterBook;
    }

    public SkillDialog getSkillDialog() {
        return skillDialog;
    }

    public void setSkillDialog(SkillDialog skillDialog) {
        this.skillDialog = skillDialog;
    }

    public void setFinished(boolean b) {
        Message message = new Message();
        message.what = 12;
        message.obj = b;
        handler.sendMessage(message);
    }

    private class GameThread extends Thread {

        @Override
        public void run() {
            try {
                maze.move(context);
            } catch (Exception e) {
                Log.e(TAG, "MainGameActivity.GameThread", e);
                LogHelper.logException(e);
                throw new RuntimeException(e);
            }

        }
    }

    @Override
    public void onClick(View v) {
        Log.i(TAG, "onClick() -- " + v.getId() + " -- 被点击了");
        switch (v.getId()) {
            case R.id.restore_palace_button:
                handler.sendEmptyMessage(13);
                break;
            case R.id.exist_palace_button:
                showExitDialog();
                break;
            case R.id.first_skill:
                if (heroN.getFirstSkill() != null) {
                    heroN.getFirstSkill().addCount();
                }
                handler.sendEmptyMessage(0);
                heroN.click(false);
                break;
            case R.id.secondary_skill:
                if (heroN.getSecondSkill() != null) {
                    heroN.getSecondSkill().addCount();
                }
                handler.sendEmptyMessage(0);
                heroN.click(false);
                break;
            case R.id.third_skill:
                if (heroN.getThirdSkill() != null) {
                    heroN.getThirdSkill().addCount();
                }
                handler.sendEmptyMessage(0);
                heroN.click(false);
                break;

            case R.id.palace_pause_button:
                pause = !pause;
                handler.sendEmptyMessage(1);
                heroN.click(false);
                break;

            case R.id.hero_pic:
                heroN.click(true);
                if (heroN.getClick() % 2 == 0) {
                    handler.sendEmptyMessage(4);
                } else {
                    handler.sendEmptyMessage(5);
                }
                break;
            default:
                break;
        }
    }

}
