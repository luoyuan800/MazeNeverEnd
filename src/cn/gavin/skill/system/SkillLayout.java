package cn.gavin.skill.system;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import cn.gavin.Hero;
import cn.gavin.skill.Skill;
import cn.gavin.skill.type.PropertySkill;
import cn.gavin.utils.MazeContents;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luoyuan on 9/13/15.
 */
public abstract class SkillLayout extends ScrollView {
    protected List<Skill> skills;
    protected View view;
    protected Context context;
    protected TextView skillPointView;

    public void refresh() {
        for (Skill skill : skills) {
            skill.refresh();
        }
        skillPointView.setText(MazeContents.hero.getSkillPoint() + "");
    }

    public void setSkillPointView(TextView view) {
        skillPointView = view;
    }

    protected Hero hero = MazeContents.hero;

    public SkillLayout(Context context) {
        super(context);
        skills = new ArrayList<Skill>();
    }

    public OnClickListener buildOnClickListener(final Skill skill) {
        return new OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = new AlertDialog.Builder(context).create();
                TextView skillDesc = new TextView(context);
                skillDesc.setText(Html.fromHtml(skill.toString()));
                dialog.setView(skillDesc);
                if (!(skill instanceof PropertySkill) && skill.isActive() && !skill.isOnUsed()) {
                    dialog.setButton(DialogInterface.BUTTON_POSITIVE, "装备", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            skill.setOnUsed(true);
                            dialogInterface.dismiss();
                            refresh();
                        }
                    });
                } else if (!(skill instanceof PropertySkill) && skill.isActive()) {
                    dialog.setButton(DialogInterface.BUTTON_POSITIVE, "卸下", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            skill.setOnUsed(false);
                            dialogInterface.dismiss();
                            refresh();
                        }
                    });
                } else if (!skill.isActive()) {
                    dialog.setButton(DialogInterface.BUTTON_POSITIVE, "激活", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            skill.setActive(true);
                            dialogInterface.dismiss();
                            refresh();
                        }
                    });
                }
                dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "升级", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //todo
                    }
                });
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                dialog.show();
                Button positive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                if (positive != null) {
                    if (skill.isEnable()) {
                        positive.setEnabled(true);
                    } else {
                        positive.setEnabled(false);
                    }
                }
            }
        };
    }

    public abstract void init();
}
