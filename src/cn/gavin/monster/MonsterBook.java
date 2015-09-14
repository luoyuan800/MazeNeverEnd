package cn.gavin.monster;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.gavin.R;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.db.DBHelper;

/**
 * gluo on 9/8/2015.
 */
public class MonsterBook {
    private MainGameActivity context;
    private Set<String> nameKeys;
    private Set<String> nameSet;

    public MonsterBook(MainGameActivity context) {
        this.context = context;
    }

    public void showBook() {
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.monster_book, (ViewGroup) context.findViewById(R.id.monster_book));
        ListView list = (ListView) view.findViewById(R.id.monster_book_list);
        TextView text = (TextView) view.findViewById(R.id.monster_book_text);
        list.setAdapter(new MonsterAdapter(text));
        dialog.setView(view);
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "退出", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setTitle("怪物收集");
        dialog.show();
    }

    public void addMonster(Monster monster) {
        if (nameKeys == null) {
            nameKeys = getMonsterNameKeys();
        }
        String key = monster.getName() + "_" + monster.isDefeat();
        if (!nameKeys.contains(key)) {
            nameKeys.add(key);
            writeIntoDB(monster);
        }
    }

    private void writeIntoDB(Monster monster) {
        DBHelper helper = context.getDbHelper();
        String sql = String.format("insert into monster_book (name, format_name, isDefeat, %s, %s, %s) values('%s', '%s', '%s', '%s', '%s', %s)",
                monster.isDefeat() ? "hp1" : "hp", monster.isDefeat() ? "atk1" : "atk", monster.isDefeat() ? "maze_lv1" : "maze_lv",
                monster.getName(), monster.getFormatName(), monster.isDefeat(), monster.getMaxHP(), monster.getAtk(), monster.getMazeLev());
        helper.excuseSQLWithoutResult(sql);
    }

    public Set<String> getMonsterNameKeys() {
        Set<String> keys = new HashSet<String>();
        nameSet = new HashSet<String>();
        DBHelper helper = context.getDbHelper();
        String sql = "select name, isDefeat from monster_book";
        Cursor cursor = helper.excuseSOL(sql);
        while (!cursor.isAfterLast()) {
            String key = cursor.getString(cursor.getColumnIndex("name")) + "_" + cursor.getString(cursor.getColumnIndex("isDefeat"));
            keys.add(key);
            nameSet.add(cursor.getString(cursor.getColumnIndex("name")));
            cursor.moveToNext();
        }
        nameKeys = keys;
        return keys;
    }

    public static class MonsterList {
        MonsterItem a0;
        MonsterItem a1;
        MonsterItem a2;

        public MonsterList(MonsterItem a0, MonsterItem a1, MonsterItem a2) {
            this.a0 = a0;
            this.a1 = a1;
            this.a2 = a2;
        }

        public MonsterList() {
            a0 = null;
            a1 = null;
            a2 = null;
        }

        public void addMonster(MonsterItem item) {
            if (a0 == null) {
                a0 = item;
            } else if (a1 == null) {
                a1 = item;
            } else {
                a2 = item;
            }
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

        public MonsterView(Button b, final TextView textView) {
            button = b;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    textView.setText(Html.fromHtml(monster.getDesc()));
                }
            });
        }

        public void updateMonster(MonsterItem m) {
            monster = m;
            if (!monster.isDefeat()) {
                button.setEnabled(false);
            } else {
                button.setEnabled(true);
            }
            button.setText(monster.getName());
        }
    }

    class MonsterAdapter extends BaseAdapter {
        private List<MonsterList> adapterData;
        private TextView textView;

        public MonsterAdapter(TextView textView) {
            this.textView = textView;
            adapterData = new ArrayList<MonsterList>();
            getMonsterNameKeys();
            List<String> nameList = new ArrayList<String>(nameSet);
            for (int i = 0; i < nameList.size(); i += 3) {
                MonsterList monsterList = new MonsterList();
                for (int j = i; j < i + 3; j++) {
                    MonsterItem item = MonsterItem.EMPTY_MONSTER;
                    if (j < nameList.size()) {
                        String name = nameList.get(j);
                        boolean isDefeat = false;
                        if (nameKeys.contains(name + "_true")) {
                            isDefeat = true;
                        }
                        item = new MonsterItem(name, isDefeat, context.getDbHelper());
                    }
                    monsterList.addMonster(item);
                }
                adapterData.add(monsterList);
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
                holder.name = new MonsterView((Button) convertView.findViewById(R.id.monster_name), textView);

                holder.name1 = new MonsterView((Button) convertView.findViewById(R.id.monster_name_1), textView);

                holder.name2 = new MonsterView((Button) convertView.findViewById(R.id.monster_name_2), textView);
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

    private static class MonsterItem {
        private Boolean defeat;
        private String desc;
        private String name;
        private DBHelper helper;

        private void load() {
            String sql = String.format("select * from monster_book where name = '%s'", name);
            Cursor cursor = helper.excuseSOL(sql);
            StringBuilder sb = new StringBuilder();
            while (!cursor.isAfterLast()) {
                sb.append(cursor.getString(cursor.getColumnIndex("format_name")));
                sb.append("--(");
                boolean isDefeat = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("isDefeat")));
                sb.append(isDefeat ? "打败" : "相遇").append(")");
                sb.append("--在第").append(cursor.getString(cursor.getColumnIndex(isDefeat ? "maze_lv1" : "maze_lv"))).append("层");
                sb.append("<br>生命值：").append(cursor.getString(cursor.getColumnIndex(isDefeat ? "hp1" : "hp")));
                sb.append("<br>攻击力：").append(cursor.getString(cursor.getColumnIndex(isDefeat ? "atk1" : "atk")));
                sb.append("<br>**************<br>");
                cursor.moveToNext();
            }
            desc = sb.toString();
        }

        public boolean isDefeat() {
            return defeat;
        }

        public String getDesc() {
            if (desc == null) {
                load();
            }
            return desc;
        }

        public String getName() {
            return name;
        }

        public MonsterItem(String name, boolean defeat, DBHelper helper) {
            this.name = name;
            this.defeat = defeat;
            this.helper = helper;
        }

        public final static MonsterItem EMPTY_MONSTER = new MonsterItem("", false, null) {
            public boolean isDefeat() {
                return false;
            }

            public String getDesc() {
                return "";
            }

            public String getName() {
                return "";
            }
        };
    }
}
