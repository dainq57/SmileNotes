package com.example.dainq.smilenotes.ui.search;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dainq.smilenotes.common.BaseFragment;
import com.example.dainq.smilenotes.common.Utility;
import com.example.dainq.smilenotes.controller.realm.RealmController;
import com.example.dainq.smilenotes.model.CustomerObject;
import com.example.dainq.smilenotes.ui.profile.customer.CustomerAdapter;
import com.example.dainq.smilenotes.ui.profile.customer.RealmCustomerAdapter;

import io.realm.RealmResults;
import nq.dai.smilenotes.R;

public class SearchFragment extends BaseFragment {
    private Context mContext;
    private SearchView mSearch;
    private RecyclerView mListResult;
    private TextView mTextNotResult;
    private CustomerAdapter mAdapter;

    private RealmResults<CustomerObject> mRealmResult;
    private RealmController mRealmController;

    public SearchFragment(Context context) {
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, null);
        initView(view);

        return view;
    }

    private void initView(View view) {
        mSearch = (SearchView) view.findViewById(R.id.search);
        mSearch.setIconifiedByDefault(true);
        mSearch.setOnQueryTextListener(mOnQueryTextListener);

        mListResult = (RecyclerView) view.findViewById(R.id.search_list);
        mListResult.setHasFixedSize(true);
        mListResult.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new CustomerAdapter(mContext);
        mListResult.setAdapter(mAdapter);

        mTextNotResult = (TextView) view.findViewById(R.id.search_not_results);

        mRealmController = RealmController.with(this);
    }

    private SearchView.OnQueryTextListener mOnQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            if (query.length() > 0 && !Utility.isEmptyString(query)) {
                mRealmResult = mRealmController.searchCustomers(query);
                if (mRealmResult.isEmpty()) {
                    mTextNotResult.setVisibility(View.VISIBLE);
                    mListResult.setVisibility(View.GONE);
                } else {
                    mListResult.setVisibility(View.VISIBLE);
                    mTextNotResult.setVisibility(View.GONE);
                    RealmCustomerAdapter realmAdapter = new RealmCustomerAdapter(mContext, mRealmResult, true);
                    mAdapter.setRealmAdapter(realmAdapter);
                    mAdapter.notifyDataSetChanged();
                }
            }
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    };

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
