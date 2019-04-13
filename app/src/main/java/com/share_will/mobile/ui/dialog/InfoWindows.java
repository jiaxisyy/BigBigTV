package com.share_will.mobile.ui.dialog;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;
import com.share_will.mobile.R;

/**
 * Created by ChenGD on 2018/3/28.
 *
 * @author chenguandu
 */

public class InfoWindows implements AMap.InfoWindowAdapter {

    private Context mContext;
    private TextView mTitle;

    public InfoWindows(Context context){
        mContext = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_info_windows, null);
        mTitle = view.findViewById(R.id.title);
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    public void setTitle(String title){
        mTitle.setText(title);
        if (mTitle.getVisibility() != View.VISIBLE){
            mTitle.setVisibility(View.VISIBLE);
        }
    }

}
