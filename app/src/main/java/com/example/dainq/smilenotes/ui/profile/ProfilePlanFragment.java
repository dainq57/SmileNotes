package com.example.dainq.smilenotes.ui.profile;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dainq.smilenotes.common.Constant;
import com.example.dainq.smilenotes.controllers.realm.RealmController;
import com.example.dainq.smilenotes.model.CustomerObject;
import com.example.dainq.smilenotes.model.MeetingObject;
import com.example.dainq.smilenotes.ui.profile.plan.CreatePlanDialog;
import com.example.dainq.smilenotes.ui.profile.plan.PlanAdapter;
import com.example.dainq.smilenotes.ui.profile.plan.RealmMeetingAdapter;

import io.realm.RealmResults;
import nq.dai.smilenotes.R;

public class ProfilePlanFragment extends Fragment implements View.OnClickListener {
    private String TAG = "ProfilePlanFragment";

    private Context mContext;
    private CustomerObject mCustomer;
    private CreatePlanDialog mDialog;

    private PlanAdapter mAdapter;
    private RealmController mRealmController;

    public ProfilePlanFragment(Context context) {
        mContext = context;
    }

    public ProfilePlanFragment(Context context, CustomerObject customer) {
        mContext = context;
        mCustomer = customer;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_plan, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        RelativeLayout mAddNew = (RelativeLayout) view.findViewById(R.id.profile_add_new);
        mAddNew.setOnClickListener(this);

        TextView buttonAdd = (TextView) view.findViewById(R.id.text_button_add_new);
        buttonAdd.setText(R.string.new_meeting);

        RecyclerView mListMeeting = (RecyclerView) view.findViewById(R.id.list_plan_meeting);
        mListMeeting.setHasFixedSize(true);
        mListMeeting.setLayoutManager(new LinearLayoutManager(mContext));

        mAdapter = new PlanAdapter(mContext);
        mListMeeting.setAdapter(mAdapter);

        mRealmController = RealmController.with(this);
        RealmResults<MeetingObject> realmResults = mRealmController.getMeetingOfCustomer(mCustomer.getId());
        RealmMeetingAdapter realmAdapter = new RealmMeetingAdapter(mContext, realmResults, true);
        mAdapter.setRealmAdapter(realmAdapter);
        mAdapter.notifyDataSetChanged();
        mAdapter.setAdapter(mAdapter);
        mDialog = new CreatePlanDialog(mContext, mAdapter, Constant.DIALOG_CREATE);
    }

    @Override
    public void onResume() {
        mRealmController = new RealmController(mContext);
        super.onResume();
    }

    private void showDialog() {
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.KEY_ID, mCustomer.getId());
        mDialog.setArguments(bundle);

        mDialog.show(getFragmentManager(), null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.profile_add_new:
                showDialog();
                break;

            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        mRealmController.close();
        super.onDestroy();
    }
}