package com.example.dainq.smilenotes.controller.realm;

import android.content.Context;

import com.example.dainq.smilenotes.model.CustomerObject;

import io.realm.RealmResults;


public class RealmCustomerAdapter extends RealmModelAdapter<CustomerObject>{
    public RealmCustomerAdapter(Context context, RealmResults<CustomerObject> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
    }
}
