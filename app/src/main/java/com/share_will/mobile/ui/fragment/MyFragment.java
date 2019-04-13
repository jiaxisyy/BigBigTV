package com.share_will.mobile.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.share_will.mobile.R;
import com.share_will.mobile.ui.activity.ConsumeActivity;
import com.share_will.mobile.ui.activity.MyDepositActivity;
import com.share_will.mobile.ui.activity.RechargeActivity;
import com.share_will.mobile.ui.activity.RescueActivity;
import com.share_will.mobile.ui.activity.ShopActivity;
import com.ubock.library.base.BaseFragment;

public class MyFragment extends BaseFragment implements View.OnClickListener {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        setTitle("个人中心");
        showBackMenu(false);
        showTopRightMenu(true);
        view.findViewById(R.id.row_my_deposit).setOnClickListener(this);
        view.findViewById(R.id.row_my_money).setOnClickListener(this);
        view.findViewById(R.id.row_my_order).setOnClickListener(this);
        view.findViewById(R.id.row_my_consume).setOnClickListener(this);
        view.findViewById(R.id.row_my_rescue).setOnClickListener(this);
        view.findViewById(R.id.row_my_vehicle).setOnClickListener(this);
        view.findViewById(R.id.row_my_battery).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.row_my_deposit:
                startActivity(new Intent(getActivity(), MyDepositActivity.class));
                break;
            case R.id.row_my_money:
                startActivity(new Intent(getActivity(), RechargeActivity.class));
                break;
            case R.id.row_my_order:

                break;
            case R.id.row_my_consume:
                startActivity(new Intent(getActivity(), ConsumeActivity.class));
                break;
            case R.id.row_my_rescue:
                startActivity(new Intent(getActivity(), RescueActivity.class));
                break;
            case R.id.row_my_vehicle:
                startActivity(new Intent(getActivity(), ShopActivity.class));
                break;
            case R.id.row_my_battery:
                break;
        }

    }
}
