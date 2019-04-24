package com.share_will.mobile.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.share_will.mobile.App;
import com.share_will.mobile.R;
import com.share_will.mobile.listener.PickerViewOptionsListener;
import com.share_will.mobile.listener.PickerViewOptionsListener.OptionsSelectChangeListener;
import com.share_will.mobile.listener.PickerViewOptionsListener.OptionsSelectListener;
import com.share_will.mobile.model.entity.CityEntity;
import com.share_will.mobile.model.entity.StationEntity;
import com.share_will.mobile.presenter.NewRescuePresenter;
import com.share_will.mobile.ui.views.NewRescueView;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseFragmentActivity;
import com.ubock.library.ui.dialog.ToastExt;

import java.util.List;

public class NewRescueActivity extends BaseFragmentActivity<NewRescuePresenter> implements NewRescueView,
        OnOptionsSelectListener, OnOptionsSelectChangeListener {

    private OptionsPickerView mStationPickerView;
    private TextView mStation;
    private int mCityIndex;
    private CityEntity mCityEntity;
    private StationEntity mStationEntity;
    private EditText mReason;
    private OptionsSelectListener mOptionsSelectListener = PickerViewOptionsListener.select(this);
    private OptionsSelectChangeListener mOptionsSelectChangeListener = PickerViewOptionsListener.selectChange(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("申请救援");
        getPresenter().getCityList();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_new_rescue;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mStation = findViewById(R.id.tv_station);
        mReason = findViewById(R.id.et_reason);
        //站点选择器
        mStationPickerView = new OptionsPickerBuilder(this, mOptionsSelectListener)
                .setOptionsSelectChangeListener(mOptionsSelectChangeListener).build();
    }

    @Override
    protected void onDestroy() {
        mOptionsSelectChangeListener.release();
        mOptionsSelectChangeListener = null;
        mOptionsSelectListener.release();
        mOptionsSelectListener = null;
        super.onDestroy();
    }

    @Override
    public void onOptionsSelect(int options1, int options2, int options3, View v) {
        mStationEntity = getPresenter().getModel().getStation(options2);
        if (!TextUtils.isEmpty(mStationEntity.getStationName())) {
            mStation.setText(mStationEntity.getStationName());
        }
    }

    @Override
    public void onOptionsSelectChanged(int options1, int options2, int options3) {
        if (mCityIndex != options1) {
            mCityIndex = options1;
            mCityEntity = getPresenter().getModel().getCity(options1);
            String cityCode = mCityEntity.getAreaCode();
            getPresenter().getStationList(cityCode, 0);
        }
    }

    public void showStationDialog(View view) {
        if (getPresenter().getModel().getCity() == null ||
                getPresenter().getModel().getCity().isEmpty()) {
            getPresenter().getCityList();
        }
        mStationPickerView.show();
    }

    @Override
    public void onLoadStationList(BaseEntity<List<StationEntity>> ret) {
        if (ret != null && ret.getCode() == 0) {
            mStationPickerView.setNPicker(getPresenter().getModel().getCity(), ret.getData(), null);
            mStationPickerView.setSelectOptions(mCityIndex);
        }
    }

    @Override
    public void onLoadCityList(BaseEntity<List<CityEntity>> ret) {
        if (ret != null && ret.getCode() == 0 && ret.getData().size() > 0) {
            String cityCode = ret.getData().get(0).getAreaCode();
            getPresenter().getStationList(cityCode, 0);//默认骑手,用户类型未知
        }
    }

    public void onSubmit(View view) {
        if (mStationEntity == null) {
            ToastExt.showExt("请选择站点");
            return;
        }
        getPresenter().applyRescue(mStationEntity.getStationId(), App.getInstance().getUserId(), mReason.getText().toString());
    }

    @Override
    public void onApplyRescue(BaseEntity<Object> ret) {
        if (ret != null) {
            if (ret.getCode() == 0) {
                ToastExt.showExt("已成功提交申请");
                finish();
            } else {
                ToastExt.showExt(ret.getMessage());
            }
        }
    }
}
