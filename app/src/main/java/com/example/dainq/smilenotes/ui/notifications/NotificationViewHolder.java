package com.example.dainq.smilenotes.ui.notifications;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import nq.dai.smilenotes.R;

public class NotificationViewHolder extends RecyclerView.ViewHolder {
    public LinearLayout mContentLayout;
    public View mDeleteButton;
    public TextView mTextNotification;
    public FrameLayout mDivider;

    public NotificationViewHolder(View itemView) {
        super(itemView);

        mContentLayout = (LinearLayout) itemView.findViewById(R.id.notification_content);
        mDeleteButton = itemView.findViewById(R.id.notification_delete);
        mTextNotification = (TextView) itemView.findViewById(R.id.notification_text);
        mDivider = (FrameLayout) itemView.findViewById(R.id.notification_divider);
    }
}
