package com.example.dainq.smilenotes.ui.settings;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import nq.dai.smilenotes.R;

public class SettingViewHolder extends RecyclerView.ViewHolder {
    public LinearLayout mLayoutItem;
    public TextView mSettingType;
    public ImageView mIconSetting;

    SettingViewHolder(View view) {
        super(view);

        mIconSetting = (ImageView) view.findViewById(R.id.setting_image);
        mLayoutItem = (LinearLayout) view.findViewById(R.id.setting_lay);
        mSettingType = (TextView) view.findViewById(R.id.setting_type);
    }
}