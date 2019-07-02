package com.share_will.mobile.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.share_will.mobile.R;
import com.share_will.mobile.model.entity.ChargeStakeEntity;
import com.share_will.mobile.model.entity.TempEntity;

import java.util.List;

public class ChargeStakeAdapter extends BaseQuickAdapter<ChargeStakeEntity, BaseViewHolder> {
    public ChargeStakeAdapter(@Nullable List<ChargeStakeEntity> data) {
        super(R.layout.item_charge_stake_pic, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ChargeStakeEntity item) {

        helper.setText(R.id.tv_item_charge_stake_num, String.valueOf(helper.getAdapterPosition() + 1));

        if (item.getStatus() == 0) {
            helper.setImageResource(R.id.iv_item_choose_stake_pic, R.drawable.icon_socket_normal);
            helper.setVisible(R.id.iv_item_charge_stake_selected, item.isSelected());
        } else {
            helper.setImageResource(R.id.iv_item_choose_stake_pic, R.drawable.icon_socket_no_normal);
            helper.setVisible(R.id.iv_item_charge_stake_selected, false);
        }
    }
}
