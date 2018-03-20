package com.example.dainq.smilenotes.ui.profile.product;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dainq.smilenotes.common.Constant;
import com.example.dainq.smilenotes.common.Utility;
import com.example.dainq.smilenotes.controllers.realm.RealmController;
import com.example.dainq.smilenotes.model.object.ProductObject;

import java.util.Calendar;
import java.util.Date;

import nq.dai.smilenotes.R;

public class CreateProductDialog extends DialogFragment implements OnClickListener {
    private String TAG = "CreatePlanDialog";

    private int mIdcustomer;
    private TextView mTitle;
    private TextView mTime;
    private Date mTimeValue;
    private EditText mContent;

    private TextView mSave;
    private TextView mCancel;

    private ProductAdapter mAdapter;
    private RealmController mRealmController;

    private SharedPreferences mPref;
    private int mIdKey;

    public CreateProductDialog(ProductAdapter adapter) {
        mAdapter = adapter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_new_plan, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mRealmController = RealmController.getInstance();

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);

        mTitle = (TextView) view.findViewById(R.id.dialog_title_text);
        mTitle.setText(R.string.add_new_product);

        LinearLayout dialoTime = (LinearLayout) view.findViewById(R.id.dialog_time_lay);
        dialoTime.setVisibility(View.GONE);

        TextView txtTitleContent = (TextView) view.findViewById(R.id.dialog_txt_title_content);
        txtTitleContent.setText(R.string.type_product);

        TextView txtTitleDate = (TextView) view.findViewById(R.id.dialog_txt_title_schedule);
        txtTitleDate.setText(R.string.time_use);

        mSave = (TextView) view.findViewById(R.id.dialog_btn_save);
        mSave.setOnClickListener(this);

        mCancel = (TextView) view.findViewById(R.id.dialog_btn_cancel);
        mCancel.setOnClickListener(this);

        mTime = (TextView) view.findViewById(R.id.dialog_txt_schedule);
        mTime.setOnClickListener(this);

        mContent = (EditText) view.findViewById(R.id.dialog_edit_conttent);
        mContent.setOnClickListener(this);
        mContent.setText("");

        mIdcustomer = getArguments().getInt(Constant.KEY_ID_PRODUCT);
        mPref = getActivity().getSharedPreferences(Constant.PREF_PLAN, Context.MODE_PRIVATE);
        mIdKey = mPref.getInt(Constant.KEY_ID, Constant.PREF_ID_DEFAULT);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_btn_save:
                int val = validate();
                if (val == Constant.VALIDATE_SUCCESS) {
                    create();
                    Toast.makeText(getActivity(), R.string.add_success, Toast.LENGTH_SHORT).show();
                    mAdapter.notifyDataSetChanged();
                    dismiss();
                } else {
                    Toast.makeText(getActivity(), R.string.empty_content_product, Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.dialog_btn_cancel:
                dismiss();
                break;

            case R.id.dialog_txt_schedule:
                initDialogDatePicker();
                break;

            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        Window window = getDialog().getWindow();
        window.setLayout((int) (width * 0.9f), (int) (height * 0.65f));
        window.setGravity(Gravity.CENTER);
    }

    public void initDialogDatePicker() {
        DatePickerDialog dialog;
        Calendar calendar = Calendar.getInstance();
        dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                mTimeValue = newDate.getTime();
                mTime.setText(Utility.dateToString(mTimeValue));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        dialog.getDatePicker().setLayoutMode(0);
        dialog.show();
    }

    private void setData(ProductObject object) {
        object.setUsedate(mTimeValue);
        object.setName(mContent.getText().toString());
    }

    private void create() {
        ProductObject product = new ProductObject();
        int id = Utility.createId(mIdKey);
        Log.d(TAG + "dialog", " put product id: " + id);
        mPref.edit().putInt(Constant.KEY_ID, id).apply();

        product.setId(id);
        product.setIdcustomer(mIdcustomer);
        setData(product);

        mRealmController.addProduct(product);
    }

    private int validate() {
        String time = mTime.getText().toString();
        String content = mContent.getText().toString();

        if (TextUtils.isEmpty(time)
                || TextUtils.isEmpty(content)) {
            return Constant.VALIDATE_EMPTY;
        }

        return Constant.VALIDATE_SUCCESS;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        mContent.setText("");
    }
}
