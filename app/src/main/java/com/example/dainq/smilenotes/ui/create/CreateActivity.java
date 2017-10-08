package com.example.dainq.smilenotes.ui.create;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dainq.smilenotes.common.BaseActivity;
import com.example.dainq.smilenotes.common.Constant;
import com.example.dainq.smilenotes.common.Utility;
import com.example.dainq.smilenotes.controller.realm.RealmController;
import com.example.dainq.smilenotes.model.CustomerObject;
import com.example.dainq.smilenotes.ui.common.spinner.OnSpinnerItemSelectedListener;
import com.example.dainq.smilenotes.ui.common.spinner.SingleSpinnerLayout;
import com.example.dainq.smilenotes.ui.common.spinner.SpinnerItem;
import com.example.dainq.smilenotes.ui.notifications.NotificationReceiver;
import com.example.dainq.smilenotes.ui.notifications.NotificationScheduler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import nq.dai.smilenotes.R;

public class CreateActivity extends BaseActivity implements View.OnClickListener, OnSpinnerItemSelectedListener {

    private int mLevel;
    private EditText mADA;
    private EditText mName;
    private TextView mDateOfBirth;
    private Date mValueDate;
    private EditText mPhoneNumber;
    private EditText mAddress;
    private EditText mReason;
    private EditText mProblem;
    private EditText mSolution;
    private EditText mNote;

    private boolean isSave;
    private Realm mRealm;
    private RealmController mRealmController;
    private CustomerObject mCustomer;

    private DatePickerDialog mDialogDateOfBirth;
    private RatingBar mRatingBar;

    private SharedPreferences mPref;
    private int mIdKey;
    private int mAction;
    private SingleSpinnerLayout mSpinner;
    private int mCustomerId;

