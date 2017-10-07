package com.example.dainq.smilenotes.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.example.dainq.smilenotes.common.Constant;

import nq.dai.smilenotes.R;

class SettingAdapter extends RecyclerView.Adapter<SettingViewHolder> {
    private Context mContext;
    private SettingItem[] mItem;
    private SharedPreferences mPref;

    SettingAdapter(Context context, SettingItem[] item, SharedPreferences pref) {
        mContext = context;
        mItem = item;
        mPref = pref;
    }

    @Override
    public SettingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_setting, parent, false);
        return new SettingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SettingViewHolder holder, int position) {
        holder.mSettingType.setText(mItem[position].mType);

        if (position > Constant.SETTING_EVENT_NUMBER) {
            holder.mSwitch.setVisibility(View.GONE);
        }

        holder.mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //TODO
            }
        });

        holder.mLayoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mSwitch.toggle();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItem.length;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
