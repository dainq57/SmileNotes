package com.example.dainq.smilenotes.ui.common.spinner;

import android.content.Context;
import android.os.Build;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import nq.dai.smilenotes.R;

public class SpinnerAdapter<T> extends ArrayAdapter<T> {

    public static final int INVALID_POSITION = -1;

    private final Context mContext;
    private LayoutInflater mInflater;
    private boolean mIsAppBar = false;

    private int mSelectedPosition = INVALID_POSITION;

    @Override
    public int getCount() {
        return super.getCount();
    }

    public Object getSelectedItem() {
        return getItem(mSelectedPosition);
    }

    public SpinnerAdapter(Context context, T[] objects) {
        super(context, 0, objects);
        this.mContext = context;
        init(context);
    }

    public SpinnerAdapter(Context context, T[] objects, boolean isAppBar) {
        super(context, 0, objects);
        this.mContext = context;
        this.mIsAppBar = isAppBar;
        init(context);
    }

    private void init(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int selectedItemPosition = 0;
        if (parent instanceof AdapterView) {
            selectedItemPosition = ((AdapterView) parent)
                    .getSelectedItemPosition();
        }
        View view = mInflater.inflate(R.layout.single_spinner_item, null);
        setSpinnerTextAppearance(view, selectedItemPosition);
        return view;
    }


    public void setSelectedPosition(int position) {
        mSelectedPosition = position;
    }

    private void setSpinnerTextAppearance(View view, int position) {
        final TextView spinnerText = (TextView) view.findViewById(R.id.spinner_text);
        spinnerText.setText(getItemLabel(position));
        spinnerText.setEllipsize(TextUtils.TruncateAt.END);
        if (mIsAppBar) setTextAppeareance(spinnerText, R.style.TextAppearance_Spinner_AppBar);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = mInflater.inflate(R.layout.single_spinner_dropdown_item, null);
        }
        parent.setVerticalScrollBarEnabled(false);
        setDropdownTextAppearance(position, view, parent);
        return view;
    }

    private void setDropdownTextAppearance(int position, View view, ViewGroup parent) {
        final TextView labelText = (TextView) view.findViewById(R.id.spinner_dropdown_text);
        labelText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        labelText.setText(getItemLabel(position));

        if (position == mSelectedPosition) {
            labelText.setEnabled(false);
            setTextAppeareance(labelText, R.style.TextAppearance_WinsetDropDownItem_Selected);
            view.setClickable(false);
        } else {
            labelText.setEnabled(true);
            setTextAppeareance(labelText, R.style.TextAppearance_WinsetDropDownItem);
            view.setClickable(false);
        }
    }

    private String getItemLabel(int position) {
        if (getItem(position) instanceof SpinnerItem) {
            return ((SpinnerItem) getItem(position)).getSingleSpinnerLabel();
        } else {
            return getItem(position).toString();
        }
    }

    public void setTextAppeareance(TextView text, @StyleRes int style) {
        if (text == null) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            text.setTextAppearance(style);
        } else {
            text.setTextAppearance(mContext, style);
        }
    }
}