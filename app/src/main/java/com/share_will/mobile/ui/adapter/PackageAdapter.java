package com.share_will.mobile.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseViewHolder;
import com.share_will.mobile.R;
import com.share_will.mobile.model.entity.PackageEntity;
import com.ubock.library.ui.adapter.LoadMoreAdapter;

import java.text.NumberFormat;

public class PackageAdapter extends LoadMoreAdapter<PackageEntity, PackageAdapter.Holder> {
    private Context mContext;

    public PackageAdapter(Context context) {
        super(context, R.layout.package_item);
        mContext = context;
    }

    @Override
    protected void convert(Holder helper, PackageEntity item) {
        ImageView poster = helper.getView(R.id.iv_poster);
        RequestOptions options = new RequestOptions().placeholder(R.drawable.no).error(R.drawable.no);
        Glide.with(mContext).load(item.getPoster()).apply(options).into(poster);
        if(!TextUtils.isEmpty(item.getPackageName())){
            helper.setText(R.id.tv_name, item.getPackageName());
        }
        int price = item.getActivityId() != 0 ? item.getActivityPrice() : item.getPackagePrice();
        helper.setText(R.id.tv_price, String.format("￥%s元", NumberFormat.getInstance().format(price / 100f)));
        helper.setText(R.id.tv_desc, item.getDescription());
        helper.oldPrice.setText(String.format("￥%s元", NumberFormat.getInstance().format(item.getPackagePrice() / 100)));
    }

    static class Holder extends BaseViewHolder {
        TextView oldPrice;

        public Holder(View view) {
            super(view);
            oldPrice = view.findViewById(R.id.tv_old_price);
            if (oldPrice != null) {
                oldPrice.setPaintFlags(oldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }

        }

    }
}
