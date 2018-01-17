package com.example.dainq.smilenotes.ui.settings;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.dainq.smilenotes.common.BaseURL;
import com.example.dainq.smilenotes.common.Utility;
import com.example.dainq.smilenotes.controllers.api.APIUser;
import com.example.dainq.smilenotes.controllers.realm.RealmController;
import com.example.dainq.smilenotes.common.SessionManager;
import com.example.dainq.smilenotes.model.CustomerObject;
import com.example.dainq.smilenotes.model.MeetingObject;
import com.example.dainq.smilenotes.model.ProductObject;
import com.example.dainq.smilenotes.model.request.UserChangePassRequest;
import com.example.dainq.smilenotes.model.response.UserChangePassResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmResults;
import nq.dai.smilenotes.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SettingAdapter extends RecyclerView.Adapter<SettingViewHolder> {

    private static final String TAG = "SettingAdapter";

    private static final int CHANGE_INFO = 0;
    private static final int CHANGE_PASS = 1;
    private static final int BACK_UP = 2;
    private static final int ABOUT_US = 3;
    private static final int LOG_OUT = 4;

    private LinearLayout rootLayout;
    private Context mContext;
    private SettingItem[] mItem;
    private Activity mActivity;

    private SessionManager mSession;
    private SharedPreferences mPref;
    private RealmController mReamController;
    private APIUser mService;

    SettingAdapter(Context context, SettingItem[] item, SharedPreferences pref, Activity activity, SessionManager session) {
        mContext = context;
        mItem = item;
        mPref = pref;
        mActivity = activity;
        mSession = session;
    }

    @Override
    public SettingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_setting, parent, false);
        return new SettingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SettingViewHolder holder, final int position) {
        initRetrofit();

        mReamController = new RealmController(mContext);

        holder.mSettingType.setText(mItem[position].mDescription);
        holder.mIconSetting.setImageDrawable(mItem[position].mIcon);

        holder.mLayoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position) {
                    case CHANGE_INFO:
                        updateInfomation();
                        break;
                    case CHANGE_PASS:
                        changePasswordDialog();
                        break;
                    case BACK_UP:
                        requestPermission();
                        break;
                    case ABOUT_US:
                        aboutUs();
                        break;
                    case LOG_OUT:
                        dialogLogout();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    //create retrofit and api
    private void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseURL.URL_USER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mService = retrofit.create(APIUser.class);
    }

    private void requestPermission() {
        if (!Utility.checkPermissionForReadExtertalStorage(mActivity)) {
            Utility.requestPermission(mActivity);
        } else {
            backupData();
        }
    }

    private void backupData() {
        RealmResults<CustomerObject> resultsCustomer = mReamController.getCustomers();
        RealmResults<MeetingObject> resultsMeeting = mReamController.getMeetings();
        RealmResults<ProductObject> resultsProduct = mReamController.getProducts();

        Log.d("dainq ", "backup customer");
        JSONArray jsonArray1 = Utility.makeJsonArrayCustomer(resultsCustomer);
        JSONArray jsonArray2 = Utility.makeJsonArrayMeeting(resultsMeeting);
        JSONArray jsonArray3 = Utility.makeJsonArrayProduct(resultsProduct);
        try {
            JSONObject jsonObject = Utility.makJsonObject(jsonArray1, jsonArray2, jsonArray3);
            Log.d("backup", jsonObject.toString());
            String data = jsonObject.toString();

            Utility.writeFile(data);
            Toast.makeText(mContext, Utility.fileName + " saved!", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            //TODO exception
            e.printStackTrace();
        }
    }

    private void aboutUs() {
        final View view = View.inflate(mContext, R.layout.dialog_about_app, null);
        final AlertDialog.Builder builder;

        builder = new AlertDialog.Builder(mContext);
        builder.setPositiveButton("OK", null);
        builder.setView(view);
        builder.setCancelable(false);

        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
        alertDialog.show();
    }

    private void dialogLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Bạn có chắc chắn muốn thoát?")
                .setIcon(mContext.getDrawable(R.drawable.icn_power_off_128))
                .setTitle("THOÁT ỨNG DỤNG")
                .setCancelable(false)
                .setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(mContext.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mSession.logoutUser();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void updateInfomation() {
        Intent intent = new Intent(mContext, InfoActivity.class);
        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return mItem.length;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //create dialog to change password
    private void changePasswordDialog() {
        final View view = mActivity.getLayoutInflater().inflate(R.layout.dialog_change_pass, null);
        rootLayout = (LinearLayout) view.findViewById(R.id.root_change_pass);
        final AlertDialog.Builder builder;

        final EditText editOldPass = (EditText) view.findViewById(R.id.edit_old_pass);
        final EditText editNewPass = (EditText) view.findViewById(R.id.edit_new_pass);
        final EditText editNewPassAgain = (EditText) view.findViewById(R.id.edit_new_pass_again);

        builder = new AlertDialog.Builder(mContext);
        builder.setIcon(mContext.getDrawable(R.drawable.icn_key_128));
        builder.setPositiveButton("Đồng ý", null);
        builder.setNegativeButton("Huỷ", null);
        builder.setView(view);
        builder.setCancelable(false);
        builder.setTitle("ĐỔI MẬT KHẨU");

        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String oldPass = editOldPass.getText().toString();
                        String newPass = editNewPass.getText().toString();
                        String newPassAgain = editNewPassAgain.getText().toString();

                        boolean val = validChangePass(oldPass, newPass, newPassAgain);
                        if (val) {
                            processChangePass(oldPass, newPass, dialog);
                        }
                    }
                });

                Button cancel = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
        alertDialog.show();
    }

    private boolean validChangePass(String oldPass, String newPass, String newPassAgain) {
        String realPass = mSession.getUserDetails().getPassword();
        if (TextUtils.isEmpty(oldPass) || TextUtils.isEmpty(newPass) || TextUtils.isEmpty(newPassAgain)) {
            Snackbar.make(rootLayout, "Các thông tin còn trống!", Snackbar.LENGTH_SHORT).show();
            return false;

        } else if (!realPass.equals(oldPass)) {
            Snackbar.make(rootLayout, "Mật khẩu cũ chưa chính xác!", Snackbar.LENGTH_SHORT).show();
            return false;

        } else if (!Utility.isPasswordValid(newPass)) {
            Snackbar.make(rootLayout, "Mật khẩu mới quá ngắn!", Snackbar.LENGTH_SHORT).show();
            return false;

        } else if (!newPass.equals(newPassAgain)) {
            Snackbar.make(rootLayout, "Mật khẩu mới chưa khớp!", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void processChangePass(final String oldPass, final String newPass, final DialogInterface dialog) {
        //get info from user session
        String id = mSession.getUserDetails().getId();
        String token = mSession.getUserDetails().getToken();

        Log.d(TAG, "--->[change-pass] pass: " + newPass);

        //create onject request change pass word
        final UserChangePassRequest request = new UserChangePassRequest();
        request.setId(id);
        request.setOldPassword(oldPass);
        request.setNewPassword(newPass);

        //get response from server with format UserChangePassResponse
        Call<UserChangePassResponse> response = mService.changePassword(request, token);
        response.enqueue(new Callback<UserChangePassResponse>() {
            @Override
            public void onResponse(Call<UserChangePassResponse> call, Response<UserChangePassResponse> response) {
                int status = response.code();
                UserChangePassResponse serverResponse = response.body();
                int code = serverResponse.getCode();

                if (code == 1) {
                    Toast.makeText(mContext, "Thành công!", Toast.LENGTH_SHORT).show();
                    //version change, get old version
                    int version = mSession.getUserDetails().getVersion();
                    version += 1;
                    Log.d(TAG, "--->[change-pass] update version: " + version);
                    //update session with new password and increase version data in Pref
                    mSession.updateSession(SessionManager.KEY_PASSWORD, newPass, version);
                    dialog.dismiss();
                } else {
                    Snackbar.make(rootLayout, "Lỗi!", Snackbar.LENGTH_SHORT).show();
                    Log.d(TAG, "--->[change-pass] error: " + status + "/" + code + "/" + serverResponse.getMessage());
                }
            }

            @Override
            public void onFailure(Call<UserChangePassResponse> call, Throwable t) {
                Log.d("onFailure ", t.getMessage() + "\n" + t.getCause());
                Snackbar snackbar = Snackbar.make(rootLayout, "Kết nối lỗi!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("THỬ LẠI", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                processChangePass(oldPass, newPass, dialog);
                            }
                        });

                snackbar.setActionTextColor(mContext.getResources().getColor(R.color.colorAccent));
                snackbar.show();
            }
        });
    }
}