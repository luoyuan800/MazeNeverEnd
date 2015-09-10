package cn.gavin.monster;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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

    }

    public void addMonster(Monster monster) {
        if (nameKeys == null) {
            nameKeys = getMonsterNameKeys();
        }
        String key = monster.getName() + "_" + monster.isDefeat();
        if (!nameKeys.contains(key)) {
            writeToIndex(key);
            nameKeys.add(key);
            writeIntoFile(key, monster);
        }
    }

    private void writeToIndex(String key) {
        try {
            FileOutputStream output = context.openFileOutput("monster.index", Activity.MODE_PRIVATE);
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

    private void writeIntoFile(String key, Monster monster) {
        try {
            File file = new File(MainGameActivity.APK_PATH + "/" + key);
            if (!file.exists()) {
                file.mkdirs();
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file, true);
            StringBuilder sb = new StringBuilder();
            sb.append(monster.getName());
            sb.append("--(").append(monster.isDefeat() ? "打败" : "相遇").append(")");
            sb.append("--在第").append(monster.getMazeLev()).append("层");
            sb.append("\n生命值：").append(monster.getHp());
            sb.append("\n攻击力：").append(monster.getAtk());
            sb.append("**************\n");
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
    }

    static class MonsterViewHolder {
        Button name;
        Button name1;
        Button name2;
    }

    class MonsterAdapter extends BaseAdapter {
        private List<MonsterList> adapterData;
        private TextView textView;

        public MonsterAdapter(TextView textView) {
            this.textView = textView;
            adapterData = new ArrayList<MonsterList>();
            Set<String> names = getMonsterNameKeys();
            for (int i = 0; i < names.size(); i += 3) {

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
            if (convertView == null) {
                holder = new MonsterViewHolder();
                convertView = View.inflate(context,
                        R.layout.achievement_list_item, null);
                holder.name = (Button) convertView.findViewById(R.id.monster_name);
                holder.name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textView.setText(getItem(position).a0.getDesc());
                    }
                });
                holder.name1 = (Button) convertView.findViewById(R.id.monster_name_1);
                holder.name1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textView.setText(getItem(position).a1.getDesc());
                    }
                });
                holder.name2 = (Button) convertView.findViewById(R.id.monster_name_2);
                holder.name2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textView.setText(getItem(position).a2.getDesc());
                    }
                });

                convertView.setTag(holder);
            } else {
                holder = (MonsterViewHolder) convertView.getTag();
            }
            MonsterList item = getItem(position);
            holder.name.setText(item.a0.getName());
            if (!item.a0.isDefeat()) {
                holder.name.setEnabled(false);
            } else {
                holder.name.setEnabled(true);
            }
            holder.name1.setText(item.a1.getName());
            if (!item.a1.isDefeat()) {
                holder.name1.setEnabled(false);
            } else {
                holder.name1.setEnabled(true);
            }
            holder.name2.setText(item.a2.getName());
            if (!item.a2.isDefeat()) {
                holder.name2.setEnabled(false);
            } else {
                holder.name2.setEnabled(true);
            }

            return convertView;
        }

    }

    private static class MonsterItem {
        private Boolean defeat;
        private String desc;
        private String name;

        private void load() {
            String key = name + defeat;
            try {
                File file = new File(MainGameActivity.APK_PATH + "/" + key);
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
    }
}
