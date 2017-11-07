package com.example.dainq.smilenotes.ui.profile.plan;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dainq.smilenotes.common.Constant;
import com.example.dainq.smilenotes.common.Utility;
import com.example.dainq.smilenotes.controller.realm.RealmController;
import com.example.dainq.smilenotes.model.CustomerObject;
import com.example.dainq.smilenotes.model.MeetingObject;
import com.example.dainq.smilenotes.model.NotificationObject;
import com.example.dainq.smilenotes.ui.notifications.NotificationHelper;
import com.example.dainq.smilenotes.ui.notifications.NotificationReceiver;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import nq.dai.smilenotes.R;

public class CreatePlanDialog extends DialogFragment implements View.OnClickListener {

    private String TAG = "CreatePlanDialog";
    private int mType;

    private Context mContext;
    private TextView mTitle;
    private int mIdCustomer;
    private TextView mTime;
    private Date mTimeValue;
    private EditText mContent;
    private TextView mSchedule;
    private Date mScheduleValue;
    private int mIdPlan;

    private TextView mCancel;

    private MeetingObject mMeeting;
    private PlanAdapter mAdapter;
    private RealmController mRealmController;

    private SharedPreferences mPref, mPrefNoti;
    private int mIdKey;

    private int mIdNoti;
    private int mIdNotiKey;

    public CreatePlanDialog(Context context, PlanAdapter adapter, int type) {
        mContext = context;
        mAdapter = adapter;
        mType = type;
    }

    public CreatePlanDialog(Context context, PlanAdapter adapter, MeetingObject object, int type) {
        mContext = context;
        mAdapter = adapter;
        mMeeting = object;
        mType = type;
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

        TextView mSave = (TextView) view.findViewById(R.id.dialog_btn_save);
        mSave.setOnClickListener(this);

        mCancel = (TextView) view.findViewById(R.id.dialog_btn_cancel);
        mCancel.setOnClickListener(this);

        mTime = (TextView) view.findViewById(R.id.dialog_txt_time);
        mTime.setOnClickListener(this);

        mContent = (EditText) view.findViewById(R.id.dialog_edit_conttent);
        mContent.setOnClickListener(this);

        mSchedule = (TextView) view.findViewById(R.id.dialog_txt_schedule);
        mSchedule.setOnClickListener(this);

        mIdCustomer = getArguments().getInt(Constant.KEY_ID);

        if (mType == Constant.DIALOG_CREATE) {
            Log.d(TAG, " create");
            mContent.setText("");

            mPref = getActivity().getSharedPreferences(Constant.PREF_PLAN, Context.MODE_PRIVATE);
            mIdKey = mPref.getInt(Constant.KEY_ID, Constant.PREF_ID_DEFAULT);

            Calendar newDate = Calendar.getInstance();
            mTime.setText(Utility.dateToString(newDate.getTime()));
            mTimeValue = newDate.getTime();
        }

        if (mType == Constant.DIALOG_VIEW) {
            Log.d(TAG, " view");
            mTitle.setText(R.string.dialog_title_view);
            mCancel.setText(R.string.edit);
            mSave.setText(R.string.ok);

            mTime.setClickable(false);
            mContent.setEnabled(false);
            mSchedule.setClickable(false);

            getData();
        }


        mPrefNoti = getActivity().getSharedPreferences(Constant.PREF_USER, Context.MODE_PRIVATE);
        mIdNotiKey = mPrefNoti.getInt(Constant.USER_NOTIFICATION, Constant.PREF_ID_DEFAULT);
        Log.d("dainq ", "getIdNoti from pref " + mIdNotiKey);
    }

    private void getData() {
        String time = Utility.dateToString(mMeeting.getMeeting());
        mTime.setText(time);
        mContent.setText(mMeeting.getContent());
        String schedule = Utility.dateToString(mMeeting.getSchedule());
        mSchedule.setText(schedule);
    }

    @Override
    public void onResume() {
        super.onResume();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        Window window = getDialog().getWindow();
        window.setLayout((int) (width * 0.9f), (int) (height * 0.85f));
        window.setGravity(Gravity.CENTER);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_btn_save:
                if (mType == Constant.DIALOG_VIEW) {
                    dismiss();
                } else {
                    int val = 0;
                    try {
                        val = validate();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (val == Constant.VALIDATE_SUCCESS) {
                        btnRightClick();
                    }

                    makeToast(val);
                }
                break;

            case R.id.dialog_btn_cancel:
                btnLeftClick();
                break;

            case R.id.dialog_txt_time:
                initDialogDatePicker(0);
                break;

            case R.id.dialog_txt_schedule:
                initDialogDatePicker(1);
                break;

            default:
                break;
        }
    }

    private void btnRightClick() {
        if (mType == Constant.DIALOG_CREATE) {
            create();
            //set Remind plan with notification
            setNotificationn();
        } else if (mType == Constant.DIALOG_EDIT) {
            edit();

        }
        mAdapter.notifyDataSetChanged();
        dismiss();
    }

