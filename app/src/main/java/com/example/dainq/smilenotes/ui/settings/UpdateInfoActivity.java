package com.example.dainq.smilenotes.ui.settings;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dainq.smilenotes.common.BaseActivity;
import com.example.dainq.smilenotes.common.Constant;
import com.example.dainq.smilenotes.common.Utility;
import com.soundcloud.android.crop.Crop;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import nq.dai.smilenotes.R;

public class UpdateInfoActivity extends BaseActivity implements View.OnClickListener {
    private CircleImageView mAvatar;
    private EditText mEditName;
    private TextView mSave;
    private TextView mCancel;

    private SharedPreferences mPref;
    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);

        initView();
    }

    private void initView() {
        initToolBar();

        mAvatar = (CircleImageView) findViewById(R.id.setting_update_avatar);
        mAvatar.setOnClickListener(this);

        mEditName = (EditText) findViewById(R.id.setting_update_edit_name);
        mSave = (TextView) findViewById(R.id.setting_update_btn_save);
        mSave.setOnClickListener(this);
        mCancel = (TextView) findViewById(R.id.setting_update_btn_cancel);
        mCancel.setOnClickListener(this);

        mPref = getSharedPreferences(Constant.PREF_USER, Context.MODE_PRIVATE);
    }

    private void initToolBar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.tw_ic_ab_back_mtrl);
        mToolbar.getNavigationIcon().setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
        getSupportActionBar().setTitle(R.string.info_user);
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
                if (!Utility.isEmptyString(name)) {
                    save(name);
                    UpdateInfoActivity.this.finish();
                    Toast.makeText(this, "Đã lưu!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Hãy điền tên của bạn!", Toast.LENGTH_SHORT).show();
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
        String name = mPref.getString(Constant.PREF_USER_NAME, "");
        mEditName.setText(name);
    }

    @Override
    public void onBackPressed() {
        String title = getResources().getString(R.string.title_dialog_back_edit);
        String content = getString(R.string.content_dialog_cancel_update_info);
        confirmDialog(title, content);
    }

    private void confirmDialog(String title, String content) {
        final AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle(title)
                .setMessage(content)
                .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        UpdateInfoActivity.this.finish();
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

    private void save(String name) {
        if (mUri != null) {
            String mAvatarEncoder = Utility.convertImage(this, mUri);
            mPref.edit().putString(Constant.PREF_USER_AVATAR, mAvatarEncoder).apply();
            Log.d("dainq", " pref avatar user: " + mAvatarEncoder);
        }
        mPref.edit().putString(Constant.PREF_USER_NAME, name).apply();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v("dainq", "Permission: " + permissions[0] + "was " + grantResults[0]);
            //resume tasks needing this permission
            Crop.pickImage(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        Log.d("dainq ", "crop in home " + requestCode);
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
