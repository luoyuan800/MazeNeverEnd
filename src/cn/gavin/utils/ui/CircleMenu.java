package cn.gavin.utils.ui;

import android.content.Context;
import android.graphics.*;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

public class CircleMenu extends View {
    private static final String TAG = "circleMenu";
    private int lastX;
    private int lastY;

    public interface OnCircleItemSelectedListener {
        public void onItemClickedListener(int index);
    }

    private OnCircleItemSelectedListener OnCircleItemSelectedListener;

    public void setOnCircleItemSelectedListener(
            OnCircleItemSelectedListener l) {
        this.OnCircleItemSelectedListener = l;
    }

    /*
     * 是否拖动
     */
    private boolean isMove;
    /*
     * 是否关闭
     */
    private boolean isClose;
    /*
     * 旋转子菜单自身
     */
    private static final int TO_ROTATE_SUB_MENU = 0;

    private Context mContext;
    /*
     * 每个象限的触摸情况
     */
    private boolean[] quadrantTouched;

    /*
     * 图片抖动
     */
    private PaintFlagsDrawFilter mPfd;

    /*
     * 手势监听
     */
    private GestureDetector mGestureDetector;

    /*
     * 画笔
     */
    private Paint mPaint = new Paint();

    /*
     * 每个菜单按钮的间隔
     */
    private int mDegreeDelta;

    /*
     * 整个菜单半径
     */
    private int mRadius;

    /*
     * 子菜单半径
     */
    private int menuRadius;

    /*
     * 菜单中心按钮
     */
    private int mMainMenu;
    /*
     * 菜单中心按钮数据
     */
    private SubMenuHolder mMainMenuHolder;

    /*
     * 菜单中心坐标
     */
    private int mPointX, mPointY;

    /*
     * 所有子菜单
     */
    private int[] mSubMenus;

