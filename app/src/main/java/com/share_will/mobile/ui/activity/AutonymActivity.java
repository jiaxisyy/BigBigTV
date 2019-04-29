package com.share_will.mobile.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.share_will.mobile.R;
import com.ubock.library.base.BaseFragmentActivity;
import com.ubock.library.ui.dialog.ToastExt;

import java.util.List;

public class AutonymActivity extends BaseFragmentActivity implements View.OnClickListener {

    /**
     * 正面
     */
    private static final int TYPE_FRONT = 1;
    /**
     * 反面
     */
    private static final int TYPE_CONTRARY = 0;
    private int tempType = -1;

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
                tempType = TYPE_FRONT;
                selectPic();
                break;
            case R.id.iv_papers_contrary:
                tempType = TYPE_CONTRARY;
                selectPic();
                break;
            case R.id.tv_papers_submit:
                ToastExt.showExt("功能暂未开放");
                break;
        }
    }

    private void selectPic() {
        PictureSelector.create(AutonymActivity.this)
                .openGallery(PictureMimeType.ofImage())
                .maxSelectNum(1)
                .enableCrop(true)// 是否裁剪
                .isCamera(false)//TODO 点击拍照错误,待处理
                .compress(true)// 是否压缩
                .withAspectRatio(16,10)// int 裁剪比例
                .forResult(PictureConfig.CHOOSE_REQUEST);
        // Caused by: java.lang.IllegalArgumentException: Failed to find configured root that contains /storage/emulated/0/PictureSelector/CameraImage/PictureSelector_20190417_152946.JPEG
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
                        if (tempType == TYPE_FRONT) {
                            mIvFront.setImageURI(Uri.parse(path));
                        } else if (tempType == TYPE_CONTRARY) {
                            mIvContrary.setImageURI(Uri.parse(path));
                        }
                    }
                    break;
            }
        }

    }
}
