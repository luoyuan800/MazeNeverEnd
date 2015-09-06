package cn.gavin.upload;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cn.gavin.Achievement;
import cn.gavin.Hero;

/**
 * Created by gluo on 9/5/2015.
 */
public class Upload {
    private static String UPLOAD_URL = "http://mazeneverend.sinaapp.com";

    public boolean upload(Hero hero) {
        try {
            URL url = new URL(UPLOAD_URL + buildParas(hero));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            con.connect();
            InputStreamReader reader = new InputStreamReader(con.getInputStream());
            BufferedReader bReader = new BufferedReader(reader);
            String line = null;
            while((line = bReader.readLine())!=null){
                if(line.matches(".*success.*")){
                    return true;
                }
            }
            Achievement.uploader.enable(hero);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String buildParas(Hero hero) {
        String name = toHexString(hero.getName());
        StringBuilder builder = new StringBuilder();
        builder.append("?");
        builder.append("name=").append(name);
        builder.append("&hp=").append((hero.getUpperHp() + hero.getDefenseValue()));
        builder.append("&atk=").append(hero.getAttackValue());
        builder.append("&mazeLev=").append(hero.getMaxMazeLev());
        return builder.toString();
    }

    public static String toHexString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return "0x" + str;//0x表示十六进制
    }

    //转换十六进制编码为字符串
    public static String toStringHex(String s) {
        if ("0x".equals(s.substring(0, 2))) {
            s = s.substring(2);
        }
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(
                        i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            s = new String(baKeyword, "utf-8");//UTF-16le:Not
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }
}
