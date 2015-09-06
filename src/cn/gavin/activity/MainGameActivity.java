package cn.gavin.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Random;

import cn.gavin.Achievement;
import cn.gavin.Armor;
import cn.gavin.Hero;
import cn.gavin.Maze;
import cn.gavin.R;
import cn.gavin.Skill;
import cn.gavin.Sword;
import cn.gavin.alipay.Alipay;
import cn.gavin.alipay.PayResult;
import cn.gavin.upload.Upload;

public class MainGameActivity extends Activity implements OnClickListener, OnItemClickListener {
    private static final String TAG = "MainGameActivity";
    private final String APK_PATH = Environment.getExternalStorageDirectory() + "/maze";

    // 战斗刷新速度
    private long refreshInfoSpeed = 500;
    private Button uploadButton;
    private Button updateButton;
    private String VERSION_CHECK_URL = "http://7xk7ce.com1.z0.glb.clouddn.com/MazeNeverEndUpdate.jpg";
    private StringBuilder versionUpdateInfo;
    private String PACKAGE_DOWNLOAD_URL = "http://7xk7ce.com1.z0.glb.clouddn.com/MazeNeverEnd.png";
    private int lastUploadLev = 0;

    public long getRefreshInfoSpeed() {
        return refreshInfoSpeed;
    }


    // 战斗信息
    private ScrollView mainInfoSv;
    private LinearLayout mainInfoPlatform;
    private int fightInfoTotalCount = 50;

    // 英雄
    private cn.gavin.Hero heroN;
    private Maze maze;

    // 人物信息栏
    private TextView itembarContri; // 按钮-属性

    private TextView mainContriHp;
    private TextView mainContriAtt;
    private TextView mainContriDef;
    private TextView swordLev;
    private TextView armorLev;
    private TextView heroPointValue;
    private TextView mainContriCurMaterial;
    private TextView clickCount;
    //按钮
    private Button addstr;
    private Button addpow;
    private Button addagi;
    private Button upSword;
    private Button upArmor;
    private Button heroPic;
    private Button pauseButton;
    private Button achievementButton;
    private Button resetButton;
    private Button buyButton;
    private Button lifeSkillButton;
    private Button hitSkillButton;
    private Button mulSkillButton;

    private boolean pause;
    private AchievementAdapter adapter;
    private boolean gameThreadRunning;
    public static MainGameActivity context;
    private GameThread gameThread;
    private Alipay alipay;
    private String currentVersion = "";

    public boolean isGameThreadRunning() {
        return gameThreadRunning;
    }

    public boolean isPause() {
        return pause;
    }

    public Handler getHandler() {
        return handler;
    }

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

