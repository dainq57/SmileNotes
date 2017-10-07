package com.example.dainq.smilenotes.ui.customer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dainq.smilenotes.common.Constant;
import com.example.dainq.smilenotes.controller.realm.RealmController;
import com.example.dainq.smilenotes.controller.realm.RealmRecyclerViewAdapter;
import com.example.dainq.smilenotes.model.CustomerObject;

import io.realm.Realm;
import io.realm.RealmResults;
import nq.dai.smilenotes.R;

public class CustomerAdapter extends RealmRecyclerViewAdapter<CustomerObject> {
    private Context mContext;
    private Realm mRealm;
    private RealmController mRealmController;
    private RealmResults<CustomerObject> mRealmResult;

    public CustomerAdapter(Context context) {
        mContext = context;
    }

    public CustomerAdapter(Context context, RealmResults<CustomerObject> realmResults) {
        mContext = context;
        mRealmResult = realmResults;

    }

    @Override
    public CustomerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_customer, parent, false);
        return new CustomerViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        mRealm = RealmController.getInstance().getRealm();
        mRealmController = new RealmController(mContext);

        final CustomerObject customer = getItem(position);
        CustomerViewHolder holder = (CustomerViewHolder) viewHolder;

        holder.mName.setText(customer.getName());
        holder.mAddress.setText(customer.getAddress());
        holder.mRating.setRating(customer.getLevel() + 1);
        holder.mContentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(Constant.KEY_ID_CUSTOMER, customer.getId());
                Log.d(CustomerAdapter.class.getSimpleName() + "-dainq", "customer id: " + customer.getId());
                Intent intent = new Intent(mContext, ProfileActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

        holder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(CustomerAdapter.class.getSimpleName() + "-dainq", "remove customer: " + customer.getName());
                mRealm.beginTransaction();
                CustomerObject customerObject = mRealmController.getCustomer(customer.getId());
                customerObject.removeFromRealm();
                mRealm.commitTransaction();
                notifyDataSetChanged();
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

//    private class CustomerFilter extends Filter {
//
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            FilterResults results = new FilterResults();
//            if (constraint != null && constraint.length() > 0) {
//
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//
//        }
//    }
}
