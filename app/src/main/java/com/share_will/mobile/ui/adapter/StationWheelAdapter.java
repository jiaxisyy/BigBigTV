package com.share_will.mobile.ui.adapter;

import com.contrarywind.adapter.WheelAdapter;
import com.share_will.mobile.model.entity.StationEntity;

import java.util.List;

public class StationWheelAdapter implements WheelAdapter {
    private List<StationEntity> stationEntityList;

    public StationWheelAdapter(List<StationEntity> stationEntityList) {
        this.stationEntityList = stationEntityList;
    }

    @Override
    public int getItemsCount() {
        return stationEntityList.size();
    }

    @Override
    public Object getItem(int index) {
        if (index >= 0 && index < stationEntityList.size()) {
            return stationEntityList.get(index).getStationName();
        }
        return "";
    }

    @Override
    public int indexOf(Object o) {
        return stationEntityList.indexOf(o);
    }
}
