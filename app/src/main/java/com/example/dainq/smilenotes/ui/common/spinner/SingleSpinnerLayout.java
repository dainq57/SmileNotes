package com.example.dainq.smilenotes.ui.common.spinner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.support.annotation.ArrayRes;
import android.support.annotation.Px;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import nq.dai.smilenotes.R;

public class SingleSpinnerLayout extends LinearLayout {
    public static final int INVALID_POSITION = -1;
    public static final int DEFAULT_POSITION = 0;

    private Context mContext;
    private Spinner mSpinner;
    private OnSpinnerItemSelectedListener mOnSpinnerItemSelectedListener;
    private SpinnerAdapter<?> mWinsetSpinnerAdapter;
    private boolean mIsAppBar = false;
    private View mView;

    public SingleSpinnerLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        mContext = context;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.single_spinner, this, false);
        mView = view;
        this.addView(view);

        mSpinner = (Spinner) findViewById(R.id.spinner_item);
        mSpinner.getBackground().setColorFilter(ContextCompat.getColor(mContext, R.color.color_text), PorterDuff.Mode.SRC_ATOP);

        final TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.SingleSpinnerLayout);
        mIsAppBar = a.getBoolean(R.styleable.SingleSpinnerLayout_isAppBar, false);
        a.recycle();
        setSingleSpinnerItemSelectedListener();
    }

    private void setSingleSpinnerItemSelectedListener() {

        AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mWinsetSpinnerAdapter.setSelectedPosition(position);

                if (mOnSpinnerItemSelectedListener != null) {
                    mOnSpinnerItemSelectedListener.onItemSelected(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mOnSpinnerItemSelectedListener.onNothingSelected();
            }
        };
        mSpinner.setOnItemSelectedListener(onItemSelectedListener);

    }

    public void setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener listener) {
        mOnSpinnerItemSelectedListener = listener;
    }

    public SpinnerAdapter<?> getWinsetSpinnerAdapter() {
        return mWinsetSpinnerAdapter;
    }

    public Spinner getSpinner() {
        return mSpinner;
    }

    public Object getSelectedItem() {
        return mWinsetSpinnerAdapter.getSelectedItem();
    }

    public void setSpinnerList(@ArrayRes int textArrayResId) {
        CharSequence[] strings = mContext.getResources().getTextArray(textArrayResId);
        setSingleSpinnerList(strings);
    }

    public <T extends ISpinnerItem> void setSpinnerList(List<T> items) {
        if (items == null) {
            return;
        }

        ArrayList<T> list = new ArrayList<>();
        for (T item : items) {
            list.add(item);
        }
        setSingleSpinnerList(list.toArray());
    }

    public <T extends ISpinnerItem> void setSpinnerArrayList(List<T> items) {
        if (items == null) {
            return;
        }

        ArrayList<T> list = new ArrayList<>();
        for (T item : items) {
            list.add(item);
        }
        setSingleSpinnerList(list.toArray());
    }

    public <T> void setSingleSpinnerList(T[] objects) {
        mWinsetSpinnerAdapter = new SpinnerAdapter<>(mContext, objects, mIsAppBar);
        mSpinner.setAdapter(mWinsetSpinnerAdapter);
        setSelection(DEFAULT_POSITION);
    }

    public void setSelection(int position) {
        mWinsetSpinnerAdapter.setSelectedPosition(position);
        mSpinner.setSelection(position);
    }

    public void setMargins(@Px int margin) {
        if (mView != null) {
            if (mView.getLayoutParams() instanceof MarginLayoutParams) {
                MarginLayoutParams p = (MarginLayoutParams) mView.getLayoutParams();
                p.setMargins(margin, 0, 0, 0);
                mView.requestLayout();
            }
        }
    }
}
