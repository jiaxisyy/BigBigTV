package com.ubock.library.common.gson;

import android.util.Log;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import com.ubock.library.utils.NumberUtils;

/**
 * Created by ChenGD on 2017-9-4.
 */

public class DoubleTypeAdapter extends TypeAdapter<Double> {
    @Override
    public void write(JsonWriter out, Double value) {
        try {
            if (value == null){
                value = 0D;
            }
            out.value(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Double read(JsonReader in) {
        try {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                Log.e("TypeAdapter", "null is not a number");
                return 0D;
            }
            if (in.peek() == JsonToken.BOOLEAN) {
                boolean b = in.nextBoolean();
                Log.e("TypeAdapter", b + " is not a number");
                return 0D;
            }
            if (in.peek() == JsonToken.STRING) {
                String str = in.nextString();
                if (NumberUtils.isFloat(str)){
                    return Double.parseDouble(str);
                } else {
                    Log.e("TypeAdapter", str + " is not a number");
                    return 0D;
                }
            } else {
                Double value = in.nextDouble();
                return value == null ? 0D : value;
            }
        } catch (Exception e) {
            Log.e("TypeAdapter", e.getMessage(), e);
            try {
                in.skipValue();
            } catch (Exception e1) {
                Log.e("TypeAdapter", e1.getMessage(), e1);
            }
        }
        return 0D;
    }
}
