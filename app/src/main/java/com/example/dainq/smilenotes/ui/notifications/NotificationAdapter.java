package com.example.dainq.smilenotes.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dainq.smilenotes.common.Constant;
import com.example.dainq.smilenotes.common.Utility;
import com.example.dainq.smilenotes.controllers.realm.RealmController;
import com.example.dainq.smilenotes.model.CustomerObject;
import com.example.dainq.smilenotes.model.MeetingObject;
import com.example.dainq.smilenotes.model.NotificationObject;
import com.example.dainq.smilenotes.ui.common.realm.RealmRecyclerViewAdapter;
import com.example.dainq.smilenotes.ui.profile.ProfileActivity;

import nq.dai.smilenotes.R;

public class NotificationAdapter extends RealmRecyclerViewAdapter<NotificationObject> {
    private Context mContext;
    private RealmController mRealmController;

    public NotificationAdapter(Context context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        mRealmController = new RealmController(mContext);
        final NotificationObject notification = getItem(position);
        String date = Utility.dateToString(notification.getDate());

        NotificationViewHolder holder = (NotificationViewHolder) viewHolder;
        holder.mTime.setText(date);

        CustomerObject customerObject = mRealmController.getCustomer(notification.getIdcustomer());
        String name = customerObject.getName();

        int type = notification.getType();
        if (type == Constant.NOTIFICATION_EVENT) {
            MeetingObject meetingObject = mRealmController.getMetting(notification.getIdmeeting());
            String content = meetingObject.getContent();
            holder.mType.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_schedule));
            holder.mTextNotification.setText(notification.getContent() + name + ": " + content);
        } else {
            holder.mType.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_birthday));
            holder.mTextNotification.setText(notification.getContent() + name);
        }

        if (!notification.isread()) {
            holder.mContentLayout.setBackgroundColor(mContext.getResources().getColor(R.color.notification_unread));
        } else {
            holder.mContentLayout.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
        }

        holder.mBoundary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(Constant.KEY_ID, notification.getIdcustomer());
                Log.d("dainq ", "type noti " + notification.getType());
                if (notification.getType() == Constant.NOTIFICATION_EVENT) {
                    Log.d("dainq ", "plan");
                    bundle.putInt(Constant.KEY_TYPE_PRODILE, Constant.PROFILE_TYPE_PLAN);
                }
                Intent intent = new Intent(mContext, ProfileActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);

                if (!notification.isread()) {
                    mRealmController.updateNotiRead(notification);
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (getRealmAdapter() != null) {
            return getRealmAdapter().getCount();
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
