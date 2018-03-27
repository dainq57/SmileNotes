package com.example.dainq.smilenotes.ui.create;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.dainq.smilenotes.common.BaseURL;
import com.example.dainq.smilenotes.common.Constant;
import com.example.dainq.smilenotes.common.SessionManager;
import com.example.dainq.smilenotes.common.Utility;
import com.example.dainq.smilenotes.controllers.api.APICustomer;
import com.example.dainq.smilenotes.model.request.customer.CustomerRequest;
import com.example.dainq.smilenotes.model.response.customer.CustomerResponse;
import com.example.dainq.smilenotes.ui.common.spinner.OnSpinnerItemSelectedListener;
import com.example.dainq.smilenotes.ui.common.spinner.SingleSpinnerLayout;
import com.example.dainq.smilenotes.ui.common.spinner.SpinnerItem;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import nq.dai.smilenotes.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CreateActivity extends AppCompatActivity implements View.OnClickListener, OnSpinnerItemSelectedListener {
    private static final String TAG = "CreateActivity";

    private int mLevel;

    private Date dateOfBirth;

    //string dob
    private String mDob;

    //id to set object
    private EditText mADA;
    private EditText mName;
    private TextView mDateOfBirth;
    private EditText mPhoneNumber;
    private EditText mAddress;
    private EditText mReason;
    private EditText mProblem;
    private EditText mSolution;
    private EditText mNote;
    private EditText mProductNeed;
    private LinearLayout mAdaLayout;
    private CircleImageView mAvatar;
    private EditText mJob;
    private RadioButton mMale, mFemale;
    private RadioGroup mGender;

    //uri of avatar
    private Uri mUri;

    //customer object get from server to update information
    private CustomerRequest mCustomer;

    //version data of customer
    private int mVersion;

    private DatePickerDialog mDialogDateOfBirth;

    private RatingBar mRatingBar;

    //value id get from pref
    private int mAction;

    //spinner select level
    private SingleSpinnerLayout mSpinner;

    //id customer when show profile of customer
    private String mCustomerId;

    //session of user
    private SessionManager mSession;

    //interface service customer
    private APICustomer mService;

    //progress view
    private ProgressBar mProgressView;

    //rootLayout of view
    private RelativeLayout mRootLayout;

    //id user
    private String mIdUser;

    //token user
    private String mToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        initView();
    }

    private void initView() {
        initToolBar();
        //create session manager and api interface
        mSession = new SessionManager(this);
        initRetrofit();

        //get id and token of user
        mIdUser = mSession.getUserDetails().getId();
        mToken = mSession.getUserDetails().getToken();

        mAction = getAction();

        mAdaLayout = (LinearLayout) findViewById(R.id.create_filter_ada);
        TextView mButtonSave = (TextView) findViewById(R.id.create_btn_save);
        mButtonSave.setOnClickListener(this);

        TextView mButtonCancel = (TextView) findViewById(R.id.create_btn_cancel);
        mButtonCancel.setOnClickListener(this);

        mAvatar = (CircleImageView) findViewById(R.id.create_avatar);
        mAvatar.setOnClickListener(this);

        mADA = (EditText) findViewById(R.id.create_edit_ada);

        mName = (EditText) findViewById(R.id.create_edit_name);

        mDateOfBirth = (TextView) findViewById(R.id.create_edit_date_of_birth);
        mDateOfBirth.setOnClickListener(this);

        mPhoneNumber = (EditText) findViewById(R.id.create_edit_phonenumber);

        mAddress = (EditText) findViewById(R.id.create_edit_address);

        mReason = (EditText) findViewById(R.id.create_edit_reason);

        mProblem = (EditText) findViewById(R.id.create_edit_problem);

        mSolution = (EditText) findViewById(R.id.create_edit_solution);

        mNote = (EditText) findViewById(R.id.create_edit_note);

        mProductNeed = (EditText) findViewById(R.id.create_edit_product_need);

        mJob = (EditText) findViewById(R.id.create_edit_job);

        mGender = (RadioGroup) findViewById(R.id.create_gender);

        mMale = (RadioButton) mGender.findViewById(R.id.create_gender_male);

        mFemale = (RadioButton) mGender.findViewById(R.id.create_gender_female);

        mRatingBar = (RatingBar) findViewById(R.id.create_rating_bar);

        initDialogDateOfBirth();
        initSpinner();

        mProgressView = (ProgressBar) findViewById(R.id.progress_bar);
        mRootLayout = (RelativeLayout) findViewById(R.id.root_layout);

        if (mAction == Constant.ACTION_CREATE) {
            mButtonSave.setText(R.string.save);
            getSupportActionBar().setTitle(R.string.title_create_new_customer);
            mMale.setChecked(true);
        } else {
            //change text of button
            mButtonSave.setText(R.string.update);

            //change title
            getSupportActionBar().setTitle(R.string.update_info);

            //get Customer need edit
            processGetCustomer(mCustomerId);
        }
    }

    private void initRetrofit() {
        Retrofit retrofit = Utility.initRetrofit(BaseURL.URL_CUSTOMER);

        mService = retrofit.create(APICustomer.class);
    }

    /*
    get customer infomation before edit
     */
    private void processGetCustomer(String idCustomer) {
        Call<CustomerResponse> response = mService.getCustomer(mIdUser, idCustomer, mToken);
        response.enqueue(new Callback<CustomerResponse>() {
            @Override
            public void onResponse(Call<CustomerResponse> call, Response<CustomerResponse> response) {
                CustomerResponse serverResponse = response.body();
                //hide progress bar
                mProgressView.setVisibility(View.INVISIBLE);

                if (serverResponse != null) {
                    int code = serverResponse.getCode();

                    if (code == Constant.RESPONSE_SUCCESS) {
                        Log.d(TAG, "-->[process-get-customer] onResponse-success: " + serverResponse.getMessage());
                        mCustomer = serverResponse.getData();

                        Log.d(TAG, "-->[get-data-customer] version: " + mCustomer.getVersion());
                        //set data to customer before edit
                        setDataToCustomer(mCustomer);
                    } else {
                        Log.d(TAG, "-->[process-get-customer] onResponse-not-success: " + serverResponse.getMessage());
                    }
                } else {
                    Log.d(TAG, "-->[process-get-customer] onResponse-success: null");
                }
            }

            @Override
            public void onFailure(Call<CustomerResponse> call, Throwable t) {
                Log.d(TAG, "-->[process-get-customer] failure!" + t.getMessage());
            }
        });
    }

    /*
    set data to customer to show data of customer in action edit
     */
    private void setDataToCustomer(CustomerRequest customer) {
        mSpinner.setSelection(customer.getLevel());
        mADA.setText(customer.getAdaCode());
        mName.setText(customer.getName());

        String tempDate = customer.getDateOfBirth();
        mDateOfBirth.setText(tempDate);

        mPhoneNumber.setText(customer.getPhone());
        mAddress.setText(customer.getAddress());
        mReason.setText(customer.getReason());
//      mProblem.setText(mCustomer.getProblemType());
        mSolution.setText(customer.getSolution());
        mProductNeed.setText(customer.getSuggestProduct());
        mJob.setText(customer.getJob());

        if (customer.getGender() == 0) {
            mFemale.setChecked(true);
        } else {
            mMale.setChecked(true);
        }

        String avatar = customer.getPathAvatar();
        if (avatar != null) {
            mAvatar.setImageBitmap(Utility.decodeImage(avatar));
        }

        //get version
        mVersion = customer.getVersion();
    }

    //get action of user to create or update infomation of customer
    private int getAction() {
        //get data pass from MainActivity via bundle
        Bundle extras = getIntent().getExtras();
        mCustomerId = extras.getString(Constant.KEY_ID);
        Log.d(TAG, " -->[getAction] action: " + mAction);
        return extras.getInt(Constant.KEY_ACTION, Constant.ACTION_CREATE);
    }

    private void initToolBar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.tw_ic_ab_back_mtrl);
        mToolbar.getNavigationIcon().setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
    }

    private void initSpinner() {
        mSpinner = (SingleSpinnerLayout) findViewById(R.id.create_spinner_type);

        if (mSpinner != null) {
            String[] menuItem = getResources().getStringArray(R.array.array_spinner_create);
            mSpinner.setSpinnerList(SpinnerItem.getSpinnerItem(menuItem));
            mSpinner.setOnSpinnerItemSelectedListener(this);
            mSpinner.getSpinner().setDropDownHorizontalOffset(getResources().getDimensionPixelSize(R.dimen.dropdown_offset_vertical));
            mSpinner.setSelection(0);
            mLevel = Constant.CUSTOMER_LEVEL_0;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.create_btn_save:
                if (mAction == Constant.ACTION_CREATE) {
                    createCustomer();
                } else {
                    editCustomer();
                }
                break;

            case R.id.create_btn_cancel:
                onBackPressed();
                break;

            case R.id.create_edit_date_of_birth:
                mDialogDateOfBirth.show();
                break;

            case R.id.create_avatar:
                if (!Utility.checkPermissionForReadExtertalStorage(this)) {
                    Utility.requestPermission(this);
                } else {
                    Crop.pickImage(this);
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        String title, content;
        if (mAction == Constant.ACTION_CREATE) {
            title = getResources().getString(R.string.title_dialog_back_create);
            content = getResources().getString(R.string.dialog_content_create);
        } else {
            title = getResources().getString(R.string.title_dialog_back_edit);
            content = getResources().getString(R.string.dialog_content_edit);
        }
        confirmDialog(title, content);
    }

    private void confirmDialog(String title, String content) {
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(content)
                .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CreateActivity.this.finish();
                    }
                })
                .setNegativeButton(R.string.nope, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        dialog.dismiss();
                    }
                })
                .setIcon(getDrawable(R.drawable.icn_alert_128))
                .show();
    }

    /**
     * validate before create of update
     * return int value
     */
    private int validate() {
        if (isEmpty()) {
            return Constant.VALIDATE_EMPTY;
        }

        String number = mPhoneNumber.getText().toString();
        if (!isPhoneNumber(number)) {
            return Constant.VALIDATE_PHONE;
        }
        return Constant.VALIDATE_SUCCESS;
    }

    private boolean isEmpty() {
        String name = mName.getText().toString();
        String phone = mPhoneNumber.getText().toString();
        String address = mAddress.getText().toString();

        return (TextUtils.isEmpty(name)
                || TextUtils.isEmpty(phone)
                || TextUtils.isEmpty(address));

    }

    /*
    *check number is phone number by regex
     */
    private boolean isPhoneNumber(String string) {
        return string.matches(Constant.regexPhoneNumber);
    }

    /**
     * dialog to select date of birth
     */
    public void initDialogDateOfBirth() {
        Calendar calendar = Calendar.getInstance();
        //reset hour to 0h
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        //get current year and current day

        mDialogDateOfBirth = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                dateOfBirth = newDate.getTime();
                mDob = Utility.dateToString(dateOfBirth);
                mDateOfBirth.setText(mDob);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        mDialogDateOfBirth.getDatePicker().setLayoutMode(1);
    }

    @Override
    public void onItemSelected(int position) {
        mLevel = position;
        mRatingBar.setRating(position + 1);
        if (mLevel < Constant.CUSTOMER_LEVEL_3) {
            mAdaLayout.setVisibility(View.GONE);
        } else {
            mAdaLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNothingSelected() {
    }

    /*---------------------FUNTION-CREATE-CUSTOMER-------------------*/

    /*
    * do call validate and save customer if calidate is success
    * */
    private void createCustomer() {
        int val = validate();
        if (val == Constant.VALIDATE_SUCCESS) {
            //create data customer
            CustomerRequest customer = createDataCustomer();

            //show progress bar
            mProgressView.setVisibility(View.VISIBLE);

            //do create customer
            processCreateCustomer(customer);
        }
    }

    /*
    * create object customer
    * set data before create in processCreate
    * */
    private CustomerRequest createDataCustomer() {
        CustomerRequest customer = new CustomerRequest();

        //setId
        customer.setUserId(mSession.getUserDetails().getId());
        Log.d(TAG, "-->[createDataCustomer] idUser: " + mSession.getUserDetails().getId());

        //setLevel
        customer.setLevel(mLevel);

        //customer has adaCode if level > level 2
        if (mLevel > Constant.CUSTOMER_LEVEL_2) {
            String ada = mADA.getText().toString();
            if (!TextUtils.isEmpty(ada)) {
                //setAda
                customer.setAdaCode(mADA.getText().toString());
            }
        }

        if (mUri != null) {
            String avatar = Utility.convertImage(this, mUri);
            customer.setPathAvatar(avatar);
        }

        //setName
        String name = mName.getText().toString();
        customer.setName(name);

        if (mDob != null) {
            //setDOB

            customer.setDateOfBirth(mDob);
        }

        //setPhonenumber
        customer.setPhone(mPhoneNumber.getText().toString());

        //setAddress
        customer.setAddress(mAddress.getText().toString());

        //setReason
        customer.setReason(mReason.getText().toString());

        //setProblemType
//        customer.setProblemType(mProblem.getText().toString());

        //setSolution
        customer.setSolution(mSolution.getText().toString());

        //setSuggestProduct
        customer.setSuggestProduct(mProductNeed.getText().toString());

        //setJob
        customer.setJob(mJob.getText().toString());

        //setGender
        int index = mGender.indexOfChild(findViewById(mGender.getCheckedRadioButtonId()));
        customer.setGender(index);

        return customer;
    }

    /*
    * process createCustomer
    * params customer request
    * */
    private void processCreateCustomer(CustomerRequest customer) {
        //alaways add token into request to server

        //create response with info of customer and token
        Call<CustomerResponse> response = mService.createCustomer(customer, mToken);
        response.enqueue(new Callback<CustomerResponse>() {
            @Override
            public void onResponse(Call<CustomerResponse> call, Response<CustomerResponse> response) {
                CustomerResponse serverRespone = response.body();

                //hide progress
                mProgressView.setVisibility(View.INVISIBLE);

                if (serverRespone != null) {
                    //get code from serverRespone
                    int code = serverRespone.getCode();

                    Log.d(TAG, "-->[process-create-customer] response: " + response.code() + " | " + code + ": " + serverRespone.getMessage());

                    //check case with code
                    if (code == Constant.RESPONSE_CREATE_SUCCESS) {
                        Utility.makeSnackbar(mRootLayout, "Tạo thành công");

                        //remove date after create success
                        formatField();
                    } else if (code == Constant.RESPONSE_CREATE_EXIT) {
                        Utility.makeSnackbar(mRootLayout, "SDT đã tồn tại");
                    }
                } else {
                    Utility.makeSnackbar(mRootLayout, "#error unknown");
                    Log.d(TAG, "-->>[process-create-customer] Connect to server is error.");
                }
            }

            @Override
            public void onFailure(Call<CustomerResponse> call, Throwable t) {
                //hide progress
                mProgressView.setVisibility(View.INVISIBLE);
                Utility.makeSnackbar(mRootLayout, "Kiểm tra kết nối interet");
                Log.d(TAG, "--->[process-create-customer] onFailure: " + t.getMessage());
            }
        });
    }

    /*
     * reformat data after create success customer
     */
    private void formatField() {
        mAvatar.setImageBitmap(null);
        mRatingBar.setRating(0);
        mADA.setText("");
        mName.setText("");
        mDateOfBirth.setText("");
        mPhoneNumber.setText("");
        mAddress.setText("");
        mJob.setText("");
        mReason.setText("");
        mProblem.setText("");
        mSolution.setText("");
        mNote.setText("");
        mProductNeed.setText("");
    }

    /*----------------------END-FUNTION-CREATE-CUSTOMER----------------*/

    /**
     *
     *
     */

    /*----------------------FUNTION-EDIT-CUSTOMER----------------*/
    private void editCustomer() {
        int val = validate();
        if (val == Constant.VALIDATE_SUCCESS) {
            //create data customer
            CustomerRequest customerRequest = getDataUpdate();

            //show progress bar
            mProgressView.setVisibility(View.VISIBLE);

            //do create customer
            processUpdateCustomer(customerRequest);
        }
    }

    /**
     * get data update before update to server
     */
    private CustomerRequest getDataUpdate() {
        //get data from field
        CustomerRequest customerRequest = createDataCustomer();

        //set id of customer
        customerRequest.setId(mCustomerId);

        //increase version of data
//        int ver = mVersion + 1;
        Log.d(TAG, "-->[get-data-update] version: " + mVersion);
        customerRequest.setVersion(mVersion);

        return customerRequest;
    }

    /**
     * process update information of customer
     */
    private void processUpdateCustomer(CustomerRequest customer) {
        Call<CustomerResponse> response = mService.updateCustomer(customer, mToken);
        response.enqueue(new Callback<CustomerResponse>() {
            @Override
            public void onResponse(Call<CustomerResponse> call, Response<CustomerResponse> response) {
                CustomerResponse serverResponse = response.body();

                //hide progress bar
                mProgressView.setVisibility(View.INVISIBLE);

                if (serverResponse != null) {
                    int code = serverResponse.getCode();
                    if (code == Constant.RESPONSE_SUCCESS) {
                        Log.d(TAG, "--->[process-update-customer] response-sucess: " + serverResponse.getMessage());

                        Utility.makeSnackbar(mRootLayout, "Cập nhật thành công!");
                        finish();
                    } else {
                        Log.d(TAG, "--->[process-update-customer] response: " + serverResponse.getCode() + ":" + serverResponse.getMessage());
                    }
                } else {
                    if (response.code() == 11) {
                        //todo create dialog view
                        Utility.makeSnackbar(mRootLayout, "Dữ liệu hiện tại chưa phải mới nhất!");
                    } else {
                        Log.d(TAG, "--->[process-update-customer] response null: " + response.code() + ":" + response.message());
                    }
                }
            }

            @Override
            public void onFailure(Call<CustomerResponse> call, Throwable t) {
                Log.d(TAG, "--->[process-update-customer] failure " + t.getMessage());
            }
        });
    }

    /**
     * crop image to avatar
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
            //resume tasks needing this permission
            Crop.pickImage(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        }

        super.onActivityResult(requestCode, resultCode, result);
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            mAvatar.setImageDrawable(null);
            mUri = Crop.getOutput(result);
            mAvatar.setImageURI(mUri);
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}