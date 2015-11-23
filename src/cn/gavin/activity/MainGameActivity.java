package cn.gavin.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.*;
import android.text.Html;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import cn.bmob.v3.listener.BmobDialogButtonListener;
import cn.bmob.v3.update.BmobUpdateAgent;
import cn.bmob.v3.update.UpdateStatus;
import cn.gavin.*;
import cn.gavin.alipay.Alipay;
import cn.gavin.db.DBHelper;
import cn.gavin.forge.*;
import cn.gavin.forge.adapter.AccessoryAdapter;
import cn.gavin.forge.adapter.RecipeAdapter;
import cn.gavin.forge.effect.Effect;
import cn.gavin.forge.list.ItemName;
import cn.gavin.log.LogHelper;
import cn.gavin.maze.Maze;
import cn.gavin.maze.MazeService;
import cn.gavin.monster.Monster;
import cn.gavin.monster.MonsterBook;
import cn.gavin.monster.PalaceAdapt;
import cn.gavin.palace.PalaceMonster;
import cn.gavin.pet.Pet;
import cn.gavin.pet.PetDB;
import cn.gavin.pet.PetDialog;
import cn.gavin.pet.skill.PetSkillList;
import cn.gavin.pet.skill.Shaman;
import cn.gavin.save.LoadHelper;
import cn.gavin.save.SaveHelper;
import cn.gavin.skill.SkillDialog;
import cn.gavin.skill.SkillFactory;
import cn.gavin.upload.CdKey;
import cn.gavin.upload.Upload;
import cn.gavin.utils.MazeContents;
import cn.gavin.utils.StringUtils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.EnumMap;
import java.util.List;
import java.util.Random;

public class MainGameActivity extends Activity implements OnClickListener, View.OnLongClickListener, OnItemClickListener, BaseContext {
    //Constants
    public static final String TAG = "MazeNeverEnd";
    public static final String APK_PATH = Environment.getExternalStorageDirectory() + "/maze";
    private static final String VERSION_CHECK_URL = "http://7xk7ce.com1.z0.glb.clouddn.com/MazeNeverEndUpdate.jpg";
    private static final String PACKAGE_DOWNLOAD_URL = "http://7xk7ce.com1.z0.glb.clouddn.com/MazeNeverEnd.png";

    // 战斗刷新速度
    private long refreshInfoSpeed = 520;
    private Button uploadButton;
    private Button updateButton;
    private StringBuilder versionUpdateInfo;

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
    private Button resetSkillButton;
    private Button buyButton;
    private Button firstSkillButton;
    private Button secondSkillButton;
    private Button thirdSkillButton;
    private Button skillsButton;

    private boolean pause;
    private boolean gameThreadRunning;
    public static MainGameActivity context;
    private GameThread gameThread;
    private Alipay alipay;
    //Updat Control
    private String currentVersion = "";
    private String updateVersion = "";
    //Upload control
    private boolean uploading = false;
    //Data
    private AchievementAdapter achievementAdapter;
    private DBHelper dbHelper;
    private SkillDialog skillDialog;
    private SaveHelper saveHelper;
    private Button saveButton;
    private Button upTArmorButton;
    private Button upTSwordButton;
    private Button addNAgiButton;
    private Button addNPowerButton;
    private Button addNStreButton;
    private Button getSkillPointButton;
    private TextView lockBoxCount;
    private TextView keyCount;
    private Button forgeButton;
    private ViewFlipper buttonGroup;
    private GestureDetector detector; //手势检测
    private Button itemButton;
    private Button achievementButton;
    private TextView ringTextView;
    private TextView necklaceTextView;
    private TextView hatTextView;
    private Button recipeButton;
    private Button cleanButton;
    private Button buyLockBoxButton;
    private Button monsterBookButton;
    private MonsterBook monsterBook;
    private Button reincaenationButton;
    private Button upgradeHSwordButton;
    private Button upgradeHArmorButton;
    private Button palaceButton;
    private boolean isHidBattle;
    private int palaceCount;
    private boolean updatePalace;
    private TextView petView;
    private Button petDetail;
    private Button swapPetButton;


    //Get Function
    public long getRefreshInfoSpeed() {
        return refreshInfoSpeed;
    }

    @Override
    public boolean isHideBattle() {
        return isHidBattle;
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
                    case 110:
                        Toast.makeText(context, "--内购次数达到上限!--", Toast.LENGTH_LONG)
                                .show();
                        break;
                    case 109:
                        if (isHidBattle) {
                            isHidBattle = false;
                            mainInfoPlatform.removeAllViews();
                        } else {
                            isHidBattle = true;
                            mainInfoPlatform.removeAllViews();
                            TextView textView = new TextView(context);
                            textView.setText("\n\n长按显示战斗信息\n\n欢迎进群：332406332 \n举报bug和提供您的建议！");
                            mainInfoPlatform.addView(textView);
                        }
                        break;
                    case 108:
                        Toast.makeText(context, "--数据异常，上传失败!--", Toast.LENGTH_LONG)
                                .show();
                        Achievement.cribber.enable(heroN);
                        break;
                    case 107:
                        showPalaceDetail();
                        break;
                    case 106:
                        updatePalace = false;
                        palaceCount = 0;
                        break;
                    case 203:
                        palaceCount = msg.arg1;
                        break;
                    case 105:
                        Toast.makeText(context, "--上传失败!" + msg.obj + "--", Toast.LENGTH_LONG)
                                .show();
                        uploading = false;
                        break;
                    case 104:
                        MazeContents.lastUpload = heroN.getMaxMazeLev();
                        Achievement.uploader.enable(heroN);
                        uploading = false;
                        break;
                    case 103:
                        saveButton.setEnabled(false);
                        saveButton.setText("正在存档");
                        save();
                        Toast.makeText(context, "--保存成功!--", Toast.LENGTH_SHORT)
                                .show();
                        saveButton.setEnabled(true);
                        saveButton.setText("存档");
                        addMessage("存档成功");
                        break;
                    case 102:
                        break;
                    case 101:
                        uploadButton.setEnabled(false);
                        uploadButton.setText("上传中");
                        uploading = true;
                        Upload upload = new Upload(context);
                        heroN.setPay(MazeContents.payTime);
                        upload.upload(heroN);
                        break;
                    case 201:
                        updateButton.setEnabled(false);
                        updateButton.setText("V" + currentVersion);
                        break;
                    case 202:
                        updateButton.setEnabled(true);
                        updateButton.setText("升级V" + updateVersion);
                        showUpdate();
                        break;
                    case 100:
                        heroN.addMaterial(100000);
                        heroN.addPoint(20);
                        alipay.addPayTime();
                        if (alipay.getPayTime() == 10) {
                            Achievement.crapGame.enable();
                        }
                        Achievement.richer.enable(heroN);
                        showAwardPet();
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
                        heroPic.setBackgroundResource(R.drawable.h_4);
                        break;
                    case 10:
                        if (!isHidBattle) {
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
                                    oneKickInfo.setText(Html.fromHtml(str));
                                    mainInfoPlatform.addView(oneKickInfo);
                                    scrollToBottom(mainInfoSv, mainInfoPlatform);
                                }
                            }
                            if (mainInfoPlatform.getChildCount() > fightInfoTotalCount) {
                                mainInfoPlatform.removeViewAt(0);
                            }
                        }
                        // mainInfoSv.fullScroll(ScrollView.FOCUS_DOWN);
                        break;
                }
                refresh();
                super.handleMessage(msg);
            } catch (Exception exp) {
                Log.e(TAG, "MainGameActivity.Handler", exp);
            }
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

    private final Intent service = new Intent(MazeService.ACTION);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detector = new GestureDetector(this, gestureListener);
        setContentView(R.layout.main_gameview);
        context = this;
        setAlipay(new Alipay(context, MazeContents.payTime));
        Log.i(TAG, "start game~");
        initGameView();
        initGameData();
        //设置对对话框按钮的点击事件的监听
        BmobUpdateAgent.setUpdateOnlyWifi(false);
        BmobUpdateAgent.setDialogListener(new BmobDialogButtonListener() {

            @Override
            public void onClick(int status) {
                try {
                    switch (status) {
                        case UpdateStatus.Update:
                            save();
                            Achievement.updater.enable(heroN);
                            break;
                        case UpdateStatus.NotNow:
                            break;
                        case UpdateStatus.Close://只有在强制更新状态下才会在更新对话框的右上方出现close按钮,如果用户不点击”立即更新“按钮，这时候开发者可做些操作，比如直接退出应用等
                            exist();
                            break;
                    }
                } catch (Exception e) {
                    LogHelper.logException(e);
                }
            }
        });
        try {
            //BmobUpdateAgent.update(this);
            PalaceMonster.getPalaceCount(context);
        } catch (Exception e) {
            LogHelper.logException(e);
        }
