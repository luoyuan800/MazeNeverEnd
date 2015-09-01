package cn.gavin.alipay;

/**
 * Created by gluo on 9/1/2015.
 */
public class Alipay {
    private int payTime;

    public Alipay(int time) {
        this.payTime = time;
    }

    public int getPayTime() {
        return payTime;
    }

    public void addPayTime() {
        payTime++;
    }

    public boolean pay() {
        addPayTime();
        return true;
    }
}
