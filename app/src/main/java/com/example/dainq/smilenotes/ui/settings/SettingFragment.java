package com.example.dainq.smilenotes.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nq.dai.smilenotes.R;

public class SettingFragment extends Fragment {
    private Context mContext;
    private SharedPreferences mPref;
    private SettingAdapter mAdapter;
    private RecyclerView mList;

    public SettingFragment(Context context) {
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initView(view);
        super.onViewCreated(view, savedInstanceState);
    }

    private void initView(View view) {
        mPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        mList = (RecyclerView) view.findViewById(R.id.settings_list);

        String[] setting = mContext.getResources().getStringArray(R.array.array_setting_list);
        SettingItem[] settingItems = new SettingItem[setting.length];
        for (int i = 0; i < setting.length; i++) {
            settingItems[i] = new SettingItem();
            settingItems[i].mType = setting[i];
        }

        mAdapter = new SettingAdapter(mContext, settingItems, mPref);
        mAdapter.setHasStableIds(true);
        mList.setLayoutManager(new LinearLayoutManager(mContext));
        mList.setAdapter(mAdapter);
    }
}
