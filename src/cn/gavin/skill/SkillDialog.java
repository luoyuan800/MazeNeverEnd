package cn.gavin.skill;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import cn.gavin.R;
import cn.gavin.activity.MainGameActivity;

import java.util.List;

/**
 * Created by luoyuan on 9/12/15.
 */
public class SkillDialog {
    private MainGameActivity context;
    private AlertDialog dialog;
    private TextView skillDesc;
    private TextView skillPoint;
    private List<Skill> skills;
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
        dialog.setView(view);
        skillDesc = (TextView) view.findViewById(R.id.skill_description);
        skillPoint = (TextView) view.findViewById(R.id.skill_point);
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

    private final Handler handler = new Handler(){
        public void handleMessage(android.os.Message message){
            super.handleMessage(message);
        }
    };

    public Handler getHandler(){
        return handler;
    }
}
