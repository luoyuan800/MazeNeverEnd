package cn.gavin.pet.swop.ui;

import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.gavin.R;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.pet.Pet;
import cn.gavin.utils.MazeContents;
import cn.gavin.utils.StringUtils;

/**
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED
 * Created by luoyuan on 12/2/15.
 */
public class PetSimpleViewAdapter extends BaseAdapter {
    private View.OnClickListener swapDialog;
    public PetSimpleViewAdapter(View.OnClickListener swapDialog){
        this.swapDialog = swapDialog;
    }

    private final ArrayList<Pet> pets = new ArrayList<Pet>();

    public void addPets(Pet...pets){
        for(Pet p : pets){
            if(p!=null){
                this.pets.add(p);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return pets.size();
    }

    @Override
    public Pet getItem(int i) {
        return pets.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i*4;
    }

    @Override
    public View getView(int index, View view, ViewGroup viewGroup) {
        PetViewHolder holder;
        if(view == null){
            holder = new PetViewHolder(swapDialog);
            view = holder.view;
            view.setTag(holder);
        }else{
           holder = (PetViewHolder)view.getTag();
        }
        Pet pet = getItem(index);
        holder.updatePet(pet);
        return view;
    }

    public static class PetViewHolder{
        private View view;
        private ImageView petImage;
        private TextView name;
        private TextView hp;
        private TextView atk;
        private TextView def;
        private Pet pet;
        private View.OnClickListener swapDialog;

        private PetViewHolder(View.OnClickListener dialog) {
            this.swapDialog = dialog;
            view = View.inflate(MainGameActivity.context,
                    R.layout.pet_simple_view, null);
            if (view != null) {
                petImage = (ImageView) view.findViewById(R.id.pet_image);
                name = (TextView) view.findViewById(R.id.pet_name);
                hp = (TextView) view.findViewById(R.id.pet_hp);
                atk = (TextView) view.findViewById(R.id.pet_atk);
                def = (TextView) view.findViewById(R.id.pet_def);
                view.setOnClickListener(swapDialog);
                view.setTag(this);
            }
        }

        public void updatePet(final Pet... pets) {
            if (pets.length > 0 && pets[0]!=null) {
                pet = pets[0];
                name.setText(Html.fromHtml(pets[0].getFormatName()));
                name.setVisibility(View.VISIBLE);
                hp.setText("HP:" + StringUtils.formatNumber(pets[0].getUHp()));
                hp.setVisibility(View.VISIBLE);
                atk.setText("ATK:" + StringUtils.formatNumber(pets[0].getMaxAtk()));
                atk.setVisibility(View.VISIBLE);
                def.setText("DEF:" + StringUtils.formatNumber(pets[0].getMaxDef()));
                def.setVisibility(View.VISIBLE);
                petImage.setVisibility(View.VISIBLE);
                petImage.setImageResource(pet.getImage());
            } else {
                pet = null;
                name.setText("");
                name.setVisibility(View.GONE);
                hp.setVisibility(View.GONE);
                atk.setVisibility(View.GONE);
                def.setVisibility(View.GONE);
                petImage.setVisibility(View.GONE);
            }
            if("蛋".equals(pet.getType())){
                hp.setVisibility(View.GONE);
                atk.setVisibility(View.GONE);
                def.setVisibility(View.GONE);
                pet.setImage(R.drawable.egg);
            }
        }

        public Pet getPet() {
            return pet;
        }
    }
}
