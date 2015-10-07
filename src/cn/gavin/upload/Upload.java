package cn.gavin.upload;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cn.gavin.Hero;
import cn.gavin.utils.StringUtils;

/**
 * Created by gluo on 9/5/2015.
 */
public class Upload {
    private static String UPLOAD_URL = "http://mazeneverend.sinaapp.com";

    public boolean upload(Hero hero, long pay) {
        try {
            URL url = new URL(UPLOAD_URL + buildParas(hero, pay));
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
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String buildParas(Hero hero, long pay) {
        String name = StringUtils.toHexString(hero.getName());
        StringBuilder builder = new StringBuilder();
        builder.append("?");
        builder.append("name=").append(name);
        builder.append("&hp=").append((hero.getUpperHp() + hero.getDefenseValue()));
        builder.append("&atk=").append(hero.getUpperAtk());
        builder.append("&mazeLev=").append(hero.getMaxMazeLev());
        builder.append("&pay=").append(pay);
        builder.append("&skill=").append(hero.getFirstSkill());
        return builder.toString();
    }

}
