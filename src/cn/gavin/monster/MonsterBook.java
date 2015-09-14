package cn.gavin.monster;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.gavin.R;
import cn.gavin.activity.MainGameActivity;

/**
 * Created by gluo on 9/8/2015.
 */
public class MonsterBook {
    private MainGameActivity context;
    private Set<String> nameKeys;

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
        dialog.show();
    }

    public void addMonster(Monster monster) {
        if (nameKeys == null) {
            nameKeys = getMonsterNameKeys();
        }
        String key = monster.getName() + "_" + monster.isDefeat();
        if (!nameKeys.contains(key)) {
            writeToIndex(key);
            nameKeys.add(key);
            writeIntoFile(monster);
        }
    }

    private void writeToIndex(String key) {
        try {
            FileOutputStream output = context.openFileOutput("monster.index", Activity.MODE_APPEND);
            output.write(key.getBytes("UTF-8"));
            output.write("\n".getBytes("UTF-8"));
            output.flush();
            output.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeIntoFile(Monster monster) {
        try {
            File file = new File(MainGameActivity.APK_PATH + "/" + monster.getName());
            File path = new File(MainGameActivity.APK_PATH);
            if (!path.exists()) {
                path.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file, true);
            StringBuilder sb = new StringBuilder();
            sb.append(monster.getFormatName());
            sb.append("--(").append(monster.isDefeat() ? "打败" : "相遇").append(")");
            sb.append("--在第").append(monster.getMazeLev()).append("层");
            sb.append("<br>生命值：").append(monster.getMaxHP());
            sb.append("<br>攻击力：").append(monster.getAtk());
            sb.append("<br>**************<br>");
            fos.write(sb.toString().getBytes("UTF-8"));
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //从应用目录下读取index文件内容
    public Set<String> getMonsterNameKeys() {
        Set<String> names = new HashSet<String>();
        try {
            FileInputStream inputStream = context.openFileInput("monster.index");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String name;
            while ((name = reader.readLine()) != null) {
                names.add(name);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return names;
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
            List<String> nameList = new ArrayList<String>(getMonsterNameKeys());
            for (int i = 0; i < nameList.size(); i += 3) {
                MonsterList monsterList = new MonsterList();
                for (int j = i; j < i + 3; j++) {
                    MonsterItem item = MonsterItem.EMPTY_MONSTER;
                    if (j < nameList.size()) {
                        String name = nameList.get(j);
                        String[] name_key = name.split("_");
                        if (name_key.length > 1) {
                            item = new MonsterItem(name_key[0], Boolean.parseBoolean(name_key[1]));
                        }
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

        private void load() {
            try {
                File file = new File(MainGameActivity.APK_PATH + "/" + name);
                if (!file.exists()) {
                    return;
                }
                BufferedReader reader = new BufferedReader(new FileReader(file));
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line).append("\n");
                }
                desc = builder.toString();
                reader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
