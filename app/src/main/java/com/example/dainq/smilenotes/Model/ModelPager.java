package com.example.dainq.smilenotes.model;

import com.example.dainq.smilenotes.R;

public enum ModelPager {
    FIRST(R.string.first, R.layout.item_pager_step_first),
    SECOND(R.string.second, R.layout.item_pager_step_second),
    THIRD(R.string.thrid, R.layout.item_pager_step_third);

    private int mTitleResId;
    private int mLayoutResId;

    ModelPager(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }
}
