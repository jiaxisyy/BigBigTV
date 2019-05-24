package com.share_will.mobile.ui.dialog;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.share_will.mobile.MessageEvent;
import com.share_will.mobile.R;
import com.share_will.mobile.listener.DetachDialogClickListener;
import com.share_will.mobile.utils.OpenPermissionUtils;
import com.ubock.library.base.BaseFragmentActivity;
import com.ubock.library.ui.dialog.ToastExt;
import com.ubock.library.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.PermissionUtils;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class DialogActivity extends BaseFragmentActivity implements DialogInterface.OnClickListener {

    TextView mTitle;
    TextView mMessage;
    Button mCancelBtn;
    Button mOkBtn;
    View mLine;
    private boolean mShowOK;
    private boolean mNeedCheckPermission;
    public final static String PARAM_TITLE = "title";
    public final static String PARAM_MESSAGE = "message";
    public final static String PARAM_NEED_PERMISSION = "permission";
    public final static String PARAM_OK = "ok";
    public final static String PARAM_CANCEL = "cancel";
    public final static String PARAM_SHOW_OK = "show_ok";
    private ImageView mIvCancel;
    private ImageView mIvOk;
    private AlertDialog mAlertDialog;
    private DetachDialogClickListener mDetachClickListener = DetachDialogClickListener.wrap(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNeedCheckPermission = getIntent().getBooleanExtra(PARAM_NEED_PERMISSION, false);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mNeedCheckPermission) {
            try {
                DialogActivityPermissionsDispatcher.onAllowPermissionWithPermissionCheck(DialogActivity.this);
            }catch (Exception e){
                LogUtils.e(e);
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_dialog;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mLine = findViewById(R.id.view_line);
        mTitle = findViewById(R.id.auth_tip_tilte);
        mMessage = findViewById(R.id.auth_tip_txt);
        mCancelBtn = findViewById(R.id.cancel_btn);
        mOkBtn = findViewById(R.id.ok_btn);
        mIvCancel = findViewById(R.id.iv_dialog_update_cancel);
        mIvOk = findViewById(R.id.iv_dialog_update_ok);
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MessageEvent.DialogActivityEvent(false));
                finish();
            }
        });
        mOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNeedCheckPermission){
                    if (!PermissionUtils.hasSelfPermissions(DialogActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE})) {
                        if (mAlertDialog == null){
                            mAlertDialog = new AlertDialog.Builder(DialogActivity.this)
                                    .setTitle("申请权限")
                                    .setIcon(null)
                                    .setMessage("需要读写存储和安装应用权限,去设置权限吗?")
                                    .setCancelable(true)
                                    .setPositiveButton("去设置", mDetachClickListener)
                                    .setNegativeButton(R.string.alert_no_button, mDetachClickListener).create();
                            mAlertDialog.setCanceledOnTouchOutside(false);
                        }
                        mAlertDialog.show();
                        return;
                    }
                }
                EventBus.getDefault().post(new MessageEvent.DialogActivityEvent(true));
                finish();
            }
        });
        if(mIvCancel!=null&&mIvOk!=null){
            mIvCancel.setOnClickListener(v -> {
                EventBus.getDefault().post(new MessageEvent.DialogActivityEvent(false));
                finish();
            });
            mIvOk.setOnClickListener(v -> {
                EventBus.getDefault().post(new MessageEvent.DialogActivityEvent(true));
                finish();
            });
        }

        Intent intent = getIntent();
        mShowOK = intent.getBooleanExtra(PARAM_SHOW_OK, true);
        String title = intent.getStringExtra(PARAM_TITLE);
        String message = intent.getStringExtra(PARAM_MESSAGE);
        String ok = intent.getStringExtra(PARAM_OK);
        String cancel = intent.getStringExtra(PARAM_CANCEL);
        if (title != null) {
            mTitle.setText(title);
        }
        if (message != null) {
            mMessage.setText(message);
        }
        if (ok != null) {
            mOkBtn.setText(ok);
        }
        if (cancel != null) {
            mCancelBtn.setText(cancel);
        }

        if (!mShowOK) {
            mOkBtn.setVisibility(View.GONE);
            if(mIvOk!=null){
                mIvOk.setVisibility(View.GONE);
            }
            mLine.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            try {
                new OpenPermissionUtils(this).jumpPermissionPage();
            } catch (Exception e){
                LogUtils.e(e);
                ToastExt.showExt("打开权限设置界面失败,请手动打开");
            }
        }
        mAlertDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDetachClickListener.release();
        mDetachClickListener = null;
    }

    @Override
    public void onBackPressed() {
        EventBus.getDefault().post(new MessageEvent.DialogActivityEvent(false));
        super.onBackPressed();
    }

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onAllowPermission() {
        LogUtils.d("onAllowPermission");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        DialogActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnPermissionDenied({Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onPermissionDenied() {
        Toast.makeText(this, "权限被拒绝", Toast.LENGTH_LONG).show();
        finish();
    }

    @OnNeverAskAgain({Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onNeverAskAgain() {
        LogUtils.d("onNeverAskAgain");
//        if (!PermissionUtils.hasSelfPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE})) {
//            Toast.makeText(this, "权限被拒绝了", Toast.LENGTH_LONG).show();
//        }
    }
}