//        checkUpdate();
        gameThreadRunning = true;
        service.setPackage(getPackageName());
        startService(service);
        bindService(service, connection, BIND_AUTO_CREATE);
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        save();
        super.onSaveInstanceState(bundle);
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            load();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            save();
        }
    };

    private void initGameView() {
        buttonGroup = (ViewFlipper) findViewById(R.id.button_group_flipper);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.main_control_buttons, (ViewGroup) this.findViewById(R.id.main_control_buttons));
        buttonGroup.addView(view, 0);
        view = inflater.inflate(R.layout.upgrade_buttons, (ViewGroup) this.findViewById(R.id.upgrade_buttons));
        buttonGroup.addView(view, 1);
        view = inflater.inflate(R.layout.item_buttons, (ViewGroup) this.findViewById(R.id.item_buttons));
        buttonGroup.addView(view, 2);
        Button pre = (Button) findViewById(R.id.prev_button_system_button);
        pre.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonGroup.showNext();
            }
        });
        Button next = (Button) findViewById(R.id.next_button_system_button);
        next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonGroup.showPrevious();
            }
        });
        swapPetButton = (Button)findViewById(R.id.pet_swap_button);
        swapPetButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                Intent intent = new Intent(context, SwapActivity.class);
                startActivity(intent);
                //overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
                }catch (Exception e){
                    LogHelper.logException(e);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        gameThreadRunning = false;
        dbHelper.close();
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

    //Popup dialog
    private void showPayDialog() {
        AlertDialog dialog = new Builder(this).create();
        dialog.setTitle("是否确认？");
        TextView tv = new TextView(context);
        tv.setText("注意：\n1.  感谢您的支持，无论您是想踩还是赞~\n" +
                "2.  这个并不算是真正内购功能（虽然确实很像内购）\n" +
                "3.  请适量使用。过多的锻造点数并不能加快您的游戏进度。\n" +
                "4.  您会获得额外的10W点锻造点数和随机的能力点数。\n" +
                "5.  您会随机获得一个高属性宠物。\n");
        dialog.setView(tv);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alipay.pay();
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

    private void showReincarnationDialog() {
        AlertDialog dialog = new Builder(this).create();
        dialog.setTitle("是否确认转生？");
        TextView tv = new TextView(context);
        tv.setText("注意：\n1.  你会失去所有技能等级、技能点数。\n" +
                "2.  所有材料会被清空。\n" +
                "3.  装备着的饰品会继承下来，其他装备会被丢弃。\n" +
                "4.  未分配的能力点数会被清空.\n" +
                "5.  转生后的基础属性会根据转生前的属性得到加强（基础属性影响人物的成长）。\n" +
                "6.  转生消耗的锻造点数会随着转生次数递增。");
        dialog.setView(tv);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        heroN.reincarnation();
                        skillDialog.init();
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
                        exist();
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

    private void exist() {
        save();
        if (skillPointGetDialog != null) {
            skillPointGetDialog.dismiss();
        }
        this.finish();
        this.stopService(service);
        System.exit(0);
    }

    private TextView achievementDesc;
    private AlertDialog achDialog;

    private void showAchievement() {
        if (achDialog == null) {
            achDialog = new Builder(this).create();
            achDialog.setTitle("成就");
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            achievementDesc = new TextView(this);
            linearLayout.addView(achievementDesc);
            ListView listView = new ListView(this);
            achievementAdapter = new AchievementAdapter();
            listView.setAdapter(achievementAdapter);
            linearLayout.addView(listView);
            achDialog.setView(linearLayout);
            achDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "退出", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    achDialog.hide();
                }
            });
        }
        achDialog.show();
    }

    private AlertDialog skillPointGetDialog;

    private void showGetSkillPointDialog() {
        if (skillPointGetDialog == null) {
            skillPointGetDialog = new Builder(this).create();
            skillPointGetDialog.setTitle("88888锻造点数转换1点技能点");
            skillPointGetDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    heroN.addMaterial(-88888);
                    heroN.setSkillPoint(heroN.getSkillPoint() + 1);
                    handler.sendEmptyMessage(103);
                    skillPointGetDialog.hide();
                }
            });
            skillPointGetDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "退出", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    skillPointGetDialog.hide();
                }
            });
        }
        skillPointGetDialog.show();
        if (heroN.getMaterial() > 88888) {
            skillPointGetDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
        } else {
            skillPointGetDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
        }
    }

    private AlertDialog getLockBoxDialog;

    private void getLockBox() {
        if (getLockBoxDialog == null) {
            getLockBoxDialog = new Builder(this).create();
            getLockBoxDialog.setTitle("72250锻造点数换取一个带锁的宝箱");
            getLockBoxDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    heroN.addMaterial(-72250);
                    heroN.setLockBox(heroN.getLockBox() + 1);
                    handler.sendEmptyMessage(103);
                    getLockBoxDialog.hide();
                }
            });
            getLockBoxDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "退出", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getLockBoxDialog.hide();
                }
            });
        }
        getLockBoxDialog.show();
        if (heroN.getMaterial() >= 72250) {
            getLockBoxDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
        } else {
            getLockBoxDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
        }
    }

    private void showResetSkillPointDialog() {
        final long resetSkillValue = 799988 + 69988 * heroN.getResetSkillCount();
        AlertDialog resetSkillPointDialog;
        resetSkillPointDialog = new Builder(this).create();
        resetSkillPointDialog.setTitle("消耗" + resetSkillValue + "锻造点数重置技能");
        resetSkillPointDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        heroN.addMaterial(-resetSkillValue);
                        heroN.setOnSkill(false);
                        heroN.setOnChange(false);
                        heroN.setSkillAdditionHp(0l);
                        heroN.setSkillAdditionAtk(0l);
                        heroN.setSkillAdditionDef(0l);
                        heroN.setSkillPoint(heroN.getSkillPoint() + SkillFactory.reset());
                        heroN.setResetSkillCount(heroN.getResetSkillCount() + 1);
                        handler.sendEmptyMessage(103);
                        dialog.dismiss();
                    }

                });
        resetSkillPointDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }

                });
        resetSkillPointDialog.show();
        if (heroN.getMaterial() > resetSkillValue) {
            resetSkillPointDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
        } else {
            resetSkillPointDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
        }
    }

    /*
        Change name
     */
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
                        final String input = tv.getText().toString();
                        if(heroN.getAwardCount() < 26 &&input.equals("bbbbb~5")){
                            heroN.setAwardCount(heroN.getAwardCount() + 26);
                            heroN.setPetSize(3 + heroN.getReincaCount().intValue() + 5);
                        }else if (input.equals("gavin~0")) {
                            for (int i = 0; i < 10; i++) {
                                Pet.cPet(new Monster(heroN, maze), heroN.getRandom());
                            }
                        } else if (heroN.getAwardCount() < 16 && input.equalsIgnoreCase("!@#$%^&*()")) {
                            heroN.setAgility(0);
                            heroN.setStrength(0);
                            heroN.setPower(0);
                            heroN.setAttackValue(10);
                            heroN.setDefenseValue(10);
                            heroN.setAwardCount(heroN.getAwardCount() + 16);
                        } else if (input.startsWith("#")) {
                            try {
                                itembarContri.setBackgroundColor(Color.parseColor(input));
                                heroN.setTitleColor(input);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (input.startsWith("~")) {
                            showKeyDialog(input);
                        } else if (input.equals("qx22222") && heroN.getAwardCount() <= 6) {
                            Accessory hat = new Accessory();
                            hat.setType(HatBuilder.type);
                            hat.setName("沁玟之思念");
                            hat.setElement(Element.金);
                            hat.setColor("#FF8C00");
                            EnumMap<Effect, Number> pro = new EnumMap<Effect, Number>(Effect.class);
                            pro.put(Effect.ADD_PER_ATK, 500);
                            pro.put(Effect.ADD_PER_DEF, 1000);
                            pro.put(Effect.ADD_PER_UPPER_HP, 1000);
                            hat.setEffects(pro);
                            hat.save();
                            Accessory ring = new Accessory();
                            ring.setType(RingBuilder.type);
                            ring.setName("沁玟之护守");
                            ring.setElement(Element.水);
                            ring.setColor("#FF8C00");
                            pro = new EnumMap<Effect, Number>(Effect.class);
                            pro.put(Effect.ADD_CLICK_AWARD, 10000);
                            pro.put(Effect.ADD_PARRY, 20);
                            ring.setEffects(pro);
                            ring.save();
                            Accessory neck = new Accessory();
                            neck.setType(NecklaceBuilder.type);
                            neck.setName("沁玟之永恒");
                            neck.setElement(Element.火);
                            neck.setColor("#FF8C00");
                            pro = new EnumMap<Effect, Number>(Effect.class);
                            pro.put(Effect.ADD_POWER, 300000);
                            pro.put(Effect.ADD_AGI, 200000);
                            pro.put(Effect.ADD_STR, 300000);
                            neck.setEffects(pro);
                            neck.save();
                            heroN.setAwardCount(heroN.getAwardCount() + 6);
                        } else if (input.equals("332406332") && heroN.getAwardCount() < 3) {
                            heroN.addMaterial(60000);
                            heroN.setAwardCount(heroN.getAwardCount() + 3);
                        } else if (input.equals("201509181447")) {
                            heroN.setPetSize(heroN.getPetSize() + 10);
                            heroN.setPetRate(0.001f);
                            heroN.addMaterial(10000000);
                            heroN.addPoint(100000);
                            heroN.setLockBox(heroN.getLockBox() + 1000);
                            heroN.setKeyCount(heroN.getKeyCount() + 1000);
                            heroN.setMaxMazeLev(heroN.getMaxMazeLev() + 101);
                            maze.setLevel(99);
                            Achievement.dragon.enable(heroN);
                            SkillFactory.getSkill("虚无吞噬", heroN, skillDialog).setActive(true);
                            DBHelper.getDbHelper().excuseSQLWithoutResult("UPDATE recipe set found = 'true'");
                        } else if (input.equals("201509181447ac")) {
                            for (Achievement achievement : Achievement.values()) {
                                achievement.enable(heroN);
                            }
                        } else if (input.equals("sp1.1c")) {
                            if (heroN.getAwardCount() < 1) {
                                heroN.setSkillPoint(heroN.getSkillPoint() + 3);
                                heroN.setAwardCount(heroN.getAwardCount() + 1);
                            }
                        } else if (input.equals("forge1.2d") && heroN.getAwardCount() < 2) {
                            Item item = Item.buildItem(heroN, ItemName.蛇骨);
                            if (item != null) item.save(null);
                            item = Item.buildItem(heroN, ItemName.龙皮);
                            if (item != null) item.save(null);
                            item = Item.buildItem(heroN, ItemName.青檀木);
                            if (item != null) item.save(null);
                            heroN.setAwardCount(heroN.getAwardCount() + 2);
                        } else if (input.equals("palace1.4.6") && heroN.getAwardCount() < 5) {
                            heroN.setLockBox(heroN.getLockBox() + 10);
                            heroN.setKeyCount(heroN.getKeyCount() + 10);
                            heroN.setAwardCount(heroN.getAwardCount() + 6);
                        } else if (StringUtils.isNotEmpty(input)) {
                            String name = input.replaceAll("_", " ");
                            name = input.replaceAll("'", "`");
                            heroN.setName(name);
                        }
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

    private void showKeyDialog(String input) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("正在校验兑换码");
        progressDialog.show();
        CdKey.checkKey(input, progressDialog);
    }


    private void showAwardPet() {
        final Pet pet = new Pet();
        int i = 24 + heroN.getRandom().nextInt(4);
        if (i >= Monster.lastNames.length) {
            i = Monster.lastNames.length - 1;
        }
        pet.setType(Monster.lastNames[i]);
        pet.setAtk(heroN.getBaseAttackValue() *2 + 1);
        pet.setDef(heroN.getBaseDefense() * 2 + 1);
        pet.setHp(heroN.getRealHP()  + 1);
        if (heroN.getElement() != Element.无) {
            pet.setElement(heroN.getElement());
        } else {
            pet.setElement(Element.values()[heroN.getRandom().nextInt(Element.values().length)]);
        }
        pet.setName("奖励的普通" + pet.getType());
        pet.setSex(heroN.getRandom().nextInt(2));
        pet.setSkill(PetSkillList.Shaman.getSkill(pet));
        pet.setLev(maze.getLev());
        pet.setIntimacy(0l);
        pet.setOwner(heroN.getName());
        PetDB.save(pet);
        AlertDialog dialog = new Builder(this).create();
        dialog.setTitle("您获得了新宠物");
        final TextView tv = new TextView(context);
        StringBuilder builder = new StringBuilder("你获得了宠物<br>");
        builder.append(pet.getFormatName()).append("<br>").append("hp:").append(pet.getHp()).append(" | ").append("atk:").append(pet.getAtk())
                .append(" | def:").append(pet.getDef());
        tv.setText(Html.fromHtml(builder.toString()));
        dialog.setView(tv);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }

                });
        dialog.show();
    }

    private void showUpdate() {
        AlertDialog dialog = new Builder(this).create();
        dialog.setTitle("有更新版本-" + updateVersion);
        ScrollView scrollView = new ScrollView(this);
        final TextView tv = new TextView(context);
        scrollView.addView(tv);
        dialog.setView(scrollView);
        tv.setText(versionUpdateInfo);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定升级",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showDownload();
                        dialog.dismiss();
                    }

                });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "暂不升级",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }

                });
        dialog.show();
    }

    private void showAccessory() {
        AlertDialog dialog = new Builder(this).create();
        dialog.setTitle("装备列表");
        LinearLayout linearLayout = new LinearLayout(this);
        ListView listView = new ListView(this);
        AccessoryAdapter accessoryAdapter = new AccessoryAdapter();
        listView.setAdapter(accessoryAdapter);
        linearLayout.addView(listView);
        dialog.setView(linearLayout);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }

                });
        dialog.show();
    }

    private void showPalaceWarn() {
        final AlertDialog dialog = new Builder(this).create();
        dialog.setTitle("注意事项");
        TextView textView = new TextView(this);
        textView.setText(Html.fromHtml("0. 开启殿堂的大门需要3把钥匙+50W的锻造点数.<br>" +
                "1. 只有以下技能可以在殿堂挑战中使用：<br>" +
                "    <B>勇者之击、闪避、反弹、原能力、重击、魔王天赋、闪电、水波、多重攻击、欺诈游戏、虚无吞噬、龙爪、吐息、沙尘</B><br>" +
                "2. 挑战过程中不能放入后台，否则会因为手机系统关闭后台线程导致挑战中止。<br>" +
                "3. 因为很重要所以再说一遍，<font color=\"red\">不是所有的技能都可以在殿堂挑战中使用！</font><br>" +
                "4. 请大家和谐友爱互助，不要因为被打败而懊恼和诅咒作者被美女揩油。<br>" +
                "5. <font color=\"red\">作弊者</font>不允许进入殿堂！<br>" +
                "6. <font color=\"red\">至少上传过一次角色信息</font>才可以进入殿堂！<br>" +
                "7. 点击接受按钮表示你接受这些限制并且进入殿堂挑战.。"));
        dialog.setView(textView);
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "不接受",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }

                });
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "接受",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.sendEmptyMessage(103);
                        heroN.addMaterial(-500000);
                        heroN.setKeyCount(heroN.getKeyCount() - 3);
                        Intent intent = new Intent(context, PalaceActivity.class);
                        startActivity(intent);
                    }

                });
        dialog.show();
        if (heroN.getKeyCount() >= 3 && heroN.getMaterial() >= 500000 && !Achievement.cribber.isEnable() && Achievement.uploader.isEnable()) {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
        } else {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
        }
    }

    private void showPalaceDetail() {
        final AlertDialog dialog = new Builder(this).create();
        dialog.setTitle("殿堂");

        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "退出",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }

                });
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "挑战殿堂",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showPalaceWarn();
                    }

                });

        ListView listView = new ListView(this);
        PalaceAdapt palaceAdapt = new PalaceAdapt();
        listView.setAdapter(palaceAdapt);
        dialog.setView(listView);
        dialog.show();
