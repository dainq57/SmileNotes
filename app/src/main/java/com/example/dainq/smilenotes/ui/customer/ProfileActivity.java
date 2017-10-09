package com.example.dainq.smilenotes.ui.customer;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.dainq.smilenotes.common.Constant;
import com.example.dainq.smilenotes.controller.realm.RealmController;
import com.example.dainq.smilenotes.model.CustomerObject;
import com.example.dainq.smilenotes.ui.create.CreateActivity;

import nq.dai.smilenotes.R;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private Bundle mExtras;
    private int mId;
    private CustomerObject mCustomer;
    private RealmController mRealmController;

    private String mName;
    private TextView mAda;
    private RatingBar mRating;
    private FrameLayout mButtonEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initView();
    }

    private void initView() {
        mAda = (TextView) findViewById(R.id.profile_ada);
        mRating = (RatingBar) findViewById(R.id.profile_rating);
        mButtonEdit = (FrameLayout) findViewById(R.id.profile_btn_edit);
    }

    private void onUpdate() {
        getCustomerObject();
        mAda.setText(getResources().getString(R.string.ada_code, mCustomer.getAda()));
        mRating.setRating(mCustomer.getLevel() + 1);
        mButtonEdit.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onUpdate();
        initToolBar();
        initViewPager();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getCustomerObject() {
        mExtras = getIntent().getExtras();
        if (mExtras != null) {
            mId = mExtras.getInt(Constant.KEY_ID_CUSTOMER);
            Log.d(ProfileActivity.class.getSimpleName() + "-dainq", "mId: " + mId);
            mRealmController = RealmController.with(this);
            mCustomer = mRealmController.getCustomer(mId);
        }
    }

    private void initToolBar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.tw_ic_ab_back_mtrl);
        mToolbar.getNavigationIcon().setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.MULTIPLY);

        if (mExtras != null) {
            mName = mCustomer.getName();
            if (getSupportActionBar() != null) getSupportActionBar().setTitle(mName);
        }
    }

    private void initViewPager() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ProfileInfoFragment(this, mCustomer), getString(R.string.tab_info));
        adapter.addFragment(new ProfileProblemFragment(this, mCustomer), getString(R.string.tab_problem));
        adapter.addFragment(new ProfilePlanFragment(this), getString(R.string.tab_plan));
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.profile_btn_edit:
                startEdit();
                break;
            default:
                break;
        }
    }

    private void startEdit() {
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.KEY_ACTION, Constant.ACTION_EDIT);
        bundle.putInt(Constant.KEY_ID_CUSTOMER, mId);

        Intent intent = new Intent(this, CreateActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
