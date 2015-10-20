package cn.gavin.log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import cn.gavin.activity.MainGameActivity;

/**
 * Created by luoyuan on 9/20/15.
 */
public class LogHelper {

    public static void writeLog() {
//        new Thread(
//                new Runnable() {
//                    public void run() {
//                        try {
//                            Process process = Runtime.getRuntime().exec(new String[]{"logcat", MainGameActivity.TAG + ":I *:S"});
//                            BufferedReader bufferedReader = new BufferedReader(
//                                    new InputStreamReader(process.getInputStream()));
//
//                            StringBuilder log = new StringBuilder();
//                            String line;
//                            while ((line = bufferedReader.readLine()) != null) {
//                                log.append(line).append("\n");
//                            }
//                            File path = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/maze/log/");
//                            if (!path.exists()) {
//                                path.mkdirs();
//                            }
//                            File file = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/maze/log/log.txt");
//                            if (!file.exists()) {
//                                file.createNewFile();
//                            }
//                            BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
//                            writer.write(log.toString());
//                            writer.flush();
//                            writer.close();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).start();
    }

    public static void logException(final Exception e) {
        try {
            File path = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/maze/log/");
            if (!path.exists()) {
                path.mkdirs();
            }
            File file = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/maze/log/" + e.getClass().getSimpleName());
            if (!file.exists()) {
                file.createNewFile();

            }
            FileWriter writer = new FileWriter(file, false);
            e.printStackTrace(new PrintWriter(writer));
            writer.flush();
            writer.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
