package com.example.dainq.smilenotes.ui.notifications;

import android.content.Context;

import com.example.dainq.smilenotes.model.NotificationObject;
import com.example.dainq.smilenotes.ui.common.realm.RealmModelAdapter;

import io.realm.RealmResults;

public class RealmNotificationAdapter extends RealmModelAdapter<NotificationObject> {
    public RealmNotificationAdapter(Context context, RealmResults<NotificationObject> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
    }
}
