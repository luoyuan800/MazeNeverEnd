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
        Skill r = SkillFactory.getSkill("勇者之击", hero, dialog);
        Button rB = (Button) view.findViewById(R.id.base_skill_r_button);
        r.setSkillButton(rB);

        Skill s = SkillFactory.getSkill("闪避",hero, dialog);
        Button sB = (Button)view.findViewById(R.id.base_skill_s_button);
        r.setSkillButton(sB);

        Skill t = SkillFactory.getSkill("铁拳",hero, dialog);
        Button tB = (Button)view.findViewById(R.id.skill_base_t_button);
        t.setSkillButton(tB);
    }
}
