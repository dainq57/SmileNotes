package com.example.dainq.smilenotes.ui.other;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import nq.dai.smilenotes.R;

public class GoldKeyFragment extends Fragment {
    private Context mContext;

    public GoldKeyFragment(Context context) {
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_gold_key, null);
    }

}
