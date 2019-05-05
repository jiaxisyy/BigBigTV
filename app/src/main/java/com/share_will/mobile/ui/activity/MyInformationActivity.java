package com.share_will.mobile.ui.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.share_will.mobile.App;
import com.share_will.mobile.Constant;
import com.share_will.mobile.R;
import com.share_will.mobile.model.entity.UserInfo;

import com.share_will.mobile.ui.widget.CircleImageView;
import com.ubock.library.base.BaseFragmentActivity;
import com.ubock.library.utils.SharedPreferencesUtils;

import org.w3c.dom.Text;

import java.util.List;

public class MyInformationActivity extends BaseFragmentActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private static final int RESULTCODE_HEADPATH = 10020;
    private CircleImageView mHead;
    private TextView mTvPhone;
    private TextView mTvPhone2;
    private TextView mTvName;
    private TextView mTvGender;
    private PopupWindow mPopupWindow;
    private TextView mCancel;
    private RadioButton mMan;
    private RadioButton mWoman;
    private RadioGroup mRgGender;
    private LinearLayout mLlGender;

    private String mGender;
    private UserInfo mUserInfo;
    private RadioButton mRbMan;
    private RadioButton mRbWoman;

    private static final String GENDER_MAN = "男";
    private static final String GENDER_WOMAN = "女";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_myinformation;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("个人资料");
        mHead = findViewById(R.id.civ_information_head);
        mTvPhone = findViewById(R.id.tv_information_phone);
        mTvPhone2 = findViewById(R.id.tv_information_phone2);
        mTvName = findViewById(R.id.tv_information_name);
        mTvGender = findViewById(R.id.tv_information_gender);
        mLlGender = findViewById(R.id.ll_information_gender);
        mHead.setOnClickListener(this);
        mLlGender.setOnClickListener(this);
        mTvPhone.setText(App.getInstance().getUserId());
        mTvPhone2.setText(App.getInstance().getUserId());
        mUserInfo = SharedPreferencesUtils.getDeviceData(this, App.getInstance().getUserId());
        if (!TextUtils.isEmpty(mUserInfo.getUserName())) {
            mTvName.setText(mUserInfo.getUserName());
        }
        mGender = mUserInfo.getGender();
        if (!TextUtils.isEmpty(mGender)) {
            mTvGender.setText(mGender);
        }
        if (!TextUtils.isEmpty(mUserInfo.getHeadPicPath())) {
            mHead.setImageURI(Uri.parse(mUserInfo.getHeadPicPath()));
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.civ_information_head:
                selectPic();
                break;
            case R.id.ll_information_gender:
                showGenderDialog();
                break;
        }

    }

    private void showGenderDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_selector_gender, null);
        mPopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mPopupWindow.setFocusable(false);
        //下面的是设置外部是否可以点击
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mCancel = view.findViewById(R.id.tv_dialog_gender_cancel);
        mRgGender = view.findViewById(R.id.rg_dialog_gender);
        mRbMan = view.findViewById(R.id.rb_dialog_gender_man);
        mRbWoman = view.findViewById(R.id.rb_dialog_gender_woman);

        if (mTvGender.getText().toString().equals(GENDER_MAN)) {
            mRbMan.setChecked(true);
        } else {
            mRbWoman.setChecked(true);
        }

        mRgGender.setOnCheckedChangeListener(this);

        mCancel.setOnClickListener(v -> {
            if (mPopupWindow != null) {
                mPopupWindow.dismiss();
            }
        });
        if (!mPopupWindow.isShowing()) {
            mPopupWindow.showAsDropDown(view, 0, 0, Gravity.FILL);
        }
    }

    private void selectPic() {
        PictureSelector.create(MyInformationActivity.this)
                .openGallery(PictureMimeType.ofImage())
                .maxSelectNum(1)
                .isCamera(false)
                .enableCrop(true)
                .compress(true)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片、视频、音频选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    if (selectList.get(0).isCompressed()) {
                        String path = selectList.get(0).getCompressPath();
                        mHead.setImageURI(Uri.parse(path));
                        mUserInfo.setHeadPicPath(path);
                        Intent intent = new Intent();
                        intent.putExtra("head_pic_path", path);
                        setResult(RESULTCODE_HEADPATH, intent);

                    }
                    break;
            }
        }

    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (i == R.id.rb_dialog_gender_man) {
            mRbMan.setChecked(true);
            mTvGender.setText(GENDER_MAN);
            mUserInfo.setGender(GENDER_MAN);
            mPopupWindow.dismiss();
        } else if (i == R.id.rb_dialog_gender_woman) {
            mRbWoman.setChecked(true);
            mTvGender.setText(GENDER_WOMAN);
            mUserInfo.setGender(GENDER_WOMAN);
            mPopupWindow.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferencesUtils.saveDeviceData(this, App.getInstance().getUserId(), mUserInfo);
    }
}
