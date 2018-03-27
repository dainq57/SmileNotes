package com.example.dainq.smilenotes.ui.profile.product;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.dainq.smilenotes.common.Constant;
import com.example.dainq.smilenotes.common.SessionManager;
import com.example.dainq.smilenotes.controllers.api.APIProduct;
import com.example.dainq.smilenotes.model.object.ProductObject;
import com.example.dainq.smilenotes.model.request.product.ProductRequest;
import com.example.dainq.smilenotes.model.response.RemoveResponse;
import com.example.dainq.smilenotes.ui.common.realm.RealmRecyclerViewAdapter;
import com.example.dainq.smilenotes.ui.profile.ProfileActivity;
import com.example.dainq.smilenotes.ui.profile.customer.CustomerViewHolder;

import java.util.Calendar;
import java.util.List;

import nq.dai.smilenotes.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductAdapter extends RealmRecyclerViewAdapter<ProductObject> {
    private String TAG = "ProductAdapter";
    private Context mContext;

    //list product receive
    private List<ProductRequest> mData;

    //session of user
    private SessionManager mSession;

    //interface service
    private APIProduct mService;

    public ProductAdapter() {
    }

    /**
     * adapter contructor
     *
     * @param context context
     * @param data    list product
     * @param session session of user to manage info
     * @param service api interface service
     */
    public ProductAdapter(Context context, List<ProductRequest> data, SessionManager session, APIProduct service) {
        mContext = context;
        mData = data;
        mSession = session;
        mService = service;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        //in list product of user on profile
        if (mContext instanceof ProfileActivity) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_product, parent, false);
            return new ProductViewHolder(view, mContext);
        }
        //list customer has product will expire on home
        else {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_customer_none_swipe, parent, false);
            return new CustomerViewHolder(view, mContext);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final ProductRequest product = mData.get(position);
        if (mContext instanceof ProfileActivity) {
            ProductViewHolder holder = (ProductViewHolder) viewHolder;

            holder.mIndex.setText((position + 1) + ".");
            holder.mName.setText(product.getName());
            holder.mDate.setText(product.getExpireDate());

            holder.mLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final CharSequence[] items = {"Chỉnh sửa", "Xóa"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int position) {
                            if (position == 0) {

                            } else {
                                //show dialog confirm delete
                                confirmDelete(product.getId(), product.getCustomerId());
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
            holder.mExpriedDate.setText(product.getExpireDate());

//            if (customer != null) {
//                holder.mName.setText(customer.getName());
//            }

//            holder.mContentLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Bundle bundle = new Bundle();
//                    bundle.putInt(Constant.KEY_ID, customer.getId());
//                    bundle.putInt(Constant.KEY_TYPE_PRODILE, Constant.PROFILE_TYPE_PRODUCT);
//                    Intent intent = new Intent(mContext, ProfileActivity.class);
//                    intent.putExtras(bundle);
//                    mContext.startActivity(intent);
//                }
//            });
        }
    }

    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }

    /**
     * dialog confirm delete product
     *
     * @param productId  id of product
     * @param customerId id of customer
     */
    private void confirmDelete(final String productId, final String customerId) {
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
                        //call process delete
                        processRemoveProduct(productId, customerId);

                        //hide dialog
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //hide dialog
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /**
     * process delete product of customer
     *
     * @param productId  id product
     * @param customerId id customer
     */
    private void processRemoveProduct(String productId, String customerId) {
        //get userId and token from session
        String userId = mSession.getUserDetails().getId();
        String token = mSession.getUserDetails().getToken();

        Call<RemoveResponse> response = mService.removeProduct(userId, customerId, productId, token);
        response.enqueue(new Callback<RemoveResponse>() {
            @Override
            public void onResponse(@NonNull Call<RemoveResponse> call, @NonNull Response<RemoveResponse> response) {
                RemoveResponse removeResponse = response.body();

                if (removeResponse != null) {
                    int code = removeResponse.getCode();
                    if (code == Constant.RESPONSE_SUCCESS) {
                        //show toast
                        Toast.makeText(mContext, "Đã xóa sản phẩm!", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d(TAG, "-->[process-remove-product] onResponse: " + removeResponse.getCode() + ": " + removeResponse.getMessage());
                    }
                } else {
                    Log.d(TAG, "-->[process-remove-product] onResponse: " + response.code() + ": " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<RemoveResponse> call, @NonNull Throwable t) {
                Log.d(TAG, "-->[process-remove-product] onFailure: " + t.getMessage());
            }
        });
    }



    private void initDialogDatePicker(final ProductRequest product) {
        DatePickerDialog dialog;
        Calendar calendar = Calendar.getInstance();
        dialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        dialog.getDatePicker().setLayoutMode(0);
        dialog.show();
    }
}
