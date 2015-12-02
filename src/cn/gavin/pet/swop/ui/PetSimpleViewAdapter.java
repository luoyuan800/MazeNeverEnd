package cn.gavin.pet.swop.ui;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.gavin.R;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.pet.Pet;
import cn.gavin.utils.MazeContents;

/**
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED
 * Created by luoyuan on 12/2/15.
 */
public class PetSimpleViewAdapter extends BaseAdapter {
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Pet getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        PetViewHolder holder;
        if(view == null){
            holder = new PetViewHolder();
        }
        return view;
    }

    static class PetViewHolder {
        private View view;
        private ImageView petImage;
        private TextView name;
        private ImageView petImage1;
        private TextView name1;
        private ImageView petImage2;
        private TextView name2;
        private ImageView petImage3;
        private TextView name3;

        private PetViewHolder() {
            view = View.inflate(MainGameActivity.context,
                    R.layout.pet_simple_view, null);
            if (view != null) {
                petImage = (ImageView) view.findViewById(R.id.pet_image);
                name = (TextView) view.findViewById(R.id.pet_name);
                petImage1 = (ImageView) view.findViewById(R.id.pet_image_1);
                name1 = (TextView) view.findViewById(R.id.pet_name_1);
                petImage2 = (ImageView) view.findViewById(R.id.pet_image_2);
                name2 = (TextView) view.findViewById(R.id.pet_name_2);
                petImage3 = (ImageView) view.findViewById(R.id.pet_image_3);
                name3 = (TextView) view.findViewById(R.id.pet_name_3);
                view.setTag(this);
            }
        }

        public void updatePet(final Pet... pets) {
            if (pets.length > 0) {
                name.setText(pets[0].getFormatName());
                petImage.setVisibility(View.VISIBLE);
                petImage.setImageResource((MazeContents.getImageByName(pets[0].getName())));
            } else {
                name.setText("");
                petImage.setVisibility(View.GONE);
            }
            if (pets.length > 1) {
                name1.setText(pets[1].getFormatName());
                petImage1.setVisibility(View.VISIBLE);
                petImage1.setImageResource((MazeContents.getImageByName(pets[1].getName())));
            } else {
                name1.setText("");
                petImage1.setVisibility(View.GONE);
            }
            if (pets.length > 2) {
                name2.setText(pets[2].getFormatName());
                petImage2.setVisibility(View.VISIBLE);
                petImage2.setImageResource((MazeContents.getImageByName(pets[2].getName())));
            } else {
                name2.setText("");
                petImage2.setVisibility(View.GONE);
            }
            if (pets.length > 3) {
                name3.setText(pets[3].getFormatName());
                petImage3.setVisibility(View.VISIBLE);
                petImage3.setImageResource((MazeContents.getImageByName(pets[3].getName())));
            } else {
                name3.setText("");
                petImage3.setVisibility(View.GONE);
            }
        }
    }
}
