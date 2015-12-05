package cn.gavin.skill.type;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.gavin.R;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.skill.system.BaseSkill;
import cn.gavin.skill.system.SkillLayout;
import cn.gavin.utils.MazeContents;

/**
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED
 * Created by luoyuan on 12/6/15.
 */
public class SkillMainDialog implements View.OnClickListener {
    AlertDialog mainDialog;
    MainGameActivity context = MainGameActivity.context;

    public SkillMainDialog() {
        mainDialog = new AlertDialog.Builder(context).create();
        View view = View.inflate(context, R.layout.skill_main_view, (ViewGroup) context.findViewById(R.id.skill_main_view_root));
        mainDialog.setButton(DialogInterface.BUTTON_POSITIVE, "退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        mainDialog.setView(view);
    }

    public void show() {
        mainDialog.show();
        Button button = mainDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        if (button != null) {
            button.setBackgroundColor(Color.parseColor("#ff000000"));
            button.setTextColor(Color.parseColor("#ffffffff"));
        }
        mainDialog.findViewById(R.id.base_skill).setOnClickListener(this);
        mainDialog.findViewById(R.id.evil_skill).setOnClickListener(this);
        mainDialog.findViewById(R.id.element_skill).setOnClickListener(this);
        mainDialog.findViewById(R.id.long_skill).setOnClickListener(this);
        mainDialog.findViewById(R.id.pet_skill).setOnClickListener(this);
        mainDialog.findViewById(R.id.swindler_skill).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        SkillLayout skillLayout = null;
        switch (view.getId()) {
            case R.id.base_skill:
                skillLayout = new BaseSkill(context);
                skillLayout.setTag("勇者");
                break;
        }
        if (skillLayout != null) {
            View skillView = buildSkillView(skillLayout);
            AlertDialog skillDialog = new AlertDialog.Builder(context).create();
            skillDialog.setTitle(skillLayout.getTag().toString());
            skillDialog.setView(skillView);
            skillDialog.setButton(DialogInterface.BUTTON_POSITIVE, "退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            skillDialog.show();
            skillLayout.init();
        }
    }

    private View buildSkillView(SkillLayout skillLayout) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        ViewGroup.LayoutParams layoutParams = linearLayout.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            linearLayout.setLayoutParams(layoutParams);
        }
        linearLayout.setGravity(Gravity.CENTER);
        TextView sp = new TextView(context);
        sp.setGravity(Gravity.CENTER);
        sp.setTextSize(12);
        sp.setText(MazeContents.hero.getSkillPoint() + "");
        linearLayout.addView(sp);
        linearLayout.addView(skillLayout);
        skillLayout.setSkillPointView(sp);
        return linearLayout;
    }
}
