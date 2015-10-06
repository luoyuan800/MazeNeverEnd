package cn.gavin.monster;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import cn.gavin.R;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * gluo on 9/8/2015.
 */
public class MonsterBook {
    private Context context;
    private AlertDialog alertDialog;
    private MonsterDetailDialog detailDialog;

    public MonsterBook(Context context) {
        this.context = context;
        detailDialog = new MonsterDetailDialog(context);
    }

    public void showBook(MainGameActivity context) {
        initView(context);
        alertDialog.show();
    }

    public void addMonster(Monster monster) {
        MonsterItem item = new MonsterItem();
        int index = Monster.getIndex(monster.getName());
        String name = "";
        if (index < Monster.lastNames.length) {
            name = Monster.lastNames[index];
        }else{
            if(monster.getName().endsWith("守护者")){
                name = "守护者";
            }else{
                name = monster.getName();
            }
        }
            item.setName(name);
            item.load();
            long atk = StringUtils.isNotEmpty(item.getMaxATKATK()) ? Long.parseLong(item.getMaxATKATK()) : 0;
            long hp = StringUtils.isNotEmpty(item.getMaxHPHP()) ? Long.parseLong(item.getMaxHPHP()) : 0;
            if (monster.getAtk() > atk) {
                item.setMaxATKName(monster.getFormatName());
                item.setMaxATKATK(monster.getAtk() + "");
                item.setMaxATKHP(monster.getMaxHP() + "");
                item.setMaxATKLev(monster.getMazeLev() + "");
                item.setMaxATKDefeat(monster.isDefeat());
                item.setMaxATKDesc(monster.getBattleMsg());
            }
            if (monster.getHp() > hp) {
                item.setMaxHPName(monster.getFormatName());
                item.setMaxHPATK(monster.getAtk() + "");
                item.setMaxHPHP(monster.getMaxHP() + "");
                item.setMaxHPLev(monster.getMazeLev() + "");
                item.setMaxHPDefeat(monster.isDefeat());
                item.setMaxHPDesc(monster.getBattleMsg());
            }
            if (monster.isDefeat()) {
                item.setDefeat(item.getDefeat() + 1);
            } else {
                item.setDefeated(item.getDefeated() + 1);
            }
            item.save();
    }

    public void initView(MainGameActivity context) {
        this.context = context;
        alertDialog = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.monster_book, (ViewGroup) context.findViewById(R.id.monster_book));
        ListView list = (ListView) view.findViewById(R.id.monster_book_list);
        TextView text = (TextView) view.findViewById(R.id.monster_book_text);
        list.setAdapter(new MonsterAdapter(context));
        alertDialog.setView(view);
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "退出", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                detailDialog.dismiss();
                alertDialog.dismiss();
            }
        });
        alertDialog.setTitle("怪物列表");
    }

    public static class MonsterList {
        MonsterItem a0;
        MonsterItem a1;
        MonsterItem a2;
        MonsterItem a3;

        public MonsterList() {
            a0 = null;
            a1 = null;
            a2 = null;
            a3 = null;
        }

        public boolean addMonster(MonsterItem name) {
            if (a0 == null) {
                a0 = name;
            } else if (a1 == null) {
                a1 = name;
            } else if (a2 == null) {
                a2 = name;
            } else if (a3 == null) {
                a3 = name;
            } else {
                return false;
            }
            return true;
        }

        public boolean full() {
            return a0 != null && a1 != null && a2 != null;
        }
    }

    static class MonsterViewHolder {
        MonsterView name;
        MonsterView name1;
        MonsterView name2;
    }

    static class MonsterView {
        Button button;
        MonsterItem monster;

        public MonsterView(Button b, final MonsterDetailDialog dialog) {
            button = b;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.show(monster);
                }
            });
        }

        public void updateMonster(MonsterItem m) {
            monster = m;
            if (monster != null) {
                button.setEnabled(true);
                button.setText(monster.getName());
            } else {
                button.setEnabled(false);
                button.setText("");
            }
        }
    }

    class MonsterAdapter extends BaseAdapter {
        private List<MonsterList> adapterData;
        private Context context;

        public MonsterAdapter(Context context) {
            this.context = context;
            List<MonsterItem> monsterItems = MonsterItem.loadMonsterItems();
            adapterData = new ArrayList<MonsterList>();
            MonsterList list = new MonsterList();
            adapterData.add(list);
            for (MonsterItem item : monsterItems) {
                if (!list.addMonster(item)) {
                    list = new MonsterList();
                    list.addMonster(item);
                    adapterData.add(list);
                }
            }
        }

        @Override
        public int getCount() {
            return adapterData.size();
        }

        @Override
        public MonsterList getItem(int position) {
            if (position >= getCount()) position = 0;
            return adapterData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            MonsterViewHolder holder;
            MonsterList item = getItem(position);
            if (convertView == null) {
                holder = new MonsterViewHolder();
                convertView = View.inflate(context,
                        R.layout.monster_book_item, null);
                holder.name = new MonsterView((Button) convertView.findViewById(R.id.monster_name), detailDialog);

                holder.name1 = new MonsterView((Button) convertView.findViewById(R.id.monster_name_1), detailDialog);

                holder.name2 = new MonsterView((Button) convertView.findViewById(R.id.monster_name_2), detailDialog);
                convertView.setTag(holder);
            } else {
                holder = (MonsterViewHolder) convertView.getTag();
            }
            holder.name.updateMonster(item.a0);
            holder.name1.updateMonster(item.a1);
            holder.name2.updateMonster(item.a2);
            return convertView;
        }
    }

}
