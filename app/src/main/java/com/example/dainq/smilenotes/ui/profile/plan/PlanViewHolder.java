package com.example.dainq.smilenotes.ui.profile.plan;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dainq.smilenotes.common.Utility;

import nq.dai.smilenotes.R;

public class PlanViewHolder extends RecyclerView.ViewHolder {
    public Context mContext;

    public LinearLayout mLayout;
    public TextView mTime;
    public TextView mSchedule;
    public TextView mContent;
    public FrameLayout mRemove;

    PlanViewHolder(View itemView, Context context) {
        super(itemView);

        mContext = context;
        mLayout = (LinearLayout) itemView.findViewById(R.id.item_plan_layout);
        mTime = (TextView) itemView.findViewById(R.id.item_plan_time_meeting);

        mSchedule = (TextView) itemView.findViewById(R.id.item_plan_schedule);
        mSchedule.setTextColor(mContext.getColor(R.color.colorAccent));
        Utility.setTextViewDrawableColor(mContext, mSchedule, R.color.colorAccent);

        mContent = (TextView) itemView.findViewById(R.id.item_plan_content);
        mRemove = (FrameLayout) itemView.findViewById(R.id.item_plan_btn_remove);
    }
}
