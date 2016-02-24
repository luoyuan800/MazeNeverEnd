package cn.gavin.alipay;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.utils.MazeContents;
import com.bmob.pay.tool.BmobPay;
import com.bmob.pay.tool.PayListener;

/**
 * Created by gluo on 9/1/2015.
 */
public class Alipay {
    private long payTime;
    private long oldPayTime;
    private MainGameActivity context;
    private BmobPay bmobPay;
    private ProgressDialog dialog;

    public Alipay(MainGameActivity context, long time) {
        this.payTime = time;
        this.context = context;
        bmobPay = new BmobPay(context);
    }

    public long getPayTime() {
        return payTime;
    }

    public void addPayTime() {
        payTime++;
        MazeContents.payTime ++;
    }


    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void pay() {
//        context.getHandler().sendEmptyMessage(100);
        if (payTime < 4000) {
            showDialog("正在获取订单...");
            String orderName = "";
            if (MazeContents.hero != null) orderName = MazeContents.hero.getName();
            final String name = "勇者进贡";

            bmobPay.pay(1, name, orderName, new PayListener() {

                // 因为网络等原因,支付结果未知(小概率事件),出于保险起见稍后手动查询
                @Override
                public void unknow() {
                Toast.makeText(context, "--进贡结果未知,请稍后手动查询--",
                        Toast.LENGTH_SHORT).show();
                context.addMessage(name + "--进贡结果未知--");
                    hideDialog();
                }

                // 支付成功,如果金额较大请手动查询确认
                @Override
                public void succeed() {
                    Toast.makeText(context, "--进贡成功!--", Toast.LENGTH_SHORT)
                            .show();
                    context.addMessage(name + "--进贡成功--");
                    context.getHandler().sendEmptyMessage(100);
                    hideDialog();
                }

                // 无论成功与否,返回订单号
                @Override
                public void orderId(String orderId) {
                    // 此处应该保存订单号,比如保存进数据库等,以便以后查询
                    context.addMessage("--" + name + "的进贡订单号是" + orderId + "--");
                    showDialog("获取订单成功!请等待跳转到支付页面~");
                }

                // 支付失败,原因可能是用户中断支付操作,也可能是网络原因
                @Override
                public void fail(int code, String reason) {
                    Toast.makeText(context, "支付中断!", Toast.LENGTH_SHORT)
                            .show();
                    context.addMessage("--" + name + "失败--");
                    hideDialog();
                }
            });
        } else {
            context.getHandler().sendEmptyMessage(110);
        }
    }


    void showDialog(String message) {
        if (dialog == null) {
            dialog = new ProgressDialog(context);
            dialog.setCancelable(false);
        }
        dialog.setMessage(message);
        dialog.show();
    }

    void hideDialog() {
        if (dialog != null && dialog.isShowing())
            try {
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public long getOldPayTime() {
        SharedPreferences preferences0 = context.getSharedPreferences("hero", Context.MODE_PRIVATE);
        long payTime = preferences0.getLong("payTime", 0);
        if(payTime > 0){
            SharedPreferences.Editor editor = preferences0.edit();
            editor.putLong("payTime", 0);
            editor.apply();
        }
        return payTime;
    }

    public void setOldPayTime(long oldPayTime) {
        this.oldPayTime = oldPayTime;
    }
}
