package com.share_will.mobile.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.share_will.mobile.App;
import com.share_will.mobile.R;
import com.share_will.mobile.model.entity.AlarmEntity;
import com.share_will.mobile.model.entity.BatteryEntity;
import com.share_will.mobile.model.entity.ChargeBatteryEntity;
import com.share_will.mobile.model.entity.UserInfo;
import com.share_will.mobile.presenter.HomeFragmentPresenter;
import com.share_will.mobile.presenter.UserCenterPresenter;
import com.share_will.mobile.ui.activity.AlarmListActivity;
import com.share_will.mobile.ui.activity.CaptureActivity;
import com.share_will.mobile.ui.activity.ChargeStakeActivity;
import com.share_will.mobile.ui.activity.HomeActivity;
import com.share_will.mobile.ui.activity.HomeServiceActivity;
import com.share_will.mobile.ui.activity.MyBatteryActivity;
import com.share_will.mobile.ui.views.IHomeFragmentView;
import com.share_will.mobile.ui.views.UserCenterView;
import com.ubock.library.annotation.PresenterInjector;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseFragment;
import com.ubock.library.ui.dialog.ToastExt;
import com.ubock.library.utils.DateUtils;
import com.ubock.library.utils.LogUtils;

import java.text.DecimalFormat;
import java.util.List;

public class HomeFragment extends BaseFragment<HomeFragmentPresenter> implements IHomeFragmentView, View.OnClickListener
        , UserCenterView {
    private TextView mAlarmTitle;
    private TextView mAlarmPositionName;
    private TextView mAlarmRemark;
    private TextView mAlarmTime;
    private TextView mAlarmLevel;
    private TextView mTopCharge;
    private TextView mTopChargeStake;

    private RelativeLayout mRlAlarm;
    private RelativeLayout mRlBattery;
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
    private RelativeLayout mCardMoney;
    private TextView mNoAlarm;
    private LinearLayout mNoBatteryCon;
    private Button mRentalBattery;
    private Button mGetBattery;
    private Button mBindBattery;
    private View mLayoutBottom;
    private SwipeRefreshLayout mRefreshLayout;
    private boolean hasChargeBatteryInfo = false;
    /**
     * 网络请求
     */
    private int flag_http_success = -1;


    @PresenterInjector
    UserCenterPresenter mUserCenterPresenter;

    private UserInfo mUserInfo;
    //扫码绑定电池
    public final static int REQUEST_CODE_BIND_BATTERY = 100;
    //扫码领取电池
    public final static int REQUEST_CODE_GET_BATTERY = 101;
    private ImageView mArrowRight;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        showBackMenu(false);
        setTitle("MOSS");
//        View vie = view.findViewById(R.id.topbar);
//        vie.setBackgroundColor(Color.parseColor("#FFFFFF"));
        mRefreshLayout = view.findViewById(R.id.refresh_home);
        mRefreshLayout.setRefreshing(true);
        //执行刷新
        mRefreshLayout.setOnRefreshListener(this::initData);
        mAlarmTitle = view.findViewById(R.id.tv_home_alarm_title);
        mAlarmPositionName = view.findViewById(R.id.tv_home_alarm_positionName);
        mAlarmRemark = view.findViewById(R.id.tv_home_alarm_remark);
        mAlarmTime = view.findViewById(R.id.tv_home_alarm_time);
        mAlarmLevel = view.findViewById(R.id.tv_home_alarm_level);
        mTopCharge = view.findViewById(R.id.tv_home_top_charge);
        mTopChargeStake = view.findViewById(R.id.tv_home_top_charge_stake);

        mRlAlarm = view.findViewById(R.id.rl_home_alarmInfo);
        mRlBattery = view.findViewById(R.id.rl_home_batteryInfo);
        mNoAlarm = view.findViewById(R.id.tv_home_no_alarm);
        mNoBatteryCon = view.findViewById(R.id.ll_no_battery_con);
        mRentalBattery = view.findViewById(R.id.rental_battery);
        mGetBattery = view.findViewById(R.id.get_battery);
        mBindBattery = view.findViewById(R.id.bind_battery);
        mLayoutBottom = view.findViewById(R.id.include_layout_home_bottom);
        mStartTime = view.findViewById(R.id.tv_home_charge_start_time);
        mEnoughTime = view.findViewById(R.id.tv_home_charge_enough_time);
        mDurationTime = view.findViewById(R.id.tv_home_charge_duration_time);
        mNowSop = view.findViewById(R.id.tv_home_charge_now_sop);
        mEnergy = view.findViewById(R.id.tv_home_charge_energy);
        mAddress = view.findViewById(R.id.tv_home_charge_address);
        mDoor = view.findViewById(R.id.tv_home_charge_door);
        mMoneyCharge = view.findViewById(R.id.tv_home_money_charge);
        mMoneManage = view.findViewById(R.id.tv_home_money_manage);
        mMoneyAll = view.findViewById(R.id.tv_home_money_all);
        mCardMoney = view.findViewById(R.id.rl_card_money);
        mArrowRight = view.findViewById(R.id.iv_main_arrow_right);

        mTopCharge.setOnClickListener(this);
        mTopChargeStake.setOnClickListener(this);
        mRlAlarm.setOnClickListener(this);
        mRlBattery.setOnClickListener(this);
        mRentalBattery.setOnClickListener(this);
        mGetBattery.setOnClickListener(this);
        mBindBattery.setOnClickListener(this);
        initData();
    }


    private void initData() {

        mUserCenterPresenter.getBalance(App.getInstance().getUserId(), false);
        getPresenter().getAlarmList(App.getInstance().getUserId(), App.getInstance().getToken());
        getPresenter().getChargeBatteryInfo(App.getInstance().getUserId(), App.getInstance().getToken());
    }


    @Override
    public void onLoadBalance(BaseEntity<UserInfo> data) {
        if (data != null) {
            mUserInfo = data.getData();
        }
        getPresenter().getChargeBatteryInfo(App.getInstance().getUserId(), App.getInstance().getToken());
    }


