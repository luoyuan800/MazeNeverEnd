package cn.gavin.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.*;
import android.text.Html;
import android.text.InputFilter;
import android.util.Log;
import android.util.TypedValue;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.BmobDialogButtonListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.update.BmobUpdateAgent;
import cn.bmob.v3.update.UpdateStatus;
import cn.gavin.Achievement;
import cn.gavin.Element;
import cn.gavin.Hero;
import cn.gavin.R;
import cn.gavin.alipay.Alipay;
import cn.gavin.db.DBHelper;
import cn.gavin.forge.*;
import cn.gavin.forge.adapter.AccessoryAdapter;
import cn.gavin.forge.adapter.RecipeAdapter;
import cn.gavin.forge.effect.Effect;
import cn.gavin.forge.list.ItemName;
import cn.gavin.gift.GiftDialog;
import cn.gavin.good.GoodsDialog;
import cn.gavin.good.GoodsType;
import cn.gavin.good.ShopDialog;
import cn.gavin.log.LogHelper;
import cn.gavin.maze.Maze;
import cn.gavin.maze.MazeService;
import cn.gavin.monster.Monster;
import cn.gavin.monster.MonsterBook;
import cn.gavin.story.PalaceAdapt;
import cn.gavin.pet.Pet;
import cn.gavin.pet.PetDB;
import cn.gavin.pet.PetDialog;
import cn.gavin.pet.skill.PetSkillList;
import cn.gavin.pet.swop.ui.SwapPetMainDialog;
import cn.gavin.save.LoadHelper;
import cn.gavin.save.SaveHelper;
import cn.gavin.skill.SkillFactory;
import cn.gavin.skill.SkillMainDialog;
import cn.gavin.upload.CdKey;
import cn.gavin.story.PalaceObject;
import cn.gavin.upload.Upload;
import cn.gavin.utils.MazeContents;
import cn.gavin.utils.ScreenUtils;
import cn.gavin.utils.StringUtils;
import cn.gavin.utils.ui.AddPointDialog;
import cn.gavin.utils.ui.CircleMenu;

import java.util.EnumMap;
import java.util.List;

@SuppressWarnings("ALL")
public class MainGameActivity extends Activity implements OnClickListener, View.OnLongClickListener, OnItemClickListener, BaseContext {
    //Constants
    public static final String TAG = "MazeNeverEnd";
    public static final String APK_PATH = Environment.getExternalStorageDirectory() + "/maze";

    // 战斗刷新速度
    private long refreshInfoSpeed = 520;

    // 战斗信息
    private ScrollView mainInfoSv;
    private LinearLayout mainInfoPlatform;

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
    private Button upSword;
    private Button upArmor;
    private Button heroPic;
    private Button pauseButton;
    private Button firstSkillButton;
    private Button secondSkillButton;
    private Button thirdSkillButton;

    private boolean pause;
    private boolean gameThreadRunning;
    public static MainGameActivity context;
    private Alipay alipay;

    //Upload control
    private boolean uploading = false;
    private DBHelper dbHelper;
    private SaveHelper saveHelper;
    private Button saveButton;
    private Button upTArmorButton;
    private Button upTSwordButton;

    private TextView lockBoxCount;
    private TextView keyCount;
    private TextView ringTextView;
    private TextView necklaceTextView;
    private TextView hatTextView;
    private MonsterBook monsterBook;
    private Button upgradeHSwordButton;
    private Button upgradeHArmorButton;
    private boolean isHidBattle;
    private boolean updatePalace;
    private TextView characterName;
    private View mainRightDown;
    private View mainLeftUp;
    private LinearLayout mainLeftDown;
    TextView shopTipView;

    private WindowManager mWindowManager = null;
    private WindowManager.LayoutParams wmParams = null;
    // 用于显示浮动图标
    private ImageView img_Float;
    private CircleMenu netButton;
    private Button fourthSkillButton;
    private Button sixthSkillButton;
    private Button fifthSkillButton;
    private TextView onUsedPetLists;
    private Button reniButton;
    private Button colButton;
    private TextView mainContriAgi;
    private TextView mainContriStr;
    private TextView mainContriLife;


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

