package com.share_will.mobile.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.share_will.mobile.R;
import com.youth.banner.loader.ImageLoader;

public class GlideImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //Glide 加载图片简单用法
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.no)
                .error(R.drawable.no)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .format(DecodeFormat.PREFER_ARGB_8888)
                .override(1080, 450);
        Glide.with(context).asBitmap().load((String) path).apply(options).into(imageView);
//        Glide.with(context).load(path).into(imageView);
//        imageView.setImageURI(Uri.parse((String) path));
    }
}
