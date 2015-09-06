package cn.gavin.alipay;

import cn.gavin.activity.MainGameActivity;
import com.bmob.pay.tool.BmobPay;
import com.bmob.pay.tool.PayListener;

/**
 * Created by gluo on 9/1/2015.
 */
public class Alipay {
    private int payTime;
    private MainGameActivity context;
    private BmobPay bmobPay;

    public Alipay(MainGameActivity context, int time) {
        this.payTime = time;
        this.context = context;
        bmobPay = new BmobPay(context);
    }

    public int getPayTime() {
        return payTime;
    }

    public void addPayTime() {
        payTime++;
    }


    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void pay() {
        new Thread(new Runnable(){
            public void run(){
                bmobPay.pay(0.02, "勇者进贡", new PayListener() {

                    @Override
                    public void orderId(String s) {
                        context.addMessage("进贡订单号："+ s);
                    }

                    @Override
                    public void succeed() {
                        context.getHandler().sendEmptyMessage(100);
                    }

                    @Override
                    public void fail(int i, String s) {

                    }

                    @Override
                    public void unknow() {

                    }
                });
            }
        }).start();
    }


}
