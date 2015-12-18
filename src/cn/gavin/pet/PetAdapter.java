package cn.gavin.pet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Html;
import android.text.InputFilter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.gavin.Hero;
import cn.gavin.activity.MainGameActivity;

/**
 * Copyright 2015 gluo.
 * ALL RIGHTS RESERVED.
 * Created by gluo on 11/2/2015.
 */
public class PetAdapter extends BaseAdapter {

    private final AlertDialog farther;

    public PetAdapter(AlertDialog selectPetDialog) {
        adapterData = PetDB.loadPet(null);
        farther = selectPetDialog;

    }

    private List<Pet> adapterData;

    @Override
    public int getCount() {
        return adapterData != null ? adapterData.size() : 0;
    }

    @Override
    public Pet getItem(int position) {
        if (position >= getCount()) position = 0;
        return adapterData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        PetViewHolder holder;
        if (convertView == null) {
            holder = new PetViewHolder();
            convertView = holder.view;
            convertView.setTag(holder);
        } else {
            holder = (PetViewHolder) convertView.getTag();
        }
        Pet pet = getItem(position);
        holder.updatePet(pet);
        return convertView;
    }

    class PetViewHolder {
        private TextView view = new TextView(MainGameActivity.context);
        private Pet pet;

        public void updatePet(final Pet pet) {
            this.pet = pet;
            if (pet != null) {
                if (view == null) {
                    view = new TextView(MainGameActivity.context);
                }
                view.setTextSize(20);
                view.setText(Html.fromHtml(pet.getFormatName() + (pet.isOnUsed() ? "  √ " : "")));
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog changeName = new AlertDialog.Builder(MainGameActivity.context).create();
                        changeName.setTitle("输入一个前缀");
                        LinearLayout linearLayout = new LinearLayout(MainGameActivity.context);
                        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                        final EditText editText = new EditText(MainGameActivity.context);
                        editText.setText(pet.getFirstName());
                        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
                        linearLayout.addView(editText);
                        final TextView textView = new TextView(MainGameActivity.context);
                        textView.setText("的" + pet.getLastName());
                        linearLayout.addView(textView);
                        changeName.setView(linearLayout);
                        changeName.setButton(DialogInterface.BUTTON_POSITIVE, "确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                pet.setName(editText.getText().toString() + textView.getText());
                                pet.save();
                                farther.dismiss();
                            }
                        });
                        changeName.show();
                    }
                });
            }
        }
    }


}
