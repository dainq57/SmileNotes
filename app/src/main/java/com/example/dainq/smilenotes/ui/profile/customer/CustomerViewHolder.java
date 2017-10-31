package com.example.dainq.smilenotes.ui.profile.customer;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import nq.dai.smilenotes.R;

public class CustomerViewHolder extends RecyclerView.ViewHolder {

    public Context mContext;
    public RelativeLayout mContentLayout;
    public TextView mName;
    public TextView mExpriedDate;
    public RatingBar mRating;
    public TextView mProduct;
    public ImageButton mDelete;
    public ImageButton mEdit;

    public CustomerViewHolder(View itemView, Context context) {
        super(itemView);

        mContext = context;
        mContentLayout = (RelativeLayout) itemView.findViewById(R.id.customer_item_content);
        mName = (TextView) itemView.findViewById(R.id.customer_item_name);
        mExpriedDate = (TextView) itemView.findViewById(R.id.customer_item_expried_date);
        mProduct = (TextView) itemView.findViewById(R.id.item_product_name);
        mRating = (RatingBar) itemView.findViewById(R.id.customer_item_rating);

        if (mContext instanceof ListCustomerActivity) {
            mDelete = (ImageButton) itemView.findViewById(R.id.customer_item_delete);
            mEdit = (ImageButton) itemView.findViewById(R.id.customer_item_edit);
        }
    }
}