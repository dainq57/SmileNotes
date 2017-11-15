package com.example.dainq.smilenotes.ui.notifications;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import nq.dai.smilenotes.R;

public class NotificationViewHolder extends RecyclerView.ViewHolder {

    public LinearLayout mBoundary;
    public LinearLayout mContentLayout;
    public TextView mTextNotification;
    public TextView mTime;
    public FrameLayout mDivider;
    public ImageView mType;

    public NotificationViewHolder(View itemView) {
        super(itemView);
        mContentLayout = (LinearLayout) itemView.findViewById(R.id.notification_content);
        mBoundary = (LinearLayout) itemView.findViewById(R.id.notification_layout);
        mTextNotification = (TextView) itemView.findViewById(R.id.notification_text);
        mTime = (TextView) itemView.findViewById(R.id.notification_time);
        mDivider = (FrameLayout) itemView.findViewById(R.id.notification_divider);
        mType = (ImageView) itemView.findViewById(R.id.notification_type);
    }
}
