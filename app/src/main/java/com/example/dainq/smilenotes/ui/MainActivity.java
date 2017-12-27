package com.example.dainq.smilenotes.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.IdRes;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dainq.smilenotes.common.Constant;
import com.example.dainq.smilenotes.common.Utility;
import com.example.dainq.smilenotes.controller.realm.RealmController;
import com.example.dainq.smilenotes.ui.create.CreateActivity;
import com.example.dainq.smilenotes.ui.home.HomeFragment;
import com.example.dainq.smilenotes.ui.notifications.NotificationFragment;
import com.example.dainq.smilenotes.ui.other.GoldKeyFragment;
import com.example.dainq.smilenotes.ui.other.PigFragment;
import com.example.dainq.smilenotes.ui.search.SearchActivity;
import com.example.dainq.smilenotes.ui.settings.SettingFragment;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import nq.dai.smilenotes.R;

public class MainActivity extends AppCompatActivity implements OnTabSelectListener, View.OnClickListener {
    private BottomBarTab mNotificationTab;

    protected FragmentTransaction mFragment;
    private HomeFragment mHomeFragment;
    private NotificationFragment mNotificationFragment;
    private SettingFragment mSettingFragment;
    private GoldKeyFragment mGoldKeyFragment;
    private PigFragment mPigFragment;

    private FloatingActionMenu mFab;

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
        mGoldKeyFragment = new GoldKeyFragment(this);
        mPigFragment = new PigFragment(this);

        mFragment = getSupportFragmentManager().beginTransaction();
        mFragment.add(R.id.fragment, mHomeFragment);
        mFragment.commit();

        mFab = (FloatingActionMenu) findViewById(R.id.fab);
        mFab.setClosedOnTouchOutside(true);

        FloatingActionButton mFabAdd = (FloatingActionButton) findViewById(R.id.fab_add);
        mFabAdd.setOnClickListener(this);

        FloatingActionButton mFabSearch = (FloatingActionButton) findViewById(R.id.fab_search);
        mFabSearch.setOnClickListener(this);

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
        updateBadge();
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
    public void onTabSelected(@IdRes int tabId) {
        switch (tabId) {
            case R.id.tab_home:
                replaceFragment(mHomeFragment);
                break;

            case R.id.tab_gold_key:
                replaceFragment(mGoldKeyFragment);
                break;

            case R.id.tab_notifications:
                replaceFragment(mNotificationFragment);
                mNotificationTab.removeBadge();
                break;

            case R.id.tab_pig:
                replaceFragment(mPigFragment);
                break;

            case R.id.tab_settings:
                replaceFragment(mSettingFragment);
                break;

            default:
                break;
        }
    }

    @SuppressLint("CommitTransaction")
    private void replaceFragment(Fragment fragment) {
        mFragment = getSupportFragmentManager().beginTransaction();
        mFragment.replace(R.id.fragment, fragment);
        mFragment.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_add:
                startCreate();
                mFab.close(true);
                break;

            case R.id.fab_search:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                mFab.close(true);
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