    private String updateVersion = "";
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 101:
                    new Thread(new Runnable(){
                        public void run(){
                            Upload upload = new Upload();
                            if(upload.upload(heroN)){
                                lastUploadLev = heroN.getMaxMazeLev();
                            }
                        }
                    }).start();
                    break;
                case 201:
                    updateButton.setEnabled(false);
                    updateButton.setText("V" + currentVersion);
                    break;
                case 202:
                    updateButton.setEnabled(true);
                    updateButton.setText("升级V" + updateVersion);
                    break;
                case 100:
                    PayResult payResult = new PayResult((String) msg.obj);
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        heroN.addMaterial(100000);
                        heroN.addPoint(20);
                        alipay.addPayTime();
                        Achievement.richer.enable(heroN);
                    }
                    break;
                case 1:
                    if (pause) {
                        pause = false;
                        pauseButton.setText("暂停");
                    } else {
                        pause = true;
                        pauseButton.setText("继续");
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
                            if (str.matches(".*遇到了.*")) {
                                heroPic.setBackgroundResource(R.drawable.h_3);
                            } else if (str.matches(".*击败了.*")) {
                                heroPic.setBackgroundResource(R.drawable.h_2);
                            } else if (str.matches(".*被.*打败了.*")) {
                                heroPic.setBackgroundResource(R.drawable.h_1);
                            }
                            TextView oneKickInfo = new TextView(MainGameActivity.this);
                            // 将一次信息数据显示到页面中
                            oneKickInfo.setText(str);
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
        }

    };

    /**
     * SrcollView战斗信息栏滚到最底部
     *
     * @param scroll
     * @param inner
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
        setContentView(R.layout.main_gameview);
        context = this;
        Log.i(TAG, "start game~");

        initGameData();

        gameThreadRunning = true;
        gameThread = new GameThread();
        gameThread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        save();
        gameThreadRunning = false;
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

    private void reset() {
        Random random = new Random();
        heroN.setArmor(Armor.破布);
        heroN.setSword(Sword.木剑);
        heroN.setMaxMazeLev(1);
        heroN.setAgility(random.nextInt(5));
        heroN.setPoint(0);
        heroN.setStrength(random.nextInt(5));
        heroN.setPower(random.nextInt(5));
        heroN.setArmorLev(0);
        heroN.setSwordLev(0);
        heroN.setMaterial(0);
        maze.setLevel(1);
        heroN.setDefenseValue(10);
        heroN.setHp(20);
        heroN.setUpperHp(20);
        heroN.setAttackValue(10);
        Achievement.reBird.enable(heroN);
    }

    /**
     * 弹出退出程序提示框
     */
    private void showExitDialog() {
        AlertDialog dialog = new Builder(this).create();
        dialog.setTitle("是否退出游戏");
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        save();
                        MainGameActivity.this.finish();
                        System.exit(0);
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

    private TextView achievementDesc;

    private void showAchievement() {
        AlertDialog dialog = new Builder(this).create();
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        ListView listView = new ListView(this);
        adapter = new AchievementAdapter();
        listView.setAdapter(adapter);
        linearLayout.addView(listView);
        achievementDesc = new TextView(this);
        linearLayout.addView(achievementDesc);
        dialog.setView(linearLayout);
        dialog.setTitle("成就");
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "退出", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showNameDialog() {
        AlertDialog dialog = new Builder(this).create();
        dialog.setTitle("给勇者取个名字");
        final EditText tv = new EditText(this);
        tv.setText(heroN.getName());
        dialog.setView(tv);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        heroN.setName(tv.getText().toString().replaceAll("_", " "));
                        dialog.dismiss();
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
        if (!load()) {
            heroN = new cn.gavin.Hero("ly");
            maze = new Maze(heroN);
            alipay = new Alipay(this, 0);
        }
        // 左侧战斗信息
        mainInfoSv = (ScrollView) findViewById(R.id.main_info_sv);
        mainInfoPlatform = (LinearLayout) findViewById(R.id.main_info_ll);
        // ---- ---- 标题（人物名称 | 最深迷宫数)
        itembarContri = (TextView) findViewById(R.id.character_itembar_contribute);
        itembarContri.setOnClickListener(this);
        // ---- ---- 属性
        mainContriHp = (TextView) findViewById(R.id.main_contri_hp);
        mainContriAtt = (TextView) findViewById(R.id.main_contri_att);
        mainContriDef = (TextView) findViewById(R.id.main_contri_def);
        swordLev = (TextView) findViewById(R.id.main_contri_level);
        armorLev = (TextView) findViewById(R.id.main_armor_level);
        heroPointValue = (TextView) findViewById(R.id.main_contri_needexp);
        mainContriCurMaterial = (TextView) findViewById(R.id.main_contri_currentexp);
        addstr = (Button) findViewById(R.id.main_contri_add_str);
        addstr.setOnClickListener(this);
        addagi = (Button) findViewById(R.id.main_contri_add_agi);
        addagi.setOnClickListener(this);
        addpow = (Button) findViewById(R.id.main_contri_add_pow);
        addpow.setOnClickListener(this);
        upSword = (Button) findViewById(R.id.up_sword);
        upSword.setOnClickListener(this);
        upArmor = (Button) findViewById(R.id.up_armor);
        upArmor.setOnClickListener(this);
        heroPic = (Button) findViewById(R.id.hero_pic);
        heroPic.setOnClickListener(this);
        heroPic.setBackgroundResource(R.drawable.h_1);
        heroPic.setTextColor(getResources().getColor(R.color.red));
        heroPic.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        pauseButton = (Button) findViewById(R.id.pause_button);
        pauseButton.setOnClickListener(this);
        achievementButton = (Button) findViewById(R.id.achieve_button);
        achievementButton.setOnClickListener(this);
        clickCount = (TextView) findViewById(R.id.hero_pic_click_count);
        resetButton = (Button) findViewById(R.id.reset_button);
        resetButton.setOnClickListener(this);
        buyButton = (Button) findViewById(R.id.buy_button);
        buyButton.setOnClickListener(this);
        lifeSkillButton = (Button) findViewById(R.id.life_skill);
        lifeSkillButton.setOnClickListener(this);
        hitSkillButton = (Button) findViewById(R.id.hit_skill);
        hitSkillButton.setOnClickListener(this);
        mulSkillButton = (Button) findViewById(R.id.mul_skill);
        mulSkillButton.setOnClickListener(this);
        uploadButton = (Button) findViewById(R.id.upload_button);
        updateButton = (Button) findViewById(R.id.update_button);
        uploadButton.setOnClickListener(this);
        updateButton.setOnClickListener(this);
        updateButton.setEnabled(false);
        refresh();
    }

    private synchronized void refresh() {
        clickCount.setText("点击\n" + heroN.getClick());
        mainContriHp.setText(heroN.getHp() + "");
        mainContriAtt.setText(heroN.getUpperAtk() + "");
        mainContriDef.setText(heroN.getUpperDef() + "");
        swordLev.setText(heroN.getSword() + "\n+" + heroN.getSwordLev());
        armorLev.setText(heroN.getArmor() + "\n+" + heroN.getArmorLev());
        mainContriCurMaterial.setText(heroN.getMaterial() + "");
        heroPointValue.setText(heroN.getPoint() + "");
        if (heroN.getMaterial() >= 100 + heroN.getSwordLev()) {
            upSword.setEnabled(true);
        } else {
            upSword.setEnabled(false);
        }
        if (heroN.getMaterial() >= 80 + heroN.getArmorLev()) {
            upArmor.setEnabled(true);
        } else {
            upArmor.setEnabled(false);
        }
        if (heroN.getPoint() > 0) {
            addpow.setEnabled(true);
            addstr.setEnabled(true);
            addagi.setEnabled(true);
        } else {
            addpow.setEnabled(false);
            addstr.setEnabled(false);
            addagi.setEnabled(false);
        }
        itembarContri.setText(heroN.getName() + "\n迷宫到达(当前/记录）层\n" + maze.getLev() + "/" + heroN.getMaxMazeLev());
        heroPic.setText(heroN.getSword() + "\n\n\n " + heroN.getArmor());
        lifeSkillButton.setText(heroN.getSkill(Skill.治疗).getCount() + "");
        hitSkillButton.setText(heroN.getSkill(Skill.重击).getCount() + "");
        mulSkillButton.setText(heroN.getSkill(Skill.多重攻击).getCount() + "");
        if(lastUploadLev + 100 <= heroN.getMaxMazeLev()) {
            uploadButton.setEnabled(true);
            uploadButton.setText("上传角色信息");
        }else{
            uploadButton.setEnabled(false);
            uploadButton.setText("" + (100 - heroN.getMaxMazeLev()+lastUploadLev));
        }
    }

    private long saveTime = 0;

    private class GameThread extends Thread {

        @Override
        public void run() {
            checkUpdate();
            new MoveThread().start();
            while (gameThreadRunning) {
                try {
                    Thread.sleep(refreshInfoSpeed);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!pause) {
                    saveTime += refreshInfoSpeed;
                    if (saveTime >= refreshInfoSpeed * 200)
                        save();
                }
            }
        }
    }

    private class MoveThread extends Thread {
        public void run() {
            maze.move(context);
        }
    }

    private void checkUpdate() {
        new Thread(new Runnable(){
            public void run() {
                try {
                    PackageInfo pInfo = getPackageManager().getPackageInfo(
                            getPackageName(), 0);
                    currentVersion = pInfo.versionName.trim();
                    // 构造URL
                    URL url = new URL(VERSION_CHECK_URL);
                    // 打开连接
                    URLConnection con = url.openConnection();
                    //获得文件的长度
                    int contentLength = con.getContentLength();
                    // 输入流
                    InputStream is = con.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));

                    int curVersionCode = pInfo.versionCode;
                    updateVersion = br.readLine().replace((char)65279, ' ').trim();

                    if (updateVersion.equalsIgnoreCase(currentVersion)) {
                        handler.sendEmptyMessage(201);
                    }else {
                        String info = br.readLine();
                        versionUpdateInfo = new StringBuilder("updateVersion:" + updateVersion);
                        while (info != null && info.isEmpty()) {
                            versionUpdateInfo.append("\n").append(info);
                            info = br.readLine();
                        }
                        handler.sendEmptyMessage(202);
                    }
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(201);
                }
            }
        }).start();

    }

    public boolean downloadPAK(Handler handler) {
        try {
            // 构造URL
            URL url = new URL(PACKAGE_DOWNLOAD_URL);
            // 打开连接
            URLConnection con = url.openConnection();
            //获得文件的长度
            int contentLength = con.getContentLength();
            Message sizeMsg = new Message();
            sizeMsg.what = 2;
            sizeMsg.arg1 = contentLength;
            handler.sendMessage(sizeMsg);
            // 输入流
            InputStream is = con.getInputStream();
            BufferedInputStream br = new BufferedInputStream(is);
            byte[] data = new byte[1024];
            int len;
            // 输出的文件流
            File file = new File(APK_PATH + "/MazeNeverEnd.apk");
            if(file.exists()){
                file.delete();
            }
            File path = new File(APK_PATH);
            if(!path.exists()){
                path.mkdirs();
            }
            file.createNewFile();
            OutputStream os = new FileOutputStream(file);
            // 开始读取
            int alreadySize = 0;
            while ((len = is.read(data)) != -1) {
                os.write(data, 0, len);
                alreadySize += len;
                Message message = new Message();
                message.what = 3;
                message.arg1 = alreadySize;
                handler.sendMessage(message);
            }
            os.flush();
            os.close();
            br.close();
            handler.sendEmptyMessage(1);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        handler.sendEmptyMessage(-1);
        return false;
    }

    private TextView progressText ;

    private void showDownload() {
        final AlertDialog dialog = new Builder(this).create();
        progressText = new TextView(this);
        dialog.setView(progressText);
        final Handler handlerD = new Handler() {
            private int maxSize = 100;
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case -1:
                        progressText.setText("下载失败，请检查网络！");
                        break;
                    case 1:
                        dialog.dismiss();
                        save();
                        installAPK();
                        break;
                    case 2:
                        maxSize = msg.arg1;
                        if(maxSize == 0){
                            maxSize = 100;
                        }
                        break;
                    case 3:
                        progressText.setText((msg.arg1/maxSize) + "%");
                        break;
                }
            }
        };
        dialog.setTitle("下载更新");
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消下载", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                handlerD.sendEmptyMessage(-1);
                dialog.dismiss();
            }
        });
        dialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                downloadPAK(handlerD);
            }
        }).start();
    }
    private void showUpload() {
        final AlertDialog dialog = new Builder(this).create();
        TextView info  = new TextView(this);
        info.setText("上传勇者角色信息到服务器，下个版本您的角色将会作为迷宫的守护者拦截各个挑战的勇者。\n" +
                "上传成功后，下一次上传必须是再前进100层迷宫之后。");
        dialog.setView(info);
        dialog.setTitle("上传角色信息");
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确认", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handler.sendEmptyMessage(101);
                dialog.dismiss();
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private void installAPK() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(APK_PATH+"/MazeNeverEnd.apk")),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        Log.i(TAG, "onClick() -- " + v.getId() + " -- 被点击了");
        switch (v.getId()) {
            case R.id.update_button:
                showDownload();
                break;
            case R.id.upload_button:
                showUpload();
                break;
            case R.id.life_skill:
                Skill health = heroN.getSkill(Skill.治疗);
                if (health != null) {
                    health.addCount();
                }
                handler.sendEmptyMessage(0);
                heroN.click(false);
                break;
            case R.id.hit_skill:
                Skill hit = heroN.getSkill(Skill.重击);
                if (hit != null) {
                    hit.addCount();
                }
                handler.sendEmptyMessage(0);
                heroN.click(false);
                break;
            case R.id.mul_skill:
                Skill mul = heroN.getSkill(Skill.多重攻击);
                if (mul != null) {
                    mul.addCount();
                }
                handler.sendEmptyMessage(0);
                heroN.click(false);
                break;
            case R.id.buy_button:
                alipay.pay();
                heroN.click(false);
                break;
            case R.id.character_itembar_contribute:
                showNameDialog();
                heroN.click(false);
                break;
            case R.id.reset_button:
                reset();
                handler.sendEmptyMessage(0);
                heroN.click(false);
                break;
            case R.id.pause_button:
                handler.sendEmptyMessage(1);
                heroN.click(false);
                break;
            case R.id.achieve_button:
                showAchievement();
                heroN.click(false);
                break;
            case R.id.up_armor:
                heroN.upgradeArmor();
                handler.sendEmptyMessage(0);
                heroN.click(false);
                break;
            case R.id.up_sword:
                heroN.upgradeSword();
                handler.sendEmptyMessage(0);
                heroN.click(false);
                break;
            case R.id.main_contri_add_agi:
                heroN.addAgility();
                handler.sendEmptyMessage(0);
                heroN.click(false);
                break;
            case R.id.main_contri_add_pow:
                heroN.addLife();
                handler.sendEmptyMessage(0);
                heroN.click(false);
                break;
            case R.id.main_contri_add_str:
                heroN.addStrength();
                handler.sendEmptyMessage(0);
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

    public static class AchievementList {
        Achievement a0;
        Achievement a1;
        Achievement a2;
        Achievement a3;

        public AchievementList(Achievement a0, Achievement a1, Achievement a2, Achievement a3) {
            this.a0 = a0;
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
        }
    }

    private final List<AchievementList> adapterData = Achievement.getAchievementListAdp();

    class AchievementAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return adapterData.size();
        }

        @Override
        public AchievementList getItem(int position) {
            if (position >= getCount()) position = 0;
            return adapterData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            AchievementViewHolder holder;
            if (convertView == null) {
                holder = new AchievementViewHolder();
                convertView = View.inflate(MainGameActivity.this,
                        R.layout.achievement_list_item, null);
                holder.name = (Button) convertView.findViewById(R.id.achieve_name);
                holder.name.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        achievementDesc.setText(getItem(position).a0.getDesc());
                    }
                });
                holder.name1 = (Button) convertView.findViewById(R.id.achieve_name_1);
                holder.name1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        achievementDesc.setText(getItem(position).a1.getDesc());
                    }
                });
                holder.name2 = (Button) convertView.findViewById(R.id.achieve_name_2);
                holder.name2.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        achievementDesc.setText(getItem(position).a2.getDesc());
                    }
                });
                holder.name3 = (Button) convertView.findViewById(R.id.achieve_name_3);
                holder.name3.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        achievementDesc.setText(getItem(position).a3.getDesc());
                    }
                });
                convertView.setTag(holder);
            } else {
                holder = (AchievementViewHolder) convertView.getTag();
            }
            AchievementList item = getItem(position);
            holder.name.setText(item.a0.getName());
            if (!item.a0.isEnable()) {
                holder.name.setEnabled(false);
            } else {
                holder.name.setEnabled(true);
            }
            holder.name1.setText(item.a1.getName());
            if (!item.a1.isEnable()) {
                holder.name1.setEnabled(false);
            } else {
                holder.name1.setEnabled(true);
            }
            holder.name2.setText(item.a2.getName());
            if (!item.a2.isEnable()) {
                holder.name2.setEnabled(false);
            } else {
                holder.name2.setEnabled(true);
            }
            holder.name3.setText(item.a3.getName());
            if (!item.a3.isEnable()) {
                holder.name3.setEnabled(false);
            } else {
                holder.name3.setEnabled(true);
            }
            return convertView;
        }

    }

    static class AchievementViewHolder {
        Button name;
        Button name1;
        Button name2;
        Button name3;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private void save() {
        try {
            FileOutputStream fos = this.openFileOutput("yzcmg.ave", Activity.MODE_PRIVATE);
            StringBuffer sb = new StringBuffer();
            sb.append(heroN.getName()).append("_").append(heroN.getHp()).append("_").append(heroN.getUpperHp()).
                    append("_").append(heroN.getBaseAttackValue()).append("_").append(heroN.getBaseDefense()).append("_").
                    append(heroN.getClick()).append("_").append(heroN.getPoint()).append("_").append(heroN.getMaterial())
                    .append("_").append(heroN.getSwordLev()).append("_").append(heroN.getArmorLev()).append("_").
                    append(heroN.getSword()).append("_").append(heroN.getArmor()).append("_").append(heroN.getMaxMazeLev())
                    .append("_").append(heroN.getStrength()).append("_").append(heroN.getPower()).append("_").
                    append(heroN.getAgility()).append("_").append(heroN.getClickAward());
            sb.append("_");
            for (Achievement achievement : Achievement.values()) {
                if (achievement.isEnable()) {
                    sb.append(1);
                } else {
                    sb.append(0);
                }
            }
            sb.append("_");
            sb.append(maze.getLev());
            sb.append("_").append(alipay.getPayTime());
            sb.append("_").append(heroN.getDeathCount());
            sb.append("_").append(heroN.getExistSkill().get(0).getCount());
            sb.append("_").append(heroN.getExistSkill().get(1).getCount());
            sb.append("_").append(heroN.getExistSkill().get(2).getCount());
            fos.write(sb.toString().getBytes("UTF-8"));
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean load() {
        try {
            FileInputStream fis = openFileInput("yzcmg.ave");
            byte[] b = new byte[1];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while (fis.read(b) != -1) {
                baos.write(b, 0, b.length);
            }
            baos.close();
            fis.close();
            String save = baos.toString("UTF-8");
            String[] atts = save.split("_");
            if (atts.length >= 20) {
                heroN = new Hero(atts[0]);
                heroN.setHp(Integer.parseInt(atts[1]));
                heroN.setUpperHp(Integer.parseInt(atts[2]));
                heroN.setAttackValue(Integer.parseInt(atts[3]));
                heroN.setDefenseValue(Integer.parseInt(atts[4]));
                heroN.setClick(Integer.parseInt(atts[5]));
                heroN.setPoint(Integer.parseInt(atts[6]));
                heroN.setMaterial(Integer.parseInt(atts[7]));
                heroN.setSwordLev(Integer.parseInt(atts[8]));
                heroN.setArmorLev(Integer.parseInt(atts[9]));
                heroN.setMaxMazeLev(Integer.parseInt(atts[12]));
                heroN.setStrength(Integer.parseInt(atts[13]));
                heroN.setPower(Integer.parseInt(atts[14]));
                heroN.setAgility(Integer.parseInt(atts[15]));
                heroN.setClickAward(Integer.parseInt(atts[16]));
                heroN.setSword(Sword.valueOf(atts[10]));
                heroN.setArmor(Armor.valueOf(atts[11]));
                if (atts.length >= 24) {
                    heroN.setDeathCount(Integer.parseInt(atts[20]));
                    heroN.getExistSkill().get(0).setCount(Integer.parseInt(atts[21]));
                    heroN.getExistSkill().get(1).setCount(Integer.parseInt(atts[22]));
                    heroN.getExistSkill().get(2).setCount(Integer.parseInt(atts[23]));
                }
                maze = new Maze(heroN);
                maze.setLevel(Integer.parseInt(atts[18]));
                if (maze.getLev() > heroN.getMaxMazeLev()) {
                    maze.setLevel(heroN.getMaxMazeLev());
                }
                for (int i = 0; i < atts[17].length() && i < Achievement.values().length; i++) {
                    int enable = Integer.parseInt(atts[17].charAt(i) + "");
                    if (enable == 1) {
                        Achievement.values()[i].enable();
                    }
                }
                alipay = new Alipay(this, Integer.parseInt(atts[19]));
                Achievement.linger.enable(heroN);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
