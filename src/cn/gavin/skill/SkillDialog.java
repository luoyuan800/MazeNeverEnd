package cn.gavin.skill;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.List;

import cn.gavin.R;
import cn.gavin.activity.MainGameActivity;

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
        LongSkill longSkill = new LongSkill(context);
        viewFlipper.addView(baseSkill);
        viewFlipper.addView(longSkill);
        linearLayout.addView(viewFlipper);
        detector = new GestureDetector(context, this);
        dialog.setView(view);
        skillDesc = (TextView) view.findViewById(R.id.skill_description);
        skillPoint = (TextView) view.findViewById(R.id.skill_point);
        viewFlipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return detector.onTouchEvent(event);
            }
        });
    }


    public View setDialog(int layoutId, int rootGroupId) {
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(layoutId, (ViewGroup) context.findViewById(rootGroupId));
        dialog.setView(view);
        return view;
    }

    public void show() {
        dialog.show();
    }

    public View.OnClickListener getClickListener(final String desc) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skillDesc.setText(desc);
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
                    layoutParams.alpha = 0.3f;
                    dialog.getWindow().setAttributes(layoutParams);
                    if (skill.isActive() && !skill.isOnUsed()) {
                        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "装备", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                skill.setOnUsed(true);
                                dialogInterface.dismiss();
                            }
                        });
                    } else if (skill.isActive()) {
                        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "卸下", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                skill.setOnUsed(false);
                                dialogInterface.dismiss();
                            }
                        });
                    } else {
                        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "激活", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                skill.setActive(true);
                                dialogInterface.dismiss();
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
            switch (message.what) {
                case 0:
                    for (Skill skill : skills) {
                        skill.refresh();
                    }
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
}
