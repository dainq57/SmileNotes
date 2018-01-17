package com.example.dainq.smilenotes.ui.profile;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dainq.smilenotes.common.Constant;
import com.example.dainq.smilenotes.controllers.realm.RealmController;
import com.example.dainq.smilenotes.model.CustomerObject;
import com.example.dainq.smilenotes.model.ProductObject;
import com.example.dainq.smilenotes.ui.profile.product.CreateProductDialog;
import com.example.dainq.smilenotes.ui.profile.product.ProductAdapter;
import com.example.dainq.smilenotes.ui.profile.product.RealmProductAdapter;

import io.realm.RealmResults;
import nq.dai.smilenotes.R;

public class ProfileProductFragment extends Fragment implements View.OnClickListener {
    private Context mContext;
    private CustomerObject mCustomer;
    private CreateProductDialog mDialog;
    private ProductAdapter mAdapter;
    private RealmController mRealmController;

    public ProfileProductFragment(Context context) {
        mContext = context;
    }

    public ProfileProductFragment(Context context, CustomerObject customer) {
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
        RelativeLayout mAddNew = (RelativeLayout) view.findViewById(R.id.profile_add_new);
        mAddNew.setOnClickListener(this);

        TextView buttonAdd = (TextView) view.findViewById(R.id.text_button_add_new);
        buttonAdd.setText(R.string.add_new_product_s);

        RecyclerView mListProduct = (RecyclerView) view.findViewById(R.id.list_plan_meeting);
        mListProduct.setHasFixedSize(true);
        mListProduct.setLayoutManager(new LinearLayoutManager(mContext));

        mAdapter = new ProductAdapter(mContext);
        mListProduct.setAdapter(mAdapter);

        mRealmController = RealmController.with(this);
        RealmResults<ProductObject> realmResults = mRealmController.getProductOfCustomer(mCustomer.getId());
        RealmProductAdapter realmAdapter = new RealmProductAdapter(mContext, realmResults, true);

        mAdapter.setRealmAdapter(realmAdapter);
        mAdapter.notifyDataSetChanged();
        mDialog = new CreateProductDialog(mAdapter);
    }

    private void showDialog() {
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.KEY_ID_PRODUCT, mCustomer.getId());
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

    @Override
    public void onResume() {
        mRealmController = new RealmController(mContext);
        super.onResume();
    }

    @Override
    public void onDestroy() {
        mRealmController.close();
        super.onDestroy();
    }
}
