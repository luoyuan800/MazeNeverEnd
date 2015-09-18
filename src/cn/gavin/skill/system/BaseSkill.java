package cn.gavin.skill.system;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import cn.gavin.Hero;
import cn.gavin.R;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.skill.Skill;
import cn.gavin.skill.SkillDialog;
import cn.gavin.skill.SkillFactory;

/**
 * Created by luoyuan on 9/13/15.
 */
public class BaseSkill extends SkillLayout {
    public static String getSystemName() {
        return "勇者";
    }

    private View view;
    private Context context;
    private Hero hero = MainGameActivity.context.getHero();

    public BaseSkill(Context context) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.skill_layout_base, (ViewGroup) this.findViewById(R.id.skill_dialog));
        this.addView(view);
        this.view = view;
        this.context = context;
    }

    public void init(SkillDialog dialog) {
        Skill skill = SkillFactory.getSkill("勇者之击", hero, dialog);
        Button button = (Button) view.findViewById(R.id.base_skill_r_button);
        skill.setSkillButton(button);

        skill = SkillFactory.getSkill("闪避",hero, dialog);
        button = (Button)view.findViewById(R.id.base_skill_s_button);
        skill.setSkillButton(button);

        skill = SkillFactory.getSkill("铁拳",hero, dialog);
        button = (Button)view.findViewById(R.id.skill_base_t_button);
        skill.setSkillButton(button);

        skill = SkillFactory.getSkill("反弹",hero, dialog);
        button = (Button)view.findViewById(R.id.skill_base_f_button);
        skill.setSkillButton(button);

        skill = SkillFactory.getSkill("巨大化",hero, dialog);
        button = (Button)view.findViewById(R.id.skill_base_j_button);
        skill.setSkillButton(button);

        skill = SkillFactory.getSkill("定身",hero, dialog);
        button = (Button)view.findViewById(R.id.skill_base_d_button);
        skill.setSkillButton(button);

        skill = SkillFactory.getSkill("超能量",hero, dialog);
        button = (Button)view.findViewById(R.id.skill_base_c_button);
        skill.setSkillButton(button);
    }
}
