package com.example.dainq.smilenotes;

import android.annotation.SuppressLint;
import android.support.annotation.IdRes;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.example.dainq.smilenotes.common.BaseActivity;
import com.example.dainq.smilenotes.common.BaseFragment;
import com.example.dainq.smilenotes.ui.create.NewNoteFragment;
import com.example.dainq.smilenotes.ui.home.HomeFragment;
import com.example.dainq.smilenotes.ui.notifications.NotificationFragment;
import com.example.dainq.smilenotes.ui.search.SearchFragment;
import com.example.dainq.smilenotes.ui.settings.SettingsFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends BaseActivity implements OnTabSelectListener {
    private BottomBarTab mNotificationTab;

    private FragmentTransaction mFragment;
    private HomeFragment mHomeFragment;
    private NotificationFragment mNotificationFragment;
    private SettingsFragment mSettingsFragment;
    private NewNoteFragment mNewNoteFragment;
    private SearchFragment mSearchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initBottomView();
    }

    @SuppressLint("CommitTransaction")
    private void initView() {
        mHomeFragment = new HomeFragment();
        mNotificationFragment = new NotificationFragment();
        mSettingsFragment = new SettingsFragment();
        mNewNoteFragment = new NewNoteFragment();
        mSearchFragment = new SearchFragment();

        mFragment = getSupportFragmentManager().beginTransaction();
        mFragment.add(R.id.fragment, mHomeFragment);
        mFragment.commit();
    }

    private void initBottomView() {
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(this);

        mNotificationTab = bottomBar.getTabWithId(R.id.tab_notifications);
        mNotificationTab.setBadgeCount(3);
    }

    @Override
    public void onTabSelected(@IdRes int tabId) {
        switch (tabId) {
            case R.id.tab_home:
                replaceFragment(mHomeFragment);
                break;

            case R.id.tab_write:
                replaceFragment(mNewNoteFragment);
                break;

            case R.id.tab_search:
                replaceFragment(mSearchFragment);
                break;

            case R.id.tab_notifications:
                replaceFragment(mNotificationFragment);
                mNotificationTab.removeBadge();
                break;

            case R.id.tab_settings:
                replaceFragment(mSettingsFragment);
                break;

            default:
                break;
        }
    }

    @SuppressLint("CommitTransaction")
    private void replaceFragment(BaseFragment fragment) {
        mFragment = getSupportFragmentManager().beginTransaction();
        mFragment.replace(R.id.fragment, fragment);
//        mFragment.addToBackStack(null);
        mFragment.commit();
    }
}
