package com.example.dainq.smilenotes.ui.profile.product;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.dainq.smilenotes.common.BaseURL;
import com.example.dainq.smilenotes.common.Constant;
import com.example.dainq.smilenotes.common.SessionManager;
import com.example.dainq.smilenotes.common.Utility;
import com.example.dainq.smilenotes.controllers.api.APIProduct;
import com.example.dainq.smilenotes.model.request.product.ProductRequest;
import com.example.dainq.smilenotes.model.response.product.ProductResponse;

import java.util.Calendar;
import java.util.Date;

import nq.dai.smilenotes.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CreateProductDialog extends DialogFragment implements OnClickListener {
    private String TAG = "CreateProductDialog";

    private TextView mTitle;
    private TextView mTime;
    private Date mTimeValue;
    private EditText mName;
    private EditText mMemo;

    private TextView mSave;
    private TextView mCancel;

    private ProductAdapter mAdapter;

    //session of user
    private SessionManager mSession;

    //interface service
    private APIProduct mService;

    //id of customer
    private String mIdcustomer;

    //context
    private Context mContext;

    public CreateProductDialog(ProductAdapter adapter, Context context) {
        mContext = context;
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
        //create session of user
        mSession = new SessionManager(mContext);

        //init retrofit
        initRetrofit();

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);

        mTitle = (TextView) view.findViewById(R.id.dialog_title_text);
        mTitle.setText(R.string.add_new_product);

        //rename title
        TextView txtTitleName = (TextView) view.findViewById(R.id.dialog_txt_title_time);
        txtTitleName.setText(R.string.type_product);

        TextView txtTitleMemo = (TextView) view.findViewById(R.id.dialog_txt_title_content);
        txtTitleMemo.setText(R.string.memo_product);

        TextView txtTitleDate = (TextView) view.findViewById(R.id.dialog_txt_title_schedule);
        txtTitleDate.setText(R.string.time_use);

        //button save
        mSave = (TextView) view.findViewById(R.id.dialog_btn_save);
        mSave.setOnClickListener(this);

        //button cancel
        mCancel = (TextView) view.findViewById(R.id.dialog_btn_cancel);
        mCancel.setOnClickListener(this);

        //edit date
        mTime = (TextView) view.findViewById(R.id.dialog_txt_schedule);
        mTime.setOnClickListener(this);

        //edit name
        mName = (EditText) view.findViewById(R.id.dialog_txt_time);
        mName.setText("");

        //edit memo
        mMemo = (EditText) view.findViewById(R.id.dialog_edit_conttent);
        mMemo.setText("");

        mIdcustomer = getArguments().getString(Constant.KEY_ID_PRODUCT);
    }

    /*
    create retrofit
     */
    private void initRetrofit() {
        Retrofit retrofit = Utility.initRetrofit(BaseURL.URL_PRODUCT);
        mService = retrofit.create(APIProduct.class);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_btn_save:
                int val = validate();
                if (val == Constant.VALIDATE_SUCCESS) {
                    String date = mTime.getText().toString();
                    String name = mName.getText().toString();
                    String memo = mMemo.getText().toString();

                    processCreateProduct(name, date, memo);
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

    private int validate() {
        String time = mTime.getText().toString();
        String name = mName.getText().toString();
        String memo = mName.getText().toString();

        if (TextUtils.isEmpty(time)
                || TextUtils.isEmpty(name) || TextUtils.isEmpty(memo)) {
            return Constant.VALIDATE_EMPTY;
        }

        return Constant.VALIDATE_SUCCESS;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        mName.setText("");
        mMemo.setText("");
        super.onDismiss(dialog);
    }

    /*
     * process to create new product of that customer
     */
    private void processCreateProduct(String name, String date, String memo) {
        //get userId and token from session
        String userId = mSession.getUserDetails().getId();
        String token = mSession.getUserDetails().getToken();

        //customerId get in arguments pass from fragment
        String customerId = mIdcustomer;

        //create request to send
        final ProductRequest request = new ProductRequest();
        request.setUserId(userId);
        request.setCustomerId(customerId);
        request.setName(name);
        request.setExpireDate(date);
        request.setMemo(memo);

        //create response
        Call<ProductResponse> response = mService.addNewProduct(request, token);
        response.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                ProductResponse serverRespone = response.body();

                if (serverRespone != null) {
                    int code = serverRespone.getCode();
                    if (code == Constant.RESPONSE_SUCCESS) {
                        Log.d(TAG, "-->[process-create-product] onResponse: " + serverRespone.getCode() + ": " + serverRespone.getMessage());

                        Toast.makeText(mContext, "Đã thêm mới!", Toast.LENGTH_SHORT).show();

                        //dismiss dialog
                        dismiss();
                    } else {
                        Log.d(TAG, "-->[process-create-product] onResponse: " + serverRespone.getCode() + ": " + serverRespone.getMessage());
                    }
                } else {
                    Log.d(TAG, "-->[process-create-product] onResponse: " + response.code() + ": " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Log.d(TAG, "-->[process-create-product] failure: " + t.getMessage());
            }
        });
    }
}
