package com.example.dainq.smilenotes.ui.profile.product;

import android.content.Context;

import com.example.dainq.smilenotes.model.object.ProductObject;
import com.example.dainq.smilenotes.ui.common.realm.RealmModelAdapter;

import io.realm.RealmResults;

public class RealmProductAdapter extends RealmModelAdapter<ProductObject> {
    public RealmProductAdapter(Context context, RealmResults<ProductObject> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
    }
}
