package com.example.dainq.smilenotes.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import nq.dai.smilenotes.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private final String email = "hadesnq@gmail.com";
    private final String pass = "111111";

    private TextView mBtnSignIn;
    private TextView mBtnSignUp;
    private TextView mBtnForgetPass;
    private TextView mBtnRegister;

    private EditText mEmailSignIn;
    private EditText mPassSignIn;

    private EditText mNameSignUp;
    private EditText mEmailSignUp;
    private EditText mPassSignUp;
    private EditText mRePassSignUp;

    private RelativeLayout mLayoutSignIn;
    private RelativeLayout mLayoutSignUp;

    private CheckBox mCheckboxRemember;
    private TextView txtQuestion;

    //--------

    private boolean isLogin = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView() {
        mBtnSignIn = (TextView) findViewById(R.id.id_login_btn);
        mBtnSignIn.setOnClickListener(this);
        mBtnSignUp = (TextView) findViewById(R.id.id_signup_btn);
        mBtnSignUp.setOnClickListener(this);
        mBtnForgetPass = (TextView) findViewById(R.id.id_login_forgot_pass);
        mBtnForgetPass.setOnClickListener(this);
        mBtnRegister = (TextView) findViewById(R.id.id_login_register);
        mBtnRegister.setOnClickListener(this);

        txtQuestion = (TextView) findViewById(R.id.id_login_question);

        mLayoutSignIn = (RelativeLayout) findViewById(R.id.layout_sign_in);
        mLayoutSignUp = (RelativeLayout) findViewById(R.id.layout_sign_up);

        mCheckboxRemember = (CheckBox) findViewById(R.id.id_login_checkbox_rem);

        mEmailSignIn = (EditText) findViewById(R.id.id_login_edit_email);
        mPassSignIn = (EditText) findViewById(R.id.id_login_edit_pass);

        mNameSignUp = (EditText) findViewById(R.id.id_signup_edit_name);
        mEmailSignUp = (EditText) findViewById(R.id.id_signup_edit_email);
        mPassSignUp = (EditText) findViewById(R.id.id_signup_edit_pass);
        mRePassSignUp = (EditText) findViewById(R.id.id_signup_edit_re_pass);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_login_register:
                switchSignInUp();
                break;

            case R.id.id_login_btn:
                attempLogin();
                break;

            case R.id.id_signup_btn:
                attempSignUp();
                break;
            default:
                break;
        }
    }

    //choose action signIn or signUp
    private void switchSignInUp() {
        if (isLogin) {
            mLayoutSignIn.setVisibility(View.GONE);
            mLayoutSignUp.setVisibility(View.VISIBLE);

            mBtnRegister.setText(R.string.signup_login);
            txtQuestion.setText(R.string.already_have_accout);
            isLogin = false;
        } else {
            mLayoutSignIn.setVisibility(View.VISIBLE);
            mLayoutSignUp.setVisibility(View.GONE);

            mBtnRegister.setText(R.string.login_register);
            txtQuestion.setText(R.string.do_not_have_accout);
            isLogin = true;
        }
    }

    //when click login, valid email and password before login
    private void attempLogin() {
        // Reset errors.
//        mEmailSignIn.setError(null);
//        mPassSignIn.setError(null);
//
//        // Store values at the time of the login attempt.
//        String email = mEmailSignIn.getText().toString();
//        String password = mPassSignIn.getText().toString();
//
//        boolean cancel = false;
//
//        // Check for a valid password, if the user entered one.
//        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
//            mPassSignIn.setError(getString(R.string.error_invalid_password_login));
//            cancel = true;
//        }
//
//        // Check for a valid email address.
//        if (TextUtils.isEmpty(email)) {
//            mEmailSignIn.setError(getString(R.string.error_field_required));
//            cancel = true;
//        } else if (!isEmailValid(email)) {
//            mEmailSignIn.setError(getString(R.string.error_invalid_email));
//            cancel = true;
//        }
//
//        if (cancel) {
//            // There was an error; don't attempt login and focus the first
//            // form field with an error.
//        } else {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
//        }
    }

    private void attempSignUp() {
        mNameSignUp.setError(null);
        mEmailSignUp.setError(null);
        mPassSignUp.setError(null);
        mRePassSignUp.setError(null);

        // Store values at the time of the login attempt.
        String name = mNameSignUp.getText().toString();
        String email = mEmailSignUp.getText().toString();
        String password = mPassSignUp.getText().toString();
        String repassword = mRePassSignUp.getText().toString();

        boolean cancel = false;

        //Check for name
        if (TextUtils.isEmpty(name)) {
            mNameSignUp.setError(getString(R.string.error_field_required));
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPassSignUp.setError(getString(R.string.error_invalid_password));
            cancel = true;
        }

        if(!TextUtils.isEmpty(repassword) && !isRePasswordValid(password, repassword)){
            mRePassSignUp.setError(getString(R.string.error_incorrect_password));
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailSignUp.setError(getString(R.string.error_field_required));
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailSignUp.setError(getString(R.string.error_invalid_email));
            cancel = true;
        }
    }

    private boolean isEmailValid(String email) {
        return (email.contains("@") && (email.contains(".")));
    }

    private boolean isPasswordValid(String password) {
        // replace with correct password in server
        return password.length() > 5;
    }

    private boolean isRePasswordValid(String pass, String repass) {
        return repass.endsWith(pass);
    }
}

