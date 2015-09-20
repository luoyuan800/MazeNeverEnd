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

        skill = SkillFactory.getSkill("瞬间移动",hero, dialog);
        button = (Button)view.findViewById(R.id.skill_base_sy_button);
        skill.setSkillButton(button);

        skill = SkillFactory.getSkill("斩击",hero, dialog);
        button = (Button)view.findViewById(R.id.skill_base_zj_button);
        skill.setSkillButton(button);

        skill = SkillFactory.getSkill("裂空剑",hero, dialog);
        button = (Button)view.findViewById(R.id.skill_base_lkj_button);
        skill.setSkillButton(button);

        skill = SkillFactory.getSkill("传送",hero, dialog);
        button = (Button)view.findViewById(R.id.skill_base_cs_button);
        skill.setSkillButton(button);

        skill = SkillFactory.getSkill("错位",hero, dialog);
        button = (Button)view.findViewById(R.id.skill_base_cw_button);
        skill.setSkillButton(button);

        skill = SkillFactory.getSkill("原能力",hero, dialog);
        button = (Button)view.findViewById(R.id.skill_base_ynl_button);
        skill.setSkillButton(button);

        skill = SkillFactory.getSkill("寻宝",hero, dialog);
        button = (Button)view.findViewById(R.id.skill_base_xb_button);
        skill.setSkillButton(button);

        skill = SkillFactory.getSkill("超防御",hero, dialog);
        button = (Button)view.findViewById(R.id.skill_base_cfy_button);
        skill.setSkillButton(button);

        skill = SkillFactory.getSkill("重击",hero, dialog);
        button = (Button)view.findViewById(R.id.skill_base_zj_button);
        skill.setSkillButton(button);
    }
}
