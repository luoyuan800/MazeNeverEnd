package cn.gavin.skill.system;

import android.content.Context;
import android.view.View;
import android.widget.ScrollView;
import cn.gavin.Hero;
import cn.gavin.utils.MazeContents;

/**
 * Created by luoyuan on 9/13/15.
 */
public class SkillLayout extends ScrollView {
    protected View view;
    protected Context context;
    protected Hero hero = MazeContents.hero;

    public SkillLayout(Context context) {
        super(context);
    }
}
