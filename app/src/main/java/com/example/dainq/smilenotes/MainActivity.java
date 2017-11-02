package com.example.dainq.smilenotes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.dainq.smilenotes.common.BaseActivity;
import com.example.dainq.smilenotes.common.BaseFragment;
import com.example.dainq.smilenotes.common.Constant;
import com.example.dainq.smilenotes.common.Utility;
import com.example.dainq.smilenotes.ui.create.CreateActivity;
import com.example.dainq.smilenotes.ui.home.HomeFragment;
import com.example.dainq.smilenotes.ui.notifications.NotificationFragment;
import com.example.dainq.smilenotes.ui.search.SearchFragment;
import com.example.dainq.smilenotes.ui.settings.SettingFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;
import com.soundcloud.android.crop.Crop;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import nq.dai.smilenotes.R;

public class MainActivity extends BaseActivity implements OnTabSelectListener, View.OnClickListener {
    private BottomBarTab mNotificationTab;

    protected FragmentTransaction mFragment;
    private HomeFragment mHomeFragment;
    private NotificationFragment mNotificationFragment;
    private SettingFragment mSettingFragment;
    private SearchFragment mSearchFragment;

    private FloatingActionButton mFab;
    private SharedPreferences mPref;
    private boolean isFirst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initBottomView();
        createRealmData();
    }

    @SuppressLint("CommitTransaction")
    private void initView() {
        mHomeFragment = new HomeFragment(this);
        mNotificationFragment = new NotificationFragment(this);
        mSettingFragment = new SettingFragment(this);
        mSearchFragment = new SearchFragment(this);

        mFragment = getSupportFragmentManager().beginTransaction();
        mFragment.add(R.id.fragment, mHomeFragment);
        mFragment.commit();

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.getDrawable().mutate().setTint(ContextCompat.getColor(this, R.color.white));
        mFab.setOnClickListener(this);

        mPref = this.getSharedPreferences(Constant.PREF_USER, Context.MODE_PRIVATE);
        isFirst = mPref.getBoolean(Constant.PREF_USER_FIRST_USE, false);

        if (isFirst) {

        }
    }

    private void inputUserName() {

    }

    private void initBottomView() {
        BottomBar mBottomBar = (BottomBar) findViewById(R.id.bottomBar);
        mBottomBar.setOnTabSelectListener(this);

        mNotificationTab = mBottomBar.getTabWithId(R.id.tab_notifications);
//        mNotificationTab.setBadgeCount(3);
    }

    @Override
    public void onTabSelected(@IdRes int tabId) {
        switch (tabId) {
            case R.id.tab_home:
                replaceFragment(mHomeFragment);
                break;

            case R.id.tab_search:
                replaceFragment(mSearchFragment);
                break;

            case R.id.tab_notifications:
                replaceFragment(mNotificationFragment);
                mNotificationTab.removeBadge();
                break;

            case R.id.tab_settings:
                replaceFragment(mSettingFragment);
                break;

            default:
                break;
        }
    }

    @SuppressLint("CommitTransaction")
    private void replaceFragment(BaseFragment fragment) {
        mFragment = getSupportFragmentManager().beginTransaction();
        mFragment.replace(R.id.fragment, fragment);
        mFragment.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                startCreate();
                break;

            default:
                break;
        }
    }

    private void startCreate() {
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.KEY_ACTION, Constant.ACTION_CREATE);

        Intent intent = new Intent(this, CreateActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void createRealmData() {
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name(Constant.REALM_DATA_CUSTOMER_DEFAULT)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
