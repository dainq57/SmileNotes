package com.example.dainq.smilenotes.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dainq.smilenotes.R;
import com.example.dainq.smilenotes.common.BaseFragment;

public class HomeFragment extends BaseFragment {
    public HomeFragment() {
    }

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, null);
    }
}
