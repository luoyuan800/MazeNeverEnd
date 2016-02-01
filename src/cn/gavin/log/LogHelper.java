package cn.gavin.log;

import cn.bmob.v3.datatype.BmobFile;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.utils.MazeContents;
import com.bmob.BTPFileResponse;
import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadListener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

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

    public static void logException(final Exception e, boolean upload) {
        e.printStackTrace();
        try {
            final MainGameActivity context = MainGameActivity.context;
                String pkName = MainGameActivity.context.getPackageName();
                int versionCode = MainGameActivity.context.getPackageManager()
                        .getPackageInfo(pkName, 0).versionCode;
            File path = new File(MazeContents.SD_PATH + "/log/");
            if (!path.exists()) {
                path.mkdirs();
            }
            File file = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/maze/log/" + e.getClass().getSimpleName() + "." + versionCode);
            if (!file.exists()) {
                file.createNewFile();

            }
            FileWriter writer = new FileWriter(file, false);
            e.printStackTrace(new PrintWriter(writer));
            writer.flush();
            writer.close();
            if (upload) {
                BTPFileResponse response = BmobProFile.getInstance(context).upload(file.getPath(), new UploadListener() {

                    @Override
                    public void onSuccess(String fileName, String url, BmobFile file) {
                        // TODO Auto-generated method stub
                        // fileName ：文件名（带后缀），这个文件名是唯一的，开发者需要记录下该文件名，方便后续下载或者进行缩略图的处理
                        // url        ：文件地址
                        // file        :BmobFile文件类型，`V3.4.1版本`开始提供，用于兼容新旧文件服务。
                    }

                    @Override
                    public void onProgress(int progress) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onError(int statuscode, String errormsg) {
                        // TODO Auto-generated method stub
                    }
                });
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
