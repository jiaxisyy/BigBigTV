package com.share_will.mobile.ui.dialog;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.share_will.mobile.MessageEvent;
import com.share_will.mobile.R;
import com.ubock.library.base.BaseFragmentActivity;

import org.greenrobot.eventbus.EventBus;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class DialogActivity extends BaseFragmentActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                    DialogActivityPermissionsDispatcher.onAllowPermissionWithPermissionCheck(DialogActivity.this);
                } else {
                    EventBus.getDefault().post(new MessageEvent.DialogActivityEvent(true));
                    finish();
                }
            }
        });

        Intent intent = getIntent();
        mShowOK = intent.getBooleanExtra(PARAM_SHOW_OK, true);
        String title = intent.getStringExtra(PARAM_TITLE);
        String message = intent.getStringExtra(PARAM_MESSAGE);
        mNeedCheckPermission = intent.getBooleanExtra(PARAM_NEED_PERMISSION, false);
        String ok = intent.getStringExtra(PARAM_OK);
        String cancel = intent.getStringExtra(PARAM_CANCEL);
        if (title != null){
            mTitle.setText(title);
        }
        if (message != null){
            mMessage.setText(message);
        }
        if (ok != null){
            mOkBtn.setText(ok);
        }
        if (cancel != null){
            mCancelBtn.setText(cancel);
        }

        if (!mShowOK){
            mOkBtn.setVisibility(View.GONE);
            mLine.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        EventBus.getDefault().post(new MessageEvent.DialogActivityEvent(false));
        super.onBackPressed();
    }

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.REQUEST_INSTALL_PACKAGES})
    void onAllowPermission() {
        EventBus.getDefault().post(new MessageEvent.DialogActivityEvent(true));
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        DialogActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnPermissionDenied({Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.REQUEST_INSTALL_PACKAGES})
    void onPermissionDenied() {
        EventBus.getDefault().post(new MessageEvent.DialogActivityEvent(false));
        Toast.makeText(this, "权限被拒绝", Toast.LENGTH_LONG).show();
        finish();
    }

    @OnNeverAskAgain({Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.REQUEST_INSTALL_PACKAGES})
    void onNeverAskAgain() {
        EventBus.getDefault().post(new MessageEvent.DialogActivityEvent(false));
        Toast.makeText(this, "权限被拒绝", Toast.LENGTH_LONG).show();
        finish();
    }
}
