package cn.gavin.skill.system;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import cn.gavin.Hero;
import cn.gavin.activity.R;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.skill.Skill;
import cn.gavin.skill.SkillDialog;
import cn.gavin.skill.SkillFactory;

/**
 * Created by luoyuan on 9/28/15.
 */
public class EvilSkill extends SkillLayout {
    private View view;
    private Context context;
    private Hero hero = MainGameActivity.context.getHero();

    public EvilSkill(Context context) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.skill_layout_devils, (ViewGroup) this.findViewById(R.id.skill_dialog));
        this.addView(view);
        this.view = view;
        this.context = context;
    }

    public void init(SkillDialog dialog) {
        Skill skill = SkillFactory.getSkill("魔王天赋", hero, dialog);
        Button button = (Button) view.findViewById(R.id.skill_evil_m_button);
        skill.setSkillButton(button);

        skill = SkillFactory.getSkill("腐蚀", hero, dialog);
        button = (Button) view.findViewById(R.id.skill_evil_f_button);
        skill.setSkillButton(button);

        skill = SkillFactory.getSkill("强化", hero, dialog);
        button = (Button) view.findViewById(R.id.skill_evil_q_button);
        skill.setSkillButton(button);

        skill = SkillFactory.getSkill("迷雾", hero, dialog);
        button = (Button) view.findViewById(R.id.skill_evil_mw_button);
        skill.setSkillButton(button);

        skill = SkillFactory.getSkill("闪电", hero, dialog);
        button = (Button) view.findViewById(R.id.skill_evil_s_button);
        skill.setSkillButton(button);

        skill = SkillFactory.getSkill("隐身", hero, dialog);
        button = (Button) view.findViewById(R.id.skill_evil_y_button);
        skill.setSkillButton(button);

        skill = SkillFactory.getSkill("水波", hero, dialog);
        button = (Button) view.findViewById(R.id.skill_evil_sb_button);
        skill.setSkillButton(button);

        skill = SkillFactory.getSkill("精神力", hero, dialog);
        button = (Button) view.findViewById(R.id.skill_evil_j_button);
        skill.setSkillButton(button);

        skill = SkillFactory.getSkill("反杀", hero, dialog);
        button = (Button) view.findViewById(R.id.skill_evil_fs_button);
        skill.setSkillButton(button);

        skill = SkillFactory.getSkill("不死之身", hero, dialog);
        button = (Button) view.findViewById(R.id.skill_evil_b_button);
        skill.setSkillButton(button);

        skill = SkillFactory.getSkill("变身", hero, dialog);
        button = (Button) view.findViewById(R.id.skill_evil_bs_button);
        skill.setSkillButton(button);

        skill = SkillFactory.getSkill("生命吸收", hero, dialog);
        button = (Button) view.findViewById(R.id.skill_evil_sm_button);
        skill.setSkillButton(button);

        skill = SkillFactory.getSkill("多重攻击", hero, dialog);
        button = (Button) view.findViewById(R.id.skill_evil_mul_button);
        skill.setSkillButton(button);
    }
}
