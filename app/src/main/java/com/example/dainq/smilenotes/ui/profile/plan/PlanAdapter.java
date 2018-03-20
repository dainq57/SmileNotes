package com.example.dainq.smilenotes.ui.profile.plan;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dainq.smilenotes.common.Constant;
import com.example.dainq.smilenotes.common.Utility;
import com.example.dainq.smilenotes.controllers.realm.RealmController;
import com.example.dainq.smilenotes.model.object.MeetingObject;
import com.example.dainq.smilenotes.model.object.NotificationObject;
import com.example.dainq.smilenotes.ui.common.realm.RealmRecyclerViewAdapter;

import io.realm.RealmResults;
import nq.dai.smilenotes.R;

public class PlanAdapter extends RealmRecyclerViewAdapter<MeetingObject> {
    private String TAG = "CustomerAdapter";
    private Context mContext;
    private CreatePlanDialog mDialog;
    private RealmController mRealmController;
    private PlanAdapter mAdapter;

    public PlanAdapter(Context context) {
        mContext = context;
    }

    public void setAdapter(PlanAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_card_plan, parent, false);
        mRealmController = new RealmController(mContext);
        return new PlanViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final MeetingObject meetingObject = getItem(position);
        PlanViewHolder holder = (PlanViewHolder) viewHolder;

        String time = Utility.dateToString(meetingObject.getMeeting());
        holder.mTime.setText(time);
        holder.mContent.setText(meetingObject.getContent());
        String schedule = Utility.dateToString(meetingObject.getSchedule());
        holder.mSchedule.setText(schedule);

        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog = new CreatePlanDialog(mContext, mAdapter, meetingObject, Constant.DIALOG_VIEW);
                FragmentManager manager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                mDialog.show(manager, null);
            }
        });

        holder.mRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmRemove(meetingObject.getId());
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

    private void confirmRemove(final int id) {
        final AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(mContext, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(mContext);
        }
        builder.setTitle(R.string.dialog_delete_meeting_title)
                .setMessage(R.string.dialog_delete_meeting_content)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mRealmController.deleteMeeting(id);
                        deleteAllOfMetting(id);
                        notifyDataSetChanged();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void deleteAllOfMetting(int id) {
        RealmResults<NotificationObject> notificationList = mRealmController.getNotificationOfMetting(id);
        mRealmController.removeAllNotification(notificationList);
    }
}