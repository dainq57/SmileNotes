package com.example.dainq.smilenotes.ui.search;

import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.dainq.smilenotes.common.Utility;
import com.example.dainq.smilenotes.controllers.realm.RealmController;
import com.example.dainq.smilenotes.model.CustomerObject;
import com.example.dainq.smilenotes.ui.profile.customer.CustomerAdapter;
import com.example.dainq.smilenotes.ui.profile.customer.RealmCustomerAdapter;

import io.realm.RealmResults;
import nq.dai.smilenotes.R;

public class SearchActivity extends AppCompatActivity {

    private SearchView mSearch;
    private RecyclerView mListResult;
    private TextView mTextNotResult;
    private CustomerAdapter mAdapter;

    private RealmResults<CustomerObject> mRealmResult;
    private RealmController mRealmController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initView();
    }

    private void initView(){
        initToolBar();

        mSearch = (SearchView) findViewById(R.id.search);
        mSearch.setIconifiedByDefault(true);
        mSearch.setOnQueryTextListener(mOnQueryTextListener);

        mListResult = (RecyclerView) findViewById(R.id.search_list);
        mListResult.setHasFixedSize(true);
        mListResult.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CustomerAdapter(this);
        mListResult.setAdapter(mAdapter);

        mTextNotResult = (TextView) findViewById(R.id.search_not_results);

        mRealmController = RealmController.with(this);
    }

    private void initToolBar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.tw_ic_ab_back_mtrl);
        mToolbar.getNavigationIcon().setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
        getSupportActionBar().setTitle("TÌM KIẾM");

    }

    private SearchView.OnQueryTextListener mOnQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            if (query.length() > 0 && !TextUtils.isEmpty(query)) {
                mRealmResult = mRealmController.searchCustomers(query);
                if (mRealmResult.isEmpty()) {
                    mTextNotResult.setVisibility(View.VISIBLE);
                    mListResult.setVisibility(View.GONE);
                } else {
                    mListResult.setVisibility(View.VISIBLE);
                    mTextNotResult.setVisibility(View.GONE);
                    RealmCustomerAdapter realmAdapter = new RealmCustomerAdapter(getApplicationContext(), mRealmResult, true);
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
        mRealmController = new RealmController(this);
        super.onResume();
    }

    @Override
    public void onDestroy() {
        mRealmController.close();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
