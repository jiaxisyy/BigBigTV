package com.share_will.mobile.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.share_will.mobile.R;
import com.ubock.library.base.BaseFragmentActivity;

public class AutonymActivity extends BaseFragmentActivity implements View.OnClickListener {

    private ImageView mIvFront;
    private ImageView mIvContrary;
    private EditText mEtCode;
    private EditText mEtName;
    private TextView mTvSubmit;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_autonym;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("实名认证");

        mIvFront = findViewById(R.id.iv_papers_front);
        mIvContrary = findViewById(R.id.iv_papers_contrary);
        mEtCode = findViewById(R.id.et_papers_code);
        mEtName = findViewById(R.id.et_papers_name);
        mTvSubmit = findViewById(R.id.tv_papers_submit);
        mIvFront.setOnClickListener(this);
        mIvContrary.setOnClickListener(this);
        mTvSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_papers_front:

                break;
            case R.id.iv_papers_contrary:

                break;
            case R.id.tv_papers_submit:

                break;
        }
    }
}