    private int mDateDOB;
    private int mMonthDOB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        initView();
    }

    private void initView() {
        initToolBar();
        isSave = false;
        mAction = getAction();

        TextView mButtonSave = (TextView) findViewById(R.id.create_btn_save);
        mButtonSave.setOnClickListener(this);

        TextView mButtonCancel = (TextView) findViewById(R.id.create_btn_cancel);
        mButtonCancel.setOnClickListener(this);

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

        mRealmController = RealmController.with(this);
        mRealm = RealmController.with(this).getRealm();
        mRatingBar = (RatingBar) findViewById(R.id.create_rating_bar);

        initDialogDateOfBirth();
        initSpinner();

        mPref = this.getSharedPreferences(Constant.PREF_NAME, Context.MODE_PRIVATE);
        mIdKey = mPref.getInt(Constant.KEY_ID_CUSTOMER, Constant.PREF_ID_DEFAULT);
        Log.d(CreateActivity.class.getSimpleName() + "-dainq", " pref id: " + mIdKey);

        if (mAction == Constant.ACTION_CREATE) {
            mButtonSave.setText(R.string.save);
            getSupportActionBar().setTitle(R.string.title_create_new_customer);
        } else {
            mButtonSave.setText(R.string.update);
            getSupportActionBar().setTitle(R.string.update_info);

            RealmController mRealmController = RealmController.with(this);
            mCustomer = mRealmController.getCustomer(mCustomerId);

            mSpinner.setSelection(mCustomer.getLevel());
            mADA.setText(mCustomer.getAda());
            mName.setText(mCustomer.getName());
            mDateOfBirth.setText(mCustomer.getDateofbirth().toString());
            mPhoneNumber.setText(mCustomer.getPhonenumber());
            mAddress.setText(mCustomer.getAddress());
            mReason.setText(mCustomer.getReason());
            mProblem.setText(mCustomer.getProblem());
            mSolution.setText(mCustomer.getSolution());
            mNote.setText(mCustomer.getNote());
        }
    }

    private int getAction() {
        Bundle extras = getIntent().getExtras();
        mCustomerId = extras.getInt(Constant.KEY_ID_CUSTOMER);
        Log.d(CreateActivity.class.getSimpleName() + "-dainq", " action: " + mAction);
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
                    onSave();
                } else {
                    onEdit();
                }
                break;

            case R.id.create_btn_cancel:
                onBackPressed();
                break;

            case R.id.create_edit_date_of_birth:
                mDialogDateOfBirth.show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


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
        String ada = mADA.getText().toString();
        String phone = mPhoneNumber.getText().toString();
        String address = mAddress.getText().toString();

        return (Utility.isEmptyString(ada)
                || Utility.isEmptyString(name)
                || Utility.isEmptyString(phone)
                || Utility.isEmptyString(address));

    }

    private boolean isPhoneNumber(String string) {
        return string.matches(Constant.regexPhoneNumber);
    }

    private void makeToast(int val) {
        if (val == Constant.VALIDATE_SUCCESS) {
            Toast.makeText(this, R.string.save_success, Toast.LENGTH_SHORT).show();
            return;
        }
        if (val == Constant.VALIDATE_EMPTY) {
            Toast.makeText(this, R.string.toast_validate_string, Toast.LENGTH_SHORT).show();
            return;
        }
        if (val == Constant.VALIDATE_PHONE) {
            Toast.makeText(this, R.string.phone_number_is_wrong, Toast.LENGTH_SHORT).show();
        }
    }

    public void initDialogDateOfBirth() {
        final SimpleDateFormat dateFormat = new SimpleDateFormat(Constant.FORMAT_DATE, Locale.US);
        Calendar calendar = Calendar.getInstance();
        mDialogDateOfBirth = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                mDateOfBirth.setText(dateFormat.format(newDate.getTime()));

                mValueDate = newDate.getTime();
                mDateDOB = dayOfMonth;
                mMonthDOB = monthOfYear;
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        mDialogDateOfBirth.getDatePicker().setLayoutMode(1);
    }

    @Override
    public void onItemSelected(int position) {
        mLevel = position;
        mRatingBar.setRating(position + 1);
    }

    @Override
    public void onNothingSelected() {
    }

    private int createId() {
        return mIdKey + 1;
    }

    private void saveCustomer() {
        int val = validate();
        if (val == Constant.VALIDATE_SUCCESS) {
            save();
            isSave = true;
        } else {
            isSave = false;
        }
        makeToast(val);
    }

    private void onSave() {
        saveCustomer();
        if (isSave) {
            onBackPressed();
        }
    }

    private void save() {
        CustomerObject customer = new CustomerObject();
        int id = createId();
        Log.d(CreateActivity.class.getSimpleName() + "-dainq", " put id: " + id);
        mPref.edit().putInt(Constant.KEY_ID_CUSTOMER, id).apply();

        customer.setId(id);
        customer.setLevel(mLevel);
        customer.setAda(mADA.getText().toString());
        customer.setName(mName.getText().toString());
        customer.setDateofbirth(mValueDate);
        customer.setPhonenumber(mPhoneNumber.getText().toString());
        customer.setAddress(mAddress.getText().toString());
        customer.setReason(mReason.getText().toString());
        customer.setProblem(mProblem.getText().toString());
        customer.setSolution(mSolution.getText().toString());
        customer.setNote(mNote.getText().toString());

        mRealmController.addCustomer(customer);

        NotificationScheduler.scheduleRepeatingRTCNotification(this, "0", "1");
    }

    private void onEdit() {
        editCustomer();
        if (isSave) {
            onBackPressed();
        }
    }

    private void editCustomer() {
        int val = validate();
        if (val == Constant.VALIDATE_SUCCESS) {
            edit();
            isSave = true;
        } else {
            isSave = false;
        }
        makeToast(val);
    }

    private void edit() {
        mRealm.beginTransaction();

        mCustomer.setLevel(mLevel);
        mCustomer.setAda(mADA.getText().toString());
        mCustomer.setName(mName.getText().toString());
        mCustomer.setDateofbirth(mValueDate);
        mCustomer.setPhonenumber(mPhoneNumber.getText().toString());
        mCustomer.setAddress(mAddress.getText().toString());
        mCustomer.setReason(mReason.getText().toString());
        mCustomer.setProblem(mProblem.getText().toString());
        mCustomer.setSolution(mSolution.getText().toString());
        mCustomer.setNote(mNote.getText().toString());

        mRealm.commitTransaction();

        Intent intent = new Intent(this, NotificationReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.CUSTOMER_NAME, mCustomer.getName());
        bundle.putInt(Constant.NOTIFICATION_TYPE, Constant.NOTIFICATION_BIRTH_DAY);
        intent.putExtras(bundle);

//        NotificationScheduler.scheduleRepeatingElapsedNotification(this);
    }

//    private void setNotification(Context context, long time) {
//        Intent intent = new Intent(context, NotificationReceiver.class);
//        Bundle bundle = new Bundle();
//        bundle.putString(Constant.CUSTOMER_NAME, mCustomer.getName());
//        bundle.putInt(Constant.NOTIFICATION_TYPE, Constant.NOTIFICATION_BIRTH_DAY);
//        intent.putExtras(bundle);
//
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//
//        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        am.setExact(1, time, pendingIntent);
//        Log.d("dainq", " Notification create");
//    }
}