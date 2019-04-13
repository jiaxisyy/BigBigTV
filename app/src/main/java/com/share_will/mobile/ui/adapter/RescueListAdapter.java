package com.share_will.mobile.ui.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseViewHolder;
import com.share_will.mobile.Constant;
import com.share_will.mobile.R;
import com.share_will.mobile.model.entity.RescueEntity;
import com.ubock.library.ui.adapter.LoadMoreAdapter;

import java.text.SimpleDateFormat;

/**
 * Created by ChenGD on 2018/2/27.
 *
 * @author chenguandu
 */

public class RescueListAdapter extends LoadMoreAdapter<RescueEntity, BaseViewHolder> {

    public RescueListAdapter(Context context){
        super(context, R.layout.rescue_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, RescueEntity entity) {
        helper.setText(R.id.tv_status, Constant.RescueStatus.get(entity.getStatus()));
        helper.setText(R.id.tv_reason, entity.getRescueCause());
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        helper.setText(R.id.tv_time, format.format(entity.getCreateTime()));
    }
}
