package com.example.dainq.smilenotes.ui.settings;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;

import com.example.dainq.smilenotes.common.Constant;

import nq.dai.smilenotes.R;

public class SettingAdapter extends RecyclerView.Adapter<SettingViewHolder> {
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
    public void onBindViewHolder(final SettingViewHolder holder, final int position) {
        holder.mSettingType.setText(mItem[position].mType);

//        if (position > Constant.SETTING_EVENT_NUMBER) {
        holder.mSwitch.setVisibility(View.GONE);
//        }

        holder.mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //TODO
            }
        });

        holder.mLayoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                holder.mSwitch.toggle();
                switch (position) {
                    case 0:
                        updateInfomation();
                        break;
                    case 1:
                        backupData();
                        break;
                    case 2:
                        aboutUs();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void backupData() {
        createDialog(mContext.getResources().getString(R.string.alert_anouncement_backup));
    }


    private void aboutUs() {
        final View view = View.inflate(mContext, R.layout.dialog_about_app, null);
        final AlertDialog.Builder builder;

        builder = new AlertDialog.Builder(mContext);
        builder.setPositiveButton("OK", null);
        builder.setView(view);
        builder.setCancelable(false);

        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
        alertDialog.show();
    }

    private void createDialog(String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(title)
                .setCancelable(false)
                .setPositiveButton(mContext.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void updateInfomation() {
        Intent intent = new Intent(mContext, UpdateInfoActivity.class);
        mContext.startActivity(intent);
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