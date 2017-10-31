package com.example.dainq.smilenotes.ui.profile.product;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import nq.dai.smilenotes.R;

public class ProductViewHolder extends RecyclerView.ViewHolder{
    public Context mContext;

    public LinearLayout mLayout;
    public TextView mIndex;
    public TextView mName;
    public TextView mDate;

    ProductViewHolder(View itemView, Context context) {
        super(itemView);

        mContext = context;
        mLayout = (LinearLayout) itemView.findViewById(R.id.product_lay);
        mIndex = (TextView) itemView.findViewById(R.id.product_index);
        mName = (TextView) itemView.findViewById(R.id.product_name);
        mDate = (TextView) itemView.findViewById(R.id.product_use_date);
    }
}
