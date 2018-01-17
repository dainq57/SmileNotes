package com.example.dainq.smilenotes.ui.profile.customer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dainq.smilenotes.common.Constant;
import com.example.dainq.smilenotes.controllers.realm.RealmController;
import com.example.dainq.smilenotes.model.CustomerObject;
import com.example.dainq.smilenotes.model.MeetingObject;
import com.example.dainq.smilenotes.model.NotificationObject;
import com.example.dainq.smilenotes.model.ProductObject;
import com.example.dainq.smilenotes.ui.common.realm.RealmRecyclerViewAdapter;
import com.example.dainq.smilenotes.ui.create.CreateActivity;
import com.example.dainq.smilenotes.ui.profile.ProfileActivity;

import io.realm.RealmResults;
import nq.dai.smilenotes.R;

public class CustomerAdapter extends RealmRecyclerViewAdapter<CustomerObject> {
    private String TAG = "CustomerAdapter";

    private Context mContext;
    private RealmController mRealmController;

    public CustomerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public CustomerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (mContext instanceof ListCustomerActivity) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_customer, parent, false);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_customer_none_swipe, parent, false);
        }
        return new CustomerViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        mRealmController = new RealmController(mContext);

        final CustomerObject customer = getItem(position);
        CustomerViewHolder holder = (CustomerViewHolder) viewHolder;

        holder.mName.setText(customer.getName());
        holder.mRating.setRating(customer.getLevel() + 1);
        holder.mContentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(Constant.KEY_ID, customer.getId());
                Log.d(TAG, "customer id: " + customer.getId());
                Intent intent = new Intent(mContext, ProfileActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

        if (mContext instanceof ListCustomerActivity) {
            holder.mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "remove customer: " + customer.getName());
                    confirmDelete(customer.getId());
                }
            });

            holder.mEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startEdit(customer.getId());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (getRealmAdapter() != null) {
            return getRealmAdapter().getCount();
        }
        return 0;
    }

    private void startEdit(int id) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.KEY_ACTION, Constant.ACTION_EDIT);
        bundle.putInt(Constant.KEY_ID, id);

        Intent intent = new Intent(mContext, CreateActivity.class);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    private void confirmDelete(final int id) {
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.title_dialog_delete)
                .setMessage(R.string.dialog_delete_content)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mRealmController.deleteCustomer(id);
                        deleteAllOfCustomer(id);
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

    private void deleteAllOfCustomer(int id) {
        RealmResults<ProductObject> productList = mRealmController.getProductOfCustomer(id);
        RealmResults<MeetingObject> meetingList = mRealmController.getMeetingOfCustomer(id);
        RealmResults<NotificationObject> notificationList = mRealmController.getNotificationOfCustomer(id);

        Log.d(TAG, "dainq product/meeting: " + productList.size() + "/" + meetingList.size());
        mRealmController.removeAllProduct(productList);
        mRealmController.removeAllPlan(meetingList);
        mRealmController.removeAllNotification(notificationList);
    }
}
