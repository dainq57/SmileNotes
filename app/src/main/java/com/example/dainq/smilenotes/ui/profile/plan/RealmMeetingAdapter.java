package com.example.dainq.smilenotes.ui.profile.plan;

import android.content.Context;

import com.example.dainq.smilenotes.model.object.MeetingObject;
import com.example.dainq.smilenotes.ui.common.realm.RealmModelAdapter;

import io.realm.RealmResults;

public class RealmMeetingAdapter extends RealmModelAdapter<MeetingObject> {
    public RealmMeetingAdapter(Context context, RealmResults<MeetingObject> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
    }
}
