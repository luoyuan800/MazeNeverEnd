package cn.gavin.skill;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.*;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import cn.gavin.Hero;
import cn.gavin.R;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.skill.system.*;
import cn.gavin.skill.type.PropertySkill;

import java.util.List;

/**
 * Created by luoyuan on 9/12/15.
 */
@Deprecated
public class SkillDialog extends GestureDetector.SimpleOnGestureListener {
    private MainGameActivity context;
    private AlertDialog alertDialog;
    private TextView skillDesc;
    private TextView skillPoint;
    private List<Skill> skills;
    private ViewFlipper viewFlipper;
    private GestureDetector detector; //手势检测
    private TextView sillNameText;
    private boolean isInit;

    public SkillDialog(MainGameActivity context) {
        this.context = context;
    }

    public SkillDialog() {

    }

    public void init() {
        handler = new Handler() {
            public void handleMessage(android.os.Message message) {
                SkillFactory.refreshSkillStatus();
                switch (message.what) {
                    case 1:
                        skillDesc.setText("");
                        skillPoint.setText("技能点数:" + message.obj);
                        break;
                    case 0:
                        skillDesc.setText("");
                        Message msg = new Message();
                        msg.obj = context.getHero().getSkillPoint();
                        msg.what = 1;
                        this.sendMessage(msg);
                        break;
                }
                super.handleMessage(message);
            }
        };
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("技能选择");
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "退出",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.hide();
                    }

                });

        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.skill_dialog, (ViewGroup) context.findViewById(R.id.skill_dialog));
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.skill_dialog);
        viewFlipper = new ViewFlipper(context);

        BaseSkill baseSkill = new BaseSkill(context);
        baseSkill.init();

        EvilSkill evilSkill = new EvilSkill(context);
        evilSkill.init();

        SwindlerSkill swindlerSkill = new SwindlerSkill(context);
        swindlerSkill.init();

        LongSkill longSkill = new LongSkill(context);
        longSkill.init();

        PetSkill petSkill = new PetSkill(context);
        petSkill.init();

        viewFlipper.addView(baseSkill);
        viewFlipper.addView(evilSkill);
        viewFlipper.addView(swindlerSkill);
        viewFlipper.addView(longSkill);
        viewFlipper.addView(petSkill);
        linearLayout.addView(viewFlipper);
        detector = new GestureDetector(context, this);
        alertDialog.setView(view);
        skillDesc = (TextView) view.findViewById(R.id.skill_description);
        skillPoint = (TextView) view.findViewById(R.id.skill_point);
        sillNameText = (TextView) view.findViewById(R.id.skill_system_name);
        sillNameText.setText("勇者");
        viewFlipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return detector.onTouchEvent(event);
            }
        });
        Button pre = (Button) view.findViewById(R.id.prev_skill_system_button);
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewFlipper.showPrevious();
                changeSkillSystem();
            }
        });
        Button next = (Button) view.findViewById(R.id.next_skill_system_button);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewFlipper.showNext();
                changeSkillSystem();
            }
        });
        isInit = true;
    }

    private void changeSkillSystem() {
        if(viewFlipper.getCurrentView() instanceof LongSkill){
            sillNameText.setText("龙裔");
        }
        if(viewFlipper.getCurrentView() instanceof BaseSkill){
            sillNameText.setText("勇者");
        }
        if(viewFlipper.getCurrentView() instanceof EvilSkill){
            sillNameText.setText("魔王");
        }
        if(viewFlipper.getCurrentView() instanceof SwindlerSkill){
            sillNameText.setText("欺诈师");
        }
        if(viewFlipper.getCurrentView() instanceof PetSkill){
            sillNameText.setText("宠物大师");
        }
        skillDesc.setText("");
    }

    int index = 0;
    String[] systemNames = {"勇者技能", "魔王技能", "欺诈师技能", "龙裔技能"};

    private String getNextSystemName() {
        if (index >= systemNames.length - 1) {
            index = 0;
        } else {
            index++;
        }
        return systemNames[index];
    }

    private void setBak() {
        switch (index) {
            case 3:
                final View view = viewFlipper.getCurrentView();
                if (view != null) {
                    view.setBackgroundResource(R.drawable.long_bak);
                    new Thread(){
                        public void run(){
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            view.setBackgroundResource(android.R.color.background_light);

                        }
                    }.start();
                }
                break;
        }
    }

    private String getPrevSystemName() {
        if (index <= 0) {
            index = systemNames.length - 1;
        } else {
            index--;
        }
        return systemNames[index];
    }

    public void show(Hero hero) {
        Message msg = new Message();
        msg.obj = hero.getSkillPoint();
        msg.what = 1;
        handler.sendMessage(msg);
        alertDialog.show();
    }

    public View.OnClickListener getClickListener(final Skill skill) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skillDesc.setText(Html.fromHtml(skill.toString()));
            }
        };
    }

    public View.OnLongClickListener getLongClickListener(final Skill skill) {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (skill.isEnable()) {
                    final AlertDialog dialog = new AlertDialog.Builder(context).create();
                    WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
                    dialog.getWindow().setAttributes(layoutParams);
                    TextView tv = new TextView(context);
                    tv.setText(Html.fromHtml(skill.description()));
                    dialog.setView(tv);
                    if (!(skill instanceof PropertySkill) && skill.isActive() && !skill.isOnUsed()) {
                        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "装备", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                skill.setOnUsed(true);
                                dialogInterface.dismiss();
                                handler.sendEmptyMessage(0);
                            }
                        });
                    } else if (!(skill instanceof PropertySkill) && skill.isActive()) {
                        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "卸下", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                skill.setOnUsed(false);
                                dialogInterface.dismiss();
                                handler.sendEmptyMessage(0);
                            }
                        });
                    } else if (!skill.isActive()) {
                        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "激活", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                skill.setActive(true);
                                dialogInterface.dismiss();
                                handler.sendEmptyMessage(0);
                            }
                        });
                    }
                    dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "退出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    dialog.show();
                }
                return false;
            }
        };
    }

    private Handler handler;

    public Handler getHandler() {
        return handler;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getX() - e2.getX() > 100) {
            viewFlipper.showNext();//向右滑动
            return true;
        } else if (e1.getX() - e2.getY() < -100) {
            viewFlipper.showPrevious();//向左滑动
            return true;
        }
        return false;
    }

    public boolean isInit() {
        return isInit;
    }

    public void setContext(MainGameActivity context) {
        this.context = context;
    }
}
