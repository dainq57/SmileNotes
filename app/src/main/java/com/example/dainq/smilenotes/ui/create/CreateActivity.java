package com.example.dainq.smilenotes.ui.create;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.dainq.smilenotes.R;
import com.example.dainq.smilenotes.common.BaseActivity;
import com.example.dainq.smilenotes.common.Constant;

public class CreateActivity extends BaseActivity implements View.OnClickListener {
    private CustomViewPager mPager;
    private CreateAdapter mAdapter;

    private TextView mButtonPrevious;
    private TextView mButtonNext;
    private TextView mTextViewSteps;

    private int mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        initView();
    }

    private void initView() {
        initViewPager();
        //TODO
    }

    private void initViewPager() {
        mPosition = Constant.STEP_CREATE_FIRST;

        mAdapter = new CreateAdapter(this);
        mPager = (CustomViewPager) findViewById(R.id.viewpager);
        mPager.setAdapter(mAdapter);
        mPager.setPagingEnabled(false);
        mPager.setCurrentItem(mPosition);

        mButtonNext = (TextView) findViewById(R.id.create_btn_next);
        mButtonNext.setOnClickListener(this);

        mButtonPrevious = (TextView) findViewById(R.id.create_btn_previous);
        mButtonPrevious.setOnClickListener(this);

        mTextViewSteps = (TextView) findViewById(R.id.create_txt_step);

        showHideButton();
        setStep();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.create_btn_next:
                nextPage();
                break;

            case R.id.create_btn_previous:
                previousPage();
                break;

            default:
                break;
        }
    }

    private void showHideButton() {
        if (mPosition == Constant.STEP_CREATE_FIRST) {
            mButtonPrevious.setVisibility(View.GONE);
        } else {
            mButtonPrevious.setVisibility(View.VISIBLE);
        }

        if (mPosition == Constant.STEP_CREATE_THIRD) {
            mButtonNext.setText("save");
        } else {
            mButtonNext.setText("next");
        }
    }

    private void setStep() {
        mTextViewSteps.setText("step " + (mPosition + 1) + "/" + (Constant.STEP_CREATE_THIRD + 1));
    }

    private void nextPage() {
        if (mPosition < Constant.STEP_CREATE_THIRD) {
            mPosition++;
            mPager.setCurrentItem(mPosition);
            showHideButton();
            setStep();
        }
    }

    private void previousPage() {
        if (mPosition > Constant.STEP_CREATE_FIRST) {
            mPosition--;
            mPager.setCurrentItem(mPosition);
            showHideButton();
            setStep();
        }
    }
}
