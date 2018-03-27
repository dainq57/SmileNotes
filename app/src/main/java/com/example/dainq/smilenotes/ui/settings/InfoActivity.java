package com.example.dainq.smilenotes.ui.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dainq.smilenotes.common.BaseURL;
import com.example.dainq.smilenotes.common.Utility;
import com.example.dainq.smilenotes.common.SessionManager;
import com.example.dainq.smilenotes.controllers.api.APIUser;
import com.example.dainq.smilenotes.model.request.user.UserRequest;
import com.example.dainq.smilenotes.model.response.user.UserResponse;
import com.soundcloud.android.crop.Crop;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import nq.dai.smilenotes.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class InfoActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "InfoActivity";

    private LinearLayout rootLayout;
    private CircleImageView mAvatar;
    private EditText mEditName;
    private EditText mEditEmail;
    private TextView mSave;
    private TextView mCancel;

    private SessionManager mSession;
    private APIUser mService;

    private ProgressBar mProgressView;

    //    private SharedPreferences mPref;
    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);

        initView();
    }

    private void initView() {
        initToolBar();
        initRetrofit();

        mSession = new SessionManager(this);

        rootLayout = (LinearLayout) findViewById(R.id.root);

        mAvatar = (CircleImageView) findViewById(R.id.setting_update_avatar);
        mAvatar.setOnClickListener(this);
        //get avatar from session
        String avatar = mSession.getUserDetails().getPathAvatar();
        if (avatar != null) {
            mAvatar.setImageBitmap(Utility.decodeImage(avatar));
        }

        mEditName = (EditText) findViewById(R.id.setting_update_edit_name);
        mEditEmail = (EditText) findViewById(R.id.setting_update_edit_email);
        mEditEmail.setClickable(false);

        mEditName.setText(mSession.getUserDetails().getFullName());
        mEditEmail.setText(mSession.getUserDetails().getEmail());

        mSave = (TextView) findViewById(R.id.setting_update_btn_save);
        mSave.setOnClickListener(this);
        mCancel = (TextView) findViewById(R.id.setting_update_btn_cancel);
        mCancel.setOnClickListener(this);

        mProgressView = (ProgressBar) findViewById(R.id.setting_progress_bar);
    }

    private void initToolBar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.tw_ic_ab_back_mtrl);
        mToolbar.getNavigationIcon().setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
        getSupportActionBar().setTitle(R.string.info_user);
    }

    private void initRetrofit() {
        Retrofit retrofit = Utility.initRetrofit(BaseURL.URL_USER);

        mService = retrofit.create(APIUser.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_update_avatar:
                if (!Utility.checkPermissionForReadExtertalStorage(this)) {
                    Utility.requestPermission(this);
                } else {
                    Crop.pickImage(this);
                }
                break;

            case R.id.setting_update_btn_save:
                String name = mEditName.getText().toString();

                if (!TextUtils.isEmpty(name)) {
                    //change passAvatar
                    String mAvatarPath = null;
                    if (mUri != null) {
                        mAvatarPath = Utility.convertImage(this, mUri);
                        Log.d(TAG, "-->[update-info] patchAvatar user: " + mAvatarPath);
                    }

                    //call update Infomation
                    processUpdateInfo(name, mAvatarPath);

                    //show progressbar
                    mProgressView.setVisibility(View.VISIBLE);
                } else {
                    Snackbar.make(rootLayout, "Không được để trống họ tên! ", Snackbar.LENGTH_SHORT).show();
                }
                break;

            case R.id.setting_update_btn_cancel:
                onBackPressed();
                break;

            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        String title = getResources().getString(R.string.title_dialog_back_edit);
        String content = getString(R.string.content_dialog_cancel_update_info);
        confirmDialog(title, content);
    }

    private void confirmDialog(String title, String content) {
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(content)
                .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        InfoActivity.this.finish();
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

    private void processUpdateInfo(final String name, final String pathAvatar) {
        //get id and email paramester from userdetail of session
        String id = mSession.getUserDetails().getId();
        String email = mSession.getUserDetails().getEmail();
        final String password = mSession.getUserDetails().getPassword();
        final int version = mSession.getUserDetails().getVersion();

        Log.d(TAG, "--->[update-info] update name - " + name);

        //create object UserRequest request
        UserRequest request = new UserRequest();
        request.setId(id);
        request.setVersion(version);
        request.setFullName(name);
        request.setEmail(email);
        request.setPassword(password);
        request.setPathAvatar(pathAvatar);

        //get token from session in pref
        String token = mSession.getUserDetails().getToken();
        Log.d(TAG, "--->[update-info] token used - " + token);

        //create response
        Call<UserResponse> response = mService.updateInfo(request, token);

        response.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                UserResponse userResponse = response.body();
                mProgressView.setVisibility(View.INVISIBLE);
                int code = userResponse.getCode();
                if (code == 1) {
                    Utility.makeSnackbar(rootLayout, "Cập nhật thành công!");
                    //create value new version
                    int ver = version + 1;
                    Log.d(TAG, "--->[update-info] update version: " + ver);
                    //update name avatar and version into data in Pref
                    mSession.updateSession(SessionManager.KEY_NAME, name, ver);
                    mSession.updateSession(SessionManager.KEY_AVATAR, pathAvatar, ver);

                    finish();
                } else {
                    Log.d(TAG, "--->[update-info] response: " + code + " - " + userResponse.getMessage());
                    Utility.makeSnackbar(rootLayout, "Lỗi!");
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                //connect failure
                mProgressView.setVisibility(View.INVISIBLE);
                Log.d("onFailure ", t.getMessage() + "\n" + t.getCause());
                Snackbar snackbar = Snackbar.make(rootLayout, "Kết nối lỗi!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("THỬ LẠI", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                processUpdateInfo(name, pathAvatar);
                            }
                        });

                snackbar.setActionTextColor(getResources().getColor(R.color.colorAccent));
                snackbar.show();
            }
        });
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
        Log.d(TAG, "crop in home " + requestCode);
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(this.getCacheDir(), "cropped"));
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}