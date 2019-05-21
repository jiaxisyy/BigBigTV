package com.share_will.mobile.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.share_will.mobile.App;
import com.share_will.mobile.R;
import com.share_will.mobile.model.entity.BatteryEntity;
import com.share_will.mobile.model.entity.ChargeBatteryEntity;
import com.share_will.mobile.model.entity.UserInfo;
import com.share_will.mobile.presenter.HomeFragmentPresenter;
import com.share_will.mobile.presenter.UserCenterPresenter;
import com.share_will.mobile.ui.views.IHomeFragmentView;
import com.share_will.mobile.ui.views.UserCenterView;
import com.ubock.library.annotation.PresenterInjector;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseFragmentActivity;
import com.ubock.library.ui.dialog.ToastExt;
import com.ubock.library.utils.DateUtils;
import com.ubock.library.utils.LogUtils;

import java.text.DecimalFormat;

public class MyBatteryActivity extends BaseFragmentActivity implements IHomeFragmentView, View.OnClickListener, UserCenterView {
    private static final int REQUEST_CODE_SCAN_SN = 10010;
    private static final int REQUEST_CODE_BIND_BATTERY_SUCCESS = 10011;
    final public static String KEY_BIND_SUCCESS = "bind_success";
    @PresenterInjector
    HomeFragmentPresenter homeFragmentPresenter;
    @PresenterInjector
    private UserCenterPresenter mUserCenterPresenter;
    //扫码绑定电池
    public final static int REQUEST_CODE_BIND_BATTERY = 100;
    //扫码领取电池
    public final static int REQUEST_CODE_GET_BATTERY = 101;
    private TextView mStartTime;
    private TextView mEnoughTime;
    private TextView mDurationTime;
    private TextView mNowSop;
    private TextView mEnergy;
    private TextView mAddress;
    private TextView mDoor;
    private TextView mMoneyCharge;
    private TextView mMoneManage;
    private TextView mMoneyAll;

    /**
     * 我的电池信息
     */
    private View mMyBatteryView;
    private ImageView mIvBatteryPic;
    private TextView mTvMyBatterySn;
    private TextView mTvMyBatteryModel;
    private TextView mTvMyBatteryUsed;
    private TextView mTvMyBatteryMileage;
    private TextView mTvMyBatterySop;

    private RelativeLayout mCardMoney;
    private LinearLayout mLlCardBatteryInfo;
    private LinearLayout mLlCardBatteryBind;
    private EditText mBindSn;
    private EditText mBindSnAgain;
    private TextView mBindSubmit;
    private View mLayoutBottom;
    private SwipeRefreshLayout mRefreshLayout;
    private LinearLayout mNoBatteryCon;
    private Button mRentalBattery;
    private Button mGetBattery;
    private Button mBindBattery;
    private UserInfo mUserInfo;
    private boolean isShowBindView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_battery;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("我的电池");
        isShowBindView = getIntent().getBooleanExtra("isShowBindView", false);
        mStartTime = findViewById(R.id.tv_home_charge_start_time);
        mEnoughTime = findViewById(R.id.tv_home_charge_enough_time);
        mDurationTime = findViewById(R.id.tv_home_charge_duration_time);
        mNowSop = findViewById(R.id.tv_home_charge_now_sop);
        mEnergy = findViewById(R.id.tv_home_charge_energy);
        mAddress = findViewById(R.id.tv_home_charge_address);
        mDoor = findViewById(R.id.tv_home_charge_door);
        mMoneyCharge = findViewById(R.id.tv_home_money_charge);
        mMoneManage = findViewById(R.id.tv_home_money_manage);
        mMoneyAll = findViewById(R.id.tv_home_money_all);
        mCardMoney = findViewById(R.id.rl_card_money);
        mLlCardBatteryInfo = findViewById(R.id.ll_card_battery_info);
        mLlCardBatteryBind = findViewById(R.id.rl_card_bind);
        mLayoutBottom = findViewById(R.id.include_layout_my_battery_bottom);
        mRefreshLayout = findViewById(R.id.refresh_my_battery);
        isShowBindView();
        mBindSn = findViewById(R.id.et_my_battery_bind_sn);
        mBindSnAgain = findViewById(R.id.et_my_battery_bind_sn_again);
        mBindSubmit = findViewById(R.id.tv_my_battery_bind_submit);
        mNoBatteryCon = findViewById(R.id.ll_no_battery_con);
        mRentalBattery = findViewById(R.id.rental_battery);
        mGetBattery = findViewById(R.id.get_battery);
        mBindBattery = findViewById(R.id.bind_battery);