    private void btnLeftClick() {
        if (mType == Constant.DIALOG_VIEW) {
            mType = Constant.DIALOG_EDIT;
            mTime.setClickable(true);
            mSchedule.setClickable(true);
            mContent.setEnabled(true);
            mContent.setSelection(mContent.length());
            mCancel.setText(R.string.cancel);
            mTitle.setText(R.string.edit_info);
        } else {
            dismiss();
        }
    }

    private void makeToast(int val) {
        if (val == Constant.VALIDATE_SUCCESS) {
            Toast.makeText(getActivity(), R.string.save_success, Toast.LENGTH_SHORT).show();
            return;
        }
        if (val == Constant.VALIDATE_EMPTY) {
            Toast.makeText(getActivity(), R.string.empty_info, Toast.LENGTH_SHORT).show();
            return;
        }
        if (val == Constant.VALIDATE_SCHEDULE_TIME) {
            Toast.makeText(getActivity(), R.string.validate_schedule_time, Toast.LENGTH_SHORT).show();
            return;
        }
        if (val == Constant.DIALOG_CREATE) {
            Toast.makeText(getActivity(), R.string.create_success, Toast.LENGTH_SHORT).show();
            return;
        }
        if (val == Constant.DIALOG_EDIT) {
            Toast.makeText(getActivity(), R.string.change_success, Toast.LENGTH_SHORT).show();
        }
    }

    public void initDialogDatePicker(final int type) {
        DatePickerDialog dialog;
        Calendar calendar = Calendar.getInstance();
        dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                if (type == 0) {
                    mTimeValue = newDate.getTime();
                    mTime.setText(Utility.dateToString(mTimeValue));
                } else {
                    mScheduleValue = newDate.getTime();
                    mSchedule.setText(Utility.dateToString(mScheduleValue));
                }
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        dialog.getDatePicker().setLayoutMode(0);
        dialog.show();
    }

    private void create() {
        MeetingObject meeting = new MeetingObject();
        mIdPlan = Utility.createId(mIdKey);
        Log.d(TAG + "dialog", " put plan id: " + mIdPlan);
        mPref.edit().putInt(Constant.KEY_ID, mIdPlan).apply();

        meeting.setId(mIdPlan);
        meeting.setIdcustomer(mIdCustomer);
        setData(meeting);

        mRealmController.addPlan(meeting);
    }

    private void setData(MeetingObject object) {
        object.setMeeting(mTimeValue);
        object.setContent(mContent.getText().toString());
        object.setSchedule(mScheduleValue);
    }

    private void edit() {
        mRealmController.getRealm().beginTransaction();
        setData(mMeeting);
        mRealmController.getRealm().commitTransaction();
    }

    private int validate() throws ParseException {
        String time = mTime.getText().toString();
        String content = mContent.getText().toString();
        String schedule = mSchedule.getText().toString();
        if (mType == Constant.DIALOG_EDIT) {
            mTimeValue = Utility.stringToDate(mTime.getText().toString());
            mScheduleValue = Utility.stringToDate(mSchedule.getText().toString());
        }

        if (Utility.isEmptyString(time)
                || Utility.isEmptyString(content)
                || Utility.isEmptyString(schedule)) {
            return Constant.VALIDATE_EMPTY;
        }
        if (mTimeValue.after(mScheduleValue)) {
            return Constant.VALIDATE_SCHEDULE_TIME;
        }

        return Constant.VALIDATE_SUCCESS;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        mContent.setText("");
    }

    private void setNotificationn() {
        CustomerObject customer = mRealmController.getCustomer(mIdCustomer);
        String name = customer.getName();
        String avatar = customer.getAvatar();

        //create notification to realm
        createNotification(name, avatar);

        //push notification use broadcast receiver
        Intent intent = new Intent(mContext, NotificationReceiver.class);
        Bundle bundle = new Bundle();

        bundle.putInt(Constant.NOTIFICATION_TYPE, Constant.NOTIFICATION_EVENT);
        bundle.putString(Constant.NOTIFICATION_NAME_CUSTOMER, name);
        bundle.putInt(Constant.NOTIFICATION_ID, mIdNoti);

        intent.putExtras(bundle);

        NotificationHelper.setRemindRTC(mContext, mScheduleValue, intent);
        NotificationHelper.enableBootReceiver(mContext);
    }

    private void createNotification(String name, String avatar) {
        NotificationObject notification = new NotificationObject();
        mIdNoti = Utility.createId(mIdNotiKey);
        Log.d(TAG + "dainq", " put notification id: " + mIdNoti);
        mPrefNoti.edit().putInt(Constant.USER_NOTIFICATION, mIdNoti).apply();

        notification.setId(mIdNoti);
        notification.setIdcustomer(mIdCustomer);
        notification.setIsread(false);
        notification.setIdmeeting(mIdPlan);
        notification.setType(Constant.NOTIFICATION_EVENT);
        notification.setContent("Kế hoạch với " + name);
        notification.setDate(mScheduleValue);
        notification.setAvatar(avatar);

        mRealmController.addNotification(notification);
    }
}