    @SuppressWarnings("HandlerLeak")
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(final Message msg) {
            try {
                switch (msg.what) {
                    case 123:
                        String gift = "";
                        if (heroN.getGift() != null) {
                            gift = "<" + heroN.getGift().getName() + ">\n";
                        }
                        characterName.setText(gift +
                                heroN.getName() + (heroN.getReincaCount() != 0 ? ("(" + heroN.getReincaCount() + ")") : ""));
                        break;
                    case 122:
                        if (shopTipView != null) {
                            mWindowManager.removeView(shopTipView);
                        }
                        shopTipView = null;
                        break;
                    case 121:
                        // 获取WindowManager
                        // 设置LayoutParams(全局变量）相关参数
                        WindowManager.LayoutParams params = new WindowManager.LayoutParams();

                        params.type = WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG; // 设置window type
                        params.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明
                        // 设置Window flag
                        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

                        // 以屏幕左上角为原点，设置x、y初始值
                        params.x = 400;
                        params.y = 500;
                        // 设置悬浮窗口长宽数据
                        params.width = 80;
                        params.height = 80;

                        shopTipView = new TextView(context);
                        shopTipView.setText("锻造点数：" + MazeContents.hero.getMaterial());

//        img_Float.setAlpha(23);
                        // 调整悬浮窗口
                        params.gravity = Gravity.START | Gravity.CENTER_VERTICAL;
                        // 显示myFloatView图像
                        mWindowManager.addView(shopTipView, params);
                        break;
                    case 120:
                        if (isFloat) {
                            mWindowManager.removeView(img_Float);
                            isFloat = false;
                        }
                        break;
                    case 119:
                        final String dieMsg = msg.obj.toString();
                        if (isFloat) {
                            mWindowManager.removeView(img_Float);
                            isFloat = false;
                        }
                        img_Float.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View arg0) {
                                AlertDialog dieMessage = new Builder(context).create();
                                dieMessage.setTitle("最近一次被打败的信息");
                                ScrollView scrollView = new ScrollView(context);
                                TextView msgText = new TextView(context);
                                scrollView.addView(msgText);
                                dieMessage.setView(scrollView);
                                msgText.setText(Html.fromHtml(dieMsg));
                                dieMessage.setButton(DialogInterface.BUTTON_NEGATIVE, "确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        mWindowManager.removeView(img_Float);
                                        isFloat = false;
                                    }
                                });
                                dieMessage.show();
                            }
                        });
                        mWindowManager.addView(img_Float, wmParams);
                        isFloat = true;
                        break;
                    case 118:
                        AlertDialog inDialog = new Builder(context).create();
                        final PalaceObject palaceObjectIn = (PalaceObject) msg.obj;
                        final String[] goodsAwardIn = StringUtils.split(palaceObjectIn.getAward(), "-");
                        StringBuilder goodNamesIn = new StringBuilder();
                        for (String good : goodsAwardIn) {
                            GoodsType goodsType = GoodsType.valueOf(good);
                            goodNamesIn.append(goodsType.getName()).append("\n");
                        }
                        inDialog.setMessage("恭喜您进入殿堂！\n您获得了奖励为\n" + goodNamesIn +
                                "\n请您及时领取，过期无效！");
                        inDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "暂不领取", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        inDialog.setButton(DialogInterface.BUTTON_POSITIVE, "领取", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PalaceObject palaceObject1 = new PalaceObject();
                                palaceObject1.setValue("award", " ");
                                palaceObject1.setObjectId(palaceObjectIn.getObjectId());
                                palaceObject1.setAward(" ");
                                palaceObject1.setTableName("PalaceObject");
                                palaceObject1.update(context, palaceObjectIn.getObjectId(), new UpdateListener() {
                                    @Override
                                    public void onSuccess() {
                                        Message message = new Message();
                                        message.obj = goodsAwardIn;
                                        message.what = 116;
                                        handler.sendMessage(message);
                                    }

                                    @Override
                                    public void onFailure(int i, String s) {
                                        handler.sendEmptyMessage(114);
                                    }
                                });
                                dialog.dismiss();
                            }
                        });
                        inDialog.show();
                        break;
                    case 117:
                        AlertDialog alreadyGetDialog = new Builder(context).create();
                        alreadyGetDialog.setMessage("对不起！\n您已经领取过奖励了！\n请明天再来！");
                        alreadyGetDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "知道了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alreadyGetDialog.show();
                        break;
                    case 116:
                        if ((String[]) msg.obj != null) {
                            for (String goodName : (String[]) msg.obj) {
                                GoodsType goods = GoodsType.loadByName(goodName);
                                goods.setCount(goods.getCount() + 1);
                                goods.save();
                                Toast.makeText(context, "--获得了" + goods.getName() + "--", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                        break;
                    case 115:
                        AlertDialog notInSortDialog = new Builder(context).create();
                        notInSortDialog.setMessage("对不起！\n您没有进入殿堂！\n请继续努力！");
                        notInSortDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "知道了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                            }
                        });
                        notInSortDialog.show();
                        break;
                    case 114:
                        AlertDialog errorDialog = new Builder(context).create();
                        errorDialog.setMessage("对不起！网络故障，请您稍后再试！");
                        errorDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "知道了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        errorDialog.show();
                        break;
                    case 113:
                        AlertDialog successDialog = new Builder(context).create();
                        final PalaceObject palaceObject = (PalaceObject) msg.obj;
                        final String[] goodsAward = StringUtils.split(palaceObject.getAward(), "-");
                        StringBuilder goodNames = new StringBuilder();
                        for (String good : goodsAward) {
                            GoodsType goodsType = GoodsType.valueOf(good);
                            goodNames.append(goodsType.getName()).append("\n");
                        }
                        successDialog.setMessage("恭喜您成为殿堂" + (msg.arg1 > 0 ? "第" + msg.arg1 : "最后一") + " 名！\n您获得了奖励物品为\n" + goodNames +
                                "请您及时领取，过期无效！");
                        successDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "暂不领取", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        successDialog.setButton(DialogInterface.BUTTON_POSITIVE, "领取", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PalaceObject palaceObject1 = new PalaceObject();
                                palaceObject1.setValue("award", " ");
                                palaceObject1.setObjectId(palaceObject.getObjectId());
                                palaceObject1.setAward(" ");
                                palaceObject1.setTableName("PalaceObject");
                                palaceObject1.update(context, palaceObject.getObjectId(), new UpdateListener() {
                                    @Override
                                    public void onSuccess() {
                                        Message message = new Message();
                                        message.obj = goodsAward;
                                        message.what = 116;
                                        handler.sendMessage(message);
                                    }

                                    @Override
                                    public void onFailure(int i, String s) {
                                        handler.sendEmptyMessage(114);
                                    }
                                });
                                dialog.dismiss();
                            }
                        });
                        successDialog.show();
                        break;
                    case 112:
                        heroN.setAwardCount(heroN.getAwardCount() + 1);
                        GoodsType firstGoods = GoodsType.RandomGoods;
                        firstGoods.load();
                        firstGoods.setCount(firstGoods.getCount() + 1);
                        firstGoods.save();
                        ScrollView scrollView = new ScrollView(context);
                        AlertDialog dialog = new Builder(context).create();
                        dialog.setTitle("欢迎您进入迷宫");
                        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "我知道了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        TextView welcome = new TextView(context);
                        welcome.setText(Html.fromHtml("这是您第一次进入这个世界，这是一个简单的游戏，游戏中有物品系统、技能系统、装备打造系统、宠物系统<br>\n" +
                                "游戏玩法就是挂机挂机再挂机，希望您能喜欢这个放置类游戏<br>\n" +
                                "游戏的主界面左边是游戏信息和内容的显示的地方，请随时关注<br>\n" +
                                "游戏右边会显示人物形象和相应的控制按钮，点击左右切换按钮可以切换其他按钮面板<br>\n" +
                                "请百度搜索游戏的贴吧：勇者闯迷宫game 获取更多的帮助信息<br>\n" +
                                "系统赠送了一个随机物品给你，请到物品背包中查收<br>\n" +
                                "祝您游戏快乐！"));
                        scrollView.addView(welcome);
                        dialog.setView(scrollView);
                        dialog.show();
                        showGiftChoose();
                        break;
                    case 111:
                        heroN.setAwardCount(heroN.getAwardCount() + 3000);
                        GoodsType goods = GoodsType.RandomGoods;
                        goods.load();
                        goods.setCount(goods.getCount() + 1);
                        goods.save();
                        Toast.makeText(context, "--感谢您一直以来的支持!特赠送您一个随机物品和十把钥匙，请到物品背包中查收！--", Toast.LENGTH_LONG)
                                .show();
                        break;
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
                            textView.setText("\n\n长按显示战斗信息\n\n欢迎光临贴吧：勇者闯迷宫game\n欢迎进群：332406332 \n举报bug和提供您的建议！");
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
                        break;
                    case 203:
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
                        uploading = true;
                        Upload upload = new Upload(context);
                        heroN.setPay(MazeContents.payTime);
                        upload.upload(heroN);
                        break;

                    case 100:
                        heroN.addMaterial(5000000);
                        heroN.addPoint(200);
                        alipay.addPayTime();
                        if (alipay.getPayTime() == 10) {
                            Achievement.crapGame.enable();
                        }
                        Achievement.richer.enable(heroN);
                        showAwardPet("感谢您的支持");
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
                            int fightInfoTotalCount = 50;
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

    private final Intent service = new Intent(MazeService.ACTION);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_main_gameview);
        context = this;
        setAlipay(new Alipay(context, MazeContents.payTime));
        Log.i(TAG, "start game~");
        initGameView();
        initGameData();
        intFloatImage();