        //我的电池
        mIvBatteryPic = findViewById(R.id.iv_my_battery_pic);
        mTvMyBatterySn = findViewById(R.id.tv_my_battery_sn);
        mTvMyBatteryModel = findViewById(R.id.tv_my_battery_model);
        mTvMyBatteryUsed = findViewById(R.id.tv_my_battery_used);
        mTvMyBatteryMileage = findViewById(R.id.tv_my_battery_mileage);
        mTvMyBatterySop = findViewById(R.id.tv_my_battery_sop);
        mMyBatteryView = findViewById(R.id.ll_my_battery);

        mRefreshLayout.setOnRefreshListener(this::initData);
        mBindSubmit.setOnClickListener(this);
        mRentalBattery.setOnClickListener(this);
        mGetBattery.setOnClickListener(this);
        mBindBattery.setOnClickListener(this);

    }

    private void showMyBatteryView(boolean show) {
        if (show) {
            mMyBatteryView.setVisibility(View.VISIBLE);
            mLayoutBottom.setVisibility(View.GONE);
        } else {
            mMyBatteryView.setVisibility(View.GONE);
//            mLayoutBottom.setVisibility(View.VISIBLE);
        }
    }

    private void isShowBindView() {
        if (isShowBindView) {
            mLlCardBatteryBind.setVisibility(View.VISIBLE);
            mLayoutBottom.setVisibility(View.VISIBLE);
            mLlCardBatteryInfo.setVisibility(View.INVISIBLE);
            mCardMoney.setVisibility(View.INVISIBLE);
            mRefreshLayout.setEnabled(false);
        } else {
            mRefreshLayout.setEnabled(true);
            initData();
        }
    }

    private void initData() {
        homeFragmentPresenter.getChargeBatteryInfo(App.getInstance().getUserId(), App.getInstance().getToken());
        mUserCenterPresenter.getBalance(App.getInstance().getUserId(), false);
    }


    @Override
    public void onLoadBatteryInfoResult(BaseEntity<BatteryEntity> data) {
        if (data != null) {
            BatteryEntity batteryEntity = data.getData();
            if (batteryEntity != null) {
                showMyBatteryView(true);
                if (!TextUtils.isEmpty(batteryEntity.getSn())) {
                    mTvMyBatterySn.setText("电池SN:   " + batteryEntity.getSn());
                }
                if (!TextUtils.isEmpty(batteryEntity.getDischarges())) {
                    mTvMyBatteryUsed.setText("电池已使用次数:    " + batteryEntity.getDischarges() + "次");
                }


               /* if (!TextUtils.isEmpty(batteryEntity.getSop())) {
                    Integer sop = Integer.valueOf(batteryEntity.getSop());
                    mTvMyBatteryMileage.setText("电池可骑行里程 (预估) :   " + sop / 20f * 6 + "km");
                    mTvMyBatterySop.setText(batteryEntity.getSop() + "%");
                    switch (sop / 20) {
                        case 0:
                            mIvBatteryPic.setImageResource(R.drawable.icon_mybattery_00);
                            break;
                        case 1:
                            mIvBatteryPic.setImageResource(R.drawable.icon_mybattery_01);
                            break;
                        case 2:
                            mIvBatteryPic.setImageResource(R.drawable.icon_mybattery_02);
                            break;
                        case 3:
                            mIvBatteryPic.setImageResource(R.drawable.icon_mybattery_03);
                            break;
                        case 4:
                            mIvBatteryPic.setImageResource(R.drawable.icon_mybattery_04);
                            break;
                        case 5:
                            mIvBatteryPic.setImageResource(R.drawable.icon_mybattery_05);
                            break;
                    }
                }*/
                if (batteryEntity.isOnline()) {
                    if (!TextUtils.isEmpty(batteryEntity.getSop())) {
                        Integer sop = Integer.valueOf(batteryEntity.getSop());
                        mTvMyBatteryMileage.setText("电池可骑行里程 (预估) :   " + sop / 100f * 30 + "km");
                        mTvMyBatterySop.setText(batteryEntity.getSop() + "%");
                        switch (sop / 20) {
                            case 0:
                                mIvBatteryPic.setImageResource(R.drawable.icon_mybattery_00);
                                break;
                            case 1:
                                mIvBatteryPic.setImageResource(R.drawable.icon_mybattery_01);
                                break;
                            case 2:
                                mIvBatteryPic.setImageResource(R.drawable.icon_mybattery_02);
                                break;
                            case 3:
                                mIvBatteryPic.setImageResource(R.drawable.icon_mybattery_03);
                                break;
                            case 4:
                                mIvBatteryPic.setImageResource(R.drawable.icon_mybattery_04);
                                break;
                            case 5:
                                mIvBatteryPic.setImageResource(R.drawable.icon_mybattery_05);
                                break;
                        }
                    }
                } else {
                    long l = batteryEntity.getTime();
                    long min = l / (1000 * 60);
                    String oldSop = batteryEntity.getSop();
                    float minPP = 100 / 120f;//跑1分钟消耗电量百分比
                    float consume = min * minPP;
                    float v = Float.parseFloat(oldSop) - consume;
                    if (v > 0) {
                        mTvMyBatteryMileage.setText("电池可骑行里程 (预估) :   " + v / 100f * 30 + "km");
                        mTvMyBatterySop.setText(v + "%");
                        if (v <= 20) {
                            mIvBatteryPic.setImageResource(R.drawable.icon_mybattery_01);
                        } else if (v > 20 && v <= 40) {
                            mIvBatteryPic.setImageResource(R.drawable.icon_mybattery_02);
                        } else if (v > 40 && v <= 60) {
                            mIvBatteryPic.setImageResource(R.drawable.icon_mybattery_03);
                        } else if (v > 60 && v < 100) {
                            mIvBatteryPic.setImageResource(R.drawable.icon_mybattery_04);
                        } else if (v == 100) {
                            mIvBatteryPic.setImageResource(R.drawable.icon_mybattery_05);
                        }
                    } else {
                        mIvBatteryPic.setImageResource(R.drawable.icon_mybattery_00);
                        mTvMyBatteryMileage.setText("电池可骑行里程 (预估) :   0km");
                        mTvMyBatterySop.setText("0%");
                    }
                }
                showNoBatteryView(false, mUserInfo != null && mUserInfo.getDeposit() > 0);
            } else {
                showMyBatteryView(false);
                showNoBatteryView(true, mUserInfo != null && mUserInfo.getDeposit() > 0);
                mLlCardBatteryInfo.setVisibility(View.GONE);
                mCardMoney.setVisibility(View.GONE);
                mLlCardBatteryBind.setVisibility(View.VISIBLE);
                mLayoutBottom.setVisibility(View.VISIBLE);
                mLlCardBatteryBind.setVisibility(View.INVISIBLE);
            }
        } else {
            showMyBatteryView(false);
            showNoBatteryView(true, mUserInfo != null && mUserInfo.getDeposit() > 0);
            mLlCardBatteryInfo.setVisibility(View.GONE);
            mCardMoney.setVisibility(View.GONE);
            mLlCardBatteryBind.setVisibility(View.VISIBLE);
            mLayoutBottom.setVisibility(View.VISIBLE);
            mLlCardBatteryBind.setVisibility(View.INVISIBLE);
        }
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_my_battery_bind_submit:
                bindSubmit();
                break;
            case R.id.get_battery:
                Intent inte = new Intent(this, CaptureActivity.class);
                startActivityForResult(inte, REQUEST_CODE_GET_BATTERY);
                break;
            case R.id.rental_battery:
                Intent intent = new Intent(this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("page", HomeActivity.PAGE_SHOP);
                startActivity(intent);
                break;
            case R.id.bind_battery:
                Intent intent1 = new Intent(this, CaptureActivity.class);
                intent1.putExtra(CaptureActivity.KEY_SHOW_MANUAL_INPUT, true);
                intent1.putExtra(CaptureActivity.KEY_SHOW_LIGHT, true);
                startActivityForResult(intent1, REQUEST_CODE_BIND_BATTERY);
                break;
        }
    }


    private void bindSubmit() {
        String sn = mBindSn.getText().toString().trim();
        String snAgain = mBindSnAgain.getText().toString().trim();
        if (!sn.equals(snAgain) || TextUtils.isEmpty(sn)) {
            showMessage("SN码错误");

        } else {
            homeFragmentPresenter.bindBattery(App.getInstance().getUserId(), sn);
        }
    }

    @Override
    public void onLoadBalance(BaseEntity<UserInfo> data) {
        if (data != null) {
            mUserInfo = data.getData();
        }
//        homeFragmentPresenter.getChargeBatteryInfo(App.getInstance().getUserId(), App.getInstance().getToken());
    }

    @Override
    public void onBindBatteryResult(BaseEntity<Object> data) {

        if (data != null) {
            if (data.getCode() == 0) {
                ToastExt.showExt("绑定电池成功");
                Intent intent = new Intent();
                intent.putExtra(KEY_BIND_SUCCESS, "success");
                setResult(RESULT_OK, intent);
                finish();
            } else {
                ToastExt.showExt(data.getMessage());
            }
        } else {
            ToastExt.showExt("绑定电池失败");
        }
    }

    /**
     * 显示无电池信息框
     *
     * @param show       是否显示
     * @param hasDeposit 是否有押金
     */
    private void showNoBatteryView(boolean show, boolean hasDeposit) {
        if (show) {
            mNoBatteryCon.setVisibility(View.VISIBLE);
            if (hasDeposit) {
                mGetBattery.setVisibility(View.VISIBLE);
                mRentalBattery.setVisibility(View.GONE);
            } else {
                mGetBattery.setVisibility(View.GONE);
                mRentalBattery.setVisibility(View.VISIBLE);
            }
        } else {
            mNoBatteryCon.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            String resultData = data.getStringExtra(CaptureActivity.KEY_SCAN_RESULT);
            if (requestCode == REQUEST_CODE_BIND_BATTERY) {
                boolean manualInput = data.getBooleanExtra(CaptureActivity.KEY_SHOW_MANUAL_INPUT, false);
                if (manualInput) {
                    Intent intent = new Intent(this, MyBatteryActivity.class);
                    intent.putExtra("isShowBindView", true);
                    startActivityForResult(intent, REQUEST_CODE_BIND_BATTERY_SUCCESS);
                } else {
                    if (TextUtils.isEmpty(resultData) || resultData.length() != 16) {
                        ToastExt.showExt("无效二维码/二维码");
                    } else {
                        homeFragmentPresenter.bindBattery(App.getInstance().getUserId(), resultData);
                    }
                }
            } else if (requestCode == REQUEST_CODE_GET_BATTERY) {
                if (TextUtils.isEmpty(resultData)) {
                    ToastExt.showExt("无效二维码");
                } else {
                    try {
                        Uri uri = Uri.parse(resultData);
                        if (uri != null) {
                            String sn = uri.getQueryParameter("sn");
                            String time = uri.getQueryParameter("time");
                            if (!TextUtils.isEmpty(sn) && !TextUtils.isEmpty(time)) {
                                homeFragmentPresenter.scanCodeGetBattery(sn, App.getInstance().getUserId());
                            } else {
                                ToastExt.showExt("无效二维码");
                            }
                        } else {
                            ToastExt.showExt("无效二维码");
                        }
                    } catch (Exception e) {
                        ToastExt.showExt("无效二维码");
                        LogUtils.e(e);
                    }
                }
            } else if (requestCode == REQUEST_CODE_BIND_BATTERY_SUCCESS) {
                String success = data.getStringExtra(KEY_BIND_SUCCESS);
                if (success.equals("success")) {
                    LogUtils.d("刷新成功");
                    initData();
                }
            }
        }

    }

    @Override
    public void onScanCodeGetBatteryResult(BaseEntity<Object> data) {
        if (data != null) {
            if (data.getCode() == 0) {
                ToastExt.showExt("扫码验证成功,请及时领取电池");
            } else {
                ToastExt.showExt(data.getMessage());
            }
        } else {
            ToastExt.showExt("领取电池失败,请稍候再试");
        }
    }

    @Override
    public void onLoadChargeBatteryInfoResult(BaseEntity<ChargeBatteryEntity> data) {
        if (data != null) {
            mLayoutBottom.setVisibility(View.VISIBLE);
            showMyBatteryView(false);
            ChargeBatteryEntity entity = data.getData();
            if (entity.getStartTime() > 0) {
                mStartTime.setText("开始时间:   " + DateUtils.timeStampToString(entity.getStartTime(), DateUtils.YYYYMMDD_HHMMSS));
            }
            if (entity.getFullTime() != 0) {
                mEnoughTime.setText("充满时间:   " + DateUtils.timeStampToString(entity.getFullTime(), DateUtils.YYYYMMDD_HHMMSS));
                long l = entity.getFullTime() - entity.getStartTime();
                mDurationTime.setText("充电时长:   " + DateUtils.unixToUTcTimeDuration(l));
            } else {
                mEnoughTime.setVisibility(View.GONE);
                long l = entity.getNowTime() - entity.getStartTime();
                mDurationTime.setText("充电时长:   " + DateUtils.unixToUTcTimeDuration(l));
            }
            mNowSop.setText("当前电量:   " + entity.getSop() + "%");
            mEnergy.setText("已充能量点:   " + entity.getEnergy());
            if (!TextUtils.isEmpty(entity.getCabinetAddress())) {
                mAddress.setText("电池位置:   " + entity.getCabinetAddress());
            }
            mDoor.setText("仓门号:   " + entity.getDoor() + "号");
            mMoneyCharge.setText(intChange(entity.getMoney() / 100f) + "元");
            mMoneManage.setText(intChange(entity.getManageMoney() / 100f) + "元");
            int all = entity.getMoney() + entity.getManageMoney();
            mMoneyAll.setText("合计:" + intChange(all / 100f) + "元");
            mRefreshLayout.setRefreshing(false);
            showNoBatteryView(false, mUserInfo != null && mUserInfo.getDeposit() > 0);
            mLlCardBatteryInfo.setVisibility(View.VISIBLE);
            mCardMoney.setVisibility(View.VISIBLE);
            mLlCardBatteryBind.setVisibility(View.GONE);

        } else {
            //没有充电电池,展示已有电池信息

            homeFragmentPresenter.getBatteryInfo(App.getInstance().getUserId(), App.getInstance().getToken());
        }
    }

    public String intChange(float num) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(num);
    }

}
