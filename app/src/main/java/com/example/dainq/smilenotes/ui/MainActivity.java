package com.example.dainq.smilenotes.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.IdRes;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dainq.smilenotes.common.BaseActivity;
import com.example.dainq.smilenotes.common.BaseFragment;
import com.example.dainq.smilenotes.common.Constant;
import com.example.dainq.smilenotes.common.Utility;
import com.example.dainq.smilenotes.controller.realm.RealmController;
import com.example.dainq.smilenotes.ui.create.CreateActivity;
import com.example.dainq.smilenotes.ui.home.HomeFragment;
import com.example.dainq.smilenotes.ui.notifications.NotificationFragment;
import com.example.dainq.smilenotes.ui.search.SearchFragment;
import com.example.dainq.smilenotes.ui.settings.SettingFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import nq.dai.smilenotes.R;

public class MainActivity extends BaseActivity implements OnTabSelectListener, View.OnClickListener {
    private BottomBarTab mNotificationTab;

    protected FragmentTransaction mFragment;
    private HomeFragment mHomeFragment;
    private NotificationFragment mNotificationFragment;
    private SettingFragment mSettingFragment;
    private SearchFragment mSearchFragment;

    private SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createRealmData();
        initView();
        initBottomView();
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

        FloatingActionButton mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.getDrawable().mutate().setTint(ContextCompat.getColor(this, R.color.white));
        mFab.setOnClickListener(this);

        mPref = this.getSharedPreferences(Constant.PREF_USER, Context.MODE_PRIVATE);
        boolean isFirst = mPref.getBoolean(Constant.USER_FIRST_USE, true);
        Log.d("dainq ", "isFirst " + isFirst);
        if (isFirst) {
            inputUserName();
        }
    }

    private void inputUserName() {
        final View view = getLayoutInflater().inflate(R.layout.dialog_input_name, null);
        final AlertDialog.Builder builder;
        final EditText editName = (EditText) view.findViewById(R.id.dialog_input_name);

        builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Đồng ý", null);
        builder.setView(view);
        builder.setCancelable(false);
        builder.setTitle("Nhập tên của bạn");

        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = editName.getText().toString();
                        if (!Utility.isEmptyString(name)) {
                            mPref.edit().putString(Constant.USER_NAME, name).apply();
                            mPref.edit().putBoolean(Constant.USER_FIRST_USE, false).apply();
                            mHomeFragment.getmNameUser().setText(name);
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.enter_your_name_touse_app, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        alertDialog.show();
    }

    private void initBottomView() {
        BottomBar mBottomBar = (BottomBar) findViewById(R.id.bottomBar);
        mBottomBar.setOnTabSelectListener(this);
        mNotificationTab = mBottomBar.getTabWithId(R.id.tab_notifications);
    }

    private void updateBadge() {
        RealmController mRealmController = RealmController.with(this);
        Calendar calendar = Calendar.getInstance();
        Date date = Utility.resetCalendar(calendar);

        RealmResults results = mRealmController.getNotificationUnread(date);
        int badge = results.size();
        Log.d("dainq", " number bage: " + badge);

        if (badge > 0) {
            mNotificationTab.setBadgeCount(badge);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateBadge();
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