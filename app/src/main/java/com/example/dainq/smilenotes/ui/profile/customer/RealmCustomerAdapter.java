package com.example.dainq.smilenotes.ui.profile.customer;

import android.content.Context;

import com.example.dainq.smilenotes.model.object.CustomerObject;
import com.example.dainq.smilenotes.ui.common.realm.RealmModelAdapter;

import io.realm.RealmResults;


public class RealmCustomerAdapter extends RealmModelAdapter<CustomerObject> {
    public RealmCustomerAdapter(Context context, RealmResults<CustomerObject> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
    }
}
