package com.example.dainq.smilenotes.ui.notifications;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.dainq.smilenotes.common.BaseFragment;
import com.example.dainq.smilenotes.common.Utility;

import nq.dai.smilenotes.R;


public class NotificationFragment extends BaseFragment {
    private Context mContext;
    private RecyclerView mListNotification;
    private NotificationAdapter mAdapter;
    private Button mCreate;

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
        mListNotification = (RecyclerView) view.findViewById(R.id.list_notification);
        mListNotification.setHasFixedSize(true);

        mListNotification.setLayoutManager(new LinearLayoutManager(mContext));

        mAdapter = new NotificationAdapter(mContext, Utility.createList(20, "Notification"));
        mAdapter.setHasStableIds(true);
        mListNotification.setLayoutManager(new LinearLayoutManager(mContext));
        mListNotification.setAdapter(mAdapter);
    }
}
