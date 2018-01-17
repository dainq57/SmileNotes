package com.example.dainq.smilenotes.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dainq.smilenotes.common.Constant;
import com.example.dainq.smilenotes.controllers.realm.RealmController;
import com.example.dainq.smilenotes.common.SessionManager;
import com.example.dainq.smilenotes.model.ProductObject;
import com.example.dainq.smilenotes.ui.common.spinner.OnSpinnerItemSelectedListener;
import com.example.dainq.smilenotes.ui.common.spinner.SingleSpinnerLayout;
import com.example.dainq.smilenotes.ui.common.spinner.SpinnerItem;
import com.example.dainq.smilenotes.ui.profile.customer.ListCustomerActivity;
import com.example.dainq.smilenotes.ui.profile.product.ProductAdapter;
import com.example.dainq.smilenotes.ui.profile.product.RealmProductAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import im.dacer.androidcharts.PieHelper;
import im.dacer.androidcharts.PieView;
import io.realm.RealmResults;
import io.realm.Sort;
import nq.dai.smilenotes.R;

public class HomeFragment extends Fragment implements OnSpinnerItemSelectedListener, View.OnClickListener {
    private String TAG = "HomeFragment";

    private Context mContext;
    private ProductAdapter mAdapter;
    private RealmController mRealmController;

    private TextView mCPotentialMonth;
    private TextView mCPotential;
    private TextView mCConsumer;
    private TextView mCDistribution;
    private CircleImageView mAvatarUser;
    private TextView mNameUser;
    private TextView mTextNotResults;
    private SingleSpinnerLayout mSpinner;
    private PieView mPieView;

    private LinearLayout mLayoutSumary;
    private LinearLayout mLayoutPie;
    private TextView mTxtSumary;
    private TextView mTxtPie;

    private int potetialMonth;
    private int potetial;
    private int consumer;
    private int distribution;

    private SharedPreferences mPref;
    private SessionManager mSession;

