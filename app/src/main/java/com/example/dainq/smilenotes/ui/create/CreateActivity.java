package com.example.dainq.smilenotes.ui.create;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.dainq.smilenotes.common.BaseActivity;
import com.example.dainq.smilenotes.common.Constant;
import com.example.dainq.smilenotes.common.Utility;
import com.example.dainq.smilenotes.controller.realm.RealmController;
import com.example.dainq.smilenotes.model.CustomerObject;
import com.example.dainq.smilenotes.model.NotificationObject;
import com.example.dainq.smilenotes.ui.common.spinner.OnSpinnerItemSelectedListener;
import com.example.dainq.smilenotes.ui.common.spinner.SingleSpinnerLayout;
import com.example.dainq.smilenotes.ui.common.spinner.SpinnerItem;
import com.example.dainq.smilenotes.ui.notifications.NotificationHelper;
import com.example.dainq.smilenotes.ui.notifications.NotificationReceiver;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.RealmResults;
import nq.dai.smilenotes.R;

public class CreateActivity extends BaseActivity implements View.OnClickListener, OnSpinnerItemSelectedListener {
    private static final String TAG = "CreateActivity";
    private static final int NUM_BIRTHDAY = 5;

    private int mLevel;
    //id to set object
    private int mId;
    private EditText mADA;
    private EditText mName;
    private TextView mDateOfBirth;
    //value date to view
    private Date dateOfBirth;
    //value date of birth to set notification
    private Date[] dateOfBirthValue;
    private EditText mPhoneNumber;
    private EditText mAddress;
    private EditText mReason;
    private EditText mProblem;
    private EditText mSolution;
    private EditText mNote;
    private EditText mProductNeed;
    private LinearLayout mAdaLayout;
    private CircleImageView mAvatar;
    //uri of avatar
    private Uri mUri;

    private boolean isSave;
    private CustomerObject mCustomer;
    private RealmController mRealmController;

    private DatePickerDialog mDialogDateOfBirth;
    private RatingBar mRatingBar;

    private SharedPreferences mPref, mPrefNoti;
    //value id get from pref
    private int mIdKey;
    private int mAction;
    private SingleSpinnerLayout mSpinner;
    //id customer when show profile of customer
    private int mCustomerId;

    //id get from pref of notification
    private int mIdNotiKey;

    private RealmResults<NotificationObject> mNotification;

    private String tempDate = "";

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

        mRealmController = RealmController.with(this);

        mRatingBar = (RatingBar) findViewById(R.id.create_rating_bar);
        dateOfBirthValue = new Date[NUM_BIRTHDAY + 1];
        initDialogDateOfBirth();
        initSpinner();

        mPref = this.getSharedPreferences(Constant.PREF_NAME, Context.MODE_PRIVATE);
        mIdKey = mPref.getInt(Constant.KEY_ID, Constant.PREF_ID_DEFAULT);
        Log.d(CreateActivity.class.getSimpleName() + "-dainq", " pref id: " + mIdKey);

        if (mAction == Constant.ACTION_CREATE) {
            mButtonSave.setText(R.string.save);
            getSupportActionBar().setTitle(R.string.title_create_new_customer);
        } else {
            mButtonSave.setText(R.string.update);
            getSupportActionBar().setTitle(R.string.update_info);

            mCustomer = mRealmController.getCustomer(mCustomerId);

            mSpinner.setSelection(mCustomer.getLevel());
            mADA.setText(mCustomer.getAda());
            mName.setText(mCustomer.getName());

            tempDate = Utility.dateToString(mCustomer.getDateofbirth());
            mDateOfBirth.setText(tempDate);
            mPhoneNumber.setText(mCustomer.getPhonenumber());
            mAddress.setText(mCustomer.getAddress());
            mReason.setText(mCustomer.getReason());
            mProblem.setText(mCustomer.getProblem());
            mSolution.setText(mCustomer.getSolution());
            mNote.setText(mCustomer.getNote());
            mProductNeed.setText(mCustomer.getProduct());

            String avatar = mCustomer.getAvatar();
            if (avatar != null) {
                mAvatar.setImageBitmap(Utility.decodeImage(avatar));
            }

            mNotification = mRealmController.getListNotificationBirthDay(mCustomerId);
        }