//        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
    }

    private void showPalace() {
        Cursor cursor = DBHelper.getDbHelper().excuseSOL("SELECT count(*) FROM palace");

        if (palaceCount == 0 || cursor.getLong(0) == palaceCount) {
            handler.sendEmptyMessage(107);
        } else {
            AlertDialog updateAlert = new Builder(this).create();
            updateAlert.setTitle("殿堂更新了，是否下载数据？");
            updateAlert.setButton(DialogInterface.BUTTON_NEGATIVE, "不更新",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            handler.sendEmptyMessage(107);
                        }

                    });
            updateAlert.setButton(DialogInterface.BUTTON_POSITIVE, "更新殿堂",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            updatePalace = true;
                            new Thread() {
                                @Override
                                public void run() {
                                    PalaceMonster.updatePalace(context);
                                }
                            }.start();

                            final ProgressDialog progressDialog = new ProgressDialog(context);
                            progressDialog.setTitle("正在更新殿堂数据");
                            progressDialog.show();
                            new Thread() {
                                @Override
                                public void run() {
                                    int count = 0;
                                    while (updatePalace && count < 100000) {
                                        try {
                                            Thread.sleep(refreshInfoSpeed);
                                        } catch (InterruptedException e) {
                                            Thread.currentThread().interrupt();
                                            e.printStackTrace();
                                        }
                                        count++;
                                    }
                                    progressDialog.dismiss();
                                    handler.sendEmptyMessage(107);
                                }
                            }.start();
                        }
                    });
            updateAlert.show();
        }
    }

    private void showLockBox() {
        AlertDialog dialog = new Builder(this).create();
        dialog.setTitle("开启宝箱");
        ScrollView scrollView = new ScrollView(this);
        TextView textView = new TextView(this);
        scrollView.addView(textView);
        heroN.setLockBox(heroN.getLockBox() - 1);
        StringBuilder builder = new StringBuilder("开宝箱获得的材料:<br>------<br>");
        ItemName itemName = ItemName.values()[heroN.getRandom().nextInt(ItemName.values().length)];
        Item item = Item.buildItem(heroN, itemName);
        item.save(null);
        builder.append(item.toString());
        builder.append("<br>").append("-------<br>");
        if (heroN.getRandom().nextBoolean()) {
            itemName = ItemName.values()[heroN.getRandom().nextInt(ItemName.values().length)];
            item = Item.buildItem(heroN, itemName);
            item.save(null);
            builder.append(item.toString());
            builder.append("<br>").append("-------<br>");
        }
        if (heroN.getRandom().nextBoolean()) {
            itemName = ItemName.values()[heroN.getRandom().nextInt(ItemName.values().length)];
            item = Item.buildItem(heroN, itemName);
            item.save(null);
            builder.append(item.toString());
            builder.append("<br>").append("-------<br>");
        }
        textView.setText(Html.fromHtml(builder.toString()));
        dialog.setView(scrollView);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }

                });
        dialog.show();
    }
    private void showItemFull() {
        AlertDialog dialog = new Builder(this).create();
        dialog.setTitle("背包满了！");
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }

                });
        dialog.show();
    }

    private void showCleanDialog() {
        AlertDialog dialog = new Builder(this).create();
        dialog.setTitle("清除数据");
        Button cleanSkill = new Button(this);
        cleanSkill.setText("清除技能");
        cleanSkill.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                DBHelper.getDbHelper().excuseSQLWithoutResult("DELETE FROM skill");
                view.setEnabled(false);
            }
        });
        Button cleanMonster = new Button(this);
        cleanMonster.setText("清除怪物");
        cleanMonster.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                DBHelper.getDbHelper().excuseSQLWithoutResult("DELETE FROM monster");
                view.setEnabled(false);
            }
        });
        Button cleanItem = new Button(this);
        cleanItem.setText("清除材料");
        cleanItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                DBHelper.getDbHelper().excuseSQLWithoutResult("DELETE FROM item");
                view.setEnabled(false);
            }
        });
        Button cleanAcc = new Button(this);
        cleanAcc.setText("清除物品");
        cleanAcc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                DBHelper.getDbHelper().excuseSQLWithoutResult("DELETE FROM accessory");
                view.setEnabled(false);
            }
        });
        Button deleteDB = new Button(this);
        deleteDB.setText("删除数据库");
        deleteDB.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                pause = true;
                DBHelper.getDbHelper().close();
                context.deleteDatabase(DBHelper.DB_NAME);
                DBHelper.init(context);
                view.setEnabled(false);
                pause = false;
            }
        });
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(cleanAcc);
        linearLayout.addView(cleanItem);
        linearLayout.addView(cleanMonster);
        linearLayout.addView(cleanSkill);
        linearLayout.addView(deleteDB);
        dialog.setView(linearLayout);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }

    private void showRecipe() {
        AlertDialog dialog = new Builder(this).create();
        dialog.setTitle("已发现的配方列表");
        ListView listView = new ListView(this);
        RecipeAdapter recipeAdapter = new RecipeAdapter();
        listView.setAdapter(recipeAdapter);
        dialog.setView(listView);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }


    private void showDownload() {
        final AlertDialog dialog = new Builder(this).create();
        final TextView text = new TextView(this);
        dialog.setView(text);
        final Handler handlerD = new Handler() {
            private int maxSize = 100;
            private TextView progressText = text;

            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case -1:
                        progressText.setText("下载失败，请检查网络！");
                        break;
                    case 1:
                        dialog.dismiss();
                        Achievement.updater.enable(heroN);
                        save();
                        installAPK();
                        break;
                    case 2:
                        maxSize = msg.arg1;
                        if (maxSize == 0) {
                            maxSize = 100;
                        }
                        break;
                    case 3:
                        progressText.setText((msg.arg1 / maxSize) + "%");
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
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        TextView info = new TextView(this);
        info.setText("上传勇者角色信息到服务器，下个版本您的角色将会作为迷宫的守护者拦截各个挑战的勇者。\n" +
                "上传成功后，下一次上传必须是再前进100层迷宫之后。\n输入您的口号：\n");
        linearLayout.addView(info);
        final EditText textView = new EditText(this);
        textView.setText("遇見了吾，你將止步於此！");
        linearLayout.addView(textView);
        dialog.setView(linearLayout);
        dialog.setTitle("上传角色信息");
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String hello = textView.getText().toString();
                hello = hello.replaceAll("'", "`");
                heroN.setHello(hello);
                handler.sendEmptyMessage(101);
                dialog.dismiss();
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void initGameData() {
        dbHelper = DBHelper.getDbHelper();
        if (dbHelper == null) {
            DBHelper.init(context);
            dbHelper = DBHelper.getDbHelper();
        }
        saveHelper = new SaveHelper(this);
        // 英雄
        load();
        skillDialog = MazeContents.skillDialog;
        if (skillDialog == null) {
            skillDialog = new SkillDialog(context);
        }
        skillDialog.setContext(this);
        monsterBook = new MonsterBook(this);
        // 左侧战斗信息
        mainInfoSv = (ScrollView) findViewById(R.id.main_info_sv);
        mainInfoPlatform = (LinearLayout) findViewById(R.id.main_info_ll);
        mainInfoPlatform.setOnLongClickListener(this);
        // ---- ---- 标题（人物名称 | 最深迷宫数)
        itembarContri = (TextView) findViewById(R.id.character_itembar_contribute);
        itembarContri.setOnClickListener(this);
        try {
            itembarContri.setBackgroundColor(Color.parseColor(heroN.getTitleColor()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // ---- ---- 属性
        mainContriHp = (TextView) findViewById(R.id.main_contri_hp);
        mainContriAtt = (TextView) findViewById(R.id.main_contri_att);
        mainContriDef = (TextView) findViewById(R.id.main_contri_def);
        swordLev = (TextView) findViewById(R.id.main_contri_level);
        armorLev = (TextView) findViewById(R.id.main_armor_level);
        heroPointValue = (TextView) findViewById(R.id.hero_points);
        mainContriCurMaterial = (TextView) findViewById(R.id.hero_material_count);
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
        pauseButton = (Button) findViewById(R.id.pause_button);
        pauseButton.setOnClickListener(this);
        clickCount = (TextView) findViewById(R.id.hero_pic_click_count);
        resetSkillButton = (Button) findViewById(R.id.reset_skill_button);
        resetSkillButton.setOnClickListener(this);
        buyButton = (Button) findViewById(R.id.buy_button);
        buyButton.setOnClickListener(this);
        firstSkillButton = (Button) findViewById(R.id.first_skill);
        firstSkillButton.setOnClickListener(this);
        secondSkillButton = (Button) findViewById(R.id.secondary_skill);
        secondSkillButton.setOnClickListener(this);
        thirdSkillButton = (Button) findViewById(R.id.third_skill);
        thirdSkillButton.setOnClickListener(this);
        uploadButton = (Button) findViewById(R.id.upload_button);
        updateButton = (Button) findViewById(R.id.update_button);
        uploadButton.setOnClickListener(this);
        updateButton.setOnClickListener(this);
        skillsButton = (Button) findViewById(R.id.skill_button);
        skillsButton.setOnClickListener(this);
        saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(this);
        upTSwordButton = (Button) findViewById(R.id.up_t_sword);
        upTSwordButton.setOnClickListener(this);
        upTArmorButton = (Button) findViewById(R.id.up_t_armor);
        upTArmorButton.setOnClickListener(this);
        addNAgiButton = (Button) findViewById(R.id.add_n_agi);
        addNAgiButton.setOnClickListener(this);
        addNStreButton = (Button) findViewById(R.id.add_n_stre);
        addNStreButton.setOnClickListener(this);
        addNPowerButton = (Button) findViewById(R.id.add_n_power);
        addNPowerButton.setOnClickListener(this);
        getSkillPointButton = (Button) findViewById(R.id.skill_point_get_button);
        getSkillPointButton.setOnClickListener(this);
        lockBoxCount = (TextView) findViewById(R.id.local_box);
        lockBoxCount.setOnClickListener(this);
        keyCount = (TextView) findViewById(R.id.key_count);
        forgeButton = (Button) findViewById(R.id.forge_button);
        forgeButton.setOnClickListener(this);
        itemButton = (Button) findViewById(R.id.accessory_button);
        itemButton.setOnClickListener(this);
        achievementButton = (Button) findViewById(R.id.achievement_button);
        achievementButton.setOnClickListener(this);
        ringTextView = (TextView) findViewById(R.id.ring_view);
        ringTextView.setOnClickListener(this);
        necklaceTextView = (TextView) findViewById(R.id.necklace_view);
        necklaceTextView.setOnClickListener(this);
        hatTextView = (TextView) findViewById(R.id.hat_view);
        hatTextView.setOnClickListener(this);
        recipeButton = (Button) findViewById(R.id.forge_recipe_button);
        recipeButton.setOnClickListener(this);
        cleanButton = (Button) findViewById(R.id.transfer_button);
        cleanButton.setOnClickListener(this);
        buyLockBoxButton = (Button) findViewById(R.id.lock_box_get_button);
        buyLockBoxButton.setOnClickListener(this);
        monsterBookButton = (Button) findViewById(R.id.monster_button);
        monsterBookButton.setOnClickListener(this);
        reincaenationButton = (Button) findViewById(R.id.rebirth_button);
        reincaenationButton.setOnClickListener(this);
        upgradeHArmorButton = (Button) findViewById(R.id.up_h_armor);
        upgradeHArmorButton.setOnClickListener(this);
        upgradeHSwordButton = (Button) findViewById(R.id.up_h_sword);
        upgradeHSwordButton.setOnClickListener(this);
        palaceButton = (Button) findViewById(R.id.palace_button);
        palaceButton.setOnClickListener(this);
        petView = (TextView) findViewById(R.id.pet_pic);
        petView.setOnClickListener(this);
        petDetail = (Button) findViewById(R.id.pet_detail_button);
        petDetail.setOnClickListener(this);
        refresh();
    }

    private synchronized void refresh() {
        if (pause) {
            pauseButton.setText("继续");
        } else {
            pauseButton.setText("暂停");
        }
        clickCount.setText("点击\n" + heroN.getClick());
        mainContriHp.setText(StringUtils.formatNumber(heroN.getHp()) + "/" +
                StringUtils.formatNumber(heroN.getUpperHp()));
        mainContriAtt.setText(StringUtils.formatNumber(heroN.getUpperAtk()));
        mainContriDef.setText(StringUtils.formatNumber(heroN.getUpperDef()));
        swordLev.setText(heroN.getSword() + "+" + heroN.getSwordLev());
        armorLev.setText(heroN.getArmor() + "+" + heroN.getArmorLev());
        mainContriCurMaterial.setText(StringUtils.formatNumber(heroN.getMaterial()));
        heroPointValue.setText(StringUtils.formatNumber(heroN.getPoint()));
        if (heroN.getMaterial() >= 100 + heroN.getSwordLev()) {
            upSword.setEnabled(true);
            if (heroN.getMaterial() > 10 * (100 + heroN.getSwordLev())) {
                upTSwordButton.setEnabled(true);
            } else {
                upTSwordButton.setEnabled(false);
            }
        } else {
            upSword.setEnabled(false);
            upTSwordButton.setEnabled(false);
        }
        if (heroN.getMaterial() > 100 * (80 + heroN.getArmorLev())) {
            upgradeHArmorButton.setEnabled(true);
        } else {
            upgradeHArmorButton.setEnabled(false);
        }
        if (heroN.getMaterial() > 100 * (100 + heroN.getSwordLev())) {
            upgradeHSwordButton.setEnabled(true);
        } else {
            upgradeHSwordButton.setEnabled(false);
        }
        if (heroN.getMaterial() >= 80 + heroN.getArmorLev()) {
            upArmor.setEnabled(true);
            if (heroN.getMaterial() > 10 * (80 + heroN.getArmorLev())) {
                upTArmorButton.setEnabled(true);
            } else {
                upTArmorButton.setEnabled(false);
            }
        } else {
            upArmor.setEnabled(false);
            upTArmorButton.setEnabled(false);
        }
        if (heroN.getPoint() > 0) {
            addpow.setEnabled(true);
            addstr.setEnabled(true);
            addagi.setEnabled(true);
            addNPowerButton.setEnabled(true);
            addNStreButton.setEnabled(true);
            addNAgiButton.setEnabled(true);
        } else {
            addpow.setEnabled(false);
            addstr.setEnabled(false);
            addagi.setEnabled(false);
            addNPowerButton.setEnabled(false);
            addNStreButton.setEnabled(false);
            addNAgiButton.setEnabled(false);
        }
        itembarContri.setText(heroN.getName() + (heroN.getReincaCount() != 0 ? ("(" + heroN.getReincaCount() + ")") : "") +
                "\n迷宫到达(当前/记录）层\n" + maze.getLev() + "/" + heroN.getMaxMazeLev());
        if (heroN.getFirstSkill() != null) {
            firstSkillButton.setText(heroN.getFirstSkill().getDisplayName());
            firstSkillButton.setEnabled(true);
        } else {
            firstSkillButton.setText("");
            firstSkillButton.setEnabled(false);
        }
        if (heroN.getSecondSkill() != null) {
            secondSkillButton.setText(heroN.getSecondSkill().getDisplayName());
            secondSkillButton.setEnabled(true);
        } else {
            secondSkillButton.setText("");
            secondSkillButton.setEnabled(false);
        }
        if (heroN.getThirdSkill() != null) {
            thirdSkillButton.setText(heroN.getThirdSkill().getDisplayName());
            thirdSkillButton.setEnabled(true);
        } else {
            thirdSkillButton.setText("");
            thirdSkillButton.setEnabled(false);
        }
        if (!uploading) {
            if (MazeContents.lastUpload + 100 <= heroN.getMaxMazeLev()) {
                uploadButton.setEnabled(true);
                uploadButton.setText("上传角色信息");
            } else {
                uploadButton.setEnabled(false);
                uploadButton.setText("还差" + (100 - heroN.getMaxMazeLev() + MazeContents.lastUpload) + "层");
            }
        }

        lockBoxCount.setText("" + heroN.getLockBox());
        keyCount.setText("" + heroN.getKeyCount());
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
        StringBuilder builder = new StringBuilder();
        for (Pet pet : heroN.getPets()) {
            if (!pet.getType().equals("蛋")) {
                builder.append(pet.getType()).append(pet.getSex() == 0 ? "♂" : "♀").append("<br>");
            } else {
                builder.append(pet.getType()).append("<br>");
            }
        }
        petView.setText(Html.fromHtml(builder.toString()));
    }

    private long saveTime = 0;

    public DBHelper getDbHelper() {
        return dbHelper;
    }

    public void setDbHelper(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
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

    public Alipay getAlipay() {
        return alipay;
    }

    public void setAlipay(Alipay alipay) {
        this.alipay = alipay;
    }

    public void setLastUploadLev(long lastUploadLev) {
        MazeContents.lastUpload = lastUploadLev;
    }

    public void setHeroN(Hero heroN) {
        this.heroN = heroN;
    }

    public void setMaze(Maze maze) {
        this.maze = maze;
    }

    public SkillDialog getSkillDialog() {
        return skillDialog;
    }

    public MonsterBook getMonsterBook() {
        return monsterBook;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public void startMaze() {
        new GameThread().start();
    }

    public void stopMaze() {
        gameThreadRunning = false;
        save();
    }

    public int getPalaceCount() {
        return palaceCount;
    }

    public void setPalaceCount(int palaceCount) {
        this.palaceCount = palaceCount;
    }

    public void setHidBattle(boolean isHidBattle) {
        this.isHidBattle = isHidBattle;
    }

    private class GameThread extends Thread {

        @Override
        public void run() {
            try {
                new MoveThread().start();
                while (gameThreadRunning) {
                    try {
                        Thread.sleep(refreshInfoSpeed);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (!pause) {
                    saveTime += refreshInfoSpeed;
                    if (saveTime >= refreshInfoSpeed * 300)
                        save();
                }
            } catch (Exception exp) {
                Log.e(TAG, "MainGameActivity.GameThread", exp);
                LogHelper.logException(exp);
            }
        }
    }

    private class MoveThread extends Thread {
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

    private void checkUpdate() {
        new Thread(new Runnable() {
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
                    updateVersion = br.readLine().replace((char) 65279, ' ').trim();

                    if (updateVersion.equalsIgnoreCase(currentVersion)) {
                        handler.sendEmptyMessage(201);
                    } else {
                        String info = br.readLine();
                        versionUpdateInfo = new StringBuilder("新版本:" + updateVersion);
                        while (info != null && !info.isEmpty()) {
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
                PalaceMonster.getPalaceCount(context);
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
            if (file.exists()) {
                file.delete();
            }
            File path = new File(APK_PATH);
            if (!path.exists()) {
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
            Log.e(TAG, "DownloadUpdate", e);
        }
        handler.sendEmptyMessage(-1);
        return false;
    }

    private void installAPK() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(APK_PATH + "/MazeNeverEnd.apk")),
                "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.main_info_ll:
                handler.sendEmptyMessage(109);
                break;
        }
        return false;
    }


    @Override
    public void onClick(View v) {
        Log.i(TAG, "onClick() -- " + v.getId() + " -- 被点击了");
        switch (v.getId()) {
            case R.id.pet_detail_button:
                saveHelper.savePet();
                new PetDialog(context).show(heroN);
                break;
            case R.id.pet_pic:
                for (Pet pet : heroN.getPets()) {
                    pet.click();
                }
                heroN.click(false);
                break;
            case R.id.palace_button:
                showPalace();
                break;
            case R.id.up_h_armor:
                heroN.upgradeArmor(100);
                handler.sendEmptyMessage(0);
                heroN.click(false);
                break;
            case R.id.up_h_sword:
                heroN.upgradeSword(100);
                handler.sendEmptyMessage(0);
                heroN.click(false);
                break;
            case R.id.rebirth_button:
                showReincarnationDialog();
                handler.sendEmptyMessage(0);
                break;
            case R.id.monster_button:
                monsterBook.showBook(context);
                break;
            case R.id.lock_box_get_button:
                getLockBox();
                heroN.click(false);
                break;
            case R.id.local_box:
                boolean count = Item.getItemCount() < 1000;
                if (heroN.getKeyCount() > 0 && heroN.getLockBox() > 0 && count) {
                    heroN.setKeyCount(heroN.getKeyCount() - 1);
                    showLockBox();
                    heroN.click(false);
                }else if(!count){
                    showItemFull();
                }
                break;
            case R.id.transfer_button:
                showCleanDialog();
                break;
            case R.id.ring_view:
            case R.id.necklace_view:
            case R.id.hat_view:
                showAccessory();
                break;
            case R.id.forge_recipe_button:
                showRecipe();
                break;
            case R.id.achievement_button:
                showAchievement();
                break;
            case R.id.accessory_button:
                showAccessory();
                break;
            case R.id.forge_button:
                handler.sendEmptyMessage(103);
                Intent intent = new Intent(this, ForgeActivity.class);
                startActivity(intent);
                break;
            case R.id.skill_point_get_button:
                showGetSkillPointDialog();
                break;
            case R.id.add_n_agi:
                long agi = heroN.getRandom().nextLong(heroN.getPoint() + 1);
                heroN.addAgility(agi);
                heroN.addPoint(-agi);
                handler.sendEmptyMessage(0);
                heroN.click(false);
                break;
            case R.id.add_n_power:
                long life = heroN.getRandom().nextLong(heroN.getPoint() + 1);
                heroN.addLife(life);
                heroN.addPoint(-life);
                handler.sendEmptyMessage(0);
                heroN.click(false);
                break;
            case R.id.add_n_stre:
                long str = heroN.getRandom().nextLong(heroN.getPoint() + 1);
                heroN.addStrength(str);
                heroN.addPoint(-str);
                handler.sendEmptyMessage(0);
                heroN.click(false);
                break;
            case R.id.up_t_armor:
                heroN.upgradeArmor(10);
                handler.sendEmptyMessage(0);
                heroN.click(false);
                break;
            case R.id.up_t_sword:
                heroN.upgradeSword(10);
                handler.sendEmptyMessage(0);
                heroN.click(false);
                break;
            case R.id.save_button:
                handler.sendEmptyMessage(103);
                break;
            case R.id.skill_button:
                if (!skillDialog.isInit()) {
                    skillDialog.init();
                }
                skillDialog.show(heroN);
                break;
            case R.id.update_button:
                BmobUpdateAgent.forceUpdate(context);
//                showUpdate();
                break;
            case R.id.upload_button:
                showUpload();
                break;
            case R.id.first_skill:
                if (heroN.getFirstSkill() != null) {
                    heroN.getFirstSkill().addCount();
                }
                handler.sendEmptyMessage(0);
                break;
            case R.id.secondary_skill:
                if (heroN.getSecondSkill() != null) {
                    heroN.getSecondSkill().addCount();
                }
                handler.sendEmptyMessage(0);
                break;
            case R.id.third_skill:
                if (heroN.getThirdSkill() != null) {
                    heroN.getThirdSkill().addCount();
                }
                handler.sendEmptyMessage(0);
                break;
            case R.id.buy_button:
                showPayDialog();
                heroN.click(false);
                break;
            case R.id.character_itembar_contribute:
                showNameDialog();
                heroN.click(false);
                break;
            case R.id.reset_skill_button:
                showResetSkillPointDialog();
                heroN.click(false);
                break;
            case R.id.pause_button:
                handler.sendEmptyMessage(1);
                heroN.click(false);
                break;
            case R.id.up_armor:
                heroN.upgradeArmor(1);
                handler.sendEmptyMessage(0);
                heroN.click(false);
                break;
            case R.id.up_sword:
                heroN.upgradeSword(1);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public synchronized void save() {
        saveHelper.save();
    }

    private void load() {
        if (MazeContents.hero == null) {
            if (heroN == null) {
                LoadHelper loadHelper = new LoadHelper(this);
                loadHelper.loadHero();
                DBHelper.init(MainGameActivity.this);
                MazeContents.skillDialog = new SkillDialog();
                loadHelper.loadHero();
                loadHelper.loadSkill(MazeContents.hero, MazeContents.skillDialog);
            }
        }
        heroN = MazeContents.hero;
        maze = MazeContents.maze;
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

    static class AchievementViewHolder {
        Button name;
        Button name1;
        Button name2;
        Button name3;
    }

    class AchievementAdapter extends BaseAdapter {
        private final List<AchievementList> adapterData = Achievement.getAchievementListAdp();

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

                holder.name1 = (Button) convertView.findViewById(R.id.achieve_name_1);

                holder.name2 = (Button) convertView.findViewById(R.id.achieve_name_2);

                holder.name3 = (Button) convertView.findViewById(R.id.achieve_name_3);

                convertView.setTag(holder);
            } else {
                holder = (AchievementViewHolder) convertView.getTag();
            }
            final AchievementList item = getItem(position);
            holder.name.setText(item.a0.getName());
            holder.name.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.a0.isEnable()) {
                        achievementDesc.setText(Html.fromHtml("<font color=\"blue\"><b>" + item.a0.getName() + "</b> : " + item.a0.getDesc() + "</font>"));
                    } else {
                        achievementDesc.setText(Html.fromHtml("<b>" + item.a0.getName() + "</b> : " + item.a0.getUnlockDesc() + "。效果：？？？"));
                    }
                }
            });
            if (!item.a0.isEnable()) {
                holder.name.setTextColor(convertView.getResources().getColor(R.color.disable));
            } else {
                holder.name.setTextColor(convertView.getResources().getColor(R.color.onUse));
            }
            holder.name1.setText(item.a1.getName());
            holder.name1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.a1.isEnable()) {
                        achievementDesc.setText(Html.fromHtml("<font color=\"blue\"><b>" + item.a1.getName() + "</b> : " + item.a1.getDesc() + "</font>"));
                    } else {
                        achievementDesc.setText(Html.fromHtml("<b>" + item.a1.getName() + "</b> : " + item.a1.getUnlockDesc() + "。效果：？？？"));
                    }
                }
            });
            if (!item.a1.isEnable()) {
                holder.name1.setTextColor(convertView.getResources().getColor(R.color.disable));
            } else {
                holder.name1.setTextColor(convertView.getResources().getColor(R.color.onUse));
            }
            holder.name2.setText(item.a2.getName());
            holder.name2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.a2.isEnable()) {
                        achievementDesc.setText(Html.fromHtml("<font color=\"blue\"><b>" + item.a2.getName() + "</b> : " + getItem(position).a2.getDesc() + "</font>"));
                    } else {
                        achievementDesc.setText(Html.fromHtml("<b>" + item.a2.getName() + "</b> : " + item.a2.getUnlockDesc() + "。效果：？？？"));
                    }
                }
            });
            if (!item.a2.isEnable()) {
                holder.name2.setTextColor(convertView.getResources().getColor(R.color.disable));
            } else {
                holder.name2.setTextColor(convertView.getResources().getColor(R.color.onUse));
            }
            holder.name3.setText(item.a3.getName());
            holder.name3.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.a3.isEnable()) {
                        achievementDesc.setText(Html.fromHtml("<font color=\"blue\"><b>" + item.a3.getName() + "</b> : " + getItem(position).a3.getDesc() + "</font>"));
                    } else {
                        achievementDesc.setText(Html.fromHtml("<b>" + item.a3.getName() + "</b> : " + item.a3.getUnlockDesc() + "。效果：？？？"));
                    }
                }
            });
            if (!item.a3.isEnable()) {
                holder.name3.setTextColor(convertView.getResources().getColor(R.color.disable));

            } else {
                holder.name3.setTextColor(convertView.getResources().getColor(R.color.onUse));
            }
            return convertView;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    public GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() - e2.getX() > 100) {
                buttonGroup.showNext();
                return true;
            } else if (e1.getX() - e2.getY() < -100) {
                buttonGroup.showPrevious();
                return true;
            }
            return false;
        }
    };
}