//        checkUpdate();
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
            BmobUpdateAgent.update(this);
        } catch (Exception e) {
            LogHelper.logException(e);
        }
        gameThreadRunning = true;
        service.setPackage(getPackageName());
        startService(service);
        bindService(service, connection, BIND_AUTO_CREATE);

    }

    public void intFloatImage() {
        // 获取WindowManager
        mWindowManager = getWindowManager();
        // 设置LayoutParams(全局变量）相关参数
        wmParams = new WindowManager.LayoutParams();

        wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG; // 设置window type
        wmParams.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明
        // 设置Window flag
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        // 以屏幕左上角为原点，设置x、y初始值
        wmParams.x = 50;
        wmParams.y = 300;
        System.out.println("*************" + wmParams.y);
        // 设置悬浮窗口长宽数据
        wmParams.width = 80;
        wmParams.height = 80;
        createFloatView();
    }

    /**
     * 创建悬浮图片按钮
     */
    private void createFloatView() {
        img_Float = new ImageView(this);
        img_Float.setImageResource(R.drawable.die_msg);
//        img_Float.setAlpha(23);
        // 调整悬浮窗口
        wmParams.gravity = Gravity.START | Gravity.CENTER_VERTICAL;
        // 显示myFloatView图像
    }

    private boolean isFloat = false;

    public void showFloatView(final String dieMsg) {
        Message message = new Message();
        message.what = 119;
        message.obj = dieMsg;
        handler.sendMessage(message);
    }

    public void hidFloatView() {
        Message message = new Message();
        message.what = 120;
        handler.sendMessage(message);
    }


    @SuppressWarnings("NullableProblems")
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

    AlertDialog upgradeSwordDialog;
    AlertDialog upgradeArmorDialog;

    private void initGameView() {
        ImageButton upgradeSword = (ImageButton) findViewById(R.id.upgrade_sword_button);
        ImageButton upgradeArmor = (ImageButton) findViewById(R.id.upgrade_armor_button);
        final View upgradeSwordView = View.inflate(context, R.layout.upgrade_sword_buttons,
                (ViewGroup) (context).findViewById(R.id.add_point_root));
        final View upgradeArmorView = View.inflate(context, R.layout.upgrade_armor_buttons,
                (ViewGroup) (context).findViewById(R.id.add_point_root));
        upgradeHArmorButton = (Button) upgradeArmorView.findViewById(R.id.up_h_armor);
        upgradeHArmorButton.setOnClickListener(this);
        upgradeHSwordButton = (Button) upgradeSwordView.findViewById(R.id.up_h_sword);
        upgradeHSwordButton.setOnClickListener(this);
        upTSwordButton = (Button) upgradeSwordView.findViewById(R.id.up_t_sword);
        upTSwordButton.setOnClickListener(this);
        upTArmorButton = (Button) upgradeArmorView.findViewById(R.id.up_t_armor);
        upTArmorButton.setOnClickListener(this);
        upSword = (Button) upgradeSwordView.findViewById(R.id.up_sword);
        upSword.setOnClickListener(this);
        upArmor = (Button) upgradeArmorView.findViewById(R.id.up_armor);
        upArmor.setOnClickListener(this);
        upgradeSword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (upgradeSwordDialog == null) {
                    upgradeSwordDialog = new Builder(context).create();
                    upgradeSwordDialog.setTitle("升级武器");
                    upgradeSwordDialog.setView(upgradeSwordView);
                    upgradeSwordDialog.setButton(DialogInterface.BUTTON_POSITIVE, "退出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            upgradeSwordDialog.hide();
                        }
                    });
                }
                upgradeSwordDialog.show();
            }
        });
        upgradeArmor.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (upgradeArmorDialog == null) {
                    upgradeArmorDialog = new Builder(context).create();
                    upgradeArmorDialog.setTitle("升级护具");
                    upgradeArmorDialog.setView(upgradeArmorView);
                    upgradeArmorDialog.setButton(DialogInterface.BUTTON_POSITIVE, "退出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            upgradeArmorDialog.hide();
                        }
                    });
                }
                upgradeArmorDialog.show();
            }
        });

        Button netButton = (Button) findViewById(R.id.net_button);
        netButton.setOnClickListener(this);

        Button helpButton = (Button) findViewById(R.id.help_button);
        helpButton.setOnClickListener(this);

        Button bagButton = (Button) findViewById(R.id.bag_button);
        bagButton.setOnClickListener(this);

        Button fenpeiButton = (Button) findViewById(R.id.fenpei_button);
        fenpeiButton.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        gameThreadRunning = false;
        dbHelper.close();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, @SuppressWarnings("NullableProblems") KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                showExitDialog();

                return true;

            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    //Popup dialog
    private void showPayDialog() {
        AlertDialog dialog = new Builder(this).create();
        dialog.setTitle("是否确认？");
        TextView tv = new TextView(context);
        tv.setText("注意：\n1.  感谢您的支持，无论您是想踩还是赞~\n" +
                "2.  这个并不算是真正内购功能（虽然确实很像内购）\n" +
                "3.  请适量使用。过多的锻造点数并不能加快您的游戏进度。\n" +
                "4.  您会获得额外的500W点锻造点数和随机的能力点数。\n" +
                "5.  您会随机获得一个高成长的宠物。\n");
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
                "2.  迷宫记录会被清除。\n" +
                "3.  所有材料会被清空。\n" +
                "4  装备着的饰品会继承下来，其他装备会被丢弃。\n" +
                "5.  未分配的能力点数会被清空.\n" +
                "6.  转生后的基础属性会根据转生前的属性得到加强（基础属性影响人物的成长）。\n" +
                "7.  转生消耗的锻造点数会随着转生次数递增。\n" +
                "8.  转生会增加宠物携带上限。\n" +
                "9. 转生后怪物属性会增强，但你也可以爬得更高。");
        dialog.setView(tv);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        heroN.reincarnation();
                        dialog.dismiss();
                        refreshCharacterName();
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
        this.finish();
        this.stopService(service);
        System.exit(0);
    }

    private TextView achievementDesc;
    private AlertDialog achDialog;

    private void showAchievement() {
        if (achDialog == null) {
            achDialog = new Builder(this).create();
            int count = 0;
            for (Achievement achievement : Achievement.values()) {
                if (achievement.isEnable()) {
                    count++;
                }
            }
            achDialog.setTitle("成就 " + (count * 100 / Achievement.values().length) + "%");
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            achievementDesc = new TextView(this);
            linearLayout.addView(achievementDesc);
            ListView listView = new ListView(this);
            AchievementAdapter achievementAdapter = new AchievementAdapter();
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

    public void showResetSkillPointDialog() {
        AlertDialog resetSkillPointDialog;
        resetSkillPointDialog = new Builder(this).create();
        resetSkillPointDialog.setTitle("重置技能");
        resetSkillPointDialog.setMessage("重置技能后技能的使用/点击(熟练度)次数会减半。返回激活消耗的技能点，但是升级消耗的技能点不会返回。");
        resetSkillPointDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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
    }

    /*
        Change name
     */
    private void showNameDialog() {
        AlertDialog dialog = new Builder(this).create();
        dialog.setTitle("给勇者取个名字");
        final EditText tv = new EditText(this);
        tv.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
        tv.setText(heroN.getName());
        dialog.setView(tv);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String input = tv.getText().toString();
                        if (heroN.getAwardCount() < 26 && input.equals("bbbbb~5")) {
                            heroN.setAwardCount(heroN.getAwardCount() + 26);
                            heroN.setPetSize(3 + heroN.getReincaCount().intValue() + 5);
                        } else if (input.equals("gavin~0")) {
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
                                String[] color = StringUtils.split(input, "_");
                                if (color.length > 0) {
                                    if (color[0].startsWith("#ff") || color[0].startsWith("#FF")) {
                                        color[0] = color[0].replaceFirst("#(ff|FF)", "#8b");
                                    }
                                }
                                if (color.length < 2) {
                                    itembarContri.setBackgroundColor(Color.parseColor(input));
                                    heroN.setTitleColor(input);
                                } else {
                                    switch (StringUtils.toInt(color[1])) {
                                        case 1:
                                            mainLeftUp.setBackgroundColor(Color.parseColor(color[0]));
                                            onUsedPetLists.setBackgroundColor(Color.parseColor(color[0]));
                                            heroN.setLeftUpColor(color[0]);
                                            break;
                                        case 2:
                                            mainLeftDown.setBackgroundColor(Color.parseColor(color[0]));
                                            heroN.setLeftDownColor(color[0]);
                                            break;
                                        case 3:
                                            mainRightDown.setBackgroundColor(Color.parseColor(color[0]));
                                            heroN.setRightDownColor(color[0]);
                                            break;
                                    }
                                }
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
                            for (GoodsType goodsType : GoodsType.values()) {
                                goodsType.setCount(100);
                                goodsType.save();
                            }
                            heroN.setPetSize(heroN.getPetSize() + 10);
                            heroN.setPetRate(0.001f);
                            heroN.addMaterial(10000000);
                            heroN.addPoint(100000);
                            heroN.setLockBox(heroN.getLockBox() + 1000);
                            heroN.setKeyCount(heroN.getKeyCount() + 1000);
                            heroN.setMaxMazeLev(heroN.getMaxMazeLev() + 101);
                            maze.setLevel(99);
                            Achievement.dragon.enable(heroN);
                            SkillFactory.getSkill("虚无吞噬", heroN).setActive(true);
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
                            name = name.replaceAll("'", "`");
                            heroN.setName(name);
                            refreshCharacterName();
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


    private void showAwardPet(String title) {
        final Pet pet = new Pet();
        int[] indexs = {37, 42, 24, 26, 27, 29, 30, 39, 98};
        int i = heroN.getRandom().nextInt(indexs.length);
        int index = indexs[i];
        Cursor cursor = dbHelper.excuseSOL("select atk, hp, type, egg_rate, image, catch from monster where id = '" + index + "'");
        pet.setType(cursor.getString(cursor.getColumnIndex("type")));
        pet.setAtk(StringUtils.toLong(cursor.getString(cursor.getColumnIndex("atk"))) + heroN.getBaseAttackValue() / 200 + 20);
        long hp = StringUtils.toLong(cursor.getString(cursor.getColumnIndex("hp"))) / 2;
        cursor.close();
        pet.setDef(hp + heroN.getBaseDefense() / 200 + 10);
        pet.setHp(hp + heroN.getRealHP() / 500 + 10);
        pet.setAtk_rise(heroN.ATR_RISE);
        pet.setHp_rise(heroN.MAX_HP_RISE);
        pet.setDef_rise(heroN.DEF_RISE);
        if (heroN.getElement() != Element.无) {
            pet.setElement(heroN.getElement());
        } else {
            pet.setElement(Element.values()[heroN.getRandom().nextInt(Element.values().length)]);
        }
        String[] secondName = {"红色", "绿色", "普通", "臭臭"};
        pet.setName("奖励的" + secondName[heroN.getRandom().nextInt(4)] + pet.getType());
        pet.setSex(heroN.getRandom().nextInt(2));
        pet.setSkill(PetSkillList.getRandomSkill(heroN.getRandom(), pet, 0, 8));
        pet.setLev(maze.getLev());
        pet.setIntimacy(0l);
        pet.setOwner(heroN.getName());
        pet.setOwnerId(heroN.getUuid());
        PetDB.save(pet);
        AlertDialog dialog = new Builder(this).create();
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
        dialog.setTitle(title);
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
                "    <B>勇者之击、闪避、反弹、原能力、重击、魔王天赋、闪电、水波、多重攻击、欺诈游戏、虚无吞噬、龙爪、沙尘</B><br>" +
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


    private void checkPalaceSort() {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("正在校验殿堂排名");
        progressDialog.show();
        BmobQuery<PalaceObject> query = new BmobQuery<PalaceObject>();
        query.setLimit(5);
        query.order("-lev");
        query.findObjects(context, new FindListener<PalaceObject>() {
            @Override
            public void onSuccess(List<PalaceObject> palaceObjects) {
                if (!palaceObjects.isEmpty()) {
                    boolean award = false;
                    for (int i = 0; i < palaceObjects.size(); i++) {
                        PalaceObject palaceObject = palaceObjects.get(i);
                        if (heroN.getUuid().equals(palaceObject.getUuid())) {
                            if (!StringUtils.isNotEmpty(palaceObject.getAward())) {
                                handler.sendEmptyMessage(117);
                                award = true;
                            } else {
                                Message message = new Message();
                                message.what = 113;
                                message.obj = palaceObject;
                                message.arg1 = i + 1;
                                handler.sendMessage(message);
                                award = true;
                            }
                            break;
                        }
                    }
                    if (!award) {//检查最后一名
                        BmobQuery<PalaceObject> query = new BmobQuery<PalaceObject>();
                        query.setLimit(1);
                        query.order("lev");
                        query.findObjects(context, new FindListener<PalaceObject>() {

                            @Override
                            public void onSuccess(List<PalaceObject> palaceObjects) {
                                boolean award = false;
                                if (!palaceObjects.isEmpty()) {
                                    PalaceObject palaceObject = palaceObjects.get(0);
                                    if (heroN.getUuid().equals(palaceObject.getUuid())) {
                                        if (!StringUtils.isNotEmpty(palaceObject.getAward())) {
                                            handler.sendEmptyMessage(117);
                                            award = true;
                                        } else {
                                            Message message = new Message();
                                            message.what = 113;
                                            message.obj = palaceObject;
                                            message.arg1 = 0;
                                            handler.sendMessage(message);
                                            award = true;
                                        }
                                    }

                                }
                                if (!award) {//检查是否进入殿堂
                                    BmobQuery<PalaceObject> query = new BmobQuery<PalaceObject>();
                                    query.setLimit(1);
                                    query.addWhereEqualTo("uuid", heroN.getUuid());
                                    query.order("lev");
                                    query.findObjects(context, new FindListener<PalaceObject>() {
                                        @Override
                                        public void onSuccess(List<PalaceObject> palaceObjects) {
                                            boolean award = false;
                                            if (!palaceObjects.isEmpty()) {
                                                PalaceObject palaceObject = palaceObjects.get(0);
                                                if (heroN.getUuid().equals(palaceObject.getUuid())) {
                                                    if (!StringUtils.isNotEmpty(palaceObject.getAward())) {
                                                        handler.sendEmptyMessage(117);
                                                        award = true;
                                                    } else {
                                                        Message message = new Message();
                                                        message.what = 118;
                                                        message.obj = palaceObject;
                                                        message.arg1 = 0;
                                                        handler.sendMessage(message);
                                                        award = true;
                                                    }
                                                }
                                            }
                                            if (!award) {
                                                handler.sendEmptyMessage(115);
                                            }
                                        }

                                        @Override
                                        public void onError(int i, String s) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onError(int i, String s) {
                                handler.sendEmptyMessage(114);
                            }
                        });
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onError(int i, String s) {
                handler.sendEmptyMessage(114);
                progressDialog.dismiss();
            }
        });
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
        dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "领取奖励",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkPalaceSort();
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
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
    }


    private void updatePalace(){
        updatePalace = true;
        new Thread() {
            @Override
            public void run() {
                PalaceObject.updatePalace(context);
            }
        }.start();

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("正在更新殿堂数据");
        progressDialog.show();
        new Thread() {
            @Override
            public void run() {
                int count = 0;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    LogHelper.logException(e);
                    e.printStackTrace();
                }
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

    private void showRecipe() {
        AlertDialog dialog = new Builder(this).create();
        RecipeAdapter recipeAdapter = new RecipeAdapter();
        dialog.setTitle("已发现的配方列表 " + (recipeAdapter.getCount() * 100 / Recipe.getTotalCount()) + "%");
        ListView listView = new ListView(this);
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
        textView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
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

        View mainLayout = findViewById(R.id.main_game_activity);

        // 左侧战斗信息
        mainInfoSv = (ScrollView) findViewById(R.id.main_info_sv);
        mainInfoPlatform = (LinearLayout) findViewById(R.id.main_info_ll);
        mainInfoPlatform.setOnLongClickListener(this);
        // ---- ---- 标题（人物名称 | 最深迷宫数)
        itembarContri = (TextView) findViewById(R.id.character_itembar_contribute);
        characterName = (TextView) findViewById(R.id.character_name);
        characterName.setOnClickListener(this);
        itembarContri.setOnClickListener(this);
        mainRightDown = findViewById(R.id.character_item_contribute);
        mainLeftUp = findViewById(R.id.linearLayout2);
        mainLeftDown = mainInfoPlatform;
        onUsedPetLists = (TextView) findViewById(R.id.onuse_pet_list);
        try {
            Bitmap bitmap = MazeContents.loadImageFromSD("maze_bak.png");
            if (bitmap != null) {
                BitmapDrawable bitmapDrawable = new BitmapDrawable(this.getResources(), bitmap);
                //noinspection deprecation
                mainLayout.setBackgroundDrawable(bitmapDrawable);
            }
            itembarContri.setBackgroundColor(Color.parseColor(heroN.getTitleColor()));
            mainLeftUp.setBackgroundColor(Color.parseColor(heroN.getLeftUpColor()));
            onUsedPetLists.setBackgroundColor(Color.parseColor(heroN.getLeftUpColor()));
            mainLeftDown.setBackgroundColor(Color.parseColor(heroN.getLeftDownColor()));
            mainRightDown.setBackgroundColor(Color.parseColor(heroN.getRightDownColor()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // ---- ---- 属性
        mainContriHp = (TextView) findViewById(R.id.main_contri_hp);
        mainContriAtt = (TextView) findViewById(R.id.main_contri_att);
        mainContriDef = (TextView) findViewById(R.id.main_contri_def);
        mainContriLife = (TextView) findViewById(R.id.life_value);
        mainContriStr = (TextView) findViewById(R.id.str_value);
        mainContriAgi = (TextView) findViewById(R.id.agi_value);
        swordLev = (TextView) findViewById(R.id.sword_level);
        armorLev = (TextView) findViewById(R.id.armor_level);
        heroPointValue = (TextView) findViewById(R.id.hero_points);
        mainContriCurMaterial = (TextView) findViewById(R.id.hero_material_count);
        heroPic = (Button) findViewById(R.id.hero_pic);
        heroPic.setOnClickListener(this);
        pauseButton = (Button) findViewById(R.id.push_button);
        pauseButton.setOnClickListener(this);
        clickCount = (TextView) findViewById(R.id.hero_pic_click_count);
        firstSkillButton = (Button) findViewById(R.id.first_skill);
        firstSkillButton.setOnClickListener(this);
        secondSkillButton = (Button) findViewById(R.id.secondary_skill);
        secondSkillButton.setOnClickListener(this);
        thirdSkillButton = (Button) findViewById(R.id.third_skill);
        thirdSkillButton.setOnClickListener(this);
        fourthSkillButton = (Button) findViewById(R.id.fourth_skill);
        fourthSkillButton.setOnClickListener(this);
        fifthSkillButton = (Button) findViewById(R.id.fifit_skill);
        fifthSkillButton.setOnClickListener(this);
        sixthSkillButton = (Button) findViewById(R.id.sixth_skill);
        sixthSkillButton.setOnClickListener(this);
        saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(this);
        lockBoxCount = (TextView) findViewById(R.id.local_box);
        lockBoxCount.setOnClickListener(this);
        keyCount = (TextView) findViewById(R.id.key_count);
        reniButton = (Button) findViewById(R.id.rein_button);
        reniButton.setOnClickListener(this);
        ringTextView = (TextView) findViewById(R.id.ring_view);
        ringTextView.setOnClickListener(this);
        necklaceTextView = (TextView) findViewById(R.id.necklace_view);
        necklaceTextView.setOnClickListener(this);
        hatTextView = (TextView) findViewById(R.id.hat_view);
        hatTextView.setOnClickListener(this);
        colButton = (Button) findViewById(R.id.col_button);
        colButton.setOnClickListener(this);
        refreshCharacterName();
        refresh();
    }

    public void refreshCharacterName() {
        handler.sendEmptyMessage(123);
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
        mainContriLife.setText(StringUtils.formatNumber(heroN.getPower()));
        mainContriStr.setText(StringUtils.formatNumber(heroN.getStrength()));
        mainContriAgi.setText(StringUtils.formatNumber(heroN.getAgility()));
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

        itembarContri.setText("迷宫到达(当前/记录）层\n" + maze.getLev() + "/" + heroN.getMaxMazeLev());
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
        if (heroN.isFourthSkillEnable()) {
            if (heroN.getFourthSkill() != null) {
                fourthSkillButton.setText(heroN.getFourthSkill().getDisplayName());
                fourthSkillButton.setEnabled(true);
            } else {
                fourthSkillButton.setText("");
                fourthSkillButton.setEnabled(false);
            }
        }else{
            fourthSkillButton.setEnabled(false);
        }
        if (heroN.isFifitSkillEnable()) {
            if (heroN.getFifthSkill() != null) {
                fifthSkillButton.setText(heroN.getFifthSkill().getDisplayName());
                fifthSkillButton.setEnabled(true);
            } else {
                fifthSkillButton.setText("");
                fifthSkillButton.setEnabled(false);
            }
        }else{
            fifthSkillButton.setEnabled(false);
        }
        if (heroN.isSixthSkillEnable()) {
            if (heroN.getSixthSkill() != null) {
                sixthSkillButton.setText(heroN.getSixthSkill().getDisplayName());
                sixthSkillButton.setEnabled(true);
            } else {
                sixthSkillButton.setText("");
                sixthSkillButton.setEnabled(false);
            }
        }else{
            sixthSkillButton.setEnabled(false);
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

        StringBuilder pets = new StringBuilder();
        for (Pet pet : heroN.getPets()) {
            pets.append(pet.getFormatName()).append("<br>");
        }
        onUsedPetLists.setText(Html.fromHtml(pets.toString()));
        if ((heroN.getReincaCount() + 1) % 1000 == 0) {
            reniButton.setEnabled(true);
        } else {
            reniButton.setEnabled(false);
        }
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

    public void setHidBattle(boolean isHidBattle) {
        this.isHidBattle = isHidBattle;
    }

    private class GameThread extends Thread {

        @Override
        public void run() {
            try {
                new MoveThread().start();
                if (heroN.getAwardCount() < 3000 && alipay.getPayTime() > 0) {
                    handler.sendEmptyMessage(111);
                } else if (heroN.getAwardCount() == 0) {
                    showFirstIn();
                }
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

    private void showFirstIn() {
        handler.sendEmptyMessage(112);
    }

    public void showGiftChoose() {
        GiftDialog dialog = new GiftDialog(context);
        dialog.show();
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
            case R.id.ji_neng_button:
                SkillMainDialog skillMainDialog = new SkillMainDialog();
                skillMainDialog.show();
                break;
            case R.id.add_li_button:
                final AddPointDialog addStrPointDialog = new AddPointDialog(context);
                addStrPointDialog.setTitle("力量");
                addStrPointDialog.show(new OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               heroN.addStrength();
                                               handler.sendEmptyMessage(0);
                                               heroN.click(false);
                                               addStrPointDialog.refresh();
                                           }
                                       }, new OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               long str = heroN.getRandom().nextLong(heroN.getPoint() + 1);
                                               heroN.addStrength(str);
                                               heroN.addPoint(-str);
                                               handler.sendEmptyMessage(0);
                                               addStrPointDialog.refresh();
                                           }
                                       }, R.drawable.h_4
                );
                break;
            case R.id.add_ti_button:
                final AddPointDialog addLifePointDialog = new AddPointDialog(context);
                addLifePointDialog.setTitle("体力");
                addLifePointDialog.show(new OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                heroN.addLife();
                                                handler.sendEmptyMessage(0);
                                                heroN.click(false);
                                                addLifePointDialog.refresh();
                                            }
                                        }, new OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                long life = heroN.getRandom().nextLong(heroN.getPoint() + 1);
                                                heroN.addLife(life);
                                                heroN.addPoint(-life);
                                                handler.sendEmptyMessage(0);
                                                heroN.click(false);
                                                addLifePointDialog.refresh();
                                            }
                                        }, R.drawable.h_4
                );
                break;
            case R.id.add_min_button:
                final AddPointDialog addAgiPointDialog = new AddPointDialog(context);
                addAgiPointDialog.setTitle("敏捷");
                addAgiPointDialog.show(new OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               heroN.addAgility();
                                               handler.sendEmptyMessage(0);
                                               heroN.click(false);
                                               addAgiPointDialog.refresh();
                                           }
                                       }, new OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               long agi = heroN.getRandom().nextLong(heroN.getPoint() + 1);
                                               heroN.addAgility(agi);
                                               heroN.addPoint(-agi);
                                               handler.sendEmptyMessage(0);
                                               heroN.click(false);
                                               addAgiPointDialog.refresh();
                                           }
                                       }, R.drawable.h_4
                );
                break;
            case R.id.fenpei_button:
                AlertDialog fenpeiDialog = new Builder(context).create();
                fenpeiDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                View shuxunView = View.inflate(context, R.layout.shu_xin_fen_pei, (ViewGroup) context.findViewById(R.id.shu_xin_den_pei_root));
                fenpeiDialog.setView(shuxunView);
                fenpeiDialog.show();
                //fixDialogSize(fenpeiDialog, 320, 300);
                fenpeiDialog.findViewById(R.id.ji_neng_button).setOnClickListener(this);
                fenpeiDialog.findViewById(R.id.add_li_button).setOnClickListener(this);
                fenpeiDialog.findViewById(R.id.add_min_button).setOnClickListener(this);
                fenpeiDialog.findViewById(R.id.add_ti_button).setOnClickListener(this);
                break;
            case R.id.wu_pin_button:
                GoodsDialog goodsDialog = new GoodsDialog(context);
                goodsDialog.show();
                break;
            case R.id.chong_wu_button:
                saveHelper.savePet();
                new PetDialog(context).show(heroN);
                break;
            case R.id.accessory_button:
                showAccessory();
                break;
            case R.id.bag_button:
                AlertDialog bagDialog = new Builder(context).create();
                bagDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                View bagView = View.inflate(context, R.layout.bag_layout, (ViewGroup) context.findViewById(R.id.bag_root));
                bagDialog.setView(bagView);
                bagDialog.show();
                //fixDialogSize(bagDialog, 200, 200);
                bagDialog.findViewById(R.id.chong_wu_button).setOnClickListener(this);
                bagDialog.findViewById(R.id.build_button).setOnClickListener(this);
                bagDialog.findViewById(R.id.wu_pin_button).setOnClickListener(this);
                bagDialog.findViewById(R.id.accessory_button).setOnClickListener(this);
                break;
            case R.id.dian_button://殿堂
                updatePalace();
                break;
            case R.id.shang_button://商店
                ShopDialog shopDialog = new ShopDialog(context);
                shopDialog.show();
                break;
            case R.id.swap_pet_button://换宠
                try {
                    new SwapPetMainDialog();
                } catch (Exception e) {
                    LogHelper.logException(e);
                }
                break;
            case R.id.net_button:
                AlertDialog netDialog = new Builder(context).create();
                netDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                View netView = View.inflate(context, R.layout.liang_wang, (ViewGroup) context.findViewById(R.id.lian_wang_root));
                netDialog.setView(netView);
                netDialog.show();
                //fixDialogSize(netDialog, 200, 200);
                View shopButton = netDialog.findViewById(R.id.shang_button);
                shopButton.setOnClickListener(this);
                if (maze.isSailed()) {
                    shopButton.setBackgroundResource(R.drawable.yuanhuan_bak);
                } else {
                    shopButton.setBackgroundColor(R.color.toumin);
                }
                netDialog.findViewById(R.id.dian_button).setOnClickListener(this);
                netDialog.findViewById(R.id.swap_pet_button).setOnClickListener(this);
                break;
            case R.id.zan_zu_button:
                showPayDialog();
                break;
            case R.id.upload_button:
                if (!uploading) {
                    if (MazeContents.lastUpload + 100 <= heroN.getMaxMazeLev()) {
                        showUpload();
                    } else {
                        AlertDialog uploadAlert = new Builder(context).create();
                        uploadAlert.setMessage("还差" + (100 - heroN.getMaxMazeLev() + MazeContents.lastUpload) + "层");
                        uploadAlert.setButton(DialogInterface.BUTTON_NEGATIVE, "继续努力", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        uploadAlert.show();
                    }
                }
                break;
            case R.id.update_button:
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
                                    save();
                                    break;
                            }
                        } catch (Exception e) {
                            LogHelper.logException(e);
                        }
                    }
                });
                BmobUpdateAgent.forceUpdate(context);
                break;
            case R.id.help_button:
                AlertDialog helpDialog = new Builder(context).create();
                helpDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                View helpView = View.inflate(context, R.layout.zan_zu_layout, (ViewGroup) context.findViewById(R.id.zan_zu_root));
                helpDialog.setView(helpView);
                helpDialog.show();
                //fixDialogSize(helpDialog, 200, 200);
                helpDialog.findViewById(R.id.zan_zu_button).setOnClickListener(this);
                helpDialog.findViewById(R.id.upload_button).setOnClickListener(this);
                helpDialog.findViewById(R.id.update_button).setOnClickListener(this);
                break;
            case R.id.wuxin_up:
                showRecipe();
                break;
            case R.id.wuxin_left:
                MonsterBook book = new MonsterBook(context);
                book.showBook();
                break;
            case R.id.wuxin_right:
                showAchievement();
                break;
            case R.id.wuxin_down_left:
                //npc
                break;
            case R.id.wuxin_down_right:
                //?
                break;
            case R.id.col_button:
                AlertDialog colDialog = new Builder(context).create();
                View colView = View.inflate(context, R.layout.collection_layout, (ViewGroup) context.findViewById(R.id.collection_root));
                colDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                colDialog.setView(colView);
                colDialog.show();
                //fixDialogSize(colDialog, 233, 233);
                View wxu = colDialog.findViewById(R.id.wuxin_up);
                if (Recipe.getCurrentCount() * 100 / Recipe.getTotalCount() > 80) {
                    wxu.setBackgroundResource(R.drawable.wuxin_up);
                } else {
                    wxu.setBackgroundColor(R.color.toumin);
                }
                wxu.setOnClickListener(this);
                View wxl = colDialog.findViewById(R.id.wuxin_left);
                wxl.setOnClickListener(this);
                if (MonsterBook.getCurrentCount() * 100 / MonsterBook.getTotalCount() > 60) {
                    wxl.setBackgroundResource(R.drawable.wuxin_left);
                } else {
                    wxl.setBackgroundColor(R.color.toumin);
                }
                View wxr = colDialog.findViewById(R.id.wuxin_right);
                wxr.setOnClickListener(this);
                if (Achievement.getCurrentCount() * 100 / Achievement.getTotalCount() > 90) {
                    wxr.setBackgroundResource(R.drawable.wuxin_right);
                } else {
                    wxr.setBackgroundColor(R.color.toumin);
                }
                View wxdl = colDialog.findViewById(R.id.wuxin_down_left);
                wxdl.setOnClickListener(this);
                View wxdr = colDialog.findViewById(R.id.wuxin_down_right);
                wxdr.setOnClickListener(this);
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
            case R.id.rein_button:
                showReincarnationDialog();
                handler.sendEmptyMessage(0);
                break;
            case R.id.local_box:
                boolean count = Item.getItemCount() < 1000;
                if (heroN.getKeyCount() > 0 && heroN.getLockBox() > 0 && count) {
                    heroN.setKeyCount(heroN.getKeyCount() - 1);
                    showLockBox();
                    heroN.click(false);
                } else if (!count) {
                    showItemFull();
                }
                break;
            case R.id.ring_view:
            case R.id.necklace_view:
            case R.id.hat_view:
                showAccessory();
                break;
            case R.id.build_button:
                handler.sendEmptyMessage(103);
                Intent intent = new Intent(this, ForgeActivity.class);
                startActivity(intent);
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
            case R.id.fourth_skill:
                if (heroN.getFourthSkill() != null) {
                    heroN.getFourthSkill().addCount();
                }
                handler.sendEmptyMessage(0);
                break;
            case R.id.fifit_skill:
                if (heroN.getFifthSkill() != null) {
                    heroN.getFifthSkill().addCount();
                }
                handler.sendEmptyMessage(0);
                break;
            case R.id.sixth_skill:
                if (heroN.getSixthSkill() != null) {
                    heroN.getSixthSkill().addCount();
                }
                handler.sendEmptyMessage(0);
                break;
            case R.id.character_name:
            case R.id.character_itembar_contribute:
                showNameDialog();
                heroN.click(false);
                break;
            case R.id.push_button:
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

    private void fixDialogSize(AlertDialog colDialog, int width, int height) {
        WindowManager.LayoutParams params =

                colDialog.getWindow().getAttributes();

        params.width = ScreenUtils.getRawSize(TypedValue.COMPLEX_UNIT_DIP, width, context);

        params.height = ScreenUtils.getRawSize(TypedValue.COMPLEX_UNIT_DIP, height, context);

        colDialog.getWindow().setAttributes(params);
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
                loadHelper.loadHero();
                loadHelper.loadSkill(MazeContents.hero);
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
                //noinspection deprecation
                holder.name.setTextColor(convertView.getResources().getColor(R.color.disable));
            } else {
                //noinspection deprecation
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
                //noinspection deprecation
                holder.name1.setTextColor(convertView.getResources().getColor(R.color.disable));
            } else {
                //noinspection deprecation
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
                //noinspection deprecation
                holder.name2.setTextColor(convertView.getResources().getColor(R.color.disable));
            } else {
                //noinspection deprecation
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
                //noinspection deprecation
                holder.name3.setTextColor(convertView.getResources().getColor(R.color.disable));

            } else {
                //noinspection deprecation
                holder.name3.setTextColor(convertView.getResources().getColor(R.color.onUse));
            }
            return convertView;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

}
