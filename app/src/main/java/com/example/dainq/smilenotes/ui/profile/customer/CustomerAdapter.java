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

import com.example.dainq.smilenotes.common.BaseURL;
import com.example.dainq.smilenotes.common.Constant;
import com.example.dainq.smilenotes.common.SessionManager;
import com.example.dainq.smilenotes.common.Utility;
import com.example.dainq.smilenotes.controllers.api.APICustomer;
import com.example.dainq.smilenotes.model.request.customer.CustomerRequest;
import com.example.dainq.smilenotes.model.response.RemoveResponse;
import com.example.dainq.smilenotes.ui.create.CreateActivity;
import com.example.dainq.smilenotes.ui.profile.ProfileActivity;

import java.util.List;

import nq.dai.smilenotes.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerViewHolder> {
    private String TAG = "CustomerAdapter";

    //data customer to pass into adapter
    private List<CustomerRequest> mData;

    //context using in adapter
    private Context mContext;

    //session
    private SessionManager mSession;

    //service api
    private APICustomer mService;

    //contructor
    public CustomerAdapter() {
    }

    public CustomerAdapter(Context context, List<CustomerRequest> data) {
        mContext = context;
        mData = data;
        mSession = new SessionManager(mContext);
        initRetrofit();
    }

    private void initRetrofit() {
        Retrofit retrofit = Utility.initRetrofit(BaseURL.URL_CUSTOMER);

        mService = retrofit.create(APICustomer.class);
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
    public void onBindViewHolder(CustomerViewHolder holder, final int position) {

        final CustomerRequest customer = mData.get(position);
        final String idCustomer = customer.getId();
        Log.d(TAG, "-->[onBindVH] lenght data: " + mData.size());

        holder.mName.setText(customer.getName());
        holder.mRating.setRating(customer.getLevel() + 1);
        holder.mContentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "-->[Item customer] onClick item customer: " + customer.getName());

                Bundle bundle = new Bundle();
                bundle.putString(Constant.KEY_ID, idCustomer);

                Intent intent = new Intent(mContext, ProfileActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

        if (mContext instanceof ListCustomerActivity) {
            holder.mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "-->[Item customer] remove customer: " + customer.getName());
                    confirmDelete(idCustomer, position);
                }
            });

            holder.mEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "-->[Item customer] edit customer: " + customer.getName());
                    startEditCustomer(idCustomer);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }

    /* edit funtion*/
    private void startEditCustomer(String idCustomer) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.KEY_ACTION, Constant.ACTION_EDIT);
        bundle.putString(Constant.KEY_ID, idCustomer);

        Intent intent = new Intent(mContext, CreateActivity.class);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    // confirm before delete customer
    private void confirmDelete(final String idCustomer, final int position) {
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.title_dialog_delete)
                .setMessage(R.string.dialog_delete_content)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        processDeleteCustomer(idCustomer, position);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(mContext.getDrawable(R.drawable.icn_alert_128))
                .show();
    }

    /*
    process deleted customer
    */
    //todo check response from server
    private void processDeleteCustomer(String idCustomer, final int position) {
        String idUser = mSession.getUserDetails().getId();
        String token = mSession.getUserDetails().getToken();

        Call<RemoveResponse> response = mService.removeCustomer(idUser, idCustomer, token);
        response.enqueue(new Callback<RemoveResponse>() {
            @Override
            public void onResponse(Call<RemoveResponse> call, Response<RemoveResponse> response) {
                RemoveResponse responseServer = response.body();
                Log.d(TAG, "--[process-delete] response: " + responseServer.getCode() + " | " + responseServer.getMessage());

                mData.remove(position);
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<RemoveResponse> call, Throwable t) {
                Log.d(TAG, "--[process-delete] failure: " + t.getMessage());
            }
        });
    }
}