//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//    }

    @Override
    public void onLoadAlarmResult(BaseEntity<AlarmEntity> data) {
        int validPos = -1;
        if (data != null) {
            List<AlarmEntity.SmokeBean> smokeBeanList = data.getData().getSmoke();
            int size = smokeBeanList.size();
            for (int i = 0; i < size; i++) {
                if (smokeBeanList.get(i).getAlarmcode() != 0) {
                    validPos = i;
                }
            }
            if (validPos != -1) {
                AlarmEntity.SmokeBean alarmEntity = data.getData().getSmoke().get(validPos);
                mAlarmTitle.setVisibility(View.VISIBLE);
                mAlarmPositionName.setVisibility(View.VISIBLE);
                mAlarmRemark.setVisibility(View.VISIBLE);
                mAlarmTime.setVisibility(View.VISIBLE);
                mAlarmLevel.setVisibility(View.VISIBLE);
                mNoAlarm.setVisibility(View.INVISIBLE);
                if (!TextUtils.isEmpty(alarmEntity.getTitle())) {
                    mAlarmTitle.setText("标题: " + alarmEntity.getTitle());
                }
                if (!TextUtils.isEmpty(alarmEntity.getPositionName())) {
                    mAlarmPositionName.setText(alarmEntity.getPositionName());
                }
                if (!TextUtils.isEmpty(alarmEntity.getRemark())) {
                    mAlarmRemark.setText(alarmEntity.getRemark());
                }
                if (alarmEntity.getAlarmtime() != 0) {
                    mAlarmTime.setText("告警时间   " + DateUtils.timeStampToString(alarmEntity.getAlarmtime(), DateUtils.YYYYMMDD_HHMMSS));
                }
                mAlarmLevel.setText("告警级别   " + alarmEntity.getAlarmlevel() + "级");
            } else {
                mNoAlarm.setVisibility(View.VISIBLE);
                mAlarmTitle.setVisibility(View.INVISIBLE);
                mAlarmPositionName.setVisibility(View.INVISIBLE);
                mAlarmRemark.setVisibility(View.INVISIBLE);
                mAlarmTime.setVisibility(View.INVISIBLE);
                mAlarmLevel.setVisibility(View.INVISIBLE);
            }
        }
        flag_http_success = 0;
    }


    @Override
    public void onLoadChargeBatteryInfoResult(BaseEntity<ChargeBatteryEntity> data) {
        if (data != null) {
            mLayoutBottom.setVisibility(View.VISIBLE);
            ChargeBatteryEntity entity = data.getData();
            mStartTime.setText("开始时间:   " + DateUtils.timeStampToString(entity.getStartTime(), DateUtils.YYYYMMDD_HHMMSS));
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
            mDoor.setText("仓门号:   " + entity.getDoor());
            mMoneyCharge.setText(intChange(entity.getMoney() / 100f) + "元");
            mMoneManage.setText(intChange(entity.getManageMoney() / 100f) + "元");
            int all = entity.getMoney() + entity.getManageMoney();
            mMoneyAll.setText("合计:" + intChange(all / 100f) + "元");
            if (flag_http_success == 0) {
                mRefreshLayout.setRefreshing(false);
            }
            mArrowRight.setVisibility(View.VISIBLE);
            hasChargeBatteryInfo = true;
        } else {
            //没有充电电池,展示已有电池信息
            hasChargeBatteryInfo = false;
            getPresenter().getBatteryInfo(App.getInstance().getUserId(), App.getInstance().getToken());
        }

    }

    public String intChange(float num) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(num);
    }

    @Override
    public void onLoadBatteryInfoResult(BaseEntity<BatteryEntity> data) {
        BatteryEntity entity = data.getData();
        if (data != null && !TextUtils.isEmpty(entity.getSn())) {
            if (!TextUtils.isEmpty(entity.getSn())) {
                mStartTime.setText("电池SN:   " + entity.getSn());
            }
            if (!TextUtils.isEmpty(entity.getSop())) {
                mEnoughTime.setText("当前电量:   " + entity.getSop() + "%");
            }

            mDurationTime.setVisibility(View.GONE);
            mNowSop.setVisibility(View.GONE);
            mEnergy.setVisibility(View.GONE);
            mAddress.setVisibility(View.GONE);
            mDoor.setVisibility(View.GONE);
            mCardMoney.setVisibility(View.GONE);
            mLayoutBottom.setVisibility(View.VISIBLE);
            showNoBatteryView(false, mUserInfo != null && mUserInfo.getDeposit() > 0);

        } else {
            mLayoutBottom.setVisibility(View.GONE);
            showNoBatteryView(true, mUserInfo != null && mUserInfo.getDeposit() > 0);
            mArrowRight.setVisibility(View.INVISIBLE);
        }
        if (flag_http_success == 0) {
            mRefreshLayout.setRefreshing(false);
            flag_http_success = -1;
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_home_top_charge:
                startActivity(new Intent(getActivity(), HomeServiceActivity.class));
                break;
            case R.id.tv_home_top_charge_stake:
                startActivity(new Intent(getActivity(), ChargeStakeActivity.class));
                break;

            case R.id.rl_home_alarmInfo:
                startActivity(new Intent(getActivity(), AlarmListActivity.class));
                break;
            case R.id.rl_home_batteryInfo:
                if (mArrowRight.getVisibility() == View.VISIBLE) {
                    if (hasChargeBatteryInfo) {
                        startActivity(new Intent(getActivity(), HomeServiceActivity.class));
                    } else {
                        Intent intent = new Intent(getActivity(), MyBatteryActivity.class);
                        intent.putExtra("isShowBindView", false);
                        startActivity(intent);
                    }

                }
                break;
            case R.id.get_battery:
                Intent inte = new Intent(this.getContext(), CaptureActivity.class);
                startActivityForResult(inte, REQUEST_CODE_GET_BATTERY);
                break;
            case R.id.rental_battery:
                Intent intent = new Intent(this.getContext(), HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("page", HomeActivity.PAGE_SHOP);
                startActivity(intent);
                break;
            case R.id.bind_battery:
                Intent intent1 = new Intent(this.getContext(), CaptureActivity.class);
                intent1.putExtra(CaptureActivity.KEY_SHOW_MANUAL_INPUT, true);
                startActivityForResult(intent1, REQUEST_CODE_BIND_BATTERY);
                break;
            default:
                break;

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
    public void onBindBatteryResult(BaseEntity<Object> data) {
        if (data != null) {
            if (data.getCode() == 0) {
                ToastExt.showExt("绑定电池成功");
            } else {
                ToastExt.showExt(data.getMessage());
            }
        } else {
            ToastExt.showExt("绑定电池失败");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            String resultData = data.getStringExtra(CaptureActivity.KEY_SCAN_RESULT);
            if (requestCode == REQUEST_CODE_BIND_BATTERY) {
                boolean manualInput = data.getBooleanExtra(CaptureActivity.KEY_SHOW_MANUAL_INPUT, false);
                if (manualInput) {
                    Intent intent = new Intent(getActivity(), MyBatteryActivity.class);
                    intent.putExtra("isShowBindView", true);
                    startActivity(intent);
                } else {
                    if (TextUtils.isEmpty(resultData) || resultData.length() != 16) {
                        ToastExt.showExt("无效二维码/二维码");
                    } else {
                        getPresenter().bindBattery(App.getInstance().getUserId(), resultData);
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
                                getPresenter().scanCodeGetBattery(sn, App.getInstance().getUserId());
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
            } else {
            }
        }
    }
}
