package com.example.dainq.smilenotes.ui.profile.customer;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dainq.smilenotes.model.request.CustomerRequest;

import nq.dai.smilenotes.R;

public class CCustomerAdapter extends RecyclerView.Adapter<CustomerViewHolder> {
    private String TAG = "CCustomerAdapter";

    //data customer to pass into adapter
    private CustomerRequest[] mData;

    //context using in adapter
    private Context mContext;

    //contructor
    CCustomerAdapter() {
    }

    CCustomerAdapter(Context context, CustomerRequest[] data) {
        mContext = context;
        mData = data;
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
    public void onBindViewHolder(CustomerViewHolder holder, int position) {

        final CustomerRequest customer = mData[position];
        Log.d(TAG, "-->[onBindVH] lenght data: " + mData.length);

        holder.mName.setText(customer.getName());
        holder.mRating.setRating(customer.getLevel() + 1);
        holder.mContentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "-->[Item customer] onClick item customer");
            }
        });

        if (mContext instanceof ListCustomerActivity) {
            holder.mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "-->[Item customer] remove customer: " + customer.getName());
                }
            });

            holder.mEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "-->[Item customer] edit customer: " + customer.getName());
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.length;
        }
        return 0;
    }
}
