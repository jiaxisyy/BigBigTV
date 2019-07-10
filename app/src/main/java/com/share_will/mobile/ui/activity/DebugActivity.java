package com.share_will.mobile.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.share_will.mobile.Constant;
import com.share_will.mobile.R;
import com.ubock.library.base.BaseConfig;
import com.ubock.library.base.BaseFragmentActivity;
import com.ubock.library.ui.dialog.ToastExt;
import com.ubock.library.utils.SharedPreferencesUtils;
import com.umeng.socialize.media.Base;

import java.util.HashMap;
import java.util.Map;

public class DebugActivity extends BaseFragmentActivity implements RadioGroup.OnCheckedChangeListener {

    private EditText mServer;
    private EditText mProjectName;
    private RadioGroup mRadioGroup;
    private Map<Integer, RadioButton> mLogLevel = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mServer = findViewById(R.id.et_server_addr);
        mServer.setHint(String.format("默认:%s", BaseConfig.DEFAULT_SERVER_HOST));
        mProjectName = findViewById(R.id.et_project_name);
        mProjectName.setHint(String.format("默认:%s", BaseConfig.PROJECT_NAME));
        mServer.setText(SharedPreferencesUtils.getStringSF(this, Constant.KEY_SERVER_ADDRESS));
        mProjectName.setText(SharedPreferencesUtils.getStringSF(this, Constant.KEY_SERVER_PROJECT));
        mRadioGroup = findViewById(R.id.rg_debug_level);
        mRadioGroup.setOnCheckedChangeListener(this);
        initLogLevel();
        mLogLevel.get(BaseConfig.LOG_LEVEL).setChecked(true);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_debug;
    }

    private void initLogLevel(){
        mLogLevel.put(Log.VERBOSE, findViewById(R.id.rb_level_v));
        mLogLevel.put(Log.DEBUG, findViewById(R.id.rb_level_d));
        mLogLevel.put(Log.INFO, findViewById(R.id.rb_level_i));
        mLogLevel.put(Log.WARN, findViewById(R.id.rb_level_w));
        mLogLevel.put(Log.ERROR, findViewById(R.id.rb_level_e));
        mLogLevel.put(Log.ASSERT, findViewById(R.id.rb_level_a));
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        for (int key: mLogLevel.keySet()) {
            if (checkedId == mLogLevel.get(key).getId()){
                if (BaseConfig.LOG_LEVEL != key) {
                    BaseConfig.LOG_LEVEL = key;
                    SharedPreferencesUtils.setIntergerSF(this, Constant.KEY_LOG_LEVEL, key);
                    ToastExt.showExt("Log级别已更新");
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String server = mServer.getText().toString().trim();
        if (!TextUtils.isEmpty(server)){
            if (!server.startsWith("http://") && !server.startsWith("https://")){
                server = "http://" + server;
            }
            if (!server.endsWith("/")){
                server += "/";
            }
        }
        SharedPreferencesUtils.setStringSF(this, Constant.KEY_SERVER_ADDRESS, server);

        String project = mProjectName.getText().toString().trim();
        SharedPreferencesUtils.setStringSF(this, Constant.KEY_SERVER_PROJECT, project);
    }

    public void exit(View view){
        finish();
    }
}
