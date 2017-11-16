package com.example.dainq.smilenotes.ui.profile;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.dainq.smilenotes.common.Constant;
import com.example.dainq.smilenotes.common.Utility;
import com.example.dainq.smilenotes.controller.realm.RealmController;
import com.example.dainq.smilenotes.model.CustomerObject;
import com.example.dainq.smilenotes.ui.create.CreateActivity;

import de.hdodenhof.circleimageview.CircleImageView;
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
    private TextView mDateCreate;
    private CircleImageView mAvatar;


    private int type;

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
        mDateCreate = (TextView) findViewById(R.id.profile_date_create);
        mAvatar = (CircleImageView) findViewById(R.id.profile_avatar);
    }

    private void onUpdate() {
        getCustomerObject();
        if (mCustomer.getLevel() > Constant.CUSTOMER_LEVEL_2) {
            String ada = mCustomer.getAda();
            if (ada == null) {
                mAda.setText(getResources().getString(R.string.ada_code, ""));
            } else {
                mAda.setText(getResources().getString(R.string.ada_code, mCustomer.getAda()));
            }
            mAda.setVisibility(View.VISIBLE);
        } else {
            mAda.setVisibility(View.GONE);
        }
        mRating.setRating(mCustomer.getLevel() + 1);
        mButtonEdit.setOnClickListener(this);
        String date = Utility.dateToString(mCustomer.getDatecreate());
        mDateCreate.setText(getResources().getString(R.string.date_create, date));

        String avatar = mCustomer.getAvatar();
        if (avatar != null) {
            mAvatar.setImageBitmap(Utility.decodeImage(avatar));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        onUpdate();
        initToolBar();
        initViewPager();

//        RealmResults<NotificationObject> notificationObjects = mRealmController.getNotificationOfCustomer(mCustomer.getId());
//        Log.d("dainq size notification ", "" + notificationObjects.size());
//        for (int i = 0; i < notificationObjects.size(); i++) {
//            Log.d("dainq noti: ", notificationObjects.get(i).getDatevalue() + "");
//        }
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
            mId = mExtras.getInt(Constant.KEY_ID);
            type = mExtras.getInt(Constant.KEY_TYPE_PRODILE);
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
        if (type == Constant.PROFILE_TYPE_PRODUCT) {
            viewPager.setCurrentItem(3);
        }
        if (type == Constant.PROFILE_TYPE_PLAN) {
            viewPager.setCurrentItem(2);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ProfileInfoFragment(this, mCustomer), getString(R.string.tab_info));
        adapter.addFragment(new ProfileProblemFragment(this, mCustomer), getString(R.string.tab_problem));
        adapter.addFragment(new ProfilePlanFragment(this, mCustomer), getString(R.string.tab_plan));
        ProfileProductFragment fragmentProduct = new ProfileProductFragment(this, mCustomer);
        if (mCustomer.getLevel() > Constant.CUSTOMER_LEVEL_0) {
            adapter.addFragment(fragmentProduct, getString(R.string.tab_product));
        }
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
        bundle.putInt(Constant.KEY_ID, mId);

        Intent intent = new Intent(this, CreateActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        mRealmController.close();
        super.onDestroy();
    }
}
