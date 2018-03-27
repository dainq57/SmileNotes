package com.example.dainq.smilenotes.ui.profile;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dainq.smilenotes.common.BaseURL;
import com.example.dainq.smilenotes.common.Constant;
import com.example.dainq.smilenotes.common.SessionManager;
import com.example.dainq.smilenotes.common.Utility;
import com.example.dainq.smilenotes.controllers.api.APIProduct;
import com.example.dainq.smilenotes.controllers.realm.RealmController;
import com.example.dainq.smilenotes.model.object.CustomerObject;
import com.example.dainq.smilenotes.model.object.ProductObject;
import com.example.dainq.smilenotes.model.request.customer.CustomerRequest;
import com.example.dainq.smilenotes.model.request.product.ProductRequest;
import com.example.dainq.smilenotes.model.response.product.ListProductResponse;
import com.example.dainq.smilenotes.ui.profile.product.CreateProductDialog;
import com.example.dainq.smilenotes.ui.profile.product.ProductAdapter;
import com.example.dainq.smilenotes.ui.profile.product.RealmProductAdapter;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;
import nq.dai.smilenotes.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileProductFragment extends Fragment implements View.OnClickListener {
    private String TAG = "ProfileProductFragment";

    private Context mContext;
    private CustomerRequest mCustomer;
    private CreateProductDialog mDialog;
    private ProductAdapter mAdapter;

    //list show product
    private RecyclerView mListProduct;

    //session of user
    private SessionManager mSession;

    //interface service
    private APIProduct mService;

    //list product receive
    private ArrayList<ProductRequest> mData;

    public ProfileProductFragment(Context context) {
        mContext = context;
    }

    public ProfileProductFragment(Context context, CustomerRequest customer) {
        mContext = context;
        mCustomer = customer;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_plan, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        initRetrofit();
        //create session of user
        mSession = new SessionManager(mContext);

        RelativeLayout mAddNew = (RelativeLayout) view.findViewById(R.id.profile_add_new);
        mAddNew.setOnClickListener(this);

        TextView buttonAdd = (TextView) view.findViewById(R.id.text_button_add_new);
        buttonAdd.setText(R.string.add_new_product_s);

        mListProduct = (RecyclerView) view.findViewById(R.id.list_plan_meeting);
        mListProduct.setHasFixedSize(true);
        mListProduct.setLayoutManager(new LinearLayoutManager(mContext));

        mAdapter = new ProductAdapter();

        //call get list product
        processGetListCustomer();

        //create dialog create product
        mDialog = new CreateProductDialog(mAdapter, mContext);
    }

    /*
    create retrofit
     */
    private void initRetrofit() {
        Retrofit retrofit = Utility.initRetrofit(BaseURL.URL_PRODUCT);
        mService = retrofit.create(APIProduct.class);
    }

    private void showDialog() {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY_ID_PRODUCT, mCustomer.getId());
        mDialog.setArguments(bundle);

        mDialog.show(getFragmentManager(), null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_add_new:
                showDialog();
                break;
            default:
                break;
        }
    }

    /**
     * get list product of customer
     */
    private void processGetListCustomer() {
        //get customerId
        String customerId = mCustomer.getId();

        //get userId and token from session
        String userId = mSession.getUserDetails().getId();
        String token = mSession.getUserDetails().getToken();

        Call<ListProductResponse> response = mService.getListProduct(userId, customerId, token);
        response.enqueue(new Callback<ListProductResponse>() {
            @Override
            public void onResponse(Call<ListProductResponse> call, Response<ListProductResponse> response) {
                ListProductResponse serverResponse = response.body();

                if (serverResponse != null) {
                    int code = serverResponse.getCode();
                    if (code == Constant.RESPONSE_SUCCESS) {
                        Log.d(TAG, "-->[process-get-list-product] onResponse: " + serverResponse.getCode() + ": " + serverResponse.getMessage());
                        mData = serverResponse.getData();

                        Log.d(TAG, "-->[process-get-list-product] data.size(): " + mData.size());

                        mAdapter = new ProductAdapter(mContext, mData, mSession, mService);
                        mListProduct.setAdapter(mAdapter);
                    } else {
                        Log.d(TAG, "-->[process-get-list-product] onResponse: " + serverResponse.getCode() + ": " + serverResponse.getMessage());
                    }
                } else {
                    Log.d(TAG, "-->[process-get-list-product] onResponse: " + response.code() + ": " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ListProductResponse> call, Throwable t) {
                Log.d(TAG, "-->[process-get-list-product] onFailure! " + t.getMessage());
            }
        });
    }
}
