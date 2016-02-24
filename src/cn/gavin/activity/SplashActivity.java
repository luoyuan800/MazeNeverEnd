package cn.gavin.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;

import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;

import cn.gavin.R;


public class SplashActivity extends Activity implements SplashADListener {

    @SuppressWarnings("unused")
    private SplashAD splashAD;
    private ViewGroup container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        container = (ViewGroup) this.findViewById(R.id.splash_container);
        /**
         * 开屏广告已增加新的接口，可以由开发者在代码中设置开屏的超时时长
         * SplashAD(Activity activity, ViewGroup container, String appId, String posId, SplashADListener adListener, int fetchDelay)
         * fetchDelay参数表示开屏的超时时间，单位为ms，取值范围[2000, 5000]。设置为0时表示使用广点通的默认开屏超时配置
         */
        // splashAD = new SplashAD(this, container, "1104849170", "1080705809909034", this);
        splashAD = new SplashAD(this, container, "1104849170", "6040208900840052", this, 0);
    }

    @Override
    public void onADPresent() {
        Log.i("AD_DEMO", "SplashADPresent");
    }

    @Override
    public void onADDismissed() {
        Log.i("AD_DEMO", "SplashADDismissed");
        next();
    }

    private void next() {
        this.startActivity(new Intent(this, MainMenuActivity.class));
        this.finish();
    }

    @Override
    public void onNoAD(int arg0) {
        Log.i("AD_DEMO", "LoadSplashADFail,ecode=" + arg0);
        next();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