        mPrefNoti = getSharedPreferences(Constant.PREF_USER, Context.MODE_PRIVATE);
        mIdNotiKey = mPrefNoti.getInt(Constant.USER_NOTIFICATION, Constant.PREF_ID_DEFAULT);
        Log.d("dainqid ", "Create getIdNoti from pref " + mIdNotiKey);
    }

    private int getAction() {
        Bundle extras = getIntent().getExtras();
        mCustomerId = extras.getInt(Constant.KEY_ID);
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
                    saveCustomer();
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
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private int validate() {
        if (isEmpty()) {
            return Constant.VALIDATE_EMPTY;
        }

        if (mLevel > Constant.CUSTOMER_LEVEL_2) {
            String ada = mADA.getText().toString();
            if (adaIsExit()) {
                if (mAction == Constant.ACTION_EDIT) {
                    if (!ada.equals(mCustomer.getAda())) {
                        return Constant.VALIDATE_ADA;
                    }
                } else {
                    return Constant.VALIDATE_ADA;
                }
            }
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

        return (Utility.isEmptyString(name)
                || Utility.isEmptyString(phone)
                || Utility.isEmptyString(address)
                || Utility.isEmptyString(tempDate));

    }

    private boolean isPhoneNumber(String string) {
        return string.matches(Constant.regexPhoneNumber);
    }

    private boolean adaIsExit() {
        String ada = mADA.getText().toString();
        return mRealmController.isExit(ada);
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

        if (val == Constant.VALIDATE_ADA) {
            Toast.makeText(this, R.string.ada_is_exit, Toast.LENGTH_SHORT).show();
        }
    }

    public void initDialogDateOfBirth() {
        Calendar calendar = Calendar.getInstance();
        //reset hour to 0h
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        //get current year and current day
        final int currentyear = calendar.get(Calendar.YEAR);
        final int currentmonth = calendar.get(Calendar.MONTH);
        final int currentday = calendar.get(Calendar.DAY_OF_MONTH);

        mDialogDateOfBirth = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                newDate.set(Calendar.HOUR_OF_DAY, 0);

                dateOfBirth = newDate.getTime();
                String dateSet = Utility.dateToString(dateOfBirth);
                tempDate = dateSet;
                mDateOfBirth.setText(dateSet);

                //Value to set notification birthday
                //calculate real birthday
                if (monthOfYear >= currentmonth) {
                    if (dayOfMonth >= currentday) {
                        Log.d("dainq setdate", "dayOfMonth >= currentday");
                        newDate.add(Calendar.YEAR, (currentyear - year));
                    } else {
                        Log.d("dainq setdate", "dayOfMonth < currentday");
                        newDate.add(Calendar.YEAR, (currentyear - year) + 1);
                    }
                } else {
                    Log.d("dainq setdate", "monthOfYear < currentmonth");
                    newDate.add(Calendar.YEAR, (currentyear - year) + 1);
                }
//                newDate.set(Calendar.HOUR_OF_DAY, 0);
                dateOfBirthValue[0] = newDate.getTime();
                Log.d("dainq year", "dateofBirth noti " + dateOfBirthValue[0]);
                for (int i = 1; i < NUM_BIRTHDAY; i++) {
                    newDate.add(Calendar.YEAR, 1);
                    dateOfBirthValue[i] = newDate.getTime();
                    Log.d("dainq year", "dateofBirth noti " + dateOfBirthValue[i]);
                }
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        mDialogDateOfBirth.getDatePicker().

                setLayoutMode(1);
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

    private void saveCustomer() {
        int val = validate();
        if (val == Constant.VALIDATE_SUCCESS) {
            save();
            isSave = true;
        } else {
            isSave = false;
        }
        makeToast(val);
        if (isSave) {
            CreateActivity.this.finish();
        }
    }

    private void save() {
        CustomerObject customer = new CustomerObject();
        mId = Utility.createId(mIdKey);
        Log.d(CreateActivity.class.getSimpleName() + "-dainq", " put id: " + mId);
        mPref.edit().putInt(Constant.KEY_ID, mId).apply();

        Calendar dateCreate = Calendar.getInstance();
        customer.setId(mId);
        customer.setDatecreate(dateCreate.getTime());
        setData(customer);

        mRealmController.addCustomer(customer);
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
        if (isSave) {
            CreateActivity.this.finish();
        }
    }

    private void edit() {
        mRealmController.getRealm().beginTransaction();
        setData(mCustomer);
        mRealmController.getRealm().commitTransaction();

        for (int i = 0; i < NUM_BIRTHDAY; i++) {
            if (dateOfBirthValue[i] != null) {
                updateNotification(mNotification.get(i), dateOfBirthValue[i]);
            }
        }
    }

    private void setData(CustomerObject object) {
        object.setLevel(mLevel);
        if (mLevel > Constant.CUSTOMER_LEVEL_2) {
            String ada = mADA.getText().toString();
            if (!Utility.isEmptyString(ada)) {
                object.setAda(mADA.getText().toString());
            }
        }

        if (mUri != null) {
            String avatar = Utility.convertImage(this, mUri);
            object.setAvatar(avatar);
        }
        String tempName = mName.getText().toString();

        object.setName(tempName);
        if (dateOfBirth != null) {
            object.setDateofbirth(dateOfBirth);
        }
        object.setPhonenumber(mPhoneNumber.getText().toString());
        object.setAddress(mAddress.getText().toString());
        object.setReason(mReason.getText().toString());
        object.setProblem(mProblem.getText().toString());
        object.setSolution(mSolution.getText().toString());
        object.setNote(mNote.getText().toString());
        object.setProduct(mProductNeed.getText().toString());
        if (dateOfBirth != null) {
            if (mAction == Constant.ACTION_CREATE) {
                int id = mIdNotiKey;
                for (int i = 0; i < NUM_BIRTHDAY; i++) {
                    setNotification(dateOfBirthValue[i], id);
                    id++;
                }
                Log.d("dainqid ", "id noti put " + id);
                mPrefNoti.edit().putInt(Constant.USER_NOTIFICATION, id).apply();
            }
        }
    }

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

    @Override
    protected void onResume() {
        mRealmController = new RealmController(this);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mRealmController.close();
        super.onDestroy();
    }

    private void setNotification(Date date, int id) {
        //create notification to realm
        createNotification(date, id);
        //push notification use broadcast receiver
        Intent intent = new Intent(this, NotificationReceiver.class);
        Bundle bundle = new Bundle();

        bundle.putInt(Constant.NOTIFICATION_ID, id);

        intent.putExtras(bundle);

        NotificationHelper.setRemindRTC(this, date, intent);
        NotificationHelper.enableBootReceiver(this);
    }

    private void createNotification(Date date, int id) {
        NotificationObject notification = new NotificationObject();

        notification.setId(id);
        notification.setIdcustomer(mId);
        notification.setIsread(false);
        notification.setType(Constant.NOTIFICATION_BIRTH_DAY);
        notification.setContent("Sinh nhật ");
        notification.setDate(dateOfBirth);
        notification.setDatevalue(date);

        Calendar calendar = Calendar.getInstance();
        calendar.get(Calendar.YEAR);

        mRealmController.addNotification(notification);
    }

    private void updateNotification(NotificationObject notification, Date date) {
        mRealmController.getRealm().beginTransaction();
        notification.setDatevalue(date);
        notification.setDate(dateOfBirth);
        if (date != notification.getDatevalue()) {
            notification.setIsread(false);
        }
        mRealmController.getRealm().commitTransaction();
    }
}