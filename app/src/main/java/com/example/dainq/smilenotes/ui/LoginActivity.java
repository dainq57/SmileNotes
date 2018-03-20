package com.example.dainq.smilenotes.ui;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dainq.smilenotes.common.BaseURL;
import com.example.dainq.smilenotes.common.Constant;
import com.example.dainq.smilenotes.common.SessionManager;
import com.example.dainq.smilenotes.common.Utility;
import com.example.dainq.smilenotes.controllers.api.APIUser;
import com.example.dainq.smilenotes.model.request.UserRequest;
import com.example.dainq.smilenotes.model.response.UserResponse;

import nq.dai.smilenotes.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";

    private TextView mBtnRegister;

    private EditText mEmailSignIn;
    private EditText mPassSignIn;

    private EditText mNameSignUp;
    private EditText mEmailSignUp;
    private EditText mPassSignUp;
    private EditText mRePassSignUp;

    private RelativeLayout mLayoutSignIn;
    private RelativeLayout mLayoutSignUp;

    private TextView txtQuestion;

    private ProgressBar mProgressView;
    private RelativeLayout relativeLayout;

    //--------

    private boolean isLogin = true;
    private APIUser mService;
    private SessionManager mSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        initRetrofit();
    }

    private void initView() {
        TextView btnSignIn = (TextView) findViewById(R.id.id_login_btn);
        btnSignIn.setOnClickListener(this);

        TextView btnSignUp = (TextView) findViewById(R.id.id_signup_btn);
        btnSignUp.setOnClickListener(this);

        TextView btnForgetPass = (TextView) findViewById(R.id.id_login_forgot_pass);
        btnForgetPass.setOnClickListener(this);

        mBtnRegister = (TextView) findViewById(R.id.id_login_register);
        mBtnRegister.setOnClickListener(this);

        txtQuestion = (TextView) findViewById(R.id.id_login_question);

        mLayoutSignIn = (RelativeLayout) findViewById(R.id.layout_sign_in);
        mLayoutSignUp = (RelativeLayout) findViewById(R.id.layout_sign_up);

        mEmailSignIn = (EditText) findViewById(R.id.id_login_edit_email);
        mPassSignIn = (EditText) findViewById(R.id.id_login_edit_pass);

        mNameSignUp = (EditText) findViewById(R.id.id_signup_edit_name);
        mEmailSignUp = (EditText) findViewById(R.id.id_signup_edit_email);
        mPassSignUp = (EditText) findViewById(R.id.id_signup_edit_pass);
        mRePassSignUp = (EditText) findViewById(R.id.id_signup_edit_re_pass);

        mProgressView = (ProgressBar) findViewById(R.id.login_progressbar);
        relativeLayout = (RelativeLayout) findViewById(R.id.relative_login);

        //create new session manager
        mSession = new SessionManager(LoginActivity.this);
    }

    /**
     * create service with retrofit*
     */
    private void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseURL.URL_USER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mService = retrofit.create(APIUser.class);
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
        //switch to login layout
        if (isLogin) {
            mLayoutSignIn.setVisibility(View.GONE);
            mLayoutSignUp.setVisibility(View.VISIBLE);

            //change text
            mBtnRegister.setText(R.string.signup_login);
            txtQuestion.setText(R.string.already_have_accout);
            isLogin = false;
        }
        //switch to signup layout
        else {
            mLayoutSignIn.setVisibility(View.VISIBLE);
            mLayoutSignUp.setVisibility(View.GONE);

            //change text
            mBtnRegister.setText(R.string.login_register);
            txtQuestion.setText(R.string.do_not_have_accout);
            isLogin = true;
        }
    }

    //when click login, valid email and password before login
    private void attempLogin() {
        // Store values at the time of the login attempt.
        String email = mEmailSignIn.getText().toString();
        String password = mPassSignIn.getText().toString();

        boolean cancel = false;

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Log.d("dainq ", "empty");
            Snackbar.make(relativeLayout, "Thông tin còn trống!", Snackbar.LENGTH_LONG).show();
            cancel = true;
        } else if (!Utility.isEmailValid(email)) {
            Snackbar.make(relativeLayout, "Email không đúng định dạng!", Snackbar.LENGTH_LONG).show();
            cancel = true;
        }

        if (!cancel) {
            //show progress
            mProgressView.setVisibility(View.VISIBLE);
            //login account
            processLogIn(email, password);
        }
    }

    /***
     * Login to smilenote
     * params email, password
     ***/
    private void processLogIn(final String email, final String password) {
        UserRequest request = new UserRequest();
        request.setEmail(email);
        request.setPassword(password);

        Call<UserResponse> response = mService.logIn(request);

        response.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                UserResponse userResponse = response.body();

                int code = userResponse.getCode();
                Log.d(TAG, "--->[login] header: " + response.headers().get("Authorization"));
                //login success
                if (code == Constant.RESPONSE_SUCCESS) {
                    //create intent MainActivity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                    //get data from server to create userRequest session
                    UserRequest userRequest = userResponse.getData();
                    String emailUser = userRequest.getEmail();
                    String nameUser = userRequest.getFullName();
                    String idUser = userRequest.getId();
                    String token = response.headers().get("Authorization");
                    int version = userRequest.getVersion();

                    Log.d(TAG, "--->[login] getversion: " + userRequest.getVersion());
                    //create session
                    mSession.createLoginSession(nameUser, emailUser, idUser, token, password, version);

                    //start MainActivity
                    startActivity(intent);

                    //finish activity login
                    finish();
                } else if (code == Constant.RESPONSE_WRONG_PASSWORD || code == Constant.RESPONSE_USER_NOT_EXIT) {
                    Snackbar.make(relativeLayout, "Tài khoản hoặc mật khẩu không chính xác!", Snackbar.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "--->[login] error: " + code + " - " + userResponse.getMessage());
                    Snackbar.make(relativeLayout, "Unknown error! ", Snackbar.LENGTH_SHORT).show();
                }
                //dimiss progress bar
                mProgressView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                //connect failure
                mProgressView.setVisibility(View.INVISIBLE);
                Log.d(TAG,"onFailure " + t.getMessage() + "\n" + t.getCause());
                Snackbar snackbar = Snackbar.make(relativeLayout, "Kết nối lỗi!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("THỬ LẠI", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                attempLogin();
                            }
                        });

                snackbar.setActionTextColor(getResources().getColor(R.color.colorAccent));
                snackbar.show();
            }
        });
    }

    private void attempSignUp() {
        // Store values at the time of the login attempt.
        String name = mNameSignUp.getText().toString();
        String email = mEmailSignUp.getText().toString();
        String password = mPassSignUp.getText().toString();
        String repassword = mRePassSignUp.getText().toString();

        boolean cancel = false;

        //if some field is empty
        if (TextUtils.isEmpty(name) | TextUtils.isEmpty(email)
                || TextUtils.isEmpty(password) || TextUtils.isEmpty(repassword)) {
            Snackbar.make(relativeLayout, "Thông tin còn trống!", Snackbar.LENGTH_LONG).show();
            cancel = true;
        } else if (!Utility.isEmailValid(email)) {
            Snackbar.make(relativeLayout, "Email không đúng định dạng!", Snackbar.LENGTH_LONG).show();
            cancel = true;
        } else if (!Utility.isPasswordValid(password)) {
            Snackbar.make(relativeLayout, "Mật khẩu quá ngắn!", Snackbar.LENGTH_LONG).show();
            cancel = true;
        } else if (!repassword.equals(password)) {
            Snackbar.make(relativeLayout, "Mật khẩu không khớp!", Snackbar.LENGTH_LONG).show();
            cancel = true;
        }

        //here is start register if cancel == false
        if (!cancel) {
            //show progress
            mProgressView.setVisibility(View.VISIBLE);
            //signup account
            processSignUp(name, email, password);
        }
    }

    private void processSignUp(String name, final String email, final String password) {
        //create request information
        UserRequest request = new UserRequest();
        request.setFullName(name);
        request.setEmail(email);
        request.setPassword(password);

        Call<UserResponse> response = mService.signUp(request);
        response.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                int status = response.code();
                UserResponse userResponse = response.body();
                int code = userResponse.getCode();
                mProgressView.setVisibility(View.INVISIBLE);

                if (code == Constant.RESPONSE_SUCCESS) {
                    Snackbar snackbar = Snackbar.make(relativeLayout, "Thành công!", Snackbar.LENGTH_INDEFINITE)
                            .setAction("ĐĂNG NHẬP", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    processLogIn(email, password);
                                }
                            });

                    snackbar.setActionTextColor(getResources().getColor(R.color.colorAccent));
                    snackbar.show();
                } else if (code == Constant.RESPONSE_USER_EXIT) {
                    Snackbar.make(relativeLayout, "Tài khoản đã tồn tại!", Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(relativeLayout, "Unknown error " + code + "/" + status, Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                //connect failure
                mProgressView.setVisibility(View.INVISIBLE);
                Snackbar snackbar = Snackbar.make(relativeLayout, "Kết nối lỗi!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("THỬ LẠI", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                attempSignUp();
                            }
                        });

                snackbar.setActionTextColor(getResources().getColor(R.color.colorAccent));
                snackbar.show();
            }
        });
    }
}