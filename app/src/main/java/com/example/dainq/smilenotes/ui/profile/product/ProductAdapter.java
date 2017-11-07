package com.example.dainq.smilenotes.ui.profile.product;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.example.dainq.smilenotes.common.Constant;
import com.example.dainq.smilenotes.common.Utility;
import com.example.dainq.smilenotes.controller.realm.RealmController;
import com.example.dainq.smilenotes.model.CustomerObject;
import com.example.dainq.smilenotes.model.ProductObject;
import com.example.dainq.smilenotes.ui.common.realm.RealmRecyclerViewAdapter;
import com.example.dainq.smilenotes.ui.profile.ProfileActivity;
import com.example.dainq.smilenotes.ui.profile.customer.CustomerViewHolder;

import java.util.Calendar;
import java.util.Date;

import nq.dai.smilenotes.R;

public class ProductAdapter extends RealmRecyclerViewAdapter<ProductObject> {
    private String TAG = "ProductAdapter";
    private Context mContext;
    private RealmController mRealmController;

    public ProductAdapter(Context context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        mRealmController = new RealmController(mContext);

        if (mContext instanceof ProfileActivity) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_product, parent, false);
            return new ProductViewHolder(view, mContext);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_customer_none_swipe, parent, false);
            return new CustomerViewHolder(view, mContext);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final ProductObject product = getItem(position);
        if (mContext instanceof ProfileActivity) {
            ProductViewHolder holder = (ProductViewHolder) viewHolder;

            holder.mIndex.setText((position + 1) + ".");
            holder.mName.setText(product.getName());
            String useDate = Utility.dateToString(product.getUsedate());
            holder.mDate.setText(useDate);

            holder.mLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final CharSequence[] items = {"Chỉnh sửa ngày", "Xóa"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int position) {
                            if (position == 0) {
                                initDialogDatePicker(product);
                            } else {
                                confirmDelete(product.getId());
                            }
                        }
                    });
                    builder.show();
                    return true;
                }
            });
        } else {
            CustomerViewHolder holder = (CustomerViewHolder) viewHolder;

            holder.mProduct.setVisibility(View.VISIBLE);
            holder.mRating.setVisibility(View.GONE);

            holder.mProduct.setText(product.getName());
            String date = Utility.dateToString(product.getUsedate());
            holder.mExpriedDate.setText(date);

            final CustomerObject customer = mRealmController.getCustomer(product.getIdcustomer());
            if (customer != null) {
                holder.mName.setText(customer.getName());
            }

            holder.mContentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constant.KEY_ID, customer.getId());
                    bundle.putInt(Constant.KEY_TYPE_PRODILE, Constant.PROFILE_TYPE_PRODUCT);
                    Intent intent = new Intent(mContext, ProfileActivity.class);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
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

    private void confirmDelete(final int id) {
        final AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(mContext, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(mContext);
        }
        builder.setTitle(R.string.delete_product)
                .setMessage(R.string.confirm_delete_product)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mRealmController.deleteProduct(id);
                        notifyDataSetChanged();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void updateDate(ProductObject object, Date date) {
        mRealmController.getRealm().beginTransaction();
        object.setUsedate(date);
        mRealmController.getRealm().commitTransaction();
        notifyDataSetChanged();
    }

    private void initDialogDatePicker(final ProductObject object) {
        DatePickerDialog dialog;
        Calendar calendar = Calendar.getInstance();
        dialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                updateDate(object, newDate.getTime());
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        dialog.getDatePicker().setLayoutMode(0);
        dialog.show();
    }
}