    /*
     * 所有子菜单相关数据
     */
    private SubMenuHolder[] mSubMenuHolder;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TO_ROTATE_SUB_MENU:
                    float velocity = Float.parseFloat(msg.obj.toString());
                    rotateButtons(velocity / 75);
                    velocity /= 1.0666F;
                    new Thread(new FlingRunnable(velocity)).start();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    public CircleMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        // 添加抖动效果，使绘图更好
        mPfd = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
                | Paint.FILTER_BITMAP_FLAG);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(2);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Style.STROKE);
        PathEffect effect = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
        mPaint.setPathEffect(effect);
        quadrantTouched = new boolean[]{false, false, false, false, false};
        mGestureDetector = new GestureDetector(context, new MyGestureDetector());
    }

    // 初始化每个子菜单的角度和图片
    public void initSubMenus(int mainMenuImage, int[] subMenuImages) {
        mMainMenu = mainMenuImage;
        mSubMenus = subMenuImages;
        mSubMenuHolder = new SubMenuHolder[mSubMenus.length];
        SubMenuHolder subMenuHolder;
        int angle = 270;
        mDegreeDelta = 360 / mSubMenus.length;
        for (int i = 0; i < mSubMenus.length; i++) {
            subMenuHolder = new SubMenuHolder();
            if (angle >= 360) {
                angle -= 360;
            } else if (angle < 0) {
                angle += 360;
            }
            subMenuHolder.angle = angle;
            subMenuHolder.bitmap = BitmapFactory.decodeResource(getResources(),
                    mSubMenus[i]);
            angle += mDegreeDelta;
            mSubMenuHolder[i] = subMenuHolder;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mPointX = this.getMeasuredWidth() / 2;
        mPointY = this.getMeasuredHeight() / 2;
        mRadius = mPointX - mPointX / 5;
        menuRadius = (int) (mPointX / 5.5);
        computeCoordinates();
    }

    /*
     * 计算子菜单坐标
     */
    private void computeCoordinates() {
        SubMenuHolder subMenuHolder;
        if (mSubMenuHolder != null) {
            for (SubMenuHolder aMSubMenuHolder : mSubMenuHolder) {
                subMenuHolder = aMSubMenuHolder;
                subMenuHolder.x = (float) (mPointX + (mRadius * Math.cos(Math
                        .toRadians(subMenuHolder.angle))));
                subMenuHolder.y = (float) (mPointY + (mRadius * Math.sin(Math
                        .toRadians(subMenuHolder.angle))));
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mMainMenuHolder == null) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                    mMainMenu);
            mMainMenuHolder = new SubMenuHolder();
            mMainMenuHolder.bitmap = bitmap;
        }
        drawInCenter(canvas, mMainMenuHolder.bitmap, mPointX, mPointY);

        if (!isClose) {
            if (mSubMenuHolder != null) {
                for (SubMenuHolder aMSubMenuHolder : mSubMenuHolder) {
                    drawInCenter(canvas, aMSubMenuHolder.bitmap, aMSubMenuHolder.x,
                            aMSubMenuHolder.y);
                }
            }
        }
    }

    /*
     * 旋转子菜单菜单自身
     */
    private void rotateButtons(double degree) {
        for (int i = 0; i < mSubMenuHolder.length; i++) {
            mSubMenuHolder[i].angle -= degree;
            if (mSubMenuHolder[i].angle < 0) {
                mSubMenuHolder[i].angle += 360;
            } else if (mSubMenuHolder[i].angle >= 360) {
                mSubMenuHolder[i].angle -= 360;
            }
        }
        computeCoordinates();
        invalidate();
    }

    /*
     * 绘制每个子菜单
     */
    private void drawInCenter(Canvas canvas, Bitmap bitmap, float left,
                              float top) {
        if (bitmap != null) {
            Rect dst = new Rect();
            dst.left = (int) (left - menuRadius);
            dst.top = (int) (top - menuRadius);
            dst.right = (int) (left + menuRadius);
            dst.bottom = (int) (top + menuRadius);
            canvas.setDrawFilter(mPfd);
            canvas.drawBitmap(bitmap, null, dst, mPaint);
        }
    }

    private double startAngle;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            startAngle = computeCurrentAngle(x, y);
            lastX = (int) event.getRawX();
            lastY = (int) event.getRawY();
            if (isClose && getInCircle(x, y) != -2) {
                return false;
            }
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (isMove) {
                int dx = (int) event.getRawX() - lastX;
                int dy = (int) event.getRawY() - lastY;
                int left = getLeft() + dx;
                int top = getTop() + dy;
                int right = getRight() + dx;
                int bottom = getBottom() + dy;
                this.layout(left, top, right, bottom);
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
            } else {
                double currentAngle = computeCurrentAngle(x, y);
                rotateButtons(startAngle - currentAngle);
                startAngle = currentAngle;
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            isMove = false;
        }
        if (!isMove) {
            mGestureDetector.onTouchEvent(event);
        }
        return true;
    }

    /**
     * 计算某点的角度
     *
     * @param x
     * @param y
     * @return
     */
    private int computeCurrentAngle(float x, float y) {
        float distance = (float) Math
                .sqrt(((x - mPointX) * (x - mPointX) + (y - mPointY)
                        * (y - mPointY)));
        int degree = (int) (Math.acos((x - mPointX) / distance) * 180 / Math.PI);
        if (y < mPointY) {
            degree = -degree;
        }
        Log.d("RoundSpinView", "x:" + x + ",y:" + y + ",degree:" + degree);
        return degree;
    }

    private int getInCircle(int x, int y) {
        if (((x - mPointX) * (x - mPointX) + (y - mPointY) * (y - mPointY)) < menuRadius
                * menuRadius) {
            return -2;
        }
        for (int i = 0; i < mSubMenuHolder.length; i++) {
            SubMenuHolder holder = mSubMenuHolder[i];
            int mx = (int) holder.x;
            int my = (int) holder.y;
            if (((x - mx) * (x - mx) + (y - my) * (y - my)) < menuRadius
                    * menuRadius) {
                return i;
            }
        }
        return -1;
    }

    private class MyGestureDetector extends SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            int q1 = getQuadrant(e1.getX() - mPointX, mPointY - e1.getY());
            int q2 = getQuadrant(e2.getX() - mPointX, mPointY - e2.getY());
            if ((q1 == 2 && q2 == 2 && Math.abs(velocityX) < Math
                    .abs(velocityY))
                    || (q1 == 3 && q2 == 3)
                    || (q1 == 1 && q2 == 3)
                    || (q1 == 4 && q2 == 4 && Math.abs(velocityX) > Math
                    .abs(velocityY))
                    || ((q1 == 2 && q2 == 3) || (q1 == 3 && q2 == 2))
                    || ((q1 == 3 && q2 == 4) || (q1 == 4 && q2 == 3))
                    || (q1 == 2 && q2 == 4 && quadrantTouched[3])
                    || (q1 == 4 && q2 == 2 && quadrantTouched[3])) {
                new Thread(new FlingRunnable(velocityX + velocityY)).start();
            } else {
                new Thread(new FlingRunnable(-(velocityX + velocityY))).start();
            }

            return true;

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            int x = (int) e.getX();
            int y = (int) e.getY();
            int menuIndex = getInCircle(x, y);
            if (menuIndex != -1) {
                if (OnCircleItemSelectedListener != null) {
                    OnCircleItemSelectedListener.onItemClickedListener(menuIndex);
                }
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.d(TAG, "Long Press ");
            isMove = true;
        }
    }

    private class FlingRunnable implements Runnable {

        private float velocity;

        public FlingRunnable(float velocity) {
            this.velocity = velocity;
        }

        @Override
        public void run() {
            if (Math.abs(velocity) >= 200) {
                Message message = Message.obtain();
                message.what = TO_ROTATE_SUB_MENU;
                message.obj = velocity;
                handler.sendMessage(message);
            }
        }
    }

    /**
     * @return The selected quadrant.
     */
    private static int getQuadrant(double x, double y) {
        if (x >= 0) {
            return y >= 0 ? 1 : 4;
        } else {
        }
        return y >= 0 ? 2 : 3;
    }

    private class SubMenuHolder {
        Bitmap bitmap;
        int angle;
        float x;
        float y;
    }

    /*
     * 关闭或打开子菜单
     */
    public void closeOrOpen() {
        isClose = !isClose;
        invalidate();
    }

}
