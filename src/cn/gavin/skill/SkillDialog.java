package cn.gavin.skill;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.List;

import cn.gavin.Hero;
import cn.gavin.R;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.skill.system.BaseSkill;
import cn.gavin.skill.system.LongSkill;
import cn.gavin.skill.type.PropertySkill;

/**
 * Created by luoyuan on 9/12/15.
 */
public class SkillDialog extends GestureDetector.SimpleOnGestureListener {
    private MainGameActivity context;
    private AlertDialog dialog;
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

    public void init() {
        dialog = new AlertDialog.Builder(context).create();
        dialog.setTitle("技能选择");
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "退出",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }

                });

        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.skill_dialog, (ViewGroup) context.findViewById(R.id.skill_dialog));
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.skill_dialog);
        viewFlipper = new ViewFlipper(context);
        BaseSkill baseSkill = new BaseSkill(context);
        baseSkill.init(this);
        LongSkill longSkill = new LongSkill(context);
        longSkill.init(this);
        viewFlipper.addView(baseSkill);
        viewFlipper.addView(longSkill);
        linearLayout.addView(viewFlipper);
        detector = new GestureDetector(context, this);
        dialog.setView(view);
        skillDesc = (TextView) view.findViewById(R.id.skill_description);
        skillPoint = (TextView) view.findViewById(R.id.skill_point);
        sillNameText = (TextView) view.findViewById(R.id.skill_system_name);
        sillNameText.setText(systemNames[0]);
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
                sillNameText.setText(getPrevSystemName());
            }
        });
        Button next = (Button) view.findViewById(R.id.next_skill_system_button);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewFlipper.showNext();
                sillNameText.setText(getPrevSystemName());
            }
        });
        isInit = true;
    }

    int index = 0;
    String[] systemNames = {"勇者技能", "龙裔技能", "魔王技能"};

    private String getNextSystemName() {
        if (index >= systemNames.length - 1) {
            index = 0;
        } else {
            index++;
        }
        return systemNames[index];
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
        dialog.show();
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

    private final Handler handler = new Handler() {
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
}
