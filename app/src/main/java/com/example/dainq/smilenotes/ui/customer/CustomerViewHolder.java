package com.example.dainq.smilenotes.ui.customer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dainq.smilenotes.MainActivity;

import nq.dai.smilenotes.R;

public class CustomerViewHolder extends RecyclerView.ViewHolder {

    public Context mContext;
    public RelativeLayout mContentLayout;
    public TextView mName;
    public TextView mAddress;
    public TextView mExpriedDate;
    public RatingBar mRating;
    public ImageButton mDelete;
    public ImageButton mSetDate;

    CustomerViewHolder(View itemView, Context context) {
        super(itemView);

        mContext = context;
        mContentLayout = (RelativeLayout) itemView.findViewById(R.id.customer_item_content);
        mName = (TextView) itemView.findViewById(R.id.customer_item_name);
        mAddress = (TextView) itemView.findViewById(R.id.customer_item_address);
        mExpriedDate = (TextView) itemView.findViewById(R.id.customer_item_expried_date);
        mRating = (RatingBar) itemView.findViewById(R.id.customer_item_rating);
        mDelete = (ImageButton) itemView.findViewById(R.id.customer_item_delete);
        mSetDate = (ImageButton) itemView.findViewById(R.id.customer_item_set_date);

        if (mContext instanceof ListCustomerActivity || mContext instanceof MainActivity) {
            mExpriedDate.setVisibility(View.GONE);
        }
    }
}