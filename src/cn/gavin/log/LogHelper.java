package cn.gavin.log;

import android.widget.TextView;
import cn.gavin.R;
import cn.gavin.activity.MainGameActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by luoyuan on 9/20/15.
 */
public class LogHelper {

    public void getLog(){
        MainGameActivity context = MainGameActivity.context;
        try {
            Process process = Runtime.getRuntime().exec(String.format("logcat -s %s:I ",MainGameActivity.TAG));
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            StringBuilder log=new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                log.append(line);
            }
            //TextView tv = (TextView)context.findViewById(R.id.textView1);
            //tv.setText(log.toString());
        } catch (IOException e) {
        }
    }
}
