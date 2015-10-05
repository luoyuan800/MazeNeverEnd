package cn.gavin.monster;

import android.app.AlertDialog;
import android.content.Context;
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
import cn.gavin.R;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.db.DBHelper;
import cn.gavin.utils.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * gluo on 9/8/2015.
 */
public class MonsterBook {
    private Context context;
    private DBHelper dbHelper;
    private AlertDialog alertDialog;
    private Map<String, MonsterItem> monsterItemMap;
    private List<MonsterItem> sortItems;

    public MonsterBook(Context context) {
        this.context = context;
        monsterItemMap = new ConcurrentHashMap<String, MonsterItem>();
        sortItems = new ArrayList<MonsterItem>();
    }

    public void showBook(MainGameActivity context) {
        initView(context);
        alertDialog.show();
    }

    public void addMonster(Monster monster) {
        MonsterItem item = monsterItemMap.get(monster.getName());
        if (item == null) {
            item = new MonsterItem(monster.getName(), monster.isDefeat());
            item.count = 0;
            item.defeat = monster.isDefeat();
            if (monster.isDefeat()) {
                item.atk1 = monster.getAtk();
                item.hp1 = monster.getMaxHP();
                item.mazeLev1 = monster.getMazeLev();
            } else {
                item.atk = monster.getAtk();
                item.hp = monster.getMaxHP();
                item.mazeLev = monster.getMazeLev();
            }
            item.formatName = monster.getFormatName();
            monsterItemMap.put(monster.getName(), item);
            sortItems.add(item);
        } else if (!item.isDefeat() && monster.isDefeat()) {
            item.defeat = true;
            item.atk1 = monster.getAtk();
            item.hp1 = monster.getMaxHP();
            item.mazeLev1 = monster.getMazeLev();
        }
        item.count++;
    }

    public void writeIntoDB() {
        String baseSql = "replace into monster_book (name, format_name, isDefeat, hp, hp1,atk,atk1,maze_lv,maze_lv1, count) values('%s', '%s', '%s','%s', '%s', '%s', '%s','%s','%s','%s')";
        dbHelper.beginTransaction();
        for (MonsterItem monster : sortItems) {
            if (monster != null) {
                String sql = String.format(baseSql,
                        monster.name, monster.formatName, monster.isDefeat(), monster.hp, monster.hp1, monster.atk, monster.atk1, monster.mazeLev, monster.mazeLev1, monster.count);
                dbHelper.excuseSQLWithoutResult(sql);
            }
        }
        dbHelper.endTransaction();
    }

    public Set<String> getMonsterNameKeys() {
        String sql = "select * from monster_book";
        Cursor cursor = dbHelper.excuseSOL(sql);
        while (!cursor.isAfterLast()) {
            String fName = cursor.getString(cursor.getColumnIndex("format_name"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String hp = cursor.getString(cursor.getColumnIndex("hp"));
            String hp1 = cursor.getString(cursor.getColumnIndex("hp1"));
            String atk = cursor.getString(cursor.getColumnIndex("atk"));
            String atk1 = cursor.getString(cursor.getColumnIndex("atk1"));
            String count = cursor.getString(cursor.getColumnIndex("count"));
            String mazeLev = cursor.getString(cursor.getColumnIndex("maze_lv"));
            String mazeLev1 = cursor.getString(cursor.getColumnIndex("maze_lv1"));
            boolean isDefeat = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("isDefeat")));
            MonsterItem item = new MonsterItem(name, isDefeat);
            item.count = StringUtils.isNotEmpty(count) ? Long.parseLong(count) : 0l;
            item.atk = StringUtils.isNotEmpty(atk) ? Long.parseLong(atk) : 0l;
            item.atk1 = StringUtils.isNotEmpty(atk1) ? Long.parseLong(atk1) : 0l;
            item.hp = StringUtils.isNotEmpty(hp) ? Long.parseLong(hp) : 0l;
            item.hp1 = StringUtils.isNotEmpty(hp1) ? Long.parseLong(hp1) : 0l;
            item.mazeLev = StringUtils.isNotEmpty(mazeLev) ? Long.parseLong(mazeLev) : 0l;
            item.mazeLev1 = StringUtils.isNotEmpty(mazeLev1) ? Long.parseLong(mazeLev1) : 0l;
            item.formatName = fName;
            monsterItemMap.put(name, item);
            cursor.moveToNext();
        }
        sortItems.addAll(monsterItemMap.values());

        sort();
        return monsterItemMap.keySet();
    }

    private void sort() {
        try {
            Collections.sort(sortItems, new Comparator<MonsterItem>() {
                @Override
                public int compare(MonsterItem s, MonsterItem s2) {
                    if (s.equals(s2)) return 0;
                    int index = Monster.getIndex(s.name);
                    int index1 = Monster.getIndex(s2.name);
                    if (index > index1) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            });
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    private static MonsterBook mb;

    public static void init(Context context) {
        if (mb == null) {
            mb = new MonsterBook(context);
            mb.dbHelper = DBHelper.getDbHelper();
            mb.getMonsterNameKeys();
        } else {
            mb.context = context;
        }
    }

    public synchronized static MonsterBook getMonsterBook() {
        if (mb == null) {
            MonsterBook.init(MainGameActivity.context);
        }
        return mb;
    }

    public void initView(MainGameActivity context) {
        this.context = context;
        alertDialog = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.monster_book, (ViewGroup) context.findViewById(R.id.monster_book));
        ListView list = (ListView) view.findViewById(R.id.monster_book_list);
        TextView text = (TextView) view.findViewById(R.id.monster_book_text);
        list.setAdapter(new MonsterAdapter(text));
        alertDialog.setView(view);
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "退出", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.setTitle("怪物收集");
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
            MonsterList list = new MonsterList();
            sort();
            for (MonsterItem item : sortItems) {
                if (item == null) {
                    item = MonsterItem.EMPTY_MONSTER;
                }
                if (list.full()) {
                    adapterData.add(list);
                    list = new MonsterList();
                }
                list.addMonster(item);
            }
            while (!list.full()) {
                list.addMonster(MonsterItem.EMPTY_MONSTER);
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
        private long count;
        private long hp, hp1, atk, atk1;
        private String formatName;
        public long mazeLev;
        public long mazeLev1;

        public void addCount() {
            count++;
        }

        private void load() {
            StringBuilder sb = new StringBuilder();
            sb.append(formatName);
            sb.append("--");
            if (defeat) {
                sb.append("打败(" + count + ")");
                sb.append("--在第").append(mazeLev1).append("层");
                sb.append("<br>生命值：").append(hp1);
                sb.append("<br>攻击力：").append(atk1);
                sb.append("<br>**************<br>");
            }
            if (hp != 0) {
                sb.append("第一次相遇");
                sb.append("--在第").append(mazeLev).append("层");
                sb.append("<br>生命值：").append(hp);
                sb.append("<br>攻击力：").append(atk);
                sb.append("<br>**************<br>");
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

        public MonsterItem(String name, boolean defeat) {
            this.name = name;
            this.defeat = defeat;
        }

        public final static MonsterItem EMPTY_MONSTER = new MonsterItem("", false) {
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
