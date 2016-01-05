package cn.gavin.upload;

import android.os.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cn.bmob.v3.listener.SaveListener;
import cn.gavin.Hero;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.log.LogHelper;
import cn.gavin.skill.Skill;
import cn.gavin.story.PalaceObject;
import cn.gavin.utils.MazeContents;
import cn.gavin.utils.StringUtils;

/**
 * Created by gluo on 9/5/2015.
 */
public class Upload {
    private static String UPLOAD_URL = "http://mazeneverend.sinaapp.com";

    private MainGameActivity context;

    public Upload(MainGameActivity context) {
        this.context = context;
    }

    public boolean upload(final Hero hero) {
        new Thread() {
            public void run() {
                boolean check = MazeContents.checkCheat(hero);
                if (check) {
                    PalaceObject object = new PalaceObject();
                    object.setHello(hero.getHello());
                    object.setAtk(hero.getUpperAtk().toString());
                    object.setDef(hero.getUpperDef().toString());
                    object.setHitRate(hero.getHitRate().toString());
                    object.setHp(hero.getUpperHp().toString());
                    object.setName(hero.getName());
                    object.setParry(hero.getParry().toString());
                    StringBuilder skill = new StringBuilder();
                    if(hero.getFirstSkill()!=null){
                        skill.append(hero.getFirstSkill().getName()).append("-");
                    }
                    if(hero.getSecondSkill()!=null){
                        skill.append(hero.getSecondSkill().getName()).append("-");
                    }
                    if(hero.getThirdSkill()!=null){
                        skill.append(hero.getThirdSkill().getName()).append("-");
                    }
                    if(hero.getFourthSkill()!=null){
                        skill.append(hero.getFourthSkill().getName()).append("-");
                    }
                    if(hero.getFifthSkill()!=null){
                        skill.append(hero.getFifthSkill().getName()).append("-");
                    }
                    if(hero.getSixthSkill()!=null){
                        skill.append(hero.getSixthSkill().getName()).append("-");
                    }
                    object.setSkill(skill.toString());
                    object.setPay(hero.getPay().toString());
                    object.setLev(hero.getMaxMazeLev());
                    object.setElement(hero.getElement().name());
                    object.setReCount(hero.getReincaCount());
                    object.setUuid(hero.getUuid());
                    try {
                        String pkName = MainGameActivity.context.getPackageName();
                        int versionCode = MainGameActivity.context.getPackageManager()
                                .getPackageInfo(pkName, 0).versionCode;
                        object.setVersion(versionCode);
                    } catch (Exception e) {
                        LogHelper.logException(e);
                    }
                    object.save(context, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            context.getHandler().sendEmptyMessage(104);
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Message message = new Message();
                            message.what = 105;
                            message.obj = s;
                            context.getHandler().sendMessage(message);
                        }
                    });
                } else {
                    context.getHandler().sendEmptyMessage(108);
                }
            }
        }.start();

        return true;
    }

    public boolean upload(Hero hero, long pay) {
        try {
            URL url = new URL(UPLOAD_URL + buildParas(hero, pay));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            con.connect();
            InputStreamReader reader = new InputStreamReader(con.getInputStream());
            BufferedReader bReader = new BufferedReader(reader);
            String line = null;
            while ((line = bReader.readLine()) != null) {
                if (line.matches(".*success.*")) {
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
        Skill skill = hero.getFirstSkill();
        if (skill != null) {
            builder.append("&skill=").append(StringUtils.toHexString(skill.getName())).append("_").append(skill.getCount());
        }
        return builder.toString();
    }

}
