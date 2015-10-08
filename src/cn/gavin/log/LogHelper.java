package cn.gavin.log;

import android.widget.TextView;
import cn.gavin.R;
import cn.gavin.activity.MainGameActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by luoyuan on 9/20/15.
 */
public class LogHelper {

    public static void writeLog(){
        try {
            Process process = Runtime.getRuntime().exec(String.format("logcat -s %s:I ",MainGameActivity.TAG));
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            StringBuilder log=new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                log.append(line).append("\n");
            }
           File path = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/maze/log/");
           if(!path.exists()){
               path.mkdirs();
           }
            File file = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/maze/log/log.txt");
            if(!file.exists()){
                file.createNewFile();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(log.toString());
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
