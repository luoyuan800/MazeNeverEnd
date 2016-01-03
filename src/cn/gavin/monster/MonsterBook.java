package cn.gavin.monster;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.gavin.R;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.db.DBHelper;
import cn.gavin.utils.StringUtils;
import cn.gavin.utils.ui.WheelView;

/**
 * gluo on 9/8/2015.
 */
public class MonsterBook {
    private final TextView defeatCount;
    private final TextView beatCount;
    private final TextView firstCatch;
    private final TextView firstMeet;
    private final TextView baseEggRate;
    private final TextView basePetRate;
    private final TextView basehp;
    private final TextView baseAtk;
    private final TextView monsterDesc;
    private final TextView monsterName;
    private final ImageView monsterImg;
    private MainGameActivity context;
    private AlertDialog alertDialog;
    private WheelView wheelView;
    private List<Monster> monsters;


    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    int i = wheelView.getSelectedPosition();
                    Monster monster = monsters.get(i);
                    if (monster.getCatchCount() > 0) {
                        monsterName.setText(monster.getType());
                        monsterDesc.setText(monster.getDesc());
                        monsterImg.setImageResource(monster.getImageId());
                        baseAtk.setText("基础攻击：" + StringUtils.formatNumber(monster.getBaseAtk()));
                        basehp.setText("基础HP：" + StringUtils.formatNumber(monster.getBaseHp()));
                        baseEggRate.setText("生蛋率：" + monster.getBaseEggRate());
                        basePetRate.setText("捕获率：" + monster.getBasePetRate());
                    } else {
                        monsterDesc.setText("???");
                        monsterImg.setImageResource(R.drawable.wenhao);
                        baseAtk.setText("基础攻击：???");
                        basehp.setText("基础HP：???");
                        baseEggRate.setText("生蛋率：??");
                        basePetRate.setText("捕获率：??");
                    }
                    if (monster.getMeet_lev() > 0) {
                        monsterName.setText(monster.getType());
                    } else {
                        monsterName.setText("???");
                    }
                    defeatCount.setText("你击败了它" + StringUtils.formatNumber(monster.getDefeatCount()) + "次");
                    beatCount.setText("它击败了你" + StringUtils.formatNumber(monster.getBeatCount()) + "次");
                    if (monster.getCatch_lev() > 0) {
                        firstCatch.setText("第一次捕获在第" + StringUtils.formatNumber(monster.getCatch_lev()) + "层");
                    } else {
                        firstCatch.setText("还未成功捕获过");
                    }
                    if (monster.getMeet_lev() > 0) {
                        firstMeet.setText("第一次遇见在第" + StringUtils.formatNumber(monster.getMeet_lev()) + "层");
                    } else {
                        firstMeet.setText("还未在迷宫中遇见过");
                    }
                    break;
            }
        }
    };

    public MonsterBook(MainGameActivity context) {
        this.context = context;
        alertDialog = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.new_monster_detail, (ViewGroup) context.findViewById(R.id.monster_book_new));
        monsterName = (TextView) view.findViewById(R.id.name_text);
        monsterDesc = (TextView) view.findViewById(R.id.desc_text);
        baseAtk = (TextView) view.findViewById(R.id.base_atk);
        basehp = (TextView) view.findViewById(R.id.base_hp);
        basePetRate = (TextView) view.findViewById(R.id.base_pet_rate);
        baseEggRate = (TextView) view.findViewById(R.id.base_egg_rate);
        firstMeet = (TextView) view.findViewById(R.id.first_meet_lev);
        firstCatch = (TextView) view.findViewById(R.id.first_catch_lev);
        beatCount = (TextView) view.findViewById(R.id.beat_count_text);
        defeatCount = (TextView) view.findViewById(R.id.defeat_count_text);
        monsterImg = (ImageView) view.findViewById(R.id.head_png);
        wheelView = (WheelView) view.findViewById(R.id.monster_name_list);
        List<Monster> allMonster = MonsterDB.loadMonster();
        monsters = new ArrayList<Monster>(allMonster.size());
        List<String> names = new ArrayList<String>(allMonster.size());
        int knownCount = 0;
        for (Monster monster : allMonster) {
            if (monster.getMeet_lev() > 0) {
                names.add(monster.getType());
                monsters.add(monster);
                knownCount++;
            }
        }
        wheelView.setItems(names);
        wheelView.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onWheelItemChanged(WheelView wheelView, int position) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onWheelItemSelected(WheelView wheelView, int position) {

            }
        });
        alertDialog.setView(view);
        alertDialog.setTitle("怪物图鉴 " + (knownCount/allMonster.size() * 100) + "%");
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "退出", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
    }

    public void showBook() {
        alertDialog.show();
        handler.sendEmptyMessage(0);
    }

    public static int getCurrentCount() {
        Cursor cursor = DBHelper.getDbHelper().excuseSOL("select * from monster where catch > 0");
        int count = 0;
        if(!cursor.isAfterLast()){
            count = cursor.getInt(0);
            cursor.close();
        }
        return count;
    }

    public static int getTotalCount() {
        int count = 0;
        Cursor cursor = DBHelper.getDbHelper().excuseSOL("select count(*) from monster");
        if(!cursor.isAfterLast()){
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }
}
