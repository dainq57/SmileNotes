package com.example.dainq.smilenotes.ui.settings;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.dainq.smilenotes.common.SessionManager;

import nq.dai.smilenotes.R;

public class SettingFragment extends Fragment {
    private Context mContext;
    private SharedPreferences mPref;
    private SessionManager mSession;

    private Activity mActivity;
    private SettingAdapter mAdapter;
    private RecyclerView mList;

    public SettingFragment(Context context, Activity activity, SessionManager sessionManager) {
        mContext = context;
        mActivity = activity;
        mSession = sessionManager;
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

        //list icon set into item of setting
        Drawable[] iconList = new Drawable[]{
                getResources().getDrawable(R.drawable.icn_profile_64),
                getResources().getDrawable(R.drawable.icn_key_64),
                getResources().getDrawable(R.drawable.icn_sync_data),
                getResources().getDrawable(R.drawable.icn_about_us),
                getResources().getDrawable(R.drawable.icn_power_off_64)};

        //list description set into description item of setting
        String[] descriptionList = mContext.getResources().getStringArray(R.array.array_setting_list);

        int length = descriptionList.length;
        //create list setting with description.lenght item
        SettingItem[] settingItems = new SettingItem[length];

        for (int i = 0; i < descriptionList.length; i++) {
            settingItems[i] = new SettingItem(descriptionList[i], iconList[i]);
        }

        mAdapter = new SettingAdapter(mContext, settingItems, mPref, mActivity, mSession);
        mAdapter.setHasStableIds(true);
        mList.setLayoutManager(new LinearLayoutManager(mContext));
        mList.setAdapter(mAdapter);
    }
}