    public HomeFragment(Context context, SessionManager session) {
        mContext = context;
        mSession = session;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        initView(view);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initView(View view) {
        mPieView = (PieView) view.findViewById(R.id.pie_view);
        initSummaryView(view);

        RecyclerView mListCustomer = (RecyclerView) view.findViewById(R.id.home_list_customer);
        mListCustomer.setHasFixedSize(true);
        mListCustomer.setLayoutManager(new LinearLayoutManager(mContext));

        mAdapter = new ProductAdapter(mContext);
        mListCustomer.setAdapter(mAdapter);

        mRealmController = RealmController.with(this);
        initSpinner(view);

        mTextNotResults = (TextView) view.findViewById(R.id.home_list_not_results);

        mTxtSumary = (TextView) view.findViewById(R.id.home_btn_sumary);
        mTxtSumary.setOnClickListener(this);
        mTxtPie = (TextView) view.findViewById(R.id.home_btn_pie);
        mTxtPie.setOnClickListener(this);

        mLayoutSumary = (LinearLayout) view.findViewById(R.id.home_summary);
        mLayoutPie = (LinearLayout) view.findViewById(R.id.home_pie_chart);
    }

    private void initSummaryView(View view) {
        LinearLayout customerPotentialMonth = (LinearLayout) view.findViewById(R.id.home_customer_potential_month);
        customerPotentialMonth.setOnClickListener(this);

        LinearLayout customerPotential = (LinearLayout) view.findViewById(R.id.home_customer_potential);
        customerPotential.setOnClickListener(this);

        LinearLayout customerConsumer = (LinearLayout) view.findViewById(R.id.home_customer_consumer);
        customerConsumer.setOnClickListener(this);

        LinearLayout customerDistribution = (LinearLayout) view.findViewById(R.id.home_customer_distribution);
        customerDistribution.setOnClickListener(this);

        mCPotentialMonth = (TextView) view.findViewById(R.id.home_customer_potential_month_number);
        mCPotential = (TextView) view.findViewById(R.id.home_customer_potential_number);
        mCConsumer = (TextView) view.findViewById(R.id.home_customer_consumer_number);
        mCDistribution = (TextView) view.findViewById(R.id.home_customer_distribution_number);

        mAvatarUser = (CircleImageView) view.findViewById(R.id.home_avatar_user);
        mNameUser = (TextView) view.findViewById(R.id.home_name_user);

        //get name user from session afrer login
        mNameUser.setText(mSession.getUserDetails().getFullName());

        mPref = getActivity().getSharedPreferences(Constant.PREF_USER, Context.MODE_PRIVATE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setDataPieView(PieView pieView) {
        int sum = potetial + consumer + distribution;
        float pote = 0, cons = 0, dis = 0;
        if (sum != 0) {
            pote = 100 * potetial / sum;
            cons = 100 * consumer / sum;
            dis = 100 * distribution / sum;
        }
        ArrayList<PieHelper> pieHelperArrayList = new ArrayList<>();
        pieHelperArrayList.add(new PieHelper(pote, mContext.getResources().getColor(R.color.color_piechart_level_1, null)));
        pieHelperArrayList.add(new PieHelper(cons, mContext.getResources().getColor(R.color.color_piechart_level_3, null)));
        pieHelperArrayList.add(new PieHelper(dis, mContext.getResources().getColor(R.color.color_piechart_level_4, null)));

        pieView.setDate(pieHelperArrayList);
        pieView.setOnPieClickListener(new PieView.OnPieClickListener() {
            @Override
            public void onPieClick(int index) {
                switch (index) {
                    case 0:
                        openListCustomer(Constant.CUSTOMER_TYPE_NEW);
                        break;
                    case 1:
                        openListCustomer(Constant.CUSTOMER_TYPE_CONSUMER);
                        break;
                    case 2:
                        openListCustomer(Constant.CUSTOMER_TYPE_DISTRIBUTION);
                        break;
                }
            }
        });
    }

    private void setDataSumary() {
        mCPotentialMonth.setText(potetialMonth > 10 ? ("" + potetialMonth) : ("0" + potetialMonth));
        mCPotential.setText(potetial > 10 ? ("" + potetial) : ("0" + potetial));
        mCConsumer.setText(consumer > 10 ? ("" + consumer) : ("0" + consumer));
        mCDistribution.setText(distribution > 10 ? ("" + distribution) : ("0" + distribution));
    }

    private void calculatorPercent() {
        //Need change to potential of month
        Calendar calendar = Calendar.getInstance();
        Date end = calendar.getTime();

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
        String day = dateFormat.format(end);
        int temp = Integer.parseInt(day);

        //get start date before from today
        calendar.add(Calendar.DAY_OF_MONTH, -(temp - 1));
        Date start = calendar.getTime();

        potetialMonth = mRealmController.getCountCustomer(Constant.CUSTOMER_DATE_CREATE, start, end);
        potetial = mRealmController.getCountCustomer(Constant.CUSTOMER_TYPE_NEW);
        consumer = mRealmController.getCountCustomer(Constant.CUSTOMER_TYPE_CONSUMER);
        distribution = mRealmController.getCountCustomer(Constant.CUSTOMER_TYPE_DISTRIBUTION);
    }

    private void openListCustomer(int id) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.CUSTOMER_LEVEL, id);

        Intent intent = new Intent(mContext, ListCustomerActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void initSpinner(View view) {
        mSpinner = (SingleSpinnerLayout) view.findViewById(R.id.home_spinner_select_number);

        if (mSpinner != null) {
            String[] menuItem = getResources().getStringArray(R.array.array_spinner_home);
            mSpinner.setSpinnerList(SpinnerItem.getSpinnerItem(menuItem));
            mSpinner.setOnSpinnerItemSelectedListener(this);
            mSpinner.getSpinner().setDropDownHorizontalOffset(mContext.getResources().getDimensionPixelSize(R.dimen.dropdown_offset_vertical));
        }
    }

    @Override
    public void onItemSelected(int position) {
        Log.d("dainq ", "spinner " + position);
        sortCustomerBetween(position);
    }

    @Override
    public void onNothingSelected() {
    }

    private void sortCustomerBetween(int type) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        Date start = calendar.getTime();

        //Seclect list in 1 week or 1 month from today
        if (type == 0) {
            calendar.add(Calendar.DAY_OF_MONTH, 8);
        } else {
            calendar.add(Calendar.DAY_OF_MONTH, 31);
        }
        Date end = calendar.getTime();
        RealmResults<ProductObject> mRealmResult = mRealmController.getProductBetween(start, end, Sort.ASCENDING);
        Log.d(TAG, "realmResult: " + mRealmResult.size());

        if (!mRealmResult.isEmpty()) {
            RealmProductAdapter realmAdapter = new RealmProductAdapter(mContext, mRealmResult, true);
            mAdapter.setRealmAdapter(realmAdapter);
            mAdapter.notifyDataSetChanged();
            mTextNotResults.setVisibility(View.GONE);
        } else {
            mTextNotResults.setVisibility(View.VISIBLE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onResume() {
        super.onResume();
        mRealmController = new RealmController(mContext);
        mSpinner.setSelection(0);

        //reset avatar and name after change information

//        String avatar = mPref.getString(Constant.USER_AVATAR, "");
//        mAvatarUser.setImageBitmap(Utility.decodeImage(avatar));

        String name = mSession.getUserDetails().getFullName();
        mNameUser.setText(name);

        calculatorPercent();
        setDataSumary();
        setDataPieView(mPieView);

        sortCustomerBetween(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_customer_potential:
                openListCustomer(Constant.CUSTOMER_TYPE_NEW);
                break;

            case R.id.home_customer_potential_month:
                openListCustomer(Constant.CUSTOMER_TYPE_NEW_MONTH);
                break;

            case R.id.home_customer_consumer:
                openListCustomer(Constant.CUSTOMER_TYPE_CONSUMER);
                break;

            case R.id.home_customer_distribution:
                openListCustomer(Constant.CUSTOMER_TYPE_DISTRIBUTION);
                break;

            case R.id.home_btn_pie:
                switchView(false, mTxtPie, mTxtSumary);
                break;

            case R.id.home_btn_sumary:
                switchView(true, mTxtSumary, mTxtPie);
                break;

            default:
                break;
        }
    }

    private void switchView(boolean value, TextView btnSwitch, TextView btnHide) {
        btnSwitch.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
        btnHide.setTextColor(mContext.getResources().getColor(R.color.border_color));

        if (value) {
            mLayoutSumary.setVisibility(View.VISIBLE);
            mLayoutPie.setVisibility(View.GONE);
        } else {
            mLayoutSumary.setVisibility(View.GONE);
            mLayoutPie.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        mRealmController.close();
        super.onDestroy();
    }
}