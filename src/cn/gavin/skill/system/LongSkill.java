package cn.gavin.skill.system;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.gavin.R;
import cn.gavin.skill.SkillDialog;

/**
 * Created by luoyuan on 9/13/15.
 */
public class LongSkill extends  SkillLayout {
    public LongSkill(Context context) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.skill_layout_long, (ViewGroup) this.findViewById(R.id.skill_dialog));
        this.addView(view);
    }

    public void init(SkillDialog dialog){

    }
}
