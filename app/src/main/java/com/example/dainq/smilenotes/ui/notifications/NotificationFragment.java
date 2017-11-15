package com.example.dainq.smilenotes.ui.notifications;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.example.dainq.smilenotes.common.BaseFragment;
import com.example.dainq.smilenotes.common.Utility;
import com.example.dainq.smilenotes.controller.realm.RealmController;
import com.example.dainq.smilenotes.model.NotificationObject;

import java.util.Calendar;
import java.util.Date;

import io.realm.RealmResults;
import nq.dai.smilenotes.R;

public class NotificationFragment extends BaseFragment implements View.OnClickListener {
    private Context mContext;
    private NotificationAdapter mAdapter;
    private RealmController mRealmController;
    private TextView mTextNotResult;
    private RealmResults<NotificationObject> mRealmResults;

    public NotificationFragment(Context context) {
        mContext = context;
    }

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        RecyclerView mListNotification = (RecyclerView) view.findViewById(R.id.list_notification);
        mListNotification.setHasFixedSize(true);
        mListNotification.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new NotificationAdapter(mContext);
        mAdapter.setHasStableIds(true);
        mListNotification.setAdapter(mAdapter);

        mTextNotResult = (TextView) view.findViewById(R.id.notification_not_result);
        TextView mDeleteNoti = (TextView) view.findViewById(R.id.notification_delete_notification);
        mDeleteNoti.setOnClickListener(this);
    }

    private void getData() {
        mRealmController = RealmController.with(this);
        Calendar calendar = Calendar.getInstance();
        Date date = Utility.resetCalendar(calendar);
        Log.d("dainq date today ", "" + date);
        mRealmResults = mRealmController.getNotificationToday(date);

        Log.d("dainq ", "empty? " + mRealmResults.isEmpty());
        if (mRealmResults.isEmpty()) {
            mTextNotResult.setVisibility(View.VISIBLE);
        } else {
            mTextNotResult.setVisibility(View.GONE);
            RealmNotificationAdapter realmAdapter = new RealmNotificationAdapter(mContext, mRealmResults, true);
            mAdapter.setRealmAdapter(realmAdapter);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.notification_delete_notification:
                if (!mRealmResults.isEmpty()) {
                    deleteAllNotification();
                    Log.d("dainq ", "delete all notification");
                } else {
                    Toast.makeText(mContext, "Không có thông báo!", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;
        }
    }

    private void deleteAllNotification() {
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.title_dialog_delete_notification)
                .setMessage(R.string.dialog_delete_content_notification)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mRealmController.clearAllNotification();
                        mAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}