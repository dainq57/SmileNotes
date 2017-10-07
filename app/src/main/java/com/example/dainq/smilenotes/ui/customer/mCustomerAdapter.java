package com.example.dainq.smilenotes.ui.customer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nq.dai.smilenotes.R;

public class mCustomerAdapter extends RecyclerView.Adapter<mCustomerAdapter.CustomerViewHolder> {
    private Context mContext;
    private List<String> mDataSet = new ArrayList<>();

    public mCustomerAdapter(Context context, List<String> dataSet) {
        mContext = context;
        mDataSet = dataSet;
    }

    @Override
    public CustomerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_customer, parent, false);
        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomerViewHolder holder, int position) {
        if (mDataSet != null && 0 <= position && position < mDataSet.size()) {
            final String data = mDataSet.get(position);

            holder.mName.setText(data);
            holder.mContentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class CustomerViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout mContentLayout;
        public TextView mName;
        public TextView mAddress;
        public TextView mExpriedDate;

        public CustomerViewHolder(View itemView) {
            super(itemView);

            mContentLayout = (RelativeLayout) itemView.findViewById(R.id.customer_item_content);
            mName = (TextView) itemView.findViewById(R.id.customer_item_name);
            mAddress = (TextView) itemView.findViewById(R.id.customer_item_address);
            mExpriedDate = (TextView) itemView.findViewById(R.id.customer_item_expried_date);
        }
    }
}
