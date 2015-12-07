package cn.gavin.pet.swop.ui.net;

import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.gavin.R;
import cn.gavin.activity.MainGameActivity;
import cn.gavin.pet.swop.SwapPet;
import cn.gavin.utils.MazeContents;
import cn.gavin.utils.StringUtils;

/**
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED
 * Created by luoyuan on 12/2/15.
 */
public class NetPetSimpleViewAdapter extends BaseAdapter {
    private NetPetDialog netPetDialog;
    private final ArrayList<SwapPet> pets = new ArrayList<SwapPet>();

    public void clean(){
        pets.clear();
        notifyDataSetChanged();
    }

    public void addPets(SwapPet... pets) {
        for (SwapPet p : pets) {
            if (p != null) {
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
    public SwapPet getItem(int i) {

        return pets.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int index, View view, ViewGroup viewGroup) {
        PetViewHolder holder;
        if (view == null) {
            holder = new PetViewHolder(netPetDialog);
            view = holder.view;
            view.setTag(holder);
        } else {
            holder = (PetViewHolder) view.getTag();
        }
        SwapPet pet = getItem(index);
        holder.updatePet(pet);
        return view;
    }

    public NetPetDialog getNetPetDialog() {
        return netPetDialog;
    }

    public void setNetPetDialog(NetPetDialog netPetDialog) {
        this.netPetDialog = netPetDialog;
    }

    static class PetViewHolder {
        private View view;
        private ImageView petImage;
        private TextView name;
        private TextView hp;
        private TextView atk;
        private TextView def;
        private SwapPet pet;

        private PetViewHolder(View.OnClickListener listener) {
            view = View.inflate(MainGameActivity.context,
                    R.layout.pet_simple_view, null);
            if (view != null) {
                petImage = (ImageView) view.findViewById(R.id.pet_image);
                name = (TextView) view.findViewById(R.id.pet_name);
                hp = (TextView) view.findViewById(R.id.pet_hp);
                atk = (TextView) view.findViewById(R.id.pet_atk);
                def = (TextView) view.findViewById(R.id.pet_def);
                view.setTag(this);
                view.setOnClickListener(listener);
            }
        }

        public void updatePet(final SwapPet pet) {
            this.pet = pet;
            if (pet != null) {
                name.setText(Html.fromHtml(pet.getFormateName()));
                name.setVisibility(View.VISIBLE);
                hp.setText("HP:" + StringUtils.formatNumber(pet.getHp()));
                hp.setVisibility(View.VISIBLE);
                atk.setText("ATK:" + StringUtils.formatNumber(pet.getAtk()));
                atk.setVisibility(View.VISIBLE);
                def.setText("DEF:" + StringUtils.formatNumber(pet.getDef()));
                def.setVisibility(View.VISIBLE);
                petImage.setVisibility(View.VISIBLE);
                petImage.setImageResource((MazeContents.getImageByName(pet.getName(),
                        pet.getType() == Integer.MAX_VALUE-1 ? "蛋" : "")));
            } else {
                this.pet = null;
                name.setText("");
                name.setVisibility(View.GONE);
                hp.setVisibility(View.GONE);
                atk.setVisibility(View.GONE);
                def.setVisibility(View.GONE);
                petImage.setVisibility(View.GONE);
            }
            if ((null == pet.getType()) || (Integer.MAX_VALUE-1 == pet.getType())) {
                hp.setVisibility(View.GONE);
                atk.setVisibility(View.GONE);
                def.setVisibility(View.GONE);
            }
        }

        public SwapPet getPet() {
            return pet;
        }
    }
}
