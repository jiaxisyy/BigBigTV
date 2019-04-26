package com.share_will.mobile.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.contrarywind.adapter.WheelAdapter;
import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;
import com.share_will.mobile.Constant;
import com.share_will.mobile.R;
import com.share_will.mobile.model.entity.CityEntity;
import com.share_will.mobile.model.entity.StationEntity;
import com.share_will.mobile.presenter.RegisterPresenter;
import com.share_will.mobile.ui.adapter.StationForPhoneAdapter;
import com.share_will.mobile.ui.views.RegisterView;
import com.share_will.mobile.ui.widget.RecyclerViewItemDecoration;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseFragmentActivity;
import com.ubock.library.ui.dialog.ToastExt;
import com.ubock.library.utils.AppUtils;
import com.ubock.library.utils.LogUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SelectStationActivity extends BaseFragmentActivity<RegisterPresenter> implements View.OnClickListener, RegisterView {

    /**
     * 扫码->电话
     */
    private static final int REQUEST_CODE_SCAN_PHONE = 10010;
    private EditText mEtPhone;

    private TextView mTvSearch;
    private TextView mTvScan;
    private TextView mTvSelected;

    private RecyclerView mRvStation;

    private OptionsPickerView mStationPickerView;
    private CityEntity mCityEntity;
    private StationEntity mStationEntity;
    private LinearLayout mLlPicker;
    private Intent mStationIntent;
    private StationForPhoneAdapter stationForPhoneAdapter;
    private List<StationEntity> list;

    private int mCityIndex;
    private int tempPos = 0;

    private int itemSelectedPos = 0;


    private boolean isclicked = false;
    private WheelView mWvCity;
    private WheelView mWvName;

    private int register_type = 1;//默认为1 个人用户
    private List<CityEntity> cityEntities;
    private TextView mCancel;
    private TextView mSure;
    private List<StationEntity> stationEntities;
    private List<String> mOptionsItems;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_station;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("选择归属");
        register_type = getIntent().getIntExtra("register_type", 1);

        mEtPhone = findViewById(R.id.et_select_station_phone);
        mTvSearch = findViewById(R.id.tv_select_station_search);
        mTvScan = findViewById(R.id.tv_select_station_scan);
        mLlPicker = findViewById(R.id.ll_select_station_picker);
        mTvSelected = findViewById(R.id.tv_select_station_selected);
        mRvStation = findViewById(R.id.rv_select_station);
        mCancel = findViewById(R.id.tv_select_station_cancel);
        mSure = findViewById(R.id.tv_select_station_sure);
        mCancel.setOnClickListener(this);
        mSure.setOnClickListener(this);
        mTvSearch.setOnClickListener(this);
        mTvScan.setOnClickListener(this);
        mTvSelected.setOnClickListener(this);

        //下划线
        mTvSelected.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mStationIntent = new Intent();
        mWvCity = findViewById(R.id.wv_select_station_city);
        mWvName = findViewById(R.id.wv_select_station_name);
        mWvCity.setCyclic(false);
        mWvName.setCyclic(false);
        //站点选择器
       /* mStationPickerView = new OptionsPickerBuilder(this, (options1, option2, options3, v) -> {

            StationEntity station = getPresenter().getModel().getStation(option2);
            if (station != null) {
                LogUtils.d("===getStationName==" + mStationEntity.getStationName());
                mTvSelected.setText("当前:" + mStationEntity.getStationName());
                if (list != null) {
                    list.clear();
                    mRvStation.removeAllViews();
                }
            }
        })
                .setOutSideCancelable(false)
                .setOptionsSelectChangeListener((options1, options2, options3) -> {

                    if (mCityIndex != options1) {
                        mCityIndex = options1;
                        mCityEntity = getPresenter().getModel().getCity(options1);
                        String cityCode = mCityEntity.getAreaCode();
                        LogUtils.d("==cityCode===" + cityCode);
                        getPresenter().getStationList(cityCode, register_type);
                    }
                }).build();*/
        getPresenter().getCityList();
        initWheelView();
    }

    private void initWheelView() {
        mWvCity.setLineSpacingMultiplier(3.0F);
        mWvName.setLineSpacingMultiplier(3.0F);
        mWvCity.setDividerColor(Color.parseColor("#FF3F3F"));
        mWvCity.setTextColorOut(Color.parseColor("#999999"));
        mWvCity.setTextColorCenter(Color.parseColor("#FF3F3F"));
        mWvName.setDividerColor(Color.parseColor("#FF3F3F"));
        mWvName.setTextColorOut(Color.parseColor("#999999"));
        mWvName.setTextColorCenter(Color.parseColor("#FF3F3F"));
        mWvCity.setDividerType(WheelView.DividerType.WRAP);
        mWvName.setDividerType(WheelView.DividerType.WRAP);
        //初始化,不添加此行不显示条目,不可删
        mOptionsItems = new ArrayList<>();
        mWvCity.setAdapter(new ArrayWheelAdapter(mOptionsItems));
        mWvName.setAdapter(new ArrayWheelAdapter(mOptionsItems));
        mWvCity.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                mWvName.setCurrentItem(0);
                itemSelectedPos = index;
                if (cityEntities != null && cityEntities.size() > 0) {
                    String areaCode = cityEntities.get(index).getAreaCode();
                    getPresenter().getStationList(areaCode, register_type);
                }
            }
        });
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tv_select_station_search:
                search();
                break;
            case R.id.tv_select_station_scan:
                scan();
                break;
            case R.id.tv_select_station_selected:
                if (AppUtils.getAppMetaData(this, Constant.CHANNEL) != null) {
                    showStationDialog();
                }
                break;
            case R.id.tv_select_station_cancel:
                finish();
                break;
            case R.id.tv_select_station_sure:

                if (stationEntities != null && stationEntities.size() > 0) {
                    mStationEntity = stationEntities.get(mWvName.getCurrentItem());
                    mStationIntent.putExtra("station_entity", mStationEntity);
                    setResult(RESULT_OK, mStationIntent);
                    finish();
                } else {
                    ToastExt.showExt("无效选择");
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SCAN_PHONE && resultCode == RESULT_OK) {
            String result = data.getStringExtra("scan_result");
            LogUtils.d(result + "=====");
            if (result.length() == 11) {
                mEtPhone.setText(result);
            }
        }
    }

    @Override
    public void showMessage(String message) {
        super.showMessage(message);
    }

    @Override
    public void onRegister(boolean success, String message) {

    }

    @Override
    public void onSendVerifyCode(boolean success, String message) {

    }

    @Override
    public void onLoadStationList(BaseEntity<List<StationEntity>> ret) {
        if (ret != null && ret.getCode() == 0 && ret.getData().size() > 0) {
//            mStationPickerView.setNPicker(getPresenter().getModel().getCity(), ret.getData(), null);
//            mStationPickerView.setSelectOptions(mCityIndex);
//            mTvSelected.setText("当前:" + ret.getData().get(0).getStationName());
//            mStationEntity = ret.getData().get(0);
            List<String> names = new ArrayList<>();
            stationEntities = ret.getData();
            for (StationEntity stationEntity : stationEntities) {
                if (!TextUtils.isEmpty(stationEntity.getStationName())) {
                    names.add(stationEntity.getStationName());
                }
            }
            mWvName.setAdapter(new ArrayWheelAdapter(names));

        } else {
            //站点404处理
//            List<StationEntity> nullList = new ArrayList<>();
//            mStationPickerView.setNPicker(getPresenter().getModel().getCity(), nullList, null);
//            mStationPickerView.setSelectOptions(mCityIndex);
            mWvName.setAdapter(new ArrayWheelAdapter(mOptionsItems));
            stationEntities = null;
        }
//        mStationPickerView.show();
    }

    @Override
    public void onLoadCityList(BaseEntity<List<CityEntity>> ret) {
        if (ret != null && ret.getCode() == 0 && ret.getData().size() > 0) {
            cityEntities = ret.getData();
            List<String> citys = new ArrayList<>();
            for (CityEntity cityEntity : cityEntities) {
                if (!TextUtils.isEmpty(cityEntity.getStationCity())) {
                    citys.add(cityEntity.getStationCity());
                }
            }
            mWvCity.setAdapter(new ArrayWheelAdapter(citys));
            String cityCode = ret.getData().get(0).getAreaCode();
            getPresenter().getStationList(cityCode, register_type);
        }
    }


    @Override
    public void onLoadStationForPhone(BaseEntity<List<StationEntity>> ret) {
        list = ret.getData();
        stationForPhoneAdapter.setNewData(list);
    }

    @Override
    public void finish() {
//        mStationIntent.putExtra("station_name", mTvSelected.getText().toString().substring(3));
//        mStationIntent.putExtra("station_entity", mStationEntity);
////        LogUtils.d(mStationEntity.toString());
//        if (mStationIntent != null) {
//            setResult(RESULT_OK, mStationIntent);
//        }
        super.finish();
    }

    private void search() {
        String phone = mEtPhone.getText().toString();
        list = new ArrayList<>();
        if (phone.trim().length() == 11 && phone.trim().startsWith("1")) {
            getPresenter().getStationForPhone(phone);
        } else {
            showMessage("号码有误");

        }
    }

    private void scan() {
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SCAN_PHONE);
    }

    public void showStationDialog() {
        if (getPresenter().getModel().getCity() == null ||
                getPresenter().getModel().getCity().isEmpty()) {
            getPresenter().getCityList();
        } else {
            mStationPickerView.show();
        }
    }

    private void initStationList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRvStation.setLayoutManager(layoutManager);
        mRvStation.addItemDecoration(new RecyclerViewItemDecoration(2, 0xFFd8d8d8));
        stationForPhoneAdapter = new StationForPhoneAdapter(R.layout.item_select_station, null);
        mRvStation.setAdapter(stationForPhoneAdapter);
        stationForPhoneAdapter.setOnItemClickListener((adapter, view, position) -> {
            stationForPhoneAdapter.setData(tempPos, new StationEntity(list.get(tempPos).getStationName(), isclicked));
            LogUtils.d("SYY", "tempPos=" + tempPos + ",position=" + position);
            stationForPhoneAdapter.setData(position, new StationEntity(list.get(position).getStationName(), !isclicked));
            mStationIntent.putExtra("station_entity", list.get(position));
            mTvSelected.setText("当前:" + list.get(position).getStationName());
            tempPos = position;
        });
    }

}