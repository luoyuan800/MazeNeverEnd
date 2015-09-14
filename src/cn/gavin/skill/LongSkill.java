package cn.gavin.skill;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.gavin.R;

/**
 * Created by luoyuan on 9/13/15.
 */
public class LongSkill extends  SkillLayout {
    public LongSkill(Context context) {
        super(context);
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        LayoutInflater inflater = LayoutInflater.from(context);
        setLayoutParams(layoutParams);
        View view = inflater.inflate(R.layout.skill_layout_long, (ViewGroup) this.findViewById(R.id.skill_dialog));
        this.addView(view);
    }
}
